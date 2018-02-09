package com.sss.car.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.fragment.FragmentOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/10/8.
 */

public class ActivityOrderReturnsChange extends BaseFragmentActivity implements NavMenuLayout.OnItemSelectedListener {
    /*底部导航栏文字*/
    String[] text = new String[]{"待处理", "退货", "换货", "已退款"};
    /*底部导航栏未选中图标*/
    int[] unSelectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏选中图标*/
    int[] selectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏未选中颜色*/
    String unSelectColor = "#333333";
    /*底部导航栏选中颜色*/
    String selectColor = "#df3347";
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.navMenuLayout_activity_order_returns_change)
    NavMenuLayout navMenuLayoutActivityOrderReturnsChange;
    @BindView(R.id.parent_activity_order_returns_change)
    FrameLayout parentActivityOrderReturnsChange;
    @BindView(R.id.activity_order_returns_change)
    LinearLayout activityOrderReturnsChange;

    String status;

    FragmentOrder fragmentOrderDispose;

    FragmentOrder fragmentOrderReturn;

    FragmentOrder fragmentOrderChange;

    FragmentOrder fragmentOrderComplete;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        text = null;
        unSelectIcon = null;
        selectIcon = null;
        unSelectColor = null;
        selectColor = null;
        backTop = null;
        titleTop = null;
        navMenuLayoutActivityOrderReturnsChange = null;
        parentActivityOrderReturnsChange = null;
        activityOrderReturnsChange = null;
        if (fragmentOrderDispose != null) {
            fragmentOrderDispose.onDestroy();
        }
        fragmentOrderDispose = null;
        if (fragmentOrderReturn != null) {
            fragmentOrderReturn.onDestroy();
        }
        fragmentOrderReturn = null;
        if (fragmentOrderChange != null) {
            fragmentOrderChange.onDestroy();
        }
        fragmentOrderChange = null;
        if (fragmentOrderComplete != null) {
            fragmentOrderComplete.onDestroy();
        }
        fragmentOrderComplete = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_returns_change);
        ButterKnife.bind(this);
        customInit(activityOrderReturnsChange, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            finish();
        }
        titleTop.setText(getIntent().getExtras().getString("title"));
        navMenuLayoutActivityOrderReturnsChange
                .initMenu(4)//初始化布局
                .setIconRes(unSelectIcon)//设置未选中图标
                .setIconResSelected(selectIcon)//设置选中图标
                .setTextRes(text)//设置文字
                .setUnderhigh(5)//下方横线的高度
                .setIconSize(60, 60)//设置图标大小
                .setTextSize(13)//设置文字大小
                .setIconIsShow(false)//设置图标是否显示
                .setTextIsShow(true)//设置文字是否显示
                .setTextColor(Color.parseColor(unSelectColor))//未选中状态下文字颜色
                .setTextColorSelected(Color.parseColor(selectColor))//选中状态下文字颜色
                .setUnderIsShow(true)//是否显示下方横线
                .setUnderWidthOffset(10)//下方横线的偏移量
                .setDefaultUnderWidth(52)//下方横线的初始宽度
//                .setBackColor(Color.WHITE)//设置背景色
//                .setBackColor(1,Color.RED)//设置背景色
//                .setMarginTop(PixelUtil.dpToPx(Main.this, 5))//文字和图标直接的距离，默认为5dp
                .setSelected(0)//设置选中的位置
                .setOnItemSelectedListener(this);

        status = "6";
        if (FragmentOrder.INCOME == getIntent().getExtras().getInt("what")) {
            if (fragmentOrderDispose == null) {
                fragmentOrderDispose = new FragmentOrder(
                        status,
                        ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER,
                        getIntent().getExtras().getInt("what"),
                        false,
                        true
                        );
                FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderDispose, R.id.parent_activity_order_returns_change);
            }
        } else {
            if (fragmentOrderDispose == null) {
                fragmentOrderDispose = new FragmentOrder(
                        status,
                        ListViewOrder.TYPE_WAITING_FOR_DISPOSE_NORMAL_FROM_BUY,
                        getIntent().getExtras().getInt("what"),
                        false,
                        true);
                FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderDispose, R.id.parent_activity_order_returns_change);
            }
        }
        FragmentUtils.hideAllShowFragment(fragmentOrderDispose);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case 0:
                status = "6";
                if (FragmentOrder.INCOME == getIntent().getExtras().getInt("what")) {
                    if (fragmentOrderDispose == null) {
                        fragmentOrderDispose = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_SELLER,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderDispose, R.id.parent_activity_order_returns_change);
                    }
                } else {
                    if (fragmentOrderDispose == null) {
                        fragmentOrderDispose = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_DISPOSE_RETURN_CHANGE_FROM_BUY,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderDispose, R.id.parent_activity_order_returns_change);
                    }
                }
                FragmentUtils.hideAllShowFragment(fragmentOrderDispose);
                break;
            case 1:
                status = "7";
                if (FragmentOrder.INCOME == getIntent().getExtras().getInt("what")) {
                    if (fragmentOrderReturn == null) {
                        fragmentOrderReturn = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_RETURNS_FROMSELLER,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderReturn, R.id.parent_activity_order_returns_change);
                    }
                } else {
                    if (fragmentOrderReturn == null) {
                        fragmentOrderReturn = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_RETURNS_FROM_BUY,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderReturn, R.id.parent_activity_order_returns_change);
                    }
                }
                FragmentUtils.hideAllShowFragment(fragmentOrderReturn);
                break;
            case 2:
                status = "8";
                if (FragmentOrder.INCOME == getIntent().getExtras().getInt("what")) {
                    if (fragmentOrderChange == null) {
                        fragmentOrderChange = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_CHANGE_FROMSELLER,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderChange, R.id.parent_activity_order_returns_change);
                    }
                } else {
                    if (fragmentOrderChange == null) {
                        fragmentOrderChange = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_CHANGE_FROM_BUY,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderChange, R.id.parent_activity_order_returns_change);
                    }
                }
                FragmentUtils.hideAllShowFragment(fragmentOrderChange);
                break;
            case 3:
                status = "9";
                if (FragmentOrder.INCOME == getIntent().getExtras().getInt("what")) {
                    if (fragmentOrderComplete == null) {
                        fragmentOrderComplete = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_SELLER,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderComplete, R.id.parent_activity_order_returns_change);
                    }
                } else {
                    if (fragmentOrderComplete == null) {
                        fragmentOrderComplete = new FragmentOrder(
                                status,
                                ListViewOrder.TYPE_WAITING_FOR_COMPLETE_RETURN_CHANGE_FROM_BUY,
                                getIntent().getExtras().getInt("what"),
                                false,
                                true);
                        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderComplete, R.id.parent_activity_order_returns_change);
                    }
                }
                FragmentUtils.hideAllShowFragment(fragmentOrderComplete);
                break;
        }
    }
}
