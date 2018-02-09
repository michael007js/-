package com.blankj.utilcode.customwidget.Layout.LayoutNavMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * author: chensen
 * date: 2017年03月17日10:08
 * desc: 底部导航菜单
 */


/*
 *
    step1:

     //底部导航栏文字
    String[] text = new String[]{"标题", "标题", "标题", "标题"};
    //底部导航栏未选中图标
        int[] unSelectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //底部导航栏选中图标
        int[] selectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //底部导航栏未选中颜色
        String unSelectColor = "#333333";
    //底部导航栏选中颜色
        String selectColor = "#df3347";

        step2:

         navMenuLayoutActivityOrderReturnsChange
                .initMenu(4)//初始化布局
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

 * */

public class NavMenuLayout extends LinearLayout {

    private static final String TAG = NavMenuLayout.class.getSimpleName();
    private NavMenuLayout instance;

    private Context mContext;
    private List<MenuItem> menuList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnItemReSelectedListener mOnItemReSelectedListener;

    private int[] mListIconRes;//未选中图标
    private int[] mListIconResSelected;//选中图标
    private int mIconWidth, mIconHeight;//图标大小


    private String[] mListText;//的文字
    private int mTextSize = 10;//文字大小
    private int mTextColor = Color.GRAY;//未选中的颜色(默认灰色)
    private int mTextColorSelected = Color.parseColor("#db4f55");//选中的颜色(默认红色)
    private int mUnderColor = Color.parseColor("#db4f55");//下方横线的颜色
    private boolean mUnderIsShow = false;
    private int mUnderHigh = 3;//下方横线的高度
    private int mUnderWidthOffset = 10;////下方横线的宽度偏移量
    private int mDefaultUnderWidth = 52;//下方横线的默认宽度

    private int marginTop = 0;//文字和图标的距离

    private int mCount = 0;//菜单数量
    private int mCurrentPosition = 0;//选中的位置


    public NavMenuLayout(Context context) {
        this(context, null);
    }

    public NavMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        marginTop = PixelUtil.dpToPx(context, 5);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        instance = this;

        setOrientation(HORIZONTAL);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NavMenuLayout, defStyleAttr, 0);
        if (typedArray != null) {
            //菜单数量
            mCount = typedArray.getInt(R.styleable.NavMenuLayout_menuCount, 5);
            typedArray.recycle();
        }
    }


    //初始化布局
    public NavMenuLayout initMenu(int count) {
        this.mCount = count;
        menuList = new ArrayList<>(mCount);
        for (int i = 0; i < mCount; i++) {
            final MenuItem menuItem = new MenuItem(mContext);
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            params.gravity = Gravity.CENTER;
            menuItem.setLayoutParams(params);
            menuItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getSelectedPosition(menuItem);
                    if (position == mCurrentPosition) {  //已经是选中状态
                        NavMenuLayout.this.OnItemReClick(position);
                    } else {
                        NavMenuLayout.this.OnItemClick(position);
                    }
                }
            });
            menuList.add(menuItem);
            addView(menuItem);
        }
        return instance;
    }


    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public NavMenuLayout setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
        return this;
    }

    public interface OnItemReSelectedListener {
        void onItemReSelected(int position);
    }

    public NavMenuLayout setOnItemReSelectedListener(OnItemReSelectedListener listener) {
        this.mOnItemReSelectedListener = listener;
        return this;
    }

    private void OnItemClick(int position) {
        for (int i = 0; i < mCount; i++) {
            if (i == position) {
                setSelected(position);
            }
        }
        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(position);
        }
    }

    private void OnItemReClick(int position) {
        if (mOnItemReSelectedListener != null) {
            mOnItemReSelectedListener.onItemReSelected(position);
        }
    }

    /**
     * 更改选中状态
     *
     * @param position
     */
    public NavMenuLayout setSelected(int position) {
        if (position >= 0 && position < mCount) {
            mCurrentPosition = position;
            refreshState();
        } else {
            LogUtils.e(TAG, "setSelected(int position) ---> the position is not correct");
        }

        return instance;
    }

    /**
     * 刷新状态
     */
    public void refreshState() {
        for (int i = 0; i < mCount; i++) {
            MenuItem menuItem = menuList.get(i);
            if (i == mCurrentPosition) {
                menuItem.setSelected(true);
            } else {
                menuItem.setSelected(false);
            }
        }
    }

    /**
     * 是否显示下方横线
     *
     * @param underIsShow
     */
    public NavMenuLayout setUnderIsShow(boolean underIsShow) {
        this.mUnderIsShow = underIsShow;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setUnderIsShow(underIsShow);
                }
            }
        }
        return this;
    }

    /**
     * 设置下方横线宽度偏移量
     *
     * @param underWidthOffset
     */
    public NavMenuLayout setUnderWidthOffset(int underWidthOffset) {
        this.mUnderWidthOffset = underWidthOffset;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setUnderWidthOffset(underWidthOffset);
                }
            }
        }
        return this;
    }

    /**
     * 设置下方横线颜色
     *
     * @param underWidth
     */
    public NavMenuLayout setDefaultUnderWidth(int underWidth) {
        this.mDefaultUnderWidth = underWidth;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setDefaultUnderWidth(underWidth);
                }
            }
        }
        return this;
    }

    /**
     * 设置下方横线高度
     *
     * @param high
     */
    public NavMenuLayout setUnderhigh(int high) {
        this.mUnderHigh = high;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setUnderhigh(high);
                }
            }
        }
        return this;
    }

    /**
     * 设置下方横线颜色
     *
     * @param underColor
     */
    public NavMenuLayout setUnderColor(int underColor) {
        this.mUnderColor = underColor;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setUnderColor(underColor);
                }
            }
        }
        return this;
    }

    /**
     * 设置未选中的图片
     *
     * @param listIconRes
     * @return
     */
    public NavMenuLayout setIconRes(int[] listIconRes) {
        this.mListIconRes = listIconRes;
        if (mCount != mListIconRes.length) {
            LogUtils.e(TAG, "the iconRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconRes[i] != 0) {
                    menuList.get(i).setIcon(mListIconRes[i]);
                }
            }
        }
        return instance;
    }


    /**
     * 设置选中的图片
     *
     * @param listIconResSelected
     * @return
     */
    public NavMenuLayout setIconResSelected(int[] listIconResSelected) {
        this.mListIconResSelected = listIconResSelected;
        if (mCount != mListIconResSelected.length) {
            LogUtils.e(TAG, "the iconResSelected length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconResSelected[i] != 0) {
                    menuList.get(i).setIconSelected(mListIconResSelected[i]);
                }
            }
        }
        return instance;
    }

    /**
     * 设置图片大小
     *
     * @param iconWidth
     * @param iconHeight
     * @return
     */
    public NavMenuLayout setIconSize(int iconWidth, int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        if (mCount != mListIconResSelected.length) {
            LogUtils.e(TAG, "the iconResSelected length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListIconResSelected[i] != 0) {
                    menuList.get(i).setIconSize(mIconWidth, mIconHeight);
                }
            }
        }
        return instance;
    }

    /**
     * 设置某一个图片大小
     *
     * @param iconWidth
     * @param iconHeight
     * @return
     */
    public NavMenuLayout setIconSize(int position, int iconWidth, int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        if (mCount != mListIconResSelected.length) {
            LogUtils.e(TAG, "the iconResSelected length is not equals count");
        } else {
            if (position >= 0 && position < mCount) {
                menuList.get(position).setIconSize(mIconWidth, mIconHeight);
            } else {
                LogUtils.e(TAG, "setIconSize(int position, int iconWidth, int iconHeight) ---> the position  is not correct");
            }
        }

        return instance;
    }


    /**
     * 设置文字
     *
     * @param listText
     * @return
     */
    public NavMenuLayout setTextRes(String[] listText) {
        this.mListText = listText;
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setText(mListText[i]);
                }
            }
        }
        return instance;
    }

    /**
     * 设置图标是否显示
     *
     * @param isShow
     */
    public NavMenuLayout setIconIsShow(boolean isShow) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setIconIsShow(isShow);
                }
            }
        }
        return this;
    }

    /**
     * 设置文字是否显示
     *
     * @param isShow
     */
    public NavMenuLayout setTextIsShow(boolean isShow) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setTextIsShow(isShow);
                }
            }
        }
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public NavMenuLayout setTextSize(int textSize) {
        this.mTextSize = textSize;
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setTextSize(mTextSize);
                }
            }
        }

        return instance;
    }

    /**
     * 设置某一位置文字大小
     *
     * @param position
     * @param textSize
     * @return
     */
    public NavMenuLayout setTextSize(int position, int textSize) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            if (position >= 0 && position < mCount) {
                menuList.get(position).setTextSize(mTextSize);
            } else {
                LogUtils.e("TAG", "setTextSize(int position, int textSize) ----> the position  is not correct");
            }

        }
        return instance;
    }


    /**
     * 文字颜色
     *
     * @param textColor
     */
    public NavMenuLayout setTextColor(int textColor) {
        this.mTextColor = textColor;
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setTextColor(textColor);
                }
            }
        }

        return instance;
    }

    public NavMenuLayout setTextColor(int position, int textColor) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            if (position >= 0 && position < mCount) {
                menuList.get(position).setTextColor(textColor);
            } else {
                LogUtils.e(TAG, "setTextColor(int position, int textColor) ---> the position  is not correct ");
            }
        }

        return instance;
    }


    /**
     * 选中的文字颜色
     *
     * @param textColorSelected
     */
    public NavMenuLayout setTextColorSelected(int textColorSelected) {
        this.mTextColorSelected = textColorSelected;
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setSelectedTextColor(textColorSelected);
                }
            }
        }
        return instance;
    }

    public NavMenuLayout setTextColorSelected(int position, int textColorSelected) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            if (position >= 0 && position < mCount) {
                menuList.get(position).setSelectedTextColor(textColorSelected);
            } else {
                LogUtils.e(TAG, "setTextColorSelected(int position, int textColorSelected) ---> the position  is not correct ");
            }
        }
        return instance;
    }

    /**
     * 图标和文字之间的距离
     *
     * @param marginTop
     */
    public NavMenuLayout setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            for (int i = 0; i < mCount; i++) {
                if (mListText[i] != null) {
                    menuList.get(i).setMarginTop(marginTop);
                }
            }
        }
        return instance;
    }

    public NavMenuLayout setMarginTop(int position, int marginTop) {
        if (mCount != mListText.length) {
            LogUtils.e(TAG, "the textRes length is not equals count");
        } else {
            if (position >= 0 && position < mCount) {
                menuList.get(position).setMarginTop(marginTop);
            } else {
                LogUtils.e(TAG, "setTextColorSelected(int position, int textColorSelected) ---> the position  is not correct ");
            }
        }
        return instance;
    }


    /**
     * 背景颜色
     *
     * @param backColor
     */
    public NavMenuLayout setBackColor(int backColor) {
        setBackgroundColor(backColor);
        return instance;
    }

    public NavMenuLayout setBackColor(int position, int backColor) {
        if (position >= 0 && position < mCount) {
            menuList.get(position).setBackgroundColor(backColor);
        } else {
            LogUtils.e(TAG, "setBackColor(int position, int backColor) ---> the positon is not correct");
        }
        return instance;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 获取点击的位置
     */
    public int getSelectedPosition(MenuItem item) {
        for (int i = 0; i < mCount; i++) {
            if (item == menuList.get(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 设置未读信息
     *
     * @param position
     * @param msg
     * @return
     */
    public NavMenuLayout setMsg(int position, String msg) {
        if (position >= 0 && position < mCount) {
            if (menuList != null) {
                menuList.get(position).setMsg(msg);
            }
        } else {
            LogUtils.d(TAG, "setMsg(int position, String msg) ---> the position is not correct");
        }
        return instance;

    }

    /**
     * 隐藏未读信息
     *
     * @param position
     * @return
     */
    public NavMenuLayout hideMsg(int position) {
        if (position >= 0 && position < mCount) {
            if (menuList != null) {
                menuList.get(position).hideMsg();
            }
        } else {
            LogUtils.d(TAG, "hideMsg(int position) ---> the position is not correct");
        }
        return instance;
    }


    /**
     * 显示红点
     *
     * @param position
     * @return
     */
    public NavMenuLayout showRedPoint(int position) {
        if (position >= 0 && position < mCount) {
            if (menuList != null) {
                menuList.get(position).showRedPoint();
            }
        } else {
            LogUtils.d(TAG, "showRedPoint(int position) ---> the position is not correct");
        }
        return instance;

    }

    /**
     * 隐藏红点
     *
     * @param position
     * @return
     */
    public NavMenuLayout hideRedPoint(int position) {
        if (position >= 0 && position < mCount) {
            menuList.get(position).hideRedPoint();
        } else {
            LogUtils.d(TAG, "hideRedPoint(int position) ---> the position is not correct");
        }
        return instance;

    }

    /**
     * 隐藏红点
     *
     * @param position
     * @return
     */
    public NavMenuLayout hideAllTips(int position) {
        if (position >= 0 && position < mCount) {
            menuList.get(position).hideAllTips();
        } else {
            LogUtils.d(TAG, "hideRedPoint(int position) ---> the position is not correct");
        }
        return instance;

    }
}
