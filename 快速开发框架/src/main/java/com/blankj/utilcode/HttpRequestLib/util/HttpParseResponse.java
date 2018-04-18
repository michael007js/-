package com.blankj.utilcode.HttpRequestLib.util;

import com.blankj.utilcode.HttpRequestLib.constant.ErrorCodeConstant;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by leilei on 2018/4/17.
 */

public class HttpParseResponse {
    /**
     * 读取返回数据
     *
     * @param inputStream
     * @return
     */
    public  static String getContent(InputStream inputStream, IHttpListener iHttpListener) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.IOException, e.getMessage());
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }
}
