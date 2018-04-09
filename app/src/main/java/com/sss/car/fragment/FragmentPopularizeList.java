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
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedGoodsList;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.MyGoodsModel;
import com.sss.car.popularize.PopularizeEdit;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityGoodsServiceEdit;

import org.greenrobot.eventbus.EventBus;
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
public class FragmentPopularizeList extends BaseFragment {
    @BindView(R.id.listview_fragment_popularize_list)
    PullToRefreshListView listviewFragmentPopularizeList;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    String type;//1车品2车服
    String classify_id;
    List<MyGoodsModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    int p = 1;

    public FragmentPopularizeList(String type) {
        this.type = type;
    }

    public void reRequest(String classify_id) {
        p = 1;
        this.classify_id = classify_id;
        popularize();
    }

    @Override
    public void onDestroy() {
        listviewFragmentPopularizeList = null;
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

    public FragmentPopularizeList() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_popularize_list;
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
                                listviewFragmentPopularizeList.setMode(PullToRefreshBase.Mode.BOTH);
                                listviewFragmentPopularizeList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        popularize();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        popularize();
                                    }
                                });
                                initAdapter();
                                popularize();
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
        sss_adapter = new SSS_Adapter<MyGoodsModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_popularize_list, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final MyGoodsModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title_item_fragment_popularize_list, bean.title);
                helper.setText(R.id.slogan_item_fragment_popularize_list, bean.slogan);
                addImageViewList(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_fragment_popularize_list)), 3f));
                helper.setVisibility(R.id.view_item_fragment_popularize_list, View.VISIBLE);
                helper.setVisibility(R.id.cancel_popularize_item_fragment_popularize_list, View.VISIBLE);
                if ("1".equals(bean.is_payment)) {
                    helper.setVisibility(R.id.payment, View.VISIBLE);
                    helper.getView(R.id.payment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), PopularizeEdit.class)
                                    .putExtra("canOperation",true)
                                    .putExtra("title",  bean.title)
                                    .putExtra("type","edit")
                                    .putExtra("classify_name",  bean.classify_name)
                                    .putExtra("popularize_id",Integer.valueOf(bean.popularize_id))
                                    .putExtra("goods_id", bean.goods_id));
                        }
                    });
                } else {
                    helper.setVisibility(R.id.payment, View.GONE);
                    helper.getView(R.id.payment).setOnClickListener(null);
                }

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_fragment_popularize_list);
                helper.setItemChildClickListener(R.id.view_item_fragment_popularize_list);
                helper.setItemChildClickListener(R.id.cancel_popularize_item_fragment_popularize_list);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_fragment_popularize_list:
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), PopularizeEdit.class)
                                    .putExtra("canOperation", false)
                                    .putExtra("title", list.get(position).title)
                                    .putExtra("classify_name", list.get(position).classify_name)
                                    .putExtra("goods_id", list.get(position).goods_id)
                                    .putExtra("popularize_id",Integer.valueOf(list.get(position).popularize_id))
                                    .putExtra("type", "edit"));
                        }
                        break;
                    case R.id.view_item_fragment_popularize_list:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_popularize_list)).smoothClose();
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), PopularizeEdit.class)
                                    .putExtra("canOperation", false)
                                    .putExtra("title", list.get(position).title)
                                    .putExtra("classify_name", list.get(position).classify_name)
                                    .putExtra("goods_id", list.get(position).goods_id)
                                    .putExtra("popularize_id", list.get(position).popularize_id)
                                    .putExtra("type", "edit"));
                        }

                        break;
                    case R.id.cancel_popularize_item_fragment_popularize_list:
                        if (getBaseFragmentActivityContext() != null) {
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_fragment_popularize_list)).smoothClose();
                            cancel_popularize(list.get(position).popularize_id);
                        }
                        break;
                }
            }
        });
        listviewFragmentPopularizeList.setAdapter(sss_adapter);
    }

    /**
     * 我的商品
     */
    void popularize() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("p", p)
                            .put("is_popularize", "1")
                            .put("status", "1")
                            .put("classify_id", classify_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentPopularizeList != null) {
                                listviewFragmentPopularizeList.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentPopularizeList != null) {
                                listviewFragmentPopularizeList.onRefreshComplete();
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
                                            myGoodsModel.is_payment = jsonArray.getJSONObject(i).getString("is_payment");
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
     * 取消推广
     */
    void cancel_popularize(final String popularize_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cancel_popularize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("popularize_id", popularize_id)
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
                                    list.clear();
                                    sss_adapter.setList(list);
                                    EventBus.getDefault().getDefault().post(new ChangedGoodsList());

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
