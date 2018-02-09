package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentSaleAndSeckillFilterHot;
import com.sss.car.fragment.FragmentSaleAndSeckillFilterLater;
import com.sss.car.fragment.FragmentSaleAndSeckillFilterNow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 淘秒杀淘筛选列表
 * Created by leilei on 2017/9/22.
 */

public class ActivitySaleAndSeckillSaleFilter extends BaseFragmentActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.parent_activity_sale_and_seckill_sale_filter)
    FrameLayout parentActivitySaleAndSeckillSaleFilter;
    @BindView(R.id.activity_sale_and_seckill_sale_filter)
    LinearLayout activitySaleAndSeckillSaleFilter;
    @BindView(R.id.hot_activity_sale_and_seckill_sale_filter)
    TextView hotActivitySaleAndSeckillSaleFilter;
    @BindView(R.id.ing_activity_sale_and_seckill_sale_filter)
    TextView ingActivitySaleAndSeckillSaleFilter;
    @BindView(R.id.will_activity_sale_and_seckill_sale_filter)
    TextView willActivitySaleAndSeckillSaleFilter;

    FragmentSaleAndSeckillFilterHot fragmentSaleAndSeckillFilterHot;

    FragmentSaleAndSeckillFilterNow fragmentSaleAndSeckillFilterNow;

    FragmentSaleAndSeckillFilterLater fragmentSaleAndSeckillFilterLater;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        parentActivitySaleAndSeckillSaleFilter = null;
        activitySaleAndSeckillSaleFilter = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_and_seckill_sale_filter);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        titleTop.setText(getIntent().getExtras().getString("title"));
        if (fragmentSaleAndSeckillFilterHot==null){
            fragmentSaleAndSeckillFilterHot=new FragmentSaleAndSeckillFilterHot(getIntent().getExtras().getString("classify_id"),
                    getIntent().getExtras().getString("type"));
            FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillFilterHot,R.id.parent_activity_sale_and_seckill_sale_filter);
        }
        FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillFilterHot);

    }

    @OnClick({R.id.hot_activity_sale_and_seckill_sale_filter, R.id.ing_activity_sale_and_seckill_sale_filter, R.id.will_activity_sale_and_seckill_sale_filter,R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hot_activity_sale_and_seckill_sale_filter:
                hotActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.mainColor));
                ingActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                willActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                if (fragmentSaleAndSeckillFilterHot==null){
                    fragmentSaleAndSeckillFilterHot=new FragmentSaleAndSeckillFilterHot(getIntent().getExtras().getString("classify_id"),
                            getIntent().getExtras().getString("type"));
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillFilterHot,R.id.parent_activity_sale_and_seckill_sale_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillFilterHot);
                break;
            case R.id.ing_activity_sale_and_seckill_sale_filter:
                hotActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                ingActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.mainColor));
                willActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                if (fragmentSaleAndSeckillFilterNow==null){
                    fragmentSaleAndSeckillFilterNow=new FragmentSaleAndSeckillFilterNow(getIntent().getExtras().getString("classify_id"),
                            getIntent().getExtras().getString("type"));
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillFilterNow,R.id.parent_activity_sale_and_seckill_sale_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillFilterNow);
                break;
            case R.id.will_activity_sale_and_seckill_sale_filter:
                hotActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                ingActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.black_dark));
                willActivitySaleAndSeckillSaleFilter.setBackgroundColor(getResources().getColor(R.color.mainColor));
                if (fragmentSaleAndSeckillFilterLater==null){
                    fragmentSaleAndSeckillFilterLater=new FragmentSaleAndSeckillFilterLater(getIntent().getExtras().getString("classify_id"),
                            getIntent().getExtras().getString("type"));
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillFilterLater,R.id.parent_activity_sale_and_seckill_sale_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillFilterLater);
                break;
            case R.id.back_top:
                finish();
                break;
        }
    }
}
