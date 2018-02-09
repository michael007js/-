package com.blankj.utilcode.MyRecycleview.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

/**
 * 作者：yuanYe创建于2016/11/1
 * QQ：962851730
 * recyclerView的上拉滑动监听
 */

public abstract class PullToLoading extends RecyclerView.OnScrollListener {



    /**
     * SCROLL_STATE_TOUCH_SCROLL#手接触ScrollView触发一次
     * 　　SCROLL_STATE_DRAGGING#正在滚动
     * 　　SCROLL_STATE_IDLE#滑动停止
     * @param recyclerView
     * @param newState
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //已经加载出来的item数量
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int total = recyclerView.getLayoutManager().getItemCount(); //item的总数
                int lastPosition = -1;
                if (manager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                } else if (manager instanceof GridLayoutManager) {
                    lastPosition = ((GridLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    int[] lastPs = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                    ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(lastPs);
                    lastPosition = findMax(lastPs);
                }
                if (lastPosition == total - 1 && (recyclerView.getChildCount() != total)) { //当最后一个位置等于总的item的个数的时候
                    Log.i("TAG", "已经到达底部");
//                    ((RecyclerViewHelper) recyclerView.getAdapter()).showPullToLoading();
                    loading(recyclerView);
                    //判断数据发生改变
                    showBottom();
                }else {
                    hideBottom();
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                hideBottom();
                break;
        }


    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }



    /**
     * 滑动到底部调用
     */
    public abstract void loading(RecyclerView recyclerView);

    /**
     * 滑动到底部调用
     */
    public abstract void showBottom( );

    /**
     * 滑动到底部调用
     */
    public abstract void hideBottom( );
}
