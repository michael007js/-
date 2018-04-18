package com.sss.car.dao;

import com.rey.material.app.BottomSheetDialog;
import com.sss.car.model.OrderPayModel;

import java.util.List;

/**
 * Created by leilei on 2018/4/13.
 */

public interface OnPriceGoodsServiceCallBack {
    void onPriceCallBack(List<OrderPayModel> list, BottomSheetDialog bottomSheetDialog);
}
