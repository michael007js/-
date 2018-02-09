package com.sss.car.commodity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCommodity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的商品==>编辑商品信息
 * Created by leilei on 2017/10/26.
 */

public class CommodityEdit extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_commodity_edit)
    FrameLayout parentCommodityEdit;
    @BindView(R.id.commodity_edit)
    LinearLayout CommodityEdit;

    FragmentCommodity fragmentCommodity;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        parentCommodityEdit = null;
        CommodityEdit = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_edit);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(CommodityEdit, false, true, false);
        titleTop.setText("编辑商品信息");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        if (fragmentCommodity == null) {
            fragmentCommodity = new FragmentCommodity(getIntent().getExtras().getString("type"),FragmentCommodity.LAUNCH_MODE_EDIT,getIntent().getExtras().getString("goods_id"));
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCommodity, R.id.parent_commodity_edit);
        }
        FragmentUtils.hideAllShowFragment(fragmentCommodity);
    }

    @OnClick({R.id.back_top,R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                fragmentCommodity.addAndUpdate();
                break;
        }
    }


}
