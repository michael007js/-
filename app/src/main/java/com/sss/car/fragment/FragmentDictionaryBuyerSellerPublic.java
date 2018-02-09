package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dictionary.DectionaryDetails;
import com.sss.car.model.ArticleModel;
import com.sss.car.model.DictionaryTitleModel;

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
 * 买家卖家宝典共用fragment
 * Created by leilei on 2017/11/3.
 */
@SuppressLint("ValidFragment")
public class FragmentDictionaryBuyerSellerPublic extends BaseFragment {


    @BindView(R.id.listview_fragment_dictionary_buyer_seller_public)
    ListView listviewFragmentDictionaryBuyerSellerPublic;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    List<ArticleModel> list = new ArrayList<>();
    SSS_Adapter sss_Adapter;
    String title;
    String cate_id;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sss_Adapter != null) {
            sss_Adapter.clear();
        }
        sss_Adapter = null;
        title = null;
        cate_id = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        listviewFragmentDictionaryBuyerSellerPublic = null;
    }

    public FragmentDictionaryBuyerSellerPublic() {
    }


    public FragmentDictionaryBuyerSellerPublic(String cate_id) {
        this.cate_id = cate_id;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_dictionary_buyer_seller_public;
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
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
                                request();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    @Override
    protected void stopLoad() {

    }


    public void initAdapter() {
        sss_Adapter = new SSS_Adapter<ArticleModel>(getBaseFragmentActivityContext(), R.layout.item_dectionary, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, ArticleModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_dectionary, bean.title);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_dectionary);
            }
        };
        sss_Adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_dectionary:
                        startActivity(new Intent(getBaseFragmentActivityContext(), DectionaryDetails.class)
                                .putExtra("article_id", list.get(position).article_id)
                                .putExtra("title", list.get(position).cate_name)
                        );
                        break;
                }
            }
        });

        listviewFragmentDictionaryBuyerSellerPublic.setAdapter(sss_Adapter);


    }


    public void request() {
        list.clear();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.catList(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ArticleModel articleModel = new ArticleModel();
                                            articleModel.article_id = jsonArray.getJSONObject(i).getString("article_id");
                                            articleModel.title = jsonArray.getJSONObject(i).getString("title");
                                            articleModel.cate_name = jsonArray.getJSONObject(i).getString("cate_name");
                                            list.add(articleModel);
                                        }
                                        sss_Adapter.setList(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
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
