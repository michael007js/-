package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付绑定
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizePayBindManager extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.click_zhifubao)
    LinearLayout clickZhifubao;
    @BindView(R.id.click_weixin)
    LinearLayout clickWeixin;

    @BindView(R.id.activity_my_data_synthesize_pay_bind_manager)
    LinearLayout activityMyDataSynthesizePayBindManager;
    @BindView(R.id.title_top)
    TextView titleTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        topParent = null;
        clickZhifubao = null;
        clickWeixin = null;
        activityMyDataSynthesizePayBindManager = null;
        titleTop = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_pay_bind_manager);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizePayBindManager, false, true, false);
        titleTop.setText("支付绑定");
    }

    @OnClick({R.id.back_top,  R.id.click_zhifubao, R.id.click_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_zhifubao:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeBindAccountWxZfb.class)
                    .putExtra("what",ActivityMyDataSynthesizeBindAccountWxZfb.ZFB));
                }
                break;
            case R.id.click_weixin:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeBindAccountWxZfb.class)
                            .putExtra("what",ActivityMyDataSynthesizeBindAccountWxZfb.WX));
                }
                break;
        }
    }
}
