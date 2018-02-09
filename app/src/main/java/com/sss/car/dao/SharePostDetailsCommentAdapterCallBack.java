package com.sss.car.dao;

import com.sss.car.model.SharePostDetailsCommentModel;

import java.util.List;

/**
 * Created by leilei on 2017/9/6.
 */

public interface SharePostDetailsCommentAdapterCallBack {
    void onComment(int position, List<SharePostDetailsCommentModel> list, SharePostDetailsCommentModel sharePostDetailsCommentModel);

    void onPraise(int position, List<SharePostDetailsCommentModel> list, SharePostDetailsCommentModel sharePostDetailsCommentModel);

}
