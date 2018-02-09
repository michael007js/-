package com.blankj.utilcode.customwidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.KeyboardUtils;


/**
 * Created by leilei on 2017/9/1.
 */

public class KeyboardInput extends RelativeLayout {
    KeyboardInputOperationCallBack keyboardInputOperationCallBack;
    View view;
    EditText editText;
    TextView send;
    Context context;

    public void clear() {
        keyboardInputOperationCallBack = null;
        view = null;
        editText = null;
        context = null;
        context = null;
    }

    public KeyboardInput(Context context) {
        super(context);
        init(context);
    }

    public KeyboardInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getSend() {
        return send;
    }

    void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.keybroad_layout, null);
        editText = $.f(view, R.id.input_keyboard_layout);
        editText.setHintTextColor(getResources().getColor(R.color.line));
        editText.setTextSize(13f);
        editText.setPadding(10, 0, 10, 0);
        send = $.f(view, R.id.send_keyboard_layout);

        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardInputOperationCallBack != null) {
                    keyboardInputOperationCallBack.onSend(editText.getText().toString().trim(), send, editText);
                    KeyboardUtils.hideSoftInput(KeyboardInput.this.context, editText);
                }
            }
        });
        addView(view);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 85));
    }

    public KeyboardInput setTitle(String title) {
        if (send != null) {
            send.setText(title);
        }
        return this;
    }

    public KeyboardInput setBackGround(Drawable drawable) {
        APPOftenUtils.setBackgroundOfVersion(view, drawable);
        return this;
    }

    public KeyboardInput setKeyboardInputOperationCallBack(KeyboardInputOperationCallBack keyboardInputOperationCallBack) {
        this.keyboardInputOperationCallBack = keyboardInputOperationCallBack;
        return this;
    }

    public KeyboardInput setHintText(String hintText) {
        if (editText != null) {
            editText.setHint(hintText);
        }
        return this;
    }

    public KeyboardInput setHintTextColor(int color) {
        if (editText != null) {
            editText.setHintTextColor(color);
        }
        return this;
    }

    public KeyboardInput setTextSize(float size) {
        if (editText != null) {
            editText.setTextSize(size);
        }
        return this;
    }

    public KeyboardInput setTextColor(int color) {
        if (editText != null) {
            editText.setTextColor(color);
        }
        return this;
    }

    public void showKeyBoard() {
        KeyboardUtils.showSoftInput(context, editText);
    }

    public void hideKeyBoard() {
        KeyboardUtils.hideSoftInput(context, editText);
    }

    public void visibility(int visibility) {
        this.setVisibility(visibility);
        if (visibility == VISIBLE) {
            KeyboardUtils.showSoftInput(context, editText);
        }
    }

    public interface KeyboardInputOperationCallBack {
        void onSend(String content, TextView textView, EditText editText);
    }

}
