package com.sss.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.Community_Userinfo_PostsAdapter;
import com.sss.car.dao.Community_Userinfo_PostsOperationCallBack;
import com.sss.car.dao.CustomRefreshLayoutCallBack;
import com.sss.car.dao.NineAdapter2OperationCallBack;
import com.sss.car.model.Community_Userinfo_Posts_Model;
import com.sss.car.utils.ShareUtils;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivitySharePostDetails;
import com.sss.car.view.ActivitySharePostOther;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.R.attr.id;

/**
 * 分享==>社区调用
 * 用户信息==>帖子调用
 * <p>com.qbw.customview
 * Created by leilei on 2017/9/4.
 */

@SuppressWarnings("ALL")
public class FragmentCommunity_Userinfo_Posts extends BaseFragment implements
        LoadImageCallBack,
        Community_Userinfo_PostsOperationCallBack,
        NineAdapter2OperationCallBack {
    @BindView(R.id.can_content_view)
    public PullToRefreshListView canContentView;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    public int p = 1;
    String cate_id;
    String confirm;
    String friend_id;
    String essence;//精华
    String reply;//最后回复
    String newest;//最新发布


    String collect;//如果传1，则为兴趣社区，否则为其他

    boolean isShow = false;//控制是否显示type_name_item_community_userinfo_posts_adapter与arrows_item_community_userinfo_posts_adapter
    public List<Community_Userinfo_Posts_Model> list = new ArrayList<>();

    Community_Userinfo_PostsAdapter community_userinfo_postsAdapter;

    boolean isNeedEmptyView;
    CustomRefreshLayoutCallBack addHeadViewCallBack;

    public FragmentCommunity_Userinfo_Posts() {
    }

    /**
     * 分享==>社区调用
     *
     * @param isShow
     * @param cate_id             此参数如果不传,则默认刷新所有类型的集合
     * @param confirm             社区调用,假如需要查看我的帖子,则需要传1
     * @param collect             社区调用,假如需要查看兴趣社区,则需要传1
     * @param addHeadViewCallBack
     */
    public FragmentCommunity_Userinfo_Posts(boolean isNeedEmptyView, boolean isShow, String cate_id, String confirm, String collect, CustomRefreshLayoutCallBack addHeadViewCallBack) {
        this.isNeedEmptyView = isNeedEmptyView;
        this.isShow = isShow;
        this.cate_id = cate_id;
        this.collect = collect;
        this.confirm = confirm;
        this.addHeadViewCallBack = addHeadViewCallBack;

    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setEssence(String essence) {
        this.essence = essence;
    }

    public ListView getCanContentView() {
        return canContentView.getRefreshableView();
    }

    /**
     * 用户信息==>帖子调用
     * 分享==>社区调用(我的帖子)
     *
     * @param friend_id 帖子调用,则需要传ID
     */
    public FragmentCommunity_Userinfo_Posts(boolean isNeedEmptyView, boolean isShow, String friend_id, CustomRefreshLayoutCallBack addHeadViewCallBack) {
        this.isNeedEmptyView = isNeedEmptyView;
        this.isShow = isShow;
        this.friend_id = friend_id;
        this.addHeadViewCallBack = addHeadViewCallBack;
    }


    @Override
    public void onDestroy() {
        if (community_userinfo_postsAdapter != null) {
            community_userinfo_postsAdapter.clear();
        }
        community_userinfo_postsAdapter = null;
        canContentView = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_community_userinfo_posts;
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
                                canContentView.setMode(PullToRefreshBase.Mode.BOTH);
                                if (isNeedEmptyView) {
                                    canContentView.setEmptyView(LayoutInflater.from(getBaseFragmentActivityContext()).inflate(R.layout.empty_view, null));
                                }
                                canContentView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                    @Override
                                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                        p = 1;
                                        communityArticle(null);
                                    }

                                    @Override
                                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                                        communityArticle(null);
                                    }
                                });


                                community_userinfo_postsAdapter = new Community_Userinfo_PostsAdapter(isShow, getBaseFragmentActivityContext(), list,
                                        FragmentCommunity_Userinfo_Posts.this,
                                        FragmentCommunity_Userinfo_Posts.this,
                                        FragmentCommunity_Userinfo_Posts.this);
                                canContentView.setAdapter(community_userinfo_postsAdapter);
                                if (addHeadViewCallBack != null) {
                                    addHeadViewCallBack.onAdd(canContentView.getRefreshableView());
                                }
                                communityArticle(null);
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
     * 获取动态社区文章
     */
    public void communityArticle(final TextView textView) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.communityArticle(
                    new JSONObject()
                            .put("p", p)
                            .put("member_id", Config.member_id)
                            .put("cate_id", cate_id)
                            .put("collect", collect)
                            .put("confirm", confirm)//如果在分享==>社区调用,假如需要查看我的帖子,则需要传1其余地方调用的全部都不传
                            .put("friend_id", friend_id)//如果在用户信息==>帖子调用,则需要传ID,其他地方调用的全部不传
                            .put("essence", essence)//精华
                            .put("reply", reply)//最后回复
                            .put("newest", newest)//最新发布
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                            if (canContentView != null) {
                                canContentView.onRefreshComplete();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (canContentView != null) {
                                canContentView.onRefreshComplete();
                            }
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
                                        community_userinfo_posts_model.username = jsonArray.getJSONObject(i).getString("username");
                                        community_userinfo_posts_model.face = jsonArray.getJSONObject(i).getString("face");
                                        community_userinfo_posts_model.day = jsonArray.getJSONObject(i).getString("day");
                                        community_userinfo_posts_model.month = jsonArray.getJSONObject(i).getString("month");
                                        community_userinfo_posts_model.week = jsonArray.getJSONObject(i).getString("week");
                                        community_userinfo_posts_model.vehicle_name = jsonArray.getJSONObject(i).getString("vehicle_name");
                                        community_userinfo_posts_model.cate_name = jsonArray.getJSONObject(i).getString("cate_name");
                                        community_userinfo_posts_model.is_collect = jsonArray.getJSONObject(i).getString("is_collect");
                                        community_userinfo_posts_model.is_essence = jsonArray.getJSONObject(i).getString("is_essence");
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
                                    if (community_userinfo_postsAdapter != null) {
                                        community_userinfo_postsAdapter.refresh(list);
                                    }
                                    if (textView != null) {
                                        APPOftenUtils.setBackgroundOfVersion(textView, getResources().getDrawable(R.color.line));
                                    }

                                } else {

                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:community article-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:community article-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取帖子评论。点赞。分享数据
     *
     * @param community_id
     */
    public void getPostsPraiseCommentSharenumber(final String cid) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
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
                                        if (list.get(i).community_id.equals(cid)) {
                                            list.get(i).comment_count = jsonObject.getJSONObject("data").getString("comment_count");
                                            list.get(i).collect_count = jsonObject.getJSONObject("data").getString("collect_count");
                                            list.get(i).share = jsonObject.getJSONObject("data").getString("share");
                                            list.get(i).is_collect = jsonObject.getJSONObject("data").getString("is_collect");
                                            community_userinfo_postsAdapter.refresh(list);
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
     * 帖子收藏（取消收藏）
     *
     * @param community_id
     */
    public void postsCollectCancelCollect(final Community_Userinfo_Posts_Model model, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        model.collect_count = String.valueOf(Integer.valueOf(model.collect_count) + 1);
                                        model.is_collect = "1";
                                    } else if ("0".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        model.collect_count = String.valueOf(Integer.valueOf(model.collect_count) - 1);
                                        model.is_collect = "0";
                                    }
                                    FragmentCommunity_Userinfo_Posts.this.list.set(position, model);
                                    community_userinfo_postsAdapter.updateItem(position, FragmentCommunity_Userinfo_Posts.this.list, canContentView.getRefreshableView());
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

    /**
     * 载入图片
     *
     * @param imageView
     */
    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    /**
     * 类型被点击
     *
     * @param model
     * @param list
     * @param position
     */
    @Override
    public void onType(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position) {
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostOther.class)
                    .putExtra("cate_id", model.cate_id)
                    .putExtra("cate_name", model.cate_name));
        }
    }

    /**
     * item被点击
     *
     * @param model
     * @param list
     * @param position
     */
    @Override
    public void onClickItem(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position) {
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                    .putExtra("community_id", model.community_id));
        }
    }

    /**
     * 评论
     *
     * @param model
     * @param list
     * @param position
     */
    @Override
    public void onComment(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position) {
        if (getBaseFragmentActivityContext() != null) {
            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySharePostDetails.class)
                    .putExtra("community_id", model.community_id)
                    .putExtra("is_show_keyboard", true));
        }
    }

    /**
     * 分享
     *
     * @param model
     * @param list
     * @param position
     */
    @Override
    public void onShare(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position) {
        ShareUtils.prepareShare(ywLoadingDialog, getActivity(), "community", model.community_id);
    }

    /**
     * 收藏
     *
     * @param model
     * @param list
     * @param position
     */
    @Override
    public void onCollection(Community_Userinfo_Posts_Model model, List<Community_Userinfo_Posts_Model> list, int position) {
        postsCollectCancelCollect(model, position);
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
}
