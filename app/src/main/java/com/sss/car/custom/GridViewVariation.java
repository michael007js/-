package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/20.
 */

public class GridViewVariation extends LinearLayout {
    List<View> list = new ArrayList<>();


    public GridViewVariation(Context context) {
        super(context);
    }

    public GridViewVariation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewVariation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
