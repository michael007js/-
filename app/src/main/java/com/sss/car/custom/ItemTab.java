package com.sss.car.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;
import com.sss.car.model.CateModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/7.
 */

public class ItemTab extends LinearLayout {
    int count = 5;
    ViewPager viewPager;

    public ItemTab(Context context) {
        super(context);
        init(context);
    }

    public ItemTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        viewPager = new ViewPager(context);
        this.addView(viewPager);
    }

    /**
     * 一页显示的数量
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    List<View> ViewList = new ArrayList<>();

    public void setAdapter(List<CateModel> data, Context context) {
        if (data.size() > 0) {
            if (data.size() <= count) {
                LinearLayout linearlayout = new LinearLayout(context);
                for (int i = 0; i < data.size(); i++) {
                    linearlayout.addView( initView(context,i,data));
                }
                ViewList.add(linearlayout);
            } else {


                int pager = data.size() / count;
                int surplus = data.size() % count;
                for (int i = 0; i < pager; i++) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    for (int j = 0; j < count; j++) {
                        linearLayout.addView( initView(context,i*count+j,data));
                        ViewList.add(linearLayout);
                    }
                }



                LinearLayout linearLayout = new LinearLayout(context);
                for (int i = 0; i < surplus; i++) {
                    linearLayout.addView( initView(context,pager*count+i,data));
                    ViewList.add(linearLayout);
                }
            }
            viewPager. setOffscreenPageLimit(0);
            viewPager.setAdapter(new ViewPagerObjAdpter(ViewList));

        }
    }


    View initView(Context context,int position,List<CateModel> data){
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        LinearLayout click_item_tab = $.f(view, R.id.click_item_tab);
        click_item_tab.setTag(position);
        SimpleDraweeView pic_item_tab = $.f(view, R.id.pic_item_tab);
        TextView text_item_tab = $.f(view, R.id.text_item_tab);
        text_item_tab.setText(/*data.get(position).cate_name*/"dsadasd");
        LogUtils.e(position);
        return  view;
    }
}
