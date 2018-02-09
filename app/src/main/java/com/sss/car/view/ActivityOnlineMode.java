package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
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
 * Created by leilei on 2017/12/27.
 */

public class ActivityOnlineMode extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_online)
    SimpleDraweeView showOnline;
    @BindView(R.id.click_online)
    LinearLayout clickOnline;
    @BindView(R.id.show_hidden)
    SimpleDraweeView showHidden;
    @BindView(R.id.click_hidden)
    LinearLayout clickHidden;
    @BindView(R.id.activity_online_mode)
    LinearLayout activityOnlineMode;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_mode);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        titleTop.setText("登录状态");
        customInit(activityOnlineMode,false,true,false);
        if ("1".equals(getIntent().getExtras().getString("online"))){
            showOnline.setVisibility(View.VISIBLE);
            showHidden.setVisibility(View.GONE);
        }else  if ("2".equals(getIntent().getExtras().getString("online"))){
            showHidden.setVisibility(View.VISIBLE);
            showOnline.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.back_top, R.id.click_online, R.id.click_hidden})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_online:
                setUsinfo("online","1");
                showOnline.setVisibility(View.VISIBLE);
                showHidden.setVisibility(View.GONE);
                break;
            case R.id.click_hidden:
                setUsinfo("online","2");
                showHidden.setVisibility(View.VISIBLE);
                showOnline.setVisibility(View.GONE);
                break;
        }
    }
    /**
     * 设置用户设置资料
     */
    public void setUsinfo(String key, String value) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUsinfo(
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
