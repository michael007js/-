package com.blankj.utilcode.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by leilei on 2017/9/7.
 */

public class ViewPagerObjAdpter extends PagerAdapter {
    private List<View> mViews;
    public ViewPagerObjAdpter(List<View> mViews ) {
        super();
        this.mViews = mViews;
    }

    public void clear() {
        mViews = null;
    }

    @Override
    public int getCount() {

        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }
}
