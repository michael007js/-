package com.sss.car.order_new;

/**
 * Created by leilei on 2017/11/10.
 */

public class Constant {
    public static final int Order_My_Payment = -1;
    public static final int Order_My_Sure_receive = -2;
    public static final int Order_My_Comment = -3;
    public static final int Order_My_Complete = -4;

    public static final int Non_Payment = 1;//未付款
    public static final int Have_Already_Paid_Awating_Delivery = 2;//已付款（待发货）
    public static final int Have_Already_Delivery_Awating_Sign_For = 3;//已发货（待签收）
    public static final int Awating_Comment = 4;//待评价
    //    public static final int Complete = 5;//已完成
    public static final int Awating_Dispose_Returns = 6;//待处理（已申请退货）
    public static final int Returns = 7;//已退货
    public static final int Changed = 8;//已换货
    public static final int Refunded = 9;//已退款
    public static final int Awating_Service = 10;//待服务
    public static final int Ready_Buy = 11;//预购
    public static final int Cancel = 12;//取消
    public static final int Delete = 13;//已删除
}
