package com.sss.car.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ZhiFuBaoPasswordStyle.PassWordKeyboard;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedScoreModel;
import com.sss.car.EventBusModel.CouponChange;
import com.sss.car.P;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnPayPasswordVerificationCallBack;
import com.sss.car.fragment.FragmentWalletIntegralSendFriendAttentionFansChat;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.scrollTab;

/**
 * 我的钱包==>积分/优惠券选择赠与人
 * Created by leilei on 2017/10/25.
 */

public class ActivityWalletIntegral_And_CouponSend extends BaseActivity implements  FragmentWalletIntegralSendFriendAttentionFansChat.OnSelectUserCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.next_activity_wallet_integral_send)
    TextView nextActivityWalletIntegralSend;
    @BindView(R.id.activity_wallet_integral_send)
    LinearLayout activityWalletIntegralSend;

    FragmentWalletIntegralSendFriendAttentionFansChat friend;
    FragmentWalletIntegralSendFriendAttentionFansChat attention;
    FragmentWalletIntegralSendFriendAttentionFansChat fans;
    FragmentWalletIntegralSendFriendAttentionFansChat chat;
    String member_id/*好友ID*/, face/*好友头像*/, username/*好友昵称*/;
    YWLoadingDialog ywLoadingDialog;
    MenuDialog menuDialog;
    @BindView(scrollTab)
    ScrollTab ScrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentAdapter!=null){
            fragmentAdapter.clear();
        }
        fragmentAdapter=null;
        ScrollTab = null;
        viewpager = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (friend != null) {
            friend.onDestroy();
        }
        friend = null;
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
        if (chat != null) {
            chat.onDestroy();
        }
        chat = null;
        backTop = null;
        titleTop = null;
        nextActivityWalletIntegralSend = null;
        activityWalletIntegralSend = null;
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedScoreModel changedScoreModel){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_integral_send);
        ButterKnife.bind(this);
        customInit(activityWalletIntegralSend, false, true, true);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showLongToast(getBaseActivityContext(), "数据传递失败");
            finish();
        }
        titleTop.setText("选择赠予人");

        //底部导航栏文字
        String[] text = new String[]{"     好友     ", "     关注     ", "     粉丝     ", "     最近聊天     "};


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), text);
        friend = new FragmentWalletIntegralSendFriendAttentionFansChat("1", this);//1好友，2关注，3粉丝 ,4最近聊天
        attention = new FragmentWalletIntegralSendFriendAttentionFansChat("2", this);//1好友，2关注，3粉丝 ,4最近聊天
        fans = new FragmentWalletIntegralSendFriendAttentionFansChat("3", this);//1好友，2关注，3粉丝 ,4最近聊天
        chat = new FragmentWalletIntegralSendFriendAttentionFansChat("4", this);//1好友，2关注，3粉丝 ,4最近聊天


        fragmentAdapter.addFragment(friend);
        fragmentAdapter.addFragment(attention);
        fragmentAdapter.addFragment(fans); fragmentAdapter.addFragment(chat);
        ScrollTab.setTitles(Arrays.asList(text));
        ScrollTab.setViewPager(viewpager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });

        viewpager.setAdapter(fragmentAdapter);viewpager.setOffscreenPageLimit(4);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                member_id = null;
                username = null;
                face = null;
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.back_top, R.id.next_activity_wallet_integral_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.next_activity_wallet_integral_send:
                if (StringUtils.isEmpty(member_id) || StringUtils.isEmpty(face) || StringUtils.isEmpty(username)) {
                    ToastUtils.showLongToast(getBaseActivityContext(), "请选择赠予对象");
                    return;
                }
                if ("integral".equals(getIntent().getExtras().getString("mode"))) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityWalletIntegralSendInput.class)
                                .putExtra("face", face)
                                .putExtra("member_id", member_id)
                                .putExtra("username", username));
                    }
                } else if ("coupon".equals(getIntent().getExtras().getString("mode"))) {
                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getBaseActivity());
                    }
                    menuDialog.createPasswordInputDialog("请输入您的支付密码", getBaseActivity(), new OnPayPasswordVerificationCallBack() {
                        @Override
                        public void onVerificationPassword(final String password, final PassWordKeyboard passWordKeyboard, final BottomSheetDialog bottomSheetDialog) {
                            P.r(ywLoadingDialog, Config.member_id, password, getBaseActivity(), new P.r() {
                                @Override
                                public void match() {
                                    bottomSheetDialog.dismiss();
                                    passWordKeyboard.setStatus(true);
                                    give_coupon();
                                }

                                @Override
                                public void mismatches() {
                                    passWordKeyboard.setStatus(false);
                                }
                            });
                        }
                    });

                }

                break;
        }
    }

    public void give_coupon() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.give_coupon(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("id", getIntent().getExtras().getString("id"))
                            .put("friend_id", member_id)

                            .toString()
                    , new StringCallback() {
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
                                    EventBus.getDefault().post(new CouponChange());
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


    @Override
    public void onSelectUserCallBack(String member_id, String face, String username, String type) {
        this.member_id = member_id;
        this.username = username;
        this.face = face;
        if (!StringUtils.isEmpty(type)) {
            switch (type) {
                case "1":
                    if (fans != null) {
                        fans.refreshList();
                    }
                    if (attention != null) {
                        attention.refreshList();
                    }
                    if (chat != null) {
                        chat.refreshList();
                    }
                    break;
                case "2":
                    if (friend != null) {
                        friend.refreshList();
                    }
                    if (fans != null) {
                        fans.refreshList();
                    }
                    if (chat != null) {
                        chat.refreshList();
                    }
                    break;
                case "3":
                    if (attention != null) {
                        attention.refreshList();
                    }
                    if (friend != null) {
                        friend.refreshList();
                    }
                    if (chat != null) {
                        chat.refreshList();
                    }
                    break;

                case "4":
                    if (attention != null) {
                        attention.refreshList();
                    }
                    if (friend != null) {
                        friend.refreshList();
                    }
                    if (fans != null) {
                        fans.refreshList();
                    }
                    break;

            }
        }
    }
}
