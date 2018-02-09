package com.sss.car.dao;

/**
 * 我的车辆==>热门车型,车型列表点击回调接口
 * Created by leilei on 2017/8/17.
 */

public interface MyCarOperationCallBack {

    void clickCarFromHotListOrCarList(String from, String brand_id, String logo, String name);
    void clickFromChildCarList(String carType, String series_id, String logo, String name);
}
