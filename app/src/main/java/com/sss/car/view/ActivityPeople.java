package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.view.ActivityUpdatePic.One;
import static com.sss.car.view.ActivityUpdatePic.Three;
import static com.sss.car.view.ActivityUpdatePic.Two;

/**
 * 个人认证
 * Created by leilei on 2017/11/8.
 */

public class ActivityPeople extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.name_activity_people)
    LinearLayout nameActivityPeople;
    @BindView(R.id.id_card_activity_people)
    LinearLayout idCardActivityPeople;
    @BindView(R.id.top_activity_people)
    LinearLayout topActivityPeople;
    @BindView(R.id.back_activity_people)
    LinearLayout backActivityPeople;
    @BindView(R.id.hold_activity_people)
    LinearLayout holdActivityPeople;
    @BindView(R.id.activity_people)
    LinearLayout activityPeople;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.show_user_name_activity_people)
    TextView showUserNameActivityPeople;
    @BindView(R.id.show_id_card_activity_people)
    TextView showIdCardActivityPeople;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        ButterKnife.bind(this);
        titleTop.setText("个人认证");
        customInit(activityPeople, false, true, true);
        get_personage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "UserName":
                if (!StringUtils.isEmpty(event.msg)) {
                    showUserNameActivityPeople.setText(event.msg);
                    set_personage("name", event.msg);
                }
                break;
            case "idCard":
                if (!StringUtils.isEmpty(event.msg)) {
                    showIdCardActivityPeople.setText(event.msg);
                    set_personage("card", event.msg);
                }
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.name_activity_people, R.id.id_card_activity_people, R.id.top_activity_people, R.id.back_activity_people, R.id.hold_activity_people})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.name_activity_people:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "UserName")
                            .putExtra("canChange", true)
                            .putExtra("extra", showUserNameActivityPeople.getText().toString().trim()));
                }
                break;
            case R.id.id_card_activity_people:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "idCard")
                            .putExtra("canChange", true)
                            .putExtra("extra", showIdCardActivityPeople.getText().toString().trim()));
                }
                break;
            case R.id.top_activity_people:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityUpdatePic.class)
                            .putExtra("mode", One));
                }
                break;
            case R.id.back_activity_people:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityUpdatePic.class)
                            .putExtra("mode", Two));
                }
                break;
            case R.id.hold_activity_people:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityUpdatePic.class)
                            .putExtra("mode", Three));
                }
                break;
        }
    }

    /**
     * 获取个人认证资料
     */
    public void get_personage() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_personage(
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
                                    showUserNameActivityPeople.setText(jsonObject.getJSONObject("data").getString("name"));
                                    showIdCardActivityPeople.setText(jsonObject.getJSONObject("data").getString("card"));

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

    /**
     * 设置个人认证资料
     */
    public void set_personage(String key, String value) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_personage(
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

}
