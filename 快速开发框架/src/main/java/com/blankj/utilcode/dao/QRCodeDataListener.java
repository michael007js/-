package com.blankj.utilcode.dao;

import android.content.Context;

/**
 * Created by leilei on 2017/5/9.
 */

public interface QRCodeDataListener {
    /**
     * 二维码数据回调
     */
    public void onQRCodeDataChange(String data, Context baseContext);
}
