package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
import com.sss.car.model.OrderModel_goods_data;
import com.sss.car.order_new.OrderModel;
import com.sss.car.order_new.OrderModel_GoodsData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/10/7.
 */

public class ListViewOrderRetuenOrChange extends LinearLayout {


    List<OrderModel> list = new ArrayList<>();
    ListViewOrderRetuenOrChangeCallBack callBack;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        callBack = null;
    }

    public ListViewOrderRetuenOrChange(Context context) {
        super(context);
    }

    public ListViewOrderRetuenOrChange(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrderRetuenOrChange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListViewOrderCallBack(ListViewOrderRetuenOrChangeCallBack callBack) {
        this.callBack = callBack;
    }

    public void setList(Context context, List<OrderModel> list) {
        this.removeAllViews();
        this.list = list;
        showData(context);
    }

    public List<OrderModel> getList() {
        return list;
    }

    void showData(Context context) {
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_order, null);
            final CheckBox cb_listview_order = $.f(view, R.id.cb_listview_order);
            LinearLayout click_listview_order = $.f(view, R.id.click_listview_order);
            final InnerListview list_listview_order = $.f(view, R.id.list_listview_order);
            TextView name_listview_order = $.f(view, R.id.name_listview_order);
            name_listview_order.setText(list.get(finalI).name);
            FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(i).picture), ((SimpleDraweeView) $.f(view,R.id.pic_listview_order)), 9999f);
            cb_listview_order.setVisibility(GONE);
            click_listview_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) {
                        if (callBack != null) {
                            callBack.onName(list.get(finalI).picture, list.get(finalI).name, list.get(finalI).id);
                        }
                    }
                }
            });
            SSS_Adapter sss_adapter = new SSS_Adapter<OrderModel_GoodsData>(context, R.layout.item_listview_order_adapter) {
                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, OrderModel_GoodsData bean,SSS_Adapter instance) {
                    helper.setVisibility(R.id.cb_item_listview_order_adapter, GONE);
                    helper.setVisibility(R.id.qr_item_listview_order_adapter, GONE);
                    helper.setText(R.id.content_item_listview_order_adapter, bean.title);
                    helper.setText(R.id.price_item_listview_order_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_listview_order_adapter, "×" + bean.number);
                    FrescoUtils.showImage(false, 50, 50, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_listview_order_adapter)), 0f);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                }
            };
            list_listview_order.setAdapter(sss_adapter);
            sss_adapter.setList(list.get(finalI).goods_data);
            this.addView(view);
        }
    }

    public interface ListViewOrderRetuenOrChangeCallBack {

        void onName(String targetPic, String targetName, String targetId);


    }

}
