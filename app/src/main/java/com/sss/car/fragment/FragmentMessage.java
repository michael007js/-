package com.sss.car.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.Config;
import com.sss.car.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 消息(已改版,此模块不再使用)
 * Created by leilei on 2017/8/23.
 */

public class FragmentMessage extends BaseFragment {
//    @BindView(R.id.NavMenuLayout_fragment_message)
//    NavMenuLayout NavMenuLayoutFragmentMessage;
    Unbinder unbinder;

    /*互动*/
    public FragmentMessageInteraction fragmentMessageInteraction;
    /*评价*/
    public   FragmentMessageComment fragmentMessageComment;
    /*订单*/
    public  FragmentMessageOrder fragmentMessageOrder;
    @BindView(R.id.location_path_left_location)
    public TextView locationPathLeftLocation;
    @BindView(R.id.search_path_left_location)
    ImageView searchPathLeftLocation;
    @BindView(R.id.right_menu)
    ImageView rightMenu;
//    @BindView(R.id.parent_fragment_message)
//    FrameLayout parentFragmentMessage;

    public FragmentMessage() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void lazyLoad() {
//        if (fragmentMessageInteraction == null) {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        sleep(300);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (!StringUtils.isEmpty(Config.city)) {
//                                    if (locationPathLeftLocation != null) {
//                                        locationPathLeftLocation.setText(Config.city);
//                                    }
//                                }
//                                fragmentMessageInteraction = new FragmentMessageInteraction();
//                                FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageInteraction, R.id.parent_fragment_message);
//                                NavMenuLayoutFragmentMessage
//                                        .initMenu(4)//初始化布局
//                                        .setIconRes(new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher})//设置未选中图标
//                                        .setIconResSelected(new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher})//设置选中图标
//                                        .setTextRes(new String[]{"互动", "评价", "订单", "系统"})//设置文字
//                                        .setIconSize(60, 60)//设置图标大小
//                                        .setTextSize(13)//设置文字大小
//                                        .setIconIsShow(false)//设置图标是否显示
//                                        .setTextIsShow(true)//设置文字是否显示
//                                        .setTextColor(Color.parseColor("#333333"))//未选中状态下文字颜色
//                                        .setTextColorSelected(Color.parseColor("#df3347"))//选中状态下文字颜色
//                                        .setUnderIsShow(true)//是否显示下方横线
//                                        .setUnderhigh(4)//下方横线的高度
//                                        .setUnderWidthOffset(10)//下方横线的偏移量
//                                        .setDefaultUnderWidth(52)//下方横线的初始宽度
////                .setBackColor(Color.WHITE)//设置背景色
////                .setBackColor(1,Color.RED)//设置背景色
////                .setMarginTop(PixelUtil.dpToPx(Main.this, 5))//文字和图标直接的距离，默认为5dp
//                                        .setSelected(0)//设置选中的位置
//                                        .setOnItemSelectedListener(new NavMenuLayout.OnItemSelectedListener() {
//                                            @Override
//                                            public void onItemSelected(int position) {
//                                                switch (position) {
//                                                    case 0:
//                                                        if (fragmentMessageInteraction==null){
//                                                            fragmentMessageInteraction = new FragmentMessageInteraction();
//                                                            FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageInteraction, R.id.parent_fragment_message);
//                                                        }
//                                                        FragmentUtils.hideAllShowFragment(fragmentMessageInteraction);
//                                                        break;
//                                                    case 1:
//                                                        if (fragmentMessageComment==null){
//                                                            fragmentMessageComment = new FragmentMessageComment();
//                                                            FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageComment, R.id.parent_fragment_message);
//                                                        }
//                                                        FragmentUtils.hideAllShowFragment(fragmentMessageComment);
//                                                        break;
//                                                    case 2:
//                                                        if (fragmentMessageOrder==null){
//                                                            fragmentMessageOrder = new FragmentMessageOrder();
//                                                            FragmentUtils.addFragment(getChildFragmentManager(), fragmentMessageOrder, R.id.parent_fragment_message);
//                                                        }
//                                                        FragmentUtils.hideAllShowFragment(fragmentMessageOrder);
//                                                        break;
//                                                }
//                                            }
//                                        });
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }
    }

    @Override
    protected void stopLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.location_path_left_location, R.id.search_path_left_location, R.id.right_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location_path_left_location:

                break;
            case R.id.search_path_left_location:
                break;
            case R.id.right_menu:
                break;
        }
    }
}
