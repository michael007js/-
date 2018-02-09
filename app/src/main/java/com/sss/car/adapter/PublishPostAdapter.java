package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.model.CateModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/7.
 */

public class PublishPostAdapter extends BaseAdapter {
    List<CateModel> cateList;
    Context context;

    public PublishPostAdapter(List<CateModel> cateList, Context context) {
        this.cateList = cateList;
        this.context = context;
    }

    public void clear() {
        if (cateList != null) {
            cateList.clear();
        }
        cateList = null;
        context = null;
    }

    @Override
    public int getCount() {
        return cateList.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_post_adapter, null);
            TextView name_item_publish_post_adapter = $.f(convertView, R.id.name_item_publish_post_adapter);
            name_item_publish_post_adapter.setText(cateList.get(position).cate_name);
        }

        return convertView;
    }
}


