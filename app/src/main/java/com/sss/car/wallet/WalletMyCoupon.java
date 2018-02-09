package com.sss.car.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.PieChart;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CouponChange;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityWalletMyCouponDetails;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包==>我的优惠券
 * Created by leilei on 2017/10/26.
 */

public class WalletMyCoupon extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.total_wallet_my_coupon)
    TextView totalWalletMyCoupon;
    @BindView(R.id.one_wallet_my_coupon)
    TextView oneWalletMyCoupon;
    @BindView(R.id.two_wallet_my_coupon)
    TextView twoWalletMyCoupon;
    @BindView(R.id.three_wallet_my_coupon)
    TextView threeWalletMyCoupon;
    @BindView(R.id.calc_price_wallet_my_coupon)
    PieChart calcPriceWalletMyCoupon;
    @BindView(R.id.wallet_my_coupon)
    LinearLayout walletMyCoupon;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        totalWalletMyCoupon = null;
        oneWalletMyCoupon = null;
        twoWalletMyCoupon = null;
        threeWalletMyCoupon = null;
        calcPriceWalletMyCoupon = null;
        walletMyCoupon = null;rightButtonTop=null;
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my_coupon);
        ButterKnife.bind(this);
        customInit(walletMyCoupon, false, true, true);
        titleTop.setText("我的优惠券");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("明细");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
    }
    @Override
    protected void onResume() {
        super.onResume();
        walletCoupon();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CouponChange couponChange){
        walletCoupon();
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityWalletMyCouponDetails.class));
                }
                break;
        }
    }

    void walletCoupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.walletCoupon(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    int total = jsonObject.getJSONObject("data").getInt("count");
                                    int full_cut = jsonObject.getJSONObject("data").getInt("full_cut");
                                    int cash = jsonObject.getJSONObject("data").getInt("cash");
                                    int discount = jsonObject.getJSONObject("data").getInt("discount");
                                    float[] x = {(float) cash / total, (float) discount / total, (float) full_cut / total};
                                    String[] y = {"#fba62f", "#f26956", "#2466b0"};
                                    calcPriceWalletMyCoupon.initSrc(x, y);
                                    oneWalletMyCoupon.setText(cash + "");
                                    twoWalletMyCoupon.setText(discount + "");
                                    threeWalletMyCoupon.setText(full_cut + "");
                                    totalWalletMyCoupon.setText(total + "张");
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


}
