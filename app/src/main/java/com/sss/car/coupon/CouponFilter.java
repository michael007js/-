package com.sss.car.coupon;

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
import com.sss.car.fragment.FragmentCouponDiscountsFullCutCash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的==>优惠券==>领取优惠券==>筛选==>优惠券分类
 * Created by leilei on 2017/9/22.
 */

public class CouponFilter extends BaseActivity implements NavMenuLayout.OnItemSelectedListener {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_coupon_filter)
    FrameLayout parentCouponFilter;
    @BindView(R.id.coupon_filter)
    LinearLayout CouponFilter;
    @BindView(R.id.NavMenuLayout_coupon_filter)
    NavMenuLayout NavMenuLayoutCouponFilter;
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


    FragmentCouponDiscountsFullCutCash fragmentCouponDiscounts;

    FragmentCouponDiscountsFullCutCash fragmentCouponCash;

    FragmentCouponDiscountsFullCutCash fragmentCouponFullCut;
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    String type,classify_id;

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        parentCouponFilter = null;
        CouponFilter = null;
        NavMenuLayoutCouponFilter = null;
        if (fragmentCouponDiscounts!=null){
            fragmentCouponDiscounts.onDestroy();
        }
        fragmentCouponDiscounts=null;
        if (fragmentCouponCash!=null){
            fragmentCouponCash.onDestroy();
        }
        fragmentCouponCash=null;
        if (fragmentCouponFullCut!=null){
            fragmentCouponFullCut.onDestroy();
        }
        fragmentCouponFullCut=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_filter);
        ButterKnife.bind(this);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        type=getIntent().getExtras().getString("type");
        classify_id=getIntent().getExtras().getString("classify_id");
        titleTop.setText(getIntent().getExtras().getString("title"));

        customInit(CouponFilter, false, true, false);

        NavMenuLayoutCouponFilter
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


        if (fragmentCouponDiscounts==null){
            fragmentCouponDiscounts=new FragmentCouponDiscountsFullCutCash(classify_id,type);
            FragmentUtils.addFragment(getSupportFragmentManager(),fragmentCouponDiscounts,R.id.parent_coupon_filter);
        }
        FragmentUtils.hideAllShowFragment(fragmentCouponDiscounts);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemSelected(int position) {


        switch (position){
            case 0:
                if (fragmentCouponDiscounts==null){
                    fragmentCouponDiscounts=new FragmentCouponDiscountsFullCutCash(classify_id,"1");    /*1满减券，2现金券，3折扣券*/
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentCouponDiscounts,R.id.parent_coupon_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentCouponDiscounts);

                break;
            case 1:
                if (fragmentCouponFullCut==null){
                    fragmentCouponFullCut=new FragmentCouponDiscountsFullCutCash(classify_id,"2");    /*1满减券，2现金券，3折扣券*/
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentCouponFullCut,R.id.parent_coupon_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentCouponFullCut);


                break;

            case 2:
                if (fragmentCouponCash==null){
                    fragmentCouponCash=new FragmentCouponDiscountsFullCutCash(classify_id,"3");    /*1满减券，2现金券，3折扣券*/
                    FragmentUtils.addFragment(getSupportFragmentManager(),fragmentCouponCash,R.id.parent_coupon_filter);
                }
                FragmentUtils.hideAllShowFragment(fragmentCouponCash);
                break;
        }

    }
}
