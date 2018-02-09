package com.sss.car.custom.GoodsTypeSelect.View;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.KeyboardUtils;
import com.sss.car.R;

/**
 * 监听EditText是否失去焦点
 * Created by leilei on 2017/10/28.
 */

public class FocusEditText extends LinearLayout {
    private View view;
    private EditText input_focus_edittext;
    private  OnFocusEditTextCallBack onFocusEditTextCallBack;
    private OnTextChanged onTextChanged;

    public void clear() {
        view = null;
        input_focus_edittext = null;
        onFocusEditTextCallBack = null;
    }

    public void setOnTextChanged(OnTextChanged onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public FocusEditText setOnFocusEditTextCallBack(OnFocusEditTextCallBack onFocusEditTextCallBack) {
        this.onFocusEditTextCallBack = onFocusEditTextCallBack;
        return this;
    }

    public FocusEditText(Context context) {
        super(context);
        init(context);
    }


    public FocusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.focus_edittext, null);
        input_focus_edittext = $.f(view, R.id.input_focus_edittext);

        input_focus_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            /*判断是否是“done”键*/
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        if ( !"".equals(input_focus_edittext.getText().toString().trim())) {
                            input_focus_edittext.setFocusable(false);
                            if (onFocusEditTextCallBack != null) {
                                onFocusEditTextCallBack.onLostFocus(input_focus_edittext.getText().toString().trim());
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        input_focus_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onTextChanged!=null){
                    onTextChanged.afterTextChanged(s.toString());
                }
            }
        });
        this.addView(view);
    }


    public FocusEditText setHint(String hint) {
        if (!"".equals(hint)) {
            if (input_focus_edittext != null) {
                input_focus_edittext.setHint(hint);
            }
        }

        return this;
    }

    public interface OnFocusEditTextCallBack {

        void onLostFocus(String content);
    }

    public interface OnTextChanged{

        void afterTextChanged(String str);

    }

}
