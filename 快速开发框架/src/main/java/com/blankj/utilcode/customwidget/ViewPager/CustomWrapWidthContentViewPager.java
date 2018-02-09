package com.blankj.utilcode.customwidget.ViewPager;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CustomWrapWidthContentViewPager extends android.support.v4.view.ViewPager{
    //private static final String tag=ViewPager.class.getSimpleName();
//    protected ViewPager viewPager=null;
//    protected final int wrap_content=-2;
//    protected final int match_parent=-1;
//    protected int w  =match_parent;
//    protected int h   =match_parent;
//    protected int ph =match_parent;
//    protected int pw =match_parent;

    public CustomWrapWidthContentViewPager(Context context) {
        super(context);
//        viewPager=this;
    }
    public CustomWrapWidthContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        viewPager=this;
    }
//    @Override
//    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
//        super.onLayout(arg0, arg1, arg2, arg3, arg4);
//        ViewGroup.LayoutParams params =null;
//        ph=viewPager.getLayoutParams().height;
//        pw=viewPager.getLayoutParams().width;
//
//        Drawable  bg=  viewPager.getBackground();
//        if (bg!=null) {
//            int h = bg.getIntrinsicHeight();
//            int w= bg.getIntrinsicWidth();
//            if (ph==wrap_content) {
//                this.h=h;
//            }else if( ph==match_parent) {
//                this.h=ph;
//            }
//
//            if (pw==wrap_content) {
//                this.w=w;
//            }else if( pw==match_parent) {
//                this.w=pw;
//            }
//        }
//        ViewParent pv=viewPager.getParent();
//        if (pv instanceof LinearLayout) {
//            params = new LinearLayout.LayoutParams(this.w, this.h);
//        }else if (pv instanceof RelativeLayout) {
//            params = new RelativeLayout.LayoutParams(this.w, this.h);
//        }else if (pv instanceof FrameLayout) {
//            params = new FrameLayout.LayoutParams(this.w, this.h);
//        }
//        viewPager.setLayoutParams(params);
//    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int height = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height)
//                height = h;
//        }
//        int width = 0;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec);
//            int h = child.getMeasuredWidth();
//            if (h > width)
//                width = h;
//        }
//LogUtils.e(height+"---"+width);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }


}