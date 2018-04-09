package com.sss.car.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityChangeInfo;
import com.sss.car.view.ActivityMyDataCarSetCarInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/18.
 */

public class FragmentMyDataCarMyCurrentCarInfo extends BaseFragment {
    @BindView(R.id.car_logo_fragment_my_data_car_my_current_car_info)
    ImageView carLogoFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.car_name_fragment_my_data_car_my_current_car_info)
    TextView carNameFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.car_type_fragment_my_data_car_my_current_car_info)
    TextView carTypeFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.displacement_show_fragment_my_data_car_my_current_car_info)
    TextView displacementShowFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.displacement_fragment_my_data_car_my_current_car_info)
    LinearLayout displacementFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.year_show_fragment_my_data_car_my_current_car_info)
    TextView yearShowFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.year_fragment_my_data_car_my_current_car_info)
    LinearLayout yearFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.type_show_fragment_my_data_car_my_current_car_info)
    TextView typeShowFragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.type_fragment_my_data_car_my_current_car_info)
    LinearLayout typeFragmentMyDataCarMyCurrentCarInfo;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
   public String vehicle_id, ids, name, logo, type, style,displacement, year;

    boolean is_other;

    public FragmentMyDataCarMyCurrentCarInfo() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        carLogoFragmentMyDataCarMyCurrentCarInfo = null;
        carNameFragmentMyDataCarMyCurrentCarInfo = null;
        carTypeFragmentMyDataCarMyCurrentCarInfo = null;
        displacementShowFragmentMyDataCarMyCurrentCarInfo = null;
        displacementFragmentMyDataCarMyCurrentCarInfo = null;
        yearShowFragmentMyDataCarMyCurrentCarInfo = null;
        yearFragmentMyDataCarMyCurrentCarInfo = null;
        typeShowFragmentMyDataCarMyCurrentCarInfo = null;
        typeFragmentMyDataCarMyCurrentCarInfo = null;
        unbinder = null;
        vehicle_id = null;
        ids = null;
        name = null;
        logo = null;
        type = null;
        style = null;

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_my_data_car_my_current_car_info;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            myCurrentCarInfo();
        }
    }

    @Override
    protected void stopLoad() {
        clearRequestCall();
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

    @OnClick({R.id.displacement_fragment_my_data_car_my_current_car_info, R.id.year_fragment_my_data_car_my_current_car_info, R.id.type_fragment_my_data_car_my_current_car_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.displacement_fragment_my_data_car_my_current_car_info:
                if (is_other == false) {
                    jump("1");
                }else {
                    startActivity(new Intent(getBaseFragmentActivityContext(),ActivityChangeInfo.class)
                            .putExtra("type", "displacement")
                            .putExtra("canChange", true)
                            .putExtra("extra",displacement ));
                }
                break;
            case R.id.year_fragment_my_data_car_my_current_car_info:
                if (is_other == false) {
                    jump("2");
                }else {
                    startActivity(new Intent(getBaseFragmentActivityContext(),ActivityChangeInfo.class)
                            .putExtra("type", "year")
                            .putExtra("canChange", true)
                            .putExtra("extra",year ));
                }
                break;
            case R.id.type_fragment_my_data_car_my_current_car_info:
                if (is_other == false) {
                    jump("3");
                }else {
                    startActivity(new Intent(getBaseFragmentActivityContext(),ActivityChangeInfo.class)
                            .putExtra("type", "style")
                            .putExtra("canChange", true)
                            .putExtra("extra",style ));
                }
                break;
        }
    }




    public void add_vehicle() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.add_vehicle(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("name", name)
                            .put("type", type)
                            .put("displacement",displacement)
                            .put("year", year)
                            .put("style", style)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    myCurrentCarInfo();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }



    /**
     * 跳转到选择页面
     */
    void jump(String jumpMode) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataCarSetCarInfo.class)
                    .putExtra("name", name)
                    .putExtra("type", type)
                    .putExtra("logo", logo)
                    .putExtra("id", vehicle_id)
                    .putExtra("jumpMode", jumpMode)
                    .putExtra("ids", ids));
        }
    }


    /**
     * 请求我的默认车辆信息
     */
    public void myCurrentCarInfo() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.myCurrentCarInfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
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
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        carNameFragmentMyDataCarMyCurrentCarInfo.setText(jsonObject.getJSONObject("data").getString("name") + jsonObject.getJSONObject("data").getString("type"));
                                        carTypeFragmentMyDataCarMyCurrentCarInfo.setText(jsonObject.getJSONObject("data").getString("year")
                                                + "  " + jsonObject.getJSONObject("data").getString("displacement")
                                                + jsonObject.getJSONObject("data").getString("style")
                                        );
                                        carLogoFragmentMyDataCarMyCurrentCarInfo.setTag(R.id.glide_tag, Config.url + jsonObject.getJSONObject("data").getString("brand"));
                                        if (getBaseFragmentActivityContext() != null) {
                                            addImageViewList(GlidUtils.downLoader(false, carLogoFragmentMyDataCarMyCurrentCarInfo, getBaseFragmentActivityContext()));
                                        }
                                        displacementShowFragmentMyDataCarMyCurrentCarInfo.setText(jsonObject.getJSONObject("data").getString("displacement"));
                                        yearShowFragmentMyDataCarMyCurrentCarInfo.setText(jsonObject.getJSONObject("data").getString("year"));
                                        typeShowFragmentMyDataCarMyCurrentCarInfo.setText(jsonObject.getJSONObject("data").getString("displacement") + jsonObject.getJSONObject("data").getString("style"));
                                        vehicle_id = jsonObject.getJSONObject("data").getString("vehicle_id");
                                        ids = jsonObject.getJSONObject("data").getString("ids");
                                        name = jsonObject.getJSONObject("data").getString("name");
                                        displacement= jsonObject.getJSONObject("data").getString("displacement");
                                        logo = jsonObject.getJSONObject("data").getString("brand");
                                        year = jsonObject.getJSONObject("data").getString("year");
                                        type = jsonObject.getJSONObject("data").getString("type");
                                        style = jsonObject.getJSONObject("data").getString("style");
                                        is_other = "1".equals(jsonObject.getJSONObject("data").getString("is_other"));
                                    } else {
                                        if (getBaseFragmentActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: car info-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: car info-0");
            }
            e.printStackTrace();
        }
    }
}
