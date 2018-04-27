package com.sss.car.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageAdapter;
import com.blankj.utilcode.customwidget.Tab.CustomTab.ViewHolder;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.custom.ListViewShowGoodsService;
import com.sss.car.custom.TAB;
import com.sss.car.dao.OnRefreshCallBack;
import com.sss.car.model.CateModel;
import com.sss.car.model.GoodsModel;
import com.sss.car.model.GoodsServiceHotModel;
import com.sss.car.model.ListViewShowGoodsServiceModel;
import com.sss.car.model.TopTabModel;
import com.sss.car.view.ActivityGoodsList;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityGoodsServiceListPublic;
import com.sss.car.view.ActivityMore;
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

import static cn.jpush.android.api.JPushInterface.a.t;
import static com.sss.car.Config.classify_id;
import static com.sss.car.R.id.listViewShowGoodsService;


/**
 * Created by leilei on 2017/11/30.
 */

@SuppressWarnings("ALL")
public class FragmentGoodsServiceChild extends BaseFragment {
    @BindView(R.id.scoll_linearlayout_fragment_goods)
    LinearLayout scollLinearlayoutFragmentGoods;
    Unbinder unbinder;


    YWLoadingDialog ywLoadingDialog;


    /*一级父类菜单集合*/
    List<TopTabModel> goodsClassifyModelList = new ArrayList<>();


    /*二级菜单集合*/
    List<CateModel> cateList = new ArrayList<>();


    /*视图列表集合*/
    List<ListViewShowGoodsService> listViewShowGoodsServices = new ArrayList<>();

    /*商品列表集合*/
    List<ListViewShowGoodsServiceModel> showGoodsServiceModelList = new ArrayList<>();


    /*热门商品集合*/
    List<GoodsServiceHotModel> goodsServiceHotModelList = new ArrayList<>();


    String type = "1";//1车品2车服

    int current = 0;//当前viewpager 的索引(0为综合)
    @BindView(R.id.viewpager_up_fragment_goods_head)
    BannerVariation viewpager_up_fragment_goods_head;
    @BindView(R.id.tab_fragment_goods_head)
    TAB tab_fragment_goods_head;
    @BindView(R.id.viewpager_down_fragment_goods_head)
    BannerVariation viewpager_down_fragment_goods_head;


    public String classify_id;
    @BindView(R.id.scoll_view_fragment_goods)
    PullToRefreshScrollView scollViewFragmentGoods;
    @BindView(R.id.click_top)
    ImageView clickTop;
    @BindView(R.id.enter)
    TextView enter;
    @BindView(R.id.activity_parent)
    RelativeLayout activityParent;
    OnRefreshCallBack onRefreshCallBack;
    @Override
    public void onDestroy() {
        if (tab_fragment_goods_head != null) {
            tab_fragment_goods_head.clear();
        }
        tab_fragment_goods_head = null;
        if (viewpager_down_fragment_goods_head != null) {
            viewpager_down_fragment_goods_head.clear();
        }
        viewpager_down_fragment_goods_head = null;
        if (viewpager_up_fragment_goods_head != null) {
            viewpager_up_fragment_goods_head.clear();
        }
        viewpager_up_fragment_goods_head = null;
        scollLinearlayoutFragmentGoods = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (goodsClassifyModelList != null) {
            goodsClassifyModelList.clear();
        }
        if (cateList != null) {
            cateList.clear();
        }
        if (listViewShowGoodsServices != null) {
            for (int i = 0; i < listViewShowGoodsServices.size(); i++) {
                if (listViewShowGoodsServices.get(i) != null) {
                    listViewShowGoodsServices.get(i).clear();
                }
            }
            listViewShowGoodsServices.clear();
        }
        if (showGoodsServiceModelList != null) {
            showGoodsServiceModelList.clear();
        }
        if (goodsServiceHotModelList != null) {
            goodsServiceHotModelList.clear();
        }
        super.onDestroy();
    }

    public FragmentGoodsServiceChild() {
    }

    public FragmentGoodsServiceChild setOnRefreshCallBack(OnRefreshCallBack onRefreshCallBack) {
        this.onRefreshCallBack = onRefreshCallBack;
        return this;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service_child;
    }

    public FragmentGoodsServiceChild(List<TopTabModel> goodsClassifyModelList, int current, String classify_id, String type) {
        this.goodsClassifyModelList = goodsClassifyModelList;
        this.current = current;
        this.classify_id = classify_id;
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
                                initView();
                                request();
                                viewpager_up_fragment_goods_head.setDelayTime(Config.flash);
                                viewpager_down_fragment_goods_head.setDelayTime(Config.flash);

                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }


    public void request() {
//        if (showGoodsServiceModelList != null) {
//            showGoodsServiceModelList.clear();
//        }
//        if (scollLinearlayoutFragmentGoods != null) {
//            scollLinearlayoutFragmentGoods.removeAllViews();
//        }
//        if (isLoad) {
////            clearView();
////            classify_goods();
////            goods_subclass();
//            if (current == 0) {
//                activityParent.setVisibility(View.VISIBLE);
//                classify_four();
//            } else {
//                activityParent.setVisibility(View.GONE);
//                goods_subclass();
//            }
//            classify_goods();
//        }


        if (showGoodsServiceModelList != null) {
            showGoodsServiceModelList.clear();
        }
        if (scollLinearlayoutFragmentGoods != null) {
            scollLinearlayoutFragmentGoods.removeAllViews();
        }
        if (current == 0) {
            activityParent.setVisibility(View.VISIBLE);
            classify_four();
        } else {
            activityParent.setVisibility(View.GONE);
            goods_subclass();
        }
        if ("1".equals(type)) {
            initAdv("3", classify_id, viewpager_up_fragment_goods_head);
            initAdv("5", classify_id, viewpager_down_fragment_goods_head);
        } else if ("2".equals(type)) {
            initAdv("4", classify_id, viewpager_up_fragment_goods_head);
            initAdv("6", classify_id, viewpager_down_fragment_goods_head);
        }
        classify_goods();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isLoad && viewpager_down_fragment_goods_head != null) {
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
        unbinder.unbind();unbinder=null;
    }



    /**
     * 初始化视图
     */
    void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollViewFragmentGoods.getRefreshableView().setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > Config.scoll_HighRestriction) {
                        clickTop.setVisibility(View.VISIBLE);
                    } else {
                        clickTop.setVisibility(View.GONE);

                    }
                }
            });

        }
        scollViewFragmentGoods.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        scollViewFragmentGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                request();
//                if (onRefreshCallBack!=null){
//                    onRefreshCallBack.onRefresh(FragmentGoodsServiceChild.this);
//                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        clickTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scollViewFragmentGoods.getRefreshableView().smoothScrollTo(0, 0);
            }
        });



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySaleAndSeckill.class)
                            .putExtra("type", type));
                }
            }
        });

    }

//    void startActivity(String classify_id, String title) {
////        switch (type) {
////            case "1":
//////                if (getBaseFragmentActivityContext() != null) {
//////                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
//////                            .putExtra("classify_id", classify_id)
//////                            .putExtra("type", type)
//////                            .putExtra("title", title)
//////                    );
//////                }
////
////                if ("0".equals(classify_id)) {
////                    if (getBaseFragmentActivityContext() != null) {
////                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityServiceGoodsHotMore.class)
////                                .putExtra("type", type)
////                                .putExtra("classify_id", classify_id)
////                        );
////                    }
////                } else {
////                    if (getBaseFragmentActivityContext() != null) {
////                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceListPublic.class)
////                                .putExtra("classify_id", classify_id)
////                                .putExtra("title", title)
////                        );
////                    }
////                }
////                break;
////            case "2":
////
////                break;
////        }
//
//LogUtils.e(classify_id);
//        if ("0".equals(classify_id)) {
//
//        } else {
//            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
//                    .putExtra("classify_id", classify_id)
//                    .putExtra("type", type)
//                    .putExtra("title", title)
//            );
//        }
//    }

    private void initAdv(String site_id, String classify_id, final BannerVariation bannerVariation) {
        AdvertisementManager.advertisement(site_id, classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                if (bannerVariation!=null){
                    bannerVariation.stopAutoPlay();
                }
                bannerVariation
                        .setImages(list)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setDelayTime(Config.flash)
                        .setImageLoader(new ImageLoaderInterface() {
                            @Override
                            public void displayImage(Context context, final Object path, View imageView) {
                                imageView.setTag(R.id.glide_tag, ((AdvertisementModel) path).picture);
                                addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AdvertisementManager.jump(((AdvertisementModel) path),getBaseFragmentActivityContext());
                                    }
                                });
                            }

                            @Override
                            public View createImageView(Context context) {
                                return null;
                            }
                        });
                bannerVariation.start();

                bannerVariation.startAutoPlay();
            }


        });
    }

    /**
     * 获取车品子分类(TAB)综合
     */
    public void classify_four() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_four(
                    new JSONObject()
                            .put("type", type)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
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
                                    if (cateList.size() > 0) {
                                        showTAB_Data();
                                        if (tab_fragment_goods_head != null) {
                                            tab_fragment_goods_head.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
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
        if (ywLoadingDialog == null) {
            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.subclass(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
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
                                    if (cateList.size() > 0) {
                                        showTAB_Data();
                                        tab_fragment_goods_head.setVisibility(View.VISIBLE);
                                    }

                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
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
                addImageViewList(FrescoUtils.showImage(false, 100, 100, cateList.get(i).logo, ((SimpleDraweeView) viewHolder.getView(R.id.pic_item_tab)), 0f));

                ((LinearLayout) viewHolder.getView(R.id.click_item_tab)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cateList.size() > 0) {
                            if ("0".equals(cateList.get(i).cate_id)) {
//                                if (getBaseFragmentActivityContext() != null) {
//                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityServiceGoodsHotMore.class)
//                                            .putExtra("type", type)
//                                            .putExtra("classify_id", classify_id));
//                                }
                                if (current == 0) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMore.class)
                                                .putExtra("type", type));
                                    }
                                } else {
                                    if (getBaseFragmentActivityContext() != null) {
                                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMore.class)
                                                .putExtra("classify_id", classify_id)
                                                .putExtra("type", type));
                                    }
                                }

                            } else {
                                if (current == 0) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceListPublic.class)
                                            .putExtra("classify_id", cateList.get(i).cate_id)
                                            .putExtra("showHead", false)
                                            .putExtra("type", type)
                                            .putExtra("title", cateList.get(i).cate_name)
                                    );
                                } else {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
                                            .putExtra("classify_id", cateList.get(i).cate_id)
                                            .putExtra("type", type)
                                            .putExtra("title", cateList.get(i).cate_name));
                                }

                            }
                        }

                    }
                });
            }
        }, cateList.size(), getActivity());
    }

    /**
     * 车品子分类(竖向列表--推广)
     */
    public void classify_goods(String type, String gps) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.synthesize(
                    new JSONObject()
                            .put("type", type)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
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
                                            listViewShowGoodsServiceModel.count = jsonObject1.getInt("count");
                                            listViewShowGoodsServiceModel.number = jsonObject1.getInt("number");
                                            listViewShowGoodsServiceModel.page = jsonObject1.getInt("page");
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

                                        if (ywLoadingDialog != null) {
                                            ywLoadingDialog.disMiss();
                                        }
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
     * 车品子分类(竖向列表)
     */
    public void classify_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }
        if (goodsClassifyModelList == null || goodsClassifyModelList.size() == 0 || goodsClassifyModelList.get(current) == null) {
            return;
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_goods(
                    new JSONObject()
                            .put("classify_id", goodsClassifyModelList.get(current).id)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
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
                                            listViewShowGoodsServiceModel.page = jsonObject1.getInt("page");
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
                                        if (ywLoadingDialog != null) {
                                            ywLoadingDialog.disMiss();
                                        }

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
     * 创建商品列表
     *
     * @param list
     */
    void showGoodsList(final List<ListViewShowGoodsServiceModel> list) {
        if (scollLinearlayoutFragmentGoods != null) {
            for (int i = 0; i < list.size(); i++) {
                final ListViewShowGoodsService listViewShowGoodsService = new ListViewShowGoodsService(getBaseFragmentActivityContext());
                listViewShowGoodsServices.add(listViewShowGoodsService);
                final int finalI1 = i;
                listViewShowGoodsService.setListViewShowGoodsServiceOperationCallBack(new ListViewShowGoodsService.ListViewShowGoodsServiceOperationCallBack() {
                    @Override
                    public void onClickMore_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title, int pager, int count, int number, ListViewShowGoodsService listViewShowGoodsService) {
                        get_goods(classify_id, listViewShowGoodsService, list.get(finalI1).list, pager, count, number);
                    }

                    @Override
                    public void onClickTitle_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsList.class)
                                .putExtra("classify_id", classify_id)
                                .putExtra("type", type)
                                .putExtra("title", title));
                    }
                });

                final int finalI = i;
                listViewShowGoodsService.create(list.get(i).name, list.get(i).classify_id, list.get(i).page, list.get(i).count, list.get(i).number, new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter) {

                    @Override
                    protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean, SSS_Adapter instance) {
                        helper.setItemChildClickListener(R.id.click_item_goods_service_list_adapter);
                        addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_goods_service_list_adapter)), 0f));
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
                listViewShowGoodsService.setData(list.get(finalI).list, current == 0);
                if (list.get(finalI).list.size() >= list.get(finalI).number) {
                    if (list.get(finalI).page > 1) {
                        listViewShowGoodsService.hideModeButton(false);
                    } else {
                        listViewShowGoodsService.hideModeButton(true);
                    }
                } else {
                    listViewShowGoodsService.hideModeButton(true);
                }


                scollLinearlayoutFragmentGoods.addView(listViewShowGoodsService);
            }


        }
    }

    /**
     * 清除视图
     */
    public void clearView() {
        cateList.clear();
        if (scollLinearlayoutFragmentGoods != null) {
            for (int i = 0; i < listViewShowGoodsServices.size(); i++) {
                scollLinearlayoutFragmentGoods.removeView(listViewShowGoodsServices.get(i));
            }
            showGoodsServiceModelList.clear();
        }
    }


    /**
     * 获取商品列表(更多)
     */
    public void get_goods(String classify_id, final ListViewShowGoodsService listViewShowGoodsService, final List<GoodsModel> list, final int pager, final int count, final int number) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.top_goods_list(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p", listViewShowGoodsService.p)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    LogUtils.e(list.size());
                                    if (jsonArray.length() > 0) {
                                        listViewShowGoodsService.p++;
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
                                        if (list.size() >= number) {
                                            listViewShowGoodsService.hideModeButton(true);
                                        }
                                        listViewShowGoodsService.setData(list, current == 0);
                                    }

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

}
