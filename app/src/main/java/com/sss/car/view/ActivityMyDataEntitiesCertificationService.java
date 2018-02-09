package com.sss.car.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CertificationServiceAdapter;
import com.sss.car.dao.CertificationServiceClickCallBack;
import com.sss.car.model.EntitiesCertificationServiceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static cn.jpush.android.api.JPushInterface.a.p;
import static com.sss.car.Config.member_id;

/**
 * Created by leilei on 2017/8/21.
 */

public class ActivityMyDataEntitiesCertificationService extends BaseActivity implements CertificationServiceClickCallBack, LoadImageCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.list_activity_my_data_entities_certification_setvice)
    RecyclerView listActivityMyDataEntitiesCertificationSetvice;
    @BindView(R.id.activity_my_data_entities_certification_setvice)
    LinearLayout activityMyDataEntitiesCertificationSetvice;
    YWLoadingDialog ywLoadingDialog;
    List<EntitiesCertificationServiceModel> list = new ArrayList<>();
    CertificationServiceAdapter certificationServiceAdapter;
    JSONArray jsonArray;

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
        backTop = null;
        titleTop = null;
        listActivityMyDataEntitiesCertificationSetvice = null;
        activityMyDataEntitiesCertificationSetvice = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_entities_certification_setvice);
        ButterKnife.bind(this);
        customInit(activityMyDataEntitiesCertificationSetvice, false, true, false);
        certificationServiceAdapter = new CertificationServiceAdapter(list, this,this, getBaseActivityContext());
        listActivityMyDataEntitiesCertificationSetvice.setAdapter(certificationServiceAdapter);
        listActivityMyDataEntitiesCertificationSetvice.setHasFixedSize(true);
        listActivityMyDataEntitiesCertificationSetvice.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        titleTop.setText("店铺服务");compayService();

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    /**
     * 服务范围
     */
    void compayService() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.compayService(
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
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        list.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(new EntitiesCertificationServiceModel(
                                                    jsonArray.getJSONObject(i).getString("id"),
                                                    jsonArray.getJSONObject(i).getString("name"),
                                                    jsonArray.getJSONObject(i).getString("is_check")
                                            ));
                                        }

                                        certificationServiceAdapter.refresh(list);

                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay service-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay service-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 设置服务范围
     */
    void compaySetService(JSONArray jsonArray) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        }
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setCompany(
                    new JSONObject()
                            .put("server_scope", jsonArray)
                            .put("member_id", member_id)
                            .toString(), "设置服务范围", new StringCallback() {
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
                                        compayService();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay service-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: compay service-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 选择被改变
     */

    @Override
    public void onClickChange(int pos, List<EntitiesCertificationServiceModel> list) {
        LogUtils.e("onClickChange");
        jsonArray = null;
        jsonArray = new JSONArray();
        if ("0".equals(list.get(pos).is_check)){
            jsonArray.put(list.get(pos).id);
        }
        for (int i = 0; i < list.size(); i++) {
            if (i != pos && "1".equals(list.get(i).is_check)) {
                jsonArray.put(list.get(i).id);
            }
        }
        compaySetService(jsonArray);
    }

    /**
     * 图片加载回调
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }
}
