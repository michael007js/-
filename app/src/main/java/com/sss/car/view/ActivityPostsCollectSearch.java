package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangedPostsModel;
import com.sss.car.R;
import com.sss.car.fragment.FragmentCollect;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/16.
 */

public class ActivityPostsCollectSearch extends BaseActivity {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.right_search_top_image)
    LinearLayout rightSearchTopImage;
    @BindView(R.id.activity_posts_collect_search)
    LinearLayout activityPostsCollectSearch;
    FragmentCollect fragmentCollect;
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        activityPostsCollectSearch = null;
        if (fragmentCollect!=null){
            fragmentCollect.onDestroy();
        }
        fragmentCollect=null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPostsModel changedPostsModel) {
        fragmentCollect.p = 1;
        fragmentCollect.collect();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_collect_search);
        ButterKnife.bind(this);
        customInit(activityPostsCollectSearch,false,true,false,true);
        fragmentCollect = new FragmentCollect(true);
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentCollect, R.id.parent);
        FragmentUtils.hideAllShowFragment(fragmentCollect);


    }

    @OnClick({R.id.back_top_image, R.id.right_search_top_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top_image:
                finish();
                break;
            case R.id.right_search_top_image:
                if (StringUtils.isEmpty(editText.getText().toString().trim())){
                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入搜索标签");
                    return;
                }
                if (fragmentCollect!=null){
                    fragmentCollect.search(editText.getText().toString().trim());
                }
                break;
        }
    }
}
