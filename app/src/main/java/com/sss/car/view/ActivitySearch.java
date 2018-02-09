package com.sss.car.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CreateGroupFriendAttentionFansRecentlyChatPublicAdapter;
import com.sss.car.adapter.MessageChatListAdapter;
import com.sss.car.dao.CreateGroupFriendAttentionFansRecentlyChatClickCallBack;
import com.sss.car.dao.MessageChatListOperationCallBack;
import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;
import com.sss.car.model.MessageChatListModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import okhttp3.Call;



/**
 * 搜索页面
 * 消息==>互动==>互动管理==>创建新聊天==>选择联系人==>搜索
 * 消息==>互动==>互动管理==>群组==>群详情==>邀请加入群==>选择联系人==>搜索
 * Created by leilei on 2017/8/28.
 */

public class ActivitySearch extends BaseActivity implements
        MessageChatListOperationCallBack,//搜索会话操作回调
        LoadImageCallBack, //图片载入回调
        CreateGroupFriendAttentionFansRecentlyChatClickCallBack //创建群时搜索好友与邀请好友加入群时搜索好友操作回调
{
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.list_activity_search)
    RecyclerView listActivitySearch;
    @BindView(R.id.input_activity_search)
    EditText inputActivitySearch;
    @BindView(R.id.search_activity_search)
    TextView searchActivitySearch;
    @BindView(R.id.activity_search)
    LinearLayout activitySearch;
    YWLoadingDialog ywLoadingDialog;
    List<MessageChatListModel> listConversation = new ArrayList<>();
    MessageChatListAdapter messageChatListAdapter;
    List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> createGroupFriendAttentionFansRecentlyChatPublicAdapterList = new ArrayList<>();
    CreateGroupFriendAttentionFansRecentlyChatPublicAdapter createGroupFriendAttentionFansRecentlyChatPublicAdapter;



    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        listActivitySearch = null;
        activitySearch = null;
        inputActivitySearch = null;
        searchActivitySearch = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (listConversation != null) {
            listConversation.clear();
        }
        listConversation = null;
        if (messageChatListAdapter != null) {
            messageChatListAdapter.clear();
        }

        messageChatListAdapter = null;
        if (createGroupFriendAttentionFansRecentlyChatPublicAdapterList != null) {
            createGroupFriendAttentionFansRecentlyChatPublicAdapterList.clear();
        }
        createGroupFriendAttentionFansRecentlyChatPublicAdapterList = null;
        if (createGroupFriendAttentionFansRecentlyChatPublicAdapter != null) {
            createGroupFriendAttentionFansRecentlyChatPublicAdapter.clear();
        }
        createGroupFriendAttentionFansRecentlyChatPublicAdapter = null;
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        customInit(activitySearch, false, true, false);
        titleTop.setText("搜索");
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        switch (getIntent().getExtras().getString("type")) {
            case "search conversation"://搜索会话
                messageChatListAdapter = new MessageChatListAdapter(this, getBaseActivityContext(), listConversation);
                listActivitySearch.setAdapter(messageChatListAdapter);
                listActivitySearch.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
                break;
            case "createGroup"://创建群时搜索好友
                createGroupFriendAttentionFansRecentlyChatPublicAdapter = new CreateGroupFriendAttentionFansRecentlyChatPublicAdapter(createGroupFriendAttentionFansRecentlyChatPublicAdapterList, getBaseActivityContext(), this, this);
                listActivitySearch.setAdapter(createGroupFriendAttentionFansRecentlyChatPublicAdapter);
                listActivitySearch.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
                break;
            case "invitationAddGroup"://邀请好友加入群时搜索好友
                createGroupFriendAttentionFansRecentlyChatPublicAdapter = new CreateGroupFriendAttentionFansRecentlyChatPublicAdapter(createGroupFriendAttentionFansRecentlyChatPublicAdapterList, getBaseActivityContext(), this, this);
                listActivitySearch.setAdapter(createGroupFriendAttentionFansRecentlyChatPublicAdapter);
                listActivitySearch.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
                break;
        }
    }

    @OnClick({R.id.back_top, R.id.search_activity_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.search_activity_search:
                if (StringUtils.isEmpty(inputActivitySearch.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您的输入为空");
                    return;
                }
                try {
                    search();
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误");
                    e.printStackTrace();
                }
                break;
        }
    }

    void search() throws JSONException {
        switch (getIntent().getExtras().getString("type")) {
            case "search conversation"://搜索会话
                searchConversation();
                break;
            case "createGroup"://创建群时搜索好友
                switch (getIntent().getExtras().getInt("who")) {
                    case 1://好友
                        friendRelation(new JSONObject().put("type", "1"), "1");
                        break;
                    case 2://关注
                        friendRelation(new JSONObject().put("type", "2"), "2");
                        break;
                    case 3://粉丝
                        friendRelation(new JSONObject().put("type", "3"), "3");
                        break;
                    case 4://最近聊天
                        getChatList();
                        break;
                }

                break;
            case "invitationAddGroup"://邀请好友加入群时搜索好友
                switch (getIntent().getExtras().getInt("who")) {
                    case 1://好友
                        friendRelation(new JSONObject().put("type", "1"), "1");
                        break;
                    case 2://关注
                        friendRelation(new JSONObject().put("type", "2"), "2");
                        break;
                    case 3://粉丝
                        friendRelation(new JSONObject().put("type", "3"), "3");
                        break;
                    case 4://最近聊天
                        getChatList();
                        break;
                }
                break;
        }
    }

    /**
     * 搜索会话
     */
    void searchConversation() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.searchConversation(new JSONObject()
                            .put("member_id", Config.member_id)
                    .put("keywords",inputActivitySearch.getText().toString().trim())
                            .put("keywords", inputActivitySearch.getText().toString().trim())
                            .put("cate_id", getIntent().getExtras().getString("cate_id")).toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            listConversation.clear();
                                            //1好友聊天  2交易聊天  3关注聊天  4陌生招呼
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                MessageChatListModel messageChatListModel = new MessageChatListModel();
                                                messageChatListModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                                messageChatListModel.member_pid = jsonArray.getJSONObject(i).getString("member_pid");
                                                messageChatListModel.cate_id = jsonArray.getJSONObject(i).getString("cate_id");
                                                messageChatListModel.is_remind = jsonArray.getJSONObject(i).getString("is_remind");
                                                messageChatListModel.face = jsonArray.getJSONObject(i).getString("face");
                                                messageChatListModel.username = jsonArray.getJSONObject(i).getString("username");
                                                messageChatListModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                                messageChatListModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                                listConversation.add(messageChatListModel);
                                            }
                                            messageChatListAdapter.refresh(listConversation);
                                        }

                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: Search Conversation-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Search Conversation-0");
            e.printStackTrace();
        }
    }

    /**
     * 消息列表被点击
     *
     * @param position
     * @param messageChatListModel
     * @param list
     */
    @Override
    public void onClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list) {
        RongIM.getInstance().startPrivateChat(getBaseActivityContext(), messageChatListModel.member_pid, messageChatListModel.username);
    }

    /**
     * 消息列表被长按
     *
     * @param position
     * @param messageChatListModel
     * @param list
     */
    @Override
    public void onLongClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list) {

    }


    /**
     * 搜索好友,关注,粉丝
     *
     * @param send
     * @throws JSONException
     */
    void friendRelation(final JSONObject send, String meaning) throws JSONException {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friendRelation(
                send.put("member_id", Config.member_id).toString(), meaning, new StringCallback() {
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        createGroupFriendAttentionFansRecentlyChatPublicAdapterList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CreateGroupFriendAttentionFansRecentlyChatPublicModel createGroupFriendAttentionFansRecentlyChatPublicModel = new CreateGroupFriendAttentionFansRecentlyChatPublicModel();
                                            if (jsonArray.getJSONObject(i).has("face") &&
                                                    jsonArray.getJSONObject(i).has("member_id") &&
                                                    jsonArray.getJSONObject(i).has("username")
                                                    ) {
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.face = jsonArray.getJSONObject(i).getString("face");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.username = jsonArray.getJSONObject(i).getString("username");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.isChoose = false;
                                                createGroupFriendAttentionFansRecentlyChatPublicAdapterList.add(createGroupFriendAttentionFansRecentlyChatPublicModel);
                                            }

                                        }
                                        createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(createGroupFriendAttentionFansRecentlyChatPublicAdapterList);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend-0");
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }

    /**
     * 搜索最近聊天列表
     */
    void getChatList() {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }

        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }

        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getChatList(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("keywords",inputActivitySearch.getText().toString().trim())
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            createGroupFriendAttentionFansRecentlyChatPublicAdapterList.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                CreateGroupFriendAttentionFansRecentlyChatPublicModel createGroupFriendAttentionFansRecentlyChatPublicModel = new CreateGroupFriendAttentionFansRecentlyChatPublicModel();

                                                createGroupFriendAttentionFansRecentlyChatPublicModel.member_id = jsonArray.getJSONObject(i).getString("member_pid");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.face = jsonArray.getJSONObject(i).getString("face");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.username = jsonArray.getJSONObject(i).getString("username");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.isChoose = false;
                                                createGroupFriendAttentionFansRecentlyChatPublicAdapterList.add(createGroupFriendAttentionFansRecentlyChatPublicModel);
                                            }
                                            createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(createGroupFriendAttentionFansRecentlyChatPublicAdapterList);
                                        }

                                    } else {

                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: list-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: list-0");
            }
            e.printStackTrace();
        }
    }

    /**
     * 图片载入回调
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    /**
     * 创建群时搜索好友与邀请好友加入群时搜索好友操作回调
     *
     * @param isChoose
     * @param position
     * @param model
     * @param holder
     */
    @Override
    public void clickChanged(boolean isChoose, int position, CreateGroupFriendAttentionFansRecentlyChatPublicModel model, RecyclerView.ViewHolder holder) {
        createGroupFriendAttentionFansRecentlyChatPublicAdapterList.get(position).isChoose=isChoose;
        EventBus.getDefault().post(new ChooseCreateGroupFriendAttentionFansRecentlyChatPublicModel(getIntent().getExtras().getString("type"),model,isChoose));
    }
}
