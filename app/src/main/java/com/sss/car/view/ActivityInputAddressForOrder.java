package com.sss.car.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.dao.LocationStatusListener;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChooseAdress;
import com.sss.car.R;
import com.sss.car.gaode.Geocoding;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.gaode.SSS_GeocodingListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/10/20.
 */

public class ActivityInputAddressForOrder extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.input_activity_input_address_for_order)
    EditText inputActivityInputAddressForOrder;
    @BindView(R.id.activity_input_address_for_order)
    LinearLayout activityInputAddressForOrder;
    Geocoding geocoding;

    YWLoadingDialog ywLoadingDialog;

    LocationConfig locationConfig;
    String cityCode, centent;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        geocoding = null;
        if (locationConfig != null) {
            locationConfig.release();
        }
        locationConfig = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        inputActivityInputAddressForOrder = null;
        activityInputAddressForOrder = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_address_for_order);
        ButterKnife.bind(this);
        titleTop.setText("填写地址");
        rightButtonTop.setText("保存");
        if (geocoding == null) {
            geocoding = new Geocoding();
            geocoding.init(getBaseActivityContext(), new SSS_GeocodingListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    LogUtils.e(i);
                    LogUtils.e(regeocodeResult.getRegeocodeAddress().getCity());
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    if (ywLoadingDialog != null) {
                        ywLoadingDialog.disMiss();
                    }
                    ywLoadingDialog = null;
                    LogUtils.e(i);
                    for (int j = 0; j < geocodeResult.getGeocodeAddressList().size(); j++) {
                        EventBus.getDefault().post(new ChooseAdress(geocodeResult.getGeocodeAddressList().get(j).getCity() + geocodeResult.getGeocodeAddressList().get(j).getFormatAddress(),
                                        String.valueOf(geocodeResult.getGeocodeAddressList().get(j).getLatLonPoint().getLatitude()),
                                        String.valueOf(geocodeResult.getGeocodeAddressList().get(j).getLatLonPoint().getLongitude())
                                )
                        );
                    }
                    finish();

                }
            });
        }

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(inputActivityInputAddressForOrder.getText().toString())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的输入为空");
                } else {
                    centent = inputActivityInputAddressForOrder.getText().toString();
                    if (StringUtils.isEmpty(cityCode)) {
                        location();
                    } else {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ywLoadingDialog = null;
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                        ywLoadingDialog.show();
                        geocoding.query(centent, cityCode);

                    }
                }
                break;
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
                        cityCode = aMapLocation.getCityCode() + "";
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ywLoadingDialog = null;
                        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                        ywLoadingDialog.show();
                        geocoding.query(centent, cityCode);
                    } else {
                        APPOftenUtils.createLocationErrorTip(weakReference, "网络卡顿,定位失败", new LocationStatusListener() {
                            @Override
                            public void onReLocation(Context context) {
                                if (ywLoadingDialog != null) {
                                    ywLoadingDialog.disMiss();
                                }
                                ywLoadingDialog = null;
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
}
