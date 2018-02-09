package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedList;
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
        customInit(activityShareInteractionManageSettingPeopleManage, false, true, false);
        titleTop.setText(getIntent().getExtras().getString("title"));
        initAdapter();
        friend_into();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedList changedList) {
        list.clear();
        friend_into();

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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PeopleModel.class));
                                    }
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
}
