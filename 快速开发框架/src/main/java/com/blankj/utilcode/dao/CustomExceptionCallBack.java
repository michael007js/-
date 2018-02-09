package com.blankj.utilcode.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by leilei on 2017/8/8.
 */

public interface CustomExceptionCallBack {

    /**
     * 空异常
     */
    void onEmptyException(String errorMsg);

    /**
     * 算法错误异常
     * @param e
     */
    void onNoSuchAlgorithmException(NoSuchAlgorithmException e);

    /**
     * 编码不支持异常
     * @param e
     */
    void onUnsupportedEncodingException(UnsupportedEncodingException e);
}
