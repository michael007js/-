package com.sss.car.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CouponChange;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.model.CouponGetModel_List;
import com.sss.car.model.CouponModel;
import com.sss.car.model.CouponModel2;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;
import com.sss.car.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;

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

import static android.R.attr.data;


/**
 * 优惠券详情
 * Created by leilei on 2017/9/23.
 */

@SuppressWarnings("ALL")
public class ActivityCoupon extends BaseActivity {
    @BindView(R.id.back_activity_goods_service_details)
    LinearLayout backActivityGoodsServiceDetails;
    @BindView(R.id.goods_name_activity_goods_service_details)
    TextView goodsNameActivityGoodsServiceDetails;
    @BindView(R.id.click_shop_car_activity_goods_service_details)
    LinearLayout clickShopCarActivityGoodsServiceDetails;
    @BindView(R.id.more_activity_goods_service_details)
    ImageView moreActivityGoodsServiceDetails;
    @BindView(R.id.listview_activity_coupon)
    InnerListview listviewActivityCoupon;
    @BindView(R.id.refresh_activity_coupon)
    PullToRefreshScrollView refreshActivityCoupon;
    @BindView(R.id.top_activity_coupon)
    ImageView topActivityCoupon;
    @BindView(R.id.activity_coupon)
    LinearLayout activityCoupon;
    @BindView(R.id.buy_activity_coupon)
    TextView buyActivityCoupon;
    YWLoadingDialog ywLoadingDialog;


    MenuDialog menuDialog;

    SSS_Adapter sss_adapter;


    List<CouponModel2> list = new ArrayList<>();
    int p = 1;


    View view;
    TextView discounts_activity_coupon_head;
    LinearLayout click_share;
    SimpleDraweeView pic_activity_coupon_head;
    TextView type_activity_coupon_head;
    TextView date_activity_coupon_head;
    TextView name_activity_coupon_head;
    TextView price_activity_coupon_head;
    TextView explain_activity_coupon_head;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        clickShopCarActivityGoodsServiceDetails = null;
        goodsNameActivityGoodsServiceDetails = null;
        backActivityGoodsServiceDetails = null;
        moreActivityGoodsServiceDetails = null;
        discounts_activity_coupon_head = null;
        explain_activity_coupon_head = null;
        price_activity_coupon_head = null;
        name_activity_coupon_head = null;
        type_activity_coupon_head = null;
        date_activity_coupon_head = null;
        pic_activity_coupon_head = null;
        listviewActivityCoupon = null;
        refreshActivityCoupon = null;
        topActivityCoupon = null;
        buyActivityCoupon = null;
        activityCoupon = null;
        view = null;


        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);


        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }

        customInit(activityCoupon, false, true, false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            listviewActivityCoupon.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topActivityCoupon.setVisibility(View.VISIBLE);
                    } else {
                        topActivityCoupon.setVisibility(View.GONE);
                    }
                }
            });
        }


        if ("coupon_manager".equals(getIntent().getExtras().getString("mode"))) {
            buyActivityCoupon.setVisibility(View.GONE);
        }
        if ("details_from_my".equals(getIntent().getExtras().getString("mode"))) {
            clickShopCarActivityGoodsServiceDetails.setVisibility(View.GONE);
            buyActivityCoupon.setText("立即加入");
        }

        if ("FragmentCouponSaleSeckillDiscounts".equals(getIntent().getExtras().getString("mode"))) {
            clickShopCarActivityGoodsServiceDetails.setVisibility(View.GONE);
            buyActivityCoupon.setText("立即购买");
        }

        initClick();

        refreshActivityCoupon.setMode(PullToRefreshBase.Mode.BOTH);
        refreshActivityCoupon.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                couponShop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                couponShop();
            }
        });
        initHeadAndAdapter();
        couponDetails();


    }


    void initClick() {
        if ("details".equals(getIntent().getExtras().getString("mode"))) {
            buyActivityCoupon.setText("立即购买");
            buyActivityCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(price_activity_coupon_head.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "优惠券信息获取中...");
                        return;
                    }
                    PayUtils.requestPayment(ywLoadingDialog,false,"0", getIntent().getExtras().getString("coupon_id"),
                            3, 0, PriceUtils.formatBy2Scale(Double.valueOf(price_activity_coupon_head.getText().toString().trim()), 2), getBaseActivity(),null,"0");
                }
            });
        } else if ("returns".equals(getIntent().getExtras().getString("mode"))) {
            buyActivityCoupon.setText("立即退款");
            buyActivityCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APPOftenUtils.createAskDialog(getBaseActivityContext(), "确认要立即退款吗？", new OnAskDialogCallBack() {
                        @Override
                        public void onOKey(Dialog dialog) {
                            dialog.dismiss();
                            dialog=null;
                            if (menuDialog == null) {
                                menuDialog = new MenuDialog(getBaseActivity());
                            }

                            /**
                             * 输入密码
                             */
                            P.e(ywLoadingDialog, Config.member_id, getBaseActivity(), new P.p() {
                                @Override
                                public void exist() {
                                    if (menuDialog == null) {
                                        menuDialog = new MenuDialog(getBaseActivity());
                                    }
                                    menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                                        @Override
                                        public void onVerificationPassword(String password, final PassWordKeyboard passWordKeyboard, final com.rey.material.app.BottomSheetDialog bottomSheetDialog) {
                                            P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                                                @Override
                                                public void match() {
                                                    bottomSheetDialog.dismiss();
                                                    passWordKeyboard.setStatus(true);
                                                    returnsCoupon();
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
                                    if (getBaseActivityContext() != null) {
                                        startActivity(new Intent(getBaseActivityContext(), ActivityMyDataSetPassword.class));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancel(Dialog dialog) {
                            dialog.dismiss();
                            dialog=null;
                        }
                    });
                }
            });
        } else if ("details_from_my".equals(getIntent().getExtras().getString("mode"))) {
            buyActivityCoupon.setText("立即加入");
            buyActivityCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinCoupon();
                }
            });
        } else if ("FragmentCouponSaleSeckillDiscounts".equals(getIntent().getExtras().getString("mode"))) {
            buyActivityCoupon.setText("立即购买");
            buyActivityCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(price_activity_coupon_head.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "优惠券信息获取中...");
                        return;
                    }
                    String a=price_activity_coupon_head.getText().toString().trim();
                    double b=Double.valueOf(a);
                    PayUtils.requestPayment(ywLoadingDialog,false,"0", getIntent().getExtras().getString("coupon_id"), 3, 0, PriceUtils.formatBy2Scale(b, 2)
                            , getBaseActivity(),null,"0");
                }
            });
        }
    }

    @OnClick({R.id.back_activity_goods_service_details, R.id.click_shop_car_activity_goods_service_details, R.id.more_activity_goods_service_details, R.id.top_activity_coupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_goods_service_details:
                finish();
                break;
            case R.id.click_shop_car_activity_goods_service_details:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShoppingCart.class));
                }
                break;
            case R.id.more_activity_goods_service_details:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                menuDialog.createGoodsMenu(moreActivityGoodsServiceDetails, getBaseActivity(), "coupon", getIntent().getExtras().getString("coupon_id"));
                break;
            case R.id.top_activity_coupon:
                listviewActivityCoupon.smoothScrollToPosition(0);
                break;
        }
    }

    void initHeadAndAdapter() {

        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_coupon_head, null);
        discounts_activity_coupon_head = $.f(view, R.id.discounts_activity_coupon_head);
        click_share = $.f(view, R.id.click_share);
        pic_activity_coupon_head = $.f(view, R.id.pic_activity_coupon_head);
        type_activity_coupon_head = $.f(view, R.id.type_activity_coupon_head);
        date_activity_coupon_head = $.f(view, R.id.date_activity_coupon_head);
        name_activity_coupon_head = $.f(view, R.id.name_activity_coupon_head);
        price_activity_coupon_head = $.f(view, R.id.price_activity_coupon_head);
        explain_activity_coupon_head = $.f(view, R.id.explain_activity_coupon_head);
        click_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.prepareShare(ywLoadingDialog, getBaseActivity(), "coupon", getIntent().getExtras().getString("coupon_id"));
            }
        });
        sss_adapter = new SSS_Adapter<CouponModel2>(getBaseActivityContext(), R.layout.item_coupon) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CouponModel2 bean, SSS_Adapter instance) {
                addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.logo),
                        ((SimpleDraweeView) helper.getView(R.id.pic_item_coupon)), 10f));
                helper.setText(R.id.name_item_coupon, bean.name);
                helper.setText(R.id.path_item_coupon, bean.address);
                helper.setText(R.id.distance_item_coupon, bean.distance);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_coupon);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_coupon:
                        if ("1".equals(list.get(position).scope)){
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                        .putExtra("shop_id", list.get(position).shop_id));
                            }
                        }else   if ("2".equals(list.get(position).scope)){
                            startActivity(new Intent(getBaseActivityContext(), ActivityShopIinfoAllFilterLaterList.class)
                                    .putExtra("name", list.get(position).classify_name)
                                    .putExtra("classify_id",list.get(position).classify_id)
                            );
                        }else   if ("3".equals(list.get(position).scope)){
                            startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                    .putExtra("goods_id", list.get(position).goods_id)
                                    .putExtra("type", list.get(position).goods_type));
                        }



                        break;
                }
            }
        });
        listviewActivityCoupon.setAdapter(sss_adapter);
        listviewActivityCoupon.addHeaderView(view);
    }

    /**
     * 优惠券详情
     */
    public void couponDetails() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.couponDetails(
                    new JSONObject()
                            .put("id", getIntent().getExtras().getString("coupon_id"))
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
                                    discounts_activity_coupon_head.setText(jsonObject.getJSONObject("data").getString("money"));
                                    date_activity_coupon_head.setText(jsonObject.getJSONObject("data").getString("duration"));
                                    name_activity_coupon_head.setText(jsonObject.getJSONObject("data").getString("name"));
                                    price_activity_coupon_head.setText(jsonObject.getJSONObject("data").getString("sell_price"));
                                    explain_activity_coupon_head.setText(jsonObject.getJSONObject("data").getString("describe"));
                                    switch ((jsonObject.getJSONObject("data").getString("type"))) {//1满减券，2现金券，3折扣券
                                        case "1":
                                            type_activity_coupon_head.setText("满减券");
                                            break;
                                        case "2":
                                            type_activity_coupon_head.setText("现金券");
                                            break;
                                        case "3":
                                            type_activity_coupon_head.setText("折扣券");
                                            break;
                                    }
                                    couponShop();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取加入优惠券店铺
     */
    public void couponShop() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.couponShop(
                    new JSONObject()
                            .put("coupon_id", getIntent().getExtras().getString("coupon_id"))
                            .put("p", p)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshActivityCoupon != null) {
                                refreshActivityCoupon.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshActivityCoupon != null) {
                                refreshActivityCoupon.onRefreshComplete();
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
                                            CouponModel2 couponModel = new CouponModel2();
                                            couponModel.shop_id = jsonArray.getJSONObject(i).getString("shop_id");
                                            couponModel.name = jsonArray.getJSONObject(i).getString("name");
                                            couponModel.logo = jsonArray.getJSONObject(i).getString("logo");
                                            couponModel.address = jsonArray.getJSONObject(i).getString("address");
                                            couponModel.lng = jsonArray.getJSONObject(i).getString("lng");
                                            couponModel.lat = jsonArray.getJSONObject(i).getString("lat");
                                            couponModel.distance = jsonArray.getJSONObject(i).getString("distance");
                                            couponModel. scope= jsonArray.getJSONObject(i).getString("scope");
                                            couponModel. classify_id= jsonArray.getJSONObject(i).getString("classify_id");
                                            couponModel. classify_name= jsonArray.getJSONObject(i).getString("classify_name");
                                            couponModel. goods_id= jsonArray.getJSONObject(i).getString("goods_id");
                                            couponModel.goods_type= jsonArray.getJSONObject(i).getString("goods_type");
                                            list.add(couponModel);
                                        }

                                        sss_adapter.setList(list);
                                    }


                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }

    /**
     * 优惠券购买
     */
    public void buyCoupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.buyCoupon(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("coupon_id", getIntent().getExtras().getString("coupon_id"))//这里获取的是coupon_id
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new CouponChange());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }

    /**
     * 退还优惠券
     */
    public void returnsCoupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.returnsCoupon(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("id", getIntent().getExtras().getString("id"))//这里获取的是ID
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    EventBus.getDefault().post(new CouponChange());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }


    /**
     * 获取平台优惠券==>加入优惠券
     */
    public void joinCoupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.joinCoupon(
                    new JSONObject()
                            .put("coupon_id", getIntent().getExtras().getString("coupon_id"))
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
                                    finish();
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
