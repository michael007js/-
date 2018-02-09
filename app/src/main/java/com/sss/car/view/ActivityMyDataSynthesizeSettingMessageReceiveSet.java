package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息接收设置
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingMessageReceiveSet extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.parent_activity_my_data_synthesize_setting_message_receive_set)
    FrameLayout parentActivityMyDataSynthesizeSettingMessageReceiveSet;
    @BindView(R.id.activity_my_data_synthesize_setting_message_receive_set)
    LinearLayout activityMyDataSynthesizeSettingMessageReceiveSet;

    FragmentSetMessageReceive fragmentSetMessageReceive;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
         backTop=null;
         titleTop=null;
         rightButtonTop=null;
         parentActivityMyDataSynthesizeSettingMessageReceiveSet=null;
         activityMyDataSynthesizeSettingMessageReceiveSet=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_message_receive_set);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }

        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingMessageReceiveSet, false, true, false);
        switch (getIntent().getExtras().getString("type")) {
            case "1":
                titleTop.setText("互动消息");
                break;
            case "2":
                titleTop.setText("评价消息");
                break;
            case "3":
                titleTop.setText("订单消息");
                break;
            case "4":
                titleTop.setText("系统消息");
                break;
            case "5":
                titleTop.setText("分享消息");
                break;
        }
        rightButtonTop.setText("保存");

        if (fragmentSetMessageReceive == null) {
            fragmentSetMessageReceive = new FragmentSetMessageReceive(getIntent().getExtras().getString("type"));
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentSetMessageReceive, R.id.parent_activity_my_data_synthesize_setting_message_receive_set);
        }
        FragmentUtils.hideAllShowFragment(fragmentSetMessageReceive);

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (fragmentSetMessageReceive!=null){
                    fragmentSetMessageReceive.request();
                }
                break;
        }
    }
}
