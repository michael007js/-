package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.EventBusModel.SearchModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentSearchGoodsShopUser;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/13.
 */

@SuppressWarnings("ALL")
public class ActivitySearchGoodsShopUser extends BaseActivity {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.tip)
    EditText tip;
    @BindView(R.id.scrollTab)
    ScrollTab ScrollTab;
    @BindView(R.id.parent_activity_search_goods_shop_user)
    ViewPager viewpager;
    @BindView(R.id.activity_search_goods_shop_user)
    LinearLayout activitySearchGoodsShopUser;
    FragmentAdapter fragmentAdapter;

    FragmentSearchGoodsShopUser goods, shop, user;
    String type = "1";//1商品信息，2店铺信息，3账户信息
    @BindView(R.id.search)
    SimpleDraweeView search;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(SearchModel searchModel) {
        if ("1".equals(searchModel.type)) {
            if (goods != null) {
                goods.search_into();
            }
        } else if ("2".equals(searchModel.type)) {
            if (shop != null) {
                shop.search_into();
            }
        } else if ("3".equals(searchModel.type)) {
            if (user != null) {
                user.search_into();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods_shop_user);
        ButterKnife.bind(this);
        customInit(activitySearchGoodsShopUser, false, true, true);


        String[] title = {
                "      商品      ",
                "      商铺      ",
                "      用户      ",
        };
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        goods = new FragmentSearchGoodsShopUser("goods");
        shop = new FragmentSearchGoodsShopUser("shop");
        user = new FragmentSearchGoodsShopUser("account");
        fragmentAdapter.addFragment(goods);
        fragmentAdapter.addFragment(shop);
        fragmentAdapter.addFragment(user);
        ScrollTab.setTitles(Arrays.asList(title));
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
                ScrollTab.onPageSelected(position);
                switch (position) {
                    case 0:
                        tip.setHint("商品名称/商品编码");
                        type = "1";
                        break;
                    case 1:
                        tip.setHint("商铺名称");
                        type = "2";
                        break;
                    case 2:
                        tip.setHint("用户ID");
                        type = "3";
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(tip.getText().toString().trim())) {
                    startActivity(new Intent(getBaseActivityContext(), ActivitySearchGoodsShopUserListPublic.class)
                            .putExtra("type", type)
                            .putExtra("keywords", tip.getText().toString().trim()));
                }
            }
        });
//        tip.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        tip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (!"".equals(tip.getText().toString().trim())) {
//                        startActivity(new Intent(getBaseActivityContext(), ActivitySearchGoodsShopUserListPublic.class)
//                                .putExtra("type", type)
//                                .putExtra("keywords", tip.getText().toString().trim()));
//                    }
//                }
//                return true;
//
//            }
//
//        });
    }

    @OnClick(R.id.back_top_image)
    public void onViewClicked() {
        finish();
    }
}
