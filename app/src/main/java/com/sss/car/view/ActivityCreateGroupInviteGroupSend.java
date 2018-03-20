package com.sss.car.view;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedGroupList;
import com.sss.car.EventBusModel.ChangedGroupMember;
import com.sss.car.EventBusModel.ChangedList;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.fragmentUserManager;
import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import okhttp3.Call;

import static android.R.attr.targetId;

/**
 * 创建群组,邀请加群,群发
 * Created by leilei on 2017/12/15.
 */

@SuppressWarnings("ALL")
public class ActivityCreateGroupInviteGroupSend extends BaseActivity implements fragmentUserManager.OnSelectCallBack {
    public static final int send = 1;//群发
    public static final int create_group = 2;//创建群聊
    public static final int create_private = 3;//创建私聊
    public static final int invitation_Add_Group = 4;//邀请加群
    public static final int speccial_attention = 5;//特别关注
    public static final int do_not_see_target = 6;//不看TA的动态
    public static final int do_not_see_me = 7;//不让TA看我的动态
    public static final int black = 8;//黑名单
    public static final int shop_service = 9;//店铺客服
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview_activity_create_group_invite_group_send)
    HorizontalListView listviewActivityCreateGroupInviteGroupSend;
    @BindView(R.id.viewpager_activity_create_group_invite_group_send)
    ViewPager viewpagerActivityCreateGroupInviteGroupSend;
    @BindView(R.id.activity_create_group_invite_group_send)
    LinearLayout activityCreateGroupInviteGroupSend;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    YWLoadingDialog ywLoadingDialog;


    String[] title = {"    好友    ", "    关注    ", "    粉丝    ", "    最近聊天    "};
    FragmentAdapter fragmentAdapter;
    fragmentUserManager friend, fans, attention, chat;
    List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> selectList = new ArrayList<>();

    SSS_Adapter sss_adapter;
    @BindView(R.id.all)
    TextView all;
    @BindView(R.id.parent_pic)
    LinearLayout parentPic;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        if (friend != null) {
            friend.onDestroy();
        }
        friend = null;
        if (fans != null) {
            fans.onDestroy();
        }
        fans = null;
        if (attention != null) {
            attention.onDestroy();
        }
        attention = null;
        if (chat != null) {
            chat.onDestroy();
        }
        chat = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        if (selectList != null) {
            selectList.clear();
        }
        selectList = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        all = null;
        title = null;
        backTop = null;
        rightButtonTop = null;
        parentPic = null;
        titleTop = null;
        listviewActivityCreateGroupInviteGroupSend = null;
        viewpagerActivityCreateGroupInviteGroupSend = null;
        activityCreateGroupInviteGroupSend = null;
        scrollTab = null;
        super.onDestroy();
    }


    public void initAdapter() {
        sss_adapter = new SSS_Adapter<CreateGroupFriendAttentionFansRecentlyChatPublicModel>(getBaseActivityContext(), R.layout.item_pic) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CreateGroupFriendAttentionFansRecentlyChatPublicModel bean, SSS_Adapter instance) {
                LogUtils.e(Config.url + bean.face);
                FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic)), 40f);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listviewActivityCreateGroupInviteGroupSend.setAdapter(sss_adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        setContentView(R.layout.activity_create_group_invite_group_send);
        ButterKnife.bind(this);
        if (create_private == getIntent().getExtras().getInt("type")) {
            parentPic.setVisibility(View.GONE);
        } else if (send == getIntent().getExtras().getInt("type")) {
            parentPic.setVisibility(View.VISIBLE);
        } else if (create_group == getIntent().getExtras().getInt("type")) {
            parentPic.setVisibility(View.VISIBLE);
            rightButtonTop.setVisibility(View.VISIBLE);
        }


        customInit(activityCreateGroupInviteGroupSend, false, true, false);
        titleTop.setText("选择联系人");
        rightButtonTop.setText("下一步");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        initAdapter();
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);


        friend = new fragmentUserManager(create_private == getIntent().getExtras().getInt("type"),shop_service == getIntent().getExtras().getInt("type"), "1", this);
        fans = new fragmentUserManager(create_private == getIntent().getExtras().getInt("type"), shop_service == getIntent().getExtras().getInt("type"),"2", this);
        attention = new fragmentUserManager(create_private == getIntent().getExtras().getInt("type"),shop_service == getIntent().getExtras().getInt("type"), "3", this);
        chat = new fragmentUserManager(create_private == getIntent().getExtras().getInt("type"),shop_service == getIntent().getExtras().getInt("type"), "4", this);

        fragmentAdapter.addFragment(friend);
        fragmentAdapter.addFragment(attention);
        fragmentAdapter.addFragment(fans);
        fragmentAdapter.addFragment(chat);

        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpagerActivityCreateGroupInviteGroupSend);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpagerActivityCreateGroupInviteGroupSend.setCurrentItem(position);
            }
        });
        viewpagerActivityCreateGroupInviteGroupSend.setAdapter(fragmentAdapter);
        viewpagerActivityCreateGroupInviteGroupSend.setOffscreenPageLimit(4);
        viewpagerActivityCreateGroupInviteGroupSend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewpagerActivityCreateGroupInviteGroupSend.getCurrentItem()) {
                    case 0:
                        for (int i = 0; i < friend.list.size(); i++) {
                            friend.list.get(i).isChoose = true;
                        }
                        for (int i = 0; i < friend.list.size(); i++) {
                            int a = 0;
                            for (int j = 0; j < selectList.size(); j++) {
                                if (friend.list.get(i).member_id != null) {
                                    if (friend.list.get(i).member_id.equals(selectList.get(j).member_id)) {
                                        a++;
                                    }
                                }
                            }
                            if (a == 0) {
                                selectList.add(friend.list.get(i));
                            }
                        }


                        if (friend.sss_adapter != null) {
                            friend.sss_adapter.setList(friend.list);
                        }

                        break;
                    case 1:
                        for (int i = 0; i < attention.list.size(); i++) {
                            attention.list.get(i).isChoose = true;
                        }

                        for (int i = 0; i < attention.list.size(); i++) {
                            int a = 0;
                            for (int j = 0; j < selectList.size(); j++) {
                                if (StringUtils.isEmpty(attention.list.get(i).member_id)) {
                                    if (attention.list.get(i).member_id != null) {
                                        if (attention.list.get(i).member_id.equals(selectList.get(j).member_id)) {
                                            a++;
                                        }
                                    }
                                }
                            }
                            if (a == 0) {
                                selectList.add(attention.list.get(i));
                            }
                        }
                        if (attention.sss_adapter != null) {
                            attention.sss_adapter.setList(attention.list);
                        }
                        break;
                    case 2:
                        for (int i = 0; i < fans.list.size(); i++) {
                            fans.list.get(i).isChoose = true;
                        }
                        for (int i = 0; i < fans.list.size(); i++) {
                            int a = 0;
                            for (int j = 0; j < selectList.size(); j++) {
                                if (fans.list.get(i).member_id != null) {
                                    if (fans.list.get(i).member_id.equals(selectList.get(j).member_id)) {
                                        a++;
                                    }
                                }
                            }
                            if (a == 0) {
                                selectList.add(fans.list.get(i));
                            }
                        }

                        if (fans.sss_adapter != null) {
                            fans.sss_adapter.setList(fans.list);
                        }
                        break;
                    case 3:
                        for (int i = 0; i < chat.list.size(); i++) {
                            chat.list.get(i).isChoose = true;
                        }

                        for (int i = 0; i < chat.list.size(); i++) {
                            int a = 0;
                            for (int j = 0; j < selectList.size(); j++) {
                                if (chat.list.get(i).member_id != null) {
                                    if (chat.list.get(i).member_id.equals(selectList.get(j).member_id)) {
                                        a++;
                                    }
                                }
                            }
                            if (a == 0) {
                                selectList.add(chat.list.get(i));
                            }
                        }
                        if (chat.sss_adapter != null) {
                            chat.sss_adapter.setList(chat.list);
                        }
                        break;
                }
                for (int i = 0; i < selectList.size(); i++) {
                    if (selectList.get(i).member_id==null|selectList.get(i).face==null){
                        selectList.remove(i);
                    }
                }
                sss_adapter.setList(selectList);
            }
        });

    }


    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                for (int i = 0; i < selectList.size(); i++) {
                    if (selectList.get(i).member_id==null){
                        selectList.remove(i);
                    }
                }
                if (create_group == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        createGroup(jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (invitation_Add_Group == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        invitationAddGroup(jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (speccial_attention == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        set_relation("1", jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (do_not_see_target == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        set_relation("2", jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (do_not_see_me == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        set_relation("3", jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (black == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        set_relation("4", jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (shop_service == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < selectList.size(); i++) {
                            jsonArray.put(selectList.get(i).member_id);
                        }
                        set_relation("5", jsonArray);
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                    }
                } else if (send == getIntent().getExtras().getInt("type")) {
                    if (selectList.size() == 0) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "您没有选择任何人");
                        return;
                    }
                    createInputDialog();
                }

                break;
        }
    }


    void createInputDialog() {
        final Dialog dialog = new Dialog(getBaseActivityContext(), R.style.DialogStyle);
        View view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.dialog_input, null);
        final EditText input = $.f(view, R.id.input);
        TextView cancel = $.f(view, R.id.cancel);
        TextView okey = $.f(view, R.id.okey);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(input.getText().toString())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "输入不能为空");
                    return;
                }

                for (int i = 0; i < selectList.size(); i++) {
                    TextMessage textMessage = TextMessage.obtain(input.getText().toString());
                    RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, selectList.get(i).member_id, textMessage, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {
                            // 消息成功存到本地数据库的回调
                        }

                        @Override
                        public void onSuccess(Message message) {
                            // 消息发送成功的回调
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            LogUtils.e("onError" + errorCode);
                            // 消息发送失败的回调
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


    public void set_relation(String type, JSONArray jsonArray) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_relation(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", jsonArray)
                            .put("type", type)
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
                                    EventBus.getDefault().post(new ChangedList());
                                    finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
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

    @Override
    public void onSelectCallBack(CreateGroupFriendAttentionFansRecentlyChatPublicModel model) {
        if (model.isChoose) {

            for (int i = 0; i < selectList.size(); i++) {
                if (selectList.get(i).member_id != null) {
                    if (selectList.get(i).member_id.equals(model.member_id)) {
                        return;
                    }
                }
            }
            selectList.add(model);

        } else {
            for (int i = 0; i < selectList.size(); i++) {
                if (selectList.get(i).member_id != null) {
                    if (selectList.get(i).member_id.equals(model.member_id)) {
                        selectList.remove(i);
                    }
                }
            }

        }
        sss_adapter.setList(selectList);
    }

}
