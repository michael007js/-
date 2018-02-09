package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.ListView.PullToRefreshListViewSSS;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCommunity_Userinfo_Posts;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/12/30.
 */

public class ActivitySharePostsInterest extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.activity_share_posts_interest)
    LinearLayout activitySharePostsInterest;
    FragmentCommunity_Userinfo_Posts fragmentCommunity_userinfo_posts;
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
        setContentView(R.layout.activity_share_posts_interest);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        titleTop.setText("兴趣社区");
        customInit(activitySharePostsInterest,false,true,false);
        fragmentCommunity_userinfo_posts=new FragmentCommunity_Userinfo_Posts(false,true, "0", "","1",null);
        FragmentUtils.addFragment(getSupportFragmentManager(),fragmentCommunity_userinfo_posts,R.id.parent);

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
