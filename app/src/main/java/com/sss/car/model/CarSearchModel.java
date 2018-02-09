package com.sss.car.model;

/**
 * Created by leilei on 2017/8/18.
 */

public class CarSearchModel {
    public String brand_id;
    public String logo;
    public String name;

    public CarSearchModel(String brand_id,String logo, String name) {
        this.brand_id = brand_id;
        this.logo=logo;
        this.name = name;
    }
}
