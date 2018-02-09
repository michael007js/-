package com.sss.car.model;

/**
 * Created by leilei on 2017/10/27.
 */

public class CommodityAddGoodsTypeModelThree {
    public String classify_id;
    public String name;
    public String parent_id;
    public int position = -1;

    public CommodityAddGoodsTypeModelThree(String classify_id, String name, String parent_id) {
        this.classify_id = classify_id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public CommodityAddGoodsTypeModelThree() {
    }
}
