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
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.DataEntitiesCertificationModel;
import com.sss.car.model.EntitiesCertificationModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.Config.member_id;

/**
 * 实体认证
 * Created by leilei on 2017/8/19.
 */

public class ActivityMyDataEntitiesCertification extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.company_show_name_activity_my_data_entities_certification)
    TextView companyShowNameActivityMyDataEntitiesCertification;
    @BindView(R.id.company_click_activity_my_data_entities_certification)
    LinearLayout companyClickActivityMyDataEntitiesCertification;
    @BindView(R.id.name_show_activity_my_data_entities_certification)
    TextView nameShowActivityMyDataEntitiesCertification;
    @BindView(R.id.name_click_activity_my_data_entities_certification)
    LinearLayout nameClickActivityMyDataEntitiesCertification;
    @BindView(R.id.business_license_click_activity_my_data_entities_certification)
    LinearLayout businessLicenseClickActivityMyDataEntitiesCertification;
    @BindView(R.id.pic_click_activity_my_data_entities_certification)
    LinearLayout picClickActivityMyDataEntitiesCertification;
    @BindView(R.id.service_click_activity_my_data_entities_certification)
    LinearLayout serviceClickActivityMyDataEntitiesCertification;
    @BindView(R.id.address_click_activity_my_data_entities_certification)
    LinearLayout addressClickActivityMyDataEntitiesCertification;
    @BindView(R.id.activity_my_data_entities_certification)
    LinearLayout activityMyDataEntitiesCertification;
    YWLoadingDialog ywLoadingDialog;
    String compay = "", compayPeopleName = "", compayAddress = "";
    String code = "";
    Call call;
    DataEntitiesCertificationModel dataEntitiesCertificationModel;
    boolean can = false;
    @BindView(R.id.pic_show_click_activity_my_data_entities_certification)
    TextView picShowClickActivityMyDataEntitiesCertification;
    @BindView(R.id.show_address_click_activity_my_data_entities_certification)
    TextView showAddressClickActivityMyDataEntitiesCertification;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (call != null) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
        call = null;
        dataEntitiesCertificationModel = null;
        backTop = null;
        titleTop = null;
        compay = null;
        compayPeopleName = null;
        compayAddress = null;
        companyShowNameActivityMyDataEntitiesCertification = null;
        companyClickActivityMyDataEntitiesCertification = null;
        nameShowActivityMyDataEntitiesCertification = null;
        nameClickActivityMyDataEntitiesCertification = null;
        businessLicenseClickActivityMyDataEntitiesCertification = null;
        picClickActivityMyDataEntitiesCertification = null;
        serviceClickActivityMyDataEntitiesCertification = null;
        addressClickActivityMyDataEntitiesCertification = null;
        activityMyDataEntitiesCertification = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_entities_certification);
        ButterKnife.bind(this);
        titleTop.setText("实体认证");
        customInit(activityMyDataEntitiesCertification, false, true, true);
        try {
            requestUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求用户基本信息
     *
     * @throws JSONException
     */
    void requestUserInfo() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.userInfo(
                new JSONObject()
                        .put("member_id", Config.member_id).toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                        }
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        if (StringUtils.isEmpty(response)) {
                            if (getBaseActivityContext() != null) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("0".equals(jsonObject.getJSONObject("data").getString("is_auth"))) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                                        can = false;
                                    } else {
                                        can = true;
                                        getCompany();
                                    }
                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:save-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    @OnClick({R.id.back_top, R.id.company_click_activity_my_data_entities_certification, R.id.name_click_activity_my_data_entities_certification, R.id.business_license_click_activity_my_data_entities_certification, R.id.pic_click_activity_my_data_entities_certification, R.id.service_click_activity_my_data_entities_certification, R.id.address_click_activity_my_data_entities_certification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.company_click_activity_my_data_entities_certification:
                if (can == false) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "compay")
                            .putExtra("canChange", true)
                            .putExtra("extra", compay));
                }
                break;
            case R.id.name_click_activity_my_data_entities_certification:
                if (can == false) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "compayPeopleName")
                            .putExtra("canChange", true)
                            .putExtra("extra", compayPeopleName));
                }
                break;
            case R.id.business_license_click_activity_my_data_entities_certification:
                if (dataEntitiesCertificationModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "店铺信息获取失败");
                    return;

                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataEntitiesCertificationBusinessLicense.class)
                            .putExtra("type", "compayBusinessLicense")
                            .putExtra("extra", dataEntitiesCertificationModel));
                }
                break;
            case R.id.pic_click_activity_my_data_entities_certification:
                if (can == false) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                    return;
                }
                if (dataEntitiesCertificationModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "店铺信息获取失败");
                    return;

                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "code")
                            .putExtra("canChange", true)
                            .putExtra("extra", code));
                }
                break;
            case R.id.service_click_activity_my_data_entities_certification:
                if (can == false) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataEntitiesCertificationService.class));
                }
                break;
            case R.id.address_click_activity_my_data_entities_certification:
                if (can == false) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "尚未通过个人认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "compayAddress")
                            .putExtra("extra", compayAddress));
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "compay":
                if (!StringUtils.isEmpty(event.msg)) {


                    setcompany(new EntitiesCertificationModel("company_name", event.msg), "法人");
                }
                break;
            case "compayPeopleName":
                if (!StringUtils.isEmpty(event.msg)) {
                    setcompany(new EntitiesCertificationModel("possessor", event.msg), "店铺名称");
                }
                break;
            case "compayAddress":
                if (!StringUtils.isEmpty(event.msg)) {
                    setcompany(new EntitiesCertificationModel("manage_path", event.msg), "经营地址");
                }
                break;
            case "compayBusinessLicense":
                if (!StringUtils.isEmpty(event.msg)) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;
                    if (getBaseActivityContext() != null) {
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                    }
                    ywLoadingDialog.show();
                    setcompany(new EntitiesCertificationModel("business_license", ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(event.msg, 480, 960))), "店铺营业执照");
                }
                break;
            case "compayPic":
                if (!StringUtils.isEmpty(event.msg)) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;
                    if (getBaseActivityContext() != null) {
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                    }
                    ywLoadingDialog.show();
                }
                break;
            case "code":
                if (!StringUtils.isEmpty(event.msg)) {
                    setcompany(new EntitiesCertificationModel("credit_code", event.msg), "信用代码");
                }
                break;
        }
    }


    /**
     * 获取店铺信息
     */
    void getCompany() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getCompany(
                    new JSONObject()
                            .put("member_id", member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        dataEntitiesCertificationModel = new DataEntitiesCertificationModel();
                                        dataEntitiesCertificationModel.possessor = jsonObject.getJSONObject("data").getString("possessor");
                                        dataEntitiesCertificationModel.company_name = jsonObject.getJSONObject("data").getString("company_name");
                                        dataEntitiesCertificationModel.manage_path = jsonObject.getJSONObject("data").getString("manage_path");
                                        dataEntitiesCertificationModel.business_license = jsonObject.getJSONObject("data").getString("business_license");
                                        dataEntitiesCertificationModel.auth_id = jsonObject.getJSONObject("data").getString("auth_id");
                                        if (!StringUtils.isEmpty(dataEntitiesCertificationModel.possessor)) {
                                            compayPeopleName = dataEntitiesCertificationModel.possessor;
                                            nameShowActivityMyDataEntitiesCertification.setText(dataEntitiesCertificationModel.possessor);
                                        }
                                        if (!StringUtils.isEmpty(dataEntitiesCertificationModel.company_name)) {
                                            compay = dataEntitiesCertificationModel.company_name;
                                            companyShowNameActivityMyDataEntitiesCertification.setText(dataEntitiesCertificationModel.company_name);
                                        }
                                        if (!StringUtils.isEmpty(dataEntitiesCertificationModel.manage_path)) {
                                            compayAddress = dataEntitiesCertificationModel.manage_path;
                                        }
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 创建/修改
     */
    void setcompany(final EntitiesCertificationModel entitiesCertificationModel, final String meaning) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setCompany(
                    new JSONObject()
                            .put(entitiesCertificationModel.Key, entitiesCertificationModel.value)
                            .put("member_id", member_id)
                            .toString(), meaning, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        switch (meaning) {
                                            case "法人":
                                                companyShowNameActivityMyDataEntitiesCertification.setText(entitiesCertificationModel.value);
                                                compayPeopleName = entitiesCertificationModel.value;
                                                dataEntitiesCertificationModel.possessor = compayPeopleName;
                                                break;
                                            case "店铺名称":
                                                nameShowActivityMyDataEntitiesCertification.setText(entitiesCertificationModel.value);
                                                compay = entitiesCertificationModel.value;
                                                dataEntitiesCertificationModel.company_name = compay;
                                                break;
                                            case "店铺地址":
                                                compayAddress = entitiesCertificationModel.value;
                                                dataEntitiesCertificationModel.manage_path = compayAddress;
                                                break;
                                            case "店铺营业执照":
                                                dataEntitiesCertificationModel.business_license = jsonObject.getJSONObject("data").getString("business_license");
                                                ToastUtils.showShortToast(getBaseActivityContext(), "上传成功");
                                                break;
                                            case "信用代码":
                                                picShowClickActivityMyDataEntitiesCertification.setText(entitiesCertificationModel.value);
                                                break;
                                            case "经营地址":
                                                showAddressClickActivityMyDataEntitiesCertification.setText(entitiesCertificationModel.value);
                                                break;
                                        }
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay-0");
            }
            e.printStackTrace();
        }
    }


}
