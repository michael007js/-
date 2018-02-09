package com.blankj.utilcode.util;


import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 对OKHttp的简单封装
 * 参考http://www.cnblogs.com/loaderman/p/6445755.html
 * Created by leilei on 2017/5/2.
 */

public class OkHttpUtil_S {

    /**
     * post上传String(为Json服务)
     *  @param url
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public static Call doPostWithString(String url, JSONObject jsonObject) throws IOException {
        return new OkHttpClient().newCall(new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()))
                .build());
    }


    /**
     *多图上传
     * @param url
     * @param list
     * @param callback
     */
    private static void doUploadImages(String url , List<String> list, Callback callback){
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <list.size() ; i++) {
            File f=new File(list.get(i));
            if (f!=null) {
                builder.addFormDataPart("img", f.getName(), RequestBody.create(MediaType.parse("image/png"), f));
            }
        }
        //添加其它信息
//        builder.addFormDataPart("time",takePicTime);
//        builder.addFormDataPart("mapX", SharedInfoUtils.getLongitude());
//        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
//        builder.addFormDataPart("name",SharedInfoUtils.getUserName());


        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        new OkHttpClient().newCall(request).enqueue(callback);
    }


}
