package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Slidebar.WaveSideBar;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.MessageInteractionManageFriendAttentionFansPublicAdapter;
import com.sss.car.dao.MessageInteractionManageFriendAttentionFansPublicClickCallBack;
import com.sss.car.model.MessageInteractionManageFriendAttentionFansPublicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import okhttp3.Call;


/**
 * 消息==>互动==>互动管理==>好友关注粉丝公用fragment
 * Created by leilei on 2017/8/28.
 */
@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentMessageInteractionManageFriendAttentionFansPublic extends BaseFragment implements LoadImageCallBack, MessageInteractionManageFriendAttentionFansPublicClickCallBack {
    @BindView(R.id.list_fragment_message_interaction_manage_friend_attention_fans_public)
    ListView listFragmentMessageInteractionManageFriendAttentionFansPublic;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    int type = 0;
    List<MessageInteractionManageFriendAttentionFansPublicModel> list = new ArrayList<>();
    MessageInteractionManageFriendAttentionFansPublicAdapter messageInteractionManageFriendAttentionFansPublicAdapter;
    @BindView(R.id.side_bar)
    WaveSideBar sideBar;


    public FragmentMessageInteractionManageFriendAttentionFansPublic() {
    }

    public FragmentMessageInteractionManageFriendAttentionFansPublic(int type) {
        this.type = type;
    }

    @Override
    public void onDestroy() {
        if (messageInteractionManageFriendAttentionFansPublicAdapter != null) {
            messageInteractionManageFriendAttentionFansPublicAdapter.clear();
        }
        messageInteractionManageFriendAttentionFansPublicAdapter = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        listFragmentMessageInteractionManageFriendAttentionFansPublic = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_interaction_manage_friend_attention_fans_public;
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
                                messageInteractionManageFriendAttentionFansPublicAdapter = new MessageInteractionManageFriendAttentionFansPublicAdapter(
                                        FragmentMessageInteractionManageFriendAttentionFansPublic.this, getBaseFragmentActivityContext(),
                                        FragmentMessageInteractionManageFriendAttentionFansPublic.this, list);
                                listFragmentMessageInteractionManageFriendAttentionFansPublic.setAdapter(messageInteractionManageFriendAttentionFansPublicAdapter);
                                request();
                                sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
                                    @Override
                                    public void onSelectIndexItem(String index) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).title != null) {
                                                if (index.equals(list.get(i).spell)) {
                                                    listFragmentMessageInteractionManageFriendAttentionFansPublic.setSelection(i);
                                                    return;
                                                }
                                            }
                                        }

                                    }
                                });

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

    }


    public void request() {
        switch (type) {
            case 1://好友
                try {
                    friendRelation(new JSONObject()
                            .put("type", "1"), "获取好友列表");
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend-0");
                    e.printStackTrace();
                }
                break;
            case 2://关注
                try {
                    friendRelation(new JSONObject()
                            .put("type", "2"), "获取关注列表");
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend-0");
                    e.printStackTrace();
                }
                break;
            case 3://粉丝
                try {
                    friendRelation(new JSONObject()
                            .put("type", "3"), "获取粉丝列表");
                } catch (JSONException e) {
                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend-0");
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 好友关系-获取好友,粉丝,关注
     *
     * @param send
     * @throws JSONException
     */
    void friendRelation(final JSONObject send, String meaning) throws JSONException {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friendRelation(
                send
                        .put("member_id", Config.member_id).toString(), meaning, new StringCallback() {
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
                                    list.clear();
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            MessageInteractionManageFriendAttentionFansPublicModel messageInteractionManageFriendAttentionFansPublicModel = new MessageInteractionManageFriendAttentionFansPublicModel();
                                            if (jsonArray.getJSONObject(i).has("face")) {
                                                messageInteractionManageFriendAttentionFansPublicModel.face = jsonArray.getJSONObject(i).getString("face");
                                            }
                                            if (jsonArray.getJSONObject(i).has("spell")) {
                                                messageInteractionManageFriendAttentionFansPublicModel.spell = jsonArray.getJSONObject(i).getString("spell");
                                            }
                                            if (jsonArray.getJSONObject(i).has("title")) {
                                                messageInteractionManageFriendAttentionFansPublicModel.title = jsonArray.getJSONObject(i).getString("title");
                                            }
                                            if (jsonArray.getJSONObject(i).has("member_id")) {
                                                messageInteractionManageFriendAttentionFansPublicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            }
                                            if (jsonArray.getJSONObject(i).has("username")) {
                                                messageInteractionManageFriendAttentionFansPublicModel.username = jsonArray.getJSONObject(i).getString("username");
                                            }

                                            list.add(messageInteractionManageFriendAttentionFansPublicModel);
                                        }
                                    }
                                    if (messageInteractionManageFriendAttentionFansPublicAdapter != null) {
                                        messageInteractionManageFriendAttentionFansPublicAdapter.refresh(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend-0");
                                e.printStackTrace();
                            }
                        }
                    }
                })));
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
    }

    /**
     * 图片被载入
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    /**
     * item被点击删除
     *
     * @param poistion
     * @param messageInteractionManageFriendAttentionFansPublicModel
     * @param list
     */
    @Override
    public void onDelete(int poistion, MessageInteractionManageFriendAttentionFansPublicModel messageInteractionManageFriendAttentionFansPublicModel, List<MessageInteractionManageFriendAttentionFansPublicModel> list) {

        try {
            friendOperation(new JSONObject()
                    .put("friend_id", messageInteractionManageFriendAttentionFansPublicModel.member_id)
                    .put("status", String.valueOf(type)), "删除")
            ;
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend operation-0");
            e.printStackTrace();
        }
    }

    /**
     * item被点击
     *
     * @param poistion
     * @param messageInteractionManageFriendAttentionFansPublicModel
     * @param list
     */
    @Override
    public void onClick(int poistion, MessageInteractionManageFriendAttentionFansPublicModel messageInteractionManageFriendAttentionFansPublicModel, List<MessageInteractionManageFriendAttentionFansPublicModel> list) {
//        LogUtils.e(Config.member_id+"---"+ messageInteractionManageFriendAttentionFansPublicModel.member_id+"---"+ messageInteractionManageFriendAttentionFansPublicModel.username);
        RongIM.getInstance().startPrivateChat(getBaseFragmentActivityContext(), messageInteractionManageFriendAttentionFansPublicModel.member_id, messageInteractionManageFriendAttentionFansPublicModel.username);
//       if (getBaseFragmentActivityContext()!=null){
//           startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
//                   .putExtra("id", messageInteractionManageFriendAttentionFansPublicModel.member_id)
//                   .putExtra("nikename", messageInteractionManageFriendAttentionFansPublicModel.username));
//       }
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
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
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
                                    request();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误err:friend operation-0");
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }

}
