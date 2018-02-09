package com.sss.car.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.EventBusModel.ChangedSOSList;
import com.sss.car.R;
import com.sss.car.fragment.FragmentOrderSOSSeller;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/10/14.
 */

public class ActivityOrderSOS extends BaseActivity implements NavMenuLayout.OnItemSelectedListener {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.NavMenuLayout_activity_order_sos)
    NavMenuLayout NavMenuLayoutActivityOrderSos;
    @BindView(R.id.parent_activity_order_sos)
    FrameLayout parentActivityOrderSos;
    @BindView(R.id.activity_order_sos)
    LinearLayout activityOrderSos;

    //底部导航栏文字
    String[] text = new String[]{"未完成", "已完成"};
    //底部导航栏未选中图标
    int[] unSelectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //底部导航栏选中图标
    int[] selectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //底部导航栏未选中颜色
    String unSelectColor = "#333333";
    //底部导航栏选中颜色
    String selectColor = "#df3347";


    FragmentOrderSOSSeller fragmentOrderSOSUnfinished;

    FragmentOrderSOSSeller fragmentOrderSOSComplete;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sos);
        ButterKnife.bind(this);
        customInit(activityOrderSos, false, true, true);
        titleTop.setText("SOS订单");

        NavMenuLayoutActivityOrderSos
                .initMenu(2)//初始化布局
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

//        if (fragmentOrderSOSUnfinished == null) {
//            fragmentOrderSOSUnfinished = new FragmentOrderSOSSeller(FragmentOrderSOSSeller.FragmentOrderSOSSeller_Service_unFinish, FragmentOrderSOSSeller.FragmentOrderSOSSeller_rescuer);
//            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderSOSUnfinished, R.id.parent_activity_order_sos);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedSOSList event) {
        fragmentOrderSOSUnfinished.changeList(event.sos_id);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {
//        switch (position) {
//            case 0:
//                if (fragmentOrderSOSUnfinished == null) {
//                    fragmentOrderSOSUnfinished = new FragmentOrderSOSSeller(FragmentOrderSOSSeller.FragmentOrderSOSSeller_Service_unFinish, FragmentOrderSOSSeller.FragmentOrderSOSSeller_rescuer);
//                    FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderSOSUnfinished, R.id.parent_activity_order_sos);
//                }
//                FragmentUtils.hideAllShowFragment(fragmentOrderSOSUnfinished);
//                break;
//            case 1:
//                if (fragmentOrderSOSComplete == null) {
//                    fragmentOrderSOSComplete = new FragmentOrderSOSSeller(FragmentOrderSOSSeller.FragmentOrderSOSSeller_Service_complete, FragmentOrderSOSSeller.FragmentOrderSOSSeller_rescuer);
//                    FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrderSOSComplete, R.id.parent_activity_order_sos);
//                }
//                FragmentUtils.hideAllShowFragment(fragmentOrderSOSComplete);
//                break;
//        }

    }
}
