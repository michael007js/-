package com.sss.car.dao;

import com.sss.car.model.CateModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public interface ItemTabAdapterOperationCallBack {
    void onClickTab(int position, CateModel cateModel, List<CateModel> list);

}
