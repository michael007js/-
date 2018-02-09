package com.sss.car.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.APPOftenUtils;
import com.sss.car.R;
import com.sss.car.popularize.PopularizeList;
import com.sss.car.view.ActivityGoodsServiceEdit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的商品
 * Created by leilei on 2017/10/26.
 */

public class Commodity extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.click_on_commodity)
    LinearLayout clickOnCommodity;
    @BindView(R.id.click_down_commodity)
    LinearLayout clickDownCommodity;
    @BindView(R.id.click_commodity)
    LinearLayout clickCommodity;
    @BindView(R.id.commodity)
    LinearLayout commodity;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        clickOnCommodity = null;
        clickDownCommodity = null;
        clickCommodity = null;
        commodity = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity);
        ButterKnife.bind(this);
        rightButtonTop.setText("添加商品");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        titleTop.setText("我的商品");
        customInit(commodity, false, true, false);


    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.click_on_commodity, R.id.click_down_commodity, R.id.click_commodity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceEdit.class));
                }

                break;
            case R.id.click_on_commodity:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),CommodityList.class)
                    .putExtra("status","1"));
                }

                break;
            case R.id.click_down_commodity:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),CommodityList.class)
                            .putExtra("status","0"));
                }
                break;
            case R.id.click_commodity:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),PopularizeList.class));
                }
                break;
        }
    }
}
