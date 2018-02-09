package com.blankj.utilcode.customwidget.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leilei on 2017/9/10.
 */

public class ScollViewPagerInViewPagerOutter extends ViewPager{
    public ScollViewPagerInViewPagerOutter(Context context) {
        super(context);
    }

    public ScollViewPagerInViewPagerOutter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
        @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {

        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dx, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        if (checkV) {
            if (v instanceof ViewPager) {
                return ((ViewPager)v).canScrollHorizontally(-dx);
            } else {
                return ViewCompat.canScrollHorizontally(v, -dx);
            }
        } else {
            return false;
        }
    }
}
