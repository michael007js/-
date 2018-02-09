package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/11/4.
 */

public class ActivityLogin extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.pic_activity_login)
    ImageView picActivityLogin;
    @BindView(R.id.account_activity_login)
    EditText accountActivityLogin;
    @BindView(R.id.password_activity_login)
    EditText passwordActivityLogin;
    @BindView(R.id.forget_activity_login)
    TextView forgetActivityLogin;
    @BindView(R.id.login_activity_login)
    TextView loginActivityLogin;
    @BindView(R.id.register_activity_login)
    TextView registerActivityLogin;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        picActivityLogin = null;
        accountActivityLogin = null;
        passwordActivityLogin = null;
        forgetActivityLogin = null;
        loginActivityLogin = null;
        registerActivityLogin = null;
        activityLogin = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        titleTop.setText("登录");
        if ("show".equals(getIntent().getExtras().getString("isShowBack"))){

        }
    }

    @OnClick({R.id.back_top, R.id.forget_activity_login, R.id.login_activity_login, R.id.register_activity_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.forget_activity_login:
                break;
            case R.id.login_activity_login:
                break;
            case R.id.register_activity_login:
                break;
        }
    }
}
