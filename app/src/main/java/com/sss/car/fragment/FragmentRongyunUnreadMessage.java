package com.sss.car.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.UnReadMessageModel;
import com.sss.car.rongyun.RongYunUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

import static android.R.attr.mode;
import static android.R.attr.targetId;

/**
 * 融云未读消息
 * Created by leilei on 2017/12/14.
 */

@SuppressWarnings("ALL")
public class FragmentRongyunUnreadMessage extends BaseFragment {
    @BindView(R.id.listview_fragment_rongyun_unread_message)
    PullToRefreshListView listviewFragmentRongyunUnreadMessage;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    List<UnReadMessageModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    Gson gson = new Gson();

    public FragmentRongyunUnreadMessage() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_rongyun_unread_message;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listviewFragmentRongyunUnreadMessage.setMode(PullToRefreshBase.Mode.DISABLED);
                                initAdapter();
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
        unbinder = null;
    }

    private void initAdapter() {
        sss_adapter = new SSS_Adapter<UnReadMessageModel>(getBaseFragmentActivityContext(), R.layout.item_conversation_list) {
            @Override
            protected void setView(final SSS_HolderHelper helper, int position, final UnReadMessageModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name, bean.username);
                helper.setText(R.id.content, bean.contents);
                helper.setTextColor(R.id.content,getResources().getColor(R.color.grayness));
                helper.setText(R.id.date,TimeUtils.LongFormatTime(TimeUtils.string2Millis(bean.create_time)));
                addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic)), 80));
                helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((SwipeMenuLayout) helper.getView(R.id.scoll)).isExpand){
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        }else {
                            helper.setTextColor(R.id.name,getResources().getColor(R.color.grayness));
                            helper.setTextColor(R.id.date,getResources().getColor(R.color.grayness));
                            if ("5".equals(bean.cate_id)) {
                                RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.GROUP, bean.member_id, bean.username);
                            } else {
                                RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE, bean.member_id, bean.username);
                            }
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

                if ("1".equals(bean.is_top)) {
                    helper.setBackgroundRes(R.id.click, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setText(R.id.top, "取消置顶");
                    helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();

                            if ("5".equals(bean.cate_id)) {
                                RongYunUtils.setConversationToTop(Conversation.ConversationType.GROUP, bean.member_id, false, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        top(bean.member_id);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            } else {
                                RongYunUtils.setConversationToTop(Conversation.ConversationType.PRIVATE, bean.member_id, false, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        top(bean.member_id);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            }


                        }
                    });
                } else {
                    helper.setBackgroundRes(R.id.click, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setText(R.id.top, "置顶");
                    helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                            if ("5".equals(bean.cate_id)) {
                                ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();

                                RongYunUtils.setConversationToTop(Conversation.ConversationType.GROUP, bean.member_id, true, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        top(bean.member_id);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            } else {
                                RongYunUtils.setConversationToTop(Conversation.ConversationType.PRIVATE, bean.member_id, true, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        top(bean.member_id);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                            }
                        }

                    });

                }

                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        read_window(bean);
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        listviewFragmentRongyunUnreadMessage.setAdapter(sss_adapter);
        listviewFragmentRongyunUnreadMessage.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view, null));
    }


    /**
     * 消息综合置顶/取消置顶
     */
    public void top(String ids) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.top(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("ids", ids)
                            .put("type", "chat")//sys系统，chat互动，comment评论，order订单
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
                                    list.clear();
                                    unread();
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
    void read_window(UnReadMessageModel model) {
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
                            .put("window_type", getType(model.chat_type))
                            .put("member_id", model.member_id).toString(), new StringCallback() {
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
                                if ("1".equals(jsonObject.getString("status"))) {
                                    unread();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
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

    /**
     * 未读消息
     */
    public void unread() {
        if (isVisibleToUser) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ywLoadingDialog = null;
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
            try {
                addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getChatList(
                        new JSONObject()
                                .put("member_id", Config.member_id)
                                .put("is_read", "1")
                                .toString(), new StringCallback() {
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
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        list.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), UnReadMessageModel.class));
                                        }
                                        sss_adapter.setList(list);

                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
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
    }
}
