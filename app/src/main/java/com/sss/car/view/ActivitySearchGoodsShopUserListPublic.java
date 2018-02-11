package com.sss.car.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.pullToRefresh.PullToRefreshBase;
import com.blankj.utilcode.pullToRefresh.PullToRefreshListView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.sss.car.Config;
import com.sss.car.EventBusModel.SearchModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.SearchGoodsShopUserListPublic_Goods;
import com.sss.car.model.SearchGoodsShopUserListPublic_Shop;
import com.sss.car.model.SearchGoodsShopUserListPublic_User;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * Created by leilei on 2018/1/14.
 */

@SuppressWarnings("ALL")
public class ActivitySearchGoodsShopUserListPublic extends BaseActivity {
    @BindView(R.id.back_top_image)
    LinearLayout backTopImage;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.activity_search_goods_shop_user_list_public)
    LinearLayout activitySearchGoodsShopUserListPublic;
    YWLoadingDialog ywLoadingDialog;
    int p = 1;
    Gson gson = new Gson();

    List<SearchGoodsShopUserListPublic_Goods> goodsList = new ArrayList<>();
    SSS_Adapter goodsAdapter;


    List<SearchGoodsShopUserListPublic_Shop> shopList = new ArrayList<>();
    SSS_Adapter shopAdapter;

    List<SearchGoodsShopUserListPublic_User> userList = new ArrayList<>();
    SSS_Adapter userAdapter;
    @BindView(R.id.search)
    SimpleDraweeView search;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误！");
            return;
        }
        setContentView(R.layout.activity_search_goods_shop_user_list_public);
        ButterKnife.bind(this);
        customInit(activitySearchGoodsShopUserListPublic, false, true, false);
        input.setText(getIntent().getExtras().getString("keywords"));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(input.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "关键字为空");
                    return;
                }
                p = 1;
                all_search_into();
            }
        });
//        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (event.getAction()) {
//                    case KeyEvent.ACTION_UP:
//                        if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
//                            if (StringUtils.isEmpty(input.getText().toString().trim())) {
//                                LogUtils.e(actionId + "all_search_into();");
//                                ToastUtils.showShortToast(getBaseActivityContext(), "关键字为空");
//                                return true;
//                            }
//
//                        }
//                        p = 1;
//                        all_search_into();
//                        return true;
//                    default:
//                        return true;
//                }
//            }
//
//        });
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getRefreshableView().setEmptyView(LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.empty_view,null));
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (StringUtils.isEmpty(input.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "关键字为空");
                    return;
                }
                p = 1;
                all_search_into();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (StringUtils.isEmpty(input.getText().toString().trim())) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "关键字为空");
                    return;
                }
                all_search_into();
            }
        });
        initAdapter();
        if (input.getText().toString().trim()!=null&&!"".equals(input.getText().toString().trim())) {
            all_search_into();
        }
    }

    @OnClick(R.id.back_top_image)
    public void onViewClicked() {
        finish();
    }

    void initAdapter() {
        if ("1".equals(getIntent().getExtras().getString("type"))) {//1商品信息，2店铺信息，3账户信息
            goodsAdapter = new SSS_Adapter<SearchGoodsShopUserListPublic_Goods>(getBaseActivityContext(), R.layout.item_search_goods_shop_user_list_public_goods) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, final SearchGoodsShopUserListPublic_Goods bean, SSS_Adapter instance) {
                    helper.setText(R.id.title, bean.title);
                    helper.setText(R.id.slogan, bean.slogan);
                    helper.setText(R.id.price, "¥" + bean.price);
                    helper.setText(R.id.distance, bean.distance);
                    addImageViewList(FrescoUtils.showImage(false, 100, 100, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic)), 0f));
                    helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                        .putExtra("goods_id", bean.goods_id)
                                        .putExtra("type", bean.type));
                            }
                        }
                    });
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview.setAdapter(goodsAdapter);
        } else if ("2".equals(getIntent().getExtras().getString("type"))) {//1商品信息，2店铺信息，3账户信息
            shopAdapter = new SSS_Adapter<SearchGoodsShopUserListPublic_Shop>(getBaseActivityContext(), R.layout.item_search_goods_shop_user_list_public_shop) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, final SearchGoodsShopUserListPublic_Shop bean, SSS_Adapter instance) {
                    addImageViewList(FrescoUtils.showImage(false, 180, 180, Uri.parse(Config.url + bean.logo), ((SimpleDraweeView) helper.getView(R.id.pic)), 0f));
                    helper.setText(R.id.auth, bean.auth_type);
                    helper.setText(R.id.name, bean.name);
                    helper.setText(R.id.login_time, bean.last_time);
                    helper.setText(R.id.open_time, bean.business_hours);
                    helper.setText(R.id.score, "+" + bean.credit);
                    helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                        .putExtra("shop_id", bean.shop_id));
                            }
                        }
                    });
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview.setAdapter(shopAdapter);
        } else if ("3".equals(getIntent().getExtras().getString("type"))) {//1商品信息，2店铺信息，3账户信息
            userAdapter = new SSS_Adapter<SearchGoodsShopUserListPublic_User>(getBaseActivityContext(), R.layout.item_search_goods_shop_user_list_public_user) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, final SearchGoodsShopUserListPublic_User bean, SSS_Adapter instance) {
                    helper.setText(R.id.name, bean.username);
                    addImageViewList(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.face), ((SimpleDraweeView) helper.getView(R.id.pic)), 9999f));
                    helper.getView(R.id.click).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getBaseActivityContext() != null) {
                                startActivity(new Intent(getBaseActivityContext(), ActivityUserInfo.class)
                                        .putExtra("id", bean.member_id));

                            }
                        }
                    });
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {

                }
            };
            listview.setAdapter(userAdapter);
        }
    }

    public void all_search_into() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.all_search_into(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("p", p)
                            .put("keywords", input.getText().toString().trim())
                            .put("gps", Config.latitude + "," + Config.longitude)
                            .put("type", getIntent().getExtras().getString("type"))//1商品信息，2店铺信息，3账户信息
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.dismiss();
                            }
                            if (listview != null) {
                                listview.onRefreshComplete();
                            }

                            try {
                                final JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        if ("1".equals(getIntent().getExtras().getString("type"))) {
                                            if (p == 1) {
                                                goodsList.clear();
                                            }
                                            p++;

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                goodsList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), SearchGoodsShopUserListPublic_Goods.class));
                                            }
                                            goodsAdapter.setList(goodsList);
                                        } else if ("2".equals(getIntent().getExtras().getString("type"))) {
                                            if (p == 1) {
                                                shopList.clear();
                                            }
                                            p++;

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                shopList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), SearchGoodsShopUserListPublic_Shop.class));
                                            }
                                            shopAdapter.setList(shopList);
                                        } else if ("3".equals(getIntent().getExtras().getString("type"))) {
                                            if (p == 1) {
                                                userList.clear();
                                            }
                                            p++;

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                userList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), SearchGoodsShopUserListPublic_User.class));
                                            }

                                        }
                                    }
                                    userAdapter.setList(userList);
                                    EventBus.getDefault().post(new SearchModel(getIntent().getExtras().getString("type"), input.getText().toString().trim()));

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-2");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }
}
