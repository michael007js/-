package com.blankj.utilcode.customwidget.Layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.ViewPager.CustomWrapWidthContentViewPager;


/**
 * Created by leilei on 2017/9/13.
 */

public class IndicatorPointLayout extends LinearLayout implements CustomWrapWidthContentViewPager.OnPageChangeListener {
    private int currentPosition;//当前选中位置
    private int itemHeight = 50;//item宽高
    private int itemMargin = 3;//item间距
    private int itemSelectedBgResId = R.drawable.indicator_point_layout_selected;
    private int itemDefaultBgResId = R.drawable.indicator_point_layout_default;

    public IndicatorPointLayout(Context context) {
        this(context, null, 0);
    }

    public IndicatorPointLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorPointLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }


    public void initViews(CustomWrapWidthContentViewPager viewPager) {
        viewPager.setOnPageChangeListener(this);
        removeAllViews();
        int count = viewPager.getAdapter().getCount();

        if (count == 0 || itemHeight == 0) {
            return;
        }
        View view = createView(itemHeight, itemMargin);
        view.setBackgroundResource(itemSelectedBgResId);
        addView(view);
        if (count == 1) {
            return;
        }
        for (int i = 1; i < count; i++) {
            view = createView(itemHeight, itemMargin);
            view.setBackgroundResource(itemDefaultBgResId);
            addView(view);
        }
    }


    public void initViews(int itemHeight, int itemMargin, CustomWrapWidthContentViewPager viewPager) {
        viewPager.setOnPageChangeListener(this);
        this.itemHeight = itemHeight;
        this.itemMargin = itemMargin;
        removeAllViews();
        int count = viewPager.getAdapter().getCount();
        if (count == 0 || itemHeight == 0) {
            return;
        }
        View view = createView(itemHeight, itemMargin);
        view.setBackgroundResource(itemSelectedBgResId);
        addView(view);
        if (count == 1) {
            return;
        }
        for (int i = 1; i < count; i++) {
            view = createView(itemHeight, itemMargin);
            view.setBackgroundResource(itemDefaultBgResId);
            addView(view);
        }
    }

    public void initViews(int itemHeight, int itemMargin, CustomWrapWidthContentViewPager viewPager, int itemSelectedBgResId, int itemDefaultBgResId) {
        viewPager.setOnPageChangeListener(this);
        this.itemSelectedBgResId = itemSelectedBgResId;
        this.itemDefaultBgResId = itemDefaultBgResId;
        this.itemHeight = itemHeight;
        this.itemMargin = itemMargin;
        removeAllViews();
        int count = viewPager.getAdapter().getCount();
        if (count == 0 || itemHeight == 0) {
            return;
        }
        View view = createView(itemHeight, itemMargin);
        view.setBackgroundResource(itemSelectedBgResId);
        addView(view);
        if (count == 1) {
            return;
        }
        for (int i = 1; i < count; i++) {
            view = createView(itemHeight, itemMargin);
            view.setBackgroundResource(itemDefaultBgResId);
            addView(view);
        }
    }

    /**
     * 创建view
     *
     * @param sideLength 边长
     * @param itemMargin 外间距
     * @return
     */
    public View createView(int sideLength, int itemMargin) {
        TextView textview = new TextView(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(sideLength, sideLength);
        if (itemMargin > 0) {
            params.setMargins(itemMargin, 0, itemMargin, 0);
        }
        textview.setLayoutParams(params);
        return textview;
    }

    //切换到目标位置
    public void changePosition(int position) {
        if (getChildCount() <= 1) {
            return;
        }
        getChildAt(currentPosition).setBackgroundResource(itemDefaultBgResId);
        currentPosition = position % getChildCount();
        getChildAt(currentPosition).setBackgroundResource(itemSelectedBgResId);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        changePosition(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
