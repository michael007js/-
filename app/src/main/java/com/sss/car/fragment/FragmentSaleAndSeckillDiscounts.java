package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.sss.car.R;
import com.sss.car.model.SaleAndSeckillDiscountsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 淘优惠界面
 * Created by leilei on 2017/9/22.
 */

@SuppressWarnings("deprecation")
@SuppressLint("ValidFragment")
public class FragmentSaleAndSeckillDiscounts extends BaseFragment {

    FragmentCouponSaleSeckillDiscounts cutFragment, subFragment, cashFragment;
    List<SaleAndSeckillDiscountsModel> list = new ArrayList<>();
    @BindView(R.id.cut)
    TextView cut;
    @BindView(R.id.sub)
    TextView sub;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.parent)
    ViewPager parent;
    Unbinder unbinder;
    String type;
    FragmentAdapter fragmentAdapter;

    public FragmentSaleAndSeckillDiscounts() {
    }

    public FragmentSaleAndSeckillDiscounts(String type) {
        this.type = type;
    }

    @Override
    public void onDestroy() {
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        parent = null;
        if (cutFragment != null) {
            cutFragment.onDestroy();
        }
        cutFragment = null;
        if (subFragment != null) {
            subFragment.onDestroy();
        }
        subFragment = null;
        if (cashFragment != null) {
            cashFragment.onDestroy();
        }
        cashFragment = null;
        super.onDestroy();

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_sale_and_seckill_discounts;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
                                cutFragment = new FragmentCouponSaleSeckillDiscounts(type, "3");
                                subFragment = new FragmentCouponSaleSeckillDiscounts(type, "1");
                                cashFragment = new FragmentCouponSaleSeckillDiscounts(type, "2");
                                fragmentAdapter.addFragment(cutFragment);
                                fragmentAdapter.addFragment(subFragment);
                                fragmentAdapter.addFragment(cashFragment);
                                parent.setAdapter(fragmentAdapter);
                                parent.setOffscreenPageLimit(3);
                                parent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        changed(position);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
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

    @OnClick({R.id.cut, R.id.sub, R.id.cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cut:
                changed(0);
                parent.setCurrentItem(0);
                break;
            case R.id.sub:
                changed(1);
                parent.setCurrentItem(1);
                break;
            case R.id.cash:
                changed(2);
                parent.setCurrentItem(2);
                break;
        }
    }


    void changed(int position) {
        switch (position) {
            case 0:
                cut.setBackgroundColor(getResources().getColor(R.color.mainColor));
                sub.setBackgroundColor(getResources().getColor(R.color.black_dark));
                cash.setBackgroundColor(getResources().getColor(R.color.black_dark));

                break;
            case 1:
                cut.setBackgroundColor(getResources().getColor(R.color.black_dark));
                sub.setBackgroundColor(getResources().getColor(R.color.mainColor));
                cash.setBackgroundColor(getResources().getColor(R.color.black_dark));

                break;
            case 2:
                cut.setBackgroundColor(getResources().getColor(R.color.black_dark));
                sub.setBackgroundColor(getResources().getColor(R.color.black_dark));
                cash.setBackgroundColor(getResources().getColor(R.color.mainColor));
                break;
        }
    }
}
