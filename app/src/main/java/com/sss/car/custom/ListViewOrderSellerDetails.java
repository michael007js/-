package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.EditText.PriceEditText;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;


/**
 * Created by leilei on 2017/10/15.
 */

public class ListViewOrderSellerDetails extends LinearLayout {
    SSS_Adapter sss_adapter;
    View view;
    TextView shopname_listview_order_seller_details;
    InnerListview listview_listview_order_seller_details;
    ListViewOrderSellerDetailsCallBack listViewOrderSellerDetailsCallBack;

    public void clear() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
    }

    public void setListViewOrderSellerDetailsCallBack(ListViewOrderSellerDetailsCallBack listViewOrderSellerDetailsCallBack) {
        this.listViewOrderSellerDetailsCallBack = listViewOrderSellerDetailsCallBack;
    }

    public ListViewOrderSellerDetails(Context context) {
        super(context);
    }

    public ListViewOrderSellerDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewOrderSellerDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(final Context context, final OrderSellerModel orderSellerModel) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_order_seller_details, null);
        shopname_listview_order_seller_details = $.f(view, R.id.shopname_listview_order_seller_details);
        listview_listview_order_seller_details = $.f(view, R.id.listview_listview_order_seller_details);
        shopname_listview_order_seller_details.setText(orderSellerModel.shop_name);
        shopname_listview_order_seller_details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewOrderSellerDetailsCallBack != null) {
                    listViewOrderSellerDetailsCallBack.onClickShopName(orderSellerModel.shop_id);
                }
            }
        });


        sss_adapter = new SSS_Adapter<OrderSellerModel_Order_Goods>(context, R.layout.item_listview_order_seller_details_adapter, orderSellerModel.goods_data) {
            @Override
            protected void setView(final SSS_HolderHelper helper, int position, OrderSellerModel_Order_Goods bean, SSS_Adapter instance) {

                helper.setText(R.id.content_item_listview_order_seller_details_adapter, bean.title);
                helper.setText(R.id.price_item_listview_order_seller_details_adapter, "¥" + bean.price);

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
         }
        };
        listview_listview_order_seller_details.setAdapter(sss_adapter);
        this.addView(view);
    }


    public interface ListViewOrderSellerDetailsCallBack {

        void onClickShopName(String shop_id);

    }

}
