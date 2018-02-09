package com.blankj.utilcode.customwidget.ViewPager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import static com.blankj.utilcode.R.mipmap.xx;

/**
 * 解决嵌套到viewpager中无法滑动的问题
 * Created by leilei on 2017/9/10.
 */

public class ScollViewPagerInViewPagerInner extends ViewPager{
    public ScollViewPagerInViewPagerInner(Context context) {
        super(context);
    }

    public ScollViewPagerInViewPagerInner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private float mLastX;
    private float deltX;
    private float mLastY;
    private float deltY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean result = super.onInterceptTouchEvent(arg0);

        float x = arg0.getRawX();
        float y = arg0.getRawY();
        switch (arg0.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                //不要拦截down 只要move时候拦截即可，这样不会影响点击事件
                break;
            case MotionEvent.ACTION_MOVE:
                deltX = x - mLastX;
                deltY = y - mLastY;
                mLastX = x;
                mLastY = y;
                if (result = needIntercepter()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //        Trace.d("chuan", this.getClass().getSimpleName()
        //                + " onInterceptTouchEvent arg0=" + arg0.getAction()
        //                + " result=" + result + " deltX=" + deltX + " deltY=" + deltY);
        return result;
    }

    private boolean needIntercepter() {
        if (Math.abs(deltX) < Math.abs(deltY)) {
            return false;
        }
        if (deltX < 0 && getCurrentItem() == 0) {
            return true;
        } else if (deltX > 0 && getCurrentItem() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        boolean result = false;
        switch (arg0.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //不要直接对down事件处理，否则就不会有点击事件
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                result = super.onTouchEvent(arg0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //自动滑动内容
                result = super.onTouchEvent(arg0);
                break;
            default:
                break;
        }
        //        Trace.d("chuan", this.getClass().getSimpleName()
        //                + " onTouchEvent arg0=" + arg0.getAction() + " result="
        //                + result);
        return result;
    }

}
