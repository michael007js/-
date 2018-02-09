package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.GoodsModel;

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
 * Created by leilei on 2017/9/19.
 */

public class ActivityShopIinfoAllFilterLaterList extends BaseActivity implements RefreshLoadMoreLayout.CallBack{
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_hot_activity_shop_info_all_filter_later_list)
    TextView showHotActivityShopInfoAllFilterLaterList;
    @BindView(R.id.image_hot_activity_shop_info_all_filter_later_list)
    ImageView imageHotActivityShopInfoAllFilterLaterList;
    @BindView(R.id.click_hot_activity_shop_info_all_filter_later_list)
    LinearLayout clickHotActivityShopInfoAllFilterLaterList;
    @BindView(R.id.show_price_activity_shop_info_all_filter_later_list)
    TextView showPriceActivityShopInfoAllFilterLaterList;
    @BindView(R.id.image_price_activity_shop_info_all_filter_later_list)
    ImageView imagePriceActivityShopInfoAllFilterLaterList;
    @BindView(R.id.click_price_activity_shop_info_all_filter_later_list)
    LinearLayout clickPriceActivityShopInfoAllFilterLaterList;
    @BindView(R.id.show_collect_activity_shop_info_all_filter_later_list)
    TextView showCollectActivityShopInfoAllFilterLaterList;
    @BindView(R.id.image_collect_activity_shop_info_all_filter_later_list)
    ImageView imageCollectActivityShopInfoAllFilterLaterList;
    @BindView(R.id.click_collect_activity_shop_info_all_filter_later_list)
    LinearLayout clickCollectActivityShopInfoAllFilterLaterList;
    @BindView(R.id.listview_activity_shop_info_all_filter_later_list)
    InnerListview listviewActivityShopInfoAllFilterLaterList;
    @BindView(R.id.refresh_activity_shop_info_all_filter_later_list)
    RefreshLoadMoreLayout refreshActivityShopInfoAllFilterLaterList;
    @BindView(R.id.activity_shop_info_all_filter_later_list)
    LinearLayout activityShopInfoAllFilterLaterList;
    @BindView(R.id.scollview_activity_shop_info_all_filter_later_list)
    ScrollView scollviewActivityShopInfoAllFilterLaterList;
    @BindView(R.id.top_activity_shop_info_all_filter_later_list)
    ImageView topActivityShopInfoAllFilterLaterList;


    int p = 1;
    List<GoodsModel> goodsModelList = new ArrayList<>();

    YWLoadingDialog ywLoadingDialog;


    SSS_Adapter sss_adapter;


    String looks="asc",price,collect;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (goodsModelList != null) {
            goodsModelList.clear();
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        goodsModelList = null;

        backTop = null;
        titleTop = null;
        showHotActivityShopInfoAllFilterLaterList = null;
        imageHotActivityShopInfoAllFilterLaterList = null;
        clickHotActivityShopInfoAllFilterLaterList = null;
        showPriceActivityShopInfoAllFilterLaterList = null;
        imagePriceActivityShopInfoAllFilterLaterList = null;
        clickPriceActivityShopInfoAllFilterLaterList = null;
        showCollectActivityShopInfoAllFilterLaterList = null;
        imageCollectActivityShopInfoAllFilterLaterList = null;
        clickCollectActivityShopInfoAllFilterLaterList = null;
        listviewActivityShopInfoAllFilterLaterList = null;
        refreshActivityShopInfoAllFilterLaterList = null;
        activityShopInfoAllFilterLaterList = null;
        scollviewActivityShopInfoAllFilterLaterList = null;topActivityShopInfoAllFilterLaterList=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info_all_filter_later_list);
        ButterKnife.bind(this);
        customInit(activityShopInfoAllFilterLaterList,false,true,false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误error-1");
            finish();
        }

        titleTop.setText(getIntent().getExtras().getString("name"));


        refreshActivityShopInfoAllFilterLaterList.init(new RefreshLoadMoreLayout.Config(this).canLoadMore(true).canRefresh(true));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollviewActivityShopInfoAllFilterLaterList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topActivityShopInfoAllFilterLaterList.setVisibility(View.VISIBLE);
                    }else {
                        topActivityShopInfoAllFilterLaterList.setVisibility(View.GONE);
                    }
                }
            });
        }


        sss_adapter = new SSS_Adapter<GoodsModel>(getBaseActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter, goodsModelList) {

            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean,SSS_Adapter instance) {
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
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                           .putExtra("goods_id", goodsModelList.get(position).goods_id)
                            .putExtra("type",goodsModelList.get(position).type)
                    );
                }

            }
        });

        listviewActivityShopInfoAllFilterLaterList.setAdapter(sss_adapter);

        goods_list();
    }

    @OnClick({R.id.back_top, R.id.click_hot_activity_shop_info_all_filter_later_list,R.id.top_activity_shop_info_all_filter_later_list, R.id.click_price_activity_shop_info_all_filter_later_list, R.id.click_collect_activity_shop_info_all_filter_later_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_hot_activity_shop_info_all_filter_later_list:
                showHotActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.mainColor));
                showPriceActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                showCollectActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                if ("asc".equals(looks)){
                    looks="desc";
                    addImageViewList(GlidUtils.glideLoad(false,imageHotActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_down));
                }else {
                    looks="asc";
                    addImageViewList(GlidUtils.glideLoad(false,imageHotActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_up));
                }
                price=null;
                collect=null;
                p=1;
                goods_list();
                break;
            case R.id.click_price_activity_shop_info_all_filter_later_list:
                showHotActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                showPriceActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.mainColor));
                showCollectActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                if ("asc".equals(price)){
                    price="desc";
                    addImageViewList(GlidUtils.glideLoad(false,imagePriceActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_down));
                }else{
                    price="asc";
                    addImageViewList(GlidUtils.glideLoad(false,imagePriceActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_up));
                }
                looks=null;
                collect=null;
                p=1;
                goods_list();
                break;
            case R.id.click_collect_activity_shop_info_all_filter_later_list:
                showHotActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                showPriceActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.textColor));
                showCollectActivityShopInfoAllFilterLaterList.setTextColor(getResources().getColor(R.color.mainColor));
                if ("asc".equals(collect)){
                    collect="desc";
                    addImageViewList(GlidUtils.glideLoad(false,imageCollectActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_down));
                }else{
                    collect="asc";
                    addImageViewList(GlidUtils.glideLoad(false,imageCollectActivityShopInfoAllFilterLaterList,getBaseActivityContext(),R.mipmap.logo_arrows_up));
                }
                looks=null;
                price=null;
                p=1;
                goods_list();
                break;
            case R.id.top_activity_shop_info_all_filter_later_list:
                scollviewActivityShopInfoAllFilterLaterList.smoothScrollTo(0,0);
                break;
        }
    }


    /**
     * 获取商品列表
     */
    public void goods_list() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_list(
                    new JSONObject()
                            .put("classify_id",getIntent().getExtras().getString("classify_id") )
                            .put("p", p)
                            .put("looks",looks)
                            .put("price",price)
                            .put("collect",collect)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshActivityShopInfoAllFilterLaterList!=null){
                                refreshActivityShopInfoAllFilterLaterList.stopLoadMore();
                                refreshActivityShopInfoAllFilterLaterList.stopRefresh();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshActivityShopInfoAllFilterLaterList!=null){
                                refreshActivityShopInfoAllFilterLaterList.stopLoadMore();
                                refreshActivityShopInfoAllFilterLaterList.stopRefresh();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            goodsModelList.clear();
                                        }
                                        p++;

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
                                            goodsModelList.add(goodsModel);
                                        }
                                        sss_adapter.setList(goodsModelList);

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

    @Override
    public void onRefresh() {
        p=1;
        goods_list();
    }

    @Override
    public void onLoadMore() {
goods_list();
    }
}
