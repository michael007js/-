package com.sss.car.custom;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.model.LabelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/12.
 */

public class GridLayoutDown extends GridLayout {

    List<TextView> list = new ArrayList<>();
    GridLayoutDownOperationCallBack gridLayoutDownOperationCallBack;

    public List<TextView> getList() {
        return list;
    }

    public GridLayoutDown(Context context) {
        super(context);
    }

    public GridLayoutDown(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridLayoutDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init( GridLayoutDownOperationCallBack gridLayoutDownOperationCallBack) {
        this.gridLayoutDownOperationCallBack = gridLayoutDownOperationCallBack;
    }

    public void add(final TextView textview) {
        if (StringUtils.isEmpty(textview.getText().toString().trim())) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().toString().trim().equals(textview.getText().toString().trim())) {
                return;
            }
        }
        this.removeAllViews();
        textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridLayoutDownOperationCallBack!=null){
                    gridLayoutDownOperationCallBack.onClick(textview.getText().toString().trim());
                }
            }
        });
        list.add(textview);
        for (int i = 0; i < list.size(); i++) {
            this.addView(list.get(i));
        }
    }



    public interface GridLayoutDownOperationCallBack {


        void onClick(String str);
    }

}
