package com.sss.car.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.NineView;
import com.blankj.utilcode.customwidget.Layout.LayoutNineGrid.RatioImageView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.MessageInteractionManageGroupAdapter;
import com.sss.car.dao.MessageInteractionManageGroupClickCallBack;
import com.sss.car.model.MessageInteractionManageGroupModel;


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
 * 消息==>互动==>互动管理==>群列表
 * Created by leilei on 2017/8/28.
 */

public class FragmentMessageInteractionManageGroup extends BaseFragment implements LoadImageCallBack, MessageInteractionManageGroupClickCallBack, NineView.NineViewShowCallBack {
    @BindView(R.id.list_fragment_message_interaction_manage_group)
    RecyclerView listFragmentMessageInteractionManageGroup;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    MessageInteractionManageGroupAdapter messageInteractionManageGroupAdapter;
    List<MessageInteractionManageGroupModel> list = new ArrayList<>();


    @Override
    public void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        listFragmentMessageInteractionManageGroup = null;
        if (messageInteractionManageGroupAdapter != null) {
            messageInteractionManageGroupAdapter.clear();
        }
        messageInteractionManageGroupAdapter = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        super.onDestroy();
    }

    public FragmentMessageInteractionManageGroup() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_interaction_manage_group;
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
                                messageInteractionManageGroupAdapter = new MessageInteractionManageGroupAdapter(list,
                                        getBaseFragmentActivityContext(),
                                        FragmentMessageInteractionManageGroup.this,
                                        FragmentMessageInteractionManageGroup.this,
                                        FragmentMessageInteractionManageGroup.this);
                                listFragmentMessageInteractionManageGroup.setAdapter(messageInteractionManageGroupAdapter);
                                listFragmentMessageInteractionManageGroup.setLayoutManager(new ExStaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
                                request(false);
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
    }


    /**
     * 请求网络获取列表
     *
     * @param isClear
     */
    public void request(final boolean isClear) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getGroupList(
                    new JSONObject().put("member_id", Config.member_id)
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
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                                    if (jsonArray.length()>0){
                                        if (isClear){
                                            list.clear();
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            MessageInteractionManageGroupModel messageInteractionManageGroupModel=new MessageInteractionManageGroupModel();
                                            messageInteractionManageGroupModel.account=jsonArray.getJSONObject(i).getString("account");
                                            messageInteractionManageGroupModel.group_id=jsonArray.getJSONObject(i).getString("group_id");
                                            messageInteractionManageGroupModel.name=jsonArray.getJSONObject(i).getString("name");
                                            messageInteractionManageGroupModel.picture=jsonArray.getJSONObject(i).getString("picture");
                                            list.add(messageInteractionManageGroupModel);

                                        }
                                    }
                                    messageInteractionManageGroupAdapter.refresh(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:request-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
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
     * item被点击
     *
     * @param poistion
     * @param messageInteractionManageGroupModel
     * @param list
     */
    @Override
    public void onClickItem(int poistion, MessageInteractionManageGroupModel messageInteractionManageGroupModel, List<MessageInteractionManageGroupModel> list) {
        RongIM.getInstance().startGroupChat(getBaseFragmentActivityContext(), messageInteractionManageGroupModel.group_id, "5");//客服传6，商品详情客服传3，群组传5
    }

    @Override
    public void onDisplayOneImage(RatioImageView imageView, String url, int parentWidth, Context context) {

    }

    @Override
    public void onDisplayImage(RatioImageView imageView,RatioImageView closeButton, String url,int parentWidth, Context context) {
        LogUtils.e(Config.url+url);
        imageView.setTag(R.id.glide_tag,Config.url+url);
        addImageViewList(GlidUtils.downLoader(false,imageView,getBaseFragmentActivityContext()));
    }

    @Override
    public void onClickImage(int position, String url, List<String> urlList, Context context) {

    }

    @Override
    public void onClickImageColse(int position, String url, List<String> urlList, Context context) {

    }

    @Override
    public void onSamePhotos(List<String> mRejectUrlList) {

    }

}
