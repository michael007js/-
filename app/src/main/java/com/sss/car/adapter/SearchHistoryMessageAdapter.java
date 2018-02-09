package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.dao.SearchHistoryMessageOperationCallBack;
import com.sss.car.model.SearchHistoryMessageModel;

import java.util.List;

import io.rong.imlib.model.Message;

/**
 * Created by leilei on 2017/8/30.
 */

public class SearchHistoryMessageAdapter extends RecyclerView.Adapter<SearchHistoryMessageAdapterHolder> {
    Context context;
    List<SearchHistoryMessageModel> list;
    SearchHistoryMessageOperationCallBack searchHistoryMessageOperationCallBack;

    public SearchHistoryMessageAdapter(Context context, List<SearchHistoryMessageModel> list, SearchHistoryMessageOperationCallBack searchHistoryMessageOperationCallBack) {
        this.context = context;
        this.list = list;
        this.searchHistoryMessageOperationCallBack = searchHistoryMessageOperationCallBack;
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public void refresh(List<SearchHistoryMessageModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public SearchHistoryMessageAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHistoryMessageAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_search_history_message_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHistoryMessageAdapterHolder holder, final int position) {
        holder.text_item_search_history_message_adapter.setText(list.get(position).content);

        if (searchHistoryMessageOperationCallBack != null) {
            holder.click_item_search_history_message_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchHistoryMessageOperationCallBack.onClickHistroyMessage(position, list);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SearchHistoryMessageAdapterHolder extends RecyclerView.ViewHolder {
    LinearLayout click_item_search_history_message_adapter;
    TextView text_item_search_history_message_adapter;

    public SearchHistoryMessageAdapterHolder(View itemView) {
        super(itemView);
        click_item_search_history_message_adapter = $.f(itemView, R.id.click_item_search_history_message_adapter);
        text_item_search_history_message_adapter = $.f(itemView, R.id.text_item_search_history_message_adapter);
    }
}
