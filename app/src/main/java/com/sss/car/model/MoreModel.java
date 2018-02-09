package com.sss.car.model;

/**
 * Created by leilei on 2018/1/20.
 */

public class MoreModel {
    public String classify_id;
    public String name;
    public String type;
    public String logo;
    public String spell;

    @Override
    public String toString() {
        return "MoreModel{" +
                "classify_id='" + classify_id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", spell='" + spell + '\'' +
                '}';
    }
}
