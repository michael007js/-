package com.sss.car.dao;

import com.sss.car.model.AddressInfoModel;

/**
 * 收货信息==>地址列表点击回调
 * Created by leilei on 2017/8/19.
 */

public interface AdressListClickCallBack {

    void onDefault(AddressInfoModel addressInfoModel);
    void onEdit(AddressInfoModel addressInfoModel);
    void onDelete(AddressInfoModel addressInfoModel);


}
