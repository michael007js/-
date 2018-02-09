package com.sss.car.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.AddressDelete;
import com.sss.car.EventBusModel.DefaultAddressChanged;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.AdressListAdapter;
import com.sss.car.dao.AdressListClickCallBack;
import com.sss.car.model.AddressInfoModel;
import com.sss.car.view.ActivityMyDataAdressAddAndEditAdressPublic;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/19.
 */

public class FragmentMyDataAddressList extends BaseFragment implements LoadImageCallBack, AdressListClickCallBack {
    @BindView(R.id.listview_frgment_my_data_address_addresslist)
    ListView listviewFrgmentMyDataAddressAddresslist;
    @BindView(R.id.add_info_frgment_my_data_address_addresslist)
    TextView addInfoFrgmentMyDataAddressAddresslist;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    List<AddressInfoModel> list = new ArrayList<>();
    AdressListAdapter adressListAdapter;

    @Override
    public void onDestroy() {
        listviewFrgmentMyDataAddressAddresslist = null;
        addInfoFrgmentMyDataAddressAddresslist = null;
        unbinder = null;
        if (adressListAdapter != null) {
            adressListAdapter.clear();
        }
        adressListAdapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        super.onDestroy();
    }

    public FragmentMyDataAddressList() {
    }

    @Override
    protected int setContentView() {
        return R.layout.frgment_my_data_address_addresslist;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            getUserAdress(1);
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


    /**
     * 请求获取地址列表
     *
     * @param mode 1设为默认与APP正常逻辑调用   2删除后再次调用
     */
    public void getUserAdress(final int mode) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUserAdress(
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
                                    list.clear();
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            AddressInfoModel addressInfoModel = new AddressInfoModel();
                                            addressInfoModel.address_id = jsonArray.getJSONObject(i).getString("address_id");
                                            addressInfoModel.mobile = jsonArray.getJSONObject(i).getString("mobile");
                                            addressInfoModel.province = jsonArray.getJSONObject(i).getString("province");
                                            addressInfoModel.city = jsonArray.getJSONObject(i).getString("city");
                                            addressInfoModel.county = jsonArray.getJSONObject(i).getString("county");
                                            addressInfoModel.address = jsonArray.getJSONObject(i).getString("address");
                                            addressInfoModel.recipients = jsonArray.getJSONObject(i).getString("recipients");
                                            addressInfoModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            addressInfoModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            addressInfoModel.is_default = jsonArray.getJSONObject(i).getString("is_default");
                                            list.add(addressInfoModel);
                                        }
                                        if (getBaseFragmentActivityContext() != null) {
                                            if (adressListAdapter == null) {
                                                adressListAdapter = new AdressListAdapter(list, getBaseFragmentActivityContext(), FragmentMyDataAddressList.this, FragmentMyDataAddressList.this);
                                                listviewFrgmentMyDataAddressAddresslist.setAdapter(adressListAdapter);
                                            } else {
                                                adressListAdapter.refresh(list);
                                            }
                                        }

                                    }
                                    if (list.size() < 1) {
                                        if (mode == 2) {
                                            EventBus.getDefault().post(new AddressDelete());
                                        }

                                    }


                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: user address-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 保存地址信息
     */
    void setDefaultAddress(AddressInfoModel addressInfoModel) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setDefaultAddress(
                    new JSONObject()
                            .put("address_id", addressInfoModel.address_id)
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
                                        EventBus.getDefault().post(new DefaultAddressChanged());
                                        getUserAdress(2);
                                    } else {
                                        if (getBaseFragmentActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: default address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: default address-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 删除地址
     */
    void deleteAddress(AddressInfoModel addressInfoModel) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
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
                                        EventBus.getDefault().post(new DefaultAddressChanged());
                                        getUserAdress(2);
                                    } else {
                                        if (getBaseFragmentActivityContext() != null) {
                                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: delete address-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: delete address-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 图片回调
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    /**
     * 设为默认回调
     *
     * @param addressInfoModel
     */
    @Override
    public void onDefault(AddressInfoModel addressInfoModel) {
        setDefaultAddress(addressInfoModel);

    }

    /**
     * 编辑回调
     *
     * @param addressInfoModel
     */
    @Override
    public void onEdit(AddressInfoModel addressInfoModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataAdressAddAndEditAdressPublic.class)
                    .putExtra("mode", "edit")
                    .putExtra("model", addressInfoModel)
                    .putExtra("from", "FragmentMyDataAddressList"));
        }
    }

    /**
     * 删除回调
     *
     * @param addressInfoModel
     */
    @Override
    public void onDelete(AddressInfoModel addressInfoModel) {
        createDeleteDialog(addressInfoModel);
    }


    /**
     * 创建删除对话框
     */
    void createDeleteDialog(final AddressInfoModel addressInfoModel) {
        if ( getBaseFragmentActivityContext() == null) {
            return;
        }
        String[] stringItems = {"删除"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getBaseFragmentActivityContext(), stringItems, null)
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

    @OnClick(R.id.add_info_frgment_my_data_address_addresslist)
    public void onViewClicked() {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataAdressAddAndEditAdressPublic.class)
                    .putExtra("mode", "add")
                    .putExtra("from", "FragmentMyDataAddressList"));
        }
    }
}
