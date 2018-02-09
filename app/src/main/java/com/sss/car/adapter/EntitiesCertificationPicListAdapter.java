package com.sss.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.DataEntitiesCertificationPicModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/21.
 */

public class EntitiesCertificationPicListAdapter extends RecyclerView.Adapter<EntitiesCertificationPicListAdapterHolder> {
    Context context;
    LoadImageCallBack loadImageCallBack;
    EntitiesCertificationPicListAdapterHolder entitiesCertificationPicListAdapterHolder;
    List<DataEntitiesCertificationPicModel> list;
    int wAndH = 100;
    LinearLayout.LayoutParams layoutParams;

    public void refresh(List<DataEntitiesCertificationPicModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        layoutParams = null;
        context = null;
        loadImageCallBack = null;
        entitiesCertificationPicListAdapterHolder = null;
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public EntitiesCertificationPicListAdapter(Context context, int wAndH, List<DataEntitiesCertificationPicModel> list,   LoadImageCallBack loadImageCallBack) {
        this.context = context;
        this.wAndH = wAndH;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
    }

    @Override
    public EntitiesCertificationPicListAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            entitiesCertificationPicListAdapterHolder = new EntitiesCertificationPicListAdapterHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_entities_certification_pic_adapter, null));
        LogUtils.e("onCreateViewHolder");
        return entitiesCertificationPicListAdapterHolder;
    }

    @Override
    public void onBindViewHolder(EntitiesCertificationPicListAdapterHolder holder, int position) {
        layoutParams = (LinearLayout.LayoutParams) holder.pic_item_entities_certification_pic_adapter.getLayoutParams();
        layoutParams.width = wAndH;
        layoutParams.height = wAndH;
        holder.pic_item_entities_certification_pic_adapter.setLayoutParams(layoutParams);
        if (loadImageCallBack != null) {
            holder.pic_item_entities_certification_pic_adapter.setTag(R.id.glide_tag, Config.url + list.get(position).path);
            loadImageCallBack.onLoad(GlidUtils.downLoader(false, 10,holder.pic_item_entities_certification_pic_adapter, context));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class EntitiesCertificationPicListAdapterHolder extends RecyclerView.ViewHolder {
    ImageView pic_item_entities_certification_pic_adapter;

    public EntitiesCertificationPicListAdapterHolder(View itemView) {
        super(itemView);
        pic_item_entities_certification_pic_adapter = $.f(itemView, R.id.pic_item_entities_certification_pic_adapter);
    }
}