package com.blankj.utilcode.customwidget.TextView;

/**
 * Created by leilei on 2017/6/8.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * 角标控件
 *
 *
 * 参考地址http://blog.csdn.net/ylyg050518/article/details/51985943         https://github.com/Jack011/BadgeViewDemo
 */
public class BadgeView extends TextView {

    private boolean mHideOnNull = true;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public void init() {
        if (!(getLayoutParams() instanceof LayoutParams)) {
            LayoutParams layoutParams =new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.TOP);
            setLayoutParams(layoutParams);
        }

        // 设置默认字体
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        setPadding(dip2Px(5), dip2Px(1), dip2Px(5), dip2Px(1));

        // 设置默认背景
        setBackground(9, Color.parseColor("#d3321b"));

        //设置内容居中
        setGravity(Gravity.CENTER);

        // 设置默认值
        setHideOnNull(true);//设置当内容为空时隐藏
        setBadgeCount(0);//默认数量为0
    }

    //设置圆角弧度和背景色
    @SuppressWarnings("deprecation")
    public void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px(dipRadius);
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackgroundDrawable(bgDrawable);
    }

    //返回当内容为空(0/null)时是否隐藏
    public boolean isHideOnNull() {
        return mHideOnNull;
    }

    //设置内容为空(0/null)时是否隐藏
    public void setHideOnNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }

    /*
     * 设置文字，当isHideOnNull为true时且文字为0或者为null时隐藏
     *
     * @see android.widget.TextView#setText(java.lang.CharSequence, android.widget.TextView.BufferType)
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
        super.setText(text, type);
    }

    //设置要显示的数字
    public void setBadgeCount(int count) {
        setText(String.valueOf(count));
    }

    //获取要显示的数字
    public Integer getBadgeCount() {
        if (getText() == null) {
            return null;
        }

        String text = getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //设置内容的gravity
    public void setBadgeGravity(int gravity) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    // 获取内容的gravity
    public int getBadgeGravity() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return params.gravity;
    }

    //设置内容区域填充，用同一个填充距离
    public void setBadgeMargin(int dipMargin) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
    }

    //设置内容填充，可详细设置上下左右的填充距离
    public void setBadgeMargin(int leftDipMargin, int topDipMargin, int rightDipMargin, int bottomDipMargin) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.leftMargin = dip2Px(leftDipMargin);
        params.topMargin = dip2Px(topDipMargin);
        params.rightMargin = dip2Px(rightDipMargin);
        params.bottomMargin = dip2Px(bottomDipMargin);
        setLayoutParams(params);
    }

    //获得Margin值
    public int[] getBadgeMargin() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return new int[]{params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin};
    }

    //设置显示数字增加increment
    public void incrementBadgeCount(int increment) {
        Integer count = getBadgeCount();
        if (count == null) {
            setBadgeCount(increment);
        } else {
            setBadgeCount(increment + count);
        }
    }

    //设置显示数字减少decrement
    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
    }


    //设置TabWidget在tabIndex出显示BadgeView
    public void setTargetView(TabWidget target, int tabIndex) {
        View tabView = target.getChildTabViewAt(tabIndex);
        setTargetView(tabView);
    }

    /*
     * 将badgeView与target 控件关联
     *
     */
    public void setTargetView(View target) {
        //如果已经设置了父级控件，要从父级控件中移除当前view
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        //target为null直接返回，不执行任何操作
        if (target == null) {
            return;
        }
        //如果target的父容器是FrameLayout,直接将当前view添加进去
        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);
            //如果target的父容器是非FrameLayout的其他类型
        } else if (target.getParent() instanceof ViewGroup) {

            //取得target的父容器
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            //取得target在父容器当中的位置
            int groupIndex = parentContainer.indexOfChild(target);
            //先从父容器中移除target
            parentContainer.removeView(target);
            //创建FrameLayout包裹容器
            FrameLayout badgeContainer = new FrameLayout(getContext());
            //获取target的布局参数
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();
            //设置包裹容器的布局参数
            badgeContainer.setLayoutParams(parentLayoutParams);
            //设置target填充包裹容器
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //将包裹容器添加到原来target的父容器中
            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            //将target添加到包裹容器中
            badgeContainer.addView(target);
            //将当前badgeView添加到包裹容器中
            badgeContainer.addView(this);
            //如果target没有父容器，报错
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }

    }

    /*
     * dip转换为Px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}