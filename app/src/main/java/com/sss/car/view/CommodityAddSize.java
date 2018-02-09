package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.CommodityAddSizeModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.GoodsTypeSelect.View.ListGoodsTypeSelect;
import com.sss.car.model.CommoditySizeModel;

import org.greenrobot.eventbus.EventBus;
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
 * 我的商品==>添加商品==>尺寸
 * Created by leilei on 2017/10/27.
 */

public class CommodityAddSize extends BaseActivity {
    YWLoadingDialog ywLoadingDialog;
    List<CommoditySizeModel> list = new ArrayList<>();
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listGoodsTypeSelect_commodity_add_size)
    ListGoodsTypeSelect listGoodsTypeSelectCommodityAddSize;
    @BindView(R.id.commodity_add_size)
    LinearLayout commodityAddSize;
    @BindView(R.id.sure_commodity_add_size)
    TextView sureCommodityAddSize;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_add_size);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        titleTop.setText("商品规格");
        customInit(commodityAddSize, false, true, false);
        classify_size();
    }


    /**
     * 我的商品==>添加商品==>获取商品规格
     */
    public void classify_size() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_size(
                    new JSONObject()
                            .put("classify_id", getIntent().getExtras().getString("classify_id"))
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

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CommoditySizeModel commoditySizeModel = new CommoditySizeModel();
                                            commoditySizeModel.name = jsonArray.getJSONObject(i).getString("name");
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("subclass");
//                                            List<SelectDataModel> subclass = new ArrayList<>();
                                            List<String> subclass = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                subclass.add(jsonArray1.getJSONObject(j).getString("name"));
//                                                subclass.add(new SelectDataModel(jsonArray1.getJSONObject(j).getString("name"),false,jsonArray.getJSONObject(i).getString("name")));
                                            }
                                            commoditySizeModel.list = subclass;
                                            list.add(commoditySizeModel);
                                        }
                                        listGoodsTypeSelectCommodityAddSize.setData(getBaseActivityContext(), list);
                                    }

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

    @OnClick({R.id.back_top, R.id.sure_commodity_add_size})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.sure_commodity_add_size:
                try {
                    if (listGoodsTypeSelectCommodityAddSize.getSizeName().length() == 0) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请选择商品规格");
                        return;
                    }
                    if (listGoodsTypeSelectCommodityAddSize.getSizeData().length() == 0) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "请选择商品价格/库存");
                        return;
                    }
                    EventBus.getDefault().post(new CommodityAddSizeModel(getIntent().getExtras().getString("type"),
                            listGoodsTypeSelectCommodityAddSize.getSizeName(),
                            listGoodsTypeSelectCommodityAddSize.getSizeData()));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


//    void parseToArray() {
//        List<List<String>> arrayList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            List<String> temp=new ArrayList<>();
//            for (int j = 0; j < list.get(i).list.size(); j++) {
//                temp.add(list.get(i).list.get(j));
//            }
//            arrayList.add(temp);
//        }
//        LogUtils.e(ArrayListTurnsUtils.turns(arrayList));
//
//
//
//    }
}
