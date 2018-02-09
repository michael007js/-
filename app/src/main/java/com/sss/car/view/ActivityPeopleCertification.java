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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;

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

/**
 * 改版后个人认证
 * Created by leilei on 2017/12/12.
 */

@SuppressWarnings("ALL")
public class ActivityPeopleCertification extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.et_three)
    EditText etThree;
    @BindView(R.id.click_front)
    SimpleDraweeView clickFront;
    @BindView(R.id.click_back)
    SimpleDraweeView clickBack;
    @BindView(R.id.click_save)
    TextView clickSave;
    @BindView(R.id.activity_people_certification)
    LinearLayout activityPeopleCertification;
    @BindView(R.id.title_top)
    TextView titleTop;
    YWLoadingDialog ywLoadingDialog;
    String front, back;
    String frontChoose, backChoose;
    String state = "-1";////0审核中，1已审核，2未通过

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        activityPeopleCertification = null;
        rightButtonTop = null;
        backTop = null;
        etOne = null;
        etTwo = null;
        etThree = null;
        clickFront = null;
        clickBack = null;
        clickSave = null;
        titleTop = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_certification);
        ButterKnife.bind(this);
        customInit(activityPeopleCertification, false, true, false);
        titleTop.setText("个人认证");
        get_personage();


    }

    @OnClick({R.id.back_top, R.id.click_front, R.id.click_back, R.id.click_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_front:
                if ("-1".equals(state) || "2".equals(state)) {
                    APPOftenUtils.createPhotoChooseDialog(0, 1, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList == null || resultList.size() == 0) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "未选中图片");
                                return;
                            }
                            frontChoose = resultList.get(0).getPhotoPath();
                            addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(resultList.get(0).getPhotoPath())), clickFront, 0f));
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                        }
                    });
                } else {
                    if (getBaseActivityContext() != null) {
                        List<String> temp = new ArrayList<>();
                        if (!StringUtils.isEmpty(frontChoose)) {
                            temp.add(frontChoose);
                        } else {
                            temp.add(Config.url + front);
                        }

                        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", 0));
                    }

                }
                break;
            case R.id.click_back:
                if ("-1".equals(state) || "2".equals(state)) {
                    APPOftenUtils.createPhotoChooseDialog(0, 1, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList == null || resultList.size() == 0) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "未选中图片");
                                return;
                            }
                            backChoose = resultList.get(0).getPhotoPath();
                            addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(resultList.get(0).getPhotoPath())), clickBack, 0f));
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {
                            ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                        }
                    });
                } else {
                    if (getBaseActivityContext() != null) {
                        List<String> temp = new ArrayList<>();
                        if (!StringUtils.isEmpty(backChoose)) {
                            temp.add(backChoose);
                        } else {
                            temp.add(Config.url + back);
                        }
                        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", 0));
                    }
                }
                break;
            case R.id.click_save:
                set_personage();
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
                                    etOne.setText(jsonObject.getJSONObject("data").getString("name"));
                                    etTwo.setText(jsonObject.getJSONObject("data").getString("card"));
                                    etThree.setText(jsonObject.getJSONObject("data").getString("end_time"));
                                    front = jsonObject.getJSONObject("data").getString("front_card");
                                    addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + front), clickFront, 0f));
                                    back = jsonObject.getJSONObject("data").getString("reverse_card");
                                    addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + back), clickBack, 0f));
                                    state = jsonObject.getJSONObject("data").getString("state");
                                    if ("0".equals(state)) {//0审核中，1已审核，2未通过
                                        clickSave.setText("审核中");
//                                        APPOftenUtils.setBackgroundOfVersion(clickSave, getResources().getDrawable(R.drawable.bg_gray));
                                        clickSave.setOnClickListener(null);
                                    } else if ("1".equals(state)) {
                                        clickSave.setText("已通过");
//                                        APPOftenUtils.setBackgroundOfVersion(clickSave, getResources().getDrawable(R.drawable.bg_gray));
                                        clickSave.setOnClickListener(null);
                                    } else if ("2".equals(state)) {
                                        clickSave.setText("未通过");
                                    }
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
    public void set_personage() {
        if (StringUtils.isEmpty(etOne.getText().toString().trim()) || StringUtils.isEmpty(etTwo.getText().toString().trim()) || StringUtils.isEmpty(etThree.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请将基本信息填写完整");
            return;

        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        String front = null;
        if (StringUtils.isEmpty(frontChoose)) {
            front = ActivityPeopleCertification.this.front;
        } else {
            front = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(frontChoose, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));

        }
        String back = null;
        if (StringUtils.isEmpty(backChoose)) {
            back = ActivityPeopleCertification.this.back;
        } else {
            back = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(backChoose, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_personage(
                    new JSONObject()
                            .put("name", etOne.getText().toString().trim())
                            .put("card", etTwo.getText().toString().trim())
                            .put("end_time", etThree.getText().toString().trim())
                            .put("front_card", front)
                            .put("reverse_card", back)
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

}
