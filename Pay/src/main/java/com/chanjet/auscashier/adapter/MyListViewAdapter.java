package com.chanjet.auscashier.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanjet.auscashier.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<String> mDataList;

    private MyListViewAdapter() {
    }

    public MyListViewAdapter(Activity activity, ArrayList<String> list) {
        mActivity = activity;
        mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView mHolderView;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_view_simple_item, null);
            mHolderView = new HolderView();
            mHolderView.tvMenu = (TextView) convertView.findViewById(R.id.tv_menu);
            convertView.setTag(mHolderView);
        } else
            mHolderView = (HolderView) convertView.getTag();

        mHolderView.tvMenu.setText(mDataList.get(position));
        return convertView;
    }

    private class HolderView {
        public TextView tvMenu;
    }
}
