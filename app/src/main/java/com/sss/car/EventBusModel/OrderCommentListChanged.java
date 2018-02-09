package com.sss.car.EventBusModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/10.
 */

public class OrderCommentListChanged {
    public String order_id;
    public List<String> good_id;

    public OrderCommentListChanged(String order_id, List<String> good_id) {
        this.order_id = order_id;
        this.good_id = good_id;
    }

    public OrderCommentListChanged(String order_id) {
        this.order_id = order_id;
    }
}
