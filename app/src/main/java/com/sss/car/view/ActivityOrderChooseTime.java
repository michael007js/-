package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
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
 * 服务订单中选择时间页面
 * Created by leilei on 2017/8/22.
 */

public class ActivityOrderChooseTime extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.show_activity_order_choose_time)
    TextView showActivitActivityOrderChooseTime;
    @BindView(R.id.from_activity_order_choose_time)
    TextView fromActivitActivityOrderChooseTime;
    @BindView(R.id.to_activity_order_choose_time)
    TextView toActivitActivityOrderChooseTime;
    @BindView(R.id.activity_order_choose_time)
    LinearLayout activitActivityOrderChooseTime;
    DatePicker datePicker;
    String start, end;
    ChangeInfoModel changeInfoModel;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (datePicker != null) {
            datePicker.clear();
        }
        datePicker = null;
        backTop = null;
        titleTop = null;
        changeInfoModel = null;
        start = null;
        end = null;
        showActivitActivityOrderChooseTime = null;
        fromActivitActivityOrderChooseTime = null;
        toActivitActivityOrderChooseTime = null;
        activitActivityOrderChooseTime = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_choose_time);
        ButterKnife.bind(this);
        customInit(activitActivityOrderChooseTime, false, true, false);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
            finish();
        }
        titleTop.setText("预约时间");
    }

    @OnClick({R.id.back_top, R.id.from_activity_order_choose_time, R.id.to_activity_order_choose_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.from_activity_order_choose_time:
                createDialog(1);
                break;
            case R.id.to_activity_order_choose_time:
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
        if (datePicker != null) {
            datePicker.clear();
        }
        datePicker = null;
        datePicker = new DatePicker(getBaseActivityContext());
        datePicker.setDateListener(new DatePicker.OnDateCListener() {
            @Override
            public void onDateSelected(String year, String month, String day) {
                datePicker.dismiss();
                datePicker.dismiss();
                if (from == 1) {

                    if (!StringUtils.isEmpty(end)) {
                        if (TimeUtils.string2Millis(end, "yyyy-MM-dd") > TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                            if (changeInfoModel == null) {
                                changeInfoModel = new ChangeInfoModel();
                            }
                            if (System.currentTimeMillis()> TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "所选时间必须大于当前时间");
                                return;
                            }
                            start = year + "-" + month + "-" + day;
                            changeInfoModel.type = getIntent().getExtras().getString("type");
                            changeInfoModel.msg = start + "-" + end;
                            EventBus.getDefault().post(changeInfoModel);
                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    if (System.currentTimeMillis()> TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "所选时间必须大于当前时间");
                        return;
                    }
                    start = year + "-" + month + "-" + day;
                    fromActivitActivityOrderChooseTime.setText("从" + start);
                } else {
                    if (!StringUtils.isEmpty(start)) {
                        if (TimeUtils.string2Millis(year + "-" + month + "-" + day , "yyyy-MM-dd") > TimeUtils.string2Millis(start, "yyyy-MM-dd")) {
                            if (changeInfoModel == null) {
                                changeInfoModel = new ChangeInfoModel();
                            }
                            if (System.currentTimeMillis()> TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "所选时间必须大于当前时间");
                                return;
                            }
                            end = year + "-" + month + "-" + day;
                            changeInfoModel.type = getIntent().getExtras().getString("type");
                            changeInfoModel.msg = start + "-" + end;
                            EventBus.getDefault().post(changeInfoModel);
                        } else {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    if (System.currentTimeMillis()> TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "所选时间必须大于当前时间");
                        return;
                    }
                    end = year + "-" + month + "-" + day;
                    toActivitActivityOrderChooseTime.setText("至" + end);
                }
            }

        });
        datePicker.show();
    }
}
