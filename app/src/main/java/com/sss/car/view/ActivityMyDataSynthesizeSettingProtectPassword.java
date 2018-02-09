package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SafetyPasswordModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 综合设置==>密保
 * Created by leilei on 2017/11/6.
 */

public class ActivityMyDataSynthesizeSettingProtectPassword extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.submit_activity_my_data_synthesize_setting_protect_password)
    TextView submitActivityMyDataSynthesizeSettingProtectPassword;
    @BindView(R.id.activity_my_data_synthesize_setting_protect_password)
    LinearLayout activityMyDataSynthesizeSettingProtectPassword;
    YWLoadingDialog ywLoadingDialog;
    List<SafetyPasswordModel> list = new ArrayList<>();

    SSS_Adapter sss_adapterOne, sss_adapterTwo;
    @BindView(R.id.spinner_one_activity_my_data_synthesize_setting_protect_password)
    Spinner spinnerOneActivityMyDataSynthesizeSettingProtectPassword;
    @BindView(R.id.input_one_activity_my_data_synthesize_setting_protect_password)
    EditText inputOneActivityMyDataSynthesizeSettingProtectPassword;
    @BindView(R.id.spinner_two_activity_my_data_synthesize_setting_protect_password)
    Spinner spinnerTwoActivityMyDataSynthesizeSettingProtectPassword;
    @BindView(R.id.input_two_activity_my_data_synthesize_setting_protect_password)
    EditText inputTwoActivityMyDataSynthesizeSettingProtectPassword;

    String id_one, id_two;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        submitActivityMyDataSynthesizeSettingProtectPassword = null;
        spinnerOneActivityMyDataSynthesizeSettingProtectPassword = null;
        inputOneActivityMyDataSynthesizeSettingProtectPassword = null;
        spinnerTwoActivityMyDataSynthesizeSettingProtectPassword = null;
        inputTwoActivityMyDataSynthesizeSettingProtectPassword = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_synthesize_setting_protect_question);
        ButterKnife.bind(this);
        customInit(activityMyDataSynthesizeSettingProtectPassword, false, true, false);
        titleTop.setText("密保问题");
        initAdapter();
        getSafety();
    }

    void initAdapter() {
        sss_adapterOne = new SSS_Adapter<SafetyPasswordModel>(getBaseActivityContext(), R.layout.item_safe_question_adapter, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, SafetyPasswordModel bean, SSS_Adapter instance) {
                helper.setText(R.id.question_item_safe_question_adapter, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        spinnerOneActivityMyDataSynthesizeSettingProtectPassword.setAdapter(sss_adapterOne);
        spinnerOneActivityMyDataSynthesizeSettingProtectPassword.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_one = list.get(position).safety_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sss_adapterTwo = new SSS_Adapter<SafetyPasswordModel>(getBaseActivityContext(), R.layout.item_safe_question_adapter, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, SafetyPasswordModel bean, SSS_Adapter instance) {
                helper.setText(R.id.question_item_safe_question_adapter, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        spinnerTwoActivityMyDataSynthesizeSettingProtectPassword.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_two = list.get(position).safety_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTwoActivityMyDataSynthesizeSettingProtectPassword.setAdapter(sss_adapterTwo);
    }

    /**
     * 获取用户设置资料
     */
    public void getSafety() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getSafety(
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(), SafetyPasswordModel.class));
                                    }
                                    sss_adapterOne.setList(list);
                                    sss_adapterTwo.setList(list);
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
     * 获取用户设置资料
     */
    public void setSafety() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray question=new JSONArray();
        question.put(id_one);
        question.put(id_two);

        JSONArray answer=new JSONArray();
        answer.put(inputOneActivityMyDataSynthesizeSettingProtectPassword.getText().toString().trim());
        answer.put(inputTwoActivityMyDataSynthesizeSettingProtectPassword.getText().toString().trim());

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setSafety(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("safety_id",question)
                            .put("answer",answer)
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
                                    finish();
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

    @OnClick({R.id.back_top, R.id.submit_activity_my_data_synthesize_setting_protect_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.submit_activity_my_data_synthesize_setting_protect_password:
                if (StringUtils.isEmpty(id_one) || StringUtils.isEmpty(id_two) || StringUtils.isEmpty(inputOneActivityMyDataSynthesizeSettingProtectPassword.getText().toString().trim()) || StringUtils.isEmpty(inputTwoActivityMyDataSynthesizeSettingProtectPassword.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择密保问题或填写答案");
                    return;
                }

                if (id_one.equals(id_two)){
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择不同的密保问题");
                    return;
                }
                setSafety();
                break;
        }
    }
}
