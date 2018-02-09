package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentDraftPublic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 草稿箱==>搜索
 * Created by leilei on 2017/11/6.
 */

public class ActivityDraftSearch extends BaseActivity {
    @BindView(R.id.back_activity_draft_search)
    LinearLayout backActivityDraftSearch;
    @BindView(R.id.input_activity_draft_search)
    EditText inputActivityDraftSearch;
    @BindView(R.id.search_activity_draft_search)
    TextView searchActivityDraftSearch;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.activity_draft_search)
    LinearLayout activityDraftSearch;
    FragmentDraftPublic fragmentDraftPublic;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        topParent = null;
        activityDraftSearch = null;
        backActivityDraftSearch = null;
        inputActivityDraftSearch = null;
        searchActivityDraftSearch = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_search);
        ButterKnife.bind(this);
        customInit(activityDraftSearch, false, true, false);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showShortToast(getBaseActivityContext(),"数据传输错误");
            finish();
        }
        switch (getIntent().getExtras().getInt("search_mode")){
            case FragmentDraftPublic.REQUEST_MODE_GOODS:
                inputActivityDraftSearch.setHint("搜索商品");
                break;
            case FragmentDraftPublic.REQUEST_MODE_SOS:
                inputActivityDraftSearch.setHint("搜索SOS订单");
                break;
            case FragmentDraftPublic.REQUEST_MODE_ORDER:
                inputActivityDraftSearch.setHint("搜索订单");
                break;
            case FragmentDraftPublic.REQUEST_MODE_POPULARIZE:
                inputActivityDraftSearch.setHint("搜索推广");
                break;

        }
        if (fragmentDraftPublic == null) {
            fragmentDraftPublic = new FragmentDraftPublic(getIntent().getExtras().getInt("search_mode"), false);
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentDraftPublic, R.id.parent_activity_draft_search);
            FragmentUtils.hideAllShowFragment(fragmentDraftPublic);
        }
    }

    @OnClick({R.id.back_activity_draft_search, R.id.search_activity_draft_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_activity_draft_search:
                finish();
                break;
            case R.id.search_activity_draft_search:
                if (StringUtils.isEmpty(inputActivityDraftSearch.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请输入搜索内容");
                    return;
                }

                fragmentDraftPublic.search(inputActivityDraftSearch.getText().toString().trim());
                break;
        }
    }
}
