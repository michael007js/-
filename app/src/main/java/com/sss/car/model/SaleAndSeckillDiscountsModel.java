package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/22.
 */

public class SaleAndSeckillDiscountsModel {
    public String classify_id;
    public String name;
    public String size_id;
    public String logo;
    public String sort;
    public String parent_id;
    public String is_top;
    public List <SaleAndSeckillDiscountsCouponModel> list=new ArrayList<>();
}
