package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.SharePostDetailsCommentAdapterCallBack;
import com.sss.car.model.SharePostDetailsCommentModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/6.
 */

public class SharePostDetailsCommentAdapter extends BaseAdapter {
    List<SharePostDetailsCommentModel> list;
    Context context;
    SharePostDetailsCommentAdapterCallBack sharePostDetailsCommentAdapterCallBack;
    LoadImageCallBack loadImageCallBack;
    String hold;

    public SharePostDetailsCommentAdapter(String hold,List<SharePostDetailsCommentModel> list, Context context, SharePostDetailsCommentAdapterCallBack sharePostDetailsCommentAdapterCallBack, LoadImageCallBack loadImageCallBack) {
        this.hold=hold;
        this.list = list;
        this.context = context;
        this.sharePostDetailsCommentAdapterCallBack = sharePostDetailsCommentAdapterCallBack;
        this.loadImageCallBack = loadImageCallBack;
    }

    public void refresh(List<SharePostDetailsCommentModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        sharePostDetailsCommentAdapterCallBack = null;
        context = null;
        loadImageCallBack = null;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        SharePostDetailsCommentAdapterHolder sharePostDetailsCommentAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_post_details_comment_adapter, null);
            sharePostDetailsCommentAdapterHolder = new SharePostDetailsCommentAdapterHolder();
            sharePostDetailsCommentAdapterHolder.pic_item_share_post_details_comment_adapter = $.f(convertView, R.id.pic_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.nikename_item_share_post_details_comment_adapter = $.f(convertView, R.id.nikename_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.car_name_dymaic_details_adapter = $.f(convertView, R.id.car_name_dymaic_details_adapter);
            sharePostDetailsCommentAdapterHolder.floor_item_share_post_details_comment_adapter = $.f(convertView, R.id.floor_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.list_item_share_post_details_comment_adapter = $.f(convertView, R.id.list_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.content_item_share_post_details_comment_adapter = $.f(convertView, R.id.content_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.date_item_share_post_details_comment_adapter = $.f(convertView, R.id.date_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.click_praise_item_share_post_details_comment_adapter = $.f(convertView, R.id.click_praise_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.praise_image_item_share_post_details_comment_adapter = $.f(convertView, R.id.praise_image_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.praise_number_item_share_post_details_comment_adapter = $.f(convertView, R.id.praise_number_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.click_comment_item_share_post_details_comment_adapter = $.f(convertView, R.id.click_comment_item_share_post_details_comment_adapter);
            sharePostDetailsCommentAdapterHolder.is_null = $.f(convertView, R.id.is_null);
            sharePostDetailsCommentAdapterHolder.no_null = $.f(convertView, R.id.no_null);
            convertView.setTag(sharePostDetailsCommentAdapterHolder);
        } else {
            sharePostDetailsCommentAdapterHolder = (SharePostDetailsCommentAdapterHolder) convertView.getTag();
        }
        if (hold.equals(list.get(position).contents)) {
            sharePostDetailsCommentAdapterHolder.is_null.setVisibility(View.VISIBLE);
            sharePostDetailsCommentAdapterHolder.no_null.setVisibility(View.GONE);
        } else {
            sharePostDetailsCommentAdapterHolder.no_null.setVisibility(View.VISIBLE);
            sharePostDetailsCommentAdapterHolder.is_null.setVisibility(View.GONE);
            if (loadImageCallBack != null) {
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + list.get(position).face), sharePostDetailsCommentAdapterHolder.pic_item_share_post_details_comment_adapter, 0f));
                if ("1".equals(list.get(position).is_likes)) {
                    loadImageCallBack.onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_praise_yes), sharePostDetailsCommentAdapterHolder.praise_image_item_share_post_details_comment_adapter, 0f));
                } else {
                    loadImageCallBack.onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_praise_no), sharePostDetailsCommentAdapterHolder.praise_image_item_share_post_details_comment_adapter, 0f));
                }
            }
            sharePostDetailsCommentAdapterHolder.nikename_item_share_post_details_comment_adapter.setText(list.get(position).username);
            sharePostDetailsCommentAdapterHolder.car_name_dymaic_details_adapter.setText(list.get(position).vehicle_name);
            sharePostDetailsCommentAdapterHolder.floor_item_share_post_details_comment_adapter.setText((position + 1) + "楼");
            sharePostDetailsCommentAdapterHolder.content_item_share_post_details_comment_adapter.setText(list.get(position).contents);
            sharePostDetailsCommentAdapterHolder.date_item_share_post_details_comment_adapter.setText(list.get(position).create_time);
            sharePostDetailsCommentAdapterHolder.praise_number_item_share_post_details_comment_adapter.setText(list.get(position).likes);

            if (sharePostDetailsCommentAdapterCallBack != null) {
                final int finalPosition = position;
                final int finalPosition1 = position;
                sharePostDetailsCommentAdapterHolder.click_comment_item_share_post_details_comment_adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sharePostDetailsCommentAdapterCallBack.onComment(finalPosition, list, list.get(finalPosition1));
                    }
                });
                sharePostDetailsCommentAdapterHolder.click_praise_item_share_post_details_comment_adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharePostDetailsCommentAdapterCallBack.onPraise(finalPosition, list, list.get(finalPosition1));
                    }
                });
            }

            sharePostDetailsCommentAdapterHolder.list_item_share_post_details_comment_adapter.setAdapter(new SharePostDetailsComment_ReplyListAdapter(context, list.get(position).reply_list));
        }
        return convertView;
    }
}

class SharePostDetailsCommentAdapterHolder {

    SimpleDraweeView pic_item_share_post_details_comment_adapter;
    TextView nikename_item_share_post_details_comment_adapter;
    TextView car_name_dymaic_details_adapter;
    TextView floor_item_share_post_details_comment_adapter;
    InnerListview list_item_share_post_details_comment_adapter;
    TextView content_item_share_post_details_comment_adapter;
    TextView date_item_share_post_details_comment_adapter;
    LinearLayout click_praise_item_share_post_details_comment_adapter;
    SimpleDraweeView praise_image_item_share_post_details_comment_adapter;
    TextView praise_number_item_share_post_details_comment_adapter;
    LinearLayout click_comment_item_share_post_details_comment_adapter;
    LinearLayout no_null;
    TextView is_null;
}
