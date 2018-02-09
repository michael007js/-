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
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SaleAndSeckillDiscountsCouponModel;
import com.sss.car.view.ActivityCoupon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.R.id.list;


/**
 * 现金券列表
 * Created by leilei on 2017/9/22.
 */

@SuppressLint("ValidFragment")
public class FragmentSaleAndSeckillDiscountsCash extends BaseFragment implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.listview_fragment_sale_and_seckill_discounts_cash)
    InnerListview listviewFragmentSaleAndSeckillDiscountsCash;
    @BindView(R.id.refresh_fragment_sale_and_seckill_discounts_cash)
    RefreshLoadMoreLayout refreshFragmentSaleAndSeckillDiscountsCash;
    @BindView(R.id.top_fragment_sale_and_seckill_discounts_cash)
    ImageView topFragmentSaleAndSeckillDiscountsCash;
    @BindView(R.id.scollview_fragment_sale_and_seckill_discounts_cash)
    ScrollView scollviewFragmentSaleAndSeckillDiscountsCash;
    @BindView(R.id.fragment_sale_and_seckill_discounts_cash)
    LinearLayout FragmentSaleAndSeckillDiscountsCash;
    Unbinder unbinder;
    int p = 1;
    String classify_id;
String type;

    List<SaleAndSeckillDiscountsCouponModel> saleAndSeckillDiscountsCouponModelList = new ArrayList<>();
    SSS_Adapter sss_adapter;

    public FragmentSaleAndSeckillDiscountsCash(String classify_id,String type) {
        this.classify_id = classify_id;
        this.type=type;
    }

    public FragmentSaleAndSeckillDiscountsCash() {
    }

    @Override
    public void onDestroy() {
        classify_id = null;
        if (saleAndSeckillDiscountsCouponModelList != null) {
            saleAndSeckillDiscountsCouponModelList.clear();
        }
        saleAndSeckillDiscountsCouponModelList = null;


        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        scollviewFragmentSaleAndSeckillDiscountsCash = null;
        listviewFragmentSaleAndSeckillDiscountsCash = null;
        refreshFragmentSaleAndSeckillDiscountsCash = null;
        topFragmentSaleAndSeckillDiscountsCash = null;
        FragmentSaleAndSeckillDiscountsCash = null;
        super.onDestroy();
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_sale_and_seckill_discounts_cash;

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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                scollviewFragmentSaleAndSeckillDiscountsCash.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                    @Override
                                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                        if (scrollY > Config.scoll_HighRestriction) {
                                            topFragmentSaleAndSeckillDiscountsCash.setVisibility(View.VISIBLE);
                                        } else {
                                            topFragmentSaleAndSeckillDiscountsCash.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            topFragmentSaleAndSeckillDiscountsCash.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scollviewFragmentSaleAndSeckillDiscountsCash.smoothScrollTo(0,0);
                                }
                            });
                            refreshFragmentSaleAndSeckillDiscountsCash.init(new RefreshLoadMoreLayout.Config(FragmentSaleAndSeckillDiscountsCash.this).canLoadMore(true).canRefresh(true));
                            initAdapter();
                            get_coupon();
                        }
                    });
                }
            }.start();
        }

    }

    @Override
    protected void stopLoad() {

    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<SaleAndSeckillDiscountsCouponModel>(getBaseFragmentActivityContext(), R.layout.item_sale_and_seckill_discounts) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, SaleAndSeckillDiscountsCouponModel bean,SSS_Adapter instance) {
                helper.setText(R.id.discounts_item_sale_and_seckill_discounts, bean.money);
                helper.setText(R.id.date_item_sale_and_seckill_discounts, bean.duration);
                helper.setText(R.id.title_item_sale_and_seckill_discounts, bean.name);
                helper.setText(R.id.slogan_item_sale_and_seckill_discounts, bean.describe);
                helper.setText(R.id.slogan_item_sale_and_seckill_discounts, bean.describe);
                helper.setText(R.id.sell_item_sale_and_seckill_discounts, "已抢" + bean.sell + "件");
                helper.setText(R.id.price_item_sale_and_seckill_discounts, "¥" + bean.sell_price);
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_discounts)).setMax(Integer.valueOf(bean.number));
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_discounts)).setProgress(Integer.valueOf(bean.sell));
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_discounts)).setEnabled(false);
                addImageViewList(FrescoUtils.showImage(false, 150, 110, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_coupon), ((SimpleDraweeView) helper.getView(R.id.pic_item_sale_and_seckill_discounts)), 0f));
                helper.setText(R.id.type_item_sale_and_seckill_discounts, "现金券");
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.get_item_sale_and_seckill_discounts);

            }

        };
sss_adapter.setOnItemListener(new SSS_OnItemListener() {
    @Override
    public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
        switch (view.getId()){
            case R.id.type_item_sale_and_seckill_discounts:
                if (getBaseFragmentActivityContext()!=null){
                    startActivity(new Intent(getBaseFragmentActivityContext(),ActivityCoupon.class)
                            .putExtra("mode","details")
                            .putExtra("money",saleAndSeckillDiscountsCouponModelList.get(position).money)
                            .putExtra("coupon_id",saleAndSeckillDiscountsCouponModelList.get(position).coupon_id)
                            .putExtra("type",type));
                }
                break;
        }
    }
});

        listviewFragmentSaleAndSeckillDiscountsCash.setAdapter(sss_adapter);
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

    @Override
    public void onRefresh() {
        p = 1;
        get_coupon();
    }

    @Override
    public void onLoadMore() {
        get_coupon();

    }


    /**
     * 淘优惠-更多
     */
    public void get_coupon() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_coupon(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p", p)
                            .put("type", "2")// 1满减券，2现金券，3折扣券
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentSaleAndSeckillDiscountsCash != null) {
                                refreshFragmentSaleAndSeckillDiscountsCash.stopRefresh();
                                refreshFragmentSaleAndSeckillDiscountsCash.stopLoadMore();
                            }

                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (refreshFragmentSaleAndSeckillDiscountsCash != null) {
                                refreshFragmentSaleAndSeckillDiscountsCash.stopRefresh();
                                refreshFragmentSaleAndSeckillDiscountsCash.stopLoadMore();
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            saleAndSeckillDiscountsCouponModelList.clear();
                                        }
                                        p++;
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            SaleAndSeckillDiscountsCouponModel saleAndSeckillDiscountsCouponModel = new SaleAndSeckillDiscountsCouponModel();
                                            saleAndSeckillDiscountsCouponModel.duration = jsonArray.getJSONObject(j).getString("duration");
                                            saleAndSeckillDiscountsCouponModel.type = jsonArray.getJSONObject(j).getString("type");
                                            saleAndSeckillDiscountsCouponModel.money = jsonArray.getJSONObject(j).getString("money");
                                            saleAndSeckillDiscountsCouponModel.price = jsonArray.getJSONObject(j).getString("price");
                                            saleAndSeckillDiscountsCouponModel.sell_price = jsonArray.getJSONObject(j).getString("sell_price");
                                            saleAndSeckillDiscountsCouponModel.name = jsonArray.getJSONObject(j).getString("name");
                                            saleAndSeckillDiscountsCouponModel.sell = jsonArray.getJSONObject(j).getString("sell");
                                            saleAndSeckillDiscountsCouponModel.describe = jsonArray.getJSONObject(j).getString("describe");
                                            saleAndSeckillDiscountsCouponModel.number = jsonArray.getJSONObject(j).getString("number");
                                            saleAndSeckillDiscountsCouponModel.start_time = jsonArray.getJSONObject(j).getString("start_time");
                                            saleAndSeckillDiscountsCouponModel.end_time = jsonArray.getJSONObject(j).getString("end_time");
                                            saleAndSeckillDiscountsCouponModel.coupon_id = jsonArray.getJSONObject(j).getString("coupon_id");
                                            saleAndSeckillDiscountsCouponModelList.add(saleAndSeckillDiscountsCouponModel);
                                        }
                                        sss_adapter.setList(saleAndSeckillDiscountsCouponModelList);
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:SecKill discounts-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:SecKill discounts-0");
            e.printStackTrace();
        }
    }

}
