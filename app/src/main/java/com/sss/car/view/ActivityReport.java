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
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.ReportModel;

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
 * 举报页
 * Created by leilei on 2017/8/30.
 */

@SuppressWarnings("ALL")
public class ActivityReport extends BaseActivity implements NineView.NineViewShowCallBack {

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.list_activity_report)
    InnerListview listActivityReport;
    @BindView(R.id.activity_report)
    LinearLayout activityReport;

    List<String> imageList = new ArrayList<>();


    boolean black;

    List<ReportModel> list = new ArrayList<>();


    SSS_Adapter sss_adapter;

    View view;
    SwitchButton switchButton;
    TextView report_black_activity_report;
    @BindView(R.id.switch_black_activity_report)
    SwitchButton switchBlackActivityReport;
    @BindView(R.id.layout_black_activity_report)
    LinearLayout layoutBlackActivityReport;
    @BindView(R.id.report_black_activity_report)
    TextView reportBlackActivityReport;
    @BindView(R.id.nine)
    NineView nine;
    @BindView(R.id.input)
    EditText input;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        activityReport = null;
        listActivityReport = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        if (list != null) {
            list.clear();
        }
        list = null;

        view = null;
        switchButton = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        customInit(activityReport, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        imageList.add("default");
        nine.urlList(imageList);
        titleTop.setText("举报");
        initAdapter();
        getcomplain();
        if ("private".equals(getIntent().getExtras().getString("type"))) {
            black = true;
            layoutBlackActivityReport.setVisibility(View.VISIBLE);
        } else {
            layoutBlackActivityReport.setVisibility(View.GONE);
        }
        nine.maxCount(9)
                .isShowCloseButton(true)
                .spacing(3)
                .CloseSize(30)
                .isRemoveSame(true)
                .isShowAll(false);
        nine.setNineViewShowCallBack(this);

    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<ReportModel>(getBaseActivityContext(), R.layout.item_report) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final ReportModel bean, SSS_Adapter instance) {

                helper.setChecked(R.id.cb_item_report, bean.isChoose);
                helper.setText(R.id.text_item_report, bean.name);
                helper.getView(R.id.click_report).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.isChoose) {
                            bean.isChoose = false;

                        } else {
                            bean.isChoose = true;
                        }
                        sss_adapter.setList(list);
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listActivityReport.setAdapter(sss_adapter);
    }


    @OnClick({R.id.back_top, R.id.report_black_activity_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.report_black_activity_report:
                int count = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isChoose) {
                        count++;
                    }
                }
                if (count == 0 && StringUtils.isEmpty(input.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "填写您的举报理由");
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isChoose) {
                        jsonArray.put(list.get(i).from_id);
                    }
                }
                if ("private".equals(getIntent().getExtras().getString("type"))) {
                    try {//举报个人
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ywLoadingDialog = null;
                        if (getBaseActivityContext() != null) {
                            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                            ywLoadingDialog.show();
                        }
                        JSONArray picture=new JSONArray();
                        for (int i = 0; i < imageList.size(); i++) {
                            if (!"default".equals(imageList.get(i))) {
                                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(imageList.get(i), getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())));
                            }
                        }
                        complain(new JSONObject()
                                .put("type", "member")
                                .put("contents",input.getText().toString().trim())
                                .put("member_pid", Config.member_id)
                                .put("picture",picture)
                                .put("member_id", getIntent().getExtras().getString("id"))
                                .put("form_id", jsonArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {//举报群
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ywLoadingDialog = null;
                        if (getBaseActivityContext() != null) {
                            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                            ywLoadingDialog.show();
                        }
                        JSONArray picture=new JSONArray();
                        for (int i = 0; i < imageList.size(); i++) {
                            if (!"default".equals(imageList.get(i))) {
                                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(imageList.get(i), getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())));
                            }
                        }
                        complain(new JSONObject()
                                .put("type", "group")
                                .put("contents",input.getText().toString().trim())
                                .put("member_pid", Config.member_id)
                                .put("picture",picture)
                                .put("group_id", getIntent().getExtras().getString("id"))
                                .put("form_id", jsonArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    /**
     * 获取要举报的条目
     */
    public void getcomplain() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.complain(
                new JSONObject()
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
                                    list.add(new ReportModel(jsonArray.getJSONObject(i).getString("form_id"),
                                            jsonArray.getJSONObject(i).getString("name"),
                                            false));
                                }
                                sss_adapter.setList(list);
                            } else {
                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:complain-0");
                            e.printStackTrace();
                        }
                    }
                })));
    }

    /**
     * 举报
     */
    public void complain(JSONObject jsonObject) {

        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.reportComplain(
                jsonObject.toString(), new StringCallback() {
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
                            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:complain-0");
                            e.printStackTrace();
                        }
                    }
                })));
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
