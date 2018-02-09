package com.sss.car.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by leilei on 2017/9/9.
 */

public class ShareDynamicAdapter extends BaseAdapter {
    private final int GOODS_ORDER = 0;
    private final int DYNIMAIC = 1;

    boolean canRefresh;
    boolean isShowOnFrontPager;
    Context context;
    List<ShareDynamicModel> list;
    LoadImageCallBack loadImageCallBack;
    DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack;
    ShareDynamicAdapterOperationCallBack shareDynamicAdapterOperationCallBack;


    public ShareDynamicAdapter(boolean isShowOnFrontPager, Context context, List<ShareDynamicModel> list, LoadImageCallBack loadImageCallBack,
                               DymaicDetailsOperationCallBack dymaicDetailsOperationCallBack,
                               ShareDynamicAdapterOperationCallBack shareDynamicAdapterOperationCallBack) {
        this.isShowOnFrontPager = isShowOnFrontPager;
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
        this.dymaicDetailsOperationCallBack = dymaicDetailsOperationCallBack;
        this.shareDynamicAdapterOperationCallBack = shareDynamicAdapterOperationCallBack;
    }

    public void canRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void refresh(List<ShareDynamicModel> list) {
        this.list = list;
       this.notifyDataSetChanged();

    }

    public void clear() {
        context = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        loadImageCallBack = null;
        dymaicDetailsOperationCallBack = null;
    }

    @Override
    public int getCount() {
        LogUtils.e(list.size());
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
LogUtils.e("getView");
        ShareDynamicAdapterHoderDynamic shareDynamicAdapterHoderDynamic = null;
        ShareDynamicAdapterHoderGoodsAndOrder shareDynamicAdapterHoderGoodsAndOrder;
        int type = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case GOODS_ORDER:
                    shareDynamicAdapterHoderGoodsAndOrder = new ShareDynamicAdapterHoderGoodsAndOrder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_share_dynamic_adapter_dynamic, null);
                    convertView.setTag(shareDynamicAdapterHoderGoodsAndOrder);
                    break;
                case DYNIMAIC:
                    shareDynamicAdapterHoderDynamic = new ShareDynamicAdapterHoderDynamic();

                    convertView = LayoutInflater.from(context).inflate(R.layout.item_share_dynamic_adapter_dynamic, null);
                    shareDynamicAdapterHoderDynamic.click_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.click_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.pic_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.pic_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.nikename_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.nikename_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.content_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.content_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.loacation_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.loacation_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.time_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.time_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.click_praise_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.click_praise_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.praise_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.praise_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.show_praise_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.show_praise_item_share_dynamic_adapter_dynamic);

                    shareDynamicAdapterHoderDynamic.click_comment_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.click_comment_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.comment_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.comment_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.show_comment_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.show_comment_item_share_dynamic_adapter_dynamic);

                    shareDynamicAdapterHoderDynamic.click_share_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.click_share_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.share_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.share_item_share_dynamic_adapter_dynamic);
                    shareDynamicAdapterHoderDynamic.show_share_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.show_share_item_share_dynamic_adapter_dynamic);

                    shareDynamicAdapterHoderDynamic.praise_list_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.praise_list_item_share_dynamic_adapter_dynamic);

                    shareDynamicAdapterHoderDynamic.listview_item_share_dynamic_adapter_dynamic = $.f(convertView, R.id.listview_item_share_dynamic_adapter_dynamic);
                    convertView.setTag(shareDynamicAdapterHoderDynamic);
                    break;
            }

        } else {
            switch (type) {
                case GOODS_ORDER:
                    shareDynamicAdapterHoderGoodsAndOrder = (ShareDynamicAdapterHoderGoodsAndOrder) convertView.getTag();
                    break;
                case DYNIMAIC:
                    shareDynamicAdapterHoderDynamic = (ShareDynamicAdapterHoderDynamic) convertView.getTag();
                    break;
            }

        }
        switch (type) {
            case GOODS_ORDER:
                break;
            case DYNIMAIC:
                /*如果在首页显示,则评论列表,定位与点赞列表不显示*/
                if (isShowOnFrontPager) {
                    shareDynamicAdapterHoderDynamic.praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                    shareDynamicAdapterHoderDynamic.listview_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                    shareDynamicAdapterHoderDynamic.loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                } else {
                    shareDynamicAdapterHoderDynamic.praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    shareDynamicAdapterHoderDynamic.listview_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    shareDynamicAdapterHoderDynamic.listview_item_share_dynamic_adapter_dynamic.setAdapter(new ActivityDymaicDetailsAdapter(context,
                            list.get(position).comment_list, dymaicDetailsOperationCallBack, loadImageCallBack));

                    shareDynamicAdapterHoderDynamic.loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                    shareDynamicAdapterHoderDynamic.loacation_item_share_dynamic_adapter_dynamic.setText(list.get(position).city_path);
                }
                shareDynamicAdapterHoderDynamic.nikename_item_share_dynamic_adapter_dynamic.setText(list.get(position).username);
                shareDynamicAdapterHoderDynamic.content_item_share_dynamic_adapter_dynamic.setText(list.get(position).contents);
                shareDynamicAdapterHoderDynamic.time_item_share_dynamic_adapter_dynamic.setText(list.get(position).create_time);
                shareDynamicAdapterHoderDynamic.show_praise_item_share_dynamic_adapter_dynamic.setText(list.get(position).likes);
                shareDynamicAdapterHoderDynamic.show_comment_item_share_dynamic_adapter_dynamic.setText(list.get(position).comment_count);
                shareDynamicAdapterHoderDynamic.show_share_item_share_dynamic_adapter_dynamic.setText(list.get(position).transmit);


                if (loadImageCallBack != null) {
                    loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse(Config.url + list.get(position).face), shareDynamicAdapterHoderDynamic.pic_item_share_dynamic_adapter_dynamic, 0f));

                    if ("1".equals(list.get(position).is_praise)) {
                        loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_praise_yes), shareDynamicAdapterHoderDynamic.praise_item_share_dynamic_adapter_dynamic, 0f));
                    } else {
                        loadImageCallBack.onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_praise_no), shareDynamicAdapterHoderDynamic.praise_item_share_dynamic_adapter_dynamic, 0f));
                    }
                }

                if (shareDynamicAdapterOperationCallBack != null) {
                    shareDynamicAdapterHoderDynamic.click_praise_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicPraise(position, list.get(position), list);
                        }
                    });
                    shareDynamicAdapterHoderDynamic.click_comment_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicComment(position, list.get(position), list);
                        }
                    });
                    shareDynamicAdapterHoderDynamic.click_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamicShare(position, list.get(position), list);
                        }
                    });

                    shareDynamicAdapterHoderDynamic.click_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareDynamicAdapterOperationCallBack.onClickDynamic(position, list.get(position), list);
                        }
                    });


                }

                break;

        }

        return convertView;
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}

class ShareDynamicAdapterHoderDynamic {
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


}

class ShareDynamicAdapterHoderGoodsAndOrder {


}
