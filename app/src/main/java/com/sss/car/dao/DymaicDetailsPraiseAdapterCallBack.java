package com.sss.car.dao;

import com.sss.car.model.DymaicDetailsCommentModel;
import com.sss.car.model.DymaicDetailsPraiseModel;

/**
 * Created by leilei on 2017/9/2.
 */

public interface DymaicDetailsPraiseAdapterCallBack {
    void onClickPraiseItem(int poistion, DymaicDetailsPraiseModel dymaicDetailsPraiseModel);
}
