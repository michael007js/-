package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SettingModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/12/26.
 */

public class ActivityShareInteractionManageSetting extends BaseActivity implements SwitchButton.OnStateChangedListener {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.sb_phone_book)
    SwitchButton sbPhoneBook;
    @BindView(R.id.sb_attention)
    SwitchButton sbAttention;
    @BindView(R.id.sb_greeting)
    SwitchButton sbGreeting;
    @BindView(R.id.click_login_status)
    LinearLayout clickLoginStatus;
    @BindView(R.id.sb_friend_see)
    SwitchButton sbFriendSee;
    @BindView(R.id.sb_message_details)
    SwitchButton sbMessageDetails;
    @BindView(R.id.click_special_attention)
    LinearLayout clickSpecialAttention;
    @BindView(R.id.click_do_not_see_target)
    LinearLayout clickDoNotSeeTarget;
    @BindView(R.id.click_do_not_see_me)
    LinearLayout clickDoNotSeeMe;
    @BindView(R.id.click_black)
    LinearLayout clickBlack;
    @BindView(R.id.activity_share_interaction_manage_setting)
    LinearLayout activityShareInteractionManageSetting;
    YWLoadingDialog ywLoadingDialog;
    SettingModel settingModel = new SettingModel();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_interaction_manage_setting);
        ButterKnife.bind(this);
        titleTop.setText("设置");
        customInit(activityShareInteractionManageSetting, false, true, false);
        getUsinfo();
        sbPhoneBook.setOnStateChangedListener(this);
        sbAttention.setOnStateChangedListener(this);
        sbGreeting.setOnStateChangedListener(this);
        sbFriendSee.setOnStateChangedListener(this);
        sbMessageDetails.setOnStateChangedListener(this);
    }



    @OnClick({R.id.back_top, R.id.click_login_status, R.id.click_special_attention, R.id.click_do_not_see_target, R.id.click_do_not_see_me, R.id.click_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_login_status:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityOnlineMode.class)
                    .putExtra("online",settingModel.online));
                }
                break;
            case R.id.click_special_attention:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityShareInteractionManageSettingPeopleManage.class)
                            .putExtra("title","特别关注")
                    .putExtra("type","1"));
                }
                break;
            case R.id.click_do_not_see_target:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityShareInteractionManageSettingPeopleManage.class)
                            .putExtra("title","不看TA的动态").putExtra("type","2"));
                }
                break;
            case R.id.click_do_not_see_me:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityShareInteractionManageSettingPeopleManage.class)
                            .putExtra("title","不让TA看我的动态").putExtra("type","3"));
                }
                break;
            case R.id.click_black:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityShareInteractionManageSettingPeopleManage.class)
                            .putExtra("title","黑名单").putExtra("type","4"));
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsinfo();
    }

    /**
     * 获取用户设置资料
     */
    public void getUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    settingModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SettingModel.class);
                                    showData();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    void showData() {
        sbPhoneBook.setOpened("1".equals(settingModel.address_book));
        sbAttention.setOpened("1".equals(settingModel.attention));
        sbGreeting.setOpened("1".equals(settingModel.stranger));
        sbFriendSee.setOpened("1".equals(settingModel.trends));
        sbMessageDetails.setOpened("1".equals(settingModel.messages));
    }

    /**
     * 设置用户设置资料
     */
    public void setUsinfo(String key, String value, final SwitchButton switchButton) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put(key, value)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                            if (switchButton != null) {
                                if (switchButton.isOpened()) {
                                    switchButton.setOpened(false);
                                } else {
                                    switchButton.setOpened(true);
                                }
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    if (switchButton != null) {
                                        if (switchButton.isOpened()) {
                                            switchButton.setOpened(false);
                                        } else {
                                            switchButton.setOpened(true);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                if (switchButton != null) {
                                    if (switchButton.isOpened()) {
                                        switchButton.setOpened(false);
                                    } else {
                                        switchButton.setOpened(true);
                                    }
                                }
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            if (switchButton.isOpened()) {
                switchButton.setOpened(false);
            } else {
                switchButton.setOpened(true);
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


    @Override
    public void toggleToOn(SwitchButton view) {
        switch (view.getId()) {
            case R.id.sb_phone_book:
                view.setOpened(true);
                setUsinfo("address_book", "1", view);
                break;
            case R.id.sb_attention:
                view.setOpened(true);
                setUsinfo("attention", "1", view);
                break;
            case R.id.sb_greeting:
                view.setOpened(true);
                setUsinfo("stranger", "1", view);
                break;
            case R.id.sb_friend_see:
                view.setOpened(true);
                setUsinfo("trends", "1", view);
                break;
            case R.id.sb_message_details:
                view.setOpened(true);
                setUsinfo("messages", "1", view);
                break;
        }

    }

    @Override
    public void toggleToOff(SwitchButton view) {
        switch (view.getId()) {
            case R.id.sb_phone_book:
                view.setOpened(false);
                setUsinfo("address_book", "0", view);
                break;
            case R.id.sb_attention:
                view.setOpened(false);
                setUsinfo("attention", "0", view);
                break;
            case R.id.sb_greeting:
                view.setOpened(false);
                setUsinfo("stranger", "0", view);
                break;
            case R.id.sb_friend_see:
                view.setOpened(false);
                setUsinfo("trends", "0", view);
                break;
            case R.id.sb_message_details:
                view.setOpened(false);
                setUsinfo("messages", "0", view);
                break;
        }
    }
}
