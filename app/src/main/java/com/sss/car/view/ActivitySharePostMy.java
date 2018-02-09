package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.EventBusModel.Posts;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.ShareMyPostAdapter;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.dao.OnExistsShopCallBack;
import com.sss.car.dao.ShareMyPostAdapterCallBack;
import com.sss.car.model.Community_Userinfo_Posts_Model;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 我的帖子
 * Created by leilei on 2017/9/4.
 */

public class ActivitySharePostMy extends BaseActivity implements LoadImageCallBack,
        NineAdapter2OperationCallBack,
        ShareMyPostAdapterCallBack {
    @BindView(R.id.back_top_more)
    LinearLayout backTopMore;
    @BindView(R.id.search_top_more)
    ImageView searchTopMore;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.ten_top_more)
    ImageView tenTopMore;
    @BindView(R.id.setting_top_more)
    ImageView settingTopMore;
    TextView postedActivitySharePostHead;
    @BindView(R.id.top_activity_share_post)
    ImageView topActivitySharePost;
    @BindView(R.id.activity_share_post)
    LinearLayout activitySharePost;
    MenuDialog menuDialog;


    ShareMyPostAdapter shareMyPostAdapter;

    int p = 1;
    List<Community_Userinfo_Posts_Model> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    @BindView(R.id.listview_activity_share_post)
    PullToRefreshListView listviewActivitySharePost;
    View view;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view = null;
        listviewActivitySharePost = null;
        backTopMore = null;
        searchTopMore = null;
        titleTop = null;
        tenTopMore = null;
        settingTopMore = null;
        postedActivitySharePostHead = null;
        topActivitySharePost = null;
        activitySharePost = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (shareMyPostAdapter != null) {
            shareMyPostAdapter.clear();
        }
        shareMyPostAdapter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_my_post);
        ButterKnife.bind(this);
        customInit(activitySharePost, false, true, true, false);
        searchTopMore.setVisibility(View.INVISIBLE);
        tenTopMore.setVisibility(View.INVISIBLE);
        settingTopMore.setVisibility(View.INVISIBLE);
        menuDialog = new MenuDialog(this);

//        addImageViewList(GlidUtils.glideLoad(false, tenTopMore, getBaseActivityContext(), R.mipmap.logo_search));
//        addImageViewList(GlidUtils.glideLoad(false, settingTopMore, getBaseActivityContext(), R.mipmap.logo_ten));

        listviewActivitySharePost.setMode(PullToRefreshBase.Mode.BOTH);
        listviewActivitySharePost.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                communityArticle();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                communityArticle();
            }
        });

        shareMyPostAdapter = new ShareMyPostAdapter(getBaseActivityContext(), list, this, this, this);
        listviewActivitySharePost.setAdapter(shareMyPostAdapter);

        /*暂时不用*/
        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_share_my_post_head, null);

        postedActivitySharePostHead = $.f(view, R.id.posted_activity_share_post_head);
        postedActivitySharePostHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityPublishPost.class));
                }
            }
        });

        listviewActivitySharePost.getRefreshableView().addHeaderView(view);
        titleTop.setText("我的帖子");
        communityArticle();
    }

    /**
     * 帖子详情分享，评论，收藏后回调
     *
     * @param model
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Posts model) {
        getPostsPraiseCommentSharenumber(model.id);
    }

    /**
     * 获取帖子评论。点赞。分享数据
     *
     * @param cid
     */
    public void getPostsPraiseCommentSharenumber(final String cid) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getPostsPraiseCommentSharenumber(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("community_id", cid)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                        if (list.get(i).community_id.equals(cid)) {
                                            list.get(i).comment_count = jsonObject.getJSONObject("data").getString("comment_count");
                                            list.get(i).collect_count = jsonObject.getJSONObject("data").getString("collect_count");
                                            list.get(i).share = jsonObject.getJSONObject("data").getString("share");
                                            list.get(i).is_collect = jsonObject.getJSONObject("data").getString("is_collect");
                                            shareMyPostAdapter.refresh(list);
                                            return;
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    /**
     * 帖子被删除通知更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel event) {
        p = 1;
        communityArticle();
    }

    @OnClick({R.id.back_top_more, R.id.ten_top_more, R.id.setting_top_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_more:
                finish();
                break;
            case R.id.ten_top_more:
                break;
            case R.id.setting_top_more:
                menuDialog.createMainRightMenu(settingTopMore, this, new OnExistsShopCallBack() {
                    @Override
                    public void onExists() {
                        startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceEdit.class));
                    }
                });
                break;

        }
    }


    /**
     * 获取动态社区文章
     */
    public void communityArticle() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.communityArticle(
                    new JSONObject()
                            .put("p", p)
                            .put("member_id", Config.member_id)
                            .put("friend_id", Config.member_id)//我的帖子
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            listviewActivitySharePost.onRefreshComplete();
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            listviewActivitySharePost.onRefreshComplete();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (p == 1) {
                                        list.clear();
                                    }
                                    p++;
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Community_Userinfo_Posts_Model community_userinfo_posts_model = new Community_Userinfo_Posts_Model();
                                        community_userinfo_posts_model.community_id = jsonArray.getJSONObject(i).getString("community_id");
                                        community_userinfo_posts_model.title = jsonArray.getJSONObject(i).getString("title");
                                        community_userinfo_posts_model.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                        community_userinfo_posts_model.cate_id = jsonArray.getJSONObject(i).getString("cate_id");
                                        community_userinfo_posts_model.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                        community_userinfo_posts_model.share = jsonArray.getJSONObject(i).getString("share");
                                        community_userinfo_posts_model.is_top = jsonArray.getJSONObject(i).getString("is_top");
                                        community_userinfo_posts_model.is_hot = jsonArray.getJSONObject(i).getString("is_hot");
                                        community_userinfo_posts_model.is_essence = jsonArray.getJSONObject(i).getString("is_essence");
                                        community_userinfo_posts_model.username = jsonArray.getJSONObject(i).getString("username");
                                        community_userinfo_posts_model.face = jsonArray.getJSONObject(i).getString("face");
                                        community_userinfo_posts_model.day = jsonArray.getJSONObject(i).getString("day");
                                        community_userinfo_posts_model.month = jsonArray.getJSONObject(i).getString("month");
                                        community_userinfo_posts_model.week = jsonArray.getJSONObject(i).getString("week");
                                        community_userinfo_posts_model.vehicle_name = jsonArray.getJSONObject(i).getString("vehicle_name");
                                        community_userinfo_posts_model.cate_name = jsonArray.getJSONObject(i).getString("cate_name");
                                        community_userinfo_posts_model.is_collect = jsonArray.getJSONObject(i).getString("is_collect");
                                        community_userinfo_posts_model.collect_count = jsonArray.getJSONObject(i).getString("collect_count");
                                        community_userinfo_posts_model.comment_count = jsonArray.getJSONObject(i).getString("comment_count");

                                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("picture");
                                        if (jsonArray1.length() > 0) {
                                            List<String> pic = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                pic.add(jsonArray1.getString(j));
                                            }
                                            community_userinfo_posts_model.picture = pic;
                                        }
                                        list.add(community_userinfo_posts_model);
                                    }
                                    shareMyPostAdapter.refresh(list);
                                } else {

                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:community article-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:community article-0");
            e.printStackTrace();
        }
    }

    /**
     * 帖子收藏（取消收藏）
     */
    public void postsCollectCancelCollect(final Community_Userinfo_Posts_Model model, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsCollectCancelCollect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "community")
                            .put("collect_id", model.community_id)//此处传要收藏的文章ID
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        model.collect_count = String.valueOf(Integer.valueOf(model.collect_count) + 1);
                                        model.is_collect = "1";
                                    } else if ("0".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        model.collect_count = String.valueOf(Integer.valueOf(model.collect_count) - 1);
                                        model.is_collect = "0";
                                    }
                                    list.set(position, model);
                                    shareMyPostAdapter.updateItem(position, list, listviewActivitySharePost.getRefreshableView());
                                    EventBus.getDefault().post(new ChangedPostsModel());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

    /**
     * 删除社区文章
     */
    public void deletePosts(final Community_Userinfo_Posts_Model model, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deletePosts(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("community_id", model.community_id)//此处传要收藏的文章ID
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    list.remove(position);
                                    shareMyPostAdapter.refresh(list);
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    @Override
    public void onClickImage(int position, String url, List<String> urlList) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                    .putExtra("current", position));
        }
    }

    @Override
    public void onClickItem(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {
        if (getBaseActivityContext() != null) {
            getBaseActivityContext().startActivity(new Intent(getBaseActivityContext(), ActivitySharePostDetails.class)
                    .putExtra("community_id", model.community_id));
        }
    }

    @Override
    public void onClickType(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {
        if (getBaseActivityContext() != null) {
            getBaseActivityContext().startActivity(new Intent(getBaseActivityContext(), ActivitySharePostOther.class)
                    .putExtra("cate_id", model.cate_id)
                    .putExtra("cate_name", model.cate_name));
        }
    }

    @Override
    public void onDelete(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {
        deletePosts(model, position);
    }

    @Override
    public void onShare(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {

    }

    @Override
    public void onComment(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {
        if (getBaseActivityContext() != null) {
            getBaseActivityContext().startActivity(new Intent(getBaseActivityContext(), ActivitySharePostDetails.class)
                    .putExtra("community_id", model.community_id)
                    .putExtra("is_show_keyboard", true));
        }
    }

    @Override
    public void onCollect(int position, Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list) {
        postsCollectCancelCollect(model, position);
    }

}
