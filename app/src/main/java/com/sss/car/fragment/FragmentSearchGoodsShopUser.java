package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.FragmentSearchGoodsShopUserModel;
import com.sss.car.view.ActivitySearchGoodsShopUserListPublic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * Created by leilei on 2018/1/13.
 */
@SuppressLint("ValidFragment")
public class FragmentSearchGoodsShopUser extends BaseFragment {
    @BindView(R.id.gridview_fragment_search_goods_shop_user)
    GridView gridviewFragmentSearchGoodsShopUser;
    Unbinder unbinder;
    SSS_Adapter sss_adapter;
    @BindView(R.id.tip)
    TextView tip;
    String type;
    YWLoadingDialog ywLoadingDialog;
    Gson gson = new Gson();
    List<FragmentSearchGoodsShopUserModel> list = new ArrayList<>();

    public FragmentSearchGoodsShopUser() {
    }


    public FragmentSearchGoodsShopUser(String type) {
        this.type = type;
    }

    @Override
    public void onDestroy() {
        if (sss_adapter != null) {
            sss_adapter.clear();
        }
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_search_goods_shop_user;
    }

    @Override
    protected void lazyLoad() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(100);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initAdapter();
                            search_into();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void stopLoad() {

    }


    void initAdapter() {
        sss_adapter = new SSS_Adapter<FragmentSearchGoodsShopUserModel>(getBaseFragmentActivityContext(), R.layout.item_search) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final FragmentSearchGoodsShopUserModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_search, bean.keywords);
                helper.getView(R.id.text_search).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String type = null;
                        if ("goods".equals(FragmentSearchGoodsShopUser.this.type)) {
                            type = "1";
                        } else if ("shop".equals(FragmentSearchGoodsShopUser.this.type)) {
                            type = "2";
                        } else if ("account".equals(FragmentSearchGoodsShopUser.this.type)) {
                            type = "3";
                        }
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySearchGoodsShopUserListPublic.class)
                                .putExtra("type", type)
                                .putExtra("keywords", bean.keywords));
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        gridviewFragmentSearchGoodsShopUser.setAdapter(sss_adapter);
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
    }

    public void search_into() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.search_into(new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", type)
                            .toString(),
                    new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器访问错误");
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseFragmentActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        list.clear();
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), FragmentSearchGoodsShopUserModel.class));
                                            }
                                            tip.setVisibility(View.VISIBLE);
                                        }

                                        if (sss_adapter != null) {
                                            sss_adapter.setList(list);
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (getBaseFragmentActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }
}
