package com.blankj.utilcode.customwidget.Slidebar.indexbar.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;

import java.util.List;

public abstract class SideBarListViewAdapter extends SSS_Adapter {

	public SideBarListViewAdapter(Context context) {
		super(context);
	}

	public SideBarListViewAdapter(Context context, int itemLayoutId) {
		super(context, itemLayoutId);
	}

	public SideBarListViewAdapter(Context context, int itemLayoutId, List mList) {
		super(context, itemLayoutId, mList);
	}

	public abstract  int getPositionForSection(int section);
}
