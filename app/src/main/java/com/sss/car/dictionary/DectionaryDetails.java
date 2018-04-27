package com.sss.car.dictionary;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 文章详情
 * Created by leilei on 2017/11/4.
 */

public class DectionaryDetails extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.text_dectionary_details)
    TextView textDectionaryDetails;
    @BindView(R.id.dectionary_details)
    LinearLayout dectionaryDetails;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_dectionary_details)
    TextView titleDectionaryDetails;

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
        textDectionaryDetails = null;
        dectionaryDetails = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dectionary_details);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        ButterKnife.bind(this);
        customInit(dectionaryDetails, false, true, false);
        titleTop.setText(getIntent().getExtras().getString("title"));
        cateArticle();
        rightButtonTop.setText("收藏");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dectionaryCollectCancelCollect();
            }
        });
    }

    /**
     * 宝典分类==>文章详情
     */
    public void cateArticle() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cateArticle(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("article_id", getIntent().getExtras().getString("article_id"))
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
                                    textDectionaryDetails.setText(jsonObject.getJSONObject("data").getString("contents"));
                                    titleDectionaryDetails.setText(jsonObject.getJSONObject("data").getString("title"));
                                    if ("0".equals(jsonObject.getJSONObject("data").getString("is_collect"))){
                                        rightButtonTop.setText("收藏");
                                    }else {
                                        rightButtonTop.setText("已收藏");
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
     * 收藏
     */
    public void dectionaryCollectCancelCollect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.dectionaryCollectCancelCollect(
                    new JSONObject()
                            .put("type", "book")
                            .put("collect_id", getIntent().getExtras().getString("article_id"))
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
                                    if ("0".equals(jsonObject.getJSONObject("data").getString("code"))){
                                        rightButtonTop.setText("收藏");
                                    }else {
                                        rightButtonTop.setText("已收藏");
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:shop-0");
            e.printStackTrace();
        }
    }


    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
