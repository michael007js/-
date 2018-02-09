package com.sss.car.dao;

import com.sss.car.model.ReportModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/30.
 */

public interface ReportAdapterOperationCallBack {
    void onClickReport(int poistion, boolean isChoose, ReportModel model, List<ReportModel> list);
}
