package com.blankj.utilcode.TimeProfession;


import java.util.Calendar;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.ToastUtils;
import com.rey.material.app.BottomSheetDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class time {
    /**
     * Called when the activity is first created.
     */

    private int minYear = 1970;  //最小年份
    private int fontSize = 13;     //字体大小
    private WheelView yearWheel, monthWheel, dayWheel, hourWheel, minuteWheel, secondWheel;
    private TextView yes;
    public static String[] yearContent = null;
    public static String[] monthContent = null;
    public static String[] dayContent = null;
    public static String[] hourContent = null;
    public static String[] minuteContent = null;
    public static String[] secondContent = null;
    private OnTimeCallBack onTimeCallBack;

    public void setOnTimeCallBack(OnTimeCallBack onTimeCallBack) {
        this.onTimeCallBack = onTimeCallBack;
    }

    public void initContent() {
        yearContent = new String[10];
        for (int i = 0; i < 10; i++)
            yearContent[i] = String.valueOf(i + 2018);

        monthContent = new String[12];
        for (int i = 0; i < 12; i++) {
            monthContent[i] = String.valueOf(i + 1);
            if (monthContent[i].length() < 2) {
                monthContent[i] = "0" + monthContent[i];
            }
        }

        dayContent = new String[31];
        for (int i = 0; i < 31; i++) {
            dayContent[i] = String.valueOf(i + 1);
            if (dayContent[i].length() < 2) {
                dayContent[i] = "0" + dayContent[i];
            }
        }
        hourContent = new String[24];
        for (int i = 0; i < 24; i++) {
            hourContent[i] = String.valueOf(i);
            if (hourContent[i].length() < 2) {
                hourContent[i] = "0" + hourContent[i];
            }
        }

        minuteContent = new String[60];
        for (int i = 0; i < 60; i++) {
            minuteContent[i] = String.valueOf(i);
            if (minuteContent[i].length() < 2) {
                minuteContent[i] = "0" + minuteContent[i];
            }
        }
        secondContent = new String[60];
        for (int i = 0; i < 60; i++) {
            secondContent[i] = String.valueOf(i);
            if (secondContent[i].length() < 2) {
                secondContent[i] = "0" + secondContent[i];
            }
        }
    }

    public void buttonClick(final Context context) {
        View view = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_picker, null);

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curSecond = calendar.get(Calendar.SECOND);

        yearWheel = (WheelView) view.findViewById(R.id.yearwheel);
        monthWheel = (WheelView) view.findViewById(R.id.monthwheel);
        dayWheel = (WheelView) view.findViewById(R.id.daywheel);
        hourWheel = (WheelView) view.findViewById(R.id.hourwheel);
        minuteWheel = (WheelView) view.findViewById(R.id.minutewheel);
        secondWheel = (WheelView) view.findViewById(R.id.secondwheel);
        yes = (TextView) view.findViewById(R.id.yes);

        yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
        yearWheel.setCurrentItem(curYear - 2018);
        yearWheel.setCyclic(true);
        yearWheel.setInterpolator(new AnticipateOvershootInterpolator());


        monthWheel.setAdapter(new StrericWheelAdapter(monthContent));

        monthWheel.setCurrentItem(curMonth - 1);

        monthWheel.setCyclic(true);
        monthWheel.setInterpolator(new AnticipateOvershootInterpolator());

        dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
        dayWheel.setCurrentItem(curDay - 1);
        dayWheel.setCyclic(true);
        dayWheel.setInterpolator(new AnticipateOvershootInterpolator());

        hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
        hourWheel.setCurrentItem(curHour);
        hourWheel.setCyclic(true);
        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());

        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
        minuteWheel.setCurrentItem(curMinute);
        minuteWheel.setCyclic(true);
        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());

        secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
        secondWheel.setCurrentItem(curSecond);
        secondWheel.setCyclic(true);
        secondWheel.setInterpolator(new AnticipateOvershootInterpolator());


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                sb.append(yearWheel.getCurrentItemValue()).append("-").append(monthWheel.getCurrentItemValue()).append("-").append(dayWheel.getCurrentItemValue());
                sb.append(" ");
                sb.append(hourWheel.getCurrentItemValue())
//                        .append(":").append(minuteWheel.getCurrentItemValue())
//                        .append(":").append(secondWheel.getCurrentItemValue() )
                ;
//                time_tv.setText(sb);
//                ToastUtils.showShortToast(context, sb.toString());
                if (onTimeCallBack!=null){
                    onTimeCallBack.onCallBack(yearWheel.getCurrentItemValue(),monthWheel.getCurrentItemValue(),dayWheel.getCurrentItemValue(),hourWheel.getCurrentItemValue(),minuteWheel.getCurrentItemValue(),secondWheel.getCurrentItemValue());
                }
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.show();
    }


    public interface OnTimeCallBack{
        void onCallBack(String year,String month,String day,String hour,String min,String sec);
    }
}