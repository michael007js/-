package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.Notification.pugnotification.notification.Simple;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicDetailsOperationCallBack;
import com.sss.car.model.DymaicDetailsCommentModel;

import java.util.List;


/**
 * Created by leilei on 2017/9/2.
 */

public class ActivityDymaicDetailsAdapter extends BaseAdapter {
    Context context;
    List<DymaicDetailsCommentModel> list;
    DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack;
    LoadImageCallBack loadImageCallBack;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        dymaicDetailsOperationCallBack = null;
    }

    public void refresh(List<DymaicDetailsCommentModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public ActivityDymaicDetailsAdapter(Context context, List<DymaicDetailsCommentModel> list, DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack, LoadImageCallBack loadImageCallBack) {
        this.context = context;
        this.list = list;
        this.dymaicDetailsOperationCallBack = dymaicDetailsOperationCallBack;
        this.loadImageCallBack = loadImageCallBack;
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ActivityDymaicDetailsAdapterHolder activityDymaicDetailsAdapterHolder;
        if (convertView == null) {
            activityDymaicDetailsAdapterHolder = new ActivityDymaicDetailsAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dymaic_details_adapter, null);
            activityDymaicDetailsAdapterHolder.click_item_dymaic_details_adapter = $.f(convertView, R.id.click_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.pic_item_dymaic_details_adapter = $.f(convertView, R.id.pic_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.nikename_item_dymaic_details_adapter = $.f(convertView, R.id.nikename_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.time_item_dymaic_details_adapter = $.f(convertView, R.id.time_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.reply_item_dymaic_details_adapter = $.f(convertView, R.id.reply_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.reply_nikename_item_dymaic_details_adapter = $.f(convertView, R.id.reply_nikename_item_dymaic_details_adapter);
            activityDymaicDetailsAdapterHolder.content_nikename_item_dymaic_details_adapter = $.f(convertView, R.id.content_nikename_item_dymaic_details_adapter);
            convertView.setTag(activityDymaicDetailsAdapterHolder);
        } else {
            activityDymaicDetailsAdapterHolder = (ActivityDymaicDetailsAdapterHolder) convertView.getTag();
        }
        if (dymaicDetailsOperationCallBack != null) {
            activityDymaicDetailsAdapterHolder.click_item_dymaic_details_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dymaicDetailsOperationCallBack.onClickItem(position, list.get(position));
                }
            });
            if (StringUtils.isEmpty(list.get(position).user_name)) {//被回复者是否为空
                activityDymaicDetailsAdapterHolder.reply_item_dymaic_details_adapter.setVisibility(View.GONE);
                activityDymaicDetailsAdapterHolder.reply_nikename_item_dymaic_details_adapter.setVisibility(View.GONE);
            } else {
                activityDymaicDetailsAdapterHolder.reply_item_dymaic_details_adapter.setVisibility(View.VISIBLE);
                activityDymaicDetailsAdapterHolder.reply_nikename_item_dymaic_details_adapter.setVisibility(View.VISIBLE);
                activityDymaicDetailsAdapterHolder.reply_nikename_item_dymaic_details_adapter.setText(list.get(position).user_name + ": ");
            }
            activityDymaicDetailsAdapterHolder.content_nikename_item_dymaic_details_adapter.setText(list.get(position).contents);
            activityDymaicDetailsAdapterHolder.nikename_item_dymaic_details_adapter.setText(list.get(position).username);
            if (loadImageCallBack != null) {
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + list.get(position).face), activityDymaicDetailsAdapterHolder.pic_item_dymaic_details_adapter, 9999f));
            }
            activityDymaicDetailsAdapterHolder.time_item_dymaic_details_adapter.setText(list.get(position).create_time);
        }
        return convertView;
    }
}

class ActivityDymaicDetailsAdapterHolder {
    LinearLayout click_item_dymaic_details_adapter;
    SimpleDraweeView pic_item_dymaic_details_adapter;
    TextView nikename_item_dymaic_details_adapter;
    TextView time_item_dymaic_details_adapter;
    TextView reply_item_dymaic_details_adapter;
    TextView reply_nikename_item_dymaic_details_adapter;
    TextView content_nikename_item_dymaic_details_adapter;


}
