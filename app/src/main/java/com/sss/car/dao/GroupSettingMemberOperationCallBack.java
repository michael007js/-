package com.sss.car.dao;

import com.sss.car.model.GroupMemberModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/29.
 */

public interface GroupSettingMemberOperationCallBack {
    void onClickGroupMember(int postion, GroupMemberModel memberModel, List<GroupMemberModel> list);
}
