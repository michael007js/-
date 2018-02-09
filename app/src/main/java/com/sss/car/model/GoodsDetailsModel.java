package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/14.
 */

public class GoodsDetailsModel {
    public String goods_id;
    public String slogan;
    public String is_like;
    public String title ;//汽车记录仪，德国原装进口，莱卡双摄",
    public String attr_data; // 商品属性
    public String cost_price; // 原价
    public String price; // 现价
    public String price_type; // 价格类型
    public String start_time; // 开始时间
    public String end_time; // 结束时间
    public String likes; // 收藏数量
    public String share; // 分享数量
    public String sell; // 销量
    public String member_id;
    public String shop_id;
    public String is_collect;
    public String classify_name;//商品路径类型
    public String master_map;
    public List<String> picture=new ArrayList<>();// 商品图片

}
