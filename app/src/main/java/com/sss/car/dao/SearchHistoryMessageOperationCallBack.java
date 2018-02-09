package com.sss.car.dao;

import com.sss.car.model.SearchHistoryMessageModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/30.
 */

public interface SearchHistoryMessageOperationCallBack {
    void onClickHistroyMessage(int position, List<SearchHistoryMessageModel> list);
}
