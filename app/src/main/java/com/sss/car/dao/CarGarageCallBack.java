package com.sss.car.dao;

import com.sss.car.model.CarGarageModel;

/**
 * Created by leilei on 2017/8/17.
 */

public interface CarGarageCallBack {

    void onDeleteCar(String id);

    void onDefault(CarGarageModel carGarageModel);

    void onClick(CarGarageModel carGarageModel);
}
