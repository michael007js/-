package com.sss.car.view;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.EventbusModel.SendMessageFromActivityInputKeyBoard;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangedDynamicList;
import com.sss.car.R;
import com.sss.car.custom.ListViewVariation;
import com.sss.car.dao.CustomRefreshLayoutCallBack2;
import com.sss.car.fragment.Fragment_Dynamic_Friend_Attention_community_Near;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/9/9.
 */

public class ActivityShareDymaicPublic extends BaseActivity {
    @BindView(R.id.back_top_more)
    LinearLayout backTopMore;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_activity_share_dymaic_public)
    FrameLayout parentActivityShareDymaicPublic;
    @BindView(R.id.top_activity_share_dymaic_public)
    ImageView topActivityShareDymaicPublic;
    @BindView(R.id.activity_share_dymaic_public)
    LinearLayout activityShareDymaicPublic;

    Fragment_Dynamic_Friend_Attention_community_Near fragmentShareDymaic;

    View view;
    LinearLayout publish_activity_share_dynamic_public_head;
    TextView share_activity_share_dynamic_public_head;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentShareDymaic != null) {
            fragmentShareDymaic.onDestroy();
        }
        super.onDestroy();
    }

    /**
     * 发布动态后被回调通知动态页面更新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedDynamicList event) {
        if (fragmentShareDymaic == null) {
            return;
        }
        try {
            fragmentShareDymaic.p = 1;
            fragmentShareDymaic.getDymaic();
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:dymaic-0");
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageFromActivityInputKeyBoard model) {
        if ("dymaic".equals(model.type)) {
            if (fragmentShareDymaic == null) {
                return;
            }
            fragmentShareDymaic.p = 1;
            fragmentShareDymaic.comment(
                    fragmentShareDymaic.shareDynamicModel, model.content, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dymaic_public);
        ButterKnife.bind(this);
        customInit(activityShareDymaicPublic, false, true, true);
        //（type ==1所有，2我的，3好友，4关注，6周边）
        switch (getIntent().getExtras().getString("type")) {
            case "2":
                titleTop.setText("我的动态");
                break;
            case "3":
                titleTop.setText("好友动态");
                break;
            case "4":
                titleTop.setText("关注动态");
                break;
            case "6":
                titleTop.setText("周边动态");
                break;
        }


        if (fragmentShareDymaic == null) {
            fragmentShareDymaic = new Fragment_Dynamic_Friend_Attention_community_Near(null,Config.member_id,true,getIntent().getExtras().getString("type"), false, new CustomRefreshLayoutCallBack2() {
                @Override
                public void onAdd(ListViewVariation listViewVariation) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        listViewVariation.setScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                if (scrollY > 500) {
                                    topActivityShareDymaicPublic.setVisibility(View.VISIBLE);
                                } else {
                                    topActivityShareDymaicPublic.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    addHead(listViewVariation);
                }

            });
        }
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentShareDymaic, R.id.parent_activity_share_dymaic_public);
    }

    @OnClick({R.id.back_top_more, R.id.top_activity_share_dymaic_public})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_more:
                finish();
                break;
            case R.id.top_activity_share_dymaic_public:
                topActivityShareDymaicPublic.setVisibility(View.GONE);
                fragmentShareDymaic.getListFragmentDynamicFriendAttentionCommunityNear().getRefreshableView().smoothScrollTo(0, 0);
                break;
        }
    }


    void addHead(ListViewVariation listViewVariation) {
        view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.activity_share_dynamic_public_head, null);
        publish_activity_share_dynamic_public_head = $.f(view, R.id.publish_activity_share_dynamic_public_head);
        share_activity_share_dynamic_public_head = $.f(view, R.id.share_activity_share_dynamic_public_head);
        share_activity_share_dynamic_public_head.setText("立即分享");
        share_activity_share_dynamic_public_head.setTextColor(getResources().getColor(R.color.mainColor));
        share_activity_share_dynamic_public_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseActivityContext(), ActivityPublishDymaic.class));
            }
        });
        if ("6".equals(getIntent().getExtras().getString("type"))) {
            publish_activity_share_dynamic_public_head.setVisibility(View.VISIBLE);
        }
        listViewVariation.addHeadView(view);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
