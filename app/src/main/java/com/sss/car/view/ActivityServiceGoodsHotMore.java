package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.GoodsServiceHotModel;

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
 * 车品车服热门更多
 * Created by leilei on 2017/11/8.
 */

public class ActivityServiceGoodsHotMore extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_activity_service_goods_hot_more)
    ListView listviewActivityServiceGoodsHotMore;
    @BindView(R.id.activity_service_goods_hot_more)
    LinearLayout activityServiceGoodsHotMore;
    YWLoadingDialog ywLoadingDialog;
    /*热门商品集合*/
    List<GoodsServiceHotModel> goodsServiceHotModelList = new ArrayList<>();
    SSS_Adapter sss_adapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        listviewActivityServiceGoodsHotMore = null;
        activityServiceGoodsHotMore = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_goods_hot_more);
        ButterKnife.bind(this);
        customInit(activityServiceGoodsHotMore, false, true, false);
        titleTop.setText("更多");
        init();
        hot_subclass();
    }

    void init() {
        sss_adapter = new SSS_Adapter<GoodsServiceHotModel>(getBaseActivityContext(), R.layout.item_goods_servicehot_more_adapter, goodsServiceHotModelList) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsServiceHotModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_goods_servicehot_more_adapter, bean.name);

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.text_item_goods_servicehot_more_adapter);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.text_item_goods_servicehot_more_adapter:
                        startActivity(new Intent(getBaseActivityContext(), ActivityGoodsList.class)
                                .putExtra("classify_id", goodsServiceHotModelList.get(position).classify_id)
                                .putExtra("type",getIntent().getExtras().getString("type"))
                                .putExtra("title", goodsServiceHotModelList.get(position).name));
                        break;
                }
            }
        });
        listviewActivityServiceGoodsHotMore.setAdapter(sss_adapter);
    }

    /**
     * 获取热门
     */
    public void hot_subclass() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.hot_subclass(
                    new JSONObject()
                            .put("more", "1")//	1为更多
                            .put("classify_id", getIntent().getExtras().getString("classify_id"))
                            .put("type", getIntent().getExtras().getString("type"))
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
                                        sss_adapter.setList(goodsServiceHotModelList);
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

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
