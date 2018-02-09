package com.sss.car.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.RequestWeb;
import com.sss.car.view.ActivityUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Call;

/**
 * Created by leilei on 2018/1/23.
 */

public class QRUtils {

    /**
     * 二维码扫描结果跳转汇总
     *
     * @param data
     * @param baseContext
     * @param ywLoadingDialog
     */
    public static void start(String data, Context baseContext, YWLoadingDialog ywLoadingDialog) {


        if (StringUtils.isEmpty(data)) {
            ToastUtils.showShortToast(baseContext, "数据出错");
            return;
        }
        try {
            LogUtils.e(URLDecoder.decode(data.split("\\?")[1], "utf-8"));
            String[] parameter = URLDecoder.decode(data.split("\\?")[1], "utf-8").split("&");
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < parameter.length; i++) {
                String[] temp = parameter[i].split("=");
                if (temp.length == 2) {
                    jsonObject.put(temp[0], temp[1]);
                }
            }
            if (jsonObject.length() == 0) {
                ToastUtils.showShortToast(baseContext, "解码出错!");
                return;
            }
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ywLoadingDialog = null;
            if (baseContext != null) {
                ywLoadingDialog = new YWLoadingDialog(baseContext);
            }
            jump(jsonObject, baseContext, ywLoadingDialog);
        } catch (UnsupportedEncodingException e) {
            ToastUtils.showShortToast(baseContext, "解码出错Err-1" + e.getMessage());
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            ToastUtils.showShortToast(baseContext, "解码出错Err-2" + e.getMessage());
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShortToast(baseContext, "解码出错Err-3" + e.getMessage());
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ToastUtils.showShortToast(baseContext, "解码出错Err-4" + e.getMessage());
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
        }
    }


    private static void jump(final JSONObject jsonObject, final Context context, final YWLoadingDialog ywLoadingDialog) throws JSONException,NumberFormatException {
        if ("member".equals(jsonObject.getString("type"))) {
            context.startActivity(new Intent(context, ActivityUserInfo.class).putExtra("id", jsonObject.getString("ids")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if ("group".equals(jsonObject.getString("type"))) {
            APPOftenUtils.createAskDialog(context, "确认是否要加入该群？", new OnAskDialogCallBack() {
                @Override
                public void onOKey(Dialog dialog) {
                    try {
                        join(ywLoadingDialog, context, jsonObject.getString("ids"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.showShortToast(context, "解码出错Err-5" + e.getMessage());
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                    }
                    dialog.dismiss();
                    dialog = null;
                }

                @Override
                public void onCancel(Dialog dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            });
        } else if ("order".equals(jsonObject.getString("type"))) {
            if ( !Config.member_id.equals(jsonObject.getString("member_pid"))&&!Config.member_id.equals(jsonObject.getString("member_id"))){
                ToastUtils.showShortToast(context,"没有查询到相关订单信息");
                return;
            }
            if ("1".equals(jsonObject.getString("order_type"))){
                CarUtils.orderJump(context,
                        "goods",
                        Integer.parseInt(jsonObject.getString("status")),
                        jsonObject.getString("ids"),
                        Config.member_id.equals(jsonObject.getString("member_pid")),
                        jsonObject.getString("goods_comment"),
                        jsonObject.getString("is_comment"),
                        jsonObject.getString("exchange_id"),
                        jsonObject.getString("exchange_status"));
            }else  if ("1".equals(jsonObject.getString("order_type"))){
                CarUtils.orderJump(context,
                        "service",
                        Integer.parseInt(jsonObject.getString("status")),
                        jsonObject.getString("ids"),
                        Config.member_id.equals(jsonObject.getString("member_pid")),
                        jsonObject.getString("goods_comment"),
                        jsonObject.getString("is_comment"),
                        jsonObject.getString("exchange_id"),
                        jsonObject.getString("exchange_status"));
            }

        } else if ("sos".equals(jsonObject.getString("type"))) {
            CarUtils.orderJump(context,
                    jsonObject.getString("type"),
                    Integer.parseInt(jsonObject.getString("status")),
                    jsonObject.getString("ids"),
                    Config.member_id.equals(jsonObject.getString("member_pid")),
                    jsonObject.getString("goods_comment"),
                    jsonObject.getString("is_comment"),
                    jsonObject.getString("exchange_id"),
                    jsonObject.getString("exchange_status"));
        }else {
            ToastUtils.showShortToast(context, "未知");
        }
    }


    /**
     * 邀请加群
     */
    private static void join(final YWLoadingDialog ywLoadingDialog, final Context context, String group_id) {
        ywLoadingDialog.show();
        try {
            RequestWeb.join(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("group_id", group_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(context, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtils.showShortToast(context, jsonObject.getString("message"));
                                    } else {
                                        ToastUtils.showShortToast(context, jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(context, "数据解析错误err:invite group-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(context, "数据解析错误err:invite group-0");
            e.printStackTrace();
        }
    }
}
