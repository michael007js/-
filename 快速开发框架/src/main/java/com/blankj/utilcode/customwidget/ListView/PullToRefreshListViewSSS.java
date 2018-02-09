package com.blankj.utilcode.customwidget.ListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.util.$;



/**
 * Created by leilei on 2017/9/5.
 */

public class PullToRefreshListViewSSS extends RelativeLayout{

    View view, viewBottom;
    RefreshLoadMoreLayout refresh;
    ListView listview;
    TextView bottom;
    PullToRefreshListViewSSSCallBack pullToRefreshListViewSSSCallBack;

    public ListView getlistview() {
        return listview;
    }

    public TextView getTextView() {
        return bottom;
    }

    public PullToRefreshListViewSSS(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListViewSSS(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListViewSSS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_listview_sss, null);
        refresh = $.f(view, R.id.refresh);
        listview = $.f(view, R.id.listview);
        viewBottom = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_listview_sss_bottom, null);
        bottom = $.f(viewBottom, R.id.bottom);

    }

    public void setPullToRefreshListViewSSSCallBack(PullToRefreshListViewSSSCallBack pullToRefreshListViewSSSCallBack) {
        this.pullToRefreshListViewSSSCallBack = pullToRefreshListViewSSSCallBack;
    }

    public void addHeadView(View view) {
        listview.addFooterView(view);
    }

    public void addFooterView(View view) {
        listview.addFooterView(view);
    }

    public void addFooterView(PullToRefreshListViewSSSCallBack pullToRefreshListViewSSSCallBack) {
        this.pullToRefreshListViewSSSCallBack = pullToRefreshListViewSSSCallBack;
        listview.addFooterView(bottom);
        if (pullToRefreshListViewSSSCallBack != null) {
            pullToRefreshListViewSSSCallBack.onAddFooter(bottom, listview);
        }
    }

    public void init(RefreshLoadMoreLayout.Config config) {
        refresh.init(config);
    }

    public void complete() {
        refresh.stopLoadMore();
        refresh.stopRefresh();
    }

    public void setAdapter(ListAdapter adapter) {
        listview.setAdapter(adapter);
    }

    public interface PullToRefreshListViewSSSCallBack {
        void onAddFooter(TextView textView, ListView listView);

    }

}
