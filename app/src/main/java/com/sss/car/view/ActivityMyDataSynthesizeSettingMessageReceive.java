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
 * 消息接收
 * Created by leilei on 2017/11/7.
 */

public class ActivityMyDataSynthesizeSettingMessageReceive extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.click_chat)
    LinearLayout clickChat;
    @BindView(R.id.click_comment)
    LinearLayout clickComment;
    @BindView(R.id.click_order)
    LinearLayout clickOrder;
    @BindView(R.id.click_system)
    LinearLayout clickSystem;
    @BindView(R.id.click_share)
    LinearLayout clickShare;
    @BindView(R.id.activity_my_data_synthesize_setting_message_receive)
    LinearLayout activityMyDataSynthesizeSettingMessageReceive;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        clickChat = null;
        clickComment = null;
        clickOrder = null;
        clickSystem = null;
        clickShare = null;
        activityMyDataSynthesizeSettingMessageReceive = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_message_receive);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingMessageReceive, false, true, false);
        titleTop.setText("消息接收");
    }

    @OnClick({R.id.back_top, R.id.click_chat, R.id.click_comment, R.id.click_order, R.id.click_system, R.id.click_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_chat:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingMessageReceiveSet.class)
                    .putExtra("type","1"));
                }
                break;
            case R.id.click_comment:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingMessageReceiveSet.class)
                            .putExtra("type","2"));
                }
                break;
            case R.id.click_order:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingMessageReceiveSet.class)
                            .putExtra("type","3"));
                }
                break;
            case R.id.click_system:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingMessageReceiveSet.class)
                            .putExtra("type","4"));
                }
                break;
            case R.id.click_share:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataSynthesizeSettingMessageReceiveSet.class)
                            .putExtra("type","5"));
                }
                break;
        }
    }
}
