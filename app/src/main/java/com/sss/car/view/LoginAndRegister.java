package com.sss.car.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.CustomExceptionCallBack;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.dao.Webbiz;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.HttpRequestLib.HttpRequestUtils;
import com.blankj.utilcode.HttpRequestLib.dao.IDataListener;
import com.sss.car.Config;
import com.sss.car.EventBusModel.RegisterComplete;
import com.sss.car.EventBusModel.RegisterModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.HideShowButton;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.CarUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.imlib.RongIMClient;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/8.
 */

public class LoginAndRegister extends BaseActivity implements CustomExceptionCallBack {
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
    YWLoadingDialog ywLoadingDialog;
    List<PermissionItem> permissionItemList = new ArrayList<>();
    @BindView(R.id.eyes)
    HideShowButton eyes;
    SPUtils spUtils;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (permissionItemList != null) {
            permissionItemList.clear();
        }
        permissionItemList = null;
        backTop = null;
        titleTop = null;
        picActivityLogin = null;
        accountActivityLogin = null;
        passwordActivityLogin = null;
        forgetActivityLogin = null;
        loginActivityLogin = null;
        registerActivityLogin = null;
        activityLogin = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarUtils.startAdvertisement(getBaseActivityContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }

        spUtils = new SPUtils(getBaseActivityContext(), Config.defaultFileName, Context.MODE_PRIVATE);
        if (getIntent().getExtras().getBoolean("isClearUserInfo")) {
            spUtils.put("password", "");
            accountActivityLogin.setText("");
            passwordActivityLogin.setText("");
        }
        if (!StringUtils.isEmpty(spUtils.getString("account"))) {
            accountActivityLogin.setText(spUtils.getString("account"));
        }
        if (!StringUtils.isEmpty(spUtils.getString("password"))) {
            passwordActivityLogin.setText(spUtils.getString("password"));
        }
        req();
        if (!StringUtils.isEmpty(spUtils.getString("account")) && !StringUtils.isEmpty(spUtils.getString("password"))) {
            LogUtils.e("123");
            if (!getIntent().getExtras().getBoolean("KICKED_OFFLINE_BY_OTHER_CLIENT")){//用户账户在其他设备登录，本机会被踢掉线
                if (Config.license == false) {
                    requestAPPLicense(true);
                }
            }else {
                requestAPPLicense(false);
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "您的账号在其他设备上登陆，您已被迫下线！", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog=null;
                    }
                });
            }
        }else {
            LogUtils.e("456");
            requestAPPLicense(false);
        }


        titleTop.setText("登录");
        if (getIntent().getExtras().getBoolean("isShowBack")) {
            backTop.setVisibility(View.VISIBLE);
        } else {
            backTop.setVisibility(View.GONE);
        }
        customInit(null, false, false, true);

        LogUtils.e(JPushInterface.getRegistrationID(this));
        eyes.setOnHideShowButtonCallBack(new HideShowButton.OnHideShowButtonCallBack() {
            @Override
            public void onHideShowButtonClick(boolean isHide) {
                eyes.changed(60, 60, getBaseActivityContext());
                if (isHide) {
                    passwordActivityLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordActivityLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @OnClick({R.id.back_top, R.id.forget_activity_login, R.id.login_activity_login, R.id.register_activity_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.forget_activity_login:
                if (Config.license == false) {
                    requestAPPLicense(false);
                    return;
                }
                startActivity(new Intent(this, ActivityFindPassword.class)
                        .putExtra("mode", ActivityFindPassword.login));
                break;
            case R.id.login_activity_login:
                if (StringUtils.isEmpty(accountActivityLogin.getText()) || StringUtils.isEmpty(passwordActivityLogin.getText())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "账号或密码为空");
                    return;
                }
                if (Config.license == false) {
                    requestAPPLicense(false);
                    return;
                }
                login();
//                HttpRequestUtils.doGet(10000, "http://apis.juhe.cn/lottery/types", new IDataListener() {
//                    @Override
//                    public void onSuccess(String response) {
//                        ToastUtils.showShortToast(getBaseActivityContext(),response);
//                    }
//
//                    @Override
//                    public void onFail(int responseCode, String responseMessage) {
//                        ToastUtils.showShortToast(getBaseActivityContext(),responseCode+"---"+responseMessage);
//                    }
//                });
                break;
            case R.id.register_activity_login:
                if (Config.license == false) {
                    requestAPPLicense(false);
                    return;
                }
                startActivity(new Intent(this, ActivityRegister.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterModel registerModel){
        accountActivityLogin.setText(registerModel.mobile);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterComplete event) {
        finish();
    }

    public void login() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ywLoadingDialog != null) {
                    ywLoadingDialog.disMiss();
                }
                ywLoadingDialog=null;
                ywLoadingDialog=new YWLoadingDialog(LoginAndRegister.this);
                ywLoadingDialog.show();
            }
        });

        try {
            addRequestCall(new RequestModel(getLocalClassName(), RequestWeb.login(new JSONObject()
                    .put("device_number", DeviceUtils.getUUID(getBaseActivityContext()))
                    .put("mobile", accountActivityLogin.getText().toString())
                    .put("password", passwordActivityLogin.getText().toString())
                    .toString(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ywLoadingDialog=null;
                        }
                    });
                }

                @Override
                public void onResponse(String response, int id) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ywLoadingDialog=null;
                        }
                    });
                    try {
                        final JSONObject jsonObject = new JSONObject(response);

                        if ("1".equals(jsonObject.getString("status"))) {
                            Config.member_id = jsonObject.getJSONObject("data").getString("member_id");
                            Config.nikename = jsonObject.getJSONObject("data").getString("username");
                            Config.face = jsonObject.getJSONObject("data").getString("face");
                            Config.flash = jsonObject.getJSONObject("data").getInt("flash")*1000;
                            Config.mobile = jsonObject.getJSONObject("data").getString("mobile");
                            Config.token = jsonObject.getJSONObject("data").getString("token");
                            Config.account = jsonObject.getJSONObject("data").getString("account");
                            if (spUtils!=null){
                                spUtils.put("account",accountActivityLogin.getText().toString().trim());
                                spUtils.put("password",passwordActivityLogin.getText().toString().trim());
                            }
                            RongYunUtils.connect(Config.token, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    if (getBaseActivityContext() != null) {
                                        startActivity(new Intent(getBaseActivityContext(), Main.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(final RongIMClient.ErrorCode errorCode) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Looper.prepare();
                                                ToastUtils.showShortToast(getBaseActivityContext(), "error:" + errorCode);
                                                Looper.loop();
                                            } catch (RuntimeException e) {
                                                ToastUtils.showShortToast(getBaseActivityContext(), "Only one Looper may be created per thread");
                                            }
                                        }
                                    });
                                    LogUtils.e(errorCode);
                                }

                                @Override
                                public void onTokenIncorrect() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showShortToast(getBaseActivityContext(), "onTokenIncorrect");
                                        }
                                    });
                                }
                            });
                        } else {
                            spUtils.put("password","");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ywLoadingDialog != null) {
                                    ywLoadingDialog.disMiss();
                                }
                                ywLoadingDialog=null;
                            }
                        });
                        e.printStackTrace();
                    }
                }
            })));
        } catch (JSONException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog=null;
                }
            });
            e.printStackTrace();
        }
    }


    /**
     * 请求APP许可
     * @param isLogin
     */
    public void requestAPPLicense(final boolean isLogin) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() == null) {
            onEmptyException("requestAPPLicense,context is null");
        } else {
            if (getBaseActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                ywLoadingDialog.show();
                ywLoadingDialog.setTitle("请稍候...");
            }
        }
        try {
            new RequestModel(getLocalClassName(), Webbiz.requestAPPLicense(getLocalClassName(), getBaseActivityContext(), "1", new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ToastUtils.showShortToast(LoginAndRegister.this, "Error:" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if ("1".equals(jsonObject.getString("status"))) {
                            Config.license = true;
//                            ToastUtils.showShortToast(LoginAndRegister.this, jsonObject.getString("message"));
                            if (isLogin){
                                login();
                            }
                        } else {
                            ToastUtils.showShortToast(LoginAndRegister.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        ToastUtils.showShortToast(LoginAndRegister.this, "Error:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, LoginAndRegister.this));

        } catch (JSONException e) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 请求权限
     */
    protected void req() {

        //访问网络连接
        permissionItemList.add(new PermissionItem(PermissionUtils.INTERNET, "访问网络连接", com.blankj.utilcode.R.drawable.permission));
        //获取WiFi状态，获取当前WiFi接入的状态以及WLAN热点的信息，wifi信息会用于进行网络定位
        permissionItemList.add(new PermissionItem(PermissionUtils.ACCESS_WIFI_STATE, "获取WiFi状态", com.blankj.utilcode.R.drawable.permission));
        //用于进行网络定位
        permissionItemList.add(new PermissionItem(PermissionUtils.ACCESS_COARSE_LOCATION, "网络定位", com.blankj.utilcode.R.drawable.permission));
        //用于访问GPS定位
        permissionItemList.add(new PermissionItem(PermissionUtils.ACCESS_FINE_LOCATION, "访问GPS定位", com.blankj.utilcode.R.drawable.permission));
        //用于获取运营商信息，用于支持提供运营商信息相关的接口
        permissionItemList.add(new PermissionItem(PermissionUtils.ACCESS_NETWORK_STATE, "获取运营商信息", com.blankj.utilcode.R.drawable.permission));
        //电话权限
        permissionItemList.add(new PermissionItem(PermissionUtils.CALL_PHONE, "电话权限", com.blankj.utilcode.R.drawable.permission));
        //用于获取wifi权限，wifi信息会用来进行网络定位
        permissionItemList.add(new PermissionItem(PermissionUtils.CHANGE_WIFI_STATE, "获取wifi权限", com.blankj.utilcode.R.drawable.permission));
        //用于读取手机当前的状态
        permissionItemList.add(new PermissionItem(PermissionUtils.READ_PHONE_STATE, "读取手机状态", com.blankj.utilcode.R.drawable.permission));
        //用于写入缓存数据到扩展存储卡
        permissionItemList.add(new PermissionItem(PermissionUtils.WRITE_EXTERNAL_STORAGE, "读写权限", com.blankj.utilcode.R.drawable.permission));
        //用于申请获取蓝牙信息进行室内定位
        permissionItemList.add(new PermissionItem(PermissionUtils.BLUETOOTH, "获取蓝牙定位", com.blankj.utilcode.R.drawable.permission));
        //蓝牙管理
        permissionItemList.add(new PermissionItem(PermissionUtils.BLUETOOTH_ADMIN, "蓝牙管理", com.blankj.utilcode.R.drawable.permission));
        //访问摄像头
        permissionItemList.add(new PermissionItem(PermissionUtils.CAMERA, "访问网摄像头", com.blankj.utilcode.R.drawable.permission));
        //唤醒
        permissionItemList.add(new PermissionItem(PermissionUtils.WAKE_LOCK, "唤醒", com.blankj.utilcode.R.drawable.permission));
        //读取磁盘
        permissionItemList.add(new PermissionItem(PermissionUtils.READ_EXTERNAL_STORAGE, "读取磁盘", com.blankj.utilcode.R.drawable.permission));
        //震动
        permissionItemList.add(new PermissionItem(PermissionUtils.VIBRATE, "震动", com.blankj.utilcode.R.drawable.permission));
        //调用A-GPS模块
        permissionItemList.add(new PermissionItem(PermissionUtils.ACCESS_LOCATION_EXTRA_COMMANDS, "调用A-GPS模块", com.blankj.utilcode.R.drawable.permission));

        requestPermissions(permissionItemList, new PermissionCallback() {
            @Override
            public void onClose() {
                if (getBaseActivityContext() != null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您未授权必要的权限,页面被关闭");
                }
                finish();
            }

            @Override
            public void onFinish() {
                if (getBaseActivityContext() != null) {
                    APPOftenUtils.initNotificationServiceListener(weakReference, LoginAndRegister.this);
                }
                if (Config.license == false) {
                    requestAPPLicense(false);
                    return;
                }
            }

            @Override
            public void onDeny(String permission, int position) {
                ToastUtils.showShortToast(getBaseActivityContext(), "您未授权必要的权限,页面被关闭");
                finish();
                LogUtils.e("onDeny" + permission + position);
            }

            @Override
            public void onGuarantee(String permission, int position) {
//                ToastUtils.showShortToast(getBaseActivityContext(), "onGuarantee");
                LogUtils.e("onGuarantee" + permission + position);
            }
        });
    }


    @Override
    public void onEmptyException(String errorMsg) {
        LogUtils.e(errorMsg);
        if (getBaseActivityContext() != null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + errorMsg);
        }
    }

    @Override
    public void onNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        if (getBaseActivityContext() != null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
        }
    }

    @Override
    public void onUnsupportedEncodingException(UnsupportedEncodingException e) {
        if (getBaseActivityContext() != null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
        }
    }
}
