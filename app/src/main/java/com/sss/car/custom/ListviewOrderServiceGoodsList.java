package com.sss.car.custom;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.MyRecycleview.FullyLinearLayoutManager;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RViewHolder;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.ShoppingCart;
import com.sss.car.model.ShoppingCart_Data;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.maxLength;


/**
 * Created by leilei on 2017/9/29.
 */

public class ListviewOrderServiceGoodsList extends LinearLayout {
    List<ShoppingCart> shoppingCartOrderlist = new ArrayList<>();
    ListviewOrderServiceGoodsListOperationCallBacn listviewOrderServiceGoodsListOperationCallBacn;
    AddAndSubtractCallBack addAndSubtractCallBack;
    boolean isShowNumberSelect;

    public ListviewOrderServiceGoodsList(Context context) {
        super(context);
    }

    public ListviewOrderServiceGoodsList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewOrderServiceGoodsList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAddAndSubtractCallBack(AddAndSubtractCallBack addAndSubtractCallBack) {
        this.addAndSubtractCallBack = addAndSubtractCallBack;
    }

    public void setListviewOrderServiceGoodsListOperationCallBacn(ListviewOrderServiceGoodsListOperationCallBacn listviewOrderServiceGoodsListOperationCallBacn) {
        this.listviewOrderServiceGoodsListOperationCallBacn = listviewOrderServiceGoodsListOperationCallBacn;
    }

    public void setList(Activity activity, List<ShoppingCart> shoppingCartOrderlist, boolean isShowNumberSelect) {
        this.isShowNumberSelect = isShowNumberSelect;
        this.shoppingCartOrderlist = shoppingCartOrderlist;
        this.removeAllViews();
        showData(activity);
    }

    public List<ShoppingCart> getShoppingCartOrderlist() {
        return shoppingCartOrderlist;
    }

    void showData(final Activity activity) {
        for (int i = 0; i < shoppingCartOrderlist.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(activity).inflate(R.layout.listview_order_service_goods_list, null);
            TextView shopname_listview_order_service_goods_list = $.f(view, R.id.shopname_listview_order_service_goods_list);
            RecyclerView listview_listview_order_service_goods_list = $.f(view, R.id.listview_listview_order_service_goods_list);
            listview_listview_order_service_goods_list.setLayoutManager(new FullyLinearLayoutManager(activity, RecyclerView.VERTICAL, false));
            shopname_listview_order_service_goods_list.setText(shoppingCartOrderlist.get(finalI).name);
            shopname_listview_order_service_goods_list.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listviewOrderServiceGoodsListOperationCallBacn != null) {
                        listviewOrderServiceGoodsListOperationCallBacn.onClickFromShopName(shoppingCartOrderlist.get(finalI).shop_id);
                    }
                }
            });

            SSS_RVAdapter sss_rvAdapter = new SSS_RVAdapter<ShoppingCart_Data>(listview_listview_order_service_goods_list, R.layout.item_listview_order_service_goods_list_adapter) {

                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, ShoppingCart_Data bean) {
                    helper.getView(R.id.parent_item_listview_order_service_goods_list_adapter).setLayoutParams(new LayoutParams(activity.getWindowManager().getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
                    LogUtils.e(bean.num);
                    helper.setText(R.id.edit_item_listview_order_service_goods_list_adapter, bean.num);
                    helper.setText(R.id.content_item_listview_order_service_goods_list_adapter, bean.name);
                    helper.setText(R.id.price_item_listview_order_service_goods_list_adapter, "¥" + bean.price);
                    if (isShowNumberSelect) {
                        helper.setVisibility(R.id.subtract_item_listview_order_service_goods_list_adapter, View.VISIBLE);
                        helper.setVisibility(R.id.edit_item_listview_order_service_goods_list_adapter, View.VISIBLE);
                        helper.setVisibility(R.id.add_item_listview_order_service_goods_list_adapter, View.VISIBLE);

                        final String[] a = {""};
                        ((EditText) helper.getView(R.id.edit_item_listview_order_service_goods_list_adapter)).addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                a[0] = ((EditText) helper.getView(R.id.edit_item_listview_order_service_goods_list_adapter)).getText().toString().trim();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                if (!StringUtils.isEmpty( ((EditText) helper.getView(R.id.edit_item_listview_order_service_goods_list_adapter)).getText().toString().trim())) {
                                    if ( ((EditText) helper.getView(R.id.edit_item_listview_order_service_goods_list_adapter)).getText().toString().length() < maxLength - 1) {
                                        shoppingCartOrderlist.get(finalI).data.get(position).num = s.toString();
                                    }

                                } else {
                                    ((EditText) helper.getView(R.id.edit_item_listview_order_service_goods_list_adapter)).setText(a[0]);
                                }
                                totalCount();
                                totalPrice();
                            }
                        });
                    } else {
                        helper.setVisibility(R.id.subtract_item_listview_order_service_goods_list_adapter, View.GONE);
                        helper.setVisibility(R.id.edit_item_listview_order_service_goods_list_adapter, View.GONE);
                        helper.setVisibility(R.id.add_item_listview_order_service_goods_list_adapter, View.GONE);
                        helper.setItemChildClickListener(R.id.subtract_item_listview_order_service_goods_list_adapter);
                        helper.setItemChildClickListener(R.id.add_item_listview_order_service_goods_list_adapter);
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.subtract_item_listview_order_service_goods_list_adapter);
                    helper.setItemChildClickListener(R.id.add_item_listview_order_service_goods_list_adapter);
                }

            };


            sss_rvAdapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.subtract_item_listview_order_service_goods_list_adapter:
                            int a = Integer.parseInt(shoppingCartOrderlist.get(finalI).data.get(position).num);
                            if (a > 1) {
                                shoppingCartOrderlist.get(finalI).data.get(position).num = String.valueOf(a - 1);
                                holder.setText(R.id.edit_item_listview_order_service_goods_list_adapter, shoppingCartOrderlist.get(finalI).data.get(position).num);
                            }
                            if (addAndSubtractCallBack != null) {
                                addAndSubtractCallBack.onAddAndSubtract(shoppingCartOrderlist.get(finalI).data.get(position).num, shoppingCartOrderlist.get(finalI).data.get(position).sid, shoppingCartOrderlist);
                            }
                            break;
                        case R.id.add_item_listview_order_service_goods_list_adapter:
                            int b = Integer.parseInt(shoppingCartOrderlist.get(finalI).data.get(position).num);
                            shoppingCartOrderlist.get(finalI).data.get(position).num = String.valueOf(b + 1);
                            holder.setText(R.id.edit_item_listview_order_service_goods_list_adapter, shoppingCartOrderlist.get(finalI).data.get(position).num);
                            if (addAndSubtractCallBack != null) {
                                addAndSubtractCallBack.onAddAndSubtract(shoppingCartOrderlist.get(finalI).data.get(position).num, shoppingCartOrderlist.get(finalI).data.get(position).sid, shoppingCartOrderlist);
                            }
                            break;
                    }
                }
            });

            listview_listview_order_service_goods_list.setAdapter(sss_rvAdapter);


            sss_rvAdapter.setList(shoppingCartOrderlist.get(finalI).data);

            this.addView(view);

        }
    }


    /**
     * 计算总价
     */
    int a = 0;

    public void totalPrice() {
        a = 0;
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
        if (shoppingCartOrderlist.size() != 0) {
            for (int i = 0; i < shoppingCartOrderlist.size(); i++) {
                for (int j = 0; j < shoppingCartOrderlist.get(i).data.size(); j++) {
                    if (shoppingCartOrderlist.get(i).data.size() > 0) {
                        a = a + Integer.valueOf(shoppingCartOrderlist.get(i).data.get(j).num) *(int)(Double.parseDouble( shoppingCartOrderlist.get(i).data.get(j).price));
//                                LogUtils.e(shoppingCartOrderlist.get(i).data.get(j).num+"--"+shoppingCartOrderlist.get(i).data.get(j).price);
                    }
                }
            }
            if (listviewOrderServiceGoodsListOperationCallBacn != null) {
                listviewOrderServiceGoodsListOperationCallBacn.onTotalPrice(a);
            }
        }
//
//            }
//        }.start();

    }


    /**
     * 计算总数
     */
    int b = 0;

    public void totalCount() {
        b = 0;
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
        if (shoppingCartOrderlist.size() != 0) {
            for (int i = 0; i < shoppingCartOrderlist.size(); i++) {
                for (int j = 0; j < shoppingCartOrderlist.get(i).data.size(); j++) {
                    if (shoppingCartOrderlist.get(i).data.size() > 0) {
                        if (!StringUtils.isEmpty(shoppingCartOrderlist.get(i).data.get(j).num)) {
                            b = b + Integer.valueOf(shoppingCartOrderlist.get(i).data.get(j).num);
                        }
                    }
                }
            }
        }
        if (listviewOrderServiceGoodsListOperationCallBacn != null) {
            listviewOrderServiceGoodsListOperationCallBacn.onTotalCount(b);
        }

//            }
//        }.start();


    }


    public interface ListviewOrderServiceGoodsListOperationCallBacn {
        void onClickFromShopName(String shop_id);

        void onTotalPrice(int totalPrice);

        void onTotalCount(int totalCount);
    }


    public interface AddAndSubtractCallBack {
        void onAddAndSubtract(String number, String sid, List<ShoppingCart> list);
    }


}
