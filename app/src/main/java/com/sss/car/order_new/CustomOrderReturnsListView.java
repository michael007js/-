package com.sss.car.order_new;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;

/**
 * Created by leilei on 2017/11/23.
 */

public class CustomOrderReturnsListView extends LinearLayout {
    OnCustomOrderReturnsListViewCallBack onCustomOrderReturnsListViewCallBack;
    OrderModel orderModel;
    View headView;

    public CustomOrderReturnsListView(Context context) {
        super(context);
    }

    public void setOnCustomOrderReturnsListViewCallBack(OnCustomOrderReturnsListViewCallBack onCustomOrderReturnsListViewCallBack) {
        this.onCustomOrderReturnsListViewCallBack = onCustomOrderReturnsListViewCallBack;
    }

    public CustomOrderReturnsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomOrderReturnsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    /**
     * @param context
     * @param what    1支出2收入
     */
    public void setList(Context context, OrderModel orderModel, int what) {
        this.removeAllViews();
        if (headView != null) {
            addView(headView);
        }
        this.orderModel = orderModel;

        showData(context, what);
    }

    /**
     * @param context
     * @param what    1支出2收入
     */
    void showData(Context context, int what) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_order, null);
        LinearLayout click_custom_listview_order = $.f(view, R.id.click_custom_listview_order);
        SimpleDraweeView pic_custom_listview_order = $.f(view, R.id.pic_custom_listview_order);
        final InnerListview list_custom_listview_order = $.f(view, R.id.list_custom_listview_order);
        TextView order_code_custom_listview_order = $.f(view, R.id.order_code_custom_listview_order);
        TextView order_date_custom_listview_order = $.f(view, R.id.order_date_custom_listview_order);
        TextView target_name_custom_listview_order = $.f(view, R.id.name_custom_listview_order);
        order_code_custom_listview_order.setText(orderModel.order_code);
        order_date_custom_listview_order.setText(orderModel.create_time);
        if (what == 1) {
            target_name_custom_listview_order.setText(orderModel.target_name);
            FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + orderModel.picture), pic_custom_listview_order, 30f);
            click_custom_listview_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCustomOrderReturnsListViewCallBack != null) {
                        onCustomOrderReturnsListViewCallBack.onUser(orderModel.target_id, orderModel.target_name);
                    }
                }
            });
        } else if (what == 2) {
            target_name_custom_listview_order.setText(orderModel.name);
            FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + orderModel.picture), pic_custom_listview_order, 30f);
            click_custom_listview_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCustomOrderReturnsListViewCallBack != null) {
                        onCustomOrderReturnsListViewCallBack.onShop(orderModel.shop_id);
                    }

                }
            });
        }

        SSS_Adapter sss_adapter = new SSS_Adapter<OrderModel_GoodsData>(context, R.layout.item_custom_listview_order_adapter) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, OrderModel_GoodsData bean, SSS_Adapter instance) {
                helper.setText(R.id.content_item_custom_listview_order_adapter, bean.title);
                helper.setText(R.id.price_item_custom_listview_order_adapter, "¥" + bean.price);
                helper.setText(R.id.number_item_custom_listview_order_adapter, "×" + bean.number);
                FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_custom_listview_order_adapter)), 0f);
                helper.setVisibility(R.id.qr_item_custom_listview_order_adapter, GONE);

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_custom_listview_order_adapter);
                helper.setItemChildClickListener(R.id.qr_item_custom_listview_order_adapter);
            }

        };

        list_custom_listview_order.setAdapter(sss_adapter);

        sss_adapter.setList(orderModel.goods_data);

        this.addView(view);
    }


    public interface OnCustomOrderReturnsListViewCallBack {
        void onShop(String shop_id);

        void onUser(String user_id, String nikeName);
    }

}
