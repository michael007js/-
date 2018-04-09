package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedMessage;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnListViewCallBack;
import com.sss.car.model.MessageCommentModel;
import com.sss.car.model.MessageSystemModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityGoodsCommentList;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivitySharePostDetails;

import org.greenrobot.eventbus.EventBus;
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

import static android.R.id.list;


/**
 * 消息==>评价==>动态评价,帖子评价交易评价公用fragment
 * Created by leilei on 2017/10/19.
 */

@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class FragmentMessageCommentDymaicPostsPublic extends BaseFragment {


    @BindView(R.id.listview_fragment_message_comment_dymaic_posts_public)
    PullToRefreshListView listviewFragmentMessageCommentDymaicPostsPublic;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;

    OnListViewCallBack onListViewCallBack;
    String type;// trends动态，community帖子，order交易信息

    public int p = 1;
    String is_read;//	未读消息参数为1

    List<MessageCommentModel> list = new ArrayList<>();

    SSS_Adapter sss_adapter;


    @Override
    protected int setContentView() {
        return R.layout.fragment_message_comment_dymaic_posts_public;
    }

    public FragmentMessageCommentDymaicPostsPublic() {
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
                                listviewFragmentMessageCommentDymaicPostsPublic.setMode(PullToRefreshBase.Mode.BOTH);
                                listviewFragmentMessageCommentDymaicPostsPublic.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        managementOfMessageEvaluation();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        managementOfMessageEvaluation();
                                    }
                                });
                                managementOfMessageEvaluation();
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


    public FragmentMessageCommentDymaicPostsPublic(String is_read, String type, OnListViewCallBack onListViewCallBack) {
        this.is_read = is_read;
        this.type = type;
        this.onListViewCallBack = onListViewCallBack;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listviewFragmentMessageCommentDymaicPostsPublic = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        onListViewCallBack = null;
        type = null;
        is_read = null;

        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
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

    void initAdapter() {
        sss_adapter = new SSS_Adapter<MessageCommentModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_message_comment, list) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final MessageCommentModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name_item_fragment_message_comment, bean.username);
                helper.setText(R.id.date_item_fragment_message_comment, bean.create_time);
                helper.setText(R.id.type_item_fragment_message_comment, bean.title);
                LogUtils.e(bean.is_read);
                if ("0".equals(bean.is_read)) {
                    helper.setTextColor(R.id.date_item_fragment_message_comment, getResources().getColor(R.color.black));
                    helper.setTextColor(R.id.content_item_fragment_message_comment, getResources().getColor(R.color.black));
                    helper.setTextColor(R.id.type_item_fragment_message_comment, getResources().getColor(R.color.black));
                } else {
                    helper.setTextColor(R.id.date_item_fragment_message_comment, getResources().getColor(R.color.grayness));
                    helper.setTextColor(R.id.content_item_fragment_message_comment, getResources().getColor(R.color.grayness));
                    helper.setTextColor(R.id.type_item_fragment_message_comment, getResources().getColor(R.color.grayness));
                }
                if ("1".equals(list.get(position).is_top)) {
                    helper.setBackgroundRes(R.id.click_item_fragment_message_comment, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setText(R.id.top, "取消置顶");
                } else {
                    helper.setBackgroundRes(R.id.click_item_fragment_message_comment, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setText(R.id.top, "置顶");
                }

                addImageViewList(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.user_pic_item_fragment_message_comment)), 99999));
                if (StringUtils.isEmpty(bean.picture)) {
                    helper.setVisibility(R.id.pic_item_fragment_message_comment, View.GONE);
                } else {
                    addImageViewList(FrescoUtils.showImage(false, 120, 120, Uri.parse(Config.url + bean.picture), ((SimpleDraweeView) helper.getView(R.id.pic_item_fragment_message_comment)), 0f));
                    helper.setVisibility(R.id.pic_item_fragment_message_comment, View.VISIBLE);
                }
                helper.setText(R.id.content_item_fragment_message_comment, bean.remark);
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_synthesize("comment", bean.id, position);
                    }
                });
                helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        top("comment", bean.id, position);
                    }
                });
                helper.getView(R.id.click_item_fragment_message_comment).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothExpand();
                        return true;
                    }
                });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_reply_item_fragment_message_comment);
                helper.setItemChildClickListener(R.id.click_item_fragment_message_comment);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                if (!((SwipeMenuLayout) holder.getView(R.id.scoll)).isExpand) {
                    switch (view.getId()) {
                        case R.id.click_reply_item_fragment_message_comment:
//                        startActivity(new Intent(getBaseFragmentActivityContext(),ActivityInputKeyboard.class)
//                                .putExtra("type",type));
                            switch (list.get(position).type) {
                                // trends动态，community帖子，order交易信息
                                case "trends":
                                    if (getBaseFragmentActivityContext() != null) {
                                        getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                                                .putExtra("id", list.get(position).comment_id));
                                    }
                                    break;
                                case "community":
                                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                                            .putExtra("community_id", list.get(position).comment_id)
                                            .putExtra("is_show_keyboard", true));
                                    break;
                                case "order":
                                    if (getBaseFragmentActivityContext() != null) {
                                        getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                                                .putExtra("type", type)
                                                .putExtra("goods_id", list.get(position).comment_id));
                                    }
                                    break;
                                case "goods":

                                    break;
                            }
                            break;
                        case R.id.click_item_fragment_message_comment:
                            read_comment(list.get(position),position);

                            break;
                    }
                } else {
                    ((SwipeMenuLayout) holder.getView(R.id.scoll)).smoothClose();
                }

            }
        });
        listviewFragmentMessageCommentDymaicPostsPublic.setAdapter(sss_adapter);
        if (onListViewCallBack != null) {
            onListViewCallBack.onListViewCallBack(listviewFragmentMessageCommentDymaicPostsPublic.getRefreshableView());
        }
    }
    public void read_comment(final MessageCommentModel model, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.read_comment(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("id",model.id )

                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    jump(model);
                                    EventBus.getDefault().post(new ChangedMessage());
                                    list.get(position).is_read="1";
                                    sss_adapter.setList(list);

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

    private void jump(MessageCommentModel model) {
        switch (model.type) {
            // trends动态，community帖子，order交易信息
            case "trends":
                if (getBaseFragmentActivityContext() != null) {
                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                            .putExtra("id",model.comment_id));
                }
                break;
            case "community":
                getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                        .putExtra("community_id",model.comment_id)
                        .putExtra("is_show_keyboard", true));
                break;
            case "order":
                if (getBaseFragmentActivityContext() != null) {
                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                            .putExtra("type", type)
                            .putExtra("goods_id", model.comment_id));
                }
                break;
            case "goods":
                if (getBaseFragmentActivityContext() != null) {
                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsCommentList.class)
                            .putExtra("goods_id", model.comment_id));
                }

                break;
        }
    }

    /**
     * 侧滑删除
     */
    public void del_synthesize(final String type, final String ids, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_synthesize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("ids", ids)
                            .put("type", type)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    p = 1;
                                    managementOfMessageEvaluation();

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

    /**
     * 置顶/取消置顶
     */
    public void top(String type, String ids, final int position) {
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
                            .put("type", type)
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
                                    p = 1;
                                    managementOfMessageEvaluation();
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

    /**
     * 消息评价管理
     */
    public void managementOfMessageEvaluation() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.managementOfMessageEvaluation(
                    new JSONObject()
                            .put("is_read", is_read)
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .put("p", p)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentMessageCommentDymaicPostsPublic != null) {
                                listviewFragmentMessageCommentDymaicPostsPublic.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            MessageCommentModel messageCommentModel = new MessageCommentModel();
                                            messageCommentModel.id = jsonArray.getJSONObject(i).getString("id");
                                            messageCommentModel.type = jsonArray.getJSONObject(i).getString("type");
                                            messageCommentModel.remark = jsonArray.getJSONObject(i).getString("remark");
                                            messageCommentModel.comment_id = jsonArray.getJSONObject(i).getString("comment_id");
                                            messageCommentModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            messageCommentModel.friend_id = jsonArray.getJSONObject(i).getString("friend_id");
                                            messageCommentModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            messageCommentModel.status = jsonArray.getJSONObject(i).getString("status");
                                            messageCommentModel.face = jsonArray.getJSONObject(i).getString("face");
                                            messageCommentModel.username = jsonArray.getJSONObject(i).getString("username");
                                            messageCommentModel.title = jsonArray.getJSONObject(i).getString("title");
                                            messageCommentModel.is_top = jsonArray.getJSONObject(i).getString("is_top");
                                            messageCommentModel.is_read = jsonArray.getJSONObject(i).getString("is_read");
                                            messageCommentModel.picture = jsonArray.getJSONObject(i).getString("picture");
                                            list.add(messageCommentModel);
                                        }
                                        sss_adapter.setList(list);
                                    }
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

}
