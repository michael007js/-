package com.sss.car.model;

/**
 * Created by leilei on 2017/8/17.
 */

public class CarListModel {
    public String brand_id;
    public String name;
    public String logo;
    public String pell;
    public String title;

    @Override
    public String toString() {
        return "CarListModel{" +
                "brand_id='" + brand_id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", pell='" + pell + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
