package com.sss.car.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.ViewpagerHelper.indicator.ZoomIndicator;
import com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.CountDownTimerUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.AdvertisementManager;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.WebViewActivity;
import com.sss.car.custom.Advertisement.AdvertisementViewPagerHelper;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by leilei on 2017/8/8.
 */

public class Splash extends BaseActivity {
    @BindView(R.id.count_time_splash)
    TextView countTimeSplash;
    @BindView(R.id.splash)
    RelativeLayout splash;
    Intent intent;
    SPUtils spUtils;
    List<AdvertisementModel> list = new ArrayList<>();
    CountDownTimerUtils countDownTimer = new CountDownTimerUtils(4000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            countTimeSplash.setText("倒计时" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            onViewClicked();
        }
    };
    AdvertisementViewPagerHelper advertisementViewPagerHelper;
    @BindView(R.id.banner)
    BannerViewPager banner;
    @BindView(R.id.bottom_zoom_arc)
    ZoomIndicator bottomZoomArc;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, LoginAndRegister.class)
                .putExtra("isShowBack", false);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        customInit(splash, false, false, false);
        advertisementViewPagerHelper = new AdvertisementViewPagerHelper();
        countDownTimer.start();
        advertisement();
//        AdvertisementManager.advertisement("16", "", new AdvertisementManager.OnAdvertisementCallBack() {
//            @Override
//            public void onCallBack(List<AdvertisementModel> list) {
//                advertisementViewPagerHelper
//                        .setView(banner, bottomZoomArc)
//                        .setData(getBaseActivity(),
//                                getWindowManager().getDefaultDisplay().getWidth(),
//                                getWindowManager().getDefaultDisplay().getHeight(),
//                                list, false);
//            }
//        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (advertisementViewPagerHelper != null) {
            advertisementViewPagerHelper.onReusme();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (advertisementViewPagerHelper != null) {
            advertisementViewPagerHelper.onPause();
        }

    }

    /**
     * 广告
     */
    void advertisement() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.advertisement(new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("site_id", "16")
                            .toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.e(response);
                            if (StringUtils.isEmpty(response)) {
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            AdvertisementModel advertisementModel = new AdvertisementModel();
                                            if ("1".equals(jsonArray.getJSONObject(i).getString("is_video"))) {
                                                advertisementModel.is_video = true;
                                                advertisementModel.picture = Config.url + jsonArray.getJSONObject(i).getString("picture");
                                            } else if ("0".equals(jsonArray.getJSONObject(i).getString("is_video"))) {
                                                advertisementModel.is_video = false;
                                                advertisementModel.picture = Config.url + jsonArray.getJSONObject(i).getString("picture");
                                            }
                                            advertisementModel.link_url = jsonArray.getJSONObject(i).getString("link_url");
                                            list.add(advertisementModel);
                                        }
                                        advertisementViewPagerHelper
                                                .setView(banner, bottomZoomArc)
                                                .setData(getBaseActivity(),
                                                        getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight(),
                                                        list, false)
                                                .setOnAdvertisementClickCallBack(new AdvertisementViewPagerHelper.OnAdvertisementClickCallBack() {
                                                    @Override
                                                    public void onClick(String url) {
                                                        Config.tempUrl= url;
                                                        onViewClicked();
                                                        isFinishByUser=true;
                                                    }
                                                });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    boolean isFinishByUser = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        if (advertisementViewPagerHelper != null) {
            advertisementViewPagerHelper.onDestroy();
        }
        spUtils = null;
    }


    @OnClick(R.id.count_time_splash)
    public void onViewClicked() {
        if (isFinishByUser == false) {
            if (spUtils == null) {
                spUtils = new SPUtils(getBaseActivityContext(), Config.defaultFileName, Context.MODE_PRIVATE);
            }
            if (!StringUtils.isEmpty(spUtils.getString("account")) && !StringUtils.isEmpty(spUtils.getString("password"))) {
                startActivity(new Intent(getBaseActivityContext(), Main.class)
                        .putExtra("where", "fromSplash"));
            } else {
                startActivity(intent);

            }
            finish();
        }
    }
}
