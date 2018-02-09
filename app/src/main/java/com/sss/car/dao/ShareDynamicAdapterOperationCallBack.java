package com.sss.car.dao;

import com.sss.car.model.ShareDynamicModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/9.
 */

public interface ShareDynamicAdapterOperationCallBack {

    void onClickDynamic(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list);

    void onClickDynamicPraise(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list);

    void onClickDynamicComment(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list);

    void onClickDynamicShare(int position, ShareDynamicModel shareDynamicModel, List<ShareDynamicModel> list);

}
