package com.sss.car.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.EventbusModel.SendMessageFromActivityInputKeyBoard;
import com.blankj.utilcode.activity.ActivityInputKeyboard;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.customwidget.KeyboardInput;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshScrollView;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedDynamicList;
import com.sss.car.EventBusModel.Praise;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.ActivityDymaicDetailsAdapter;
import com.sss.car.adapter.DymaicDetailsPraiseAdapter;
import com.sss.car.adapter.NineAdapter;
import com.sss.car.dao.DymaicDetailsOperationCallBack;
import com.sss.car.dao.DymaicDetailsPraiseAdapterCallBack;
import com.sss.car.dao.DymaicOperationCallBack;
import com.sss.car.model.DymaicDetailsCommentModel;
import com.sss.car.model.DymaicDetailsModel;
import com.sss.car.model.DymaicDetailsPraiseModel;
import com.sss.car.model.DymaicModel;
import com.sss.car.utils.ShareUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;

import static com.sss.car.R.id.share;


/**
 * 动态详情页
 * Created by leilei on 2017/9/1.
 */

@SuppressWarnings("ALL")
public class ActivityDymaicDetails extends BaseActivity implements LoadImageCallBack,
        DymaicDetailsOperationCallBack, DymaicOperationCallBack, KeyboardInput.KeyboardInputOperationCallBack
        , DymaicDetailsPraiseAdapterCallBack {
    LinearLayout back_top;
    TextView title_top;
    SimpleDraweeView pic_activity_dymaic_details_head;
    InnerListview list_activity_dymaic_details;
    TextView nikename_activity_dymaic_details_head;
    HorizontalListView horizontalListView_activity_dymaic_details_head;
    TextView content_activity_dymaic_details_head;
    InnerGridView nine_view_activity_dymaic_details_head;
    TextView loaction_view_activity_dymaic_details_head;
    TextView time_activity_dymaic_details_head;
    SimpleDraweeView right_button_top;
    SimpleDraweeView praise_activity_dymaic_details_head;
    TextView praise_number_activity_dymaic_details_head;
    SimpleDraweeView comment_activity_dymaic_details_head;
    TextView comment_number_activity_dymaic_details_head;
    SimpleDraweeView share_activity_dymaic_details_head;
    TextView share_number_activity_dymaic_details_head;
    LinearLayout prise_parent_activity_dymaic_details_head;
    YWLoadingDialog ywLoadingDialog;
    TextView comment;

    LinearLayout activity_dymaic_details;
    View view;

    DymaicDetailsModel dymaicDetailsModel;


    ActivityDymaicDetailsAdapter activityDymaicDetailsAdapter;

    int p = 1;

    NineAdapter nineAdapter;

    DymaicDetailsPraiseAdapter dymaicDetailsPraiseAdapter;

    PullToRefreshScrollView refresh;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        refresh = null;
        back_top = null;
        title_top = null;
        dymaicDetailsModel = null;
        activity_dymaic_details = null;
        list_activity_dymaic_details = null;
        nikename_activity_dymaic_details_head = null;
        content_activity_dymaic_details_head = null;
        nine_view_activity_dymaic_details_head = null;
        loaction_view_activity_dymaic_details_head = null;
        time_activity_dymaic_details_head = null;
        right_button_top = null;
        pic_activity_dymaic_details_head = null;
        prise_parent_activity_dymaic_details_head = null;
        praise_activity_dymaic_details_head = null;
        praise_number_activity_dymaic_details_head = null;
        comment_activity_dymaic_details_head = null;
        comment_number_activity_dymaic_details_head = null;
        share_activity_dymaic_details_head = null;
        share_number_activity_dymaic_details_head = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (activityDymaicDetailsAdapter != null) {
            activityDymaicDetailsAdapter.clear();
        }
        activityDymaicDetailsAdapter = null;
        if (nineAdapter != null) {
            nineAdapter.clear();
        }
        nineAdapter = null;
        if (dymaicDetailsPraiseAdapter != null) {
            dymaicDetailsPraiseAdapter.clear();
        }
        dymaicDetailsPraiseAdapter = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageFromActivityInputKeyBoard model) {
        if ("dymaicDetails".equals(model.type)) {
            comment(member_pid, model.content, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dymaic_details);
        ButterKnife.bind(this);
        customInit(activity_dymaic_details, false, true, true, false);

        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }

        dymaicDetailsModel = new DymaicDetailsModel();

        back_top = (LinearLayout) findViewById(R.id.back_top);
        list_activity_dymaic_details = (InnerListview) findViewById(R.id.can_content_view);
        activity_dymaic_details = (LinearLayout) findViewById(R.id.activity_dymaic_details);
        title_top = (TextView) findViewById(R.id.title_top);
        refresh = (PullToRefreshScrollView) findViewById(R.id.refresh);
        comment = (TextView) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityInputKeyboard.class)
                            .putExtra("titleText", "评论")
                            .putExtra("type", "dymaicDetails"));
                }
            }
        });

        right_button_top = (SimpleDraweeView) findViewById(R.id.right_button_top);

        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_dymaic_details_head, null);
        content_activity_dymaic_details_head = $.f(view, R.id.content_activity_dymaic_details_head);
        nine_view_activity_dymaic_details_head = $.f(view, R.id.nine_view_activity_dymaic_details_head);
        loaction_view_activity_dymaic_details_head = $.f(view, R.id.loaction_view_activity_dymaic_details_head);
        time_activity_dymaic_details_head = $.f(view, R.id.time_activity_dymaic_details_head);

        praise_activity_dymaic_details_head = $.f(view, R.id.praise_activity_dymaic_details_head);
        praise_number_activity_dymaic_details_head = $.f(view, R.id.praise_number_activity_dymaic_details_head);
        comment_activity_dymaic_details_head = $.f(view, R.id.comment_activity_dymaic_details_head);
        comment_number_activity_dymaic_details_head = $.f(view, R.id.comment_number_activity_dymaic_details_head);
        share_activity_dymaic_details_head = $.f(view, R.id.share_activity_dymaic_details_head);
        share_number_activity_dymaic_details_head = $.f(view, R.id.share_number_activity_dymaic_details_head);
        nikename_activity_dymaic_details_head = $.f(view, R.id.nikename_activity_dymaic_details_head);
        pic_activity_dymaic_details_head = $.f(view, R.id.pic_activity_dymaic_details_head);
        prise_parent_activity_dymaic_details_head = $.f(view, R.id.prise_parent_activity_dymaic_details_head);
        horizontalListView_activity_dymaic_details_head = $.f(view, R.id.horizontalListView_activity_dymaic_details_head);

        dymaicDetailsPraiseAdapter = new DymaicDetailsPraiseAdapter(dymaicDetailsModel.likes_list, getBaseActivityContext(), this, this);
        horizontalListView_activity_dymaic_details_head.setAdapter(dymaicDetailsPraiseAdapter);

        activityDymaicDetailsAdapter = new ActivityDymaicDetailsAdapter(getBaseActivityContext(), dymaicDetailsModel.comment_list, this, this);
        list_activity_dymaic_details.setAdapter(activityDymaicDetailsAdapter);
        list_activity_dymaic_details.addHeaderView(view);
        refresh.setMode(PullToRefreshBase.Mode.BOTH);
        refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                p = 1;
                dymaicDetailsComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                dymaicDetailsComment();
            }
        });
        title_top.setText("动态详情");
        right_button_top.setVisibility(View.INVISIBLE);


        dymaicDetails();
        dymaicDetailsComment();
        setClick();

    }

    void setClick() {
        back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right_button_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeleteDialog(dymaicDetailsModel.trends_id);
            }
        });
        praise_activity_dymaic_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praise(dymaicDetailsModel.trends_id);
            }
        });
        praise_number_activity_dymaic_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praise(dymaicDetailsModel.trends_id);
            }
        });
        share_activity_dymaic_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.prepareShare(ywLoadingDialog, getBaseActivity(), "trends", dymaicDetailsModel.trends_id);
            }
        });
        share_number_activity_dymaic_details_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.prepareShare(ywLoadingDialog, getBaseActivity(), "trends", dymaicDetailsModel.trends_id);
            }
        });
    }

    /**
     * 动态点赞
     *
     * @param trends_id
     */
    public void praise(final String trends_id) {
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
                            .put("type", "trends")//trends:动态点赞
                            .put("likes_id", trends_id)
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
                                        dymaicDetailsModel.is_praise = jsonObject.getJSONObject("data").getString("code");
                                        if ("1".equals(dymaicDetailsModel.is_praise)) {
                                            dymaicDetailsModel.likes = String.valueOf(Integer.valueOf(dymaicDetailsModel.likes) + 1);
                                            addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_praise_yes), praise_activity_dymaic_details_head, 0f));
                                            praise_number_activity_dymaic_details_head.setText(dymaicDetailsModel.likes);

                                            DymaicDetailsPraiseModel dymaicDetailsPraiseModel = new DymaicDetailsPraiseModel();
                                            dymaicDetailsPraiseModel.face = Config.face;
                                            dymaicDetailsPraiseModel.id = "";
                                            dymaicDetailsPraiseModel.member_id = Config.member_id;
                                            dymaicDetailsPraiseModel.username = Config.nikename;
                                            dymaicDetailsModel.likes_list.add(dymaicDetailsPraiseModel);
                                            dymaicDetailsPraiseAdapter.refresh(dymaicDetailsModel.likes_list);


                                        } else {
                                            addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_praise_no), praise_activity_dymaic_details_head, 0f));
                                            dymaicDetailsModel.likes = String.valueOf(Integer.valueOf(dymaicDetailsModel.likes) - 1);
                                            praise_number_activity_dymaic_details_head.setText(dymaicDetailsModel.likes);
                                            for (int i = 0; i < dymaicDetailsModel.likes_list.size(); i++) {
                                                if (dymaicDetailsModel.likes_list.get(i).member_id.equals(Config.member_id)) {
                                                    dymaicDetailsModel.likes_list.remove(i);
                                                }
                                            }
                                            dymaicDetailsPraiseAdapter.refresh(dymaicDetailsModel.likes_list);
                                        }
                                        EventBus.getDefault().post(new Praise(trends_id));
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
     * 动态详情
     *
     * @throws JSONException
     */
    public void dymaicDetails() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.dymaicDetails(
                    new JSONObject()
                            .put("p", p)
                            .put("trends_id", getIntent().getExtras().getString("id"))
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        dymaicDetailsModel.trends_id = jsonObject.getJSONObject("data").getString("trends_id");
                                        dymaicDetailsModel.contents = jsonObject.getJSONObject("data").getString("contents");
                                        dymaicDetailsModel.city_path = jsonObject.getJSONObject("data").getString("city_path");
                                        dymaicDetailsModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                        dymaicDetailsModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                        dymaicDetailsModel.looks = jsonObject.getJSONObject("data").getString("looks");
                                        dymaicDetailsModel.likes = jsonObject.getJSONObject("data").getString("likes");
                                        dymaicDetailsModel.username = jsonObject.getJSONObject("data").getString("username");
                                        dymaicDetailsModel.comment_count = jsonObject.getJSONObject("data").getString("comment_count");
                                        dymaicDetailsModel.face = jsonObject.getJSONObject("data").getString("face");
                                        dymaicDetailsModel.transmit = jsonObject.getJSONObject("data").getString("transmit");
                                        dymaicDetailsModel.is_praise = jsonObject.getJSONObject("data").getString("is_praise");


                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");


                                        if (jsonArray.length() > 0) {
                                            dymaicDetailsModel.picture.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                dymaicDetailsModel.picture.add(jsonArray.getString(i));
                                            }
                                            nine_view_activity_dymaic_details_head.setAdapter(
                                                    new NineAdapter(dymaicDetailsModel.picture,
                                                            getBaseActivity(),
                                                            ActivityDymaicDetails.this, ActivityDymaicDetails.this));
                                        }


                                        JSONArray jsonArray1 = jsonObject.getJSONObject("data").getJSONArray("likes_list");


                                        if (jsonArray1.length() > 0) {
                                            dymaicDetailsModel.likes_list.clear();
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                DymaicDetailsPraiseModel dymaicDetailsPraiseModel = new DymaicDetailsPraiseModel();
                                                dymaicDetailsPraiseModel.face = jsonArray1.getJSONObject(i).getString("face");
                                                dymaicDetailsPraiseModel.id = jsonArray1.getJSONObject(i).getString("id");
                                                dymaicDetailsPraiseModel.member_id = jsonArray1.getJSONObject(i).getString("member_id");
                                                dymaicDetailsPraiseModel.username = jsonArray1.getJSONObject(i).getString("username");
                                                dymaicDetailsModel.likes_list.add(dymaicDetailsPraiseModel);
                                            }
                                            prise_parent_activity_dymaic_details_head.setVisibility(View.VISIBLE);
                                            dymaicDetailsPraiseAdapter.refresh(dymaicDetailsModel.likes_list);
                                        }

                                        nikename_activity_dymaic_details_head.setText(dymaicDetailsModel.username);
                                        content_activity_dymaic_details_head.setText(dymaicDetailsModel.contents);
                                        time_activity_dymaic_details_head.setText(dymaicDetailsModel.create_time);
                                        comment_number_activity_dymaic_details_head.setText(dymaicDetailsModel.comment_count);
                                        addImageViewList(FrescoUtils.showImage(true, 100, 100, Uri.parse(Config.url + dymaicDetailsModel.face), pic_activity_dymaic_details_head, 99999));
                                        if ("1".equals(dymaicDetailsModel.is_praise)) {
                                            addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_praise_yes), praise_activity_dymaic_details_head, 0f));
                                        } else {
                                            addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivityContext()) + "/" + R.mipmap.logo_praise_no), praise_activity_dymaic_details_head, 0f));
                                        }
                                        praise_number_activity_dymaic_details_head.setText(dymaicDetailsModel.likes);
                                        share_number_activity_dymaic_details_head.setText(dymaicDetailsModel.transmit);

                                        if (Config.member_id.equals(dymaicDetailsModel.member_id)) {
                                            right_button_top.setVisibility(View.VISIBLE);
                                        }

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
     * 动态评论列表
     *
     * @throws JSONException
     */
    public void dymaicDetailsComment() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.dymaicDetailsComment(
                    new JSONObject()
                            .put("p", p)
                            .put("trends_id", getIntent().getExtras().getString("id"))
                            .put("member_id", Config.member_id).toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refresh != null) {
                                refresh.onRefreshComplete();
                            }
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refresh != null) {
                                refresh.onRefreshComplete();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {


                                        JSONArray jsonArray2 = jsonObject.getJSONArray("data");


                                        if (jsonArray2.length() > 0) {
                                            if (p == 1) {
                                                if (dymaicDetailsModel != null) {
                                                    if (dymaicDetailsModel.comment_list != null) {
                                                        dymaicDetailsModel.comment_list.clear();
                                                    }
                                                }
                                            }
                                            p++;
                                            for (int i = 0; i < jsonArray2.length(); i++) {
                                                DymaicDetailsCommentModel dymaicDetailsCommentModel = new DymaicDetailsCommentModel();
                                                dymaicDetailsCommentModel.face = jsonArray2.getJSONObject(i).getString("face");
                                                dymaicDetailsCommentModel.comment_id = jsonArray2.getJSONObject(i).getString("comment_id");
                                                dymaicDetailsCommentModel.contents = jsonArray2.getJSONObject(i).getString("contents");
                                                dymaicDetailsCommentModel.create_time = jsonArray2.getJSONObject(i).getString("create_time");
                                                dymaicDetailsCommentModel.trends_id = jsonArray2.getJSONObject(i).getString("trends_id");
                                                dymaicDetailsCommentModel.member_id = jsonArray2.getJSONObject(i).getString("member_id");
                                                dymaicDetailsCommentModel.username = jsonArray2.getJSONObject(i).getString("username");
                                                dymaicDetailsCommentModel.user_name = jsonArray2.getJSONObject(i).getString("user_name");
                                                dymaicDetailsModel.comment_list.add(dymaicDetailsCommentModel);
                                            }
                                        }
                                        activityDymaicDetailsAdapter.refresh(dymaicDetailsModel.comment_list);


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
     * 动态转发
     *
     * @throws JSONException
     */
    public void transmitDymaic(final String trends_pid) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        dymaicDetailsModel.transmit = String.valueOf(Integer.valueOf(dymaicDetailsModel.transmit) + 1);
                                        share_number_activity_dymaic_details_head.setText(dymaicDetailsModel.transmit);
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
     * 创建删除对话框
     *
     * @param trends_id
     */
    public void createDeleteDialog(final String trends_id) {

        String[] stringItems = {"确定"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getBaseActivityContext(), stringItems, null)
                .isTitleShow(true)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("是否要删除该动态?");
        dialog.titleTextSize_SP(14.5f).show();
        stringItems = null;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        deleteDymaic(trends_id);
                        break;
                }
            }
        });
    }

    /**
     * 动态删除
     *
     * @throws JSONException
     */
    public void deleteDymaic(String trends_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteDymaic(
                    new JSONObject()
                            .put("trends_id", trends_id)
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
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        finish();
                                        EventBus.getDefault().post(new ChangedDynamicList());
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


    String member_pid;

    /**
     * 动态评论
     *
     * @param content
     * @param textView
     */
    public void comment(String member_pid, String content, final EditText textView) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.commentDymaic(
                    new JSONObject()
                            .put("contents", content)
                            .put("member_pid", member_pid)
                            .put("trends_id", dymaicDetailsModel.trends_id)
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
                                        ActivityDymaicDetails.this.member_pid = null;
                                        if (textView != null) {
                                            textView.setText("");
                                        }
                                        p = 1;
                                        dymaicDetailsComment();
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

    }

    /**
     * 评论列表item被点击
     *
     * @param position
     * @param dymaicDetailsCommentModel
     */
    @Override
    public void onClickItem(int position, DymaicDetailsCommentModel dymaicDetailsCommentModel) {
        member_pid = dymaicDetailsCommentModel.member_id;
    }

    /**
     * 不用
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void click(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {

    }

    @Override
    public void comment(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {

    }

    /**
     * 不用
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void praise(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent) {

    }


    /**
     * 不用
     *
     * @param poistion
     * @param shareDymaicModel
     * @param list
     */
    @Override
    public void delete(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list) {

    }

    /**
     * 不用
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

    /**
     * 发送按钮被点击
     *
     * @param content
     * @param textView
     * @param editText
     */
    @Override
    public void onSend(String content, TextView textView, EditText editText) {

        comment(member_pid, content, editText);
    }

    /**
     * 点赞用户的头像被点击
     *
     * @param poistion
     * @param dymaicDetailsCommentModel
     */
    @Override
    public void onClickPraiseItem(int poistion, DymaicDetailsPraiseModel dymaicDetailsCommentModel) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                    .putExtra("id", dymaicDetailsCommentModel.member_id));

        }
    }

}
