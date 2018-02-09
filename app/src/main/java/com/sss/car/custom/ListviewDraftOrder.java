package com.sss.car.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.model.DrapOrder;
import com.sss.car.model.DrapOrder_GoodsData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/11/4.
 */

public class ListviewDraftOrder extends LinearLayout {
    List<DrapOrder> list = new ArrayList<>();
    OnListviewDraftOrderCallBack onListviewDraftOrderCallBack;

    public void setOnListviewDraftOrderCallBack(OnListviewDraftOrderCallBack onListviewDraftOrderCallBack) {
        this.onListviewDraftOrderCallBack = onListviewDraftOrderCallBack;
    }

    public ListviewDraftOrder(Context context) {
        super(context);

    }

    public ListviewDraftOrder(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ListviewDraftOrder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public void setList(List<DrapOrder> list, Context context) {
        this.removeAllViews();
        this.list = list;
        showData(context);
    }

    void showData(Context context) {
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_draft_order, null);
            TextView total_item_shopping_cart_edit_adapter = $.f(view, R.id.total_item_shopping_cart_edit_adapter);
            TextView shopname_listview_draft_order = $.f(view, R.id.shopname_listview_draft_order);
            RecyclerView listview_listview_draft_order = $.f(view, R.id.listview_listview_draft_order);
            listview_listview_draft_order.setLayoutManager(new LinearLayoutManager(context));
            shopname_listview_draft_order.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onListviewDraftOrderCallBack != null) {
                        onListviewDraftOrderCallBack.onShopName(list.get(finalI).shop_id);
                    }

                }
            });
            for (int j = 0; j < list.get(finalI).goods_data.size(); j++) {
                final int finalJ = j;
                shopname_listview_draft_order.setText(list.get(finalI).shop_name);
                total_item_shopping_cart_edit_adapter.setText("总价:¥" + list.get(finalI).total);

                SSS_RVAdapter sss_rvAdapter = new SSS_RVAdapter<DrapOrder_GoodsData>(listview_listview_draft_order, R.layout.item_listview_draft_order, list.get(finalI).goods_data) {
                    @Override
                    protected void setView(SSS_HolderHelper helper, int position, DrapOrder_GoodsData bean) {
                        helper.setText(R.id.content_item_listview_draft_order, bean.title);
                        helper.setText(R.id.price_item_listview_draft_order, "¥" + bean.price);
                        helper.setText(R.id.number_item_listview_draft_order, "×" + bean.number);
                        helper.setText(R.id.date_item_listview_draft_order,  list.get(finalI).create_time);
                    }

                    @Override
                    protected void setItemListener(SSS_HolderHelper helper) {
                        helper.setItemChildClickListener(R.id.click_item_listview_draft_order);
                        helper.setItemChildClickListener(R.id.edit_item_listview_draft_order);
                        helper.setItemChildClickListener(R.id.delete_item_listview_draft_order);
                    }
                };
                sss_rvAdapter.setOnItemListener(new SSS_OnItemListener() {
                    @Override
                    public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                        switch (view.getId()) {
                            case R.id.click_item_listview_draft_order:
                                if (onListviewDraftOrderCallBack != null) {
                                    onListviewDraftOrderCallBack.onClickGoods(list.get(finalI).type,list.get(finalI).order_id,list.get(finalI).shop_id);
                                }
                                break;
                            case R.id.edit_item_listview_draft_order:
                                ((SwipeMenuLayout)holder.getView(R.id.scoll_item_listview_draft_order)).smoothClose();
                                if (onListviewDraftOrderCallBack != null) {
                                    onListviewDraftOrderCallBack.onEdit(list.get(finalI).type,list.get(finalI).order_id,list.get(finalI).shop_id);
                                }
                                break;
                            case R.id.delete_item_listview_draft_order:
                                ((SwipeMenuLayout)holder.getView(R.id.scoll_item_listview_draft_order)).smoothClose();
                                if (onListviewDraftOrderCallBack != null) {
                                    onListviewDraftOrderCallBack.onDelete(list.get(finalI).type,list.get(finalI).order_id,list.get(finalI).shop_id);
                                }

                                break;
                        }
                    }
                });
                listview_listview_draft_order.setAdapter(sss_rvAdapter);
            }
            this.addView(view);
        }

    }


    public interface OnListviewDraftOrderCallBack {

        void onShopName(String shop_id);

        void onClickGoods(String type,String order_id,String shop_id);

        void onEdit(String type,String order_id,String shop_id);

        void onDelete(String type,String order_id,String shop_id);

    }


}
