package com.sss.car.utils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PriceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.model.CouponModel3;

import java.util.List;

/**
 * Created by leilei on 2018/3/20.
 */

public class C {

    public static Double D(List<CouponModel3> v1, double v2, String v3) {
        LogUtils.e("v1:" + v1 + "    v2:" + v2 + "   v3:" + v3);
        if (StringUtils.isEmpty(v3) || v1 == null || v1.size() == 0) {
            return 0.00;
        }

        for (int i = 0; i < v1.size(); i++) {
            if (v3.equals(v1.get(i).coupon_id)) {
                if ("1".equals(v1.get(i).type)) {
                    if (v2 > Double.valueOf(v1.get(i).coupon_id)) {
                        return PriceUtils.subtract(v2, Double.valueOf(v1.get(i).money), 2);
                    } else {
                        return 0.0;
                    }
                } else if ("2".equals(v1.get(i).type)) {
                    return PriceUtils.subtract(v2, Double.valueOf(v1.get(i).money), 2);
                } else if ("3".equals(v1.get(i).type)) {
                    LogUtils.e(v2+"---"+(Double.valueOf(v1.get(i).money)));
                    return Double.valueOf(PriceUtils.formatBy2Scale(PriceUtils.multiply(v2, (Double.valueOf(v1.get(i).money)), 2) / 100, 2));
                }
            }
        }
        return 0.00;
    }
}
