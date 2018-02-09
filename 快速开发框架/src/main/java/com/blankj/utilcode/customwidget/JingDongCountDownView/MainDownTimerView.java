package com.blankj.utilcode.customwidget.JingDongCountDownView;

import android.content.Context;
import android.util.AttributeSet;

import com.blankj.utilcode.customwidget.JingDongCountDownView.base.BaseCountDownTimerView;


public class MainDownTimerView extends BaseCountDownTimerView {

	public MainDownTimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MainDownTimerView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MainDownTimerView(Context context) {
		this(context,null);
	}

	@Override
	protected String getStrokeColor() {
		return "333333";
	}

	@Override
	protected String getTextColor() {
		return "000033";
	}

	@Override
	protected int getCornerRadius() {
		return 1;
	}

	@Override
	protected int getTextSize() {
		return 15;
	}

	@Override
	protected String getBackgroundColor() {
		return null;
	}

	
	
}
