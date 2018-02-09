package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.WalletIntegralSendFriendAttentionFansChatModel;

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
 * 钱包==>积分==>选择赠予人==>好友/关注/粉丝/最近聊天公用
 * Created by leilei on 2017/10/25.
 */

@SuppressLint("ValidFragment")
public class FragmentWalletIntegralSendFriendAttentionFansChat extends BaseFragment {
    @BindView(R.id.listview_fragment_wallet_integral_send_friend_attention_fans_chat)
    ListView listviewFragmentWalletIntegralSendFriendAttentionFansChat;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    String type;
    OnSelectUserCallBack onSelectUserCallBack;

    List<WalletIntegralSendFriendAttentionFansChatModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;

    public FragmentWalletIntegralSendFriendAttentionFansChat(String type,OnSelectUserCallBack onSelectUserCallBack) {
        this.type = type;
        this.onSelectUserCallBack=onSelectUserCallBack;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_wallet_integral_send_friend_attention_fans_chat;
    }

    public FragmentWalletIntegralSendFriendAttentionFansChat() {
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
                                initAdapter();
                                if ("1".equals(type) || "2".equals(type) || "3".equals(type)) {
                                    select_member();
                                } else if ("4".equals(type)) {
                                    getChatList();
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

    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
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
        listviewFragmentWalletIntegralSendFriendAttentionFansChat = null;
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
        unbinder = null;
    }

    public void refreshList() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isChoose=false;
        }
        if (sss_adapter!=null){
            sss_adapter.setList(list);
        }
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<WalletIntegralSendFriendAttentionFansChatModel>(getBaseFragmentActivityContext(), R.layout.item_friend_attention_fans_chat_adapter) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, WalletIntegralSendFriendAttentionFansChatModel bean,SSS_Adapter instance) {
                helper.setChecked(R.id.cb_item_friend_attention_fans_chat_adapter,bean.isChoose);
                helper.setText(R.id.text_item_friend_attention_fans_chat_adapter, bean.username);
                addImageViewList(FrescoUtils.showImage(false, 50, 50, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item_friend_attention_fans_chat_adapter)), 100f));
                ((LinearLayout)helper.getView(R.id.click_item_friend_attention_fans_chat_adapter)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                list.get(i).isChoose = true;
                                if (onSelectUserCallBack!=null){
                                    onSelectUserCallBack.onSelectUserCallBack(list.get(i).member_id, list.get(i).face,list.get(i). username,type);
                                }
                            } else {
                                list.get(i).isChoose = false;
                            }
                        }
                        sss_adapter.setList(list);
                    }
                });


                ((CheckBox)helper.getView(R.id.cb_item_friend_attention_fans_chat_adapter)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                list.get(i).isChoose = true;
                                if (onSelectUserCallBack!=null){
                                    onSelectUserCallBack.onSelectUserCallBack(list.get(i).member_id, list.get(i).face,list.get(i). username,type);
                                }
                            } else {
                                list.get(i).isChoose = false;
                            }
                        }
                        sss_adapter.setList(list);
                    }
                });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
            }
        };
        listviewFragmentWalletIntegralSendFriendAttentionFansChat.setAdapter(sss_adapter);
    }


    void select_member() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.select_member(
                    new JSONObject()
                            .put("type", type)
                            .put("member_id", Config.member_id)
                            .toString()
                    , new StringCallback() {
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        WalletIntegralSendFriendAttentionFansChatModel model = new WalletIntegralSendFriendAttentionFansChatModel();
                                        model.face = jsonArray.getJSONObject(i).getString("face");
                                        model.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                        model.username = jsonArray.getJSONObject(i).getString("username");
                                        list.add(model);
                                    }
                                    sss_adapter.setList(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }



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
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            WalletIntegralSendFriendAttentionFansChatModel model = new WalletIntegralSendFriendAttentionFansChatModel();
                                            model.face = jsonArray.getJSONObject(i).getString("face");
                                            model.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            model.username = jsonArray.getJSONObject(i).getString("username");
                                            list.add(model);
                                        }
                                        sss_adapter.setList(list);
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


    public interface OnSelectUserCallBack{
        void onSelectUserCallBack(String member_id, String face, String username, String type);
    }
}
