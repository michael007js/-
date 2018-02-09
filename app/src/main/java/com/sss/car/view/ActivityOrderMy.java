package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.order.OrderSOSMyBuyer;
import com.sss.car.order.OrderSOSMySeller;
import com.sss.car.order_new.NewOrderBuyer;
import com.sss.car.order_new.NewOrderSeller;
import com.sss.car.order_new.OrderCheckInfo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的订单
 * Created by leilei on 2017/10/7.
 */

public class ActivityOrderMy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.in_activity_order_my)
    LinearLayout inActivityOrderMy;
    @BindView(R.id.out_activity_order_my)
    LinearLayout outActivityOrderMy;
    @BindView(R.id.sos_activity_order_my_seller)
    LinearLayout sosActivityOrderMySeller;
    @BindView(R.id.activity_order_my)
    LinearLayout activityOrderMy;
    @BindView(R.id.sos_activity_order_my_buyer)
    LinearLayout sosActivityOrderMyBuyer;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.check)
    TextView check;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        inActivityOrderMy = null;
        outActivityOrderMy = null;
        sosActivityOrderMySeller = null;
        activityOrderMy = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_my);
        ButterKnife.bind(this);
        customInit(activityOrderMy, false, true, false);
        titleTop.setText("我的订单");
//        try {
//            userInfo();
//        } catch (JSONException e) {
//            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-1");
//            e.printStackTrace();
//        }
    }

    @OnClick({R.id.back_top, R.id.in_activity_order_my, R.id.out_activity_order_my, R.id.sos_activity_order_my_seller, R.id.sos_activity_order_my_buyer,R.id.check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.in_activity_order_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), NewOrderSeller.class));
                }
                break;
            case R.id.out_activity_order_my:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), NewOrderBuyer.class));
                }
                break;
            case R.id.sos_activity_order_my_seller:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), OrderSOSMySeller.class));
                }

                break;
            case R.id.sos_activity_order_my_buyer:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), OrderSOSMyBuyer.class));
                }
                break;
            case R.id.check:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), OrderCheckInfo.class));
                }
                break;
        }
    }

    /**
     * 请求用户基本信息(是否已开店)
     *
     * @throws JSONException
     */
    void userInfo() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.userInfo(
                new JSONObject()
                        .put("member_id", Config.member_id).toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                        }
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        if (StringUtils.isEmpty(response)) {
                            if (getBaseActivityContext() != null) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (!"0".equals(jsonObject.getJSONObject("data").getString("shop_count"))) {
                                        sosActivityOrderMySeller.setVisibility(View.VISIBLE);
                                        inActivityOrderMy.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

}
