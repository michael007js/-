package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.dao.ShareMyPostAdapterCallBack;
import com.sss.car.view.ActivityDymaicDetails;

import java.util.List;


/**
 * Created by leilei on 2017/9/1.
 */

public class NineAdapter2 extends BaseAdapter {
    List<String> list;
    Context context;
    LoadImageCallBack loadImageCallBack;
    int width, height = 0;
    NineAdapter2OperationCallBack nineAdapter2OperationCallBack;

    public NineAdapter2(List<String> list, Context context, LoadImageCallBack loadImageCallBack, NineAdapter2OperationCallBack nineAdapter2OperationCallBack) {
        this.list = list;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.nineAdapter2OperationCallBack = nineAdapter2OperationCallBack;
    }

    public NineAdapter2(int width, int height, List<String> list, Context context, LoadImageCallBack loadImageCallBack, NineAdapter2OperationCallBack nineAdapter2OperationCallBack) {
        this.list = list;
        this.height = height;
        this.width = width;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.nineAdapter2OperationCallBack = nineAdapter2OperationCallBack;
    }


    public void refresh(List<String> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void changeImageSize(int width, int height){
        this.height = height;
        this.width = width;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        loadImageCallBack = null;
        nineAdapter2OperationCallBack = null;

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
        NineAdapter2Holder NineAdapter2Holder;
        if (convertView == null) {
            NineAdapter2Holder = new NineAdapter2Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ltem_nine_view, null);
            NineAdapter2Holder.pic_item_nine_view = $.f(convertView, R.id.pic_item_nine_view);
            convertView.setTag(NineAdapter2Holder);
        } else {
            NineAdapter2Holder = (NineAdapter2Holder) convertView.getTag();
        }

        if (width == 0||height==0) {
            if (loadImageCallBack != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) NineAdapter2Holder.pic_item_nine_view.getLayoutParams();
                layoutParams.width = 100;
                layoutParams.height = 100;
                NineAdapter2Holder.pic_item_nine_view.setLayoutParams(layoutParams);
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + list.get(position)), NineAdapter2Holder.pic_item_nine_view, 0f));
            }
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) NineAdapter2Holder.pic_item_nine_view.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            NineAdapter2Holder.pic_item_nine_view.setLayoutParams(layoutParams);
            if (loadImageCallBack != null) {
                LogUtils.e(Config.url + list.get(position));
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, width, height, Uri.parse(Config.url + list.get(position)), NineAdapter2Holder.pic_item_nine_view, 0f));
            }

        }


        if (nineAdapter2OperationCallBack != null) {
            NineAdapter2Holder.pic_item_nine_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nineAdapter2OperationCallBack.onClickImage(position, list.get(position), list);
                }
            });

        }
        return convertView;
    }
}

class NineAdapter2Holder {
    public SimpleDraweeView pic_item_nine_view;
}
