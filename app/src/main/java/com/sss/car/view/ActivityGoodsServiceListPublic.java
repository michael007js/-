package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentGoodsServiceListPublic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/6.
 */

public class ActivityGoodsServiceListPublic extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent)
    FrameLayout parent;
    @BindView(R.id.activity_goods_service_list_public)
    LinearLayout activityGoodsServiceListPublic;
    FragmentGoodsServiceListPublic fragmentGoodsServiceListPublic;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentGoodsServiceListPublic != null) {
            fragmentGoodsServiceListPublic.onDestroy();
        }
        fragmentGoodsServiceListPublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        setContentView(R.layout.activity_goods_service_list_public);
        ButterKnife.bind(this);
        customInit(activityGoodsServiceListPublic, false, true, false);
        titleTop.setText(getIntent().getExtras().getString("title"));
        int [] a={1,3};
        fragmentGoodsServiceListPublic = new FragmentGoodsServiceListPublic(getIntent().getExtras().getString("type"),getIntent().getExtras().getBoolean("showHead"),getIntent().getExtras().getString("classify_id")
                , getIntent().getExtras().getString("shop_id"),/*如果传shop_id，返回的数据则是该店铺下的商品*/
                new int[]{1, 3}
        );

        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentGoodsServiceListPublic, R.id.parent);
        FragmentUtils.hideAllShowFragment(fragmentGoodsServiceListPublic);
    }

    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
        }
    }


}
