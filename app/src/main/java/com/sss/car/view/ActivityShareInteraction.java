package com.sss.car.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.PullToRefreshRecyleView.PullToRefreshRecyclerView;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessageList;
import com.sss.car.EventBusModel.ChangedMessageType;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.MessageChatListOperationCallBack;
import com.sss.car.model.MessageChatListModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.MenuDialog;

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
import io.rong.imkit.RongIM;
import io.rong.imkit.model.Event;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static com.sss.car.R.id.conversation;


/**
 * 消息==>互动==>好友聊天,交易聊天关注聊天,陌生招呼页面
 * Created by leilei on 2017/8/28.
 */

@SuppressWarnings("ALL")
public class ActivityShareInteraction extends BaseActivity implements MessageChatListOperationCallBack {
    YWLoadingDialog ywLoadingDialog;
    List<MessageChatListModel> list = new ArrayList<>();
    @BindView(R.id.listview_activity_interaction)
    PullToRefreshListView listviewActivityInteraction;
    @BindView(R.id.activity_interaction)
    LinearLayout activityInteraction;
    MenuDialog menuDialog;
    SSS_Adapter sss_rvAdapter;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    int p = 1;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_rvAdapter != null) {
            sss_rvAdapter.clear();
        }
        sss_rvAdapter = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        titleTop = null;
        listviewActivityInteraction = null;
        activityInteraction = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_interaction);
        ButterKnife.bind(this);
        customInit(activityInteraction, false, true, true);
        menuDialog = new MenuDialog(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        initAdapter();
        //1好友聊天  2交易聊天  3关注聊天  4陌生招呼
        getChatList(true);
        switch (getIntent().getExtras().getString("cate_id")) {
            case "1":
                titleTop.setText("好友聊天");
                break;
            case "2":
                titleTop.setText("交易聊天");
                break;
            case "3":
                titleTop.setText("关注聊天");
                break;
            case "4":
                titleTop.setText("其他聊天");
                break;
        }
        listviewActivityInteraction.setMode(PullToRefreshBase.Mode.BOTH);
        listviewActivityInteraction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                getChatList(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getChatList(false);
            }
        });

    }


    private void initAdapter() {
        sss_rvAdapter = new SSS_Adapter<MessageChatListModel>(getBaseActivityContext(), R.layout.item_message_chat_list_adapter) {

            @Override
            protected void setView(final SSS_HolderHelper helper, int position, final MessageChatListModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name_item_message_chat_list_adapter, list.get(position).username);
                helper.setText(R.id.content_item_message_chat_list_adapter, list.get(position).contents);
                helper.setText(R.id.time_item_message_chat_list_adapter, list.get(position).create_time);
                addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item_message_chat_list_adapter)), 99999));
                if ("1".equals(list.get(position).is_remind)) {
                    helper.setVisibility(R.id.ring_item_message_chat_list_adapter, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.ring_item_message_chat_list_adapter, View.INVISIBLE);
                }
                helper.getView(R.id.click_item_message_chat_list_adapter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RongIM.getInstance().startPrivateChat(getBaseActivityContext(), bean.member_id, bean.username);
                    }
                });
                helper.getView(R.id.click_item_message_chat_list_adapter).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }

        };
        listviewActivityInteraction.setAdapter(sss_rvAdapter);
    }

    /**
     * 设置消息已读
     */
    void close_window(final String cate_id, final String targetId) {
        String window_type = null;
        if ("5".equals(cate_id)) {
            window_type = "group";
        } else {
            window_type = "private";
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.close_window(
                    new JSONObject()
                            .put("member_pid", Config.member_id)
                            .put("window_type", window_type)
                            .put("member_id", targetId).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ywLoadingDialog = null;
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ywLoadingDialog = null;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("5".equals(cate_id)) {
                                    RongYunUtils.removeConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            getChatList(true);
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), errorCode.getMessage());
                                        }
                                    });
                                } else {
                                    RongYunUtils.removeConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            getChatList(true);
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            ToastUtils.showShortToast(getBaseActivityContext(), errorCode.getMessage());
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "解析错误err-0" + e.getMessage());
                                e.printStackTrace();
                            }

                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "解析错误err-1" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 收到拉黑好友的通知后再次请求刷新列表
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.AddToBlacklistEvent event) {
        getChatList(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedMessageList event) {
        p = 1;
        getChatList(false);
    }

    /*
     * 获取列表
     */
    public void getChatList(boolean createDialog) {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (createDialog) {
            ywLoadingDialog = null;
            if (getBaseActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                ywLoadingDialog.show();
            }
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getChatList(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("cate_id", getIntent().getExtras().getString("cate_id"))//
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
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        if (jsonArray.length() > 0) {
                                            p++;
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
                                                list.add(messageChatListModel);
                                            }
                                            sss_rvAdapter.setList(list);
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

    @Override
    public void onClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list) {
        RongIM.getInstance().startPrivateChat(getBaseActivityContext(), messageChatListModel.member_pid, messageChatListModel.username);
    }

    @Override
    public void onLongClickMessage(int position, MessageChatListModel messageChatListModel, List<MessageChatListModel> list) {

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
