package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.EventBusModel.ChangedCollectLabel;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCollect;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 收藏页
 * Created by leilei on 2017/9/11.
 */

@SuppressWarnings("ALL")
public class ActivityShareCollect extends BaseActivity {

    @BindView(R.id.activity_share_collect)
    LinearLayout activityShareCollect;


    @BindView(R.id.right_button_top)
    TextView rightButtonTop;


    FragmentCollect fragmentCollect;


    @BindView(R.id.back_top)
    LinearLayout backTopImage;
    @BindView(R.id.title_top)
    TextView titleTopImage;
    @BindView(R.id.search)
    ImageView search;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        activityShareCollect = null;
        if (fragmentCollect != null) {
            fragmentCollect.onDestroy();
        }
        fragmentCollect = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_collect);
        ButterKnife.bind(this);
        customInit(activityShareCollect, false, true, true);
        titleTopImage.setText("我的收藏");
        rightButtonTop.setText("编辑");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        fragmentCollect = new FragmentCollect();
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCollect, R.id.parent);
        FragmentUtils.hideAllShowFragment(fragmentCollect);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel changedPostsModel) {
        fragmentCollect.p = 1;
        fragmentCollect.collect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedCollectLabel event) {
        fragmentCollect.isEdit = false;
        rightButtonTop.setText("编辑");
        fragmentCollect.parentBottomActivityShareCollect.setVisibility(View.GONE);
        fragmentCollect.selectList.clear();
        for (int i = 0; i < fragmentCollect.list.size(); i++) {
            fragmentCollect.list.get(i).isChoose = false;
        }


        fragmentCollect.p = 1;
        fragmentCollect.collect();
    }


    @OnClick({R.id.back_top, R.id.right_button_top, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (!fragmentCollect.isEdit) {
                    rightButtonTop.setText("取消");
                    fragmentCollect.parentBottomActivityShareCollect.setVisibility(View.VISIBLE);
                    fragmentCollect.isEdit = true;
                } else {
                    rightButtonTop.setText("编辑");
                    fragmentCollect.isEdit = false;
                    fragmentCollect.parentBottomActivityShareCollect.setVisibility(View.GONE);
                    fragmentCollect.selectList.clear();
                    for (int i = 0; i < fragmentCollect.list.size(); i++) {
                        fragmentCollect.list.get(i).isChoose = false;
                    }
                }
                if (fragmentCollect.listviewActivityShareCollect != null) {
                    fragmentCollect.listviewActivityShareCollect.onRefreshComplete();
                }
                fragmentCollect.sss_adapter.setList(fragmentCollect.list);

                if (fragmentCollect.menuDialog == null) {
                    fragmentCollect.menuDialog = new MenuDialog(getBaseActivity());
                }
                break;
            case R.id.search:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityPostsCollectSearch.class));
                }
                break;
        }
    }


}

