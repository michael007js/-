package com.sss.car.order_new;

import android.content.Context;
import android.graphics.Paint;
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
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 支出
 * Created by leilei on 2017/10/7.
 */

@SuppressWarnings("ALL")
public class CustomListViewOrderBuyer extends LinearLayout {
    List<OrderModel> list = new ArrayList<>();
    OnListViewOrderCallBack onListViewOrderCallBack;

    View headView;


    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        onListViewOrderCallBack = null;
    }

    public List<OrderModel> getList() {
        return list;
    }

    public CustomListViewOrderBuyer(Context context) {
        super(context);
    }

    public CustomListViewOrderBuyer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListViewOrderBuyer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnListViewOrderCallBack(OnListViewOrderCallBack onListViewOrderCallBack) {
        this.onListViewOrderCallBack = onListViewOrderCallBack;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    /**
     * @param context
     * @param list
     * @param what    1支出2收入
     */
    public void setList(Context context, List<OrderModel> list, int what) {
        this.removeAllViews();
        if (headView != null) {
            addView(headView);
        }
        this.list = list;


        showData(context, what);
    }

    /**
     * @param context
     * @param what    1支出2收入
     */
    void showData(Context context, int what) {
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_order, null);
            LinearLayout click_custom_listview_order = $.f(view, R.id.click_custom_listview_order);
            SimpleDraweeView pic_custom_listview_order = $.f(view, R.id.pic_custom_listview_order);
            final InnerListview list_custom_listview_order = $.f(view, R.id.list_custom_listview_order);
            TextView order_code_custom_listview_order = $.f(view, R.id.order_code_custom_listview_order);
            TextView order_date_custom_listview_order = $.f(view, R.id.order_date_custom_listview_order);
            TextView target_name_custom_listview_order = $.f(view, R.id.name_custom_listview_order);
            final TextView state_custom_listview_order = $.f(view, R.id.state_custom_listview_order);
            TextView one_order_custom_listview_order = $.f(view, R.id.one_order_custom_listview_order);
            TextView two_order_custom_listview_order = $.f(view, R.id.two_order_custom_listview_order);
            TextView three_order_custom_listview_order = $.f(view, R.id.three_order_custom_listview_order);
            TextView four_order_custom_listview_order = $.f(view, R.id.four_order_custom_listview_order);
            order_code_custom_listview_order.setText("订单编号"+list.get(finalI).order_code);
            order_date_custom_listview_order.setText(list.get(finalI).create_time);
            state_custom_listview_order.setText(list.get(finalI).status_name);
            if (what == 1) {
                target_name_custom_listview_order.setText(list.get(finalI).target_name);
                FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(finalI).picture), pic_custom_listview_order, 30f);
                click_custom_listview_order.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onListViewOrderCallBack != null) {
                            onListViewOrderCallBack.onUser(list.get(finalI).shop_id, list.get(finalI).target_name);
                        }
                    }
                });
            } else if (what == 2) {
                target_name_custom_listview_order.setText(list.get(finalI).name);
                FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(finalI).picture), pic_custom_listview_order, 30f);
                click_custom_listview_order.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onListViewOrderCallBack != null) {
                            onListViewOrderCallBack.onShop(list.get(finalI).shop_id);
                        }
                    }
                });
            }

            LogUtils.e(list.get(finalI).status);
            switch (list.get(finalI).status) {
                case Constant.Non_Payment://未付款
                    addLine(one_order_custom_listview_order, "取消订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onCancelOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "立即付款").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onPayment(list.get(finalI));
                            }
                        }
                    });
                    addLine(three_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });

                    break;
                case Constant.Have_Already_Paid_Awating_Delivery://已付款（待发货）
                    addLine(one_order_custom_listview_order, "取消订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onCancelOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    if ("2".equals(list.get(finalI).type)) {
                        addLine(two_order_custom_listview_order, "待服务").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (onListViewOrderCallBack != null) {
//                                    onListViewOrderCallBack.onConfirmedService(list.get(finalI).order_id);
//                                }
                            }
                        });
                    } else if ("1".equals(list.get(finalI).type)) {
                        addLine(three_order_custom_listview_order, "待发货").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    addLine(four_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });

                    break;
                case Constant.Have_Already_Delivery_Awating_Sign_For://已发货（待签收）
                    addLine(one_order_custom_listview_order, "查看物流").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onLogistics(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "确认收货").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConfirmReceipt(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(three_order_custom_listview_order, "退换货").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onReturnsAndChanged(list.get(finalI));
                            }
                        }
                    });
                    break;
                case Constant.Awating_Comment://待评价
                    if ("0".equals(list.get(finalI).is_comment)) {
                        addLine(one_order_custom_listview_order, "立即评论").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onListViewOrderCallBack != null) {
                                    onListViewOrderCallBack.onImmediateComment(list.get(finalI));
                                }
                            }
                        });
                    } else {
                        addLine(one_order_custom_listview_order, "删除订单").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onListViewOrderCallBack != null) {
                                    onListViewOrderCallBack.onDeleteOrder(list.get(finalI).order_id);
                                }
                            }
                        });
                    }

                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });
                    break;

                case Constant.Awating_Dispose_Returns://待处理（已申请退货）

                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });

                    if ("1".equals(list.get(finalI).exchange_status)) {
                        addLine(three_order_custom_listview_order, "完善资料").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onListViewOrderCallBack != null) {
                                    onListViewOrderCallBack.onCompleteTheInformation(list.get(finalI));
                                }
                            }
                        });
                    } else if ("0".equals(list.get(finalI).exchange_status)) {
                        addLine(one_order_custom_listview_order, "未处理").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                    break;
                case Constant.Returns://已退货

                    addLine(one_order_custom_listview_order, "查看物流").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onLogistics(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });

                    break;
                case Constant.Changed://已换货
                    if ("3".equals(list.get(finalI).exchange_status)) {
                        addLine(one_order_custom_listview_order, "确认收货").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    } else if ("4".equals(list.get(finalI).exchange_status)) {
                        addLine(one_order_custom_listview_order, "待发货").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    } else if ("5".equals(list.get(finalI).exchange_status)) {
                        addLine(one_order_custom_listview_order, "已完成").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onListViewOrderCallBack != null) {
                                    onListViewOrderCallBack.onConfirmReceipt(list.get(finalI).order_id);
                                }
                            }
                        });
                    } else if ("6".equals(list.get(finalI).exchange_status)) {
                        addLine(one_order_custom_listview_order, "待签收").setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onListViewOrderCallBack != null) {
                                    onListViewOrderCallBack.onConfirmReceipt(list.get(finalI).target_id);
                                }
                            }
                        });
                    }
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });


                    break;
                case Constant.Refunded://已退款
                    addLine(one_order_custom_listview_order, "已完成").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });
                    addLine(three_order_custom_listview_order, "删除订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onDeleteOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    break;
                case Constant.Awating_Service://待服务
                    addLine(one_order_custom_listview_order, "取消订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onCancelOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });
                    break;
                case Constant.Ready_Buy://预购
                    addLine(one_order_custom_listview_order, "取消订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onCancelOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });
                    break;
                case Constant.Cancel://取消
                    addLine(one_order_custom_listview_order, "删除订单").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onDeleteOrder(list.get(finalI).order_id);
                            }
                        }
                    });
                    addLine(two_order_custom_listview_order, "联系卖家").setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onConnectSeller(list.get(finalI).target_id, list.get(finalI).target_name);
                            }
                        }
                    });

                    break;

            }

            SSS_Adapter sss_adapter = new SSS_Adapter<OrderModel_GoodsData>(context, R.layout.item_custom_listview_order_adapter) {
                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, OrderModel_GoodsData bean, SSS_Adapter instance) {
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
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onQRCode(Config.url+list.get(finalI).qr_code);
                            }
                            LogUtils.e(Config.url+list.get(finalI).qr_code);
                            break;
                        case R.id.click_item_custom_listview_order_adapter:
                            if (onListViewOrderCallBack != null) {
                                onListViewOrderCallBack.onItem(list.get(finalI).type, list.get(finalI).order_id, list.get(finalI).shop_id, list.get(finalI).status, list.get(finalI));
                            }

                            break;
                    }
                }
            });


            list_custom_listview_order.setAdapter(sss_adapter);

            sss_adapter.setList(list.get(finalI).goods_data);

            this.addView(view);
        }
    }

    TextView addLine(TextView textView, String content) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
        textView.setVisibility(VISIBLE);
        textView.setText(content);
        return textView;
    }


    public interface OnListViewOrderCallBack {

        void onQRCode(String qr);

        /**
         * 列表项被点击
         */
        void onItem(String type, String order_id, String shop_id, int status, OrderModel orderModel);

        /**
         * 店铺被点击
         *
         * @param shop_id
         */
        void onShop(String shop_id);

        /**
         * 用户被点击
         *
         * @param user_id
         * @param target_name
         */
        void onUser(String user_id, String target_name);

        /**
         * 付款
         */
        void onPayment(OrderModel orderModel);

        /**
         * 联系卖家
         */
        void onConnectSeller(String target_id, String target_name);

        /**
         * 待发货
         */
        void onWaitForSend();

//        /**
//         * 待服务
//         */
//        void onWaitForService();

        /**
         * 查看物流
         */
        void onLogistics(String order_id);

        /**
         * 待签收
         * @param target_id
         * @param target_name
         */
        void onWaitForSign(String target_id, String target_name);

        /**
         * 立即评论
         */
        void onImmediateComment(OrderModel orderModel);

        /**
         * 删除订单
         */
        void onDeleteOrder(String order_id);

        /**
         * 确认收货
         */
        void onConfirmReceipt(String order_id);

        /**
         * 已签收
         */
        void onHaveBeenSigned();

        /**
         * 已退款
         */
        void onRefunded();

        /**
         * 取消订单
         */
        void onCancelOrder(String order_id);

        /**
         * 确认服务
         *
         * @param order_id
         */
        void onConfirmedService(String order_id);

        /**
         * 立即收货
         */
        void onGoodsReceived();

        /**
         * 退换货
         */
        void onReturnsAndChanged(OrderModel orderModel);

        /**
         * 完善资料
         */
        void onCompleteTheInformation(OrderModel orderModel);


//        /**
//         * 已发货
//         */
//        void onDelivered();

        /**
         * 已完成
         */
        void omComplete();


    }

}
