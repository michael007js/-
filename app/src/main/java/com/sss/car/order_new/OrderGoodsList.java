package com.sss.car.order_new;

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
 * Created by leilei on 2017/10/7.
 */

public class OrderGoodsList extends LinearLayout {

    List<OrderModel> list = new ArrayList<>();
    ListViewOrderCallBack listViewOrderCallBack;
    boolean isShowQR;

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

    public OrderGoodsList(Context context) {
        super(context);
    }

    public OrderGoodsList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderGoodsList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListViewOrderCallBack(ListViewOrderCallBack listViewOrderCallBack) {
        this.listViewOrderCallBack = listViewOrderCallBack;
    }


    public void setList(Context context, List<OrderModel> list, boolean isShowQR) {
        this.removeAllViews();

        this.list = list;
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
            order_code_listview_order.setText(list.get(finalI).order_code);
            order_date_listview_order.setText(list.get(finalI).create_time);
            name_listview_order.setText(list.get(finalI).target_name);
            cb_listview_order.setVisibility(GONE);
            FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + list.get(finalI).picture), pic_listview_order, 9999f);
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

            SSS_Adapter sss_adapter = new SSS_Adapter<OrderModel_GoodsData>(context, R.layout.item_listview_order_adapter) {
                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, OrderModel_GoodsData bean, SSS_Adapter instance) {
                    helper.setVisibility(R.id.cb_item_listview_order_adapter, GONE);
                    helper.setText(R.id.content_item_listview_order_adapter, bean.slogan);
                    helper.setText(R.id.price_item_listview_order_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_listview_order_adapter, "×" + bean.number);
                    FrescoUtils.showImage(false, 160, 160, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_listview_order_adapter)), 0f);

                    if (isShowQR) {
                        helper.setVisibility(R.id.qr_item_listview_order_adapter, VISIBLE);
                        helper.setItemChildClickListener(R.id.qr_item_listview_order_adapter);
                    } else {
                        helper.setVisibility(R.id.qr_item_listview_order_adapter, GONE);
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_listview_order_adapter);

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
                    }
                }
            });
            list_listview_order.setAdapter(sss_adapter);
            sss_adapter.setList(list.get(finalI).goods_data);
            this.addView(view);
        }
    }


    public interface ListViewOrderCallBack {


        void onName(String targetPic, String targetName, String targetId);

        void onQR(String QR);


    }

}
