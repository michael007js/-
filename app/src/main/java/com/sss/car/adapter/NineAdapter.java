package com.sss.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicOperationCallBack;

import java.util.List;


/**
 * Created by leilei on 2017/9/1.
 */

public class NineAdapter extends BaseAdapter {
    List<String> list;
    Activity activity;
    LoadImageCallBack loadImageCallBack;
    DymaicOperationCallBack dymaicOperationCallBack;

    public NineAdapter(List<String> list, Activity activity,LoadImageCallBack loadImageCallBack,DymaicOperationCallBack dymaicOperationCallBack) {
        this.list = list;
        this.activity = activity;
        this.loadImageCallBack=loadImageCallBack;
        this.dymaicOperationCallBack=dymaicOperationCallBack;
    }



    public void clear(){
        if (list!=null){
            list.clear();
        }
        list=null;activity=null;loadImageCallBack=null;dymaicOperationCallBack=null;

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        NineAdapterHolder nineAdapterHolder;
        if (convertView==null){
            nineAdapterHolder=new NineAdapterHolder();
            convertView= LayoutInflater.from(activity).inflate(R.layout.ltem_nine_view,null);
            nineAdapterHolder.pic_item_nine_view= $.f(convertView,R.id.pic_item_nine_view);
            convertView.setTag(nineAdapterHolder);
        }else {
            nineAdapterHolder= (NineAdapterHolder) convertView.getTag();
        }

        if (loadImageCallBack!=null){
            nineAdapterHolder.pic_item_nine_view.setLayoutParams(new LinearLayout.LayoutParams(activity.getWindowManager().getDefaultDisplay().getWidth()/3,activity.getWindowManager().getDefaultDisplay().getWidth()/3));
            loadImageCallBack.onLoad(FrescoUtils.showImage(false, activity.getWindowManager().getDefaultDisplay().getWidth()/3, activity.getWindowManager().getDefaultDisplay().getWidth()/3, Uri.parse(Config.url + list.get(position)), nineAdapterHolder.pic_item_nine_view, 0f));
        }
        if (dymaicOperationCallBack!=null){
            nineAdapterHolder.pic_item_nine_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dymaicOperationCallBack.onClickImage(position,list.get(position),list);
                }
            });

        }
        return convertView;
    }
}

class NineAdapterHolder{
    public SimpleDraweeView pic_item_nine_view;
}
