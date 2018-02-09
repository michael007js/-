package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.CreateGroupFriendAttentionFansRecentlyChatClickCallBack;
import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/29.
 */

public class CreateGroupFriendAttentionFansRecentlyChatPublicAdapter extends RecyclerView.Adapter<CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder> {
    List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list;
    Context context;
    LoadImageCallBack loadImageCallBack;
    CreateGroupFriendAttentionFansRecentlyChatClickCallBack createGroupFriendAttentionFansRecentlyChatClickCallBack;

    public CreateGroupFriendAttentionFansRecentlyChatPublicAdapter(List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list, Context context, LoadImageCallBack loadImageCallBack, CreateGroupFriendAttentionFansRecentlyChatClickCallBack createGroupFriendAttentionFansRecentlyChatClickCallBack) {
        this.list = list;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.createGroupFriendAttentionFansRecentlyChatClickCallBack = createGroupFriendAttentionFansRecentlyChatClickCallBack;
    }


    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        loadImageCallBack = null;
        createGroupFriendAttentionFansRecentlyChatClickCallBack = null;
    }

    public void refresh(List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }


    public void refreshPosition(List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list,int position,RecyclerView.ViewHolder holder){
        this.list = list;
        onBindViewHolder((CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder) holder,position);

    }

    @Override
    public CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_create_group_friend_attention_fans_recently_chat_public, parent, false));
    }

    @Override
    public void onBindViewHolder(final CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder holder, final int position) {
        holder.text_item_create_group_friend_attention_fans_recently_chat_public.setText(list.get(position).username);
        holder.cb_item_create_group_friend_attention_fans_recently_chat_public.setChecked(list.get(position).isChoose);
        if (createGroupFriendAttentionFansRecentlyChatClickCallBack != null) {
            holder.cb_item_create_group_friend_attention_fans_recently_chat_public.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    createGroupFriendAttentionFansRecentlyChatClickCallBack.clickChanged(isChecked, position, list.get(position),holder);
                }
            });
        }

        if (loadImageCallBack!=null){
            holder.pic_item_create_group_friend_attention_fans_recently_chat_public.setTag(R.id.glide_tag, Config.url+list.get(position).face);
            loadImageCallBack.onLoad(GlidUtils.downLoader(true,holder.pic_item_create_group_friend_attention_fans_recently_chat_public,context));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder extends RecyclerView.ViewHolder {

    LinearLayout click_item_create_group_friend_attention_fans_recently_chat_public;
    ImageView pic_item_create_group_friend_attention_fans_recently_chat_public;
    TextView text_item_create_group_friend_attention_fans_recently_chat_public;
    CheckBox cb_item_create_group_friend_attention_fans_recently_chat_public;


    public CreateGroupFriendAttentionFansRecentlyChatPublicAdapterHolder(View itemView) {
        super(itemView);
        click_item_create_group_friend_attention_fans_recently_chat_public = $.f(itemView, R.id.click_item_create_group_friend_attention_fans_recently_chat_public);
        pic_item_create_group_friend_attention_fans_recently_chat_public = $.f(itemView, R.id.pic_item_create_group_friend_attention_fans_recently_chat_public);
        text_item_create_group_friend_attention_fans_recently_chat_public = $.f(itemView, R.id.text_item_create_group_friend_attention_fans_recently_chat_public);
        cb_item_create_group_friend_attention_fans_recently_chat_public = $.f(itemView, R.id.cb_item_create_group_friend_attention_fans_recently_chat_public);
    }
}