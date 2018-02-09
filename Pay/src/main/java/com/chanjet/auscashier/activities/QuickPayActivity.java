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

public class QuickPayActivity extends Activity implements View.OnClickListener {

    private HashMap<String, String> reqMap;
//    private TextView tvQueryAuthentication, tvAuthentication, tvRequestPay;\
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
        /*tvQueryAuthentication = (TextView) findViewById(R.id.tv_authentication_query);
        tvAuthentication = (TextView) findViewById(R.id.tv_authentication);
        tvRequestPay = (TextView) findViewById(R.id.tv_request_pay);*/
        ivBack.setOnClickListener(this);
        /*tvQueryAuthentication.setOnClickListener(this);
        tvAuthentication.setOnClickListener(this);
        tvRequestPay.setOnClickListener(this);*/

        lvMenu = (ListView) findViewById(R.id.lv_menu);
        adapter = new MyListViewAdapter(QuickPayActivity.this, mMenuList);
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(mItemClickListener);
    }

    private void initData() {
        reqMap = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
        String[] menus = getResources().getStringArray(R.array.quick_pay_menu);
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
                intent.setClass(QuickPayActivity.this, QueryAuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_authentication:
                if (intent == null)
                    intent = new Intent();
                intent.putExtra("reqMap", reqMap);
                intent.setClass(QuickPayActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                break;*/
        } else {/*if (intent == null)
                    intent = new Intent();
                intent.putExtra("reqMap", reqMap);
                intent.setClass(QuickPayActivity.this, QueryAuthenticationActivity.class);
                startActivity(intent);*/

        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            switch (position) {
                case 1://鉴权绑卡确认
                    if (intent == null)
                        intent = new Intent();
                    if (oriType != null && "auth_order".equals(oriType)) {
                        reqMap.put("oriNo", oriNo);
                        reqMap.put("oriType", oriType);
                    } else {
                        reqMap.put("oriNo", "");
                        reqMap.put("oriType", "auth_order");
                    }
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, ConfirmTradeActivity.class);
                    startActivity(intent);
                    break;
                case 2://鉴权绑卡查询
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, QueryAuthenticationActivity.class);
                    startActivity(intent);
                    break;
                case 3://支付请求
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, RequestPayActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case 4://支付确认
                    if (intent == null)
                        intent = new Intent();
                    if (oriType != null && "pay_order".equals(oriType)) {
                        reqMap.put("oriNo", oriNo);
                        reqMap.put("oriType", oriType);
                    } else {
                        reqMap.put("oriNo", "");
                        reqMap.put("oriType", "pay_order");
                    }
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, ConfirmTradeActivity.class);
                    startActivity(intent);
                    break;
                case 5://确认收货
                    if (intent == null)
                        intent = new Intent();
                    if (oriType != null && "pay_order".equals(oriType)) {
                        reqMap.put("oriNo", oriNo);
                    } else {
                        reqMap.put("oriNo", "");
                    }
                    reqMap.put("oriType", "confirm_receipt");
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, ConfirmTradeActivity.class);
                    startActivity(intent);
                    break;
                default://鉴权绑卡请求
                    if (intent == null)
                        intent = new Intent();
                    intent.putExtra("reqMap", reqMap);
                    intent.setClass(QuickPayActivity.this, AuthenticationActivity.class);
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
