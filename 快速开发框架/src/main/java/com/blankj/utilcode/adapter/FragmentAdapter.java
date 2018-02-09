package com.blankj.utilcode.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blankj.utilcode.Fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/6/13.
 */

/**
 * Fragment适配器
 * Created by wcy on 2015/11/26.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    public Fragment getFragment(int position) {
        if (mFragments.size() - 1 > position) {
            return mFragments.get(position);
        }
        return null;
    }


    public List<Fragment> getmFragments() {
        return mFragments;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void clear() {
        mFragments.clear();
        mTitles = null;
    }
    public void clearFragment() {
        mFragments.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null) {
            return null;
        }
        return mTitles[position];
    }
}
