package com.sss.car.coupon;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCoupon;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CouponGetModel_List;
import com.sss.car.view.ActivityCoupon;
import com.sss.car.view.ActivityWalletMyCouponDetails;
import com.sss.car.view.CouponEdit;

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

import static android.R.id.list;


/**
 * 优惠券管理
 * Created by leilei on 2017/11/3.
 */

public class CouponManager extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_coupon_manager)
    InnerListview listviewCouponManager;
    @BindView(R.id.scollview_coupon_manager)
    PullToRefreshScrollView scollviewCouponManager;
    @BindView(R.id.top_coupon_manager)
    ImageView topCouponManager;
    @BindView(R.id.coupon_manager)
    LinearLayout couponManager;

    YWLoadingDialog ywLoadingDialog;

    int p = 1;

    SSS_Adapter sss_adapter;
    List<CouponGetModel_List> data = new ArrayList<>();


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
        listviewCouponManager = null;
        scollviewCouponManager = null;
        topCouponManager = null;
        couponManager = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedCoupon changedCoupon) {
        p = 1;
        manageCoupon();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_manager);
        ButterKnife.bind(this);
        customInit(couponManager, false, true, true);
        titleTop.setText("优惠券管理");
        scollviewCouponManager.setMode(PullToRefreshBase.Mode.BOTH);
        scollviewCouponManager.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                manageCoupon();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                manageCoupon();
            }
        });
        initAdapter();
        manageCoupon();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollviewCouponManager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topCouponManager.setVisibility(View.VISIBLE);
                    } else {
                        topCouponManager.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    @OnClick({R.id.back_top, R.id.top_coupon_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.top_coupon_manager:
                scollviewCouponManager.scrollTo(0, 0);
                break;
        }
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<CouponGetModel_List>(getBaseActivityContext(), R.layout.item_coupon_manager) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, CouponGetModel_List bean, final SSS_Adapter instance) {
                helper.setText(R.id.discounts_item_coupon_manager, bean.money);
                helper.setText(R.id.date_item_coupon_manager, bean.duration);
                helper.setText(R.id.title_item_coupon_manager, bean.name);
                helper.setText(R.id.slogan_item_coupon_manager, bean.describe);
                helper.setText(R.id.price_item_coupon_manager, "¥" + bean.sell_price);
                addImageViewList(FrescoUtils.showImage(false, 150, 110, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_coupon), ((SimpleDraweeView) helper.getView(R.id.pic_item_coupon_manager)), 0f));
                  /*1满减券，2现金券，3折扣券*/
                switch (bean.type) {
                    case "1":
                        helper.setText(R.id.type_item_coupon_manager, "满减券");
                        break;
                    case "2":
                        helper.setText(R.id.type_item_coupon_manager, "现金券");
                        break;
                    case "3":
                        helper.setText(R.id.type_item_coupon_manager, "折扣券");
                        break;
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_coupon_manager);
                helper.setItemChildClickListener(R.id.delete_item_coupon_manager);
                helper.setItemChildClickListener(R.id.see_item_coupon_manager);
                helper.setItemChildClickListener(R.id.edit_item_coupon_manager);
            }

        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.delete_item_coupon_manager:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_coupon_manager)).smoothClose();
                        del_coupon(data.get(position).coupon_id, position);
                        break;
                    case R.id.see_item_coupon_manager:
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityCoupon.class)
                                    .putExtra("money",data.get(position).money)
                                    .putExtra("mode", "coupon_manager")
                                    .putExtra("coupon_id", data.get(position).coupon_id));
                        }
                        break;
                    case R.id.click_item_coupon_manager:
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityCoupon.class)
                                    .putExtra("mode", "coupon_manager")
                                    .putExtra("money",data.get(position).money)
                                    .putExtra("coupon_id", data.get(position).coupon_id));
                        }
                        break;
                    case R.id.edit_item_coupon_manager:
                        ((SwipeMenuLayout) holder.getView(R.id.scoll_item_coupon_manager)).smoothClose();
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), CouponEdit.class)
                                    .putExtra("mode", "coupon_manager")
                                    .putExtra("data", data.get(position)));
                        }
                        break;
                }
            }
        });

        listviewCouponManager.setAdapter(sss_adapter);
    }

    /**
     * 优惠券管理
     */
    public void manageCoupon() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.manageCoupon(
                    new JSONObject()
                            .put("p", p)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (scollviewCouponManager != null) {
                                scollviewCouponManager.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (scollviewCouponManager != null) {
                                scollviewCouponManager.onRefreshComplete();
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
                                            couponGetModel_list.id = jsonArray1.getJSONObject(j).getString("id");
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
                                            couponGetModel_list.duration = jsonArray1.getJSONObject(j).getString("duration");
                                            data.add(couponGetModel_list);
                                        }
                                        sss_adapter.setList(data);

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

    /**
     * 优惠券管理
     */
    public void del_coupon(String coupon_id, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_coupon(
                    new JSONObject()
                            .put("coupon_id", coupon_id)
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
                                    data.remove(position);
                                    if (data.size() > 0) {
                                        sss_adapter.setList(data);
                                    } else {
                                        listviewCouponManager.removeAllViewsInLayout();
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

}
