package com.sss.car;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;
import com.sss.car.utils.CarUtils;
import com.sss.car.view.ActivityDymaicDetails;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivitySharePostDetails;
import com.sss.car.view.ActivityShopInfo;
import com.sss.car.view.ActivityUserInfo;
import com.sss.car.view.ActivityWeb;

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
                                            AdvertisementModel advertisementModel=new AdvertisementModel();
                                            advertisementModel.ad_id=jsonArray.getJSONObject(i).getString("ad_id");
                                            advertisementModel.goods_type=jsonArray.getJSONObject(i).getString("goods_type");
                                            advertisementModel. ids=jsonArray.getJSONObject(i).getString("ids");
                                            advertisementModel.city_id=jsonArray.getJSONObject(i).getString("city_id");
                                            advertisementModel.link_url=jsonArray.getJSONObject(i).getString("link_url");
                                            advertisementModel.picture=Config.url + jsonArray.getJSONObject(i).getString("picture");
                                            advertisementModel. is_video=jsonArray.getJSONObject(i).getString("is_video");
                                            advertisementModel.type=jsonArray.getJSONObject(i).getString("type");
                                            list.add(advertisementModel);
                                        }
                                        if (onAdvertisementCallBack != null) {
                                            onAdvertisementCallBack.onSuccessCallBack(list);
                                        }
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

    public static void jump(AdvertisementModel model, Context context) {
        //0默认，1商品，2店铺，3动态，4帖子，5用户，6外接，7活动专题
        if ("1".equals(model.type)) {
            context.startActivity(new Intent(context, ActivityGoodsServiceDetails.class)
                    .putExtra("type", model.goods_type).putExtra("goods_id", model.ids));
        } else if ("2".equals(model.type)) {
            context.startActivity(new Intent(context, ActivityShopInfo.class).putExtra("shop_id", model.ids));
        } else if ("3".equals(model.type)) {
            context.startActivity(new Intent(context, ActivityDymaicDetails.class).putExtra("id", model.ids));
        } else if ("4".equals(model.type)) {
            context.startActivity(new Intent(context, ActivitySharePostDetails.class) .putExtra("community_id", model.ids).putExtra("is_show_keyboard", false));
        } else if ("5".equals(model.type)) {
            context.startActivity(new Intent(context, ActivityUserInfo.class).putExtra("id", model.ids));
        } else if ("6".equals(model.type)) {
            Config.tempUrl=model.link_url;
            CarUtils.startAdvertisement(context);
        } else if ("7".equals(model.type)) {
            context.startActivity(new Intent(context, ActivityWeb.class) .putExtra("type", ActivityWeb.ACTIVITY) .putExtra("id", model.ids));
        }

    }


    public interface OnAdvertisementCallBack {
        void onSuccessCallBack(List<AdvertisementModel> list);
    }

}
