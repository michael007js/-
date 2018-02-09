package com.blankj.utilcode.ScreenShot;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;

import com.blankj.utilcode.R;
import com.blankj.utilcode.dao.CustomExceptionCallBack;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ServiceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * 截录屏管理
 * <p>
 * 说明:
 * 如果需要录屏,
 * 则构造之后需判断系统版本是否符合judgeVersion()
 * 申请权限checkPermission(),
 * 随后调用create方法(需要 RecordService支持   <service android:name="com.blankj.utilcode.ScreenShot.RecordService"/>)
 * <p>
 * <p>
 * 如果仅仅只是截屏,
 * 则构造之后直接调用screenShot()即可
 * <p>
 * <p>
 * Created by leilei on 2017/8/3.
 */

public class ScreenShotManager {
    /**
     * 录屏管理器
     */
    private MediaProjectionManager projectionManager;
    /**
     * 媒体捕捉器
     */
    private MediaProjection mediaProjection;
    /**
     * 权限列表
     */
    List<PermissionItem> list = new ArrayList<>();
    /**
     * 是否已连接录屏服务控制器FLAG
     */
    boolean isConnted = false;
    /**
     * 弱引用上下文
     */
    WeakReference<Activity> context;
    /**
     * 录屏服务
     */
    RecordService recordService;
    /**
     * 意图
     */
    String state = "";

    /**
     * ScreenShotManager事件回调
     */
    ScreenShotManagerCallBack screenShotManagerCallBack;

    public interface ScreenShotManagerCallBack {
        /**
         * 上下文为空
         *
         * @param what
         */
        void onContextIsNull(String what);

        /**
         * 系统版本过低
         *
         * @param version
         */
        void onVersionIsTooLow(int version);

        /**
         * 系统版本符合
         *
         * @param version
         */
        void onVersionIsMatch(int version);

        /**
         * 录屏服务被异常关闭
         */
        void onRecordServiceDead();

        /**
         * 开始录制
         *
         * @param isSuccess
         */
        void onStart(boolean isSuccess);

        /**
         * 停止录制
         *
         * @param isSuccess
         */
        void onStop(boolean isSuccess);

        /**
         * 截图
         *
         * @param isSuccess
         */
        void onScreenShot(boolean isSuccess, String msg);
    }


    /**
     * 构造
     *
     * @param screenShotManagerCallBack
     * @param weakReference
     */
    public ScreenShotManager(WeakReference<Activity> weakReference, ScreenShotManagerCallBack screenShotManagerCallBack) {
        this.screenShotManagerCallBack = screenShotManagerCallBack;
        context = weakReference;
    }

    /*
   MediaProjectionManager  mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                                Intent captureIntent = null;
                                captureIntent = mediaProjectionManager.createScreenCaptureIntent();
                                startActivityForResult(captureIntent, 100);


        if (requestCode == 100 && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            screenShotManager.create(mediaProjectionManager, mediaProjection);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                        screenShotManager.startRecord();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
         */

    /**
     * @param what              1录屏  2截屏
     * @param projectionManager 截屏可以为空
     * @param project           截屏可以为空
     */
    public void create(int what, MediaProjectionManager projectionManager, MediaProjection project) {
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("init");
            }
        } else {
            if (what == 1) {
                this.state = "record";
                this.mediaProjection = project;
                this.projectionManager = projectionManager;
                ServiceUtils.startService(context.get(), RecordService.class);
                ServiceUtils.bindService(context.get(), RecordService.class, serviceConnection, BIND_AUTO_CREATE);
            } else if (what == 2) {
                this.state = "shot";
            }
        }
    }

    /**
     * 判断系统版本
     */
    public void judgeVersion() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onVersionIsTooLow(android.os.Build.VERSION.SDK_INT);
            }
        } else {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onVersionIsMatch(android.os.Build.VERSION.SDK_INT);
            }
        }
    }

    /**
     * 请求权限
     *
     * @param permissionCallback
     * @return
     */
    public void checkPermission(PermissionCallback permissionCallback) {
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("check permission");
            }
        } else {
            list.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写权限", R.drawable.permission));
            list.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录制权限", R.drawable.permission));
            PermissionUtils.requestRunTimePermission(context.get(), list, permissionCallback);
        }
    }


    /**
     * 服务连接
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recordService = ((RecordService.RecordBinder) service).getRecordService();
            if (recordService != null) {
                isConnted = true;
            }
            if (context.get() == null) {
                if (screenShotManagerCallBack != null) {
                    screenShotManagerCallBack.onContextIsNull("check permission");
                }
            } else {
                DisplayMetrics metrics = new DisplayMetrics();
                context.get().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
                recordService.setMediaProject(mediaProjection);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnted = false;
        }
    };

    /**
     * 是否已连接录屏服务控制器
     *
     * @return
     */
    public boolean isConnted() {
        return isConnted;
    }


    /**
     * 销毁
     */
    public void release() {
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("release");
            }
        }
        if ("record".equals(state)) {
            if (isConnted && ServiceUtils.isServiceRunning(context.get(), RecordService.class)) {
                ServiceUtils.stopService(context.get(), RecordService.class);
                ServiceUtils.unbindService(context.get(), serviceConnection);
                serviceConnection = null;
            } else if (ServiceUtils.isServiceRunning(context.get(), RecordService.class)) {
                ServiceUtils.stopService(context.get(), RecordService.class);
                serviceConnection = null;
            }
        }
        if (list != null) {
            list.clear();
        }
        list = null;
        screenShotManagerCallBack = null;
        recordService = null;
    }


    /**
     * 开始录屏
     *
     * @return
     */
    public void startRecord(CustomExceptionCallBack customExceptionCallBack) {
        LogUtils.e("startRecord");
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("start record");
            }
        } else {
            if (!ServiceUtils.isServiceRunning(context.get(), RecordService.class)) {
                if (screenShotManagerCallBack != null) {
                    screenShotManagerCallBack.onRecordServiceDead();
                }
            } else {
                if (screenShotManagerCallBack != null) {
                    screenShotManagerCallBack.onStart(recordService.startRecord(customExceptionCallBack));
                }
            }
        }
    }


    /**
     * 停止录屏
     *
     * @return
     */
    public void stopRecord() {
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("stop record");
            }
        } else {
            if (!ServiceUtils.isServiceRunning(context.get(), RecordService.class)) {
                if (screenShotManagerCallBack != null) {
                    screenShotManagerCallBack.onRecordServiceDead();
                }
            } else {
                if (screenShotManagerCallBack != null) {
                    screenShotManagerCallBack.onStop(recordService.stopRecord());
                }
            }
        }
    }

    /**
     * 截屏
     *
     * @return
     */
    public void startScreenShot() {
        LogUtils.e("startScreenShot");
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("start screen shot");
            }
        } else {
            screenShot();
        }
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    public void screenShot() {
        String SavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String filepath = SavePath + "/" + System.currentTimeMillis() + ".png";
//            File path = new File(SavePath);
        File file = new File(filepath);
        if (context.get() == null) {
            if (screenShotManagerCallBack != null) {
                screenShotManagerCallBack.onContextIsNull("screen shot");
            }
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            context.get().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            View decorview = context.get().getWindow().getDecorView();
            decorview.setDrawingCacheEnabled(true);
            Bitmap Bmp = decorview.getDrawingCache();

            try {
//                if (!path.exists()) {
//                    path.mkdirs();
//                }
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);
                if (null != fos) {
                    Bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
                screenShotManagerCallBack.onScreenShot(false, e.getMessage());
            }
        }
        if (file.exists()) {
            screenShotManagerCallBack.onScreenShot(true, "截屏保存成功,\n截图目录" + file.getAbsolutePath());
        }else {
            screenShotManagerCallBack.onScreenShot(false, "截屏保存失败");
        }
    }


}
