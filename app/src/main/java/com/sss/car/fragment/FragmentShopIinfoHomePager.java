package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 店铺首页介绍
 * Created by leilei on 2017/9/17.
 */

@SuppressLint("ValidFragment")
public class FragmentShopIinfoHomePager extends BaseFragment {
    Unbinder unbinder;

    String contents;
    @BindView(R.id.content)
    TextView content;

    public FragmentShopIinfoHomePager(String contents) {
        this.contents = contents;
    }

    public void setContents(String contents){
        this.contents=contents;
        content.setText(contents);
    }

    public FragmentShopIinfoHomePager() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop_info_home_pager;
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
                                content.setText(contents);
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
        unbinder = null;
    }


}
