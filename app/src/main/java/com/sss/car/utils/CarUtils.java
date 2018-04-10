package com.sss.car.utils;


import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.Config;
import com.sss.car.WebViewActivity;
import com.sss.car.order.OrderGoodsMyOrderBuyer;
import com.sss.car.order.OrderGoodsOrderSendSeller;
import com.sss.car.order.OrderGoodsOrderTip;
import com.sss.car.order.OrderSOSAffirmBuyer;
import com.sss.car.order.OrderSOSAffirmSeller;
import com.sss.car.order.OrderSOSDetails;
import com.sss.car.order.OrderSOSGrabList;
import com.sss.car.order.OrderServiceMyOrderBuyer;
import com.sss.car.order.OrderServiceOrderTip;
import com.sss.car.order_new.Constant;
import com.sss.car.order_new.OrderGoodsReadyBuyList;
import com.sss.car.order_new.OrderServiceReadyBuyList;
import com.sss.car.view.ActivityWeb;

import static com.blankj.utilcode.okhttp.OkHttpUtils.delete;
import static com.sss.car.R.id.comment;


/**
 * Created by leilei on 2017/8/28.
 */

@SuppressWarnings("ALL")
public class CarUtils {


    public static int isDeposit(String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        } else {
            if (str.contains("%")) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    public static void startAdvertisement(Context context) {
        if (!StringUtils.isEmpty(Config.tempUrl)) {
            context.startActivity(new Intent(context, WebViewActivity.class)
                    .putExtra("title", "广告")
                    .putExtra("url", Config.tempUrl));
        }
    }


    /**
     * 地址选择
     *
     * @param context
     * @param city
     * @param lat
     * @param lng
     * @param onCitySelectCallBack
     */
    public static void select(Context context, String city, double lat, double lng, OnCitySelectCallBack onCitySelectCallBack) {
        if (StringUtils.isEmpty(city)) {
            if (onCitySelectCallBack != null) {
                onCitySelectCallBack.onFail("城市名选择出错!");
            }
            return;
        }

        SPUtils spUtils = new SPUtils(context, Config.defaultFileName, Context.MODE_PRIVATE);
        spUtils.put("city", city);
        spUtils.put("lat", String.valueOf(lat));
        spUtils.put("lng", String.valueOf(lng));
        Config.city = city;
        Config.latitude = String.valueOf(lat);
        Config.longitude = String.valueOf(lng);

        if (onCitySelectCallBack != null) {
            onCitySelectCallBack.onSuccess();
        }
    }

    /**
     * 订单跳转汇总
     *
     * @param context
     * @param type            车品车服或SOS
     * @param status          订单状态
     * @param ids             订单ID
     * @param isIncome        是否为收入(SOS不需要)
     * @param goodsComment    该商品相对于商家来讲是否已评论
     * @param isComment       该商品相对于买家来讲是否已评论
     * @param exchange_id     退换货ID
     * @param exchange_status 1同意退换货 2不同意退换货
     */
    public static void orderJump(Context context, String type, int status, String ids, boolean isIncome, String goodsComment, String isComment, String exchange_id, String exchange_status) {
        LogUtils.e("type:" + type + "     status:" + status + "     id:" + ids + "     isIncome:" + isIncome + "     goodsComment:" + goodsComment + "     isComment:" + isComment + "     exchange_status:" + exchange_status);
        switch (type) {
            //1实物订单，2服务订单，3SOS订单
            case "sos":
                sos(context, status, ids, goodsComment, isComment);
                break;
            case "goods":
                goods(context, status, ids, isIncome, goodsComment, isComment, exchange_id, exchange_status);
                break;
            case "service":
                service(context, status, ids, isIncome, goodsComment, isComment);
                break;
        }
    }

    /**
     * 车品订单跳转
     *
     * @param context
     * @param status
     * @param ids
     * @param isIncome
     */
    private static void goods(Context context, int status, String ids, boolean isIncome, String goodsComment, String isComment, String exchange_id, String exchange_status) {
        switch (status) {
            case Constant.Ready_Buy://预购
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsReadyBuyList.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids));
                } else {
                    context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("isIncome", isIncome)
                            .putExtra("status", status));
                }
                break;
            case Constant.Non_Payment://未付款
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("buttonTitle", "待买家支付"));
                } else {
                    context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("isIncome", isIncome)
                            .putExtra("status", status));
                }
                break;
            case Constant.Have_Already_Paid_Awating_Delivery:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status));
                } else {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("buttonTitle", "待发货"));
                }
                break;
            case Constant.Have_Already_Delivery_Awating_Sign_For:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status)
                            .putExtra("buttonTitle", "待签收"));
                } else {
                    context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("isIncome", isIncome)
                            .putExtra("status", status));
                }
                break;
            case Constant.Awating_Comment:
                if (isIncome) {
                    if ("0".equals(goodsComment)) {
                        context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("isIncome", isIncome)
                                .putExtra("status", status));


                    } else {
                        context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("isIncome", isIncome)
                                .putExtra("status", -100));/*传-100为删除订单*/
                    }
                } else {
                    if ("0".equals(isComment)) {
                        context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("isIncome", isIncome)
                                .putExtra("status", status));
                    } else {
                        context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("isIncome", isIncome)
                                .putExtra("status", -100));/*传-100为删除订单*/
                    }
                }
                break;
            case Constant.Delete:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("hideButton", true));
                } else {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("hideButton", true));
                }
                break;
            case Constant.Awating_Dispose_Returns:
                if (isIncome) {
                    if ("1".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "已同意"));
                    } else if ("2".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "未同意"));
                    } else {
                        context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", status));
                    }

                } else {
                    if ("1".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("what", 2)
                                .putExtra("isIncome", isIncome)
                                .putExtra("status", status));
                    } else if ("2".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "未同意"));
                    }else {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "待处理"));
                    }
                }

                break;
            case Constant.Returns:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("exchange_id", exchange_id)
                            .putExtra("order_id", ids)
                            .putExtra("exchange_status", exchange_status)
                            .putExtra("status", status));
                } else {
                    if ("3".equals(exchange_status)){
//                   context.startActivity(new Intent(context, ActivityWeb.class)
//                            .putExtra("type", ActivityWeb.LOGISTICS)
//                            .putExtra("order_id", ids));
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "待签收"));
                    }
                }
                break;

            case Constant.Changed:
                if (isIncome) {
                    if (!"4".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("exchange_id", exchange_id)
                                .putExtra("exchange_status", exchange_status)
                                .putExtra("status", status));
                    }
                } else {
                    if ("3".equals(exchange_status)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "待收货"));
                    } else  if ("6".equals(exchange_status)){
                            context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("order_id", ids)
                                    .putExtra("exchange_id", exchange_id)
                                    .putExtra("exchange_status", exchange_status)
                                    .putExtra("status", status));
                    }else  if ("4".equals(exchange_status)){
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "待发货"));
                    }else  {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "待签收"));
                    }
                }
                break;
            case Constant.Refunded:
                if (isIncome) {
                    if ("5".equals(exchange_id)) {
                        context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("grayness", true)
                                .putExtra("buttonTitle", "已退款"));
                    } else {
                        context.startActivity(new Intent(context, OrderGoodsOrderSendSeller.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", status));
                    }
                } else {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("grayness", true)
                            .putExtra("buttonTitle", "已退款"));
                }

                break;
            case Constant.Cancel:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("buttonTitle", "已取消"));


                } else {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("buttonTitle", "已取消"));
                }
                break;
        }
    }

    /**
     * 车服订单跳转
     *
     * @param context
     * @param status
     * @param ids
     * @param isIncome
     */
    private static void service(Context context, int status, String ids, boolean isIncome, String goodsComment, String isComment) {
        switch (status) {
            case Constant.Ready_Buy://预购
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderServiceReadyBuyList.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids));
                } else {
                    context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status));
                }
                break;
            case Constant.Non_Payment:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderGoodsOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("buttonTitle", "待买家支付"));
                } else {
                    context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status));
                }

                break;
            case Constant.Have_Already_Paid_Awating_Delivery:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderServiceOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status)
                            .putExtra("buttonTitle", "待服务"));
                } else {
                    context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("status", status));
                }
                break;
            case Constant.Awating_Comment:
                if (isIncome) {
                    if ("0".equals(goodsComment)) {
                        context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", status));
                    } else {
                        context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", -100));/*传-100为删除订单*/
                    }
                } else {
                    if ("0".equals(isComment)) {
                        context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", status));
                    } else {
                        context.startActivity(new Intent(context, OrderServiceMyOrderBuyer.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("order_id", ids)
                                .putExtra("status", -100));/*传-100为删除订单*/
                    }
                }
                break;
            case Constant.Delete:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderServiceOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("hideButton", true));
                } else {
                    context.startActivity(new Intent(context, OrderServiceOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("hideButton", true));
                }
                break;
            case Constant.Cancel:
                if (isIncome) {
                    context.startActivity(new Intent(context, OrderServiceOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("grayness", true)
                            .putExtra("buttonTitle", "已取消"));


                } else {
                    context.startActivity(new Intent(context, OrderServiceOrderTip.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("order_id", ids)
                            .putExtra("grayness", true)
                            .putExtra("buttonTitle", "已取消"));
                }
                break;
        }
    }

    /**
     * SOS订单跳转
     *
     * @param context
     * @param status
     * @param ids
     */
    private static void sos(Context context, int status, String ids, String goodsComment, String isComment) {
        LogUtils.e(status);
        switch (status) {
            case 0://求助中
                context.startActivity(new Intent(context, OrderSOSGrabList.class)/*OrderSOSDetails详情*/
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
            case 1://求助者进行中
                context.startActivity(new Intent(context, OrderSOSAffirmBuyer.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
            case 2://已完成
                context.startActivity(new Intent(context, OrderSOSDetails.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
            case 3://已取消
                context.startActivity(new Intent(context, OrderSOSDetails.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
            case 5:// 施救者进行中(建立关系后)
                context.startActivity(new Intent(context, OrderSOSAffirmSeller.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
            case 6://施救者进行中(建立关系前)
                context.startActivity(new Intent(context, OrderSOSDetails.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("sos_id", ids));
                break;
        }
    }


    public interface OnCitySelectCallBack {
        void onSuccess();

        void onFail(String msg);
    }

}
