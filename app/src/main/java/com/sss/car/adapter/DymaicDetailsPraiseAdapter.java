package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicDetailsPraiseAdapterCallBack;
import com.sss.car.model.DymaicDetailsPraiseModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/2.
 */

public class DymaicDetailsPraiseAdapter extends BaseAdapter {
    List<DymaicDetailsPraiseModel> list;
    Context context;
    LoadImageCallBack loadImageCallBack;
    DymaicDetailsPraiseAdapterCallBack dymaicDetailsPraiseAdapterCallBack;


    public DymaicDetailsPraiseAdapter(List<DymaicDetailsPraiseModel> list, Context context, LoadImageCallBack loadImageCallBack, DymaicDetailsPraiseAdapterCallBack dymaicDetailsPraiseAdapterCallBack) {
        this.list = list;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.dymaicDetailsPraiseAdapterCallBack = dymaicDetailsPraiseAdapterCallBack;
    }


    public void refresh(List<DymaicDetailsPraiseModel> list){
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        loadImageCallBack = null;
        dymaicDetailsPraiseAdapterCallBack = null;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DymaicDetailsPraiseAdapterHolder dymaicDetailsPraiseAdapterHolder;
        if (convertView == null) {
            dymaicDetailsPraiseAdapterHolder = new DymaicDetailsPraiseAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dymaic_details_praise_adapter, null);
            dymaicDetailsPraiseAdapterHolder.pic_item_dymaic_details_praise_adapter = $.f(convertView, R.id.pic_item_dymaic_details_praise_adapter);
            convertView.setTag(dymaicDetailsPraiseAdapterHolder);
        } else {
            dymaicDetailsPraiseAdapterHolder = (DymaicDetailsPraiseAdapterHolder) convertView.getTag();
        }
        if (loadImageCallBack != null) {
            loadImageCallBack.onLoad(FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + list.get(position).face), dymaicDetailsPraiseAdapterHolder.pic_item_dymaic_details_praise_adapter, 9999f));
        }
        if (dymaicDetailsPraiseAdapterCallBack != null) {
            dymaicDetailsPraiseAdapterHolder.pic_item_dymaic_details_praise_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dymaicDetailsPraiseAdapterCallBack.onClickPraiseItem(position, list.get(position));
                }
            });
        }
        return convertView;
    }
}

class DymaicDetailsPraiseAdapterHolder {
    SimpleDraweeView pic_item_dymaic_details_praise_adapter;
}
