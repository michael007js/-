package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class GridLayoutUp extends GridLayout {

    List<TextView> list = new ArrayList<>();
    EditText edit;
    GridLayoutUpOperationCallBack gridLayoutUpOperationCallBack;

    public List<TextView> getList() {
        return list;
    }

    public GridLayoutUp(Context context) {
        super(context);
    }

    public GridLayoutUp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridLayoutUp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init(Context context, GridLayoutUpOperationCallBack gridLayoutUpOperationCallBack) {
        this.gridLayoutUpOperationCallBack = gridLayoutUpOperationCallBack;
        edit = new EditText(context);
        edit.setSingleLine(true);
        edit.setBackground(null);
        if (gridLayoutUpOperationCallBack != null) {
            gridLayoutUpOperationCallBack.onAddEdit(edit);
        }
        this.addEdit();
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
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getText().toString().trim().equals(textview.getText().toString().trim())) {
                        list.remove(i);
                    }
                }
                GridLayoutUp.this.removeView(textview);
                if (gridLayoutUpOperationCallBack != null) {
                    gridLayoutUpOperationCallBack.onDelete(textview.getText().toString().trim());
                }
            }
        });
        list.add(textview);
        for (int i = 0; i < list.size(); i++) {
            this.addView(list.get(i));
        }

        addEdit();
    }


    public void delete(String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().toString().trim().equals(text)) {
                if (gridLayoutUpOperationCallBack != null) {
                    gridLayoutUpOperationCallBack.onDelete(list.get(i).getText().toString().trim());
                }
                this.removeView(list.get(i));
                list.remove(i);
            }
        }
    }


    public void addEdit() {
        this.addView(edit);
    }


    public interface GridLayoutUpOperationCallBack {

        void onAddEdit(EditText edittect);

        void onDelete(String str);
    }

}
