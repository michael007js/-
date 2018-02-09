package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.CarSetCarinfoCallBack;
import com.sss.car.model.CarSetCarInfoModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/18.
 */

public class CarSetCarinfoAdapter extends BaseAdapter {
    List<CarSetCarInfoModel> list;
    Context contex;
    CarSetCarinfoAdapterHolder carSetCarinfoAdapterHolder;
    CarSetCarinfoCallBack carSetCarinfoCallBack;

    public CarSetCarinfoAdapter(List<CarSetCarInfoModel> list, Context contex , CarSetCarinfoCallBack carSetCarinfoCallBack) {
        this.list = list;
        this.contex = contex;
        this.carSetCarinfoCallBack = carSetCarinfoCallBack;
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        contex = null;
        carSetCarinfoAdapterHolder = null;
        carSetCarinfoCallBack = null;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(contex).inflate(R.layout.item_car_set_car_info_adapter, null);
            carSetCarinfoAdapterHolder = new CarSetCarinfoAdapterHolder();
            carSetCarinfoAdapterHolder.name_item_car_set_car_info_adapter = $.f(convertView, R.id.name_item_car_set_car_info_adapter);
            convertView.setTag(carSetCarinfoAdapterHolder);
        } else {
            carSetCarinfoAdapterHolder = (CarSetCarinfoAdapterHolder) convertView.getTag();
        }
        carSetCarinfoAdapterHolder.name_item_car_set_car_info_adapter.setText(list.get(position).name);
        carSetCarinfoAdapterHolder.name_item_car_set_car_info_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carSetCarinfoCallBack!=null){
                    carSetCarinfoCallBack.onClickCarinfo(list.get(position));
                }
            }
        });
        return convertView;
    }
}

class CarSetCarinfoAdapterHolder {
    TextView name_item_car_set_car_info_adapter;

}
