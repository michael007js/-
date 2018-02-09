package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.util.DateUtils;
import com.sss.car.Config;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.OrderModel;
import com.sss.car.model.OrderModel_goods_data;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityMyDataSetPassword;
import com.sss.car.view.ActivityOrderApplyForReturnsChangeRightTopButtonDetails;
import com.sss.car.view.ActivityOrderCommentBuyer;
import com.sss.car.view.ActivityOrderCommentSeller;
import com.sss.car.view.ActivityOrderCompleteDataBuyerRightTopButtonDetails;
import com.sss.car.view.ActivityOrderDetailsPublic;
import com.sss.car.view.ActivityOrderDisposeSellerRightTopButtonDetails;
import com.sss.car.view.ActivityQRImage;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static android.R.attr.password;
import static com.amap.api.mapcore2d.q.t;


/**
 * 订单模块收入支出公用Fragment
 * Created by leilei on 2017/10/7.
 */

@SuppressLint("ValidFragment")
public class FragmentOrder extends BaseFragment implements ListViewOrder.ListViewOrderCallBack, RefreshLoadMoreLayout.CallBack {
    public static final int INCOME = 0X001A;
    public static final int EXPEND = 0X002A;
    @BindView(R.id.ListViewOrder_fragment_order)
    ListViewOrder ListViewOrderFragmentOrder;
    @BindView(R.id.refresh_fragment_order)
    RefreshLoadMoreLayout refreshFragmentOrder;
    @BindView(R.id.top_fragment_order)
    ImageView topFragmentOrder;
    @BindView(R.id.scollview_fragment_order)
    ScrollView scollviewFragmentOrder;
    Unbinder unbinder;

    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    List<OrderModel> list = new ArrayList<>();
    String status;
    int mode;
    int what;
    boolean isShowCheckBox;
    boolean isShowQR;

    View headView;

    MenuDialog menuDialog;
    String start, end;
    @BindView(R.id.cb_fragment_order)
    CheckBox cbFragmentOrder;
    @BindView(R.id.price_fragment_order)
    TextView priceFragmentOrder;
    @BindView(R.id.all_fragment_order)
    LinearLayout allFragmentOrder;
    @BindView(R.id.pay_fragment_order)
    TextView payFragmentOrder;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        allFragmentOrder = null;
        payFragmentOrder = null;
        cbFragmentOrder = null;
        priceFragmentOrder = null;
        headView = null;
        if (ListViewOrderFragmentOrder != null) {
            ListViewOrderFragmentOrder.clear();
        }
        ListViewOrderFragmentOrder = null;
        refreshFragmentOrder = null;
        topFragmentOrder = null;
        scollviewFragmentOrder = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;

    }

    public FragmentOrder() {
    }

    /**
     * @param status         0---支出:预购+收入:无，
     *                       1---支出:待付款+收入:无，
     *                       2---支出:待服务+收入:已付款(待发货)，
     *                       3---支出:待收货+收入:已发货，
     *                       4---支出:待评价+收入:待评价，
     *                       5---支出:已完成+收入:已完成，
     *                       6---支出:待处理（已申请退货）+收入:待处理（已申请退货），
     *                       7---支出:退货+收入:退货，
     *                       8---支出:换货+收入:换货，
     *                       9---支出:已退款+收入:已退款,
     *                       10---支出:待服务+收入:待服务
     * @param mode           TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY = 0X0001;//买家版待处理(退换货状态下--支出)
     *                       TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER = 0X0002;//卖家版待处理(退换货状态下--收入)
     *                       TYPE_WAITING_FOR_SEND = 0X0003;//待发货(收入)
     *                       TYPE_WAITING_FOR_SERVICE_FROM_BUY = 0X0004;//买家版待服务(支出)
     *                       TYPE_WAITING_FOR_SERVICE_FROMSELLER = 0X0005;//卖家版待服务(收入)
     *                       TYPE_WAITING_FOR_COMMENT_FROM_BUY = 0X0006;//买家版待评价(支出)
     *                       TYPE_WAITING_FOR_COMMENT_FROMSELLER = 0X0007;//卖家版待评价(收入)
     *                       TYPE_WAITING_FOR_READY_BUY = 0X0008;//待预购(支出)
     *                       TYPE_WAITING_FOR_PAYMENT = 0X0009;//待付款(支出)
     *                       TYPE_WAITING_FOR_RECEIVING = 0X0010;//待收货(支出)
     *                       TYPE_WAITING_FOR_RETURNS_FROM_BUY = 0X0011;//买家版待退货(支出)
     *                       TYPE_WAITING_FOR_RETURNS_FROMSELLER = 0X0012;//卖家版待退货(收入)
     *                       TYPE_WAITING_FOR_CHANGE_FROM_BUY = 0X0013;//买家版待换换货(支出)
     *                       TYPE_WAITING_FOR_CHANGE_FROMSELLER = 0X0014;//卖家版待换换货(收入)
     *                       TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY = 0X00015;//买家版待处理(正常状态下--支出)
     *                       TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY = 0X00017;//买家版已完成(退换货状态下--支出)
     *                       TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_SELLER = 0X0018;//卖家版已完成(退换货状态下--收入)
     *                       TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY = 0X00019;//买家版已完成(正常状态下--支出)
     *                       TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_SELLER = 0X00020;//卖家版已完成(正常状态下--收入)
     * @param what           FragmentOrder.INCOME//收入
     *                       FragmentOrder.EXPEND//支出
     * @param isShowCheckBox 实否要显示CheckBox
     * @param isShowQR       实否要显示二维码
     */
    public FragmentOrder(String status, int mode, int what, boolean isShowCheckBox, boolean isShowQR) {
        this.status = status;
        this.mode = mode;
        this.what = what;
        this.isShowCheckBox = isShowCheckBox;
        this.isShowQR = isShowQR;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_order;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    @Override
    protected void stopLoad() {

    }

    public void changeList(List<String> goods_id, boolean isShowBottomPayment) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < goods_id.size(); j++) {
                if (list.get(i).data.get(j).goods_id.equals(goods_id)) {
                    list.get(i).data.remove(j);
                    ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), list, mode, isShowCheckBox, isShowQR);
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).data.size() == 0) {
                list.remove(i);
            }
        }
        if (isShowBottomPayment) {
            if (list.size() == 0) {
                allFragmentOrder.setVisibility(View.GONE);
            } else {
                allFragmentOrder.setVisibility(View.VISIBLE);
            }
        }
    }

    public void changeList(String order_id, boolean isShowBottomPayment) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).order_id.equals(order_id)) {
                list.remove(i);
                ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), list, mode, isShowCheckBox, isShowQR);
            }
        }
        if (isShowBottomPayment) {
            if (list.size() == 0) {
                allFragmentOrder.setVisibility(View.GONE);
            } else {
                allFragmentOrder.setVisibility(View.VISIBLE);
            }
        }
    }

    void init() {
        initHead();


        scollviewFragmentOrder.setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollviewFragmentOrder.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topFragmentOrder.setVisibility(View.VISIBLE);
                    } else {
                        topFragmentOrder.setVisibility(View.GONE);
                    }
                }
            });
        }
        topFragmentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scollviewFragmentOrder.smoothScrollTo(0, 0);
            }
        });

        if (mode == ListViewOrder.TYPE_WAITING_FOR_READY_BUY || mode == ListViewOrder.TYPE_WAITING_FOR_PAYMENT) {



            menuDialog = new MenuDialog(getActivity());
            cbFragmentOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).isChoose = isChecked;
                        for (int j = 0; j < list.get(i).data.size(); j++) {
                            list.get(i).data.get(j).isChoose = isChecked;
                        }
                    }
                    ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), list, mode, isShowCheckBox, isShowQR);
                    if (ListViewOrderFragmentOrder != null) {
                        ListViewOrderFragmentOrder.totalPrice();
                    }
                }
            });
            payFragmentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() == 0) {
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据错误");
                        return;
                    }
                    final JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isChoose){
                            jsonArray.put(list.get(i).order_id);
                        }
                    }
                    if (jsonArray.length()==0){
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "请选择付款订单");
                        return;
                    }
                    menuDialog.createPaymentDialog(ywLoadingDialog, "", getActivity(), new OnPayPasswordVerificationCallBack() {
                        @Override
                        public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                            P.r(ywLoadingDialog, Config.member_id, password, getActivity(), new P.r() {
                                @Override
                                public void match() {
                                    bottomSheetDialog.dismiss();
                                    passWordKeyboard.setStatus(true);
                                    sureOrderGoods(jsonArray);
                                }

                                @Override
                                public void mismatches() {
                                    passWordKeyboard.setStatus(false);
                                }
                            });
                        }

                    });
                }
            });
        }


        refreshFragmentOrder.init(new RefreshLoadMoreLayout.Config(this).canRefresh(true).canLoadMore(true));
        ListViewOrderFragmentOrder.setListViewOrderCallBack(this);
        request();
    }

    void initHead() {
        if (mode == ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_BUY || mode == ListViewOrder.TYPE_WAITING_FOR_COMMENT_FROM_SELLER) {
            headView = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_order_head, null);
            LinearLayout click_start_fragment_order_head = $.f(headView, R.id.click_start_fragment_order_head);
            final TextView show_start_fragment_order_head = $.f(headView, R.id.show_start_fragment_order_head);
            LinearLayout click_end_fragment_order_head = $.f(headView, R.id.click_end_fragment_order_head);
            final TextView show_end_fragment_order_head = $.f(headView, R.id.show_end_fragment_order_head);
            TextView click_check_end_fragment_order_head = $.f(headView, R.id.click_check_end_fragment_order_head);
            click_start_fragment_order_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = new DatePicker(getBaseFragmentActivityContext());
                    datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                    datePicker.setDateListener(new DatePicker.OnDateCListener() {
                        @Override
                        public void onDateSelected(String year, String month, String day) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                long a = simpleDateFormat.parse(year + "-" + month + "-" + day).getTime();
                                long b = new Date().getTime();
                                if (a > b) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您选择的时间不能大于当前时间");
                                    return;
                                }
                                start = year + "-" + month + "-" + day;
                                show_start_fragment_order_head.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                show_start_fragment_order_head.getPaint().setAntiAlias(true);//抗锯齿
                                show_start_fragment_order_head.setText(year + "-" + month + "-" + day);
                                show_start_fragment_order_head.setVisibility(View.VISIBLE);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    datePicker.show();
                }
            });

            click_end_fragment_order_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = new DatePicker(getBaseFragmentActivityContext());
                    datePicker.setMaxYear(Integer.valueOf(DateUtils.getNowYear()));
                    datePicker.setDateListener(new DatePicker.OnDateCListener() {
                        @Override
                        public void onDateSelected(String year, String month, String day) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                long a = simpleDateFormat.parse(year + "-" + month + "-" + day).getTime();
                                long b = new Date().getTime();
                                if (a > b) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您选择的时间不能大于当前时间");
                                    return;
                                }
                                end = year + "-" + month + "-" + day;
                                show_end_fragment_order_head.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                show_end_fragment_order_head.getPaint().setAntiAlias(true);//抗锯齿
                                show_end_fragment_order_head.setText(year + "-" + month + "-" + day);
                                show_end_fragment_order_head.setVisibility(View.VISIBLE);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    datePicker.show();
                }
            });
            click_check_end_fragment_order_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p = 1;
                    request();
                }
            });
        }
        ListViewOrderFragmentOrder.setHeadView(headView);

    }

    void request() {
        switch (what) {
            case INCOME://收入订单
                getOrderinfo();
                break;
            case EXPEND://支出订单
                getOrderinfoInto();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }

    /**
     * 我的收入订单
     */
    public void getOrderinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderinfo(
                    new JSONObject()
                            .put("p", p)
                            .put("beg", start)
                            .put("end", end)
                            .put("member_id", Config.member_id)//用户Id
                            .put("status", status)
                            .toString(), status + "- " +
                            "     *                       0---支出:预购+收入:无，\n" +
                            "     *                       1---支出:待付款+收入:无，\n" +
                            "     *                       2---支出:待收货+收入:已付款(待发货)，\n" +
                            "     *                       3---支出:待服务+收入:已发货，\n" +
                            "     *                       4---支出:待评价+收入:待评价，\n" +
                            "     *                       5---支出:已完成+收入:已完成，\n" +
                            "     *                       6---支出:待处理（已申请退货）+收入:待处理（已申请退货），\n" +
                            "     *                       7---支出:退货+收入:退货，\n" +
                            "     *                       8---支出:换货+收入:换货，\n" +
                            "     *                       9---支出:已退款+收入:已退款,\n" +
                            "     *                       10---支出:待服务+收入:待服务", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrder != null) {
                                refreshFragmentOrder.stopLoadMore();
                                refreshFragmentOrder.stopRefresh();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrder != null) {
                                refreshFragmentOrder.stopLoadMore();
                                refreshFragmentOrder.stopRefresh();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            OrderModel orderModel = new OrderModel();
                                            orderModel.order_id = jsonArray.getJSONObject(i).getString("order_id");
                                            orderModel.qr_code = jsonArray.getJSONObject(i).getString("qr_code");
                                            orderModel.order_code = jsonArray.getJSONObject(i).getString("order_code");
                                            orderModel.id = jsonArray.getJSONObject(i).getString("id");
                                            orderModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            orderModel.picture = jsonArray.getJSONObject(i).getString("picture");
                                            orderModel.name = jsonArray.getJSONObject(i).getString("name");
                                            orderModel.exchange_id = jsonArray.getJSONObject(i).getString("exchange_id");
                                            orderModel.target_id = jsonArray.getJSONObject(i).getString("target_id");
                                            orderModel.target_name = jsonArray.getJSONObject(i).getString("target_name");
                                            orderModel.exchange_status = jsonArray.getJSONObject(i).getString("exchange_status");
                                            orderModel.lng = jsonArray.getJSONObject(i).getString("lng");
                                            orderModel.lat = jsonArray.getJSONObject(i).getString("lat");
                                            List<OrderModel_goods_data> data = new ArrayList<>();
                                            if (jsonArray.getJSONObject(i).has("goods_data")) {
                                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("goods_data");
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    OrderModel_goods_data orderModel_goods_data = new OrderModel_goods_data();
                                                    orderModel_goods_data.goods_id = jsonArray1.getJSONObject(j).getString("goods_id");
                                                    orderModel_goods_data.number = jsonArray1.getJSONObject(j).getString("number");
                                                    orderModel_goods_data.title = jsonArray1.getJSONObject(j).getString("title");
                                                    orderModel_goods_data.slogan = jsonArray1.getJSONObject(j).getString("slogan");
                                                    orderModel_goods_data.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                    orderModel_goods_data.cost_price = jsonArray1.getJSONObject(j).getString("cost_price");
                                                    orderModel_goods_data.price = jsonArray1.getJSONObject(j).getString("price");
                                                    List<String> photo = new ArrayList<>();
                                                    photo.add("default");
                                                    orderModel_goods_data.photo = photo;
                                                    data.add(orderModel_goods_data);
                                                }
                                            }


                                            orderModel.data = data;

                                            list.add(orderModel);
                                        }
                                    }
                                    if (mode == ListViewOrder.TYPE_WAITING_FOR_READY_BUY || mode == ListViewOrder.TYPE_WAITING_FOR_PAYMENT) {
                                        allFragmentOrder.setVisibility(View.VISIBLE);
                                    }
                                    ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), list, mode, isShowCheckBox, isShowQR);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的支出订单
     */
    public void getOrderinfoInto() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderinfoInto(
                    new JSONObject()
                            .put("p", p)
                            .put("beg", start)
                            .put("end", end)
                            .put("member_id", Config.member_id)//用户Id
                            .put("status", status)
                            .toString(), status + "- 0---支出:预购+收入:无，\n" +
                            "     *                       1---支出:待付款+收入:无，\n" +
                            "     *                       2---支出:待收货+收入:已付款(待发货)，\n" +
                            "     *                       3---支出:待服务+收入:已发货，\n" +
                            "     *                       4---支出:待评价+收入:待评价，\n" +
                            "     *                       5---支出:已完成+收入:已完成，\n" +
                            "     *                       6---支出:待处理（已申请退货）+收入:待处理（已申请退货），\n" +
                            "     *                       7---支出:退货+收入:退货，\n" +
                            "     *                       8---支出:换货+收入:换货，\n" +
                            "     *                       9---支出:已退款+收入:已退款,\n" +
                            "     *                       10---支出:待服务+收入:待服务", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrder != null) {
                                refreshFragmentOrder.stopLoadMore();
                                refreshFragmentOrder.stopRefresh();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrder != null) {
                                refreshFragmentOrder.stopLoadMore();
                                refreshFragmentOrder.stopRefresh();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            OrderModel orderModel = new OrderModel();
                                            orderModel.order_id = jsonArray.getJSONObject(i).getString("order_id");
                                            orderModel.qr_code = jsonArray.getJSONObject(i).getString("qr_code");
                                            orderModel.order_code = jsonArray.getJSONObject(i).getString("order_code");
                                            orderModel.id = jsonArray.getJSONObject(i).getString("id");
                                            orderModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            orderModel.picture = jsonArray.getJSONObject(i).getString("picture");
                                            orderModel.name = jsonArray.getJSONObject(i).getString("name");
                                            orderModel.exchange_id = jsonArray.getJSONObject(i).getString("exchange_id");
                                            orderModel.target_id = jsonArray.getJSONObject(i).getString("target_id");
                                            orderModel.target_name = jsonArray.getJSONObject(i).getString("target_name");
                                            orderModel.exchange_status = jsonArray.getJSONObject(i).getString("exchange_status");
                                            orderModel.lng = jsonArray.getJSONObject(i).getString("lng");
                                            orderModel.lat = jsonArray.getJSONObject(i).getString("lat");
                                            List<OrderModel_goods_data> data = new ArrayList<>();
                                            if (jsonArray.getJSONObject(i).has("goods_data")) {
                                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("goods_data");
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    OrderModel_goods_data orderModel_goods_data = new OrderModel_goods_data();
                                                    orderModel_goods_data.goods_id = jsonArray1.getJSONObject(j).getString("goods_id");
                                                    orderModel_goods_data.number = jsonArray1.getJSONObject(j).getString("number");
                                                    orderModel_goods_data.title = jsonArray1.getJSONObject(j).getString("title");
                                                    orderModel_goods_data.slogan = jsonArray1.getJSONObject(j).getString("slogan");
                                                    orderModel_goods_data.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                    orderModel_goods_data.cost_price = jsonArray1.getJSONObject(j).getString("cost_price");
                                                    orderModel_goods_data.price = jsonArray1.getJSONObject(j).getString("price");
                                                    List<String> photo = new ArrayList<>();
                                                    photo.add("default");
                                                    orderModel_goods_data.photo = photo;
                                                    data.add(orderModel_goods_data);
                                                }
                                            }

                                            orderModel.data = data;

                                            list.add(orderModel);
                                        }
                                    }
                                    if (mode == ListViewOrder.TYPE_WAITING_FOR_READY_BUY || mode == ListViewOrder.TYPE_WAITING_FOR_PAYMENT) {
                                        allFragmentOrder.setVisibility(View.VISIBLE);
                                    }
                                    ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), list, mode, isShowCheckBox, isShowQR);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     *
     * @param targetOrderId
     */
    public void cancelOrder(final String targetOrderId) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cancelOrder(
                    new JSONObject()
                            .put("order_id", targetOrderId)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    changeList(targetOrderId, true);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     */
    public void sureOrderGoods(final JSONArray jsonArray) {

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.sureOrderGoods(
                    new JSONObject()
                            .put("order_id", jsonArray)
                            .put("password", password)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        changeList(jsonArray.getString(i), false);
                                        if (list.size()==0){
                                            allFragmentOrder.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 删除订单
     *
     * @param order_id
     */
    public void deleteOrder(final String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteOrder(
                    new JSONObject()
                            .put("order_id", order_id)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    changeList(order_id, false);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 立即发货
     */
    public void deliver(final String order_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        LogUtils.e(order_id);
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deliver(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("order_id", order_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    changeList(order_id, false);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onDispose(OrderModel orderModel, String targetPic, String targetName, String targetId, String exchange_id) {
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderDisposeSellerRightTopButtonDetails.class)
                    .putExtra("exchange_id", exchange_id)
                    .putExtra("what", what)
                    .putExtra("data", orderModel));
        }
    }

    @Override
    public void onConnectBuyer(String targetPic, String targetName, String targetId) {
        RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, targetId, targetName);

    }

    @Override
    public void onConnectSeller(String targetPic, String targetName, String targetId) {
        RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, targetId, targetName);
    }

    @Override
    public void onName(String targetPic, String targetName, String targetId) {
        switch (what) {
            case INCOME:
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", targetId));
                break;

            case EXPEND:
                if (getBaseFragmentActivityContext() != null) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", targetId));
                    }
                }
                break;
        }
    }

    @Override
    public void onQR(String QR) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityQRImage.class)
                    .putExtra("data", QR));
        }
    }

    @Override
    public void onClickFromGoods(String targetOrderId) {
        LogUtils.e(mode);
        if (getBaseFragmentActivityContext() != null) {
            String goods_or_service;
            if (mode == ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_BUY || mode == ListViewOrder.TYPE_WAITING_FOR_SERVICE_FROM_SELLER) {
                goods_or_service = "service";
            } else {
                goods_or_service = "goods";
            }
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderDetailsPublic.class)
                    .putExtra("mode", mode)
                    .putExtra("what", what)
                    .putExtra("goods_or_service", goods_or_service)
                    .putExtra("order_id", targetOrderId));
        }
    }

    @Override
    public void onCancelOrder(String targetOrderId, List<OrderModel> list) {
        cancelOrder(targetOrderId);
    }

    @Override
    public void onCommentOrder(String targetOrderId, String targetPic, List<OrderModel_goods_data> data) {
        if (targetPic == null) {//如果targetPic==null则说明是买家端反之,则是卖家版
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderCommentBuyer.class)
                        .putExtra("targetPic", targetPic)
                        .putExtra("targetOrderId", targetOrderId)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) data));
            }
        } else {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderCommentSeller.class)
                        .putExtra("targetPic", targetPic)
                        .putExtra("targetOrderId", targetOrderId));
            }

        }


    }

    @Override
    public void onLogistics(String targetOrderId) {

    }

    @Override
    public void onSend(final String targetOrderId) {
        /**
         * 立即发货输入密码
         */
        P.e(ywLoadingDialog, Config.member_id, getActivity(), new P.p() {
            @Override
            public void exist() {
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                menuDialog.createPasswordInputDialog("请输入您的支付密码", getActivity(), new OnPayPasswordVerificationCallBack() {
                    @Override
                    public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                        P.r(ywLoadingDialog, Config.member_id, password, getActivity(), new P.r() {
                            @Override
                            public void match() {
                                bottomSheetDialog.dismiss();
                                passWordKeyboard.setStatus(true);
                                deliver(targetOrderId);
                            }

                            @Override
                            public void mismatches() {

                                passWordKeyboard.setStatus(false);
                            }
                        });
                    }

                });
            }


            @Override
            public void nonexistence() {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataSetPassword.class));
                }
            }
        });
    }

    @Override
    public void onReturns(OrderModel orderModel, String targetId, String targetName) {
        int a = 0;
        for (int i = 0; i < orderModel.data.size(); i++) {
            if (orderModel.data.get(i).isChoose) {
                a++;
            }
        }
        if (a > 0) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderApplyForReturnsChangeRightTopButtonDetails.class)
                    .putExtra("data", orderModel)
                    .putExtra("targetId", targetId)
                    .putExtra("targetName", targetName)
                    .putExtra("order_id", orderModel.order_id));
        } else {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "请选择您要退换货的商品");
        }
//        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderApplyForReturnsChangeRightTopButtonDetails.class)
//                .putExtra("data", orderModel)
//                .putExtra("order_id", orderModel.order_id)
//                .putExtra("targetId", targetId)
//                .putExtra("targetName", targetName));
    }

    @Override
    public void onSureOrder(final String targetOrderId) {
        menuDialog.createPaymentDialog(ywLoadingDialog, "", getActivity(), new OnPayPasswordVerificationCallBack() {
            @Override
            public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard,final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                P.r(ywLoadingDialog, Config.member_id, password, getActivity(), new P.r() {
                    @Override
                    public void match() {
                        bottomSheetDialog.dismiss();
                        passWordKeyboard.setStatus(true);
                        final JSONArray jsonArray = new JSONArray();
                        jsonArray.put(targetOrderId);
                        sureOrderGoods(jsonArray);
                    }

                    @Override
                    public void mismatches() {
                        passWordKeyboard.setStatus(false);
                    }
                });
            }
        });


    }

    @Override
    public void onDeleteOrder(String targetOrderId) {
        deleteOrder(targetOrderId);
    }

    @Override
    public void onNavigation(String targetOrderId, String lat, String lng) {

    }

    @Override
    public void onCompleteService(String targetOrderId, int price) {

    }

    @Override
    public void onCompleteData(OrderModel orderModel, String targetId, String targetName, String exchange_id) {
        String type = "";
        if (mode == ListViewOrder.TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY) {//1买家完善退货资料
            type = "1";
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderCompleteDataBuyerRightTopButtonDetails.class)
                    .putExtra("data", orderModel)
                    .putExtra("type", type)
                    .putExtra("what", what)
                    .putExtra("status", status)
                    .putExtra("order_id", orderModel.order_id)
                    .putExtra("targetId", targetId)
                    .putExtra("exchange_id", exchange_id)
                    .putExtra("targetName", targetName));
        } else if (mode == ListViewOrder.TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY) {//2买家完善换货资料
            type = "2";
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderCompleteDataBuyerRightTopButtonDetails.class)
                    .putExtra("data", orderModel)
                    .putExtra("type", type)
                    .putExtra("what", what)
                    .putExtra("status", status)
                    .putExtra("exchange_id", exchange_id)
                    .putExtra("order_id", orderModel.order_id)
                    .putExtra("targetId", targetId)
                    .putExtra("targetName", targetName));
        }
//        String type="4";
//        if (mode == ListViewOrder.TYPE_WAITING_FOR_RETURNS_FROM_BUY) {//1退货
//            type = "1";
//        } else if (mode == ListViewOrder.TYPE_WAITING_FOR_CHANGE_FROM_BUY) {//2换货
//            type = "2";
//        }else if (mode==ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY){//4完善资料
//            type="4";
//        }else {
//            type="3";//申请
//        }

//        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityOrderReturnsChangeApplyForAndCompleteData.class)
//                .putExtra("data", orderModel)
//                .putExtra("what", what)
//                .putExtra("status",status)
//                .putExtra("type", type)
//                .putExtra("order_id", orderModel.order_id)
//                .putExtra("targetId", targetId)
//                .putExtra("isShowclickRefund", false)
//                .putExtra("targetName", targetName));
    }

    @Override
    public void onTotalPrice(int price) {
        priceFragmentOrder.setText("¥" + price);
    }

    @Override
    public void onCheckedChangedFromOutter(List<OrderModel> list, int i, boolean isChecked) {
        this.list = list;
        ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), this.list, mode, isShowCheckBox, isShowQR);
        if (ListViewOrderFragmentOrder != null) {
            ListViewOrderFragmentOrder.totalPrice();
        }
    }

    @Override
    public void onCheckedChangedFromInner(List<OrderModel> list, int i, int position, boolean isChecked) {
        this.list = list;
        ListViewOrderFragmentOrder.setList(getBaseFragmentActivityContext(), this.list, mode, isShowCheckBox, isShowQR);
        if (ListViewOrderFragmentOrder != null) {
            ListViewOrderFragmentOrder.totalPrice();
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        request();
    }

    @Override
    public void onLoadMore() {
        request();
    }
}
