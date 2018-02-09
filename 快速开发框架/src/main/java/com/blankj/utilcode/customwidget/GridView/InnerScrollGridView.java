package com.blankj.utilcode.customwidget.GridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ScrollView;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by leilei on 2017/10/27.
 */

public class InnerScrollGridView extends GridView {
    ScrollView scrollView;

    public void clear() {
        scrollView = null;
    }

    public InnerScrollGridView(Context context) {
        super(context);
    }

    public InnerScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch
                (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setParentScrollAble(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview    停住不能滚动
                LogUtils.e("onInterceptTouchEventdown");
            case MotionEvent.ACTION_MOVE:
                LogUtils.e("onInterceptTouchEvent move");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.e("onInterceptTouchEvent up");
            case MotionEvent.ACTION_CANCEL:
                LogUtils.e("onInterceptTouchEvent cancel");
                setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    private void setParentScrollAble(boolean flag) {
        if (scrollView != null) {
            scrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个
        }
    }
}
