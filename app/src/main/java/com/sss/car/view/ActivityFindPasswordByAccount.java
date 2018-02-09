package com.sss.car.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.CodeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.FindPassword;
import com.sss.car.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/8/16.
 */

public class ActivityFindPasswordByAccount extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.mobile_find_password_by_account)
    EditText mobileFindPasswordByAccount;
    @BindView(R.id.code_find_password_by_account)
    EditText codeFindPasswordByAccount;
    @BindView(R.id.get_code_find_password_by_account)
    ImageView getCodeFindPasswordByAccount;
    @BindView(R.id.next_find_password_by_account)
    TextView nextFindPasswordByAccount;
    @BindView(R.id.by_sms_find_password_by_account)
    TextView bySMSFinfPasswordByAccount;
    @BindView(R.id.find_password_by_account)
    LinearLayout findPasswordByAccount;
    String code = "";

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        mobileFindPasswordByAccount = null;
        codeFindPasswordByAccount = null;
        getCodeFindPasswordByAccount = null;
        nextFindPasswordByAccount = null;
        bySMSFinfPasswordByAccount = null;
        findPasswordByAccount = null;
        code = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_by_account);
        ButterKnife.bind(this);
        customInit(findPasswordByAccount, false, true, true);
        createCode();
        titleTop.setText("重置登录密码");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FindPassword event) {
        finish();
    }

    /**
     * 生成验证码
     */
    void createCode() {
        CodeUtils.getInstance().createBitmapAndCode(new CodeUtils.CreateImageVerificationCodeCallBack() {
            @Override
            public void onCreateBitmap(Bitmap bitmap) {
                if (getBaseActivityContext() != null) {
                    addImageViewList(GlidUtils.glidLoad(false, getCodeFindPasswordByAccount, getBaseActivityContext(), bitmap));
                }
            }

            @Override
            public void onCreateString(String code) {
                ActivityFindPasswordByAccount.this.code = code;
            }
        });
    }

    @OnClick({R.id.back_top, R.id.get_code_find_password_by_account, R.id.next_find_password_by_account, R.id.by_sms_find_password_by_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.get_code_find_password_by_account:
                createCode();
                break;
            case R.id.next_find_password_by_account:
                next();
                break;
            case R.id.by_sms_find_password_by_account:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityFindPasswordBySMS.class));
                }
                break;
        }
    }

    /**
     * 下一步
     */
    void next() {
        if (StringUtils.isEmpty(bySMSFinfPasswordByAccount.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "手机号为空");
            }
            return;
        }
        if (!code.equals(codeFindPasswordByAccount.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "验证码不正确");
            }
            return;
        }
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityFindPasswordSetPassword.class)
                    .putExtra("type", "account")
                    .putExtra("extra", bySMSFinfPasswordByAccount.getText().toString().trim()));
        }


    }
}
