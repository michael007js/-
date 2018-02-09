package com.sss.car.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.MyCarOperationCallBack;
import com.sss.car.model.CarListModel;

import java.util.List;



/**
 * Created by leilei on 2017/8/17.
 */

public class CarListAdapter extends BaseAdapter {
    List<CarListModel> carList;
    Context context;
    MyCarOperationCallBack myCarOperationCallBack;
    LoadImageCallBack loadImageCallBack;
    ContentCarListAdapterHolder contentCarListAdapterHolder;
    public void clear() {
        if (carList != null) {
            carList.clear();
        }
        carList = null;
        context = null;
        myCarOperationCallBack = null;
        loadImageCallBack = null;
        contentCarListAdapterHolder=null;
    }

    public CarListAdapter(List<CarListModel> carList, Context context, MyCarOperationCallBack myCarOperationCallBack, LoadImageCallBack loadImageCallBack) {
        this.carList = carList;
        this.context = context;
        this.myCarOperationCallBack = myCarOperationCallBack;
        this.loadImageCallBack = loadImageCallBack;
    }

    public void refresh(List<CarListModel> carList) {
        this.carList=carList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return carList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car_list_adapter_content, null);
            contentCarListAdapterHolder = new ContentCarListAdapterHolder();
            contentCarListAdapterHolder.logo_item_car_list_adapter_content = $.f(convertView, R.id.logo_item_car_list_adapter_content);
            contentCarListAdapterHolder.name_item_car_list_adapter_content = $.f(convertView, R.id.name_item_car_list_adapter_content);
            contentCarListAdapterHolder.bg_item_car_list_adapter_content=$.f(convertView,R.id.bg_item_car_list_adapter_content);
            contentCarListAdapterHolder.line_item_car_list_adapter_content=$.f(convertView,R.id.line_item_car_list_adapter_content);
            convertView.setTag(contentCarListAdapterHolder);
        } else {
            contentCarListAdapterHolder = (ContentCarListAdapterHolder) convertView.getTag();
        }


        if (!StringUtils.isEmpty(carList.get(position).title)) {
            contentCarListAdapterHolder.name_item_car_list_adapter_content.setText(carList.get(position).title);
            contentCarListAdapterHolder.bg_item_car_list_adapter_content.setBackgroundColor(Color.parseColor("#f3f3f3"));
            contentCarListAdapterHolder.logo_item_car_list_adapter_content.setVisibility(View.GONE);
            contentCarListAdapterHolder.line_item_car_list_adapter_content.setVisibility(View.GONE);
        } else if (!StringUtils.isEmpty(carList.get(position).name)) {
            contentCarListAdapterHolder.logo_item_car_list_adapter_content.setVisibility(View.VISIBLE);
            contentCarListAdapterHolder.line_item_car_list_adapter_content.setVisibility(View.VISIBLE);
            contentCarListAdapterHolder.bg_item_car_list_adapter_content.setBackgroundColor(Color.WHITE);
            contentCarListAdapterHolder.name_item_car_list_adapter_content.setText(carList.get(position).name);
            if (loadImageCallBack != null) {
                contentCarListAdapterHolder.logo_item_car_list_adapter_content.setTag(R.id.glide_tag, Config.url + carList.get(position).logo);
                loadImageCallBack.onLoad(GlidUtils.downLoader(false, contentCarListAdapterHolder.logo_item_car_list_adapter_content, context));
            }
            contentCarListAdapterHolder.name_item_car_list_adapter_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myCarOperationCallBack != null) {
                        myCarOperationCallBack.clickCarFromHotListOrCarList("carList", carList.get(position).brand_id, carList.get(position).logo, carList.get(position).name);
                    }
                }
            });

        }
        return convertView;
    }

}


class ContentCarListAdapterHolder {
    ImageView logo_item_car_list_adapter_content;
    TextView name_item_car_list_adapter_content;
    LinearLayout bg_item_car_list_adapter_content;
    TextView line_item_car_list_adapter_content;

}