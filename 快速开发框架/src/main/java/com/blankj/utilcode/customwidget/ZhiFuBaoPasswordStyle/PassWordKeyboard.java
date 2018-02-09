package com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by leilei on 2017/10/12.
 */

public class PassWordKeyboard extends LinearLayout {
    public OnPassWordKeyboardCallBack onPassWordKeyboardCallBack;
    View view;
    RotateAnimation an;
    SimpleDraweeView iv_progress;
    PayEditText PayEditText_pay;
    Keyboard KeyboardView_pay;
    TickView progress_result;
    TextView tv_title;
    String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<<", "0", "完成"
    };

    public PassWordKeyboard(Context context) {
        super(context);
        init(context);
    }


    public PassWordKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PassWordKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PassWordKeyboard title(String title) {
        tv_title.setText(title);
        return this;
    }

    public PassWordKeyboard titleColor(int color) {
        tv_title.setTextColor(color);
        return this;
    }

    public void setOnPassWordKeyboardCallBack(OnPassWordKeyboardCallBack onPassWordKeyboardCallBack) {
        this.onPassWordKeyboardCallBack = onPassWordKeyboardCallBack;
    }

    /**
     * 设置loading的旋转图片
     *
     * @param context
     * @param res
     * @return
     */
    public PassWordKeyboard setLoadingDraw(Context context, int res) {
        FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + context.getPackageName() + "/" + res), iv_progress, 0f);
        return this;
    }

    /**
     * 设置结果动画的颜色
     *
     * @param res
     * @return
     */
    public PassWordKeyboard setColor(int res) {
        progress_result.setColor(res);
        return this;
    }

    /**
     * 自定义最后一颗按键的功能
     *
     * @param title
     * @return
     */
    public PassWordKeyboard customFunction(String title) {
        KEY[11] = title;
        return this;
    }

    /**
     * 重写键盘隐藏动画
     *
     * @param activity
     * @return
     */
    public PassWordKeyboard overridePendingTransition(Activity activity) {
        activity.overridePendingTransition(R.anim.keyboard_show, R.anim.keyboard_hide);
        return this;
    }

    /**
     * 设置键盘状态
     *
     * @param isSuccess
     */
    public void setStatus(boolean isSuccess) {
        showResult(isSuccess);
    }


    /**
     * 立即获取键盘中的密码(支持不需要等到6位密码全部输入完成)
     *
     * @return
     */
    public Keyboard getPassword() {
        //当点击完成的时候，PayEditText_pay.getText()获取密码，此时不应该注册OnInputFinishedListener接口
        if (!StringUtils.isEmpty(PayEditText_pay.getText().trim())) {
            showLoading(PayEditText_pay.getText());
        }
        return KeyboardView_pay;
    }

    /**
     * 初始化
     *
     * @param context
     */
    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.password_keyboard, null);
        PayEditText_pay = $.f(view, R.id.PayEditText_pay);
        KeyboardView_pay = $.f(view, R.id.KeyboardView_pay);
        iv_progress = $.f(view, R.id.iv_progress);
        tv_title = $.f(view, R.id.tv_title);
        FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.password_loading), iv_progress, 0f);
        KeyboardView_pay.setKeyboardKeys(KEY);
        progress_result = $.f(view, R.id.progress_result);
        progress_result.setColor(context.getResources().getColor(R.color.mainColor));
        PayEditText_pay.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                showLoading(password);
            }
        });
        KeyboardView_pay.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    PayEditText_pay.add(value);
                } else if (position == 9) {
                    PayEditText_pay.remove();
                } else if (position == 11) {
                    if (!"".equals(KEY[11])) {
                        if (onPassWordKeyboardCallBack != null) {
                            onPassWordKeyboardCallBack.onCustomFunction();
                        }
                    }

                }
            }
        });

        an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setInterpolator(new LinearInterpolator());
        an.setRepeatCount(-1);//重复次数
        an.setDuration(1000);
        addView(view);
    }

    /**
     * 显示密码是否正确
     *
     * @param isSuccess
     */
    void showResult(final boolean isSuccess) {
        iv_progress.clearAnimation();
        iv_progress.setVisibility(GONE);
        KeyboardView_pay.setVisibility(GONE);
        progress_result.setVisibility(View.VISIBLE);
        progress_result.setSuccess(isSuccess);
        progress_result.postDelayed(new Runnable() {
            @Override
            public void run() {
                PayEditText_pay.clear();
                if (isSuccess) {
                    if (onPassWordKeyboardCallBack != null) {
                        onPassWordKeyboardCallBack.onFinish();
                    }
                } else {
                    showKeyBoard();
                }
            }
        }, 3200);

    }

    /**
     * 显示旋转loading
     *
     * @param password
     */
    void showLoading(final String password) {
        iv_progress.setVisibility(VISIBLE);
        KeyboardView_pay.setVisibility(GONE);
        progress_result.setVisibility(GONE);
        iv_progress.startAnimation(an);
        iv_progress.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onPassWordKeyboardCallBack != null) {
                    onPassWordKeyboardCallBack.onPassword(password);
                }
            }
        }, 3000);
    }

    /**
     * 显示键盘
     */
    void showKeyBoard() {
        iv_progress.setVisibility(GONE);
        KeyboardView_pay.setVisibility(VISIBLE);
        progress_result.setVisibility(View.GONE);
    }

    public interface OnPassWordKeyboardCallBack {

        void onPassword(String pasword);

        void onFinish();

        void onCustomFunction();
    }

}
