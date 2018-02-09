package com.blankj.utilcode.filedownloader.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;

/**
 * 类功能描述：下载器后台服务</br>
 *
 * @author zhuiji7  (470508081@qq.com)
 * @version 1.0
 * </p>
 */

public class DownLoadService extends Service {
    private static DownLoadManager  downLoadManager;
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        LogUtils.e("onBind"+downLoadManager == null);
        return null;
    }
    
    public static DownLoadManager getDownLoadManager(){
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downLoadManager = new DownLoadManager(DownLoadService.this);
        LogUtils.e("onCreate"+downLoadManager == null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放downLoadManager
        downLoadManager.stopAllTask();
        downLoadManager = null;
        LogUtils.e("onDestroy"+downLoadManager == null);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(downLoadManager == null){
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
        LogUtils.e("onStart"+downLoadManager == null);
    }
    
    
    
    
    

}
