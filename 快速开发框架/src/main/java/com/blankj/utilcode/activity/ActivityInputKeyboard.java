package com.blankj.utilcode.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.EventbusModel.SendMessageFromActivityInputKeyBoard;
import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.customwidget.bilibili_search_box.utils.KeyBoardUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by leilei on 2017/10/19.
 */

/*

        step1
        <activity
            android:name="com.blankj.utilcode.activity.ActivityInputKeyboard"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"/>



            step2
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityInputKeyboard.class)
                            .putExtra("titleText", "回帖")
                            .putExtra("type", "posts"));
                }


                step3
                  @Subscribe(threadMode = ThreadMode.MAIN)
                    public void onMessageEvent(SendMessageFromActivityInputKeyBoard model) {
                        if ("posts".equals(model.type)) {
                        model.content.....
                        }
                    }
*
* */
@SuppressWarnings("ALL")
public class ActivityInputKeyboard extends Activity implements View.OnClickListener {
    RelativeLayout activity_input_keyboard;

    TextView cancel, ok, title;
    EditText content;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_keyboard);

        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(this, "输入类型错误!");
            finish();
        }

        cancel = (TextView) findViewById(R.id.cancel);
        ok = (TextView) findViewById(R.id.ok);
        title = (TextView) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        title.setText(getIntent().getExtras().getString("titleText"));
        activity_input_keyboard = $.f(this, R.id.activity_input_keyboard);
        activity_input_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isSoftShowing(ActivityInputKeyboard.this)) {
                    KeyBoardUtils.closeKeyboard(ActivityInputKeyboard.this, content);
                } else {
                    finish();
                }
            }
        });
        ok.setOnClickListener(this);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isSoftShowing(ActivityInputKeyboard.this)) {
                    KeyBoardUtils.closeKeyboard(ActivityInputKeyboard.this, content);
                }
                finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MENU:
                finish();
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            case KeyEvent.KEYCODE_HOME:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (StringUtils.isEmpty(content.getText().toString().trim())){
            return;
        }
        EventBus.getDefault().post(new SendMessageFromActivityInputKeyBoard(content.getText().toString(), getIntent().getExtras().getString("type")));
        finish();
    }
}
