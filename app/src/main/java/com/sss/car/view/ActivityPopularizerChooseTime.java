package com.sss.car.view;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.DatePicker;
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

import static com.sss.car.R.id.from;
import static com.sss.car.R.id.to;

/**
 * Created by leilei on 2017/8/22.
 */

@SuppressWarnings("ALL")
public class ActivityPopularizerChooseTime extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.from_activity_popularize_choose_time)
    TextView fromActivityPopularizerChooseTime;
    @BindView(R.id.to_activity_popularize_choose_time)
    TextView toActivityPopularizerChooseTime;
    @BindView(R.id.activity_popularize_choose_time)
    LinearLayout ActivityPopularizerChooseTime;
    DatePicker datePicker;
    String start="", end="";
    ChangeInfoModel changeInfoModel;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

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
        fromActivityPopularizerChooseTime = null;
        toActivityPopularizerChooseTime = null;
        ActivityPopularizerChooseTime = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularize_choose_time);
        ButterKnife.bind(this);
        customInit(ActivityPopularizerChooseTime, false, true, false);
//        if (getIntent() == null || getIntent().getExtras() == null) {
//            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误");
//            finish();
//        }
        titleTop.setText("活动时间");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        rightButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请选择活动时间");
                    return;
                }
                changeInfoModel=new ChangeInfoModel();
                changeInfoModel.type="popularize";
                changeInfoModel.startTime=start;
                changeInfoModel.endTime=end;
                EventBus.getDefault().post(changeInfoModel);
                finish();
            }
        });
//        if (!StringUtils.isEmpty(getIntent().getExtras().getString("extra"))) {
//            String[] a = getIntent().getExtras().getString("extra").split("-");
//            if (a == null || a.length == 0 || a.length == 1) {
//                return;
//            }
//            start = a[0];
//            end = a[1];
//            fromActivityPopularizerChooseTime.setText("从" + start);
//            toActivityPopularizerChooseTime.setText("至" + end);
//        }

    }

    @OnClick({R.id.back_top, R.id.from_activity_popularize_choose_time, R.id.to_activity_popularize_choose_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.from_activity_popularize_choose_time:
                createDialog(1);
                break;
            case R.id.to_activity_popularize_choose_time:
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
                    if (!StringUtils.isEmpty(end)) {
                        if (TimeUtils.string2Millis(end, "yyyy-MM-dd") < TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd")) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    start = year + "-" + month + "-" + day;
                    fromActivityPopularizerChooseTime.setText("从"+start);
                } else {
                    if (!StringUtils.isEmpty(start)) {
                        if (TimeUtils.string2Millis(year + "-" + month + "-" + day, "yyyy-MM-dd") < TimeUtils.string2Millis(start, "yyyy-MM-dd")) {
                            ToastUtils.showShortToast(getBaseActivityContext(), "结束时间必须大于开始时间");
                            return;
                        }
                    }
                    end = year + "-" + month + "-" + day;
                    toActivityPopularizerChooseTime.setText("至" + end);
                }
            }
        });
        datePicker.show();
    }
}
