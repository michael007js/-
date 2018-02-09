package com.blankj.utilcode.customwidget.ListView.StickyListHeadersListView;

/**
 * Created by leilei on 2017/6/1.
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public interface StickyListHeadersAdapter extends ListAdapter {
    View getHeaderView(int position, View convertView, ViewGroup parent);
    long getHeaderId(int position);
}