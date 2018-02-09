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

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCollectGoodsList;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.GoodsCollectModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
 * 我的==>商品收藏
 * Created by leilei on 2017/11/3.
 */

public class ActivityCollect extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_activity_collect)
    InnerListview listviewActivityCollect;
    @BindView(R.id.scollview_activity_collect)
    PullToRefreshScrollView scollviewActivityCollect;
    @BindView(R.id.top_activity_collect)
    ImageView topActivityCollect;
    @BindView(R.id.activity_collect)
    LinearLayout activityCollect;
    List<GoodsCollectModel> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;

    int p = 1;

    SSS_Adapter sss_adapter;
    @BindView(R.id.empty_view)
    SimpleDraweeView emptyView;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        backTop = null;
        titleTop = null;
        listviewActivityCollect = null;
        scollviewActivityCollect = null;
        topActivityCollect = null;
        activityCollect = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        customInit(activityCollect, false, true, true);
        titleTop.setText("商品收藏");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollviewActivityCollect.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topActivityCollect.setVisibility(View.VISIBLE);
                    } else {
                        topActivityCollect.setVisibility(View.GONE);
                    }
                }
            });
        }
        scollviewActivityCollect.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                goods_collect();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                goods_collect();
            }
        });
        initAdapter();
        goods_collect();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedCollectGoodsList changedCollectGoodsList) {
        p = 1;
        goods_collect();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<GoodsCollectModel>(getBaseActivityContext(), R.layout.item_activity_collect_adapter) {

            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsCollectModel bean, SSS_Adapter instance) {
                helper.setItemChildClickListener(R.id.click_item_activity_collect_adapter);
                addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_activity_collect_adapter)), 0f));
                helper.setText(R.id.title_item_activity_collect_adapter, bean.title);
                helper.setText(R.id.slogan_item_activity_collect_adapter, bean.slogan);
                helper.setText(R.id.price_item_activity_collect_adapter, "¥" + bean.price);


            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_activity_collect_adapter);
                helper.setItemChildClickListener(R.id.buy_item_activity_collect_adapter);
                helper.setItemChildClickListener(R.id.delete_item_activity_collect_adapter);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_activity_collect_adapter:
                        try {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                        .putExtra("goods_id", list.get(position).goods_id)
                                        .putExtra("type", list.get(position).type));
                            }
                        } catch (IndexOutOfBoundsException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "IndexOutOfBoundsException" + e.getMessage());
                        }
                        break;
                    case R.id.buy_item_activity_collect_adapter:
                        try {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                        .putExtra("goods_id", list.get(position).goods_id)
                                        .putExtra("showBuyDialog", "show")
                                        .putExtra("type", list.get(position).type));
                            }
                        } catch (IndexOutOfBoundsException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "IndexOutOfBoundsException" + e.getMessage());
                        }
                        break;
                    case R.id.delete_item_activity_collect_adapter:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_activity_collect_adapter)).smoothClose();
                        insert_collect_cancel_collect(list.get(position).goods_id);
                        break;
                }

            }
        });

        listviewActivityCollect.setAdapter(sss_adapter);
    }


    @OnClick({R.id.back_top, R.id.top_activity_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.top_activity_collect:
                scollviewActivityCollect.scrollTo(0, 0);
                break;
        }
    }


    /**
     * 收藏/取消收藏
     */
    public void insert_collect_cancel_collect(final String goods_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_collect_cancel_collect(
                    new JSONObject()
                            .put("collect_id", goods_id)
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
                                    if ("0".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (goods_id.equals(list.get(i).goods_id)) {
                                                list.remove(i);
                                            }
                                        }

                                        if (list.size() > 0) {
                                            sss_adapter.setList(list);
                                            emptyView.setVisibility(View.GONE);
                                        } else {
                                            listviewActivityCollect.removeAllViewsInLayout();
                                            emptyView.setVisibility(View.VISIBLE);
                                        }
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


    /**
     * 获取商品收藏
     */
    public void goods_collect() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_collect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (scollviewActivityCollect != null) {
                                scollviewActivityCollect.onRefreshComplete();
                            }

                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollviewActivityCollect != null) {
                                scollviewActivityCollect.onRefreshComplete();
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
                                            GoodsCollectModel goodsCollectModel = new GoodsCollectModel();
                                            goodsCollectModel.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                            goodsCollectModel.title = jsonArray.getJSONObject(i).getString("title");
                                            goodsCollectModel.slogan = jsonArray.getJSONObject(i).getString("slogan");
                                            goodsCollectModel.master_map = jsonArray.getJSONObject(i).getString("master_map");
                                            goodsCollectModel.price = jsonArray.getJSONObject(i).getString("price");
                                            goodsCollectModel.type = jsonArray.getJSONObject(i).getString("type");
                                            list.add(goodsCollectModel);
                                        }
                                    }
                                    sss_adapter.setList(list);
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                                if (list.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                } else {
                                    emptyView.setVisibility(View.VISIBLE);
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
}
