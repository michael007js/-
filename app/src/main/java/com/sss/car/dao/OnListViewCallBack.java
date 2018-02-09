package com.sss.car.dao;

import android.widget.ListView;

import com.blankj.utilcode.customwidget.ListView.InnerListview;

/**
 * Created by leilei on 2017/10/19.
 */

public interface OnListViewCallBack {
    void onListViewCallBack(InnerListview innerListview);

    void onListViewCallBack(ListView listView);
}
