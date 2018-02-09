package com.sss.car.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SettingModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.sss.car.R.id.parent;

/**
 * 消息设置公用界面
 * Created by leilei on 2017/11/7.
 */
@SuppressLint("ValidFragment")
public class FragmentSetMessageReceive extends BaseFragment {

    @BindView(R.id.shield)
    LinearLayout shield;
    @BindView(R.id.open)
    LinearLayout open;
    @BindView(R.id.open_but_do_not_shield)
    LinearLayout openButDoNotShield;
    Unbinder unbinder;
    @BindView(R.id.cb_shield)
    CheckBox cbShield;
    @BindView(R.id.cb_open)
    CheckBox cbOpen;
    @BindView(R.id.cb_open_but_do_not_shield)
    CheckBox cbOpenButDoNotShield;
    String type;
    String requestKey, requestValue;
    YWLoadingDialog ywLoadingDialog;


    public FragmentSetMessageReceive(String type) {
        this.type = type;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_set_message_receive;
    }

    @Override
    protected void lazyLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(100);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case "1":
                                    requestKey = "interact";
                                    break;
                                case "2":
                                    requestKey = "comment";
                                    break;
                                case "3":
                                    requestKey = "order";
                                    break;
                                case "4":
                                    requestKey = "system";
                                    break;
                                case "5":
                                    requestKey = "share";
                                    break;
                            }

                            cbShield.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cbShield.setChecked(true);
                                    cbOpen.setChecked(false);
                                    cbOpenButDoNotShield.setChecked(false);
                                    requestValue = "0";
                                }
                            });
                            cbOpen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cbShield.setChecked(false);
                                    cbOpen.setChecked(true);
                                    cbOpenButDoNotShield.setChecked(false);
                                    requestValue = "1";
                                }
                            });
                            cbOpenButDoNotShield.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cbShield.setChecked(false);
                                    cbOpen.setChecked(false);
                                    cbOpenButDoNotShield.setChecked(true);
                                    requestValue = "2";
                                }
                            });

                            getUsinfo();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void stopLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.shield, R.id.open, R.id.open_but_do_not_shield})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shield:
                requestValue = "0";
                cbShield.setChecked(true);
                cbOpen.setChecked(false);
                cbOpenButDoNotShield.setChecked(false);
                break;
            case R.id.open:
                requestValue = "1";
                cbShield.setChecked(false);
                cbOpen.setChecked(true);
                cbOpenButDoNotShield.setChecked(false);
                break;
            case R.id.open_but_do_not_shield:
                cbShield.setChecked(false);
                cbOpen.setChecked(false);
                cbOpenButDoNotShield.setChecked(true);
                requestValue = "2";
                break;
        }
    }

    public void request(){
        if (StringUtils.isEmpty(requestKey)||StringUtils.isEmpty(requestValue)){
            ToastUtils.showShortToast(getBaseFragmentActivityContext(),"请选择消息类型");
            return;

        }
        setUsinfo();
    }


    /**
     * 获取用户设置资料
     */
    public void getUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    SettingModel  settingModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SettingModel.class);
                                    switch (type) {
                                        case "1":
                                            switch (settingModel.interact){
                                                case "0":
                                                    requestValue="0";
                                                    cbShield.setChecked(true);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "1":
                                                    requestValue="1";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(true);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "2":
                                                    requestValue="2";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(true);
                                                    break;
                                            }
                                            break;
                                        case "2":
                                            switch (settingModel.comment){
                                                case "0":
                                                    requestValue="0";
                                                    cbShield.setChecked(true);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "1":
                                                    requestValue="1";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(true);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "2":
                                                    requestValue="2";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(true);
                                                    break;
                                            }
                                            break;
                                        case "3":
                                            switch (settingModel.order){
                                                case "0":
                                                    requestValue="0";
                                                    cbShield.setChecked(true);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "1":
                                                    requestValue="1";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(true);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "2":
                                                    requestValue="2";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(true);
                                                    break;
                                            }
                                            break;
                                        case "4":
                                            switch (settingModel.system){
                                                case "0":
                                                    requestValue="0";
                                                    cbShield.setChecked(true);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "1":
                                                    requestValue="1";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(true);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "2":
                                                    requestValue="2";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(true);
                                                    break;
                                            }
                                            break;
                                        case "5":
                                            switch (settingModel.share){
                                                case "0":
                                                    requestValue="0";
                                                    cbShield.setChecked(true);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "1":
                                                    requestValue="1";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(true);
                                                    cbOpenButDoNotShield.setChecked(false);
                                                    break;
                                                case "2":
                                                    requestValue="2";
                                                    cbShield.setChecked(false);
                                                    cbOpen.setChecked(false);
                                                    cbOpenButDoNotShield.setChecked(true);
                                                    break;
                                            }
                                            break;
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


    /**
     * 设置用户设置资料
     */
    public void setUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put(requestKey, requestValue)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

}
