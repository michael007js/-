package com.blankj.utilcode.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.LogUtils;


/**
 * 一像素保活Activity
 * Created by Administrator on 2017/7/10.
 */
public class OnePiexlActivity extends Activity {
    PowerManager pm;
    private BroadcastReceiver br;
    CountDownTimerUtils countDownTimerUtils=new CountDownTimerUtils(Long.MAX_VALUE,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            LogUtils.e("I am alive,surplus:"+millisUntilFinished);
        }

        @Override
        public void onFinish() {
            LogUtils.e("I am die");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                LogUtils.i(intent.getAction());
//                LogUtils.i(context.toString());
//                Intent intent1 = new Intent(Intent.ACTION_MAIN);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent1.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent1);
                LogUtils.d("OnepxActivity finish   ================");
                finish();
            }
        };
        registerReceiver(br, new IntentFilter("finish activity"));
        checkScreenOn("onCreate");
        countDownTimerUtils.start();

    }

    private void checkScreenOn(String methodName) {
         pm = (PowerManager) OnePiexlActivity.this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        LogUtils.e("isScreenOn: "+isScreenOn);
        if(isScreenOn){
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        if (countDownTimerUtils!=null){
            countDownTimerUtils.onFinish();
            countDownTimerUtils.cancel();
        }
        countDownTimerUtils=null;
        pm=null;
        LogUtils.e("===onDestroy===");
        try{
            unregisterReceiver(br);
        }catch (IllegalArgumentException e){
            LogUtils.e("receiver is not resisted: "+e);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreenOn("onResume");
    }
}