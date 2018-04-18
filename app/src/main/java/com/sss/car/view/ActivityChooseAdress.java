package com.sss.car.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.blankj.utilcode.Fragment.FragmentMoveMapAdressChoose;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.dao.LocationStatusListener;
import com.blankj.utilcode.model.TargetInfoModel;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChooseAdress;
import com.sss.car.R;
import com.sss.car.gaode.Geocoding;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.gaode.SSS_GeocodingListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地址选择器
 * Created by leilei on 2017/10/14.
 */

public class ActivityChooseAdress extends BaseActivity implements FragmentMoveMapAdressChoose.OnFragmentMoveMapAdressChooseCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_activity_choose_adress)
    FrameLayout parentActivityChooseAdress;
    @BindView(R.id.listview_activity_choose_adress)
    ListView listviewActivityChooseAdress;
    @BindView(R.id.activity_choose_adress)
    LinearLayout activityChooseAdress;


    FragmentMoveMapAdressChoose fragmentMoveMapAdressChoose;

    SSS_Adapter sss_adapter;




    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {


        backTop = null;
        titleTop = null;
        activityChooseAdress = null;
        parentActivityChooseAdress = null;
        listviewActivityChooseAdress = null;
        if (fragmentMoveMapAdressChoose != null) {
            fragmentMoveMapAdressChoose.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_adress);
        ButterKnife.bind(this);

        customInit(activityChooseAdress, false, true, false);
        if (fragmentMoveMapAdressChoose == null) {
            fragmentMoveMapAdressChoose = new FragmentMoveMapAdressChoose(getBaseActivityContext(), this);
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentMoveMapAdressChoose, R.id.parent_activity_choose_adress);
        }
        titleTop.setText("选择地址");
        init();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentMoveMapAdressChoose.Create(savedInstanceState);
                            fragmentMoveMapAdressChoose.locationPoint(Double.valueOf(Config.latitude), Double.valueOf(Config.longitude));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }





    void init() {
        sss_adapter = new SSS_Adapter<TargetInfoModel>(getBaseActivityContext(), R.layout.item_map_search_adapter) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final TargetInfoModel bean,SSS_Adapter instance) {
                helper.setText(R.id.item_map_search_adapter, bean.provinceName + bean.cityName + bean.snippet);
                ((TextView) helper.getView(R.id.item_map_search_adapter)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ChooseAdress(bean.snippet, String.valueOf(bean.latLonPoint.getLatitude()),
                                String.valueOf(bean.latLonPoint.getLongitude()),bean.provinceName,bean.cityName,bean.district));
                        finish();
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listviewActivityChooseAdress.setAdapter(sss_adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentMoveMapAdressChoose.onSaveInstanceState(outState);
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onTouch(MotionEvent event) {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onPoiSearchedSuccess(List<TargetInfoModel> list) {
        sss_adapter.setList(list);
    }

    @Override
    public void onPoiSearchedFail(int errCode) {
        ToastUtils.showShortToast(getBaseActivityContext(), "搜索失败" + errCode);
    }
}
