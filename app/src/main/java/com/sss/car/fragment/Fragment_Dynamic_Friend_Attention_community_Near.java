package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.MyRecycleview.ExStaggeredGridLayoutManager;
import com.blankj.utilcode.activity.ActivityInputKeyboard;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.NineAdapter2;
import com.sss.car.custom.ListViewVariation;
import com.sss.car.dao.CustomRefreshLayoutCallBack2;
import com.sss.car.dao.DymaicDetailsOperationCallBack;
import com.sss.car.dao.DymaicDetailsPraiseAdapterCallBack;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.dao.ShareDynamicAdapterOperationCallBack;
import com.sss.car.model.DymaicDetailsCommentModel;
import com.sss.car.model.DymaicDetailsPraiseModel;
import com.sss.car.model.DymaicModel;
import com.sss.car.model.ShareDynamicModel;
import com.sss.car.model.ShareDynamic_GoodsModel;
import com.sss.car.utils.ShareUtils;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityUserInfo;

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
 * 我的动态，好友动态，关注动态，社区动态，周边动态公用fragment
 * Created by leilei on 2017/9/10.
 */

@SuppressWarnings("ALL")
@SuppressLint("ValidFragment")
public class Fragment_Dynamic_Friend_Attention_community_Near extends BaseFragment implements
        DymaicOperationCallBack, LoadImageCallBack, NineAdapter2OperationCallBack,
        DymaicDetailsOperationCallBack, DymaicDetailsPraiseAdapterCallBack, ShareDynamicAdapterOperationCallBack, PullToRefreshBase.OnRefreshListener2, KeyboardInput.KeyboardInputOperationCallBack {
    Unbinder unbinder;
    public int p = 1;
    String type = "1";//（type == 1所有，2我的，3好友，4关注，5社区，6周边）
    boolean isShowOnFrontPager;
    List<ShareDynamicModel> list = new ArrayList<>();

    CustomRefreshLayoutCallBack2 addHeadViewCallBack2;
    @BindView(R.id.list_fragment_dynamic_friend_attention_community_near)
    ListViewVariation listFragmentDynamicFriendAttentionCommunityNear;

    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.input_fragment_dynamic_friend_attention_community_near)
    KeyboardInput inputFragmentDynamicFriendAttentionCommunityNear;
    boolean isNeedEmptyView=false;

    public Fragment_Dynamic_Friend_Attention_community_Near() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        addHeadViewCallBack2 = null;
        inputFragmentDynamicFriendAttentionCommunityNear = null;

        String type = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        list = null;

        if (listFragmentDynamicFriendAttentionCommunityNear != null) {
            listFragmentDynamicFriendAttentionCommunityNear.clear();
        }
        listFragmentDynamicFriendAttentionCommunityNear = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
    }

    public ListViewVariation getListFragmentDynamicFriendAttentionCommunityNear() {
        return listFragmentDynamicFriendAttentionCommunityNear;
    }

    public Fragment_Dynamic_Friend_Attention_community_Near(boolean isNeedEmptyView,String type, boolean isShowOnFrontPager, CustomRefreshLayoutCallBack2 addHeadViewCallBack2) {
        this.isNeedEmptyView=isNeedEmptyView;
        this.type = type;
        this.isShowOnFrontPager = isShowOnFrontPager;
        this.addHeadViewCallBack2 = addHeadViewCallBack2;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_dynamic_friend_attention_community_near;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listFragmentDynamicFriendAttentionCommunityNear.setOnRefreshListener2(Fragment_Dynamic_Friend_Attention_community_Near.this);
                                inputFragmentDynamicFriendAttentionCommunityNear.setKeyboardInputOperationCallBack(Fragment_Dynamic_Friend_Attention_community_Near.this);
                                try {
                                    getDymaic();
                                } catch (JSONException e) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    e.printStackTrace();
                                }
                                if (addHeadViewCallBack2 != null) {
                                    addHeadViewCallBack2.onAdd(listFragmentDynamicFriendAttentionCommunityNear);
                                }
                                if (isNeedEmptyView){
//                                    listFragmentDynamicFriendAttentionCommunityNear.addEmptyView(getBaseFragmentActivityContext());
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

    /**
     * 获取动态评论。点赞。分享数据
     *
     * @param cid
     */
    public void getDymaicPraiseCommentSharenumber(final String trends_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getDymaicPraiseCommentSharenumber(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("trends_id", trends_id)
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
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).trends_id.equals(trends_id)) {
                                            list.get(i).comment_count = jsonObject.getJSONObject("data").getString("comment_count");
                                            list.get(i).transmit = jsonObject.getJSONObject("data").getString("transmit");
                                            list.get(i).likes = jsonObject.getJSONObject("data").getString("likes");
                                            list.get(i).is_praise = jsonObject.getJSONObject("data").getString("is_praise");

                                            return;
                                        }
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


    /**
     * 获取动态
     *
     * @throws JSONException
     */
    public void getDymaic() throws JSONException {
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getDymaic(
                new JSONObject()
                        .put("type", type)
                        .put("gps",Config.latitude+","+Config.longitude)
                        .put("p", String.valueOf(p))
                        .put("member_id", Config.member_id)
                        .toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (listFragmentDynamicFriendAttentionCommunityNear != null) {
                            listFragmentDynamicFriendAttentionCommunityNear.onRefreshComplete();
                        }
                        if (getBaseFragmentActivityContext() != null) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (listFragmentDynamicFriendAttentionCommunityNear != null) {
                            listFragmentDynamicFriendAttentionCommunityNear.onRefreshComplete();
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
                                        if (p == 1) {
                                            list.clear();
                                            listFragmentDynamicFriendAttentionCommunityNear.refreshData();
                                        }
                                        p++;

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ShareDynamicModel shareDynamicModel = new ShareDynamicModel();
                                            shareDynamicModel.trends_id = jsonArray.getJSONObject(i).getString("trends_id");
                                            shareDynamicModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                            shareDynamicModel.is_praise = jsonArray.getJSONObject(i).getString("is_praise");
                                            shareDynamicModel.city_path = jsonArray.getJSONObject(i).getString("city_path");
                                            shareDynamicModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            shareDynamicModel.trends_pid = jsonArray.getJSONObject(i).getString("trends_pid");
                                            shareDynamicModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            shareDynamicModel.looks = jsonArray.getJSONObject(i).getString("looks");
                                            shareDynamicModel.likes = jsonArray.getJSONObject(i).getString("likes");
                                            shareDynamicModel.transmit = jsonArray.getJSONObject(i).getString("transmit");
                                            shareDynamicModel.face = jsonArray.getJSONObject(i).getString("face");
                                            shareDynamicModel.username = jsonArray.getJSONObject(i).getString("username");
                                            shareDynamicModel.day = jsonArray.getJSONObject(i).getString("day");
                                            shareDynamicModel.month = jsonArray.getJSONObject(i).getString("month");
                                            shareDynamicModel.week = jsonArray.getJSONObject(i).getString("week");
                                            shareDynamicModel.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                            shareDynamicModel.comment_count = jsonArray.getJSONObject(i).getString("comment_count");
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                            List<String> picList = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                picList.add(jsonArray1.getString(j));
                                            }
                                            shareDynamicModel.picture = picList;


                                            JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("likes_list");
                                            List<DymaicDetailsPraiseModel> praiseList = new ArrayList<>();
                                            for (int j = 0; j < jsonArray2.length(); j++) {
                                                DymaicDetailsPraiseModel shareDymaicPraiseModel = new DymaicDetailsPraiseModel();
                                                shareDymaicPraiseModel.id = jsonArray2.getJSONObject(j).getString("id");
                                                shareDymaicPraiseModel.member_id = jsonArray2.getJSONObject(j).getString("member_id");
                                                shareDymaicPraiseModel.face = jsonArray2.getJSONObject(j).getString("face");
                                                shareDymaicPraiseModel.username = jsonArray2.getJSONObject(j).getString("username");
                                                praiseList.add(shareDymaicPraiseModel);
                                            }
                                            shareDynamicModel.likes_list = praiseList;

                                            JSONArray jsonArray3 = jsonArray.getJSONObject(i).getJSONArray("comment_list");
                                            List<DymaicDetailsCommentModel> commentList = new ArrayList<>();
                                            for (int j = 0; j < jsonArray3.length(); j++) {
                                                DymaicDetailsCommentModel shareDynamicCommentModel = new DymaicDetailsCommentModel();

                                                shareDynamicCommentModel.contents = jsonArray3.getJSONObject(j).getString("contents");
                                                shareDynamicCommentModel.user_name = jsonArray3.getJSONObject(j).getString("user_name");//回复其他评论,被回复的人的昵称

                                                shareDynamicCommentModel.comment_id = jsonArray3.getJSONObject(j).getString("comment_id");
                                                shareDynamicCommentModel.member_pid = jsonArray3.getJSONObject(j).getString("member_pid");
                                                shareDynamicCommentModel.username = jsonArray3.getJSONObject(j).getString("username");

                                                shareDynamicCommentModel.trends_id = jsonArray3.getJSONObject(j).getString("trends_id");
                                                shareDynamicCommentModel.member_id = jsonArray3.getJSONObject(j).getString("member_id");
                                                shareDynamicCommentModel.create_time = jsonArray3.getJSONObject(j).getString("create_time");
                                                shareDynamicCommentModel.face = jsonArray3.getJSONObject(j).getString("face");
                                                commentList.add(shareDynamicCommentModel);
                                            }
                                            shareDynamicModel.comment_list = commentList;

                                            List<ShareDynamic_GoodsModel> list1 = new ArrayList<>();
                                            JSONArray jsonArray4 = jsonArray.getJSONObject(i).getJSONArray("goods_data");
                                            for (int j = 0; j < jsonArray4.length(); j++) {
                                                ShareDynamic_GoodsModel shareDynamic_goodsModel = new ShareDynamic_GoodsModel();
                                                shareDynamic_goodsModel.goods_id = jsonArray4.getJSONObject(j).getString("goods_id");
                                                shareDynamic_goodsModel.goods_title = jsonArray4.getJSONObject(j).getString("goods_title");
                                                shareDynamic_goodsModel.solgan = jsonArray4.getJSONObject(j).getString("slogan");
                                                shareDynamic_goodsModel.mater_map = jsonArray4.getJSONObject(j).getString("master_map");
                                                shareDynamic_goodsModel.distance = jsonArray4.getJSONObject(j).getString("distance");
                                                shareDynamic_goodsModel.goods_type = jsonArray4.getJSONObject(j).getString("goods_type");
                                                shareDynamic_goodsModel.price = jsonArray4.getJSONObject(j).getString("price");
                                                list1.add(shareDynamic_goodsModel);
                                            }
                                            shareDynamicModel.goods_data = list1;
                                            list.add(shareDynamicModel);
                                            showData(shareDynamicModel, list.size() - 1);

                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    public ShareDynamicModel shareDynamicModel;


    /**
     * 显示布局
     *
     * @param shareDynamicModel
     * @param position
     */
    void showData(final ShareDynamicModel shareDynamicModel, final int position) {
        if ("".equals(shareDynamicModel.goods_id)) {//普通动态
            View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.item_share_dynamic_adapter_dynamic, null);
            FrameLayout bg_item_share_dynamic_adapter_dynamic = $.f(view, R.id.bg_item_share_dynamic_adapter_dynamic);
            TextView month_item_share_dynamic_adapter_dynamic = $.f(view, R.id.month_item_share_dynamic_adapter_dynamic);
            TextView day_item_share_dynamic_adapter_dynamic = $.f(view, R.id.day_item_share_dynamic_adapter_dynamic);

            InnerGridView nine_view_item_share_dynamic_adapter_dynamic = $.f(view, R.id.nine_view_item_share_dynamic_adapter_dynamic);
            LinearLayout click_item_share_dynamic_adapter_dynamic = $.f(view, R.id.click_item_share_dynamic_adapter_dynamic);
            SimpleDraweeView pic_item_share_dynamic_adapter_dynamic = $.f(view, R.id.pic_item_share_dynamic_adapter_dynamic);
            TextView nikename_item_share_dynamic_adapter_dynamic = $.f(view, R.id.nikename_item_share_dynamic_adapter_dynamic);
            TextView content_item_share_dynamic_adapter_dynamic = $.f(view, R.id.content_item_share_dynamic_adapter_dynamic);
            TextView loacation_item_share_dynamic_adapter_dynamic = $.f(view, R.id.loacation_item_share_dynamic_adapter_dynamic);
            TextView time_item_share_dynamic_adapter_dynamic = $.f(view, R.id.time_item_share_dynamic_adapter_dynamic);
            LinearLayout click_praise_item_share_dynamic_adapter_dynamic = $.f(view, R.id.click_praise_item_share_dynamic_adapter_dynamic);
            final SimpleDraweeView praise_item_share_dynamic_adapter_dynamic = $.f(view, R.id.praise_item_share_dynamic_adapter_dynamic);
            final TextView show_praise_item_share_dynamic_adapter_dynamic = $.f(view, R.id.show_praise_item_share_dynamic_adapter_dynamic);

            LinearLayout click_comment_item_share_dynamic_adapter_dynamic = $.f(view, R.id.click_comment_item_share_dynamic_adapter_dynamic);
            final TextView show_comment_item_share_dynamic_adapter_dynamic = $.f(view, R.id.show_comment_item_share_dynamic_adapter_dynamic);

            LinearLayout click_share_item_share_dynamic_adapter_dynamic = $.f(view, R.id.click_share_item_share_dynamic_adapter_dynamic);
            final TextView show_share_item_share_dynamic_adapter_dynamic = $.f(view, R.id.show_share_item_share_dynamic_adapter_dynamic);

            HorizontalListView praise_list_item_share_dynamic_adapter_dynamic = $.f(view, R.id.praise_list_item_share_dynamic_adapter_dynamic);

            ListView listview_item_share_dynamic_adapter_dynamic = $.f(view, R.id.listview_item_share_dynamic_adapter_dynamic);

         /*如果在首页显示,则评论列表,定位与点赞列表不显示*/
            //（type == 1所有，2我的，3好友，4关注，5社区，6周边）
            if ("1".equals(type)) {

                praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
//                listview_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
//                loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                bg_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                pic_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                onLoad(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + shareDynamicModel.face), pic_item_share_dynamic_adapter_dynamic, 99999));
//                LogUtils.e(Config.url + shareDynamicModel.face);
            } else if ("2".equals(type)) {
                praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
//                listview_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
//                listview_item_share_dynamic_adapter_dynamic.setAdapter(new ActivityDymaicDetailsAdapter(getBaseFragmentActivityContext(), shareDynamicModel.comment_list, Fragment_Dynamic_Friend_Attention_community_Near.this, Fragment_Dynamic_Friend_Attention_community_Near.this));
//                praise_list_item_share_dynamic_adapter_dynamic.setAdapter(new DymaicDetailsPraiseAdapter(
//                        shareDynamicModel.likes_list,
//                        getBaseFragmentActivityContext(), this, this));
//                loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
//                loacation_item_share_dynamic_adapter_dynamic.setText(list.get(position).city_path);
//                loacation_item_share_dynamic_adapter_dynamic.setTextColor(getBaseFragmentActivityContext().getResources().getColor(R.color.mainColor));
                bg_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                pic_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
                month_item_share_dynamic_adapter_dynamic.setText(shareDynamicModel.month);
                day_item_share_dynamic_adapter_dynamic.setText(shareDynamicModel.day);
                switch (shareDynamicModel.week) {
                    case "0":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_one));
                        break;
                    case "1":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_two));
                        break;
                    case "2":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_three));
                        break;
                    case "3":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_four));
                        break;
                    case "4":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_five));
                        break;
                    case "5":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_six));
                        break;
                    case "6":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_dynamic, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_seven));
                        break;
                }
            } else {
                pic_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
                onLoad(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + shareDynamicModel.face), pic_item_share_dynamic_adapter_dynamic, 99999));
//                loacation_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
//                loacation_item_share_dynamic_adapter_dynamic.setText(list.get(position).city_path);
//                loacation_item_share_dynamic_adapter_dynamic.setTextColor(getBaseFragmentActivityContext().getResources().getColor(R.color.mainColor));
                bg_item_share_dynamic_adapter_dynamic.setVisibility(View.GONE);
//                praise_list_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
//                listview_item_share_dynamic_adapter_dynamic.setVisibility(View.VISIBLE);
//                listview_item_share_dynamic_adapter_dynamic.setAdapter(new ActivityDymaicDetailsAdapter(getBaseFragmentActivityContext(), shareDynamicModel.comment_list, Fragment_Dynamic_Friend_Attention_community_Near.this, Fragment_Dynamic_Friend_Attention_community_Near.this));
//                praise_list_item_share_dynamic_adapter_dynamic.setAdapter(new DymaicDetailsPraiseAdapter(
//                        shareDynamicModel.likes_list,
//                        getBaseFragmentActivityContext(), this, this));
            }

            pic_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                                .putExtra("id", list.get(position).member_id));
                    }
                }
            });
            nikename_item_share_dynamic_adapter_dynamic.setText(list.get(position).username);
            content_item_share_dynamic_adapter_dynamic.setText(list.get(position).contents);
            time_item_share_dynamic_adapter_dynamic.setText(list.get(position).create_time);
            show_praise_item_share_dynamic_adapter_dynamic.setText(list.get(position).likes);
            show_comment_item_share_dynamic_adapter_dynamic.setText(list.get(position).comment_count);
            show_share_item_share_dynamic_adapter_dynamic.setText(list.get(position).transmit);
            nine_view_item_share_dynamic_adapter_dynamic.setNumColumns(3);
            nine_view_item_share_dynamic_adapter_dynamic.setHorizontalSpacing(2);
            nine_view_item_share_dynamic_adapter_dynamic.setVerticalSpacing(2);
            nine_view_item_share_dynamic_adapter_dynamic.setAdapter(new NineAdapter2(
                    300, 300,
                    shareDynamicModel.picture,
                    getBaseFragmentActivityContext(),
                    Fragment_Dynamic_Friend_Attention_community_Near.this,
                    Fragment_Dynamic_Friend_Attention_community_Near.this));

            if ("1".equals(shareDynamicModel.is_praise)) {
                onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_yes), praise_item_share_dynamic_adapter_dynamic, 0f));
            } else {
                onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_no), praise_item_share_dynamic_adapter_dynamic, 0f));
            }
            click_praise_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praise(shareDynamicModel.trends_id, show_praise_item_share_dynamic_adapter_dynamic, shareDynamicModel, praise_item_share_dynamic_adapter_dynamic);
                }
            });
            click_comment_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (isShowOnFrontPager == false) {
//                        if (getBaseFragmentActivityContext()!=null){
//                            startActivity(new Intent(getBaseFragmentActivityContext(),ActivityInputKeyboard.class)
//                                    .putExtra("type","dymaic"));
//                        }
//                        inputFragmentDynamicFriendAttentionCommunityNear.visibility(View.GONE);
//                        Fragment_Dynamic_Friend_Attention_community_Near.this.shareDynamicModel = shareDynamicModel;
//                    } else {
//                        if (getBaseFragmentActivityContext() != null) {
//                            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
//                                    .putExtra("id", shareDynamicModel.trends_id));
//                        }
//                    }
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityInputKeyboard.class)
                                .putExtra("type", "dymaic"));
                    }
                    inputFragmentDynamicFriendAttentionCommunityNear.visibility(View.GONE);
                    Fragment_Dynamic_Friend_Attention_community_Near.this.shareDynamicModel = shareDynamicModel;
                }
            });
            click_share_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.prepareShare(ywLoadingDialog,getActivity(),"trends",shareDynamicModel.trends_id);
                }
            });
            click_item_share_dynamic_adapter_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getBaseFragmentActivityContext() != null) {
                        getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                                .putExtra("id", shareDynamicModel.trends_id));
                    }
                }
            });
            listFragmentDynamicFriendAttentionCommunityNear.addData(view);
        } else {//商品动态
            View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.item_share_dynamic_adapter_goods, null);
            FrameLayout bg_item_share_dynamic_adapter_goods = $.f(view, R.id.bg_item_share_dynamic_adapter_goods);
            TextView month_item_share_dynamic_adapter_goods = $.f(view, R.id.month_item_share_dynamic_adapter_goods);
            TextView day_item_share_dynamic_adapter_goods = $.f(view, R.id.day_item_share_dynamic_adapter_goods);
            LinearLayout click_item_share_dynamic_adapter_goods = $.f(view, R.id.click_item_share_dynamic_adapter_goods);
            TextView content_item_share_dynamic_adapter_goods = $.f(view, R.id.content_item_share_dynamic_adapter_goods);
            SimpleDraweeView pic_item_share_dynamic_adapter_goods = $.f(view, R.id.pic_item_share_dynamic_adapter_goods);
            TextView nikename_item_share_dynamic_adapter_goods = $.f(view, R.id.nikename_item_share_dynamic_adapter_goods);
            TextView title_item_share_dynamic_adapter_goods = $.f(view, R.id.title_item_share_dynamic_adapter_goods);
            TextView price_item_share_dynamic_adapter_goods = $.f(view, R.id.price_item_share_dynamic_adapter_goods);
            TextView loacation_item_share_dynamic_adapter_goods = $.f(view, R.id.loacation_item_share_dynamic_adapter_goods);
            TextView time_item_share_dynamic_adapter_goods = $.f(view, R.id.time_item_share_dynamic_adapter_goods);
            LinearLayout click_praise_item_share_dynamic_adapter_goods = $.f(view, R.id.click_praise_item_share_dynamic_adapter_goods);
            final SimpleDraweeView praise_item_share_dynamic_adapter_goods = $.f(view, R.id.praise_item_share_dynamic_adapter_goods);
            final TextView show_praise_item_share_dynamic_adapter_goods = $.f(view, R.id.show_praise_item_share_dynamic_adapter_goods);
            LinearLayout click_comment_item_share_dynamic_adapter_goods = $.f(view, R.id.click_comment_item_share_dynamic_adapter_goods);
            TextView show_comment_item_share_dynamic_adapter_goods = $.f(view, R.id.show_comment_item_share_dynamic_adapter_goods);
            LinearLayout click_share_item_share_dynamic_adapter_goods = $.f(view, R.id.click_share_item_share_dynamic_adapter_goods);
            TextView show_share_item_share_dynamic_adapter_goods = $.f(view, R.id.show_share_item_share_dynamic_adapter_goods);
            LinearLayout parent=$.f(view,R.id.parent);
//            InnerListview listview = $.f(view, R.id.listview);
            RecyclerView recyclerView=$.f(view,R.id.recyclerView);
            //（type == 1所有，2我的，3好友，4关注，5社区，6周边）
            parent.setVisibility(View.GONE);
            if ("2".equals(type)) {
                bg_item_share_dynamic_adapter_goods.setVisibility(View.VISIBLE);
                pic_item_share_dynamic_adapter_goods.setVisibility(View.GONE);
                month_item_share_dynamic_adapter_goods.setText(shareDynamicModel.month);
                day_item_share_dynamic_adapter_goods.setText(shareDynamicModel.day);
                switch (shareDynamicModel.week) {
                    case "0":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_one));
                        break;
                    case "1":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_two));
                        break;
                    case "2":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_three));
                        break;
                    case "3":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_four));
                        break;
                    case "4":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_five));
                        break;
                    case "5":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_six));
                        break;
                    case "6":
                        APPOftenUtils.setBackgroundOfVersion(bg_item_share_dynamic_adapter_goods, getBaseFragmentActivityContext().getResources().getDrawable(R.drawable.bg_color_seven));
                        break;
                }
            } else {
                bg_item_share_dynamic_adapter_goods.setVisibility(View.GONE);
                pic_item_share_dynamic_adapter_goods.setVisibility(View.VISIBLE);
            }
            if (shareDynamicModel.goods_data.size() > 0) {
//                listview.setAdapter(new SSS_Adapter<ShareDynamic_GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_goods_service, shareDynamicModel.goods_data) {
//                    @Override
//                    protected void setView(SSS_HolderHelper helper, int position, final ShareDynamic_GoodsModel bean, SSS_Adapter instance) {
//                        LogUtils.e(bean.toString());
//                        helper.setItemChildClickListener(R.id.click_item_goods_service_list_adapter);
//                        addImageViewList(FrescoUtils.showImage(false, 140, 100, Uri.parse(Config.url + bean.mater_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_goods_service_list_adapter)), 0f));
//                        helper.setText(R.id.title_item_goods_service_list_adapter, bean.goods_title);
//                        helper.setText(R.id.slogan_item_goods_service_list_adapter, bean.solgan);
//                        helper.setText(R.id.price_item_goods_service_list_adapter, "¥" + bean.price);
//                        helper.setText(R.id.distance_item_goods_service_list_adapter, bean.distance);
//                        helper.getView(R.id.click_item_goods_service_list_adapter).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (getBaseFragmentActivityContext() != null) {
//                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
//                                            .putExtra("goods_id", bean.goods_id)
//                                            .putExtra("type", bean.goods_type));
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected void setItemListener(SSS_HolderHelper helper) {
//
//                    }
//                });
                recyclerView.setLayoutManager(new ExStaggeredGridLayoutManager(1,OrientationHelper.VERTICAL));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(new SSS_RVAdapter<ShareDynamic_GoodsModel>(recyclerView,R.layout.item_goods_service,shareDynamicModel.goods_data) {
                    @Override
                    protected void setView(SSS_HolderHelper helper, int position, final ShareDynamic_GoodsModel bean) {
                        LogUtils.e(bean.toString());
                        helper.setItemChildClickListener(R.id.click_item_goods_service_list_adapter);
                        addImageViewList(FrescoUtils.showImage(false, 140, 100, Uri.parse(Config.url + bean.mater_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_goods_service_list_adapter)), 0f));
                        helper.setText(R.id.title_item_goods_service_list_adapter, bean.goods_title);
                        helper.setText(R.id.slogan_item_goods_service_list_adapter, bean.solgan);
                        helper.setText(R.id.price_item_goods_service_list_adapter, "¥" + bean.price);
                        helper.setText(R.id.distance_item_goods_service_list_adapter, bean.distance);
                        helper.getView(R.id.click_item_goods_service_list_adapter).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                                            .putExtra("goods_id", bean.goods_id)
                                            .putExtra("type", bean.goods_type));
                                }
                            }
                        });
                    }

                    @Override
                    protected void setItemListener(SSS_HolderHelper helper) {

                    }

                });

            }
            content_item_share_dynamic_adapter_goods.setText(shareDynamicModel.contents);
            nikename_item_share_dynamic_adapter_goods.setText(shareDynamicModel.username);
//            title_item_share_dynamic_adapter_goods.setText(shareDynamicModel.goods_title);
//            price_item_share_dynamic_adapter_goods.setText(shareDynamicModel.price);
//            loacation_item_share_dynamic_adapter_goods.setText(shareDynamicModel.distance);
            time_item_share_dynamic_adapter_goods.setText(shareDynamicModel.create_time);
            show_praise_item_share_dynamic_adapter_goods.setText(shareDynamicModel.likes);
            show_comment_item_share_dynamic_adapter_goods.setText(shareDynamicModel.comment_count);
            show_share_item_share_dynamic_adapter_goods.setText(shareDynamicModel.transmit);
            if ("1".equals(shareDynamicModel.is_praise)) {
                onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_yes), praise_item_share_dynamic_adapter_goods, 0f));
            } else {
                onLoad(FrescoUtils.showImage(true, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_no), praise_item_share_dynamic_adapter_goods, 0f));
            }
            onLoad(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + shareDynamicModel.face), pic_item_share_dynamic_adapter_goods, 9999f));
            click_praise_item_share_dynamic_adapter_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    praise(shareDynamicModel.trends_id, show_praise_item_share_dynamic_adapter_goods, shareDynamicModel, praise_item_share_dynamic_adapter_goods);
                }
            });
            click_comment_item_share_dynamic_adapter_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowOnFrontPager == false) {
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityInputKeyboard.class)
                                    .putExtra("type", "dymaic"));
                        }
                        inputFragmentDynamicFriendAttentionCommunityNear.visibility(View.GONE);
                        Fragment_Dynamic_Friend_Attention_community_Near.this.shareDynamicModel = shareDynamicModel;
                    } else {
                        if (getBaseFragmentActivityContext() != null) {
                            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityDymaicDetails.class)
                                    .putExtra("id", shareDynamicModel.trends_id));
                        }
                    }
                }
            });
            click_share_item_share_dynamic_adapter_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.prepareShare(ywLoadingDialog,getActivity(),"trends",shareDynamicModel.trends_id);
                }
            });
            listFragmentDynamicFriendAttentionCommunityNear.addData(view);
        }
    }

    /**
     * 动态评论
     *
     * @param content
     */
    public void comment(ShareDynamicModel model, String content, final EditText editText) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.commentDymaic(
                    new JSONObject()
                            .put("contents", content)
                            .put("trends_id", model.trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }

                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
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
                                        inputFragmentDynamicFriendAttentionCommunityNear.visibility(View.GONE);
                                        if (editText != null) {
                                            editText.setText("");
                                        }
                                        p = 1;
                                        getDymaic();
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * 动态点赞
     *
     * @param trends_id
     */
    public void praise(String trends_id, final TextView praise_number, final ShareDynamicModel shareDynamicModel, final SimpleDraweeView praise) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.praise(
                    new JSONObject()
                            .put("type", "trends")//trends:动态点赞
                            .put("likes_id", trends_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
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
                                        shareDynamicModel.is_praise = jsonObject.getJSONObject("data").getString("code");
                                        if ("1".equals(shareDynamicModel.is_praise)) {
                                            shareDynamicModel.likes = String.valueOf(Integer.valueOf(shareDynamicModel.likes) + 1);
                                            onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_yes), praise, 0f));
                                            praise_number.setText(shareDynamicModel.likes);
                                        } else {
                                            onLoad(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + getBaseFragmentActivityContext().getPackageName() + "/" + R.mipmap.logo_praise_no), praise, 0f));
                                            shareDynamicModel.likes = String.valueOf(Integer.valueOf(shareDynamicModel.likes) - 1);
                                            praise_number.setText(shareDynamicModel.likes);
                                            for (int i = 0; i < shareDynamicModel.likes_list.size(); i++) {
                                                if (shareDynamicModel.likes_list.get(i).member_id.equals(Config.member_id)) {
                                                    shareDynamicModel.likes_list.remove(i);
                                                }
                                            }
                                        }
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * 动态转发
     *
     * @throws JSONException
     */
    public void transmitDymaic(final String trends_pid, final TextView share_number, final ShareDynamicModel shareDynamicModel) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.transmitDymaic(
                    new JSONObject()
                            .put("trends_pid", trends_pid)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseFragmentActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
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
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                        shareDynamicModel.transmit = String.valueOf(Integer.valueOf(shareDynamicModel.transmit) + 1);
                                        share_number.setText(shareDynamicModel.transmit);
                                    } else {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    /**
     * item被点击
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void click(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {

    }

    /**
     * 评论
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void comment(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {

    }

    /**
     * 点赞
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void praise(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {

    }


    /**
     * 删除
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */

    @Override
    public void delete(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {

    }

    /**
     * 分享
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void share(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {

    }


    /**
     * 图片被点击
     *
     * @param position
     * @param url
     * @param urlList
     */
    @Override
    public void onClickImage(int position, String url, List<String> urlList) {
        if (getBaseFragmentActivityContext() != null) {
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < urlList.size(); i++) {
                temp.add(Config.url + urlList.get(i));
            }
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", position));
        }
    }

    /**
     * 加载图片
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);

    }

    /**
     * 评论列表被点击
     *
     * @param position
     * @param dymaicDetailsCommentModel
     */
    @Override
    public void onClickItem(int position, DymaicDetailsCommentModel dymaicDetailsCommentModel) {
    }

    /**
     * 动态列表被点击
     *
     * @param position
     * @param shareDynamicModel
     * @param list
     */
    @Override
    public void onClickDynamic(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list) {

    }

    /**
     * 动态列表点赞
     *
     * @param position
     * @param shareDynamicModel
     * @param list
     */
    @Override
    public void onClickDynamicPraise(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list) {

    }

    /**
     * 动态列表评论
     *
     * @param position
     * @param shareDynamicModel
     * @param list
     */
    @Override
    public void onClickDynamicComment(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list) {

    }

    /**
     * 动态列表分享
     *
     * @param position
     * @param shareDynamicModel
     * @param list
     */
    @Override
    public void onClickDynamicShare(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        p = 1;
        try {
            getDymaic();
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        try {
            getDymaic();
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onClickPraiseItem(int poistion, DymaicDetailsPraiseModel dymaicDetailsPraiseModel) {
        if (getBaseFragmentActivityContext() != null) {
            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUserInfo.class)
                    .putExtra("id", dymaicDetailsPraiseModel.member_id));

        }
    }

    @Override
    public void onSend(String content, TextView textView, EditText editText) {
        if (StringUtils.isEmpty(content)) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "您的评论为空");
        }
        comment(shareDynamicModel, content, editText);
    }


}