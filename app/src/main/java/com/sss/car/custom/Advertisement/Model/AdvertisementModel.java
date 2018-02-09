package com.sss.car.custom.Advertisement.Model;

/**
 * Created by leilei on 2017/11/27.
 */

public class AdvertisementModel {
    public String link_url;
    public String picture;
    public boolean is_video;

    public AdvertisementModel() {
    }

    public AdvertisementModel(String link_url, String picture, boolean is_video) {
        this.link_url = link_url;
        this.picture = picture;
        this.is_video = is_video;
    }
}
