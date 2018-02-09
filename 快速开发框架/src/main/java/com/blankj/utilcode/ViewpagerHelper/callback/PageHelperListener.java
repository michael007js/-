package com.blankj.utilcode.ViewpagerHelper.callback;

import android.view.View;

/**
 * Created by Administrator on 2017/11/9.
 */

public interface PageHelperListener<T> {
    void getItemView(View view, T data);
}
