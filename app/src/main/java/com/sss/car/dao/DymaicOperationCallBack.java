package com.sss.car.dao;


import android.view.View;
import android.view.ViewGroup;

import com.sss.car.adapter.DymaicAdapter;
import com.sss.car.model.DymaicModel;

import java.util.List;


/**
 * Created by leilei on 2017/8/26.
 */

public interface DymaicOperationCallBack {
    void click(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list);

    void comment(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent);

    void praise(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent);

    void delete(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list);

    void share(int poistion, DymaicModel shareDymaicModel, List<DymaicModel> list, View convertView, ViewGroup parent);

    void onClickImage(int position, String url, List<String> urlList);
}
