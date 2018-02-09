package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/25.
 */

public class ShoppingCart_Data {
    public String id;
    public String name;
    public String num;
    public String price;
    public String shop_id;
    public String master_map;
    public String sid;
    public String total;
    public String is_collect;
    public String size_name;
    public String type;
    public boolean isChoose=false;
    public boolean editMode=false;
    public boolean isAddShow=true;
    public List<ShoppingCart_Data_Options> options=new ArrayList<>();

}
