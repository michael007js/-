package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GalleryHorizontalListView.GalleryHorizontalListView;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.xrichtext.RichTextEditor;
import com.blankj.utilcode.xrichtext.util.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedGoodsList;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dictionary.DictionaryBuyerSellerPublic;
import com.sss.car.model.CassifyData;
import com.sss.car.model.GoodsServiceEditModel;
import com.sss.car.model.SpecificationPriceModel;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;


/**
 * Created by leilei on 2018/1/4.
 */

@SuppressWarnings("ALL")
public class ActivityGoodsServiceEdit extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.et_main)
    EditText etMain;
    @BindView(R.id.click_main)
    TextView clickMain;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.click_one)
    TextView clickOne;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.click_two)
    TextView clickTwo;
    @BindView(R.id.et_three)
    EditText etThree;
    @BindView(R.id.click_three)
    TextView clickThree;
    @BindView(R.id.et_brand)
    EditText etBrand;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_recommend)
    EditText etRecommend;
    @BindView(R.id.et_car)
    EditText etCar;
    @BindView(R.id.et_price_type)
    EditText etPriceType;
    @BindView(R.id.click_price_type)
    TextView clickPrice;
    @BindView(R.id.click_add)
    SimpleDraweeView clickAdd;
    @BindView(R.id.et_specification)
    EditText etSpecification;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.click_subtract)
    SimpleDraweeView clickSubtract;
    @BindView(R.id.et_inventory)
    EditText etInventory;
    @BindView(R.id.et_time)
    EditText etTime;
    @BindView(R.id.click_time)
    TextView clickTime;
    @BindView(R.id.et_service)
    EditText etService;
    @BindView(R.id.activity_goods_service_edit)
    LinearLayout activityGoodsServiceEdit;
    @BindView(R.id.specificationAndPriceListView)
    InnerListview specificationAndPriceListView;
    @BindView(R.id.GalleryHorizontalListView)
    com.blankj.utilcode.customwidget.GalleryHorizontalListView.GalleryHorizontalListView GalleryHorizontalListView;
    @BindView(R.id.RichTextEditor)
    com.blankj.utilcode.xrichtext.RichTextEditor RichTextEditor;
    YWLoadingDialog ywLoadingDialog;
    Gson gson = new Gson();
    MenuDialog menuDialog;
    BottomSheetDialog bottomSheetDialog;

    String main;/*1车品2车服*/
    List<String> mainList = new ArrayList<>();
    SSS_Adapter mainAdapter;

    String one;
    List<GoodsServiceEditModel> oneList = new ArrayList<>();
    SSS_Adapter oneAdapter;

    String two;
    List<GoodsServiceEditModel> twoList = new ArrayList<>();
    SSS_Adapter twoAdapter;

    String three;
    List<GoodsServiceEditModel> threeList = new ArrayList<>();
    SSS_Adapter threeAdapter;

    String priceType;/*1一口价，2区间价格，3面议价格*/
    List<String> priceTypeList = new ArrayList<>();
    SSS_Adapter priceTypeAdapter;


    SSS_Adapter specificationAdapter;
    List<SpecificationPriceModel> specificationList = new ArrayList<>();

    List<EditText> edittextList = new ArrayList<>();

    List<CassifyData> cassifyDataList = new ArrayList<>();
    @BindView(R.id.insert)
    TextView insert;
    @BindView(R.id.click_check)
    TextView clickCheck;
    @BindView(R.id.click_goods_view)
    TextView clickGoodsView;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.click_submit)
    TextView clickSubmit;
    List<String> temp = new ArrayList<>();
    String goods_id;
    @BindView(R.id.price_set_parent)
    LinearLayout priceSetParent;

    @Override

    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_service_edit);
        ButterKnife.bind(this);
        titleTop.setText("编辑商品信息");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setText("保存");
        customInit(activityGoodsServiceEdit, false, true, true);
        initAdapter();
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                goods_id = getIntent().getExtras().getString("goods_id");
            }
        }
        temp.add(GalleryHorizontalListView.Holder);
        GalleryHorizontalListView.setList(temp);
        GalleryHorizontalListView.setHigth(getBaseActivityContext(), 70);
        GalleryHorizontalListView.setWidth(getBaseActivityContext(), 70);
        GalleryHorizontalListView.setOnGalleryHorizontalListViewCallBack(new GalleryHorizontalListView.OnGalleryHorizontalListViewCallBack() {
            @Override
            public void onClickImage(SimpleDraweeView simpleDraweeView, int position, List<String> list) {
                startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                        .putStringArrayListExtra("data", (ArrayList<String>) list)
                        .putExtra("current", position));
            }

            @Override
            public void onClose(int position, List<String> list) {

            }
        });
        RichTextEditor.init(getBaseActivityContext(), null);
        tip();
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if ("edit".equals(getIntent().getExtras().getString("mode"))) {
                    update_goods();
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel event) {
        String[] a = event.msg.split("—");
        if (a.length == 2) {
            etTime.setText(a[0] + "—" + a[1]);
        }
    }

    @OnClick({R.id.click_check, R.id.click_goods_view, R.id.click_submit, R.id.insert, R.id.back_top, R.id.right_button_top, R.id.click_main, R.id.click_one, R.id.click_two, R.id.click_three, R.id.click_price_type, R.id.click_add, R.id.click_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_check:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), DictionaryBuyerSellerPublic.class)
                            .putExtra("title", DictionaryBuyerSellerPublic.TITLE_SELLER)
                            .putExtra("type", "2")
                    );
                }
                break;
            case R.id.click_goods_view:
                clearFocus();
                preview_goods();
                break;
            case R.id.click_submit:
                clearFocus();
                publish();
                break;
            case R.id.insert:
                APPOftenUtils.createPhotoChooseDialog(0, 3, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        for (int i = 0; i < resultList.size(); i++) {
                            RichTextEditor.insertImage(resultList.get(i).getPhotoPath(), getWindowManager().getDefaultDisplay().getWidth() - 30, getWindowManager().getDefaultDisplay().getWidth());
                        }

                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                clearFocus();
                save_goods();
                break;
            case R.id.click_main:
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                bottomSheetDialog = menuDialog.creasteGoodsServiceEditPublic(getBaseActivityContext(), mainAdapter);
                break;
            case R.id.click_one:
                if (StringUtils.isEmpty(main)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请设置主品类");
                    return;
                }
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                bottomSheetDialog = menuDialog.creasteGoodsServiceEditPublic(getBaseActivityContext(), oneAdapter);
                break;
            case R.id.click_two:
                if (StringUtils.isEmpty(one)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请设置一级分类");
                    return;
                }
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                bottomSheetDialog = menuDialog.creasteGoodsServiceEditPublic(getBaseActivityContext(), twoAdapter);
                break;
            case R.id.click_three:
                if (StringUtils.isEmpty(two)) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "请设置二级分类");
                    return;
                }
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }
                bottomSheetDialog = menuDialog.creasteGoodsServiceEditPublic(getBaseActivityContext(), threeAdapter);
                break;
            case R.id.click_price_type:
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog = null;
                if (menuDialog == null) {
                    menuDialog = new MenuDialog(getBaseActivity());
                }

                bottomSheetDialog = menuDialog.creasteGoodsServiceEditPublic(getBaseActivityContext(), priceTypeAdapter);
                break;
            case R.id.click_add:
                clearFocus();
                specificationList.add(new SpecificationPriceModel());
                specificationAdapter.setList(specificationList);
                for (int i = 0; i < specificationList.size(); i++) {
                    LogUtils.e(specificationList.get(i).toString());
                }
                break;
            case R.id.click_time:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityChooseDate.class)
                            .putExtra("type", "shopTime"));
                }
                break;
        }
    }

    void clearFocus() {
        for (int i = 0; i < edittextList.size(); i++) {
            if (edittextList.get(i) != null) {
                edittextList.get(i).clearFocus();
                edittextList.get(i).setFocusable(true);
                edittextList.get(i).setFocusableInTouchMode(true);
            }
        }
    }


    void initType(int what) {
        switch (what) {
            case 0:
                one = null;
                two = null;
                three = null;
                oneList.clear();
                twoList.clear();
                threeList.clear();
                oneAdapter.setList(oneList);
                twoAdapter.setList(twoList);
                threeAdapter.setList(threeList);
                etOne.setText("");
                etTwo.setText("");
                etThree.setText("");
                break;
            case 1:
                two = null;
                three = null;
                twoList.clear();
                threeList.clear();
                twoAdapter.setList(twoList);
                threeAdapter.setList(threeList);
                etTwo.setText("");
                etThree.setText("");
                break;
            case 2:
                three = null;
                threeList.clear();
                threeAdapter.setList(threeList);
                etThree.setText("");
                break;

        }

    }


    void initAdapter() {
        /***************************************************主品类*****************************************************************/
        mainList.add("车品");
        mainList.add("车服");
        mainAdapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_goods_service_edit, mainList) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final String bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean);
                helper.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        bottomSheetDialog = null;
                        etMain.setText(bean);
                        if ("车品".equals(bean)) {
                            if (!"1".equals(main)) {
                                initType(0);
                                subclass(1, "1");
                            }
                            main = "1";
                        } else {
                            if (!"2".equals(main)) {
                                initType(0);
                                subclass(1, "2");
                            }
                            main = "2";
                        }

                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        /***************************************************一级分类*****************************************************************/
        oneAdapter = new SSS_Adapter<GoodsServiceEditModel>(getBaseActivityContext(), R.layout.item_goods_service_edit, oneList) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final GoodsServiceEditModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean.name);
                helper.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        if (!bean.classify_id.equals(one)) {
                            bottomSheetDialog = null;
                            initType(1);
                            etOne.setText(bean.name);
                            subclass(2, bean.classify_id);
                        }
                        one = bean.classify_id;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        /***************************************************二级分类*****************************************************************/
        twoAdapter = new SSS_Adapter<GoodsServiceEditModel>(getBaseActivityContext(), R.layout.item_goods_service_edit, twoList) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final GoodsServiceEditModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean.name);
                helper.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        if (!bean.classify_id.equals(two)) {
                            bottomSheetDialog = null;
                            initType(2);
                            etTwo.setText(bean.name);
                            subclass(3, bean.classify_id);
                        }
                        two = bean.classify_id;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        /***************************************************三级分类*****************************************************************/
        threeAdapter = new SSS_Adapter<GoodsServiceEditModel>(getBaseActivityContext(), R.layout.item_goods_service_edit, threeList) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final GoodsServiceEditModel bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean.name);
                helper.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        bottomSheetDialog = null;
                        etThree.setText(bean.name);
                        three = bean.classify_id;
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        /***************************************************价格类型*****************************************************************/
        priceTypeList.add("一口价");
        priceTypeList.add("区间价格");
        priceTypeList.add("面议价格");
        priceTypeAdapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_goods_service_edit, priceTypeList) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final String bean, SSS_Adapter instance) {
                helper.setText(R.id.title, bean);
                helper.getView(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        bottomSheetDialog = null;
                        etPriceType.setText(bean);
                        if ("一口价".equals(bean)) {
                            priceType = "1";
                            priceSetParent.setVisibility(View.GONE);
                            specificationAndPriceListView.setVisibility(View.GONE);
                            if ("面议".equals(etPrice.getText().toString().trim())){
                                etPrice.setText("0");
                            }
                        } else if ("区间价格".equals(bean)) {
                            priceType = "2";
                            priceSetParent.setVisibility(View.VISIBLE);
                            specificationAndPriceListView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < specificationList.size(); i++) {
                                if ("面议".equals(specificationList.get(i).price)){
                                    specificationList.get(i).price="0";
                                }
                            }
                            specificationAdapter.setList(specificationList);
                            if ("面议".equals(etPrice.getText().toString().trim())){
                                etPrice.setText("0");
                            }
                        } else if ("面议价格".equals(bean)) {
                            priceType = "3";
                            priceSetParent.setVisibility(View.VISIBLE);
                            specificationAndPriceListView.setVisibility(View.VISIBLE);
                            etPrice.setText("面议");
                            for (int i = 0; i < specificationList.size(); i++) {
                                specificationList.get(i).price="面议";
                            }
                            specificationAdapter.setList(specificationList);
                        }

                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        etPriceType.setText("一口价");
        priceType = "1";
        priceSetParent.setVisibility(View.GONE);
        /***************************************************规格价格*****************************************************************/
        specificationAdapter = new SSS_Adapter<SpecificationPriceModel>(getBaseActivityContext(), R.layout.layout_price) {
            @Override
            protected void setView(final SSS_HolderHelper helper, final int position, final SpecificationPriceModel bean, SSS_Adapter instance) {
                LogUtils.e(bean.toString());
                helper.setVisibility(R.id.click_subtract, View.VISIBLE);
                helper.setText(R.id.et_specification, bean.specification);
                helper.setText(R.id.et_price, bean.price);
                edittextList.add(((EditText) helper.getView(R.id.et_specification)));
                edittextList.add(((EditText) helper.getView(R.id.et_price)));
                if (priceType.equals("3")){
                    helper.getView(R.id.et_price).setEnabled(false);
                }else {
                    helper.getView(R.id.et_price).setEnabled(true);
                }
                ((EditText) helper.getView(R.id.et_specification)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (position < specificationList.size()) {
                            specificationList.get(position).specification = ((EditText) helper.getView(R.id.et_specification)).getText().toString().trim();
                        }
                    }
                });


                ((EditText) helper.getView(R.id.et_price)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (((EditText) helper.getView(R.id.et_price)).getText().toString().trim().toString().startsWith("0")) {
                            helper.setText(R.id.et_specification, "");
                        } else {
                            if (position < specificationList.size()) {
                                specificationList.get(position).price = ((EditText) helper.getView(R.id.et_price)).getText().toString().trim();
                            }
                        }
                    }
                });


                helper.getView(R.id.click_subtract).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < edittextList.size(); i++) {
                            if (edittextList.get(i) != null) {
                                if (edittextList.get(i) == helper.getView(R.id.et_specification)) {
                                    edittextList.remove(i);
                                }
                                if (edittextList.get(i) == helper.getView(R.id.et_price)) {
                                    edittextList.remove(i);
                                }
                            }
                        }
                        specificationList.remove(position);
                        specificationAdapter.setList(specificationList);
                    }
                });
            }


            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        specificationAndPriceListView.setAdapter(specificationAdapter);

    }


    /**
     * 获取车品子分类
     */
    public void subclass(final int what, String classify_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        if (ywLoadingDialog == null) {
            if (getBaseActivityContext() != null) {
                ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
                ywLoadingDialog.show();
            }
        }

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.subclass(
                    new JSONObject()
                            .put("classify_id", classify_id)
                            .put("more", "1")
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
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if (1 == what) {
                                            oneList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsServiceEditModel.class));
                                            oneAdapter.setList(oneList);
                                        } else if (2 == what) {
                                            twoList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsServiceEditModel.class));
                                            twoAdapter.setList(twoList);
                                        } else if (3 == what) {
                                            threeList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), GoodsServiceEditModel.class));
                                            threeAdapter.setList(threeList);
                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 获取订单提示
     */
    public void tip() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderTip(
                    new JSONObject()
                            .put("article_id", "13")
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
                                    tip.setText(jsonObject.getJSONObject("data").getString("contents"));
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }

    /**
     * 发布
     */
    public void publish() {
        if (StringUtils.isEmpty(main) || StringUtils.isEmpty(one) || StringUtils.isEmpty(two) || StringUtils.isEmpty(three)) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请完善品类");
            return;
        }
        if (StringUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请完善品名");
            return;
        }

        if (StringUtils.isEmpty(priceType)) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请完善价格类型");
            return;
        }
        if (StringUtils.isEmpty(etSpecification.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请设置规格");
            return;
        }
        if (StringUtils.isEmpty(etPrice.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请设置价格");
            return;
        }
        if (StringUtils.isEmpty(etInventory.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请设置库存");
            return;
        }
        if (StringUtils.isEmpty(etService.getText().toString().trim())) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请设置服务保障");
            return;
        }

        if (GalleryHorizontalListView.getList().size() < 1) {
            ToastUtils.showShortToast(getBaseActivityContext(), "请添加图片");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        String[] a = etTime.getText().toString().trim().split("—");
        String startTime = null, endTime = null;
        if (a.length == 2) {
            startTime = a[0];
            endTime = a[1];
        }
        JSONArray size_data = new JSONArray();
        List<RichTextEditor.EditData> list = RichTextEditor.buildEditData();
        JSONArray photo = new JSONArray();
        try {

            if ("2".equals(priceType)){
                for (int i = 0; i < specificationList.size(); i++) {
                    size_data.put(new JSONObject().put("id", i + 1)
                            .put("name", specificationList.get(i).specification)
                            .put("price", specificationList.get(i).price));

                }
            }else   if ("3".equals(priceType)) {
                for (int i = 0; i < specificationList.size(); i++) {
                    size_data.put(new JSONObject().put("id", i + 1)
                            .put("name", specificationList.get(i).specification)
                            .put("price","0"));

                }
            }
            size_data.put(new JSONObject()
                    .put("name", etSpecification.getText().toString().trim())
                    .put("price", etPrice.getText().toString().trim()));

            for (int i = 0; i < list.size(); i++) {
                LogUtils.e(list.get(i).toString());
                if (!StringUtils.isEmpty(list.get(i).imagePath)) {
                    if (list.get(i).imagePath.startsWith("http")) {
                        photo.put(new JSONObject().put("img", list.get(i).imagePath));
                    } else {
                        photo.put(
                                new JSONObject()
                                        .put("img", ConvertUtils.bitmapToBase64(
                                                BitmapUtils.decodeSampledBitmapFromFile(
                                                        list.get(i).imagePath, getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight()
                                                ))
                                        )
                        );
                    }


                } else if (!StringUtils.isEmpty(list.get(i).inputStr)) {
                    photo.put(new JSONObject().put("text", list.get(i).inputStr));
                }
            }
//            for (int i = 0; i < list.size(); i++) {
//                LogUtils.e(list.get(i).toString());
//                if (!StringUtils.isEmpty(list.get(i).imagePath)) {
//                    photo.put(
//                            new JSONObject()
//                                    .put("img", ConvertUtils.bitmapToBase64(
//                                            BitmapUtils.decodeSampledBitmapFromFile(
//                                                    list.get(i).imagePath, getWindowManager().getDefaultDisplay().getWidth(),
//                                                    getWindowManager().getDefaultDisplay().getHeight()
//                                            ))
//                                    )
//                    );
//
//                } else if (!StringUtils.isEmpty(list.get(i).inputStr)) {
//                    photo.put(new JSONObject().put("text", list.get(i).inputStr));
//                }
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray picture = new JSONArray();
        for (int i = 0; i < GalleryHorizontalListView.getList().size(); i++) {
            if (!GalleryHorizontalListView.Holder.equals(GalleryHorizontalListView.getList().get(i))) {
                if (GalleryHorizontalListView.getList().get(i).startsWith("http")) {
                    picture.put(GalleryHorizontalListView.getList().get(i));
                } else {
                    picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(GalleryHorizontalListView.getList().get(i), getWindowManager().getDefaultDisplay().getWidth(),
                            getWindowManager().getDefaultDisplay().getHeight())));
                }
            }
        }
//        JSONArray picture = new JSONArray();
//        for (int i = 0; i < GalleryHorizontalListView.getList().size(); i++) {
//            if (!GalleryHorizontalListView.Holder.equals(GalleryHorizontalListView.getList().get(i))) {
//                picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(GalleryHorizontalListView.getList().get(i), getWindowManager().getDefaultDisplay().getWidth(),
//                        getWindowManager().getDefaultDisplay().getHeight())));
//            }
//        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("classify_id", three)
                            .put("type", main)
                            .put("status", "1")
                            .put("goods_id", goods_id)
                            .put("title", etName.getText().toString().trim())
                            .put("brand", etBrand.getText().toString().trim())
                            .put("slogan", etRecommend.getText().toString().trim())
                            .put("vehicle_type", etCar.getText().toString().trim())
                            .put("price_type", priceType)
                            .put("number", etInventory.getText().toString().trim())
                            .put("service", etService.getText().toString().trim())
                            .put("start_time", startTime)
                            .put("end_time", endTime)
                            .put("picture", picture)
                            .put("photo", photo)
                            .put("size_data", size_data)
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
                                    EventBus.getDefault().post(new ChangedGoodsList());
                                    ToastUtils.showLongToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                } else {
                                    ToastUtils.showLongToast(getBaseActivityContext(), jsonObject.getString("message"));
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

    /**
     * 保存
     */
    public void save_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        String[] a = etTime.getText().toString().trim().split("—");
        String startTime = null, endTime = null;
        if (a.length == 2) {
            startTime = a[0];
            endTime = a[1];
        }
        JSONArray size_data = new JSONArray();
        List<RichTextEditor.EditData> list = RichTextEditor.buildEditData();
        JSONArray photo = new JSONArray();
        try {

            for (int i = 0; i < specificationList.size(); i++) {
                size_data.put(new JSONObject().put("id", i + 1)
                        .put("name", specificationList.get(i).specification)
                        .put("price", specificationList.get(i).price));

            }
            size_data.put(new JSONObject()
                    .put("name", etSpecification.getText().toString().trim())
                    .put("price", etPrice.getText().toString().trim()));


            for (int i = 0; i < list.size(); i++) {
                LogUtils.e(list.get(i).toString());
                if (!StringUtils.isEmpty(list.get(i).imagePath)) {
                    if (list.get(i).imagePath.startsWith("http")) {
                        photo.put(new JSONObject().put("img", list.get(i).imagePath));
                    } else {
                        photo.put(
                                new JSONObject()
                                        .put("img", ConvertUtils.bitmapToBase64(
                                                BitmapUtils.decodeSampledBitmapFromFile(
                                                        list.get(i).imagePath, getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight()
                                                ))
                                        )
                        );
                    }


                } else if (!StringUtils.isEmpty(list.get(i).inputStr)) {
                    photo.put(new JSONObject().put("text", list.get(i).inputStr));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray picture = new JSONArray();
        for (int i = 0; i < GalleryHorizontalListView.getList().size(); i++) {
            if (!GalleryHorizontalListView.Holder.equals(GalleryHorizontalListView.getList().get(i))) {
                if (GalleryHorizontalListView.getList().get(i).startsWith("http")) {
                    picture.put(GalleryHorizontalListView.getList().get(i));
                } else {
                    picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(GalleryHorizontalListView.getList().get(i), getWindowManager().getDefaultDisplay().getWidth(),
                            getWindowManager().getDefaultDisplay().getHeight())));
                }
            }
        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.save_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("classify_id", three)
                            .put("type", main)
                            .put("goods_id", goods_id)
                            .put("title", etName.getText().toString().trim())
                            .put("brand", etBrand.getText().toString().trim())
                            .put("slogan", etRecommend.getText().toString().trim())
                            .put("vehicle_type", etCar.getText().toString().trim())
                            .put("price_type", priceType)
                            .put("number", etInventory.getText().toString().trim())
                            .put("service", etService.getText().toString().trim())
                            .put("start_time", startTime)
                            .put("end_time", endTime)
                            .put("picture", picture)
                            .put("photo", photo)
                            .put("size_data", size_data)
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
                                    ToastUtils.showLongToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    finish();
                                } else {
                                    ToastUtils.showLongToast(getBaseActivityContext(), jsonObject.getString("message"));
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


    /**
     * 预览商品
     */
    public void preview_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        String[] a = etTime.getText().toString().trim().split("—");
        String startTime = null, endTime = null;
        if (a.length == 2) {
            startTime = a[0];
            endTime = a[1];
        }
        JSONArray size_data = new JSONArray();
        List<RichTextEditor.EditData> list = RichTextEditor.buildEditData();
        JSONArray photo = new JSONArray();
        try {

            for (int i = 0; i < specificationList.size(); i++) {
                size_data.put(new JSONObject().put("id", i + 1)
                        .put("name", specificationList.get(i).specification)
                        .put("price", specificationList.get(i).price));

            }
            size_data.put(new JSONObject()
                    .put("name", etSpecification.getText().toString().trim())
                    .put("price", etPrice.getText().toString().trim()));


            for (int i = 0; i < list.size(); i++) {
                LogUtils.e(list.get(i).toString());
                if (!StringUtils.isEmpty(list.get(i).imagePath)) {
                    if (list.get(i).imagePath.startsWith("http")) {
                        photo.put(new JSONObject().put("img", list.get(i).imagePath));
                    } else {
                        photo.put(
                                new JSONObject()
                                        .put("img", ConvertUtils.bitmapToBase64(
                                                BitmapUtils.decodeSampledBitmapFromFile(
                                                        list.get(i).imagePath, getWindowManager().getDefaultDisplay().getWidth(),
                                                        getWindowManager().getDefaultDisplay().getHeight()
                                                ))
                                        )
                        );
                    }


                } else if (!StringUtils.isEmpty(list.get(i).inputStr)) {
                    photo.put(new JSONObject().put("text", list.get(i).inputStr));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray picture = new JSONArray();
        for (int i = 0; i < GalleryHorizontalListView.getList().size(); i++) {
            if (!GalleryHorizontalListView.Holder.equals(GalleryHorizontalListView.getList().get(i))) {
                if (GalleryHorizontalListView.getList().get(i).startsWith("http")) {
                    picture.put(GalleryHorizontalListView.getList().get(i));
                } else {
                    picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(GalleryHorizontalListView.getList().get(i), getWindowManager().getDefaultDisplay().getWidth(),
                            getWindowManager().getDefaultDisplay().getHeight())));
                }
            }
        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.preview_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("classify_id", three)
                            .put("type", main)
                            .put("goods_id", goods_id)
                            .put("title", etName.getText().toString().trim())
                            .put("brand", etBrand.getText().toString().trim())
                            .put("slogan", etRecommend.getText().toString().trim())
                            .put("vehicle_type", etCar.getText().toString().trim())
                            .put("price_type", priceType)
                            .put("number", etInventory.getText().toString().trim())
                            .put("service", etService.getText().toString().trim())
                            .put("start_time", startTime)
                            .put("end_time", endTime)
                            .put("picture", picture)
                            .put("photo", photo)
                            .put("size_data", size_data)
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
                                    if (getBaseActivityContext() != null) {
                                        startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                                .putExtra("goods_id", jsonObject.getJSONObject("data").getString("goods_id"))
                                                .putExtra("stop", true)
                                                .putExtra("type", main));
                                    }

                                } else {
                                    ToastUtils.showLongToast(getBaseActivityContext(), jsonObject.getString("message"));
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

    /**
     * 我的商品==>获取商品信息
     */
    void update_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.update_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .toString()
                    , new StringCallback() {
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
                                    etName.setText(jsonObject.getJSONObject("data").getString("title"));
                                    etBrand.setText(jsonObject.getJSONObject("data").getString("brand"));
                                    etRecommend.setText(jsonObject.getJSONObject("data").getString("slogan"));
                                    etCar.setText(jsonObject.getJSONObject("data").getString("vehicle_type"));
                                    etService.setText(jsonObject.getJSONObject("data").getString("service"));
                                    etInventory.setText(jsonObject.getJSONObject("data").getString("number"));
                                    if (!"0".equals(jsonObject.getJSONObject("data").getString("start_time")) && !"0".equals(jsonObject.getJSONObject("data").getString("end_time"))) {
                                        etTime.setText(jsonObject.getJSONObject("data").getString("start_time") + "—" + jsonObject.getJSONObject("data").getString("end_time"));
                                    }


                                    /***************************************************************************/
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        temp.add(Config.url + jsonArray.getString(i));
                                    }
                                    GalleryHorizontalListView.setList(temp);
                                    /***************************************************************************/
                                    JSONArray size_data = jsonObject.getJSONObject("data").getJSONArray("size_data");
                                    if (size_data.length() > 0) {
                                        for (int i = 0; i < size_data.length() - 1; i++) {
                                            SpecificationPriceModel specificationPriceModel = new SpecificationPriceModel();
                                            specificationPriceModel.price = size_data.getJSONObject(i).getString("price");
                                            specificationPriceModel.specification = size_data.getJSONObject(i).getString("name");
                                            specificationList.add(specificationPriceModel);
                                        }
                                        specificationAdapter.setList(specificationList);
                                        etSpecification.setText(size_data.getJSONObject(size_data.length() - 1).getString("name"));
                                        etPrice.setText(size_data.getJSONObject(size_data.length() - 1).getString("price"));
                                    }
                                    /***************************************************************************/
                                    JSONArray classify_data = jsonObject.getJSONObject("data").getJSONArray("classify_data");
                                    for (int i = 0; i < classify_data.length(); i++) {
                                        CassifyData cassifyData = new CassifyData();
                                        cassifyData.classify_id = classify_data.getJSONObject(i).getString("classify_id");
                                        cassifyData.name = classify_data.getJSONObject(i).getString("name");
                                        cassifyData.level = classify_data.getJSONObject(i).getString("level");
                                        cassifyDataList.add(cassifyData);
                                    }

                                    for (int i = 0; i < cassifyDataList.size(); i++) {
                                        if ("1".equals(cassifyDataList.get(i).level)) {
                                            etMain.setText(cassifyDataList.get(i).name);
                                            main = cassifyDataList.get(i).classify_id;
                                            subclass(1, cassifyDataList.get(i).classify_id);
                                        } else if ("2".equals(cassifyDataList.get(i).level)) {
                                            etOne.setText(cassifyDataList.get(i).name);
                                            one = cassifyDataList.get(i).classify_id;
                                            subclass(2, cassifyDataList.get(i).classify_id);
                                        } else if ("3".equals(cassifyDataList.get(i).level)) {
                                            etTwo.setText(cassifyDataList.get(i).name);
                                            two = cassifyDataList.get(i).classify_id;
                                            subclass(3, cassifyDataList.get(i).classify_id);
                                        } else if ("4".equals(cassifyDataList.get(i).level)) {
                                            etThree.setText(cassifyDataList.get(i).name);
                                            three = cassifyDataList.get(i).classify_id;
                                        }
                                    }
                                    /***************************************************************************/
                                    JSONArray photo = jsonObject.getJSONObject("data").getJSONArray("photo");
                                    for (int i = 0; i < photo.length(); i++) {
                                        if (photo.getJSONObject(i).has("img")) {
                                            RichTextEditor.addImageViewAtIndex(i, Config.url + photo.getJSONObject(i).getString("img"),
                                                    ScreenUtils.getScreenWidth(getBaseActivityContext()),
                                                    ScreenUtils.getScreenHeight(getBaseActivityContext()));
                                        } else if (photo.getJSONObject(i).has("text")) {
                                            RichTextEditor.addEditTextAtIndex(i, photo.getJSONObject(i).getString("text"));
                                        }
                                    }

                                    if ("1".equals(jsonObject.getJSONObject("data").getString("price_type"))) {//	1一口价，2区间价格，3面议价格
                                        etPriceType.setText(priceTypeList.get(0));
                                        priceType = "1";
                                    } else if ("2".equals(jsonObject.getJSONObject("data").getString("price_type"))) {//	1一口价，2区间价格，3面议价格
                                        etPriceType.setText(priceTypeList.get(1));
                                        priceType = "2";
                                    } else if ("3".equals(jsonObject.getJSONObject("data").getString("price_type"))) {//	1一口价，2区间价格，3面议价格
                                        etPriceType.setText(priceTypeList.get(2));
                                        priceType = "3";
                                    }

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

}
