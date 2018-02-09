package com.sss.car.model;

/**
 * Created by leilei on 2017/8/30.
 */

public class ReportModel {
    public String from_id;
    public String name;
    public boolean isChoose;

    public ReportModel(String from_id, String name,boolean isChoose) {
        this.from_id = from_id;
        this.name = name;
        this.isChoose=isChoose;
    }
}
