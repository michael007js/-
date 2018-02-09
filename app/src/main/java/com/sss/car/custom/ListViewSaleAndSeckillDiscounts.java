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
import com.sss.car.model.SaleAndSeckillDiscountsCouponModel;
import com.sss.car.model.SaleAndSeckillDiscountsModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


/**
 * Created by leilei on 2017/9/13.
 */

public class ListViewSaleAndSeckillDiscounts extends LinearLayout {
    String classify_id;
    String title;
    View view;
    InnerListview listview_listview_sale_and_seckill_discounts;
    LinearLayout click_listview_sale_and_seckill_discounts;
    TextView title_listview_sale_and_seckill_discounts;
    TextView more_listview_sale_and_seckill_discounts;
    ListViewSaleAndSeckillDiscountsOperationCallBack listViewSaleAndSeckillDiscountsOperationCallBack;
    SSS_Adapter sss_adapter;


    public ListViewSaleAndSeckillDiscounts(Context context) {
        super(context);
        init(context);
    }

    public ListViewSaleAndSeckillDiscounts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewSaleAndSeckillDiscounts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_sale_and_seckill_discounts, null);
        listview_listview_sale_and_seckill_discounts = $.f(view, R.id.listview_listview_sale_and_seckill_discounts);
        click_listview_sale_and_seckill_discounts = $.f(view, R.id.click_listview_sale_and_seckill_discounts);
        title_listview_sale_and_seckill_discounts = $.f(view, R.id.title_listview_sale_and_seckill_discounts);
        more_listview_sale_and_seckill_discounts = $.f(view, R.id.more_listview_sale_and_seckill_discounts);
        click_listview_sale_and_seckill_discounts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewSaleAndSeckillDiscountsOperationCallBack != null) {
                    listViewSaleAndSeckillDiscountsOperationCallBack.onClickTitle_ListViewSaleAndSeckillDiscountsOperationCallBack(classify_id, title);
                }
            }
        });
        more_listview_sale_and_seckill_discounts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewSaleAndSeckillDiscountsOperationCallBack != null) {
                    listViewSaleAndSeckillDiscountsOperationCallBack.onClickMore_ListViewSaleAndSeckillDiscountsOperationCallBack(classify_id, title, ListViewSaleAndSeckillDiscounts.this);
                }
            }
        });
        this.addView(view);
    }


    public void setListViewSaleAndSeckillDiscountsOperationCallBack(ListViewSaleAndSeckillDiscountsOperationCallBack ListViewSaleAndSeckillDiscountsOperationCallBack) {
        this.listViewSaleAndSeckillDiscountsOperationCallBack = ListViewSaleAndSeckillDiscountsOperationCallBack;
    }



    public void create(String title, String classify_id  , SSS_Adapter sss_adapter, SSS_OnItemListener sss_onItemListener) {
        this.title = title;
        this.classify_id = classify_id;
        this.sss_adapter = sss_adapter;
        sss_adapter.setOnItemListener(sss_onItemListener);
        title_listview_sale_and_seckill_discounts.setText(this.title);
        listview_listview_sale_and_seckill_discounts.setSmoothScrollbarEnabled(true);
        listview_listview_sale_and_seckill_discounts.setAdapter(sss_adapter);
    }

    List <SaleAndSeckillDiscountsCouponModel>data=new ArrayList<>();
    public void setData(SaleAndSeckillDiscountsModel saleAndSeckillDiscountsModel ) {
        if (sss_adapter != null) {
            data.addAll(saleAndSeckillDiscountsModel.list);
            sss_adapter.setList(data);
        }
    }

    public void setData(List<SaleAndSeckillDiscountsCouponModel> list ) {
        if (sss_adapter != null) {
            data.addAll(list);
            sss_adapter.setList(data);
        }
    }



    public interface ListViewSaleAndSeckillDiscountsOperationCallBack {

        void onClickMore_ListViewSaleAndSeckillDiscountsOperationCallBack(String classify_id, String title, ListViewSaleAndSeckillDiscounts ListViewSaleAndSeckillDiscounts);

        void onClickTitle_ListViewSaleAndSeckillDiscountsOperationCallBack(String classify_id, String title);
    }

}
