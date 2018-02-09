package com.blankj.utilcode.customwidget.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by leilei on 2017/8/31.
 */

public class RecyleAdapterLayout extends LinearLayout{
    RecyleAdapterLayoutCallBack recyleAdapterLayoutCallBack;

    RecyclerView.ViewHolder viewHolder;
    int postion;

    public void setPoistion(int postion) {
        this.postion = postion;
    }

    public void setViewHolder(RecyclerView.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void setRecyleAdapterLayoutCallBack(RecyleAdapterLayoutCallBack recyleAdapterLayoutCallBack) {
        this.recyleAdapterLayoutCallBack = recyleAdapterLayoutCallBack;
    }

    public RecyleAdapterLayout(Context context) {
        super(context);
    }

    public RecyleAdapterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyleAdapterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            if (recyleAdapterLayoutCallBack!=null){
                recyleAdapterLayoutCallBack.onVisibilityChanged(1,postion,viewHolder);
                LogUtils.e("v");
            }
            //开始某些任务
        } else if (visibility == INVISIBLE || visibility == GONE) {
            //停止某些任务
            if (recyleAdapterLayoutCallBack!=null){
                recyleAdapterLayoutCallBack.onVisibilityChanged(2,postion,viewHolder);
            }
        }

    }

    public interface  RecyleAdapterLayoutCallBack{
        void onVisibilityChanged(int visibility, int postion, RecyclerView.ViewHolder viewHolder);
    }
}
