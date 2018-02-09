package com.sss.car.custom.GoodsTypeSelect.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;

/**
 * 可被动态删除的TextText
 * Created by leilei on 2017/10/28.
 */

public class DelTextView extends LinearLayout {
    private View view;
    private TextView input_del_edittext;
    private TextView delete_del_edittext;
    private OnDelTextTextCallBack onDelTextTextCallBack;

    public void remove() {
        view = null;
        input_del_edittext = null;
        delete_del_edittext = null;
        onDelTextTextCallBack = null;

    }

    public void setOnDelTextTextCallBack(OnDelTextTextCallBack onDelTextTextCallBack) {
        this.onDelTextTextCallBack = onDelTextTextCallBack;
    }

    public DelTextView(Context context) {
        super(context);
        init(context);
    }

    public DelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DelTextView setText(String str) {
        if (!"".equals(str)) {
            input_del_edittext.setText(str);
        }
        return this;
    }

    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.del_edittext, null);
        input_del_edittext = $.f(view, R.id.input_del_edittext);
        delete_del_edittext = $.f(view, R.id.delete_del_edittext);
        delete_del_edittext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelTextTextCallBack != null) {
                    onDelTextTextCallBack.onDelete(input_del_edittext.getText().toString().trim());
                }
            }
        });
        this.setGravity(Gravity.CENTER);
        this.addView(view);
    }


    public interface OnDelTextTextCallBack {

        void onDelete(String content);
    }

}
