package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.ListViewUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.WalletDetailsModel;
import com.sss.car.model.WalletDetails_SubclassModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/25.
 */

public class ListViewWalletDetails extends LinearLayout {
    List<WalletDetailsModel> list = new ArrayList<>();
    List<SSS_Adapter> sssAdapterList = new ArrayList<>();
    View view;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sssAdapterList != null) {
            sssAdapterList.clear();
        }
        sssAdapterList = null;
    }

    public ListViewWalletDetails(Context context) {
        super(context);
    }

    public ListViewWalletDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewWalletDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addHead(View view) {
        this.view = view;
        this.addView(this.view);
    }

    public void clearOtherView(){
        this.removeAllViews();
        this.addView(view);
    }

    public void setList(Context context, List<WalletDetailsModel> list) {
        this.removeAllViews();
        this.list = list;
        this.addView(view);
        showData(context);
    }

    private void showData(Context context) {
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_wallet_details, null);
            TextView type_name_item_fragment_wallet_details = $.f(view, R.id.type_name_item_fragment_wallet_details);
            InnerListview listview_item_fragment_wallet_details = $.f(view, R.id.listview_item_fragment_wallet_details);
            type_name_item_fragment_wallet_details.setText(list.get(i).type_name);
            SSS_Adapter sss_adapter = new SSS_Adapter<WalletDetails_SubclassModel>(context, R.layout.item_fragment_wallet_details_child) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, WalletDetails_SubclassModel bean,SSS_Adapter instance) {
                    LogUtils.e(bean.toString());
                    helper.setText(R.id.type_item_fragment_wallet_details_child, bean.remark);
                    helper.setText(R.id.date_item_fragment_wallet_details_child, "日期:" + bean.create_time);
                    //1收入，2支出，3积分,4资金
                    if ("1".equals(bean.type)){
                        helper.setVisibility(R.id.parent,VISIBLE);
                        if (!StringUtils.isEmpty(bean.order_code)){
                            helper.setText(R.id.code_item_fragment_wallet_details_child, "订单编号:"+bean.order_code);
                        }
                        helper.setText(R.id.price_item_fragment_wallet_details_child, "发生金额:" + bean.total);
                        helper.setText(R.id.three, "收入金额:"+bean.money);
                        helper.setText(R.id.four, "费用:"+bean.rate_price);
                    }else  if ("2".equals(bean.type)){
                        helper.setVisibility(R.id.parent,VISIBLE);
                        if (!StringUtils.isEmpty(bean.order_code)){
                            helper.setText(R.id.code_item_fragment_wallet_details_child, "订单编号:"+bean.order_code);
                        }
                        helper.setText(R.id.price_item_fragment_wallet_details_child, "发生金额:" + bean.total);
                        helper.setText(R.id.three, "支出金额:"+bean.money);
                        helper.setText(R.id.four, "费用:"+bean.rate_price);
                    }else if ("3".equals(bean.type)){
                        helper.setText(R.id.price_item_fragment_wallet_details_child, "数量:" + bean.integral);
                    }else if ("4".equals(bean.type)){
                        if (!StringUtils.isEmpty(bean.order_code)){
                            helper.setText(R.id.code_item_fragment_wallet_details_child, "订单编号:"+bean.order_code);
                        }
                        helper.setText(R.id.price_item_fragment_wallet_details_child, "金额:" + bean.money);
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview_item_fragment_wallet_details.setAdapter(sss_adapter);
            sss_adapter.setList(list.get(i).subclass);
            sssAdapterList.add(sss_adapter);
            this.addView(view);
        }
    }


}
