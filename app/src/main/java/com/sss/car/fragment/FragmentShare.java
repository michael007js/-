package com.sss.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutNavMenu.NavMenuLayout;
import com.blankj.utilcode.customwidget.ScollView.InCludeLandscapeScrollView;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.customwidget.ViewPager.AutofitViewPager;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dictionary.DictionaryMy;
import com.sss.car.model.TopTabModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.view.ActivityLocationCitySelect;
import com.sss.car.view.ActivityPC;
import com.sss.car.view.ActivitySearchGoodsShopUser;

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
 * 分享fragment
 * Created by leilei on 2017/8/24.
 */

public class FragmentShare extends BaseFragment {
    Unbinder unbinder;
//    /*综合*/
//    public FragmentShareSynthesize fragmentShareSynthesize;

    /*动态*/
    public FragmentShareDynamic fragmentShareDynamic;

    /*社区帖子*/
    public FragmentShareCommunity fragmentShareCommunity;
    @BindView(R.id.click_book_fragment_share)
    TextView clickBookFragmentShare;
    @BindView(R.id.click_search)
    LinearLayout clickSearch;
    @BindView(R.id.top_tab_fragment_message)
    ScrollTab topTabFragmentMessage;
    @BindView(R.id.click_menu)
    ImageView clickMenu;
    @BindView(R.id.CustomCacheViewPager)
    AutofitViewPager CustomCacheViewPager;
    @BindView(R.id.scoll_fragment_share)
    InCludeLandscapeScrollView scollFragmentShare;
    List<TopTabModel> goodsClassifyModelList = new ArrayList<>();
    String[] title = new String[3];
    FragmentAdapter fragmentAdapter;
    MenuDialog menuDialog;
    YWLoadingDialog ywLoadingDialog;

    @Override
    protected int setContentView() {
        return R.layout.fragment_share;
    }

    public FragmentShare() {
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
                                classify();


                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    public void classify() {
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
                            .put("type", "4")//分类(1车品2车服3消息4分享)
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
                                        init();
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

    void init() {

        if (goodsClassifyModelList.size() == 2) {
            for (int i = 0; i < goodsClassifyModelList.size(); i++) {
                title[i] = "           " + goodsClassifyModelList.get(i).name + "           ";
            }
            addImageViewList(GlidUtils.glideLoad(false, clickMenu, getBaseFragmentActivityContext(), R.mipmap.logo_ten));
//            fragmentShareSynthesize=new FragmentShareSynthesize(goodsClassifyModelList.get(0).id);
            fragmentShareDynamic = new FragmentShareDynamic(goodsClassifyModelList.get(0).id);
            fragmentShareCommunity = new FragmentShareCommunity(goodsClassifyModelList.get(1).id);
            fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
//            fragmentAdapter.addFragment(fragmentShareSynthesize);
            fragmentAdapter.addFragment(fragmentShareDynamic);
            fragmentAdapter.addFragment(fragmentShareCommunity);

            topTabFragmentMessage.setTitles(Arrays.asList(title));
            topTabFragmentMessage.setViewPager(CustomCacheViewPager);
            topTabFragmentMessage.setOnTabListener(new ScrollTab.OnTabListener() {
                @Override
                public void onChange(int position, View v) {
                    CustomCacheViewPager.setCurrentItem(position);
                }
            });
            CustomCacheViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    topTabFragmentMessage.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            CustomCacheViewPager.setOffscreenPageLimit(2);
            CustomCacheViewPager.setAdapter(fragmentAdapter);
        } else {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "服务器返回数据不符!");
        }

    }

    @Override
    protected void stopLoad() {
        clearRequestCall();
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


    @OnClick({R.id.click_book_fragment_share, R.id.click_search, R.id.click_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_book_fragment_share:
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), DictionaryMy.class));
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
                menuDialog.createShare(clickMenu, getActivity());
                break;
        }
    }
}
