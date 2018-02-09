package com.blankj.utilcode.customwidget.Tab.CustomTab;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;


import com.blankj.utilcode.customwidget.ViewPager.CustomWrapWidthContentViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/12/29.
 */


@SuppressWarnings("ALL")
public class HorizontalPageGridView<T> extends LinearLayout {
    private Context context;
    private CustomWrapWidthContentViewPager viewPager;
    private List<View> viewList;
    private int mPageSize;
    private int mNumColumns;
    private View view;
    private HorizontalPageAdapter mAdapter;
    private HorizontalPageListener listener;

    public void clear() {
        context = null;
        viewPager = null;
        if (viewList != null) {
            viewList.clear();
        }
        viewList = null;
        view = null;
        if (mAdapter != null) {
            mAdapter.clear();
        }
        mAdapter = null;
        listener = null;
    }

    public void setListener(HorizontalPageListener l) {
        this.listener = l;
    }

    public HorizontalPageGridView(Context context) {
        this(context, (AttributeSet) null);
        this.init(context);
    }

    public HorizontalPageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPageSize = 4;
        this.mNumColumns = 4;
        this.init(context);
    }

    public HorizontalPageGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPageSize = 4;
        this.mNumColumns = 4;
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.viewPager = new CustomWrapWidthContentViewPager(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.viewPager.setLayoutParams(lp);
        this.addView(this.viewPager);
    }

    public void setAdapter(HorizontalPageAdapter adapter) {
        this.mAdapter = adapter;
        int totalPage = (int) Math.ceil((double) this.mAdapter.getData().size() * 1.0D / (double) this.mPageSize);
        this.viewList = new ArrayList();

        for (int i = 0; i < totalPage; ++i) {
            GridView gridView = new GridView(this.context);
            gridView.setNumColumns(this.mNumColumns);
            gridView.setLayoutParams(new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.mAdapter = (HorizontalPageAdapter) adapter.clone();
            this.mAdapter.setIndex(i);
            this.mAdapter.setPagesize(this.mPageSize);
            gridView.setAdapter(this.mAdapter);
            this.viewList.add(gridView);
            this.mAdapter.setOnItemListener(new HorizontalPageListener() {
                public void onItemClickListener(Object o, int pos) {
                    if (HorizontalPageGridView.this.listener != null) {
                        HorizontalPageGridView.this.listener.onItemClickListener(o, pos);
                    }

                }
            });
        }

        this.viewPager.setAdapter(new HorizontalPageGridView.ViewPagerAdapter(this.viewList));
    }

    public HorizontalPageGridView setPageSize(int pageSize) {
        this.mPageSize = pageSize;
        return this;
    }

    public HorizontalPageGridView setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
        return this;
    }

    public CustomWrapWidthContentViewPager getViewPager() {
        if (this.mAdapter != null && this.mAdapter.getData().size() > 0) {
            return this.viewPager;
        } else {
            throw new RuntimeException("适配器数据异常");
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<View> viewList;

        public ViewPagerAdapter(List<View> list) {
            this.viewList = list;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) this.viewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView((View) this.viewList.get(position));
            return this.viewList.get(position);
        }

        public int getCount() {
            return this.viewList != null ? this.viewList.size() : 0;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
