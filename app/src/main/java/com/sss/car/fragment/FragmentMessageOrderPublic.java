package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedMessage;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnListViewCallBack;
import com.sss.car.dao.OnPullToRefreshListViewCallBack;
import com.sss.car.model.MessageCommentModel;
import com.sss.car.model.MessageOrderModel;
import com.sss.car.model.MessageOrderModel_Data;
import com.sss.car.order.OrderSOSAffirmBuyer;
import com.sss.car.order.OrderSOSDetails;
import com.sss.car.order.OrderSOSGrabList;
import com.sss.car.utils.CarUtils;

import org.greenrobot.eventbus.EventBus;
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
 * 消息==>订单公用页fragment
 * Created by leilei on 2017/10/19.
 */

@SuppressLint("ValidFragment")
public class FragmentMessageOrderPublic extends BaseFragment {
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    String status;//	1SOS订单，2收入订单，3支出订单
    OnPullToRefreshListViewCallBack onPullToRefreshListViewCallBack;
    OnListViewCallBack onListViewCallBack;
    @BindView(R.id.listview_fragment_message_order_public)
    PullToRefreshListView listviewFragmentMessageOrderPublic;
    List<MessageOrderModel> list = new ArrayList<>();

    SSS_Adapter sss_adapter;
    public int p = 1;
    String is_read;//	未读消息参数为1

    public FragmentMessageOrderPublic(String is_read, OnListViewCallBack onListViewCallBack, String status) {
        this.is_read = is_read;
        this.onListViewCallBack = onListViewCallBack;
        this.status = status;
    }

    public void changeList() {
        p = 1;
        messageOrderGetOrderInfo();
    }

    public FragmentMessageOrderPublic() {

    }

    public void setOnPullToRefreshListViewCallBack(OnPullToRefreshListViewCallBack onPullToRefreshListViewCallBack) {
        this.onPullToRefreshListViewCallBack = onPullToRefreshListViewCallBack;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        listviewFragmentMessageOrderPublic = null;
        status = null;
        onListViewCallBack = null;
        listviewFragmentMessageOrderPublic = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        is_read = null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_order_public;
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
                                listviewFragmentMessageOrderPublic.setMode(PullToRefreshBase.Mode.BOTH);
                                listviewFragmentMessageOrderPublic.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        messageOrderGetOrderInfo();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        messageOrderGetOrderInfo();
                                    }
                                });
                                initAdapter();
                                if (onListViewCallBack != null) {
                                    onListViewCallBack.onListViewCallBack(listviewFragmentMessageOrderPublic.getRefreshableView());
                                }
                                messageOrderGetOrderInfo();
                                if (onPullToRefreshListViewCallBack != null) {
                                    onPullToRefreshListViewCallBack.onPullToRefreshListViewCallBack(listviewFragmentMessageOrderPublic);
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

    public void setHeigh(int height) {
        listviewFragmentMessageOrderPublic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getActivity().getWindowManager().getDefaultDisplay().getHeight() - height));
    }

    @Override
    protected void stopLoad() {

    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<MessageOrderModel>(getBaseFragmentActivityContext(), R.layout.item_fragment_message_order_public) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final MessageOrderModel bean, SSS_Adapter instance) {
                helper.setText(R.id.date_item_item_fragment_message_order_public, bean.form_time);
                helper.setText(R.id.title_item_fragment_message_order_public, bean.remark);
                addImageViewList(FrescoUtils.showImage(false, 120, 120, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item_fragment_message_order_public)), 0f));
                helper.setText(R.id.content_item_fragment_message_order_public, bean.describe);
                if ("0".equals(bean.is_read)) {
                    helper.setTextColor(R.id.date_item_item_fragment_message_order_public, getResources().getColor(R.color.black));
                    helper.setTextColor(R.id.title_item_fragment_message_order_public, getResources().getColor(R.color.black));
                    helper.setTextColor(R.id.content_item_fragment_message_order_public, getResources().getColor(R.color.black));
                } else {
                    helper.setTextColor(R.id.date_item_item_fragment_message_order_public, getResources().getColor(R.color.grayness));
                    helper.setTextColor(R.id.title_item_fragment_message_order_public, getResources().getColor(R.color.grayness));
                    helper.setTextColor(R.id.content_item_fragment_message_order_public, getResources().getColor(R.color.grayness));
                }
                if ("1".equals(list.get(position).is_top)) {
                    helper.setBackgroundRes(R.id.click_item_item_fragment_message_order_public, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setText(R.id.top, "取消置顶");
                } else {
                    helper.setBackgroundRes(R.id.click_item_item_fragment_message_order_public, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setText(R.id.top, "置顶");
                }

                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_synthesize("order", bean.messages_id, position);
                    }
                });
                helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        top("order", bean.messages_id, position);
                    }
                });
                helper.getView(R.id.click_item_item_fragment_message_order_public).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothExpand();
                        return true;
                    }
                });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_item_fragment_message_order_public);

            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_item_fragment_message_order_public:
                        if (!((SwipeMenuLayout) holder.getView(R.id.scoll)).isExpand) {
                            read_order(list.get(position), position);

                        } else {
                            ((SwipeMenuLayout) holder.getView(R.id.scoll)).smoothClose();
                        }

                        break;
                }
            }
        });

        listviewFragmentMessageOrderPublic.setAdapter(sss_adapter);
    }

    public void read_order(final MessageOrderModel model, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.read_order(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("messages_id", model.messages_id)

                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    jump(model);
                                    EventBus.getDefault().post(new ChangedMessage());
                                    list.get(position).is_read = "1";
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

    private void jump(MessageOrderModel model) {
        LogUtils.e(model.messageOrderModel_data.member_id + "---" + Config.member_id);
        try {
            if ("1".equals(model.status)) {//1sos2收入3支出
                CarUtils.orderJump(
                        getBaseFragmentActivityContext(),
                        "sos",
                        Integer.valueOf(model.messageOrderModel_data.status),
                        model.messageOrderModel_data.ids,
                        false,
                        model.goods_comment,
                        model.is_comment,
                        model.exchange_id,
                        model.exchange_status);
            } else if ("2".equals(model.status)) {//1sos2收入3支出
                if ("1".equals(model.type)){//type含义：1车品2车服3sos
                    CarUtils.orderJump(
                            getBaseFragmentActivityContext(),
                            "goods",
                            Integer.valueOf(model.messageOrderModel_data.status),
                            model.messageOrderModel_data.ids,
                            true,
                            model.goods_comment,
                            model.is_comment,
                            model.exchange_id,
                            model.exchange_status);
                }else if ("2".equals(model.type)){//type含义：1车品2车服3sos
                    CarUtils.orderJump(
                            getBaseFragmentActivityContext(),
                            "service",
                            Integer.valueOf(model.messageOrderModel_data.status),
                            model.messageOrderModel_data.ids,
                            true,
                            model.goods_comment,
                            model.is_comment,
                            model.exchange_id,
                            model.exchange_status);
                }

            } else if ("3".equals(model.status)) {//1sos2收入3支出
                if ("1".equals(model.type)){//type含义：1车品2车服3sos
                    CarUtils.orderJump(
                            getBaseFragmentActivityContext(),
                            "goods",
                            Integer.valueOf(model.messageOrderModel_data.status),
                            model.messageOrderModel_data.ids,
                            false,
                            model.goods_comment,
                            model.is_comment,
                            model.exchange_id,
                            model.exchange_status);
                }else if ("2".equals(model.type)){//type含义：1车品2车服3sos
                    CarUtils.orderJump(
                            getBaseFragmentActivityContext(),
                            "service",
                            Integer.valueOf(model.messageOrderModel_data.status),
                            model.messageOrderModel_data.ids,
                            false,
                            model.goods_comment,
                            model.is_comment,
                            model.exchange_id,
                            model.exchange_status);
                }
            }
        } catch (NumberFormatException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "订单类型错误");
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
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    p = 1;
                                    messageOrderGetOrderInfo();

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
                                    messageOrderGetOrderInfo();
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

    public void messageOrderGetOrderInfo() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.messageOrderGetOrderInfo(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("is_read", is_read)
                            .put("status", status)//			1SOS订单，2收入订单，3支出订单
                            .put("p", p)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listviewFragmentMessageOrderPublic != null) {
                                listviewFragmentMessageOrderPublic.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (p == 1) {
                                        list.clear();
                                    }
                                    if (jsonArray.length() > 0) {
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            MessageOrderModel messageOrderModel = new MessageOrderModel();
                                            messageOrderModel.messages_id = jsonArray.getJSONObject(i).getString("messages_id");
                                            messageOrderModel.remark = jsonArray.getJSONObject(i).getString("remark");
                                            messageOrderModel.describe = jsonArray.getJSONObject(i).getString("describe");
                                            messageOrderModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            messageOrderModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            messageOrderModel.friend_id = jsonArray.getJSONObject(i).getString("friend_id");
                                            messageOrderModel.order_id = jsonArray.getJSONObject(i).getString("order_id");
                                            messageOrderModel.type = jsonArray.getJSONObject(i).getString("type");
                                            messageOrderModel.status = jsonArray.getJSONObject(i).getString("status");
                                            messageOrderModel.face = jsonArray.getJSONObject(i).getString("face");
                                            messageOrderModel.is_top = jsonArray.getJSONObject(i).getString("is_top");
                                            messageOrderModel.is_read = jsonArray.getJSONObject(i).getString("is_read");
                                            messageOrderModel.form_time = jsonArray.getJSONObject(i).getString("form_time");
                                            messageOrderModel.exchange_id = jsonArray.getJSONObject(i).getString("exchange_id");
                                            messageOrderModel.exchange_status = jsonArray.getJSONObject(i).getString("exchange_status");
                                            messageOrderModel.goods_comment = jsonArray.getJSONObject(i).getString("goods_comment");
                                            messageOrderModel.is_comment = jsonArray.getJSONObject(i).getString("is_comment");
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("data");
                                            MessageOrderModel_Data messageOrderModel_data = new MessageOrderModel_Data();
                                            messageOrderModel_data.ids = jsonObject1.getString("ids");
                                            messageOrderModel_data.type = jsonObject1.getString("type");
                                            messageOrderModel_data.status = jsonObject1.getString("status");
                                            messageOrderModel_data.member_id = jsonObject1.getString("member_id");
                                            messageOrderModel_data.member_pid = jsonObject1.getString("member_pid");
                                            messageOrderModel_data.shop_id = jsonObject1.getString("shop_id");
                                            messageOrderModel.messageOrderModel_data = messageOrderModel_data;
                                            list.add(messageOrderModel);
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
