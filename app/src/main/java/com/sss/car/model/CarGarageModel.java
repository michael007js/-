package com.sss.car.model;

/**
 * Created by leilei on 2017/8/17.
 */

public class CarGarageModel {
    public String vehicle_id;
    public String name;
    public String brand;
    public String type;
    public String displacement;
    public String year;
    public String ids;
    public String create_time;
    public String member_id;
    public String is_default;

    @Override
    public String toString() {
        return "CarGarageModel{" +
                "vehicle_id='" + vehicle_id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", displacement='" + displacement + '\'' +
                ", year='" + year + '\'' +
                ", ids='" + ids + '\'' +
                ", create_time='" + create_time + '\'' +
                ", member_id='" + member_id + '\'' +
                ", is_default='" + is_default + '\'' +
                '}';
    }
}
