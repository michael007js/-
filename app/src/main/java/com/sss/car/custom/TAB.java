package com.sss.car.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.customwidget.Layout.IndicatorPointLayout;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageAdapter;
import com.blankj.utilcode.customwidget.Tab.CustomTab.HorizontalPageGridView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.SizeUtils;
import com.sss.car.R;


/**
 * Created by leilei on 2017/9/7.
 */

public class TAB extends LinearLayout {
    HorizontalPageGridView horizontalPageGridView;
    View view;
    IndicatorPointLayout pont;

    public void clear() {
        if (horizontalPageGridView != null) {
            horizontalPageGridView.clear();
        }
        horizontalPageGridView = null;
        view = null;
        pont = null;
    }

    public TAB(Context context) {
        super(context);
        init(context);
    }

    public TAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    PointF downPoint = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下时候的坐标
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                if (this.getChildCount() > 1) { //有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (getParent().getParent() != null) {
                        getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.getChildCount() > 1) { //有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (getParent().getParent() != null) {
                        getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 在up时判断是否按下和松手的坐标为一个点
                if (PointF.length(evt.getX() - downPoint.x, evt.getY()
                        - downPoint.y) < (float) 5.0) {
                    onSingleTouch(this);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(evt);
    }


    public void onSingleTouch(View v) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(v);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(View v);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }


    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.tab, null);
        horizontalPageGridView = $.f(view, R.id.page_grid_view);
        pont = $.f(view, R.id.point);
        horizontalPageGridView.setNumColumns(5)// 设置每行显示数据的个数
                .setPageSize(5);//设置页数
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
        this.setOrientation(OrientationHelper.VERTICAL);
        this.setGravity(Gravity.CENTER);
        this.addView(view);
    }

    public void setAdapter(HorizontalPageAdapter horizontalPageAdapter, int count, Activity activity) {
        if (count < 6) {
            pont.setVisibility(GONE);
        }
        horizontalPageGridView.setAdapter(horizontalPageAdapter);
        pont.initViews(horizontalPageGridView.getViewPager());

    }
}
