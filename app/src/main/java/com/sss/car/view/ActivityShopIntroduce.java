package com.sss.car.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/12/26.
 */

public class ActivityShopIntroduce extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.activity_shop_introduce)
    LinearLayout activityShopIntroduce;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.input)
    EditText input;
    boolean isChanged = false;
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
        setContentView(R.layout.activity_shop_introduce);
        ButterKnife.bind(this);
        customInit(activityShopIntroduce, false, true, false);
        titleTop.setText("商铺简介");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");

            }
            finish();
        }
        input.setText(getIntent().getExtras().getString("extra"));
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (!isChanged) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "您未修改任何内容!");
                    return;
                }
                ChangeInfoModel changeUserInfoModel = new ChangeInfoModel();
                changeUserInfoModel.msg = input.getText().toString().trim();
                changeUserInfoModel.type = getIntent().getExtras().getString("type");
                EventBus.getDefault().post(changeUserInfoModel);
                finish();
                break;
        }
    }
}
