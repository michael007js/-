package com.sss.car.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包
 * Created by leilei on 2017/10/24.
 */

public class WalletMy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_income_wallet_my)
    TextView showIncomeWalletMy;
    @BindView(R.id.click_income_wallet_my)
    LinearLayout clickIncomeWalletMy;
    @BindView(R.id.show_expend_wallet_my)
    TextView showExpendWalletMy;
    @BindView(R.id.click_expend_wallet_my)
    LinearLayout clickExpendWalletMy;
    @BindView(R.id.show_integral_wallet_my)
    TextView showIntegralWalletMy;
    @BindView(R.id.click_integral_wallet_my)
    LinearLayout clickIntegralWalletMy;
    @BindView(R.id.show_coupon_wallet_my)
    TextView showCouponWalletMy;
    @BindView(R.id.click_coupon_wallet_my)
    LinearLayout clickCouponWalletMy;
    @BindView(R.id.show_money_wallet_my)
    TextView showMoneyWalletMy;
    @BindView(R.id.click_money_wallet_my)
    LinearLayout clickMoneyWalletMy;
    @BindView(R.id.check_wallet_my)
    TextView checkWalletMy;
    @BindView(R.id.wallet_my)
    LinearLayout walletMy;
    YWLoadingDialog ywLoadingDialog;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        showIncomeWalletMy = null;
        clickIncomeWalletMy = null;
        showExpendWalletMy = null;
        clickExpendWalletMy = null;
        showIntegralWalletMy = null;
        clickIntegralWalletMy = null;
        showCouponWalletMy = null;
        clickCouponWalletMy = null;
        clickMoneyWalletMy = null;
        checkWalletMy = null;
        walletMy = null;
        showMoneyWalletMy = null;
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedWalletModel changedWalletModel) {
        wallet() ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my);
        ButterKnife.bind(this);
        customInit(walletMy, false, true, true);
        titleTop.setText("我的钱包");
        wallet();
    }

    @OnClick({R.id.back_top, R.id.click_income_wallet_my, R.id.click_expend_wallet_my, R.id.click_integral_wallet_my, R.id.click_coupon_wallet_my, R.id.click_money_wallet_my, R.id.check_wallet_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_income_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletMyIncomeAndExpendPublic.class)
                            .putExtra("mode", "1"));
                }
                break;
            case R.id.click_expend_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletMyIncomeAndExpendPublic.class)
                            .putExtra("mode", "2"));
                }
                break;
            case R.id.click_integral_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletMyIntegral.class));
                }
                break;
            case R.id.click_coupon_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletMyCoupon.class));
                }
                break;
            case R.id.click_money_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletMyMoney.class));
                }
                break;
            case R.id.check_wallet_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletDetails.class)
                            .putExtra("type", ""));//1收入，2支出，3积分,4资金（不传返回所有类型）
                }
                break;
        }
    }

    public void wallet() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.wallet(
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
                                    showIncomeWalletMy.setText(jsonObject.getJSONObject("data").getString("earnings")+"元");
                                    showExpendWalletMy.setText(jsonObject.getJSONObject("data").getString("expenditure")+"元");
                                    showIntegralWalletMy.setText(jsonObject.getJSONObject("data").getString("integral")+"分");
                                    showCouponWalletMy.setText(jsonObject.getJSONObject("data").getString("coupon")+"张");
                                    showMoneyWalletMy.setText(jsonObject.getJSONObject("data").getString("balance")+"元");
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
