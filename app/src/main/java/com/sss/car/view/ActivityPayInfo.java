package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPay;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;
import com.sss.car.EventBusModel.ChangedCouponModel;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedPopularizeModel;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;
import com.sss.car.WebViewActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/25.
 */

@SuppressWarnings("ALL")
public class ActivityPayInfo extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.activity_pay_info)
    LinearLayout activityPayInfo;
    String orderID = "";
    private static final int REQUEST_CODE = 8888;
    /**
     * 调用收银台页面结果callback---处理中
     */
    private static final String CALLBACK_PROCESS = "process";
    /**
     * 调用收银台页面结果callback---失败
     */
    private static final String CALLBACK_FAILD = "failed";
    /**
     * 调用收银台页面结果callback---成功
     */
    private static final String CALLBACK_SUCCESS = "success";
    YWLoadingDialog ywLoadingDialog;


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
        setContentView(R.layout.activity_pay_info);
        ButterKnife.bind(this);
        customInit(activityPayInfo, false, true, false);
        titleTop.setText("支付");
//        orderID=getIntent().getExtras().getString("orderID");
        HashMap<String, String> respMap = new Gson().fromJson(getIntent().getExtras().getString("respMap"), HashMap.class);
        String payContent = respMap.get("PayInfo");
        WebViewActivity.startActionForResult(this, payContent, REQUEST_CODE,"支付");
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            doQueryState();
        }
    }

    private void doQueryState() {

        HashMap<String, String> queryMap = (HashMap<String, String>) getIntent().getSerializableExtra("requestMap");
        queryMap.put("TrxId", StringUtils.getOrderid());
        queryMap.put("OrderTrxId", queryMap.get("OutTradeNo"));
        queryMap.put("TradeType", "pay_order");
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(this);
        ywLoadingDialog.show();
        ywLoadingDialog.setTitle("正在获取支付结果");
        YQPay.getInstance(this).queryState(queryMap, new IYQPayCallback() {
            @Override
            public void payResult(final String status, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ywLoadingDialog = null;
                        LogUtils.e("RESULT", "CALLBACK: " + "status:" + status + " message:" + message);
                        if (CALLBACK_FAILD.equals(status)) {
                            ToastUtils.showShortToast(getBaseActivityContext(), message);
                        } else {
                            Map<String, String> respMap = new Gson().fromJson(message, HashMap.class);
                            if (CALLBACK_SUCCESS.equals(status)) {
                                deposit();
                            } else if (CALLBACK_PROCESS.equals(status)) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您的订单正在处理中");
                            }
                        }
                        finish();
                    }
                });
            }
        });
    }


    private void deposit() {
        ToastUtils.showShortToast(getBaseActivityContext(), "支付成功");
        switch (getIntent().getExtras().getInt("title_type")) {//1SOS订单支付，2订单支付，3优惠券支付，4推广订单支付，5账户充值
            case 1:
                EventBus.getDefault().post(new ChangedOrderModel());
                break;
            case 2:
                EventBus.getDefault().post(new ChangedOrderModel());
                break;
            case 3:
                EventBus.getDefault().post(new ChangedCouponModel());
                break;
            case 4:
                EventBus.getDefault().post(new ChangedPopularizeModel());
                break;
            case 5:
                EventBus.getDefault().post(new ChangedWalletModel());
                break;

        }
    }

}
