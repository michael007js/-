package com.sss.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.sss.car.R;
import com.sss.car.view.ActivityMyDataCarAdd;
import com.sss.car.view.ActivityMyDataCarCarList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by leilei on 2017/8/16.
 */

public class FragmentMyDataCarNull extends BaseFragment {

    @BindView(R.id.add_activity_find_password_by_account)
    TextView addActivityFindPasswordByAccount;
    Unbinder unbinder;

    @Override
    protected int setContentView() {
        return R.layout.fragment_my_data_car_null;
    }

    @Override
    protected void lazyLoad() {
    }

    public FragmentMyDataCarNull() {
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

    @OnClick(R.id.add_activity_find_password_by_account)
    public void onViewClicked() {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataCarAdd.class));
        }
    }
}
