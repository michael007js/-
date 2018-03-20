package com.sss.car.model;

/**
 * Created by leilei on 2018/1/28.
 */

public class BankModel {

    /**
     * card_id : 3
     * card_num : 6421*************8478
     * bank_name : 中国银行
     * bank_logo :
     */
    public String is_default;
    public String card_id;
    public String card_num;
    public String bank_name;
    public String bank_logo;
    public String card_beg;
    public String card_end;

    @Override
    public String toString() {
        return "BankModel{" +
                "is_default='" + is_default + '\'' +
                ", card_id='" + card_id + '\'' +
                ", card_num='" + card_num + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", bank_logo='" + bank_logo + '\'' +
                ", card_beg='" + card_beg + '\'' +
                ", card_end='" + card_end + '\'' +
                '}';
    }
}
