package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
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
import com.sss.car.model.CouponGetModel_List;
import com.sss.car.model.SaleAndSeckillDiscountsCouponModel;

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
 * 我的==>优惠券==>领取优惠券==>筛选==>优惠券分类==>折扣券列表
 * Created by leilei on 2017/9/22.
 */

@SuppressLint("ValidFragment")
public class FragmentCouponDiscountsFullCutCash extends BaseFragment implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.listview_fragment_coupon_discounts_full_cut_cash)
    InnerListview listviewFragmentCouponDiscountsFullCutCash;
    @BindView(R.id.refresh_fragment_coupon_discounts_full_cut_cash)
    RefreshLoadMoreLayout refreshFragmentCouponDiscountsFullCutCash;
    @BindView(R.id.top_fragment_coupon_discounts_full_cut_cash)
    ImageView topFragmentCouponDiscountsFullCutCash;
    @BindView(R.id.scollview_fragment_coupon_discounts_full_cut_cash)
    ScrollView scollviewFragmentCouponDiscountsFullCutCash;
    @BindView(R.id.fragment_coupon_discounts_full_cut_cash)
    LinearLayout FragmentCouponDiscountsFullCutCash;
    Unbinder unbinder;
    int p = 1;
    String classify_id;
    String type;    /*1满减券，2现金券，3折扣券*/
    YWLoadingDialog ywLoadingDialog;


    List<SaleAndSeckillDiscountsCouponModel> saleAndSeckillDiscountsCouponModelList = new ArrayList<>();
    SSS_Adapter sss_adapter;

    List<CouponGetModel_List> data = new ArrayList<>();

    public FragmentCouponDiscountsFullCutCash() {
    }

    public FragmentCouponDiscountsFullCutCash(String classify_id, String type) {
        this.classify_id = classify_id;
        this.type = type;
    }

    @Override
    public void onDestroy() {
        classify_id = null;
        if (saleAndSeckillDiscountsCouponModelList != null) {
            saleAndSeckillDiscountsCouponModelList.clear();
        }
        saleAndSeckillDiscountsCouponModelList = null;

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        scollviewFragmentCouponDiscountsFullCutCash = null;
        listviewFragmentCouponDiscountsFullCutCash = null;
        refreshFragmentCouponDiscountsFullCutCash = null;
        topFragmentCouponDiscountsFullCutCash = null;
        FragmentCouponDiscountsFullCutCash = null;
        super.onDestroy();
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_coupon_discounts_full_cut_cash;

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
                                scollviewFragmentCouponDiscountsFullCutCash.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                    @Override
                                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                        if (scrollY > Config.scoll_HighRestriction) {
                                            topFragmentCouponDiscountsFullCutCash.setVisibility(View.VISIBLE);
                                        } else {
                                            topFragmentCouponDiscountsFullCutCash.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            topFragmentCouponDiscountsFullCutCash.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scollviewFragmentCouponDiscountsFullCutCash.smoothScrollTo(0, 0);
                                }
                            });
                            refreshFragmentCouponDiscountsFullCutCash.init(new RefreshLoadMoreLayout.Config(FragmentCouponDiscountsFullCutCash.this).canLoadMore(true).canRefresh(true));
                            initAdapter();
                            getCoupon_more();
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
        sss_adapter = new SSS_Adapter<CouponGetModel_List>(getBaseFragmentActivityContext(), R.layout.item_coupon_discounts_full_cut_cash) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, CouponGetModel_List bean, final SSS_Adapter instance) {
                helper.setText(R.id.discounts_item_discounts_full_cut_cash, bean.money);
                helper.setText(R.id.date_item_discounts_full_cut_cash, bean.duration);
                helper.setText(R.id.title_item_discounts_full_cut_cash, bean.name);
                helper.setText(R.id.slogan_item_discounts_full_cut_cash, bean.describe);
                helper.setText(R.id.price_item_discounts_full_cut_cash, "¥" + bean.sell_price);
                addImageViewList(FrescoUtils.showImage(false, 150, 110, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_coupon), ((SimpleDraweeView) helper.getView(R.id.pic_item_discounts_full_cut_cash)), 0f));

                if ("1".equals(bean.is_join)) {
                    helper.setText(R.id.get_item_discounts_full_cut_cash, "已加入");
                    ((TextView) helper.getView(R.id.get_item_discounts_full_cut_cash)).setOnClickListener(null);
                } else if ("0".equals(bean.is_join)) {
                    helper.setText(R.id.get_item_discounts_full_cut_cash, "立即加入");
                    helper.setItemChildClickListener(R.id.get_item_discounts_full_cut_cash);
                    ((TextView) helper.getView(R.id.get_item_discounts_full_cut_cash)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            joinCoupon(data, position, instance);
                        }
                    });
                }

                  /*1满减券，2现金券，3折扣券*/
                switch (bean.type) {
                    case "1":
                        helper.setText(R.id.type_item_discounts_full_cut_cash, "满减券");
                        break;
                    case "2":
                        helper.setText(R.id.type_item_discounts_full_cut_cash, "现金券");
                        break;
                    case "3":
                        helper.setText(R.id.type_item_discounts_full_cut_cash, "折扣券");
                        break;
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }

        };

        listviewFragmentCouponDiscountsFullCutCash.setAdapter(sss_adapter);
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
        getCoupon_more();
    }

    @Override
    public void onLoadMore() {
        getCoupon_more();

    }


    /**
     * 获取平台优惠券==>更多优惠券
     */
    public void getCoupon_more() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getCoupon_more(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p", p)
                            .put("type",type)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshFragmentCouponDiscountsFullCutCash!=null){
                                refreshFragmentCouponDiscountsFullCutCash.stopRefresh();
                                refreshFragmentCouponDiscountsFullCutCash.stopLoadMore();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentCouponDiscountsFullCutCash!=null){
                                refreshFragmentCouponDiscountsFullCutCash.stopRefresh();
                                refreshFragmentCouponDiscountsFullCutCash.stopLoadMore();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                    if (jsonArray1.length() > 0) {
                                        if (p == 1) {
                                            data.clear();
                                        }
                                        p++;
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            CouponGetModel_List couponGetModel_list = new CouponGetModel_List();
                                            couponGetModel_list.coupon_id = jsonArray1.getJSONObject(j).getString("coupon_id");
                                            couponGetModel_list.classify_id = jsonArray1.getJSONObject(j).getString("classify_id");
                                            couponGetModel_list.name = jsonArray1.getJSONObject(j).getString("name");
                                            couponGetModel_list.describe = jsonArray1.getJSONObject(j).getString("describe");
                                            couponGetModel_list.type = jsonArray1.getJSONObject(j).getString("type");
                                            couponGetModel_list.money = jsonArray1.getJSONObject(j).getString("money");
                                            couponGetModel_list.price = jsonArray1.getJSONObject(j).getString("price");
                                            couponGetModel_list.sell_price = jsonArray1.getJSONObject(j).getString("sell_price");
                                            couponGetModel_list.sell = jsonArray1.getJSONObject(j).getString("sell");
                                            couponGetModel_list.number = jsonArray1.getJSONObject(j).getString("number");
                                            couponGetModel_list.create_time = jsonArray1.getJSONObject(j).getString("create_time");
                                            couponGetModel_list.start_time = jsonArray1.getJSONObject(j).getString("start_time");
                                            couponGetModel_list.end_time = jsonArray1.getJSONObject(j).getString("end_time");
                                            couponGetModel_list.state = jsonArray1.getJSONObject(j).getString("state");
                                            couponGetModel_list.is_join = jsonArray1.getJSONObject(j).getString("is_join");
                                            couponGetModel_list.duration = jsonArray1.getJSONObject(j).getString("duration");
                                            data.add(couponGetModel_list);
                                        }
                                        sss_adapter.setList(data);

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
     * 获取平台优惠券==>加入优惠券
     *
     * @param data
     * @param position
     * @param sss_adapter
     */
    public void joinCoupon(final List<CouponGetModel_List> data, final int position, final SSS_Adapter sss_adapter) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.joinCoupon(
                    new JSONObject()
                            .put("coupon_id", data.get(position).coupon_id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
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
                                    data.get(position).is_join = "1";//0未加入1已加入
                                    sss_adapter.setList(data);
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
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
