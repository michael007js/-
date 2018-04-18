package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.MessageInteractionManageGroupClickCallBack;
import com.sss.car.model.MessageInteractionManageGroupModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/28.
 */

public class MessageInteractionManageGroupAdapter extends RecyclerView.Adapter<MessageInteractionManageGroupAdapterHolder> {
    List<MessageInteractionManageGroupModel> list;
    Context context;
    LoadImageCallBack loadImageCallBack;
    MessageInteractionManageGroupClickCallBack messageInteractionManageGroupClickCallBack;
    NineView.NineViewShowCallBack nineViewShowCallBack;

    public void refresh(List<MessageInteractionManageGroupModel> list) {
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
        messageInteractionManageGroupClickCallBack = null;
    }


    public MessageInteractionManageGroupAdapter(List<MessageInteractionManageGroupModel> list, Context context, NineView.NineViewShowCallBack nineViewShowCallBack, LoadImageCallBack loadImageCallBack, MessageInteractionManageGroupClickCallBack messageInteractionManageGroupClickCallBack) {
        this.list = list;
        this.context = context;
        this.nineViewShowCallBack = nineViewShowCallBack;
        this.loadImageCallBack = loadImageCallBack;
        this.messageInteractionManageGroupClickCallBack = messageInteractionManageGroupClickCallBack;
    }

    @Override
    public MessageInteractionManageGroupAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageInteractionManageGroupAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_fragment_message_interaction_manage_group, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageInteractionManageGroupAdapterHolder holder, final int position) {
        holder.text_item_fragment_message_interaction_manage_group.setText(list.get(position).name);
        if (loadImageCallBack != null) {
            holder.pic_item_fragment_message_interaction_manage_group.setTag(R.id.glide_tag, Config.url + list.get(position).picture);
            loadImageCallBack.onLoad(GlidUtils.downLoader(true, holder.pic_item_fragment_message_interaction_manage_group, context));
        }
        if (messageInteractionManageGroupClickCallBack != null) {
            holder.data_item_fragment_message_interaction_manage_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageInteractionManageGroupClickCallBack != null) {
                        messageInteractionManageGroupClickCallBack.onClickItem(position, list.get(position), list);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return  list.size();
        } else {
            return 0;
        }
    }
}

class MessageInteractionManageGroupAdapterHolder extends RecyclerView.ViewHolder {
    LinearLayout data_item_fragment_message_interaction_manage_group;
    ImageView pic_item_fragment_message_interaction_manage_group;
    TextView text_item_fragment_message_interaction_manage_group;


    public MessageInteractionManageGroupAdapterHolder(View itemView) {
        super(itemView);
        data_item_fragment_message_interaction_manage_group = $.f(itemView, R.id.data_item_fragment_message_interaction_manage_group);
        pic_item_fragment_message_interaction_manage_group = $.f(itemView, R.id.pic_item_fragment_message_interaction_manage_group);
        text_item_fragment_message_interaction_manage_group = $.f(itemView, R.id.text_item_fragment_message_interaction_manage_group);
    }
}
