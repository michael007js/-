package com.sss.car;

import android.app.Activity;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by leilei on 2017/10/15.
 */

public class P {

    public static void e(final YWLoadingDialog ywLoadingDialog, String member_id, final Activity activity, final p p) {
        try {
           RequestWeb.exits_pass(
                    new JSONObject()
                            .put("member_id", member_id)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        if (p!=null){
                                            p.exist();
                                        }
                                    } else {
                                        if (p!=null){
                                            p.nonexistence();
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:p-0");
            e.printStackTrace();
        }
    }

    public static void r(final YWLoadingDialog ywLoadingDialog, String member_id, String password , final Activity activity, final r r) {
        try {
           new RequestModel(System.currentTimeMillis() + "", RequestWeb.pay_pass(
                    new JSONObject()
                            .put("member_id", member_id)
                            .put("password", password)
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(activity, e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    LogUtils.e("111");
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        LogUtils.e("222");
                                        if (r!=null){

                                            r.match();
                                        }
                                    } else {
                                        LogUtils.e("333");
                                        if (r!=null){
                                            r.mismatches();
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(activity, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(activity, "数据解析错误Err:p-0");
                                e.printStackTrace();
                            }
                        }
                    }));
        } catch (JSONException e) {
            ToastUtils.showShortToast(activity, "数据解析错误Err:p-0");
            e.printStackTrace();
        }
    }

    public interface p{
        void exist();

        void nonexistence();
    }

    public interface r{
        void match();

        void mismatches();
    }

}
