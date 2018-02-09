package com.sss.car.dao;

import com.sss.car.model.CarSearchModel;

/**
 * 我的爱车==>车品牌搜索回调
 * Created by leilei on 2017/8/18.
 */

public interface CarSearchClickCallBack {
    void onClickCar(CarSearchModel carSearchModel);
}
