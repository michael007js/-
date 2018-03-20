package com.sss.car.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCollectGoodsList;
import com.sss.car.EventBusModel.changedShopCart;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.CustomRefreshLayoutCallBack3;
import com.sss.car.dao.IsCollectCallBack;
import com.sss.car.dao.OnFragmentGoodsServiceDetailsGoodsCallBack;
import com.sss.car.dao.ShoppingCartCallBack;
import com.sss.car.dao.UserCallBack;
import com.sss.car.fragment.FragmentGoodsServiceDetailsComment;
import com.sss.car.fragment.FragmentGoodsServiceDetailsDetails;
import com.sss.car.fragment.FragmentGoodsServiceDetailsGoods;
import com.sss.car.model.GoodsChooseModel;
import com.sss.car.model.GoodsChooseSizeData;
import com.sss.car.model.GoodsChooseSizeName;
import com.sss.car.model.GoodsChooseSizeName_Model;
import com.sss.car.order.OrderGoodsReadyBuy;
import com.sss.car.order.OrderServiceReadyBuy;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.MenuDialog;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;


/**
 * 商品详情页
 * Created by leilei on 2017/9/14.
 */

public class ActivityGoodsServiceDetails extends BaseActivity implements UserCallBack, IsCollectCallBack, OnFragmentGoodsServiceDetailsGoodsCallBack {
    @BindView(R.id.back_activity_goods_service_details)
    LinearLayout backActivityGoodsServiceDetails;
    @BindView(R.id.more_activity_goods_service_details)
    ImageView moreActivityGoodsServiceDetails;
    @BindView(R.id.top_activity_goods_service_details)
    ImageView topActivityGoodsServiceDetails;
    @BindView(R.id.activity_goods_service_details)
    LinearLayout activityGoodsServiceDetails;
    @BindView(R.id.click_shop_activity_goods_service_details)
    LinearLayout clickShopActivityGoodsServiceDetails;
    @BindView(R.id.click_customer_service_goods_service_details)
    LinearLayout clickCustomerServiceGoodsServiceDetails;
    @BindView(R.id.click_collect_goods_service_details)
    LinearLayout clickCollectGoodsServiceDetails;
    @BindView(R.id.add_shop_car_goods_service_details)
    TextView addShopCarGoodsServiceDetails;
    @BindView(R.id.subscribe_goods_service_details)
    TextView subscribeGoodsServiceDetails;
    String price;
    boolean stop;

    MenuDialog menuDialog;
    String shop_id, member_id, name, face;
    GoodsChooseModel goodsChooseModel = new GoodsChooseModel();

    FragmentGoodsServiceDetailsGoods fragmentGoodsServiceDetailsGoods;

    FragmentGoodsServiceDetailsDetails fragmentGoodsServiceDetailsDetails;

    FragmentGoodsServiceDetailsComment fragmentGoodsServiceDetailsComment;
    String[] title = {"  商品  ", "  详情  ", "  评价  "};


    FragmentAdapter fragmentAdapter;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.image_collect_goods_service_details)
    ImageView imageCollectGoodsServiceDetails;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.bottom_parent)
    LinearLayout bottomParent;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        viewpager = null;
        scrollTab = null;
        title = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        goodsChooseModel = null;
        backActivityGoodsServiceDetails = null;

        moreActivityGoodsServiceDetails = null;
        topActivityGoodsServiceDetails = null;
        activityGoodsServiceDetails = null;
        clickShopActivityGoodsServiceDetails = null;
        clickCustomerServiceGoodsServiceDetails = null;
        clickCollectGoodsServiceDetails = null;
        addShopCarGoodsServiceDetails = null;
        subscribeGoodsServiceDetails = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(changedShopCart changedShopCart) {
        if (fragmentGoodsServiceDetailsGoods != null) {
            fragmentGoodsServiceDetailsGoods.getShoppingCart();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_service_details);
        ButterKnife.bind(this);
        customInit(activityGoodsServiceDetails, false, true, true);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        stop = getIntent().getExtras().getBoolean("stop");
        if (stop) {
            bottomParent.setVisibility(View.GONE);
        }
        menuDialog = new MenuDialog(this);

        fragmentGoodsServiceDetailsGoods = new FragmentGoodsServiceDetailsGoods(stop,getIntent().getExtras().getString("goods_id")
                , new Shop_IdCallBack() {
            @Override
            public void onLoadShop_Id(String shop_id) {
                ActivityGoodsServiceDetails.this.shop_id = shop_id;
            }
        }, this, this, this);
        fragmentGoodsServiceDetailsDetails = new FragmentGoodsServiceDetailsDetails(stop,getIntent().getExtras().getString("goods_id"), new CustomRefreshLayoutCallBack3() {
            @Override
            public void onAdd(final ScrollView scrollView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY > Config.scoll_HighRestriction) {
                                topActivityGoodsServiceDetails.setVisibility(View.GONE);
                                topActivityGoodsServiceDetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        scrollView.smoothScrollTo(0,0);
                                    }
                                });
                            } else {
                                topActivityGoodsServiceDetails.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        fragmentGoodsServiceDetailsComment = new FragmentGoodsServiceDetailsComment(stop,getIntent().getExtras().getString("goods_id"),

                new CustomRefreshLayoutCallBack3() {
                    @Override
                    public void onAdd(final ScrollView scrollView) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (scrollY > Config.scoll_HighRestriction) {
                                        topActivityGoodsServiceDetails.setVisibility(View.VISIBLE);
                                        topActivityGoodsServiceDetails.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                scrollView.smoothScrollTo(0,0);
                                            }
                                        });
                                    } else {
                                        topActivityGoodsServiceDetails.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        fragmentAdapter.addFragment(fragmentGoodsServiceDetailsGoods);
        fragmentAdapter.addFragment(fragmentGoodsServiceDetailsDetails);
        fragmentAdapter.addFragment(fragmentGoodsServiceDetailsComment);
        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpager);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setAdapter(fragmentAdapter);
        viewpager.setOffscreenPageLimit(3);


        if ("show".equals(getIntent().getExtras().getString("showBuyDialog"))) {
            createDialog(null);
        }
    }

    @OnClick({R.id.back_activity_goods_service_details,
            R.id.click_shop_activity_goods_service_details, R.id.click_customer_service_goods_service_details,
            R.id.click_collect_goods_service_details, R.id.add_shop_car_goods_service_details, R.id.subscribe_goods_service_details, R.id.more_activity_goods_service_details,
            R.id.top_activity_goods_service_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_goods_service_details:
                finish();
                break;

            case R.id.more_activity_goods_service_details:
              if (!stop){
                  menuDialog.createGoodsMenu(moreActivityGoodsServiceDetails,getBaseActivity(),"goods",getIntent().getExtras().getString("goods_id"));
              }
                break;
            case R.id.top_activity_goods_service_details:
                if (fragmentGoodsServiceDetailsDetails == null || fragmentGoodsServiceDetailsDetails.parentFragmentGoodsServiceDetailsDetails == null) {
                    return;
                }
                fragmentGoodsServiceDetailsDetails.parentFragmentGoodsServiceDetailsDetails.smoothScrollTo(0, 0);
                break;
            case R.id.click_shop_activity_goods_service_details:
                if (StringUtils.isEmpty(shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请稍后,数据载入中...");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
                break;
            case R.id.click_customer_service_goods_service_details:
                if (StringUtils.isEmpty(this.member_id) || StringUtils.isEmpty(this.name) || StringUtils.isEmpty(this.face)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "刷新中,请稍候...");
                    return;
                }
                RongYunUtils.startConversation(getBaseActivityContext(), Conversation.ConversationType.PRIVATE, member_id, "2");//客服传6，商品详情客服传2，群组传5
                break;
            case R.id.click_collect_goods_service_details:
                insert_collect_cancel_collect();
                break;
            case R.id.add_shop_car_goods_service_details:
                addToShoppingCartAndChooseDetails("cart");
                break;
            case R.id.subscribe_goods_service_details:
                addToShoppingCartAndChooseDetails("order");
                break;
        }
    }


    /**
     * 点击加入购物车时弹出的商品信息选择框
     */
    public void addToShoppingCartAndChooseDetails(String typeSer) {
        createDialog(typeSer);
    }


    void createDialog(final String typeSer) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.addToShoppingCartAndChooseDetails(
                    new JSONObject()
                            .put("goods_id", getIntent().getExtras().getString("goods_id"))
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

                                    goodsChooseModel.goods_id = jsonObject.getJSONObject("data").getString("goods_id");
                                    goodsChooseModel.title = jsonObject.getJSONObject("data").getString("title");
                                    goodsChooseModel.slogan = jsonObject.getJSONObject("data").getString("slogan");
                                    goodsChooseModel.master_map = jsonObject.getJSONObject("data").getString("master_map");
                                    goodsChooseModel.cost_price = jsonObject.getJSONObject("data").getString("cost_price");
                                    goodsChooseModel.price = jsonObject.getJSONObject("data").getString("price");
                                    goodsChooseModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
                                    goodsChooseModel.end_time = jsonObject.getJSONObject("data").getString("end_time");
                                    goodsChooseModel.likes = jsonObject.getJSONObject("data").getString("likes");
                                    goodsChooseModel.share = jsonObject.getJSONObject("data").getString("share");
                                    goodsChooseModel.sell = jsonObject.getJSONObject("data").getString("sell");
                                    goodsChooseModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    goodsChooseModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    goodsChooseModel.distance = jsonObject.getJSONObject("data").getString("distance");
                                    goodsChooseModel.is_like = jsonObject.getJSONObject("data").getString("is_like");
//                                    goodsChooseModel.classify_name = jsonObject.getJSONObject("data").getString("classify_name");
////////////////////////////////////////////////////////////商品图片↓////////////////////////////////////////////////////////////////////////////////////
                                    List<String> picture = new ArrayList<>();
                                    JSONArray jsonArray1 = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        picture.add(jsonArray1.getString(j));
                                    }
                                    goodsChooseModel.picture = picture;
//////////////////////////////////////////////////////////////详情图片↓//////////////////////////////////////////////////////////////////////////////////
                                    List<String> photo = new ArrayList<>();
                                    JSONArray jsonArray2 = jsonObject.getJSONObject("data").getJSONArray("photo");
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        picture.add(jsonArray2.getString(k));
                                    }
                                    goodsChooseModel.photo = photo;
////////////////////////////////////////////////////////////////最后的尺寸价格数据///////////////////////////////////////////////////////////////////////////////

                                    List<GoodsChooseSizeData> size_dat = new ArrayList<>();
                                    JSONArray jsonArray3 = jsonObject.getJSONObject("data").getJSONArray("size_data");
                                    for (int l = 0; l < jsonArray3.length(); l++) {
                                        GoodsChooseSizeData goodsChooseSizeData = new GoodsChooseSizeData();
                                        goodsChooseSizeData.name = jsonArray3.getJSONObject(l).getString("name");
                                        goodsChooseSizeData.price = jsonArray3.getJSONObject(l).getString("price");
                                        if (jsonArray3.getJSONObject(l).has("number")) {
                                            goodsChooseSizeData.number = jsonArray3.getJSONObject(l).getString("number");
                                        }

                                        size_dat.add(goodsChooseSizeData);
                                    }
                                    goodsChooseModel.size_dat = size_dat;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                                    JSONArray jsonArray4 = jsonObject.getJSONObject("data").getJSONArray("size_name");

                                    List<GoodsChooseSizeName> size_name = new ArrayList<>();
                                    for (int m = 0; m < jsonArray4.length(); m++) {
                                        GoodsChooseSizeName goodsChooseSizeName = new GoodsChooseSizeName();
                                        goodsChooseSizeName.title = jsonArray4.getJSONObject(m).getString("title");
                                        List<GoodsChooseSizeName_Model> datas = new ArrayList<>();
                                        JSONArray jsonArray = jsonArray4.getJSONObject(m).getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            if ("1".equals(jsonArray.getJSONObject(i).getString("is_check"))) {
                                                datas.add(new GoodsChooseSizeName_Model(jsonArray.getJSONObject(i).getString("name"), true));
                                            } else {
                                                datas.add(new GoodsChooseSizeName_Model(jsonArray.getJSONObject(i).getString("name"), false));
                                            }

                                        }
                                        goodsChooseSizeName.data = datas;
                                        size_name.add(goodsChooseSizeName);
                                    }

                                    goodsChooseModel.size_name = size_name;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                    if (goodsChooseModel.size_dat.size() > 0) {
                                    menuDialog.createGoodsBottomDialog("goods",1, getBaseActivityContext(), 3, goodsChooseModel,
                                            new ShoppingCartCallBack() {
                                                @Override
                                                public void onShoppingCartCallBack(int price, int number, List<GoodsChooseSizeName> size_name, JSONArray jsonArray, String type) {
//                                                    if (jsonArray == null || jsonArray.length() == 0) {
//                                                        ToastUtils.showShortToast(getBaseActivityContext(), "请选择商品规格");
//                                                        return;
//                                                    }
                                                    //添加购物车type=cart, 添加立即下单，type=order
                                                    if ("cart".equals(type)) {
                                                        addShoppingCartOrOrder(price + "", number + "", jsonArray, type);
                                                    } else if ("order".equals(type)) {
                                                        insert_order(price + "", number + "", jsonArray, type);
                                                    }


                                                }
                                            });


//                                    } else {
//                                        LogUtils.e("price"+price);
//                                        if (!StringUtils.isEmpty(price)) {
//                                            //添加购物车type=cart, 添加立即下单，type=order
//                                            LogUtils.e(typeSer+"---");
//                                            if ("cart".equals(typeSer)) {
//                                                addShoppingCartOrOrder(price, "1", new JSONArray(), typeSer);
//                                            } else if ("order".equals(typeSer)) {
//                                                insert_order(price, "1", new JSONArray(), typeSer);
//                                            }
//                                        }else {
//                                            ToastUtils.showShortToast(getBaseActivityContext(),"商品刷新中,请稍候...");
//                                        }
//                                    }
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
     * 添加到购物车或立即下单
     */
    public void addShoppingCartOrOrder(String price, String number, JSONArray jsonArray, final String type) {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.addShoppingCartOrOrder(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", getIntent().getExtras().getString("goods_id"))
                            .put("num", number)
                            .put("price", price)
                            .put("options", jsonArray)
                            .put("type", type)//添加购物车type=cart, 添加立即下单，type=order
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
                                    if ("cart".equals(type)) {
                                        fragmentGoodsServiceDetailsGoods.getShoppingCart();
                                    }
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cart Or Order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cart Or Order-0");
            e.printStackTrace();
        }
    }


    /**
     * 加入预购
     */
    public void insert_order(String price, String number, JSONArray jsonArray, final String type) {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        JSONArray goods_id = new JSONArray();
        goods_id.put(getIntent().getExtras().getString("goods_id"));
        JSONArray priceList = new JSONArray();
        priceList.put(price);
        JSONArray num = new JSONArray();
        num.put(number);
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_order(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("num", num)
                            .put("price", priceList)
                            .put("options", new JSONArray().put(jsonArray))
                            .put("type", type)//添加购物车type=cart, 添加立即下单，type=order
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

                                    //添加购物车type=cart, 添加立即下单，type=order
                                    if ("order".equals(type)) {
                                        if ("1".equals(getIntent().getExtras().getString("type"))) {
                                            LogUtils.e("order1");
                                            if (getBaseActivityContext() != null) {
                                                startActivity(new Intent(getBaseActivityContext(), OrderGoodsReadyBuy.class)
                                                        .putExtra("shop_id", shop_id)
                                                        .putExtra("goods_id", getIntent().getExtras().getString("goods_id"))
                                                        .putExtra("from", "ActivityGoodsServiceDetails"));
                                            }
                                        } else if ("2".equals(getIntent().getExtras().getString("type"))) {
                                            LogUtils.e("order2");
                                            if (getBaseActivityContext() != null) {
                                                startActivity(new Intent(getBaseActivityContext(), OrderServiceReadyBuy.class)
                                                        .putExtra("shop_id", shop_id)
                                                        .putExtra("goods_id", getIntent().getExtras().getString("goods_id"))
                                                        .putExtra("from", "ActivityGoodsServiceDetails"));
                                            }
                                        }
                                    }
                                    LogUtils.e("order" + getIntent().getExtras().getString("type"));
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cart Or Order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cart Or Order-0");
            e.printStackTrace();
        }
    }

    /**
     * 店家信息回调
     *
     * @param member_id
     * @param name
     * @param face
     */
    @Override
    public void onUserCallBack(String member_id, String name, String face) {
        this.member_id = member_id;
        this.name = name;
        this.face = face;
    }

    /**
     * 商品是否已经收藏
     *
     * @param isCollect
     */
    @Override
    public void onIsCollectCallBack(boolean isCollect) {
        if (isCollect) {
            addImageViewList(GlidUtils.glideLoad(false, imageCollectGoodsServiceDetails, getBaseActivityContext(), R.mipmap.logo_collect));
        } else {
            addImageViewList(GlidUtils.glideLoad(false, imageCollectGoodsServiceDetails, getBaseActivityContext(), R.mipmap.logo_collect_no));
        }

    }


    /**
     * 收藏/取消收藏
     */
    public void insert_collect_cancel_collect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_collect_cancel_collect(
                    new JSONObject()
                            .put("collect_id", getIntent().getExtras().getString("goods_id"))
                            .put("type", "goods")
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        onIsCollectCallBack(true);
                                        EventBus.getDefault().post(new ChangedCollectGoodsList(getIntent().getExtras().getString("goods_id"), true));
                                    } else {
                                        onIsCollectCallBack(false);
                                        EventBus.getDefault().post(new ChangedCollectGoodsList(getIntent().getExtras().getString("goods_id"), false));
                                    }

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
    public void onPrice(String price) {
        this.price = price;

    }

    public interface Shop_IdCallBack {
        void onLoadShop_Id(String shop_id);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
