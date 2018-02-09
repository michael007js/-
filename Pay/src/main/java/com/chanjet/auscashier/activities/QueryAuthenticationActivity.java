package com.chanjet.auscashier.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chanjet.auscashier.R;
import com.chanjet.auscashier.adapter.MyListViewAdapter;
import com.chanjet.auscashier.models.BankCardInfo;
import com.chanjet.yqpay.Constants$CallBackConstants;
import com.chanjet.yqpay.IYQPayCallback;
import com.chanjet.yqpay.YQPayApi;
import com.chanjet.yqpay.util.MsgUtil;
import com.chanjet.yqpay.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/14.
 */

public class QueryAuthenticationActivity extends Activity implements View.OnClickListener {

    private EditText etCardPrefix, etCardSuffix, etExtension;
    private RadioGroup rgCardType;
    private Button btOK;
    private ListView lvCardList;

    private String cardType = "";

    private HashMap<String, String> reqMaps;
    private ArrayList<String> mCardList = null;
    private MyListViewAdapter adapter = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_authentication);
        initData();
        initView();
    }

    private void initData() {
        reqMaps = (HashMap<String, String>) getIntent().getSerializableExtra("reqMap");
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etCardPrefix = (EditText) findViewById(R.id.et_card_prefix);
        etCardSuffix = (EditText) findViewById(R.id.et_card_suffix);

        rgCardType = (RadioGroup) findViewById(R.id.rg_card_type);
        btOK = (Button) findViewById(R.id.bt_ok);
        lvCardList = (ListView) findViewById(R.id.lv_card_list);

        etExtension = (EditText) findViewById(R.id.et_extension);

        rgCardType.setOnCheckedChangeListener(mListener);
        btOK.setOnClickListener(this);

        if (mCardList == null)
            mCardList = new ArrayList<>();
        adapter = new MyListViewAdapter(QueryAuthenticationActivity.this, mCardList);
        lvCardList.setAdapter(adapter);
    }

    private RadioGroup.OnCheckedChangeListener mListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton rbSelected = (RadioButton) rgCardType.findViewById(checkedId);
            cardType = (String) rbSelected.getTag();
            MsgUtil.showDebugLog("WJDemo", "selected " + rbSelected.getText() + ", tag is : " + rbSelected.getTag());
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btOK) {
//            if (checkInput()) {
            d0_queryAuthentication();
//            }
        }
    }

    private boolean checkInput() {
        if (etCardPrefix.getText().toString().trim().length() < 6) {
            MsgUtil.showToastShort(QueryAuthenticationActivity.this, "卡号前六位有误");
            return false;
        }
        if (etCardSuffix.getText().toString().trim().length() < 4) {
            MsgUtil.showToastShort(QueryAuthenticationActivity.this, "卡号后四位有误");
            return false;
        }
        if (cardType.equals("") || cardType == null) {
            MsgUtil.showToastShort(QueryAuthenticationActivity.this, "请选择卡类型");
            return false;
        }
        return true;
    }

    private void d0_queryAuthentication() {
        dialog = ProgressDialog.show(this, "提示", "处理中，请稍后...");
        dialog.setCancelable(true);
        if (mCardList != null && mCardList.size() > 0) {
            mCardList.clear();
            adapter.notifyDataSetChanged();
        }
        YQPayApi.doPay(QueryAuthenticationActivity.this, getQueryPara(), new IYQPayCallback() {
            @Override
            public void payResult(String status, String message) {
                dialog.dismiss();
                MsgUtil.showDebugLog("WJDemo", "Query bind card info result : " + message);
                if (status.equals(Constants$CallBackConstants.CALLBACK_FAILD))
                    Toast.makeText(QueryAuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
                else {
                    Gson gson = new Gson();
                    HashMap<String, String> respMap = gson.fromJson(message, HashMap.class);
                    ArrayList<BankCardInfo> cardList = gson.fromJson(respMap.get("BindingCards"), new TypeToken<ArrayList<BankCardInfo>>() {
                    }.getType());
                    Toast.makeText(QueryAuthenticationActivity.this, respMap.get("RetMsg"), Toast.LENGTH_LONG).show();
                    if (cardList != null && cardList.size() > 0) {
                        for (BankCardInfo info : cardList)
                            mCardList.add(info.getCardBegin() + "****" + info.getCardEnd());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private HashMap<String, String> getQueryPara() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("Version", reqMaps.get("Version"));
        maps.put("TradeDate", reqMaps.get("TradeDate"));
        maps.put("TradeTime", reqMaps.get("TradeTime"));

        maps.put("PartnerId", reqMaps.get("PartnerId"));
        maps.put("InputCharset", reqMaps.get("InputCharset"));
        maps.put("MchId", reqMaps.get("MchId"));
        maps.put("PAY_KEY", reqMaps.get("PAY_KEY"));

        maps.put("BankCode", "BindCardQuery");
        maps.put("TrxId", StringUtils.getOrderid());// 订单号
        maps.put("MerUserId", "123");
        maps.put("CardBegin", etCardPrefix.getText().toString().trim());
        maps.put("CardEnd", etCardSuffix.getText().toString().trim());
        maps.put("BkAcctTp", cardType);//卡类型 00-贷记卡  01-借记卡
        if (!StringUtils.isEmpty(etExtension.getText().toString().trim()))
            maps.put("Extension", etExtension.getText().toString().trim());//扩展字段
        return maps;
    }
}
