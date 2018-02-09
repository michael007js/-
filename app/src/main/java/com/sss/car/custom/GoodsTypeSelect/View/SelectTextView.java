package com.sss.car.custom.GoodsTypeSelect.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;

/**
 * 选中是否变色的TextView
 * Created by leilei on 2017/20/28.
 */

public class SelectTextView extends LinearLayout {
    private TextView select_textview;
    private View view;
    private boolean isSelect = false;
    private OnSelectTextViewCallBack onSelectTextViewCallBack;
    private Drawable selectDrawable;
    private Drawable disSelectDrawable;
    private int selectTextColor;
    private int disSelectTextColor;
    private String content;

    public void setOnSelectTextViewCallBack(OnSelectTextViewCallBack onSelectTextViewCallBack) {
        this.onSelectTextViewCallBack = onSelectTextViewCallBack;
    }

    public SelectTextView(Context context) {
        super(context);
        init(context);
    }

    public SelectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SelectTextView setText(String str) {
        if (!"".equals(str)) {
            content = str;
            select_textview.setText(content);
            select_textview.setPadding(40, 20, 40, 20);
        }
        return this;
    }


    void init(Context context) {
        disSelectDrawable = context.getResources().getDrawable(R.drawable.bg_disselect_textview);
        selectDrawable = context.getResources().getDrawable(R.drawable.bg_select_textview);
        selectTextColor = context.getResources().getColor(R.color.mainColor);
        disSelectTextColor = context.getResources().getColor(R.color.text_black);
        view = LayoutInflater.from(context).inflate(R.layout.select_textview, null);
        select_textview = $.f(view, R.id.select_textview);
        select_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = !isSelect;
                if (isSelect) {
                    select_textview.setText(content);
                    select_textview.setBackground(selectDrawable);
                    select_textview.setTextColor(selectTextColor);
                    select_textview.setPadding(40, 20, 40, 20);
                } else {
                    select_textview.setText(content);
                    select_textview.setBackground(disSelectDrawable);
                    select_textview.setTextColor(disSelectTextColor);
                    select_textview.setPadding(40, 20, 40, 20);
                }
                if (onSelectTextViewCallBack != null) {
                    onSelectTextViewCallBack.onSelect(isSelect,content);
                }
            }
        });
        this.addView(view);
    }


    public interface OnSelectTextViewCallBack {
        void onSelect(boolean isSelect,String content);
    }
}
