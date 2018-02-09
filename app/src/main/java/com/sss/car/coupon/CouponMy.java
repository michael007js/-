package com.sss.car.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  我的优惠券
 * Created by leilei on 2017/11/2.
 */

public class CouponMy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.click_get_coupon_my)
    LinearLayout clickGetCouponMy;
    @BindView(R.id.click_manager_coupon_my)
    LinearLayout clickManagerCouponMy;
    @BindView(R.id.coupon_my)
    LinearLayout couponMy;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        clickGetCouponMy = null;
        clickManagerCouponMy = null;
        couponMy = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_my);
        ButterKnife.bind(this);
        titleTop.setText("优惠券管理");
        customInit(couponMy,false,true,false);
    }

    @OnClick({R.id.back_top, R.id.click_get_coupon_my, R.id.click_manager_coupon_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_get_coupon_my:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),CouponGet.class));
                }
                break;
            case R.id.click_manager_coupon_my:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),CouponManager.class));
                }
                break;
        }
    }
}
