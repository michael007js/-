package com.sss.car.model;

/**
 * Created by leilei on 2017/8/29.
 */

public class GroupMemberModel {
    public String group_id;
    public String member_id;
    public String minute; //被禁言的时间
    public String state; //0已申请，1已加入，2群主，3管理员，4已拉黑
    public String remark;
    public String face;
}
