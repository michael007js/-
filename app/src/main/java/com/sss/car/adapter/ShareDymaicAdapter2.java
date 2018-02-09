package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicDetailsOperationCallBack;
import com.sss.car.dao.ShareDynamicAdapterOperationCallBack;
import com.sss.car.model.ShareDynamicModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/10.
 */

public class ShareDymaicAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int GOODS_ORDER = 0;
    private final int DYNIMAIC = 1;

    boolean isShowOnFrontPager;
    Context context;
    List<ShareDynamicModel> list;
    LoadImageCallBack loadImageCallBack;
    DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack;
    ShareDynamicAdapterOperationCallBack shareDynamicAdapterOperationCallBack;


    public void refresh(List<ShareDynamicModel> list){
        this.list=list;
        this.notifyDataSetChanged();
    }


    public void clear(){
        context=null;
        if (list!=null){
            list.clear();
        }
        list=null;
        loadImageCallBack=null;dymaicDetailsOperationCallBack=null;
    }

    public ShareDymaicAdapter2(boolean isShowOnFrontPager, Context context, List<ShareDynamicModel> list, LoadImageCallBack loadImageCallBack,
                               DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack, ShareDynamicAdapterOperationCallBack shareDynamicAdapterOperationCallBack) {
        this.isShowOnFrontPager = isShowOnFrontPager;
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
        this.dymaicDetailsOperationCallBack = dymaicDetailsOperationCallBack;
        this.shareDynamicAdapterOperationCallBack = shareDynamicAdapterOperationCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==GOODS_ORDER){
            return  new ShareDymaicAdapter2Holder(LayoutInflater.from(context).inflate(R.layout.item_share_dynamic_adapter_dynamic,parent,false));
        }else{
            return  new ShareDymaicAdapter2Holder(LayoutInflater.from(context).inflate(R.layout.item_share_dynamic_adapter_dynamic,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        switch (type) {
            case GOODS_ORDER:
                break;
            case DYNIMAIC:
                /*如果在首页显示,则评论列表,定位与点赞列表不显示*/
                if (isShowOnFrontPager) {
                    ((ShareDymaicAdapter2Holder)holder).praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                    ((ShareDymaicAdapter2Holder)holder).listview_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                    ((ShareDymaicAdapter2Holder)holder).loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                } else {
                    ((ShareDymaicAdapter2Holder)holder).praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    ((ShareDymaicAdapter2Holder)holder).listview_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    ((ShareDymaicAdapter2Holder)holder).listview_item_share_dynamic_adapter_dynamic.setAdapter(new ActivityDymaicDetailsAdapter(context,
                            list.get(position).comment_list,dymaicDetailsOperationCallBack,loadImageCallBack));

                    ((ShareDymaicAdapter2Holder)holder).loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    ((ShareDymaicAdapter2Holder)holder).loacation_item_share_dynamic_adapter_dynamic.setText(list.get(position).city_path);
                }
                ((ShareDymaicAdapter2Holder)holder).nikename_item_share_dynamic_adapter_dynamic.setText(list.get(position).username);
                ((ShareDymaicAdapter2Holder)holder).content_item_share_dynamic_adapter_dynamic.setText(list.get(position).contents);
                ((ShareDymaicAdapter2Holder)holder).time_item_share_dynamic_adapter_dynamic.setText(list.get(position).create_time);
                ((ShareDymaicAdapter2Holder)holder).show_praise_item_share_dynamic_adapter_dynamic.setText(list.get(position).likes);
                ((ShareDymaicAdapter2Holder)holder).show_comment_item_share_dynamic_adapter_dynamic.setText(list.get(position).comment_count);
                ((ShareDymaicAdapter2Holder)holder).show_share_item_share_dynamic_adapter_dynamic.setText(list.get(position).transmit);



                if (loadImageCallBack != null) {
                    loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse(Config.url + list.get(position).face), ((ShareDymaicAdapter2Holder)holder).pic_item_share_dynamic_adapter_dynamic, 0f));

                    if ("1".equals(list.get(position).is_praise)){
                        loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" +context.getPackageName()+"/"+R.mipmap.logo_praise_yes), ((ShareDymaicAdapter2Holder)holder).praise_item_share_dynamic_adapter_dynamic, 0f));
                    }else {
                        loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" +context.getPackageName()+"/"+R.mipmap.logo_praise_no), ((ShareDymaicAdapter2Holder)holder).praise_item_share_dynamic_adapter_dynamic, 0f));
                    }
                }

                if (shareDynamicAdapterOperationCallBack!=null){
                    ((ShareDymaicAdapter2Holder)holder).click_praise_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicPraise(position,list.get(position),list);
                        }
                    });
                    ((ShareDymaicAdapter2Holder)holder).click_comment_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicComment(position,list.get(position),list);
                        }
                    });
                    ((ShareDymaicAdapter2Holder)holder).click_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicShare(position,list.get(position),list);
                        }
                    });

                    ((ShareDymaicAdapter2Holder)holder).click_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamic(position,list.get(position),list);
                        }
                    });


                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        LogUtils.e(list.size());
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
       /*商品或订单*/
        if (!"".equals(list.get(position).goods_id)) {
            return GOODS_ORDER;
        } else {
            return DYNIMAIC;
        }
    }

}

class ShareDymaicAdapter2Holder extends RecyclerView.ViewHolder {
    LinearLayout click_item_share_dynamic_adapter_dynamic;
    SimpleDraweeView pic_item_share_dynamic_adapter_dynamic;
    TextView nikename_item_share_dynamic_adapter_dynamic;
    TextView content_item_share_dynamic_adapter_dynamic;
    TextView loacation_item_share_dynamic_adapter_dynamic;
    TextView time_item_share_dynamic_adapter_dynamic;
    LinearLayout click_praise_item_share_dynamic_adapter_dynamic;
    SimpleDraweeView praise_item_share_dynamic_adapter_dynamic;
    TextView show_praise_item_share_dynamic_adapter_dynamic;

    LinearLayout click_comment_item_share_dynamic_adapter_dynamic;
    SimpleDraweeView comment_item_share_dynamic_adapter_dynamic;
    TextView show_comment_item_share_dynamic_adapter_dynamic;

    LinearLayout click_share_item_share_dynamic_adapter_dynamic;
    SimpleDraweeView share_item_share_dynamic_adapter_dynamic;
    TextView show_share_item_share_dynamic_adapter_dynamic;

    HorizontalListView praise_list_item_share_dynamic_adapter_dynamic;

    ListView listview_item_share_dynamic_adapter_dynamic;

    public ShareDymaicAdapter2Holder(View itemView) {
        super(itemView);


        click_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.click_item_share_dynamic_adapter_dynamic);
        pic_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.pic_item_share_dynamic_adapter_dynamic);
        nikename_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.nikename_item_share_dynamic_adapter_dynamic);
        content_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.content_item_share_dynamic_adapter_dynamic);
        loacation_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.loacation_item_share_dynamic_adapter_dynamic);
        time_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.time_item_share_dynamic_adapter_dynamic);
        click_praise_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.click_praise_item_share_dynamic_adapter_dynamic);
        praise_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.praise_item_share_dynamic_adapter_dynamic);
        show_praise_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.show_praise_item_share_dynamic_adapter_dynamic);

        click_comment_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.click_comment_item_share_dynamic_adapter_dynamic);
        comment_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.comment_item_share_dynamic_adapter_dynamic);
        show_comment_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.show_comment_item_share_dynamic_adapter_dynamic);

        click_share_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.click_share_item_share_dynamic_adapter_dynamic);
        share_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.share_item_share_dynamic_adapter_dynamic);
        show_share_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.show_share_item_share_dynamic_adapter_dynamic);

        praise_list_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.praise_list_item_share_dynamic_adapter_dynamic);

        listview_item_share_dynamic_adapter_dynamic = $.f(itemView, R.id.listview_item_share_dynamic_adapter_dynamic);

    }
}

