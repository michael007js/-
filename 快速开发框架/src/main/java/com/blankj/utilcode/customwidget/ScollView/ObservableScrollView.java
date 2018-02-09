package com.blankj.utilcode.customwidget.ScollView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;

import static com.blankj.utilcode.R.id.textView;

/**
 observableScrollView
 .fadingHeight(999)
 .fromTopToBottom(false)
 .init(text,getResources().getDrawable(R.color.google_blue));
 *
 * 带滚动监听变色的scrollview
 * http://blog.csdn.net/android_cll/article/details/60467637
 */
public class ObservableScrollView extends ScrollView {
    private int fadingHeight = 0; // 当ScrollView滑动到什么位置时渐变消失（根据需要进行调整）
    private static final int START_ALPHA = 0;//scrollview滑动开始位置
    private static final int END_ALPHA = 255;//scrollview滑动结束位置
    private boolean isFromTopToBottom=true;//从上往下滑颜色是否由深到浅

    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                             int oldx, int oldy);

    }

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * ScrollView滑动到什么位置时渐变消失
     * @param fadingHeight
     */
    public ObservableScrollView fadingHeight(int fadingHeight) {
        this.fadingHeight = fadingHeight;
        return this;
    }

    /**
     * 从上往下滑颜色是否由深到浅
     * @param isFromTopToBottom
     * @return
     */
    public ObservableScrollView fromTopToBottom(boolean isFromTopToBottom) {
        this.isFromTopToBottom = isFromTopToBottom;
        return this;
    }

    /**
     * @param view       要改变的view(导航栏)
     * @param background 顶部渐变布局需设置的Drawable ,drawable = getResources().getDrawable(R.color.dhlbg);
     */
    public ObservableScrollView init(final View view, final Drawable background) {
        if (!isFromTopToBottom){
            background.setAlpha(START_ALPHA);
        }
//
        view.setBackgroundDrawable(background);
        this.setScrollViewListener(
                new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y > fadingHeight) {
                            y = fadingHeight; // 当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可
//                          view.setBackgroundColor(Color.WHITE);
                        } else if (y < 0) {
                            y = 0;
                        } else {
//                          view.setBackgroundColor(0x99FFFFFF);
                        }
                        if (isFromTopToBottom){
                            background.setAlpha(255-(y * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA));
                        }else {
                            background.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight + START_ALPHA);
                        }
                    }
                }
        );
        return this;
    }

}