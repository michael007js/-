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
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.MessageChatListOperationCallBack;
import com.sss.car.model.MessageChatListModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/23.
 */

public class MessageChatListAdapter extends RecyclerView.Adapter<MessageChatListAdapterHolder>{
    MessageChatListOperationCallBack messageChatListOperationCallBack;
    Context context;
    List<MessageChatListModel> list;

    public void clear(){
        if (list!=null){
            list.clear();
        }
        list=null;
        context=null;
        messageChatListOperationCallBack=null;
    }

    public MessageChatListAdapter(MessageChatListOperationCallBack messageChatListOperationCallBack, Context context, List<MessageChatListModel> list) {
        this.messageChatListOperationCallBack = messageChatListOperationCallBack;
        this.context = context;
        this.list = list;
    }

    public void refresh( List<MessageChatListModel> list){
        this.list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public MessageChatListAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageChatListAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_message_chat_list_adapter,null));
    }

    @Override
    public void onBindViewHolder(MessageChatListAdapterHolder holder, final int position) {
        holder.name_item_message_chat_list_adapter.setText(list.get(position).username);
        holder.content_item_message_chat_list_adapter.setText(list.get(position).contents);
        holder.time_item_message_chat_list_adapter.setText(list.get(position).create_time);
        holder.pic_item_message_chat_list_adapter.setTag(R.id.glide_tag, Config.url+list.get(position).face);
        GlidUtils.downLoader(true,holder.pic_item_message_chat_list_adapter,context);
        if ("1".equals(list.get(position).is_remind)){
            holder.ring_item_message_chat_list_adapter.setVisibility(View.VISIBLE);
        }else {
            holder.ring_item_message_chat_list_adapter.setVisibility(View.INVISIBLE);
        }
        if (messageChatListOperationCallBack!=null){
            holder.click_item_message_chat_list_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageChatListOperationCallBack.onClickMessage(position,list.get(position),list);
                }
            });
            holder.click_item_message_chat_list_adapter.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    messageChatListOperationCallBack.onLongClickMessage(position,list.get(position),list);
                    return true;
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MessageChatListAdapterHolder extends RecyclerView.ViewHolder{
    LinearLayout click_item_message_chat_list_adapter;
    ImageView pic_item_message_chat_list_adapter;
    TextView name_item_message_chat_list_adapter;
    TextView time_item_message_chat_list_adapter;
    TextView content_item_message_chat_list_adapter;
    ImageView ring_item_message_chat_list_adapter;





    public MessageChatListAdapterHolder(View itemView) {
        super(itemView);
        click_item_message_chat_list_adapter= $.f(itemView, R.id.click_item_message_chat_list_adapter);
        pic_item_message_chat_list_adapter= $.f(itemView, R.id.pic_item_message_chat_list_adapter);
        name_item_message_chat_list_adapter= $.f(itemView, R.id.name_item_message_chat_list_adapter);
        time_item_message_chat_list_adapter= $.f(itemView, R.id.time_item_message_chat_list_adapter);
        content_item_message_chat_list_adapter= $.f(itemView, R.id.content_item_message_chat_list_adapter);
        ring_item_message_chat_list_adapter= $.f(itemView, R.id.ring_item_message_chat_list_adapter);
    }
}
