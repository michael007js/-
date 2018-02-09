package com.sss.car.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.PaymentOverForOrder;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderSynthesize;
import com.sss.car.custom.OrderSynthsizeCustom;
import com.sss.car.dao.OnIntegralCallBack;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.OrderSynthesizeModel;
import com.sss.car.model.OrderSynthesize_DataModel;
import com.sss.car.model.OrderSynthesize_Data_ListModel;
import com.sss.car.model.OrderSynthesize_Data_List_optionsModel;
import com.sss.car.order.OrderGoodsReadyBuy;
import com.sss.car.order.OrderGoodsReadyBuyEdit;
import com.sss.car.order.OrderServiceReadyBuy;
import com.sss.car.order.OrderServiceReadyBuyEdit;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 综合预购
 * Created by leilei on 2017/9/28.
 */

public class ActivityOrderSynthesize extends BaseActivity {
    @BindView(R.id.total_price_activity_order_synthesize)
    TextView totalPriceActivityOrderSynthesize;
    @BindView(R.id.click_pay_activity_order_synthesize)
    TextView clickPayActivityOrderSynthesize;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_activity_order_synthesize)
    ListViewOrderSynthesize listviewActivityOrderSynthesize;
    @BindView(R.id.activity_order_synthesize)
    LinearLayout activityOrderSynthesize;
    YWLoadingDialog ywLoadingDialog;
    List<OrderSynthesizeModel> list = new ArrayList<>();
    MenuDialog menuDialog;
    boolean can = false;
    boolean canClick = false;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        backTop = null;
        titleTop = null;
        listviewActivityOrderSynthesize = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        totalPriceActivityOrderSynthesize = null;
        clickPayActivityOrderSynthesize = null;
        backTop = null;
        titleTop = null;
        listviewActivityOrderSynthesize = null;
        activityOrderSynthesize = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_synthesize);
        ButterKnife.bind(this);
        titleTop.setText("综合预购单");
        customInit(activityOrderSynthesize, false, true, false);
        menuDialog = new MenuDialog(this);
        listviewActivityOrderSynthesize.setListViewOrderSynthesizeCallBack(new ListViewOrderSynthesize.ListViewOrderSynthesizeCallBack() {
            @Override
            public void onClickFromShoppingName(int i, List<OrderSynthesizeModel> list) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", list.get(i).shop_id));
                }
            }

            @Override
            public void onTotalPrice(double totalPrice) {

            }

            @Override
            public void onCompleteCallBack(boolean b) {
                canClick = b;
            }
        }, new OrderSynthsizeCustom.OrderSynthsizeCustomCallBack() {
            @Override
            public void onClickFromWrite(int finalI, String shop_id, OrderSynthesize_DataModel model, List<OrderSynthesize_DataModel> data) {
                try {
                    if (model.title.equals("服务类订单")) {
                        insert_order(2, data, shop_id, data.get(finalI).order_id);
                    } else {
                        insert_order(1, data, shop_id, data.get(finalI).order_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        compoOrder();
        super.onResume();
    }

    /**
     * 获取列表
     */
    public void compoOrder() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.compoOrder(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
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
                                    JSONArray datas = jsonObject.getJSONArray("data");
                                    if (datas.length() > 0) {
                                        can = true;
                                        list.clear();
                                        for (int i = 0; i < datas.length(); i++) {
                                            OrderSynthesizeModel orderSynthesizeModel = new OrderSynthesizeModel();
                                            orderSynthesizeModel.shop_id = datas.getJSONObject(i).getString("shop_id");
                                            orderSynthesizeModel.name = datas.getJSONObject(i).getString("name");
                                            orderSynthesizeModel.logo = datas.getJSONObject(i).getString("logo");
                                            orderSynthesizeModel.total_rows = datas.getJSONObject(i).getString("total_rows");
                                            orderSynthesizeModel.total = datas.getJSONObject(i).getString("total");
                                            orderSynthesizeModel.order_id = datas.getJSONObject(i).getString("order_id");
                                            List<OrderSynthesize_DataModel> orderSynthesize_DataModel = new ArrayList<>();

                                            JSONArray data = datas.getJSONObject(i).getJSONArray("data");
                                            for (int j = 0; j < data.length(); j++) {
                                                OrderSynthesize_DataModel orderSynthesize_dataModel = new OrderSynthesize_DataModel();
                                                orderSynthesize_dataModel.title = data.getJSONObject(j).getString("title");
                                                orderSynthesize_dataModel.type = data.getJSONObject(j).getString("type");
                                                orderSynthesize_dataModel.order_id = data.getJSONObject(j).getString("order_id");
                                                List<OrderSynthesize_Data_ListModel> OrderSynthesize_Data_ListList = new ArrayList<>();
                                                JSONArray lists = data.getJSONObject(j).getJSONArray("list");
                                                for (int k = 0; k < lists.length(); k++) {
                                                    OrderSynthesize_Data_ListModel orderSynthesize_data_listModel = new OrderSynthesize_Data_ListModel();
                                                    orderSynthesize_data_listModel.id = lists.getJSONObject(k).getString("id");
                                                    orderSynthesize_data_listModel.name = lists.getJSONObject(k).getString("name");
                                                    orderSynthesize_data_listModel.num = lists.getJSONObject(k).getString("num");
                                                    orderSynthesize_data_listModel.price = lists.getJSONObject(k).getString("price");
                                                    orderSynthesize_data_listModel.shop_id = lists.getJSONObject(k).getString("shop_id");
                                                    orderSynthesize_data_listModel.master_map = lists.getJSONObject(k).getString("master_map");
                                                    List<OrderSynthesize_Data_List_optionsModel> optionsList = new ArrayList<>();
                                                    JSONArray options = lists.getJSONObject(k).getJSONArray("options");
                                                    for (int l = 0; l < options.length(); l++) {
                                                        OrderSynthesize_Data_List_optionsModel optionsModel = new OrderSynthesize_Data_List_optionsModel();
                                                        optionsModel.name = options.getJSONObject(l).getString("name");
                                                        optionsModel.title = options.getJSONObject(l).getString("title");
                                                        optionsList.add(optionsModel);
                                                    }
                                                    orderSynthesize_data_listModel.options = optionsList;
                                                    OrderSynthesize_Data_ListList.add(orderSynthesize_data_listModel);
                                                }

                                                orderSynthesize_dataModel.list = OrderSynthesize_Data_ListList;
                                                orderSynthesize_DataModel.add(orderSynthesize_dataModel);
                                            }
                                            orderSynthesizeModel.data = orderSynthesize_DataModel;
                                            list.add(orderSynthesizeModel);
                                        }


                                    }
                                    totalPriceActivityOrderSynthesize.setText(list.get(0).total + "");
                                    listviewActivityOrderSynthesize.setList(list, getBaseActivityContext());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_top, R.id.click_pay_activity_order_synthesize})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_pay_activity_order_synthesize:
                if (can) {
                    if (canClick) {
//                        showDialog(totalPriceActivityOrderSynthesize.getText().toString());
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < list.get(i).data.size(); j++) {
                                stringBuilder.append(list.get(i).data.get(j).order_id);
                                stringBuilder.append(",");
                            }


                        }

//                        PayUtils.requestPayment(ywLoadingDialog, "0", stringBuilder.toString(), 2, 0, PriceUtils.formatBy2Scale(Double.valueOf(totalPriceActivityOrderSynthesize.getText().toString()), 2), getBaseActivity());
                        ToastUtils.showLongToast(getBaseActivityContext(), "提交成功！");
                    finish();
                    } else {
                        ToastUtils.showLongToast(getBaseActivityContext(), "请完善您的订单");
                    }
                } else {
                    ToastUtils.showLongToast(getBaseActivityContext(), "预购单单刷新中,请稍后...");
                }

                break;
        }
    }

    String integral = "0";
    String mode = "balance";//支付方式：balance余额，ali_pay支付，we_chat_pay微信支付

    void showDialog(String price) {
        if (menuDialog == null) {
            menuDialog = new MenuDialog(getBaseActivity());
        }

        menuDialog.createPaymentOrderDialog(ywLoadingDialog, "支付到平台", price, getBaseActivity(), new OnIntegralCallBack() {
                    @Override
                    public void onIntegralCallBack(String integral, String mode) {
                        ActivityOrderSynthesize.this.integral = integral;
                        ActivityOrderSynthesize.this.mode = mode;
                    }
                },
                new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        try {
                                            JSONArray jsonArray = new JSONArray();
                                            for (int i = 0; i < list.size(); i++) {
                                                jsonArray.put(list.get(i).order_id);
                                            }
                                            passWordKeyboard.setStatus(true);
                                            payment_order(jsonArray, integral, mode);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                bottomSheetDialog.dismiss();
                            }

                            @Override
                            public void mismatches() {
                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }
                });
    }

    /**
     * 加入预购
     *
     * @param data
     */
    public void insert_order(final int what, List<OrderSynthesize_DataModel> data, final String shop_id, final String order_id) throws JSONException {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray goods_id = new JSONArray();
        JSONArray price = new JSONArray();
        JSONArray num = new JSONArray();
        JSONArray options = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).list.size(); j++) {
                goods_id.put(data.get(i).list.get(j).id);
                price.put(data.get(i).list.get(j).price);
                num.put(data.get(i).list.get(j).num);
                JSONArray jsonArray = new JSONArray();
                for (int k = 0; k < data.get(i).list.get(j).options.size(); k++) {
                    jsonArray.put(new JSONObject()
                            .put("title", data.get(i).list.get(j).options.get(k).title)
                            .put("name", data.get(i).list.get(j).options.get(k).name));

                }
                options.put(jsonArray);
            }
        }

        if (goods_id.length() == 0 || price.length() == 0 || num.length() == 0 || options.length() == 0) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "您的预购单出错!");
            return;
        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_order(

                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("price", price)
                            .put("num", num)
                            .put("is_synthesize", "1")//是否是综合预购页面 1 是 0不是
                            .put("options", options)
                            .put("type", "order")//订单类型（pre_order）预购，（order）订单
                            .toString(), new StringCallback() {
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
                                    if (what == 1) {
                                        if (getBaseActivityContext() != null) {
                                            if ("0".equals(order_id)) {
                                                startActivity(new Intent(getBaseActivityContext(), OrderGoodsReadyBuy.class)
                                                        .putExtra("shop_id", shop_id)
                                                        .putExtra("order_id", order_id)
                                                        .putExtra("where", "fromActivityOrderSynthesize"));
                                            } else {
                                                startActivity(new Intent(getBaseActivityContext(), OrderGoodsReadyBuyEdit.class)
                                                        .putExtra("shop_id", shop_id)
                                                        .putExtra("order_id", order_id)
                                                        .putExtra("type", "1")
                                                        .putExtra("mode", "edit"));
                                            }
                                        }
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            if ("0".equals(order_id)) {
                                                startActivity(new Intent(getBaseActivityContext(), OrderServiceReadyBuy.class).putExtra("shop_id", shop_id)
                                                        .putExtra("order_id", order_id)
                                                        .putExtra("where", "fromActivityOrderSynthesize"));
                                            } else {
                                                LogUtils.e(shop_id + "---" + order_id);
                                                startActivity(new Intent(getBaseActivityContext(), OrderServiceReadyBuyEdit.class)
                                                        .putExtra("shop_id", shop_id)
                                                        .putExtra("order_id", order_id)
                                                        .putExtra("type", "2")
                                                        .putExtra("mode", "edit"));
                                            }
                                        }

                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }


    /**
     * 付款
     *
     * @param order_id
     * @param is_integral
     * @param mode
     * @throws JSONException
     */
    void payment_order(JSONArray order_id, String is_integral, String mode) throws JSONException {
        new RequestModel(System.currentTimeMillis() + "", RequestWeb.payment_order(
                new JSONObject()
                        .put("member_id", Config.member_id)
                        .put("order_id", order_id)
                        .put("is_integral", is_integral)
                        .put("mode", mode)
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
                                EventBus.getDefault().post(new PaymentOverForOrder());
                                finish();
                            } else {
                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:p-0");
                            e.printStackTrace();
                        }
                    }
                }));
    }
}
