package com.sss.car.view;

import android.content.Intent;
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
 * 商铺商标
 * Created by leilei on 2017/11/8.
 */

@SuppressWarnings("ALL")
public class ActivityShopPic extends BaseActivity {

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
    String shop_id;
    String servelImagePath;
    @BindView(R.id.delete_activity_update_pic)
    SimpleDraweeView deleteActivityUpdatePic;

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
        titleTop.setText("商铺图标");


        picActivityUpdatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(path) && StringUtils.isEmpty(servelImagePath)) {
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
                } else {
                    if (!StringUtils.isEmpty(path)) {
                        if (getBaseActivityContext() != null) {
                            List<String> list = new ArrayList<>();
                            list.add(path);
                            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                    .putStringArrayListExtra("data", (ArrayList<String>) list)
                                    .putExtra("current", 0));
                        }
                    } else if (!StringUtils.isEmpty(servelImagePath)) {
                        if (getBaseActivityContext() != null) {
                            List<String> list = new ArrayList<>();
                            list.add(Config.url + servelImagePath);
                            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                    .putStringArrayListExtra("data", (ArrayList<String>) list)
                                    .putExtra("current", 0));
                        }
                    }
                }
            }
        });
        picActivityUpdatePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!StringUtils.isEmpty(path) || !StringUtils.isEmpty(servelImagePath)) {
                    deleteActivityUpdatePic.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        addImageViewList(FrescoUtils.showImage(false,
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.add_photo),
                picActivityUpdatePic, 0f));
        getshopInfo();
    }


    /**
     * 获取店铺信息
     */
    void getshopInfo() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getShop(
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
                                        shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                        servelImagePath = jsonObject.getJSONObject("data").getString("logo");
                                        if (!StringUtils.isEmpty(jsonObject.getJSONObject("data").getString("logo"))) {

                                            addImageViewList(FrescoUtils.showImage(false,
                                                    getWindowManager().getDefaultDisplay().getWidth(),
                                                    getWindowManager().getDefaultDisplay().getHeight(),
                                                    Uri.parse(Config.url + servelImagePath),
                                                    picActivityUpdatePic, 0f));
                                        }

                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
            }
            e.printStackTrace();
        }
    }


    @OnClick({R.id.back_top, R.id.right_button_top, R.id.delete_activity_update_pic})
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
                if (StringUtils.isEmpty(shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "商铺信息刷新中...");
                    return;
                }
                setShop();
                break;

            case R.id.delete_activity_update_pic:
                deleteActivityUpdatePic.setVisibility(View.GONE);
                path = null;
                servelImagePath = null;
                addImageViewList(FrescoUtils.showImage(false,
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight(),
                        Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.add_photo),
                        picActivityUpdatePic, 0f));
                break;
        }
    }


    /**
     * 设置店铺信息
     */
    public void setShop() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        String value = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(path, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setShop(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("shop_id", shop_id)
                            .put("logo", value)
                            .toString(), "店铺商标", new StringCallback() {
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
