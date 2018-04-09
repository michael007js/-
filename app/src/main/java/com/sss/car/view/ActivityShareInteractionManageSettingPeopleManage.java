package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedList;
import com.sss.car.EventBusModel.ChangedUserInfo;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.PeopleModel;

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
 * Created by leilei on 2017/12/27.
 */

public class ActivityShareInteractionManageSettingPeopleManage extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.activity_share_interaction_manage_setting_people_manage)
    LinearLayout activityShareInteractionManageSettingPeopleManage;

    YWLoadingDialog ywLoadingDialog;

    List<PeopleModel> list = new ArrayList<>();
    Gson gson = new Gson();
    SSS_Adapter sss_adapter;

    int p = 1;

    String friend_id;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        setContentView(R.layout.activity_share_interaction_manage_setting_people_manage);
        ButterKnife.bind(this);
        customInit(activityShareInteractionManageSettingPeopleManage, false, true, true);
        titleTop.setText(getIntent().getExtras().getString("title"));
        listview.setEmptyView(LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.empty_view, null));
        initAdapter();
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                p = 1;
                friend_into();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                friend_into();
            }
        });
        friend_into();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedList changedList) {
        p = 1;
        friend_into();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedUserInfo changedUserInfo) {
        p = 1;
        friend_into();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel changeInfoModel) {
        if ("service_username".equals(changeInfoModel.type)) {
            service_remark(changeInfoModel.msg);
        }
    }

    void initAdapter() {
        sss_adapter = new SSS_Adapter<PeopleModel>(getBaseActivityContext(), R.layout.item_people) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final PeopleModel bean, SSS_Adapter instance) {
                helper.setText(R.id.name, bean.username);
                addImageViewList(FrescoUtils.showImage(false, 40, 40, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic)), 9999f));
                helper.getView(R.id.click_item_people).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                                .putExtra("id", bean.member_id));
                    }
                });
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        del_relation(bean.member_id, position);
                    }
                });
                helper.getView(R.id.edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) helper.getView(R.id.scoll)).smoothClose();
                        friend_id = bean.member_id;
                        startActivity(new Intent(getBaseActivityContext(), ActivityChangeInfo.class)
                                .putExtra("type", "service_username")
                                .putExtra("canChange", true)
                                .putExtra("extra", bean.username));
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        listview.setAdapter(sss_adapter);
    }

    @OnClick({R.id.back_top, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.add:
                if ("1".equals(getIntent().getExtras().getString("type"))) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.speccial_attention));
                } else if ("2".equals(getIntent().getExtras().getString("type"))) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.do_not_see_target));
                } else if ("3".equals(getIntent().getExtras().getString("type"))) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.do_not_see_me));
                } else if ("4".equals(getIntent().getExtras().getString("type"))) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.black));
                } else if ("5".equals(getIntent().getExtras().getString("type"))) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityCreateGroupInviteGroupSend.class).putExtra("type", ActivityCreateGroupInviteGroupSend.shop_service));
                }
                break;
        }
    }


    public void del_relation(String id, final int position) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.del_relation(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", id)
                            .put("type", getIntent().getExtras().getString("type"))
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
                                    sss_adapter.setList(list);
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


    public void friend_into() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.friend_into(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("type", getIntent().getExtras().getString("type"))
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
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
                                            list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PeopleModel.class));
                                        }
                                        sss_adapter.setList(list);
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

    public void service_remark(String remark) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.service_remark(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("friend_id", friend_id)
                            .put("remark", remark)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    p = 1;
                                    friend_into();
                                    friend_id = null;

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
}
