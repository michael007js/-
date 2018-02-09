package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/27.
 */

public class CommodityAddGoodsTypeModelTwo {
    public String classify_id;
    public String name;
    public String parent_id;
    public int position = -1;
    public  List<CommodityAddGoodsTypeModelThree> three=new ArrayList<>();

    public CommodityAddGoodsTypeModelTwo(String classify_id, String name, String parent_id) {
        this.classify_id = classify_id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public CommodityAddGoodsTypeModelTwo() {
    }
}
