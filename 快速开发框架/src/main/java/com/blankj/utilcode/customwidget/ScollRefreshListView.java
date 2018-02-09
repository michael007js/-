package com.blankj.utilcode.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.customwidget.Layout.LayoutCanRefresh.google.GoogleCircleHookRefreshView;

/**
 * Created by leilei on 2017/8/12.
 */

public class ScollRefreshListView extends LinearLayout {
    ListView mListView;
    GoogleCircleHookRefreshView mGoogleCircleHookRefreshViewHead;
    GoogleCircleHookRefreshView mGoogleCircleHookRefreshViewFoot;
    ScollRefreshListViewScollEventCallBack mScollRefreshListViewScollEventCallBack;
    boolean isScollToBottom = true;
    boolean isScollToTop = false;


    public void setmScollRefreshListViewScollEventCallBack(ScollRefreshListViewScollEventCallBack mScollRefreshListViewScollEventCallBack) {
        this.mScollRefreshListViewScollEventCallBack = mScollRefreshListViewScollEventCallBack;
    }

    public ScollRefreshListView(Context context) {
        super(context);
        initViews(context);
    }

    public ScollRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ScollRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    /**
     * 初始化视图
     *
     * @param context
     */
    void initViews(Context context) {
        this.setGravity(Gravity.CENTER);
        mListView = new ListView(context);
        this.addView(mListView);
        mGoogleCircleHookRefreshViewHead = new GoogleCircleHookRefreshView(context);
        mGoogleCircleHookRefreshViewHead.setVisibility(View.GONE);
        mListView.addHeaderView(mGoogleCircleHookRefreshViewHead);
        mGoogleCircleHookRefreshViewFoot = new GoogleCircleHookRefreshView(context);
        mGoogleCircleHookRefreshViewFoot.setVisibility(View.GONE);
        mListView.addFooterView(mGoogleCircleHookRefreshViewFoot);
        initListener();
    }

    void initListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        scollEvent();
                        if (mScollRefreshListViewScollEventCallBack != null) {
                            mScollRefreshListViewScollEventCallBack.onSCROLL_STATE_IDLE();//滚动停止时的状态
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        if (isScollToTop){
                           refresh();
                        }
                        if (isScollToBottom){
                            loadMore();
                        }
                        if (mScollRefreshListViewScollEventCallBack != null) {
                            mScollRefreshListViewScollEventCallBack.onSCROLL_STATE_STOUCH_SCROLL();//触摸正在滚动，手指还没离开界面时的状态
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        if (mScollRefreshListViewScollEventCallBack != null) {
                            mScollRefreshListViewScollEventCallBack.onSCROLL_STATE_FLING();//用户在用力滑动后，ListView由于惯性将继续滑动时的状态
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mScollRefreshListViewScollEventCallBack != null) {
                    mScollRefreshListViewScollEventCallBack.onSCOLL(view, firstVisibleItem, visibleItemCount, totalItemCount); //正在滑动
                }
            }
        });
    }


    void scollEvent() {
        // 判断滚动到底部
        if (mListView != null) {
            if (mListView.getLastVisiblePosition() + 1 == (mListView.getCount() - 1)) {
                isScollToBottom = true;
            } else {
                isScollToBottom = false;
            }
            // 判断滚动到顶部
            if (mListView.getFirstVisiblePosition() == 0) {
                isScollToBottom = false;
                isScollToTop = true;
            } else {
                isScollToTop = false;
            }
        }
    }

    public void clear() {
        this.removeAllViews();
        mListView.clearAnimation();
        mListView.removeAllViews();
        mListView = null;
    }

    /**
     * 分割线
     *
     * @param divider
     */
    public LinearLayout divder(Drawable divider) {
        mListView.setDivider(divider);
        return this;
    }

    /**
     * 获取列表
     *
     * @return
     */
    public ListView getListView() {
        return mListView;
    }


    public void refresh() {
        if (mGoogleCircleHookRefreshViewHead != null) {
            mGoogleCircleHookRefreshViewHead.setVisibility(VISIBLE);
        }
    }

    public void loadMore() {
        if (mGoogleCircleHookRefreshViewFoot != null) {
            mGoogleCircleHookRefreshViewFoot.setVisibility(VISIBLE);
        }
    }

    public void complete() {
        if (mGoogleCircleHookRefreshViewHead != null) {
            mGoogleCircleHookRefreshViewHead.setVisibility(GONE);
        }
        if (mGoogleCircleHookRefreshViewFoot != null) {
            mGoogleCircleHookRefreshViewFoot.setVisibility(GONE);
        }
    }

    public interface ScollRefreshListViewScollEventCallBack {
        void onSCROLL_STATE_IDLE();//滚动停止时的状态

        void onSCROLL_STATE_STOUCH_SCROLL();//触摸正在滚动，手指还没离开界面时的状态

        void onSCROLL_STATE_FLING();//用户在用力滑动后，ListView由于惯性将继续滑动时的状态

        void onSCOLL(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount); //正在滑动
    }


}
