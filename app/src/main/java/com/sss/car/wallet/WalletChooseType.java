package com.sss.car.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.EventBusModel.ChangedWalletModel;
import com.sss.car.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leilei on 2018/1/28.
 */

public class WalletChooseType extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.arrows_bank)
    SimpleDraweeView arrowsBank;
    @BindView(R.id.arrows_wx)
    SimpleDraweeView arrowsWx;
    @BindView(R.id.arrows_zfb)
    SimpleDraweeView arrowsZfb;
    @BindView(R.id.wallet_choose_type)
    LinearLayout walletChooseType;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedWalletModel changedWalletModel) {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_choose_type);
        ButterKnife.bind(this);
        titleTop.setText("选择充值方式");
        customInit(walletChooseType,false,true,true);
    }

    @OnClick({R.id.back_top, R.id.bank, R.id.wx, R.id.zfb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.bank:
                arrowsBank.setVisibility(View.VISIBLE);
                arrowsWx.setVisibility(View.GONE);
                arrowsZfb.setVisibility(View.GONE);
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletRecharge.class)
                            .putExtra("type","bank")
                    );
                }
                break;
            case R.id.wx:
                arrowsBank.setVisibility(View.GONE);
                arrowsWx.setVisibility(View.VISIBLE);
                arrowsZfb.setVisibility(View.GONE);
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletRecharge.class)
                            .putExtra("type","wx")
                    );
                }
                break;
            case R.id.zfb:
                arrowsBank.setVisibility(View.GONE);
                arrowsWx.setVisibility(View.GONE);
                arrowsZfb.setVisibility(View.VISIBLE);
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), WalletRecharge.class)
                            .putExtra("type","zfb")
                    );
                }
                break;
        }
    }
}
