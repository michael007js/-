package com.sss.car.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.CarGarageCallBack;
import com.sss.car.model.CarGarageModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/17.
 */

public class CarGarageAdapter extends BaseAdapter {
    List<CarGarageModel> list;
    Context context;
    CarGarageAdapterHolder carGarageAdapterHolder;
    CarGarageCallBack carGarageCallBack;
    LoadImageCallBack loadImageCallBack;

    public CarGarageAdapter(List<CarGarageModel> list, Context context, CarGarageCallBack carGarageCallBack, LoadImageCallBack loadImageCallBack) {
        this.list = list;
        this.context = context;
        this.carGarageCallBack = carGarageCallBack;
        this.loadImageCallBack = loadImageCallBack;
    }

    public void refresh(List<CarGarageModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        carGarageAdapterHolder = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
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
            carGarageAdapterHolder = new CarGarageAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car_garage_adapter, null);
            carGarageAdapterHolder.delete_item_car_list_adapter_content = $.f(convertView, R.id.delete_item_car_list_adapter_content);
            carGarageAdapterHolder.logo_item_car_list_adapter_content = $.f(convertView, R.id.logo_item_car_list_adapter_content);
            carGarageAdapterHolder.name_item_car_list_adapter_content = $.f(convertView, R.id.name_item_car_list_adapter_content);
            carGarageAdapterHolder.type_item_car_list_adapter_content = $.f(convertView, R.id.type_item_car_list_adapter_content);
            carGarageAdapterHolder.default_item_car_list_adapter_content=$.f(convertView,R.id.default_item_car_list_adapter_content);
            carGarageAdapterHolder.default_logo_item_car_list_adapter_content=$.f(convertView,R.id.default_logo_item_car_list_adapter_content);
            carGarageAdapterHolder.click=$.f(convertView,R.id.click);
            convertView.setTag(carGarageAdapterHolder);
        } else {
            carGarageAdapterHolder = (CarGarageAdapterHolder) convertView.getTag();
        }
        if (loadImageCallBack != null) {
            carGarageAdapterHolder.logo_item_car_list_adapter_content.setTag(R.id.glide_tag, Config.url + list.get(position).brand);
            loadImageCallBack.onLoad(GlidUtils.downLoader(false, carGarageAdapterHolder.logo_item_car_list_adapter_content, context));
        }
        carGarageAdapterHolder.name_item_car_list_adapter_content.setText(list.get(position).name+list.get(position).type);
        carGarageAdapterHolder.type_item_car_list_adapter_content.setText(list.get(position).displacement+list.get(position).year);
        carGarageAdapterHolder.delete_item_car_list_adapter_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carGarageCallBack!=null){
                    carGarageCallBack.onDeleteCar(list.get(position).vehicle_id);
                }
            }
        });
        carGarageAdapterHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carGarageCallBack!=null){
                    carGarageCallBack.onClick(list.get(position));
                }
            }
        });

        //1默认
        if ("1".equals(list.get(position).is_default)){
            if (loadImageCallBack!=null){
                loadImageCallBack.onLoad(GlidUtils.glideLoad(false,carGarageAdapterHolder.default_logo_item_car_list_adapter_content,context,R.mipmap.yes));
            }
            carGarageAdapterHolder.default_item_car_list_adapter_content.setTextColor(context.getResources().getColor(R.color.mainColor));
            carGarageAdapterHolder.default_item_car_list_adapter_content.setOnClickListener(null);
            carGarageAdapterHolder.default_logo_item_car_list_adapter_content.setOnClickListener(null);
        }else {
            loadImageCallBack.onLoad(GlidUtils.glideLoad(false,carGarageAdapterHolder.default_logo_item_car_list_adapter_content,context,R.mipmap.yes_un));
            carGarageAdapterHolder.default_item_car_list_adapter_content.setTextColor(Color.parseColor("#383838"));
            carGarageAdapterHolder.default_item_car_list_adapter_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (carGarageCallBack!=null){
                        carGarageCallBack.onDefault(list.get(position));
                    }
                }
            });
            carGarageAdapterHolder.default_logo_item_car_list_adapter_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (carGarageCallBack!=null){
                        carGarageCallBack.onDefault(list.get(position));
                    }
                }
            });
        }




        return convertView;
    }
}

class CarGarageAdapterHolder {

    ImageView logo_item_car_list_adapter_content;
    TextView name_item_car_list_adapter_content;
    TextView type_item_car_list_adapter_content;
    TextView delete_item_car_list_adapter_content;
    TextView default_item_car_list_adapter_content;
    ImageView default_logo_item_car_list_adapter_content;
    LinearLayout click;

}