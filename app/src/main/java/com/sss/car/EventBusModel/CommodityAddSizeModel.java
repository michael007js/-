package com.sss.car.EventBusModel;

import org.json.JSONArray;

/**
 * Created by leilei on 2017/11/1.
 */

public class CommodityAddSizeModel {
    public String type;
    public JSONArray sizeName;
    public JSONArray sizeData;

    public CommodityAddSizeModel(String type, JSONArray sizeName, JSONArray sizeData) {
        this.type = type;
        this.sizeName = sizeName;
        this.sizeData = sizeData;
    }
}
