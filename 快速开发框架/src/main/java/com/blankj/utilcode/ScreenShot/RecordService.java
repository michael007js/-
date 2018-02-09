package com.blankj.utilcode.ScreenShot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.dao.CustomExceptionCallBack;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import static android.R.attr.data;


public class RecordService extends Service {
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;


    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
        mediaRecorder = null;
        if (mediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaProjection.stop();
            }
        }
        mediaProjection = null;
        if (virtualDisplay != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                virtualDisplay.release();
            }
        }
        virtualDisplay = null;
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    public boolean startRecord(CustomExceptionCallBack customExceptionCallBack) {
        LogUtils.e("startRecord");
        if (mediaProjection == null || running) {
            return false;
        }
        initRecorder(customExceptionCallBack);
        createVirtualDisplay();
        mediaRecorder.start();
        running = true;
        return true;
    }

    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            virtualDisplay.release();
        } else {
            ToastUtils.showShortToast(getApplicationContext(), "您的系统版本过低!Err:-2");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjection.stop();
        } else {
            ToastUtils.showShortToast(getApplicationContext(), "您的系统版本过低!Err:-3");
        }

        return true;
    }

    /**
     * 如果已经root,可以使用此方法
     */
    public void screenShot() {
        Process sh;
        try {
            sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + "/sdcard/Image.png").getBytes("ASCII"));
            os.flush();
            os.close();
            try {
                sh.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createVirtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (virtualDisplay == null) {
                virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
            }
        } else {
            ToastUtils.showShortToast(getApplicationContext(), "您的系统版本过低!Err:-4");
        }
    }

    /**
     * 初始化录屏
     */
    private void initRecorder(CustomExceptionCallBack customExceptionCallBack) {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getsaveDirectory(customExceptionCallBack) + System.currentTimeMillis() + ".mp4");
        mediaRecorder.setVideoSize(width, height);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mediaRecorder.setVideoFrameRate(30);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取保存目录
     *
     * @return
     */
    public String getsaveDirectory(CustomExceptionCallBack customExceptionCallBack) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(new WeakReference<Context>(this),customExceptionCallBack) + "/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            ToastUtils.showShortToast(getApplicationContext(), "储存目录:" + rootDir);

            return rootDir;
        } else {
            return null;
        }
    }

public class RecordBinder extends Binder {
    public RecordService getRecordService() {
        return RecordService.this;
    }
}
}