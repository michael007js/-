package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.model.OrderSynthesize_Data_ListModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by leilei on 2017/10/24.
 */

public class ListviewOrderSynthesizeMiddle extends LinearLayout{
    List<OrderSynthesize_Data_ListModel> list=new ArrayList<>();
    public ListviewOrderSynthesizeMiddle(Context context) {
        super(context);
    }

    public ListviewOrderSynthesizeMiddle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewOrderSynthesizeMiddle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void setList(List<OrderSynthesize_Data_ListModel> list ,Context context) {
        this.list = list;
        this.removeAllViews();
        this.removeAllViews();
        showData(context);
    }

    private void showData(Context context) {
        for (int i = 0; i < list.size(); i++) {
            View view= LayoutInflater.from(context).inflate(R.layout.listview_order_synthesize_middle,null);
            InnerListview listview_listview_order_synthesize_middle= $.f(view,R.id.listview_listview_order_synthesize_middle);
            SSS_Adapter sss_adapter = new SSS_Adapter<OrderSynthesize_Data_ListModel>(context, R.layout.item_listview_order_synthesize_one_adapter, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, OrderSynthesize_Data_ListModel bean,SSS_Adapter instance) {
                    helper.setText(R.id.content_item_listview_order_synthesize_one_adapter, bean.name);
                    helper.setText(R.id.price_item_listview_order_synthesize_one_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_listview_order_synthesize_one_adapter, "×" + bean.num);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview_listview_order_synthesize_middle.setAdapter(sss_adapter);
            this.addView(view);
        }
    }
}
