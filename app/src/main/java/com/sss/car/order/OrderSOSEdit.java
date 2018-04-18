package com.sss.car.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.dao.LocationStatusListener;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.model.TargetInfoModel;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CarDelete;
import com.sss.car.EventBusModel.CarModel;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChooseAdress;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.gaode.Geocoding;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.model.CouponModel3;
import com.sss.car.model.IntegrityMoneyModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityChangeInfo;
import com.sss.car.view.ActivityChooseAdress;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityInputAddressForOrder;
import com.sss.car.view.ActivityMyDataCarGarage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;


/**
 * 草稿箱==>sos订单详情
 * Created by leilei on 2017/10/19.
 */

@SuppressWarnings("ALL")
public class OrderSOSEdit extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.people_name_order_goods_ready_buy_edit)
    TextView peopleNameOrderSOSEdit;
    @BindView(R.id.mobile_name_order_goods_ready_buy_edit)
    TextView mobileNameOrderSOSEdit;
    @BindView(R.id.car_order_goods_ready_buy_edit)
    TextView carOrderSOSEdit;
    @BindView(R.id.click_choose_car_order_goods_ready_buy_edit)
    LinearLayout clickChooseCarOrderSOSEdit;
    @BindView(R.id.show_type_order_goods_ready_buy_edit)
    TextView showTypeOrderSOSEdit;
    @BindView(R.id.click_type_order_goods_ready_buy_edit)
    LinearLayout clickTypeOrderSOSEdit;
    @BindView(R.id.show_address_order_goods_ready_buy_edit)
    TextView showAddressOrderSOSEdit;
    @BindView(R.id.click_address_order_goods_ready_buy_edit)
    LinearLayout clickAddressOrderSOSEdit;
    @BindView(R.id.price_order_goods_ready_buy_edit)
    NumberSelectEdit priceOrderSOSEdit;
    @BindView(R.id.show_time_order_goods_ready_buy_edit)
    TextView showTimeOrderSOSEdit;
    @BindView(R.id.click_time_order_goods_ready_buy_edit)
    LinearLayout clickTimeOrderSOSEdit;
    @BindView(R.id.show_penal_sum_order_goods_ready_buy_edit)
    TextView showPenalSumOrderSOSEdit;
    @BindView(R.id.click_penal_sum_order_goods_ready_buy_edit)
    LinearLayout clickPenalSumOrderSOSEdit;
    @BindView(R.id.show_other_order_goods_ready_buy_edit)
    TextView showOtherOrderSOSEdit;
    @BindView(R.id.click_other_sum_order_goods_ready_buy_edit)
    LinearLayout clickOtherSumOrderSOSEdit;
    @BindView(R.id.photo)
    HorizontalListView photo;
    @BindView(R.id.tip_order_goods_ready_buy_edit)
    TextView tipOrderSOSEdit;
    @BindView(R.id.total_price_order_goods_ready_buy_edit)
    TextView totalPriceOrderSOSEdit;
    @BindView(R.id.click_submit_order_goods_ready_buy_edit)
    TextView clickSubmitOrderSOSEdit;
    @BindView(R.id.order_goods_ready_buy_edit)
    LinearLayout OrderSOSEdit;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.click_one_key_write_car_order_goods_ready_buy_edit)
    TextView clickOneKeyWriteCarOrderSOSEdit;
    @BindView(R.id.click_one_key_location_order_goods_ready_buy_edit)
    TextView clickOneKeyLocationOrderSOSEdit;
    @BindView(R.id.click_one_key_write_address_order_goods_ready_buy_edit)
    TextView clickOneKeyWriteAddressOrderSOSEdit;
    @BindView(R.id.no_exist_order_goods_ready_buy_edit)
    FrameLayout noExistOrderSOSEdit;
    @BindView(R.id.is_exist_order_goods_ready_buy_edit)
    LinearLayout isExistOrderSOSEdit;

    SSS_Adapter integrityMoneyAdapter;
    List<IntegrityMoneyModel> integrityMoneyList = new ArrayList<>();

    SSS_Adapter serviceTimeAdapter;
    List<IntegrityMoneyModel> serviceTimeList = new ArrayList<>();

    SSS_Adapter serviceTypeAdapter;
    List<IntegrityMoneyModel> serviceTypeList = new ArrayList<>();

    List<CouponModel3> list = new ArrayList<>();

    SSS_Adapter sss_adapter;
    List<String> listPhoto = new ArrayList<>();

    String sendLai, sengLng;


    MenuDialog menuDialog;

    Geocoding geocoding;
    LocationConfig locationConfig;
    @BindView(R.id.input)
    EditText input;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (locationConfig != null) {
            locationConfig.stop();
            locationConfig.release();
        }
        locationConfig = null;
        geocoding = null;
        backTop = null;
        titleTop = null;
        noExistOrderSOSEdit = null;
        rightButtonTop = null;
        isExistOrderSOSEdit = null;
        clickOneKeyWriteCarOrderSOSEdit = null;
        clickOneKeyLocationOrderSOSEdit = null;
        clickOneKeyWriteAddressOrderSOSEdit = null;
        peopleNameOrderSOSEdit = null;
        mobileNameOrderSOSEdit = null;
        carOrderSOSEdit = null;
        clickChooseCarOrderSOSEdit = null;
        showTypeOrderSOSEdit = null;
        clickTypeOrderSOSEdit = null;
        showAddressOrderSOSEdit = null;
        clickAddressOrderSOSEdit = null;
        priceOrderSOSEdit = null;
        showTimeOrderSOSEdit = null;
        clickTimeOrderSOSEdit = null;
        showPenalSumOrderSOSEdit = null;
        clickPenalSumOrderSOSEdit = null;
        showOtherOrderSOSEdit = null;
        clickOtherSumOrderSOSEdit = null;
        photo = null;
        tipOrderSOSEdit = null;
        totalPriceOrderSOSEdit = null;
        clickSubmitOrderSOSEdit = null;
        OrderSOSEdit = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_edit);
        ButterKnife.bind(this);
        customInit(OrderSOSEdit, false, true, true);
        titleTop.setText("SOS订单填写");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        APPOftenUtils.underLineOfTextView(rightButtonTop).setText("保存");
        APPOftenUtils.underLineOfTextView(clickOneKeyWriteCarOrderSOSEdit).setText("手工填写");
        APPOftenUtils.underLineOfTextView(clickOneKeyLocationOrderSOSEdit).setText("一键定位");
        APPOftenUtils.underLineOfTextView(clickOneKeyWriteAddressOrderSOSEdit).setText("手工填写");
        menuDialog = new MenuDialog(this);
        priceOrderSOSEdit.init(getBaseActivityContext(), true)
                .minNumber(0)
                .isNegativeNumber(false)
                .maxWidth(50)
                .defaultNumber(0)
                .setNumberSelectEditOperationCakkBack(new NumberSelectEdit.NumberSelectEditOperationCakkBack() {
                    @Override
                    public void onAdd(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPriceOrderSOSEdit.setText(currentNumber + ".00");
                    }

                    @Override
                    public void onSubtract(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPriceOrderSOSEdit.setText(currentNumber + ".00");
                    }

                    @Override
                    public void onZero(NumberSelectEdit numberSelectEdit) {
                        totalPriceOrderSOSEdit.setText("0.00");
                    }

                    @Override
                    public void onEditChanged(NumberSelectEdit numberSelectEdit, int currentNumber) {
                        totalPriceOrderSOSEdit.setText(currentNumber + ".00");
                    }
                });

        initAdapter();
        initPhotoAdapter();
//        encodingToString();
//        defaultVehicle();
        integrityMoney();
        serviceTime();
        serviceType();
        orderTip();
        get_drafts_SOS();
        sendLai = Config.latitude;
        sengLng = Config.longitude;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarModel event) {
        carOrderSOSEdit.setText(event.name);
        noExistOrderSOSEdit.setVisibility(View.GONE);
        peopleNameOrderSOSEdit.setText(Config.username);
        mobileNameOrderSOSEdit.setText(Config.mobile);
        isExistOrderSOSEdit.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
//        carOrderSOSEdit.setText(event.carName);
        defaultVehicle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarDelete event) {
        defaultVehicle();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        if ("other".equals(event.type)) {
            showOtherOrderSOSEdit.setText(event.msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseAdress event) {
        sendLai = event.lai;
        sengLng = event.lng;
        showAddressOrderSOSEdit.setText(event.province + event.city + event.district + event.adress);
    }

    @OnClick({R.id.back_top, R.id.no_exist_order_goods_ready_buy_edit, R.id.right_button_top, R.id.click_one_key_write_car_order_goods_ready_buy_edit, R.id.click_one_key_location_order_goods_ready_buy_edit, R.id.click_one_key_write_address_order_goods_ready_buy_edit, R.id.click_choose_car_order_goods_ready_buy_edit, R.id.click_type_order_goods_ready_buy_edit, R.id.click_address_order_goods_ready_buy_edit, R.id.click_time_order_goods_ready_buy_edit, R.id.click_penal_sum_order_goods_ready_buy_edit, R.id.click_other_sum_order_goods_ready_buy_edit, R.id.click_submit_order_goods_ready_buy_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top://保存
                saveSOS();
                break;
            case R.id.click_choose_car_order_goods_ready_buy_edit://选择车辆
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class)
                            .putExtra("where", "fromSOS"));
                }
                break;
            case R.id.click_type_order_goods_ready_buy_edit://服务类型
                serviceType();
                break;
            case R.id.click_address_order_goods_ready_buy_edit://点击地址
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChooseAdress.class));
                }
                break;
            case R.id.click_time_order_goods_ready_buy_edit://服务就位时间
                serviceTime();
                break;
            case R.id.click_penal_sum_order_goods_ready_buy_edit://违约金
                integrityMoney();
                break;
            case R.id.click_other_sum_order_goods_ready_buy_edit://其他要求
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "other")
                            .putExtra("canChange", true)
                            .putExtra("extra", ""));
                }
                break;
            case R.id.click_submit_order_goods_ready_buy_edit://提交订单
                publicSOS("1");//0草稿箱，1立即求助（求助中），2进行中，3已完成
                break;
            case R.id.click_one_key_write_car_order_goods_ready_buy_edit://手工填写车辆信息
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class)
                            .putExtra("where", "fromSOS"));
                }
                break;
            case R.id.click_one_key_location_order_goods_ready_buy_edit://一键定位
                encodingToString();
                break;
            case R.id.click_one_key_write_address_order_goods_ready_buy_edit://手工填写定位地址
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityInputAddressForOrder.class)
                    );
                }
                break;

            case R.id.no_exist_order_goods_ready_buy_edit:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarGarage.class)
                            .putExtra("where", "fromSOS"));
                }
                break;
        }
    }


    /**
     * 获取订单信息
     */
    public void get_drafts_SOS() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_drafts_SOS(
                    new JSONObject()
                            .put("sos_id", getIntent().getExtras().getString("sos_id"))
                            .toString()
                    , new StringCallback() {
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

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    peopleNameOrderSOSEdit.setText(jsonObject1.getString("recipients"));
                                    mobileNameOrderSOSEdit.setText(jsonObject1.getString("mobile"));
                                    carOrderSOSEdit.setText(jsonObject1.getString("vehicle_name"));
                                    showPenalSumOrderSOSEdit.setText(jsonObject1.getString("damages"));
                                    showTypeOrderSOSEdit.setText(jsonObject1.getString("type"));
                                    showAddressOrderSOSEdit.setText(jsonObject1.getString("address"));
                                    totalPriceOrderSOSEdit.setText(jsonObject1.getString("price"));
                                    showTimeOrderSOSEdit.setText(jsonObject1.getString("service_time"));
                                    showOtherOrderSOSEdit.setText(jsonObject1.getString("remark"));
                                    input.setText(jsonObject1.getString("title"));
                                    String a = jsonObject1.getString("price");
                                    if (!StringUtils.isEmpty(carOrderSOSEdit.getText().toString().trim())) {
                                        noExistOrderSOSEdit.setVisibility(View.GONE);
                                        isExistOrderSOSEdit.setVisibility(View.VISIBLE);
                                    }
                                    if (!StringUtils.isEmpty(a)) {
                                        priceOrderSOSEdit.setCurrentNumber((int) Double.parseDouble(a));
                                    }

                                    JSONArray jsonArray = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        listPhoto.add(jsonArray.getString(i));
                                    }
                                    sss_adapter.setList(listPhoto);


                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * gps转为String
     */
    void encodingToString() {
        if (StringUtils.isEmpty(sendLai) || StringUtils.isEmpty(sengLng)) {
            location();
        } else {
            if (geocoding == null) {
                geocoding = new Geocoding();
            }
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            sendLai = Config.latitude;
            sengLng = Config.longitude;
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
            doSearchQuery("", Double.valueOf(Config.latitude), Double.valueOf(Config.longitude));
        }
    }

    /**
     * 定位
     */
    void location() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        if (locationConfig == null) {
            locationConfig = new LocationConfig(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }
                    });
                    LogUtils.e(aMapLocation.getErrorCode());

                    /**
                     * 初始化定位监听
                     *
                     * @param activity
                     * @param mainBiz  错误代码:
                     * 0定位成功。
                     * 可以在定位回调里判断定位返回成功后再进行业务逻辑运算。
                     * 1一些重要参数为空，如context；
                     * 请对定位传递的参数进行非空判断。
                     * 2定位失败，由于仅扫描到单个wifi，且没有基站信息。
                     * 请重新尝试。
                     * 3获取到的请求参数为空，可能获取过程中出现异常。
                     * 请对所连接网络进行全面检查，请求可能被篡改。
                     * 4请求服务器过程中的异常，多为网络情况差，链路不通导致
                     * 请检查设备网络是否通畅，检查通过接口设置的网络访问超时时间，建议采用默认的30秒。
                     * 5请求被恶意劫持，定位结果解析失败。
                     * 您可以稍后再试，或检查网络链路是否存在异常。
                     * 6定位服务返回定位失败。
                     * 请获取errorDetail（通过getLocationDetail()方法获取）信息并参考定位常见问题进行解决。
                     * 7KEY鉴权失败。
                     * 请仔细检查key绑定的sha1值与apk签名sha1值是否对应，或通过高频问题查找相关解决办法。
                     * 8Android exception常规错误
                     * 请将errordetail（通过getLocationDetail()方法获取）信息通过工单系统反馈给我们。
                     * 9定位初始化时出现异常。
                     * 请重新启动定位。
                     * 10定位客户端启动失败。
                     * 请检查AndroidManifest.xml文件是否配置了APSService定位服务
                     * 11定位时的基站信息错误。
                     * 请检查是否安装SIM卡，设备很有可能连入了伪基站网络。
                     * 12缺少定位权限。
                     * 请在设备的设置中开启app的定位权限。
                     * 13定位失败，由于设备未开启WIFI模块或未插入SIM卡，且GPS当前不可用。
                     * 建议开启设备的WIFI模块，并将设备中插入一张可以正常工作的SIM卡，或者检查GPS是否开启；如果以上都内容都确认无误，请您检查App是否被授予定位权限。
                     * 14GPS 定位失败，由于设备当前 GPS 状态差。
                     * 建议持设备到相对开阔的露天场所再次尝试。
                     * 15定位结果被模拟导致定位失败
                     * 如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
                     * 16当前POI检索条件、行政区划检索条件下，无可用地理围栏
                     * 建议调整检索条件后重新尝试，例如调整POI关键字，调整POI类型，调整周边搜区域，调整行政区关键字等。
                     */
                    if (aMapLocation.getErrorCode() == 0) {
                        sendLai = aMapLocation.getLatitude() + "";
                        sengLng = aMapLocation.getLongitude() + "";
                        encodingToString();
                    } else {
                        APPOftenUtils.createLocationErrorTip(weakReference, "网络卡顿,定位失败", new LocationStatusListener() {
                            @Override
                            public void onReLocation(Context context) {
                                if (ywLoadingDialog != null) {
                                    ywLoadingDialog.disMiss();
                                }
                                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                                ywLoadingDialog.show();
                                locationConfig.start();
                            }
                        });
                    }

                }
            }, getBaseActivityContext(), LocationConfig.LocationType_Single_Positioning);
        }

        locationConfig.start();
    }

    /**
     * 开始进行poi搜索
     *
     * @param city
     * @param latitude
     * @param longitude
     */
    void doSearchQuery(String city, double latitude, double longitude) {
        String mType = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
        PoiSearch.Query query = new PoiSearch.Query("", mType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(getBaseActivityContext(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                if (ywLoadingDialog != null) {
                    ywLoadingDialog.disMiss();
                }
                if (rCode == 1000) {
                    for (int j = 0; j < poiResult.getPois().size(); j++) {
                        TargetInfoModel targetInfoModel = new TargetInfoModel();
                        targetInfoModel.provinceName = poiResult.getPois().get(j).getProvinceName();
                        targetInfoModel.provinceCode = poiResult.getPois().get(j).getProvinceCode();
                        targetInfoModel.distance = poiResult.getPois().get(j).getDistance();
                        targetInfoModel.cityName = poiResult.getPois().get(j).getCityName();
                        targetInfoModel.cityCode = poiResult.getPois().get(j).getCityCode();
                        targetInfoModel.typeDes = poiResult.getPois().get(j).getTypeDes();
                        targetInfoModel.typeCode = poiResult.getPois().get(j).getTypeCode();
                        targetInfoModel.parkingType = poiResult.getPois().get(j).getParkingType();
                        targetInfoModel.businessArea = poiResult.getPois().get(j).getBusinessArea();
                        targetInfoModel.email = poiResult.getPois().get(j).getEmail();
                        targetInfoModel.enter = poiResult.getPois().get(j).getEnter();
                        targetInfoModel.exit = poiResult.getPois().get(j).getExit();
                        targetInfoModel.indoorData = poiResult.getPois().get(j).getIndoorData();
                        targetInfoModel.latLonPoint = poiResult.getPois().get(j).getLatLonPoint();
                        targetInfoModel.photo = poiResult.getPois().get(j).getPhotos();
                        targetInfoModel.poiExtension = poiResult.getPois().get(j).getPoiExtension();
                        targetInfoModel.postCode = poiResult.getPois().get(j).getPostcode();
                        targetInfoModel.subPois = poiResult.getPois().get(j).getSubPois();
                        targetInfoModel.shopId = poiResult.getPois().get(j).getShopID();
                        targetInfoModel.snippet = poiResult.getPois().get(j).getSnippet();
                        targetInfoModel.tel = poiResult.getPois().get(j).getTel();
                        targetInfoModel.title = poiResult.getPois().get(j).getTitle();
                        targetInfoModel.website = poiResult.getPois().get(j).getWebsite();
                        LogUtils.e(targetInfoModel.toString());
                        showAddressOrderSOSEdit.setText(poiResult.getPois().get(j).getCityName() + poiResult.getPois().get(j).getTitle());
                    }
                } else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "网络卡顿,定位出错err:" + rCode);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        //以当前定位的经纬度为准搜索周围5000米范围
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000, true));//
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    void initAdapter() {
        listPhoto.add("default");

        integrityMoneyAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean, SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        integrityMoneyAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < integrityMoneyList.size(); i++) {

                            if (i == position) {
                                integrityMoneyList.get(i).is_check = "1";
                                showPenalSumOrderSOSEdit.setText(integrityMoneyList.get(i).name);
                            } else {
                                integrityMoneyList.get(i).is_check = "0";
                            }
                        }
                        integrityMoneyAdapter.setList(integrityMoneyList);
                        break;
                }
            }
        });
        /*******************************************************************************************************/
        serviceTimeAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean, SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        serviceTimeAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < serviceTimeList.size(); i++) {

                            if (i == position) {
                                serviceTimeList.get(i).is_check = "1";
                                showTimeOrderSOSEdit.setText(serviceTimeList.get(i).name);
                            } else {
                                serviceTimeList.get(i).is_check = "0";
                            }
                        }
                        serviceTimeAdapter.setList(serviceTimeList);
                        break;
                }
            }
        });
        /*******************************************************************************************************/
        serviceTypeAdapter = new SSS_Adapter<IntegrityMoneyModel>(getBaseActivityContext(), R.layout.item_dialog_coupons) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, IntegrityMoneyModel bean, SSS_Adapter instance) {

                helper.setText(R.id.name_item_dialog_coupons, bean.name);
                if ("1".equals(bean.is_check)) {
                    helper.setChecked(R.id.cb_item_dialog_coupons, true);
                } else {
                    helper.setChecked(R.id.cb_item_dialog_coupons, false);
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dialog_coupons);
            }
        };

        serviceTypeAdapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dialog_coupons:
                        for (int i = 0; i < serviceTypeList.size(); i++) {

                            if (i == position) {
                                serviceTypeList.get(i).is_check = "1";
                                showTypeOrderSOSEdit.setText(serviceTypeList.get(i).name);
                            } else {
                                serviceTypeList.get(i).is_check = "0";
                            }
                        }
                        serviceTypeAdapter.setList(serviceTypeList);
                        break;
                }
            }
        });
    }


    void initPhotoAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_image, listPhoto) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {
                if ("default".equals(bean)) {
                    FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + getBaseActivityContext().getPackageName() + "/" + R.mipmap.logo_add_image), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                } else {
                    if (bean.startsWith("/storage/")) {
                        FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                    }
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("default".equals(listPhoto.get(position))) {
                    APPOftenUtils.createPhotoChooseDialog(0, 9, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                        @Override
                        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                            if (resultList == null || resultList.size() == 0) {
                                return;
                            }
                            for (int i = 0; i < resultList.size(); i++) {
                                listPhoto.add(resultList.get(i).getPhotoPath());
                                sss_adapter.setList(listPhoto);
                            }
                        }

                        @Override
                        public void onHanlderFailure(int requestCode, String errorMsg) {

                        }
                    });
                } else {
                    if (getBaseActivityContext() != null) {
                        List<String> temp = new ArrayList<>();
                        for (int j = 0; j < listPhoto.size(); j++) {
                            if (!"default".equals(listPhoto.get(j))) {
                                if (listPhoto.get(j).startsWith("/storage/")) {
                                    temp.add(listPhoto.get(j));
                                } else {
                                    temp.add(Config.url + listPhoto.get(j));
                                }
                            }
                        }
                        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                .putExtra("current", position - 1));
                    }

                }
            }
        });
        photo.setAdapter(sss_adapter);

    }

    /**
     * 获取用户默认车辆
     */
    public void defaultVehicle() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.defaultVehicle(
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
                                    peopleNameOrderSOSEdit.setText(jsonObject.getJSONObject("data").getString("username"));
                                    mobileNameOrderSOSEdit.setText(jsonObject.getJSONObject("data").getString("mobile"));
//                                    carOrderSOSEdit.setText(jsonObject.getJSONObject("data").getString("vehicle_name"));
                                } else {
                                    peopleNameOrderSOSEdit.setText("");
                                    mobileNameOrderSOSEdit.setText("");
                                    carOrderSOSEdit.setText("");
                                    isExistOrderSOSEdit.setVisibility(View.GONE);
                                    noExistOrderSOSEdit.setVisibility(View.VISIBLE);
                                }

                                if (!StringUtils.isEmpty(carOrderSOSEdit.getText().toString().trim())) {
                                    isExistOrderSOSEdit.setVisibility(View.VISIBLE);
                                    noExistOrderSOSEdit.setVisibility(View.GONE);
                                } else {
                                    isExistOrderSOSEdit.setVisibility(View.GONE);
                                    noExistOrderSOSEdit.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:SOS-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:SOS-0");
            e.printStackTrace();
        }
    }


    /**
     * 获取订单提示
     */
    public void orderTip() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderTip(
                    new JSONObject()
                            .put("article_id", "9")//文章ID (3实物类)
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
                                    tipOrderSOSEdit.setText(jsonObject.getJSONObject("data").getString("contents"));
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

    /**
     * 获取违约金比例
     */
    public void integrityMoney() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (integrityMoneyList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "2")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToIntegrityMoneyList(jsonObject);
                                        if (list.size() > 0) {
                                            integrityMoneyAdapter.setList(integrityMoneyList);
                                            menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
                                        }
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            integrityMoneyAdapter.setList(integrityMoneyList);
            menuDialog.createIntegrityBottomDialog("违约金比例", getBaseActivityContext(), integrityMoneyAdapter);
        }

    }

    void parseToIntegrityMoneyList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            integrityMoneyList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showPenalSumOrderSOSEdit.setText(integrityMoneyModel.name);
                }
                integrityMoneyList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 获取就位时间
     */
    public void serviceTime() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (serviceTimeList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "4")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToServiceTimeList(jsonObject);
                                        if (list.size() > 0) {
                                            serviceTimeAdapter.setList(serviceTimeList);
                                            menuDialog.createIntegrityBottomDialog("就位时间", getBaseActivityContext(), serviceTimeAdapter);
                                        }
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            serviceTimeAdapter.setList(serviceTimeList);
            menuDialog.createIntegrityBottomDialog("就位时间", getBaseActivityContext(), serviceTimeAdapter);
        }

    }

    void parseToServiceTimeList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            serviceTimeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showTimeOrderSOSEdit.setText(integrityMoneyModel.name);
                }
                serviceTimeList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 获取求助类型
     */
    public void serviceType() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        if (serviceTypeList.size() == 0) {
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderAttr(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("type", "3")//1送达时效，2违约金比例，3求助类型，4服务就位
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
                                        parseToServiceTypeList(jsonObject);
                                        if (list.size() > 0) {
                                            serviceTypeAdapter.setList(serviceTypeList);
                                            menuDialog.createIntegrityBottomDialog("求助类型", getBaseActivityContext(), serviceTypeAdapter);
                                        }
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
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            serviceTypeAdapter.setList(serviceTypeList);
            menuDialog.createIntegrityBottomDialog("求助类型", getBaseActivityContext(), serviceTypeAdapter);
        }

    }

    void parseToServiceTypeList(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray.length() > 0) {
            serviceTypeList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                IntegrityMoneyModel integrityMoneyModel = new IntegrityMoneyModel();
                integrityMoneyModel.attr_id = jsonArray.getJSONObject(i).getString("attr_id");
                integrityMoneyModel.is_check = jsonArray.getJSONObject(i).getString("is_check");
                integrityMoneyModel.name = jsonArray.getJSONObject(i).getString("name");
                if ("1".equals(integrityMoneyModel.is_check)) {
                    showTypeOrderSOSEdit.setText(integrityMoneyModel.name);
                }
                serviceTypeList.add(integrityMoneyModel);
            }

        }
    }

    /**
     * 发布订单
     *
     * @param status 0草稿箱，1立即求助（求助中），2进行中，3已完成
     */
    public void publicSOS(String status) {
        if (StringUtils.isEmpty(carOrderSOSEdit.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请添加您的车辆");
            return;
        }
        if (StringUtils.isEmpty(showTypeOrderSOSEdit.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写您的求助类型");
            return;
        }

        if (StringUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请填写您的求助故障");
            return;
        }
        if (priceOrderSOSEdit.getCurrentNumber() < 1) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择您的服务价格");
            return;
        }

        if (StringUtils.isEmpty(sendLai) || StringUtils.isEmpty(sengLng) || StringUtils.isEmpty(showAddressOrderSOSEdit.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择您的地址");
            return;
        }
        if (StringUtils.isEmpty(showTimeOrderSOSEdit.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择服务就位时间");
            return;
        }
        if (StringUtils.isEmpty(showPenalSumOrderSOSEdit.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), " 请选择违约金比例");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        JSONArray picture = new JSONArray();
        for (int i = 0; i < listPhoto.size(); i++) {
            if (!"default".equals(listPhoto.get(i))) {
                if (!listPhoto.get(i).startsWith("/public/")) {
                    picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(listPhoto.get(i),
                            getWindowManager().getDefaultDisplay().getWidth(),
                            getWindowManager().getDefaultDisplay().getHeight())));
                } else {
                    picture.put(listPhoto.get(i));
                }
            }
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.publisSOS(
                    new JSONObject()
                            .put("status", status)//0草稿箱，1立即求助（求助中），2进行中，3已完成
                            .put("mobile", mobileNameOrderSOSEdit.getText().toString().trim())
                            .put("recipients", peopleNameOrderSOSEdit.getText().toString().trim())
                            .put("vehicle_name", carOrderSOSEdit.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("title", input.getText().toString().trim())
                            .put("type", showTypeOrderSOSEdit.getText().toString().trim())
                            .put("gps", sendLai + "," + sengLng)
                            .put("price", priceOrderSOSEdit.getCurrentNumber())
                            .put("service_time", showPenalSumOrderSOSEdit.getText().toString().trim())
                            .put("damages", showPenalSumOrderSOSEdit.getText().toString().trim())
                            .put("address", showAddressOrderSOSEdit.getText().toString().trim())
                            .put("remark", showOtherOrderSOSEdit.getText().toString().trim())
                            .put("picture", picture)
                            .toString()
                    , new StringCallback() {
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), "发布成功");
                                    if (getBaseActivityContext() != null) {
                                        startActivity(new Intent(getBaseActivityContext(), OrderSOSGrabList.class)
                                                .putExtra("sos_id", jsonObject.getJSONObject("data").getString("sos_id")));
                                    }
                                    finish();
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

    /**
     * 保存订单
     */
    public void saveSOS() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        JSONArray picture = new JSONArray();
        for (int i = 0; i < listPhoto.size(); i++) {

            if (!listPhoto.get(i).startsWith("/public/")) {
                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(listPhoto.get(i),
                        getWindowManager().getDefaultDisplay().getWidth(),
                        getWindowManager().getDefaultDisplay().getHeight())));
            } else {
                picture.put(listPhoto.get(i));
            }

        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.saveSOS(
                    new JSONObject()
                            .put("sos_id", getIntent().getExtras().getString("sos_id"))
                            .put("status", "0")//0草稿箱，1立即求助（求助中），2进行中，3已完成
                            .put("mobile", mobileNameOrderSOSEdit.getText().toString().trim())
                            .put("recipients", peopleNameOrderSOSEdit.getText().toString().trim())
                            .put("vehicle_name", carOrderSOSEdit.getText().toString().trim())
                            .put("member_id", Config.member_id)
                            .put("title", input.getText().toString().trim())
                            .put("type", showTypeOrderSOSEdit.getText().toString().trim())
                            .put("gps", sendLai + "," + sengLng)
                            .put("price", priceOrderSOSEdit.getCurrentNumber())
                            .put("service_time", showPenalSumOrderSOSEdit.getText().toString().trim())
                            .put("damages", showPenalSumOrderSOSEdit.getText().toString().trim())
                            .put("address", showAddressOrderSOSEdit.getText().toString().trim())
                            .put("remark", showOtherOrderSOSEdit.getText().toString().trim())
                            .put("picture", picture)

                            .toString()
                    , new StringCallback() {
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
                                    ToastUtils.showShortToast(getBaseActivityContext(), "保存成功");
                                    finish();
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
