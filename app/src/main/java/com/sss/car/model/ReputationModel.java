package com.sss.car.model;

/**
 * Created by leilei on 2017/9/4.
 */

public class ReputationModel {
    public String credit_id;
    public String integral;
    public String describe;
    public String member_id;
    public String create_time;
    public String state;//state = 0未审核，state =1 已审核, status = 0 扣分，status = 1加分
    public String status; //（status = 0）失败（status = 1）成功
}
