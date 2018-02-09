package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.CarSearchClickCallBack;
import com.sss.car.model.CarSearchModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/18.
 */

public class CarSearchAdapter extends BaseAdapter{
    List<CarSearchModel> list;
    Context context;
    CarSearchClickCallBack carSearchClickCallBack;
    CarSearchAdapterHolder carSearchAdapterHolder;

    public CarSearchAdapter(List<CarSearchModel> list, Context context, CarSearchClickCallBack carSearchClickCallBack) {
        this.list = list;
        this.context = context;
        this.carSearchClickCallBack = carSearchClickCallBack;
    }

    public void clear(){
        if (list!=null){
            list.clear();
        }
        list=null;
        context=null;
        carSearchClickCallBack=null;
        carSearchAdapterHolder=null;
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
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_car_search_adapter,null);
            carSearchAdapterHolder=new CarSearchAdapterHolder();
            carSearchAdapterHolder.name_item_car_search_adapter= $.f(convertView,R.id.name_item_car_search_adapter);
            convertView.setTag(carSearchAdapterHolder);
        }else {
            carSearchAdapterHolder= (CarSearchAdapterHolder) convertView.getTag();
        }
        carSearchAdapterHolder.name_item_car_search_adapter.setText(list.get(position).name);
        carSearchAdapterHolder.name_item_car_search_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carSearchClickCallBack!=null){
                    carSearchClickCallBack.onClickCar(list.get(position));
                }
            }
        });
        return convertView;
    }
}
class CarSearchAdapterHolder{
    TextView name_item_car_search_adapter;

}
