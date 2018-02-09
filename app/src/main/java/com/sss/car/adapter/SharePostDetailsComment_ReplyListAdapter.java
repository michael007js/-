package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;

import java.util.List;

/**
 * Created by leilei on 2017/9/6.
 */

public class SharePostDetailsComment_ReplyListAdapter extends BaseAdapter {
    Context context;
    List<SharePostDetailsCommentReplayModel> list;

    public SharePostDetailsComment_ReplyListAdapter(Context context, List<SharePostDetailsCommentReplayModel> list) {
        this.context = context;
        this.list = list;
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
        SharePostDetailsComment_ReplyListAdapterHolder sharePostDetailsComment_replyListAdapterHolder;
        if (convertView == null) {
            sharePostDetailsComment_replyListAdapterHolder = new SharePostDetailsComment_ReplyListAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_post_details_comment_reply_list_adapter, null);
            sharePostDetailsComment_replyListAdapterHolder.content_item_share_post_details_comment_reply_list_adapter = $.f(convertView, R.id.content_item_share_post_details_comment_reply_list_adapter);
            sharePostDetailsComment_replyListAdapterHolder.time_item_share_post_details_comment_reply_list_adapter = $.f(convertView, R.id.time_item_share_post_details_comment_reply_list_adapter);
            sharePostDetailsComment_replyListAdapterHolder.nikename_item_share_post_details_comment_reply_list_adapter = $.f(convertView, R.id.nikename_item_share_post_details_comment_reply_list_adapter);
            convertView.setTag(sharePostDetailsComment_replyListAdapterHolder);
        }else {
            sharePostDetailsComment_replyListAdapterHolder= (SharePostDetailsComment_ReplyListAdapterHolder) convertView.getTag();
        }
        LogUtils.e(list.get(position).toString());
        sharePostDetailsComment_replyListAdapterHolder.content_item_share_post_details_comment_reply_list_adapter.setText(list.get(position).contents);
        sharePostDetailsComment_replyListAdapterHolder.nikename_item_share_post_details_comment_reply_list_adapter.setText(list.get(position).username);
        sharePostDetailsComment_replyListAdapterHolder.time_item_share_post_details_comment_reply_list_adapter.setText(list.get(position).create_time);
        return convertView;
    }
}

class SharePostDetailsComment_ReplyListAdapterHolder {
    TextView nikename_item_share_post_details_comment_reply_list_adapter;
    TextView time_item_share_post_details_comment_reply_list_adapter;
    TextView content_item_share_post_details_comment_reply_list_adapter;
}
