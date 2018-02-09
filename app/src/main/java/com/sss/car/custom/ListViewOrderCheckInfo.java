package com.sss.car.custom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.OrderCheckInfoModel;
import com.sss.car.model.OrderCheckInfoModel_Goods_Data;
import com.sss.car.model.SOSSellerOrderModel;
import com.sss.car.order.OrderSOSAffirmBuyer;
import com.sss.car.order.OrderSOSDetails;
import com.sss.car.order_new.Constant;
import com.sss.car.order_new.OrderModel;
import com.sss.car.order_new.OrderModel_GoodsData;
import com.sss.car.view.ActivityImages;

import java.util.ArrayList;
import java.util.List;

import static com.sss.car.R.id.service_item_fragment_order_sos_seller_adapter;
import static com.sss.car.fragment.FragmentOrderSOSBuyer.FragmentOrderSOSBuyer_Service_complete;
import static com.sss.car.fragment.FragmentOrderSOSBuyer.FragmentOrderSOSBuyer_Service_unFinish;

/**
 * Created by leilei on 2018/1/10.
 */

@SuppressWarnings("ALL")
public class ListViewOrderCheckInfo extends LinearLayout {
    List<OrderCheckInfoModel> list = new ArrayList<>();
    OnListViewOrderCheckInfoCallBack onListViewOrderCheckInfoCallBack;
    View headView;

    public void setOnListViewOrderCheckInfoCallBack(OnListViewOrderCheckInfoCallBack onListViewOrderCheckInfoCallBack) {
        this.onListViewOrderCheckInfoCallBack = onListViewOrderCheckInfoCallBack;
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public List<OrderCheckInfoModel> getList() {
        return list;
    }

    public ListViewOrderCheckInfo(Context context) {
        super(context);
    }

    public ListViewOrderCheckInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrderCheckInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setHeadView(View headView) {
        this.headView = headView;
        addView(headView);
    }

    /**
     * @param context
     * @param list
     */
    public void setList(Context context, List<OrderCheckInfoModel> list) {
        this.removeAllViews();
        if (headView != null) {
            addView(headView);
        }
        this.list = list;


        showData(context);
    }

    /**
     * @param context
     */
    void showData(final Context context) {
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;

            if ("1".equals(list.get(i).type) || "2".equals(list.get(i).type)) {


                View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_order, null);
                LinearLayout click_custom_listview_order = $.f(view, R.id.click_custom_listview_order);
                SimpleDraweeView pic_custom_listview_order = $.f(view, R.id.pic_custom_listview_order);
                final InnerListview list_custom_listview_order = $.f(view, R.id.list_custom_listview_order);
                TextView order_code_custom_listview_order = $.f(view, R.id.order_code_custom_listview_order);
                TextView order_date_custom_listview_order = $.f(view, R.id.order_date_custom_listview_order);
                TextView target_name_custom_listview_order = $.f(view, R.id.name_custom_listview_order);
                final TextView state_custom_listview_order = $.f(view, R.id.state_custom_listview_order);
                order_code_custom_listview_order.setText(list.get(finalI).order_code);
                order_date_custom_listview_order.setText(list.get(finalI).create_time);
                state_custom_listview_order.setText(list.get(finalI).status_name);
                if (!list.get(finalI).member_id.equals(Config.member_id)) {
                    target_name_custom_listview_order.setText(list.get(finalI).name);
                    FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(finalI).picture), pic_custom_listview_order, 30f);
                    click_custom_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCheckInfoCallBack != null) {
                                onListViewOrderCheckInfoCallBack.onUser(list.get(finalI).member_id, list.get(finalI).name);
                            }
                        }
                    });
                } else {
                    target_name_custom_listview_order.setText(list.get(finalI).name);
                    FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(finalI).picture), pic_custom_listview_order, 30f);
                    click_custom_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCheckInfoCallBack != null) {
                                onListViewOrderCheckInfoCallBack.onShop(list.get(finalI).shop_id);
                            }
                        }
                    });
                }

                SSS_Adapter sss_adapter = new SSS_Adapter<OrderCheckInfoModel_Goods_Data>(context, R.layout.item_custom_listview_order_adapter) {
                    @Override
                    protected void setView(final SSS_HolderHelper helper, final int position, OrderCheckInfoModel_Goods_Data bean, SSS_Adapter instance) {
                        helper.setText(R.id.content_item_custom_listview_order_adapter, bean.title);
                        helper.setText(R.id.price_item_custom_listview_order_adapter, "¥" + bean.price);
                        helper.setText(R.id.number_item_custom_listview_order_adapter, "×" + bean.number);
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_custom_listview_order_adapter)), 0f);

                        switch (list.get(finalI).status) {
                            case Constant.Non_Payment://未付款
                                helper.setVisibility(R.id.qr_item_custom_listview_order_adapter, GONE);
                                break;
                            case Constant.Ready_Buy://未付款
                                helper.setVisibility(R.id.qr_item_custom_listview_order_adapter, GONE);
                                break;
                        }

                    }

                    @Override
                    protected void setItemListener(SSS_HolderHelper helper) {
                        helper.setItemChildClickListener(R.id.click_item_custom_listview_order_adapter);
                        helper.setItemChildClickListener(R.id.qr_item_custom_listview_order_adapter);
                    }


                };
                sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                    @Override
                    public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                        super.onItemChildClick(view, position, holder);
                        switch (view.getId()) {
                            case R.id.qr_item_custom_listview_order_adapter:
                                if (onListViewOrderCheckInfoCallBack != null) {
                                    onListViewOrderCheckInfoCallBack.onQR(list.get(finalI).qr_code);
                                }
                                break;
                            case R.id.click_item_custom_listview_order_adapter:
                                if (onListViewOrderCheckInfoCallBack != null) {
                                    onListViewOrderCheckInfoCallBack.onItem(list.get(finalI).type, list.get(finalI).order_id, list.get(finalI).shop_id, list.get(finalI).status, list.get(finalI), position);
                                }

                                break;
                        }
                    }
                });
                list_custom_listview_order.setAdapter(sss_adapter);
                sss_adapter.setList(list.get(finalI).goods_data);
                this.addView(view);
            } else if ("3".equals(list.get(i).type)) {

                View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_sos, null);
                InnerListview listview = $.f(view, R.id.listview);
                if (list.get(finalI).status == FragmentOrderSOSBuyer_Service_unFinish) {//1进行中,2已完成,3已取消
                    listview.setAdapter(new SSS_Adapter<OrderCheckInfoModel_Goods_Data>(context, R.layout.item_fragment_order_sos_seller_unfinish_adapter, list.get(finalI).goods_data) {
                        @Override
                        protected void setView(SSS_HolderHelper helper, final int position, final OrderCheckInfoModel_Goods_Data bean, SSS_Adapter instance) {
                            helper.setText(R.id.service_item_fragment_order_sos_seller_adapter, list.get(finalI).status_name);
                            helper.setText(R.id.adress_item_fragment_order_sos_seller_adapter, bean.address);
                            helper.setText(R.id.date_item_fragment_order_sos_seller_adapter, list.get(finalI).create_time);
                            helper.setText(R.id.content_item_fragment_order_sos_seller_adapter, bean.title);
                            FrescoUtils.showImage(false, 55, 55, Uri.parse(Config.url + bean.master_map), (SimpleDraweeView) helper.getView(R.id.pic_item_fragment_order_sos_seller_adapter), 0f);
                            FrescoUtils.showImage(false, 55, 55, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_qr),
                                    (SimpleDraweeView) helper.getView(R.id.qr_item_fragment_order_sos_seller_adapter), 0f);
                            helper.getView(R.id.click_item_fragment_order_sos_seller_adapter).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(new Intent(context, OrderSOSAffirmBuyer.class)
                                            .putExtra("sos_id", bean.goods_id));
                                }
                            });
                            helper.getView(R.id.qr_item_fragment_order_sos_seller_adapter).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<String> temp = new ArrayList<>();
                                    temp.add(Config.url + list.get(position).qr_code);
                                    context.startActivity(new Intent(context, ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                            .putExtra("current", 0));
                                }
                            });
                            helper.getView(R.id.nav_fragment_order_sos_seller_adapter).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    APPOftenUtils.navigation(context, Config.latitude, Config.longitude, "目标地址", list.get(finalI).lat, list.get(finalI).lng);
                                }
                            });
                        }

                        @Override
                        protected void setItemListener(SSS_HolderHelper helper) {
                        }
                    });

                } else if (list.get(finalI).status == FragmentOrderSOSBuyer_Service_complete) {//1进行中,2已完成,3已取消

                    listview.setAdapter(new SSS_Adapter<OrderCheckInfoModel_Goods_Data>(context, R.layout.item_fragment_order_sos_seller_complete_adapter, list.get(finalI).goods_data) {
                        @Override
                        protected void setView(SSS_HolderHelper helper, final int position, final OrderCheckInfoModel_Goods_Data bean, SSS_Adapter instance) {
                            helper.setText(R.id.service_item_fragment_order_sos_seller_adapter, list.get(finalI).status_name);
                            helper.setText(R.id.adress_item_fragment_order_sos_seller_adapter, bean.address);
                            helper.setText(R.id.date_item_fragment_order_sos_seller_complete_adapter, list.get(finalI).create_time);
                            helper.setText(R.id.content_item_fragment_order_sos_seller_complete_adapter, bean.title);
                            FrescoUtils.showImage(false, 55, 55, Uri.parse(Config.url + bean.master_map), (SimpleDraweeView) helper.getView(R.id.pic_item_fragment_order_sos_seller_complete_adapter), 0f);
                            helper.getView(R.id.click_item_fragment_order_sos_seller_complete_adapter).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(new Intent(context, OrderSOSDetails.class)
                                            .putExtra("sos_id", bean.goods_id));
                                }
                            });


                        }

                        @Override
                        protected void setItemListener(SSS_HolderHelper helper) {
                        }
                    });
                }
                this.addView(view);
            }
        }
    }


    public interface OnListViewOrderCheckInfoCallBack {

        void onItem(String type, String order_id, String shop_id, int status, OrderCheckInfoModel orderCheckInfoModel, int position);

        void onShop(String shop_id);

        void onUser(String shop_id, String target_name);

        void onQR(String qr);
    }


}
