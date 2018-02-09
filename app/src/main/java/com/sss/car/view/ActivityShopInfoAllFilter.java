package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.CommodityListChanged;
import com.sss.car.EventBusModel.PopularizeListChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.ShopInfoAllFilterOneAdapter;
import com.sss.car.adapter.ShopInfoAllFilterTwoAdapter;
import com.sss.car.coupon.CouponFilter;
import com.sss.car.dao.ChooseCallBack;
import com.sss.car.dao.ShopInfoAllFilterOneAdapterOperationCallBack;
import com.sss.car.dao.ShopInfoAllFilterTwoAdapterOperationCallBack;
import com.sss.car.model.ShopInfoAllFilterModel;
import com.sss.car.model.ShopInfoAllFilter_SubClassModel;

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
 * 商品与活动筛选界面
 * <p>
 * getIntent().getExtras().getString("mode")===goods商品
 * getIntent().getExtras().getString("mode")===secKill淘秒杀
 *  getIntent().getExtras().getString("mode")===discounts淘优惠
 *  getIntent().getExtras().getString("mode")===publish_goods发布商品
 *  getIntent().getExtras().getString("mode")===popularize推广商品
 * Created by leilei on 2017/9/18.
 */

public class ActivityShopInfoAllFilter extends BaseActivity implements
        ShopInfoAllFilterOneAdapterOperationCallBack,
        ShopInfoAllFilterTwoAdapterOperationCallBack, ChooseCallBack {

    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_one_activity_shop_info_all_filter)
    ListView listviewOneActivityShopInfoAllFilter;
    @BindView(R.id.listview_two_activity_shop_info_all_filter)
    ListView listviewTwoActivityShopInfoAllFilter;
    @BindView(R.id.activity_shop_info_all_filter)
    LinearLayout activityShopInfoAllFilter;

    YWLoadingDialog ywLoadingDialog;

    String type;

    List<ShopInfoAllFilterModel> list = new ArrayList<>();

    ShopInfoAllFilterOneAdapter shopInfoAllFilterOneAdapter;

    ShopInfoAllFilterTwoAdapter shopInfoAllFilterTwoAdapter;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        listviewOneActivityShopInfoAllFilter = null;
        listviewTwoActivityShopInfoAllFilter = null;
        activityShopInfoAllFilter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info_all_filter);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败");
            finish();
        }
        type = getIntent().getExtras().getString("type");
        customInit(activityShopInfoAllFilter, false, true, false);
        titleTop.setText("筛选");

        shopInfoAllFilterOneAdapter = new ShopInfoAllFilterOneAdapter(getBaseActivityContext(), list, this, this);
        listviewOneActivityShopInfoAllFilter.setAdapter(shopInfoAllFilterOneAdapter);
        shopInfoAllFilterTwoAdapter = new ShopInfoAllFilterTwoAdapter(getBaseActivityContext(), null, this);
        listviewTwoActivityShopInfoAllFilter.setAdapter(shopInfoAllFilterTwoAdapter);
        filtrate();

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    /**
     * 筛选
     */
    public void filtrate() {

//        String classify_id = null;
//        if ("1".equals(type)){
//            classify_id=Config.classify_id1;
//        }else   if ("2".equals(type)){
//            classify_id= Config.classify_id;
//        }

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
//        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.filtrate(
                    new JSONObject()
//                            .put("type", type)
//                            .put("classify_id", classify_id)
//                            .put("member_id", Config.member_id)
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
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ShopInfoAllFilterModel shopInfoAllFilterModel = new ShopInfoAllFilterModel();
                                        shopInfoAllFilterModel.classify_id = jsonArray.getJSONObject(i).getString("classify_id");
                                        shopInfoAllFilterModel.name = jsonArray.getJSONObject(i).getString("name");

                                        List<ShopInfoAllFilter_SubClassModel> shopInfoAllFilter_subClassModelArrayList = new ArrayList<>();
                                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("subclass");
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            ShopInfoAllFilter_SubClassModel shopInfoAllFilter_subClassModel = new ShopInfoAllFilter_SubClassModel();
                                            shopInfoAllFilter_subClassModel.classify_id = jsonArray1.getJSONObject(j).getString("classify_id");
                                            shopInfoAllFilter_subClassModel.name = jsonArray1.getJSONObject(j).getString("name");
                                            shopInfoAllFilter_subClassModelArrayList.add(shopInfoAllFilter_subClassModel);
                                        }
                                        shopInfoAllFilterModel.subclass = shopInfoAllFilter_subClassModelArrayList;
                                        list.add(shopInfoAllFilterModel);

                                        shopInfoAllFilterOneAdapter.refresh(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
//        } catch (JSONException e) {
//            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClickShopInfoAllFilterOneAdapterItem(int position, ShopInfoAllFilterModel model, List<ShopInfoAllFilterModel> list) {
        shopInfoAllFilterTwoAdapter.refresh(list.get(position).subclass);
    }

    @Override
    public void onClickShopInfoAllFilterTwoAdapterItem(int position, ShopInfoAllFilter_SubClassModel model, List<ShopInfoAllFilter_SubClassModel> list) {
        if ("goods".equals(getIntent().getExtras().getString("mode"))) {//商品
            startActivity(new Intent(getBaseActivityContext(), ActivityShopIinfoAllFilterLaterList.class)
                    .putExtra("name", model.name)
                    .putExtra("classify_id",model.classify_id)
            );
        } else if ("secKill".equals(getIntent().getExtras().getString("mode"))) {//淘秒杀
            if (getBaseActivityContext() != null) {
                startActivity(new Intent(getBaseActivityContext(), ActivitySaleAndSeckillSaleFilter.class)
                        .putExtra("type", type)
                        .putExtra("classify_id",list.get(position).classify_id )
                        .putExtra("title",list.get(position).name)
                );
            }
        }else if ("FragmentCouponSaleSeckillDiscounts".equals(getIntent().getExtras().getString("mode"))){//淘优惠
            startActivity(new Intent(getBaseActivityContext(), CouponFilter.class)
                    .putExtra("classify_id", getIntent().getExtras().getString("classify_id"))
                    .putExtra("type", type)
                    .putExtra("title", list.get(position).name));
        }else if ("publish_goods".equals(getIntent().getExtras().getString("mode"))){//我的商品==>发布列表
            EventBus.getDefault().post(new CommodityListChanged(list.get(position).classify_id));
        }else if ("coupon_get".equals(getIntent().getExtras().getString("mode"))){//我的优惠券==>领取优惠券
            startActivity(new Intent(getBaseActivityContext(), CouponFilter.class)
                    .putExtra("classify_id", getIntent().getExtras().getString("classify_id"))
                    .putExtra("type", type)
                    .putExtra("title", list.get(position).name));
        }else if ("popularize".equals(getIntent().getExtras().getString("mode"))){
            EventBus.getDefault().post(new PopularizeListChanged(list.get(position).classify_id));
        }
        finish();
    }

    @Override
    public void onChoose(int position, List<ShopInfoAllFilter_SubClassModel> list) {
        shopInfoAllFilterTwoAdapter.refresh(list);
    }

}
