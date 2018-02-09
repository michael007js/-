package com.sss.car.dao;

import com.sss.car.model.ShopInfoAllFilter_SubClassModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/19.
 */

public interface ChooseCallBack{
    void onChoose(int position, List<ShopInfoAllFilter_SubClassModel> list);
}