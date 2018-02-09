package com.sss.car.custom;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.MyRecycleview.FullyLinearLayoutManager;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.OrderModel;
import com.sss.car.model.OrderModel_goods_data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.sss.car.R.id.cb_listview_order;
import static com.sss.car.R.id.click_item_listview_order_adapter;


/**
 * Created by leilei on 2017/10/7.
 */

public class ListViewOrder extends LinearLayout {
    public static final int TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY = 111;//买家版待处理(退换货状态下--支出)
    public static final int TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER = 112;//卖家版待处理(退换货状态下--收入)
    public static final int TYPE_WAITING_FOR_SEND = 113;//待发货(收入)
    public static final int TYPE_WAITING_FOR_SERVICE_FROM_BUY = 114;//买家版待服务(支出)
    public static final int TYPE_WAITING_FOR_SERVICE_FROM_SELLER = 115;//卖家版待服务(收入)
    public static final int TYPE_WAITING_FOR_COMMENT_FROM_BUY = 116;//买家版待评价(支出)
    public static final int TYPE_WAITING_FOR_COMMENT_FROM_SELLER = 117;//卖家版待评价(收入)
    public static final int TYPE_WAITING_FOR_READY_BUY = 118;//待预购(支出)
    public static final int TYPE_WAITING_FOR_PAYMENT = 119;//待付款(支出)
    public static final int TYPE_WAITING_FOR_RECEIVING = 120;//待收货(支出)
    public static final int TYPE_WAITING_FOR_RETURNS_FROM_BUY = 121;//买家版待退货(支出)
    public static final int TYPE_WAITING_FOR_RETURNS_FROMSELLER = 122;//卖家版待退货(收入)
    public static final int TYPE_WAITING_FOR_CHANGE_FROM_BUY = 123;//买家版待换换货(支出)
    public static final int TYPE_WAITING_FOR_CHANGE_FROMSELLER = 124;//卖家版待换换货(收入)
    public static final int TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY = 125;//买家版待处理(正常状态下--支出)
    public static final int TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY = 126;//买家版已完成(退换货状态下--支出)
    public static final int TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_SELLER = 127;//卖家版已完成(退换货状态下--收入)
    public static final int TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY = 128;//买家版已完成(正常状态下--支出)
    public static final int TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_SELLER = 129;//卖家版已完成(正常状态下--收入)


    List<OrderModel> list = new ArrayList<>();
    ListViewOrderCallBack listViewOrderCallBack;

    View headView;

    int mode = -1;
    boolean isShowCheckBox;
    boolean isShowQR;
    boolean isShowInnerCheckBox;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        listViewOrderCallBack = null;
    }

    public List<OrderModel> getList() {
        return list;
    }

    public ListViewOrder(Context context) {
        super(context);
    }

    public ListViewOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListViewOrderCallBack(ListViewOrderCallBack listViewOrderCallBack) {
        this.listViewOrderCallBack = listViewOrderCallBack;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public void setList(Context context, List<OrderModel> list, int mode, boolean isShowCheckBox, boolean isShowQR) {
        this.removeAllViews();
        if (headView != null) {
            addView(headView);
        }
        this.list = list;
        this.mode = mode;
        this.isShowCheckBox = isShowCheckBox;
        this.isShowQR = isShowQR;

        showData(context);
    }

    void showData(Context context) {
        LogUtils.e(list.size());
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_order, null);
            final CheckBox cb_listview_order = $.f(view, R.id.cb_listview_order);
            LinearLayout click_listview_order = $.f(view, R.id.click_listview_order);
            SimpleDraweeView pic_listview_order = $.f(view, R.id.pic_listview_order);
            final InnerListview list_listview_order = $.f(view, R.id.list_listview_order);
            TextView order_code_listview_order = $.f(view, R.id.order_code_listview_order);
            TextView order_date_listview_order = $.f(view, R.id.order_date_listview_order);
            TextView name_listview_order = $.f(view, R.id.name_listview_order);
            TextView connect_buyer_listview_order = $.f(view, R.id.connect_buyer_listview_order);
            TextView cancel_listview_order = $.f(view, R.id.cancel_listview_order);
            TextView send_listview_order = $.f(view, R.id.send_listview_order);
            TextView navigation_listview_order = $.f(view, R.id.navigation_listview_order);
            TextView complete_service_listview_order = $.f(view, R.id.complete_service_listview_order);
            TextView connect_seller_listview_order = $.f(view, R.id.connect_seller_listview_order);
            TextView comment_listview_order = $.f(view, R.id.comment_listview_order);
            TextView sure_order_listview_order = $.f(view, R.id.sure_order_listview_order);
            final TextView logistics_listview_order = $.f(view, R.id.logistics_listview_order);
            TextView delete_order_listview_order = $.f(view, R.id.delete_order_listview_order);
            TextView returns_listview_order = $.f(view, R.id.returns_listview_order);
            TextView complete_data_listview_order = $.f(view, R.id.complete_data_listview_order);
            TextView dispose_order_listview_order = $.f(view, R.id.dispose_order_listview_order);
            order_code_listview_order.setText(list.get(finalI).order_code);
            order_date_listview_order.setText(list.get(finalI).create_time);
            name_listview_order.setText(list.get(finalI).name);

            if (isShowCheckBox) {
                cb_listview_order.setVisibility(VISIBLE);
                cb_listview_order.setChecked(list.get(finalI).isChoose);
                cb_listview_order.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.get(finalI).isChoose = cb_listview_order.isChecked();
                        for (int j = 0; j < list.get(finalI).data.size(); j++) {
                            list.get(finalI).data.get(j).isChoose = cb_listview_order.isChecked();
                        }
                        if (listViewOrderCallBack != null) {
                            listViewOrderCallBack.onCheckedChangedFromOutter(list, finalI, cb_listview_order.isChecked());
                        }
                    }
                });
            } else {
                cb_listview_order.setVisibility(GONE);
            }
            click_listview_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listViewOrderCallBack != null) {
                        if (listViewOrderCallBack != null) {
                            listViewOrderCallBack.onName(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                        }
                    }
                }
            });
            switch (mode) {
                case TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_SELLER://卖家版已完成(正常状态下--收入)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_buyer_listview_order, "联系卖家");
                    break;
                case TYPE_WAITING_FOR_COMPLETE_NORMAL_FROM_BUY://买家版已完成(正常状态下--支出)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(delete_order_listview_order, "删除订单");
                    delete_order_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onDeleteOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_SELLER://卖家版已完成(退换货状态下--收入)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_buyer_listview_order, "联系卖家");
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY://买家版已完成(退换货状态下--支出)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_buyer_listview_order, "联系卖家");
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY://买家版待处理(退换货状态下--支出)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_buyer_listview_order, "联系卖家");
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY://买家版待处理(正常状态下--支出)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_buyer_listview_order, "联系卖家");
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    /**0退货：0未处理，1已同意，2未同意，3已发货，4已退款    type = 1
                     换货：0未处理，1已同意，2未同意，3已发货，4已收货，5已发货，7已完成    type = 2
                     */
                    if ("1".equals(list.get(finalI).exchange_status)) {
                        addLine(complete_data_listview_order, "完善资料");
                        complete_data_listview_order.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listViewOrderCallBack != null) {
                                    listViewOrderCallBack.onCompleteData(list.get(finalI), list.get(finalI).target_id, list.get(finalI).target_name, list.get(finalI).exchange_id);
                                }
                            }
                        });
                    }
                    break;
                case TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER://卖家版待处理(退换货状态下--收入)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);
                    addLine(connect_buyer_listview_order, "联系买家");
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    /**0退货：0未处理，1已同意，2未同意，3已发货，4已退款    type = 1
                     换货：0未处理，1已同意，2未同意，3已发货，4已收货，5已发货，7已完成    type = 2
                     */
                    if ("0".equals(list.get(finalI).exchange_status)) {
                        addLine(dispose_order_listview_order, "立即处理");
                        dispose_order_listview_order.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listViewOrderCallBack != null) {
                                    listViewOrderCallBack.onDispose(list.get(finalI), list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id, list.get(finalI).exchange_id);
                                }
                            }
                        });
                    }

                    break;
                case TYPE_WAITING_FOR_SEND://待发货(卖家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);

                    addLine(send_listview_order, "立即发货");
                    addLine(connect_buyer_listview_order, "联系买家");
                    send_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (listViewOrderCallBack != null) {
//                                listViewOrderCallBack.onSend(list.get(finalI).order_id);
//                            }
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onClickFromGoods(list.get(finalI).order_id);
                            }
                        }
                    });
                    connect_buyer_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectBuyer(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_SERVICE_FROM_BUY://待服务(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
//                    addLine(navigation_listview_order, "一键导航");
                    addLine(cancel_listview_order, "取消订单");
                    addLine(complete_service_listview_order, "完成服务");
//                    navigation_listview_order.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (listViewOrderCallBack != null) {
//                                listViewOrderCallBack.onNavigation(list.get(finalI).order_id, list.get(finalI).lat, list.get(finalI).lng);
//                            }
//                        }
//                    });
                    complete_service_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                int a = 0;
                                for (int j = 0; j < list.get(finalI).data.size(); j++) {
                                    a = a + Integer.valueOf(list.get(finalI).data.get(j).price);
                                }
                                listViewOrderCallBack.onCompleteService(list.get(finalI).order_id, a);
                            }
                        }
                    });
                    cancel_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCancelOrder(list.get(finalI).order_id, list);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_SERVICE_FROM_SELLER://待服务(卖家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);
                    addLine(navigation_listview_order, "一键导航");
                    addLine(cancel_listview_order, "取消订单");
                    navigation_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onNavigation(list.get(finalI).order_id, list.get(finalI).lat, list.get(finalI).lng);
                            }
                        }
                    });
                    cancel_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCancelOrder(list.get(finalI).order_id, list);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_RECEIVING://待收货(买家版)

                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(logistics_listview_order, "查看物流");
                    addLine(returns_listview_order, "退换货申请");
                    addLine(sure_order_listview_order, "确认收货");
                    logistics_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onLogistics(list.get(finalI).order_id);
                            }
                        }
                    });
                    returns_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onReturns(list.get(finalI), list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });
                    sure_order_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onSureOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_COMMENT_FROM_BUY://待评价(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(comment_listview_order, "立即评价");
                    comment_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCommentOrder(list.get(finalI).order_id, null, list.get(finalI).data);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_COMMENT_FROM_SELLER://待评价(卖家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);
                    addLine(comment_listview_order, "立即评价");
                    comment_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCommentOrder(list.get(finalI).order_id, list.get(finalI).picture, list.get(finalI).data);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_READY_BUY://待预购(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(cancel_listview_order, "取消订单");
                    addLine(connect_seller_listview_order, "联系卖家");
                    cancel_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCancelOrder(list.get(finalI).order_id, list);
                            }
                        }
                    });
                    connect_seller_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectSeller(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_PAYMENT://待付款(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(cancel_listview_order, "取消订单");
                    cancel_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onCancelOrder(list.get(finalI).order_id, list);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_RETURNS_FROM_BUY://待退货(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_listview_order, 0f);
                    addLine(connect_seller_listview_order, "联系卖家");

                    connect_seller_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectSeller(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_RETURNS_FROMSELLER://待退货(卖家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);
                    addLine(connect_seller_listview_order, "联系买家");
                    addLine(sure_order_listview_order, "确认收货");
                    sure_order_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onSureOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    connect_seller_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectSeller(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_CHANGE_FROM_BUY://待换货(买家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.fromFile(new File("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no)), pic_listview_order, 0f);
                    addLine(connect_seller_listview_order, "联系卖家");
                    addLine(sure_order_listview_order, "确认收货");
                    connect_seller_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectSeller(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    sure_order_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onSureOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    break;
                case TYPE_WAITING_FOR_CHANGE_FROMSELLER://待换货(卖家版)
                    FrescoUtils.showImage(false, 30, 30, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 30f);
                    addLine(connect_seller_listview_order, "联系买家");
                    connect_seller_listview_order.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onConnectSeller(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                            }
                        }
                    });
                    break;
            }

            SSS_Adapter sss_adapter = new SSS_Adapter<OrderModel_goods_data>(context, R.layout.item_listview_order_adapter) {
                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, OrderModel_goods_data bean,SSS_Adapter instance) {
                    if (isShowInnerCheckBox) {
                        helper.setChecked(R.id.cb_item_listview_order_adapter, bean.isChoose);
                        ((CheckBox) helper.getView(R.id.cb_item_listview_order_adapter)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listViewOrderCallBack != null) {
                                    boolean a = ((CheckBox) helper.getView(R.id.cb_item_listview_order_adapter)).isChecked();
                                    list.get(finalI).data.get(position).isChoose = a;
                                    if (!a) {
                                        list.get(finalI).isChoose = a;
                                    }
                                    listViewOrderCallBack.onCheckedChangedFromInner(list, finalI, position, a);
                                }
                            }
                        });
                        helper.setVisibility(R.id.cb_item_listview_order_adapter, VISIBLE);
                    } else {
                        helper.setVisibility(R.id.cb_item_listview_order_adapter, GONE);
                    }
                    helper.setText(R.id.content_item_listview_order_adapter, bean.title);
                    helper.setText(R.id.price_item_listview_order_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_listview_order_adapter, "×" + bean.number);
                    FrescoUtils.showImage(false, 50, 50, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_listview_order_adapter)), 0f);

                    if (isShowQR) {
                        helper.setVisibility(R.id.qr_item_listview_order_adapter, VISIBLE);
                        helper.setItemChildClickListener(R.id.qr_item_listview_order_adapter);
                    } else {
                        helper.setVisibility(R.id.qr_item_listview_order_adapter, GONE);
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(click_item_listview_order_adapter);

                }


            };
            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    super.onItemChildClick(view, position, holder);
                    switch (view.getId()) {
                        case R.id.qr_item_listview_order_adapter:
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onQR(list.get(finalI).qr_code);
                            }
                            break;
                        case click_item_listview_order_adapter:
                            if (listViewOrderCallBack != null) {
                                listViewOrderCallBack.onClickFromGoods(list.get(finalI).order_id);
                            }

                            break;
                    }
                }
            });


            list_listview_order.setAdapter(sss_adapter);


            sss_adapter.setList(list.get(finalI).data);

            this.addView(view);
        }
    }

    void addLine(TextView textView, String content) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
        textView.setVisibility(VISIBLE);
        textView.setText(content);

    }

    int a = 0;

    public void totalPrice() {
        a = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).data.size(); j++) {
                if (list.get(i).data.get(j).isChoose) {
                    a = a + Integer.valueOf(list.get(i).data.get(j).number) * Integer.valueOf(list.get(i).data.get(j).price);
                }
            }
        }
        if (listViewOrderCallBack != null) {
            listViewOrderCallBack.onTotalPrice(a);
        }
    }

    public interface ListViewOrderCallBack {

        void onDispose(OrderModel orderModel, String targetPic, String targetName, String targetId, String exchange_id);

        void onConnectBuyer(String targetPic, String targetName, String targetId);

        void onConnectSeller(String targetPic, String targetName, String targetId);

        void onName(String targetPic, String targetName, String targetId);

        void onQR(String QR);

        void onClickFromGoods(String targetOrderId);

        void onCancelOrder(String targetOrderId, List<OrderModel> list);

        void onCommentOrder(String targetOrderId, String targetPic, List<OrderModel_goods_data> data);

        void onLogistics(String targetOrderId);

        void onSend(String targetOrderId);

        void onReturns(OrderModel orderModel, String targetId, String targetName);

        void onSureOrder(String targetOrderId);

        void onDeleteOrder(String targetOrderId);

        void onNavigation(String targetOrderId, String lat, String lng);

        void onCompleteService(String targetOrderId, int price);

        void onCompleteData(OrderModel orderModel, String targetId, String targetName, String exchange_id);

        void onTotalPrice(int price);

        void onCheckedChangedFromOutter(List<OrderModel> list, int i, boolean isChecked);

        void onCheckedChangedFromInner(List<OrderModel> list, int i, int position, boolean isChecked);
    }

}
