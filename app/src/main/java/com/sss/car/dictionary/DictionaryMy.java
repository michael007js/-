package com.sss.car.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的宝典
 * Created by leilei on 2017/11/3.
 */

public class DictionaryMy extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.buyer_dictionary_my)
    LinearLayout buyerDictionaryMy;
    @BindView(R.id.seller_dictionary_my)
    LinearLayout sellerDictionaryMy;
    @BindView(R.id.dictionary_my)
    LinearLayout dictionaryMy;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        buyerDictionaryMy = null;
        sellerDictionaryMy = null;
        dictionaryMy = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_my);
        ButterKnife.bind(this);
        customInit(dictionaryMy, false, true, false);
        titleTop.setText("我的宝典");

    }

    @OnClick({R.id.back_top, R.id.seller_dictionary_my, R.id.dictionary_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.seller_dictionary_my:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),DictionaryBuyerSellerPublic.class)
                            .putExtra("title",DictionaryBuyerSellerPublic.TITLE_SELLER)
                            .putExtra("type","2")
                    );
                }
                break;
            case R.id.dictionary_my:
                if (getBaseActivityContext()!=null){
                    startActivity(new Intent(getBaseActivityContext(),DictionaryBuyerSellerPublic.class)
                            .putExtra("title",DictionaryBuyerSellerPublic.TITLE_BUYER)
                            .putExtra("type","1")
                    );
                }

                break;
        }
    }
}
