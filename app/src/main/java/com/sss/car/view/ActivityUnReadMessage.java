package com.sss.car.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ViewPager.AutofitViewPager;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentMessageComment;
import com.sss.car.fragment.FragmentMessageOrder;
import com.sss.car.fragment.FragmentMessageSystem;
import com.sss.car.fragment.FragmentRongyunUnreadMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页==>信封==>未读消息
 * Created by leilei on 2017/12/14.
 */

public class ActivityUnReadMessage extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.tab_activity_un_read_message)
    ScrollTab ScrollTab;
    @BindView(R.id.viewpager_activity_un_read_message)
    AutofitViewPager viewpagerActivityUnReadMessage;
    @BindView(R.id.activity_un_read_message)
    LinearLayout activityUnReadMessage;
    FragmentAdapter fragmentAdapter;
    FragmentMessageSystem fragmentMessageSystem;

    FragmentRongyunUnreadMessage fragmentRongyunUnreadMessage;

    FragmentMessageComment fragmentMessageComment;

    FragmentMessageOrder fragmentMessageOrder;
    String[] title = {"     系统     ", "     互动   ", "     评价   ", "    交易    "};

    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 订单刷新
     *
     * @param changedOrderModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        if (fragmentMessageOrder != null) {
            if (fragmentMessageOrder.fragmentMessageOrderPublic != null) {
                fragmentMessageOrder.fragmentMessageOrderPublic.p = 1;
                fragmentMessageOrder.fragmentMessageOrderPublic.messageOrderGetOrderInfo();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        ScrollTab = null;
        viewpagerActivityUnReadMessage = null;
        activityUnReadMessage = null;
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        if (fragmentMessageSystem != null) {
            fragmentMessageSystem.onDestroy();
        }
        fragmentMessageSystem = null;
        if (fragmentRongyunUnreadMessage != null) {
            fragmentRongyunUnreadMessage.onDestroy();
        }
        fragmentRongyunUnreadMessage = null;

        if (fragmentMessageComment != null) {
            fragmentMessageComment.onDestroy();
        }
        fragmentMessageComment = null;
        if (fragmentMessageOrder != null) {
            fragmentMessageOrder.onDestroy();
        }
        fragmentMessageOrder = null;
        title = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_read_message);
        ButterKnife.bind(this);
        titleTop.setText("未读消息");
        customInit(activityUnReadMessage, false, true, true);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentMessageSystem = new FragmentMessageSystem("1", null, false, null);
        fragmentRongyunUnreadMessage = new FragmentRongyunUnreadMessage();
        fragmentMessageComment = new FragmentMessageComment("1", false, null);//	未读消息参数为1
        fragmentMessageOrder = new FragmentMessageOrder("1", false, null);//	未读消息参数为1

        fragmentAdapter.addFragment(fragmentMessageSystem);
        fragmentAdapter.addFragment(fragmentRongyunUnreadMessage);
        fragmentAdapter.addFragment(fragmentMessageComment);
        fragmentAdapter.addFragment(fragmentMessageOrder);

        viewpagerActivityUnReadMessage.setOffscreenPageLimit(4);
        viewpagerActivityUnReadMessage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        ScrollTab.setTitles(Arrays.asList(title));
        ScrollTab.setViewPager(viewpagerActivityUnReadMessage);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpagerActivityUnReadMessage.setCurrentItem(position);
            }
        });
        viewpagerActivityUnReadMessage.setAdapter(fragmentAdapter);

    }


    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
