package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.TimePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2017/8/22.
 */

@SuppressWarnings("ALL")
public class ActivityMyDataShopChooseTime extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_activity_my_data_shop_choose_time)
    TextView showActivityMyDataShopChooseTime;
    @BindView(R.id.from_activity_my_data_shop_choose_time)
    TextView fromActivityMyDataShopChooseTime;
    @BindView(R.id.to_activity_my_data_shop_choose_time)
    TextView toActivityMyDataShopChooseTime;
    @BindView(R.id.activity_my_data_shop_choose_time)
    LinearLayout activityMyDataShopChooseTime;
    TimePicker timePicker;
    String start="", end="";
    ChangeInfoModel changeInfoModel;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (timePicker != null) {
            timePicker.clear();
        }
        timePicker = null;
        backTop = null;
        titleTop = null;
        changeInfoModel = null;
        start = null;
        end = null;
        showActivityMyDataShopChooseTime = null;
        fromActivityMyDataShopChooseTime = null;
        toActivityMyDataShopChooseTime = null;
        activityMyDataShopChooseTime = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_shop_choose_time);
        ButterKnife.bind(this);
        customInit(activityMyDataShopChooseTime, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        titleTop.setText("营业时间");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)||changeInfoModel==null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择营业时间");
                    return;
                }
                finish();
                EventBus.getDefault().post(changeInfoModel);
            }
        });
        if (!StringUtils.isEmpty(getIntent().getExtras().getString("extra"))) {
            String[] a = getIntent().getExtras().getString("extra").split("-");
            if (a == null || a.length == 0 || a.length == 1) {
                return;
            }
            start = a[0];
            end = a[1];
            showActivityMyDataShopChooseTime.setText("从" + start + "至" + end);
            fromActivityMyDataShopChooseTime.setText("从" + start);
            toActivityMyDataShopChooseTime.setText("至" + end);
        }

    }

    @OnClick({R.id.back_top, R.id.from_activity_my_data_shop_choose_time, R.id.to_activity_my_data_shop_choose_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.from_activity_my_data_shop_choose_time:
                createDialog(1);
                break;
            case R.id.to_activity_my_data_shop_choose_time:
                createDialog(2);
                break;
        }
    }

    /**
     * 创建时间选择器
     *
     * @param from
     */
    void createDialog(final int from) {
        if (timePicker != null) {
            timePicker.clear();
        }
        timePicker = null;
        timePicker = new TimePicker(getBaseActivityContext(), false);
        timePicker.setTimListener(new TimePicker.OnTimeCListener() {
            @Override
            public void onDateSelected(String hour, String min, String sec) {
                timePicker.dismiss();
            }

            @Override
            public void onDateSelected(String hour, String min) {
                timePicker.dismiss();
                if (from == 1) {

                    if (!StringUtils.isEmpty(end)) {
                        if (TimeUtils.string2Millis(end, "HH:mm") > TimeUtils.string2Millis(hour + ":" + min, "HH:mm")) {
                            if (changeInfoModel == null) {
                                changeInfoModel = new ChangeInfoModel();
                            }
                            changeInfoModel.type = getIntent().getExtras().getString("type");

                            changeInfoModel.msg = hour + ":" + min + "-" + end;
                            showActivityMyDataShopChooseTime.setText( start+"-"+end);

                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    start = hour + ":" + min;
                    fromActivityMyDataShopChooseTime.setText("从" + start);
                    showActivityMyDataShopChooseTime.setText( start+"-"+end);
                    showActivityMyDataShopChooseTime.setText("从" + start + "至" + end);
                } else {
                    if (!StringUtils.isEmpty(start)) {
                        if (TimeUtils.string2Millis(hour + ":" + min, "HH:mm") > TimeUtils.string2Millis(start, "HH:mm")) {
                            if (changeInfoModel == null) {
                                changeInfoModel = new ChangeInfoModel();
                            }
                            changeInfoModel.type = getIntent().getExtras().getString("type");
                            changeInfoModel.msg = start + "-" + hour + ":" + min;
                            showActivityMyDataShopChooseTime.setText( start+"-"+end);

                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    end = hour + ":" + min;
                    toActivityMyDataShopChooseTime.setText("至" + end);
                    showActivityMyDataShopChooseTime.setText("从" + start + "至" + end);
                }
            }
        });
        timePicker.show();
    }
}
