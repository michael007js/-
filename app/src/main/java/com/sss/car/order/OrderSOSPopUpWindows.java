package com.sss.car.order;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.EditText.NumberSelectEdit;
import com.blankj.utilcode.customwidget.JingDongCountDownView.MainDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.model.PushSOSHelperFromBuyerModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amap.api.mapcore2d.q.t;

/**
 * 当前用户身份为商家时如果接到服务器的SOS订单推送则弹出本页面(dialog形式)
 * Created by leilei on 2017/10/20.
 */

public class OrderSOSPopUpWindows extends BaseActivity {
    @BindView(R.id.helper_order_sos_pop_up_windows)
    TextView helperOrderSosPopUpWindows;
    @BindView(R.id.score_order_sos_pop_up_windows)
    TextView scoreOrderSosPopUpWindows;
    @BindView(R.id.distance_order_sos_pop_up_windows)
    TextView distanceOrderSosPopUpWindows;
    @BindView(R.id.car_order_sos_pop_up_windows)
    TextView carOrderSosPopUpWindows;
    @BindView(R.id.type_order_sos_pop_up_windows)
    TextView typeOrderSosPopUpWindows;
    @BindView(R.id.price_order_sos_pop_up_windows)
    NumberSelectEdit priceOrderSosPopUpWindows;
    @BindView(R.id.penal_sum_sos_pop_up_windows)
    TextView penalSumSosPopUpWindows;
    @BindView(R.id.count_down_sos_pop_up_windows)
    SecondDownTimerView countDownSosPopUpWindows;
    @BindView(R.id.click_cancel_order_sos_pop_up_windows)
    TextView clickCancelOrderSosPopUpWindows;
    @BindView(R.id.click_get_order_sos_pop_up_windows)
    TextView clickGetOrderSosPopUpWindows;
    @BindView(R.id.order_sos_pop_up_windows)
    LinearLayout orderSosPopUpWindows;
    PushSOSHelperFromBuyerModel pushSOSHelperFromBuyerModel;
    LocationConfig locationConfig;

    boolean can = true;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (countDownSosPopUpWindows != null) {
            countDownSosPopUpWindows.cancelDownTimer();
        }
        countDownSosPopUpWindows = null;
        if (locationConfig != null) {
            locationConfig.release();
        }
        locationConfig = null;
        helperOrderSosPopUpWindows = null;
        scoreOrderSosPopUpWindows = null;
        distanceOrderSosPopUpWindows = null;
        carOrderSosPopUpWindows = null;
        typeOrderSosPopUpWindows = null;
        if (priceOrderSosPopUpWindows != null) {
            priceOrderSosPopUpWindows.clear();
        }
        priceOrderSosPopUpWindows = null;
        penalSumSosPopUpWindows = null;
        clickCancelOrderSosPopUpWindows = null;
        clickGetOrderSosPopUpWindows = null;
        orderSosPopUpWindows = null;
        pushSOSHelperFromBuyerModel = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_sos_pop_up_windows);
        if (getIntent() == null || getIntent().getExtras() == null) {
            finish();
        }
        ButterKnife.bind(this);
        pushSOSHelperFromBuyerModel = getIntent().getExtras().getParcelable("data");
        if (pushSOSHelperFromBuyerModel == null) {
            finish();
        }
        priceOrderSosPopUpWindows.init(getBaseActivityContext(), false)
                .defaultNumber(0)
                .isNegativeNumber(false)
                .maxWidth(100);
        helperOrderSosPopUpWindows.setText(pushSOSHelperFromBuyerModel.recipients);
        scoreOrderSosPopUpWindows.setText(pushSOSHelperFromBuyerModel.credit);
        typeOrderSosPopUpWindows.setText(pushSOSHelperFromBuyerModel.type);
        carOrderSosPopUpWindows.setText(pushSOSHelperFromBuyerModel.vehicle_name);
        priceOrderSosPopUpWindows.setCurrentNumber(pushSOSHelperFromBuyerModel.price);
        penalSumSosPopUpWindows.setText(pushSOSHelperFromBuyerModel.damages);

        if (locationConfig == null) {
            locationConfig = new LocationConfig(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {

                        distanceOrderSosPopUpWindows.setText(ConvertUtils.gpsToDistanceKM(aMapLocation.getLatitude(), aMapLocation.getLongitude(), pushSOSHelperFromBuyerModel.lat, pushSOSHelperFromBuyerModel.lng) + "km");
                    }
                }
            }, getBaseActivityContext(), LocationConfig.LocationType_Continuous_Positioning);
        }
        locationConfig.start();

        this.setFinishOnTouchOutside(false);
        countDownSosPopUpWindows.setDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ToastUtils.showShortToast(getBaseActivityContext(), "该订单已过期");
                can = false;
                finish();
            }
        });
        try {
            LogUtils.e(Long.valueOf(pushSOSHelperFromBuyerModel.start_time));
            countDownSosPopUpWindows.setDownTime(Long.valueOf(pushSOSHelperFromBuyerModel.start_time) * 1000);
            countDownSosPopUpWindows.startDownTimer();
        } catch (NumberFormatException e) {

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @OnClick({R.id.click_cancel_order_sos_pop_up_windows, R.id.click_get_order_sos_pop_up_windows})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_cancel_order_sos_pop_up_windows:
                finish();
                break;
            case R.id.click_get_order_sos_pop_up_windows:
                if (can) {
                    if (getBaseActivityContext() != null) {
                       startActivity(new Intent(getBaseActivityContext(), OrderSOSAcceptFromSeller.class)
                                .putExtra("sos_id", pushSOSHelperFromBuyerModel.sos_id));

                    }
                    finish();
                } else {
                    ToastUtils.showShortToast(getBaseActivityContext(), "该订单已过期");
                }
                break;
        }
    }
}
