package com.sss.car.order_new;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支出订单
 * Created by leilei on 2017/11/9.
 */

public class NewOrderBuyer extends BaseActivity  {

    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.new_order_seller)
    LinearLayout newOrderSeller;
    YWLoadingDialog ywLoadingDialog;

    NewOrderFragmentBuyer payment, receive, complete, returns;
    @BindView(R.id.ScrollTab_new_order_seller)
    ScrollTab ScrollTabNewOrderSeller;
    @BindView(R.id.viewpager_new_order_seller)
    ViewPager viewpagerNewOrderSeller;
    FragmentAdapter fragmentAdapter;
    String[] title = {
            "待付款",
            "待完成",
            "已完成",
            "退换货",
    };

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        if (payment != null) {
            payment.refresh();
        }
        if (receive != null) {
            receive.refresh();
        }
        if (complete != null) {
            complete.refresh();
        }
        if (returns != null) {
            returns.refresh();
        }

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;
        title = null;
        backTop = null;
        titleTop = null;
        newOrderSeller = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_seller);
        ButterKnife.bind(this);
        titleTop.setText("支出订单");
        customInit(newOrderSeller, false, true, true);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        payment = new NewOrderFragmentBuyer(NewOrderFragmentBuyer.Type_Payment, getBaseActivity());
        receive = new NewOrderFragmentBuyer(NewOrderFragmentBuyer.Type_Reveived, getBaseActivity());
        complete = new NewOrderFragmentBuyer(NewOrderFragmentBuyer.Type_Complete, getBaseActivity());
        returns = new NewOrderFragmentBuyer(NewOrderFragmentBuyer.Type_Returns, getBaseActivity());
        fragmentAdapter.addFragment(payment);
        fragmentAdapter.addFragment(receive);
        fragmentAdapter.addFragment(complete);
        fragmentAdapter.addFragment(returns);
        viewpagerNewOrderSeller.setOffscreenPageLimit(4);
        viewpagerNewOrderSeller.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTabNewOrderSeller.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ScrollTabNewOrderSeller.setTitles(Arrays.asList(title));
        ScrollTabNewOrderSeller.setWidth(getWindowManager().getDefaultDisplay().getWidth()/4);
        ScrollTabNewOrderSeller.setViewPager(viewpagerNewOrderSeller);
        ScrollTabNewOrderSeller.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpagerNewOrderSeller.setCurrentItem(position);
            }
        });
        viewpagerNewOrderSeller.setAdapter(fragmentAdapter);

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }



}
