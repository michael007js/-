package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.MessageInteractionManageFriendAttentionFansPublicClickCallBack;
import com.sss.car.model.MessageInteractionManageFriendAttentionFansPublicModel;

import java.util.List;

import static com.sss.car.R.id.data_item_fragment_message_interaction_manage_friend_attention_fans_public;
import static com.sss.car.R.id.delete_item_fragment_message_interaction_manage_friend_attention_fans_public;
import static com.sss.car.R.id.pic_item_fragment_message_interaction_manage_friend_attention_fans_public;
import static com.sss.car.R.id.scoll_item_fragment_message_interaction_manage_friend_attention_fans_public;
import static com.sss.car.R.id.text_item_fragment_message_interaction_manage_friend_attention_fans_public;
import static com.sss.car.R.id.title_item_fragment_message_interaction_manage_friend_attention_fans_public;

/**
 * Created by leilei on 2017/8/28.
 */

public class MessageInteractionManageFriendAttentionFansPublicAdapter extends BaseAdapter {
    MessageInteractionManageFriendAttentionFansPublicClickCallBack messageInteractionManageFriendAttentionFansPublicClickCallBack;
    List<MessageInteractionManageFriendAttentionFansPublicModel> list;
    LoadImageCallBack loadImageCallBack;
    Context context;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        messageInteractionManageFriendAttentionFansPublicClickCallBack = null;
        loadImageCallBack = null;
        context = null;
    }

    public void refresh(List<MessageInteractionManageFriendAttentionFansPublicModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public MessageInteractionManageFriendAttentionFansPublicAdapter(MessageInteractionManageFriendAttentionFansPublicClickCallBack messageInteractionManageFriendAttentionFansPublicClickCallBack, Context context, LoadImageCallBack loadImageCallBack, List<MessageInteractionManageFriendAttentionFansPublicModel> list) {
        this.messageInteractionManageFriendAttentionFansPublicClickCallBack = messageInteractionManageFriendAttentionFansPublicClickCallBack;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.list = list;
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
        MessageInteractionManageFriendAttentionFansPublicAdapterHolder myHolder;
        if (convertView == null) {
            myHolder = new MessageInteractionManageFriendAttentionFansPublicAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_message_interaction_manage_friend_attention_fans_public, null);
            myHolder.title_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.title_item_fragment_message_interaction_manage_friend_attention_fans_public);
            myHolder.data_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.data_item_fragment_message_interaction_manage_friend_attention_fans_public);
            myHolder.scoll_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.scoll_item_fragment_message_interaction_manage_friend_attention_fans_public);
            myHolder.pic_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.pic_item_fragment_message_interaction_manage_friend_attention_fans_public);
            myHolder.text_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.text_item_fragment_message_interaction_manage_friend_attention_fans_public);
            myHolder.delete_item_fragment_message_interaction_manage_friend_attention_fans_public = $.f(convertView, R.id.delete_item_fragment_message_interaction_manage_friend_attention_fans_public);
            convertView.setTag(myHolder);
        }else {
            myHolder= (MessageInteractionManageFriendAttentionFansPublicAdapterHolder) convertView.getTag();
        }
        if (!StringUtils.isEmpty(list.get(position).title)) {
            myHolder.title_item_fragment_message_interaction_manage_friend_attention_fans_public.setVisibility(View.VISIBLE);
            myHolder.title_item_fragment_message_interaction_manage_friend_attention_fans_public.setText(list.get(position).title);
            myHolder.data_item_fragment_message_interaction_manage_friend_attention_fans_public.setVisibility(View.GONE);
            myHolder.scoll_item_fragment_message_interaction_manage_friend_attention_fans_public.setSwipeEnable(false);
        } else {
            myHolder.title_item_fragment_message_interaction_manage_friend_attention_fans_public.setVisibility(View.GONE);
            myHolder.data_item_fragment_message_interaction_manage_friend_attention_fans_public.setVisibility(View.VISIBLE);
            myHolder.scoll_item_fragment_message_interaction_manage_friend_attention_fans_public.setSwipeEnable(true);
            if (loadImageCallBack != null) {
                myHolder.pic_item_fragment_message_interaction_manage_friend_attention_fans_public.setTag(R.id.glide_tag, Config.url + list.get(position).face);
                loadImageCallBack.onLoad(GlidUtils.downLoader(true, myHolder.pic_item_fragment_message_interaction_manage_friend_attention_fans_public, context));
            }
            myHolder.text_item_fragment_message_interaction_manage_friend_attention_fans_public.setText(list.get(position).username);
            if (messageInteractionManageFriendAttentionFansPublicClickCallBack != null) {
                myHolder.text_item_fragment_message_interaction_manage_friend_attention_fans_public.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageInteractionManageFriendAttentionFansPublicClickCallBack.onClick(position, list.get(position), list);
                    }
                });
                myHolder.delete_item_fragment_message_interaction_manage_friend_attention_fans_public.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageInteractionManageFriendAttentionFansPublicClickCallBack.onDelete(position, list.get(position), list);
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

}

class MessageInteractionManageFriendAttentionFansPublicAdapterHolder {

    public TextView title_item_fragment_message_interaction_manage_friend_attention_fans_public;
    public LinearLayout data_item_fragment_message_interaction_manage_friend_attention_fans_public;
    public SwipeMenuLayout scoll_item_fragment_message_interaction_manage_friend_attention_fans_public;
    public ImageView pic_item_fragment_message_interaction_manage_friend_attention_fans_public;
    public TextView text_item_fragment_message_interaction_manage_friend_attention_fans_public;
    public TextView delete_item_fragment_message_interaction_manage_friend_attention_fans_public;

}
