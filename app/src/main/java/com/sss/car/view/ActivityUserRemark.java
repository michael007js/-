package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.Remark;
import com.sss.car.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/12/27.
 */

public class ActivityUserRemark extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.activity_user_remark)
    LinearLayout activityUserRemark;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        setContentView(R.layout.activity_user_remark);
        ButterKnife.bind(this);
        titleTop.setText("备注名");
        rightButtonTop.setText("完成");
        rightButtonTop.setTextColor(getResources().getColor(R.color.white));
        APPOftenUtils.setBackgroundOfVersion(rightButtonTop,getResources().getDrawable(R.color.mainColor));
        customInit(activityUserRemark, false, true, false);
        input.setText(getIntent().getExtras().getString("content"));

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
//                if (StringUtils.isEmpty(input.getText().toString())){
//                    ToastUtils.showShortToast(getBaseActivityContext(),"请输入备注");
//                    return;
//                }
                EventBus.getDefault().post(new Remark(input.getText().toString()));
                finish();
                break;
        }
    }
}
