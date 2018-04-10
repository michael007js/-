package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerViewWithDay;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ViewPagerObjAdpter;
import com.sss.car.dao.IsCollectCallBack;
import com.sss.car.dao.OnFragmentGoodsServiceDetailsGoodsCallBack;
import com.sss.car.dao.UserCallBack;
import com.sss.car.model.ClassifyDataModel;
import com.sss.car.model.CouponModel;
import com.sss.car.model.GoodsDetailsModel;
import com.sss.car.model.GoodsDetailsShopModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.ShareUtils;
import com.sss.car.view.ActivityCoupon;
import com.sss.car.view.ActivityGoodsList;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityGoodsServiceListPublic;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityShoppingCart;
import com.sss.car.view.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 车品车服商品页fragment
 * Created by leilei on 2017/9/14.
 */

@SuppressLint("ValidFragment")
public class FragmentGoodsServiceDetailsGoods extends BaseFragment {
    @BindView(R.id.viewpager_fragment_goods_service_detalis_goods)
    ViewPager viewpagerFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.title_fragment_goods_service_detalis_goods)
    TextView titleFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.solger_fragment_goods_service_detalis_goods)
    TextView solgerFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.price_fragment_goods_service_detalis_goods)
    TextView priceFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.praise_fragment_goods_service_detalis_goods)
    SimpleDraweeView praiseFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.priase_number_fragment_goods_service_detalis_goods)
    TextView priaseNumberFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.click_praise_fragment_goods_service_detalis_goods)
    LinearLayout clickPraiseFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.share_fragment_goods_service_detalis_goods)
    SimpleDraweeView shareFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.share_number_fragment_goods_service_detalis_goods)
    TextView shareNumberFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.click_share_fragment_goods_service_detalis_goods)
    LinearLayout clickShareFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.click_shop_fragment_goods_service_detalis_goods)
    LinearLayout clickShopFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.pic_fragment_goods_service_detalis_goods)
    SimpleDraweeView picFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.shop_name_fragment_goods_service_detalis_goods)
    TextView shopNameFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.auth_type_fragment_goods_service_detalis_goods)
    TextView authTypeFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.login_time_fragment_goods_service_detalis_goods)
    TextView loginTimeFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.open_time_fragment_goods_service_detalis_goods)
    TextView openTimeFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.score_fragment_goods_service_detalis_goods)
    TextView scoreFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.location_path_fragment_goods_service_detalis_goods)
    TextView locationPathFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.distance_fragment_goods_service_detalis_goods)
    TextView distanceFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.click_size_fragment_goods_service_detalis_goods)
    LinearLayout clickSizeFragmentGoodsServiceDetalisGoods;
    @BindView(R.id.click_coupon_fragment_goods_service_detalis_goods)
    LinearLayout clickCouponFragmentGoodsServiceDetalisGoods;
    Unbinder unbinder;
    UserCallBack userCallBack;
    IsCollectCallBack isCollectCallBack;

    MenuDialog menuDialog;

    String goods_id;
    OnFragmentGoodsServiceDetailsGoodsCallBack onFragmentGoodsServiceDetailsGoodsCallBack;

    List<CouponModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;

    ActivityGoodsServiceDetails.Shop_IdCallBack shop_idCallBack;

    GoodsDetailsModel goodsDetailsModel = new GoodsDetailsModel();

    GoodsDetailsShopModel goodsDetailsShopModel = new GoodsDetailsShopModel();
    boolean stop;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.parent_banner)
    FrameLayout parentBanner;
    @BindView(R.id.unread)
    TextView unread;
    @BindView(R.id.top_shop)
    RelativeLayout topShop;
    @BindView(R.id.logo_cart)
    SimpleDraweeView logoCart;
    @BindView(R.id.jump)
    HorizontalListView jump;

    List<ClassifyDataModel> ClassifyDataModelList = new ArrayList<>();
    SSS_Adapter classifyDataModelListAdapter;
    @BindView(R.id.goods_code)
    TextView goodsCode;
    @BindView(R.id.count_time)
    SecondDownTimerViewWithDay countTime;
    @BindView(R.id.parent_count_time)
    LinearLayout parentCountTime;
    @BindView(R.id.count_name)
    TextView countName;

    public FragmentGoodsServiceDetailsGoods() {
    }

    public FragmentGoodsServiceDetailsGoods(boolean stop, String goods_id, ActivityGoodsServiceDetails.Shop_IdCallBack shop_idCallBack, UserCallBack userCallBack, IsCollectCallBack isCollectCallBack, OnFragmentGoodsServiceDetailsGoodsCallBack onFragmentGoodsServiceDetailsGoodsCallBack) {
        this.stop = stop;
        this.goods_id = goods_id;
        this.shop_idCallBack = shop_idCallBack;
        this.userCallBack = userCallBack;
        this.isCollectCallBack = isCollectCallBack;
        this.onFragmentGoodsServiceDetailsGoodsCallBack = onFragmentGoodsServiceDetailsGoodsCallBack;
    }

    @Override
    public void onDestroy() {
        if (countTime != null) {
            countTime.cancelDownTimer();
        }
        countTime = null;
        if (classifyDataModelListAdapter != null) {
            classifyDataModelListAdapter.clear();
        }
        classifyDataModelListAdapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        unread = null;
        topShop = null;
        logoCart = null;
        viewpagerFragmentGoodsServiceDetalisGoods = null;
        titleFragmentGoodsServiceDetalisGoods = null;
        solgerFragmentGoodsServiceDetalisGoods = null;
        priceFragmentGoodsServiceDetalisGoods = null;
        praiseFragmentGoodsServiceDetalisGoods = null;
        priaseNumberFragmentGoodsServiceDetalisGoods = null;
        clickPraiseFragmentGoodsServiceDetalisGoods = null;
        shareFragmentGoodsServiceDetalisGoods = null;
        shareNumberFragmentGoodsServiceDetalisGoods = null;
        clickShareFragmentGoodsServiceDetalisGoods = null;
        clickShopFragmentGoodsServiceDetalisGoods = null;
        picFragmentGoodsServiceDetalisGoods = null;
        shopNameFragmentGoodsServiceDetalisGoods = null;
        authTypeFragmentGoodsServiceDetalisGoods = null;
        loginTimeFragmentGoodsServiceDetalisGoods = null;
        openTimeFragmentGoodsServiceDetalisGoods = null;
        scoreFragmentGoodsServiceDetalisGoods = null;
        locationPathFragmentGoodsServiceDetalisGoods = null;
        distanceFragmentGoodsServiceDetalisGoods = null;
        clickSizeFragmentGoodsServiceDetalisGoods = null;
        clickCouponFragmentGoodsServiceDetalisGoods = null;
        goodsCode = null;countName=null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_detalis_goods;
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
                                menuDialog = new MenuDialog(getActivity());
                                countTime.setDownTimerListener(new OnCountDownTimerListener() {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        parentCountTime.setVisibility(View.GONE);
                                    }
                                });
                                parentBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getWindowManager().getDefaultDisplay().getWidth()));
                                initAdapter();
                                goods_details();
                                getShoppingCart();
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


    void initAdapter() {
        sss_adapter = new SSS_Adapter<CouponModel>(getBaseFragmentActivityContext(), R.layout.item_bottom_coupon_adapter) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CouponModel bean, SSS_Adapter instance) {
                helper.setText(R.id.price_item_bottom_coupon_adapter, "¥" + bean.money);
                switch (bean.type) {//1满减券，2现金券，3折扣券
                    case "1":
                        helper.setText(R.id.condition_item_bottom_coupon_adapter, "满减券");
                        break;
                    case "2":
                        helper.setText(R.id.condition_item_bottom_coupon_adapter, "现金券");
                        break;
                    case "3":
                        helper.setText(R.id.condition_item_bottom_coupon_adapter, "折扣券");
                        break;
                }

                helper.setText(R.id.date_item_bottom_coupon_adapter, bean.duration);

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.details_item_bottom_coupon_adapter);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.details_item_bottom_coupon_adapter:
                        if (!stop) {
                            if (getBaseFragmentActivityContext() != null) {
                                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCoupon.class)
                                        .putExtra("coupon_id", list.get(position).coupon_id)
                                        .putExtra("money", list.get(position).money)
                                        .putExtra("mode", "details")
                                );
                            }
                        }
                        break;
                }
            }
        });
        classifyDataModelListAdapter = new SSS_Adapter<ClassifyDataModel>(getBaseFragmentActivityContext(), R.layout.item_classify_data) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ClassifyDataModel bean, SSS_Adapter instance) {
                if (position != ClassifyDataModelList.size() - 1) {
                    helper.setText(R.id.title_class, bean.name + " > ");
                } else {
                    helper.setText(R.id.title_class, bean.name);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        jump.setAdapter(classifyDataModelListAdapter);
        jump.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(ClassifyDataModelList.get(position).level)) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), Main.class));
                } else if ("2".equals(ClassifyDataModelList.get(position).level)) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), Main.class));
                } else if ("3".equals(ClassifyDataModelList.get(position).level)) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
                            .putExtra("title", ClassifyDataModelList.get(position).name)
                            .putExtra("type", ClassifyDataModelList.get(position).type)
                            .putExtra("classify_id", ClassifyDataModelList.get(position).classify_id));
                } else if ("4".equals(ClassifyDataModelList.get(position).level)) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceListPublic.class)
                            .putExtra("classify_id", ClassifyDataModelList.get(position).classify_id)
                            .putExtra("showHead", false)
                            .putExtra("type", ClassifyDataModelList.get(position).type)
                            .putExtra("title", ClassifyDataModelList.get(position).name));
                }
            }
        });
    }

    @OnClick({R.id.top_shop, R.id.click_praise_fragment_goods_service_detalis_goods, R.id.click_share_fragment_goods_service_detalis_goods, R.id.click_shop_fragment_goods_service_detalis_goods, R.id.click_size_fragment_goods_service_detalis_goods, R.id.click_coupon_fragment_goods_service_detalis_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_shop:
                if (!stop) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShoppingCart.class));
                    }
                }

                break;
            case R.id.click_praise_fragment_goods_service_detalis_goods:
                if (!stop) {
                    praise(goods_id);
                }
                break;
            case R.id.click_share_fragment_goods_service_detalis_goods:
                if (!stop) {
                    ShareUtils.prepareShare(ywLoadingDialog, getActivity(), "goods", goodsDetailsModel.goods_id);
                }
                break;
            case R.id.click_shop_fragment_goods_service_detalis_goods:
                if (!stop) {
                    if (getBaseFragmentActivityContext() != null && goodsDetailsShopModel != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", goodsDetailsShopModel.shop_id)
                        );
                    }
                }
                break;
            case R.id.click_size_fragment_goods_service_detalis_goods:
                break;
            case R.id.click_coupon_fragment_goods_service_detalis_goods:
                if (!stop) {
                    if (sss_adapter == null) {
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "正在加载优惠券...");
                        return;
                    }
                    if (list.size() < 1) {
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "该店铺没有优惠券");
                    } else {
                        menuDialog.createGoodsBottomCouponDialog(getBaseFragmentActivityContext(), sss_adapter);
                    }
                }
                break;
        }
    }

    /**
     * 商品点赞
     */
    public void praise(String goods_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.praise(
                    new JSONObject()
                            .put("type", "goods")//trends:动态点赞
                            .put("likes_id", goods_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        goods_details();
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取购物车数量
     */
    public void getShoppingCart() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cart_num(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    int a = jsonObject.getJSONObject("data").getInt("rows");
                                    if (a > 0) {
                                        unread.setText(Integer.toString(a));
                                        unread.setVisibility(View.VISIBLE);
                                    } else {
                                        unread.setText("0");
                                        unread.setVisibility(View.GONE);
                                    }

                                } else {
                                    unread.setText("0");
                                    unread.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }

    /**
     * 商品详情信息
     */
    public void goods_details() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_details(
                    new JSONObject()
                            .put("goods_id", goods_id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
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
                                    goodsDetailsModel.goods_id = jsonObject.getJSONObject("data").getString("goods_id");
                                    goodsDetailsModel.slogan = jsonObject.getJSONObject("data").getString("slogan");
                                    goodsDetailsModel.is_like = jsonObject.getJSONObject("data").getString("is_like");
                                    goodsDetailsModel.is_collect = jsonObject.getJSONObject("data").getString("is_collect");
                                    goodsDetailsModel.title = jsonObject.getJSONObject("data").getString("title");
                                    goodsDetailsModel.attr_data = jsonObject.getJSONObject("data").getString("attr_data"); // 商品属性
                                    goodsDetailsModel.cost_price = jsonObject.getJSONObject("data").getString("cost_price"); // 原价
                                    goodsDetailsModel.price = jsonObject.getJSONObject("data").getString("price"); // 现价
                                    goodsDetailsModel.price_type = jsonObject.getJSONObject("data").getString("price_type"); // 价格类型
                                    goodsDetailsModel.start_time = jsonObject.getJSONObject("data").getString("start_time"); // 开始时间
                                    goodsDetailsModel.end_time = jsonObject.getJSONObject("data").getString("end_time"); // 结束时间
                                    goodsDetailsModel.likes = jsonObject.getJSONObject("data").getString("likes"); // 收藏数量
                                    goodsDetailsModel.share = jsonObject.getJSONObject("data").getString("share"); // 分享数量
                                    goodsDetailsModel.sell = jsonObject.getJSONObject("data").getString("sell"); // 销量
                                    goodsDetailsModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    goodsDetailsModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
//                                    goodsDetailsModel.classify_name = jsonObject.getJSONObject("data").getString("classify_name");
                                    goodsDetailsModel.master_map = jsonObject.getJSONObject("data").getString("master_map");
                                    goodsCode.setText("商品编码：" + jsonObject.getJSONObject("data").getString("goods_code"));

                                    if ("1".equals(jsonObject.getJSONObject("data").getString("is_activity"))) {
                                        countName.setText(jsonObject.getJSONObject("data").getString("count_name"));
                                        parentCountTime.setVisibility(View.VISIBLE);
                                        countTime.setDownTime((long) (jsonObject.getJSONObject("data").getInt("count_time") * 1000));
                                        countTime.startDownTimer();
                                    } else {
                                        parentCountTime.setVisibility(View.GONE);
                                    }


                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    if (onFragmentGoodsServiceDetailsGoodsCallBack != null) {
                                        onFragmentGoodsServiceDetailsGoodsCallBack.onPrice(goodsDetailsModel.price);
                                    }
                                    if (jsonArray.length() > 0) {
                                        List<String> picture = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            picture.add(jsonArray.getString(i));
                                        }
                                        goodsDetailsModel.picture = picture;
                                    }
                                    if (isCollectCallBack != null) {
                                        if ("1".equals(goodsDetailsModel.is_collect)) {
                                            isCollectCallBack.onIsCollectCallBack(true);
                                        } else if ("0".equals(goodsDetailsModel.is_collect)) {
                                            isCollectCallBack.onIsCollectCallBack(false);
                                        }
                                    }
                                    ClassifyDataModelList.clear();
                                    JSONArray jsonArray1 = jsonObject.getJSONObject("data").getJSONArray("classify_data");
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        ClassifyDataModel classifyDataModel = new ClassifyDataModel();
                                        classifyDataModel.classify_id = jsonArray1.getJSONObject(i).getString("classify_id");
                                        classifyDataModel.name = jsonArray1.getJSONObject(i).getString("name");
                                        classifyDataModel.type = jsonArray1.getJSONObject(i).getString("type");
                                        classifyDataModel.level = jsonArray1.getJSONObject(i).getString("level");
                                        ClassifyDataModelList.add(classifyDataModel);
                                    }

                                    showGoodsData();
                                    classifyDataModelListAdapter.setList(ClassifyDataModelList);
                                    goods_shop();


                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }

    /**
     * 显示商品信息
     */
    void showGoodsData() {
        if ("1".equals(goodsDetailsModel.is_like)) {
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_yes), praiseFragmentGoodsServiceDetalisGoods, 0f));
        } else {
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_no), praiseFragmentGoodsServiceDetalisGoods, 0f));
        }
        addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_share), shareFragmentGoodsServiceDetalisGoods, 0f));
        titleFragmentGoodsServiceDetalisGoods.setText(goodsDetailsModel.title);
        solgerFragmentGoodsServiceDetalisGoods.setText(goodsDetailsModel.slogan);
        if ("3".equals(goodsDetailsModel.price_type)){
            priceFragmentGoodsServiceDetalisGoods.setText("面议");
        }else {
            priceFragmentGoodsServiceDetalisGoods.setText("¥" + goodsDetailsModel.price);
        }

        priaseNumberFragmentGoodsServiceDetalisGoods.setText(goodsDetailsModel.likes);
        shareNumberFragmentGoodsServiceDetalisGoods.setText(goodsDetailsModel.share);

        final List<View> list = new ArrayList<>();
        for (int i = 0; i < goodsDetailsModel.picture.size(); i++) {
            View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.item_fragment_goods_service_details_pic, null);
            SimpleDraweeView pic_item_fragment_goods_service_details_pic = $.f(view, R.id.pic_item_fragment_goods_service_details_pic);
            addImageViewList(FrescoUtils.showImage(false, getActivity().getWindowManager().getDefaultDisplay().getWidth(), getActivity().getWindowManager().getDefaultDisplay().getWidth(), Uri.parse(Config.url + goodsDetailsModel.picture.get(i)), pic_item_fragment_goods_service_details_pic, 0f));
            list.add(view);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBaseFragmentActivityContext() != null) {

                        List<String> temp=new ArrayList<>();
                        for (int j = 0; j < goodsDetailsModel.picture.size(); j++) {
                            temp.add(Config.url+goodsDetailsModel.picture.get(j));
                        }
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", finalI));
                    }
                }
            });
        }
        viewpagerFragmentGoodsServiceDetalisGoods.setAdapter(new ViewPagerObjAdpter(list));
    }


    /**
     * 获取店铺信息
     */
    public void goods_shop() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_shop(
                    new JSONObject()
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("shop_id", goodsDetailsModel.shop_id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
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
                                    goodsDetailsShopModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    goodsDetailsShopModel.name = jsonObject.getJSONObject("data").getString("name");
                                    goodsDetailsShopModel.logo = jsonObject.getJSONObject("data").getString("logo");
                                    goodsDetailsShopModel.address = jsonObject.getJSONObject("data").getString("address");
                                    goodsDetailsShopModel.business_hours = jsonObject.getJSONObject("data").getString("business_hours");
                                    goodsDetailsShopModel.credit = jsonObject.getJSONObject("data").getString("credit");
                                    goodsDetailsShopModel.is_auth = jsonObject.getJSONObject("data").getString("is_auth");
                                    goodsDetailsShopModel.describe = jsonObject.getJSONObject("data").getString("describe");
                                    goodsDetailsShopModel.is_collect = jsonObject.getJSONObject("data").getString("is_collect");
                                    goodsDetailsShopModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    goodsDetailsShopModel.face = jsonObject.getJSONObject("data").getString("face");
                                    goodsDetailsShopModel.username = jsonObject.getJSONObject("data").getString("username");
                                    goodsDetailsShopModel.auth_type = jsonObject.getJSONObject("data").getString("auth_type");
                                    goodsDetailsShopModel.last_time = jsonObject.getJSONObject("data").getString("last_time");
                                    goodsDetailsShopModel.distance = jsonObject.getJSONObject("data").getString("distance");
                                    if (userCallBack != null) {
                                        userCallBack.onUserCallBack(goodsDetailsShopModel.member_id, goodsDetailsShopModel.name, goodsDetailsShopModel.face);
                                    }

                                    if (shop_idCallBack != null) {
                                        shop_idCallBack.onLoadShop_Id(goodsDetailsShopModel.shop_id);
                                        shop_coupon(goodsDetailsShopModel.shop_id);
                                    }
                                    showShopData();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:shop-0");
            e.printStackTrace();
        }
    }

    /**
     * 显示店铺信息
     */
    void showShopData() {
        addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + goodsDetailsShopModel.logo), picFragmentGoodsServiceDetalisGoods, 0f));
        shopNameFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.name + "");
        authTypeFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.auth_type);
        loginTimeFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.last_time + "");
        openTimeFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.business_hours + "");
        scoreFragmentGoodsServiceDetalisGoods.setText("+" + goodsDetailsShopModel.credit + "");
        locationPathFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.address + "");
        distanceFragmentGoodsServiceDetalisGoods.setText(goodsDetailsShopModel.distance + "");
    }


    /**
     * 获取店铺所支持的优惠券
     */
    public void shop_coupon(String shop_id) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.shop_coupon(
                    new JSONObject()
                            .put("shop_id", shop_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CouponModel couponModel = new CouponModel();
                                            couponModel.id = jsonArray.getJSONObject(i).getString("id");
                                            couponModel.type = jsonArray.getJSONObject(i).getString("type");
                                            couponModel.money = jsonArray.getJSONObject(i).getString("money");
                                            couponModel.price = jsonArray.getJSONObject(i).getString("price");
                                            couponModel.start_time = jsonArray.getJSONObject(i).getString("start_time");
                                            couponModel.end_time = jsonArray.getJSONObject(i).getString("end_time");
                                            couponModel.coupon_id = jsonArray.getJSONObject(i).getString("coupon_id");
                                            couponModel.duration = jsonArray.getJSONObject(i).getString("duration");
                                            if (list != null) {
                                                list.add(couponModel);
                                                sss_adapter.setList(list);
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:coupon-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:coupon-0");
            e.printStackTrace();
        }
    }

}
