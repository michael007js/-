package com.sss.car.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.EventbusModel.SendMessageFromActivityInputKeyBoard;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.ActivityInputKeyboard;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.dao.SharePostDetailsBottomDialogCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AnimationUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenLightUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.DataImageView;
import com.blankj.utilcode.xrichtext.RichTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.EventBusModel.Posts;
import com.sss.car.EventBusModel.Praise;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.SharePostDetailsCommentAdapter;
import com.sss.car.adapter.SharePostDetailsCommentReplayModel;
import com.sss.car.dao.SharePostDetailsCommentAdapterCallBack;
import com.sss.car.model.SharePostDetailsCommentModel;
import com.sss.car.model.SharePostDetailsModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;

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

import static android.R.attr.targetId;


/**
 * 社区文章详情
 * Created by leilei on 2017/9/6.
 */

@SuppressWarnings("ALL")
public class ActivitySharePostDetails extends BaseActivity implements
        LoadImageCallBack, SharePostDetailsCommentAdapterCallBack, RichTextView.OnImageClickListener {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.title_top_image)
    TextView titleTopImage;
    @BindView(R.id.logo_right_search_top_image)
    ImageView logoRightSearchTopImage;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.listview_activity_share_post_details)
    PullToRefreshListView listviewActivitySharePostDetails;
    @BindView(R.id.activity_share_post_details)
    LinearLayout activitySharePostDetails;
    @BindView(R.id.top_activity_share_post_details)
    ImageView topActivitySharePostDetails;


    MenuDialog menuDialog;

    List<SharePostDetailsCommentModel> list = new ArrayList<>();
    int p = 1;

    String comment_pid;
    SharePostDetailsCommentAdapter sharePostDetailsCommentAdapter;

    SPUtils spUtils;

    SharePostDetailsModel sharePostDetailsModel;
    YWLoadingDialog ywLoadingDialog;

    View view;
    SimpleDraweeView pic_share_post_details_head;
    TextView nikename_share_post_details_head;
    TextView car_name_share_post_details_head;
    TextView date_name_share_post_details_head;
    TextView title_share_post_details_head;
    TextView delete;
    RichTextView content_share_post_details_head;
    LinearLayout click_collect_share_post_details_head;
    TextView number_collect_share_post_details_head;
    LinearLayout click_comment_share_post_details_head;
    TextView number_comment_share_post_details_head;
    LinearLayout click_share_share_post_details_head;
    TextView number_share_share_post_details_head;
    TextView type_name_share_post_details_head;
    SimpleDraweeView collect_show;
    @BindView(R.id.lz)
    TextView lz;
    boolean isLZ;
    String lzId;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.collect_show_bottom)
    SimpleDraweeView collectShowBottom;
    @BindView(R.id.share_bottom)
    SimpleDraweeView shareBottom;
    @BindView(R.id.listview_dialog_number_share_post_details)
    ListView listviewDialogNumberSharePostDetails;
    SSS_Adapter sss_adapter;
    List<Integer> num = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        spUtils = null;
        lzId = null;
        comment = null;
        collectShowBottom = null;
        shareBottom = null;
        content_share_post_details_head = null;
        topActivitySharePostDetails = null;
        sharePostDetailsModel = null;
        pic_share_post_details_head = null;
        nikename_share_post_details_head = null;
        car_name_share_post_details_head = null;
        date_name_share_post_details_head = null;
        title_share_post_details_head = null;
        click_collect_share_post_details_head = null;
        number_collect_share_post_details_head = null;
        click_comment_share_post_details_head = null;
        number_comment_share_post_details_head = null;
        click_share_share_post_details_head = null;
        number_share_share_post_details_head = null;
        type_name_share_post_details_head = null;
        view = null;
        backTopImage = null;
        titleTopImage = null;
        logoRightSearchTopImage = null;
        rightSearchTopImage = null;
        listviewActivitySharePostDetails = null;
        activitySharePostDetails = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (sharePostDetailsCommentAdapter != null) {
            sharePostDetailsCommentAdapter.clear();
        }
        sharePostDetailsCommentAdapter = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post_details);
        ButterKnife.bind(this);
        customInit(activitySharePostDetails, false, true, true, false);
        addImageViewList(GlidUtils.glideLoad(false, logoRightSearchTopImage, getBaseActivityContext(), R.mipmap.logo_more));

        sharePostDetailsCommentAdapter = new SharePostDetailsCommentAdapter(list, getBaseActivityContext(), this, this);
        listviewActivitySharePostDetails.setAdapter(sharePostDetailsCommentAdapter);

        menuDialog = new MenuDialog(this);
        titleTopImage.setText("详情");

        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            finish();
        }

        initAdapter();
        initConfig();
        initHead();
        postsDetails(true);

        if (getIntent().getExtras().getBoolean("is_show_keyboard")) {

        }

//        listviewActivitySharePostDetails.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount > Config.scollHighRestriction) {
//                    LogUtils.e(firstVisibleItem + visibleItemCount);
//                    topActivitySharePostDetails.setVisibility(View.VISIBLE);
//                } else {
//                    topActivitySharePostDetails.setVisibility(View.GONE);
//                }
//            }
//        });
        listviewActivitySharePostDetails.setMode(PullToRefreshBase.Mode.BOTH);
        listviewActivitySharePostDetails.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                postsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                postsList();
            }
        });

        lz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharePostDetailsModel == null) {
                    return;
                }
                if (isLZ == false) {
                    isLZ = true;
                    lz.setTextColor(getResources().getColor(R.color.white));
                    APPOftenUtils.setBackgroundOfVersion(lz, getResources().getDrawable(R.drawable.bg_lz));
                    lz.setPadding(5, 5, 5, 5);
                    lzId = sharePostDetailsModel.member_id;
                } else {
                    isLZ = false;
                    lz.setTextColor(getResources().getColor(R.color.mc));
                    APPOftenUtils.setBackgroundOfVersion(lz, getResources().getDrawable(R.drawable.bg_lz_un));
                    lz.setPadding(5, 5, 5, 5);
                    lzId = null;
                }
                p = 1;
                postsList();
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityInputKeyboard.class)
                            .putExtra("titleText", "回帖")
                            .putExtra("type", "posts"));
                }
            }
        });


        titleTopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listviewDialogNumberSharePostDetails.getVisibility() == View.GONE) {
                    listviewDialogNumberSharePostDetails.setVisibility(View.VISIBLE);
                } else {
                    listviewDialogNumberSharePostDetails.setVisibility(View.GONE);
                }
            }
        });
        listviewActivitySharePostDetails.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listviewDialogNumberSharePostDetails.setVisibility(View.GONE);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageFromActivityInputKeyBoard model) {
        if ("posts".equals(model.type)) {
            postsComment(model.content);
        } else if ("onCommentPosts".equals(model.type)) {
            postsComment(model.content);
        }
    }


    @OnClick({R.id.back_top_image, R.id.right_search_top_image, R.id.top_activity_share_post_details, R.id.comment, R.id.collect_show_bottom, R.id.share_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
                menuDialog.createSharePostDetailsBottomDialog(getBaseActivityContext(), sharePostDetailsModel.member_id, sharePostDetailsModel.username, new SharePostDetailsBottomDialogCallBack() {
                    @Override
                    public void onSmallTextAndImg(boolean state) {
                        if (state) {
                            width = 300;
                            height = 200;
                        } else {
                            width = getWindowManager().getDefaultDisplay().getWidth();
                            height = 700;
                        }
                        content_share_post_details_head.changeImageSize(width, height);
                    }

                    @Override
                    public void onNight(boolean state) {
                        if (state) {
                            ScreenLightUtils.changeAppBrightness(getBaseActivityContext(), 0);
                            ScreenLightUtils.setScreenMode(0);
                        } else {
                            ScreenLightUtils.setScreenMode(1);
                            ScreenLightUtils.changeAppBrightness(getBaseActivityContext(), 100);
                        }
                    }

                    @Override
                        /*-1小0中1大*/
                    public void onTextSize(int state) {
                        textSize = state;
                        content_share_post_details_head.changeTextSize(textSize);
                    }
                });
                break;
            case R.id.top_activity_share_post_details:
                if (Build.VERSION.SDK_INT >= 8) {
                    listviewActivitySharePostDetails.getRefreshableView().smoothScrollToPosition(0);
                } else {
                    listviewActivitySharePostDetails.setSelection(0);
                }
                topActivitySharePostDetails.setVisibility(View.GONE);
                break;
            case R.id.comment:
                break;
            case R.id.collect_show_bottom:
                postsCollectCancelCollect();
                break;
            case R.id.share_bottom:
                ShareUtils.prepareShare(ywLoadingDialog,getBaseActivity(),"community",getIntent().getExtras().getString("community_id"));
                break;
        }
    }

    int width;
    int height;
    int textSize;

    /**
     * 初始化配置
     */
    void initConfig() {
        if (spUtils == null) {
            spUtils = new SPUtils(this, Config.defaultFileName, Context.MODE_PRIVATE);
        }
        switch (spUtils.getInt("text_size" + Config.account)) {
            case -1:
                textSize = -1;
                break;
            case 0:
                textSize = 0;
                break;
            case 1:
                textSize = 1;
                break;
        }


        if (spUtils.getBoolean("night" + Config.account)) {
            ScreenLightUtils.changeAppBrightness(getBaseActivityContext(), 0);
            ScreenLightUtils.setScreenMode(0);
        } else {
            ScreenLightUtils.setScreenMode(0);
            ScreenLightUtils.changeAppBrightness(getBaseActivityContext(), 80);
        }
        if (spUtils.getBoolean("small_text_image" + Config.account)) {
            width = 300;
            height = 200;
        } else {
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = 700;
        }


        if (content_share_post_details_head != null && sharePostDetailsModel != null) {
            content_share_post_details_head.changeImageSize(width, height);
            content_share_post_details_head.changeTextSize(textSize);
        }
    }

    /**
     * 添加头部
     */
    void initHead() {

        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_share_post_details_head, null);
        collect_show = $.f(view, R.id.collect_show);
        content_share_post_details_head = $.f(view, R.id.content_share_post_details_head);
        pic_share_post_details_head = $.f(view, R.id.pic_share_post_details_head);
        nikename_share_post_details_head = $.f(view, R.id.nikename_share_post_details_head);
        car_name_share_post_details_head = $.f(view, R.id.car_name_share_post_details_head);
        date_name_share_post_details_head = $.f(view, R.id.date_name_share_post_details_head);
        title_share_post_details_head = $.f(view, R.id.title_share_post_details_head);
        delete = $.f(view, R.id.delete);

        click_collect_share_post_details_head = $.f(view, R.id.click_collect_share_post_details_head);
        number_collect_share_post_details_head = $.f(view, R.id.number_collect_share_post_details_head);

        click_comment_share_post_details_head = $.f(view, R.id.click_comment_share_post_details_head);
        number_comment_share_post_details_head = $.f(view, R.id.number_comment_share_post_details_head);

        click_share_share_post_details_head = $.f(view, R.id.click_share_share_post_details_head);
        number_share_share_post_details_head = $.f(view, R.id.number_share_share_post_details_head);

        type_name_share_post_details_head = $.f(view, R.id.type_name_share_post_details_head);
        listviewActivitySharePostDetails.getRefreshableView().addHeaderView(view);
        initListener();
    }

    /**
     * 设置头部监听
     */
    void initListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPOftenUtils.createAskDialog(getBaseActivityContext(), "确定要删除该帖子？", new OnAskDialogCallBack() {
                    @Override
                    public void onOKey(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                        del_community();
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                    }
                });
            }
        });

        click_collect_share_post_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postsCollectCancelCollect();
            }
        });

        type_name_share_post_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    getBaseActivityContext().startActivity(new Intent(getBaseActivityContext(), ActivitySharePostOther.class)
                            .putExtra("cate_id", sharePostDetailsModel.cate_id)
                            .putExtra("cate_name", sharePostDetailsModel.cate_name));
                }
            }
        });

        click_comment_share_post_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_pid = null;
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityInputKeyboard.class)
                            .putExtra("titleText", "回复")
                            .putExtra("type", "onCommentPosts"));
                }
            }
        });
        click_share_share_post_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.prepareShare(ywLoadingDialog,getBaseActivity(),"community",getIntent().getExtras().getString("community_id"));
            }
        });
        pic_share_post_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                        .putExtra("id", sharePostDetailsModel.member_id));
            }
        });
    }

    /**
     * 展示服务器返回的数据
     */
    void showData() {

        if (Config.member_id.equals(sharePostDetailsModel.member_id)) {
            delete.setVisibility(View.VISIBLE);
        }

        titleTopImage.setText(p + "/" + sharePostDetailsModel.comment_page);
        if ("1".equals(sharePostDetailsModel.is_collect)) {
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect), collect_show, 0f));
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect), collectShowBottom, 0f));
        } else {
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect_no), collect_show, 0f));
            addImageViewList(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_collect_no), collectShowBottom, 0f));
        }
        addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + sharePostDetailsModel.face), pic_share_post_details_head, 9999f));
        nikename_share_post_details_head.setText(sharePostDetailsModel.username);
        car_name_share_post_details_head.setText(sharePostDetailsModel.vehicle_name);
        date_name_share_post_details_head.setText(sharePostDetailsModel.create_time);
        title_share_post_details_head.setText(sharePostDetailsModel.title);
        number_collect_share_post_details_head.setText(sharePostDetailsModel.collect_count);
        number_comment_share_post_details_head.setText(sharePostDetailsModel.comment_count);
        number_share_share_post_details_head.setText(sharePostDetailsModel.share);
        type_name_share_post_details_head.setText(sharePostDetailsModel.cate_name);
        try {
            LogUtils.e(sharePostDetailsModel.contents);
            content_share_post_details_head.showEditData(Config.url, sharePostDetailsModel.
                            contents,
                    content_share_post_details_head,
                    getBaseActivityContext(),
                    width, height, textSize);
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "帖子数据解析错误");
            e.printStackTrace();
        }

        content_share_post_details_head.setOnImageClickListener(this);
        sharePostDetailsCommentAdapter.refresh(list);
    }

    /**
     * 帖子删除
     */
    public void del_community() {
        if (sharePostDetailsModel == null) {
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_community(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("community_id", sharePostDetailsModel.community_id)
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
                                    EventBus.getDefault().post(new ChangedPostsModel());
                                    finish();
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
     * 帖子收藏（取消收藏）
     */
    public void postsCollectCancelCollect() {
        if (sharePostDetailsModel == null) {
            return;
        }
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
                            .put("collect_id", sharePostDetailsModel.community_id)//此处传要收藏的文章ID
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
                                    postsDetails(false);
                                    EventBus.getDefault().post(new Posts(sharePostDetailsModel.community_id));
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
     * 社区文章详情
     */
    public void postsDetails(final boolean isAfterPraise) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsDetails(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "1")
                            .put("community_id", getIntent().getExtras().getString("community_id"))
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


                                    List<String> list = new ArrayList<>();
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(jsonArray.getString(i));
                                    }
                                    sharePostDetailsModel = null;
                                    sharePostDetailsModel = new SharePostDetailsModel();
                                    sharePostDetailsModel.picture = list;

                                    sharePostDetailsModel.community_id = jsonObject.getJSONObject("data").getString("community_id");
                                    sharePostDetailsModel.title = jsonObject.getJSONObject("data").getString("title");

                                    sharePostDetailsModel.contents = jsonObject.getJSONObject("data").getString("contents");
                                    sharePostDetailsModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                    sharePostDetailsModel.cate_id = jsonObject.getJSONObject("data").getString("cate_id");
                                    sharePostDetailsModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    sharePostDetailsModel.looks = jsonObject.getJSONObject("data").getString("looks");
                                    sharePostDetailsModel.likes = jsonObject.getJSONObject("data").getString("likes");
                                    sharePostDetailsModel.share = jsonObject.getJSONObject("data").getString("share");
                                    sharePostDetailsModel.is_top = jsonObject.getJSONObject("data").getString("is_top");
                                    sharePostDetailsModel.is_hot = jsonObject.getJSONObject("data").getString("is_hot");
                                    sharePostDetailsModel.is_essence = jsonObject.getJSONObject("data").getString("is_essence");
                                    sharePostDetailsModel.state = jsonObject.getJSONObject("data").getString("state");
                                    sharePostDetailsModel.status = jsonObject.getJSONObject("data").getString("status");
                                    sharePostDetailsModel.username = jsonObject.getJSONObject("data").getString("username");
                                    sharePostDetailsModel.face = jsonObject.getJSONObject("data").getString("face");
                                    sharePostDetailsModel.vehicle_name = jsonObject.getJSONObject("data").getString("vehicle_name");
                                    sharePostDetailsModel.cate_name = jsonObject.getJSONObject("data").getString("cate_name");
                                    sharePostDetailsModel.is_collect = jsonObject.getJSONObject("data").getString("is_collect");
                                    sharePostDetailsModel.collect_count = jsonObject.getJSONObject("data").getString("collect_count");
                                    sharePostDetailsModel.comment_page = jsonObject.getJSONObject("data").getInt("comment_page");
                                    sharePostDetailsModel.comment_count = jsonObject.getJSONObject("data").getString("comment_count");
                                    showData();
                                    if (isAfterPraise) {
                                        postsList();
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
            e.printStackTrace();
        }
    }

    /**
     * 评论
     */
    public void postsComment(String content) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsComment(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("comment_pid", comment_pid)
                            .put("contents", content)
                            .put("community_id", getIntent().getExtras().getString("community_id"))
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
                                    comment_pid = null;
                                    p = 1;
//                                    postsDetails(false);
                                    postsList();
                                    EventBus.getDefault().post(new ChangedPostsModel());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
            e.printStackTrace();
        }
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<Integer>(getBaseActivityContext(), R.layout.item_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final Integer bean, SSS_Adapter instance) {
                if (p - 1 == bean + 1) {
                    helper.setTextColor(R.id.text_item_text, getResources().getColor(R.color.mainColor));
                } else {
                    helper.setTextColor(R.id.text_item_text, getResources().getColor(R.color.black));
                }
                helper.setText(R.id.text_item_text, "第" + (bean + 1) + "页");
                helper.getView(R.id.text_item_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        p = bean + 1;
                        postsList();
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listviewDialogNumberSharePostDetails.setAdapter(sss_adapter);
    }

    /**
     * 社区文章评论列表
     */
    public void postsList() {
        if (sharePostDetailsModel == null) {
            return;
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsList(
                    new JSONObject()
                            .put("p", p)
                            .put("member_id", Config.member_id)
                            .put("friend_id", lzId)
                            .put("community_id", getIntent().getExtras().getString("community_id"))
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                            if (listviewActivitySharePostDetails != null) {
                                listviewActivitySharePostDetails.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (listviewActivitySharePostDetails != null) {
                                listviewActivitySharePostDetails.onRefreshComplete();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                                    if (jsonArray.length()>0){
                                        num.clear();
                                        for (int i = 0; i < sharePostDetailsModel.comment_page; i++) {
                                            num.add(i);
                                        }
                                        sss_adapter.setList(num);
                                        list.clear();
                                        titleTopImage.setText(jsonObject.getJSONObject("data").getString("pager") + "/" + jsonObject.getJSONObject("data").getString("count"));
                                        p++;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            SharePostDetailsCommentModel sharePostDetailsCommentModel = new SharePostDetailsCommentModel();
                                            sharePostDetailsCommentModel.comment_id = jsonArray.getJSONObject(i).getString("comment_id");
                                            sharePostDetailsCommentModel.contents = jsonArray.getJSONObject(i).getString("contents");
                                            sharePostDetailsCommentModel.picture = jsonArray.getJSONObject(i).getString("picture");
                                            sharePostDetailsCommentModel.create_time = jsonArray.getJSONObject(i).getString("create_time");
                                            sharePostDetailsCommentModel.comment_pid = jsonArray.getJSONObject(i).getString("comment_pid");
                                            sharePostDetailsCommentModel.community_id = jsonArray.getJSONObject(i).getString("community_id");
                                            sharePostDetailsCommentModel.member_id = jsonArray.getJSONObject(i).getString("member_id");
                                            sharePostDetailsCommentModel.likes = jsonArray.getJSONObject(i).getString("likes");
                                            sharePostDetailsCommentModel.username = jsonArray.getJSONObject(i).getString("username");
                                            sharePostDetailsCommentModel.face = jsonArray.getJSONObject(i).getString("face");
                                            sharePostDetailsCommentModel.vehicle_name = jsonArray.getJSONObject(i).getString("vehicle_name");
                                            sharePostDetailsCommentModel.is_likes = jsonArray.getJSONObject(i).getString("is_likes");

                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("reply_list");
                                            List<SharePostDetailsCommentReplayModel> replayCommentList = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                SharePostDetailsCommentReplayModel sharePostDetailsCommentReplayModel = new SharePostDetailsCommentReplayModel();
                                                sharePostDetailsCommentReplayModel.comment_id = jsonArray1.getJSONObject(j).getString("comment_id");
                                                sharePostDetailsCommentReplayModel.contents = jsonArray1.getJSONObject(j).getString("contents");
                                                sharePostDetailsCommentReplayModel.member_id = jsonArray1.getJSONObject(j).getString("member_id");
                                                sharePostDetailsCommentReplayModel.username = jsonArray1.getJSONObject(j).getString("username");
                                                sharePostDetailsCommentReplayModel.create_time = jsonArray1.getJSONObject(j).getString("create_time");
                                                replayCommentList.add(sharePostDetailsCommentReplayModel);
                                            }
                                            sharePostDetailsCommentModel.reply_list = replayCommentList;
                                            list.add(sharePostDetailsCommentModel);

                                        }

                                        sharePostDetailsCommentAdapter.refresh(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Post-0");
            e.printStackTrace();
        }
    }

    /**
     * 帖子评论点赞（取消点赞）
     *
     * @param model
     * @param poistion
     */
    public void praiseComment(final SharePostDetailsCommentModel model, final int poistion) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.praise(
                    new JSONObject()
                            .put("type", "community_comment")//community_comment:点赞
                            .put("likes_id", model.comment_id)
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
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
                                        list.get(poistion).is_likes = jsonObject.getJSONObject("data").getString("code");
                                        if ("1".equals(list.get(poistion).is_likes)) {
                                            list.get(poistion).likes = String.valueOf(Integer.valueOf(list.get(poistion).likes) + 1);
                                        } else {
                                            list.get(poistion).likes = String.valueOf(Integer.valueOf(list.get(poistion).likes) - 1);
                                        }
                                        sharePostDetailsCommentAdapter.refresh(list);

                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:dymaic-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:dymaic-0");
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
     * 回复评论
     *
     * @param position
     * @param list
     * @param model
     */
    @Override
    public void onComment(int position, List<SharePostDetailsCommentModel> list, SharePostDetailsCommentModel model) {
        comment_pid = model.comment_id;
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityInputKeyboard.class)
                    .putExtra("titleText", "回复")
                    .putExtra("type", "onCommentPosts"));
        }
    }

    /**
     * 评论点赞
     *
     * @param position
     * @param list
     * @param model
     */
    @Override
    public void onPraise(int position, List<SharePostDetailsCommentModel> list, SharePostDetailsCommentModel model) {
        praiseComment(model, position);
    }


    @Override
    public void onClickImage(int position, List<String> urlList, DataImageView imageView) {
        if (getBaseActivityContext() != null) {
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < urlList.size(); i++) {
                temp.add(Config.url + urlList.get(i));
            }
            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", position));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
    
}
