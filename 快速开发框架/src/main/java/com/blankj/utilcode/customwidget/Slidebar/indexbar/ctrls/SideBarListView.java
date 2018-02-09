package com.blankj.utilcode.customwidget.Slidebar.indexbar.ctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.Slidebar.indexbar.adapter.SideBarListViewAdapter;


public class SideBarListView extends FrameLayout {

    private OnSideBarListViewCallBack onSideBarListViewCallBack;
    private ListView listview;
    private SideBarListViewAdapter myAdapter;
    private TextView tv_dialog;
    private SideBar sidebar;

    public SideBarListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_listview_sidebar,
                this);
        initView();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        tv_dialog = (TextView) findViewById(R.id.tv_dialog);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        sidebar.setTextViewDialog(tv_dialog);
        /**
         * 根据用户点击那个字母将listview移动到相应位置
         */
        sidebar.setOnTouchingLetterChangedListener(new SideBar.ITouchingLetterChangedListener() {

            @Override
            public void OnTouchingLetterChanged(String cString) {
                int position = -1;
                if (cString.length() > 0) {
                    position = myAdapter.getPositionForSection(cString
                            .charAt(0));
                }
                if (position != -1) {
                    listview.setSelection(position);
                } else if (cString.contains("#")) {
                    listview.setSelection(0);
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public SideBarListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBarListView(Context context) {
        this(context, null);
    }

    public SideBarListViewAdapter getMyAdapter() {
        return myAdapter;
    }

    public void setMyAdapter(SideBarListViewAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }


    public interface OnSideBarListViewCallBack {
        void onClickItem(int position);
    }

}
