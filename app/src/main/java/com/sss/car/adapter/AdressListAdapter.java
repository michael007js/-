package com.sss.car.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.sss.car.R;
import com.sss.car.dao.AdressListClickCallBack;
import com.sss.car.model.AddressInfoModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/19.
 */

public class AdressListAdapter extends BaseAdapter {
    List<AddressInfoModel> list;
    Context context;
    AdressListClickCallBack adressListClickCallBack;
    LoadImageCallBack loadImageCallBack;
    AdressListAdapterHolder adressListAdapterHolder;

    public AdressListAdapter(List<AddressInfoModel> list, Context context, AdressListClickCallBack adressListClickCallBack,LoadImageCallBack loadImageCallBack) {
        this.list = list;
        this.context = context;
        this.adressListClickCallBack = adressListClickCallBack;
        this.loadImageCallBack=loadImageCallBack;
    }

    public void refresh(List<AddressInfoModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        adressListAdapterHolder = null;
        list = null;
        context = null;
        adressListClickCallBack = null;
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
            adressListAdapterHolder = new AdressListAdapterHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_list_adapter, null);
            adressListAdapterHolder.name_item_address_list_adapter= $.f(convertView,R.id.name_item_address_list_adapter);
            adressListAdapterHolder.mobile_item_address_list_adapter= $.f(convertView,R.id.mobile_item_address_list_adapter);
            adressListAdapterHolder.address_item_address_list_adapter= $.f(convertView,R.id.address_item_address_list_adapter);
            adressListAdapterHolder.default_item_address_list_adapter= $.f(convertView,R.id.default_item_address_list_adapter);
            adressListAdapterHolder.edit_item_address_list_adapter= $.f(convertView,R.id.edit_item_address_list_adapter);
            adressListAdapterHolder.delete_item_address_list_adapter= $.f(convertView,R.id.delete_item_address_list_adapter);
            adressListAdapterHolder.default_logo_item_address_list_adapter= $.f(convertView,R.id.default_logo_item_address_list_adapter);
            convertView.setTag(adressListAdapterHolder);
        }else {
            adressListAdapterHolder= (AdressListAdapterHolder) convertView.getTag();
        }
        adressListAdapterHolder.name_item_address_list_adapter.setText(list.get(position).recipients);
        adressListAdapterHolder.address_item_address_list_adapter.setText(list.get(position).address);
        adressListAdapterHolder.mobile_item_address_list_adapter.setText(list.get(position).mobile);
        if ("1".equals(list.get(position).is_default)){
            adressListAdapterHolder.default_item_address_list_adapter.setText("默认地址");
            adressListAdapterHolder.default_item_address_list_adapter.setTextColor(context.getResources().getColor(R.color.mainColor));
            adressListAdapterHolder.default_item_address_list_adapter.setOnClickListener(null);
            if (loadImageCallBack!=null){
                loadImageCallBack.onLoad(GlidUtils.glideLoad(false,adressListAdapterHolder.default_logo_item_address_list_adapter,context,R.mipmap.yes));
            }
        }else {
            adressListAdapterHolder.default_item_address_list_adapter.setText("设为默认");
            adressListAdapterHolder.default_item_address_list_adapter.setTextColor(Color.parseColor("#383838"));
            if (loadImageCallBack!=null){
                loadImageCallBack.onLoad(GlidUtils.glideLoad(false,adressListAdapterHolder.default_logo_item_address_list_adapter,context,R.mipmap.yes_un));
            }
            adressListAdapterHolder.default_item_address_list_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adressListClickCallBack!=null){
                        adressListClickCallBack.onDefault(list.get(position));
                    }
                }
            });
        }
        adressListAdapterHolder.edit_item_address_list_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adressListClickCallBack!=null){
                    adressListClickCallBack.onEdit(list.get(position));
                }
            }
        });
        adressListAdapterHolder.delete_item_address_list_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adressListClickCallBack!=null){
                    adressListClickCallBack.onDelete(list.get(position));
                }
            }
        });
        return convertView;
    }
}

class AdressListAdapterHolder {
    TextView name_item_address_list_adapter;
    TextView mobile_item_address_list_adapter;
    TextView address_item_address_list_adapter;
    TextView default_item_address_list_adapter;
    TextView edit_item_address_list_adapter;
    TextView delete_item_address_list_adapter;
    ImageView default_logo_item_address_list_adapter;

}
