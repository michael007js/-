package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/8/15.
 */

public class ActivityChangeInfo extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.activity_change_info)
    LinearLayout activityChangeInfo;
    @BindView(R.id.edit_activity_change_info)
    EditText editActivityChangeInfo;
    String type = "";
    JSONObject send;
    boolean isChanged = false;
    boolean canChange = true;
    ChangeInfoModel changeUserInfoModel;

    @Override
    protected void onDestroy() {
        type = null;
        send = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        activityChangeInfo = null;
        editActivityChangeInfo = null;
        changeUserInfoModel = null;
        super.onDestroy();
    }

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");

            }
            finish();
        }
        customInit(activityChangeInfo, false, true, false);
        type = getIntent().getExtras().getString("type");
        canChange = getIntent().getExtras().getBoolean("canChange");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        switch (type) {
            case "username":
                titleTop.setText("真实姓名");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra") + "");
                break;
            case "nikename":
                titleTop.setText("昵称");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra") + "");
                break;
            case "compay":
                titleTop.setText("店铺名称");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra") + "");
                break;
            case "compayPeopleName":
                titleTop.setText("店铺法人");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra") + "");
                break;
            case "compayAddress":
                titleTop.setText("店铺地址");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "shopName":
                titleTop.setText("商铺名称");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                rightButtonTop.setTextColor(getResources().getColor(R.color.black));
                break;
            case "shopAdress":
                titleTop.setText("商铺地址");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                rightButtonTop.setTextColor(getResources().getColor(R.color.black));
                break;
            case "shopPro":
                titleTop.setText("商铺简介");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                rightButtonTop.setTextColor(getResources().getColor(R.color.black));
                break;
            case "groupName":
                titleTop.setText("群组名称");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "groupAnnouncement":
                titleTop.setText("群公告");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "groupMyNikename":
                titleTop.setText("我在本群的昵称");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "other":
                titleTop.setText("其他要求");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "expressageCompany":
                titleTop.setText("快递公司");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "expressageCode":
                titleTop.setText("快递单号");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                editActivityChangeInfo.setKeyListener(new NumberKeyListener() {

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_CLASS_NUMBER;
                    }

                    protected char[] getAcceptedChars() {
                        char numberChars[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
                        return numberChars;

                    }

                });
                editActivityChangeInfo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String editable = editActivityChangeInfo.getText().toString();
                        String regEx = "[^0-9]";  //只能输入字母或数字
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(editable);
                        String str = m.replaceAll("").trim();    //删掉不是字母或数字的字符
                        if (!editable.equals(str)) {
                            editActivityChangeInfo.setText(str);  //设置EditText的字符
                            editActivityChangeInfo.setSelection(str.length()); //因为删除了字符，要重写设置新的光标所在位置
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case "returnAndChangeReason":
                titleTop.setText("退换原因");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "result":
                titleTop.setText("卖家反馈");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "fault":
                titleTop.setText("求助故障");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "UserName":
                titleTop.setText("真实姓名");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "idCard":
                titleTop.setText("身份证号");
                editActivityChangeInfo.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "code":
                titleTop.setText("信用代码");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "returnAndChangeReason_Code":
                titleTop.setText("快递单号");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "returnAndChangeReason_Company":
                titleTop.setText("快递公司");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "year":
                titleTop.setText("生产年份");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "displacement":
                titleTop.setText("发动机排量");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "style":
                titleTop.setText("车款");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "pai":
                titleTop.setText("汽车品牌");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "xi":
                titleTop.setText("汽车系列");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;
            case "service_username":
                titleTop.setText("设置备注名");
                editActivityChangeInfo.setText(getIntent().getExtras().getString("extra"));
                break;

        }


        editActivityChangeInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(editActivityChangeInfo.getText().toString().trim())) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您的输入为空");
                    }
                    return;
                }
                try {
                    save();
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
                    }
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * 保存
     *
     * @throws JSONException
     */
    void save() throws JSONException {

        if (canChange == false) {
            ToastUtils.showShortToast(getBaseActivityContext(), "您无权修改任何内容!");
            return;
        }
        if (!isChanged) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "您未修改任何内容!");
            }
            return;
        }
        if ("idCard".equals(getIntent().getExtras().getString("type"))) {
            if (!RegexUtils.isIDCard18(editActivityChangeInfo.getText().toString().trim())) {
                ToastUtils.showShortToast(getBaseActivityContext(), "身份证不正确!");
                return;
            }
        }
        changeUserInfoModel = new ChangeInfoModel();
        changeUserInfoModel.msg = editActivityChangeInfo.getText().toString().trim();
        changeUserInfoModel.type = getIntent().getExtras().getString("type");
        EventBus.getDefault().post(changeUserInfoModel);
        finish();
    }
}
