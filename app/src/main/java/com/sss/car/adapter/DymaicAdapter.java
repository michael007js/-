package com.sss.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.model.DymaicModel;

import java.util.List;

import static com.sss.car.Config.nikename;
import static com.sss.car.R.id.click_item_dymaic_adapter;
import static com.sss.car.R.id.comment_item_dymaic_adapter;
import static com.sss.car.R.id.comment_number_item_dymaic_adapter;
import static com.sss.car.R.id.day_item_dymaic_adapter;
import static com.sss.car.R.id.delete_item_dymaic_adapter;
import static com.sss.car.R.id.month_item_dymaic_adapter;
import static com.sss.car.R.id.praise_item_dymaic_adapter;
import static com.sss.car.R.id.praise_number_item_dymaic_adapter;
import static com.sss.car.R.id.share_item_dymaic_adapter;
import static com.sss.car.R.id.share_number_item_dymaic_adapter;
import static com.sss.car.R.id.time_item_dymaic_adapter;


/**
 * Created by leilei on 2017/8/31.
 */

public class DymaicAdapter extends BaseAdapter {
    Activity activity;
    List<DymaicModel> list;
    DymaicOperationCallBack dymaicOperationCallBack;
    LoadImageCallBack loadImageCallBack;

    public DymaicAdapter(Activity activity, List<DymaicModel> list, DymaicOperationCallBack dymaicOperationCallBack, LoadImageCallBack loadImageCallBack) {
        this.activity = activity;
        this.list = list;
        this.dymaicOperationCallBack = dymaicOperationCallBack;
        this.loadImageCallBack = loadImageCallBack;
    }


    public void refresh(List<DymaicModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void refresh(List<DymaicModel> list, int position, View convertView, ViewGroup parent) {
        this.list = list;
        getView(position, convertView, parent);
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        activity = null;
        dymaicOperationCallBack = null;
        loadImageCallBack = null;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final DymaicAdapterHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_dymaic_adapter, null);
            holder = new DymaicAdapterHolder();
            holder.click_item_dymaic_adapter = $.f(convertView, click_item_dymaic_adapter);
            holder.day_item_dymaic_adapter = $.f(convertView, day_item_dymaic_adapter);
            holder.nikename = $.f(convertView, R.id.nikename);
            holder.month_item_dymaic_adapter = $.f(convertView, month_item_dymaic_adapter);
            holder.content_item_dymaic_adapter = $.f(convertView, R.id.content_item_dymaic_adapter);
            holder.time_item_dymaic_adapter = $.f(convertView, time_item_dymaic_adapter);
            holder.delete_item_dymaic_adapter = $.f(convertView, delete_item_dymaic_adapter);
            holder.praise_item_dymaic_adapter = $.f(convertView, praise_item_dymaic_adapter);
            holder.comment_item_dymaic_adapter = $.f(convertView, comment_item_dymaic_adapter);
            holder.share_item_dymaic_adapter = $.f(convertView, share_item_dymaic_adapter);
            holder.nine_view_item_dymaic_adapter = $.f(convertView, R.id.nine_view_item_dymaic_adapter);
            holder.bg_item_dymaic_adapter = $.f(convertView, R.id.bg_item_dymaic_adapter);
            holder.praise_number_item_dymaic_adapter = $.f(convertView, praise_number_item_dymaic_adapter);
            holder.comment_number_item_dymaic_adapter = $.f(convertView, comment_number_item_dymaic_adapter);
            holder.share_number_item_dymaic_adapter = $.f(convertView, share_number_item_dymaic_adapter);
            convertView.setTag(holder);
        } else {
            holder = (DymaicAdapterHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(list.get(position).trends_pid)) {
            holder.content_item_dymaic_adapter.setText(list.get(position).contents);
        } else {
            holder.content_item_dymaic_adapter.setText(list.get(position).contents);
        }
        holder.time_item_dymaic_adapter.setText(list.get(position).create_time);
        holder.day_item_dymaic_adapter.setText(list.get(position).day);
        holder.month_item_dymaic_adapter.setText(list.get(position).month);
        holder.share_number_item_dymaic_adapter.setText(list.get(position).transmit);
//        if (Config.member_id.equals(list.get(position).member_id)) {
//            holder.delete_item_dymaic_adapter.setVisibility(View.VISIBLE);
//            if (dymaicOperationCallBack != null) {
//                holder.delete_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dymaicOperationCallBack.delete(position, list.get(position), list);
//                    }
//                });
//            }
//        } else {
//            holder.delete_item_dymaic_adapter.setVisibility(View.GONE);
//        }
        holder.nikename.setText(list.get(position).username);
        switch (list.get(position).week) {
            case "0":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_one));
                break;
            case "1":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_two));
                break;
            case "2":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_three));
                break;
            case "3":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_four));
                break;
            case "4":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_five));
                break;
            case "5":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_six));
                break;
            case "6":
                APPOftenUtils.setBackgroundOfVersion(holder.bg_item_dymaic_adapter, activity.getResources().getDrawable(R.drawable.bg_color_seven));
                break;
        }
        holder.click_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dymaicOperationCallBack != null) {
                    dymaicOperationCallBack.click(position, list.get(position), list);
                }
            }
        });
        final View finalConvertView = convertView;
        holder.comment_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dymaicOperationCallBack != null) {
                    dymaicOperationCallBack.comment(position, list.get(position), list, finalConvertView, parent);
                }
            }
        });
        holder.delete_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dymaicOperationCallBack != null) {
                    dymaicOperationCallBack.delete(position, list.get(position), list);
                }
            }
        });
        final View finalConvertView1 = convertView;
        holder.share_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dymaicOperationCallBack != null) {
                    dymaicOperationCallBack.share(position, list.get(position), list, finalConvertView1, parent);
                }
            }
        });
        holder.praise_item_dymaic_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dymaicOperationCallBack != null) {
                    dymaicOperationCallBack.praise(position, list.get(position), list, finalConvertView1, parent);
                }
            }
        });

        holder.praise_number_item_dymaic_adapter.setText(list.get(position).likes);
        holder.comment_number_item_dymaic_adapter.setText(list.get(position).looks);
        holder.comment_number_item_dymaic_adapter.setText(list.get(position).comment_count);
        if (loadImageCallBack != null) {
            if ("1".equals(list.get(position).is_praise)) {
                FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + AppUtils.getAppPackageName(activity) + "/" + R.mipmap.logo_praise_yes), holder.praise_item_dymaic_adapter, 0f);
            } else {
                FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + AppUtils.getAppPackageName(activity) + "/" + R.mipmap.logo_praise_no), holder.praise_item_dymaic_adapter, 0f);

            }
        }
        NineAdapter nineAdapter = new NineAdapter(list.get(position).picture, activity, loadImageCallBack, dymaicOperationCallBack);
        holder.nine_view_item_dymaic_adapter.setAdapter(nineAdapter);
        return convertView;
    }

    public static class DymaicAdapterHolder {
        FrameLayout bg_item_dymaic_adapter;
        LinearLayout click_item_dymaic_adapter;
        TextView content_item_dymaic_adapter;
        TextView time_item_dymaic_adapter;
        TextView delete_item_dymaic_adapter;
        SimpleDraweeView praise_item_dymaic_adapter;
        SimpleDraweeView comment_item_dymaic_adapter;
        SimpleDraweeView share_item_dymaic_adapter;
        TextView month_item_dymaic_adapter;
        TextView day_item_dymaic_adapter;
        InnerGridView nine_view_item_dymaic_adapter;
        TextView nikename;
        TextView praise_number_item_dymaic_adapter;
        TextView comment_number_item_dymaic_adapter;
        TextView share_number_item_dymaic_adapter;

    }

}
