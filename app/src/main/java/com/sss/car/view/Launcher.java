package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.activity.OnePiexlActivity;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.CountDownTimerUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.networkbench.agent.impl.NBSAppAgent;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leilei on 2017/8/8.
 */

public class Launcher extends BaseActivity {
    @BindView(R.id.launcher)
    LinearLayout launcher;
    @BindView(R.id.pic_launcher)
    SimpleDraweeView picLauncher;
    Intent intent;
    CountDownTimerUtils countDownTimer=new CountDownTimerUtils(3000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (intent!=null){
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NBSAppAgent.setLicenseKey("2d36e417272e46439774db3c0914c8e7").withLocationServiceEnabled(true).start(this.getApplicationContext());
        intent=new Intent(this,Splash.class);
        setContentView(R.layout.launcher);
        ButterKnife.bind(this);
        FrescoUtils.showImage(false,
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                Uri.parse("res://com.sss.car/"+R.mipmap.launcher),
                picLauncher,0f);
        customInit(launcher,false,false,false);
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        countDownTimer=null;
        intent=null;
        launcher=null;
        picLauncher=null;
    }
}
