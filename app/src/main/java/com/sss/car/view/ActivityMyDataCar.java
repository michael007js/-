package com.sss.car.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.EventBusModel.ShowNullCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentMyDataCarMyCurrentCarInfo;
import com.sss.car.fragment.FragmentMyDataCarNull;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/16.
 */

public class ActivityMyDataCar extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.parent_activity_my_data_car)
    FrameLayout parentActivityMyDataCar;
    @BindView(R.id.activity_my_data_car)
    LinearLayout activityMyDataCar;
    FragmentManager fragmentManager;
    YWLoadingDialog ywLoadingDialog;
    FragmentMyDataCarNull fragmentMyDataCarNull;
    FragmentMyDataCarMyCurrentCarInfo fragmentMyDataCarMyCurrentCarInfo;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    /*当前是否显示的是车辆信息页面*/
    boolean isCarInfo = false;
    @BindView(R.id.black)
    ImageView black;
    @BindView(R.id.white)
    ImageView white;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        try {
            requestmyCar();
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:my car-0");
            }
            e.printStackTrace();
        }
        customInit(activityMyDataCar, false, true, true);
        titleTop.setText("爱车档案");

    }

    /**
     * 事件被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "displacement":
                fragmentMyDataCarMyCurrentCarInfo.displacement = event.msg;
                fragmentMyDataCarMyCurrentCarInfo.add_vehicle();
                break;
            case "year":
                fragmentMyDataCarMyCurrentCarInfo.year = event.msg;
                fragmentMyDataCarMyCurrentCarInfo.add_vehicle();
                break;
            case "style":
                fragmentMyDataCarMyCurrentCarInfo.style = event.msg;
                fragmentMyDataCarMyCurrentCarInfo.add_vehicle();
                break;
            case "pai":
                fragmentMyDataCarMyCurrentCarInfo.name = event.msg;
                fragmentMyDataCarMyCurrentCarInfo.add_vehicle();
                break;
            case "xi":
                fragmentMyDataCarMyCurrentCarInfo.type = event.msg;
                fragmentMyDataCarMyCurrentCarInfo.add_vehicle();
                break;


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        showCarInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowNullCarModel event) {
        showNull();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCarInfo) {
            if (fragmentMyDataCarMyCurrentCarInfo != null) {
                fragmentMyDataCarMyCurrentCarInfo.myCurrentCarInfo();
            }
        }
    }

    /**
     * 请求我的车辆信息
     *
     * @throws JSONException
     */
    void requestmyCar() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.myCar(
                new JSONObject()
                        .put("member_id", Config.member_id).toString(), new StringCallback() {
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
                                    showCarInfo();
                                } else {
                                    showNull();
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:my car-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }


    /**
     * 显示车辆信息
     */
    void showCarInfo() {
        white.setVisibility(View.VISIBLE);
        black.setVisibility(View.GONE);
        FragmentUtils.hideFragments(fragmentManager);
        if (fragmentMyDataCarMyCurrentCarInfo == null) {
            fragmentMyDataCarMyCurrentCarInfo = new FragmentMyDataCarMyCurrentCarInfo();
            FragmentUtils.addFragment(fragmentManager, fragmentMyDataCarMyCurrentCarInfo, R.id.parent_activity_my_data_car);
        } else {
            FragmentUtils.showFragment(fragmentMyDataCarMyCurrentCarInfo);
        }


        topParent.setBackgroundColor(getResources().getColor(com.blankj.utilcode.R.color.mainColor));
        titleTop.setText("我的爱车");
        titleTop.setTextColor(Color.WHITE);
        rightButtonTop.setText("管理车型");
        rightButtonTop.setTextColor(Color.WHITE);
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class));
            }
        });
        isCarInfo = true;
    }

    /**
     * 显示空
     */
    void showNull() {
        black.setVisibility(View.VISIBLE);
        white.setVisibility(View.GONE);
        FragmentUtils.hideFragments(fragmentManager);
        if (fragmentMyDataCarNull == null) {
            fragmentMyDataCarNull = new FragmentMyDataCarNull();
            FragmentUtils.addFragment(fragmentManager, fragmentMyDataCarNull, R.id.parent_activity_my_data_car);
        } else {
            FragmentUtils.showFragment(fragmentMyDataCarNull);
        }


        topParent.setBackgroundColor(getResources().getColor(com.blankj.utilcode.R.color.white));
        titleTop.setTextColor(Color.BLACK);
        rightButtonTop.setText("");
        rightButtonTop.setOnClickListener(null);
        isCarInfo = false;
    }

    @Override
    protected void onDestroy() {
        topParent = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        parentActivityMyDataCar = null;
        activityMyDataCar = null;
        fragmentManager = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        fragmentMyDataCarNull = null;
        super.onDestroy();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
