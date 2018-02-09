package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewSale;
import com.sss.car.model.GoodsModel;
import com.sss.car.model.ListViewShowGoodsServiceModel;
import com.sss.car.view.ActivityGoodsServiceDetails;
import com.sss.car.view.ActivitySaleAndSeckillSaleFilter;
import com.sss.car.view.ActivityShopInfoAllFilter;

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
 * 淘秒杀==>即将抢购
 * Created by leilei on 2017/9/21.
 */

@SuppressLint("ValidFragment")
public class FragmentSaleAndSeckillSaleLater extends BaseFragment {
    @BindView(R.id.listViewSale_fragment_sale_and_seckill_sale_later)
    LinearLayout listViewFragmentSaleAndSeckillSaleLater;
    Unbinder unbinder;

    List<ListViewShowGoodsServiceModel> list = new ArrayList<>();

    List<ListViewSale> listViewSales = new ArrayList<>();

    YWLoadingDialog ywLoadingDialog;
    String classify_id;
    String type;


    String activity = "3";/*（热门榜单activity= 1）（进行中activity= 2）（即将开场activity= 3）*/


    public FragmentSaleAndSeckillSaleLater(String classify_id, String type) {
        this.classify_id = classify_id;
        this.type = type;
    }

    public FragmentSaleAndSeckillSaleLater() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_sale_and_seckill_sale_later;
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
                                getSecKill();
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
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        listViewFragmentSaleAndSeckillSaleLater = null;
        classify_id = null;
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
        unbinder = null;
    }


    /**
     * 淘秒杀-获取两条数据
     */
    public void getSecKill() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getSecKill(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("activity", activity)/*（热门榜单activity= 1）（进行中activity= 2）（即将开场activity= 3）*/
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
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ListViewShowGoodsServiceModel listViewShowGoodsServiceModel = new ListViewShowGoodsServiceModel();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                            listViewShowGoodsServiceModel.classify_id = jsonObject1.getString("classify_id");
                                            listViewShowGoodsServiceModel.name = jsonObject1.getString("name");

                                            JSONArray jsonArray1 = jsonObject1.getJSONArray("list");
                                            if (jsonArray1.length() > 0) {
                                                List<GoodsModel> goodsModelList = new ArrayList<>();
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    GoodsModel goodsModel = new GoodsModel();
                                                    goodsModel.goods_id = jsonArray1.getJSONObject(j).getString("goods_id");
                                                    goodsModel.title = jsonArray1.getJSONObject(j).getString("title");
                                                    goodsModel.slogan = jsonArray1.getJSONObject(j).getString("slogan");
                                                    goodsModel.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                    goodsModel.cost_price = jsonArray1.getJSONObject(j).getString("cost_price");
                                                    goodsModel.price = jsonArray1.getJSONObject(j).getString("price");
                                                    goodsModel.sell = jsonArray1.getJSONObject(j).getString("sell");
                                                    goodsModel.number = jsonArray1.getJSONObject(j).getString("number");
                                                    goodsModel.member_id = jsonArray1.getJSONObject(j).getString("member_id");
                                                    goodsModelList.add(goodsModel);
                                                }
                                                listViewShowGoodsServiceModel.list = goodsModelList;
                                            }
                                            list.add(listViewShowGoodsServiceModel);

                                            showGoodsList(list);
                                        }
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

    void showGoodsList(final List<ListViewShowGoodsServiceModel> list) {

        for (int i = 0; i < list.size(); i++) {
            final ListViewSale listViewSale = new ListViewSale(getBaseFragmentActivityContext());
            listViewSales.add(listViewSale);
            listViewSale.setTag(2);
            final int finalI = i;
            listViewSale.setListViewSaleOperationCallBack(new ListViewSale.ListViewSaleOperationCallBack() {
                @Override
                public void onClickMore_ListViewSaleOperationCallBack(String classify_id, String title, ListViewSale ListViewSale) {
                    activity_list(listViewSale, list.get(finalI).list);
                }

                @Override
                public void onClickTitle_ListViewSaleOperationCallBack(String classify_id, String title) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityShopInfoAllFilter.class)
                                .putExtra("classify_id", classify_id)
                                .putExtra("type", type)
                                .putExtra("mode", "secKill")
                                .putExtra("title", title));
                    }
                }
            });

            final int finalI1 = i;
            listViewSale.create(list.get(i).name, list.get(i).classify_id, new SSS_Adapter<GoodsModel>(getBaseFragmentActivityContext(), R.layout.item_sale_and_seckill_adapter) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, GoodsModel bean, SSS_Adapter instance) {
                    addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_sale_and_seckill_adapter)), 0f));
                    helper.setText(R.id.title_item_sale_and_seckill_adapter, bean.title);
                    helper.setText(R.id.slogan_item_sale_and_seckill_adapter, bean.slogan);
                    helper.setText(R.id.price_item_sale_and_seckill_adapter, "¥" + bean.price);
                    helper.setText(R.id.sell_item_sale_and_seckill_adapter, "已抢" + bean.sell + "件");
                    ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setMax(Integer.valueOf(bean.number));
                    ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setProgress(Integer.valueOf(bean.sell));
                    ((SeekBar) helper.getView(R.id.seekbar_item_sale_and_seckill_adapter)).setEnabled(false);
                    ((FrameLayout) helper.getView(R.id.progress_item_sale_and_seckill_adapter)).setVisibility(View.GONE);
                    ((TextView) helper.getView(R.id.get_item_sale_and_seckill_adapter)).setBackgroundColor(getResources().getColor(R.color.checkupdatelibrary_normal_text_color));
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            }, new SSS_OnItemListener() {
                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                }
            });

            listViewSale.setData(list.get(i).list);
            listViewFragmentSaleAndSeckillSaleLater.addView(listViewSale);

        }
    }

    /**
     * 淘秒杀-获取更多数据
     *
     * @param listViewSale
     * @param list
     */
    void activity_list(final ListViewSale listViewSale, final List<GoodsModel> list) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;

        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.activity_list(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("p", (Integer) listViewSale.getTag())
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("activity", activity)/*（热门榜单activity= 1）（进行中activity= 2）（即将开场activity= 3）*/
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
                                    LogUtils.e(list.size());
                                    if (jsonArray.length() > 0) {
                                        int a = (Integer) listViewSale.getTag();
                                        a++;
                                        listViewSale.setTag(a);
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
                                        listViewSale.setData(list);
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


}