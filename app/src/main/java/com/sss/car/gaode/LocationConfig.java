package com.sss.car.gaode;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


/**
 * 定位需求
 * Created by leilei on 2017/4/19.
 */

public class LocationConfig {
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient  = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationClientOption = null;
    //单次定位
    public static final int LocationType_Single_Positioning=1;
    //连续定位
    public static final int LocationType_Continuous_Positioning=2;




    /**
     * 初始化定位信息
     *
     * @param mLocationListener 定位监听者
     * @param context
     * @param defaultLocationType 定位模式  1单次定位  2连续定位
     */
    public LocationConfig(AMapLocationListener mLocationListener, Context context , int defaultLocationType ) {
        if (mLocationClient ==null){
            mLocationClient  = new AMapLocationClient(context.getApplicationContext());
        }
        //初始化AMapLocationClientOption对象
        mLocationClientOption = new AMapLocationClientOption();
        //设置定位回调监听
        mLocationClient .setLocationListener(mLocationListener);
        //设置定位模式为高精度模式，Hight_Accuracy为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      switch (defaultLocationType){
          case LocationType_Single_Positioning:
              //返回最近3秒内最精确的一次
              mLocationClientOption.setOnceLocationLatest(true);
              //单次定位
              mLocationClientOption.setOnceLocation(true);
              break;
          case LocationType_Continuous_Positioning:
              //返回最近3秒内最精确的一次
              mLocationClientOption.setOnceLocationLatest(false);
              //单次定位
              mLocationClientOption.setOnceLocation(false);
              break;
      }
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationClientOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationClientOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationClientOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationClientOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationClientOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationClientOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient .setLocationOption(mLocationClientOption);

    }


    /**
     *  启动定位
     */
    public void start(){
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stop() {
        if (mLocationClient!=null){
            mLocationClient .stopLocation();
        }
    }
    /**
     * 销毁
     */
    public void release() {
        if (mLocationClient!=null){
            mLocationClient .stopLocation();
            mLocationClient .onDestroy();
            mLocationClient =null;
        }


    }


}
