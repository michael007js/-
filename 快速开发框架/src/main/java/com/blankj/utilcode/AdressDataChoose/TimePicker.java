package com.blankj.utilcode.AdressDataChoose;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.wheelview.OnItemSelectedListener;
import com.blankj.utilcode.AdressDataChoose.wheelview.WheelView;
import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描    述：
 * 创建日期：2017/7/20 14:26
 * 作    者：Chengfu
 * 邮    箱：
 * 备    注：
 */
public class TimePicker extends Dialog implements View.OnClickListener {

    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 24;
    private View view;
    private WheelView mHourWheelView;
    private WheelView mMinuteWheelView;
    private WheelView mSecondWheelView;
    private TextView mTvConfirm;
    private TextView mTvCancel;
    private OnTimeCListener mOnTimeCListener;

    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private List<String> seconds = new ArrayList<>();

    private int hourPos;
    private int minutePos;
    private int secondPos;
    private boolean withSecond = false;
    private Calendar nowCalendar;



    public void clear() {
        if (nowCalendar != null) {
            nowCalendar.clear();
        }
        nowCalendar = null;
    }


    public TimePicker(Context context, boolean withSecond) {
        super(context, R.style.transparentWindowStyle);
        this.withSecond = withSecond;
        view = View.inflate(context, R.layout.layout_address_picker, null);

        initView();
        initData();
        setListener();

        this.setContentView(view);

        this.setCanceledOnTouchOutside(true);

        //从底部弹出
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.windowAnimationStyle);  //添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    /**
     * 回调接口
     */
    public interface OnTimeCListener {
        void onDateSelected(String hour, String min, String sec);

        void onDateSelected(String hour, String min);
    }

    public void setTimListener(OnTimeCListener mOnTimeCListener) {
        this.mOnTimeCListener = mOnTimeCListener;
    }

    private void initView() {

        mHourWheelView = (WheelView) view.findViewById(R.id.wv_province);
        mMinuteWheelView = (WheelView) view.findViewById(R.id.wv_city);
        mSecondWheelView = (WheelView) view.findViewById(R.id.wv_district);
        mTvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        // 设置可见条目数量
        mHourWheelView.setVisibleItemCount(9);
        mMinuteWheelView.setVisibleItemCount(9);
        mSecondWheelView.setVisibleItemCount(9);

//        mHourWheelView.setLabel("时");
//        mMinuteWheelView.setLabel("分");
//        mSecondWheelView.setLabel("秒");

        mHourWheelView.isCenterLabel(true);
        mMinuteWheelView.isCenterLabel(true);
        mSecondWheelView.isCenterLabel(true);
        if (withSecond == false) {
            mSecondWheelView.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        //时*****************************
        mHourWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hourPos = index;
            }
        });
        //分********************
        mMinuteWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                minutePos = index;
            }
        });
        if (withSecond) {
            //秒********************
            mSecondWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    secondPos = index;
                }
            });
        }

        mTvConfirm.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    private void initData() {


        nowCalendar = Calendar.getInstance();
        hourPos = nowCalendar.get(Calendar.HOUR_OF_DAY);
        //初始化时
        for (int i = 0; i <= MAX_HOUR - MIN_HOUR; i++) {
            hours.add(format(MIN_HOUR + i));
        }
        mHourWheelView.setItems(hours);
        mHourWheelView.setCurrentItem(hourPos);

        minutePos = nowCalendar.get(Calendar.MINUTE);
        //初始化分
        for (int i = 0; i < 60; i++) {
            minutes.add(format(i + 1));
        }
        mMinuteWheelView.setItems(minutes);
        mMinuteWheelView.setCurrentItem(minutePos);


        if (withSecond) {
            secondPos = nowCalendar.get(Calendar.SECOND) - 1;
            //设置秒
            for (int i = 0; i < 60; i++) {
                seconds.add(format(i + 1));
            }
            mSecondWheelView.setItems(seconds);
            mSecondWheelView.setCurrentItem(secondPos);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (mOnTimeCListener != null) {
                int currentItem = mSecondWheelView.getCurrentItem();
                if (withSecond) {
                    currentItem = currentItem >= seconds.size() - 1 ? seconds.size() - 1 : currentItem;
                    mOnTimeCListener.onDateSelected(hours.get(mHourWheelView.getCurrentItem()), minutes.get(mMinuteWheelView.getCurrentItem()), seconds.get(currentItem));

                }else {
                    mOnTimeCListener.onDateSelected(hours.get(mHourWheelView.getCurrentItem()), minutes.get(mMinuteWheelView.getCurrentItem()));

                }


            }
        }
        cancel();
    }


    private String format(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }
}
