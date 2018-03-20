package com.sss.car.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.model.MessageSynthesizeModel;
import com.sss.car.model.MessageSynthesizeModel_Data;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.view.ActivityDymaicDetails;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;


/**
 * 消息==>综合
 * Created by leilei on 2017/12/4.
 */

@SuppressWarnings("ALL")
public class FragmentMessageSynthesize extends BaseFragment {
    BannerVariation viewpagerUpFragmentGoodsHead;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    List<MessageSynthesizeModel> list = new ArrayList<>();
    SSS_Adapter sss_adapter;
    @BindView(R.id.listview)
    PullToRefreshListView listView;

    public String classify_id;

    public FragmentMessageSynthesize() {
    }

    public FragmentMessageSynthesize(String classify_id) {
        this.classify_id = classify_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_synthesize;
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
                                initAdv("8",classify_id);
                                init();
                                messageSynthesize();
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
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isLoad && viewpagerUpFragmentGoodsHead != null) {
            if (isVisible) {
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.startAutoPlay();
                }
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.startAutoPlay();
                }
            } else {
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.stopAutoPlay();
                }
                if (viewpagerUpFragmentGoodsHead != null) {
                    viewpagerUpFragmentGoodsHead.stopAutoPlay();
                }
            }
        }
    }


    void initAdv(String site_id, String classify_id ){
        AdvertisementManager.advertisement(site_id,classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                viewpagerUpFragmentGoodsHead
                        .setImages(list)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setDelayTime(Config.flash)
                        .setImageLoader(new ImageLoaderInterface() {
                            @Override
                            public void displayImage(Context context, Object path, View imageView) {
                                imageView.setTag(R.id.glide_tag,((AdvertisementModel) path).picture);
                                addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }

                            @Override
                            public View createImageView(Context context) {
                                return null;
                            }
                        });
                viewpagerUpFragmentGoodsHead.start();

                viewpagerUpFragmentGoodsHead.startAutoPlay();
            }
        });
    }


    private void init() {
        View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_message_synthesize_head, null);
        viewpagerUpFragmentGoodsHead = $.f(view, R.id.viewpager_fragment_message_synthesize_head);


        sss_adapter = new SSS_Adapter<MessageSynthesizeModel>(getBaseFragmentActivityContext(), R.layout.item_message_syntheesize) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final MessageSynthesizeModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name_item__adapter, list.get(position).title);
                helper.setText(R.id.content_item__adapter, list.get(position).contents);
                helper.setText(R.id.time_item__adapter, list.get(position).create_time);
                addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic_item__adapter)), 99999));
                if ("1".equals(list.get(position).is_top)) {
                    helper.setBackgroundRes(R.id.click_item__adapter_long, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setText(R.id.top, "取消置顶");
                } else {
                    helper.setBackgroundRes(R.id.click_item__adapter_long, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setText(R.id.content_item__adapter, list.get(position).contents);
                    helper.setText(R.id.top, "置顶");
                }
                helper.getView(R.id.click_item__adapter_long).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((SwipeMenuLayout) helper.getView(R.id.scoll)).isExpand) {
                            if ("sys".equals(bean.type)) {
                                ToastUtils.showLongToast(getBaseFragmentActivityContext(),"跳转系统");
                            } else if ("chat".equals(bean.type)) {
                                RongIM.getInstance().startPrivateChat(getBaseFragmentActivityContext(), bean.ids, bean.title);
                            } else if ("comment".equals(bean.type)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                                            .putExtra("id", list.get(position).ids));
                                }

                            } else if ("order".equals(bean.type)) {
                                ToastUtils.showLongToast(getBaseFragmentActivityContext(),"跳转订单");

                            }
                        } else {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        }
                    }
                });
                helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        top(list.get(position).type, list.get(position).ids);
                    }
                });
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_synthesize(list.get(position).type, list.get(position).ids);
                    }
                });
                helper.getView(R.id.click_item__adapter_long).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothExpand();
                        return true;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }

        };
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                messageSynthesize();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                messageSynthesize();
            }
        });
        listView.setAdapter(sss_adapter);
        listView.getRefreshableView().addHeaderView(viewpagerUpFragmentGoodsHead);


    }

    /**
     * 消息==>综合==>侧滑删除
     */
    public void del_synthesize(final String type, final String ids) {
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
                            if (listView!=null){
                                listView.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listView!=null){
                                listView.onRefreshComplete();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("chat".equals(type)){
                                        RongYunUtils.removeConversation(Conversation.ConversationType.PRIVATE, ids, new RongIMClient.ResultCallback() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                EventBus.getDefault().post(new ChangeInfoModel());
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {

                                            }
                                        });
                                    }
                                    p = 1;
                                    list.clear();
                                    messageSynthesize();

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
     * 消息综合置顶/取消置顶
     */
    public void top(String type, String ids) {
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
                                    list.clear();
                                    messageSynthesize();
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
     * 消息综合
     */
    public void messageSynthesize() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.messageSynthesize(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listView != null) {
                                listView.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listView != null) {
                                listView.onRefreshComplete();
                            }
                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        if (jsonArray.length() > 0) {
                                            if (p == 1) {
                                                list.clear();
                                            }
                                            p++;
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                MessageSynthesizeModel messageSynthesizeModel = new MessageSynthesizeModel();
                                                messageSynthesizeModel.type = jsonArray.getJSONObject(i).getString("type");
                                                messageSynthesizeModel.ids = jsonArray.getJSONObject(i).getString("ids");
                                                messageSynthesizeModel.face = jsonArray.getJSONObject(i).getString("face");
                                                messageSynthesizeModel.title = jsonArray.getJSONObject(i).getString("title");
                                                messageSynthesizeModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                                messageSynthesizeModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                                messageSynthesizeModel.is_read = jsonArray.getJSONObject(i).getString("is_read");
                                                messageSynthesizeModel.is_top = jsonArray.getJSONObject(i).getString("is_top");
                                                messageSynthesizeModel.time = jsonArray.getJSONObject(i).getString("time");
                                                MessageSynthesizeModel_Data messageSynthesizeModel_data = new MessageSynthesizeModel_Data();
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("data");
                                                if (jsonObject1.has("type")) {
                                                    messageSynthesizeModel_data.type = jsonObject1.getString("type");
                                                }
                                                if (jsonObject1.has("ids")) {
                                                    messageSynthesizeModel_data.ids = jsonObject1.getString("ids");
                                                }
                                                if (jsonObject1.has("member_id")) {
                                                    messageSynthesizeModel_data.member_id = jsonObject1.getString("member_id");
                                                }
                                                if (jsonObject1.has("shop_id")) {
                                                    messageSynthesizeModel_data.shop_id = jsonObject1.getString("shop_id");
                                                }
                                                if (jsonObject1.has("member_pid")) {
                                                    messageSynthesizeModel_data.member_pid = jsonObject1.getString("member_pid");
                                                }
                                                if (jsonObject1.has("status")) {
                                                    messageSynthesizeModel_data.status = jsonObject1.getString("status");
                                                }
                                                messageSynthesizeModel.data = messageSynthesizeModel_data;
                                                list.add(messageSynthesizeModel);
                                            }
                                            sss_adapter.setList(list);


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-1");
                                        e.printStackTrace();
                                    }
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
    }

}
