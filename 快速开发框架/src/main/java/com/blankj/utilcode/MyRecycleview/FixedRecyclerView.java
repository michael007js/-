package com.blankj.utilcode.MyRecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 完美解决SwipeRefreshLayout与RecyclerView滑动冲突问题
 * Created by leilei on 2017/8/30.
 */

public class FixedRecyclerView extends RecyclerView {
    boolean mIsRefreshing;
    public FixedRecyclerView(Context context) {
        super(context);
        init();
    }

    public FixedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FixedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void isRefreshing(boolean isRefreshing) {
        this.mIsRefreshing = isRefreshing;
    }

    void init(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing){
                    return true;
                }else {
                    return false;
                }

            }
        });
    }
    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);

    }
}