package com.blankj.utilcode.customwidget.Tab.CustomTab;

import android.content.Context;
import android.graphics.PointF;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.Layout.IndicatorPointLayout;
import com.blankj.utilcode.util.$;

/**
 * Created by leilei on 2017/11/3.
 */

public class TAB extends LinearLayout {
    HorizontalPageGridView horizontalPageGridView;
    HorizontalPageListener horizontalPageListener;
    View view;
    IndicatorPointLayout pont;

    public TAB(Context context) {
        super(context);
    }

    public TAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHorizontalPageGridView(HorizontalPageGridView horizontalPageGridView) {
        this.horizontalPageGridView = horizontalPageGridView;
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
        void onSingleTouch(View v);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }


    /**
     * 定制功能
     *
     * @param context
     * @param isShowIndicatorPointLayout 是否显示下方圆点
     * @param numColumns                 设置每行显示数据的个数
     * @param pageSize                   设置每页显示数据的个数
     * @param heigth                     布局高度
     */
    public void init(Context context, boolean isShowIndicatorPointLayout, int numColumns, int pageSize, int heigth) {
        view = LayoutInflater.from(context).inflate(R.layout.tab, null);
        horizontalPageGridView = $.f(view, R.id.page_grid_view);
        pont = $.f(view, R.id.point);
        if (numColumns>0) {
            horizontalPageGridView.setNumColumns(numColumns);
        }
        if (pageSize >0) {
            horizontalPageGridView.setPageSize(pageSize);
        }
        if (heigth < 1) {
            heigth = 30;
        }
        if (!isShowIndicatorPointLayout){
            pont.setVisibility(GONE);
        }
        horizontalPageGridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heigth));
        horizontalPageGridView.setListener(horizontalPageListener);
        this.setOrientation(OrientationHelper.VERTICAL);
        this.setGravity(Gravity.CENTER);
        this.addView(view);


    }

    public void setAdapter(HorizontalPageAdapter horizontalPageAdapter) {
        horizontalPageGridView.setAdapter(horizontalPageAdapter);
        pont.initViews(horizontalPageGridView.getViewPager());
    }
}
