package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.SearchAddFriendAdapterCallBack;
import com.sss.car.model.SearchAddFriendModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/31.
 */

public class SearchAddFriendFriendAdapter extends RecyclerView.Adapter<SearchAddFriendFriendAdapterHolder> {
    Context context;
    List<SearchAddFriendModel> list;
    SearchAddFriendAdapterCallBack searchAddFriendAdapterCallBack;

    public SearchAddFriendFriendAdapter(Context context, List<SearchAddFriendModel> list, SearchAddFriendAdapterCallBack searchAddFriendAdapterCallBack) {
        this.context = context;
        this.list = list;
        this.searchAddFriendAdapterCallBack = searchAddFriendAdapterCallBack;
    }

    public void refresh(List<SearchAddFriendModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        searchAddFriendAdapterCallBack = null;
        context = null;
    }

    @Override
    public SearchAddFriendFriendAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchAddFriendFriendAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_search_add_friend_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(SearchAddFriendFriendAdapterHolder holder, final int position) {
        FrescoUtils.showImage(false,40,40, Uri.parse(Config.url+list.get(position).face),holder.pic,99999);
        holder.name_item_search_add_friend_adapter.setText(list.get(position).username);
        holder.click_item_search_add_friend_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchAddFriendAdapterCallBack!=null){
                    searchAddFriendAdapterCallBack.onSelectItem(position,list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SearchAddFriendFriendAdapterHolder extends RecyclerView.ViewHolder {
    LinearLayout click_item_search_add_friend_adapter;
    TextView name_item_search_add_friend_adapter;
    SimpleDraweeView pic;

    public SearchAddFriendFriendAdapterHolder(View itemView) {
        super(itemView);
        pic=$.f(itemView,R.id.pic);
        click_item_search_add_friend_adapter= $.f(itemView, R.id.click_item_search_add_friend_adapter);
        name_item_search_add_friend_adapter= $.f(itemView, R.id.name_item_search_add_friend_adapter);
    }
}
