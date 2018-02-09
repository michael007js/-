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
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityWalletIntegral_And_CouponSend;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包==>积分
 * Created by leilei on 2017/10/25.
 */

public class WalletMyIntegral extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.total_integral_wallet_my_integral)
    TextView totalIntegralWalletMyIntegral;
    @BindView(R.id.one_wallet_my_integral)
    TextView oneWalletMyIntegral;
    @BindView(R.id.two_wallet_my_integral)
    TextView twoWalletMyIntegral;
    @BindView(R.id.income_wallet_my_integral)
    TextView incomeWalletMyIntegral;
    @BindView(R.id.four_wallet_my_integral)
    TextView fourWalletMyIntegral;
    @BindView(R.id.calc_price_wallet_my_integral)
    PieChart calcPriceWalletMyIntegral;
    @BindView(R.id.give_wallet_my)
    TextView giveWalletMy;
    @BindView(R.id.wallet_my_integral)
    LinearLayout walletMyIntegral;

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
        rightButtonTop = null;
        totalIntegralWalletMyIntegral = null;
        oneWalletMyIntegral = null;
        twoWalletMyIntegral = null;
        incomeWalletMyIntegral = null;
        fourWalletMyIntegral = null;
        calcPriceWalletMyIntegral = null;
        giveWalletMy = null;
        walletMyIntegral = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my_integral);
        ButterKnife.bind(this);
        titleTop.setText("我的积分");
        customInit(walletMyIntegral,false,true,false);
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("明细");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));

    }

    @Override
    protected void onResume() {
        super.onResume();
        integral();
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.give_wallet_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletDetails.class)
                            .putExtra("type", "3"));//1收入，2支出，3积分,4资金（不传返回所有类型）
                }
                break;
            case R.id.give_wallet_my:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityWalletIntegral_And_CouponSend.class)
                    .putExtra("mode","integral"));
                }
                break;
        }
    }

    public void integral() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.integral(
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
                                    int a = jsonObject.getJSONObject("data").getInt("award");
                                    int b = jsonObject.getJSONObject("data").getInt("give");
                                    int c = jsonObject.getJSONObject("data").getInt("consume");
                                    int d = jsonObject.getJSONObject("data").getInt("gain");
                                    int e = jsonObject.getJSONObject("data").getInt("integral");
                                    totalIntegralWalletMyIntegral.setText(e + "");
                                    oneWalletMyIntegral.setText(a + "");
                                    twoWalletMyIntegral.setText(b + "");
                                    incomeWalletMyIntegral.setText(c + "");
                                    fourWalletMyIntegral.setText(d + "");
                                    float[] x = {(float) a / e, (float) b / e,(float) c / e,(float) d / e};
                                    String[] y = {"#fba62e", "#f26956","#2467b1","#b82325"};
                                    calcPriceWalletMyIntegral.initSrc(x, y);

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
