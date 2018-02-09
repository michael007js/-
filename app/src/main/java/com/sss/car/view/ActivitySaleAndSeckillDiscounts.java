package com.sss.car.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentSaleAndSeckillDiscountsCash;
import com.sss.car.fragment.FragmentSaleAndSeckillDiscountsDiscounts;
import com.sss.car.fragment.FragmentSaleAndSeckillDiscountsFullCut;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 淘优惠分类列表
 * Created by leilei on 2017/9/22.
 */

public class ActivitySaleAndSeckillDiscounts extends BaseActivity implements NavMenuLayout.OnItemSelectedListener {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_activity_sale_and_seckill_discounts)
    FrameLayout parentActivitySaleAndSeckillDiscounts;
    @BindView(R.id.activity_sale_and_seckill_discounts)
    LinearLayout activitySaleAndSeckillDiscounts;
    @BindView(R.id.NavMenuLayout_activity_sale_and_seckill_discounts)
    NavMenuLayout NavMenuLayoutActivitySaleAndSeckillDiscounts;
    /*底部导航栏文字*/
    String[] text = new String[]{"折扣券", "满减券", "现金券"};
    /*底部导航栏未选中图标*/
    int[] unSelectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏选中图标*/
    int[] selectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏未选中颜色*/
    String unSelectColor = "#333333";
    /*底部导航栏选中颜色*/
    String selectColor = "#df3347";


    FragmentSaleAndSeckillDiscountsDiscounts fragmentSaleAndSeckillDiscountsDiscounts;

    FragmentSaleAndSeckillDiscountsCash fragmentSaleAndSeckillDiscountsCash;

    FragmentSaleAndSeckillDiscountsFullCut fragmentSaleAndSeckillDiscountsFullCut;
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    String type,classify_id;

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        parentActivitySaleAndSeckillDiscounts = null;
        activitySaleAndSeckillDiscounts = null;
        NavMenuLayoutActivitySaleAndSeckillDiscounts = null;
        if (fragmentSaleAndSeckillDiscountsDiscounts!=null){
            fragmentSaleAndSeckillDiscountsDiscounts.onDestroy();
        }
        fragmentSaleAndSeckillDiscountsDiscounts=null;
        if (fragmentSaleAndSeckillDiscountsCash!=null){
            fragmentSaleAndSeckillDiscountsCash.onDestroy();
        }
        fragmentSaleAndSeckillDiscountsCash=null;
        if (fragmentSaleAndSeckillDiscountsFullCut!=null){
            fragmentSaleAndSeckillDiscountsFullCut.onDestroy();
        }
        fragmentSaleAndSeckillDiscountsFullCut=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_and_seckill_discounts);
        ButterKnife.bind(this);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        type=getIntent().getExtras().getString("type");
        classify_id=getIntent().getExtras().getString("classify_id");
        titleTop.setText(getIntent().getExtras().getString("title"));

        customInit(activitySaleAndSeckillDiscounts, false, true, false);

        NavMenuLayoutActivitySaleAndSeckillDiscounts
                .initMenu(3)//初始化布局
                .setIconRes(unSelectIcon)//设置未选中图标
                .setIconResSelected(selectIcon)//设置选中图标
                .setTextRes(text)//设置文字
                .setUnderhigh(5)//下方横线的高度
                .setIconSize(60, 60)//设置图标大小
                .setTextSize(13)//设置文字大小
                .setIconIsShow(false)//设置图标是否显示
                .setTextIsShow(true)//设置文字是否显示
                .setTextColor(Color.parseColor(unSelectColor))//未选中状态下文字颜色
                .setTextColorSelected(Color.parseColor(selectColor))//选中状态下文字颜色
                .setUnderIsShow(true)//是否显示下方横线
                .setUnderWidthOffset(10)//下方横线的偏移量
                .setDefaultUnderWidth(52)//下方横线的初始宽度
//                .setBackColor(Color.WHITE)//设置背景色
//                .setBackColor(1,Color.RED)//设置背景色
//                .setMarginTop(PixelUtil.dpToPx(Main.this, 5))//文字和图标直接的距离，默认为5dp
                .setSelected(0)//设置选中的位置
                .setOnItemSelectedListener(this);


        if (fragmentSaleAndSeckillDiscountsDiscounts==null){
            fragmentSaleAndSeckillDiscountsDiscounts=new FragmentSaleAndSeckillDiscountsDiscounts(classify_id,type);
            FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillDiscountsDiscounts,R.id.parent_activity_sale_and_seckill_discounts);
        }
        FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillDiscountsDiscounts);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {


        switch (position){
            case 0:
                if (fragmentSaleAndSeckillDiscountsDiscounts==null){
                    fragmentSaleAndSeckillDiscountsDiscounts=new FragmentSaleAndSeckillDiscountsDiscounts(classify_id,type);
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillDiscountsDiscounts,R.id.parent_activity_sale_and_seckill_discounts);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillDiscountsDiscounts);

                break;
            case 1:
                if (fragmentSaleAndSeckillDiscountsFullCut==null){
                    fragmentSaleAndSeckillDiscountsFullCut=new FragmentSaleAndSeckillDiscountsFullCut(classify_id,type);
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillDiscountsFullCut,R.id.parent_activity_sale_and_seckill_discounts);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillDiscountsFullCut);


                break;

            case 2:
                if (fragmentSaleAndSeckillDiscountsCash==null){
                    fragmentSaleAndSeckillDiscountsCash=new FragmentSaleAndSeckillDiscountsCash(classify_id,type);
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentSaleAndSeckillDiscountsCash,R.id.parent_activity_sale_and_seckill_discounts);
                }
                FragmentUtils.hideAllShowFragment(fragmentSaleAndSeckillDiscountsCash);
                break;
        }

    }
}
