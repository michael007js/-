package com.sss.car.utils;

import android.app.Activity;
import android.widget.Toast;

import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.RequestWeb;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.MainActivity;
import okhttp3.Call;

import static com.sss.car.R.id.report;

/**
 * Created by leilei on 2018/1/17.
 */

public class ShareUtils {

    /**
     * 准备分享
     *
     * @param ywLoadingDialog
     * @param activity
     * @param type            terrace 平台，goods 商品，shop 店铺， trends 动态， community 社区，coupon 优惠券
     * @param share_id        分类内容ID ,当分享平台时share_id = member_id
     */
    public static void prepareShare(YWLoadingDialog ywLoadingDialog, Activity activity, String type, String share_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(activity);
        requestServel(ywLoadingDialog, activity, type, share_id);
    }


    /**
     * 请求服务器获取分享信息
     *
     * @param ywLoadingDialog
     * @param activity
     * @param type            terrace 平台，goods 商品，shop 店铺， trends 动态， community 社区，coupon 优惠券
     * @param share_id        分类内容ID ,当分享平台时share_id = member_id
     */
    private static void requestServel(final YWLoadingDialog ywLoadingDialog, final Activity activity, final String type, final String share_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        try {
            RequestWeb.share_info(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("share_id", share_id)
                            .put("type", type).toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    startShare(
                                            ywLoadingDialog,
                                            activity,
                                            jsonObject.getJSONObject("data").getString("title"),
                                            jsonObject.getJSONObject("data").getString("picture"),
                                            jsonObject.getJSONObject("data").getString("url"),
                                            jsonObject.getJSONObject("data").getString("describe"),
                                            jsonObject.getJSONObject("data").getString("redirect_url"),
                                            jsonObject.getJSONObject("data").getJSONObject("param"));
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 开始分享
     *
     * @param ywLoadingDialog
     * @param activity
     * @param title           要分享的标题
     * @param picture         要分享的图片
     * @param url             要分享的地址
     * @param describe        描述
     * @param redirect_url    分享成功后的回调地址
     * @param sendData        分享成功后请求回调地址时所传的参数（由服务器返回）
     */
    private static void startShare(final YWLoadingDialog ywLoadingDialog, final Activity activity, String title, String picture, String url, String describe, final String redirect_url, final JSONObject sendData) {
        UMShareConfig config=new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        //当安装的时候进行SSO授权,还有一种网页授权,无论设备上是否按照微博客户端,都只会拉起网页授权
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(activity).setShareConfig(config);
        LogUtils.e(picture);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, picture));  //缩略图
        web.setDescription(describe);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        return;
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        ToastUtils.showShortToast(activity, "分享成功!");
                        report(activity, ywLoadingDialog, redirect_url, sendData);
                    }
                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        if (t != null) {
                            ToastUtils.showShortToast(activity, "分享失败:" + t.getMessage());
                        } else {
                            ToastUtils.showShortToast(activity, "分享失败!");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        LogUtils.e(platform.toString());
                        ToastUtils.showShortToast(activity, "分享取消!");
                    }
                })
                .open();

    }

    /**
     * 统一分享后给服务器返回信息
     *
     * @param ywLoadingDialog
     * @param activity
     * @param redirect_url    分享成功后的回调地址
     * @param sendData        分享成功后请求回调地址时所传的参数（由服务器返回）
     */
    private static void report(final Activity activity, final YWLoadingDialog ywLoadingDialog, String redirect_url, JSONObject sendData) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        RequestWeb.share_report(
                sendData.toString()
                , redirect_url, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.dismiss();
                        }
                        ToastUtils.showShortToast(activity, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.dismiss();
                        }

                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if ("1".equals(jsonObject.getString("status"))) {
                                ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                            } else {
                                ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.showShortToast(activity, "数据解析错误Err:-2");
                            e.printStackTrace();
                        }
                    }
                });
    }

}
