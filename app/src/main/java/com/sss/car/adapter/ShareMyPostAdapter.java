package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.dao.ShareMyPostAdapterCallBack;
import com.sss.car.model.Community_Userinfo_Posts_Model;

import java.util.List;

/**
 * Created by leilei on 2017/9/5.
 */

public class ShareMyPostAdapter extends BaseAdapter {
    Context context;
    List<Community_Userinfo_Posts_Model> list;
    LoadImageCallBack loadImageCallBack;
    NineAdapter2OperationCallBack nineAdapter2OperationCallBack;
    ShareMyPostAdapterCallBack shareMyPostAdapterCallBack;

    public ShareMyPostAdapter(Context context, List<Community_Userinfo_Posts_Model> list, LoadImageCallBack loadImageCallBack, NineAdapter2OperationCallBack nineAdapter2OperationCallBack, ShareMyPostAdapterCallBack shareMyPostAdapterCallBack) {
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
        this.nineAdapter2OperationCallBack = nineAdapter2OperationCallBack;
        this.shareMyPostAdapterCallBack = shareMyPostAdapterCallBack;
    }


    public void refresh(List<Community_Userinfo_Posts_Model> list) {
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
        nineAdapter2OperationCallBack = null;
        shareMyPostAdapterCallBack = null;
    }

    public void updateItem(int position, List<Community_Userinfo_Posts_Model> list,ListView listView) {
        this.list=list;
        this.notifyDataSetChanged();

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
        ShareMyPostAdapterHolder shareMyPostAdapterHolder;
        if (convertView == null) {
            shareMyPostAdapterHolder = new ShareMyPostAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_post_my_adapter, null);
            shareMyPostAdapterHolder.click_item_share_my_post_adapter = $.f(convertView, R.id.click_item_share_my_post_adapter);
            shareMyPostAdapterHolder.bg_item_share_my_post_adapter = $.f(convertView, R.id.bg_item_share_my_post_adapter);
            shareMyPostAdapterHolder.month_item_share_my_post_adapter = $.f(convertView, R.id.month_item_share_my_post_adapter);
            shareMyPostAdapterHolder.day_item_share_my_post_adapter = $.f(convertView, R.id.day_item_share_my_post_adapter);
            shareMyPostAdapterHolder.content_item_share_my_post_adapter = $.f(convertView, R.id.content_item_share_my_post_adapter);
            shareMyPostAdapterHolder.nine_view_item_share_my_post_adapter = $.f(convertView, R.id.nine_view_item_share_my_post_adapter);
            shareMyPostAdapterHolder.click_collect_item_share_my_post_adapter = $.f(convertView, R.id.click_collect_item_share_my_post_adapter);
            shareMyPostAdapterHolder.number_collect_item_share_my_post_adapter = $.f(convertView, R.id.number_collect_item_share_my_post_adapter);
            shareMyPostAdapterHolder.click_comment_item_share_my_post_adapter = $.f(convertView, R.id.click_comment_item_share_my_post_adapter);
            shareMyPostAdapterHolder.number_comment_item_share_my_post_adapter = $.f(convertView, R.id.number_comment_item_share_my_post_adapter);
            shareMyPostAdapterHolder.click_share_item_share_my_post_adapter = $.f(convertView, R.id.click_share_item_share_my_post_adapter);
            shareMyPostAdapterHolder.number_share_item_share_my_post_adapter = $.f(convertView, R.id.number_share_item_share_my_post_adapter);
            shareMyPostAdapterHolder.type_name_item_share_my_post_adapter = $.f(convertView, R.id.type_name_item_share_my_post_adapter);
            shareMyPostAdapterHolder.click_type_name_item_share_my_post_adapter = $.f(convertView, R.id.click_type_name_item_share_my_post_adapter);
            shareMyPostAdapterHolder.delete_view_item_share_my_post_adapter = $.f(convertView, R.id.delete_view_item_share_my_post_adapter);
            shareMyPostAdapterHolder.image_collect=$.f(convertView,R.id.image_collect);
            convertView.setTag(shareMyPostAdapterHolder);
        } else {
            shareMyPostAdapterHolder = (ShareMyPostAdapterHolder) convertView.getTag();
        }

        if (nineAdapter2OperationCallBack!=null){
            shareMyPostAdapterHolder.nine_view_item_share_my_post_adapter.setAdapter(new NineAdapter2(300,300,list.get(position).picture,context,loadImageCallBack,nineAdapter2OperationCallBack));
        }

        switch (list.get(position).week) {
            case "0":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_one));
                break;
            case "1":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_two));
                break;
            case "2":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_three));
                break;
            case "3":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_four));
                break;
            case "4":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_five));
                break;
            case "5":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_six));
                break;
            case "6":
                APPOftenUtils.setBackgroundOfVersion(shareMyPostAdapterHolder.bg_item_share_my_post_adapter, context.getResources().getDrawable(R.drawable.bg_color_seven));
                break;
        }

        if ("1".equals(list.get(position).is_collect)){
            FrescoUtils.showImage(false,20,20, Uri.parse("res://"+ AppUtils.getAppPackageName(context)+"/"+R.mipmap.logo_collect),shareMyPostAdapterHolder.image_collect,0f);
        }else {
            FrescoUtils.showImage(false,20,20, Uri.parse("res://"+ AppUtils.getAppPackageName(context)+"/"+R.mipmap.logo_collect_no),shareMyPostAdapterHolder.image_collect,0f);
        }
        shareMyPostAdapterHolder.content_item_share_my_post_adapter.setText(list.get(position).title);
        shareMyPostAdapterHolder.number_collect_item_share_my_post_adapter.setText(list.get(position).collect_count);
        shareMyPostAdapterHolder.number_comment_item_share_my_post_adapter.setText(list.get(position).comment_count);
        shareMyPostAdapterHolder.number_share_item_share_my_post_adapter.setText(list.get(position).share);
        shareMyPostAdapterHolder.type_name_item_share_my_post_adapter.setText(list.get(position).cate_name);
        shareMyPostAdapterHolder.click_type_name_item_share_my_post_adapter.setVisibility(View.VISIBLE);
        shareMyPostAdapterHolder.day_item_share_my_post_adapter.setText(list.get(position).day);
        shareMyPostAdapterHolder.month_item_share_my_post_adapter.setText(list.get(position).month);

        if (shareMyPostAdapterCallBack != null) {
            shareMyPostAdapterHolder.click_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onClickItem(position, list.get(position), list);
                }
            });
            shareMyPostAdapterHolder.click_collect_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onCollect(position, list.get(position), list);
                }
            });
            shareMyPostAdapterHolder.click_comment_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onComment(position, list.get(position), list);
                }
            });
            shareMyPostAdapterHolder.click_share_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onShare(position, list.get(position), list);
                }
            });

            shareMyPostAdapterHolder.click_type_name_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onClickType(position, list.get(position), list);
                }
            });
            shareMyPostAdapterHolder.delete_view_item_share_my_post_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMyPostAdapterCallBack.onDelete(position, list.get(position), list);
                }
            });
        }


        return convertView;
    }
}

class ShareMyPostAdapterHolder {

    LinearLayout click_item_share_my_post_adapter;
    SimpleDraweeView image_collect;

    FrameLayout bg_item_share_my_post_adapter;
    TextView month_item_share_my_post_adapter;
    TextView day_item_share_my_post_adapter;
    TextView content_item_share_my_post_adapter;
    LinearLayout click_type_name_item_share_my_post_adapter;
    InnerGridView nine_view_item_share_my_post_adapter;

    LinearLayout click_collect_item_share_my_post_adapter;
    TextView number_collect_item_share_my_post_adapter;
    LinearLayout click_comment_item_share_my_post_adapter;
    TextView number_comment_item_share_my_post_adapter;
    LinearLayout click_share_item_share_my_post_adapter;
    TextView number_share_item_share_my_post_adapter;

    TextView type_name_item_share_my_post_adapter;
    TextView delete_view_item_share_my_post_adapter;
}
