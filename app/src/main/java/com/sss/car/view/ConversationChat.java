package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedAttentionList;
import com.sss.car.EventBusModel.ChangedBlackList;
import com.sss.car.EventBusModel.ChangedGroupName;
import com.sss.car.EventBusModel.ChangedUserInfo;
import com.sss.car.EventBusModel.ExitGroup;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.UserinfoModel;
import com.sss.car.rongyun.RongYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import okhttp3.Call;

import static com.sss.car.R.id.sex;


/**
 * 会话聊天页面
 * Created by leilei on 2017/8/9.
 */

public class ConversationChat extends BaseFragmentActivity implements RongIM.OnSendMessageListener {
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.attention_conversation_chat)
    TextView attentionConversationChat;
    @BindView(R.id.add_to_black_conversation_chat)
    TextView addToBlackConversationChat;
    @BindView(R.id.layout_attention_conversation_chat)
    LinearLayout layoutAttentionConversationChat;
    @BindView(R.id.conversation_chat)
    LinearLayout conversationChat;
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.back_top_more)
    LinearLayout backTopMore;
    @BindView(R.id.setting_top_more)
    LinearLayout settingTopMore;


    String messageType;

    String targetId;
    Conversation.ConversationType conversationType;

    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(Long.MAX_VALUE, 5000) {
        @Override
        public void onTick(long millisUntilFinished) {
            read_window();
        }

        @Override
        public void onFinish() {
            read_window();
        }
    };
    @BindView(R.id.logo_right)
    SimpleDraweeView logoRight;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimerUtils.onContinue();
    }

    @Override
    protected void onPause() {
        super.onResume();
        countDownTimerUtils.onPause();
    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
        }
        countDownTimerUtils = null;
        backTopMore = null;
        titleTop = null;
        targetId = null;
        conversationType = null;
        attentionConversationChat = null;
        addToBlackConversationChat = null;
        layoutAttentionConversationChat = null;
        conversationChat = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_chat);
        ButterKnife.bind(this);
        customInit(null, false, true, true);
        RongContext.getInstance().setOnSendMessageListener(this);
        targetId = getIntent().getData().getQueryParameter("targetId");
//        if ("1".equals(targetId)){
//            settingTopMore.setVisibility(View.INVISIBLE);
//        }



        messageType = getIntent().getData().getQueryParameter("title");//客服传6，商品详情客服传2，群组传5
        if ("6".equals(messageType)||"2".equals(messageType)){
            logoRight.setVisibility(View.INVISIBLE);
        }
        //会话类型
        conversationType = Conversation.ConversationType.valueOf(getIntent().getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        switch (conversationType.getName()) {
            case "private":
                FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_user_details), logoRight, 0f);
                isAttention();
                try {
                    getUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showShortToast(getBaseActivityContext(), "解析错误" + e.getLocalizedMessage());
                }
                break;
            case "group":
                getGroupInfo();
                FrescoUtils.showImage(false, 60, 60, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_more), logoRight, 0f);
                break;
        }


        countDownTimerUtils.start();

    }

    String content;
    JSONObject jsonObject;

    @Override
    public Message onSend(Message message) {
        message.setExtra(messageType);
//        parseMessage("onSend", message);
        return message;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        message.setExtra(messageType);
        if (sentMessageErrorCode != null) {
            LogUtils.e(sentMessageErrorCode.getMessage());
        }
        parseMessage("onSent", message);
        return false;
    }

    void parseMessage(String type, Message message) {
        content = new String(message.getContent().encode()).toString();
        LogUtils.e(message.getSenderUserId() + "--" + message.getTargetId());
        message.setExtra(messageType);
        LogUtils.e(type + "\n" +
                "\n类型" + message.getConversationType().getName() +
                "\n消息头" + message.getObjectName() +
                "\n内容" + content +
                "\n附加消息" + message.getExtra() +
                "\n消息ID" + message.getMessageId() +
                "\n发送者ID" + message.getSenderUserId() +
                "\n目标ID" + message.getTargetId()
        );
        try {
            jsonObject = null;
            jsonObject = new JSONObject(content);


            switch (type) {
                case "onSent":
                    if (!StringUtils.isEmpty(message.getObjectName())) {
                        switch (message.getObjectName()) {
                            case "RC:ImgMsg":
                                LogUtils.e("ImgMsg");
                                uploadMessageInfo(new JSONObject()
                                        .put("cate_id", messageType)
                                        .put("chat_type", getType(message.getConversationType().getName()))
                                        .put("member_id", getUserId(message, type)).put("cate_id", messageType)
                                        .put("member_pid", Config.member_id).put("contents", "[图片]"));
                                break;
                            case "RC:FileMsg":
                                LogUtils.e("FileMsg");
                                uploadMessageInfo(new JSONObject()
                                        .put("cate_id", messageType)
                                        .put("chat_type", getType(message.getConversationType().getName()))
                                        .put("member_id", getUserId(message, type)).put("cate_id", messageType)
                                        .put("member_pid", Config.member_id).put("contents", "[文件]"));
                                break;
                            case "RC:TxtMsg":
                                LogUtils.e("TxtMsg");
                                if ("error".equals(EncodeUtils.getEncoding(jsonObject.getString("content"))) || "GB2312".equals(EncodeUtils.getEncoding(jsonObject.getString("content"))) || "unKnow".equals(EncodeUtils.getEncoding(jsonObject.getString("content")))) {
                                    LogUtils.e("error||GB2312||unKnow");
                                    uploadMessageInfo(new JSONObject()
                                            .put("cate_id", messageType)
                                            .put("chat_type", getType(message.getConversationType().getName()))
                                            .put("member_id", getUserId(message, type)).put("cate_id", messageType)
                                            .put("member_pid", Config.member_id).put("contents", jsonObject.getString("content")));
                                } else if ("UTF-8".equals(EncodeUtils.getEncoding(jsonObject.getString("content")))) {
                                    LogUtils.e("UTF");
                                    uploadMessageInfo(new JSONObject()
                                            .put("cate_id", messageType)
                                            .put("chat_type", getType(message.getConversationType().getName()))
                                            .put("member_id", getUserId(message, type)).put("cate_id", messageType)
                                            .put("member_pid", Config.member_id).put("contents", "[表情]"));
                                }
                                break;
                        }
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getUserId(Message message, String type) {
        if ("onReceived".equals(type)) {
            return message.getSenderUserId();
        } else {
            return message.getTargetId();
        }
    }

    /**
     * 上传消息
     *
     * @param jsonObject
     */
    void uploadMessageInfo(JSONObject jsonObject) {
        RequestWeb.uploadMessageInfo(jsonObject.toString(), null);
    }

    /**
     * 用户名被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedUserInfo event) {
        try {
            getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群名被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedGroupName event) {
        if (!StringUtils.isEmpty(event.name)) {
            titleTop.setText(event.name);
        }
    }

    /**
     * 用户退群
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitGroup event) {
        if ("group".equals(conversationType.getName()) && targetId.equals(event.group_id)) {
            finish();
        }
    }

    /**
     * 设置消息已读
     */
    void read_window() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.read_window(
                    new JSONObject()
                            .put("member_pid", Config.member_id)
                            .put("member_id", targetId).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                        }
                    })));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getType(String type) {
        if ("group".equals(type)) {
            return "2";
        } else if ("private".equals(type)) {
            return "1";
        } else {
            return "";
        }
    }

    private void getUserInfo() throws JSONException {

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.trends_member(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", targetId)
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
                                    if (titleTop!=null){
                                        titleTop.setText(jsonObject.getJSONObject("data").getString("remark"));
                                    }
                                    RongYunUtils.refreshUserinfo(targetId, jsonObject.getJSONObject("data").getString("remark"), Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("face")));

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:user info-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:user info-0");
            e.printStackTrace();
        }

    }

    private void getGroupInfo() {
        try {
            RequestWeb.getGroupInfo(new JSONObject()
                    .put("group_id", targetId)
                    .put("member_id", Config.member_id)
                    .toString(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if ("1".equals(jsonObject.getString("status"))) {
                            titleTop.setText(jsonObject.getJSONObject("data").getString("name"));
                            RongYunUtils.refreshGroupInfoCache(jsonObject.getJSONObject("data").getString("group_id"),
                                    jsonObject.getJSONObject("data").getString("name"),
                                    Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("picture")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 是否已经被对方关注
     */
    void isAttention() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.isAttention(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", targetId).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        layoutAttentionConversationChat.setVisibility(View.VISIBLE);
                                    }else {
                                        layoutAttentionConversationChat.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 好友操作
     *
     * @param send
     * @throws JSONException
     */
    void friendOperation(final JSONObject send) throws JSONException {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friendOperation(
                send
                        .put("member_id", Config.member_id)
                        .put("friend_id", targetId).toString(), "关注对方", new StringCallback() {
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
                                    switch (send.getString("status")) {
                                        case "1"://关注
                                            EventBus.getDefault().post(new ChangedAttentionList(targetId));
                                            isAttention();
                                            break;
                                        case "3"://3黑名单
                                            EventBus.getDefault().post(new ChangedBlackList(targetId));
                                            finish();
                                            break;
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-0");
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }

    @OnClick({R.id.back_top_more, R.id.setting_top_more, R.id.attention_conversation_chat, R.id.add_to_black_conversation_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_more:
                finish();
                break;
            case R.id.setting_top_more:
                if (conversationType.getName().equals("group")) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityGroupSetting.class)
                            .putExtra("group_id", targetId)
                            .putExtra("conversationType", conversationType.getName()));
                } else if (conversationType.getName().equals("private")) {
                    if (StringUtils.isEmpty(titleTop.getText().toString().trim())) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "信息获取中...");
                        return;
                    }
                    startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                            .putExtra("id", targetId));
                }

                break;
            case R.id.attention_conversation_chat:
                try {
                    friendOperation(new JSONObject()
                            .put("status", "1"));//关注
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-0");
                    e.printStackTrace();
                }
                break;
            case R.id.add_to_black_conversation_chat:
                try {
                    friendOperation(new JSONObject()
                            .put("status", "3"));//3黑名单
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误err:friend operation-0");
                    e.printStackTrace();
                }
                break;
        }
    }
}
