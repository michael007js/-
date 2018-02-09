package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedDynamicList;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

import static com.sss.car.R.id.nine;

/**
 * Created by leilei on 2017/8/25.
 */

public class ActivityPublishDymaic extends BaseActivity implements NineView.NineViewShowCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input_activity_publish_dymaic)
    EditText inputActivityPublishDymaic;
    @BindView(R.id.photo_activity_publish_dymaic)
    NineView nine;
    @BindView(R.id.activity_publish_dymaic)
    LinearLayout activityPublishDymaic;
    List<String> imageList = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    public String addImage = "http://r.photo.store.qq.com/psb?/V11KriI80t2NdO/jNXsmatwDG79QHUEn2z9rTZ1GCpDKm5tUa4Fq7Rd3Sc!/o/dG0BAAAAAAAA&ek=1&kp=1&pt=0&bo=eAB4AHgAeAADACU!&tm=1503633600&sce=0-12-12&rf=viewer_311";
    JSONArray jsonArray = new JSONArray();
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        jsonArray = null;
        addImage = null;
        if (imageList != null) {
            imageList.clear();
        }
        imageList = null;
        backTop = null;
        titleTop = null;
        inputActivityPublishDymaic = null;
        nine = null;
        rightButtonTop = null;
        activityPublishDymaic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dymaic);
        ButterKnife.bind(this);
        customInit(activityPublishDymaic, false, true, false);
        imageList.add("default");
        nine.isShowCloseButton(true)
                .spacing(5)
                .maxCount(9)
                .CloseSize(30)
                .isRemoveSame(false)
                .setNineViewShowCallBack(this);
        nine.urlList(imageList);
        titleTop.setText("发布动态");
        rightButtonTop.setText("发送");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
    }



    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(inputActivityPublishDymaic.getText().toString().trim()) && jsonArray.length() == 0) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "不能发表空动态");
                    return;
                }
                convert();
                break;
        }
    }


    void convert() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        for (int i = 0; i < imageList.size(); i++) {
            if (!imageList.get(i).startsWith("default")) {
                jsonArray.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(imageList.get(i), 480, 960)));
            }
        }
        publishDymaic(jsonArray);

    }


    /**
     * 获取我的动态
     *
     * @param jsonArray
     */
    void publishDymaic(JSONArray jsonArray) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.publishDymaic(
                    new JSONObject().put("member_id", Config.member_id)
                            .put("contents", inputActivityPublishDymaic.getText().toString().trim())
                            .put("status", "0")//0公开动态  1不公开动态
                            .put("city_path", Config.city)
                            .put("picture", jsonArray)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ActivityPublishDymaic.this.jsonArray = null;
                            ActivityPublishDymaic.this.jsonArray = new JSONArray();
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ActivityPublishDymaic.this.jsonArray = null;
                            ActivityPublishDymaic.this.jsonArray = new JSONArray();
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedDynamicList());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: Publish Dymaic-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {
        if ("default".equals(url)) {
            addImageViewList(GlidUtils.glideLoad(false, imageView, getBaseActivityContext(), R.mipmap.add_photo));
        } else {
            imageView.setTag(R.id.glide_tag, url);
            addImageViewList(GlidUtils.downLoader(false, imageView, getBaseActivityContext()));
        }
    }

    @Override
    public void onDisplayImage(RatioImageView imageView, RatioImageView closeButton, String url, int parentWidth, Context context) {
        if ("default".equals(url)) {
            addImageViewList(GlidUtils.glideLoad(false, imageView, getBaseActivityContext(), R.mipmap.add_photo));
            if (closeButton != null) {
                closeButton.setVisibility(View.GONE);
            }
        } else {
            imageView.setTag(R.id.glide_tag, url);
            addImageViewList(GlidUtils.downLoader(false, imageView, getBaseActivityContext()));
            if (closeButton != null) {
                closeButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClickImage(int position, String url, List<String> urlList, Context context) {
        if ("default".equals(url)) {
            int count = 9;
            if (imageList.size() > 1) {
                count = 9 - imageList.size() + 1;
            }
            APPOftenUtils.createPhotoChooseDialog(0, count, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    if (resultList == null || resultList.size() == 0) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "照片选择错误");
                        return;
                    }

                    for (int i = 0; i < imageList.size(); i++) {
                        if ("default".equals(imageList.get(i))) {
                            imageList.remove(i);
                        }
                    }
                    for (int i = 0; i < resultList.size(); i++) {
                        imageList.add(resultList.get(i).getPhotoPath());
                    }
                    if (imageList.size() < 9) {
                        imageList.add("default");
                    }
                    nine.urlList(imageList);

                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                }
            });
        } else {
            List<String> temp = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < imageList.size(); i++) {
                if (!"default".equals(imageList.get(i))) {
                    temp.add(imageList.get(i));
                } else {
                    count++;
                }
            }
            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", position));
        }

    }

    @Override
    public void onClickImageColse(int position, String url, List<String> urlList, Context context) {
        imageList.remove(position);
        int count = 0;
        for (int i = 0; i < imageList.size(); i++) {
            if ("default".equals(imageList.get(i))) {
                count++;
            }
        }
        if (count == 0) {
            imageList.add("default");
        }

        nine.urlList(imageList);
    }

    @Override
    public void onSamePhotos(List<String> mRejectUrlList) {

    }
}
