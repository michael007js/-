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
import com.blankj.utilcode.customwidget.Slidebar.WaveSideBar;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.MoreModel;

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
 * Created by leilei on 2018/1/20.
 */

public class ActivityMore extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.activity_more)
    LinearLayout activityMore;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.side_bar)
    WaveSideBar sideBar;

    SSS_Adapter sss_adapter;
    List<MoreModel> moreData = new ArrayList<>();

    YWLoadingDialog ywLoadingDialog;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        customInit(activityMore,false,true,false);
        titleTop.setText("更多");
        initAdapter();
        more_subclass();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<MoreModel>(getBaseActivityContext(), R.layout.item_city_search_adapter, moreData) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, MoreModel bean, SSS_Adapter instance) {
                if (bean.classify_id != null) {
                    helper.setText(R.id.text_item_city_search_adapter, bean.name);
                    helper.setBackgroundColor(R.id.text_item_city_search_adapter, getResources().getColor(R.color.white));
                } else {
                    helper.setText(R.id.text_item_city_search_adapter, bean.spell);
                    helper.setBackgroundColor(R.id.text_item_city_search_adapter, getResources().getColor(R.color.line));
                }

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.text_item_city_search_adapter);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.text_item_city_search_adapter:
                        if (moreData.get(position).classify_id != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceListPublic.class)
                                    .putExtra("classify_id", moreData.get(position).classify_id)
                                    .putExtra("type",moreData.get(position).type)
                                    .putExtra("showHead", false)
                                    .putExtra("title", moreData.get(position).name));
                        }
                }
            }
        });
        listview.setAdapter(sss_adapter);
    }

    /**
     * 车品车服更多
     */
    public void more_subclass() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.more_subclass(
                    new JSONObject()
                            .put("classify_id",getIntent().getExtras().getString("classify_id"))
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
                                    JSONArray jsonArray= jsonObject.getJSONArray("data");
                                    for (int i = 0; i <jsonArray.length(); i++) {
                                        MoreModel moreModel=new MoreModel();
                                        moreData.add(moreModel);
                                        moreModel.spell=jsonArray.getJSONObject(i).getString("spell");
                                        JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("subclass");
                                        for (int j = 0; j <jsonArray1.length() ; j++) {
                                            MoreModel moreModel2=new MoreModel();
                                            moreModel2.classify_id=jsonArray1.getJSONObject(j).getString("classify_id");
                                            moreModel2.type=jsonArray1.getJSONObject(j).getString("type");
                                            moreModel2.name=jsonArray1.getJSONObject(j).getString("name");
                                            moreModel2.logo=jsonArray1.getJSONObject(j).getString("logo");
                                            moreModel2.spell=jsonArray1.getJSONObject(j).getString("spell");
                                            moreData.add(moreModel2);
                                        }

                                    }
                                    sss_adapter.setList(moreData);

                                    sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
                                        @Override
                                        public void onSelectIndexItem(String index) {
                                            for (int i = 0; i < moreData.size(); i++) {
                                                if (moreData.get(i).classify_id != null) {
                                                    if (index.equals(moreData.get(i).spell)) {
                                                        listview.setSelection(i);
                                                        return;
                                                    }

                                                }
                                            }

                                        }
                                    });

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }
}
