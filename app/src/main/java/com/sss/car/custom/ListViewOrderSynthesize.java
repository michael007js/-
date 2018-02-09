package com.sss.car.custom;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.model.OrderSynthesizeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/28.
 */

public class ListViewOrderSynthesize extends LinearLayout {
    List<OrderSynthesizeModel> list = new ArrayList<>();
    ListViewOrderSynthesizeCallBack listViewOrderSynthesizeCallBack;
    OrderSynthsizeCustom.OrderSynthsizeCustomCallBack orderSynthsizeCustomCallBack;

    public ListViewOrderSynthesize(Context context) {
        super(context);
    }

    public ListViewOrderSynthesize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrderSynthesize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setList(List<OrderSynthesizeModel> list, Context context) {
        this.list = list;
        this.removeAllViews();
        showData(context);
    }

    public void setListViewOrderSynthesizeCallBack(ListViewOrderSynthesizeCallBack listViewOrderSynthesizeCallBack, OrderSynthsizeCustom.OrderSynthsizeCustomCallBack orderSynthsizeCustomCallBack) {
        this.listViewOrderSynthesizeCallBack = listViewOrderSynthesizeCallBack;
        this.orderSynthsizeCustomCallBack = orderSynthsizeCustomCallBack;
    }
    int a = 0;
    void showData(Context context) {
        a=0;
        /***************************************综合订单列表***************************************************************/
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_order_synthesize_one, null);
            TextView shopname_listview_order_synthesize_one = $.f(view, R.id.shopname_listview_order_synthesize_one);
            shopname_listview_order_synthesize_one.setText(list.get(i).name);
            shopname_listview_order_synthesize_one.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listViewOrderSynthesizeCallBack != null) {
                        listViewOrderSynthesizeCallBack.onClickFromShoppingName(finalI, list);
                    }
                }
            });
            OrderSynthsizeCustom orderSynthsizeCustom_listview_order_synthesize_one = $.f(view, R.id.orderSynthsizeCustom_listview_order_synthesize_one);
            orderSynthsizeCustom_listview_order_synthesize_one.setOrderIDCallBack(new OrderSynthsizeCustom.OrderIDCallBack() {
                @Override
                public void onCallBack() {
                    a++;

                }
            });
            orderSynthsizeCustom_listview_order_synthesize_one.setOrderSynthsizeCustomCallBack(list.get(i).shop_id, orderSynthsizeCustomCallBack);
            orderSynthsizeCustom_listview_order_synthesize_one.setList(list.get(i).data, context);
            shopname_listview_order_synthesize_one.setText(list.get(i).name);

            this.addView(view);
        }
        totalPrice();
        LogUtils.e(a+"---"+list.size());
        if (listViewOrderSynthesizeCallBack != null) {
            listViewOrderSynthesizeCallBack.onCompleteCallBack(a==list.size());
        }
    }


    void totalPrice() {
        if (list.size() > 0) {
            listViewOrderSynthesizeCallBack.onTotalPrice(Double.valueOf(list.get(0).total));
        } else {
            listViewOrderSynthesizeCallBack.onTotalPrice(0.00);
        }

    }


    public interface ListViewOrderSynthesizeCallBack {
        void onClickFromShoppingName(int i, List<OrderSynthesizeModel> list);

        void onTotalPrice(double totalPrice);

        void onCompleteCallBack(boolean b);
    }

}
