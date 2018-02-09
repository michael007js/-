package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;

/*
  <com.sss.car.custom.HideShowButton
                    xmlns:fresco="http://schemas.android.com/apk/res-auto"
                    android:id="@+id/eyes_one"
                    android:layout_width="20dp"
                    android:layout_height="20dp"
                    android:layout_marginLeft="10dp"
                    android:layout_centerInParent="true"
                    fresco:placeholderImage="@mipmap/logo_eyes_close"
                    fresco:placeholderImageScaleType="fitCenter"
                    fresco:roundAsCircle="true"/>

*/

/**
 * Created by leilei on 2017/12/2.
 */

public class HideShowButton extends SimpleDraweeView implements View.OnClickListener {
    OnHideShowButtonCallBack onHideShowButtonCallBack;

    public void setOnHideShowButtonCallBack(OnHideShowButtonCallBack onHideShowButtonCallBack) {
        this.onHideShowButtonCallBack = onHideShowButtonCallBack;
    }

    boolean isHide = false;

    public HideShowButton(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context);
    }

    public HideShowButton(Context context) {
        super(context);
        init(context);
    }

    public HideShowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HideShowButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public HideShowButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.setOnClickListener(this);
    }

    public void changed(int w, int h, Context context){
        if (isHide){
            FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_eyes_open), this, 0f);
        }else {
            FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_eyes_close), this, 0f);
        }

    }


    public void hide(int w, int h, Context context) {
        FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_eyes_close), this, 0f);
    }

    public void show(int w, int h, Context context) {
        FrescoUtils.showImage(false, w, h, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_eyes_open), this, 0f);
    }

    @Override
    public void onClick(View v) {
        if (isHide) {
            isHide = false;
        } else {
            isHide = true;
        }
        if (onHideShowButtonCallBack != null) {
            onHideShowButtonCallBack.onHideShowButtonClick(isHide);

        }
        LogUtils.e(isHide);
    }

    public interface OnHideShowButtonCallBack {
        void onHideShowButtonClick(boolean isHide);
    }

}
