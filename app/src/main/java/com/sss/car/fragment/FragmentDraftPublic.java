package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListviewDraftGoods;
import com.sss.car.custom.ListviewDraftOrder;
import com.sss.car.custom.ListviewDraftPopularize;
import com.sss.car.custom.ListviewDraftSos;
import com.sss.car.model.DrapGoods;
import com.sss.car.model.DrapOrder;
import com.sss.car.model.DrapPopularize;
import com.sss.car.model.DrapSOS;
import com.sss.car.order.OrderGoodsReadyBuyEdit;
import com.sss.car.order.OrderSOSEdit;
import com.sss.car.order.OrderServiceReadyBuyEdit;
import com.sss.car.popularize.PopularizeEdit;
import com.sss.car.view.ActivityGoodsServiceEdit;
import com.sss.car.view.ActivityShopInfo;

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
 * 草稿箱订单 SOS  商品  推广公用Fragment
 * Created by leilei on 2017/11/4.
 */

@SuppressLint("ValidFragment")
public class FragmentDraftPublic extends BaseFragment {
    public static final int REQUEST_MODE_ORDER = -1;//订单模式
    public static final int REQUEST_MODE_SOS = -2;//SOS模式
    public static final int REQUEST_MODE_GOODS = -3;//SOS模式
    public static final int REQUEST_MODE_POPULARIZE = -4;//推广模式
    @BindView(R.id.listview_fragment_draft_public)
    LinearLayout listviewFragmentDraftPublic;
    Unbinder unbinder;
    public int p = 1;
    YWLoadingDialog ywLoadingDialog;


    int search_mode = REQUEST_MODE_ORDER;

    String keywords;
    boolean autoRequest = true;//是否自动加载,如果是,说明不是搜索模式,反之,则为搜索模式

    List<DrapOrder> drapOrderList = new ArrayList<>();
    public ListviewDraftOrder listviewDraftOrder;

    List<DrapSOS> drapSOSList = new ArrayList<>();
    public ListviewDraftSos listviewDraftSos;

    List<DrapGoods> drapGoodsList = new ArrayList<>();
    ListviewDraftGoods listviewDraftGoods;

    List<DrapPopularize> drapPopularizeList = new ArrayList<>();
    ListviewDraftPopularize listviewDraftPopularize;

    @BindView(R.id.refresh_fragment_draft_public)
    PullToRefreshScrollView refreshFragmentDraftPublic;
    Gson gson = new Gson();
    @BindView(R.id.empty_view)
    SimpleDraweeView emptyView;

    public FragmentDraftPublic(int search_mode, boolean autoRequest) {
        this.search_mode = search_mode;
        this.autoRequest = autoRequest;
    }

    public FragmentDraftPublic() {
    }

    @Override
    public void onDestroy() {
        listviewFragmentDraftPublic = null;
        refreshFragmentDraftPublic = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_draft_public;
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
                                refreshFragmentDraftPublic.setMode(PullToRefreshBase.Mode.BOTH);
                                refreshFragmentDraftPublic.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        p = 1;
                                        if (autoRequest) {
                                            request();
                                        } else {
                                            search(keywords);
                                        }
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                                        if (autoRequest) {
                                            request();
                                        } else {
                                            search(keywords);
                                        }
                                    }
                                });
                                init();
                                request();

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

    public void search(String keywords) {
        p = 1;
        this.keywords = keywords;
        if (search_mode == REQUEST_MODE_ORDER) {
            drapOrderList.clear();
            drafts_order();
        } else if (search_mode == REQUEST_MODE_SOS) {
            drapSOSList.clear();
            drafts_sos();
        } else if (search_mode == REQUEST_MODE_GOODS) {
            drapGoodsList.clear();
            drafts_goods();
        } else if (search_mode == REQUEST_MODE_POPULARIZE) {
            drafts_popularize();
        }
    }


    public void request() {
        if (search_mode == REQUEST_MODE_ORDER) {
            if (autoRequest) {
                drafts_order();
            }
        } else if (search_mode == REQUEST_MODE_SOS) {
            if (autoRequest) {
                drafts_sos();
            }
        } else if (search_mode == REQUEST_MODE_GOODS) {
            if (autoRequest) {
                drafts_goods();
            }
        } else if (search_mode == REQUEST_MODE_POPULARIZE) {
            drafts_popularize();
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

    void init() {
        if (search_mode == REQUEST_MODE_ORDER) {
            listviewDraftOrder = new ListviewDraftOrder(getBaseFragmentActivityContext());
            listviewDraftOrder.setOrientation(OrientationHelper.VERTICAL);
            listviewFragmentDraftPublic.addView(listviewDraftOrder);
            listviewDraftOrder.setOnListviewDraftOrderCallBack(new ListviewDraftOrder.OnListviewDraftOrderCallBack() {
                @Override
                public void onShopName(String shop_id) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfo.class)
                                .putExtra("shop_id", shop_id));
                    }
                }

                @Override
                public void onClickGoods(String type, String order_id, String shop_id) {
                    if (getBaseFragmentActivityContext() != null) {
                        if ("1".equals(type)) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsReadyBuyEdit.class)
                                    .putExtra("shop_id", shop_id)
                                    .putExtra("order_id", order_id)
                                    .putExtra("type", type)
                                    .putExtra("mode", "edit")
                            );
                        } else {
                            startActivity(new Intent(getBaseFragmentActivityContext(), OrderServiceReadyBuyEdit.class)
                                    .putExtra("shop_id", shop_id)
                                    .putExtra("order_id", order_id)
                                    .putExtra("type", type)
                                    .putExtra("mode", "edit")
                            );
                        }

                    }

                }

                @Override
                public void onEdit(String type, String order_id, String shop_id) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderGoodsReadyBuyEdit.class)
                            .putExtra("shop_id", shop_id)
                            .putExtra("order_id", order_id)
                            .putExtra("type", type)
                            .putExtra("mode", "edit")
                    );
                }

                @Override
                public void onDelete(String type, String order_id, String shop_id) {
                    del_drafts(order_id, "order");
                }
            });
        } else if (search_mode == REQUEST_MODE_SOS) {
            listviewDraftSos = new ListviewDraftSos(getBaseFragmentActivityContext());
            listviewDraftSos.setOrientation(OrientationHelper.VERTICAL);
            listviewFragmentDraftPublic.addView(listviewDraftSos);
            listviewDraftSos.setOnListviewDraftSosCallBack(new ListviewDraftSos.OnListviewDraftSosCallBack() {
                @Override
                public void onClickFromSOS(String sos_id) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderSOSEdit.class)
                                .putExtra("sos_id", sos_id));
                    }
                }

                @Override
                public void onEdit(String sos_id) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), OrderSOSEdit.class)
                                .putExtra("sos_id", sos_id));
                    }
                }

                @Override
                public void onDelete(String sos_id) {
                    del_drafts(sos_id, "sos");
                }
            });

        } else if (search_mode == REQUEST_MODE_GOODS) {
            listviewDraftGoods = new ListviewDraftGoods(getBaseFragmentActivityContext());
            listviewDraftGoods.setOrientation(OrientationHelper.VERTICAL);
            listviewFragmentDraftPublic.addView(listviewDraftGoods);
            listviewDraftGoods.setOnListviewDraftGoodsCallBack(new ListviewDraftGoods.OnListviewDraftGoodsCallBack() {
                @Override
                public void onClickFromGoods(String goods_id, String type) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class)
                                .putExtra("mode", "edit")
                                .putExtra("goods_id", goods_id));
                    }
                }

                @Override
                public void onEdit(String goods_id, String type) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class)
                                .putExtra("mode", "edit")
                                .putExtra("goods_id", goods_id));
                    }
                }

                @Override
                public void onDelete(String goods_id) {
                    del_drafts(goods_id, "goods");
                }
            });

        } else if (search_mode == REQUEST_MODE_POPULARIZE) {
            listviewDraftPopularize = new ListviewDraftPopularize(getBaseFragmentActivityContext());
            listviewDraftPopularize.setOrientation(OrientationHelper.VERTICAL);
            listviewFragmentDraftPublic.addView(listviewDraftPopularize);
            listviewDraftPopularize.setOnListviewDraftGoodsCallBack(new ListviewDraftPopularize.OnListviewDraftPopularizeCallBack() {
                @Override
                public void onClickFromPopularize(DrapPopularize popularize) {

                }

                @Override
                public void onEdit(DrapPopularize popularize) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), PopularizeEdit.class)
                            .putExtra("canOperation", true)
                            .putExtra("title", popularize.title)
                            .putExtra("classify_name", popularize.classify_name)
                            .putExtra("popularize_id", popularize.popularize_id)
                            .putExtra("goods_id", popularize.goods_id));
                }

                @Override
                public void onDelete(DrapPopularize popularize) {
                    del_popularize(String.valueOf(popularize.popularize_id));
                }
            });

        }

    }

    /**
     * 删除推广信息
     */
    public void del_popularize(final String popularize_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_popularize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("popularize_id", popularize_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    for (int i = 0; i < drapPopularizeList.size(); i++) {
                                        if (popularize_id.equals(drapPopularizeList.get(i).popularize_id)) {
                                            drapPopularizeList.remove(i);
                                        }
                                    }
                                    if (drapPopularizeList.size() > 0) {
                                        emptyView.setVisibility(View.GONE);
                                    }else {
                                        emptyView.setVisibility(View.VISIBLE);
                                    }
                                    listviewDraftPopularize.setList(drapPopularizeList, getBaseFragmentActivityContext());
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
     * 草稿箱==>order 订单，sos 订单， goos 商品==>删除订单
     */
    public void del_drafts(final String targetId, final String type) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_drafts(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("id", targetId)
                            .put("type", type)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    LogUtils.e(type);
                                    switch (type) {
                                        case "order":
                                            p = 1;
                                            request();
                                            break;
                                        case "sos":
                                            for (int i = 0; i < drapSOSList.size(); i++) {
                                                if (targetId.equals(drapSOSList.get(i).sos_id)) {
                                                    drapSOSList.remove(i);
                                                }
                                            }
                                            if (drapSOSList.size() > 0) {
                                                emptyView.setVisibility(View.GONE);
                                            }else {
                                                emptyView.setVisibility(View.VISIBLE);
                                            }
                                            listviewDraftSos.setList(drapSOSList, getBaseFragmentActivityContext());
                                            break;

                                        case "goods":
                                            for (int i = 0; i < drapGoodsList.size(); i++) {
                                                if (targetId.equals(drapGoodsList.get(i).goods_id)) {
                                                    drapGoodsList.remove(i);
                                                }
                                            }
                                            if (drapGoodsList.size() > 0) {
                                                emptyView.setVisibility(View.GONE);
                                            }else {
                                                emptyView.setVisibility(View.VISIBLE);
                                            }
                                            listviewDraftGoods.setList(drapGoodsList, getBaseFragmentActivityContext());
                                            break;
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
     * 草稿箱==>订单
     */
    public void drafts_order() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.drafts_order(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("keywords", keywords)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
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
                                            drapOrderList.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            drapOrderList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), DrapOrder.class));
                                        }
                                        listviewDraftOrder.setList(drapOrderList, getBaseFragmentActivityContext());
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                                if (drapOrderList.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                }else {
                                    emptyView.setVisibility(View.VISIBLE);
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
     * 草稿箱==>SOS
     */
    public void drafts_sos() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.drafts_sos(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("keywords", keywords)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
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
                                            drapSOSList.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            drapSOSList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), DrapSOS.class));
                                        }
                                        listviewDraftSos.setList(drapSOSList, getBaseFragmentActivityContext());
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                                if (drapSOSList.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                }else {
                                    emptyView.setVisibility(View.VISIBLE);
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
     * 草稿箱==>商品
     */
    public void drafts_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.drafts_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("keywords", keywords)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
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
                                            drapGoodsList.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            drapGoodsList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), DrapGoods.class));
                                        }
                                        listviewDraftGoods.setList(drapGoodsList, getBaseFragmentActivityContext());
                                    }


                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                                if (drapGoodsList.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                }else {
                                    emptyView.setVisibility(View.VISIBLE);
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
     * 草稿箱==>推广
     */
    public void drafts_popularize() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_drafts(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("keywords", keywords)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentDraftPublic != null) {
                                refreshFragmentDraftPublic.onRefreshComplete();
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
                                            drapPopularizeList.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            drapPopularizeList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), DrapPopularize.class));
                                        }
                                        listviewDraftPopularize.setList(drapPopularizeList, getBaseFragmentActivityContext());
                                    }


                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                                if (drapPopularizeList.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                }else {
                                    emptyView.setVisibility(View.VISIBLE);
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


}
