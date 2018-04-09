package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentShopIinfoHomePager;
import com.sss.car.fragment.FragmentShopInfoAll;
import com.sss.car.model.CouponModel;
import com.sss.car.model.GoodsDetailsShopModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;

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
 * 商铺资料
 * Created by leilei on 2017/9/17.
 */
public class ActivityShopInfo extends BaseActivity {
    @BindView(R.id.back_activity_shop_info)
    LinearLayout backActivityShopInfo;
    @BindView(R.id.details_activity_goods_service_details)
    TextView detailsActivityGoodsServiceDetails;
    @BindView(R.id.attention_activity_shop_info)
    ImageView attentionActivityShopInfo;
    @BindView(R.id.share_activity_shop_info)
    LinearLayout shareActivityShopInfo;
    @BindView(R.id.parent_activity_shop_info)
    FrameLayout parentActivityShopInfo;
    @BindView(R.id.linearLayout_activity_shop_info)
    LinearLayout linearLayoutActivityShopInfo;
    @BindView(R.id.scoll_view_activity_shop_info)
    PullToRefreshScrollView scollViewActivityShopInfo;
    @BindView(R.id.top_activity_shop_info)
    ImageView topActivityShopInfo;
    @BindView(R.id.activity_shop_info)
    LinearLayout activityShopInfo;

    View view;
    LinearLayout click;
    SimpleDraweeView pic_activity_shop_info_head;
    TextView shop_name_activity_shop_info_head;
    TextView auth_type_activity_shop_info_head;
    TextView login_time_activity_shop_info_head;
    TextView open_time_activity_shop_info_head;
    TextView score_activity_shop_info_head;
    TextView location_path_activity_shop_info_head;
    TextView distance_activity_shop_info_head;
    LinearLayout click_coupon_activity_shop_info_head;
    LinearLayout click_home_page_activity_shop_info_head;
    TextView home_page_activity_shop_info_head;
    TextView hone_pager_line_activity_shop_info_head;
    LinearLayout click_all_activity_shop_info_head;
    TextView all_activity_shop_info_head;
    TextView all_line_activity_shop_info_head;
    BannerVariation advertisement;


    List<CouponModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    MenuDialog menuDialog;


    GoodsDetailsShopModel goodsDetailsShopModel = new GoodsDetailsShopModel();


    YWLoadingDialog ywLoadingDialog;


    FragmentShopIinfoHomePager fragmentShopIinfoHomePager;

    FragmentShopInfoAll fragmentShopInfoAll;


    int currentPager = 1;
    boolean isRequested = false;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (advertisement != null) {
            advertisement.clear();
        }
        advertisement = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        backActivityShopInfo = null;
        detailsActivityGoodsServiceDetails = null;
        attentionActivityShopInfo = null;
        shareActivityShopInfo = null;
        parentActivityShopInfo = null;
        linearLayoutActivityShopInfo = null;
        scollViewActivityShopInfo = null;
        topActivityShopInfo = null;
        activityShopInfo = null;
        view = null;
        advertisement = null;
        pic_activity_shop_info_head = null;
        auth_type_activity_shop_info_head = null;
        login_time_activity_shop_info_head = null;
        open_time_activity_shop_info_head = null;
        score_activity_shop_info_head = null;
        location_path_activity_shop_info_head = null;
        distance_activity_shop_info_head = null;
        click_coupon_activity_shop_info_head = null;
        click_home_page_activity_shop_info_head = null;
        home_page_activity_shop_info_head = null;
        hone_pager_line_activity_shop_info_head = null;
        click_all_activity_shop_info_head = null;
        all_activity_shop_info_head = null;
        shop_name_activity_shop_info_head = null;
        all_line_activity_shop_info_head = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败");
            finish();
        }
        customInit(activityShopInfo, false, true, false);

        menuDialog = new MenuDialog(this);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            scollViewActivityShopInfo.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topActivityShopInfo.setVisibility(View.VISIBLE);
                    } else {
                        topActivityShopInfo.setVisibility(View.GONE);
                    }
                }
            });
        }
        scollViewActivityShopInfo.setMode(PullToRefreshBase.Mode.BOTH);
        scollViewActivityShopInfo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                switch (currentPager) {
                    case 1:
                        goods_shop();
                        break;
                    case 2:
                        fragmentShopInfoAll.p = 1;
                        fragmentShopInfoAll.shop_all();
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                switch (currentPager) {
                    case 1:
                        goods_shop();
                        break;
                    case 2:
                        fragmentShopInfoAll.shop_all();
                        break;
                }
            }
        });

        initAdapter();
        addHead();
        shop_coupon(getIntent().getExtras().getString("shop_id"));
        goods_shop();
    }

    void addHead() {

        if (view == null) {
            view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_shop_info_head, null);
            advertisement = $.f(view, R.id.advertisement);
            click = $.f(view, R.id.click);
            shop_name_activity_shop_info_head = $.f(view, R.id.shop_name_activity_shop_info_head);
            pic_activity_shop_info_head = $.f(view, R.id.pic_activity_shop_info_head);
            auth_type_activity_shop_info_head = $.f(view, R.id.auth_type_activity_shop_info_head);
            login_time_activity_shop_info_head = $.f(view, R.id.login_time_activity_shop_info_head);
            open_time_activity_shop_info_head = $.f(view, R.id.open_time_activity_shop_info_head);
            score_activity_shop_info_head = $.f(view, R.id.score_activity_shop_info_head);
            location_path_activity_shop_info_head = $.f(view, R.id.location_path_activity_shop_info_head);
            distance_activity_shop_info_head = $.f(view, R.id.distance_activity_shop_info_head);
            home_page_activity_shop_info_head = $.f(view, R.id.home_page_activity_shop_info_head);
            hone_pager_line_activity_shop_info_head = $.f(view, R.id.hone_pager_line_activity_shop_info_head);
            all_activity_shop_info_head = $.f(view, R.id.all_activity_shop_info_head);
            all_line_activity_shop_info_head = $.f(view, R.id.all_line_activity_shop_info_head);


            click_coupon_activity_shop_info_head = $.f(view, R.id.click_coupon_activity_shop_info_head);
            click_coupon_activity_shop_info_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sss_adapter == null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "正在加载优惠券...");
                        return;
                    }
                    if (list.size() < 1) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "该店铺没有优惠券");
                    } else {
                        menuDialog.createGoodsBottomCouponDialog(getBaseActivityContext(), sss_adapter);
                    }

                }
            });


            click_home_page_activity_shop_info_head = $.f(view, R.id.click_home_page_activity_shop_info_head);
            click_home_page_activity_shop_info_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPager = 1;
                    home_page_activity_shop_info_head.setTextColor(getResources().getColor(R.color.mainColor));
                    hone_pager_line_activity_shop_info_head.setVisibility(View.VISIBLE);
                    all_activity_shop_info_head.setTextColor(getResources().getColor(R.color.rc_picsel_toolbar_transparent));
                    all_line_activity_shop_info_head.setVisibility(View.GONE);
                    if (isRequested) {
                        if (fragmentShopIinfoHomePager == null) {
                            fragmentShopIinfoHomePager = new FragmentShopIinfoHomePager(goodsDetailsShopModel.describe);
                            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentShopIinfoHomePager, R.id.parent_activity_shop_info);
                        }
                        FragmentUtils.hideAllShowFragment(fragmentShopIinfoHomePager);
                    }
                }
            });

            click_all_activity_shop_info_head = $.f(view, R.id.click_all_activity_shop_info_head);
            click_all_activity_shop_info_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPager = 2;
                    all_activity_shop_info_head.setTextColor(getResources().getColor(R.color.mainColor));
                    all_line_activity_shop_info_head.setVisibility(View.VISIBLE);
                    home_page_activity_shop_info_head.setTextColor(getResources().getColor(R.color.rc_picsel_toolbar_transparent));
                    hone_pager_line_activity_shop_info_head.setVisibility(View.GONE);
                    if (fragmentShopInfoAll == null) {
                        fragmentShopInfoAll = new FragmentShopInfoAll(getIntent().getExtras().getString("shop_id"), scollViewActivityShopInfo);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentShopInfoAll, R.id.parent_activity_shop_info);
                    }
                    FragmentUtils.hideAllShowFragment(fragmentShopInfoAll);
                }
            });

            linearLayoutActivityShopInfo.addView(view);
        }
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    if (goodsDetailsShopModel != null)
                        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                                .putExtra("id", goodsDetailsShopModel.member_id));

                }
            }
        });
    }

    @OnClick({R.id.back_activity_shop_info, R.id.attention_activity_shop_info, R.id.share_activity_shop_info, R.id.top_activity_shop_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_shop_info:
                finish();
                break;
            case R.id.attention_activity_shop_info:
                shop_collect();
                break;
            case R.id.share_activity_shop_info:
                ShareUtils.prepareShare(ywLoadingDialog, getBaseActivity(), "shop", getIntent().getExtras().getString("shop_id"));
                break;
            case R.id.top_activity_shop_info:
                scollViewActivityShopInfo.getRefreshableView().smoothScrollTo(0, 0);
                break;
        }
    }


    /**
     * 获取店铺信息
     */
    public void goods_shop() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_shop(
                    new JSONObject()
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("shop_id", getIntent().getExtras().getString("shop_id"))
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (scollViewActivityShopInfo != null) {
                                scollViewActivityShopInfo.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollViewActivityShopInfo != null) {
                                scollViewActivityShopInfo.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    isRequested = true;
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
                                    goodsDetailsShopModel.auth_type = jsonObject.getJSONObject("data").getString("auth_type");
                                    goodsDetailsShopModel.last_time = jsonObject.getJSONObject("data").getString("last_time");
                                    goodsDetailsShopModel.distance = jsonObject.getJSONObject("data").getString("distance");
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    List<String> pic = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        pic.add(Config.url + jsonArray.getString(i));
                                    }
                                    goodsDetailsShopModel.picture = pic;
                                    showShopData();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
            e.printStackTrace();
        }
    }


    /**
     * 显示店铺信息
     */

    void showShopData() {

        if (advertisement == null) {
            return;
        }
        if (goodsDetailsShopModel.picture.size() > 0) {
            advertisement
                    .setImages(goodsDetailsShopModel.picture)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setDelayTime(Config.flash)
                    .setImageLoader(new ImageLoaderInterface() {
                        @Override
                        public void displayImage(Context context, Object path, View imageView) {
                            imageView.setTag(R.id.glide_tag, ((String) path));
                            addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));

                        }

                        @Override
                        public View createImageView(Context context) {
                            return null;
                        }
                    });
            advertisement.start();
            advertisement.startAutoPlay();
            advertisement.setVisibility(View.VISIBLE);
        } else {
            advertisement.setVisibility(View.GONE);
        }

        if (fragmentShopIinfoHomePager == null) {
            fragmentShopIinfoHomePager = new FragmentShopIinfoHomePager(goodsDetailsShopModel.describe);
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentShopIinfoHomePager, R.id.parent_activity_shop_info);
        }
        FragmentUtils.hideAllShowFragment(fragmentShopIinfoHomePager);
        addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + goodsDetailsShopModel.logo), pic_activity_shop_info_head, 0f));
        shop_name_activity_shop_info_head.setText(goodsDetailsShopModel.name + "");
        auth_type_activity_shop_info_head.setText(goodsDetailsShopModel.auth_type);
        login_time_activity_shop_info_head.setText(goodsDetailsShopModel.last_time + "");
        open_time_activity_shop_info_head.setText(goodsDetailsShopModel.business_hours + "");
        score_activity_shop_info_head.setText("+" + goodsDetailsShopModel.credit + "");
        location_path_activity_shop_info_head.setText(goodsDetailsShopModel.address + "");
        distance_activity_shop_info_head.setText(goodsDetailsShopModel.distance + "");
        if ("1".equals(goodsDetailsShopModel.is_collect)) {
            addImageViewList(GlidUtils.glideLoad(false, attentionActivityShopInfo, getBaseActivityContext(), R.mipmap.logo_attention));
        } else {
            addImageViewList(GlidUtils.glideLoad(false, attentionActivityShopInfo, getBaseActivityContext(), R.mipmap.logo_attention_no));
        }
    }


    /**
     * 收藏
     */
    public void shop_collect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.shop_collect(
                    new JSONObject()
                            .put("type", "shop")
                            .put("collect_id", getIntent().getExtras().getString("shop_id"))
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
                                        addImageViewList(GlidUtils.glideLoad(false, attentionActivityShopInfo, getBaseActivityContext(), R.mipmap.logo_attention));
                                    } else {
                                        addImageViewList(GlidUtils.glideLoad(false, attentionActivityShopInfo, getBaseActivityContext(), R.mipmap.logo_attention_no));
                                    }
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
            e.printStackTrace();
        }
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
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                            list.add(couponModel);
                                            sss_adapter.setList(list);
                                        }
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


    void initAdapter() {
        sss_adapter = new SSS_Adapter<CouponModel>(getBaseActivityContext(), R.layout.item_bottom_coupon_adapter) {
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

                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityCoupon.class)
                                    .putExtra("coupon_id", list.get(position).coupon_id)
                                    .putExtra("money", list.get(position).money)
                                    .putExtra("mode", "details")
                            );
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
