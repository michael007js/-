package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.GoodsModel;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityShopInfoAllFilter;

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
 * 商铺全部宝贝
 * Created by leilei on 2017/9/17.
 */

@SuppressLint("ValidFragment")
public class FragmentShopInfoAll extends BaseFragment {
    @BindView(R.id.show_synthesis_fragment_shop_info_all)
    TextView showSynthesisFragmentShopInfoAll;
    @BindView(R.id.click_synthesis_fragment_shop_info_all)
    LinearLayout clickSynthesisFragmentShopInfoAll;
    @BindView(R.id.show_price_fragment_shop_info_all)
    TextView showPriceFragmentShopInfoAll;
    @BindView(R.id.click_price_fragment_shop_info_all)
    LinearLayout clickPriceFragmentShopInfoAll;
    @BindView(R.id.show_filter_fragment_shop_info_all)
    TextView showFilterFragmentShopInfoAll;
    @BindView(R.id.click_filter_fragment_shop_info_all)
    LinearLayout clickFilterFragmentShopInfoAll;
    @BindView(R.id.listview_fragment_shop_info_all)
    InnerListview listviewFragmentShopInfoAll;
    @BindView(R.id.img_price_fragment_shop_info_all)
    ImageView imgPriceFragmentShopInfoAll;
    Unbinder unbinder;

    PullToRefreshScrollView refreshLoadMoreLayout;
    String price = "asc";//asc（升序）desc（降序））

    String type;
    SSS_Adapter sss_adapter;


    List<GoodsModel> goodsModelList = new ArrayList<>();
    public int p = 1;
    String shop_id;

    YWLoadingDialog ywLoadingDialog;

    public FragmentShopInfoAll() {
    }

    public FragmentShopInfoAll(String shop_id, PullToRefreshScrollView refreshLoadMoreLayout ) {
        this.shop_id = shop_id;
        this.refreshLoadMoreLayout = refreshLoadMoreLayout;
    }

    @Override
    public void onDestroy() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        if (goodsModelList != null) {
            goodsModelList.clear();
        }
        goodsModelList = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        showSynthesisFragmentShopInfoAll = null;
        clickSynthesisFragmentShopInfoAll = null;
        showPriceFragmentShopInfoAll = null;
        clickPriceFragmentShopInfoAll = null;
        showFilterFragmentShopInfoAll = null;
        clickFilterFragmentShopInfoAll = null;
        listviewFragmentShopInfoAll = null;
        imgPriceFragmentShopInfoAll = null;
        refreshLoadMoreLayout = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_info_all;
    }

    @Override
    protected void lazyLoad() {
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
                            shop_all();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    @OnClick({R.id.click_synthesis_fragment_shop_info_all, R.id.click_price_fragment_shop_info_all, R.id.click_filter_fragment_shop_info_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_synthesis_fragment_shop_info_all:
                showSynthesisFragmentShopInfoAll.setTextColor(getResources().getColor(R.color.mainColor));
                showPriceFragmentShopInfoAll.setTextColor(getResources().getColor(R.color.textColor));
                break;
            case R.id.click_price_fragment_shop_info_all:
                showSynthesisFragmentShopInfoAll.setTextColor(getResources().getColor(R.color.textColor));
                showPriceFragmentShopInfoAll.setTextColor(getResources().getColor(R.color.mainColor));
                if ("asc".equals(price)) {
                    price = "desc";
                    addImageViewList(GlidUtils.glideLoad(false, imgPriceFragmentShopInfoAll, getBaseFragmentActivityContext(), R.mipmap.logo_triangle_arrows_down));
                } else {
                    price = "asc";
                    addImageViewList(GlidUtils.glideLoad(false, imgPriceFragmentShopInfoAll, getBaseFragmentActivityContext(), R.mipmap.logo_triangle_arrows_up));
                }
                p = 1;
                shop_all();
                break;
            case R.id.click_filter_fragment_shop_info_all:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfoAllFilter.class)
                    .putExtra("mode","goods"));
                }
                break;
        }
    }


    void init() {
        sss_adapter = new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_goods_service_list_adapter, goodsModelList) {

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
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                            .putExtra("type",goodsModelList.get(position).type)
                            .putExtra("goods_id", goodsModelList.get(position).goods_id)
                    );
                }

            }
        });
        listviewFragmentShopInfoAll.setAdapter(sss_adapter);

    }
    public void shop_all() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.shop_all(
                    new JSONObject()
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("shop_id", shop_id)
                            .put("price", price)
                            .put("p", p)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshLoadMoreLayout != null) {
                                refreshLoadMoreLayout.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshLoadMoreLayout != null) {
                                refreshLoadMoreLayout.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {

                                    if (p == 1) {
                                        goodsModelList.clear();
                                    }
                                    p++;
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        GoodsModel goodsModel = new GoodsModel();
                                        goodsModel.goods_id = jsonArray.getJSONObject(j).getString("goods_id");
                                        goodsModel.title = jsonArray.getJSONObject(j).getString("title");
                                        goodsModel.slogan = jsonArray.getJSONObject(j).getString("slogan");
                                        goodsModel.master_map = jsonArray.getJSONObject(j).getString("master_map");
                                        goodsModel.cost_price = jsonArray.getJSONObject(j).getString("cost_price");
                                        goodsModel.price = jsonArray.getJSONObject(j).getString("price");
                                        goodsModel.sell = jsonArray.getJSONObject(j).getString("sell");
                                        goodsModel.type = jsonArray.getJSONObject(j).getString("type");
                                        goodsModel.member_id = jsonArray.getJSONObject(j).getString("member_id");
                                        goodsModel.distance = jsonArray.getJSONObject(j).getString("distance");
                                        goodsModelList.add(goodsModel);
                                    }

                                    sss_adapter.setList(goodsModelList);
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
}
