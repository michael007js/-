package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CouponModel;
import com.sss.car.model.CouponModel4;
import com.sss.car.view.ActivityCoupon;
import com.sss.car.view.ActivityWalletIntegral_And_CouponSend;

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
 * 钱包==>积分==>选择赠予人==>好友/关注/粉丝/最近聊天公用
 * Created by leilei on 2017/10/25.
 */

@SuppressLint("ValidFragment")
public class FragmentWalletCashDiscountFullCut extends BaseFragment {
    @BindView(R.id.listview_fragment_wallet_cash_discount_full_cut)
    ListView listviewFragmentWalletCashDiscountFullCut;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    String type;//	1满减券，2现金券，3折扣券
    OnSelectUserCallBack onSelectUserCallBack;

    List<CouponModel4> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    public int p = 1;
    @BindView(R.id.refresh_fragment_wallet_cash_discount_full_cut)
    RefreshLoadMoreLayout refreshFragmentWalletCashDiscountFullCut;
    @BindView(R.id.top_fragment_wallet_cash_discount_full_cut)
    ImageView topFragmentWalletCashDiscountFullCut;

    public FragmentWalletCashDiscountFullCut(String type, OnSelectUserCallBack onSelectUserCallBack) {
        this.type = type;
        this.onSelectUserCallBack = onSelectUserCallBack;
    }

    public FragmentWalletCashDiscountFullCut() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_wallet_cash_discount_full_cut;
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
                                refreshFragmentWalletCashDiscountFullCut.init(new RefreshLoadMoreLayout.Config(new RefreshLoadMoreLayout.CallBack() {
                                    @Override
                                    public void onRefresh() {
                                        p = 1;
                                        walletGetCouponDetails();
                                    }

                                    @Override
                                    public void onLoadMore() {
                                        walletGetCouponDetails();
                                    }
                                })
                                        .canRefresh(false)
                                        .canLoadMore(false));
                                initAdapter();
                                walletGetCouponDetails();

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
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        refreshFragmentWalletCashDiscountFullCut = null;
        topFragmentWalletCashDiscountFullCut = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        listviewFragmentWalletCashDiscountFullCut = null;
        super.onDestroy();
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
        sss_adapter = new SSS_Adapter<CouponModel4>(getBaseFragmentActivityContext(), R.layout.item_wallet_cash_discount_full_cut) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, CouponModel4 bean,SSS_Adapter instance) {
                helper.setText(R.id.name_item_wallet_cash_discount_full_cut, bean.name);
                helper.setText(R.id.date_item_wallet_cash_discount_full_cut, bean.duration);
                helper.setText(R.id.price_item_wallet_cash_discount_full_cut, "¥" + bean.price);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.details_item_wallet_cash_discount_full_cut);
                helper.setItemChildClickListener(R.id.returns_item_wallet_cash_discount_full_cut);
                helper.setItemChildClickListener(R.id.send_item_wallet_cash_discount_full_cut);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.details_item_wallet_cash_discount_full_cut:
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCoupon.class)
                                    .putExtra("mode", "details")
                                    .putExtra("money",list.get(position).money)
                                    .putExtra("coupon_id", list.get(position).coupon_id));
                        }
                        break;
                    case R.id.returns_item_wallet_cash_discount_full_cut:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_wallet_cash_discount_full_cut)).smoothClose();
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityCoupon.class)
                                    .putExtra("mode", "returns")
                                    .putExtra("money",list.get(position).money)
                                    .putExtra("id",list.get(position).id)
                                    .putExtra("coupon_id", list.get(position).coupon_id));
                        }
                        break;
                    case R.id.send_item_wallet_cash_discount_full_cut:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_wallet_cash_discount_full_cut)).smoothClose();
                        if (getBaseFragmentActivityContext()!=null){
                            startActivity(new Intent(getBaseFragmentActivityContext(),ActivityWalletIntegral_And_CouponSend.class)
                                    .putExtra("id",list.get(position).id)
                                    .putExtra("mode","coupon"));
                        }
                        break;
                }
            }
        });
        listviewFragmentWalletCashDiscountFullCut.setAdapter(sss_adapter);
    }


   public void walletGetCouponDetails() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.walletGetCouponDetails(
                    new JSONObject()
                            .put("type", type)//1满减券，2现金券，3折扣券
                            .put("p", p)
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CouponModel4 couponModel4 = new CouponModel4();
                                            couponModel4.id = jsonArray.getJSONObject(i).getString("id");
                                            couponModel4.classify_id = jsonArray.getJSONObject(i).getString("classify_id");
                                            couponModel4.describe = jsonArray.getJSONObject(i).getString("describe");
                                            couponModel4.sell_price = jsonArray.getJSONObject(i).getString("sell_price");
                                            couponModel4.sell = jsonArray.getJSONObject(i).getString("sell");
                                            couponModel4.number = jsonArray.getJSONObject(i).getString("number");
                                            couponModel4.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            couponModel4.type = jsonArray.getJSONObject(i).getString("type");
                                            couponModel4.money = jsonArray.getJSONObject(i).getString("money");
                                            couponModel4.price = jsonArray.getJSONObject(i).getString("price");
                                            couponModel4.start_time = jsonArray.getJSONObject(i).getString("start_time");
                                            couponModel4.end_time = jsonArray.getJSONObject(i).getString("end_time");
                                            couponModel4.name = jsonArray.getJSONObject(i).getString("name");
                                            couponModel4.coupon_id = jsonArray.getJSONObject(i).getString("coupon_id");
                                            couponModel4.status = jsonArray.getJSONObject(i).getString("status");
                                            couponModel4.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            couponModel4.duration = jsonArray.getJSONObject(i).getString("duration");
                                            list.add(couponModel4);
                                        }
                                        sss_adapter.setList(list);
                                    }else {
                                        if (p==1){
                                            listviewFragmentWalletCashDiscountFullCut.removeAllViewsInLayout();
                                            listviewFragmentWalletCashDiscountFullCut.postInvalidate();
                                        }
                                    }

                                } else {
                                    if (p==1){
                                        listviewFragmentWalletCashDiscountFullCut.removeAllViewsInLayout();
                                        listviewFragmentWalletCashDiscountFullCut.postInvalidate();
                                    }
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


    public interface OnSelectUserCallBack {
        void onSelectUserCallBack(String member_id, String face, String username, String type);
    }
}
