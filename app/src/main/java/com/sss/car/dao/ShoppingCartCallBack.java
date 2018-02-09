package com.sss.car.dao;


import com.sss.car.model.GoodsChooseSizeName;

import org.json.JSONArray;

import java.util.List;


/**
 * Created by leilei on 2017/9/25.
 */

public interface ShoppingCartCallBack {
    void onShoppingCartCallBack(int price, int number, List<GoodsChooseSizeName> size_name, JSONArray jsonArray, String type);
}
