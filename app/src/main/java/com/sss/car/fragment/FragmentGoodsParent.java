package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ViewPager.AutofitViewPager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dao.OnExistsShopCallBack;
import com.sss.car.dao.OnRefreshCallBack;
import com.sss.car.model.TopTabModel;
import com.sss.car.order.OrderSOSPublish;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityGoodsServiceEdit;
import com.sss.car.view.ActivityLocationCitySelect;
import com.sss.car.view.ActivityMyDataShop;
import com.sss.car.view.ActivitySearchGoodsShopUser;
import com.sss.car.view.ActivityUnReadMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 车品车服改版后的公用页面
 * Created by leilei on 2017/11/29.
 */

@SuppressWarnings("ALL")
public class FragmentGoodsParent extends BaseFragment {
    @BindView(R.id.click_location_fragment_goods_parent)
    public TextView clickLocationFragmentGoodsParent;
    @BindView(R.id.search_fragment_goods_parent)
    ImageView searchFragmentGoodsParent;
    @BindView(R.id.click_search)
    LinearLayout clickSearch;
    @BindView(R.id.top_tab_fragment_goods)
    ScrollTab topTabFragmentGoods;
    @BindView(R.id.click_menu)
    ImageView clickMenu;
    Unbinder unbinder;


    YWLoadingDialog ywLoadingDialog;


    /*一级父类菜单集合与适配器*/
    List<TopTabModel> goodsClassifyModelList = new ArrayList<>();
    /*一级父类菜单选中项(顶部)*/
    int currentFirstSeclect = 0;


    int currentSelectFragment = 0;


    MenuDialog menuDialog;

    String type = "1";//1车品2车服

    @BindView(R.id.click_SOS_fragment_goods_parent)
    TextView clickSOSFragmentGoodsParent;
    @BindView(R.id.logo_location)
    ImageView logoLocation;
    @BindView(R.id.CustomCacheViewPager)
    AutofitViewPager innerViewPager;


    FragmentAdapter fragmentAdapter;
    @BindView(R.id.unread)
    TextView unread;
    @BindView(R.id.top_message)
    RelativeLayout topMessage;
    boolean loadOver;

    public FragmentGoodsParent() {
    }

    public FragmentGoodsParent(String type) {
        this.type = type;
    }

    public void setMessageNumber(int count) {
        if (count > 0) {
            if (unread != null) {
                unread.setVisibility(View.VISIBLE);
                unread.setText(Integer.toString(count));
            }
        } else {
            if (unread != null) {
                unread.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_service;
    }

    @Override
    protected void lazyLoad() {
//        if (!isLoad) {
        init();
//        }
    }

    public void init(){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(100);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("2".equals(type)) {
                                topMessage.setVisibility(View.GONE);
                            } else {
                                topMessage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (getBaseFragmentActivityContext() != null) {
                                            startActivity(new Intent(getBaseFragmentActivityContext(), ActivityUnReadMessage.class));
                                        }
                                    }
                                });
                            }
                            if (fragmentAdapter!=null){
                                for (int i = 0; i < fragmentAdapter.getmFragments().size(); i++) {
                                    ((FragmentGoodsServiceChild)fragmentAdapter.getmFragments().get(i)).onDestroy();
                                }
                                fragmentAdapter.clearFragment();
                            }
                            initLocation();
                            initView();
                            goods_classify();
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

    @OnClick({R.id.click_location_fragment_goods_parent, R.id.click_search, R.id.click_menu, R.id.click_SOS_fragment_goods_parent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_location_fragment_goods_parent:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityLocationCitySelect.class));
                }
                break;
            case R.id.click_search:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySearchGoodsShopUser.class));
                }
                break;
            case R.id.click_menu:
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getActivity());
                }
                menuDialog.createMainRightMenu(clickMenu, getActivity(), new OnExistsShopCallBack() {
                    @Override
                    public void onExists() {
                        exists_shop();
                    }
                });
                break;

            case R.id.click_SOS_fragment_goods_parent:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), OrderSOSPublish.class));
                }
                break;

        }
    }

    /**
     * 用户是否已经开店
     */
    public void exists_shop() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.exists_shop(
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        startActivity(new Intent(getBaseFragmentActivityContext(), ActivityGoodsServiceEdit.class));
                                    } else {
                                        final Dialog dialog = new Dialog(getBaseFragmentActivityContext(), com.blankj.utilcode.R.style.RcDialog);
                                        View view = LayoutInflater.from(getBaseFragmentActivityContext()).inflate(com.blankj.utilcode.R.layout.dialog_ask, null);
                                        TextView content_dialog_ask = $.f(view, com.blankj.utilcode.R.id.content_dialog_ask);
                                        TextView cancel_dialog_ask = $.f(view, com.blankj.utilcode.R.id.cancel_dialog_ask);
                                        TextView yes_dialog_ask = $.f(view, com.blankj.utilcode.R.id.yes_dialog_ask);
                                        content_dialog_ask.setText("你的店铺资料尚未完善");
                                        content_dialog_ask.setTextColor(getBaseFragmentActivityContext().getResources().getColor(com.blankj.utilcode.R.color.textColor));
                                        cancel_dialog_ask.setText("取消");
                                        cancel_dialog_ask.setTextColor(getResources().getColor(R.color.mainColor));
                                        yes_dialog_ask.setText("去完善");
                                        yes_dialog_ask.setTextColor(getResources().getColor(R.color.mainColor));
                                        cancel_dialog_ask.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();

                                            }
                                        });
                                        yes_dialog_ask.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                if (getBaseFragmentActivityContext() != null) {
                                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityMyDataShop.class));
                                                }
                                            }
                                        });
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.setContentView(view);
                                        dialog.show();
                                    }

                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

    private void initLocation() {
        clickLocationFragmentGoodsParent.setText(Config.city);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取车品分类(顶部)
     */
    public void goods_classify() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseFragmentActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.goods_classify(
                    new JSONObject()
                            .put("type", type)//分类(1车品2车服3消息4分享)
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

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        goodsClassifyModelList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            TopTabModel goodsClassifyModel = new TopTabModel();
                                            goodsClassifyModel.id = jsonArray.getJSONObject(i).getString("classify_id");
                                            goodsClassifyModel.name = jsonArray.getJSONObject(i).getString("name");
                                            goodsClassifyModelList.add(goodsClassifyModel);
                                        }
                                        setData();
                                    }
                                    if (ywLoadingDialog != null) {
                                        ywLoadingDialog.disMiss();
                                    }
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:goods_classify-0");
            e.printStackTrace();
        }
    }

    private void add(String[] title, int i) {
        fragmentAdapter.addFragment(new FragmentGoodsServiceChild(goodsClassifyModelList, i, goodsClassifyModelList.get(i).id, type).setOnRefreshCallBack(new OnRefreshCallBack() {
            @Override
            public void onRefresh(FragmentGoodsServiceChild fragmentGoodsServiceChild) {
//                goods_classify();
            }
        }));
        title[i] = goodsClassifyModelList.get(i).name;
        fragmentAdapter.notifyDataSetChanged();
    }

    List<FragmentGoodsServiceChild> fragmentGoodsServiceChildList = new ArrayList<>();

    public void setData() {
        topTabFragmentGoods.isLoad = false;
        setClassifyID(type);
        String[] title = new String[goodsClassifyModelList.size()];
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), title);
        for (int i = 0; i < goodsClassifyModelList.size(); i++) {
            add(title, i);
        }

        LogUtils.e("this add" + fragmentAdapter.getmFragments().size());
        innerViewPager.setOffscreenPageLimit(goodsClassifyModelList.size());
        innerViewPager.setAdapter(fragmentAdapter);
        innerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentSelectFragment = position;
                topTabFragmentGoods.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        topTabFragmentGoods.setTitles(Arrays.asList(title));
        topTabFragmentGoods.setViewPager(innerViewPager);
        topTabFragmentGoods.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                innerViewPager.setCurrentItem(position);
                setClassifyID(type);
                if (currentFirstSeclect != position) {
                    currentFirstSeclect = position;
                }
            }
        });
        loadOver = true;
        topTabFragmentGoods.onPageSelected(0);
/******************************************************************************************/

    }

    /**
     * 初始化视图
     */
    @SuppressLint("InflateParams")
    void initView() {
        if ("1".equals(type)) {
            clickSOSFragmentGoodsParent.setVisibility(View.GONE);
            logoLocation.setVisibility(View.VISIBLE);
            clickLocationFragmentGoodsParent.setVisibility(View.VISIBLE);
        } else {
            clickSOSFragmentGoodsParent.setVisibility(View.VISIBLE);
            logoLocation.setVisibility(View.GONE);
            clickLocationFragmentGoodsParent.setVisibility(View.GONE);
        }


        addImageViewList(GlidUtils.glideLoad(false, clickMenu, getBaseFragmentActivityContext(), R.mipmap.logo_ten));
//        scollLinearlayoutFragmentGoods.addView(view);
//        refreshFragmentGoods.init(new RefreshLoadMoreLayout.Config(this).canRefresh(true).canLoadMore(false).includeScrollHorizontal(true));
    }


    public void setClassifyID(String type) {
        LogUtils.e("000000" + type);
        //当type=2时车服店铺详情页全部宝贝点击筛选之后的页面必须,车品不需要
//        if ("2".equals(type)) {
        LogUtils.e("111111");
        if (goodsClassifyModelList != null) {
            LogUtils.e("22222222" + type);
            if (goodsClassifyModelList.size() > 0) {
                LogUtils.e("3333333333" + type);
                Config.classify_id = goodsClassifyModelList.get(currentFirstSeclect).id;
            }
        }
//        } else {
//            LogUtils.e("555555555" + type);
//            Config.classify_id = null;
//        }
    }


}
