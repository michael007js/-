package com.sss.car.dictionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.FragmentAdapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.customwidget.Tab.CustomTab.TAB;
import com.blankj.utilcode.customwidget.Tab.tab.ScrollTab;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.fragment.FragmentDictionaryBuyerSellerPublic;
import com.sss.car.model.DictionaryTitleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.sss.car.R.id.scrollTab;


/**
 * 买家卖家宝典共用页面
 * Created by leilei on 2017/11/3.
 */

public class DictionaryBuyerSellerPublic extends BaseActivity {
    public static final String TITLE_BUYER = "买家宝典";
    public static final String TITLE_SELLER = "卖家宝典";
    public String currentTitle = TITLE_BUYER;
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.parent_dictionary_buyer_seller_public)
    ViewPager viewpager;
    @BindView(R.id.dictionary_buyer_seller_public)
    LinearLayout dictionaryBuyerSellerPublic;
    @BindView(R.id.nav_dictionary_buyer_seller_public)
    ScrollTab ScrollTab;
    YWLoadingDialog ywLoadingDialog;
    List<DictionaryTitleModel> dictionaryTitlelist = new ArrayList<>();

    List<FragmentDictionaryBuyerSellerPublic> list = new ArrayList<>();
    FragmentAdapter fragmentAdapter;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (list!=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).onDestroy();
            }
        }
        list.clear();
        if (fragmentAdapter != null) {
            fragmentAdapter.clear();
        }
        fragmentAdapter = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        viewpager = null;
        ScrollTab = null;
        dictionaryBuyerSellerPublic = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_buyer_seller_public);
        ButterKnife.bind(this);
        customInit(dictionaryBuyerSellerPublic, false, true, false);
        currentTitle = getIntent().getExtras().getString("title");
        titleTop.setText(currentTitle);
        cate();
    }

    @OnClick(R.id.back_top)
    public void onViewClicked() {
        finish();
    }


    /**
     * 宝典分类
     */
    public void cate() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.cate(
                    new JSONObject()
                            .put("type", getIntent().getExtras().getString("type"))
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
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
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        DictionaryTitleModel dictionaryTitleModel = new DictionaryTitleModel();
                                        dictionaryTitleModel.cate_id = jsonArray.getJSONObject(i).getString("cate_id");
                                        dictionaryTitleModel.name = jsonArray.getJSONObject(i).getString("name");
                                        dictionaryTitleModel.sort = jsonArray.getJSONObject(i).getString("sort");
                                        dictionaryTitleModel.parent_id = jsonArray.getJSONObject(i).getString("parent_id");
                                        dictionaryTitlelist.add(dictionaryTitleModel);
                                    }
                                    init();


                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    void init() {
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String[] title = new String[dictionaryTitlelist.size()];
        for (int i = 0; i < dictionaryTitlelist.size(); i++) {
            title[i] = "  " + dictionaryTitlelist.get(i).name + "  ";
            FragmentDictionaryBuyerSellerPublic fragmentDictionaryBuyerSellerPublic = new FragmentDictionaryBuyerSellerPublic(dictionaryTitlelist.get(i).cate_id);
            list.add(fragmentDictionaryBuyerSellerPublic);
            fragmentAdapter.addFragment(fragmentDictionaryBuyerSellerPublic);
        }

        ScrollTab.setTitles(Arrays.asList(title));
        ScrollTab.setViewPager(viewpager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewpager.setCurrentItem(position);
            }
        });

        viewpager.setAdapter(fragmentAdapter);
        viewpager.setOffscreenPageLimit(dictionaryTitlelist.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
