package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.sss.car.EventBusModel.ChangedDraftGoods;
import com.sss.car.EventBusModel.ChangedDraftOrder;
import com.sss.car.EventBusModel.ChangedPopularizeModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentDraftPublic;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/11/4.
 */

public class ActivityDraftMy extends BaseActivity {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.title_top_image)
    TextView titleTopImage;
    @BindView(R.id.logo_right_search_top_image)
    ImageView logoRightSearchTopImage;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.activity_draft_my)
    LinearLayout activityDraftMy;

    //底部导航栏文字
    String[] title = new String[]{"    订单    ", "    SOS    ", "    商品    ", "    推广    "};
    FragmentDraftPublic order, sos, goods, popularize;
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
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        backTopImage = null;
        titleTopImage = null;
        logoRightSearchTopImage = null;
        rightSearchTopImage = null;
        activityDraftMy = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedDraftOrder changedDraftOrder) {
        if (order != null) {
            order.p = 1;
            order.drafts_order();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPopularizeModel changedPopularizeModel) {
        if (popularize != null) {
            popularize.p = 1;
            popularize.drafts_popularize();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedDraftGoods changedDraftGoods) {
        if (goods != null) {
            goods.p = 1;
            goods.drafts_goods();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_my);
        ButterKnife.bind(this);
        customInit(activityDraftMy, false, true, true);
        titleTopImage.setText("我的草稿箱");
//        addImageViewList(GlidUtils.glideLoad(false, logoRightSearchTopImage, getBaseActivityContext(), R.mipmap.logo_search));
        viewpager.setOffscreenPageLimit(4);
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

        order = new FragmentDraftPublic(FragmentDraftPublic.REQUEST_MODE_ORDER, true);
        sos = new FragmentDraftPublic(FragmentDraftPublic.REQUEST_MODE_SOS, true);
        goods = new FragmentDraftPublic(FragmentDraftPublic.REQUEST_MODE_GOODS, true);
        popularize = new FragmentDraftPublic(FragmentDraftPublic.REQUEST_MODE_POPULARIZE, true);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);

        fragmentAdapter.addFragment(order);
        fragmentAdapter.addFragment(sos);
        fragmentAdapter.addFragment(goods);
        fragmentAdapter.addFragment(popularize);
        viewpager.setAdapter(fragmentAdapter);
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


    }

    @OnClick({R.id.back_top_image, R.id.right_search_top_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
//                switch (scrollTab.position) {
//                    case 0:
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), ActivityDraftSearch.class)
//                                    .putExtra("search_mode", FragmentDraftPublic.REQUEST_MODE_ORDER));
//                        }
//                        break;
//                    case 1:
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), ActivityDraftSearch.class)
//                                    .putExtra("search_mode", FragmentDraftPublic.REQUEST_MODE_SOS));
//                        }
//                        break;
//                    case 2:
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), ActivityDraftSearch.class)
//                                    .putExtra("search_mode", FragmentDraftPublic.REQUEST_MODE_GOODS));
//                        }
//                        break;
//                    case 3:
//                        if (getBaseActivityContext() != null) {
//                            startActivity(new Intent(getBaseActivityContext(), ActivityDraftSearch.class)
//                                    .putExtra("search_mode", FragmentDraftPublic.REQUEST_MODE_POPULARIZE));
//                        }
//                        break;
//                }
                break;
        }
    }


}
