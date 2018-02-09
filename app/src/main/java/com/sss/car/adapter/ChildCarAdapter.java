package com.sss.car.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.MyCarOperationCallBack;
import com.sss.car.model.ChildCarModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/17.
 */

public class ChildCarAdapter extends BaseAdapter{
    List<ChildCarModel> childList;
    Context context;
    ChildCarAdapterHolder childCarAdapterHolder;
    MyCarOperationCallBack myCarOperationCallBack;
    String carType="";

    public ChildCarAdapter(List<ChildCarModel> childList, Context context,MyCarOperationCallBack myCarOperationCallBack) {
        this.childList = childList;
        this.context = context;
        this.myCarOperationCallBack=myCarOperationCallBack;
    }
    public void refresh(String carType,List<ChildCarModel> childList) {
        this.childList = childList;
        this.carType=carType;
        this.notifyDataSetChanged();
    }
    public void clear(){
        if (childList!=null){
            childList.clear();
        }
        childCarAdapterHolder=null;
        childList=null;
        context=null;
        myCarOperationCallBack=null;
    }

    @Override
    public int getCount() {
        return childList.size();
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
        if (convertView==null){
            childCarAdapterHolder=new ChildCarAdapterHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_child_car_adapter,null);
            childCarAdapterHolder.name_item_child_car_adapter= $.f(convertView,R.id.name_item_child_car_adapter);
            convertView.setTag(childCarAdapterHolder);
        }else {
            childCarAdapterHolder= (ChildCarAdapterHolder) convertView.getTag();
        }
        childCarAdapterHolder.name_item_child_car_adapter.setText(childList.get(position).name);
        //0为标题,其余为有数据
        if ("0".equals(childList.get(position).parent_id)){
            childCarAdapterHolder.name_item_child_car_adapter.setBackgroundColor(Color.parseColor("#f3f3f3"));
            childCarAdapterHolder.name_item_child_car_adapter.setOnClickListener(null);
        }else {
            childCarAdapterHolder.name_item_child_car_adapter.setBackgroundColor(Color.WHITE);
            childCarAdapterHolder.name_item_child_car_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myCarOperationCallBack!=null){
                        myCarOperationCallBack.clickFromChildCarList(carType,childList.get(position).series_id,childList.get(position).logo,childList.get(position).name);
                    }
                }
            });
        }

        return convertView;
    }
}
class ChildCarAdapterHolder{
    TextView name_item_child_car_adapter;
}
