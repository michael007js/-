package com.sss.car.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.ActivityOtherModel;
import com.sss.car.view.ActivityWeb;

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
 * Created by leilei on 2017/9/23.
 */

public class FragmentOther extends BaseFragment implements RefreshLoadMoreLayout.CallBack {
    @BindView(R.id.listview_fragment_other)
    InnerListview listviewFragmentOther;
    @BindView(R.id.scollview_listview_fragment_other)
    ScrollView scollviewListviewFragmentOther;
    @BindView(R.id.top_listview_fragment_other)
    ImageView topListviewFragmentOther;
    Unbinder unbinder;
    @BindView(R.id.refresh_listview_fragment_other)
    RefreshLoadMoreLayout refreshListviewFragmentOther;

    int p = 1;
    List<ActivityOtherModel> list = new ArrayList<>();

    SSS_Adapter sss_adapter;

    public FragmentOther() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_other;
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
                                init();
                                subject();
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
    public void onDestroy() {
        listviewFragmentOther = null;
        scollviewListviewFragmentOther = null;
        topListviewFragmentOther = null;
        super.onDestroy();
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


    void init() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            refreshListviewFragmentOther.init(new RefreshLoadMoreLayout.Config(this).canLoadMore(true).canRefresh(true));
            scollviewListviewFragmentOther.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > Config.scoll_HighRestriction) {
                        topListviewFragmentOther.setVisibility(View.VISIBLE);
                    } else {
                        topListviewFragmentOther.setVisibility(View.GONE);
                    }
                }
            });
            topListviewFragmentOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scollviewListviewFragmentOther.smoothScrollTo(0, 0);
                }
            });
        }


        sss_adapter = new SSS_Adapter<ActivityOtherModel>(getBaseFragmentActivityContext(), R.layout.item_activity_adapter) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, ActivityOtherModel bean,SSS_Adapter instance) {
                FrescoUtils.showImage(false, 240, 160, Uri.parse(Config.url + bean.picture), ((SimpleDraweeView) helper.getView(R.id.pic_item_activity_adapter)), 0f);
                helper.setText(R.id.title_item_activity_adapter, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.click_item_activity_adapter);
                helper.setItemChildClickListener(R.id.get_item_activity_adapter);
            }
        };

        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.click_item_activity_adapter:
                        if (getBaseFragmentActivityContext() != null) {
                            getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivityWeb.class)
                                    .putExtra("type", ActivityWeb.ACTIVITY)
                                    .putExtra("id", list.get(position).subject_id));
                        }
                        break;
                    case R.id.get_item_activity_adapter:
                        break;
                }
            }
        });

        listviewFragmentOther.setAdapter(sss_adapter);

    }


    /**
     * 获取加入优惠券店铺
     */
    public void subject() {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.subject(
                    new JSONObject()
                            .put("p", p)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (refreshListviewFragmentOther != null) {
                                refreshListviewFragmentOther.stopRefresh();
                                refreshListviewFragmentOther.stopLoadMore();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshListviewFragmentOther != null) {
                                refreshListviewFragmentOther.stopRefresh();
                                refreshListviewFragmentOther.stopLoadMore();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if (p == 1) {
                                            list.clear();
                                        }
                                        p++;
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                ActivityOtherModel activityOther = new ActivityOtherModel();
                                                activityOther.name = jsonArray.getJSONObject(j).getString("name");
                                                activityOther.picture = jsonArray.getJSONObject(j).getString("picture");
                                                activityOther.subject_id = jsonArray.getJSONObject(j).getString("subject_id");
                                                list.add(activityOther);
                                            }
                                            sss_adapter.setList(list);

                                    }


                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:other-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:other-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        subject();
    }

    @Override
    public void onLoadMore() {
        subject();
    }
}
