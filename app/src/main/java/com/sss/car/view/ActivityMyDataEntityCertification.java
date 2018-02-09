package com.sss.car.view;

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
 * Created by leilei on 2017/11/8.
 */

public class ActivityMyDataEntityCertification extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people)
    LinearLayout people;
    @BindView(R.id.shop)
    LinearLayout shop;
    @BindView(R.id.activity_my_data_entity_certification)
    LinearLayout activityMyDataEntityCertification;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        backTop = null;
        titleTop = null;
        people = null;
        shop = null;
        activityMyDataEntityCertification = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_entity_certification);
        ButterKnife.bind(this);
        titleTop.setText("用户认证");
        customInit(activityMyDataEntityCertification,false,true,false);
    }

    @OnClick({R.id.back_top, R.id.people, R.id.shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.people:
                if (getBaseActivityContext()!=null){
//                    startActivity(new Intent(getBaseActivityContext(),ActivityPeople.class));
                    startActivity(new Intent(getBaseActivityContext(),ActivityPeopleCertification.class));
                }
                break;
            case R.id.shop:
                if (getBaseActivityContext()!=null){
//                    startActivity(new Intent(getBaseActivityContext(),ActivityMyDataEntitiesCertification.class));
                    startActivity(new Intent(getBaseActivityContext(),ActivityShopCertification.class));
                }
                break;
        }
    }
}
