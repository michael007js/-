package com.sss.car.dao;


import com.sss.car.model.EntitiesCertificationServiceModel;

import java.util.List;

/**
 *
 * Created by leilei on 2017/8/22.
 */

public interface CertificationServiceClickCallBack {
    void onClickChange(int pos, List<EntitiesCertificationServiceModel> list);
}
