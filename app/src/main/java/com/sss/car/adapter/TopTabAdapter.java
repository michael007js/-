package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.model.TopTabModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/11.
 */

public class TopTabAdapter extends BaseAdapter{
    Context context;
    List<TopTabModel> list;
    int selectPosition=0;

    public TopTabAdapter(Context context, List<TopTabModel> list) {
        this.context = context;
        this.list = list;
    }
    public void refrtesh(int selectPosition, List<TopTabModel> list){
        this.list=list;
        this.selectPosition=selectPosition;
        this.notifyDataSetChanged();
    }
    public void refrtesh(int selectPosition){
        this.selectPosition=selectPosition;
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
        TopTabAdapterHolder topTabAdapterHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_top_tab,null);
            topTabAdapterHolder=new TopTabAdapterHolder();
            topTabAdapterHolder.click_item_top_tab= $.f(convertView,R.id.click_item_top_tab);
            topTabAdapterHolder.name_item_top_tab= $.f(convertView,R.id.name_item_top_tab);
            topTabAdapterHolder.line_item_top_tab= $.f(convertView,R.id.line_item_top_tab);
            convertView.setTag(topTabAdapterHolder);
        }else {
            topTabAdapterHolder= (TopTabAdapterHolder) convertView.getTag();
        }
        topTabAdapterHolder.name_item_top_tab.setText(list.get(position).name);
        if (position==selectPosition){
            topTabAdapterHolder.name_item_top_tab.setTextColor(context.getResources().getColor(R.color.mainColor));
            topTabAdapterHolder.line_item_top_tab.setBackgroundColor(context.getResources().getColor(R.color.mainColor));
        }else {
            topTabAdapterHolder.name_item_top_tab.setTextColor(context.getResources().getColor(R.color.black));
            topTabAdapterHolder.line_item_top_tab.setBackgroundColor(0);
        }
        return convertView;
    }
}
class TopTabAdapterHolder {
    LinearLayout click_item_top_tab;
    TextView name_item_top_tab;
    TextView line_item_top_tab;

}
