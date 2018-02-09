package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.sss.car.R;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.fragment.FragmentOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/10/8.
 */

public class ActivityOrderExpend extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.ready_buy_activity_order_expend)
    LinearLayout readyBuyActivityOrderExpend;
    @BindView(R.id.waiting_for_payment_activity_order_expend)
    LinearLayout waitingForPaymentActivityOrderExpend;
    @BindView(R.id.waiting_for_service_activity_order_expend)
    LinearLayout waitingForServiceActivityOrderExpend;
    @BindView(R.id.waiting_for_get_activity_order_expend)
    LinearLayout waitingForGetActivityOrderExpend;
    @BindView(R.id.waiting_for_comment_activity_order_expend)
    LinearLayout waitingForCommentActivityOrderExpend;
    @BindView(R.id.waiting_for_returns_and_change_activity_order_expend)
    LinearLayout waitingForReturnsAndChangeActivityOrderExpend;
    @BindView(R.id.activity_order_expend)
    LinearLayout activityOrderExpend;
    @BindView(R.id.complete_activity_order_expend)
    LinearLayout completeActivityOrderExpend;
    @BindView(R.id.waiting_for_ready_buy_activity_order_expend)
    LinearLayout waitingForReadyBuyActivityOrderExpend;

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
        setContentView(R.layout.activity_order_expend);
        ButterKnife.bind(this);
        customInit(activityOrderExpend, false, true, false);
        titleTop.setText("我的支出订单");
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.back_top,R.id.waiting_for_ready_buy_activity_order_expend, R.id.waiting_for_payment_activity_order_expend, R.id.complete_activity_order_expend, R.id.ready_buy_activity_order_expend, R.id.waiting_for_service_activity_order_expend, R.id.waiting_for_get_activity_order_expend, R.id.waiting_for_comment_activity_order_expend, R.id.waiting_for_returns_and_change_activity_order_expend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.waiting_for_ready_buy_activity_order_expend:

                break;
            case R.id.ready_buy_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "0")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_READY_BUY)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", false)
                            .putExtra("title", "待预购订单"));
                }
                break;
            case R.id.waiting_for_payment_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "1")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_PAYMENT)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", true)
                            .putExtra("isShowQR", false)
                            .putExtra("title", "待付款订单"));
                }
                break;
            case R.id.waiting_for_service_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "10")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_BUY)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待服务订单"));
                }
                break;
            case R.id.waiting_for_get_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "3")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_RECEIVING)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", true)
                            .putExtra("isShowQR", true)
                            .putExtra("isShowInnerCheckBox", false)
                            .putExtra("title", "待收货订单"));
                }
                break;
            case R.id.waiting_for_comment_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "4")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_BUY)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待评价订单"));
                }
                break;
            case R.id.waiting_for_returns_and_change_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrderReturnsChange.class)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("title", "退换货订单"));
                }
                break;
            case R.id.complete_activity_order_expend:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "5")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY)
                            .putExtra("what", FragmentOrder.EXPEND)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "已完成订单"));
                }
                break;
        }
    }
}
