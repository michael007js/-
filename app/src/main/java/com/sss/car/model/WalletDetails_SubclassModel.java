package com.sss.car.model;

/**
 * Created by leilei on 2017/10/25.
 */

public class WalletDetails_SubclassModel {
    public String id;
    public String remark;
    public String order_code;
    public String money;
    public String integral;
    public String create_time;
    public String type;

    @Override
    public String toString() {
        return "WalletDetails_SubclassModel{" +
                "id='" + id + '\'' +
                ", remark='" + remark + '\'' +
                ", order_code='" + order_code + '\'' +
                ", money='" + money + '\'' +
                ", integral='" + integral + '\'' +
                ", create_time='" + create_time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
