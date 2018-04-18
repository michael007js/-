package com.sss.car.order;

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
import com.sss.car.EventBusModel.ChangedMessageOrderList;
import com.sss.car.EventBusModel.ChangedSOSList;
import com.sss.car.R;
import com.sss.car.fragment.FragmentOrderSOSSeller;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的==>订单==>sos求助订单
 * Created by leilei on 2017/10/14.
 */

public class OrderSOSMySeller extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.order_sos_my_seller)
    LinearLayout activitOrderSOSMySeller;
    FragmentAdapter fragmentAdapter;

    //底部导航栏文字
    String[] text = new String[]{"            未完成            ", "            已结束            "};


    FragmentOrderSOSSeller fragmentOrderSOSSellerUnfinish;

    FragmentOrderSOSSeller fragmentOrderSOSSellerComplete;
    @BindView(R.id.scrollTab)
    ScrollTab ScrollTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_my_seller);
        ButterKnife.bind(this);
        customInit(activitOrderSOSMySeller, false, true, true);
        titleTop.setText("SOS增援订单");


        fragmentOrderSOSSellerUnfinish = new FragmentOrderSOSSeller(FragmentOrderSOSSeller.FragmentOrderSOSSeller_Service_unFinish);
        fragmentOrderSOSSellerComplete = new FragmentOrderSOSSeller(FragmentOrderSOSSeller.FragmentOrderSOSSeller_Service_complete);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),text);
        fragmentAdapter.addFragment(fragmentOrderSOSSellerUnfinish);
        fragmentAdapter.addFragment(fragmentOrderSOSSellerComplete);
        ScrollTab.setTitles(Arrays.asList(text));
        ScrollTab.setViewPager(viewPager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewPager.setCurrentItem(position);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        viewPager.setAdapter(fragmentAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSOSList event) {
        fragmentOrderSOSSellerUnfinish.changeList(event.sos_id);
        fragmentOrderSOSSellerComplete.changeList(event.sos_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedMessageOrderList event) {
        fragmentOrderSOSSellerUnfinish.p = 1;
        fragmentOrderSOSSellerUnfinish.getSOSSellerList();
        fragmentOrderSOSSellerComplete.p = 1;
        fragmentOrderSOSSellerComplete.getSOSSellerList();
    }

    @Override
    protected void onDestroy() {
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;
        super.onDestroy();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

}
