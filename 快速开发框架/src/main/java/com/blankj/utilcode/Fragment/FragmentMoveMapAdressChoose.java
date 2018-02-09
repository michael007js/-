package com.blankj.utilcode.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureSupportMapFragment;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.R;
import com.blankj.utilcode.model.TargetInfoModel;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图拖动搜索周边
 * Created by leilei on 2017/10/14.
 */

@SuppressLint("ValidFragment")
public class FragmentMoveMapAdressChoose extends TextureSupportMapFragment implements AMap.OnMapTouchListener {
    MapView map_fragment_move_map_address_choose;
    Context context;
    Marker marker;
    OnFragmentMoveMapAdressChooseCallBack onFragmentMoveMapAdressChooseCallBack;
    List<TargetInfoModel> targetInfoModelList = new ArrayList<>();

    public FragmentMoveMapAdressChoose(Context context, OnFragmentMoveMapAdressChooseCallBack onFragmentMoveMapAdressChooseCallBack) {
        this.context = context;
        this.onFragmentMoveMapAdressChooseCallBack = onFragmentMoveMapAdressChooseCallBack;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_move_map_address_choose, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        map_fragment_move_map_address_choose = $.f(view, R.id.map_fragment_move_map_address_choose);
        init();
    }

    void init() {

        map_fragment_move_map_address_choose.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                marker = map_fragment_move_map_address_choose.getMap().addMarker(new MarkerOptions());
                marker.setPositionByPixels(map_fragment_move_map_address_choose.getWidth() / 2, map_fragment_move_map_address_choose.getHeight() / 2);
            }
        });

        map_fragment_move_map_address_choose.getMap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LogUtils.e("onCameraChange");
                if (onFragmentMoveMapAdressChooseCallBack != null) {
                    onFragmentMoveMapAdressChooseCallBack.onCameraChange(cameraPosition);
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LogUtils.e("onCameraChangeFinish");
                doSearchQuery("", cameraPosition.target.latitude, cameraPosition.target.longitude);
            }
        });

    }

    /**
     * 开始进行poi搜索
     */
    void doSearchQuery(String city, double latitude, double longitude) {
        String mType = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
        PoiSearch.Query query = new PoiSearch.Query("", mType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                if (rCode == 1000) {
                    targetInfoModelList.clear();
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
                        targetInfoModelList.add(targetInfoModel);
                        LogUtils.e(targetInfoModel.toString());
                    }
                    if (onFragmentMoveMapAdressChooseCallBack != null) {
                        onFragmentMoveMapAdressChooseCallBack.onPoiSearchedSuccess(targetInfoModelList);
                    }
                } else {
                    if (onFragmentMoveMapAdressChooseCallBack != null) {
                        onFragmentMoveMapAdressChooseCallBack.onPoiSearchedFail(rCode);
                    }
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


    /**
     * 移动到指定位置
     */
    public void locationPoint(double lai, double lng) {
        map_fragment_move_map_address_choose.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lai, lng), 17/*MAX 18*/));
        doSearchQuery("", lai, lng);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    /**
     * activity中必须要加,否则无法显示(必须在runOnUiThread中加入)
     *
     * @param bundle
     */
    public void Create(Bundle bundle) {
        map_fragment_move_map_address_choose.onCreate(bundle);
    }

    public void onPause() {
        super.onPause();
        map_fragment_move_map_address_choose.onPause();
    }

    public void onResume() {
        super.onResume();
        map_fragment_move_map_address_choose.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_fragment_move_map_address_choose.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (map_fragment_move_map_address_choose != null) {
            map_fragment_move_map_address_choose.onDestroy();
        }
    }

    /**
     * 在activity执行onSaveInstanceState时执行mmMapView.onSaveInstanceState (outState)，保存地图当前的状态
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_fragment_move_map_address_choose.onSaveInstanceState(outState);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (onFragmentMoveMapAdressChooseCallBack != null) {
            onFragmentMoveMapAdressChooseCallBack.onTouch(motionEvent);
        }
    }

    public interface OnFragmentMoveMapAdressChooseCallBack {

        void onTouch(MotionEvent event);

        void onCameraChange(CameraPosition cameraPosition);

        void onPoiSearchedSuccess(List<TargetInfoModel> list);

        void onPoiSearchedFail(int errCode);
    }
}
