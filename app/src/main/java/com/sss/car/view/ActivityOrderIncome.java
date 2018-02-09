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
 * 我的收入订单
 * Created by leilei on 2017/10/7.
 */

public class ActivityOrderIncome extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.waitting_for_send_activity_order_income)
    LinearLayout waittingForSendActivityOrderIncome;
    @BindView(R.id.waitting_for_service_activity_order_income)
    LinearLayout waittingForServiceActivityOrderIncome;
    @BindView(R.id.waitting_for_comment_activity_order_income)
    LinearLayout waittingForCommentActivityOrderIncome;
    @BindView(R.id.waitting_for_alteration_activity_order_income)
    LinearLayout waittingForAlterationActivityOrderIncome;
    @BindView(R.id.activity_order_income)
    LinearLayout activityOrderIncome;
    @BindView(R.id.waitting_for_readybuy_activity_order_income)
    LinearLayout waittingForReadybuyActivityOrderIncome;

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
        setContentView(R.layout.activity_order_income);
        ButterKnife.bind(this);
        customInit(activityOrderIncome, false, true, false);
        titleTop.setText("我的收入订单");

    }

    @OnClick({R.id.back_top,R.id.waitting_for_readybuy_activity_order_income, R.id.waitting_for_send_activity_order_income, R.id.waitting_for_service_activity_order_income, R.id.waitting_for_comment_activity_order_income, R.id.waitting_for_alteration_activity_order_income})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.waitting_for_readybuy_activity_order_income:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "1")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_SEND)
                            .putExtra("what", FragmentOrder.INCOME)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待预购订单"));
                }
                break;
            case R.id.waitting_for_send_activity_order_income:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "2")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_SEND)
                            .putExtra("what", FragmentOrder.INCOME)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待发货订单"));
                }
                break;
            case R.id.waitting_for_service_activity_order_income:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "10")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_SELLER)
                            .putExtra("what", FragmentOrder.INCOME)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待服务订单"));
                }
                break;
            case R.id.waitting_for_comment_activity_order_income:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrder.class)
                            .putExtra("status", "4")
                            .putExtra("mode", ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_SELLER)
                            .putExtra("what", FragmentOrder.INCOME)
                            .putExtra("isShowCheckBox", false)
                            .putExtra("isShowQR", true)
                            .putExtra("title", "待评价订单"));
                }
                break;
            case R.id.waitting_for_alteration_activity_order_income:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityOrderReturnsChange.class)
                            .putExtra("what", FragmentOrder.INCOME)
                            .putExtra("title", "退换货订单"));
                }
                break;
        }
    }
}
