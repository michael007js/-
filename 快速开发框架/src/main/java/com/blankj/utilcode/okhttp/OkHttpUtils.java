package com.blankj.utilcode.okhttp;


import com.blankj.utilcode.okhttp.builder.GetBuilder;
import com.blankj.utilcode.okhttp.builder.HeadBuilder;
import com.blankj.utilcode.okhttp.builder.OtherRequestBuilder;
import com.blankj.utilcode.okhttp.builder.PostFileBuilder;
import com.blankj.utilcode.okhttp.builder.PostFormBuilder;
import com.blankj.utilcode.okhttp.builder.PostStringBuilder;
import com.blankj.utilcode.okhttp.callback.Callback;
import com.blankj.utilcode.okhttp.request.RequestCall;
import com.blankj.utilcode.okhttp.utils.Platform;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Source;
import okio.Timeout;


/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 30_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;
    private boolean print = true;//日志打印开关

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }

    public OkHttpUtils setPrintLogEnable(boolean enable) {
        print = enable;
        return mInstance;
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final Map<String, String> params, final String send, final String meaning, final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
                StringBuilder parameter = new StringBuilder();
                ///////////////////////////////////////////////////////////////////////↓/////////////////////////////////////////////////////////////////////////////////
                logWebRequest(call.request().method(),call.request().url().toString(), "请求失败", meaning, "访问失败了还返回个P啊", getSend(params,parameter),send, -1, e.getMessage());
                ///////////////////////////////////////////////////////////////////////↑/////////////////////////////////////////////////////////////////////////////////
            }
            @Override
            public void onResponse(final Call call, final Response response) {
                StringBuilder parameter = new StringBuilder();
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }
                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        ///////////////////////////////////////////////////////////////////////↓/////////////////////////////////////////////////////////////////////////////////
                        logWebRequest(call.request().method(),call.request().url().toString(), "请求失败", meaning, "访问失败了还返回个P啊", getSend(params,parameter), send,response.code(), "request failed , reponse's code is : " + response.code());
                        ///////////////////////////////////////////////////////////////////////↑/////////////////////////////////////////////////////////////////////////////////
                        return;
                    }
                    String type=call.request().method();
                Object o = null;
                try {
                    o = finalCallback.parseNetworkResponse(response, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendSuccessResultCallback(o, finalCallback, id);
                    ///////////////////////////////////////////////////////////////////////↓/////////////////////////////////////////////////////////////////////////////////
                    if (response.isSuccessful()) {

                        logWebRequest(type,response.request().url().toString(), "请求成功", meaning, (String) o,getSend(params,parameter), send,response.code(), "无");
                    } else {
                        logWebRequest(type,response.request().url().toString(), "请求成功,但服务器返回失败", meaning, (String) o,getSend(params,parameter).toString(),send, response.code(), "未收到服务器响应");
                    }
                    ///////////////////////////////////////////////////////////////////////↑/////////////////////////////////////////////////////////////////////////////////
                } catch (Exception e) {
                    e.printStackTrace();
                    sendFailResultCallback(call, e, finalCallback, id);
                    ///////////////////////////////////////////////////////////////////////↓/////////////////////////////////////////////////////////////////////////////////
                    logWebRequest("未知",response.request().url().toString(), "请求失败", meaning, "无", response.request().headers().toString(),send, response.code(), e.getMessage());
                    ///////////////////////////////////////////////////////////////////////↑/////////////////////////////////////////////////////////////////////////////////
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    String getSend(Map<String, String> params,StringBuilder send ){
        if (params==null){
            return "";
        }

        if (params.entrySet()==null){
            return "";
        }

        Iterator<Map.Entry<String, String>> iterator=params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry=iterator.next();
            send.append("\n          >");
            send.append(entry.getKey());
            send.append("---");
            send.append(entry.getValue());
            send.append("<");
            entry=null;
        }
        iterator.remove();
        iterator=null;
        return send.toString();
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }


    /**
     * @param url
     * @param successOrFail
     * @param meaning
     * @param resule
     * @param send
     * @param Code
     * @param errorInfo
     */
    String send="";

    /**
     * @param type
     * @param url
     * @param successOrFail
     * @param meaning
     * @param result
     * @param send
     * @param parameter
     * @param Code
     * @param errorInfo
     */
    public void logWebRequest(String type,String url, String successOrFail, String meaning, String result, String parameter,String send, int Code, String errorInfo) {
        if (EmptyUtils.isNotEmpty(parameter)){
            this.send=parameter;
        }
        if (EmptyUtils.isNotEmpty(send)){
            this.send=send;
        }
        if (print) {
            LogUtils.e("ssssss", "====================================网络请求[" + meaning + "]执行完毕,执行日志如下====================================" +
                    "\n请求类型:" + type +
                    "\n请求地址:" + url +
                    "\n状态:" + successOrFail +
                    "\n响应代码:" + Code +
                    "\n发送的数据:" + this.send +
                    "\n服务器返回的数据>" + result +"<"+
                    "\n错误信息:" + errorInfo );

        }
    }

}

