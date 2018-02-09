package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SOSSellerOrderModel;
import com.sss.car.utils.CarUtils;
import com.sss.car.view.ActivityImages;

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
 * SOS订单卖家版完成/未完成界面
 * Created by leilei on 2017/10/14.
 */

@SuppressLint("ValidFragment")
public class FragmentOrderSOSSeller extends BaseFragment implements RefreshLoadMoreLayout.CallBack {
    public static final int FragmentOrderSOSSeller_Service_unFinish = 1;//1服务中，
    public static final int FragmentOrderSOSSeller_Service_complete = 2;//2服务已完成

    @BindView(R.id.listview_fragment_order_sos_seller)
    InnerListview listviewFragmentOrderSosSeller;

    @BindView(R.id.scoll_view_fragment_order_sos_seller)
    ScrollView scollViewFragmentOrderSosSeller;
    @BindView(R.id.refresh_fragment_order_sos_seller)
    RefreshLoadMoreLayout refreshFragmentOrderSosSeller;
    @BindView(R.id.top_fragment_order_sos_seller)
    ImageView topFragmentOrderSosSeller;

    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    int status;
    public int p = 1;

    SSS_Adapter sss_adapter;

    List<SOSSellerOrderModel> list = new ArrayList<>();
    @BindView(R.id.empty_view)
    SimpleDraweeView emptyView;


    public FragmentOrderSOSSeller(int status) {
        this.status = status;
    }

    public FragmentOrderSOSSeller() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scollViewFragmentOrderSosSeller = null;
        refreshFragmentOrderSosSeller = null;
        topFragmentOrderSosSeller = null;
        listviewFragmentOrderSosSeller = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;

        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_order_sos_seller;
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
                                init();
                                initAdapter();
                                getSOSSellerList();
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


    public void changeList(String sos_id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).sos_id.equals(sos_id)) {
                list.remove(i);
            }
        }
        if (list.size() > 0) {
            sss_adapter.setList(list);
        } else {
            listviewFragmentOrderSosSeller.removeAllViewsInLayout();
        }

    }

    void init() {
        refreshFragmentOrderSosSeller.init(new RefreshLoadMoreLayout.Config(this).canLoadMore(true).canRefresh(true));
        scollViewFragmentOrderSosSeller.setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollViewFragmentOrderSosSeller.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topFragmentOrderSosSeller.setVisibility(View.VISIBLE);
                    } else {
                        topFragmentOrderSosSeller.setVisibility(View.GONE);
                    }
                }
            });
        }
        topFragmentOrderSosSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scollViewFragmentOrderSosSeller.smoothScrollTo(0, 0);
            }
        });
    }

    void initAdapter() {
        if (status == FragmentOrderSOSSeller_Service_unFinish) {
            sss_adapter = new SSS_Adapter<SOSSellerOrderModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_sos_seller_unfinish_adapter, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, SOSSellerOrderModel bean, SSS_Adapter instance) {
                    helper.setText(R.id.service_item_fragment_order_sos_seller_adapter, bean.type);
                    helper.setText(R.id.adress_item_fragment_order_sos_seller_adapter, bean.address);
                    helper.setText(R.id.date_item_fragment_order_sos_seller_adapter, bean.create_time);
                    helper.setText(R.id.content_item_fragment_order_sos_seller_adapter, bean.title);
                    addImageViewList(FrescoUtils.showImage(false, 55, 55, Uri.parse(Config.url + bean.face), (SimpleDraweeView) helper.getView(R.id.pic_item_fragment_order_sos_seller_adapter), 0f));
                    addImageViewList(FrescoUtils.showImage(false, 55, 55, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + R.mipmap.logo_qr),
                            (SimpleDraweeView) helper.getView(R.id.qr_item_fragment_order_sos_seller_adapter), 0f));
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_fragment_order_sos_seller_adapter);
                    helper.setItemChildClickListener(R.id.qr_item_fragment_order_sos_seller_adapter);
                    helper.setItemChildClickListener(R.id.nav_fragment_order_sos_seller_adapter);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.click_item_fragment_order_sos_seller_adapter:
                            CarUtils.orderJump(
                                    getBaseFragmentActivityContext(),
                                    "sos",
                                    Integer.valueOf(list.get(position).status),
                                    list.get(position).sos_id,
                                    false,
                                    null,
                                    null,
                                    null,
                                    null);
                            break;
                        case R.id.qr_item_fragment_order_sos_seller_adapter:
                            List<String> temp = new ArrayList<>();
                            temp.add(Config.url + list.get(position).qr_code);
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                    .putExtra("current", 0));
                            break;
                        case R.id.nav_fragment_order_sos_seller_adapter:
                            String[] gps = list.get(position).gps.split(",");
                            if (gps == null || gps.length != 2) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器GPS返回错误");
                                return;

                            }
                            APPOftenUtils.navigation(getBaseFragmentActivityContext(), Config.latitude, Config.longitude, list.get(position).address, gps[0], gps[1]);
                            break;
                    }
                }
            });

        } else if (status == FragmentOrderSOSSeller_Service_complete) {
            sss_adapter = new SSS_Adapter<SOSSellerOrderModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_order_sos_seller_complete_adapter, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, SOSSellerOrderModel bean, SSS_Adapter instance) {
                    helper.setText(R.id.service_item_fragment_order_sos_seller_adapter, bean.type);
                    helper.setText(R.id.adress_item_fragment_order_sos_seller_adapter, bean.address);
                    helper.setText(R.id.date_item_fragment_order_sos_seller_complete_adapter, bean.create_time);
                    helper.setText(R.id.content_item_fragment_order_sos_seller_complete_adapter, bean.title);
                    addImageViewList(FrescoUtils.showImage(false, 55, 55, Uri.parse(Config.url + bean.face), (SimpleDraweeView) helper.getView(R.id.pic_item_fragment_order_sos_seller_complete_adapter), 0f));
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_fragment_order_sos_seller_complete_adapter);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.click_item_fragment_order_sos_seller_complete_adapter:
                            CarUtils.orderJump(
                                    getBaseFragmentActivityContext(),
                                    "sos",
                                    Integer.valueOf(list.get(position).status),
                                    list.get(position).sos_id,
                                    false,
                                    null,
                                    null,
                                    null,
                                    null);
//                            startActivity(new Intent(getActivity(), OrderSOSDetails.class)
//                                    .putExtra("sos_id", list.get(position).sos_id));
                            break;
                    }
                }
            });
        }

        listviewFragmentOrderSosSeller.setAdapter(sss_adapter);
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
     * 获取SOS列表信息(卖家版)
     */
    public void getSOSSellerList() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getSOSSellerList(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("status", status)
                            .put("type", "2")//	1求助订单,2增援订单
                            .put("p", p)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrderSosSeller != null) {
                                refreshFragmentOrderSosSeller.stopRefresh();
                                refreshFragmentOrderSosSeller.stopLoadMore();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentOrderSosSeller != null) {
                                refreshFragmentOrderSosSeller.stopRefresh();
                                refreshFragmentOrderSosSeller.stopLoadMore();
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
                                            SOSSellerOrderModel sosSellerOrderModel = new SOSSellerOrderModel();
                                            sosSellerOrderModel.sos_id = jsonArray.getJSONObject(i).getString("sos_id");
                                            sosSellerOrderModel.title = jsonArray.getJSONObject(i).getString("title");
                                            sosSellerOrderModel.type = jsonArray.getJSONObject(i).getString("type");
                                            sosSellerOrderModel.gps = jsonArray.getJSONObject(i).getString("gps");
                                            sosSellerOrderModel.order_code = jsonArray.getJSONObject(i).getString("order_code");
                                            sosSellerOrderModel.address = jsonArray.getJSONObject(i).getString("address");
                                            sosSellerOrderModel.qr_code = jsonArray.getJSONObject(i).getString("qr_code");
                                            sosSellerOrderModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            sosSellerOrderModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            sosSellerOrderModel.friend_id = jsonArray.getJSONObject(i).getString("friend_id");
                                            sosSellerOrderModel.face = jsonArray.getJSONObject(i).getString("face");
                                            sosSellerOrderModel.status = jsonArray.getJSONObject(i).getString("status");
                                            list.add(sosSellerOrderModel);
                                        }
                                        sss_adapter.setList(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                                if (list.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                }else {
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        p = 1;
        getSOSSellerList();


    }

    @Override
    public void onLoadMore() {
        getSOSSellerList();

    }
}
