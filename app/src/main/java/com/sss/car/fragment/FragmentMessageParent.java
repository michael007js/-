package com.sss.car.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ScollView.InCludeLandscapeScrollView;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ViewPager.AutofitViewPager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessage;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnOneKeyReadCallBack;
import com.sss.car.model.TopTabModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityPC;
import com.sss.car.view.ActivitySearchGoodsShopUser;

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
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

/**
 * 消息改版后
 * Created by leilei on 2017/8/23.
 */

@SuppressWarnings("ALL")
public class FragmentMessageParent extends BaseFragment {
    Unbinder unbinder;
    //    /*综合*/
//    public FragmentMessageSynthesize fragmentMessageSynthesize;
    /*系统*/
    public FragmentMessageSystem fragmentMessageSystem;
    /*互动*/
    public FragmentMessageInteraction fragmentMessageInteraction;
    /*评价*/
    public FragmentMessageComment fragmentMessageComment;
    /*交易*/
    public FragmentMessageOrder fragmentMessageOrder;

    @BindView(R.id.click_PC_fragment_message_parent)
    TextView clickPCFragmentMessageParent;
    @BindView(R.id.click_search)
    LinearLayout clickSearch;
    @BindView(R.id.top_tab_fragment_message)
    public ScrollTab topTabFragmentMessage;
    @BindView(R.id.click_menu)
    ImageView clickMenu;
    @BindView(R.id.CustomCacheViewPager)
    public AutofitViewPager CustomCacheViewPager;
    @BindView(R.id.scoll_view_fragment_message)
    InCludeLandscapeScrollView scollViewFragmentMessage;
    @BindView(R.id.click_top)
    ImageView clickTop;
    List<TopTabModel> goodsClassifyModelList = new ArrayList<>();
    String[] title = new String[5];
    YWLoadingDialog ywLoadingDialog;
    FragmentAdapter fragmentAdapter;

    MenuDialog menuDialog;

    public FragmentMessageParent() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(300);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                classify();
                                unread();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    protected void stopLoad() {

    }

    public void setMessageNumber(int unreadCount) {
        LogUtils.e(unreadCount);
        if (isLoad) {
            if (unreadCount > 0) {
                topTabFragmentMessage.setNumber(1, "", View.VISIBLE);
            } else {
                topTabFragmentMessage.setNumber(1, "", View.GONE);
            }
        }
    }

    public void setMessageNumber(String sys, String comment, String order) {
        if (isLoad) {
            if ("0".equals(sys)) {
                topTabFragmentMessage.setNumber(0, "", View.GONE);
            } else {
                topTabFragmentMessage.setNumber(0, "", View.VISIBLE);
            }
            if ("0".equals(comment)) {
                topTabFragmentMessage.setNumber(2, "", View.GONE);
            } else {
                topTabFragmentMessage.setNumber(2, "", View.VISIBLE);
            }
            if ("0".equals(order)) {
                topTabFragmentMessage.setNumber(3, "", View.GONE);
            } else {
                topTabFragmentMessage.setNumber(3, "", View.VISIBLE);
            }
        }


    }

    public void unread() {
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
                                    setMessageNumber(jsonObject.getJSONObject("data").getString("sys"), jsonObject.getJSONObject("data").getString("comment"), jsonObject.getJSONObject("data").getString("order"));
                                }
                                Conversation.ConversationType[] a = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP};
                                RongYunUtils.addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
                                    @Override
                                    public void onCountChanged(int i) {
                                        setMessageNumber(i);
                                    }
                                }, a);
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void classify() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_classify(
                    new JSONObject()
                            .put("type", "3")//分类(1车品2车服3消息4分享)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        goodsClassifyModelList.clear();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            TopTabModel goodsClassifyModel = new TopTabModel();
                                            goodsClassifyModel.id = jsonArray.getJSONObject(i).getString("classify_id");
                                            goodsClassifyModel.name = jsonArray.getJSONObject(i).getString("name");
                                            goodsClassifyModelList.add(goodsClassifyModel);
                                        }
                                        init();
                                    }
                                    if (ywLoadingDialog != null) {
                                        ywLoadingDialog.disMiss();
                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }


    int currentSelect;

    void init() {

        scollViewFragmentMessage.setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scollViewFragmentMessage.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    LogUtils.e(scrollY);
                    if (scrollY > Config.scoll_HighRestriction) {
                        clickTop.setVisibility(View.VISIBLE);
                    } else {
                        clickTop.setVisibility(View.GONE);

                    }
                }
            });
        }
        addImageViewList(GlidUtils.glideLoad(false, clickMenu, getBaseFragmentActivityContext(), R.mipmap.logo_ten));


        if (goodsClassifyModelList.size() == 4) {
            for (int i = 0; i < goodsClassifyModelList.size(); i++) {
                title[i] = "       " + goodsClassifyModelList.get(i).name + "       ";
            }
//            fragmentMessageSynthesize = new FragmentMessageSynthesize(goodsClassifyModelList.get(0).id);
            fragmentMessageSystem = new FragmentMessageSystem(null, null, true, goodsClassifyModelList.get(0).id);
            fragmentMessageInteraction = new FragmentMessageInteraction(goodsClassifyModelList.get(1).id);
            fragmentMessageComment = new FragmentMessageComment(null, true, goodsClassifyModelList.get(2).id);
            fragmentMessageOrder = new FragmentMessageOrder(null, true, goodsClassifyModelList.get(3).id);
            fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), title);
//            fragmentAdapter.addFragment(fragmentMessageSynthesize);
            fragmentAdapter.addFragment(fragmentMessageSystem);
            fragmentAdapter.addFragment(fragmentMessageInteraction);
            fragmentAdapter.addFragment(fragmentMessageComment);
            fragmentAdapter.addFragment(fragmentMessageOrder);
            CustomCacheViewPager.setAdapter(fragmentAdapter);
            CustomCacheViewPager.setOffscreenPageLimit(4);

            CustomCacheViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentSelect = position;
                    topTabFragmentMessage.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            topTabFragmentMessage.setTitles(Arrays.asList(title));
            topTabFragmentMessage.setViewPager(CustomCacheViewPager);
            topTabFragmentMessage.setOnTabListener(new ScrollTab.OnTabListener() {
                @Override
                public void onChange(int position, View v) {
//                 topTabFragmentMessageHold.onPageSelected(position);
//                topTabFragmentMessage.onPageSelected(position);
                    CustomCacheViewPager.setCurrentItem(position);
                }
            });
        } else {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回数据不符!");
        }

/******************************************************************************************/

    }


    @OnClick({R.id.click_PC_fragment_message_parent, R.id.click_search, R.id.click_menu, R.id.click_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_PC_fragment_message_parent:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityPC.class));
                }
                break;
            case R.id.click_search:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySearchGoodsShopUser.class));
                }
                break;
            case R.id.click_menu:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                menuDialog.createMessageMenu(clickMenu, getActivity(), new OnOneKeyReadCallBack() {
                    @Override
                    public void onOneKeyRead() {
                        switch (topTabFragmentMessage.position) {
                            case 0:
                                if (fragmentMessageSystem != null) {
                                    oneKeyRead(1);//1系统，2互动，3评价，4交易
                                }
                                break;
                            case 1:
                                if (fragmentMessageInteraction != null) {
                                    oneKeyRead(2);//1系统，2互动，3评价，4交易
                                }
                                break;
                            case 2:
                                if (fragmentMessageComment != null) {
                                    oneKeyRead(3);//1系统，2互动，3评价，4交易
                                }
                                break;
                            case 3:
                                if (fragmentMessageOrder != null) {
                                    oneKeyRead(4);//1系统，2互动，3评价，4交易
                                }
                                break;
                        }
                    }
                });
                break;
            case R.id.click_top:
                if (scollViewFragmentMessage != null) {
                    scollViewFragmentMessage.smoothScrollTo(0, 0);
                }
                break;
        }
    }


    /**
     * 一键已读消息
     *
     * @param type //1系统，2互动，3评价，4交易
     */
    public void oneKeyRead(final int type) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.oneKeyRead(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)//1系统，2互动，3评价，4交易

                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }

                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (type == 1) {
                                        if (fragmentMessageSystem != null) {
                                            fragmentMessageSystem.p = 1;
                                            fragmentMessageSystem.messageSystem();
                                        }
                                    } else if (type == 2) {
                                        List<Conversation> conversations = RongIM.getInstance().getConversationList();
                                        if (conversations==null){
                                            return;
                                        }
                                        for (int i = 0; i < conversations.size(); i++) {
                                            RongYunUtils.clearMessagesUnreadStatus(conversations.get(i).getConversationType(), conversations.get(i).getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {

                                                }

                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {

                                                }
                                            });
                                        }
                                        if (fragmentMessageInteraction != null) {
                                            fragmentMessageInteraction.getList();
                                        }

                                    } else if (type == 3) {
                                        if (fragmentMessageComment != null) {
                                            if (fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli != null) {
                                                fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli.p = 1;
                                                fragmentMessageComment.fragmentMessageCommentDymaicPostsPubli.managementOfMessageEvaluation();
                                            }
                                        }

                                    } else if (type == 4) {
                                        if (fragmentMessageOrder != null) {
                                            if (fragmentMessageOrder.fragmentMessageOrderPublic != null) {
                                                fragmentMessageOrder.fragmentMessageOrderPublic.p = 1;
                                                fragmentMessageOrder.fragmentMessageOrderPublic.messageOrderGetOrderInfo();
                                            }
                                        }

                                    }
                                    EventBus.getDefault().post(new ChangedMessage());


                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }

    }
}