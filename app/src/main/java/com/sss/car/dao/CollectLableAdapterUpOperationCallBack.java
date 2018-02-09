package com.sss.car.dao;

import com.sss.car.model.LabelModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/12.
 */

public interface CollectLableAdapterUpOperationCallBack {
    void onClickTextView(int position, LabelModel labelModel, List<LabelModel> list);
}
