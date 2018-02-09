package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.ReportAdapterOperationCallBack;
import com.sss.car.model.ReportModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/30.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapterHolder> {
    List<ReportModel> list;
    Context context;
    ReportAdapterOperationCallBack reportAdapterOperationCallBack;
    boolean onBind;

    public ReportAdapter(List<ReportModel> list, Context context, ReportAdapterOperationCallBack reportAdapterOperationCallBack) {
        this.list = list;
        this.context = context;
        this.reportAdapterOperationCallBack = reportAdapterOperationCallBack;
    }


    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        reportAdapterOperationCallBack = null;

    }

    public void refresh(List<ReportModel> list ) {
            this.list = list;
            this.notifyDataSetChanged();
    }

    @Override
    public ReportAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_report, parent, false));
    }

    @Override
    public void onBindViewHolder(ReportAdapterHolder holder, final int position) {
        holder.text_item_report.setText(list.get(position).name);
        onBind=false;
        holder.cb_item_report.setChecked(list.get(position).isChoose);
        onBind=true;
        if (reportAdapterOperationCallBack != null) {
            holder.cb_item_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onBind){
                        reportAdapterOperationCallBack.onClickReport(position, isChecked, list.get(position), list);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ReportAdapterHolder extends RecyclerView.ViewHolder {
    TextView text_item_report;
    CheckBox cb_item_report;

    public ReportAdapterHolder(View itemView) {
        super(itemView);
        text_item_report = $.f(itemView, R.id.text_item_report);
        cb_item_report = $.f(itemView, R.id.cb_item_report);
    }
}
