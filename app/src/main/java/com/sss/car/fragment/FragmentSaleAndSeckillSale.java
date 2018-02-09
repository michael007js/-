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
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.R;
import com.sss.car.model.ListViewShowGoodsServiceModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 淘秒杀界面
 * Created by leilei on 2017/9/21.
 */

@SuppressLint("ValidFragment")
public class FragmentSaleAndSeckillSale extends BaseFragment {
    @BindView(R.id.hot_fragment_sale_and_seckill_sale)
    TextView hotFragmentSaleAndSeckillSale;
    @BindView(R.id.ing_fragment_sale_and_seckill_sale)
    TextView ingFragmentSaleAndSeckillSale;
    @BindView(R.id.will_fragment_sale_and_seckill_sale)
    TextView willFragmentSaleAndSeckillSale;
    Unbinder unbinder;

    List<ListViewShowGoodsServiceModel> list = new ArrayList<>();


    String classify_id;
    String type;

    FragmentSaleAndSeckillSaleHot fragmentSaleAndSeckillSaleHot;

    FragmentSaleAndSeckillSaleNow fragmentSaleAndSeckillSaleNow;

    FragmentSaleAndSeckillSaleLater fragmentSaleAndSeckillSaleLater;
    @BindView(R.id.parent_fragment_sale_and_seckill_sale)
    ViewPager parentFragmentSaleAndSeckillSale;
    FragmentAdapter fragmentAdapter;


    public FragmentSaleAndSeckillSale(String classify_id, String type) {
        this.classify_id = classify_id;
        this.type = type;
    }

    public FragmentSaleAndSeckillSale() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_sale_and_seckill_sale;
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
                                fragmentAdapter=new FragmentAdapter(getChildFragmentManager());
                                fragmentSaleAndSeckillSaleHot = new FragmentSaleAndSeckillSaleHot(classify_id, type);
                                fragmentSaleAndSeckillSaleNow = new FragmentSaleAndSeckillSaleNow(classify_id, type);
                                fragmentSaleAndSeckillSaleLater = new FragmentSaleAndSeckillSaleLater(classify_id, type);
                                fragmentAdapter.addFragment(fragmentSaleAndSeckillSaleHot);
                                fragmentAdapter.addFragment(fragmentSaleAndSeckillSaleNow);
                                fragmentAdapter.addFragment(fragmentSaleAndSeckillSaleLater);
                                parentFragmentSaleAndSeckillSale.setAdapter(fragmentAdapter);
                                parentFragmentSaleAndSeckillSale.setOffscreenPageLimit(3);
                                parentFragmentSaleAndSeckillSale.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    public void onDestroy() {
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;parentFragmentSaleAndSeckillSale=null;
        if (fragmentSaleAndSeckillSaleHot!=null){
            fragmentSaleAndSeckillSaleHot.onDestroy();
        }
        fragmentSaleAndSeckillSaleHot=null;
        if (fragmentSaleAndSeckillSaleLater!=null){
            fragmentSaleAndSeckillSaleLater.onDestroy();
        }
        fragmentSaleAndSeckillSaleLater=null;
        if (fragmentSaleAndSeckillSaleNow!=null){
            fragmentSaleAndSeckillSaleNow.onDestroy();
        }
        fragmentSaleAndSeckillSaleNow=null;
        hotFragmentSaleAndSeckillSale = null;
        ingFragmentSaleAndSeckillSale = null;
        willFragmentSaleAndSeckillSale = null;
        classify_id = null;
        super.onDestroy();
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
        unbinder = null;
    }

    @OnClick({R.id.hot_fragment_sale_and_seckill_sale, R.id.ing_fragment_sale_and_seckill_sale, R.id.will_fragment_sale_and_seckill_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hot_fragment_sale_and_seckill_sale:
                changed(0);
                parentFragmentSaleAndSeckillSale.setCurrentItem(0);
                break;
            case R.id.ing_fragment_sale_and_seckill_sale:
                changed(1);
                parentFragmentSaleAndSeckillSale.setCurrentItem(1);

                break;
            case R.id.will_fragment_sale_and_seckill_sale:
                changed(2);
                parentFragmentSaleAndSeckillSale.setCurrentItem(2);
                break;
        }
    }

    void changed(int position){
        switch (position) {
            case 0:
                hotFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.mainColor));
                ingFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                willFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                parentFragmentSaleAndSeckillSale.setCurrentItem(0);
                break;
            case 1:
                hotFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                ingFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.mainColor));
                willFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                parentFragmentSaleAndSeckillSale.setCurrentItem(1);

                break;
            case 2:
                hotFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                ingFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.black_dark));
                willFragmentSaleAndSeckillSale.setBackgroundColor(getResources().getColor(R.color.mainColor));
                parentFragmentSaleAndSeckillSale.setCurrentItem(2);
                break;
        }
    }

}
