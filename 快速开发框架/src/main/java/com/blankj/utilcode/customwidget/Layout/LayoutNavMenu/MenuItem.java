package com.blankj.utilcode.customwidget.Layout.LayoutNavMenu;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;


/**
 * author: chensen
 * https://github.com/smashinggit/Android-NavMenuLayout
 * date: 2017年03月17日10:19
 * desc:
 */

public class MenuItem extends FrameLayout {

    private int mIconWidth, mIconHeight;//图片大小
    private int mIconRes = 0;//图片资源
    private int mIconResSelected = 0;//选中图片资源

    private int mTextColor = Color.GRAY;//未选中的颜色(默认灰色)
    private int mTextColorSelected = Color.parseColor("#db4f55");//选中的颜色(默认红色)
    private int mTextSize = 12;//字体大小
    private String mText = "";//文字
    private boolean mTextIsShow = true;//文字是否显示
    private boolean mIconIsShow = true;//图标是否显示


    private int mUnderHigh = 3;//下方横线的高度
    private int mUnderWidthOffset = 10;////下方横线的宽度偏移量
    private int mUnderColor = Color.parseColor("#db4f55");//下方横线的颜色
    private int mDefaultUnderWidth = 52;//下方横线的默认宽度
    private boolean mUnderIsShow = false;


    private View mRootView;
    private ImageView ivIcon;//图标
    private TextView tvText;//文字
    private TextView tvUnder;//下方横线
    private TextView tvUnReadNum;//未读
    private TextView tvRedPoiont;//红点

    private boolean isSelected = false;//是否选中
    private int marginTop = 0;//文字和图标的距离


    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView(context);
    }

    private void intiView(Context context) {
        marginTop = PixelUtil.dpToPx(context, 5);
        //加载布局
        mRootView = LayoutInflater.from(context).inflate(R.layout.bottom_bar_item_menu, this, false);

        ivIcon = (ImageView) mRootView.findViewById(R.id.iv_icon);
        tvText = (TextView) mRootView.findViewById(R.id.tv_text);
        tvUnder = (TextView) mRootView.findViewById(R.id.tv_under);
        tvUnder.setBackgroundColor(mUnderColor);
        tvUnReadNum = (TextView) mRootView.findViewById(R.id.tv_unred_num);
        tvRedPoiont = (TextView) mRootView.findViewById(R.id.tv_point);
        addView(mRootView);
    }



    RelativeLayout.LayoutParams layoutParams;
    RelativeLayout.LayoutParams layoutParams2;

    /**
     * 刷新状态
     */
    private void refreshState() {
        if (isSelected) {
            if (mIconResSelected != 0 && mTextColorSelected != 0) {
                ivIcon.setImageResource(mIconResSelected);
                tvText.setTextColor(mTextColorSelected);

            }

        } else {
            if (mIconRes != 0 && mIconResSelected != 0) {
                ivIcon.setImageResource(mIconRes);
                tvText.setTextColor(mTextColor);
            }
        }
        if (mUnderIsShow&&isSelected) {
            tvUnder.setVisibility(VISIBLE);
        }else {
            tvUnder.setVisibility(GONE);
        }
        if (mTextIsShow) {
            tvText.setVisibility(VISIBLE);
        }else {
            tvText.setVisibility(GONE);
        }
        if (mIconIsShow) {
            ivIcon.setVisibility(VISIBLE);
        }else {
            ivIcon.setVisibility(GONE);
        }

        if (mTextSize > 0) {
            tvText.setTextSize(mTextSize);
        }
        tvText.setText(mText);
        layoutParams = null;
        layoutParams = (RelativeLayout.LayoutParams) tvText.getLayoutParams();
        layoutParams.topMargin = marginTop;
        tvText.setLayoutParams(layoutParams);
        layoutParams2 = null;
        layoutParams2 = (RelativeLayout.LayoutParams) tvUnder.getLayoutParams();
        if (tvText.getWidth() == 0) {
            layoutParams2.width = mDefaultUnderWidth + mUnderWidthOffset;
        } else {
            layoutParams2.width = tvText.getWidth() + mUnderWidthOffset;
        }
        layoutParams2.height = mUnderHigh;
        tvUnder.setLayoutParams(layoutParams2);

    }

    /**
     * 设置选中状态
     *
     * @param isSelected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        refreshState();
    }

    /**
     * 设置图标是否显示
     * @param isShow
     */
    public void setIconIsShow(boolean isShow) {
        mIconIsShow = isShow;
    }

    /**
     * 设置文字是否显示
     * @param isShow
     */
    public void setTextIsShow(boolean isShow) {
        mTextIsShow = isShow;
    }

    /**
     * 设置图标
     *
     * @param res
     */
    public void setIcon(@DrawableRes int res) {
        this.mIconRes = res;
    }

    /**
     * 设置选中图标
     *
     * @param res
     */
    public void setIconSelected(@DrawableRes int res) {
        this.mIconResSelected = res;
    }

    /**
     * 设置图标大小
     *
     * @param width
     * @param height
     */
    public void setIconSize(int width, int height) {
        this.mIconWidth = width;
        this.mIconHeight = height;
        LayoutParams params = (LayoutParams) ivIcon.getLayoutParams();
        params.width = mIconWidth;
        params.height = mIconHeight;
        ivIcon.setLayoutParams(params);
    }

    /**
     * 是否显示下方横线
     *
     * @param underIsShow
     */
    public void setUnderIsShow(boolean underIsShow) {
        this.mUnderIsShow = underIsShow;
    }

    /**
     * 设置下方横线颜色
     *
     * @param underColor
     */
    public void setUnderColor(int underColor) {
        this.mUnderColor = underColor;
        tvUnder.setBackgroundColor(mUnderColor);
    }

    /**
     * 设置下方横线颜色
     *
     * @param underWidth
     */
    public void setDefaultUnderWidth(int underWidth) {
        this.mDefaultUnderWidth = underWidth;
    }

    /**
     * 设置下方横线宽度偏移量
     *
     * @param underWidthOffset
     */
    public void setUnderWidthOffset(int underWidthOffset) {
        this.mUnderWidthOffset = underWidthOffset;
    }

    /**
     * 设置下方横线高度
     *
     * @param high
     */
    public void setUnderhigh(int high) {
        this.mUnderHigh = high;
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        this.mText = text;
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        this.mTextSize = size;
    }

    /**
     * 文字颜色
     *
     * @param color
     */

    public void setTextColor(int color) {
        this.mTextColor = color;
    }


    /**
     * 选中的文字颜色
     *
     * @param color
     */
    public void setSelectedTextColor(int color) {
        this.mTextColorSelected = color;
    }


    /**
     * 显示提示
     *
     * @param msg
     */
    public void setMsg(String msg) {
        tvUnReadNum.setVisibility(VISIBLE);
        tvUnReadNum.setText(msg);
        tvRedPoiont.setVisibility(GONE);
    }

    /**
     * 图标和文字之间的距离
     *
     * @param marginTop
     */
    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }


    /**
     * 隐藏提示
     */
    public void hideMsg() {
        tvUnReadNum.setVisibility(GONE);
    }

    /**
     * 显示红点
     *
     */
    public void showRedPoint() {
        tvRedPoiont.setVisibility(VISIBLE);
        tvUnReadNum.setVisibility(GONE);
    }

    /**
     * 隐藏红点
     */

    public void hideRedPoint() {
        tvRedPoiont.setVisibility(GONE);
    }

    /**
     * 隐藏所有提示
     */
    public void hideAllTips() {
        tvRedPoiont.setVisibility(GONE);
        tvUnReadNum.setVisibility(GONE);
    }


    /**
     * 获取是否选中
     *
     * @return
     */
    public boolean getIsSelected() {
        return isSelected;
    }


}
