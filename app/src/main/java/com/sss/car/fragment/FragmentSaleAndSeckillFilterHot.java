package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutRefresh.RefreshLoadMoreLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.GoodsModel;
import com.sss.car.view.ActivityGoodsServiceDetails;

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
 * 淘秒杀==>筛选==>热门正在抢购中
 * Created by leilei on 2017/9/22.
 */

@SuppressLint("ValidFragment")
public class FragmentSaleAndSeckillFilterHot extends BaseFragment implements RefreshLoadMoreLayout.CallBack{
    @BindView(R.id.listview_linearlayout_fragment_sale_and_seckill_filter_hot)
    InnerListview listviewFragmentSaleAndSeckillFilterHot;
    @BindView(R.id.scoll_view_fragment_sale_and_seckill_filter_hot)
    ScrollView scollViewFragmentSaleAndSeckillFilterHot;
    @BindView(R.id.refresh_fragment_sale_and_seckill_filter_hot)
    RefreshLoadMoreLayout refreshFragmentSaleAndSeckillFilterHot;
    @BindView(R.id.top_fragment_sale_and_seckill_filter_hot)
    ImageView topFragmentSaleAndSeckillFilterHot;
    Unbinder unbinder;
SSS_Adapter sss_adapter;

    public FragmentSaleAndSeckillFilterHot() {
    }

    String type;


    String classify_id;
    YWLoadingDialog ywLoadingDialog;

    int p=1;

     List<GoodsModel> list=new ArrayList<>();

    public FragmentSaleAndSeckillFilterHot(String classify_id,String type) {
        this.classify_id = classify_id;
        this.type=type;
    }

    @Override
    public void onDestroy() {
        if (list!=null){
            list.clear();
        }
        list=null;
        if (sss_adapter!=null){
            sss_adapter.clear();
        }
        sss_adapter=null;
        listviewFragmentSaleAndSeckillFilterHot = null;
        scollViewFragmentSaleAndSeckillFilterHot = null;
        refreshFragmentSaleAndSeckillFilterHot = null;
        topFragmentSaleAndSeckillFilterHot = null;
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_sale_and_seckill_filter_hot;
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
                                refreshFragmentSaleAndSeckillFilterHot.init(new RefreshLoadMoreLayout.Config(FragmentSaleAndSeckillFilterHot.this).canRefresh(true).canLoadMore(true));
                                initAdapter();
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    scollViewFragmentSaleAndSeckillFilterHot.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                        @Override
                                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                            if (scrollY > Config.scoll_HighRestriction) {
                                                topFragmentSaleAndSeckillFilterHot.setVisibility(View.VISIBLE);
                                            } else {
                                                topFragmentSaleAndSeckillFilterHot.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    topFragmentSaleAndSeckillFilterHot.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            scollViewFragmentSaleAndSeckillFilterHot.smoothScrollTo(0, 0);
                                        }
                                    });
                                }

                                activity_list(true);
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



    void initAdapter(){
        sss_adapter=new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_sale_and_seckill_adapter) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean,SSS_Adapter instance) {
                addImageViewList(FrescoUtils.showImage(false, 50, 50, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_sale_and_seckill_adapter)), 0f));
                helper.setText(R.id.title_item_sale_and_seckill_adapter, bean.title);
                helper.setText(R.id.slogan_item_sale_and_seckill_adapter, bean.slogan);
                helper.setText(R.id.price_item_sale_and_seckill_adapter, "¥" + bean.price);
                helper.setText(R.id.sell_item_sale_and_seckill_adapter,"已抢"+bean.sell+"件");
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setMax(Integer.valueOf(bean.number));
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setProgress(Integer.valueOf(bean.sell));
                ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setEnabled(false);
                ((LinearLayout)helper.getView(R.id.click_item_sale_and_seckill_adapter)).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,220));
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.get_item_sale_and_seckill_adapter);

            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {
            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                switch (view.getId()) {
                    case R.id.get_item_sale_and_seckill_adapter:
                        if (getBaseFragmentActivityContext() != null) {
                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceDetails.class)
                                    .putExtra("type",type)
                                    .putExtra("goods_id",list.get(position).goods_id )
                            );
                        }
                        break;
                }
            }
        });
        listviewFragmentSaleAndSeckillFilterHot.setAdapter(sss_adapter);


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


    /**
     *
     */
    void activity_list(boolean isShowDialog) {

        if (isShowDialog){
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ywLoadingDialog = null;

            if (getBaseFragmentActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
                ywLoadingDialog.show();
            }
        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.activity_list(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p",p)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("activity", "1")/*（热门榜单activity= 1）（进行中activity= 2）（即将开场activity= 3）*/
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (refreshFragmentSaleAndSeckillFilterHot!=null){
                                refreshFragmentSaleAndSeckillFilterHot.stopLoadMore();
                                refreshFragmentSaleAndSeckillFilterHot.stopRefresh();
                            }
                            ToastUtils.showShortToast(getBaseFragmentActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (refreshFragmentSaleAndSeckillFilterHot!=null){
                                refreshFragmentSaleAndSeckillFilterHot.stopLoadMore();
                                refreshFragmentSaleAndSeckillFilterHot.stopRefresh();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    LogUtils.e(list.size());
                                    if (jsonArray.length() > 0) {
                                        if (p==1){
                                            list.clear();
                                        }
                                        p++;
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            GoodsModel goodsModel = new GoodsModel();
                                            goodsModel.goods_id = jsonArray.getJSONObject(j).getString("goods_id");
                                            goodsModel.title = jsonArray.getJSONObject(j).getString("title");
                                            goodsModel.slogan = jsonArray.getJSONObject(j).getString("slogan");
                                            goodsModel.master_map = jsonArray.getJSONObject(j).getString("master_map");
                                            goodsModel.cost_price = jsonArray.getJSONObject(j).getString("cost_price");
                                            goodsModel.price = jsonArray.getJSONObject(j).getString("price");
                                            goodsModel.sell = jsonArray.getJSONObject(j).getString("sell");
                                            goodsModel.number = jsonArray.getJSONObject(j).getString("number");
                                            goodsModel.member_id = jsonArray.getJSONObject(j).getString("member_id");
                                            list.add(goodsModel);
                                        }

                                        sss_adapter.setList(list);
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:SecKill-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:SecKill-0");
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        p=1;
        activity_list(false);
    }

    @Override
    public void onLoadMore() {
        activity_list(false);
    }
}
