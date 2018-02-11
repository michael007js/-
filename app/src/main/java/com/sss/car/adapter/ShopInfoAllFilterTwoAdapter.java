package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.dao.ShopInfoAllFilterTwoAdapterOperationCallBack;
import com.sss.car.model.ShopInfoAllFilter_SubClassModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/19.
 */

public class ShopInfoAllFilterTwoAdapter extends BaseAdapter {

    Context context;
    List<ShopInfoAllFilter_SubClassModel> list;
    ShopInfoAllFilterTwoAdapterOperationCallBack shopInfoAllFilterTwoAdapterOperationCallBack;

    public ShopInfoAllFilterTwoAdapter(Context context, List<ShopInfoAllFilter_SubClassModel> list, ShopInfoAllFilterTwoAdapterOperationCallBack shopInfoAllFilterTwoAdapterOperationCallBack) {
        this.context = context;
        this.list = list;
        this.shopInfoAllFilterTwoAdapterOperationCallBack = shopInfoAllFilterTwoAdapterOperationCallBack;
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        shopInfoAllFilterTwoAdapterOperationCallBack = null;
        context = null;
    }

    public void refresh(List<ShopInfoAllFilter_SubClassModel> list) {
        this.list = list;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
       if (list!=null){
           LogUtils.e(list.size());
       }
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ShopInfoAllFilterTwoAdapterHolder shopInfoAllFilterTwoAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_info_all_filter, null);
            shopInfoAllFilterTwoAdapterHolder = new ShopInfoAllFilterTwoAdapterHolder();
            shopInfoAllFilterTwoAdapterHolder.text_item_shop_info_all_filter = $.f(convertView, R.id.text_item_shop_info_all_filter);
            convertView.setTag(shopInfoAllFilterTwoAdapterHolder);
        } else {
            shopInfoAllFilterTwoAdapterHolder = (ShopInfoAllFilterTwoAdapterHolder) convertView.getTag();
        }
        shopInfoAllFilterTwoAdapterHolder.text_item_shop_info_all_filter.setText(list.get(position).name);
        shopInfoAllFilterTwoAdapterHolder.text_item_shop_info_all_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopInfoAllFilterTwoAdapterOperationCallBack != null) {
                    shopInfoAllFilterTwoAdapterOperationCallBack.onClickShopInfoAllFilterTwoAdapterItem(position, list.get(position), list);
                }
            }
        });
        return convertView;
    }
}

class ShopInfoAllFilterTwoAdapterHolder {
    TextView text_item_shop_info_all_filter;
}
