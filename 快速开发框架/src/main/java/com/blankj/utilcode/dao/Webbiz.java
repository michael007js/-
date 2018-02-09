package com.blankj.utilcode.dao;

import android.content.Context;

import com.blankj.utilcode.okhttp.OkHttpUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EmptyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.blankj.utilcode.okhttp.OkHttpUtils.postString;

/**
 * Created by leilei on 2017/8/8.
 */

public class Webbiz {
    /**
     * 请求APP许可证
     * http://apply.nx.021dr.cn/admin/login/index.html
     *
     * @param localClassName
     * @param context
     * @param app_id                  服务器数据库中记录的APPID
     * @param stringCallback
     * @param customExceptionCallBack
     * @throws JSONException
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Call requestAPPLicense(String localClassName,Context context, String app_id, StringCallback stringCallback, CustomExceptionCallBack customExceptionCallBack) throws JSONException, NoSuchAlgorithmException {
        if (context==null) {
            customExceptionCallBack.onEmptyException("requestAPPLicense,context is null");
            return null;
        }
        return requestByString(localClassName,"http://apply.nx.021dr.cn/index.php/Api/Index/into"
                , new JSONObject()
                        .put("app_id", app_id)
                        .put("app_name", AppUtils.getAppName(context, customExceptionCallBack))
                        .put("app_key", ConvertUtils.md5Add(app_id + AppUtils.getAppName(context, customExceptionCallBack), customExceptionCallBack))
                        .toString()
                , "请求APP许可"
                , stringCallback);
    }
    /**
     * 网络请求(String)
     *
     * @param url
     * @param send
     * @param meaning
     * @param stringCallback
     */
    public static Call requestByString(String localClassName,String url, String send, String meaning, StringCallback stringCallback) {
        return OkHttpUtils
                .getInstance()
                .setPrintLogEnable(true)
                .postString()
                .tag(localClassName)
                .url(url)
                .content(send)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .connTimeOut(30000)
                .writeTimeOut(30000)
                .readTimeOut(30000)
                .execute(meaning, send, stringCallback);
    }
    /**
     * 网络请求(String)
     *
     * @param url
     * @param send
     * @param meaning
     * @param stringCallback
     */
    public static Call requestByString(String url, String send, String meaning, StringCallback stringCallback) {
        return OkHttpUtils
                .getInstance()
                .setPrintLogEnable(true)
                .postString()
                .url(url)
                .content(send)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .connTimeOut(30000)
                .writeTimeOut(30000)
                .readTimeOut(30000)
                .execute(meaning, send, stringCallback);
    }


}
