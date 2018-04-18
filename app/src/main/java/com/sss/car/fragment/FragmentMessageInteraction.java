package com.sss.car.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.MyRecycleview.FullyGridLayoutManager;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ViewPager.AnimationViewPager;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.view.ActivityShareInteraction;
import com.sss.car.view.ActivityShareInteractionManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.Call;

import static android.R.id.list;


/**
 * Created by leilei on 2017/8/23.
 */

@SuppressWarnings("ALL")
public class FragmentMessageInteraction extends BaseFragment {
    Unbinder unbinder;

    @BindView(R.id.advertisement_fragment_message_interaction)
    BannerVariation advertisementFragmentMessageInteraction;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.friend)
    LinearLayout friend;
    @BindView(R.id.buy)
    LinearLayout buy;
    @BindView(R.id.attention)
    LinearLayout attention;
    @BindView(R.id.other)
    LinearLayout other;
    @BindView(R.id.manager)
    LinearLayout manager;

    public String classify_id;

    public FragmentMessageInteraction() {
    }

    public FragmentMessageInteraction(String classify_id) {
        this.classify_id = classify_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_interaction;
    }

    @Override
    protected void lazyLoad() {
        if (!isAdded && !isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(500);
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isAdded = true;
                                initAdv("8", classify_id);
                                initAdapter();
                                getList();
                                advertisementFragmentMessageInteraction.setDelayTime(Config.flash);
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

    YWLoadingDialog ywLoadingDialog;


    SSS_RVAdapter sss_rvAdapter;

    boolean isAdded = false;

    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (sss_rvAdapter != null) {
            sss_rvAdapter.clear();
        }
        sss_rvAdapter = null;
        advertisementFragmentMessageInteraction = null;
        recyclerview = null;
        super.onDestroy();
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


    public void getList() {
        if (isAdded && isVisibleToUser) {
            loadList();
        }
    }

    public void loadList(){
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (sss_rvAdapter != null) {
                    sss_rvAdapter.setList(conversations);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtils.showLongToast(getBaseFragmentActivityContext(), errorCode.getMessage());
            }
        });
    }

    JSONObject jsonObject;

    void initAdv(String site_id, String classify_id) {
        AdvertisementManager.advertisement(site_id, classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                if (advertisementFragmentMessageInteraction != null) {
                    advertisementFragmentMessageInteraction
                            .setImages(list)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                            .setDelayTime(Config.flash)
                            .setImageLoader(new ImageLoaderInterface() {
                                @Override
                                public void displayImage(Context context, final Object path, View imageView) {
                                    imageView.setTag(R.id.glide_tag, ((AdvertisementModel) path).picture);
                                    addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AdvertisementManager.jump(((AdvertisementModel) path),getBaseFragmentActivityContext());
                                        }
                                    });
                                }

                                @Override
                                public View createImageView(Context context) {
                                    return null;
                                }
                            });
                    advertisementFragmentMessageInteraction.start();
                    advertisementFragmentMessageInteraction.startAutoPlay();
                }
            }
        });
    }


    void initAdapter() {
        sss_rvAdapter = new SSS_RVAdapter<Conversation>(recyclerview, R.layout.item_conversation_list) {
            @Override
            protected void setView(final SSS_HolderHelper helper, int position, final Conversation bean) {
                try {
//                    final int defaultId1;
//                    if (bean.getConversationType().equals(Conversation.ConversationType.GROUP)) {
//                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
//                    } else if (bean.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
//                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
//                    } else {
//                        defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
//                    }
                    if (bean.getSentStatus() == Message.SentStatus.READ) {
                        helper.setVisibility(R.id.receipt, View.GONE);
                    } else {
                        helper.setVisibility(R.id.receipt, View.GONE);
                    }

                    jsonObject = null;
                    jsonObject = new JSONObject();
                    LogUtils.e(bean.getConversationType().getName());
                    if ("private".equals(bean.getConversationType().getName())) {
                        RequestWeb.trends_member(jsonObject
                                .put("member_id", Config.member_id)
                                .put("friend_id",bean.getTargetId()).toString(), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    helper.setText(R.id.name, jsonObject.getJSONObject("data").getString("remark"));
                                    addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("face")), ((SimpleDraweeView) helper.getView(R.id.pic)), 99999));
                                    RongYunUtils.refreshUserinfo(bean.getTargetId(), jsonObject.getJSONObject("data").getString("remark"), Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("face")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if ("group".equals(bean.getConversationType().getName())) {
                        LogUtils.e(bean.getTargetId());
                        RequestWeb.getGroupInfo(jsonObject
                                .put("member_id", Config.member_id)
                                .put("group_id", bean.getTargetId())
                                .toString(), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        helper.setText(R.id.name, jsonObject.getJSONObject("data").getString("name"));
                                        addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("picture")), ((SimpleDraweeView) helper.getView(R.id.pic)), 99999));
                                        RongYunUtils.refreshGroupInfoCache(jsonObject.getJSONObject("data").getString("group_id"), jsonObject.getJSONObject("data").getString("name"), Uri.parse(Config.url + jsonObject.getJSONObject("data").getString("picture")));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    }
                    if (bean.getUnreadMessageCount() > 0) {
                        helper.setVisibility(R.id.unread, View.VISIBLE);
                        if (bean.getUnreadMessageCount() > 99) {
                            helper.setText(R.id.unread, "99");
                        } else {
                            helper.setText(R.id.unread, Integer.toString(bean.getUnreadMessageCount()));
                        }
                        helper.setVisibility(R.id.unread, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id.unread, View.GONE);
                    }

                    if (bean.getLatestMessage() != null) {
                        helper.setText(R.id.content, parseContent(new String(bean.getLatestMessage().encode())));
                    }
                    helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!((SwipeMenuLayout) helper.getView(R.id.scoll)).isExpand) {
                                if ("group".equals(bean.getConversationType().getName())){
                                    RongYunUtils.startConversation(getBaseFragmentActivityContext(), bean.getConversationType(), bean.getTargetId(),"5");////客服传6，商品详情客服传2，群组传5
                                }else {
                                    RongYunUtils.startConversation(getBaseFragmentActivityContext(), bean.getConversationType(), bean.getTargetId(), bean.getConversationTitle());
                                }

                            } else {
                                ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                            }


                        }
                    });
                    helper.getView(R.id.click).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothExpand();
                            return true;
                        }
                    });
                    if (bean.getReceivedTime() > bean.getSentTime()) {
                        helper.setText(R.id.date, TimeUtils.LongFormatTime(bean.getReceivedTime()));
                    } else {
                        helper.setText(R.id.date, TimeUtils.LongFormatTime(bean.getSentTime()));
                    }

                    if (bean.isTop()) {
                        helper.setBackgroundRes(R.id.click, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                        helper.setText(R.id.top, "取消置顶");
                        helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                                RongYunUtils.setConversationToTop(bean.getConversationType(), bean.getTargetId(), false, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        FragmentMessageInteraction.this.getList();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            }
                        });
                    } else {
                        helper.setBackgroundRes(R.id.click, io.rong.imkit.R.drawable.rc_item_list_selector);
                        helper.setText(R.id.top, "置顶");
                        helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                                RongYunUtils.setConversationToTop(bean.getConversationType(), bean.getTargetId(), true, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        FragmentMessageInteraction.this.getList();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            }
                        });
                    }


                    helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                            close_window(bean);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }

        };
        recyclerview.setAdapter(sss_rvAdapter);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setLayoutManager(new FullyGridLayoutManager(getBaseFragmentActivityContext(), 1, OrientationHelper.VERTICAL, false));
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

    /**
     * 设置消息已读
     */
    void close_window(final Conversation conversation) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.close_window(
                    new JSONObject()
                            .put("member_pid", Config.member_id)
                            .put("window_type", getType(conversation.getConversationType().getName()))
                            .put("member_id", conversation.getTargetId()).toString(), new StringCallback() {
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
                                RongYunUtils.removeConversation(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        getList();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), errorCode.getMessage());
                                    }
                                });
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "解析错误err-0" + e.getMessage());
                                e.printStackTrace();
                            }

                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "解析错误err-1" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String parseContent(String msg) throws JSONException {
        JSONObject jsonObject = new JSONObject(msg);
//        LogUtils.e(jsonObject.toString());
        if (jsonObject.has("content")) {
            if (StringUtils.isEmpty(jsonObject.getString("content"))) {
                return "";
            } else {
                String temp = jsonObject.getString("content");
                if (temp.startsWith("file:///data/user/0/com.sss.car/")) {
                    return "[位置]";
                } else {
                    return jsonObject.getString("content");
                }
            }
        } else if (jsonObject.has("duration")) {
            return "[语音]";
        } else if (jsonObject.has("imageUri")) {
            return "[图片]";
        } else if (jsonObject.has("name")) {
            if (jsonObject.has("size")) {
                return "[文件]";
            } else {
                return "";
            }

        } else {
            return "";
        }
    }


    @OnClick({R.id.friend, R.id.buy, R.id.attention, R.id.other, R.id.manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.friend:
                //1好友聊天  2交易聊天  3关注聊天  4陌生招呼  5互动管理
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteraction.class).putExtra("cate_id", String.valueOf(1)));
                }
                break;
            case R.id.buy:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteraction.class).putExtra("cate_id", String.valueOf(2)));
                }
                break;
            case R.id.attention:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteraction.class).putExtra("cate_id", String.valueOf(3)));
                }
                break;
            case R.id.other:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteraction.class).putExtra("cate_id", String.valueOf(4)));
                }
                break;
            case R.id.manager:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShareInteractionManage.class));
                }
                break;
        }
    }
}
