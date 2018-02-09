package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.ItemTabAdapterOperationCallBack;
import com.sss.car.model.CateModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public class ItemTabAdapter extends BaseAdapter {
    Context context;
    List<CateModel> list;
    LoadImageCallBack loadImageCallBack;

    public ItemTabAdapter(Context context, List<CateModel> list, LoadImageCallBack loadImageCallBack ) {
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
    }

    public void refresh(List<CateModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemTabAdapterHolder itemTabAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
            itemTabAdapterHolder = new ItemTabAdapterHolder();
            itemTabAdapterHolder.click_item_tab = $.f(convertView, R.id.click_item_tab);
            itemTabAdapterHolder.pic_item_tab = $.f(convertView, R.id.pic_item_tab);
            itemTabAdapterHolder.text_item_tab = $.f(convertView, R.id.text_item_tab);
            convertView.setTag(itemTabAdapterHolder);
        } else {
            itemTabAdapterHolder = (ItemTabAdapterHolder) convertView.getTag();
        }
        itemTabAdapterHolder.text_item_tab.setText(list.get(position).cate_name);
        if (loadImageCallBack!=null){
            loadImageCallBack.onLoad(FrescoUtils.showImage(true,40,40, list.get(position).logo,itemTabAdapterHolder.pic_item_tab,0f));
        }

        return convertView;
    }
}

class ItemTabAdapterHolder {
    LinearLayout click_item_tab;
    SimpleDraweeView pic_item_tab;
    TextView text_item_tab;
}
