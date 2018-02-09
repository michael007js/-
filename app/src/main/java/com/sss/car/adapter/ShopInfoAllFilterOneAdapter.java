package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.ChooseCallBack;
import com.sss.car.dao.ShopInfoAllFilterOneAdapterOperationCallBack;
import com.sss.car.model.ShopInfoAllFilterModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/19.
 */

public class ShopInfoAllFilterOneAdapter extends BaseAdapter {
    Context context;
    List<ShopInfoAllFilterModel> list;
    ShopInfoAllFilterOneAdapterOperationCallBack shopInfoAllFilterOneAdapterOperationCallBack;
    ChooseCallBack chooseCallBack;
    int position = 0;

    public ShopInfoAllFilterOneAdapter(Context context, List<ShopInfoAllFilterModel> list, ChooseCallBack chooseCallBack, ShopInfoAllFilterOneAdapterOperationCallBack shopInfoAllFilterOneAdapterOperationCallBack) {
        this.context = context;
        this.list = list;
        this.chooseCallBack = chooseCallBack;
        this.shopInfoAllFilterOneAdapterOperationCallBack = shopInfoAllFilterOneAdapterOperationCallBack;
    }

    public void refresh(List<ShopInfoAllFilterModel> list) {
        this.list = list;
        this.notifyDataSetChanged();

    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
    }

    @Override
    public int getCount() {
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
        ShopInfoAllFilterOneAdapterHolder shopInfoAllFilterOneAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_info_all_filter, null);
            shopInfoAllFilterOneAdapterHolder = new ShopInfoAllFilterOneAdapterHolder();
            shopInfoAllFilterOneAdapterHolder.text_item_shop_info_all_filter = $.f(convertView, R.id.text_item_shop_info_all_filter);
            convertView.setTag(shopInfoAllFilterOneAdapterHolder);
        } else {
            shopInfoAllFilterOneAdapterHolder = (ShopInfoAllFilterOneAdapterHolder) convertView.getTag();
        }
        if (this.position == position) {
            shopInfoAllFilterOneAdapterHolder.text_item_shop_info_all_filter.setTextColor(context.getResources().getColor(R.color.mainColor));
        } else {
            shopInfoAllFilterOneAdapterHolder.text_item_shop_info_all_filter.setTextColor(context.getResources().getColor(R.color.textColor));
        }
        shopInfoAllFilterOneAdapterHolder.text_item_shop_info_all_filter.setText(list.get(position).name);


        if (chooseCallBack != null) {
            if (position == 0) {
                chooseCallBack.onChoose(position, list.get(0).subclass);
            }

        }
        shopInfoAllFilterOneAdapterHolder.text_item_shop_info_all_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopInfoAllFilterOneAdapterOperationCallBack != null) {
                    ShopInfoAllFilterOneAdapter.this.position = position;
                    shopInfoAllFilterOneAdapterOperationCallBack.onClickShopInfoAllFilterOneAdapterItem(position, list.get(position), list);
                }
                ShopInfoAllFilterOneAdapter.this.notifyDataSetChanged();

            }
        });

        return convertView;
    }
}

class ShopInfoAllFilterOneAdapterHolder {
    TextView text_item_shop_info_all_filter;
}
