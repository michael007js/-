package com.sss.car.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.AddressCreate;
import com.sss.car.EventBusModel.AddressDelete;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentMyDataAddressList;
import com.sss.car.fragment.FragmentMyDataAdressNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/18.
 */

public class ActivityMyDataAdress extends BaseFragmentActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.activity_adress)
    LinearLayout activityAdress;

    YWLoadingDialog ywLoadingDialog;

    FragmentMyDataAdressNull fragmentMyDataAdressNull;
    FragmentManager fragmentManager;
    FragmentMyDataAddressList fragmentMyDataAddressList;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        activityAdress = null;
        fragmentMyDataAdressNull = null;
        fragmentMyDataAddressList = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_adress);
        ButterKnife.bind(this);
        titleTop.setText("商品资料");
        customInit(activityAdress, false, true, true);
        fragmentManager = getSupportFragmentManager();
        getUserAdress();
    }


    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentMyDataAddressList != null) {
            fragmentMyDataAddressList.getUserAdress(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddressCreate event) {
        if ("FragmentMyDataAdressNull".equals(event.from)) {
            showAddressList();
        } else {
            if (fragmentMyDataAddressList != null) {
                fragmentMyDataAddressList.getUserAdress(1);
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddressDelete event) {
        showNull();

    }

    /**
     * 显示空
     */
    void showNull() {
        FragmentUtils.hideFragments(fragmentManager);
        if (fragmentMyDataAdressNull == null) {
            fragmentMyDataAdressNull = new FragmentMyDataAdressNull();
            FragmentUtils.addFragment(fragmentManager, fragmentMyDataAdressNull, R.id.parent_activity_adress);
        } else {
            FragmentUtils.showFragment(fragmentMyDataAdressNull);
        }
        titleTop.setText("管理收货信息");
    }

    /**
     * 显示地址列表
     */
    void showAddressList() {
        FragmentUtils.hideFragments(fragmentManager);
        if (fragmentMyDataAddressList == null) {
            fragmentMyDataAddressList = new FragmentMyDataAddressList();
            FragmentUtils.addFragment(fragmentManager, fragmentMyDataAddressList, R.id.parent_activity_adress);
        } else {
            FragmentUtils.showFragment(fragmentMyDataAddressList);
            if (fragmentMyDataAddressList != null) {
                fragmentMyDataAddressList.getUserAdress(1);
            }
        }
        titleTop.setText("管理收货信息");
    }

    /**
     * 请求获取地址列表
     */
    public void getUserAdress() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUserAdress(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                        showAddressList();
                                    } else {
                                        showNull();
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:get address-0");
            }
            e.printStackTrace();
        }
    }
}
