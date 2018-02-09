package com.blankj.utilcode.customwidget.ScollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.blankj.utilcode.util.LogUtils;


/**
 * 分发事件给子view的ScrollView  且
 * Created by leilei on 2017/11/7.
 */

public class InCludeLandscapeScrollView extends ScrollView{
    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private ISmartScrollChangedListener mSmartScrollChangedListener;
    public InCludeLandscapeScrollView(Context context) {
        super(context);
    }

    public InCludeLandscapeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InCludeLandscapeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }


    private int x, y;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    x = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                    int new_x = (int) ev.getX();
                    int new_y = (int) ev.getY();
//                    LogUtils.e(x + "===" + new_x);
                    //判断有水平滑动的意向
                    int move_x = x - new_x;//x轴滑动的距离
                    int move_y = y - new_y;//y轴滑动的距离
//                    LogUtils.e(move_x);
                    if (move_x > 30) {//30的偏移量
                        return false;//传递给字View
                    }
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                // 小心踩坑2: 这里不能是 >=// 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners();
        }
        // 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }

    public void setSmartScrollChangedListener(ISmartScrollChangedListener SmartScrollChangedListener) {
        this.mSmartScrollChangedListener = SmartScrollChangedListener;
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }else {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToMiddle();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }

    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
        void onScrolledToMiddle();
    }

    public interface OnOverScrolledListener{

    }
}
