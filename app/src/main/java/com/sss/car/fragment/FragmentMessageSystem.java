package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ViewPager.BannerVariation;
import com.blankj.utilcode.customwidget.banner.BannerConfig;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedMessage;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.model.MessageSystemModel;
import com.sss.car.rongyun.RongYunUtils;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityFeedback;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityMessageSystem;
import com.sss.car.view.ActivityShareInteractionManage;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;
import com.sss.car.wallet.WalletMy;
import com.sss.car.wallet.WalletMyCoupon;
import com.sss.car.wallet.WalletMyIntegral;

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
 * 消息==>系统==>积分消息,活动消息,商品消息,其他消息公用Fragment
 * Created by leilei on 2017/12/4.
 */
@SuppressLint("ValidFragment")
public class FragmentMessageSystem extends BaseFragment {
    @BindView(R.id.listview)
    PullToRefreshListView listView;
    Unbinder unbinder;

    SSS_Adapter sss_adapter;
    int p = 1;

    YWLoadingDialog ywLoadingDialog;
    View view;

    BannerVariation viewpagerFragmentMessageSystemHead;
    LinearLayout score, activity, goods, other;

    Gson gson = new Gson();

    List<MessageSystemModel> list = new ArrayList<>();

    public String classify_id;

    boolean isNeedHead = true;//是否需要显示头布局
    String is_read;//未读消息参数为1
    String type;//1积分信息，2交易信息，3活动信息，4软件消息  不传所有

    public FragmentMessageSystem() {
    }

    @Override
    public void onDestroy() {
        listView = null;
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        sss_adapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;

        view = null;
        if (viewpagerFragmentMessageSystemHead != null) {
            viewpagerFragmentMessageSystemHead.clear();
        }
        viewpagerFragmentMessageSystemHead = null;
        score = null;
        activity = null;
        goods = null;
        other = null;

        gson = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        is_read = null;
        type = null;
        super.onDestroy();
    }

    public FragmentMessageSystem(String is_read, String type, boolean isNeedHead, String classify_id) {
        this.is_read = is_read;
        this.type = type;
        this.isNeedHead = isNeedHead;
        this.classify_id = classify_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_message_system;
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
                                initAdv("8", classify_id);
                                initAdapter();
                                messageSystem();
                                listView.setMode(PullToRefreshBase.Mode.BOTH);
                                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        messageSystem();
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        messageSystem();
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

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isLoad && viewpagerFragmentMessageSystemHead != null) {
            if (isVisible) {
                if (viewpagerFragmentMessageSystemHead != null) {
                    viewpagerFragmentMessageSystemHead.startAutoPlay();
                }
                if (viewpagerFragmentMessageSystemHead != null) {
                    viewpagerFragmentMessageSystemHead.startAutoPlay();
                }
            } else {
                if (viewpagerFragmentMessageSystemHead != null) {
                    viewpagerFragmentMessageSystemHead.stopAutoPlay();
                }
                if (viewpagerFragmentMessageSystemHead != null) {
                    viewpagerFragmentMessageSystemHead.stopAutoPlay();
                }
            }
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

    void initAdv(String site_id, String classify_id) {
        AdvertisementManager.advertisement(site_id, classify_id, new AdvertisementManager.OnAdvertisementCallBack() {
            @Override
            public void onSuccessCallBack(List<AdvertisementModel> list) {
                if (viewpagerFragmentMessageSystemHead != null) {
                    viewpagerFragmentMessageSystemHead
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
                    viewpagerFragmentMessageSystemHead.start();
                    viewpagerFragmentMessageSystemHead.startAutoPlay();
                }
            }
        });
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<MessageSystemModel>(getBaseFragmentActivityContext(), R.layout.item_message_system) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final MessageSystemModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean.title);
                helper.setText(R.id.date, bean.send_time);
                helper.setText(R.id.content, bean.contents);
                if ("1".equals(list.get(position).is_top)) {
                    helper.setBackgroundRes(R.id.click_item_long, io.rong.imkit.R.drawable.rc_item_top_list_selector);
                    helper.setText(R.id.top, "取消置顶");
                } else {
                    helper.setBackgroundRes(R.id.click_item_long, io.rong.imkit.R.drawable.rc_item_list_selector);
                    helper.setText(R.id.top, "置顶");
                }

                if ("0".equals(bean.is_read)) {
                    helper.setTextColor(R.id.date, getResources().getColor(R.color.black));
                    helper.setTextColor(R.id.content, getResources().getColor(R.color.black));
                } else {
                    helper.setTextColor(R.id.date, getResources().getColor(R.color.grayness));
                    helper.setTextColor(R.id.content, getResources().getColor(R.color.grayness));
                }

                helper.getView(R.id.click_item_long).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((SwipeMenuLayout) helper.getView(R.id.scoll)).isExpand) {
                            read_system(list.get(position), position);

                        } else {
                            ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        }
                    }
                });
                helper.getView(R.id.top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        top("sys", list.get(position).id);
                    }
                });
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_synthesize("sys", list.get(position).id);
                    }
                });
                helper.getView(R.id.click_item_long).setOnLongClickListener(new View.OnLongClickListener() {
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
        if (isNeedHead) {
            listView.getRefreshableView().addHeaderView(init());
        }else {
            listView.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view,null));
        }
        listView.setAdapter(sss_adapter);
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
                                    messageSystem();

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
                                    messageSystem();
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

    private View init() {
        view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.fragment_message_system_head, null);
        viewpagerFragmentMessageSystemHead = $.f(view, R.id.viewpager_fragment_message_system_head);
        viewpagerFragmentMessageSystemHead.setDelayTime(Config.flash);
        score = $.f(view, R.id.click_score);
        activity = $.f(view, R.id.click_activity);
        goods = $.f(view, R.id.click_goods);
        other = $.f(view, R.id.click_other);
//        List<String> list1 = new ArrayList<>();
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
//        viewpagerFragmentMessageSystemHead
//                .setImages(list1)
//                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                .setDelayTime(3000)
//                .setImageLoader(new ImageLoaderInterface() {
//                    @Override
//                    public void displayImage(Context context, Object path, View imageView) {
//                        imageView.setTag(R.id.glide_tag, ((String) path));
//                        addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public View createImageView(Context context) {
//                        return null;
//                    }
//                });
//        viewpagerFragmentMessageSystemHead.start();
//        viewpagerFragmentMessageSystemHead.startAutoPlay();

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSystem.class)
                            .putExtra("type", "1")//1积分信息，2交易信息，3活动信息，4软件消息  不传所有
                            .putExtra("title", "积分消息")
                    );
                }
            }
        });

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSystem.class)
                            .putExtra("type", "2")//1积分信息，2交易信息，3活动信息，4软件消息  不传所有
                            .putExtra("title", "活动消息")
                    );
                }
            }
        });
        goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSystem.class)
                            .putExtra("type", "3")//1积分信息，2交易信息，3活动信息，4软件消息  不传所有
                            .putExtra("title", "商品消息")
                    );
                }
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMessageSystem.class)
                            .putExtra("type", "4")//1积分信息，2交易信息，3活动信息，4软件消息  不传所有
                            .putExtra("title", "其他消息")
                    );
                }
            }
        });

        return view;
    }

    public void messageSystem() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.messageSystem(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("is_read", is_read)
                            .put("type", type)
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
                                    if (p == 1) {
                                        list.clear();
                                    }
                                    p++;
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), MessageSystemModel.class));
                                    }
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

    public void read_system(final MessageSystemModel messageSystemModel, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.read_system(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("id", messageSystemModel.id)

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
                                    jump(messageSystemModel);
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

    void jump(MessageSystemModel model) {
        LogUtils.e(model.toString());
        // 0没有页面不跳转，1积分，2信誉，3优惠券，4钱包, 5商品详情，6店铺首页，7投诉提交页面，8建议提交页，9推广订单详情页，10粉丝
        if ("1".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), WalletMyIntegral.class));

            }
        } else if ("2".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", Config.member_id)
                        .putExtra("mode", "from_system_message"));

            }
        } else if ("3".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), WalletMyCoupon.class));

            }
        } else if ("4".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), WalletMy.class));

            }
        } else if ("5".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                        .putExtra("goods_id", model.friend_id)
                        .putExtra("type",type)
                );
            }

        } else if ("6".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfo.class)
                        .putExtra("shop_id", model.friend_id)
                );
            }
        } else if ("7".equals(model.target)) {

        } else if ("8".equals(model.target)) {

        } else if ("9".equals(model.target)) {

        }else if ("10".equals(model.target)) {
            if (getBaseFragmentActivityContext() != null) {
                startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", model.friend_id));
            }
        }
    }

}