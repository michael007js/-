package com.sss.car.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.AddressPicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.AddressCreate;
import com.sss.car.EventBusModel.DefaultAddressChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.AddressInfoModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/18.
 */

public class ActivityMyDataAdressAddAndEditAdressPublic extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.receiving_activity_my_data_adress_add_and_edit_adress_public)
    EditText receivingActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.mobile_activity_my_data_adress_add_and_edit_adress_public)
    EditText mobileActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.administrative_region_activity_my_data_adress_add_and_edit_adress_public)
    LinearLayout administrativeRegionActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.adress_activity_my_data_adress_add_and_edit_adress_public)
    EditText adressActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.set_activity_my_data_adress_add_and_edit_adress_public)
    Switch setActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.default_activity_my_data_adress_add_and_edit_adress_public)
    LinearLayout defaultActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.delete_activity_my_data_adress_add_and_edit_adress_public)
    LinearLayout deleteActivityMyDataAdressAddAndEditAdressPublic;
    @BindView(R.id.activity_my_data_adress_add_and_edit_adress_public)
    LinearLayout activityMyDataAdressAddAndEditAdressPublic;
    YWLoadingDialog ywLoadingDialog;

    String isDefault = "0";
    @BindView(R.id.administrative_region_activity_my_data_adress_show_add_and_edit_adress_public)
    TextView administrativeRegionActivityMyDataAdressShowAddAndEditAdressPublic;
    String province;
    String city;
    String district;
    AddressInfoModel addressInfoModel;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        province = null;
        city = null;
        district = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        topParent = null;
        addressInfoModel = null;
        receivingActivityMyDataAdressAddAndEditAdressPublic = null;
        mobileActivityMyDataAdressAddAndEditAdressPublic = null;
        administrativeRegionActivityMyDataAdressAddAndEditAdressPublic = null;
        adressActivityMyDataAdressAddAndEditAdressPublic = null;
        setActivityMyDataAdressAddAndEditAdressPublic = null;
        defaultActivityMyDataAdressAddAndEditAdressPublic = null;
        deleteActivityMyDataAdressAddAndEditAdressPublic = null;
        activityMyDataAdressAddAndEditAdressPublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_adress_add_and_edit_adress_public);
        ButterKnife.bind(this);

        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败error-1");
            }
            finish();
        }
        if ("add".equals(getIntent().getExtras().getString("mode"))) {
            titleTop.setText("添加信息");
            rightButtonTop.setText("保存");
            rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
            rightButtonTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUserAdress(null);
                }
            });
            defaultActivityMyDataAdressAddAndEditAdressPublic.setVisibility(View.VISIBLE);
            deleteActivityMyDataAdressAddAndEditAdressPublic.setVisibility(View.GONE);
            setActivityMyDataAdressAddAndEditAdressPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isDefault = "1";
                    } else {
                        isDefault = "0";
                    }
                }
            });
        } else if ("edit".equals(getIntent().getExtras().getString("mode"))) {
            addressInfoModel = getIntent().getExtras().getParcelable("model");
            if (addressInfoModel == null) {
                if (getBaseActivityContext() != null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据传输失败error-2");
                }
                finish();
            }

            titleTop.setText("编辑信息");
            rightButtonTop.setText("保存");
            province = addressInfoModel.province;
            city = addressInfoModel.city;
            district = addressInfoModel.county;
            defaultActivityMyDataAdressAddAndEditAdressPublic.setVisibility(View.GONE);
            deleteActivityMyDataAdressAddAndEditAdressPublic.setVisibility(View.VISIBLE);
            deleteActivityMyDataAdressAddAndEditAdressPublic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDeleteDialog(addressInfoModel);
                }
            });

            receivingActivityMyDataAdressAddAndEditAdressPublic.setText(addressInfoModel.recipients);
            mobileActivityMyDataAdressAddAndEditAdressPublic.setText(addressInfoModel.mobile);
            administrativeRegionActivityMyDataAdressShowAddAndEditAdressPublic.setText(addressInfoModel.province + addressInfoModel.city + addressInfoModel.county);
            adressActivityMyDataAdressAddAndEditAdressPublic.setText(addressInfoModel.address);

            if ("1".equals(addressInfoModel.is_default)) {
                setActivityMyDataAdressAddAndEditAdressPublic.setChecked(true);
            } else {
                setActivityMyDataAdressAddAndEditAdressPublic.setChecked(false);
            }
            rightButtonTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUserAdress(addressInfoModel.address_id);
                }
            });
        }
        customInit(activityMyDataAdressAddAndEditAdressPublic,false,true,false);

    }

    /**
     * 创建删除对话框
     */
    void createDeleteDialog(final AddressInfoModel addressInfoModel) {
        if (getBaseActivityContext() != null) {
            return;
        }
        String[] stringItems = {"删除"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                .isTitleShow(true)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("是否要删除您的收货地址");
        dialog.titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        deleteAddress(addressInfoModel);
                        break;
                }
            }
        });
    }

    /**
     * 删除地址
     */
    void deleteAddress(AddressInfoModel addressInfoModel) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteAddress(
                    new JSONObject()
                            .put("address_id", addressInfoModel.address_id)
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
                                        finish();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: delete address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: delete address-0");
            }
            e.printStackTrace();
        }
    }


    @OnClick({R.id.back_top, R.id.administrative_region_activity_my_data_adress_add_and_edit_adress_public})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.administrative_region_activity_my_data_adress_add_and_edit_adress_public:
                if (getBaseActivityContext() != null) {
                    AddressPicker addressPicker = new AddressPicker(getBaseActivityContext());
                    addressPicker.setAddressListener(new AddressPicker.OnAddressListener() {
                        @Override
                        public void onAddressSelected(String province, String city, String district) {
                            ActivityMyDataAdressAddAndEditAdressPublic.this.province = province;
                            ActivityMyDataAdressAddAndEditAdressPublic.this.city = city;
                            ActivityMyDataAdressAddAndEditAdressPublic.this.district = district;
                            administrativeRegionActivityMyDataAdressShowAddAndEditAdressPublic.setText(province + city + district);
                        }
                    });
                    addressPicker.show();
                }
                break;
        }
    }


    /**
     * 保存地址信息
     *
     * @param address_id 单项参数,如果传,则表示此方法用来更新信息,如果不传,则表示此方法用来保存信息
     */
    public void saveUserAdress(String address_id) {

        if (StringUtils.isEmpty(receivingActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "收货人不能为空");
            }
            return;
        }

        if (StringUtils.isEmpty(mobileActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "联系电话不能为空");
            }
            return;
        }

        if ("请选择".equals(administrativeRegionActivityMyDataAdressShowAddAndEditAdressPublic.getText().toString().trim())) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "请选择省市区信息");
            }
            return;
        }
        if (StringUtils.isEmpty(adressActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim()) || adressActivityMyDataAdressAddAndEditAdressPublic.getText().length() < 6) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "详细地址不能为空或过短");
            }
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.saveUserAdress(
                    new JSONObject()
                            .put("mobile", mobileActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim())
                            .put("recipients", receivingActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim())
                            .put("address", adressActivityMyDataAdressAddAndEditAdressPublic.getText().toString().trim())
                            .put("is_default", isDefault)
                            .put("province", province)
                            .put("city", city)
                            .put("address_id", address_id)
                            .put("county", district)
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
                                        EventBus.getDefault().post(new DefaultAddressChanged());
                                        EventBus.getDefault().post(new AddressCreate(getIntent().getExtras().getString("from")));
                                        finish();
                                    } else {
                                        if (getBaseActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: save address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: save address-0");
            }
            e.printStackTrace();
        }
    }


}
