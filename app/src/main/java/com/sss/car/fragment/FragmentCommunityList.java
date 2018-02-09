package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.commodity.CommodityEdit;
import com.sss.car.model.CommodityAddGoodsTypeModelOne;
import com.sss.car.model.CommodityAddGoodsTypeModelThree;
import com.sss.car.model.CommodityAddGoodsTypeModelTwo;
import com.sss.car.model.MyGoodsModel;
import com.sss.car.popularize.PopularizeEdit;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityGoodsServiceEdit;

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
 * 我的商品==>在架商品列表/下架商品公用fragment
 * Created by leilei on 2017/11/1.
 */

@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentCommunityList extends BaseFragment {
    @BindView(R.id.listview_fragment_community_list)
    PullToRefreshListView listviewFragmentCommunityList;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    String type;//1车品2车服
    String status;//1在架2下架
    String classify_id;
    List<MyGoodsModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    int p = 1;

    public FragmentCommunityList(String type, String status) {
        this.type = type;
        this.status = status;
    }

    public void reRequest(String classify_id) {
        p = 1;
        this.classify_id = classify_id;
        my_goods();
    }

    @Override
    public void onDestroy() {
        listviewFragmentCommunityList = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        super.onDestroy();
    }

    public FragmentCommunityList() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_community_list;
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
                                listviewFragmentCommunityList.setMode(PullToRefreshBase.Mode.BOTH);
                                listviewFragmentCommunityList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        my_goods();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        my_goods();
                                    }
                                });
                                initAdapter();
                                my_goods();
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
        sss_adapter = new SSS_Adapter<MyGoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_community_list, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, MyGoodsModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title_item_fragment_community_list, bean.title);
                helper.setText(R.id.slogan_item_fragment_community_list, bean.slogan);
                addImageViewList(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_fragment_community_list)), 3f));
                if ("1".equals(status)) {//1在架0下架
                    helper.setVisibility(R.id.down_item_fragment_community_list, View.VISIBLE);
                    helper.setVisibility(R.id.edit_item_fragment_community_list, View.VISIBLE);
                    helper.setVisibility(R.id.popularize_item_fragment_community_list, View.VISIBLE);
                    helper.setVisibility(R.id.edit_item_fragment_community_list, View.GONE);
                } else if ("0".equals(status)) {
                    helper.setVisibility(R.id.delete_item_fragment_community_list, View.VISIBLE);
                    helper.setVisibility(R.id.up_item_fragment_community_list, View.VISIBLE);
                    helper.setVisibility(R.id.edit_item_fragment_community_list, View.VISIBLE);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_fragment_community_list);
                if ("1".equals(status)) {//1在架0下架
                    helper.setItemChildClickListener(R.id.down_item_fragment_community_list);
                    helper.setItemChildClickListener(R.id.edit_item_fragment_community_list);
                    helper.setItemChildClickListener(R.id.edit_item_fragment_community_list);
                    helper.setItemChildClickListener(R.id.popularize_item_fragment_community_list);
                } else if ("0".equals(status)) {
                    helper.setItemChildClickListener(R.id.delete_item_fragment_community_list);
                    helper.setItemChildClickListener(R.id.up_item_fragment_community_list);
                    helper.setItemChildClickListener(R.id.edit_item_fragment_community_list);
                }
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_fragment_community_list:
                        if (getBaseFragmentActivityContext() != null) {
                            if ("1".equals(status)){
                                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                                        .putExtra("goods_id", list.get(position).goods_id)
                                        .putExtra("type", type));
                            }else {
                                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class)
                                        .putExtra("mode", "edit")
                                        .putExtra("goods_id", list.get(position).goods_id));
                            }

                        }
                        break;
                    case R.id.down_item_fragment_community_list:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_community_list)).smoothClose();
                        status_goods(list.get(position).goods_id);
                        break;
                    case R.id.delete_item_fragment_community_list:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_community_list)).smoothClose();
                        del_goods(list.get(position).goods_id);
                        break;
                    case R.id.up_item_fragment_community_list:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_community_list)).smoothClose();
                        status_goods(list.get(position).goods_id);
                        break;
                    case R.id.edit_item_fragment_community_list:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_community_list)).smoothClose();
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class)
                                    .putExtra("mode", "edit")
                                    .putExtra("goods_id", list.get(position).goods_id));
                        }
                        break;
                    case R.id.popularize_item_fragment_community_list:
                        if (getBaseFragmentActivityContext() != null) {
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_community_list)).smoothClose();
                            startActivity(new Intent(getBaseFragmentActivityContext(), PopularizeEdit.class)
                                    .putExtra("canOperation",true)
                                    .putExtra("title",  list.get(position).title)
                                    .putExtra("classify_name",  list.get(position).classify_name)
                                    .putExtra("popularize_id",list.get(position).popularize_id)
                                    .putExtra("goods_id", list.get(position).goods_id));
                        }
                        break;
                }
            }
        });
        listviewFragmentCommunityList.setAdapter(sss_adapter);
    }

    /**
     * 我的商品
     */
    void my_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.my_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("p", p)
                            .put("is_popularize","0")
                            .put("status", status)
                            .put("classify_id", classify_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentCommunityList != null) {
                                listviewFragmentCommunityList.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentCommunityList != null) {
                                listviewFragmentCommunityList.onRefreshComplete();
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
                                            MyGoodsModel myGoodsModel = new MyGoodsModel();
                                            myGoodsModel.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                            myGoodsModel.title = jsonArray.getJSONObject(i).getString("title");
                                            myGoodsModel.slogan = jsonArray.getJSONObject(i).getString("slogan");
                                            myGoodsModel.master_map = jsonArray.getJSONObject(i).getString("master_map");
                                            myGoodsModel.cost_price = jsonArray.getJSONObject(i).getString("cost_price");
                                            myGoodsModel.price = jsonArray.getJSONObject(i).getString("price");
                                            myGoodsModel.number = jsonArray.getJSONObject(i).getString("number");
                                            myGoodsModel.sell = jsonArray.getJSONObject(i).getString("sell");
                                            myGoodsModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            myGoodsModel.is_popularize = jsonArray.getJSONObject(i).getString("is_popularize");
                                            myGoodsModel.classify_id = jsonArray.getJSONObject(i).getString("classify_id");
                                            myGoodsModel.popularize_id = jsonArray.getJSONObject(i).getString("popularize_id");
                                            myGoodsModel.classify_name = jsonArray.getJSONObject(i).getString("classify_name");
                                            list.add(myGoodsModel);
                                        }
                                        if (sss_adapter != null) {
                                            sss_adapter.setList(list);
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 我的商品==>删除
     */
    void del_goods(final String goods_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .toString()
                    , new StringCallback() {
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
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).goods_id.equals(goods_id)) {
                                            list.remove(i);

                                        }
                                    }
                                    sss_adapter.setList(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的商品==>商品上架和下架
     */
    void status_goods(final String goods_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.status_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .toString()
                    , new StringCallback() {
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
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).goods_id.equals(goods_id)) {
                                            list.remove(i);

                                        }
                                    }
                                    sss_adapter.setList(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }
}
