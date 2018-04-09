package com.sss.car.custom.Advertisement.Model;

/**
 * Created by leilei on 2017/11/27.
 */

public class AdvertisementModel {
    public String ad_id;
    public String goods_type;
    public String ids;
    public String city_id;
    public String link_url;
    public String picture;
    public String is_video;
    public String type;

    public AdvertisementModel() {
    }

    public AdvertisementModel(String link_url, String picture, String is_video) {
        this.link_url = link_url;
        this.picture = picture;
        this.is_video = is_video;
    }
}
