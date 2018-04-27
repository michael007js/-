package com.sss.car.rongyun;

import android.content.Context;
import android.net.Uri;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.util.List;
import java.util.Map;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


/**
 * 融云相关
 * Created by leilei on 2017/5/28.
 */

@SuppressWarnings("ALL")
public class RongYunUtils {

    /**
     * 获取当前状态
     * ConnectionStatus
     * @return
     */
    public static RongIMClient.ConnectionStatusListener.ConnectionStatus getCurrentConnectionStatus(){
       return RongIM.getInstance().getCurrentConnectionStatus();
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 init(Context)之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token           从服务端获取的用户身份令牌（Token）。
     * @param connectCallback
     */
    public static void connect(String token, RongIMClient.ConnectCallback connectCallback) {
        RongIM.connect(token, connectCallback);
    }

    /**
     * <p>启动会话界面，并跳转到指定的消息位置</p>
     * <p>
     * <p>使用时，可以传入多种会话类型 {@link Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
     * <p>
     * 如果传入的是 {@link Conversation.ConversationType#CHATROOM}，sdk 会默认调用
     * <p>
     * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
     * <p>
     * 如果你的逻辑是，只允许加入已存在的聊天室，请使用接口 {@link # RongIM.getInstance().startChatRoomChat(Context, String, boolean)} 并且第三个参数为 true</p>
     *
     * @param context          应用上下文。
     * @param conversationType 会话类型。
     * @param targetId         根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
     * @param title            聊天的标题。开发者需要在聊天界面通过intent.getData().getQueryParameter("title")获取该值, 再手动设置为聊天界面的标题。
     * @param fixedMsgSentTime 需要定位的消息发送时间
     */

    public static void startConversation(Context context, Conversation.ConversationType conversationType, String targetId, String title, long fixedMsgSentTime) {
        RongIM.getInstance().startConversation(context, conversationType, targetId, title, fixedMsgSentTime);
    }


    public static void startConversation(Context context, Conversation.ConversationType conversationType, String targetId, String title){
        RongIM.getInstance().startConversation(context, conversationType,targetId,title
        );
    }

    /**
     * 根据会话类型的目标 Id，回调方式获取N条历史消息记录。
     *
     * @param conversationType 会话类型。不支持传入 ConversationType.CHATROOM。
     * @param targetId         目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
     * @param oldestMessageId  最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为:-1。
     * @param count            要获取的消息数量。
     * @param callback         获取历史消息记录的回调，按照时间顺序从新到旧排列。
     */

    public static void getHistoryMessages(Conversation.ConversationType conversationType, String targetId, int oldestMessageId, int count, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, oldestMessageId, count, callback);

    }
    /**
     * 启动会话列表界面。
     *
     * @param context               应用上下文。
     * @param supportedConversation 定义会话列表支持显示的会话类型，及对应的会话类型是否聚合显示。
     *                              例如：supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false) 非聚合式显示 private 类型的会话。
     */
    public static void startConversationList(Context context, Map<String, Boolean> supportedConversation){
        RongIM.getInstance().startConversationList(context,supportedConversation);
    }
    /**
     * 设置当前用户信息(快速实现陌生人信息显示)
     * <p>
     * 场景：A 给 B 发消息，A 和 B 不是好友。
     * 1、A、B 都要实现 RongIM.getInstance().setCurrentUserInfo(...) 接口，来设置自己的用户信息。
     * 2、A、B 设置消息内附加用户信息接口，RongIM.getInstance().setMessageAttachedUserInfo(true);
     * 3、A、B 互通消息时， 各端接收到消息后，SDK 自动会从消息中取出用户信息放入用户信息缓存中，并刷新 UI 显示。
     *
     * @param userInfo
     */
    public static void setCurrentUserInfo(UserInfo userInfo) {
        RongIM.getInstance().setCurrentUserInfo(userInfo);
    }


    /**
     * 从会话列表中删除消息
     *
     * @param conversationType
     * @param targetId
     * @param context
     * @param callback
     */
    public static void removeConversation(Conversation.ConversationType conversationType, final String targetId,  RongIMClient.ResultCallback callback) {
        RongIM.getInstance().getRongIMClient().removeConversation(conversationType, targetId, callback);
    }


    /**
     * 刷新本地缓存的用户信息
     *
     * @param id
     * @param name
     * @param portraitUri
     */
    public static void refreshUserinfo(String id, String name, Uri portraitUri) {
        if (id==null){
            return;
        }
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(id, name, portraitUri));
    }

    /**
     * 刷新本地缓存的群信息
     *
     * @param id
     * @param name
     * @param portraitUri
     */
    public static void refreshGroupInfoCache(String id, String name, Uri portraitUri) {
        RongIM.getInstance().refreshGroupInfoCache(new Group(id, name, portraitUri));
    }

    /**
     * 刷新本地缓存的群成员信息
     *
     * @param groupId
     * @param userId
     * @param nickname
     */
    public static void refreshGroupUserInfoCache(String groupId, String userId, String nickname) {
        RongIM.getInstance().refreshGroupUserInfoCache(new GroupUserInfo(groupId, userId, nickname));
    }


    /**
     * 退出登录(不启动后台push进程)
     */
    public static void logout() {
        RongIM.getInstance().logout();
    }

    /**
     * 断开连接(启动后台push进程)
     */
    public static void disconnect(boolean isPush) {
        RongIM.getInstance().disconnect(true);
    }


    /**
     * 连接监听
     *
     * @param context
     */
    public static void connectStatus(final Context context, RongIMClient.ConnectionStatusListener listener) {
        RongIM.setConnectionStatusListener(listener);
    }


    /**
     * 自定义会话扩展面板
     */
    public static void registerConversationExtend() {
        ConversationExtendProvider conversationExtendProvider = new ConversationExtendProvider();
        conversationExtendProvider.setMyExtensionModule();
        RongExtensionManager.getInstance().registerExtensionModule(conversationExtendProvider);
    }

    /**
     * 设置会话界面操作的监听器。
     *
     * @param listener 会话界面操作的监听器。
     */

    public static void setConversationBehaviorListener(RongIM.ConversationBehaviorListener listener) {
        if (RongContext.getInstance() != null) {
            RongContext.getInstance().setConversationBehaviorListener(listener);
        }
    }


    /**
     * 设置会话页面操作的监听器。
     *
     * @param listener 会话界面操作的监听器。
     */

    public static void setConversationListBehaviorListener(RongIM.ConversationListBehaviorListener listener) {
        if (RongContext.getInstance() != null) {
            RongContext.getInstance().setConversationListBehaviorListener(listener);
        }
    }


    /**
     * 注册未读消息变化监听
     *
     * @param iUnReadMessageObserver
     */
    public static void addUnReadMessageCountChangedObserver(IUnReadMessageObserver iUnReadMessageObserver, Conversation.ConversationType... conversationTypes) {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(iUnReadMessageObserver, conversationTypes);
    }

    /**
     * 解除注册未读消息变化监听
     *
     * @param iUnReadMessageObserver
     */
    public static void removeUnReadMessageCountChangedObserver(IUnReadMessageObserver iUnReadMessageObserver) {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(iUnReadMessageObserver);
    }


    /**
     * 自定义获取某一消息类型的未读消息数
     *
     * @param conversationType Conversation.ConversationType.PRIVATE  Conversation.ConversationType.GROUP  Conversation.ConversationType.SYSTEM...
     * @return
     */
    public static int getUnreadCount(Conversation.ConversationType... conversationType) {
        return RongIM.getInstance().getRongIMClient().getUnreadCount(conversationType);
    }


    /**
     * 监听收到的消息
     *
     * @param listener
     */
    public static void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener) {
        RongIM.getInstance().setOnReceiveMessageListener(listener);
    }


    /**
     * 监听发送的消息
     */
    public static void setSendMessageListener() {
        RongContext.getInstance().setOnSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                LogUtils.e("onSend\n" +
                        "\n类型" + message.getConversationType() +
                        "\n消息头" + message.getObjectName() +
                        "\n内容" + new String(message.getContent().encode()).toString() +
                        "\n附加消息" + message.getExtra() +
                        "\n消息ID" + message.getMessageId() +
                        "\n发送者ID" + message.getSenderUserId() +
                        "\n目标ID" + message.getTargetId()
                );
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                LogUtils.e("onSent\n" +
                        "\n类型" + message.getConversationType() +
                        "\n消息头" + message.getObjectName() +
                        "\n内容" + new String(message.getContent().encode()).toString() +
                        "\n附加消息" + message.getExtra() +
                        "\n消息ID" + message.getMessageId() +
                        "\n发送者ID" + message.getSenderUserId() +
                        "\n目标ID" + message.getTargetId()
                );
                return false;
            }
        });
    }


    /**
     * 清除某个会话的消息
     *
     * @param type
     * @param targetId
     * @param callback
     */
    public static void clearMessages(Conversation.ConversationType type, String targetId, RongIMClient.ResultCallback<Boolean> callback) {
        RongIM.getInstance().clearMessages(type, targetId, callback);
    }

    /**
     * 消息免打扰
     *
     * @param conversationType
     * @param targetId
     * @param notificationStatus
     * @param callback
     */
    public static void setConversationNotificationStatus(Conversation.ConversationType conversationType, String targetId, Conversation.ConversationNotificationStatus notificationStatus, RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus> callback) {
        RongIM.getInstance().getRongIMClient().setConversationNotificationStatus(conversationType, targetId, notificationStatus, callback);
    }


    /**
     * 聊天置顶
     *
     * @param type
     * @param id
     * @param isTop
     * @param callback
     */
    public static void setConversationToTop(Conversation.ConversationType type, String id, boolean isTop, RongIMClient.ResultCallback<Boolean> callback) {
        RongIM.getInstance().getRongIMClient().setConversationToTop(type, id, isTop, callback);
    }

    /**
     * 设置消息通知免打扰
     */
    public static void setConversationNotificationState(Conversation.ConversationType conversationType, String targetId, Conversation.ConversationNotificationStatus notificationStatus, RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus> callback) {
        RongIM.getInstance().getRongIMClient().setConversationNotificationStatus(conversationType, targetId, notificationStatus, callback);
    }

    /**
     * 设置消息通知免打扰时间
     */
    public static void setNotificationQuietHours(RongIMClient.OperationCallback callback) {
        RongIM.getInstance().getRongIMClient().setNotificationQuietHours(TimeUtils.millis2String(System.currentTimeMillis(), "HH:mm:ss"), 1439, callback);
    }
    /**
     * 设置消息通知免打扰时间
     * @param spanMinutes   设置的免打扰结束时间距离起始时间的间隔分钟数。 0 < spanMinutes < 1440。 比如，设置的起始时间是 00：00， 结束时间为 23：59，则 spanMinutes 为 23 * 60 + 59 = 1339 分钟。
     * @param callback
     */
    public static void setNotificationQuietHours(String startTime,int spanMinutes,RongIMClient.OperationCallback callback) {
        RongIM.getInstance().getRongIMClient().setNotificationQuietHours(startTime, spanMinutes, callback);
    }

    /**
     * 取消消息通知免打扰
     *
     * @param callback
     */
    public static void removeNotificationQuietHours(RongIMClient.OperationCallback callback) {
        RongIM.getInstance().getRongIMClient().removeNotificationQuietHours(callback);
    }

    /**
     * 拉黑
     *
     * @param userId
     * @param callback
     */
    public static void addToBlacklist(String userId, RongIMClient.OperationCallback callback) {
        RongIM.getInstance().getRongIMClient().addToBlacklist(userId, callback);
    }

    /**
     * 从黑名单移除
     *
     * @param userId
     * @param callback
     */
    public static void removeFromBlacklist(String userId, RongIMClient.OperationCallback callback) {
        RongIM.getInstance().getRongIMClient().removeFromBlacklist(userId, callback);
    }

    /**
     * 查询是否在黑名单中
     *
     * @param userId
     * @param callback
     */
    public static void getBlacklistStatus(String userId, RongIMClient.ResultCallback<RongIMClient.BlacklistStatus> callback) {
        RongIM.getInstance().getRongIMClient().getBlacklistStatus(userId, callback);
    }

    /**
     * 将某一个会话的消息设为已读
     * @param conversationType
     * @param targetId
     * @param callback
     */
    public static void clearMessagesUnreadStatus(final Conversation.ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Boolean> callback){
        RongIM.getInstance().clearMessagesUnreadStatus(conversationType,targetId,callback);
    }
}