package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CreateGroupFriendAttentionFansRecentlyChatPublicAdapter;
import com.sss.car.dao.CreateGroupFriendAttentionFansRecentlyChatClickCallBack;
import com.sss.car.dao.User_ID_ChooseCallBack;
import com.sss.car.model.CreateGroupFriendAttentionFansRecentlyChatPublicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 消息==>互动==>互动管理==>创建新聊天==>好友关注粉丝最近聊天公用Fragment
 * Created by leilei on 2017/8/29.
 */

@SuppressLint("ValidFragment")
public class FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic extends BaseFragment implements LoadImageCallBack, CreateGroupFriendAttentionFansRecentlyChatClickCallBack {


    @BindView(R.id.list_fragment_activity_share_interaction_manage_create_group_friend_attention_fans_recently_chat_public)
    RecyclerView listFragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic;
    Unbinder unbinder;

    public CreateGroupFriendAttentionFansRecentlyChatPublicAdapter createGroupFriendAttentionFansRecentlyChatPublicAdapter;
    public List<CreateGroupFriendAttentionFansRecentlyChatPublicModel> list = new ArrayList<>();
    int type = 0;
    String group_id;
    YWLoadingDialog ywLoadingDialog;
    User_ID_ChooseCallBack user_id_chooseCallBack;
    public FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic(int type,String group_id,User_ID_ChooseCallBack user_id_chooseCallBack) {
        this.type = type;
        this.group_id=group_id;
        this.user_id_chooseCallBack=user_id_chooseCallBack;
    }

    public FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_share_interaction_manage_create_group_friend_attention_fans_recently_chat_public;
    }


    /**
     * 搜索页面调用本页适配器 选择ITEM后通知刷新
     * @param id
     * @param isChoose
     */
    public void selectChanged(String id,boolean isChoose){
        if (list==null||createGroupFriendAttentionFansRecentlyChatPublicAdapter==null){
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).member_id.equals(id)){
                list.get(i).isChoose=isChoose;
            }
        }
        createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(list);


    }

    @Override
    protected void lazyLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(300);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createGroupFriendAttentionFansRecentlyChatPublicAdapter = new CreateGroupFriendAttentionFansRecentlyChatPublicAdapter(list,
                                    getBaseFragmentActivityContext(),
                                    FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic.this,
                                    FragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic.this);
                            listFragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic.setAdapter(createGroupFriendAttentionFansRecentlyChatPublicAdapter);
                            listFragmentActivityShareInteractionManageCreateGroupFriendAttentionFansRecentlyChatPublic.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
                            request();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
            case 4://最近聊天
                getChatList();
                break;
        }
    }

    /**
     * 好友关系-获取好友,关注,粉丝
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
                send                       .put("group_id",group_id)
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
                                                list.add(createGroupFriendAttentionFansRecentlyChatPublicModel);
                                            }

                                        }
                                        createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(list);
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


    /**
     * 获取最近聊天列表
     */
     void getChatList() {


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }

        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }

        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getChatList(
                    new JSONObject()
                            .put("group_id",group_id)
                            .put("member_id", Config.member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            list.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                CreateGroupFriendAttentionFansRecentlyChatPublicModel createGroupFriendAttentionFansRecentlyChatPublicModel = new CreateGroupFriendAttentionFansRecentlyChatPublicModel();

                                                createGroupFriendAttentionFansRecentlyChatPublicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.face = jsonArray.getJSONObject(i).getString("face");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.username = jsonArray.getJSONObject(i).getString("username");
                                                createGroupFriendAttentionFansRecentlyChatPublicModel.isChoose = false;
                                                list.add(createGroupFriendAttentionFansRecentlyChatPublicModel);
                                            }
                                            createGroupFriendAttentionFansRecentlyChatPublicAdapter.refresh(list);
                                        }

                                    } else {

                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: list-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseFragmentActivityContext() != null) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err: list-0");
            }
            e.printStackTrace();
        }
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
     * item选中状态被改变
     *
     * @param isChoose
     * @param position
     * @param model
     */
    @Override
    public void clickChanged(boolean isChoose, int position, CreateGroupFriendAttentionFansRecentlyChatPublicModel model, RecyclerView.ViewHolder viewHolder) {
        list.get(position).isChoose = isChoose;
        if (user_id_chooseCallBack!=null){
            user_id_chooseCallBack.onChooseID(model.member_id,isChoose);
        }
        createGroupFriendAttentionFansRecentlyChatPublicAdapter.refreshPosition(list, position, viewHolder);
    }
}
