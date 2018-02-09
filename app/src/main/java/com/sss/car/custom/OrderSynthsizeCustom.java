package com.sss.car.custom;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.OrderSynthesize_DataModel;
import com.sss.car.model.OrderSynthesize_Data_ListModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/28.
 */

public class OrderSynthsizeCustom extends LinearLayout {
    List<OrderSynthesize_DataModel> data = new ArrayList<>();
    OrderSynthsizeCustomCallBack orderSynthsizeCustomCallBack;
    String shop_id;
    OrderIDCallBack orderIDCallBack;

    public OrderSynthsizeCustom(Context context) {
        super(context);
    }

    public OrderSynthsizeCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderSynthsizeCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOrderSynthsizeCustomCallBack(String shop_id, OrderSynthsizeCustomCallBack orderSynthsizeCustomCallBack) {
        this.shop_id = shop_id;
        this.orderSynthsizeCustomCallBack = orderSynthsizeCustomCallBack;
    }

    public void setList(List<OrderSynthesize_DataModel> data, Context context) {
        this.data = data;
        this.removeAllViews();
        showData(context);
    }

    public void setOrderIDCallBack(OrderIDCallBack orderIDCallBack) {
        this.orderIDCallBack = orderIDCallBack;
    }

    void showData(Context context) {
        /***************************************综合订单列表==>订单类型列表(实物订单,服务订单)***************************************************************/
        for (int i = 0; i < data.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_order_synthesize_two, null);
            InnerListview listview_listview_order_synthesize_one = $.f(view, R.id.listview_listview_order_synthesize_two);
            TextView title_listview_order_synthesize_one = $.f(view, R.id.title_listview_order_synthesize_two);
            TextView write_listview_order_synthesize_one = $.f(view, R.id.write_listview_order_synthesize_two);
            write_listview_order_synthesize_one.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            write_listview_order_synthesize_one.getPaint().setAntiAlias(true);
            write_listview_order_synthesize_one.setTextColor(getResources().getColor(R.color.mainColor));
            if (!"0".equals(data.get(finalI).order_id)) {
                if (orderIDCallBack != null) {
                    orderIDCallBack.onCallBack();
                }
                write_listview_order_synthesize_one.setText("已填写");
            } else {
                write_listview_order_synthesize_one.setText("订单填写");
            }
            write_listview_order_synthesize_one.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderSynthsizeCustomCallBack != null) {
                        orderSynthsizeCustomCallBack.onClickFromWrite(finalI, shop_id, data.get(finalI), data);
                    }
                }
            });

            title_listview_order_synthesize_one.setText(data.get(finalI).title);
            SSS_Adapter sss_adapter = new SSS_Adapter<OrderSynthesize_Data_ListModel>(context, R.layout.item_listview_order_synthesize_one_adapter, data.get(finalI).list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, OrderSynthesize_Data_ListModel bean, SSS_Adapter instance) {
                    helper.setText(R.id.content_item_listview_order_synthesize_one_adapter, bean.name);
                    helper.setText(R.id.price_item_listview_order_synthesize_one_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_listview_order_synthesize_one_adapter, "×" + bean.num);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview_listview_order_synthesize_one.setAdapter(sss_adapter);
            this.addView(view, finalI);
        }
    }


    public interface OrderSynthsizeCustomCallBack {
        void onClickFromWrite(int position, String shop_id, OrderSynthesize_DataModel model, List<OrderSynthesize_DataModel> data);
    }

    public interface OrderIDCallBack {
        void onCallBack();
    }

}
