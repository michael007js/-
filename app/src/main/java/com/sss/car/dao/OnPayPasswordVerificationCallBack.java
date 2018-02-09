package com.sss.car.dao;

import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.rey.material.app.BottomSheetDialog;

/**
 * Created by leilei on 2017/10/12.
 */

public interface OnPayPasswordVerificationCallBack {
    void onVerificationPassword(String password, PassWordKeyboard passWordKeyboard, BottomSheetDialog bottomSheetDialog);
}
