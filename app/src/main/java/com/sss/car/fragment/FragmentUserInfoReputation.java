package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.PieChart;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.ReputationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 用户信息==>信誉fragment
 * Created by leilei on 2017/9/4.
 */

@SuppressLint("ValidFragment")
public class FragmentUserInfoReputation extends BaseFragment {
    Unbinder unbinder;
    String targetId = "";
    List<ReputationModel> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    @BindView(R.id.total_integral)
    TextView totalIntegral;
    @BindView(R.id.one)
    TextView one;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.income)
    TextView three;
    @BindView(R.id.four)
    TextView four;
    @BindView(R.id.calc)
    PieChart calc;

    public FragmentUserInfoReputation() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user_info_reputation;
    }

    public FragmentUserInfoReputation(String targetId) {
        this.targetId = targetId;
    }

    @Override
    protected void lazyLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reputationList();
                    }
                });
            }
        }.start();

    }

    @Override
    protected void stopLoad() {
        clearRequestCall();
    }

    @Override
    public void onDestroy() {
        targetId = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();

    }

    public void reputationList() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.reputationList(
                    new JSONObject()
                            .put("member_id", targetId)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    int a = jsonObject.getJSONObject("data").getInt("credit");
                                    int b = jsonObject.getJSONObject("data").getInt("system");
                                    int c = jsonObject.getJSONObject("data").getInt("auth");
                                    int d = jsonObject.getJSONObject("data").getInt("trade_inc");
                                    int e = jsonObject.getJSONObject("data").getInt("trade_dec");
                                    totalIntegral .setText(a + "");
                                    one.setText(b + "");
                                    two.setText(c + "");
                                    three.setText(d + "");
                                    four.setText(e + "");
                                    float[] x = {(float) b / a, (float) c / a,(float) d / a,(float) e / a};
                                    String[] y = {"#fba62e", "#f26956","#2467b1","#b82325"};
                                    calc.initSrc(x, y);
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:reputation-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:reputation-0");
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }
}
