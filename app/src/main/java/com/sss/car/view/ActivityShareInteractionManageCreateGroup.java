package com.sss.car.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedGroupList;
import com.sss.car.EventBusModel.ChangedGroupMember;
import com.sss.car.EventBusModel.ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.User_ID_ChooseCallBack;
import com.sss.car.fragment.FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 消息==>互动==>互动管理==>创建新聊天==>选择联系人
 * 消息==>互动==>互动管理==>群组==>群详情==>邀请加入群==>选择联系人
 * Created by leilei on 2017/8/29.
 */

public class ActivityShareInteractionManageCreateGroup extends BaseFragmentActivity implements NavMenuLayout.OnItemSelectedListener, User_ID_ChooseCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.search_activity_share_interaction_manage_create_group)
    LinearLayout searchActivityShareInteractionManageCreateGroup;
    @BindView(R.id._NavMenuLayoutactivity_share_interaction_manage_create_group)
    NavMenuLayout NavMenuLayoutactivityShareInteractionManageCreateGroup;
    @BindView(R.id.activity_share_interaction_manage_create_group)
    LinearLayout activityShareInteractionManageCreateGroup;
    /*底部导航栏文字*/
    String[] text = new String[]{"好友", "关注", "粉丝", "最近聊天"};
    /*底部导航栏未选中图标*/
    int[] unSelectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏选中图标*/
    int[] selectIcon = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    /*底部导航栏未选中颜色*/
    String unSelectColor = "#333333";
    /*底部导航栏选中颜色*/
    String selectColor = "#df3347";
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.all_choose_activity_share_interaction_manage_create_group)
    TextView allChooseActivityShareInteractionManageCreateGroup;
    @BindView(R.id.parent_activity_share_interaction_manage_create_group)
    FrameLayout parentActivityShareInteractionManageCreateGroup;
    YWLoadingDialog ywLoadingDialog;

    List<String> chooseUserId = new ArrayList<>();


    FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic friend, attention, fans, recentlyChat;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        searchActivityShareInteractionManageCreateGroup = null;
        NavMenuLayoutactivityShareInteractionManageCreateGroup = null;
        activityShareInteractionManageCreateGroup = null;
        text = null;
        unSelectColor = null;
        selectColor = null;
        rightButtonTop = null;
        allChooseActivityShareInteractionManageCreateGroup = null;
        parentActivityShareInteractionManageCreateGroup = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (friend != null) {
            friend.onDestroy();
        }
        friend = null;
        if (attention != null) {
            attention.onDestroy();
        }
        attention = null;
        if (fans != null) {
            fans.onDestroy();
        }
        fans = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_interaction_manage_create_group);
        ButterKnife.bind(this);
        customInit(activityShareInteractionManageCreateGroup, false, true, true);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        rightButtonTop.setText("下一步");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        NavMenuLayoutactivityShareInteractionManageCreateGroup
                .initMenu(4)//初始化布局
                .setIconRes(unSelectIcon)//设置未选中图标
                .setIconResSelected(selectIcon)//设置选中图标
                .setTextRes(text)//设置文字
                .setUnderhigh(5)//下方横线的高度
                .setIconSize(60, 60)//设置图标大小
                .setTextSize(13)//设置文字大小
                .setIconIsShow(false)//设置图标是否显示
                .setTextIsShow(true)//设置文字是否显示
                .setTextColor(Color.parseColor(unSelectColor))//未选中状态下文字颜色
                .setTextColorSelected(Color.parseColor(selectColor))//选中状态下文字颜色
                .setUnderIsShow(true)//是否显示下方横线
                .setUnderWidthOffset(10)//下方横线的偏移量
                .setDefaultUnderWidth(52)//下方横线的初始宽度
                .setBackColor(Color.WHITE)//设置背景色
//                .setBackColor(1,Color.RED)//设置背景色
//                .setMarginTop(PixelUtil.dpToPx(Main.this, 5))//文字和图标直接的距离，默认为5dp
                .setSelected(0)//设置选中的位置
                .setOnItemSelectedListener(this);
        titleTop.setText("选择联系人");
        friend = new FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(1, getIntent().getExtras().getString("group_id"), this);
        FragmentUtils.addFragment(getSupportFragmentManager(), friend, R.id.parent_activity_share_interaction_manage_create_group);
    }


    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel event) {
        if (event.isChoose) {
            if (!chooseUserId.contains(event.model.member_id)) {
                chooseUserId.add(event.model.member_id);
            }
        } else {
            for (int i = 0; i < chooseUserId.size(); i++) {
                if (chooseUserId.get(i).equals(event.model.member_id)) {
                    chooseUserId.remove(i);
                    break;
                }
            }
        }


        switch (NavMenuLayoutactivityShareInteractionManageCreateGroup.getCurrentPosition()) {
            case 0:
                friend.selectChanged(event.model.member_id, event.isChoose);
                break;
            case 1:
                attention.selectChanged(event.model.member_id, event.isChoose);
                break;
            case 2:
                fans.selectChanged(event.model.member_id, event.isChoose);
                break;
            case 3:
                recentlyChat.selectChanged(event.model.member_id, event.isChoose);
                break;
        }

    }


    @OnClick({R.id.back_top, R.id.right_button_top, R.id.all_choose_activity_share_interaction_manage_create_group, R.id.search_activity_share_interaction_manage_create_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_activity_share_interaction_manage_create_group:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivitySearch.class)
                            .putExtra("type", getIntent().getExtras().getString("type"))
                            .putExtra("who", NavMenuLayoutactivityShareInteractionManageCreateGroup.getCurrentPosition() + 1)
                    );
                }
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if ("createGroup".equals(getIntent().getExtras().getString("type"))) {
                    if (chooseUserId.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < chooseUserId.size(); i++) {
                            jsonArray.put(chooseUserId.get(i));
                        }
                        createGroup(jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if ("invitationAddGroup".equals(getIntent().getExtras().getString("type"))) {
                    if (chooseUserId.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < chooseUserId.size(); i++) {
                            jsonArray.put(chooseUserId.get(i));
                        }
                        invitationAddGroup(jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                }

                break;
            case R.id.all_choose_activity_share_interaction_manage_create_group:
                switch (NavMenuLayoutactivityShareInteractionManageCreateGroup.getCurrentPosition()) {
                    case 0:
                        if (friend == null || friend.createGroupFriendAttentionFansRecentlyChatPublicAdapter == null || friend.list == null) {
                            return;
                        }
                        for (int i = 0; i < friend.list.size(); i++) {
                            friend.list.get(i).isChoose = true;
                        }
                        friend.createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(friend.list);
                        break;
                    case 1:
                        if (attention == null || attention.createGroupFriendAttentionFansRecentlyChatPublicAdapter == null || attention.list == null) {
                            return;
                        }
                        for (int i = 0; i < attention.list.size(); i++) {
                            attention.list.get(i).isChoose = true;
                        }
                        attention.createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(attention.list);
                        break;
                    case 2:
                        if (fans == null || fans.createGroupFriendAttentionFansRecentlyChatPublicAdapter == null || fans.list == null) {
                            return;
                        }
                        for (int i = 0; i < fans.list.size(); i++) {
                            fans.list.get(i).isChoose = true;
                        }
                        fans.createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(fans.list);
                        break;
                    case 3:
                        if (recentlyChat == null || recentlyChat.createGroupFriendAttentionFansRecentlyChatPublicAdapter == null || recentlyChat.list == null) {
                            return;
                        }
                        for (int i = 0; i < recentlyChat.list.size(); i++) {
                            recentlyChat.list.get(i).isChoose = true;
                        }
                        recentlyChat.createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(recentlyChat.list);
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case 0://好友
                if (friend == null) {
                    friend = new FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(1, getIntent().getExtras().getString("group_id"), this);
                    FragmentUtils.addFragment(getSupportFragmentManager(), friend, R.id.parent_activity_share_interaction_manage_create_group);
                }
                FragmentUtils.hideAllShowFragment(friend);

                break;
            case 1://关注
                if (attention == null) {
                    attention = new FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(2, getIntent().getExtras().getString("group_id"), this);
                    FragmentUtils.addFragment(getSupportFragmentManager(), attention, R.id.parent_activity_share_interaction_manage_create_group);
                }
                FragmentUtils.hideAllShowFragment(attention);
                break;
            case 2://粉丝
                if (fans == null) {
                    fans = new FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(3, getIntent().getExtras().getString("group_id"), this);
                    FragmentUtils.addFragment(getSupportFragmentManager(), fans, R.id.parent_activity_share_interaction_manage_create_group);
                }
                FragmentUtils.hideAllShowFragment(fans);
                break;
            case 3://最近聊天
                if (recentlyChat == null) {
                    recentlyChat = new FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(4, getIntent().getExtras().getString("group_id"), this);
                    FragmentUtils.addFragment(getSupportFragmentManager(), recentlyChat, R.id.parent_activity_share_interaction_manage_create_group);
                }
                FragmentUtils.hideAllShowFragment(recentlyChat);
                break;
        }
    }


    @Override
    public void onChooseID(String member_id, boolean isChoose) {
        if (isChoose) {
            if (!chooseUserId.contains(member_id)) {
                chooseUserId.add(member_id);
            }
        } else {
            for (int i = 0; i < chooseUserId.size(); i++) {
                if (chooseUserId.get(i).equals(member_id)) {
                    chooseUserId.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * 邀请加群
     */
    void invitationAddGroup(JSONArray jsonArray) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.invitationAddGroup(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .put("member", jsonArray).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedGroupMember());
                                        finish();
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:invite group-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:invite group-0");
            e.printStackTrace();
        }
    }

    /**
     * 创建群
     */
    void createGroup(JSONArray jsonArray) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.createGroup(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("member", jsonArray).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedGroupList());
                                        finish();
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:create group-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:create group-0");
            e.printStackTrace();
        }
    }

}
