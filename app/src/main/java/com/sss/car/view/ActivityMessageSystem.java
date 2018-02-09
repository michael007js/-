package com.sss.car.view;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentMessageSystem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统消息详情
 * Created by leilei on 2017/12/12.
 */

public class ActivityMessageSystem extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_activity_message_system)
    FrameLayout parentActivityMessageSystem;
    @BindView(R.id.activity_message_system)
    LinearLayout activityMessageSystem;
    FragmentMessageSystem fragmentMessageSystem;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentMessageSystem != null) {
            fragmentMessageSystem.onDestroy();
        }
        fragmentMessageSystem = null;
        backTop = null;
        titleTop = null;
        parentActivityMessageSystem = null;
        activityMessageSystem = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        setContentView(R.layout.activity_message_system);
        ButterKnife.bind(this);
        customInit(activityMessageSystem, false, true, false);
        titleTop.setText(getIntent().getExtras().getString("title"));
        fragmentMessageSystem = new FragmentMessageSystem(null,getIntent().getExtras().getString("type"),false,null);
        FragmentUtils.addFragment(getSupportFragmentManager(),fragmentMessageSystem,R.id.parent_activity_message_system);
        FragmentUtils.hideAllShowFragment(fragmentMessageSystem);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
