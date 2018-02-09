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
import com.sss.car.model.DrapSOS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/11/6.
 */

public class ListviewDraftGoods extends LinearLayout {
    List<DrapGoods> list = new ArrayList<>();
    OnListviewDraftGoodsCallBack onListviewDraftGoodsCallBack;
    View view;
    InnerListview listview_listview_draft_goods;
    SSS_Adapter sss_adapter;
    LoadImageCallBack loadImageCallBack;

    public void setOnListviewDraftGoodsCallBack(OnListviewDraftGoodsCallBack onListviewDraftGoodsCallBack) {
        this.onListviewDraftGoodsCallBack = onListviewDraftGoodsCallBack;
    }

    public ListviewDraftGoods(Context context) {
        super(context);
    }

    public ListviewDraftGoods(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewDraftGoods(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setList(List<DrapGoods> list, Context context) {
        this.list = list;
        setData(context);
    }


    void setData(Context context) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_draft_goods, null);
            listview_listview_draft_goods = $.f(view, R.id.listview_listview_draft_goods);
            sss_adapter = new SSS_Adapter<DrapGoods>(context, R.layout.item_listview_draft_goods, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, DrapGoods bean, SSS_Adapter instance) {
                    helper.setText(R.id.content_item_listview_draft_goods, bean.title);
                    helper.setText(R.id.price_item_listview_draft_goods, "¥" + bean.price);
                    helper.setText(R.id.slogan_item_listview_draft_goods, bean.slogan);
                    if (loadImageCallBack != null) {
                        if (loadImageCallBack != null) {
                            loadImageCallBack.onLoad(FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_listview_draft_goods)), 0f));
                        }
                    } else {
                        FrescoUtils.showImage(false, 60, 60, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_listview_draft_goods)), 0f);
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_listview_draft_goods);
                    helper.setItemChildClickListener(R.id.edit_item_listview_draft_goods);
                    helper.setItemChildClickListener(R.id.delete_item_listview_draft_goods);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.click_item_listview_draft_goods:
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onClickFromGoods(list.get(position).goods_id, list.get(position).type);
                            }
                            break;
                        case R.id.edit_item_listview_draft_goods:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_goods)).smoothClose();
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onEdit(list.get(position).goods_id, list.get(position).type);
                            }
                            break;
                        case R.id.delete_item_listview_draft_goods:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_goods)).smoothClose();
                            if (onListviewDraftGoodsCallBack != null) {
                                onListviewDraftGoodsCallBack.onDelete(list.get(position).goods_id);
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

    public interface OnListviewDraftGoodsCallBack {
        void onClickFromGoods(String goods_id, String type);

        void onEdit(String goods_id, String type);

        void onDelete(String goods_id);
    }
}
