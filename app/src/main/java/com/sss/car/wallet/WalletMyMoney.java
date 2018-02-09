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
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.P;
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

import static com.sss.car.R.id.cash;

/**
 * 我的钱包==>我的资金
 * Created by leilei on 2017/10/26.
 */

public class WalletMyMoney extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.total_wallet_my_money)
    TextView totalWalletMyMoney;
    @BindView(R.id.one_wallet_my_money)
    TextView oneWalletMyMoney;
    @BindView(R.id.two_wallet_my_money)
    TextView twoWalletMyMoney;
    @BindView(R.id.three_wallet_my_money)
    TextView threeWalletMyMoney;
    @BindView(R.id.calc_price_wallet_my_money)
    PieChart calcPriceWalletMyMoney;
    @BindView(R.id.wallet_my_money)
    LinearLayout WalletMyMoney;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.recharge_price_wallet_my_money)
    TextView rechargePriceWalletMyMoney;
    @BindView(R.id.withdraw_price_wallet_my_money)
    TextView withdrawPriceWalletMyMoney;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        withdrawPriceWalletMyMoney = null;
        totalWalletMyMoney = null;
        rechargePriceWalletMyMoney = null;
        oneWalletMyMoney = null;
        twoWalletMyMoney = null;
        threeWalletMyMoney = null;
        calcPriceWalletMyMoney = null;
        WalletMyMoney = null;
        rightButtonTop = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedWalletModel changedWalletModel) {
        my_balance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my_money);
        ButterKnife.bind(this);
        customInit(WalletMyMoney, false, true, true);
        titleTop.setText("我的资金");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("明细");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        my_balance();
    }



    @OnClick({R.id.back_top, R.id.right_button_top, R.id.recharge_price_wallet_my_money, R.id.withdraw_price_wallet_my_money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.recharge_price_wallet_my_money:
                if (getBaseActivityContext()!=null) {
                    startActivity(new Intent(getBaseActivityContext(),WalletChooseType.class));
                }
                break;
            case R.id.withdraw_price_wallet_my_money:
                if (getBaseActivityContext()!=null) {
                    startActivity(new Intent(getBaseActivityContext(),WalletWithdraw.class));
                }
                break;
            case R.id.right_button_top:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletDetails.class)
                            .putExtra("type", "4"));//1收入，2支出，3积分,4资金（不传返回所有类型）
                }
                break;
        }
    }

    void my_balance() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.my_balance(
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
                                    int total = jsonObject.getJSONObject("data").getInt("balance");
                                    int cash_deposit = jsonObject.getJSONObject("data").getInt("cash_deposit");
                                    int freeze = jsonObject.getJSONObject("data").getInt("freeze");
                                    int expendable = jsonObject.getJSONObject("data").getInt("expendable");
                                    float[] x = {(float) cash_deposit / total, (float) freeze / total, (float) expendable / total};
                                    String[] y = {"#fba62f", "#f26956", "#2466b0"};
                                    calcPriceWalletMyMoney.initSrc(x, y);
                                    oneWalletMyMoney.setText(cash_deposit + "");
                                    twoWalletMyMoney.setText(freeze + "");
                                    threeWalletMyMoney.setText(expendable + "");
                                    totalWalletMyMoney.setText(total + "");
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
