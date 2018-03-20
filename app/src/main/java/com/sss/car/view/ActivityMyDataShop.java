package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.MyDataShopModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.Config.member_id;

/**
 * Created by leilei on 2017/8/21.
 */

public class ActivityMyDataShop extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.name_activity_my_data_shop)
    LinearLayout nameActivityMyDataShop;
    @BindView(R.id.pic_activity_my_data_shop)
    LinearLayout picActivityMyDataShop;
    @BindView(R.id.adress_activity_my_data_shop)
    LinearLayout adressActivityMyDataShop;
    @BindView(R.id.time_activity_my_data_shop)
    LinearLayout timeActivityMyDataShop;
    @BindView(R.id.pro_activity_my_data_shop)
    LinearLayout proActivityMyDataShop;
    @BindView(R.id.activity_my_data_shop)
    LinearLayout activityMyDataShop;
    YWLoadingDialog ywLoadingDialog;
    MyDataShopModel myDataShopModel = new MyDataShopModel();
    @BindView(R.id.logo_activity_my_data_shop)
    LinearLayout logoActivityMyDataShop;
    @BindView(R.id.show_name_activity_my_data_shop)
    TextView showNameActivityMyDataShop;
    @BindView(R.id.show_adress_activity_my_data_shop)
    TextView showAdressActivityMyDataShop;
    @BindView(R.id.show_time_activity_my_data_shop)
    TextView showTimeActivityMyDataShop;
    @BindView(R.id.show_pro_activity_my_data_shop)
    TextView showProActivityMyDataShop;
    @BindView(R.id.click_kefu)
    LinearLayout clickKeFu;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        myDataShopModel = null;
        nameActivityMyDataShop = null;
        picActivityMyDataShop = null;
        adressActivityMyDataShop = null;
        timeActivityMyDataShop = null;
        proActivityMyDataShop = null;
        activityMyDataShop = null;
        showProActivityMyDataShop = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_shop);
        ButterKnife.bind(this);
        customInit(activityMyDataShop, false, true, true);
        getshopInfo();
        titleTop.setText("商铺资料");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "shopName":
                try {
                    setshopInfo("设置商铺名称", new JSONObject().put("name", event.msg));
                    showNameActivityMyDataShop.setText(event.msg);
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                    e.printStackTrace();
                }
                break;
            case "shopAdress":
                try {
                    setshopInfo("设置商铺地址", new JSONObject()
                            .put("lat", event.lat)
                            .put("lng", event.lng)
                            .put("address", event.msg));
                    showAdressActivityMyDataShop.setText(event.msg);
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                    e.printStackTrace();
                }
                break;
            case "shopPro":
                try {
                    setshopInfo("设置商铺简介", new JSONObject()
                            .put("describe", event.msg));
//                    showProActivityMyDataShop.setText(event.msg);
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                    e.printStackTrace();
                }
                break;
            case "shopTime":
                try {
                    setshopInfo("设置商铺营业时间", new JSONObject()
                            .put("business_hours", event.msg));
                    showTimeActivityMyDataShop.setText(event.msg);
                    String[] a = event.msg.split("-");
                    if (a.length == 2) {
                        showTimeActivityMyDataShop.setText(a[0] + "-" + a[1]);
                    }
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                    e.printStackTrace();
                }
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.name_activity_my_data_shop,R.id.click_kefu, R.id.logo_activity_my_data_shop, R.id.pic_activity_my_data_shop, R.id.adress_activity_my_data_shop, R.id.time_activity_my_data_shop, R.id.pro_activity_my_data_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.click_kefu:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),ActivityShareInteractionManageSettingPeopleManage.class)
                            .putExtra("title","商铺客服设置")
                            .putExtra("type","5"));
                }
                break;
            case R.id.name_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(),
                            ActivityChangeInfo.class).putExtra("type", "shopName")
                            .putExtra("canChange", true)
                            .putExtra("extra", myDataShopModel.name));
                }
                break;
            case R.id.pic_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopPics.class));
                }
                break;
            case R.id.adress_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfoShopAddress.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "shopAdress").putExtra("extra", myDataShopModel.address));
                }
                break;
            case R.id.time_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataShopChooseTime.class)
                            .putExtra("type", "shopTime").putExtra("extra", myDataShopModel.business_hours));
                }
                break;
            case R.id.pro_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopIntroduce.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "shopPro").putExtra("extra", myDataShopModel.describe));
                }
                break;
            case R.id.logo_activity_my_data_shop:
                if (StringUtils.isEmpty(myDataShopModel.shop_id)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的账户尚未通过认证");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopPic.class)
                            .putExtra("canChange", true)
                            .putExtra("type", "shopPro").putExtra("extra", myDataShopModel.describe));
                }
                break;
        }
    }


    /**
     * 获取商铺信息
     */
    void getshopInfo() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getShop(
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
                                        myDataShopModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                        myDataShopModel.picture = jsonObject.getJSONObject("data").getString("picture");
                                        myDataShopModel.name = jsonObject.getJSONObject("data").getString("name");
                                        myDataShopModel.logo = jsonObject.getJSONObject("data").getString("logo");
                                        myDataShopModel.address = jsonObject.getJSONObject("data").getString("address");
                                        myDataShopModel.business_hours = jsonObject.getJSONObject("data").getString("business_hours");
                                        myDataShopModel.describe = jsonObject.getJSONObject("data").getString("describe");
                                        myDataShopModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                        showNameActivityMyDataShop.setText(myDataShopModel.name);
                                        showAdressActivityMyDataShop.setText(myDataShopModel.address);
                                        String[] a = myDataShopModel.business_hours.split("-");
                                        if (a.length == 2) {
                                            showTimeActivityMyDataShop.setText(a[0] + "-" + a[1]);
                                        }

                                        if (Config.member_id.equals(myDataShopModel.member_id)){
                                            clickKeFu.setVisibility(View.VISIBLE);
                                        }else {
                                            clickKeFu.setVisibility(View.GONE);
                                        }

                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
            }
            e.printStackTrace();
        }
    }

    /*
    * 设置商铺信息
    */
    void setshopInfo(String meaning, JSONObject jsonObject) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setShop(
                    jsonObject
                            .put("member_id", Config.member_id)
                            .put("shop_id", myDataShopModel.shop_id)
                            .toString(), meaning, new StringCallback() {
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                    getshopInfo();
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
            }
            e.printStackTrace();
        }
    }

}
