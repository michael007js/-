package com.sss.car.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.DianRuiApplicationManageHelper;
import com.blankj.utilcode.EventbusModel.SendMessageFromActivityInputKeyBoard;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.dao.Webbiz;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BadgerUtils;
import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedBlackList;
import com.sss.car.EventBusModel.ChangedChatList;
import com.sss.car.EventBusModel.ChangedDynamicList;
import com.sss.car.EventBusModel.ChangedMessage;
import com.sss.car.EventBusModel.ChangedMessageList;
import com.sss.car.EventBusModel.ChangedMessageOrderList;
import com.sss.car.EventBusModel.ChangedMessageType;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.EventBusModel.ChangedUserInfo;
import com.sss.car.EventBusModel.JiGuangModel;
import com.sss.car.EventBusModel.JumpChat;
import com.sss.car.EventBusModel.Posts;
import com.sss.car.EventBusModel.Praise;
import com.sss.car.EventBusModel.SelectCtityModel;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentGoodsParent;
import com.sss.car.fragment.FragmentMessageParent;
import com.sss.car.fragment.FragmentMy;
import com.sss.car.fragment.FragmentShare;
import com.sss.car.gaode.LocationConfig;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.CarUtils;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/15.
 */

@SuppressWarnings("ALL")
public class Main extends BaseActivity implements RongIMClient.OnReceiveMessageListener {
    @BindView(R.id.navMenuLayout_main)
    NavMenuLayout navMenuLayoutMain;
    @BindView(R.id.main)
    LinearLayout main;
    LocationConfig locationConfig;


    /*底部导航栏文字*/
    String[] text = new String[]{"车品", "车服", "消息", "分享", "我的"};
    /*底部导航栏未选中图标*/
    int[] unSelectIcon = new int[]{R.mipmap.logo_goods_tab, R.mipmap.logo_service_tab, R.mipmap.logo_message_tab, R.mipmap.logo_share_tab, R.mipmap.logo_my_tab};
    /*底部导航栏选中图标*/
    int[] selectIcon = new int[]{R.mipmap.logo_goods_tab_select, R.mipmap.logo_service_tab_select, R.mipmap.logo_message_tab_select, R.mipmap.logo_share_tab_select, R.mipmap.logo_my_tab_select};
    /*底部导航栏未选中颜色*/
    String unSelectColor = "#333333";
    /*底部导航栏选中颜色*/
    String selectColor = "#df3347";
    /*车品*/
    FragmentGoodsParent fragmentGoods;
    /*车服*/
    FragmentGoodsParent fragmentService;
    /*分享*/
    FragmentShare fragmentShare;
    /*我的*/
    FragmentMy fragmentMy;
    /*消息*/
    FragmentMessageParent fragmentMessage;
    /*消息类型1好友聊天  2交易聊天  3关注聊天  4陌生招呼*/
    String messageType = "1";

    Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP};
    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    SPUtils spUtils;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spUtils = null;
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
        }
        countDownTimerUtils = null;
        if (locationConfig != null) {
            locationConfig.release();
        }
        text = null;
        unSelectIcon = null;
        selectIcon = null;
        unSelectColor = null;
        selectColor = null;
        navMenuLayoutMain = null;
        main = null;
    }

    public void click(View view) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarUtils.startAdvertisement(getBaseActivityContext());
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        requestAPPLicense();

    }


    /**
     * 请求APP许可
     *
     * @param isLogin
     */
    public void requestAPPLicense() {
        showLoading("请稍候...");
        try {
            new RequestModel(getLocalClassName(), Webbiz.requestAPPLicense(getLocalClassName(), getBaseActivityContext(), "1", new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    dissmissLoading();
                    ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    dissmissLoading();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if ("1".equals(jsonObject.getString("status"))) {
                            Config.license = true;
//                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                            if (getIntent() != null) {
                                if (getIntent().getExtras() != null) {
                                    if ("fromSplash".equals(getIntent().getExtras().getString("where"))) {
                                        login();
                                    } else {
                                        run();
                                    }
                                } else {
                                    run();
                                }
                            } else {
                                run();
                            }
                        } else {
                            startActivity(new Intent(getBaseActivityContext(), LoginAndRegister.class));
                            ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                            finish();
                        }
                    } catch (JSONException e) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, null));

        } catch (JSONException e) {
            dissmissLoading();
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            dissmissLoading();
            ToastUtils.showShortToast(getBaseActivityContext(), "Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    void run() {
        if (StringUtils.isEmpty(Config.member_id)) {//当检测到三星之类的手机调整系统字体尺寸后Activity重启后时用户ID为空时重启
            ToastUtils.showShortToast(getBaseActivityContext(), "检测到系统异常，正在重启...");
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return;
        }
        if (StringUtils.isEmpty(Config.latitude) || StringUtils.isEmpty(Config.longitude)) {
            location();
        } else {
            init(null);
        }
    }

    public void login() {
        if (spUtils == null) {
            spUtils = new SPUtils(getBaseActivityContext(), Config.defaultFileName, Context.MODE_PRIVATE);
        }
        if (!StringUtils.isEmpty(spUtils.getString("account")) && !StringUtils.isEmpty(spUtils.getString("password"))) {
            showLoading("请稍候...");
            try {
                addRequestCall(new RequestModel(getLocalClassName(), RequestWeb.login(new JSONObject()
                        .put("device_number", DeviceUtils.getUUID(getBaseActivityContext()))
                        .put("mobile", spUtils.getString("account"))
                        .put("password", spUtils.getString("password"))
                        .toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dissmissLoading();
                        if (getBaseActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dissmissLoading();
                        try {
                            final JSONObject jsonObject = new JSONObject(response);

                            if ("1".equals(jsonObject.getString("status"))) {
                                Config.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                Config.nikename = jsonObject.getJSONObject("data").getString("username");
                                Config.face = jsonObject.getJSONObject("data").getString("face");
                                Config.flash = jsonObject.getJSONObject("data").getInt("flash") * 1000;
                                Config.mobile = jsonObject.getJSONObject("data").getString("mobile");
                                Config.account = jsonObject.getJSONObject("data").getString("account");
                                Config.token = jsonObject.getJSONObject("data").getString("token");
                                RongYunUtils.connect(Config.token, new RongIMClient.ConnectCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        run();
                                    }

                                    @Override
                                    public void onError(final RongIMClient.ErrorCode errorCode) {
                                    }

                                    @Override
                                    public void onTokenIncorrect() {
                                    }
                                });
                            } else {
                                startActivity(new Intent(getBaseActivityContext(), LoginAndRegister.class)
                                        .putExtra("isShowBack", false));
                                ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                finish();
                            }
                        } catch (JSONException e) {
                            dissmissLoading();
                            e.printStackTrace();
                        }
                    }
                })));
            } catch (JSONException e) {
                dissmissLoading();
                e.printStackTrace();
            }
        }
        spUtils = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            APPOftenUtils.createAskDialog(getBaseActivityContext(), "确认要返回桌面?", new OnAskDialogCallBack() {
                @Override
                public void onOKey(Dialog dialog) {
                    dialog.dismiss();
                    if (getBaseActivityContext() != null) {
                        APPOftenUtils.smallBackToDesktop(getBaseActivityContext());
                    }
                }

                @Override
                public void onCancel(Dialog dialog) {
                    dialog.dismiss();
                    dialog = null;
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(Long.MAX_VALUE, 60000) {
        @Override
        public void onTick(long millisUntilFinished) {
            try {
                RequestWeb.timeStatus(new JSONObject()
                        .put("gps", Config.latitude + "," + Config.longitude)
                        .put("member_id", Config.member_id).toString(), null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {

        }
    };


    @Subscribe
    public void onMessageEvent(ChangedMessage changedMessage) {
        unread(RongYunUtils.getUnreadCount(conversationTypes));
    }

    boolean isLoad = false;

    /**
     * 定位
     */
    public void location() {
        showLoading("定位中...");
        locationConfig = new LocationConfig(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LogUtils.e(aMapLocation.getErrorCode() + aMapLocation.toString());
                if ("success".equals(aMapLocation.getErrorInfo())) {
                    /*定位经纬度*/
                    Config.latitude = aMapLocation.getLatitude() + "";
                    Config.longitude = aMapLocation.getLongitude() + "";
                     /*省*/
                    Config.province = aMapLocation.getProvince();
                     /*市*/
                    String[] a = aMapLocation.getCity().split("市");
                    if (a.length > 0)
                        Config.city = a[0];
                     /*区*/
                    Config.district = aMapLocation.getDistrict();
                    /*街道*/
                    Config.address = aMapLocation.getAddress();
                    init(aMapLocation);
                    EventBus.getDefault().post(aMapLocation);
                    dissmissLoading();
                }
            }
        }, getBaseActivityContext(), LocationConfig.LocationType_Single_Positioning);

        locationConfig.start();
    }

    void init(AMapLocation aMapLocation) {
        navMenuLayoutMain.initMenu(5)//初始化布局
                .setIconRes(unSelectIcon)//设置未选中图标
                .setIconResSelected(selectIcon)//设置选中图标
                .setTextRes(text)//设置文字
                .setUnderhigh(5)//下方横线的高度
                .setIconSize(60, 60)//设置图标大小
                .setTextSize(13)//设置文字大小
                .setIconIsShow(true)//设置图标是否显示
                .setTextIsShow(true)//设置文字是否显示
                .setTextColor(Color.parseColor(unSelectColor))//未选中状态下文字颜色
                .setTextColorSelected(Color.parseColor(selectColor))//选中状态下文字颜色
                .setUnderIsShow(false)//是否显示下方横线
                .setUnderWidthOffset(10)//下方横线的偏移量
                .setDefaultUnderWidth(52)//下方横线的初始宽度
//                .setBackColor(Color.WHITE)//设置背景色
//                .setBackColor(1,Color.RED)//设置背景色
//                .setMarginTop(PixelUtil.dpToPx(Main.this, 5))//文字和图标直接的距离，默认为5dp
                .setSelected(0);//设置选中的位置
        isLoad = true;
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentGoods = new FragmentGoodsParent("1");
        fragmentService = new FragmentGoodsParent("2");
        fragmentMessage = new FragmentMessageParent();
        fragmentShare = new FragmentShare();
        fragmentMy = new FragmentMy();
        fragmentAdapter.addFragment(fragmentGoods);
        fragmentAdapter.addFragment(fragmentService);
        fragmentAdapter.addFragment(fragmentMessage);
        fragmentAdapter.addFragment(fragmentShare);
        fragmentAdapter.addFragment(fragmentMy);
        viewpagerMain.setOffscreenPageLimit(5);
        viewpagerMain.setAdapter(fragmentAdapter);
        viewpagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navMenuLayoutMain.setSelected(0);
                        break;
                    case 1:
                        navMenuLayoutMain.setSelected(1);
                        break;
                    case 2:
                        navMenuLayoutMain.setSelected(2);
                        getUnreadMessageCount();
                        break;
                    case 3:
                        navMenuLayoutMain.setSelected(3);
                        break;
                    case 4:
                        navMenuLayoutMain.setSelected(4);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setProvider();
        RongYunUtils.registerConversationExtend();
        RongIM.setOnReceiveMessageListener(this);
        customInit(main, false, false, true);
        MyApplication.initJiGuangUser(Config.account, getBaseActivityContext().getApplicationContext());
        RongYunUtils.setCurrentUserInfo(new UserInfo(Config.member_id, Config.nikename, Uri.parse(Config.url + Config.face)));
        countDownTimerUtils.start();
        navMenuLayoutMain.setOnItemSelectedListener(new NavMenuLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                viewpagerMain.setCurrentItem(position);
            }
        });
        DianRuiApplicationManageHelper.checkUpdate(getBaseActivityContext(), Config.app_id, false, true);
        RongYunUtils.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", userInfo.getUserId()));
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        unread(RongYunUtils.getUnreadCount(conversationTypes));

    }


    void getUnreadMessageCount() {
        RongYunUtils.addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                unread(i);
            }
        }, conversationTypes);
    }


    public void unread(final int unreadCount) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.unreadNumber(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    int count = unreadCount + jsonObject.getJSONObject("data").getInt("count");
                                    if (count > 0) {
                                        if (isLoad) {
                                            if (navMenuLayoutMain != null) {
                                                navMenuLayoutMain.showRedPoint(2);
                                                navMenuLayoutMain.setMsg(2, Integer.toString(count));
                                            }
                                        }

                                    } else {
                                        if (isLoad) {
                                            if (navMenuLayoutMain != null) {
                                                navMenuLayoutMain.hideAllTips(2);
                                            }
                                        }
                                        if (fragmentMessage != null) {
                                            if (fragmentMessage.topTabFragmentMessage != null) {
                                                fragmentMessage.setMessageNumber(unreadCount);
                                            }
                                        }
                                    }
                                    if (fragmentGoods != null) {
                                        fragmentGoods.setMessageNumber(count);
                                    }

                                    if (fragmentMessage != null) {
                                        fragmentMessage.setMessageNumber(jsonObject.getJSONObject("data").getString("sys"),
                                                jsonObject.getJSONObject("data").getString("comment"), jsonObject.getJSONObject("data").getString("order"));
                                    }
                                }

                                if (fragmentMessage != null) {
                                    fragmentMessage.setMessageNumber(unreadCount);
                                }


                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-2");
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
     * 设置用户信息提供者
     */
    public void setProvider() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                try {
                    RequestWeb.trends_member(new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", s)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    RongYunUtils.refreshUserinfo(jsonObject.getJSONObject("data").getString("member_id"),
                                            jsonObject.getJSONObject("data").getString("remark"),
                                            Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("face")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, true);

        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {
                ToastUtils.showShortToast(getBaseActivityContext(), s);
                try {
                    RequestWeb.getGroupInfo(new JSONObject()
                            .put("group_id", s)
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
                return null;
            }
        }, true);

    }

    String content = "";
    JSONObject jsonObject;

    @Override
    protected void onResume() {
        super.onResume();
        unread(RongYunUtils.getUnreadCount(conversationTypes));
        notif();
        if (fragmentMy != null) {
            if (fragmentMy.isLoad) {
                fragmentMy.getUserInfo(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(JiGuangModel model) {
        unread(RongYunUtils.getUnreadCount(conversationTypes));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectCtityModel model) {
        if (fragmentGoods != null) {
            if (fragmentGoods.clickLocationFragmentGoodsParent != null) {
                fragmentGoods.clickLocationFragmentGoodsParent.setText(model.city);
            }
            fragmentGoods.init();
        }

        if (fragmentService != null) {
            fragmentService.init();
        }
    }


    void notif() {
        if (fragmentMessage != null) {
            if (fragmentMessage.fragmentMessageInteraction != null) {
                fragmentMessage.fragmentMessageInteraction.getList();
            }
        }
    }


    @Override
    public boolean onReceived(Message message, int i) {
        parseMessage("onReceived", message);
        getUnreadMessageCount();
        BadgerUtils.applyCount(getBaseActivityContext(), 1);
        EventBus.getDefault().post(new ChangedMessageList());
        return false;
    }

    /**
     * 解析处理发送与接收的消息
     *
     * @param type
     * @param message
     */


    void parseMessage(String type, Message message) {
        if (navMenuLayoutMain != null) {
        }
        content = new String(message.getContent().encode()).toString();
        LogUtils.e(message.getSenderUserId() + "--" + message.getTargetId());
        message.setExtra(messageType);
            BadgerUtils.applyCount(   getBaseActivityContext(), 1);
        LogUtils.e(type + "\n" +
                "\n类型" + message.getConversationType().getName() +
                "\n消息头" + message.getObjectName() +
                "\n内容" + content +
                "\n附加消息" + message.getExtra() +
                "\n消息ID" + message.getMessageId() +
                "\n发送者ID" + message.getSenderUserId() +
                "\n目标ID" + message.getTargetId()
        );

        notif();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(JumpChat jumpChat) {
        if (navMenuLayoutMain!=null){
            navMenuLayoutMain.setSelected(2);
            if (fragmentMessage != null) {
              if (  fragmentMessage.CustomCacheViewPager!=null){
                  fragmentMessage.CustomCacheViewPager.setCurrentItem(1);
              }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedChatList changedChatList) {
        if (fragmentMessage != null) {
            if (fragmentMessage.fragmentMessageInteraction != null) {
                fragmentMessage.fragmentMessageInteraction.loadList();
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedUserInfo changedUserInfo) {
        if (fragmentMessage != null) {
            if (fragmentMessage.fragmentMessageInteraction != null) {
                fragmentMessage.fragmentMessageInteraction.loadList();
            }
            if (fragmentMessage.fragmentMessageComment != null) {
                if (fragmentMessage.fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli != null) {
                    fragmentMessage.fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli.p = 1;
                    fragmentMessage.fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli.managementOfMessageEvaluation();
                }
            }
            if (fragmentShare != null) {
                if (fragmentShare.fragmentShareDynamic != null) {
                    if (fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near != null) {
                        fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.p = 1;
                        try {
                            fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.getDymaic();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (fragmentShare.fragmentShareCommunity != null) {
                    if (fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts != null) {
                        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.p = 1;
                        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.communityArticle(null);
                    }
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageFromActivityInputKeyBoard model) {
        if ("dymaic".equals(model.type)) {
            if (fragmentShare == null || fragmentShare.fragmentShareDynamic == null || fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near == null) {
                return;
            }
            fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.p = 1;
            if (fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.isLoad) {
                if (fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.shareDynamicModel != null) {
                    fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.comment(
                            fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.shareDynamicModel, model.content, null);
                }
            }
        }
    }

    /**
     * 订单刷新
     *
     * @param changedOrderModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedOrderModel changedOrderModel) {
        if (fragmentMessage != null) {
            if (fragmentMessage.fragmentMessageOrder != null) {
                if (fragmentMessage.fragmentMessageOrder.fragmentMessageOrderPublic != null) {

                }
                fragmentMessage.fragmentMessageOrder.fragmentMessageOrderPublic.p = 1;
                fragmentMessage.fragmentMessageOrder.fragmentMessageOrderPublic.messageOrderGetOrderInfo();
            }
        }
    }

    /**
     * 帖子详情分享，评论，收藏后回调
     *
     * @param model
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Praise model) {
        if (fragmentShare == null || fragmentShare.fragmentShareDynamic == null || fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near == null) {
            return;
        }
        fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.getDymaicPraiseCommentSharenumber(model.id);
    }

    /**
     * 帖子详情分享，评论，收藏后回调
     *
     * @param model
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Posts model) {
        if (fragmentShare == null || fragmentShare.fragmentShareCommunity == null || fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts == null) {
            return;
        }
        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.getPostsPraiseCommentSharenumber(model.id);
    }

    /**
     * 聊天类型被切换回调
     * 消息类型1好友聊天  2交易聊天  3关注聊天  4陌生招呼
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedMessageType event) {
        messageType = event.type;
    }

    /**
     * 发布动态后被回调通知动态页面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedDynamicList event) {
        if (fragmentShare == null || fragmentShare.fragmentShareDynamic == null || fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near == null) {
            return;
        }
        try {
            fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.p = 1;
            fragmentShare.fragmentShareDynamic.fragment_dynamic_friend_attention_community_near.getDymaic();
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }

    }

    /**
     * 帖子被改变通知更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel event) {
        if (fragmentShare == null || fragmentShare.fragmentShareCommunity == null || fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts == null) {
            return;
        }
        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.p = 1;
        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.list.clear();
        fragmentShare.fragmentShareCommunity.fragmentCommunity_userinfo_posts.communityArticle(null);
    }

    /**
     * 消息==>订单被改变通知更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedMessageOrderList event) {
        if (fragmentMessage == null || fragmentMessage.fragmentMessageOrder == null || fragmentMessage.fragmentMessageOrder.fragmentMessageOrderPublic == null) {
            return;
        }
        fragmentMessage.fragmentMessageOrder.fragmentMessageOrderPublic.changeList();
    }

    /**
     * 拉黑好友后通知刷新会话列表
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedBlackList event) {
        RongYunUtils.clearMessages(Conversation.ConversationType.PRIVATE, event.friend_id, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtils.showShortToast(getBaseActivityContext(), "error:" + errorCode);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
