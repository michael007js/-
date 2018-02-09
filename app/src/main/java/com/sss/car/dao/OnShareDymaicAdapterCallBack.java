package com.sss.car.dao;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.model.DymaicDetailsPraiseModel;
import com.sss.car.model.ShareDynamicModel;

/**
 * Created by leilei on 2017/12/30.
 */

public interface OnShareDymaicAdapterCallBack {
    void onTransmitDymaic(String trends_pid, TextView share_number, ShareDynamicModel shareDynamicModel);

    void onPraise(String trends_id, TextView praise_number, ShareDynamicModel shareDynamicModel, SimpleDraweeView praise);

    void onClickUserPic(int poistion, ShareDynamicModel shareDynamicModel);
}
