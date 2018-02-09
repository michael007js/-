package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;
import com.sss.car.rongyun.RongYunUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;


/**
 * 用户选择管理类(邀请加群,群发助手等等)
 * Created by leilei on 2017/12/15.
 */

@SuppressLint("ValidFragment")
public class fragmentUserManager extends BaseFragment {
    YWLoadingDialog ywLoadingDialog;
    public List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list = new ArrayList<>();
    String type;//1好友列表  2关注列表  3粉丝列表   4最近聊天列表
    public SSS_Adapter sss_adapter;
    @BindView(R.id.listview_fragment_user_manager)
    ListView listviewFragmentUserManager;
    Unbinder unbinder;
    OnSelectCallBack onSelectCallBack;
    boolean is_create_private;

    public fragmentUserManager() {
    }

    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        listviewFragmentUserManager = null;
        onSelectCallBack = null;
        super.onDestroy();
    }

    public fragmentUserManager(boolean is_create_private,String type, OnSelectCallBack onSelectCallBack) {
        this.is_create_private=is_create_private;
        this.type = type;
        this.onSelectCallBack = onSelectCallBack;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user_manager;
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
                                try {
                                    initAdapter();
                                    friendRelation();
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "Json解析出错" + e.getMessage());
                                    e.printStackTrace();
                                }
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


    private void initAdapter() {
        sss_adapter = new SSS_Adapter<CreateGroupFriendAttentionFansRecentlyChatPublicModel>(getBaseFragmentActivityContext(), R.layout.item_user_manager) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final CreateGroupFriendAttentionFansRecentlyChatPublicModel bean, SSS_Adapter instance) {
                addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item_user_manager)), 40f));


                    if (!StringUtils.isEmpty(bean.title)) {
                        helper.setVisibility(R.id.title_item_user_manager, View.VISIBLE);
                        helper.setVisibility(R.id.click_item_user_manager, View.GONE);
                        helper.setText(R.id.title_item_user_manager, bean.title);
                    } else {
                        helper.setVisibility(R.id.title_item_user_manager, View.GONE);
                        helper.setVisibility(R.id.click_item_user_manager, View.VISIBLE);
                        helper.setText(R.id.text_item_user_manager, bean.username);
                        if (is_create_private) {
                            helper.setVisibility(R.id.cb_item_user_manager, View.GONE);
                            helper.getView(R.id.click_item_user_manager).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RongYunUtils.startConversation(getBaseFragmentActivityContext(), Conversation.ConversationType.PRIVATE,bean.member_id,bean.title);
                                }
                            });
                        }else {
                            helper.getView(R.id.click_item_user_manager).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (bean.isChoose) {
                                        bean.isChoose = false;
                                    } else {
                                        bean.isChoose = true;
                                    }
                                    onSelectCallBack.onSelectCallBack(bean);
                                    sss_adapter.setList(list);
                                }
                            });
                            helper.setChecked(R.id.cb_item_user_manager, bean.isChoose);
                        }

                    }



            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listviewFragmentUserManager.setAdapter(sss_adapter);
    }

    /**
     * 好友关系-获取好友,关注,粉丝,最近聊天
     *
     * @throws JSONException
     */
    void friendRelation() throws JSONException {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friendRelation(
                new JSONObject().put("type", type)
                        .put("member_id", Config.member_id).toString(), type + "1好友列表  2关注列表  3粉丝列表   4最近聊天列表", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (ywLoadingDialog != null) {
                            ywLoadingDialog.disMiss();
                        }
                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        list.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            CreateGroupFriendAttentionFansRecentlyChatPublicModel createGroupFriendAttentionFansRecentlyChatPublicModel = new CreateGroupFriendAttentionFansRecentlyChatPublicModel();
                                            if (jsonArray.getJSONObject(i).has("face") &&
                                                    jsonArray.getJSONObject(i).has("member_id") &&
                                                    jsonArray.getJSONObject(i).has("username")
                                                    ) {
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.face = jsonArray.getJSONObject(i).getString("face");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.username = jsonArray.getJSONObject(i).getString("username");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.isChoose = false;
                                            } else if (jsonArray.getJSONObject(i).has("title")) {
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.title = jsonArray.getJSONObject(i).getString("title");
                                            }
                                            list.add(createGroupFriendAttentionFansRecentlyChatPublicModel);
                                        }
                                        sss_adapter.setList(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:-0");
                                e.printStackTrace();
                            }
                        }
                    }
                })));
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


    public interface OnSelectCallBack {

        void onSelectCallBack(CreateGroupFriendAttentionFansRecentlyChatPublicModel model);

    }
}
