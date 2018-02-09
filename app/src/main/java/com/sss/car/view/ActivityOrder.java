package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseFragmentActivity;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.util.FragmentUtils;
import com.sss.car.EventBusModel.CarSearch;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.R;
import com.sss.car.custom.ListViewOrder;
import com.sss.car.fragment.FragmentOrder;
import com.sss.car.model.OrderModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的==>订单模块公用页面
 * Created by leilei on 2017/10/7.
 */

public class ActivityOrder extends BaseFragmentActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    List<OrderModel> list = new ArrayList<>();
    @BindView(R.id.parent_activity_order)
    FrameLayout parentActivityOrder;
    @BindView(R.id.activity_order)
    LinearLayout activityOrder;

    FragmentOrder fragmentOrder;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backTop = null;
        titleTop = null;
        parentActivityOrder = null;
        activityOrder = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (fragmentOrder != null) {
            fragmentOrder.onDestroy();
        }
        fragmentOrder = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        customInit(activityOrder, false, true, true);
        titleTop.setText(getIntent().getExtras().getString("title"));
        if (fragmentOrder == null) {
            fragmentOrder = new FragmentOrder(
                    getIntent().getExtras().getString("status"),
                    getIntent().getExtras().getInt("mode"),
                    getIntent().getExtras().getInt("what"),
                    getIntent().getExtras().getBoolean("isShowCheckBox"),
                    getIntent().getExtras().getBoolean("isShowQR"));
            FragmentUtils.addFragment(getSupportFragmentManager(), fragmentOrder, R.id.parent_activity_order);
        }
        FragmentUtils.hideAllShowFragment(fragmentOrder);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderCommentListChanged event) {
        if (event.good_id == null) {
            fragmentOrder.changeList(event.order_id, false);
        } else {
            fragmentOrder.changeList(event.good_id,true);
        }
    }

    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
        }
    }


}
