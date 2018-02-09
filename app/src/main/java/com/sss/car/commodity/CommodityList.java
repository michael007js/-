package com.sss.car.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangedGoodsList;
import com.sss.car.EventBusModel.CommodityListChanged;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCommunityList;
import com.sss.car.view.ActivityShopInfoAllFilter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 我的商品==>在架商品列表/下架商品列表
 * Created by leilei on 2017/11/1.
 */

public class CommodityList extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.search_top_more)
    ImageView searchTopMore;
    @BindView(R.id.filter_commodity_list)
    LinearLayout filterCommodityList;
    @BindView(R.id.commodity_list)
    LinearLayout commodityList;
    String[] title = new String[]{"     车品     ", "     车服     "};


    FragmentAdapter fragmentAdapter;
    FragmentCommunityList goods, service;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        if (goods != null) {
            goods.onDestroy();
        }
        goods = null;
        if (service != null) {
            service.onDestroy();
        }
        service = null;
        backTop = null;
        titleTop = null;
        scrollTab = null;
        searchTopMore = null;
        filterCommodityList = null;
        commodityList = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_list);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递出错");
            finish();
        }

        ButterKnife.bind(this);
        if ("1".equals(getIntent().getExtras().getString("status"))) {
            titleTop.setText("在架商品");
        } else if ("0".equals(getIntent().getExtras().getString("status"))) {
            titleTop.setText("下架商品");
        }
        customInit(commodityList, false, true, true);


        goods = new FragmentCommunityList("1", getIntent().getExtras().getString("status"));
        service = new FragmentCommunityList("2", getIntent().getExtras().getString("status"));
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        fragmentAdapter.addFragment(goods);
        fragmentAdapter.addFragment(service);
        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpager);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
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
                scrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(2);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedGoodsList changedGoodsList) {
        if (goods != null) {
            goods.reRequest(null);
        }
        if (service != null) {
            service.reRequest(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CommodityListChanged commodityListChanged) {
        if (goods != null) {
            goods.reRequest(commodityListChanged.classify_id);
        }
        if (service != null) {
            service.reRequest(commodityListChanged.classify_id);
        }
    }

    @OnClick({R.id.back_top, R.id.filter_commodity_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.filter_commodity_list:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfoAllFilter.class)
                            .putExtra("mode", "publish_goods"));
                }
                break;
        }
    }

}
