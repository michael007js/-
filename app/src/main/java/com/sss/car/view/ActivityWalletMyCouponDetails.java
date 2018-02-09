package com.sss.car.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.EventBusModel.CouponChange;
import com.sss.car.R;
import com.sss.car.fragment.FragmentWalletCashDiscountFullCut;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的==>钱包==>优惠券==>明细
 * Created by leilei on 2017/10/26.
 */

public class ActivityWalletMyCouponDetails extends BaseActivity implements  FragmentWalletCashDiscountFullCut.OnSelectUserCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.NavMenuLayout_wallet_my_coupon_details)
    ScrollTab ScrollTab;
    @BindView(R.id.parent_wallet_my_coupon_details)
    ViewPager viewpager;
    @BindView(R.id.wallet_my_coupon_details)
    LinearLayout walletMyCouponDetails;

    FragmentWalletCashDiscountFullCut cash;
    FragmentWalletCashDiscountFullCut discount;
    FragmentWalletCashDiscountFullCut full_cut;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (cash!=null){
            cash.onDestroy();
        }
        cash=null;
        if (discount!=null){
            discount.onDestroy();
        }
        discount=null;
        if (full_cut!=null){
            full_cut.onDestroy();
        }
        full_cut=null;
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;
        backTop = null;
        titleTop = null;
        ScrollTab = null;
        viewpager = null;
        walletMyCouponDetails = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_my_coupon_details);
        ButterKnife.bind(this);
        titleTop.setText("优惠券");
        customInit(walletMyCouponDetails, false, true, true);
        //底部导航栏文字
        String[] text = new String[]{"      现金券      ", "      折扣券      ", "      满减券      "};
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager());
        cash = new FragmentWalletCashDiscountFullCut("2", this);//	1满减券，2现金券，3折扣券
        discount = new FragmentWalletCashDiscountFullCut("3", this);//	1满减券，2现金券，3折扣券
        full_cut = new FragmentWalletCashDiscountFullCut("1", this);//	1满减券，2现金券，3折扣券
        fragmentAdapter.addFragment(cash);
        fragmentAdapter.addFragment(discount);
        fragmentAdapter.addFragment(full_cut);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ScrollTab.setTitles(Arrays.asList(text));
        ScrollTab.setViewPager(viewpager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });

        viewpager.setAdapter(fragmentAdapter);
        viewpager.setOffscreenPageLimit(3);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CouponChange couponChange) {
        if (cash != null) {
            cash.p = 1;
            cash.walletGetCouponDetails();
        }
        if (discount != null) {
            discount.p = 1;
            discount.walletGetCouponDetails();
        }
        if (full_cut != null) {
            full_cut.p = 1;
            full_cut.walletGetCouponDetails();
        }
    }


    @Override
    public void onSelectUserCallBack(String member_id, String face, String username, String type) {

    }
}
