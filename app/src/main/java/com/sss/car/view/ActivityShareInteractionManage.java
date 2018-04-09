package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedAttentionList;
import com.sss.car.EventBusModel.ChangedBlackList;
import com.sss.car.EventBusModel.ChangedGroupList;
import com.sss.car.EventBusModel.ChangedList;
import com.sss.car.EventBusModel.ChangedPostsList;
import com.sss.car.EventBusModel.ChangedUserInfo;
import com.sss.car.EventBusModel.ExitGroup;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentMessageInteractionManageFriendAttentionFansPublic;
import com.sss.car.fragment.FragmentMessageInteractionManageGroup;
import com.sss.car.fragment.FragmentMessageInteractionManageInterest;
import com.sss.car.model.SettingModel;
import com.sss.car.utils.MenuDialog;

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
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.Call;

import static com.sss.car.view.ActivityMyDataSynthesizSettingSetPayPassword.set;

/**
 * 消息==>互动==>互动管理
 * Created by leilei on 2017/8/28.
 */

public class ActivityShareInteractionManage extends BaseActivity {

    @BindView(R.id.activity_share_interaction_manage)
    LinearLayout activityShareInteractionManage;
    MenuDialog menuDialog;
    FragmentAdapter fragmentAdapter;
    /*底部导航栏文字*/
    String[] title = new String[]{"  好友  ", "  关注  ", "  粉丝  ", "  群组  ", "  兴趣  "};
    /*好友关注粉丝公用fragment*/
    FragmentMessageInteractionManageFriendAttentionFansPublic friend, attention, fans;

    /*群组*/
    FragmentMessageInteractionManageGroup fragmentMessageInteractionManageGroup;

    /*兴趣*/
    FragmentMessageInteractionManageInterest fragmentMessageInteractionManageInterest;
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.title_top_image)
    TextView titleTopImage;
    @BindView(R.id.logo_right_search_top_image)
    ImageView logoRightSearchTopImage;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.scrollTab)
    ScrollTab scrollTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.phone_books)
    TextView phoneBooks;
    YWLoadingDialog ywLoadingDialog;
    SettingModel settingModel;
    List<PermissionItem> permissionItemList = new ArrayList<>();
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
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
        if (fragmentMessageInteractionManageGroup != null) {
            fragmentMessageInteractionManageGroup.onDestroy();
        }
        fragmentMessageInteractionManageGroup = null;
        if (fragmentMessageInteractionManageInterest != null) {
            fragmentMessageInteractionManageInterest.onDestroy();
        }
        fragmentMessageInteractionManageInterest = null;
        title = null;
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        rightSearchTopImage = null;
        logoRightSearchTopImage = null;
        titleTopImage = null;
        backTopImage = null;

        scrollTab = null;
        viewpager = null;
        activityShareInteractionManage = null;
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
        if (permissionItemList!=null){
            permissionItemList.clear();
        }
        permissionItemList=null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_interaction_manage);
        ButterKnife.bind(this);
        customInit(activityShareInteractionManage, false, true, true);
        menuDialog = new MenuDialog(this);
        titleTopImage.setText("互动管理");
        addImageViewList(GlidUtils.glideLoad(false, logoRightSearchTopImage, getBaseActivityContext(), R.mipmap.logo_setting));
        logoRightSearchTopImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //读取联系人权限
        permissionItemList.add(new PermissionItem(PermissionUtils.READ_CONTACTS, "读取联系人权限", com.blankj.utilcode.R.drawable.permission));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollTab.onPageSelected(position);
                switch (position){
                    case 0:
                        showButton();
                        break;
                    case 1:
                        phoneBooks.setVisibility(View.GONE);
                        break;
                    case 2:
                        phoneBooks.setVisibility(View.GONE);
                        break;
                    case 3:
                        phoneBooks.setVisibility(View.GONE);
                        break;
                    case 4:
                        phoneBooks.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        friend = new FragmentMessageInteractionManageFriendAttentionFansPublic(1);
        attention = new FragmentMessageInteractionManageFriendAttentionFansPublic(2);
        fans = new FragmentMessageInteractionManageFriendAttentionFansPublic(3);
        fragmentMessageInteractionManageGroup = new FragmentMessageInteractionManageGroup();
        fragmentMessageInteractionManageInterest = new FragmentMessageInteractionManageInterest();
        fragmentAdapter.addFragment(friend);
        fragmentAdapter.addFragment(attention);
        fragmentAdapter.addFragment(fans);
        fragmentAdapter.addFragment(fragmentMessageInteractionManageGroup);
        fragmentAdapter.addFragment(fragmentMessageInteractionManageInterest);
        viewpager.setAdapter(fragmentAdapter);
        viewpager.setOffscreenPageLimit(5);
        scrollTab.setTitles(Arrays.asList(title));
        scrollTab.setViewPager(viewpager);
        scrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });
        scrollTab.onPageSelected(0);
        if (getIntent() == null || getIntent().getExtras() == null || StringUtils.isEmpty(getIntent().getExtras().getString("mode"))) {
            viewpager.setCurrentItem(0);
        } else {
            switch (getIntent().getExtras().getString("mode")) {
                case "1":
                    scrollTab.onPageSelected(0);
                    viewpager.setCurrentItem(0);
                    break;
                case "2":
                    scrollTab.onPageSelected(1);
                    viewpager.setCurrentItem(1);
                    break;
                case "3":
                    scrollTab.onPageSelected(2);
                    viewpager.setCurrentItem(2);
                    break;
                case "4":
                    scrollTab.onPageSelected(3);
                    viewpager.setCurrentItem(3);
                    break;
                case "5":
                    scrollTab.onPageSelected(4);
                    viewpager.setCurrentItem(4);
                    break;
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsinfo();
    }

    void showButton() {
        if (0 == viewpager.getCurrentItem()) {
            if (settingModel != null) {
                if ("1".equals(settingModel.address_book)) {
                    phoneBooks.setVisibility(View.VISIBLE);
                }else {
                    phoneBooks.setVisibility(View.GONE);
                }
            }
        } else {
            phoneBooks.setVisibility(View.GONE);
        }

    }

    /**
     * 获取用户设置资料
     */
    public void getUsinfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getUsinfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    settingModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SettingModel.class);
                                    showButton();

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_top_image, R.id.right_search_top_image, R.id.phone_books})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShareInteractionManageSetting.class));
                }
                break;
            case R.id.phone_books:
                if (getBaseActivityContext() != null) {
                    requestPermissions(permissionItemList, new PermissionCallback() {
                        @Override
                        public void onClose() {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "您未授权必要的权限");
                            }
                        }

                        @Override
                        public void onFinish() {
                            startActivity(new Intent(getBaseActivityContext(), ActivityPhoneBook.class));
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "您未授权必要的权限");
                            LogUtils.e("onDeny" + permission + position);
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {
//                ToastUtils.showShortToast(getBaseActivityContext(), "onGuarantee");
                            LogUtils.e("onGuarantee" + permission + position);
                        }
                    });

                }

                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedUserInfo changedUserInfo) {
        if (friend!=null){
            friend. request();
        }
        if (attention!=null){
            attention. request();
        }
        if (fans!=null){
            fans. request();
        }
    }

    /**
     * 拉黑好友后通知相关界面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedBlackList event) {
        if (attention != null) {
            attention.request();
        }
        if (friend != null) {
            friend.request();
        }

        if (fans != null) {
            fans.request();
        }
    }

    /**
     * 关注好友后通知相关界面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedList event) {
        if (attention != null) {
            attention.request();
        }
        if (friend != null) {
            friend.request();
        }

        if (fans != null) {
            fans.request();
        }
    }

    /**
     * 关注好友后通知相关界面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedAttentionList event) {
        if (attention != null) {
            attention.request();
        }
        if (friend != null) {
            friend.request();
        }


    }

    /**
     * 群组列表被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedGroupList event) {
        if (fragmentMessageInteractionManageGroup != null) {
            fragmentMessageInteractionManageGroup.request(true);
        }
    }

    /**
     * 用户退群
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitGroup event) {
        if (fragmentMessageInteractionManageGroup != null) {
            fragmentMessageInteractionManageGroup.request(true);
        }
    }

    /**
     * 群组列表被改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsList event) {
        if (fragmentMessageInteractionManageInterest != null) {
            fragmentMessageInteractionManageInterest.collect_cate();
        }
    }

}
