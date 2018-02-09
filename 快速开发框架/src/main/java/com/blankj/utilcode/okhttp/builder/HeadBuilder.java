package com.blankj.utilcode.okhttp.builder;


import com.blankj.utilcode.okhttp.OkHttpUtils;
import com.blankj.utilcode.okhttp.request.OtherRequest;
import com.blankj.utilcode.okhttp.request.RequestCall;



/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
