package com.blankj.utilcode.customwidget.ViewPager;

/**
 * Created by leilei on 2017/12/1.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;


/**
 * 根据View的内容自动适应高度的ViewPager
 */
public class AutofitViewPager extends ViewPager {
    OnSizeChangedCallBack onSizeChangedCallBack;

    public void setOnSizeChangedCallBack(OnSizeChangedCallBack onSizeChangedCallBack) {
        this.onSizeChangedCallBack = onSizeChangedCallBack;
    }

    public AutofitViewPager(Context context) {
        this(context,null);
    }

    public AutofitViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        addOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                requestLayout();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }



//            @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        // find the current child view
//        if (getChildAt(getCurrentItem()) != null) {
//            // measure the current child view with the specified measure spec
//            getChildAt(getCurrentItem()).measure(widthMeasureSpec, heightMeasureSpec);
//        }
//
//        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, getChildAt(getCurrentItem())));
//    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    public interface OnSizeChangedCallBack{

        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

}