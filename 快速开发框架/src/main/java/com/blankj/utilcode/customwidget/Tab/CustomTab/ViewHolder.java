package com.blankj.utilcode.customwidget.Tab.CustomTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by leilei on 2017/12/29.
 */

@SuppressWarnings("ALL")
public class ViewHolder {
    private final SparseArray<View> mViews = new SparseArray();
    private int mPosition;
    private View mConvertView;

    public ViewHolder(Context context, int layoutResID, ViewGroup parent) {
        this.mConvertView = LayoutInflater.from(context).inflate(layoutResID, parent, false);
        this.mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, int layoutResID, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder(context, layoutResID, parent);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mPosition = position;
        return holder;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = (View) this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.append(viewId, view);
        }

        return (T) view;
    }

    public ViewHolder setText(int id, CharSequence text) {
        TextView tv = (TextView) this.getView(id);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageBitmap(int id, Bitmap bm) {
        ImageView iv = (ImageView) this.getView(id);
        iv.setImageBitmap(bm);
        return this;
    }

    public ViewHolder setImageDrawable(int id, Drawable drawable) {
        ImageView iv = (ImageView) this.getView(id);
        iv.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageResource(int id, int resId) {
        ImageView iv = (ImageView) this.getView(id);
        iv.setImageResource(resId);
        return this;
    }



    public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
        this.getView(id).setOnClickListener(listener);
        return this;
    }

    public ViewHolder setTag(int id, Object obj) {
        this.getView(id).setTag(obj);
        return this;
    }
}