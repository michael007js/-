package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.AppUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;


/**
 * Created by leilei on 2017/12/2.
 */

public class ResultIma extends SimpleDraweeView {

    public boolean res;

    public ResultIma(Context context) {
        super(context);
    }

    public ResultIma(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }
    public ResultIma(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResultIma(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public ResultIma(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void wrong(int w, int h, Context context) {
        res = false;
        FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_result_wrong), this, 0f);
    }

    public void right(int w, int h, Context context) {
        res = true;
        FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_result_right), this, 0f);
    }

}
