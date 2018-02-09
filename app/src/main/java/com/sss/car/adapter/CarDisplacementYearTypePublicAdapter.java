package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.MyCarDisplacementYearTypeClickCallBack;
import com.sss.car.model.CarDisplacementYearTypePublicModel;

import java.util.List;



/**
 * Created by leilei on 2017/8/17.
 */

public class CarDisplacementYearTypePublicAdapter extends BaseAdapter {
    List<CarDisplacementYearTypePublicModel> list;
    Context context;
    CarDisplacementYearTypePublicAdapterHolder carDisplacementYearTypePublicAdapterHolder;
    MyCarDisplacementYearTypeClickCallBack myCarDisplacementYearTypeClickCallBack;
    public CarDisplacementYearTypePublicAdapter(List<CarDisplacementYearTypePublicModel> list, Context context,MyCarDisplacementYearTypeClickCallBack myCarDisplacementYearTypeClickCallBack) {
        this.list = list;
        this.context = context;
        this.myCarDisplacementYearTypeClickCallBack=myCarDisplacementYearTypeClickCallBack;
    }

    public void clear(){
        if (list!=null){
            list.clear();
        }
        list=null;
        context=null;
        carDisplacementYearTypePublicAdapterHolder=null;
        myCarDisplacementYearTypeClickCallBack=null;

    }

    public void refresh(List<CarDisplacementYearTypePublicModel> list){
        this.list = list;
        this.notifyDataSetChanged();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_car_displacement_year_type_public_adapter,null);
            carDisplacementYearTypePublicAdapterHolder=new CarDisplacementYearTypePublicAdapterHolder();
            carDisplacementYearTypePublicAdapterHolder.name_item_displacement_year_type_public_adapter= $.f(convertView,R.id.name_item_displacement_year_type_public_adapter);
            convertView.setTag(carDisplacementYearTypePublicAdapterHolder);
        }else {
            carDisplacementYearTypePublicAdapterHolder= (CarDisplacementYearTypePublicAdapterHolder) convertView.getTag();
        }
        carDisplacementYearTypePublicAdapterHolder.name_item_displacement_year_type_public_adapter.setText(list.get(position).name);
        carDisplacementYearTypePublicAdapterHolder.name_item_displacement_year_type_public_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCarDisplacementYearTypeClickCallBack!=null){
                    myCarDisplacementYearTypeClickCallBack.onClickCar(list.get(position).id,list.get(position).name,list.get(position).logo);
                }
            }
        });
        return convertView;
    }

}
class CarDisplacementYearTypePublicAdapterHolder{
    TextView name_item_displacement_year_type_public_adapter;
}
