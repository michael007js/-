package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.custom.ListViewVariation;
import com.sss.car.dao.CustomRefreshLayoutCallBack2;
import com.sss.car.fragment.Fragment_Dynamic_Friend_Attention_community_Near;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/9/9.
 */

public class ActivityShareDymaicMy extends BaseFragmentActivity {
    @BindView(R.id.back_top_more)
    LinearLayout backTopMore;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.ten_top_more)
    ImageView tenTopMore;
    @BindView(R.id.setting_top_more)
    ImageView settingTopMore;
    @BindView(R.id.parent_activity_share_dymaic_my)
    FrameLayout parentActivityShareDymaicMy;
    @BindView(R.id.top_activity_share_dymaic_my)
    ImageView topActivityShareDymaicMy;
    @BindView(R.id.activity_share_dymaic_my)
    LinearLayout activityShareDymaicMy;

    Fragment_Dynamic_Friend_Attention_community_Near fragmentShareDymaic;




    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (fragmentShareDymaic!=null){
            fragmentShareDymaic.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dymaic_my);
        ButterKnife.bind(this);
        customInit(activityShareDymaicMy,false,true,false);

        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传输错误");
            finish();
        }

        addImageViewList(GlidUtils.glideLoad(false,tenTopMore,getBaseActivityContext(),R.mipmap.logo_search_black));
        addImageViewList(GlidUtils.glideLoad(false,settingTopMore,getBaseActivityContext(),R.mipmap.logo_ten));

        titleTop.setText("我的动态");


        if (fragmentShareDymaic==null){
            fragmentShareDymaic=new Fragment_Dynamic_Friend_Attention_community_Near(true,getIntent().getExtras().getString("type"), false, new CustomRefreshLayoutCallBack2() {
                @Override
                public void onAdd(ListViewVariation listViewVariation) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        listViewVariation.setScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                if (scrollY > 500) {
                                    topActivityShareDymaicMy.setVisibility(View.VISIBLE);
                                } else {
                                    topActivityShareDymaicMy.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }
        FragmentUtils.addFragment(getSupportFragmentManager(),fragmentShareDymaic,R.id.parent_activity_share_dymaic_public);
    }

    @OnClick({R.id.back_top_more, R.id.ten_top_more, R.id.setting_top_more, R.id.top_activity_share_dymaic_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_more:
                finish();
                break;
            case R.id.ten_top_more:
                break;
            case R.id.setting_top_more:
                break;
            case R.id.top_activity_share_dymaic_my:
                topActivityShareDymaicMy.setVisibility(View.GONE);
                fragmentShareDymaic.getListFragmentDynamicFriendAttentionCommunityNear().getRefreshableView().smoothScrollTo(0,0);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getBaseActivityContext()).onActivityResult(requestCode, resultCode, data);
    }
}
