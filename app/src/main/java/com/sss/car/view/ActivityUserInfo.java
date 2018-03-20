package com.sss.car.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedAttentionList;
import com.sss.car.EventBusModel.ChangedBlackList;
import com.sss.car.EventBusModel.ChangedList;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.EventBusModel.HideOrShow;
import com.sss.car.EventBusModel.Remark;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnUserInfoMenuCallBack;
import com.sss.car.fragment.FragmentCommunity_Userinfo_Posts;
import com.sss.car.fragment.FragmentUserInfoDymaic;
import com.sss.car.fragment.FragmentUserInfoReputation;
import com.sss.car.model.UserinfoModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static android.R.attr.targetId;


/**
 * Created by leilei on 2017/8/31.
 */

public class ActivityUserInfo extends BaseFragmentActivity {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.title_top_image)
    TextView titleTopImage;
    @BindView(R.id.logo_right_search_top_image)
    ImageView logoRightSearchTopImage;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.pic_activity_user_info)
    ImageView picActivityUserInfo;
    @BindView(R.id.nikename_activity_user_info)
    TextView nikenameActivityUserInfo;
    @BindView(R.id.attention_image_activity_user_info)
    ImageView attentionImageActivityUserInfo;
    @BindView(R.id.attention_text_activity_user_info)
    TextView attentionTextActivityUserInfo;
    @BindView(R.id.account_activity_user_info)
    TextView accountActivityUserInfo;
    @BindView(R.id.shop_image_activity_user_info)
    ImageView shopImageActivityUserInfo;
    @BindView(R.id.shop_text_activity_user_info)
    TextView shopTextActivityUserInfo;
    @BindView(R.id.type_activity_user_info)
    TextView typeActivityUserInfo;
    @BindView(R.id.score_activity_user_info)
    TextView scoreActivityUserInfo;
    @BindView(R.id.logon_time_activity_user_info)
    TextView logonTimeActivityUserInfo;
    @BindView(R.id.activity_user_info)
    LinearLayout activityUserInfo;
    @BindView(R.id.chat_activity_user_info)
    TextView chatActivityUserInfo;


    UserinfoModel userinfoModel;

    YWLoadingDialog ywLoadingDialog;

    FragmentUserInfoDymaic fragmentUserInfoDymaic;

    FragmentCommunity_Userinfo_Posts fragmentCommunity_userinfo_posts;

    FragmentUserInfoReputation fragmentUserInfoReputation;


    MenuDialog menuDialog;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    FragmentAdapter fragmentAdapter;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (fragmentUserInfoDymaic != null) {
            fragmentUserInfoDymaic.onDestroy();
        }
        fragmentUserInfoDymaic = null;
        if (fragmentCommunity_userinfo_posts != null) {
            fragmentCommunity_userinfo_posts.onDestroy();
        }
        fragmentCommunity_userinfo_posts = null;
        if (fragmentUserInfoReputation != null) {
            fragmentUserInfoReputation.onDestroy();
        }
        fragmentUserInfoReputation = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        userinfoModel = null;
        backTopImage = null;
        titleTopImage = null;
        logoRightSearchTopImage = null;
        rightSearchTopImage = null;
        picActivityUserInfo = null;
        nikenameActivityUserInfo = null;
        attentionImageActivityUserInfo = null;
        attentionTextActivityUserInfo = null;
        accountActivityUserInfo = null;
        shopImageActivityUserInfo = null;
        shopTextActivityUserInfo = null;
        typeActivityUserInfo = null;
        scoreActivityUserInfo = null;
        logonTimeActivityUserInfo = null;
        activityUserInfo = null;
        chatActivityUserInfo = null;

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        customInit(activityUserInfo, false, true, true, true);
        titleTopImage.setText("详细资料");
        addImageViewList(GlidUtils.glideLoad(false, logoRightSearchTopImage, getBaseActivityContext(), R.mipmap.logo_more));
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误!");
            finish();
        }
        getUserInfo();

        attentionTextActivityUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ("3".equals(userinfoModel.status)){
                        friendOperation(new JSONObject().put("friend_id", userinfoModel.member_id)
                                .put("status", "3"), "拉黑");
                    }else {
                        friendOperation(new JSONObject().put("friend_id", userinfoModel.member_id)
                                .put("status", "1"), "关心");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (Config.member_id.equals(getIntent().getExtras().getString("id"))) {
            chatActivityUserInfo.setVisibility(View.GONE);
        }
    }


    void init(){
        fragmentUserInfoDymaic = new FragmentUserInfoDymaic(getIntent().getExtras().getString("id"));
        if (Config.member_id.equals(userinfoModel.member_id)) {
            fragmentCommunity_userinfo_posts = new FragmentCommunity_Userinfo_Posts(true,true, Config.member_id, null);
        } else {
            fragmentCommunity_userinfo_posts = new FragmentCommunity_Userinfo_Posts(true, true,getIntent().getExtras().getString("id"), null);
        }
        fragmentUserInfoReputation = new FragmentUserInfoReputation(getIntent().getExtras().getString("id"));
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        String[] title = {
                "      动态      ",
                "      帖子      ",
                "      信誉      ",
        };
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        fragmentAdapter.addFragment(fragmentUserInfoDymaic);
        fragmentAdapter.addFragment(fragmentCommunity_userinfo_posts);
        fragmentAdapter.addFragment(fragmentUserInfoReputation);
        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpager);
        scrollTab.setWidth(getWindowManager().getDefaultDisplay().getWidth()/3);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });

        viewpager.setAdapter(fragmentAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        if ("from_system_message".equals(getIntent().getExtras().getString("mode"))) {
            viewpager.setCurrentItem(2);

        }
    }

    @OnClick({R.id.back_top_image, R.id.right_search_top_image, R.id.chat_activity_user_info,R.id.shop_image_activity_user_info,R.id.shop_text_activity_user_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shop_image_activity_user_info:
                if (userinfoModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "用户信息获取中,请稍候...");
                    getUserInfo();
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", userinfoModel.shop_id));
                }
                break;
            case R.id.shop_text_activity_user_info:
                if (userinfoModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "用户信息获取中,请稍候...");
                    getUserInfo();
                    return;
                }
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", userinfoModel.shop_id));
                }
                break;
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
                if (userinfoModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "用户信息获取中,请稍候...");
                    getUserInfo();
                    return;
                }
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(this);
                }
                menuDialog.createUserRightMenu(userinfoModel, getBaseActivityContext(), new OnUserInfoMenuCallBack() {
                    @Override
                    public void remark() {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityUserRemark.class)
                                    .putExtra("content", userinfoModel.remark));
                        }
                    }

                    @Override
                    public void specialAttention() {
                        try {
                            friendOperation(new JSONObject().put("friend_id", userinfoModel.member_id)
                                    .put("status", "2"), "特别关注");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void share() {

                    }

                    @Override
                    public void disturb() {
                        remind();
                    }

                    @Override
                    public void privacy() {

                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityPrivacy.class)
                                    .putExtra("friend_id", userinfoModel.member_id)
                            );
                        }
                    }

                    @Override
                    public void report() {
                        startActivity(new Intent(getBaseActivityContext(), ActivityReport.class)
                                .putExtra("id", userinfoModel.member_id)
                                .putExtra("type", "private"));
                    }

                    @Override
                    public void black() {
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定要拉黑该用户?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                try {
                                    friendOperation(new JSONObject().put("friend_id", userinfoModel.member_id)
                                            .put("status", "3"), "黑名单");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void delete() {
                        APPOftenUtils.createAskDialog(getBaseActivityContext(), "是否确定要拉黑该用户?", new OnAskDialogCallBack() {
                            @Override
                            public void onOKey(Dialog dialog) {
                                dialog.dismiss();
                                try {
                                    friendOperation(new JSONObject().put("friend_id", userinfoModel.member_id)
                                            .put("status", "4"), "删除该好友");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

                break;
            case R.id.chat_activity_user_info:
                if (userinfoModel == null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "用户信息获取中，请稍候...");
                    return;
                }
                if (StringUtils.isEmpty(userinfoModel.remark)) {
                    RongYunUtils.startConversation(getBaseActivityContext(), Conversation.ConversationType.PRIVATE,
                            getIntent().getExtras().getString("id"),
                            userinfoModel.username
                    );
                } else {
                    RongYunUtils.startConversation(getBaseActivityContext(), Conversation.ConversationType.PRIVATE,
                            getIntent().getExtras().getString("id"),
                            userinfoModel.remark
                    );
                }

                break;
        }
    }

    /**
     * 好友操作
     *
     * @param send
     * @throws JSONException
     */
    void friendOperation(final JSONObject send, String meaning) throws JSONException {

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
                        .toString(), meaning, new StringCallback() {
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
                                //status:1关注，2特别关心，3黑名单，4删除
                                if ("1".equals(jsonObject.getString("status"))) {
                                    getUserInfo();
                                    if ("1".equals(send.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedAttentionList("friend_id"));
                                    } else if ("2".equals(send.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedAttentionList("friend_id"));
                                    } else if ("3".equals(send.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedBlackList("friend_id"));
                                    } else if ("4".equals(send.getString("status"))) {
                                        EventBus.getDefault().post(new ChangedAttentionList("friend_id"));
                                        EventBus.getDefault().post(new ChangedBlackList("friend_id"));
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


    /**
     * 帖子被删除通知更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel event) {
        if (fragmentCommunity_userinfo_posts != null) {
            fragmentCommunity_userinfo_posts.p = 1;
            fragmentCommunity_userinfo_posts.communityArticle(null);
        }
    }

    /**
     * 关注或取消关注好友后通知相关界面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedAttentionList event) {
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HideOrShow event) {
        chatActivityUserInfo.setVisibility(event.state);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Remark event) {
        set_remark(event.content);
    }

    /**
     * 拉黑或取消拉黑好友后通知相关界面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedBlackList event) {
        getUserInfo();
    }

    /**
     *
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedList event) {
        getUserInfo();
    }

    /**
     * 设置好友备注
     */
    void set_remark(String remark) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.set_remark(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", userinfoModel.member_id)
                            .put("remark", remark)
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
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        getUserInfo();
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


    /**
     * 好友消息免打扰
     */
    void remind() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.remind(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", userinfoModel.member_id)
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
                            if (!StringUtils.isEmpty(response)) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        userinfoModel.stranger=jsonObject.getJSONObject("data").getString("code");
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

    /**
     * 获取用户详细资料
     */
    public void getUserInfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.trends_member(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", getIntent().getExtras().getString("id"))
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
                                    userinfoModel = new UserinfoModel(
                                            jsonObject.getJSONObject("data").getString("member_id"),
                                            jsonObject.getJSONObject("data").getString("face"),
                                            jsonObject.getJSONObject("data").getString("username"),
                                            jsonObject.getJSONObject("data").getString("is_auth"),
                                            jsonObject.getJSONObject("data").getString("credit"),
                                            jsonObject.getJSONObject("data").getString("last_time"),
                                            jsonObject.getJSONObject("data").getString("auth_type"),
                                            jsonObject.getJSONObject("data").getString("friend"),
                                            jsonObject.getJSONObject("data").getString("shop_id"),
                                            jsonObject.getJSONObject("data").getString("account"),
                                            jsonObject.getJSONObject("data").getString("is_special"),
                                            jsonObject.getJSONObject("data").getString("remark"),
                                            jsonObject.getJSONObject("data").getString("status"),//1相互关注2已关注3为拉黑状态
                                            jsonObject.getJSONObject("data").getString("sex"),
                                            jsonObject.getJSONObject("data").getString("stranger")
                                    );
                                    init();
                                    LogUtils.e(userinfoModel.toString());
                                    picActivityUserInfo.setTag(R.id.glide_tag, Config.url + userinfoModel.face);
                                    addImageViewList(GlidUtils.downLoader(true, picActivityUserInfo, getBaseActivityContext()));
                                    picActivityUserInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (getBaseActivityContext() != null) {
                                                List<String> urlList=new ArrayList<>();
                                                urlList.add(Config.url+userinfoModel.face);
                                                startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                                        .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                                                        .putExtra("current", 0));
                                            }
                                        }
                                    });
                                    attentionTextActivityUserInfo.setText(userinfoModel.friend);
                                    if (!Config.member_id.equals(userinfoModel.member_id)) {
                                        rightSearchTopImage.setVisibility(View.VISIBLE);
                                        if ("相互关注".equals(userinfoModel.friend)) {
                                            attentionTextActivityUserInfo.setVisibility(View.VISIBLE);
                                        } else if ("关注".equals(userinfoModel.friend)) {
                                            attentionTextActivityUserInfo.setVisibility(View.VISIBLE);
                                        }

                                        if ("1".equals(userinfoModel.is_special)) {
                                            attentionImageActivityUserInfo.setVisibility(View.VISIBLE);
                                        } else {
                                            attentionImageActivityUserInfo.setVisibility(View.GONE);
                                        }
                                    } else {
                                        rightSearchTopImage.setVisibility(View.INVISIBLE);
                                        attentionTextActivityUserInfo.setVisibility(View.INVISIBLE);
                                    }
                                    nikenameActivityUserInfo.setText("昵称：" + userinfoModel.username);
                                    username.setText(userinfoModel.remark);
                                    sex.setText("性别：" + userinfoModel.sex);
                                    accountActivityUserInfo.setText("用户ID：" + userinfoModel.account);

                                    if ("0".equals(userinfoModel.shop_id)) {
                                        shopImageActivityUserInfo.setVisibility(View.GONE);
                                        shopTextActivityUserInfo.setVisibility(View.GONE);
                                    } else {
                                        shopImageActivityUserInfo.setVisibility(View.VISIBLE);
                                        shopTextActivityUserInfo.setVisibility(View.VISIBLE);
                                    }

//                                    if ("1".equals(userinfoModel.stranger)){
//                                        chatActivityUserInfo.setVisibility(View.GONE);
//                                    }else {
//                                        chatActivityUserInfo.setVisibility(View.VISIBLE);
//                                    }

                                    typeActivityUserInfo.setText(userinfoModel.auth_type);
                                    scoreActivityUserInfo.setText(userinfoModel.credit);
                                    logonTimeActivityUserInfo.setText(userinfoModel.last_time);
//                                    RongYunUtils.refreshUserinfo(userinfoModel.member_id, userinfoModel.username, Uri.parse(Config.url + userinfoModel.face));
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


}
