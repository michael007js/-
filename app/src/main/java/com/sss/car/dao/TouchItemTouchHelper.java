package com.sss.car.dao;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by leilei on 2017/8/21.
 */

public class TouchItemTouchHelper extends ItemTouchHelper.Callback {
    private int dragFlags;
    private int swipeFlags;
    TouchItemTouchHelperCallBack touchItemTouchHelperCallBack;

    public TouchItemTouchHelper(TouchItemTouchHelperCallBack touchItemTouchHelperCallBack) {
        this.touchItemTouchHelperCallBack = touchItemTouchHelperCallBack;
    }

    //viewHolder开始拖动
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    //viewHolder开始滑动
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }


    /**
     * 设置item是否处理拖拽事件和滑动事件，以及拖拽和滑动操作的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //判断 recyclerView的布局管理器数据
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {//设置能拖拽的方向
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;//0则不响应事件
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 当用户从item原来的位置拖动可以拖动的item到新位置的过程中调用
     *
     * @recyclerView
     * @viewHolder 拖动的 item
     * @target 目标 item
     **/
    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }


    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     *
     * @dx item 滑动的距离
     **/
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (touchItemTouchHelperCallBack != null) {
            touchItemTouchHelperCallBack.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        if (touchItemTouchHelperCallBack != null) {
            touchItemTouchHelperCallBack.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }


    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (touchItemTouchHelperCallBack != null) {
            touchItemTouchHelperCallBack.onSelectedChanged(viewHolder, actionState);
        }
    }


    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (touchItemTouchHelperCallBack != null) {
            touchItemTouchHelperCallBack.clearView(recyclerView, viewHolder);
        }
    }
}
