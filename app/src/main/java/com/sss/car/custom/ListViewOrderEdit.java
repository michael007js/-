package com.sss.car.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.OrderEdit;
import com.sss.car.model.OrderEdit_GoodsData;
import com.sss.car.model.OrderEdit_GoodsData_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/5.
 */

public class ListViewOrderEdit extends LinearLayout {

    List<OrderEdit> list = new ArrayList<>();
    OnListViewOrderEditCallBack listener;
    OrderEdit orderEdit;
    String where;

    public void setWhere(String where) {
        this.where = where;
    }

    public void setListener(OnListViewOrderEditCallBack listener) {
        this.listener = listener;
    }

    public ListViewOrderEdit(Context context) {
        super(context);
    }

    public ListViewOrderEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrderEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OrderEdit getOrderEdit() {
        return orderEdit;
    }

    public void setList(final Context context, final OrderEdit model, final String type) {
        orderEdit = model;

        for (int i = 0; i < orderEdit.goods_data.size(); i++) {
            final int finalI = i;

            final View view = LayoutInflater.from(context).inflate(R.layout.listview_order_seller_details, null);
            TextView shopname_listview_order_seller_details = $.f(view, R.id.shopname_listview_order_seller_details);
            InnerListview listview_listview_order_seller_details = $.f(view, R.id.listview_listview_order_seller_details);
            shopname_listview_order_seller_details.setText(orderEdit.goods_data.get(finalI).name);


            final SSS_Adapter adapter = new SSS_Adapter<OrderEdit_GoodsData_Data>(context, R.layout.item_listview_order_edit_adapter, orderEdit.goods_data.get(finalI).data) {
                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, final OrderEdit_GoodsData_Data bean, SSS_Adapter instance) {
                    helper.setText(R.id.content_item_listview_order_edit_adapter, bean.name);
                    helper.setText(R.id.price_item_listview_order_edit_adapter, bean.price);
                    if ("1".equals(type)) {
                        if ("fromDraft".equals(where)){
//                            helper.setText(R.id.number_listview_order_edit_adapter_,"×"+bean.num);
//                            helper.setVisibility(R.id.number_listview_order_edit_adapter_, VISIBLE);
//                            helper.setVisibility(R.id.price_item_listview_order_edit_adapter, VISIBLE);
//                            helper.setVisibility(R.id.add_item_listview_order_edit_adapter_, GONE);
//                            helper.setVisibility(R.id.edit_item_listview_order_edit_adapter, GONE);

                            helper.setVisibility(R.id.number_listview_order_edit_adapter_, GONE);
                            helper.setVisibility(R.id.subtract_item_listview_order_edit_adapter_, GONE);
                            helper.setVisibility(R.id.price_item_listview_order_edit_adapter, VISIBLE);
                            helper.setVisibility(R.id.add_item_listview_order_edit_adapter_, GONE);
                            helper.setVisibility(R.id.edit_item_listview_order_edit_adapter, GONE);
                        }else {
                            helper.setVisibility(R.id.number_listview_order_edit_adapter_, GONE);
                            helper.setVisibility(R.id.subtract_item_listview_order_edit_adapter_, VISIBLE);
                            helper.setVisibility(R.id.price_item_listview_order_edit_adapter, VISIBLE);
                            helper.setVisibility(R.id.add_item_listview_order_edit_adapter_, VISIBLE);
                            helper.setVisibility(R.id.edit_item_listview_order_edit_adapter, VISIBLE);
                        }


                        int a = 1;
                        if (!StringUtils.isEmpty(orderEdit.number)) {
                            a = Integer.parseInt(orderEdit.number);
                        }
                        helper.setText(R.id.edit_item_listview_order_edit_adapter, bean.num);


                        ((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (!StringUtils.isEmpty(s.toString()) && !"0".equals(s.toString())) {
                                    if (listener != null) {
                                        listener.onPriceChanged(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()) * (int) Double.parseDouble(bean.price));
                                        listener.onTotalCount(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()));
                                    }
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    if ("1".equals(type)) {
                        helper.setItemChildClickListener(R.id.subtract_item_listview_order_edit_adapter_);
                        helper.setItemChildClickListener(R.id.add_item_listview_order_edit_adapter_);
                    }
                }
            };


            adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper helper) {
                    switch (view.getId()) {
                        case R.id.subtract_item_listview_order_edit_adapter_:
                            if (!StringUtils.isEmpty(((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim())) {
                                String a = ((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString();
                                if (!"1".equals(a)) {
                                    helper.setText(R.id.edit_item_listview_order_edit_adapter, Integer.valueOf(a) - 1 + "");
                                    LogUtils.e(((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim());
                                    model.goods_data.get(finalI).data.get(position).num = ((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim();
                                    if (listener != null) {
                                        listener.onPriceChanged(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()) * (int) Double.parseDouble(orderEdit.goods_data.get(finalI).data.get(position).price));
                                        listener.onTotalCount(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()));
                                    }
                                }
                            }
                            break;
                        case R.id.add_item_listview_order_edit_adapter_:
                            if (!StringUtils.isEmpty(((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim())) {
                                String a = ((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString();
                                helper.setText(R.id.edit_item_listview_order_edit_adapter, Integer.valueOf(a) + 1 + "");
                                LogUtils.e(((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim());
                                model.goods_data.get(finalI).data.get(position).num = ((TextView) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString().trim();
                                if (listener != null) {
                                    listener.onPriceChanged(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()) * (int) Double.parseDouble(orderEdit.goods_data.get(finalI).data.get(position).price));
                                    listener.onTotalCount(Integer.valueOf(((EditText) helper.getView(R.id.edit_item_listview_order_edit_adapter)).getText().toString()));
                                }
                            }
                            break;
                    }
                }
            });
            listview_listview_order_seller_details.setAdapter(adapter);
            shopname_listview_order_seller_details.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onShopName(orderEdit.shop_id);
                    }
                }
            });
            this.addView(view);


        }
    }


    public interface OnListViewOrderEditCallBack {

        void onPriceChanged(int price);

        void onShopName(String shop_id);

        void onTotalCount(int number);
    }
}
