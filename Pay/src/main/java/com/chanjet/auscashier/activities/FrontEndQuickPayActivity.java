package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanjet.auscashier.R;
import com.chanjet.auscashier.adapter.MyListViewAdapter;
import com.chanjet.yqpay.util.MsgUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/15.
 */

public class FrontEndQuickPayActivity extends Activity implements View.OnClickListener {

    private HashMap<String, String> reqMap;
//    private TextView tvQueryAuthentication, tvAuthentication, tvRequestPay;
    private ImageView ivBack;
    private TextView tvTitle;
    private ListView lvMenu;
    private ArrayList<String> mMenuList;
    private MyListViewAdapter adapter;

    private String oriNo = "", oriType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_pay);
        initData();
        initView();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.img_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("快捷支付(畅捷前台)");

        ivBack.setOnClickListener(this);

        lvMenu = (ListView) findViewById(R.id.lv_menu);
        adapter = new MyListViewAdapter(FrontEndQuickPayActivity.this, mMenuList);
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(mItemClickListener);
    }

    private void initData() {
        reqMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
        String[] menus = getResources().getStringArray(R.array.quick_pay_front_end_menu);
        mMenuList = new ArrayList<String>(Arrays.asList(menus));
        MsgUtil.showDebugLog("WJDemo", "menu list's size : " + mMenuList.size());
    }

    public void onClick(View view) {
        Intent intent = null;
        int i = view.getId();
        if (i == R.id.img_back) {
            finish();

            /*case R.id.tv_authentication_query:
                if (intent == null)
                    intent = new Intent();
                intent.putExtra("reqMap", reqMap);
                intent.setClass(FrontEndQuickPayActivity.this, QueryAuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_authentication:
                if (intent == null)
                    intent = new Intent();
                intent.putExtra("reqMap", reqMap);
                intent.setClass(FrontEndQuickPayActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                break;*/
        } else {/*if (intent == null)
                    intent = new Intent();
                intent.putExtra("reqMap", reqMap);
                intent.setClass(FrontEndQuickPayActivity.this, QueryAuthenticationActivity.class);
                startActivity(intent);*/

        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            switch (position) {
                case 1://支付请求(必须先绑卡)
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(FrontEndQuickPayActivity.this, FrontEndRequestPayActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case 2://直接支付请求
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(FrontEndQuickPayActivity.this, FrontEndDirectRequestPayActivity.class);
                    startActivity(intent);
                    break;
                default://鉴权绑卡请求
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(FrontEndQuickPayActivity.this, FrontEndAuthenticationActivity.class);
                    startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            oriNo = data.getStringExtra("TrxId");
            oriType = data.getStringExtra("TradeType");
            MsgUtil.showDebugLog("WJDemo", "oriType : " + oriType + ", oriNo : " + oriNo);
        }
    }
}
