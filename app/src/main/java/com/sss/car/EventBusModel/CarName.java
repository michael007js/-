package com.sss.car.EventBusModel;

/**
 * Created by leilei on 2017/9/28.
 */

public class CarName {
    public String carName;
    public boolean close;

    public CarName(String carName, boolean close) {
        this.carName = carName;
        this.close = close;
    }

    public CarName(String carName) {
        this.carName = carName;
    }
}
