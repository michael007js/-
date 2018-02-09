package com.sss.car;

import android.app.Activity;

import com.blankj.utilcode.ViewpagerHelper.indicator.ZoomIndicator;
import com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sss.car.custom.Advertisement.AdvertisementViewPagerHelper;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by leilei on 2017/12/25.
 */

public class AdvertisementManager {

    /**
     * 广告
     */
   public static void advertisement(String site_id, String classify_id, final OnAdvertisementCallBack onAdvertisementCallBack) {
        final List<AdvertisementModel> list = new ArrayList<>();
        try {
            RequestWeb.advertisement(new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("site_id", site_id)
                            .put("classify_id", classify_id)
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
                                        if (onAdvertisementCallBack!=null){
                                            onAdvertisementCallBack.onSuccessCallBack(list);
                                        }
                                    }else {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface OnAdvertisementCallBack{
        void onSuccessCallBack(List<AdvertisementModel> list);
    }

}
