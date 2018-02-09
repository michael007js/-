package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.R;
import com.sss.car.dao.CertificationServiceClickCallBack;
import com.sss.car.model.EntitiesCertificationServiceModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/22.
 */

public class CertificationServiceAdapter extends RecyclerView.Adapter<CertificationServiceAdapterHolder> {
    List<EntitiesCertificationServiceModel> list;
    CertificationServiceClickCallBack certificationServiceClickCallBack;
    Context context;
    CertificationServiceAdapterHolder certificationServiceAdapterHolder;
    LoadImageCallBack loadImageCallBack;

    public CertificationServiceAdapter(List<EntitiesCertificationServiceModel> list,LoadImageCallBack loadImageCallBack, CertificationServiceClickCallBack certificationServiceClickCallBack, Context context) {
        this.list = list;
        this.loadImageCallBack=loadImageCallBack;
        this.certificationServiceClickCallBack = certificationServiceClickCallBack;
        this.context = context;
    }

    public void refresh(List<EntitiesCertificationServiceModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        certificationServiceClickCallBack = null;
        context = null;
    }

    @Override
    public CertificationServiceAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        certificationServiceAdapterHolder =
                new CertificationServiceAdapterHolder(LayoutInflater.from(context).inflate(R.layout.item_certification_service_adapter, null));
        return certificationServiceAdapterHolder;
    }

    @Override
    public void onBindViewHolder(final CertificationServiceAdapterHolder holder, final int position) {
        if ("1".equals(list.get(position).is_check)) {
            if (loadImageCallBack!=null){
                loadImageCallBack.onLoad(GlidUtils.glideLoad(false,holder.pic_item_certification_service_adapter,context,R.mipmap.yes));
            }
        } else {
            if (loadImageCallBack!=null){
                loadImageCallBack.onLoad(GlidUtils.glideLoad(false,holder.pic_item_certification_service_adapter,context,R.mipmap.yes_un));
            }
        }

        holder.text_item_certification_service_adapter.setText(list.get(position).name);
        holder.click_item_certification_service_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (certificationServiceClickCallBack != null) {
                    certificationServiceClickCallBack.onClickChange( position, list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CertificationServiceAdapterHolder extends RecyclerView.ViewHolder {
    LinearLayout click_item_certification_service_adapter;
    ImageView pic_item_certification_service_adapter;
    TextView text_item_certification_service_adapter;

    public CertificationServiceAdapterHolder(View itemView) {
        super(itemView);
        click_item_certification_service_adapter = $.f(itemView, R.id.click_item_certification_service_adapter);
        pic_item_certification_service_adapter = $.f(itemView, R.id.pic_item_certification_service_adapter);
        text_item_certification_service_adapter = $.f(itemView, R.id.text_item_certification_service_adapter);
    }
}
