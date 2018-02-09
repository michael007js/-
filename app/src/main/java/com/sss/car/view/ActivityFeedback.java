package com.sss.car.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GalleryHorizontalListView.GalleryHorizontalListView;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.FeedBackModel;

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



/**
 * Created by leilei on 2017/12/15.
 */

public class ActivityFeedback extends BaseActivity implements NineView.NineViewShowCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input_activity_feedback)
    EditText inputActivityFeedback;
    @BindView(R.id.submit_activity_feedback)
    TextView submitActivityFeedback;
    @BindView(R.id.activity_feedback)
    LinearLayout activityFeedback;
    YWLoadingDialog ywLoadingDialog;
    List<FeedBackModel> list = new ArrayList<>();
    Gson gson = new Gson();
    SSS_Adapter sss_adapter;
    @BindView(R.id.form)
    InnerGridView form;
    JSONArray form_id = new JSONArray();
    JSONArray picture = new JSONArray();
    @BindView(R.id.NineView)
    NineView NineView;
    List<String> imageList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        customInit(activityFeedback, false, true, false);
        titleTop.setText("建议反馈");
        initAdapter();
        form();
        imageList.add("default");
        NineView
                .isShowAll(false)
                .spacing(2)
                .maxCount(9)
                .isShowCloseButton(false).setNineViewShowCallBack(this)
        .urlList(imageList);

    }

    @OnClick({R.id.back_top, R.id.submit_activity_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.submit_activity_feedback:

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect) {
                        form_id.put(list.get(i).form_id);
                    }
                }


                if (!StringUtils.isEmpty(inputActivityFeedback.getText().toString().trim())) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;
                    if (getBaseActivityContext() != null) {
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                        ywLoadingDialog.show();
                    }
                    for (int i = 0; i < imageList.size(); i++) {
                        if (!"default".equals(imageList.get(i))) {
                            picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(imageList.get(i), getWindowManager().getDefaultDisplay().getWidth(),
                                    getWindowManager().getDefaultDisplay().getHeight())));
                        }
                    }
                    insert_feedback();
                } else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入反馈内容");
                }
                break;
        }
    }


    void initAdapter() {
        sss_adapter = new SSS_Adapter<FeedBackModel>(getBaseActivityContext(), R.layout.item_feedback) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, FeedBackModel bean, SSS_Adapter instance) {
                helper.setText(R.id.item_text, bean.name);
                if (bean.isSelect) {
                    helper.setBackgroundRes(R.id.item_text, R.drawable.bg_item_feedback_select_yes);
                    helper.setTextColor(R.id.item_text, getResources().getColor(R.color.mainColor));
                } else {
                    helper.setBackgroundRes(R.id.item_text, R.drawable.bg_item_feedback_select_no);
                    helper.setTextColor(R.id.item_text, getResources().getColor(R.color.grayness));
                }
                helper.getView(R.id.item_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).isSelect) {
                            list.get(position).isSelect = false;
                        } else {
                            list.get(position).isSelect = true;
                        }
                        sss_adapter.setList(list);
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        form.setAdapter(sss_adapter);
    }

    /**
     * 用户反馈
     */
    public void insert_feedback() {

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_feedback(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("form_id", form_id)
                            .put("contents", inputActivityFeedback.getText().toString().trim())
                            .put("picture", picture)
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
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

    /**
     * 用户反馈关键词信息
     */
    public void form() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.form(
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
                                        list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), FeedBackModel.class));
                                    }
                                    sss_adapter.setList(list);

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods_classify-0");
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
                    NineView.urlList(imageList);

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

        NineView.urlList(imageList);
    }

    @Override
    public void onSamePhotos(List<String> mRejectUrlList) {

    }
}
