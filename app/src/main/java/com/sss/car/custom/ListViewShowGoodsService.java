package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/13.
 */

@SuppressWarnings("ALL")
public class ListViewShowGoodsService extends LinearLayout {
    String classify_id;

    String title;
    View view;
    int pager;
    int number;
    int count;
    InnerListview listview_listview_show_goods_service;
    LinearLayout click_listview_show_goods_service;
    TextView title_listview_show_goods_service;
    TextView more_listview_show_goods_service;
    SimpleDraweeView right_arrows;
    ListViewShowGoodsServiceOperationCallBack listViewShowGoodsServiceOperationCallBack;
    SSS_Adapter sss_adapter;
    int selfHeight = 182;//自身的高度,XML中死数据
    public int p = 2;

    public void clear() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        classify_id = null;
        title = null;
        listViewShowGoodsServiceOperationCallBack = null;
        view = null;
        title_listview_show_goods_service = null;
        listview_listview_show_goods_service = null;
        more_listview_show_goods_service = null;
        click_listview_show_goods_service = null;
        right_arrows = null;
    }


    public ListViewShowGoodsService(Context context) {
        super(context);
        init(context);
    }

    public ListViewShowGoodsService(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewShowGoodsService(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_show_goods_service, null);
        right_arrows = $.f(view, R.id.right_arrows);
        listview_listview_show_goods_service = $.f(view, R.id.listview_listview_show_goods_service);
        click_listview_show_goods_service = $.f(view, R.id.click_listview_show_goods_service);
        title_listview_show_goods_service = $.f(view, R.id.title_listview_show_goods_service);
        more_listview_show_goods_service = $.f(view, R.id.more_listview_show_goods_service);
        click_listview_show_goods_service.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewShowGoodsServiceOperationCallBack != null) {
                    listViewShowGoodsServiceOperationCallBack.onClickTitle_ListViewShowGoodsServiceOperationCallBack(classify_id, title);
                }
            }
        });

        more_listview_show_goods_service.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewShowGoodsServiceOperationCallBack != null) {
                    listViewShowGoodsServiceOperationCallBack.onClickMore_ListViewShowGoodsServiceOperationCallBack(classify_id, title, pager, count, number, ListViewShowGoodsService.this);
                }
            }
        });
        this.addView(view);
    }


    public void setListViewShowGoodsServiceOperationCallBack(ListViewShowGoodsServiceOperationCallBack listViewShowGoodsServiceOperationCallBack) {
        this.listViewShowGoodsServiceOperationCallBack = listViewShowGoodsServiceOperationCallBack;
    }


    public void create(String title, String classify_id, int pager, int count, int number, SSS_Adapter sss_adapter, SSS_OnItemListener sss_onItemListener) {
        this.pager = pager;
        this.count = count;
        this.number = number;
        this.title = title;
        this.classify_id = classify_id;
        this.sss_adapter = sss_adapter;
        sss_adapter.setOnItemListener(sss_onItemListener);
        title_listview_show_goods_service.setText(this.title);
        listview_listview_show_goods_service.setSmoothScrollbarEnabled(true);
        listview_listview_show_goods_service.setAdapter(sss_adapter);
    }

    public int setData(List<GoodsModel> list, boolean isStopTopTitle) {
        if (sss_adapter != null) {
            sss_adapter.setList(list);
        }
        if (isStopTopTitle) {
            right_arrows.setVisibility(GONE);
            click_listview_show_goods_service.setOnClickListener(null);
        } else {
            right_arrows.setVisibility(VISIBLE);
            click_listview_show_goods_service.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listViewShowGoodsServiceOperationCallBack != null) {
                        listViewShowGoodsServiceOperationCallBack.onClickTitle_ListViewShowGoodsServiceOperationCallBack(classify_id, title);
                    }
                }
            });
        }
        return selfHeight + list.size() * 200/*200:适配器每个ITEM的高度*/;
    }

    public int setData(List<GoodsModel> list) {
        if (sss_adapter != null) {
            sss_adapter.setList(list);
        }
        return selfHeight + list.size() * 200/*200:适配器每个ITEM的高度*/;
    }

    public void hideModeButton(boolean hideModeButton) {
        if (hideModeButton) {
            if (more_listview_show_goods_service != null) {
                more_listview_show_goods_service.setVisibility(GONE);
            }
        } else {
            if (more_listview_show_goods_service != null) {
                more_listview_show_goods_service.setVisibility(VISIBLE);
            }
        }
    }


    public interface ListViewShowGoodsServiceOperationCallBack {

        void onClickMore_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title, int pager, int count, int number, ListViewShowGoodsService listViewShowGoodsService);

        void onClickTitle_ListViewShowGoodsServiceOperationCallBack(String classify_id, String title);
    }

}
