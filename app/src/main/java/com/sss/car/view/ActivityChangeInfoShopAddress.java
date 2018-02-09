package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChooseAdress;
import com.sss.car.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/12/26.
 */

public class ActivityChangeInfoShopAddress extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.edit_activity_change_info_shop_address)
    EditText editActivityChangeInfoShopAddress;
    @BindView(R.id.activity_change_info_shop_address)
    LinearLayout activityChangeInfoShopAddress;
    @BindView(R.id.select)
    TextView select;
    String lat, lng;
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
        setContentView(R.layout.activity_change_info_shop_address);
        ButterKnife.bind(this);
        titleTop.setText("商铺地址");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        if (getIntent() == null || getIntent().getExtras() == null) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");

            }
            finish();
        }
        customInit(activityChangeInfoShopAddress, false, true, true);
        editActivityChangeInfoShopAddress.setText(getIntent().getExtras().getString("extra"));
        editActivityChangeInfoShopAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseAdress event) {
        lat = event.lai;
        lng = event.lng;
        select.setText(event.adress);
        editActivityChangeInfoShopAddress.setText(event.adress);
        isChanged = true;
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.select})
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
                changeUserInfoModel.msg = editActivityChangeInfoShopAddress.getText().toString().trim();
                changeUserInfoModel.type = getIntent().getExtras().getString("type");
                if (StringUtils.isEmpty(lat)||StringUtils.isEmpty(lng)){
                    changeUserInfoModel.lat = Config.latitude;
                    changeUserInfoModel.lng= Config.longitude;
                }else {
                    changeUserInfoModel.lat = lat;
                    changeUserInfoModel.lng= lng;
                }
                EventBus.getDefault().post(changeUserInfoModel);
                finish();
                break;
            case R.id.select:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChooseAdress.class));
                }
                break;
        }
    }
}
