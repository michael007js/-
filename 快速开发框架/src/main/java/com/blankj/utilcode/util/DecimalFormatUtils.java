package com.blankj.utilcode.util;


import java.text.DecimalFormat;

/**
 * 小数解析
 * Created by leilei on 2017/5/26.
 */

public class DecimalFormatUtils {

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static String format2(double d){
        return new DecimalFormat("#0.00").format(d);
    }
}
