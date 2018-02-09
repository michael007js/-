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

import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.Community_Userinfo_PostsOperationCallBack;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.model.Community_Userinfo_Posts_Model;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leilei on 2017/9/4.
 */

public class Community_Userinfo_PostsAdapter extends BaseAdapter {
    Context context;
    List<Community_Userinfo_Posts_Model> list;
    LoadImageCallBack loadImageCallBack;
    NineAdapter2OperationCallBack nineAdapter2OperationCallBack;
    Community_Userinfo_PostsOperationCallBack community_userinfo_postsOperationCallBack;
    boolean isShowType;

    List<NineAdapter2> nineAdapter2List = new ArrayList<>();

    /*isShowType:控制是否显示type_name_item_community_userinfo_posts_adapter与arrows_item_community_userinfo_posts_adapter,
    * true显示,false不显示(热门)*/
    public Community_Userinfo_PostsAdapter(boolean isShowType, Context context,
                                           List<Community_Userinfo_Posts_Model> list,
                                           LoadImageCallBack loadImageCallBack,
                                           NineAdapter2OperationCallBack nineAdapter2OperationCallBack,
                                           Community_Userinfo_PostsOperationCallBack community_userinfo_postsOperationCallBack) {
        this.isShowType = isShowType;
        this.context = context;
        this.list = list;
        this.loadImageCallBack = loadImageCallBack;
        this.nineAdapter2OperationCallBack = nineAdapter2OperationCallBack;
        this.community_userinfo_postsOperationCallBack = community_userinfo_postsOperationCallBack;
    }

    public void refresh(List<Community_Userinfo_Posts_Model> list) {
        this.list = list;
        this.notifyDataSetChanged();

    }

    public void updateItem(int position, List<Community_Userinfo_Posts_Model> list, ListView listView) {
        this.list = list;
        this.notifyDataSetChanged();

    }

    public void clear() {
        if (nineAdapter2List != null) {
            for (int i = 0; i < nineAdapter2List.size(); i++) {
                nineAdapter2List.get(i).clear();
            }
        }
        nineAdapter2List.clear();
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        loadImageCallBack = null;
        nineAdapter2OperationCallBack = null;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : list.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Community_Userinfo_PostsAdapterHolder community_userinfo_postsAdapterHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community_userinfo_posts_adapter, null);
            community_userinfo_postsAdapterHolder = new Community_Userinfo_PostsAdapterHolder();
            community_userinfo_postsAdapterHolder.click_item_community_userinfo_posts_adapter = $.f(convertView, R.id.click_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.pic_item_community_userinfo_posts_adapter = $.f(convertView, R.id.pic_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.nikename_item_community_userinfo_posts_adapter = $.f(convertView, R.id.nikename_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.car_name_item_community_userinfo_posts_adapter = $.f(convertView, R.id.car_name_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.date_name_item_community_userinfo_posts_adapter = $.f(convertView, R.id.date_name_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter = $.f(convertView, R.id.type_name_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.title_item_community_userinfo_posts_adapter = $.f(convertView, R.id.title_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.nine_item_community_userinfo_posts_adapter = $.f(convertView, R.id.nine_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.click_collect_item_community_userinfo_posts_adapter = $.f(convertView, R.id.click_collect_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.number_collect_item_community_userinfo_posts_adapter = $.f(convertView, R.id.number_collect_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.click_comment_item_community_userinfo_posts_adapter = $.f(convertView, R.id.click_comment_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.number_comment_item_community_userinfo_posts_adapter = $.f(convertView, R.id.number_comment_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.click_share_item_community_userinfo_posts_adapter = $.f(convertView, R.id.click_share_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.number_share_item_community_userinfo_posts_adapter = $.f(convertView, R.id.number_share_item_community_userinfo_posts_adapter);

            community_userinfo_postsAdapterHolder.recommend_item_community_userinfo_posts_adapter = $.f(convertView, R.id.recommend_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.hot_item_community_userinfo_posts_adapter = $.f(convertView, R.id.hot_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.essence_item_community_userinfo_posts_adapter = $.f(convertView, R.id.essence_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.collect_item_community_userinfo_posts_adapter = $.f(convertView, R.id.collect_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.arrows_item_community_userinfo_posts_adapter = $.f(convertView, R.id.arrows_item_community_userinfo_posts_adapter);
            community_userinfo_postsAdapterHolder.logo_share = $.f(convertView, R.id.logo_share);
            convertView.setTag(community_userinfo_postsAdapterHolder);
        } else {
            community_userinfo_postsAdapterHolder = (Community_Userinfo_PostsAdapterHolder) convertView.getTag();
        }


        if ("1".equals(list.get(position).is_collect)) {

            if (loadImageCallBack != null) {
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_collect), community_userinfo_postsAdapterHolder.logo_share, 0f));
            }
            community_userinfo_postsAdapterHolder.collect_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
        } else {
            if (loadImageCallBack != null) {
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_collect_no), community_userinfo_postsAdapterHolder.logo_share, 0f));

            }
            community_userinfo_postsAdapterHolder.collect_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
        }

        if ("1".equals(list.get(position).is_essence)) {
            community_userinfo_postsAdapterHolder.essence_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
        } else {
            community_userinfo_postsAdapterHolder.essence_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
        }
        if ("1".equals(list.get(position).is_hot)) {
            community_userinfo_postsAdapterHolder.hot_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
        } else {
            community_userinfo_postsAdapterHolder.hot_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
        }

        if ("1".equals(list.get(position).is_top)) {
            community_userinfo_postsAdapterHolder.recommend_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
        } else {
            community_userinfo_postsAdapterHolder.recommend_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
        }


        community_userinfo_postsAdapterHolder.nikename_item_community_userinfo_posts_adapter.setText(list.get(position).username);
        community_userinfo_postsAdapterHolder.car_name_item_community_userinfo_posts_adapter.setText(list.get(position).vehicle_name);
        community_userinfo_postsAdapterHolder.date_name_item_community_userinfo_posts_adapter.setText(list.get(position).create_time);
        community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter.setText(list.get(position).cate_name);
        community_userinfo_postsAdapterHolder.title_item_community_userinfo_posts_adapter.setText(list.get(position).title);
        community_userinfo_postsAdapterHolder.number_collect_item_community_userinfo_posts_adapter.setText(list.get(position).collect_count);
        community_userinfo_postsAdapterHolder.number_comment_item_community_userinfo_posts_adapter.setText(list.get(position).comment_count);
        community_userinfo_postsAdapterHolder.number_share_item_community_userinfo_posts_adapter.setText(list.get(position).share);
        if (list.get(position).picture.size() > 0) {
            NineAdapter2 nineAdapter2 = new NineAdapter2(300, 300, list.get(position).picture, context, loadImageCallBack, nineAdapter2OperationCallBack);
            nineAdapter2List.add(nineAdapter2);
            community_userinfo_postsAdapterHolder.nine_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
            community_userinfo_postsAdapterHolder.nine_item_community_userinfo_posts_adapter.setAdapter(nineAdapter2);
        }else {
            community_userinfo_postsAdapterHolder.nine_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
        }

        if (loadImageCallBack != null) {
            loadImageCallBack.onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + list.get(position).face), community_userinfo_postsAdapterHolder.pic_item_community_userinfo_posts_adapter, 99999));
        }

        if (community_userinfo_postsOperationCallBack != null) {
            if (isShowType) {
                community_userinfo_postsAdapterHolder.arrows_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
                community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter.setVisibility(View.VISIBLE);
                community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        community_userinfo_postsOperationCallBack.onType(list.get(position), list, position);
                    }
                });
            } else {
                community_userinfo_postsAdapterHolder.arrows_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
                community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter.setVisibility(View.GONE);
                community_userinfo_postsAdapterHolder.type_name_item_community_userinfo_posts_adapter.setOnClickListener(null);


            }


            community_userinfo_postsAdapterHolder.click_collect_item_community_userinfo_posts_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    community_userinfo_postsOperationCallBack.onCollection(list.get(position), list, position);
                }
            });
            community_userinfo_postsAdapterHolder.click_comment_item_community_userinfo_posts_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    community_userinfo_postsOperationCallBack.onComment(list.get(position), list, position);
                }
            });
            community_userinfo_postsAdapterHolder.click_share_item_community_userinfo_posts_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    community_userinfo_postsOperationCallBack.onShare(list.get(position), list, position);
                }
            });
            community_userinfo_postsAdapterHolder.click_item_community_userinfo_posts_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    community_userinfo_postsOperationCallBack.onClickItem(list.get(position), list, position);
                }
            });
        }

        return convertView;
    }
}

class Community_Userinfo_PostsAdapterHolder {

    LinearLayout click_item_community_userinfo_posts_adapter;
    SimpleDraweeView pic_item_community_userinfo_posts_adapter;
    SimpleDraweeView logo_share;
    TextView nikename_item_community_userinfo_posts_adapter;
    TextView car_name_item_community_userinfo_posts_adapter;
    TextView date_name_item_community_userinfo_posts_adapter;
    TextView type_name_item_community_userinfo_posts_adapter;
    TextView title_item_community_userinfo_posts_adapter;
    InnerGridView nine_item_community_userinfo_posts_adapter;

    LinearLayout click_collect_item_community_userinfo_posts_adapter;
    TextView number_collect_item_community_userinfo_posts_adapter;

    LinearLayout click_comment_item_community_userinfo_posts_adapter;
    TextView number_comment_item_community_userinfo_posts_adapter;

    LinearLayout click_share_item_community_userinfo_posts_adapter;
    TextView number_share_item_community_userinfo_posts_adapter;

    TextView recommend_item_community_userinfo_posts_adapter;
    TextView hot_item_community_userinfo_posts_adapter;
    TextView essence_item_community_userinfo_posts_adapter;
    TextView collect_item_community_userinfo_posts_adapter;
    SimpleDraweeView arrows_item_community_userinfo_posts_adapter;

}
