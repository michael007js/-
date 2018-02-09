package com.sss.car.dao;

import com.sss.car.model.ShopInfoAllFilterModel;
import com.sss.car.model.ShopInfoAllFilter_SubClassModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/19.
 */

public interface ShopInfoAllFilterTwoAdapterOperationCallBack {

    void onClickShopInfoAllFilterTwoAdapterItem(int position, ShopInfoAllFilter_SubClassModel model, List<ShopInfoAllFilter_SubClassModel> list);
}
