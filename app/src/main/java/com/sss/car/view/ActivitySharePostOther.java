package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedPostsList;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.EventBusModel.Posts;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.CustomRefreshLayoutCallBack;
import com.sss.car.fragment.FragmentCommunity_Userinfo_Posts;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.top_activity_share_post_other;

/**
 * 社区  帖子  除了热门,我的之外的其他所有
 * Created by leilei on 2017/9/5.
 */

public class ActivitySharePostOther extends BaseFragmentActivity {
    @BindView(R.id.parent_activity_share_post_other)
    FrameLayout parentActivitySharePostOther;
    @BindView(top_activity_share_post_other)
    ImageView topActivitySharePostOther;
    @BindView(R.id.activity_share_post_other)
    LinearLayout activitySharePostOther;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.collect)
    SimpleDraweeView collect;
    @BindView(R.id.share)
    SimpleDraweeView share;

    View view;
    TextView all_activity_share_post_other_head;
    TextView essence_activity_share_post_other_head;
    TextView last_open_activity_share_post_other_head;
    TextView last_activity_share_post_other_head;
    TextView new_activity_share_post_other_head;

    LinearLayout parent_posted_activity_share_post_other_head;
    TextView posted_activity_share_post_other_head;

    FragmentCommunity_Userinfo_Posts fragmentCommunity_userinfo_posts;


    YWLoadingDialog ywLoadingDialog;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        if (fragmentCommunity_userinfo_posts != null) {
            fragmentCommunity_userinfo_posts.onDestroy();
        }
        fragmentCommunity_userinfo_posts = null;
        parentActivitySharePostOther = null;
        topActivitySharePostOther = null;
        activitySharePostOther = null;


        view = null;
        share = null;
        collect = null;
        backTop = null;
        titleTop = null;
        all_activity_share_post_other_head = null;
        essence_activity_share_post_other_head = null;
        last_open_activity_share_post_other_head = null;
        last_activity_share_post_other_head = null;
        new_activity_share_post_other_head = null;

        parent_posted_activity_share_post_other_head = null;
        posted_activity_share_post_other_head = null;

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post_other);
        ButterKnife.bind(this);

        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        initHead();
        customInit(activitySharePostOther, false, true, true);
        fragmentCommunity_userinfo_posts = new FragmentCommunity_Userinfo_Posts(false,false, getIntent().getExtras().getString("cate_id"), "", "",new CustomRefreshLayoutCallBack() {
            @Override
            public void onAdd(ListView listview) {
                listview.addHeaderView(view);
                listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        LogUtils.e(firstVisibleItem + "-" + visibleItemCount);
                        if (firstVisibleItem + visibleItemCount > Config.scollHighRestriction) {
                            topActivitySharePostOther.setVisibility(View.VISIBLE);
                        } else {
                            topActivitySharePostOther.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCommunity_userinfo_posts, R.id.parent_activity_share_post_other);


        titleTop.setText(getIntent().getExtras().getString("cate_name"));
        cate_collect();
    }

    boolean isOpen = false;

    void initHead() {
        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_share_post_other_head, null);
        all_activity_share_post_other_head = $.f(view, R.id.all_activity_share_post_other_head);
        essence_activity_share_post_other_head = $.f(view, R.id.essence_activity_share_post_other_head);
        last_open_activity_share_post_other_head = $.f(view, R.id.last_open_activity_share_post_other_head);

        last_activity_share_post_other_head = $.f(view, R.id.last_activity_share_post_other_head);
        new_activity_share_post_other_head = $.f(view, R.id.new_activity_share_post_other_head);

        parent_posted_activity_share_post_other_head = $.f(view, R.id.parent_posted_activity_share_post_other_head);
        posted_activity_share_post_other_head = $.f(view, R.id.posted_activity_share_post_other_head);
        setListener();
    }

    void setListener() {
        all_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_activity_share_post_other_head.setTextColor(getResources().getColor(com.blankj.utilcode.R.color.mainColor));
                essence_activity_share_post_other_head.setTextColor(getResources().getColor(com.blankj.utilcode.R.color.black));
                if (fragmentCommunity_userinfo_posts != null) {
                    fragmentCommunity_userinfo_posts.setEssence("0");
                    fragmentCommunity_userinfo_posts.p = 1;
                    fragmentCommunity_userinfo_posts.communityArticle(null);
                }

            }
        });
        essence_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_activity_share_post_other_head.setTextColor(getResources().getColor(com.blankj.utilcode.R.color.black));
                essence_activity_share_post_other_head.setTextColor(getResources().getColor(com.blankj.utilcode.R.color.mainColor));
                if (fragmentCommunity_userinfo_posts != null) {
                    fragmentCommunity_userinfo_posts.setEssence("1");
                    fragmentCommunity_userinfo_posts.p = 1;
                    fragmentCommunity_userinfo_posts.communityArticle(null);
                }
            }
        });
        last_open_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen == false) {
                    isOpen = true;
                } else {
                    isOpen = false;
                }
                if (isOpen) {
                    parent_posted_activity_share_post_other_head.setVisibility(View.GONE);
                    last_activity_share_post_other_head.setVisibility(View.VISIBLE);
                    new_activity_share_post_other_head.setVisibility(View.VISIBLE);
                } else {
                    parent_posted_activity_share_post_other_head.setVisibility(View.VISIBLE);
                    last_activity_share_post_other_head.setVisibility(View.GONE);
                    new_activity_share_post_other_head.setVisibility(View.GONE);
                }
            }
        });

        new_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCommunity_userinfo_posts != null) {
                    parent_posted_activity_share_post_other_head.setVisibility(View.VISIBLE);
                    last_activity_share_post_other_head.setVisibility(View.GONE);
                    new_activity_share_post_other_head.setVisibility(View.GONE);

                    fragmentCommunity_userinfo_posts.setNewest("1");
                    fragmentCommunity_userinfo_posts.setReply("");
                    fragmentCommunity_userinfo_posts.p = 1;
                    last_open_activity_share_post_other_head.setText("最新发布");
                    APPOftenUtils.setBackgroundOfVersion(last_activity_share_post_other_head,getResources().getDrawable(R.color.white));
                    fragmentCommunity_userinfo_posts.communityArticle(new_activity_share_post_other_head);
                }

            }
        });
        last_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCommunity_userinfo_posts != null) {

                    parent_posted_activity_share_post_other_head.setVisibility(View.VISIBLE);
                    last_activity_share_post_other_head.setVisibility(View.GONE);
                    new_activity_share_post_other_head.setVisibility(View.GONE);

                    fragmentCommunity_userinfo_posts.setNewest("");
                    fragmentCommunity_userinfo_posts.setReply("1");
                    fragmentCommunity_userinfo_posts.p = 1;
                            last_open_activity_share_post_other_head.setText("最后回复");
                    APPOftenUtils.setBackgroundOfVersion(new_activity_share_post_other_head,getResources().getDrawable(R.color.white));
                    fragmentCommunity_userinfo_posts.communityArticle(last_activity_share_post_other_head);
                }
            }
        });
        posted_activity_share_post_other_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityPublishPost.class));
                }
            }
        });
    }
    /**
     * 帖子详情分享，评论，收藏后回调
     * @param model
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Posts model) {
        fragmentCommunity_userinfo_posts. getPostsPraiseCommentSharenumber(model.id);
    }
    /**
     * 帖子被改变通知更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel event) {
        if (fragmentCommunity_userinfo_posts == null) {
            return;
        }
        fragmentCommunity_userinfo_posts.p = 1;
        fragmentCommunity_userinfo_posts.communityArticle(null);
    }


    /**
     * 收藏/取消收藏帖子分类
     */
    void collectPostsType() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.collectPostsType(new JSONObject()
                            .put("collect_id", getIntent().getExtras().getString("cate_id"))
                            .put("member_id", Config.member_id)
                            .put("type", "community_cate")
                            .toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
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
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                            addImageViewList(FrescoUtils.showImage(false, 25, 25, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect), collect, 0f));
                                        } else {
                                            addImageViewList(FrescoUtils.showImage(false, 25, 25, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect_no), collect, 0f));
                                        }
                                        EventBus.getDefault().post(new ChangedPostsList());
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }



    /**
     * 是否已收藏社区分类
     */
    void cate_collect() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cate_collect(new JSONObject()
                            .put("cate_id", getIntent().getExtras().getString("cate_id"))
                            .put("member_id", Config.member_id)
                            .toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
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
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                            addImageViewList(FrescoUtils.showImage(false, 25, 25, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect), collect, 0f));
                                        } else {
                                            addImageViewList(FrescoUtils.showImage(false, 25, 25, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect_no), collect, 0f));
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }


    @OnClick({R.id.back_top, R.id.collect, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.collect:
                collectPostsType();
                break;
            case R.id.share:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
