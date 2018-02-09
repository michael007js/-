package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.DrapGoods;
import com.sss.car.model.DrapPopularize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/6.
 */

public class ListviewDraftPopularize extends LinearLayout {
    List<DrapPopularize> list = new ArrayList<>();
    OnListviewDraftPopularizeCallBack onListviewDraftGoodsCallBack;
    View view;
    InnerListview listview_listview_draft_goods;
    SSS_Adapter sss_adapter;
    LoadImageCallBack loadImageCallBack;

    public void setOnListviewDraftGoodsCallBack(OnListviewDraftPopularizeCallBack onListviewDraftGoodsCallBack) {
        this.onListviewDraftGoodsCallBack = onListviewDraftGoodsCallBack;
    }

    public ListviewDraftPopularize(Context context) {
        super(context);
    }

    public ListviewDraftPopularize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewDraftPopularize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setList(List<DrapPopularize> list, Context context) {
        this.list = list;
        setData(context);
    }

    private void addEmpty(Context context) {
        this.addView(LayoutInflater.from(context).inflate(R.layout.empty_view, null));
    }

    void setData(Context context) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_draft_goods, null);
            listview_listview_draft_goods = $.f(view, R.id.listview_listview_draft_goods);
            sss_adapter = new SSS_Adapter<DrapPopularize>(context, R.layout.item_listview_draft_popularize, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, DrapPopularize bean, SSS_Adapter instance) {
                    helper.setText(R.id.content_item_listview_draft_popularize, bean.popularize_title);
                    helper.setText(R.id.price_item_listview_draft_popularize, bean.create_time);
                    helper.setText(R.id.slogan_item_listview_draft_popularize, bean.title);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_listview_draft_popularize);
                    helper.setItemChildClickListener(R.id.edit_item_listview_draft_popularize);
                    helper.setItemChildClickListener(R.id.delete_item_listview_draft_popularize);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.click_item_listview_draft_popularize:
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onClickFromPopularize(list.get(position));
                            }
                            break;
                        case R.id.edit_item_listview_draft_popularize:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_popularize)).smoothClose();
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onEdit(list.get(position));
                            }
                            break;
                        case R.id.delete_item_listview_draft_popularize:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_popularize)).smoothClose();
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onDelete(list.get(position));
                            }
                            break;
                    }
                }
            });

            listview_listview_draft_goods.setAdapter(sss_adapter);
            this.addView(view);
        } else {
            if (list.size() > 0) {
                sss_adapter.setList(list);
            } else {
                listview_listview_draft_goods.removeAllViewsInLayout();
            }
        }

    }

    public interface OnListviewDraftPopularizeCallBack {

        void onClickFromPopularize(DrapPopularize popularize_id);

        void onEdit(DrapPopularize popularize_id);

        void onDelete(DrapPopularize popularize_id);
    }
}
