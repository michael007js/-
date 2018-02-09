package com.sss.car.adapter;

import android.content.Context;
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
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.dao.GroupSettingMemberOperationCallBack;
import com.sss.car.model.GroupMemberModel;

import java.util.List;

/**
 * Created by leilei on 2017/8/29.
 */

public class GroupSettingMemberAdapter extends BaseAdapter {
    List<GroupMemberModel> list;
    Context context;
    LoadImageCallBack loadImageCallBack;
    GroupSettingMemberOperationCallBack groupSettingMemberOperationCallBack;
    GroupSettingMemberAdapterHolder groupSettingMemberAdapterHolder;

    public GroupSettingMemberAdapter(List<GroupMemberModel> list, Context context, LoadImageCallBack loadImageCallBack, GroupSettingMemberOperationCallBack groupSettingMemberOperationCallBack) {
        this.list = list;
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.groupSettingMemberOperationCallBack = groupSettingMemberOperationCallBack;
    }


    public void refresh(List<GroupMemberModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        context = null;
        loadImageCallBack = null;
        groupSettingMemberAdapterHolder = null;
        groupSettingMemberOperationCallBack = null;
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
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_setting_member_adapter, null);
            groupSettingMemberAdapterHolder = new GroupSettingMemberAdapterHolder();
            groupSettingMemberAdapterHolder.click_item_group_setting_member_adapter = $.f(convertView, R.id.click_item_group_setting_member_adapter);
            groupSettingMemberAdapterHolder.pic_item_group_setting_member_adapter = $.f(convertView, R.id.pic_item_group_setting_member_adapter);
            groupSettingMemberAdapterHolder.text_item_group_setting_member_adapter = $.f(convertView, R.id.text_item_group_setting_member_adapter);
            convertView.setTag(groupSettingMemberAdapterHolder);
        } else {
            groupSettingMemberAdapterHolder = (GroupSettingMemberAdapterHolder) convertView.getTag();
        }
        groupSettingMemberAdapterHolder.text_item_group_setting_member_adapter.setText(list.get(position).remark);
        if (groupSettingMemberOperationCallBack != null) {
            groupSettingMemberAdapterHolder.click_item_group_setting_member_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groupSettingMemberOperationCallBack.onClickGroupMember(position, list.get(position), list);
                }
            });
        }

        if (loadImageCallBack != null) {
            groupSettingMemberAdapterHolder.pic_item_group_setting_member_adapter.setTag(R.id.glide_tag, Config.url + list.get(position).face);
            loadImageCallBack.onLoad(GlidUtils.downLoader(false, groupSettingMemberAdapterHolder.pic_item_group_setting_member_adapter, context));
        }
        return convertView;
    }
}

class GroupSettingMemberAdapterHolder {
    LinearLayout click_item_group_setting_member_adapter;
    ImageView pic_item_group_setting_member_adapter;
    TextView text_item_group_setting_member_adapter;

}
