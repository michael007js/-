package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by leilei on 2018/1/6.
 */

public class ActivityPC extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.pic_activity_login)
    SimpleDraweeView picActivityLogin;
    @BindView(R.id.click)
    TextView click;
    @BindView(R.id.activity_pc)
    LinearLayout activityPc;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc);
        ButterKnife.bind(this);
        customInit(activityPc, false, true, false);
        titleTop.setText("PC网页端");
        APPOftenUtils.underLineOfTextView(click);

    }

    @OnClick({R.id.back_top, R.id.click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click:
                startActivity(IntentUtils.getBrowserIntent(click.getText().toString().trim()));
                break;
        }
    }
}
