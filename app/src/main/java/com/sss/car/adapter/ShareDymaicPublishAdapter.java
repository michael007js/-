package com.sss.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.model.DymaicModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/25.
 */

public class ShareDymaicPublishAdapter extends BaseAdapter {
    Context context;
    List<DymaicModel> list;
    ShareDymaicPublishAdapterHolder shareDymaicPublishAdapterHolder;
    DymaicOperationCallBack shareDymaicOperationCallBack;
    LoadImageCallBack loadImageCallBack;

    public ShareDymaicPublishAdapter(Context context, List<DymaicModel> list, DymaicOperationCallBack shareDymaicOperationCallBack, LoadImageCallBack loadImageCallBack) {
        this.context = context;
        this.list = list;
        this.shareDymaicOperationCallBack = shareDymaicOperationCallBack;
        this.loadImageCallBack=loadImageCallBack;
    }

    public void refresh(List<DymaicModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<DymaicModel> list, int position, ListView listView) {
        this.list = list;
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();
        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        LogUtils.e(position + "--" + firstVisiblePosition + "--" + lastVisiblePosition);
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            getView(position, listView.getChildAt(position - firstVisiblePosition), listView);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? 0 : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_dymaic_publish_adapter, null);
            shareDymaicPublishAdapterHolder = new ShareDymaicPublishAdapterHolder();
            shareDymaicPublishAdapterHolder.click_item_share_dymaic_publish_adapter = $.f(convertView, R.id.click_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.day_item_share_dymaic_publish_adapter = $.f(convertView, R.id.day_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.month_item_share_dymaic_publish_adapter = $.f(convertView, R.id.month_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.content_item_share_dymaic_publish_adapter = $.f(convertView, R.id.content_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.time_item_share_dymaic_publish_adapter = $.f(convertView, R.id.time_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.delete_item_share_dymaic_publish_adapter = $.f(convertView, R.id.delete_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.praise_item_share_dymaic_publish_adapter = $.f(convertView, R.id.praise_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.comment_item_share_dymaic_publish_adapter = $.f(convertView, R.id.comment_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.share_item_share_dymaic_publish_adapter = $.f(convertView, R.id.share_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.praise_list_item_share_dymaic_publish_adapter = $.f(convertView, R.id.praise_list_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.nine_view_item_share_dymaic_publish_adapter = $.f(convertView, R.id.nine_view_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.listview_item_share_dymaic_publish_adapter = $.f(convertView, R.id.listview_item_share_dymaic_publish_adapter);
            shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter = $.f(convertView, R.id.bg_item_share_dymaic_publish_adapter);
            convertView.setTag(shareDymaicPublishAdapterHolder);
        } else {
            shareDymaicPublishAdapterHolder = (ShareDymaicPublishAdapterHolder) convertView.getTag();
        }
        shareDymaicPublishAdapterHolder.content_item_share_dymaic_publish_adapter.setText(list.get(position).contents);
        shareDymaicPublishAdapterHolder.time_item_share_dymaic_publish_adapter.setText(list.get(position).create_time);
        shareDymaicPublishAdapterHolder.day_item_share_dymaic_publish_adapter.setText(list.get(position).day);
        shareDymaicPublishAdapterHolder.month_item_share_dymaic_publish_adapter.setText(list.get(position).month);

        switch (list.get(position).week) {
            case "0":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_one));
                break;
            case "1":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_two));
                break;
            case "2":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_three));
                break;
            case "3":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_four));
                break;
            case "4":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_five));
                break;
            case "5":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_six));
                break;
            case "6":
                APPOftenUtils.setBackgroundOfVersion(shareDymaicPublishAdapterHolder.bg_item_share_dymaic_publish_adapter, context.getResources().getDrawable(R.drawable.bg_color_seven));
                break;
        }

        shareDymaicPublishAdapterHolder.click_item_share_dymaic_publish_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.click(position, list.get(position), list);
                }
            }
        });
        final View finalConvertView = convertView;
        shareDymaicPublishAdapterHolder.comment_item_share_dymaic_publish_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.comment(position, list.get(position), list, finalConvertView,parent);
                }
            }
        });
        shareDymaicPublishAdapterHolder.delete_item_share_dymaic_publish_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.delete(position, list.get(position), list);
                }
            }
        });
        shareDymaicPublishAdapterHolder.share_item_share_dymaic_publish_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.share(position, list.get(position), list,finalConvertView,parent);
                }
            }
        });
        shareDymaicPublishAdapterHolder.praise_item_share_dymaic_publish_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.praise(position, list.get(position), list,finalConvertView,parent);
                }
            }
        });

        shareDymaicPublishAdapterHolder.nine_view_item_share_dymaic_publish_adapter
                .isRemoveSame(false)
                .spacing(5)
                .isShowAll(false)
                .isShowCloseButton(false)
                .maxCount(9).urlList(list.get(position).picture);
        shareDymaicPublishAdapterHolder.nine_view_item_share_dymaic_publish_adapter.setNineViewShowCallBack(new NineView.NineViewShowCallBack() {
            @Override
            public void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {
                if (loadImageCallBack!=null){
                    imageView.setTag(R.id.glide_tag, Config.url + url);
                    loadImageCallBack.onLoad(GlidUtils.downLoader(false, imageView, context));
                }
            }

            @Override
            public void onDisplayImage(RatioImageView imageView,RatioImageView closeButton, String url,int parentWidth, Context context) {
                if (loadImageCallBack!=null){
                    imageView.setTag(R.id.glide_tag, Config.url + url);
                    loadImageCallBack.onLoad(GlidUtils.downLoader(false, imageView, context));
                }
            }

            @Override
            public void onClickImage(int p, String url, List<String> urlList, Context context) {
                if (shareDymaicOperationCallBack != null) {
                    shareDymaicOperationCallBack.onClickImage( position, url, urlList);
                }
            }

            @Override
            public void onClickImageColse(int position, String url, List<String> urlList, Context context) {

            }

            @Override
            public void onSamePhotos(List<String> mRejectUrlList) {

            }
        });

        return convertView;
    }
}

class ShareDymaicPublishAdapterHolder {
    FrameLayout bg_item_share_dymaic_publish_adapter;
    LinearLayout click_item_share_dymaic_publish_adapter;
    TextView content_item_share_dymaic_publish_adapter;
    TextView time_item_share_dymaic_publish_adapter;
    TextView delete_item_share_dymaic_publish_adapter;
    ImageView praise_item_share_dymaic_publish_adapter;
    ImageView comment_item_share_dymaic_publish_adapter;
    ImageView share_item_share_dymaic_publish_adapter;
    TextView month_item_share_dymaic_publish_adapter;
    TextView day_item_share_dymaic_publish_adapter;
    HorizontalListView praise_list_item_share_dymaic_publish_adapter;
    NineView nine_view_item_share_dymaic_publish_adapter;
    ListView listview_item_share_dymaic_publish_adapter;

}
