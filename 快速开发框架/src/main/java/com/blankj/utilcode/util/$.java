package com.blankj.utilcode.util;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.GridView.InnerGridView;

/**
 * 简化版findViewById
 * 直接调用V.f()即可
 * Created by leilei on 2017/5/16.
 */

public class $ {

    /**
     * activity.findViewById()
     * @param context
     * @param id
     * @return
     */
    public static <T extends View> T f(Activity context, int id) {
        return (T) context.findViewById(id);
    }

    /**
     * rootView.findViewById()
     * @param rootView
     * @param id
     * @return
     */
    public static <T extends View> T f(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }



}
