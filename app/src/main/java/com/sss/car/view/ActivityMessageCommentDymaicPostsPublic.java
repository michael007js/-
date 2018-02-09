package com.sss.car.view;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentMessageCommentDymaicPostsPublic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息==>评价==>动态评价,帖子评价,交易评价公用页面
 * Created by leilei on 2017/10/19.
 */

public class ActivityMessageCommentDymaicPostsPublic extends BaseActivity {
    @BindView(R.id.parent_activity_message_comment_dymaic_posts_public)
    FrameLayout parentActivityMessageCommentDymaicPostsPublic;
    @BindView(R.id.activity_message_comment_dymaic_posts_public)
    LinearLayout activityMessageCommentDymaicPostsPublic;
    FragmentMessageCommentDymaicPostsPublic fragmentMessageCommentDymaicPostsPublic;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        parentActivityMessageCommentDymaicPostsPublic = null;
        activityMessageCommentDymaicPostsPublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_comment_dymaic_posts_public);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(activityMessageCommentDymaicPostsPublic, false, true, false);
        titleTop.setText(getIntent().getExtras().getString("title"));
        if (fragmentMessageCommentDymaicPostsPublic == null) {
            fragmentMessageCommentDymaicPostsPublic = new FragmentMessageCommentDymaicPostsPublic(null,getIntent().getExtras().getString("type"), null);
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentMessageCommentDymaicPostsPublic, R.id.parent_activity_message_comment_dymaic_posts_public);
        }
        FragmentUtils.hideAllShowFragment(fragmentMessageCommentDymaicPostsPublic);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
