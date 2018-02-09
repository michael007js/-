package com.sss.car.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;



/**
 * 身份证照片上传
 * Created by leilei on 2017/11/8.
 */

public class ActivityUpdatePic extends BaseActivity {
    public static final int One = 0x001;
    public static final int Two = 0x002;
    public static final int Three = 0x003;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.pic_activity_update_pic)
    SimpleDraweeView picActivityUpdatePic;
    @BindView(R.id.activity_update_pic)
    LinearLayout activityUpdatePic;
    YWLoadingDialog ywLoadingDialog;
    String path;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        picActivityUpdatePic = null;
        activityUpdatePic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pic);
        ButterKnife.bind(this);
        customInit(activityUpdatePic, false, true, false);
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        switch (getIntent().getExtras().getInt("mode")) {
            case One:
                titleTop.setText("身份证正面照");
                break;
            case Two:
                titleTop.setText("身份证反面照");
                break;
            case Three:
                titleTop.setText("手持身份证照");
                break;
        }
        addImageViewList(FrescoUtils.showImage(false,
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.add_photo),
                picActivityUpdatePic, 0f));
        get_personage();
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

                                    switch (getIntent().getExtras().getInt("mode")) {
                                        case One:
                                            String a=jsonObject.getJSONObject("data").getString("front_card");
                                            if (!StringUtils.isEmpty(a)){
                                                addImageViewList(FrescoUtils.showImage(false,
                                                        getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight(),
                                                        Uri.parse(Config.url+a),
                                                        picActivityUpdatePic, 0f));
                                            }

                                            break;

                                        case Two:
                                            String b=jsonObject.getJSONObject("data").getString("reverse_card");
                                            if (!StringUtils.isEmpty(b)){
                                                addImageViewList(FrescoUtils.showImage(false,
                                                        getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight(),
                                                        Uri.parse(Config.url+b),
                                                        picActivityUpdatePic, 0f));
                                            }

                                            break;

                                        case Three:
                                            String c=jsonObject.getJSONObject("data").getString("hold_card");
                                            if (!StringUtils.isEmpty(c)){
                                                addImageViewList(FrescoUtils.showImage(false,
                                                        getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight(),
                                                        Uri.parse(Config.url+c),
                                                        picActivityUpdatePic, 0f));
                                            }

                                            break;
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

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.pic_activity_update_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(path)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您未更改图片");
                    return;
                }
                set_personage();
                break;
            case R.id.pic_activity_update_pic:
                APPOftenUtils.createPhotoChooseDialog(0, 1, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList == null || resultList.size() == 0) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "未选中照片");
                            return;
                        }
                        path = resultList.get(0).getPhotoPath();

                        addImageViewList(FrescoUtils.showImage(false,
                                getWindowManager().getDefaultDisplay().getWidth(),
                                getWindowManager().getDefaultDisplay().getHeight(),
                                Uri.fromFile(new File(path)),
                                picActivityUpdatePic, 0f));
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                    }
                });
                break;
        }
    }


    /**
     * 设置个人认证资料
     */
    public void set_personage() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        String value = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(path, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
        String key = null;
        switch (getIntent().getExtras().getInt("mode")) {
            case One:
                key = "front_card";
                break;
            case Two:
                key = "reverse_card";
                break;
            case Three:
                key = "hold_card";
                break;
        }
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
