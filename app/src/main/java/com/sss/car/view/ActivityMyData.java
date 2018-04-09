package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.EventBusModel.ShowNullCarModel;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/15.
 */

public class ActivityMyData extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.pic_activity_my_data)
    LinearLayout picActivityMyData;
    @BindView(R.id.account_show_activity_my_data)
    TextView accountShowActivityMyData;
    @BindView(R.id.account_activity_my_data)
    LinearLayout accountActivityMyData;
    @BindView(R.id.username_show_activity_my_data)
    TextView usernameShowActivityMyData;
    @BindView(R.id.username_activity_my_data)
    LinearLayout usernameActivityMyData;
    @BindView(R.id.nikename_show_activity_my_data)
    TextView nikenameShowActivityMyData;
    @BindView(R.id.nikename_activity_my_data)
    LinearLayout nikenameActivityMyData;
    @BindView(R.id.sex_show_activity_my_data)
    TextView sexShowActivityMyData;
    @BindView(R.id.sex_activity_my_data)
    LinearLayout sexActivityMyData;
    @BindView(R.id.mobile_show_activity_my_data)
    TextView mobileShowActivityMyData;
    @BindView(R.id.mobile_activity_my_data)
    LinearLayout mobileActivityMyData;
    @BindView(R.id.car_show_activity_my_data)
    TextView carShowActivityMyData;
    @BindView(R.id.car_activity_my_data)
    LinearLayout carActivityMyData;
    @BindView(R.id.receiving_show_activity_my_data)
    TextView receivingShowActivityMyData;
    @BindView(R.id.receiving_activity_my_data)
    LinearLayout receivingActivityMyData;
    @BindView(R.id.shop_show_activity_my_data)
    TextView shopShowActivityMyData;
    @BindView(R.id.shop_activity_my_data)
    LinearLayout shopActivityMyData;
    @BindView(R.id.entity_certification_show_activity_my_data)
    TextView entityCertificationShowActivityMyData;
    @BindView(R.id.entity_certification_activity_my_data)
    LinearLayout entityCertificationActivityMyData;
    @BindView(R.id.activity_my_data)
    LinearLayout activityMyData;
    @BindView(R.id.pic_show_activity_my_data)
    ImageView picShowActivityMyData;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.register_code_show_activity_my_data)
    TextView registerCodeShowActivityMyData;
    @BindView(R.id.register_code_activity_my_data)
    LinearLayout registerCodeActivityMyData;

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        picActivityMyData = null;registerCodeActivityMyData=null;registerCodeShowActivityMyData=null;
        accountShowActivityMyData = null;
        accountActivityMyData = null;
        usernameShowActivityMyData = null;
        usernameActivityMyData = null;
        nikenameShowActivityMyData = null;
        nikenameActivityMyData = null;
        sexShowActivityMyData = null;
        sexActivityMyData = null;
        mobileShowActivityMyData = null;
        mobileActivityMyData = null;
        carShowActivityMyData = null;
        carActivityMyData = null;
        receivingShowActivityMyData = null;
        receivingActivityMyData = null;
        shopShowActivityMyData = null;
        shopActivityMyData = null;
        entityCertificationShowActivityMyData = null;
        entityCertificationActivityMyData = null;
        activityMyData = null;
        picShowActivityMyData = null;
        super.onDestroy();
    }

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        ButterKnife.bind(this);
        customInit(activityMyData, false, true, true);
        titleTop.setText("我的资料");
        try {
            requestUserInfo();
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:userinfo-0");
            }
            e.printStackTrace();
        }
        usernameActivityMyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "username").putExtra("extra", Config.username));
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        try {
            requestUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowNullCarModel event) {
        try {
            requestUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        if (event.type!=null) {
            switch (event.type) {
                case "username":
                    try {
                        save("name", event.msg, "用户真实姓名");
                    } catch (JSONException e) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:username-0");
                        }
                        e.printStackTrace();
                    }
                    break;
                case "nikename":
                    try {
                        save("username", event.msg, "昵称");
                    } catch (JSONException e) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:nikename-0");
                        }
                        e.printStackTrace();
                    }
                    break;

                case "mobile":
                    try {
                        requestUserInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        save("mobile", event.msg, "手机号");
//                    } catch (JSONException e) {
//                        if (getBaseActivityContext() != null) {
//                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:mobile-0");
//                        }
//                        e.printStackTrace();
//                    }
                    break;
            }
        }
    }

    @OnClick({R.id.back_top, R.id.pic_activity_my_data, R.id.account_activity_my_data,
            R.id.nikename_activity_my_data, R.id.sex_activity_my_data,
            R.id.mobile_activity_my_data, R.id.car_activity_my_data, R.id.receiving_activity_my_data,
            R.id.shop_activity_my_data, R.id.entity_certification_activity_my_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.account_activity_my_data:
                break;
            case R.id.pic_activity_my_data:
                createCamera();
                break;
            case R.id.nikename_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "nikename").putExtra("extra", Config.nikename));
                }
                break;
            case R.id.sex_activity_my_data:
                String[] stringItems = {"男", "女","保密"};
                final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                        .isTitleShow(false)
                        .itemTextColor(Color.parseColor("#e83e41"))
                        .setmCancelBgColor(Color.parseColor("#e83e41"))
                        .cancelText(Color.WHITE);
                dialog.title("选择您的方式");
                dialog.titleTextSize_SP(14.5f).show();
                stringItems = null;
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                dialog.dismiss();
                                try {
                                    save("sex", "0", "性别");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    save("sex", "1", "性别");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                break;
                            case 2:
                                try {
                                    save("sex", "2", "性别");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                break;

                        }
                    }
                });

                break;
            case R.id.mobile_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataChangeMobile.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "mobile").putExtra("extra", Config.mobile));
                }
                break;
            case R.id.car_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCar.class));
                }
                break;
            case R.id.receiving_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataAdress.class));
                }
                break;
            case R.id.shop_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataShop.class));
                }
                break;
            case R.id.entity_certification_activity_my_data:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataEntityCertification.class));
                }

                break;
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
                                    parseJson(jsonObject);
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


    /**
     * 解析服务器返回数据
     *
     * @param jsonObject
     * @throws JSONException
     */
    void parseJson(JSONObject jsonObject) throws JSONException {
        picShowActivityMyData.setTag(R.id.glide_tag, Config.url + jsonObject.getJSONObject("data").getString("face"));
        if (getBaseActivityContext() != null) {
            addImageViewList(GlidUtils.downLoader(true, picShowActivityMyData, getBaseActivityContext()));
        }
        accountShowActivityMyData.setText(jsonObject.getJSONObject("data").getString("account"));
        if (StringUtils.isEmpty(jsonObject.getJSONObject("data").getString("name"))) {
            usernameShowActivityMyData.setText("设置");
        } else {
            usernameShowActivityMyData.setText(jsonObject.getJSONObject("data").getString("name"));
            usernameShowActivityMyData.setOnClickListener(null);
        }
        nikenameShowActivityMyData.setText(jsonObject.getJSONObject("data").getString("username"));

        if ("0".equals(jsonObject.getJSONObject("data").getString("sex"))) {
            sexShowActivityMyData.setText("男");
        } else if ("1".equals(jsonObject.getJSONObject("data").getString("sex"))) {
            sexShowActivityMyData.setText("女");
        } else if ("2".equals(jsonObject.getJSONObject("data").getString("sex"))) {
            sexShowActivityMyData.setText("保密");
        }else {
            sexShowActivityMyData.setText("未知");
        }
        if ("0".equals(jsonObject.getJSONObject("data").getString("vehicle_count"))) {
            carShowActivityMyData.setText("设置");
        } else {
            carShowActivityMyData.setText("已设置");
        }

        if ("0".equals(jsonObject.getJSONObject("data").getString("is_auth"))) {
            entityCertificationShowActivityMyData.setText("设置");
        } else {
            entityCertificationShowActivityMyData.setText("已设置");
        }

        if ("0".equals(jsonObject.getJSONObject("data").getString("address_count"))) {
            receivingShowActivityMyData.setText("设置");
        } else {
            receivingShowActivityMyData.setText("已设置");
        }

        if ("0".equals(jsonObject.getJSONObject("data").getString("shop_count"))) {
            shopShowActivityMyData.setText("设置");
        } else {
            shopShowActivityMyData.setText("已设置");
        }
        mobileShowActivityMyData.setText(jsonObject.getJSONObject("data").getString("mobile"));
        registerCodeShowActivityMyData.setText(jsonObject.getJSONObject("data").getString("invite_code"));
    }

    /**
     * 转BASE64
     *
     * @param path
     * @throws JSONException
     */
    void convert2Base64(String path) throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        save("face", ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(path, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())), "头像");
    }

    /**
     * 保存
     *
     * @throws JSONException
     */
    void save(String key, String value, String meaning) throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUserInfo(
                new JSONObject()
                        .put(key, value)
                        .put("member_id", Config.member_id).toString(), meaning, new StringCallback() {
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
                                    switch (jsonObject.getJSONObject("data").getString("type")) {
                                        case "face":
                                            Config.face = jsonObject.getJSONObject("data").getString("value");
                                            picShowActivityMyData.setTag(R.id.glide_tag, Config.url + Config.face);
                                            if (getBaseActivityContext() != null) {
                                                addImageViewList(GlidUtils.downLoader(true, picShowActivityMyData, getBaseActivityContext()));
                                            }
                                            RongYunUtils.refreshUserinfo(Config.member_id,Config.nikename, Uri.parse(Config.url+Config.face));
                                            RongYunUtils.refreshGroupInfoCache(Config.member_id,Config.nikename, Uri.parse(Config.url+Config.face));
                                            break;
                                        case "name":
                                            Config.username = jsonObject.getJSONObject("data").getString("value");
                                            usernameShowActivityMyData.setText(Config.username);
                                            usernameActivityMyData.setOnClickListener(null);
                                            break;
                                        case "username":
                                            Config.nikename = jsonObject.getJSONObject("data").getString("value");
                                            nikenameShowActivityMyData.setText(Config.nikename);
                                            RongYunUtils.refreshUserinfo(Config.member_id,Config.nikename, Uri.parse(Config.url+Config.face));
                                            RongYunUtils.refreshGroupInfoCache(Config.member_id,Config.nikename, Uri.parse(Config.url+Config.face));
                                            break;
                                        case "mobile":
                                            Config.mobile = jsonObject.getJSONObject("data").getString("value");
                                            mobileShowActivityMyData.setText(Config.mobile);
                                            new SPUtils(getBaseActivityContext(),Config.defaultFileName, Context.MODE_PRIVATE).put("account",Config.mobile);
                                            break;
                                    }
                                    requestUserInfo();
                                    EventBus.getDefault().post(new ChangeInfoModel());
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


    /**
     * 弹出照片选择
     */
    void createCamera() {
        APPOftenUtils.createPhotoChooseDialog(8, 1, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList == null || resultList.size() == 0) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "照片获取出错!");
                    }
                } else {
                    picShowActivityMyData.setTag(R.id.glide_tag, resultList.get(0).getPhotoPath());
                    try {
                        convert2Base64(resultList.get(0).getPhotoPath());
                    } catch (JSONException e) {
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:convert2Base64-0");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                if (getBaseActivityContext() != null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "照片获取出错err:" + errorMsg);
                }
            }
        });
    }
}
