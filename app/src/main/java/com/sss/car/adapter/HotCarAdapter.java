package com.sss.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.MyCarOperationCallBack;
import com.sss.car.model.HotCarModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/8/16.
 */

public class HotCarAdapter extends BaseAdapter {
    List<HotCarModel> hotList;
    Context context;
    LoadImageCallBack loadImageCallBack;
    MyCarOperationCallBack myCarOperationCallBack;
    List<HotCarAdapterHolder> hotCarAdapterHolderList=new ArrayList<>();
    HotCarAdapterHolder hotCarAdapterHolder;

    public HotCarAdapter(List<HotCarModel> hotList, Context context, MyCarOperationCallBack myCarOperationCallBack, LoadImageCallBack loadImageCallBack) {
        this.hotList = hotList;
        this.context = context;
        this.myCarOperationCallBack = myCarOperationCallBack;
        this.loadImageCallBack = loadImageCallBack;
    }

    public void refresh(List<HotCarModel> hotList) {
        this.hotList = hotList;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (hotList == null) {
            hotList.clear();
        }
        hotList = null;
        hotCarAdapterHolder=null;
        context = null;
        loadImageCallBack = null;
    }

    @Override
    public int getCount() {
        return hotList.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hot_adapter, null);
            hotCarAdapterHolder = new HotCarAdapterHolder();
            hotCarAdapterHolderList.add(hotCarAdapterHolder);
            hotCarAdapterHolder.line_item_hot_adapter = $.f(convertView, R.id.line_item_hot_adapter);
            hotCarAdapterHolder.logo_item_hot_adapter = $.f(convertView, R.id.logo_item_hot_adapter);
            hotCarAdapterHolder.name_item_hot_adapter = $.f(convertView, R.id.name_item_hot_adapter);
            convertView.setTag(hotCarAdapterHolder);
        } else {
            hotCarAdapterHolder = (HotCarAdapterHolder) convertView.getTag();
        }
        if (loadImageCallBack != null) {
            hotCarAdapterHolder.logo_item_hot_adapter.setTag(R.id.glide_tag, Config.url + hotList.get(position).logo);
            loadImageCallBack.onLoad(GlidUtils.downLoader(false, hotCarAdapterHolder.logo_item_hot_adapter, context));
        }
        hotCarAdapterHolder.name_item_hot_adapter.setText(hotList.get(position).name);
        if (position < 5) {
            hotCarAdapterHolder.line_item_hot_adapter.setVisibility(View.VISIBLE);
        } else {
            hotCarAdapterHolder.line_item_hot_adapter.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(hotList.get(position).brand_id)){
            hotCarAdapterHolder.logo_item_hot_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myCarOperationCallBack!=null){
                        myCarOperationCallBack.clickCarFromHotListOrCarList("hotList",hotList.get(position).brand_id,hotList.get(position).logo,hotList.get(position).name);
                    }
                }
            });
        }

        return convertView;
    }
}

class HotCarAdapterHolder {
    ImageView logo_item_hot_adapter;
    TextView name_item_hot_adapter, line_item_hot_adapter;

}

