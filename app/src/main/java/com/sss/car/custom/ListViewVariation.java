package com.sss.car.custom;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by leilei on 2017/9/11.
 */

public class ListViewVariation extends PullToRefreshScrollView {
    List<View> list = new ArrayList<>();
    LinearLayout parent;
    View headView;
    View emptyView;

    public void setOnRefreshListener2(OnRefreshListener2 onRefreshListener2) {
        this.setMode(Mode.BOTH);
        this.setOnRefreshListener(onRefreshListener2);
    }

    public void clear(){
        if (parent!=null){
            parent.removeAllViews();
        }
        if (list!=null){
            list.clear();
        }
        list=null;
        headView=null;
    }

    public ListViewVariation(Context context) {
        super(context);
        init(context);
    }

    public ListViewVariation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }




     void init(Context context) {
        parent = new LinearLayout(context);
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.setOrientation(OrientationHelper.VERTICAL);
        parent.setGravity(Gravity.CENTER);
        this.addView(parent);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public void addHeadView(View view) {
        headView = view;
        if (headView.getParent()!=null){
            ((LinearLayout)headView.getParent()).removeView(headView);
        }
        parent.addView(headView);
    }

    public void addData(View view) {
        parent.addView(view);
        list.add(view);
    }

    public void addData(View view, int position) {
        parent.addView(view, position);
        list.add(position, view);
    }

    public void refreshData() {
        parent.removeAllViews();
        if (headView!=null){
            parent.addView(headView);
        }
    }


    public void setScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            this.setOnScrollChangeListener(onScrollChangeListener);
        }
    }


}
