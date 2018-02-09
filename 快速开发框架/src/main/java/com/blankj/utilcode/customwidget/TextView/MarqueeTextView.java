package com.blankj.utilcode.customwidget.TextView;

/**
 * 走马灯
 * <p>
 * https://github.com/MrChanTT/MarqueeTextView
 * Created by leilei on 2017/7/4.
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 https://github.com/MrChanTT/MarqueeTextView

 <com.blankj.utilcode.customwidget.MarqueeTextView
 android:marqueeRepeatLimit="marquee_forever"
 android:id="@+id/song_name_main_view"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:layout_weight="1"
 android:gravity="bottom"
 android:maxLines="1"
 android:singleLine="true"
 android:text="Music will be better with you"
 android:textColor="@color/white"
 android:textSize="13sp"/>


 * Created by chenquan on 2016/12/7.
 */

public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 焦点
    @Override
    public boolean isFocused() {
        return true;
    }
    /**
     * 设置省略号的位置
     * **/
    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        where = TextUtils.TruncateAt.MARQUEE;//跑马灯
//        TextUtils.TruncateAt.END;
//        TextUtils.TruncateAt.START;
//        TextUtils.TruncateAt.MIDDLE;
        this.setMarqueeRepeatLimit(-1);//无限循环
        super.setEllipsize(where);
    }
}