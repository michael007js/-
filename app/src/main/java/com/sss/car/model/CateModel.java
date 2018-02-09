package com.sss.car.model;

import android.net.Uri;

/**
 * Created by leilei on 2017/9/4.
 */

public class CateModel {
    public String cate_id;
    public String cate_name;
    public Uri logo;

    public CateModel() {
    }

    public CateModel(String cate_id, String cate_name, Uri logo) {
        this.cate_id = cate_id;
        this.cate_name = cate_name;
        this.logo = logo;
    }
}
