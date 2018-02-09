package com.sss.car.view;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 好友管理==>隐私
 * Created by leilei on 2017/12/27.
 */

public class ActivityPrivacy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.sb_do_not_see_target)
    SwitchButton sbDoNotSeeTarget;
    @BindView(R.id.sb_do_not_see_me)
    SwitchButton sbDoNotSeeMe;
    @BindView(R.id.activity_privacy)
    LinearLayout activityPrivacy;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);
        titleTop.setText("隐私设置");
        customInit(activityPrivacy,false,true,false);
        sbDoNotSeeTarget.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                sbDoNotSeeTarget.setOpened(true);
                set_trends("1",sbDoNotSeeTarget);

            }

            @Override
            public void toggleToOff(SwitchButton view) {
                sbDoNotSeeTarget.setOpened(false);
                set_trends("1",sbDoNotSeeTarget);
            }
        });
        sbDoNotSeeMe.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                sbDoNotSeeMe.setOpened(true);
                set_trends(null,sbDoNotSeeMe);
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                sbDoNotSeeMe.setOpened(false);
                set_trends(null,sbDoNotSeeMe);
            }
        });
        get_trends();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
    /**
     * 隐私设置
     * @param type     (type:传1不看TA的 不传不让TA看我的)
     */
    void set_trends(final String type, final SwitchButton switchButton) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_trends(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type",type)
                            .put("friend_id",getIntent().getExtras().getString("friend_id"))
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (switchButton.isOpened()){
                                switchButton.setOpened(false);
                            }else {
                                switchButton.setOpened(true);
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {

                                    } else {
                                        if (switchButton.isOpened()){
                                            switchButton.setOpened(false);
                                        }else {
                                            switchButton.setOpened(true);
                                        }
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (switchButton.isOpened()){
                                        switchButton.setOpened(false);
                                    }else {
                                        switchButton.setOpened(true);
                                    }
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            if (switchButton.isOpened()){
                switchButton.setOpened(false);
            }else {
                switchButton.setOpened(true);
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取隐私
     */
    void get_trends() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_trends(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id",getIntent().getExtras().getString("friend_id"))
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
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        sbDoNotSeeTarget.setOpened("1".equals(jsonObject.getJSONObject("data").getString("remind_one")));
                                        sbDoNotSeeMe.setOpened("1".equals(jsonObject.getJSONObject("data").getString("remind_two")));
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {

                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {

            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
            e.printStackTrace();
        }
    }

}
