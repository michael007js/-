package com.sss.car.gaode;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;


/**
 * 地理编码
 * Created by leilei on 2017/4/20.
 */

public class Geocoding {
    //地理编码监听者
    private GeocodingSearchListener mGeocodingSearchListener;
    //地理编码对象
    private GeocodeSearch mGeocodeSearch;
    //地理编码查询
    private GeocodeQuery mGeocodeQuery;
    //逆向地理编码查询
    private RegeocodeQuery mRegeocodeQuery;

    SSS_GeocodingListener listener;
    /**
     * 初始化地理编码
     * @param context 上下文
     */
    public void init(Context context,SSS_GeocodingListener listener){
        //实例化地理编码对象
        mGeocodeSearch=new GeocodeSearch(context);
        //地理编码监听者
        mGeocodingSearchListener=new GeocodingSearchListener(context,listener);
        //为地理编码对象设置监听
        mGeocodeSearch.setOnGeocodeSearchListener(mGeocodingSearchListener);
        this.listener=listener;
    }

    /**
     * 地理编码查询
     * @param adress 查询地址
     * @param cityCode 查询城市代码
     */
    public void query(String adress,String cityCode){
        //实例化地理编码查询
        mGeocodeQuery=new  GeocodeQuery(adress, cityCode);
        //发起查询
        mGeocodeSearch.getFromLocationNameAsyn(mGeocodeQuery);
    }

    /**
     * 逆地理编码查询
     * @param latLonPoint 等同于Latlng
     * @param distance 距离
     */
    public void reverseQuery(LatLonPoint latLonPoint, float distance){
        //实例化逆向地理编码查询， 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        mRegeocodeQuery = new RegeocodeQuery(latLonPoint, distance,GeocodeSearch.AMAP);
        //发起查询
        mGeocodeSearch.getFromLocationAsyn(mRegeocodeQuery);
    }

}

/**
 * 地理编码监听者
 */
class GeocodingSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
    //上下文
    private Context mContext;

    SSS_GeocodingListener listener;

    /**
     * 构造地理编码监听者
     * @param mContext 上下文
     * @param listener
     */
    public GeocodingSearchListener(Context mContext,SSS_GeocodingListener listener) {
        this.mContext = mContext;
        this.listener=listener;
    }

    /**
     * 查询结果
     * @param regeocodeResult 查询结果
     * @param i 查询成功与否代码 1000成功，其余失败
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        listener.onRegeocodeSearched(regeocodeResult,i);
    }
    /**
     * 查询结果
     * @param geocodeResult 查询结果
     * @param i 查询成功与否代码 1000成功，其余失败
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        listener.onGeocodeSearched(geocodeResult,i);
    }
}
