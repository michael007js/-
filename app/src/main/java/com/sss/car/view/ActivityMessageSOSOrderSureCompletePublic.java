package com.sss.car.view;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.dao.OnPullToRefreshListViewCallBack;
import com.sss.car.fragment.FragmentMessageOrderPublic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * SOS订单,2.订购订单,3.确认订单,4.完成订单
 * Created by leilei on 2017/10/19.
 */

public class ActivityMessageSOSOrderSureCompletePublic extends BaseActivity {
    @BindView(R.id.parent_activity_message_sos_order_sure_complete_public)
    FrameLayout parentActivityMessageSosOrderSureCompletePublic;
    @BindView(R.id.activity_message_sos_order_sure_complete_public)
    LinearLayout activityMessageSosOrderSureCompletePublic;
    FragmentMessageOrderPublic fragmentMessageOrderPublic;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        parentActivityMessageSosOrderSureCompletePublic = null;
        activityMessageSosOrderSureCompletePublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sos_order_sure_complete_public);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        ButterKnife.bind(this);
        customInit(activityMessageSosOrderSureCompletePublic, false, true, false);
        if (fragmentMessageOrderPublic == null) {
            fragmentMessageOrderPublic = new FragmentMessageOrderPublic(null, null, getIntent().getExtras().getString("status"));    //1SOS订单，2收入订单，3支出订单
            fragmentMessageOrderPublic.setOnPullToRefreshListViewCallBack(new OnPullToRefreshListViewCallBack() {
                @Override
                public void onPullToRefreshListViewCallBack(PullToRefreshListView pullToRefreshListView) {
                    fragmentMessageOrderPublic.setHeigh(60);
                }
            });
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentMessageOrderPublic, R.id.parent_activity_message_sos_order_sure_complete_public);
        }
        FragmentUtils.hideAllShowFragment(fragmentMessageOrderPublic);

        titleTop.setText(getIntent().getExtras().getString("title"));

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
