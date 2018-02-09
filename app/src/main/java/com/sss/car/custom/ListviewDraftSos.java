package com.sss.car.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.R;
import com.sss.car.model.DrapSOS;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;

/**
 * Created by leilei on 2017/11/6.
 */

public class ListviewDraftSos extends LinearLayout {
    List<DrapSOS> list = new ArrayList<>();
    OnListviewDraftSosCallBack onListviewDraftSosCallBack;
    View view;
    InnerListview listview_listview_draft_sos;
    SSS_Adapter sss_adapter;

    public void setOnListviewDraftSosCallBack(OnListviewDraftSosCallBack onListviewDraftSosCallBack) {
        this.onListviewDraftSosCallBack = onListviewDraftSosCallBack;
    }

    public ListviewDraftSos(Context context) {
        super(context);
    }

    public ListviewDraftSos(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewDraftSos(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setList(List<DrapSOS> list, Context context) {
        this.list = list;
        setData(context);
    }

    void setData(Context context) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_draft_sos, null);
            listview_listview_draft_sos = $.f(view, R.id.listview_listview_draft_sos);
            sss_adapter = new SSS_Adapter<DrapSOS>(context, R.layout.item_listview_draft_sos, list) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, DrapSOS bean, SSS_Adapter instance) {
                    if (StringUtils.isEmpty(bean.title)) {
                        helper.setText(R.id.content_item_listview_draft_sos, "还未设置");
                    } else {
                        helper.setText(R.id.content_item_listview_draft_sos, bean.title);
                    }
                    if (StringUtils.isEmpty(bean.address)) {
                        helper.setText(R.id.content_item_listview_draft_sos, "还未设置");
                    } else {
                        helper.setText(R.id.content_item_listview_draft_sos, bean.address);
                    }
                    if (StringUtils.isEmpty(bean.type)) {
                        helper.setText(R.id.content_item_listview_draft_sos, "还未设置");
                    } else {
                        helper.setText(R.id.content_item_listview_draft_sos, bean.type);
                    }
                    helper.setText(R.id.date_item_listview_draft_sos, bean.create_time);

                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    helper.setItemChildClickListener(R.id.click_item_listview_draft_sos);
                    helper.setItemChildClickListener(R.id.edit_item_listview_draft_sos);
                    helper.setItemChildClickListener(R.id.delete_item_listview_draft_sos);
                }
            };

            sss_adapter.setOnItemListener(new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.click_item_listview_draft_sos:
                            if (onListviewDraftSosCallBack != null) {
                                onListviewDraftSosCallBack.onClickFromSOS(list.get(position).sos_id);
                            }
                            break;
                        case R.id.edit_item_listview_draft_sos:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_sos)).smoothClose();
                            if (onListviewDraftSosCallBack != null) {
                                onListviewDraftSosCallBack.onEdit(list.get(position).sos_id);
                            }
                            break;
                        case R.id.delete_item_listview_draft_sos:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item_listview_draft_sos)).smoothClose();
                            if (onListviewDraftSosCallBack != null) {
                                onListviewDraftSosCallBack.onDelete(list.get(position).sos_id);
                            }
                            break;
                    }
                }
            });

            listview_listview_draft_sos.setAdapter(sss_adapter);
            this.addView(view);
        } else {
            if (list.size() > 0) {
                sss_adapter.setList(list);
            } else {
                listview_listview_draft_sos.removeAllViewsInLayout();
            }
        }

    }

    public interface OnListviewDraftSosCallBack {
        void onClickFromSOS(String sos_id);

        void onEdit(String sos_id);

        void onDelete(String sos_id);
    }
}
