package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListviewCouponSaleSeckillDiscount;
import com.sss.car.model.CouponGetModel;
import com.sss.car.model.CouponGetModel_List;
import com.sss.car.view.ActivityCoupon;
import com.sss.car.view.ActivityShopInfoAllFilter;

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
 * 淘优惠  优惠券界面
 * Created by leilei on 2017/11/2.
 */

@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentCouponSaleSeckillDiscounts extends BaseFragment {
    @BindView(R.id.ListviewCouponSaleSeckillDiscount)
    ListviewCouponSaleSeckillDiscount listviewFragmentCouponSaleSeckillDiscounts;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    List<CouponGetModel> list = new ArrayList<>();
    int p = 2;
    Gson gson = new Gson();
    String type;
    String types;

    @Override
    public void onDestroy() {

        listviewFragmentCouponSaleSeckillDiscounts = null;
        type = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        super.onDestroy();

    }

    public FragmentCouponSaleSeckillDiscounts() {
    }

    public FragmentCouponSaleSeckillDiscounts(String type,String types) {
        this.type = type;
        this.types=types;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_coupon_sale_seckill_discount;
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
                                listviewFragmentCouponSaleSeckillDiscounts.setListviewCouponSaleSeckillDiscountOperationCallBack(new ListviewCouponSaleSeckillDiscount.ListviewCouponSaleSeckillDiscountOperationCallBack() {
                                    @Override
                                    public void onClickCoupon(String coupon_id, String money) {
                                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCoupon.class)
                                                .putExtra("money",money)
                                                .putExtra("coupon_id", coupon_id)
                                                .putExtra("mode", "FragmentCouponSaleSeckillDiscounts")
                                        );
                                    }

                                    @Override
                                    public void onClickMore_ListviewCouponOperationCallBack(String scope, List<CouponGetModel_List> list, SSS_Adapter sss_adapter) {
                                        getCoupon_more(scope, list, sss_adapter);
                                    }

                                    @Override
                                    public void onClickTitle_ListviewCouponOperationCallBack(String classify_id, String title) {
                                        if (getBaseFragmentActivityContext() != null) {
                                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfoAllFilter.class)
                                                    .putExtra("mode", "FragmentCouponSaleSeckillDiscounts")
                                                    .putExtra("classify_id", classify_id)
                                                    .putExtra("title", title)
                                                    .putExtra("type", type));
                                        }
                                    }

                                    @Override
                                    public void onGetCoupon(List<CouponGetModel_List> data, int position, SSS_Adapter sss_adapter) {
                                        joinCoupon(data, position, sss_adapter);

                                    }
                                });
                                getCoupon();
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


    /**
     * 获取平台优惠券
     */
    public void getCoupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getCoupon(
                    new JSONObject()
                            .put("activity","1")//1区分是否是从活动专区进入
                            .put("type", types)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), CouponGetModel.class));
                                        }
                                        listviewFragmentCouponSaleSeckillDiscounts.setData(getBaseFragmentActivityContext(), list, new LoadImageCallBack() {
                                            @Override
                                            public void onLoad(ImageView imageView) {
                                                addImageViewList(imageView);
                                            }
                                        });

                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:sale-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:sale-0");
            e.printStackTrace();
        }
    }


    /**
     * 获取平台优惠券==>更多优惠券
     *
     * @param scope
     * @param data
     * @param sss_adapter
     */
    public void getCoupon_more(String scope, final List<CouponGetModel_List> data, final SSS_Adapter sss_adapter) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getCoupon_more(
                    new JSONObject()
                            .put("classify_id", type)
                            .put("scope", scope)
                            .put("p", p)
                            .put("activity","1")//1区分是否是从活动专区进入
                            .put("type", types)
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
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                    if (jsonArray1.length() > 0) {
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
