package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedCollectLabel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.GridLayoutDown;
import com.sss.car.custom.GridLayoutUp;
import com.sss.car.model.LabelModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by leilei on 2017/9/12.
 */

public class ActivityCollectLable extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_activity_collect_lable)
    GridLayoutUp topActivityCollectLable;
    @BindView(R.id.bottom_activity_collect_lable)
    GridLayoutDown bottomActivityCollectLable;
    @BindView(R.id.activity_collect_lable)
    LinearLayout activityCollectLable;
    List<String> selectLableListId = new ArrayList<>();


    YWLoadingDialog ywLoadingDialog;
    String textChanged;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_lable);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败Error-2");
            finish();
        }
        selectLableListId = getIntent().getStringArrayListExtra("data");
        if (selectLableListId == null || selectLableListId.size() == 0) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败Error-1");
            finish();
        }
        customInit(activityCollectLable, false, true, false);
        titleTop.setText("编辑标签");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));


        topActivityCollectLable.init(getBaseActivityContext(), new GridLayoutUp.GridLayoutUpOperationCallBack() {
            @Override
            public void onAddEdit(final EditText edittect) {
                edittect.setHint("添加标签");
                edittect.setTextSize(13f);
                edittect.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        textChanged=edittect.getText().toString().trim();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

            @Override
            public void onDelete(String str ) {
                bottomActivityCollectLable.add(createTextView(str));
            }
        });

        bottomActivityCollectLable.init(new GridLayoutDown.GridLayoutDownOperationCallBack() {
            @Override
            public void onClick(String str ) {
                topActivityCollectLable.add(createTextView(str));
            }
        });

        if (selectLableListId.size() == 1) {
            chooseLabelList(selectLableListId.get(0));
        }
        myLable();
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                JSONArray name=new JSONArray();
                JSONArray ids=new JSONArray();
                for (int i = 0; i < topActivityCollectLable.getList().size(); i++) {
                    name.put(topActivityCollectLable.getList().get(i).getText().toString().trim());
                }
                if (!StringUtils.isEmpty(textChanged)){
                    name.put(textChanged);
                }
                for (int i = 0; i < selectLableListId.size(); i++) {
                    ids.put(selectLableListId.get(i));
                }
                upLoadLabel(name,ids);
                break;
        }
    }

    TextView createTextView(String str) {
        TextView textView = new TextView(getBaseActivityContext());
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER);
        textView.setText(str);
        APPOftenUtils.setBackgroundOfVersion(textView, getResources().getDrawable(R.drawable.bg_tip));
        return textView;
    }

    /**
     * 获取标签
     */
    public void chooseLabelList(String id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.chooseLabelList(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("ids",id)
                            .put("type", "community")
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
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                            LabelModel labelModel = new LabelModel();
//                                            labelModel.label_id = jsonArray.getJSONObject(i).getString("label_id");
//                                            labelModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
//                                            labelModel.name = jsonArray.getJSONObject(i).getString("name");

                                            JSONArray jsonArray1=jsonArray.getJSONObject(i).getJSONArray("name");
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                topActivityCollectLable.add(createTextView(" "+jsonArray1.getString(j)+" "));
                                            }

                                        }
                                    }

                                } else {
//                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的标签
     */
    public void myLable( ) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.myLable(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "community")
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
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                            LabelModel labelModel = new LabelModel();
//                                            labelModel.label_id = jsonArray.getJSONObject(i).getString("label_id");
//                                            labelModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
//                                            labelModel.name = jsonArray.getJSONObject(i).getString("name");
                                            bottomActivityCollectLable.add(createTextView(" "+jsonArray.getJSONObject(i).getString("name")+" "));
                                        }
                                    }
                                } else {
//                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
            e.printStackTrace();
        }
    }


    /**
     * 上传标签
     */
    public void upLoadLabel(JSONArray name,JSONArray ids) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.upLoadLabel(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "community")
                            .put("name",name)
                            .put("ids",ids)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage().toString());
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    EventBus.getDefault().post(new ChangedCollectLabel());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:label-0");
            e.printStackTrace();
        }
    }
}
