package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/1.
 */

public class CommodityEditModel {
    public String goods_id;
    public String title;
    public String slogan;
    public String master_map;
    public String price;
    public String number;
    public String goods_type;//0全新1二手
    public String shop_id;
    public String type;
    public List<String> classify_id=new ArrayList<>();
}
