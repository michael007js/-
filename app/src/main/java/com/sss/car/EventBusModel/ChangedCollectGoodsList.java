package com.sss.car.EventBusModel;

/**
 * Created by leilei on 2017/11/3.
 */

public class ChangedCollectGoodsList {
    public String goods_id;
    public boolean isCollect=false;

    public ChangedCollectGoodsList(String goods_id, boolean isCollect) {
        this.goods_id = goods_id;
        this.isCollect=isCollect;
    }
}
