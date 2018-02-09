package com.sss.car.view;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.fragment.FragmentGoodsServiceDetailsComment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品评论列表(展示用  没有任何操作)
 * Created by leilei on 2017/12/7.
 */

public class ActivityGoodsCommentList extends BaseActivity {

    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_activity_goods_comment_list)
    FrameLayout parentActivityGoodsCommentList;
    @BindView(R.id.activity_goods_comment_list)
    LinearLayout activityGoodsCommentList;
    FragmentGoodsServiceDetailsComment fragmentGoodsServiceDetailsComment;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragmentGoodsServiceDetailsComment != null) {
            fragmentGoodsServiceDetailsComment.onDestroy();
        }
        fragmentGoodsServiceDetailsComment = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_comment_list);
        if (getIntent()==null||getIntent().getExtras()==null){
            ToastUtils.showLongToast(getBaseActivityContext(),"数据传递错误!");
            finish();
        }
        ButterKnife.bind(this);
        titleTop.setText("评论评价");
        fragmentGoodsServiceDetailsComment = new FragmentGoodsServiceDetailsComment(false,getIntent().getExtras().getString("goods_id"), null);
        FragmentUtils.addFragment(getSupportFragmentManager(), fragmentGoodsServiceDetailsComment, R.id.parent_activity_goods_comment_list);

    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }
}
