package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
import com.blankj.utilcode.AdressDataChoose.TimePicker;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
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
 * Created by leilei on 2018/1/5.
 */

public class ActivityChooseDate extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.to)
    TextView to;
    @BindView(R.id.activity_choose_date)
    LinearLayout activityChooseDate;
    DatePicker datePicker;
    String f, t;

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
        setContentView(R.layout.activity_choose_date);
        ButterKnife.bind(this);
        customInit(activityChooseDate, false, true, false);
        titleTop.setText("选择时间");
        rightButtonTop.setText("确定");
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.from, R.id.to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if (StringUtils.isEmpty(f) || StringUtils.isEmpty(t)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择时间");
                    return;
                }

                ChangeInfoModel changeInfoModel = new ChangeInfoModel();
                changeInfoModel.type = getIntent().getExtras().getString("type");
                changeInfoModel.msg = f + "—" + t;
                EventBus.getDefault().post(changeInfoModel);
                finish();

                break;
            case R.id.from:
                createDialog(1);
                break;
            case R.id.to:
                createDialog(2);
                break;
        }
    }

    /**
     * 创建时间选择器
     *
     * @param where
     */
    void createDialog(final int where) {
        if (datePicker != null) {
            datePicker.clear();
        }
        datePicker = null;
        datePicker = new DatePicker(getBaseActivityContext());
        datePicker.setDateListener(new DatePicker.OnDateCListener() {
            @Override
            public void onDateSelected(String year, String month, String day) {
                if (where == 1) {
                    if (!StringUtils.isEmpty(t)) {
                        if (TimeUtils.string2Millis(t, "yyyy-MM-dd") < TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    f = year + "-" + month + "-" + day;
                    from.setText("从" + year + "-" + month + "-" + day);
                } else {
                    if (!StringUtils.isEmpty(f)) {
                        if (TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd") < TimeUtils.string2Millis(f, "yyyy-MM-dd")) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    t = year + "-" + month + "-" + day;
                    to.setText("至" + year + "-" + month + "-" + day);
                }
            }
        });
        datePicker.show();
    }
}
