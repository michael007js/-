package com.sss.car.dao;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * Created by leilei on 2017/8/22.
 */

public interface TouchItemTouchHelperCallBack {



    /**
     * RecyclerView调用onDraw时调用，如果想自定义item对用户互动的响应,可以重写该方法
     *
     * @dx item 滑动的距离
     **/

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);


    /**
     * 设置手指离开后ViewHolder的动画时间，在用户手指离开后调用
     */

    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy);


    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);


    /**
     * 当用户与item的交互结束并且item也完成了动画时调用
     */

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
}
