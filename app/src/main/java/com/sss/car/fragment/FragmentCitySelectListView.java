package com.sss.car.fragment;

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
import com.blankj.utilcode.customwidget.Slidebar.WaveSideBar;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.SelectCtityModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.CitySelect;
import com.sss.car.model.CitySelectModel;
import com.sss.car.model.CitySelectModel_All_subclass;
import com.sss.car.model.CitySelectModel_Browse_Top;
import com.sss.car.utils.CarUtils;

import org.greenrobot.eventbus.EventBus;
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

@SuppressWarnings("ALL")
public class FragmentCitySelectListView extends BaseFragment implements CitySelect.CitySelectCallBack {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.side_bar)
    WaveSideBar sideBar;
    CitySelect citySelect;
    Unbinder unbinder;
    YWLoadingDialog ywLoadingDialog;
    CitySelectModel citySelectModel;
    SSS_Adapter sss_adapter;
    List<CitySelectModel_All_subclass> cityData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_city_select_listview;
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
                                citySelect = new CitySelect(getBaseFragmentActivityContext());
                                listview.addHeaderView(citySelect);
                                initAdapter();
                                get_city();
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

    void initAdapter() {
        sss_adapter = new SSS_Adapter<CitySelectModel_All_subclass>(getBaseFragmentActivityContext(), R.layout.item_city_search_adapter, cityData) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CitySelectModel_All_subclass bean, SSS_Adapter instance) {
                if (bean.id != null) {
                    helper.setText(R.id.text_item_city_search_adapter, bean.name);
                    helper.setBackgroundColor(R.id.text_item_city_search_adapter, getResources().getColor(R.color.white));
                } else {
                    helper.setText(R.id.text_item_city_search_adapter, bean.spell);
                    helper.setBackgroundColor(R.id.text_item_city_search_adapter, getResources().getColor(R.color.line));
                }

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.text_item_city_search_adapter);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.text_item_city_search_adapter:
                        if (cityData.get(position).id != null) {
                            insert_looks(cityData.get(position).id, cityData.get(position).name,cityData.get(position).lat,cityData.get(position).lng);
                        }
                        break;
                }
            }
        });
        listview.setAdapter(sss_adapter);
    }

    public void get_city() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_city(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    citySelectModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), CitySelectModel.class);
                                    citySelect.setData(Config.city, getBaseFragmentActivityContext(), citySelectModel, FragmentCitySelectListView.this);
                                    initData();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    void initData() {
        for (int i = 0; i < citySelectModel.all.size(); i++) {

            if (citySelectModel.all.get(i).subclass.size() > 0) {
                CitySelectModel_All_subclass citySelectModel_all_subclass = new CitySelectModel_All_subclass();
                citySelectModel_all_subclass.spell = citySelectModel.all.get(i).spell;
                LogUtils.e(citySelectModel_all_subclass.spell);
                cityData.add(citySelectModel_all_subclass);
            }
            for (int j = 0; j < citySelectModel.all.get(i).subclass.size(); j++) {
                cityData.add(citySelectModel.all.get(i).subclass.get(j));
            }
        }
        sss_adapter.setList(cityData);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < cityData.size(); i++) {
                    if (cityData.get(i).id != null) {
                        if (cityData.get(i).spell.equals(index)) {
                            listview.setSelection(i);
                            return;
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onCurrent() {
        getActivity().finish();

    }

    @Override
    public void onHistroy(final CitySelectModel_Browse_Top model) {

        CarUtils.select(getBaseFragmentActivityContext(), model.name, model.lat, model.lng, new CarUtils.OnCitySelectCallBack() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new SelectCtityModel(model.name));
                getActivity().finish();
            }

            @Override
            public void onFail(String msg) {
                ToastUtils.showShortToast(getBaseFragmentActivityContext(), msg);
            }
        });

    }

    @Override
    public void onTop(CitySelectModel_Browse_Top model) {
        insert_looks(model.id, model.name, model.lat, model.lng);
    }


    public void insert_looks(String looks_id, final String name, final double lat, final double lng) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_looks(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("looks_id", looks_id)
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
                                    Config.city = name;
                                    Config.latitude= String.valueOf(lat);
                                    Config.longitude= String.valueOf(lng);
                                    EventBus.getDefault().post(new SelectCtityModel(name));
                                    getActivity().finish();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


}
