package com.blankj.utilcode.customwidget.JingDongCountDownView;

import android.content.Context;
import android.util.AttributeSet;

import com.blankj.utilcode.customwidget.JingDongCountDownView.base.BaseCountDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.BaseCountDownTimerViewWithDay;


public class SecondDownTimerViewWithDay extends BaseCountDownTimerViewWithDay {

	public SecondDownTimerViewWithDay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SecondDownTimerViewWithDay(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SecondDownTimerViewWithDay(Context context) {
		this(context,null);
	}

	@Override
	protected String getStrokeColor() {
		return "a75254";
	}

	@Override
	protected String getTextColor() {
		return "ffffff";
	}

	@Override
	protected int getCornerRadius() {
		return 2;
	}

	@Override
	protected int getTextSize() {
		return 18;
	}

	@Override
	protected String getBackgroundColor() {
		return "FF9966";
	}
	
}
