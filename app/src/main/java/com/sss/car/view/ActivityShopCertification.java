package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.DataEntitiesCertificationModel;
import com.sss.car.model.EntitiesCertificationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

import static com.sss.car.Config.member_id;

/**
 * 改版后实体认证
 * Created by leilei on 2017/12/13.
 */

public class ActivityShopCertification extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.et_three)
    EditText etThree;
    @BindView(R.id.click_front)
    SimpleDraweeView clickFront;
    @BindView(R.id.et_four)
    EditText etFour;
    @BindView(R.id.et_five)
    EditText etFive;
    @BindView(R.id.click_save)
    TextView clickSave;
    @BindView(R.id.activity_shop_certification)
    LinearLayout activityShopCertification;

    YWLoadingDialog ywLoadingDialog;
    String business_license;//营业执照
    String business_licenseChoose;
    String state="-1";////0审核中，1已审核，2未通过

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        etOne = null;
        etTwo = null;
        etThree = null;
        clickFront = null;
        etFour = null;
        etFive = null;
        clickSave = null;
        activityShopCertification = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_certification);
        ButterKnife.bind(this);
        customInit(activityShopCertification, false, true, false);
        titleTop.setText("实体认证");
        getCompany();
    }


    @OnClick({R.id.back_top, R.id.click_save,R.id.click_front})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_front:
                if ("-1".equals(state)||"2".equals(state)) {
                    APPOftenUtils.createPhotoChooseDialog(0, 1, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList == null || resultList.size() == 0) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "未选中图片");
                                return;
                            }
                            business_licenseChoose = resultList.get(0).getPhotoPath();
                            addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(resultList.get(0).getPhotoPath())), clickFront, 0f));
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                        }
                    });
                }else {
                    if (getBaseActivityContext() != null) {
                        List<String> temp = new ArrayList<>();
                        if (!StringUtils.isEmpty(business_licenseChoose)){
                            temp.add(business_licenseChoose);
                        }else {
                            temp.add(Config.url+business_license);
                        }

                        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", 0));
                    }

                }
                break;
            case R.id.click_save:
                setcompany();
                break;
        }
    }
    /**
     * 创建/修改
     */
    void setcompany() {
        if (StringUtils.isEmpty(etOne.getText().toString().trim())||StringUtils.isEmpty(etTwo.getText().toString().trim())||StringUtils.isEmpty(etThree.getText().toString().trim())||
                StringUtils.isEmpty(etFour.getText().toString().trim())||StringUtils.isEmpty(etFive.getText().toString().trim())){
            ToastUtils.showShortToast(getBaseActivityContext(),"请将基本信息填写完整");
            return;

        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();

        String business_licenseChooseTemp = null;
        if (StringUtils.isEmpty(business_licenseChoose)) {
            business_licenseChooseTemp = ActivityShopCertification.this.business_license;
        } else {
             business_licenseChooseTemp= ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(business_licenseChoose,getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()));
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setCompany(
                    new JSONObject()
                            .put("company_name", etOne.getText().toString().trim())
                            .put("possessor", etTwo.getText().toString().trim())
                            .put("credit_code", etThree.getText().toString().trim())
                            .put("server_scope", etFour.getText().toString().trim())
                            .put("manage_path", etFive.getText().toString().trim())
                            .put("company_name", etOne.getText().toString().trim())
                            .put("business_license", business_licenseChooseTemp)
                            .put("member_id",Config. member_id)
                            .toString(), "", new StringCallback() {
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        finish();
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
    /**
     * 获取信息
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
                                       etOne.setText(jsonObject.getJSONObject("data").getString("company_name"));
                                        etTwo.setText(jsonObject.getJSONObject("data").getString("possessor"));
                                        etThree.setText(jsonObject.getJSONObject("data").getString("credit_code"));
                                        etFour.setText(jsonObject.getJSONObject("data").getString("server_scope"));
                                        etFive.setText(jsonObject.getJSONObject("data").getString("manage_path"));
                                        business_license=jsonObject.getJSONObject("data").getString("business_license");
                                        LogUtils.e(Config.url+business_license);
                                        addImageViewList(FrescoUtils.showImage(false,80,80, Uri.parse(Config.url+business_license),clickFront,0f));

                                        state = jsonObject.getJSONObject("data").getString("state");
                                        if ("0".equals(state)) {//0审核中，1已审核，2未通过
                                            clickSave.setText("审核中");
//                                            APPOftenUtils.setBackgroundOfVersion(clickSave, getResources().getDrawable(R.drawable.bg_gray));
                                            clickSave.setOnClickListener(null);
                                        } else if ("1".equals(state)) {
                                            clickSave.setText("已通过");
//                                            APPOftenUtils.setBackgroundOfVersion(clickSave, getResources().getDrawable(R.drawable.bg_gray));
                                            clickSave.setOnClickListener(null);
                                        } else if ("2".equals(state)) {
                                            clickSave.setText("未通过");
                                        }

                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: -0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: -0");
            }
            e.printStackTrace();
        }
    }
}
