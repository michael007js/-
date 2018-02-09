package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.model.GoodsModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/21.
 */

public class ListViewSale extends LinearLayout{
    String classify_id;
    String title;
    View view;
    InnerListview listview_listview_sale_and_seckill_sale;
    LinearLayout click_listview_sale_and_seckill_sale;
    TextView title_listview_sale_and_seckill_sale;
    TextView more_listview_sale_and_seckill_sale;
    ListViewSale.ListViewSaleOperationCallBack ListViewSaleOperationCallBack;
    SSS_Adapter sss_adapter;


    public ListViewSale(Context context) {
        super(context);
        init(context);
    }

    public ListViewSale(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewSale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        
        view = LayoutInflater.from(context).inflate(R.layout.listview_sale_and_seckill_sale, null);
        listview_listview_sale_and_seckill_sale = $.f(view, R.id.listview_listview_sale_and_seckill_sale);
        title_listview_sale_and_seckill_sale = $.f(view, R.id.title_listview_sale_and_seckill_sale);
        more_listview_sale_and_seckill_sale = $.f(view, R.id.more_listview_sale_and_seckill_sale);
        click_listview_sale_and_seckill_sale = $.f(view, R.id.click_listview_sale_and_seckill_sale);
        click_listview_sale_and_seckill_sale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListViewSaleOperationCallBack != null) {
                    ListViewSaleOperationCallBack.onClickTitle_ListViewSaleOperationCallBack(classify_id, title);
                }
            }
        });
        more_listview_sale_and_seckill_sale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListViewSaleOperationCallBack != null) {
                    ListViewSaleOperationCallBack.onClickMore_ListViewSaleOperationCallBack(classify_id, title, ListViewSale.this);
                }
            }
        });
        this.addView(view);
    }


    public void setListViewSaleOperationCallBack(ListViewSale.ListViewSaleOperationCallBack ListViewSaleOperationCallBack) {
        this.ListViewSaleOperationCallBack = ListViewSaleOperationCallBack;
    }



    public void create(String title, String classify_id  , SSS_Adapter sss_adapter, SSS_OnItemListener sss_onItemListener) {
        this.title = title;
        this.classify_id = classify_id;
        this.sss_adapter = sss_adapter;
        sss_adapter.setOnItemListener(sss_onItemListener);
        title_listview_sale_and_seckill_sale.setText(this.title);
        listview_listview_sale_and_seckill_sale.setAdapter(sss_adapter);
    }

    public void setData(List<GoodsModel> list ) {
        if (sss_adapter != null) {
            sss_adapter.setList(list);
        }
    }


    public interface ListViewSaleOperationCallBack {

        void onClickMore_ListViewSaleOperationCallBack(String classify_id, String title, ListViewSale ListViewSale);

        void onClickTitle_ListViewSaleOperationCallBack(String classify_id, String title);
    }
}
