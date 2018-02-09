package com.sss.car.dao;

import com.sss.car.model.ReputationModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public interface ReputationAdapterCallBack {

    void onDelete(ReputationModel reputationModel, int position, List<ReputationModel> list);
}
