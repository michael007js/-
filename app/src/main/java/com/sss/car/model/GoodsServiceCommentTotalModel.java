package com.sss.car.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/9/17.
 */

public class GoodsServiceCommentTotalModel {
   public String all;
    public String goods;
    public String centre;
    public String negative;
    public List<GoodsServiceCommentModel> list=new ArrayList<>();

    @Override
    public String toString() {
        return "GoodsServiceCommentTotalModel{" +
                "all='" + all + '\'' +
                ", goods='" + goods + '\'' +
                ", centre='" + centre + '\'' +
                ", negative='" + negative + '\'' +
                ", list=" + list +
                '}';
    }
}
