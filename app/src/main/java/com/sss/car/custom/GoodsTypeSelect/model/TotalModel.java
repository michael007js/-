package com.sss.car.custom.GoodsTypeSelect.model;

/**
 * Created by leilei on 2017/10/31.
 */

public class TotalModel {
    public String title;
    public String price;
    public String number;

    public TotalModel(String title, String price, String number) {
        this.title = title;
        this.price = price;
        this.number = number;
    }

    @Override
    public String toString() {
        return "TotalModel{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
