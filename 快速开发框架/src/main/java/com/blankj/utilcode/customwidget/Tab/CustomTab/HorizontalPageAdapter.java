package com.blankj.utilcode.customwidget.Tab.CustomTab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * Created by leilei on 2017/12/29.
 */


public abstract class HorizontalPageAdapter<T> extends BaseAdapter implements Cloneable {
    private Context mContext;
    private List<T> data;
    private int resId;
    private int mIndex;
    private int mPagesize;
    private HorizontalPageListener itemListener;
    public void clear(){
        mContext=null;
        if (data!=null){
            data.clear();
        }
        data=null;itemListener=null;
    }

    public void setOnItemListener(HorizontalPageListener l) {
        this.itemListener = l;
    }

    public HorizontalPageAdapter(Context context, List<T> data, int resId) {
        this.mContext = context;
        this.data = data;
        this.resId = resId;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public List<T> getData() {
        return this.data;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public void setPagesize(int pageSize) {
        this.mPagesize = pageSize;
    }

    public int getCount() {
        return this.data.size() > (this.mIndex + 1) * this.mPagesize?this.mPagesize:this.data.size() - this.mIndex * this.mPagesize;
    }

    public T getItem(int position) {
        return this.data.get(position + this.mIndex * this.mPagesize);
    }

    public long getItemId(int position) {
        return (long)(position + this.mIndex * this.mPagesize);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(this.mContext, this.resId, position, convertView, parent);
        final int pos = position + this.mIndex * this.mPagesize;
        this.bindViews(holder, this.data.get(pos), pos);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(HorizontalPageAdapter.this.itemListener != null) {
                    HorizontalPageAdapter.this.itemListener.onItemClickListener(HorizontalPageAdapter.this.data.get(pos), pos);
                }

            }
        });
        return holder.getConvertView();
    }

    public abstract void bindViews(ViewHolder var1, T var2, int var3);

    public Object clone() {
        HorizontalPageAdapter adapter = null;

        try {
            adapter = (HorizontalPageAdapter)super.clone();
        } catch (CloneNotSupportedException var3) {
            var3.printStackTrace();
        }

        return adapter;
    }
}
