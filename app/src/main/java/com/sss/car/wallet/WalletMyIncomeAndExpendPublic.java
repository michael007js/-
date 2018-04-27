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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包==>收入/支出公用
 * Created by leilei on 2017/10/24.
 */

public class WalletMyIncomeAndExpendPublic extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.total_price_wallet_my_income_and_expend_public)
    TextView totalPriceWalletMyIncomeAndExpendPublic;
    @BindView(R.id.wallet_my_income_and_expend_public)
    LinearLayout WalletMyIncomeAndExpendPublic;
    @BindView(R.id.calc_price_wallet_my_income_and_expend_public)
    PieChart calcPriceWalletMyIncomeAndExpendPublic;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.goods_wallet_my_income_and_expend_public)
    TextView goodsWalletMyIncomeAndExpendPublic;
    @BindView(R.id.deposit_wallet_my_income_and_expend_public)
    TextView depositWalletMyIncomeAndExpendPublic;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.title_goods_wallet_my_income_and_expend_public)
    TextView titleGoodsWalletMyIncomeAndExpendPublic;
    @BindView(R.id.money_wallet_my_income_and_expend_public)
    TextView moneyWalletMyIncomeAndExpendPublic;
    @BindView(R.id.three)
    TextView three;
    @BindView(R.id.three_sss)
    TextView threeSss;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        goodsWalletMyIncomeAndExpendPublic = null;
        depositWalletMyIncomeAndExpendPublic = null;
        titleTop = null;
        totalPriceWalletMyIncomeAndExpendPublic = null;
        WalletMyIncomeAndExpendPublic = null;
        calcPriceWalletMyIncomeAndExpendPublic = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        rightButtonTop = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my_income_and_expend_public);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        ButterKnife.bind(this);
        if ("1".equals(getIntent().getExtras().getString("mode"))) {
            titleTop.setText("我的收入");
            titleGoodsWalletMyIncomeAndExpendPublic.setText("销售收入");
            moneyWalletMyIncomeAndExpendPublic.setText("保证金收入");
            three.setText("其他收入");
            title.setText("收入总计(元)");
        }
        if ("2".equals(getIntent().getExtras().getString("mode"))) {
            titleTop.setText("我的支出");
            title.setText("支出总计(元)");
            titleGoodsWalletMyIncomeAndExpendPublic.setText("购买支出");
            moneyWalletMyIncomeAndExpendPublic.setText("保证金支出");
            three.setText("其他支出");
        }
        customInit(WalletMyIncomeAndExpendPublic, false, true, false);
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("明细");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(getIntent().getExtras().getString("mode"))) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), WalletDetails.class)
                                .putExtra("type", "1"));//1收入，2支出，3积分,4资金（不传返回所有类型）
                    }

                }
                if ("2".equals(getIntent().getExtras().getString("mode"))) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), WalletDetails.class)
                                .putExtra("type", "2"));//1收入，2支出，3积分,4资金（不传返回所有类型）
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        walletIncome();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    /**
     */
    public void walletIncome() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.walletIncome(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", getIntent().getExtras().getString("mode"))//1收入,2支出
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
                                    totalPriceWalletMyIncomeAndExpendPublic.setText(jsonObject.getJSONObject("data").getString("total"));
                                    totalPriceWalletMyIncomeAndExpendPublic.setText( jsonObject.getJSONObject("data").getString("total") );
                                    goodsWalletMyIncomeAndExpendPublic.setText(jsonObject.getJSONObject("data").getString("consume") );
                                    depositWalletMyIncomeAndExpendPublic.setText(jsonObject.getJSONObject("data").getString("deposit") );
                                    threeSss.setText( jsonObject.getJSONObject("data").getString("other") );
                                    float[] a = {  (float) jsonObject.getJSONObject("data").getDouble("deposit") / (float) jsonObject.getJSONObject("data").getDouble("total"),
                                            (float) jsonObject.getJSONObject("data").getDouble("consume") / (float) jsonObject.getJSONObject("data").getDouble("total"),
                                            (float) jsonObject.getJSONObject("data").getDouble("other") / (float) jsonObject.getJSONObject("data").getDouble("total")};
                                    String[] color = {"#f26956", "#fba62f", "#0000cc"};
                                    calcPriceWalletMyIncomeAndExpendPublic.initSrc(a, color);
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
