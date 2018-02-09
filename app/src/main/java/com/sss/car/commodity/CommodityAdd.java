package com.sss.car.commodity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ViewPager.CustomCacheViewPager;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.EventBusModel.CommodityAddSizeModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCommodity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的商品==>添加商品
 * Created by leilei on 2017/10/26.
 */

public class CommodityAdd extends BaseActivity  {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.add_commodity_add)
    TextView addCommodityAdd;
    @BindView(R.id.commodity_add)
    LinearLayout commodityAdd;
    //底部导航栏文字
    String[] text = new String[]{"                车服                ", "                车品                "};
    FragmentCommodity goods, service;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    String type;
    @BindView(R.id.scrollTab)
    ScrollTab ScrollTab;
    @BindView(R.id.parent_commodity_add)
    ViewPager viewpager;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        addCommodityAdd = null;
        commodityAdd = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_add);
        ButterKnife.bind(this);
        customInit(commodityAdd, false, true, true);
        titleTop.setText("添加商品");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
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
        type = "2";
        service = new FragmentCommodity(type, FragmentCommodity.LAUNCH_MODE_PUBLISH);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), text);
        goods = new FragmentCommodity(type, FragmentCommodity.LAUNCH_MODE_PUBLISH);
        fragmentAdapter.addFragment(service);
        fragmentAdapter.addFragment(goods);
        ScrollTab.setTitles(Arrays.asList(text));
        ScrollTab.setViewPager(viewpager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });
        viewpager.setAdapter(fragmentAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        type = "2";
                        break;
                    case 1:
                        type = "1";
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.back_top, R.id.add_commodity_add, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.add_commodity_add:
                if ("1".equals(type)) {
                    if (goods != null) {
                        goods.addAndUpdate();
                    }
                } else if ("2".equals(type)) {
                    if (service != null) {
                        service.addAndUpdate();
                    }
                }
                break;
            case R.id.right_button_top:
                if ("1".equals(type)) {
                    if (goods != null) {
                        goods.save_goods();
                    }
                } else if ("2".equals(type)) {
                    if (service != null) {
                        service.save_goods();
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CommodityAddSizeModel commodityAddSize) {
        if ("1".equals(commodityAddSize.type)) {
            goods.setData(commodityAddSize.sizeName, commodityAddSize.sizeData);
        } else if ("2".equals(commodityAddSize.type)) {
            service.setData(commodityAddSize.sizeName, commodityAddSize.sizeData);
        }
    }


}
