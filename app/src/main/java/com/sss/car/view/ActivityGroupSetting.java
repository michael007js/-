package com.sss.car.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.SwitchButton.SwitchButton;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.QRCodeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.WriterException;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedGroupList;
import com.sss.car.EventBusModel.ChangedGroupMember;
import com.sss.car.EventBusModel.ChangedGroupName;
import com.sss.car.EventBusModel.ExitGroup;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.GroupSettingMemberAdapter;
import com.sss.car.dao.GroupSettingMemberOperationCallBack;
import com.sss.car.model.GroupMemberModel;
import com.sss.car.model.GroupModel;
import com.sss.car.rongyun.RongYunUtils;

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
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static android.R.id.list;


/**
 * 群组设置
 * Created by leilei on 2017/8/29.
 */

@SuppressWarnings("ALL")
public class ActivityGroupSetting extends BaseActivity implements LoadImageCallBack, GroupSettingMemberOperationCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.list_activity_group_setting)
    HorizontalListView listActivityGroupSetting;
    @BindView(R.id.add_activity_group_setting)
    ImageView addActivityGroupSetting;
    @BindView(R.id.click_group_name_activity_group_setting)
    LinearLayout clickGroupNameActivityGroupSetting;
    @BindView(R.id.image_group_qr_activity_group_setting)
    ImageView imageGroupQrActivityGroupSetting;
    @BindView(R.id.click_group_qr_activity_group_setting)
    LinearLayout clickGroupQrActivityGroupSetting;
    @BindView(R.id.click_group_announcement_activity_group_setting)
    LinearLayout clickGroupAnnouncementActivityGroupSetting;
    @BindView(R.id.click_find_activity_group_setting)
    LinearLayout clickFindActivityGroupSetting;
    @BindView(R.id.switch_message_dont_disturb_activity_group_setting)
    SwitchButton switchMessageDontDisturbActivityGroupSetting;
    @BindView(R.id.click_message_dont_disturb_activity_group_setting)
    LinearLayout clickMessageDontDisturbActivityGroupSetting;
    @BindView(R.id.switch_save_activity_group_setting)
    SwitchButton switchSaveActivityGroupSetting;
    @BindView(R.id.click_my_nikename_activity_group_setting)
    LinearLayout clickMyNikenameActivityGroupSetting;
    @BindView(R.id.switch_show_nikename_activity_group_setting)
    SwitchButton switchShowNikenameActivityGroupSetting;
    @BindView(R.id.click_report_activity_group_setting)
    LinearLayout clickReportActivityGroupSetting;
    @BindView(R.id.click_clear_activity_group_setting)
    LinearLayout clickClearActivityGroupSetting;
    @BindView(R.id.click_delete_and_exit_activity_group_setting)
    TextView clickDeleteAndExitActivityGroupSetting;
    @BindView(R.id.activity_group_setting)
    LinearLayout activityGroupSetting;

    YWLoadingDialog ywLoadingDialog;
    GroupModel groupModel;

    GroupSettingMemberAdapter groupSettingMemberAdapter;

    List<GroupMemberModel> memberList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (groupSettingMemberAdapter != null) {
            groupSettingMemberAdapter.clear();
        }
        groupSettingMemberAdapter = null;
        if (memberList != null) {
            memberList.clear();
        }
        memberList = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        groupModel = null;
        backTop = null;
        titleTop = null;
        listActivityGroupSetting = null;
        addActivityGroupSetting = null;
        clickGroupNameActivityGroupSetting = null;
        imageGroupQrActivityGroupSetting = null;
        clickGroupQrActivityGroupSetting = null;
        clickGroupAnnouncementActivityGroupSetting = null;
        clickFindActivityGroupSetting = null;
        switchMessageDontDisturbActivityGroupSetting = null;
        clickMessageDontDisturbActivityGroupSetting = null;
        switchSaveActivityGroupSetting = null;
        clickMyNikenameActivityGroupSetting = null;
        switchShowNikenameActivityGroupSetting = null;
        clickReportActivityGroupSetting = null;
        clickClearActivityGroupSetting = null;
        clickDeleteAndExitActivityGroupSetting = null;
        activityGroupSetting = null;jsonArray=null;

    }
    JSONArray jsonArray=new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);
        ButterKnife.bind(this);
        customInit(activityGroupSetting, false, true, true);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        titleTop.setText("群组设置");
        addImageViewList(GlidUtils.glideLoad(false, addActivityGroupSetting, getBaseActivityContext(), R.mipmap.add_photo));
        groupSettingMemberAdapter = new GroupSettingMemberAdapter(memberList, getBaseActivityContext(), this, this);
        listActivityGroupSetting.setAdapter(groupSettingMemberAdapter);
        switchMessageDontDisturbActivityGroupSetting.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                try {
                    setGroupInfo(new JSONObject().put("shield", "1"), "群免打扰", "DontDisturb", "1",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                try {
                    setGroupInfo(new JSONObject().put("shield", "0"), "群免打扰", "DontDisturb", "0",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }
        });

        switchSaveActivityGroupSetting.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                try {
                    setGroupInfo(new JSONObject().put("records", "1"), "群保存到通讯录", "save", "1",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                try {
                    setGroupInfo(new JSONObject().put("records", "0"), "群保存到通讯录", "save", "0",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }
        });

        switchShowNikenameActivityGroupSetting.setOnStateChangedListener(new SwitchButton.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchButton view) {
                view.setOpened(true);
                try {
                    setGroupInfo(new JSONObject().put("name_show", "1"), "显示群成员昵称", "showNikename", "1",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void toggleToOff(SwitchButton view) {
                view.setOpened(false);
                try {
                    setGroupInfo(new JSONObject().put("name_show", "0"), "显示群成员昵称", "showNikename", "0",view);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
            }
        });
        getGroupInfo();
        getGroupMember(false);
        listActivityGroupSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (groupModel!=null){
                    if (Config.member_id.equals(groupModel.member_id)){
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否移除" + memberList.get(position).remark + "?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                jsonArray.put(memberList.get(position).member_id);
                                remove_member(jsonArray);
                                dialog.dismiss();
                                dialog=null;
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                                dialog=null;
                            }
                        });
                    }else {
                        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                                .putExtra("id", memberList.get(position).member_id));
                    }
                }



            }
        });
    }

    @OnClick({R.id.back_top, R.id.add_activity_group_setting, R.id.click_group_name_activity_group_setting, R.id.click_group_qr_activity_group_setting, R.id.click_group_announcement_activity_group_setting, R.id.click_find_activity_group_setting, R.id.click_my_nikename_activity_group_setting, R.id.click_report_activity_group_setting, R.id.click_clear_activity_group_setting, R.id.click_delete_and_exit_activity_group_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.add_activity_group_setting:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShareInteractionManageCreateGroup.class)
                            .putExtra("type", "invitationAddGroup")
                            .putExtra("group_id", getIntent().getExtras().getString("group_id")));
                }
                break;
            case R.id.click_group_name_activity_group_setting:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("type", "groupName")
                            .putExtra("canChange",true)
                            .putExtra("extra", groupModel.name));
                }
                break;
            case R.id.click_group_qr_activity_group_setting:
                if (groupModel==null){
                    ToastUtils.showShortToast(getBaseActivityContext(),"群信息获取中，请稍候...");
                    return;
                }
                if (getBaseActivityContext() != null) {
                    List<String> temp=new ArrayList<>();
                    temp.add(Config.url+groupModel.code);
                    startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                            .putStringArrayListExtra("data", (ArrayList<String>) temp)
                            .putExtra("current", 0));
                }
                break;
            case R.id.click_group_announcement_activity_group_setting:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("extra", groupModel.notice)
                            .putExtra("canChange", Config.member_id.equals(groupModel.member_id))
                            .putExtra("type", "groupAnnouncement"));
                }
                break;
            case R.id.click_find_activity_group_setting:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivitySearchHistoryMessage.class)
                            .putExtra("title", groupModel.name)
                            .putExtra("conversationType", getIntent().getExtras().getString("conversationType"))
                            .putExtra("group_id", getIntent().getExtras().getString("group_id"))
                    );
                }
                break;
            case R.id.click_my_nikename_activity_group_setting:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                            .putExtra("extra", groupModel.remark)
                            .putExtra("canChange", true)
                            .putExtra("type", "groupMyNikename"));
                }
                break;
            case R.id.click_report_activity_group_setting:
                startActivity(new Intent(getBaseActivityContext(), ActivityReport.class)
                        .putExtra("id", getIntent().getExtras().getString("group_id"))
                        .putExtra("type", getIntent().getExtras().getString("conversationType")));
                break;
            case R.id.click_clear_activity_group_setting:
                RongYunUtils.clearMessages(Conversation.ConversationType.GROUP, getIntent().getExtras().getString("group_id"), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "清除成功");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "error" + errorCode);
                    }
                });
                break;
            case R.id.click_delete_and_exit_activity_group_setting:
                exitGroup();
                break;
        }
    }


    /**
     * 群组成员人数被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedGroupMember event) {
        getGroupMember(true);
    }

    /**
     * 事件被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        switch (event.type) {
            case "groupName":
                try {
                    setGroupInfo(new JSONObject()
                            .put("name", event.msg), "设置群名称", "groupName", event.msg,null);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
                break;
            case "groupAnnouncement":
                try {
                    setGroupInfo(new JSONObject()
                            .put("notice", event.msg), "设置群公告", "groupAnnouncement", event.msg,null);
                } catch (JSONException e) {
                    if (getBaseActivityContext() != null) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:set group info-0");
                    }
                    e.printStackTrace();
                }
                break;
            case "groupMyNikename":
                setMyRemarkInGroup(event.msg);
                break;
        }
    }

    /**
     * 展示数据
     */
    void showData() {

        if ("1".equals(groupModel.shield)) {
            switchMessageDontDisturbActivityGroupSetting.setOpened(true);
        } else {
            switchMessageDontDisturbActivityGroupSetting.setOpened(false);
        }

        if ("1".equals(groupModel.name_show)) {
            switchShowNikenameActivityGroupSetting.setOpened(true);
        } else {
            switchShowNikenameActivityGroupSetting.setOpened(false);
        }

        if ("1".equals(groupModel.records)) {
            switchSaveActivityGroupSetting.setOpened(true);
        } else {
            switchSaveActivityGroupSetting.setOpened(false);
        }

        if (Config.member_id.equals(groupModel.member_id)) {
            clickDeleteAndExitActivityGroupSetting.setText("删除并退出");
            clickDeleteAndExitActivityGroupSetting.setVisibility(View.VISIBLE);
        } else {
            clickDeleteAndExitActivityGroupSetting.setText("退出");
            clickDeleteAndExitActivityGroupSetting.setVisibility(View.VISIBLE);
        }
        imageGroupQrActivityGroupSetting.setTag(R.id.glide_tag,Config.url+groupModel.code);
        addImageViewList(GlidUtils.downLoader(false,imageGroupQrActivityGroupSetting,getBaseActivityContext()));

        LogUtils.e(groupModel.toString());
    }

    /**
     * 退出群
     */
    JSONObject jsonObject;
    public void exitGroup() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.exitGroup(
                    new JSONObject().put("member_id", Config.member_id)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), new StringCallback() {
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
                            try {
                                  jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    RongYunUtils.removeConversation(Conversation.ConversationType.GROUP,getIntent().getExtras().getString("group_id"), new RongIMClient.ResultCallback() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    try {
                                                        EventBus.getDefault().post(new ExitGroup(getIntent().getExtras().getString("group_id")));
                                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                                        finish();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {
                                                    ToastUtils.showShortToast(getBaseActivityContext(), "error:"+errorCode);
                                                }
                                            }
                                    );

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
            e.printStackTrace();
        }
    }

    /**
     * 设置群信息
     */
    public void setGroupInfo(JSONObject jsonObject, String meaning, final String type, final String msg, final SwitchButton switchButton) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setGroupInfo(
                    jsonObject.put("member_id", Config.member_id)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), meaning, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (switchButton!=null){
                                if (switchButton.isOpened()){
                                    switchButton.setOpened(false);
                                }else {
                                    switchButton.setOpened(true);
                                }
                            }
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    switch (type) {
                                        case "groupName":
                                            groupModel.name = msg;
                                            RongYunUtils.refreshUserinfo(groupModel.group_id, msg, Uri.parse(Config.url + groupModel.picture));
                                            EventBus.getDefault().post(new ChangedGroupList());
                                            EventBus.getDefault().post(new ChangedGroupName(msg));
                                            break;
                                        case "groupAnnouncement":
                                            groupModel.notice = msg;
                                            break;
                                        case "showNikename":
                                            groupModel.name_show = msg;
                                        case "save":
                                            groupModel.records = msg;
                                        case "DontDisturb":
                                            groupModel.shield = msg;
                                            break;
                                    }
                                    getGroupInfo();
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));

                                } else {
                                    if (switchButton!=null){
                                        if (switchButton.isOpened()){
                                            switchButton.setOpened(false);
                                        }else {
                                            switchButton.setOpened(true);
                                        }
                                    }
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                if (switchButton!=null){
                                    if (switchButton.isOpened()){
                                        switchButton.setOpened(false);
                                    }else {
                                        switchButton.setOpened(true);
                                    }
                                }
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            if (switchButton!=null){
                if (switchButton.isOpened()){
                    switchButton.setOpened(false);
                }else {
                    switchButton.setOpened(true);
                }
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
            e.printStackTrace();
        }
    }

    /**
     * 设置我在群内的昵称
     */
    public void setMyRemarkInGroup(final String remark) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setMyRemarkInGroup(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("remark", remark)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), new StringCallback() {
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    groupModel.notice = remark;
                                    RongYunUtils.refreshGroupUserInfoCache(groupModel.group_id, Config.member_id, remark);
                                    getGroupInfo();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取群信息
     */
    public void getGroupInfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getGroupInfo(
                    new JSONObject().put("member_id", Config.member_id)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), new StringCallback() {
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    groupModel = null;
                                    groupModel = new GroupModel();
                                    groupModel.account = jsonObject.getJSONObject("data").getString("account");
                                    groupModel.group_id = jsonObject.getJSONObject("data").getString("group_id");
                                    groupModel.name = jsonObject.getJSONObject("data").getString("name");
                                    groupModel.code = jsonObject.getJSONObject("data").getString("code");
                                    groupModel.city_path = jsonObject.getJSONObject("data").getString("city_path");
                                    groupModel.notice = jsonObject.getJSONObject("data").getString("notice");
                                    groupModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                    groupModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    groupModel.shield = jsonObject.getJSONObject("data").getString("shield");
                                    groupModel.state = jsonObject.getJSONObject("data").getString("state");
                                    groupModel.status = jsonObject.getJSONObject("data").getString("status");
                                    groupModel.records = jsonObject.getJSONObject("data").getString("records");
                                    groupModel.name_show = jsonObject.getJSONObject("data").getString("name_show");
                                    groupModel.remark = jsonObject.getJSONObject("data").getString("remark");
                                    groupModel.picture = jsonObject.getJSONObject("data").getString("picture");
                                    showData();

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group info-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取群成员列表
     */
    public void getGroupMember(final boolean isClear) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getGroupMember(
                    new JSONObject().put("member_id", Config.member_id)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), new StringCallback() {
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (isClear) {
                                        if (jsonArray.length() > 0) {
                                            memberList.clear();
                                        }
                                    }
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        GroupMemberModel groupMemberModel = new GroupMemberModel();
                                        groupMemberModel.face = jsonArray.getJSONObject(i).getString("face");
                                        groupMemberModel.remark = jsonArray.getJSONObject(i).getString("remark");
                                        groupMemberModel.state = jsonArray.getJSONObject(i).getString("state");
                                        groupMemberModel.minute = jsonArray.getJSONObject(i).getString("minute");
                                        groupMemberModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                        groupMemberModel.group_id = jsonArray.getJSONObject(i).getString("group_id");
                                        memberList.add(groupMemberModel);
                                    }
                                    groupSettingMemberAdapter.refresh(memberList);

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group member-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group member-0");
            e.printStackTrace();
        }
    }
    /**
     * 移除群成员
     */
    public void remove_member(JSONArray member) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.remove_member(
                    new JSONObject().put("member_id", Config.member_id)
                            .put("member",member)
                            .put("group_id", getIntent().getExtras().getString("group_id"))
                            .toString(), new StringCallback() {
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    getGroupMember(true);
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group member-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:request group member-0");
            e.printStackTrace();
        }
    }
    /**
     * 图片被载入
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    /**
     * item被点击
     *
     * @param postion
     * @param memberModel
     * @param list
     */
    @Override
    public void onClickGroupMember(int postion, GroupMemberModel memberModel, List<GroupMemberModel> list) {
        if (getBaseActivityContext()!=null){
            startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                    .putExtra("id", memberModel.member_id));
        }
    }
}
