package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.WebView.GesturesWebView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/20.
 */

public class ActivityWeb extends BaseActivity {
    public static int ACTIVITY = 0X0001;
    public static int LOGISTICS = 0x0002;
    public static int BANK_BIND=0x0003;
    public static int BANK_RECHANGE=0x0004;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.web)
    GesturesWebView web;
    @BindView(R.id.activity_web)
    LinearLayout activityWeb;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (web!=null){
            web.ondestroy();
        }
        super.onDestroy();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        if (web.back() == false) {
            web.createDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误！");
            finish();
        }
        ButterKnife.bind(this);
        customInit(activityWeb, false, true, false);
        web.init(getBaseActivity())
                .enableOpenInWebview()
                .enableZoom()
                .enableJavaScriptOpenWindowsAutomatically()
                .enableAdaptive()
                .enableJavaScript();

        if (getIntent().getExtras().getInt("type") == ACTIVITY) {
            web.loadUrl(Config.url + "/home/Subject/index?=" + getIntent().getExtras().getString("id"));
            titleTop.setText("活动");
        } else if (getIntent().getExtras().getInt("type") == LOGISTICS) {
            web.loadUrl(Config.url + "/home/Express/index?order_id=" + getIntent().getExtras().getString("order_id"));
            titleTop.setText("查看物流");
        }else if (getIntent().getExtras().getInt("type") == BANK_BIND) {
            web.loadUrl(Config.url + "/index.php/Api/chan_pay/auth_card?member_id=" + Config.member_id);
            titleTop.setText("绑定银行卡");
        }else if (getIntent().getExtras().getInt("type") == BANK_RECHANGE) {
            web.loadUrl(Config.url + "/index.php/Api/chan_pay/card_pay?member_id=" + Config.member_id+"&card_id="+getIntent().getExtras().getString("card_id")+"&money="+getIntent().getExtras().getString("money"));
            titleTop.setText("银行卡充值");
        }
        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(this, "android");
    }

    /**
     * 跳转到对应的商品页面，与js交互时用到的方法
     * @param type
     * @param goods_id
     */
    @JavascriptInterface
    public void jump(String type, String goods_id) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                    .putExtra("goods_id", goods_id)
                    .putExtra("type", type));
        }
    }
}
