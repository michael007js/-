package com.blankj.utilcode.CrashException;

/**
 * Created by leilei on 2017/4/1.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;


import com.blankj.utilcode.okhttp.OkHttpUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.blankj.utilcode.CrashException.CrashHandler.LogFileLimit;


public class CrashException implements UncaughtExceptionHandler {
    private JSONObject jsonObject;


    //    /**
//     * 是否开启日志输出,在Debug状态下开启,
//     * 在Release状态下关闭以提示程序性能
//     */
//    public static boolean DEBUG = false;

    private UncaughtExceptionHandler mDefaultHandler;//系统默认的UncaughtException处理类

    private static CrashException INSTANCE;//CrashHandler实例

    private Map<String, String> infos = new HashMap<String, String>(); //用来存储设备信息和异常信息
    private Context context;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private CrashException() {

    }

    //获取CrashHandler实例 ,单例模式
    public static CrashException getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashException();
        }
        return INSTANCE;
    }

    //获取系统默认的UncaughtException处理器,设置该CrashHandler为程序的默认处理器
    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 当UncaughtException发生时会转入该函数来处理
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @return true代表处理该异常，不再向上抛异常，
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
//                可以只创建一个文件，以后全部往里面append然后发送，这样就会有重复的信息，个人不推荐
        LogUtils.e("sssss", context.getCacheDir().getAbsolutePath() + "/" + dataFormat.format(System.currentTimeMillis()) + ".log");
        collectDeviceInfo(context);
        jsonObject = null;
        jsonObject = new JSONObject();
        saveCrashInfo2File(ex);
        postReport(jsonObject.toString());
//                AlarmManager mgr = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(context, ActivityLauncher.class);
//                intent.putExtra("crash", true);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent restartIntent = PendingIntent.getActivity(MyApplication.getInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300, restartIntent); //0.3秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        return true;
    }

    // 发送错误报告到服务器
    public void postReport(String message) {
        OkHttpUtils.post()
                .url("")
                .addParams("contents", message)
                .build()
                .execute("发送错误报告", null,new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    //收集设备参数信息
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                if (jsonObject != null) {
                    jsonObject.put("versionName", versionName);
                    jsonObject.put("versionCode", versionCode);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("sss", "an error occured when collect package info", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());

            } catch (Exception e) {
                LogUtils.e("sss", "an error occured when collect crash info", e);
            }
        }
    }

    //保存错误信息到文件中
    private String saveCrashInfo2File(Throwable ex) {

        //将设备信息变成string
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//            sb.append(key + "=" + value + "\n");
            if (jsonObject != null) {
                try {
                    jsonObject.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //递归获取全部的exception信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result + "\n\n"); //将写入的结果
        if (jsonObject != null) {
            try {
                jsonObject.put("content", sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //构造文件名
        String fileName = context.getCacheDir().getAbsolutePath() + "/" + dataFormat.format(System.currentTimeMillis()) + ".log";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        fileName = null;
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(sb.toString().getBytes());
            for (int i = 0; i < sb.toString().getBytes().length; i++) {
                fos.write(sb.toString().getBytes());
            }
            fos.flush();
            fos.close();
            file = null;
        } catch (Exception e) {
        }
//        cleanLogFileToN(CrashFilePath);
        Log.e("sssss", sb.toString());
        return sb.toString();
    }

    Comparator<File> newfileFinder = new Comparator<File>() {

        @Override
        public int compare(File x, File y) {
            // TODO Auto-generated method stub
            if (x.lastModified() > y.lastModified()) return 1;
            if (x.lastModified() < y.lastModified()) return -1;
            else return 0;
        }

    };

    //排序
    private int cleanLogFileToN(String dirname) {
        File dir = new File(dirname);
        if (dir.isDirectory()) {
            File[] logFiles = dir.listFiles();
            if (logFiles.length > LogFileLimit) {
                Arrays.sort(logFiles, newfileFinder);  //从小到大排
                //删掉N个以前的
                for (int i = 0; i < logFiles.length - LogFileLimit; i++) {
                    logFiles[i].delete();
                }
            }
        }

        return 1;
    }

}