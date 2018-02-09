package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.Notification.pugnotification.notification.Simple;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.ReputationAdapterCallBack;
import com.sss.car.model.ReputationModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/4.
 */

public class ReputationAdapter extends BaseAdapter{
    Context context;
    List<ReputationModel> list;
    LoadImageCallBack loadImageCallBack;
    ReputationAdapterCallBack reputationAdapterCallBack;

    public ReputationAdapter(Context context, List<ReputationModel> list, LoadImageCallBack loadImageCallBack, ReputationAdapterCallBack reputationAdapterCallBack) {
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
        this.reputationAdapterCallBack = reputationAdapterCallBack;
    }

    public void refresh( List<ReputationModel> list){
        this.list=list;
        this.notifyDataSetChanged();

    }

    public void clear(){
        if (list!=null){
            list.clear();
        }
        list=null;context=null;loadImageCallBack=null;reputationAdapterCallBack=null;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ReputationAdapterHolder reputationAdapterHolder;
        if (convertView==null){
            reputationAdapterHolder=new ReputationAdapterHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_reputation_adapter,null);
            reputationAdapterHolder.click_item_reputation_adapter= $.f(convertView,R.id.click_item_reputation_adapter);
            reputationAdapterHolder.circle_item_reputation_adapter= $.f(convertView,R.id.circle_item_reputation_adapter);
            reputationAdapterHolder.content_item_reputation_adapter= $.f(convertView,R.id.content_item_reputation_adapter);
            reputationAdapterHolder.reputation_item_reputation_adapter= $.f(convertView,R.id.reputation_item_reputation_adapter);
            reputationAdapterHolder.date_item_reputation_adapter= $.f(convertView,R.id.date_item_reputation_adapter);
            convertView.setTag(reputationAdapterHolder);
        }else {
            reputationAdapterHolder= (ReputationAdapterHolder) convertView.getTag();
        }
        reputationAdapterHolder.content_item_reputation_adapter.setText(list.get(position).describe);
        reputationAdapterHolder.reputation_item_reputation_adapter.setText(list.get(position).integral);
        reputationAdapterHolder.date_item_reputation_adapter.setText(list.get(position).create_time);
//        if ("1".equals(list.get(position).status)){
//            if (loadImageCallBack!=null){
//                loadImageCallBack.onLoad(FrescoUtils.showImage(true,25,25, Uri.parse("res://"+ AppUtils.getAppPackageName(context)+"/"+R.mipmap.logo_circle_red),reputationAdapterHolder.circle_item_reputation_adapter,0f));
//            }
//        }else   if ("0".equals(list.get(position).status)){
//            if (loadImageCallBack!=null){
//                loadImageCallBack.onLoad(FrescoUtils.showImage(true,25,25, Uri.parse("res://"+ AppUtils.getAppPackageName(context)+"/"+R.mipmap.logo_circle_orange),reputationAdapterHolder.circle_item_reputation_adapter,0f));
//            }
//        }
        reputationAdapterHolder.circle_item_reputation_adapter.setVisibility(View.GONE);
        reputationAdapterHolder.date_item_reputation_adapter.setText(list.get(position).create_time);
        if (reputationAdapterCallBack!=null){
            if (list.get(position).member_id.equals(Config.member_id)){
                reputationAdapterHolder.click_item_reputation_adapter.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        reputationAdapterCallBack.onDelete(list.get(position),position,list);
                        return false;
                    }
                });
            }
        }


        return convertView;
    }
}

class ReputationAdapterHolder{
    public LinearLayout click_item_reputation_adapter;
    public SimpleDraweeView circle_item_reputation_adapter;
    public TextView content_item_reputation_adapter;
    public TextView reputation_item_reputation_adapter;
    public TextView date_item_reputation_adapter;

}
