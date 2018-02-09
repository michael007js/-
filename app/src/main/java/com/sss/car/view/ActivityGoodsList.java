package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.sss.car.model.CateModel;
import com.sss.car.model.GoodsModel;
import com.sss.car.model.ListViewShowGoodsServiceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.Config.classify_id;
import static com.sss.car.R.id.tab_fragment_goods_head;

/**
 * Created by leilei on 2017/9/13.
 */

@SuppressWarnings("ALL")
public class ActivityGoodsList extends BaseActivity {

    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.viewpager_up_fragment_goods_head)
    BannerVariation bannerVariation;
    @BindView(tab_fragment_goods_head)
    TAB tabFragmentGoodsHead;
    @BindView(R.id.scoll_linearlayout_fragment_goods)
    LinearLayout scollLinearlayoutFragmentGoods;
    @BindView(R.id.scoll_view_fragment_goods)
    PullToRefreshScrollView scollViewFragmentGoods;
    @BindView(R.id.click_top)
    ImageView clickTop;
    @BindView(R.id.activity_goods_list)
    LinearLayout activityGoodsList;
    YWLoadingDialog ywLoadingDialog;


    /*二级菜单集合*/
    List<CateModel> cateList = new ArrayList<>();


    /*商品列表集合*/
    List<ListViewShowGoodsServiceModel> showGoodsServiceModelList = new ArrayList<>();


    /*视图列表集合*/
    List<ListViewShowGoodsService> listViewShowGoodsServices = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        customInit(activityGoodsList, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        String site_id = null;
        if ("1".equals(getIntent().getExtras().getString("type"))) {
            site_id = "11";
        } else {
            site_id = "12";
        }
        AdvertisementManager.advertisement(site_id, getIntent().getExtras().getString("classify_id"), new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                bannerVariation
                        .setImages(list)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setDelayTime(5000)
                        .setImageLoader(new ImageLoaderInterface() {
                            @Override
                            public void displayImage(Context context, Object path, View imageView) {
                                imageView.setTag(R.id.glide_tag, ((AdvertisementModel) path).picture);
                                addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

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
                titleTop.setText(getIntent().getExtras().getString("title"));
            }
        });
        if (showGoodsServiceModelList != null) {
            showGoodsServiceModelList.clear();
        }
        clearView();
        goods_subclass();
        classify_goods();
        scollViewFragmentGoods.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        scollViewFragmentGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (showGoodsServiceModelList != null) {
                    showGoodsServiceModelList.clear();
                }
                clearView();
                goods_subclass();
                classify_goods();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

    }

    @OnClick({R.id.back_top, R.id.click_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_top:
                scollViewFragmentGoods.getRefreshableView().smoothScrollTo(0, 0);
                break;
        }
    }

    public void goods_subclass() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                ywLoadingDialog.show();
            }
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.subclass(
                    new JSONObject()
                            .put("classify_id", getIntent().getExtras().getString("classify_id"))
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (scollViewFragmentGoods != null) {
                                scollViewFragmentGoods.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

    void showTAB_Data() {
        tabFragmentGoodsHead.setAdapter(new HorizontalPageAdapter(getBaseActivityContext(), cateList, R.layout.item_tab) {
            @Override
            public void bindViews(ViewHolder viewHolder, Object o, final int i) {
                ((TextView) viewHolder.getView(R.id.text_item_tab)).setText(((CateModel) o).cate_name);
                addImageViewList(FrescoUtils.showImage(false, 100, 100, cateList.get(i).logo, ((SimpleDraweeView) viewHolder.getView(R.id.pic_item_tab)), 0f));

                ((LinearLayout) viewHolder.getView(R.id.click_item_tab)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cateList.size() > 0) {
                            if ("0".equals(cateList.get(i).cate_id)) {
//                                if (getBaseActivityContext() != null) {
//                                    startActivity(new Intent(getBaseActivityContext(), ActivityServiceGoodsHotMore.class)
//                                            .putExtra("type", getIntent().getExtras().getString("type"))
//                                            .putExtra("classify_id",  cateList.get(i).cate_name));
//                                }
                                if (getBaseActivityContext() != null) {
                                    startActivity(new Intent(getBaseActivityContext(), ActivityMore.class)
                                            .putExtra("type", getIntent().getExtras().getString("type"))
                                            .putExtra("classify_id",getIntent().getExtras().getString("classify_id") ));
                                }
                            } else {
                                startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceListPublic.class)
                                        .putExtra("classify_id", cateList.get(i).cate_id)
                                        .putExtra("showHead", false)
                                        .putExtra("type", getIntent().getExtras().getString("type"))
                                        .putExtra("title", cateList.get(i).cate_name)
                                );
                            }

                        }

                    }
                });
            }
        }, cateList.size(), getBaseActivity());
    }

    /**
     * 车品子分类(竖向列表)
     */
    public void classify_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                ywLoadingDialog.show();
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_goods(
                    new JSONObject()
                            .put("classify_id", getIntent().getExtras().getString("classify_id"))
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
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
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
                final ListViewShowGoodsService listViewShowGoodsService = new ListViewShowGoodsService(getBaseActivityContext());
                listViewShowGoodsServices.add(listViewShowGoodsService);
                final int finalI1 = i;
                listViewShowGoodsService.setListViewShowGoodsServiceOperationCallBack(new ListViewShowGoodsService.ListViewShowGoodsServiceOperationCallBack() {
                    @Override
                    public void onClickMore_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title, int pager, int count, int number, ListViewShowGoodsService listViewShowGoodsService) {
                        get_goods(classify_id,listViewShowGoodsService,list.get(finalI1).list);
                    }

                    @Override
                    public void onClickTitle_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceListPublic.class)
                                    .putExtra("classify_id", classify_id)
                                    .putExtra("showHead", false)
                                    .putExtra("type", getIntent().getExtras().getString("type"))
                                    .putExtra("title", title)
                            );
                        }
                    }
                });

                final int finalI = i;
                listViewShowGoodsService.create(list.get(i).name, list.get(i).classify_id, list.get(i).page, list.get(i).count, list.get(i).number, new SSS_Adapter<GoodsModel>(getBaseActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter) {

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
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                        .putExtra("goods_id", list.get(finalI).list.get(position).goods_id)
                                        .putExtra("type", getIntent().getExtras().getString("type")));
                            }
                        } catch (IndexOutOfBoundsException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "IndexOutOfBoundsException" + e.getMessage());
                        }
                    }
                });
                listViewShowGoodsService.setData(list.get(finalI).list);
                if (list.get(finalI).list.size() == 5) {
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
     * 获取商品列表(更多)
     */
    public void get_goods(String classify_id, final ListViewShowGoodsService listViewShowGoodsService, final List<GoodsModel> list) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
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
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                            listViewShowGoodsService.setData(list);
                                        }
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
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

}


