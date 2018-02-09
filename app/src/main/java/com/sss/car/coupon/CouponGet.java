package com.sss.car.coupon;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCouponGet;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * 我的优惠券==>领取优惠券
 * Created by leilei on 2017/11/2.
 */

public class CouponGet extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.coupon_get)
    LinearLayout couponGet;

    String[] title = new String[]{"        折扣券        ", "        满减券        ", "        现金券        "};

    FragmentCouponGet one, two,three;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;
        viewpager=null;
        backTop = null;
        titleTop = null;
        scrollTab = null;
        couponGet = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_get);
        ButterKnife.bind(this);
        customInit(couponGet, false, true, false);
        titleTop.setText("加入优惠券活动");
        one = new FragmentCouponGet("3");
        two = new FragmentCouponGet("1");
        three = new FragmentCouponGet("2");
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),title);
        fragmentAdapter.addFragment(one);
        fragmentAdapter.addFragment(two);
        fragmentAdapter.addFragment(three);
        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpager);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setAdapter(fragmentAdapter);
        viewpager.setOffscreenPageLimit(3);

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

}
