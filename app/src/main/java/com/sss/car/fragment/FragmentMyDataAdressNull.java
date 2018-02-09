package com.sss.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.sss.car.R;
import com.sss.car.view.ActivityMyDataAdressAddAndEditAdressPublic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by leilei on 2017/8/18.
 */

public class FragmentMyDataAdressNull extends BaseFragment {
    @BindView(R.id.add_fragment_adress_null)
    LinearLayout addFragmentAdressNull;
    Unbinder unbinder;

    public FragmentMyDataAdressNull() {
    }

    @Override
    public void onDestroy() {
        addFragmentAdressNull = null;
        unbinder = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mydata_adress_null;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {

        }
    }

    @Override
    protected void stopLoad() {
        clearRequestCall();
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

    @OnClick(R.id.add_fragment_adress_null)
    public void onViewClicked() {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataAdressAddAndEditAdressPublic.class)
                    .putExtra("mode", "add")
                    .putExtra("from", "FragmentMyDataAdressNull"));
        }
    }
}
