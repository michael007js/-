package com.sss.car.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.RichTextEditor;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.PublishPostAdapter;
import com.sss.car.model.CateModel;

import org.greenrobot.eventbus.EventBus;
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
 * Created by leilei on 2017/9/7.
 */

public class ActivityPublishPost extends BaseActivity   {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input_activity_publish_post)
    RichTextEditor inputActivityPublishPost;
    @BindView(R.id.send_activity_publish_post)
    TextView sendActivityPublishPost;
    @BindView(R.id.activity_publish_post)
    LinearLayout activityPublishPost;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;



    List<CateModel> cateList = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    PublishPostAdapter publishPostAdapter;

    String cate_id;
    EditText titleActivityPublishPostHead;
    Spinner chooseActivityPublishPostHead;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    void initHead(){
        View view= LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_publish_post_head,null);
        chooseActivityPublishPostHead= $.f(view,R.id.choose_activity_publish_post_head);
        titleActivityPublishPostHead=$.f(view,R.id.title_activity_publish_post_head);
        inputActivityPublishPost.init(getBaseActivityContext(),view);
    }

    @Override
    protected void onDestroy() {
        cate_id = null;
        if (publishPostAdapter != null) {
            publishPostAdapter.clear();
        }
        publishPostAdapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (cateList != null) {
            cateList.clear();
        }
        cateList = null;
        backTop = null;
        titleTop = null;
        inputActivityPublishPost = null;
        sendActivityPublishPost = null;
        activityPublishPost = null;
        rightButtonTop = null;
        titleActivityPublishPostHead = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);
        ButterKnife.bind(this);
        customInit(activityPublishPost, false, true, false);
        titleTop.setText("发布帖子");
        rightButtonTop.setText("插入图片");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        initHead();

        postsCate();
    }




    @OnClick({R.id.back_top, R.id.right_button_top, R.id.send_activity_publish_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                APPOftenUtils.createPhotoChooseDialog(0, 3, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        for (int i = 0; i < resultList.size(); i++) {
                            inputActivityPublishPost.insertImage(resultList.get(i).getPhotoPath(), getWindowManager().getDefaultDisplay().getWidth() - 30,  getWindowManager().getDefaultDisplay().getWidth());
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.send_activity_publish_post:
                try {
                    send();
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据生成错误");
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 获取社区分类
     */
    public void postsCate() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsCate(
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
                                        CateModel cateModel = new CateModel();
                                        cateModel.cate_id = jsonArray.getJSONObject(i).getString("cate_id");
                                        cateModel.cate_name = jsonArray.getJSONObject(i).getString("cate_name");
                                        cateModel.logo = Uri.parse(Config.url + jsonArray.getJSONObject(i).getString("logo"));
                                        cateList.add(cateModel);
                                    }




                                    chooseActivityPublishPostHead .setAdapter(new PublishPostAdapter(cateList,getBaseActivityContext()));
                                    chooseActivityPublishPostHead.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            cate_id = cateList.get(position).cate_id;
                                            chooseActivityPublishPostHead.setSelection(position);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }


                                    });













                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cate-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cate-0");
            e.printStackTrace();
        }
    }


    void send() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isEmpty(titleActivityPublishPostHead.getText().toString().trim()) && jsonArray.length() == 0) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请填写标题或内容");
            return;
        }

        if (StringUtils.isEmpty(cate_id)) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请选择分类");
            return;
        }
        if (getBaseActivityContext() != null) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        List<RichTextEditor.EditData> list = inputActivityPublishPost.buildEditData();

        for (int i = 0; i < list.size(); i++) {
            LogUtils.e(list.get(i).toString());
            if (!StringUtils.isEmpty(list.get(i).imagePath)) {
                jsonArray.put(
                        new JSONObject()
                                .put("img", ConvertUtils.bitmapToBase64(
                                        BitmapUtils.decodeSampledBitmapFromFile(
                                                list.get(i).imagePath, getWindowManager().getDefaultDisplay().getWidth(),
                                                getWindowManager().getDefaultDisplay().getHeight()
                                        ))
                                )
                );

            } else if (!StringUtils.isEmpty(list.get(i).inputStr)) {
                jsonArray.put(new JSONObject().put("text", list.get(i).inputStr));
            }
        }

        publishPosts(jsonArray);
    }

    /**
     * 发布帖子
     */
    public void publishPosts(    JSONArray jsonArray) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.publishPostsSpecial(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("title", titleActivityPublishPostHead.getText().toString().trim())
                            .put("cate_id", cate_id)
                            .put("contents", jsonArray)
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
                                    EventBus.getDefault().post(new ChangedPostsModel());
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cate-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Cate-0");
            e.printStackTrace();
        }
    }

}
