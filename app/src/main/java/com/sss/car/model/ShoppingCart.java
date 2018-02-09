package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/25.
 */

public class ShoppingCart {
    public String shop_id;
    public String name;
    public String logo;
    public String total_rows;
    public String total;
    public List<ShoppingCart_Data> data=new ArrayList<>();
    public boolean isChoose=false;
    public boolean editMode=false;




}
