package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.ViewpagerHelper.indicator.ZoomIndicator;
import com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ScollView.InCludeLandscapeScrollView;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageAdapter;
import com.blankj.utilcode.customwidget.Tab.CustomTab.ViewHolder;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.TopTabAdapter;
import com.sss.car.custom.Advertisement.AdvertisementViewPagerHelper;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.custom.ListViewShowGoodsService;
import com.sss.car.custom.TAB;
import com.sss.car.dao.OnExistsShopCallBack;
import com.sss.car.model.CateModel;
import com.sss.car.model.GoodsModel;
import com.sss.car.model.GoodsServiceHotModel;
import com.sss.car.model.ListViewShowGoodsServiceModel;
import com.sss.car.model.TopTabModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityGoodsList;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityGoodsServiceEdit;
import com.sss.car.view.ActivityLocationCitySelect;
import com.sss.car.view.ActivitySaleAndSeckill;
import com.sss.car.view.ActivityServiceGoodsHotMore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 车品车服公用fragment(已改版,此模块不再使用)
 * Created by leilei on 2017/9/11.
 */


@SuppressLint("ValidFragment")
@SuppressWarnings("ALL")
public class FragmentGoodsServicePublic extends BaseFragment implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.top_tab_fragment_goods)
    HorizontalListView topTabFragmentGoods;
    @BindView(R.id.location_path_left_location)
    public TextView locationPathLeftLocation;
    @BindView(R.id.search_path_left_location)
    ImageView searchPathLeftLocation;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
    @BindView(R.id.refresh_fragment_goods)
    RefreshLoadMoreLayout refreshFragmentGoods;
    @BindView(R.id.top_fragment_goods)
    ImageView topFragmentGoods;
    @BindView(R.id.scoll_linearlayout_fragment_goods)
    LinearLayout scollLinearlayoutFragmentGoods;
    @BindView(R.id.scoll_view_fragment_goods)
    InCludeLandscapeScrollView scollViewFragmentGoods;


    View view;
    BannerVariation viewpager_up_fragment_goods_head;
    TAB tab_fragment_goods_head;
    HorizontalListView hot_fragment_goods_head;
    LinearLayout adv_up_fragment_goods_head;
    BannerVariation viewpager_down_fragment_goods_head;

    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;


    /*一级父类菜单集合与适配器*/
    TopTabAdapter topTab_goodsClassify_adapter;
    List<TopTabModel> goodsClassifyModelList = new ArrayList<>();
    /*一级父类菜单选中项(顶部)*/
    int currentFirstSeclect = 0;


    /*二级菜单集合*/
    List<CateModel> cateList = new ArrayList<>();


    /*视图列表集合*/
    List<ListViewShowGoodsService> listViewShowGoodsServices = new ArrayList<>();

    /*商品列表集合*/
    List<ListViewShowGoodsServiceModel> showGoodsServiceModelList = new ArrayList<>();


    /*热门商品集合*/
    List<GoodsServiceHotModel> goodsServiceHotModelList = new ArrayList<>();

    MenuDialog menuDialog;

    String type = "1";//1车品2车服

    AdvertisementViewPagerHelper advertisementViewPagerHelper;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (advertisementViewPagerHelper != null) {
            advertisementViewPagerHelper.onDestroy();
        }
        advertisementViewPagerHelper = null;
        view = null;
        hot_fragment_goods_head = null;
        tab_fragment_goods_head = null;
        adv_up_fragment_goods_head = null;
        viewpager_down_fragment_goods_head = null;
        scollLinearlayoutFragmentGoods = null;
        refreshFragmentGoods = null;
        topFragmentGoods = null;
        topTabFragmentGoods = null;
        locationPathLeftLocation = null;
        searchPathLeftLocation = null;
        rightMenu = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_public;
    }

    public FragmentGoodsServicePublic(String type) {
        this.type = type;
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
                                if (!StringUtils.isEmpty(Config.city)) {
                                    locationPathLeftLocation.setText(Config.city);
                                    locationPathLeftLocation.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (getBaseFragmentActivityContext() != null) {
                                                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityLocationCitySelect.class));
                                            }
                                        }
                                    });
                                }
                                advertisementViewPagerHelper = new AdvertisementViewPagerHelper();
                                initView();
                                goods_classify();
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

    public FragmentGoodsServicePublic() {
    }

    /**
     * 初始化视图
     */
    @SuppressLint("InflateParams")
    void initView() {
        scollViewFragmentGoods.setSmoothScrollingEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            scollViewFragmentGoods.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topFragmentGoods.setVisibility(View.VISIBLE);
                    } else {
                        topFragmentGoods.setVisibility(View.GONE);
                    }
                }
            });
        }


        topFragmentGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topFragmentGoods.setVisibility(View.GONE);
                scollViewFragmentGoods.smoothScrollTo(0, 0);
            }
        });

        topTab_goodsClassify_adapter = new TopTabAdapter(getBaseFragmentActivityContext(), goodsClassifyModelList);
        topTabFragmentGoods.setAdapter(topTab_goodsClassify_adapter);
        topTabFragmentGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setClassifyID(type);
                if (currentFirstSeclect != position) {
                    currentFirstSeclect = position;
                    topTab_goodsClassify_adapter.refrtesh(position);
                    hot_subclass(goodsClassifyModelList.get(position).id);//热门
                    goods_subclass();//TAB
                    clearView();
                    classify_goods();//列表
                }
            }
        });
        addImageViewList(GlidUtils.glideLoad(false, rightMenu, getBaseFragmentActivityContext(), R.mipmap.logo_ten));
        if (view == null) {
            view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_goods_service_public_head, null);
            BannerViewPager upBannerViewPager = $.f(view, R.id.banner);
            ZoomIndicator upZoomIndicator = $.f(view, R.id.bottom_zoom_arc);

            List<AdvertisementModel> list = new ArrayList<>();
            String a = "http://180.97.222.16/youku/67737554FCD4B81E24638D4074/03000801005A17BA97B8AB456691039ACA2CFA-28C7-2777-932C-4272709941F9.mp4?sid=051185900138812f892a6&ctype=12&ccode=0508&duration=248&expire=18000&psid=88c9d6111cd51f2f4e43c5f6dd466f23&ups_client_netip=731c5c42&ups_ts=1511859001&ups_userid=&utid=WhCkElv8tQkCAXMcXELpPaE3&vkey=Aff97ca47ccd494c37f40b4d0428270b2&vid=XMzE4MTAxNTk5Mg%3D%3D";
            list.add(new AdvertisementModel("", a, true));
            list.add(new AdvertisementModel("", "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG", false));
            advertisementViewPagerHelper.setView(upBannerViewPager, upZoomIndicator)
                    .setData(getActivity()
                            , getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                            150,
                            list, true)
                    .setOnAdvertisementClickCallBack(new AdvertisementViewPagerHelper.OnAdvertisementClickCallBack() {
                        @Override
                        public void onClick() {
                            if (getBaseFragmentActivityContext() != null) {
                                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySaleAndSeckill.class)
                                        .putExtra("type", type));
                            }
                        }
                    });
//            viewpager_up_fragment_goods_head
//                    .setImages(list)
//                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                    .setDelayTime(3000)
//                    .setImageLoader(new ImageLoaderInterface() {
//                        @Override
//                        public void displayImage(Context context, Object path, View imageView) {
//                            imageView.setTag(R.id.glide_tag, ((String) path));
//                            addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
//                            imageView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                        }
//
//                        @Override
//                        public View createImageView(Context context) {
//                            return null;
//                        }
//                    });
//            viewpager_up_fragment_goods_head.start();
//            viewpager_up_fragment_goods_head.startAutoPlay();
            tab_fragment_goods_head = $.f(view, R.id.tab_fragment_goods_head);
            hot_fragment_goods_head = $.f(view, R.id.hot_fragment_goods_head);
            adv_up_fragment_goods_head = $.f(view, R.id.adv_up_fragment_goods_head);
            viewpager_down_fragment_goods_head = $.f(view, R.id.viewpager_down_fragment_goods_head);

            List<String> list2 = new ArrayList<>();
            list2.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
            list2.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
            list2.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
            viewpager_down_fragment_goods_head
                    .setImages(list2)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setDelayTime(3000)
                    .setImageLoader(new ImageLoaderInterface() {
                        @Override
                        public void displayImage(Context context, Object path, View imageView) {
                            imageView.setTag(R.id.glide_tag, ((String) path));
                            addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySaleAndSeckill.class)
                                                .putExtra("type", type));
                                    }
                                }
                            });
                        }

                        @Override
                        public View createImageView(Context context) {
                            return null;
                        }
                    });
            viewpager_down_fragment_goods_head.start();
            viewpager_down_fragment_goods_head.startAutoPlay();
        }

        hot_fragment_goods_head.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  /*position6为更多*/
                if (position != 6) {
                    startActivity(goodsServiceHotModelList.get(position).classify_id, goodsServiceHotModelList.get(position).name);
                } else {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityServiceGoodsHotMore.class)
                            .putExtra("type", type)
                            .putExtra("classify_id", goodsServiceHotModelList.get(position).classify_id)
                    );

                }
            }
        });

        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                menuDialog.createMainRightMenu(rightMenu, getActivity(), new OnExistsShopCallBack() {
                    @Override
                    public void onExists() {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class));
                    }
                });
            }
        });
        if (view.getParent()!=null){
            ((LinearLayout)view.getParent()).removeView(view);
        }
        scollLinearlayoutFragmentGoods.addView(view);
        refreshFragmentGoods.init(new RefreshLoadMoreLayout.Config(this).canRefresh(true).canLoadMore(false).includeScrollHorizontal(true));
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            if (viewpager_up_fragment_goods_head != null) {
                viewpager_up_fragment_goods_head.startAutoPlay();
            }
            if (viewpager_down_fragment_goods_head != null) {
                viewpager_down_fragment_goods_head.startAutoPlay();
            }
        } else {
            if (viewpager_up_fragment_goods_head != null) {
                viewpager_up_fragment_goods_head.stopAutoPlay();
            }
            if (viewpager_down_fragment_goods_head != null) {
                viewpager_down_fragment_goods_head.stopAutoPlay();
            }
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


    public void setClassifyID(String type) {
        LogUtils.e("000000" + type);
        //当type=2时车服店铺详情页全部宝贝点击筛选之后的页面必须,车品不需要
//        if ("2".equals(type)) {
        LogUtils.e("111111");
        if (goodsClassifyModelList != null) {
            LogUtils.e("22222222" + type);
            if (goodsClassifyModelList.size() > 0) {
                LogUtils.e("3333333333" + type);
                Config.classify_id = goodsClassifyModelList.get(currentFirstSeclect).id;
            }
        }
//        } else {
//            LogUtils.e("555555555" + type);
//            Config.classify_id = null;
//        }
    }

    /**
     * 获取车品分类(顶部)
     */
    public void goods_classify() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_classify(
                    new JSONObject()
                            .put("type", type)//分类(1车品2车服3消息4分享)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        clearView();
                                        goodsClassifyModelList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            TopTabModel goodsClassifyModel = new TopTabModel();
                                            goodsClassifyModel.id = jsonArray.getJSONObject(i).getString("classify_id");
                                            goodsClassifyModel.name = jsonArray.getJSONObject(i).getString("name");
                                            goodsClassifyModelList.add(goodsClassifyModel);
                                        }
                                        if (topTab_goodsClassify_adapter!=null) {
                                            topTab_goodsClassify_adapter.refrtesh(0);
                                        }
                                        setClassifyID(type);
                                        hot_subclass(goodsClassifyModelList.get(0).id);//热门
                                        goods_subclass();
                                        classify_goods();
                                    }


                                } else {

                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取车品子分类(TAB)
     */
    public void goods_subclass() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_four(
                    new JSONObject()
                            .put("type", type)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentGoods != null) {
                                refreshFragmentGoods.stopRefresh();

                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentGoods != null) {
                                refreshFragmentGoods.stopRefresh();

                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    cateList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        cateList.add(new CateModel(jsonArray.getJSONObject(i).getString("classify_id"),
                                                jsonArray.getJSONObject(i).getString("name"),
                                                Uri.parse(Config.url + jsonArray.getJSONObject(i).getString("logo"))));
                                    }
                                    showTAB_Data();

                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }


    /**
     * 显示二级横线菜单数据(TAB)
     */
    void showTAB_Data() {
        tab_fragment_goods_head.setAdapter(new HorizontalPageAdapter(getBaseFragmentActivityContext(), cateList, R.layout.item_tab) {
            @Override
            public void bindViews(ViewHolder viewHolder, Object o, final int i) {
                ((TextView) viewHolder.getView(R.id.text_item_tab)).setText(((CateModel) o).cate_name);

                addImageViewList(FrescoUtils.showImage(false, 40, 40, cateList.get(i).logo, ((SimpleDraweeView) viewHolder.getView(R.id.pic_item_tab)), 0f));

                ((LinearLayout) viewHolder.getView(R.id.click_item_tab)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cateList.size() > 0) {
                            startActivity(cateList.get(i).cate_id, cateList.get(i).cate_name);
                        }

                    }
                });
            }
        },cateList.size(),getActivity());
    }


    /**
     * 获取热门
     */
    public void hot_subclass(String classify_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.hot_subclass(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("type", type)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentGoods != null) {
                                refreshFragmentGoods.stopRefresh();

                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentGoods != null) {
                                refreshFragmentGoods.stopRefresh();

                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        goodsServiceHotModelList.clear();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            GoodsServiceHotModel goodsServiceHotModel = new GoodsServiceHotModel();
                                            goodsServiceHotModel.classify_id = jsonArray.getJSONObject(i).getString("classify_id");
                                            goodsServiceHotModel.name = jsonArray.getJSONObject(i).getString("name");
//                                            goodsServiceHotModel.size_id = jsonArray.getJSONObject(i).getString("size_id");
                                            goodsServiceHotModel.logo = jsonArray.getJSONObject(i).getString("logo");
//                                            goodsServiceHotModel.sort = jsonArray.getJSONObject(i).getString("sort");
//                                            goodsServiceHotModel.parent_id = jsonArray.getJSONObject(i).getString("parent_id");
//                                            goodsServiceHotModel.is_top = jsonArray.getJSONObject(i).getString("is_top");
                                            goodsServiceHotModelList.add(goodsServiceHotModel);
                                        }
                                        if (jsonArray.length() == 6) {
                                            GoodsServiceHotModel goodsServiceHotModel = new GoodsServiceHotModel();
                                            goodsServiceHotModel.name = "更多";
                                            goodsServiceHotModelList.add(goodsServiceHotModel);
                                        }
                                        showHotData();
                                    }


                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }


    /**
     * 显示热门商品数据
     */
    void showHotData() {
        hot_fragment_goods_head.setAdapter(new SSS_Adapter<GoodsServiceHotModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_goods_service_hot_textview, goodsServiceHotModelList) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsServiceHotModel bean, SSS_Adapter instance) {
                ((TextView) helper.getView(R.id.text_item_fragment_goods_service_hot_textview)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                ((TextView) helper.getView(R.id.text_item_fragment_goods_service_hot_textview)).getPaint().setAntiAlias(true);//抗锯齿
                helper.setText(R.id.text_item_fragment_goods_service_hot_textview, bean.name);
                ((TextView) helper.getView(R.id.text_item_fragment_goods_service_hot_textview)).setLayoutParams(new LinearLayout.LayoutParams(
                        getActivity().getWindowManager().getDefaultDisplay().getWidth() / 7,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ((TextView) helper.getView(R.id.text_item_fragment_goods_service_hot_textview)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        });


    }


    /**
     * 车品子分类(竖向列表)
     */
    public void classify_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_goods(
                    new JSONObject()
                            .put("classify_id", goodsClassifyModelList.get(currentFirstSeclect).id)
                            .put("gps", Config.latitude + "," + Config.longitude)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ListViewShowGoodsServiceModel listViewShowGoodsServiceModel = new ListViewShowGoodsServiceModel();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                            listViewShowGoodsServiceModel.classify_id = jsonObject1.getString("classify_id");
                                            listViewShowGoodsServiceModel.name = jsonObject1.getString("name");

                                            JSONArray jsonArray1 = jsonObject1.getJSONArray("list");
                                            if (jsonArray1.length() > 0) {
                                                List<GoodsModel> goodsModelList = new ArrayList<>();
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    GoodsModel goodsModel = new GoodsModel();
                                                    goodsModel.goods_id = jsonArray1.getJSONObject(j).getString("goods_id");
                                                    goodsModel.title = jsonArray1.getJSONObject(j).getString("title");
                                                    goodsModel.slogan = jsonArray1.getJSONObject(j).getString("slogan");
                                                    goodsModel.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                    goodsModel.cost_price = jsonArray1.getJSONObject(j).getString("cost_price");
                                                    goodsModel.price = jsonArray1.getJSONObject(j).getString("price");
                                                    goodsModel.sell = jsonArray1.getJSONObject(j).getString("sell");
                                                    goodsModel.member_id = jsonArray1.getJSONObject(j).getString("member_id");
                                                    goodsModel.distance = jsonArray1.getJSONObject(j).getString("distance");
                                                    goodsModelList.add(goodsModel);
                                                }
                                                listViewShowGoodsServiceModel.list = goodsModelList;
                                            }
                                            showGoodsServiceModelList.add(listViewShowGoodsServiceModel);

                                        }

                                        showGoodsList(showGoodsServiceModelList);


                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }


    void startActivity(String classify_id, String title) {
        switch (type) {
            case "1":
                if (getBaseFragmentActivityContext() != null) {

                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
                            .putExtra("classify_id", classify_id)
                            .putExtra("type",type)
                            .putExtra("title", title)
                    );

                }
                break;
            case "2":

                break;
        }
    }

    /**
     * 创建商品列表
     *
     * @param list
     */
    void showGoodsList(final List<ListViewShowGoodsServiceModel> list) {

        for (int i = 0; i < list.size(); i++) {
            final ListViewShowGoodsService listViewShowGoodsService = new ListViewShowGoodsService(getBaseFragmentActivityContext());
            listViewShowGoodsService.setTag(2);
            listViewShowGoodsServices.add(listViewShowGoodsService);
            final int finalI1 = i;
            listViewShowGoodsService.setListViewShowGoodsServiceOperationCallBack(new ListViewShowGoodsService.ListViewShowGoodsServiceOperationCallBack() {
                @Override
                public void onClickMore_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title, int pager, int count, int number, ListViewShowGoodsService listViewShowGoodsService) {
                    get_goods(classify_id, listViewShowGoodsService, list.get(finalI1).list);
                }

                @Override
                public void onClickTitle_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title) {
                    startActivity(classify_id, title);
                }
            });

            final int finalI = i;
            listViewShowGoodsService.create(list.get(i).name, list.get(i).classify_id,list.get(i).page, list.get(i).count, list.get(i).number, new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter) {

                @Override
                protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean, SSS_Adapter instance) {
                    helper.setItemChildClickListener(R.id.click_item_goods_service_list_adapter);
                    addImageViewList(FrescoUtils.showImage(false, 50, 50, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_goods_service_list_adapter)), 0f));
                    helper.setText(R.id.title_item_goods_service_list_adapter, bean.title);
                    helper.setText(R.id.slogan_item_goods_service_list_adapter, bean.slogan);
                    helper.setText(R.id.price_item_goods_service_list_adapter, "¥" + bean.price);
                    helper.setText(R.id.distance_item_goods_service_list_adapter, bean.distance);


                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            }, new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    try {
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                                    .putExtra("goods_id", list.get(finalI).list.get(position).goods_id)
                                    .putExtra("type", type));
                        }
                    } catch (IndexOutOfBoundsException e) {
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "IndexOutOfBoundsException" + e.getMessage());
                    }
                }
            });
            listViewShowGoodsService.setData(list.get(i).list);
            scollLinearlayoutFragmentGoods.addView(listViewShowGoodsService);
        }
    }


    /**
     * 清除视图
     */
    void clearView() {
        cateList.clear();
        for (int i = 0; i < listViewShowGoodsServices.size(); i++) {
            scollLinearlayoutFragmentGoods.removeView(listViewShowGoodsServices.get(i));
        }
        showGoodsServiceModelList.clear();
    }

    /**
     * 获取商品列表
     */
    public void get_goods(String classify_id, final ListViewShowGoodsService listViewShowGoodsService, final List<GoodsModel> list) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_goods(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p", (Integer) listViewShowGoodsService.getTag())
                            .put("gps", Config.latitude + "," + Config.longitude)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    LogUtils.e(list.size());
                                    if (jsonArray.length() > 0) {
                                        int a = (Integer) listViewShowGoodsService.getTag();
                                        a++;
                                        listViewShowGoodsService.setTag(a);
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            GoodsModel goodsModel = new GoodsModel();
                                            goodsModel.goods_id = jsonArray.getJSONObject(j).getString("goods_id");
                                            goodsModel.title = jsonArray.getJSONObject(j).getString("title");
                                            goodsModel.slogan = jsonArray.getJSONObject(j).getString("slogan");
                                            goodsModel.master_map = jsonArray.getJSONObject(j).getString("master_map");
                                            goodsModel.cost_price = jsonArray.getJSONObject(j).getString("cost_price");
                                            goodsModel.price = jsonArray.getJSONObject(j).getString("price");
                                            goodsModel.sell = jsonArray.getJSONObject(j).getString("sell");
                                            goodsModel.member_id = jsonArray.getJSONObject(j).getString("member_id");
                                            goodsModel.distance = jsonArray.getJSONObject(j).getString("distance");
                                            list.add(goodsModel);
                                        }
                                        listViewShowGoodsService.setData(list);
                                    }
                                    LogUtils.e(list.size());
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        clearView();
        goods_subclass();//TAB
        classify_goods();//列表
    }

    @Override
    public void onLoadMore() {

    }
}
