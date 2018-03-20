package com.sss.car.popularize;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.rey.material.app.BottomSheetDialog;
import com.sss.car.Config;
import com.sss.car.EventBusModel.ChangeInfoModel;
import com.sss.car.EventBusModel.ChangedPopularizeModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.dictionary.DictionaryBuyerSellerPublic;
import com.sss.car.model.PopularizeCateModel;
import com.sss.car.model.PopularizeCityModel;
import com.sss.car.utils.MenuDialog;
import com.sss.car.utils.PayUtils;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.ActivityPopularizerChooseTime;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by leilei on 2018/1/18.
 */

@SuppressWarnings("ALL")
public class PopularizeEdit extends BaseActivity implements View.OnClickListener, GalleryHorizontalListView.OnGalleryHorizontalListViewCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.top_parent)
    LinearLayout topParent;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.tip_type)
    TextView tipType;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.tip_category)
    TextView tipCategory;
    @BindView(R.id.et_category)
    EditText etCategory;
    @BindView(R.id.tip_one)
    TextView tipOne;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.tip_two)
    TextView tipTwo;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.tip_include)
    TextView tipInclude;
    @BindView(R.id.et_include)
    EditText etInclude;
    @BindView(R.id.tip_goods)
    TextView tipGoods;
    @BindView(R.id.et_goods)
    EditText etGoods;
    @BindView(R.id.tip_city)
    TextView tipCity;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.tip_sex)
    TextView tipSex;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.tip_number)
    TextView tipNumber;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tip_price)
    TextView tipPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tip_time)
    TextView tipTime;
    @BindView(R.id.et_time)
    EditText etTime;
    @BindView(R.id.tip_picture)
    TextView tipPicture;
    @BindView(R.id.picture)
    GalleryHorizontalListView picture;
    @BindView(R.id.tip_describe)
    TextView tipDescribe;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.tv_price_check)
    TextView tvPriceCheck;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.popularize_edit)
    LinearLayout popularizeEdit;
    YWLoadingDialog ywLoadingDialog;
    Gson gson = new Gson();
    MenuDialog menuDialog;
    BottomSheetDialog bottomSheetDialog;

    //一级菜单集合与适配器
    List<PopularizeCateModel> typeList = new ArrayList<>();
    SSS_Adapter typeAdapter;
    //二级菜单集合与适配器
    List<PopularizeCateModel> categoryList = new ArrayList<>();
    SSS_Adapter categoryAdapter;
    //三级菜单集合与适配器
    List<PopularizeCateModel> oneList = new ArrayList<>();
    SSS_Adapter oneAdapter;
    //四级菜单集合与适配器
    List<PopularizeCateModel> twoList = new ArrayList<>();
    SSS_Adapter twoAdapter;
    //城市集合与适配器
    List<PopularizeCityModel> cityList = new ArrayList<>();
    SSS_Adapter cityAdapter;
    //性别集合与适配器
    List<String> sexList = new ArrayList<>();
    SSS_Adapter sexAdapter;

    String popularize_id;//推广ID

    String type;//一级菜单ID
    String category;//二级菜单ID
    String one;//三级菜单ID
    String two;//四级菜单ID
    String coupon_id;//优惠券ID
    String subject_id; //活动ID
    String city_id; //城市ID
    String sex; //性别
    String startTime;//开始时间
    String endTime;//结束时间
    String number;//数量
    String totalPrice;//总价
    boolean twoCanClick = false;//二级菜单能否点击
    boolean threeCanClick = false;//三级菜单能否点击
    boolean fourCanClick = false;//四级菜单能否点击
    boolean isCoupon = false;//优惠券模式
    boolean isActivity = false;//活动模式
    boolean isGround = false;//地推人员模式
    boolean canOperation = true;


    List<String> temp = new ArrayList<>();


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        if (sexList != null) {
            sexList.clear();
        }
        sexList = null;
        if (sexAdapter != null) {
            sexAdapter.clear();
        }
        sexAdapter = null;
        city_id = null;
        sex = null;
        if (cityList != null) {
            cityList.clear();
        }
        cityList = null;
        if (cityAdapter != null) {
            cityAdapter.clear();
        }
        cityAdapter = null;
        totalPrice = null;
        startTime = null;
        endTime = null;
        if (temp != null) {
            temp.clear();
        }
        temp = null;
        subject_id = null;
        coupon_id = null;
        if (categoryAdapter != null) {
            categoryAdapter.clear();
        }
        categoryAdapter = null;
        category = null;
        if (categoryList != null) {
            categoryList.clear();
        }
        categoryList = null;
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
        bottomSheetDialog = null;
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (typeAdapter != null) {
            typeAdapter.clear();
        }
        typeAdapter = null;
        if (typeList != null) {
            typeList.clear();
        }
        typeList = null;
        backTop = null;
        gson = null;
        rightButtonTop = null;
        topParent = null;
        titleTop = null;
        tipType = null;
        etType = null;
        tipCategory = null;
        etCategory = null;
        tipOne = null;
        etOne = null;
        tipTwo = null;
        etTwo = null;
        tipInclude = null;
        etInclude = null;
        tipGoods = null;
        etGoods = null;
        tipCity = null;
        etCity = null;
        tipSex = null;
        etSex = null;
        tipNumber = null;
        etNumber = null;
        tipPrice = null;
        etPrice = null;
        tipTime = null;
        etTime = null;
        tipPicture = null;
        picture = null;
        tipDescribe = null;
        etDescribe = null;
        tvPriceCheck = null;
        tvTip = null;
        tvTotalPrice = null;
        submit = null;
        popularizeEdit = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popularize_edit);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误！");
            finish();
        }
        ButterKnife.bind(this);
        customInit(popularizeEdit, false, true, true);
        titleTop.setText("申请推广");
        APPOftenUtils.underLineOfTextView(rightButtonTop).setTextColor(getResources().getColor(R.color.mainColor));
        rightButtonTop.setText("保存");
        rightButtonTop.setOnClickListener(this);
        backTop.setOnClickListener(this);
        tip();
        etGoods.setText(getIntent().getExtras().getString("title"));
        if ("edit".equals(getIntent().getExtras().getString("type"))) {
            get_info(getIntent().getExtras().getString("popularize_id"));
        } else {
            init();
            popularize_type(null, 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeInfoModel changeInfoModel) {
        if ("popularize".equals(changeInfoModel.type)) {
            etTime.setText(changeInfoModel.startTime + "-" + changeInfoModel.endTime);
            startTime = changeInfoModel.startTime;
            endTime = changeInfoModel.endTime;
            requestPrice();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangedPopularizeModel changeInfoModel) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                popularize_into("4");
                break;
            case R.id.tv_price_check:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), DictionaryBuyerSellerPublic.class)
                            .putExtra("title", DictionaryBuyerSellerPublic.TITLE_SELLER)
                            .putExtra("type", "2")
                    );
                }
                break;
            case R.id.submit:
                if (canOperation) {
                    popularize_into("0");
                }
                break;
            case R.id.et_type:
                if (canOperation) {
                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getBaseActivity());
                    }
                    bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), typeAdapter);
                }
                break;
            case R.id.et_category:
                if (canOperation) {
                    if (twoCanClick == false) {
                        return;
                    }

                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getBaseActivity());
                    }
                    bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), categoryAdapter);
                }
                break;
            case R.id.et_one:
                if (canOperation) {
                    if (threeCanClick == false) {
                        return;
                    }
                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getBaseActivity());
                    }
                    bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), oneAdapter);
                }
                break;
            case R.id.et_two:
                if (canOperation) {
                    if (fourCanClick == false) {
                        return;
                    }
                    if (menuDialog == null) {
                        menuDialog = new MenuDialog(getBaseActivity());
                    }
                    bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), twoAdapter);
                }
                break;
            case R.id.et_city:
                if (canOperation) {
                    if (isGround) {
                        if (menuDialog == null) {
                            menuDialog = new MenuDialog(getBaseActivity());
                        }
                        bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), cityAdapter);
                    }
                }
                break;
            case R.id.et_sex:
                if (canOperation) {
                    if (isGround) {
                        if (menuDialog == null) {
                            menuDialog = new MenuDialog(getBaseActivity());
                        }
                        bottomSheetDialog = menuDialog.createPopularizeDialog(getBaseActivityContext(), sexAdapter);
                    }
                }
                break;

            case R.id.et_time:
                if (canOperation) {
                    if (getBaseActivityContext() != null) {
                        startActivity(new Intent(getBaseActivityContext(), ActivityPopularizerChooseTime.class));
                    }
                }
                break;
        }
    }

    private void init() {
        initAdapter();
        stopInput();
        initClick();
        initColor();
        sexList.add("男");
        sexList.add("女");
        sexList.add("全部");
        sexAdapter.setList(sexList);
        picture.setCanOperation(false);
        temp.add(GalleryHorizontalListView.Holder);
        picture.setList(temp);
        picture.setHigth(getBaseActivityContext(), 70);
        picture.setWidth(getBaseActivityContext(), 70);
        picture.setOnGalleryHorizontalListViewCallBack(this);
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isEmpty(etPrice.getText().toString().trim())) {
                    if ("0".equals(etPrice.getText().toString().trim())) {
                        etPrice.setText("");
                    } else {
                        requestPrice();
                    }
                }
            }
        });
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isEmpty(etNumber.getText().toString().trim())) {
                    if ("0".equals(etNumber.getText().toString().trim())) {
                        etNumber.setText("");
                    } else {
                        number = etNumber.getText().toString().trim();
                        requestPrice();
                    }
                }
            }
        });
    }

    private void requestPrice() {
        if (!StringUtils.isEmpty(two)) {
            popularize_charge(two, startTime, endTime, number);
        } else if (!StringUtils.isEmpty(one)) {
            popularize_charge(one, startTime, endTime, number);
        } else if (StringUtils.isEmpty(category)) {
            popularize_charge(category, startTime, endTime, number);
        } else if (StringUtils.isEmpty(type)) {
            popularize_charge(type, startTime, endTime, number);
        }
    }

    private void initClick() {
        etType.setOnClickListener(this);
        etCategory.setOnClickListener(this);
        etOne.setOnClickListener(this);
        etTwo.setOnClickListener(this);
    }

    private void stopInput() {
        etType.setInputType(InputType.TYPE_NULL);
        etCategory.setInputType(InputType.TYPE_NULL);
        etOne.setInputType(InputType.TYPE_NULL);
        etTwo.setInputType(InputType.TYPE_NULL);
        etInclude.setInputType(InputType.TYPE_NULL);
        etGoods.setInputType(InputType.TYPE_NULL);
        etCity.setInputType(InputType.TYPE_NULL);
        etSex.setInputType(InputType.TYPE_NULL);
        etNumber.setInputType(InputType.TYPE_NULL);
        etPrice.setInputType(InputType.TYPE_NULL);
        etTime.setInputType(InputType.TYPE_NULL);
    }

    private void initColor() {
        tipCity.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipSex.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipNumber.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipPrice.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipTime.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipPicture.setTextColor(getResources().getColor(R.color.edittext_stop));
        tipDescribe.setTextColor(getResources().getColor(R.color.edittext_stop));
    }

    private void clearData(int where) {
        switch (where) {
            case 1:
                category = null;
                etCategory.setText("");
                one = null;
                etOne.setText("");
                two = null;
                etTwo.setText("");
                coupon_id = null;
                subject_id = null;
                city_id = null;
                etCity.setText("");
                categoryList.clear();
                categoryAdapter.setList(categoryList);
                oneList.clear();
                oneAdapter.setList(oneList);
                twoList.clear();
                twoAdapter.setList(twoList);
                break;
            case 2:
                one = null;
                etOne.setText("");
                two = null;
                etTwo.setText("");
                tipOne.setTextColor(getResources().getColor(R.color.edittext_stop));
                tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                subject_id = null;
                oneList.clear();
                oneAdapter.setList(oneList);
                twoList.clear();
                twoAdapter.setList(twoList);
                break;
            case 3:
                two = null;
                etTwo.setText("");
                tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                coupon_id = null;
                twoList.clear();
                twoAdapter.setList(twoList);
                break;
        }

        tipInclude.setTextColor(getResources().getColor(R.color.edittext_stop));
        etInclude.setOnClickListener(null);

        tipCity.setTextColor(getResources().getColor(R.color.edittext_stop));
        etCity.setOnClickListener(null);

        tipDescribe.setTextColor(getResources().getColor(R.color.edittext_stop));
        etDescribe.setInputType(InputType.TYPE_NULL);
        etDescribe.setText("");

        tipSex.setTextColor(getResources().getColor(R.color.edittext_stop));
        etSex.setOnClickListener(null);

        tipPrice.setTextColor(getResources().getColor(R.color.edittext_stop));
        etPrice.setInputType(InputType.TYPE_NULL);
        etPrice.setText("");

        tipNumber.setTextColor(getResources().getColor(R.color.edittext_stop));
        etNumber.setInputType(InputType.TYPE_NULL);
        etNumber.setText("");

        tipTime.setTextColor(getResources().getColor(R.color.edittext_stop));
        etTime.setOnClickListener(null);

        tipPicture.setTextColor(getResources().getColor(R.color.edittext_stop));
        picture.setCanOperation(false);


        submit.setBackgroundColor(getResources().getColor(R.color.grayness));
        submit.setOnClickListener(null);

        totalPrice = null;
        tvTotalPrice.setText("");
        startTime = null;
        endTime = null;
        number = null;
        fourCanClick = false;
        if (isCoupon) {
            threeCanClick = true;
        } else {
            threeCanClick = false;
        }
        isCoupon = false;
        isActivity = false;
        etInclude.setText("");

    }


    public void resetTip(int where, boolean canOperation) {
        switch (where) {
            case 1:
                if (canOperation) {
                    tipCategory.setTextColor(getResources().getColor(R.color.black));
                    tipOne.setTextColor(getResources().getColor(R.color.edittext_stop));
                    tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                } else {
                    tipCategory.setTextColor(getResources().getColor(R.color.edittext_stop));
                    tipOne.setTextColor(getResources().getColor(R.color.edittext_stop));
                    tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                }

                break;
            case 2:
                if (canOperation) {
                    tipOne.setTextColor(getResources().getColor(R.color.black));
                    tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                } else {
                    tipOne.setTextColor(getResources().getColor(R.color.edittext_stop));
                    tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                }

                break;
            case 3:
                if (canOperation) {
                    tipTwo.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tipTwo.setTextColor(getResources().getColor(R.color.edittext_stop));
                }

                break;
        }
    }


    private void initAdapter() {

        typeAdapter = new SSS_Adapter<PopularizeCateModel>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final PopularizeCateModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean.name);
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = bean.cate_id;
                        etType.setText(bean.name);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        clearData(1);
                        fourCanClick = false;
                        threeCanClick = false;
                        if (bean.is_subclass != 0) {
                            resetTip(1, true);
                            popularize_type(bean.cate_id, 2);
                        } else {
                            resetTip(1, false);
                            popularize_rule(type, 0);
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        categoryAdapter = new SSS_Adapter<PopularizeCateModel>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final PopularizeCateModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean.name);
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        category = bean.cate_id;
                        etCategory.setText(bean.name);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        clearData(2);
                        if (bean.is_subclass != 0) {
                            popularize_type(bean.cate_id, 3);
                            fourCanClick = false;
                            threeCanClick = true;
                            isActivity = false;
                            isGround = false;
                            resetTip(2, true);
                        } else {
                            if ("活动专题".equals(bean.name)) {
                                fourCanClick = false;
                                threeCanClick = true;
                                isActivity = true;
                                isGround = false;
                                popularize_type_subject(bean.cate_id);
                                popularize_rule(category, 1);
                                resetTip(2, true);
                            } else if ("优惠券推广".equals(bean.name)) {
                                fourCanClick = false;
                                threeCanClick = true;
                                isActivity = false;
                                isGround = false;
                                popularize_type_coupon(bean.cate_id);
                                popularize_rule(category, 0);
                                resetTip(2, true);
                            } else if ("地推人员宣传单".equals(bean.name)) {
                                isGround = true;
                                popularize_city();
                                resetTip(2, false);
                                popularize_rule(category, 0);
                            } else {
                                isGround = false;
                                resetTip(2, false);
                                popularize_rule(category, 0);
                            }
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        oneAdapter = new SSS_Adapter<PopularizeCateModel>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final PopularizeCateModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean.name);
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        one = bean.cate_id;
                        etOne.setText(bean.name);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        if (isActivity) {
                            subject_id = bean.subject_id;
                        }

                        clearData(3);
                        if (bean.is_subclass != 0) {
                            fourCanClick = true;
                            isCoupon = false;
                            popularize_type(bean.cate_id, 4);
                            resetTip(3, true);
                            threeCanClick = true;
                        } else {
                            if ("满减券".equals(bean.name) || "现金券".equals(bean.name) || "折扣券".equals(bean.name)) {
                                threeCanClick = true;
                                fourCanClick = true;
                                isCoupon = true;
                                popularize_type_coupon(bean.cate_id);
                                resetTip(3, true);
                            } else {
                                isCoupon = false;
                                fourCanClick = false;
                                resetTip(3, false);
                                popularize_rule(one, 0);
                            }
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        twoAdapter = new SSS_Adapter<PopularizeCateModel>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final PopularizeCateModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean.name);
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        two = bean.cate_id;
                        etTwo.setText(bean.name);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                        popularize_rule(two, 0);
                        if (isCoupon) {
                            coupon_id = bean.coupon_id;
                            if ("1".equals(bean.scope)) {//1全场，2品类，3单品
                                etInclude.setText("全场");
                                etGoods.setText(getIntent().getExtras().getString("title"));
                            } else if ("2".equals(bean.scope)) {//1全场，2品类，3单品
                                etInclude.setText("品类");
                                etGoods.setText(getIntent().getExtras().getString("title"));
                                tipGoods.setTextColor(getResources().getColor(R.color.edittext_stop));
                                etGoods.setTextColor(getResources().getColor(R.color.edittext_stop));
                            } else if ("3".equals(bean.scope)) {//1全场，2品类，3单品
                                etInclude.setText("单品");
                                etGoods.setText(getIntent().getExtras().getString("classify_name"));
                                tipGoods.setTextColor(getResources().getColor(R.color.edittext_stop));
                                etGoods.setTextColor(getResources().getColor(R.color.edittext_stop));
                            }
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        cityAdapter = new SSS_Adapter<PopularizeCityModel>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, final PopularizeCityModel bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean.getName());
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        city_id = bean.getId();
                        etCity.setText(bean.getName());
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };

        sexAdapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_popularize_text) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final String bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_popularize, bean);
                helper.getView(R.id.text_item_popularize).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sex = String.valueOf(position);
                        etSex.setText(bean);
                        if (bottomSheetDialog != null) {
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
    }

    /**
     * 获取推广详情信息
     */
    public void get_info(final String popularize_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_info(
                    new JSONObject()
                            .put("popularize_id", popularize_id)
                            .put("member_id", Config.member_id)
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
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    PopularizeEdit.this.popularize_id = jsonObject1.getString("popularize_id");//推广ID
                                    PopularizeEdit.this.coupon_id = jsonObject1.getString("coupon_id");//优惠券ID
                                    PopularizeEdit.this.subject_id = jsonObject1.getString("subject_id"); //活动ID
                                    PopularizeEdit.this.city_id = jsonObject1.getString("city_id"); //城市ID
                                    etCity.setText(jsonObject1.getString("city_name"));
                                    PopularizeEdit.this.sex = jsonObject1.getString("sex"); //性别
                                    if ("0".equals(PopularizeEdit.this.sex)) {
                                        etSex.setText("男");
                                    } else if ("1".equals(PopularizeEdit.this.sex)) {
                                        etSex.setText("女");
                                    } else if ("2".equals(PopularizeEdit.this.sex)) {
                                        etSex.setText("保密");
                                    }

                                    PopularizeEdit.this.startTime = jsonObject1.getString("start_time");//开始时间
                                    PopularizeEdit.this.endTime = jsonObject1.getString("end_time");//结束时间
                                    etTime.setText(PopularizeEdit.this.startTime + "-" + PopularizeEdit.this.endTime);
                                    PopularizeEdit.this.number = jsonObject1.getString("number");//数量
                                    etNumber.setText(PopularizeEdit.this.number);
                                    PopularizeEdit.this.totalPrice = jsonObject1.getString("money");//总价
                                    etPrice.setText(jsonObject1.getString("price"));
                                    etDescribe.setText(jsonObject1.getString("slogan"));
                                    JSONArray jsonArray = jsonObject1.getJSONArray("cate_name");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        switch (i) {
                                            case 0:
                                                PopularizeEdit.this.type = jsonArray.getJSONObject(0).getString("cate_id");//一级菜单ID
                                                etType.setText(jsonArray.getJSONObject(0).getString("name"));
                                                popularize_type(null, 1);
                                                break;
                                            case 1:
                                                PopularizeEdit.this.category = jsonArray.getJSONObject(1).getString("cate_id");//二级菜单ID
                                                etCategory.setText(jsonArray.getJSONObject(1).getString("name"));
                                                threeCanClick = true;//三级菜单能否点击
                                                popularize_type(PopularizeEdit.this.type, 2);
                                                break;
                                            case 2:
                                                PopularizeEdit.this.one = jsonArray.getJSONObject(2).getString("cate_id");//三级菜单ID
                                                etOne.setText(jsonArray.getJSONObject(2).getString("name"));
                                                fourCanClick = true;//四级菜单能否点击
                                                popularize_type(PopularizeEdit.this.category, 3);
                                                break;
                                            case 3:
                                                PopularizeEdit.this.two = jsonArray.getJSONObject(3).getString("cate_id");//四级菜单ID
                                                etTwo.setText(jsonArray.getJSONObject(3).getString("name"));
                                                popularize_type(PopularizeEdit.this.one, 4);
                                                break;
                                        }
                                    }


                                    if (!"0".equals(jsonObject1.getString("city_id"))) {

                                    }


                                    switch (jsonArray.length()) {
                                        case 1:
                                            resetTip(1, false);
                                            break;
                                        case 2:
                                            resetTip(2, false);
                                            break;
                                        case 3:
                                            resetTip(3, false);
                                            break;
                                        case 4:
                                            break;
                                    }
                                    init();
                                    JSONArray picture = jsonObject1.getJSONArray("picture");
                                    for (int i = 0; i < picture.length(); i++) {
                                        temp.add(Config.url + picture.getString(i));
                                    }

                                    PopularizeEdit.this.picture.setList(temp);

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


    /**
     * 推广类型
     */
    public void popularize_type(final String cate_id, final int where) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_type(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (where == 1) {
                                        typeList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            typeList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                        }
                                        typeAdapter.setList(typeList);
                                    } else if (where == 2) {
                                        categoryList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            categoryList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                        }
                                        categoryAdapter.setList(categoryList);
                                        twoCanClick = jsonArray.length() > 0;
                                    } else if (where == 3) {
                                        oneList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            oneList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                        }
                                        oneAdapter.setList(oneList);
                                    } else if (where == 4) {
                                        twoList.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            twoList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                        }
                                        twoAdapter.setList(twoList);
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

    /**
     * 推广类型_活动
     */
    public void popularize_type_subject(final String cate_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_type_subject(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    oneList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        oneList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                    }
                                    oneAdapter.setList(oneList);
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

    /**
     * 推广类型_优惠券
     */
    public void popularize_type_coupon(final String cate_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_type_coupon(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    twoList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        twoList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCateModel.class));
                                    }
                                    twoAdapter.setList(twoList);
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

    /**
     * 推广类型_城市
     */
    public void popularize_city() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_city(
                    new JSONObject()
                            .put("member_id", Config.member_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        cityList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), PopularizeCityModel.class));
                                    }
                                    cityAdapter.setList(cityList);
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


    /**
     * 推广类型规则
     */
    public void popularize_rule(final String cate_id, final int is_activity) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_rule(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("member_id", Config.member_id)
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
                                    popularize_amount(cate_id, is_activity);
                                    popularize_charge(cate_id, startTime, endTime, number);
                                    showData(jsonObject.getJSONObject("data"));
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


    private void showData(JSONObject jsonObject) throws JSONException {
        if ("1".equals(jsonObject.getString("scope"))) {
            tipInclude.setTextColor(getResources().getColor(R.color.black));
            etInclude.setOnClickListener(this);
        } else if ("0".equals(jsonObject.getString("scope"))) {
            tipInclude.setTextColor(getResources().getColor(R.color.edittext_stop));
            etInclude.setOnClickListener(null);
        }

        if ("1".equals(jsonObject.getString("city"))) {
            tipCity.setTextColor(getResources().getColor(R.color.black));
            etCity.setOnClickListener(this);
        } else if ("0".equals(jsonObject.getString("city"))) {
            tipCity.setTextColor(getResources().getColor(R.color.edittext_stop));
            etCity.setOnClickListener(null);
        }

        if ("1".equals(jsonObject.getString("city"))) {
            tipCity.setTextColor(getResources().getColor(R.color.black));
            etCity.setOnClickListener(this);
        } else if ("0".equals(jsonObject.getString("city"))) {
            tipCity.setTextColor(getResources().getColor(R.color.edittext_stop));
            etCity.setOnClickListener(null);
        }


        if ("1".equals(jsonObject.getString("sex"))) {
            tipSex.setTextColor(getResources().getColor(R.color.black));
            etSex.setOnClickListener(this);
        } else if ("0".equals(jsonObject.getString("sex"))) {
            tipSex.setTextColor(getResources().getColor(R.color.edittext_stop));
            etSex.setOnClickListener(null);
        }


        if ("1".equals(jsonObject.getString("price"))) {
            tipPrice.setTextColor(getResources().getColor(R.color.black));
            etPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if ("0".equals(jsonObject.getString("price"))) {
            tipPrice.setTextColor(getResources().getColor(R.color.edittext_stop));
            etPrice.setInputType(InputType.TYPE_NULL);
        }


        if ("1".equals(jsonObject.getString("number"))) {
            tipNumber.setTextColor(getResources().getColor(R.color.black));
            etNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if ("0".equals(jsonObject.getString("number"))) {
            tipNumber.setTextColor(getResources().getColor(R.color.edittext_stop));
            etNumber.setInputType(InputType.TYPE_NULL);
        }


        if ("1".equals(jsonObject.getString("time"))) {
            tipTime.setTextColor(getResources().getColor(R.color.black));
            etTime.setOnClickListener(this);
        } else if ("0".equals(jsonObject.getString("time"))) {
            tipTime.setTextColor(getResources().getColor(R.color.edittext_stop));
            etTime.setOnClickListener(null);
        }


        if ("1".equals(jsonObject.getString("picture"))) {
            tipPicture.setTextColor(getResources().getColor(R.color.black));
            picture.setCanOperation(true);
            temp.clear();
            temp.add(GalleryHorizontalListView.Holder);
            picture.setList(temp);
        } else if ("0".equals(jsonObject.getString("picture"))) {
            tipPicture.setTextColor(getResources().getColor(R.color.edittext_stop));
            picture.setCanOperation(false);
            temp.clear();
            temp.add(GalleryHorizontalListView.Holder);
            picture.setList(temp);
        }

        if ("1".equals(jsonObject.getString("remark"))) {
            tipDescribe.setTextColor(getResources().getColor(R.color.black));
            etDescribe.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if ("0".equals(jsonObject.getString("remark"))) {
            tipDescribe.setTextColor(getResources().getColor(R.color.edittext_stop));
            etDescribe.setInputType(InputType.TYPE_NULL);
        }

    }


    /**
     * 是否还有推广位置
     */
    public void popularize_amount(final String cate_id, int is_activity) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_amount(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("type", is_activity)
                            .put("member_id", Config.member_id)
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        submit.setBackgroundColor(getResources().getColor(R.color.mainColor));
                                        submit.setOnClickListener(PopularizeEdit.this);
                                    } else {
                                        submit.setBackgroundColor(getResources().getColor(R.color.grayness));
                                        submit.setOnClickListener(null);
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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

    /**
     * 推广费用
     */
    public void popularize_charge(final String cate_id, String start_time, String end_time, String amount) {
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_charge(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("start_time", start_time)
                            .put("end_time", end_time)
                            .put("amount", amount)
                            .put("member_id", Config.member_id)
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
                                    totalPrice = jsonObject.getJSONObject("data").getString("money");
                                    tvTotalPrice.setText("¥" + totalPrice);
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

    /**
     * 推广/保存
     *
     * @param status 保存草稿箱参数必传4，发布可以不传
     */
    public void popularize_into(final String status) {
        String cate_id = null;
        if (!"4".equals(status)) {
            if (!StringUtils.isEmpty(two)) {
                cate_id = two;
            } else if (!StringUtils.isEmpty(one)) {
                cate_id = one;
            } else if (!StringUtils.isEmpty(category)) {
                cate_id = category;
            } else if (!StringUtils.isEmpty(type)) {
                cate_id = type;
            } /*else {
                ToastUtils.showShortToast(getBaseActivityContext(), "请选择推广类型");
                return;
            }*/
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < picture.getList().size(); i++) {
            LogUtils.e(picture.getList().get(i));
            jsonArray.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(picture.getList().get(i), getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())));
        }


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.popularize_into(
                    new JSONObject()
                            .put("cate_id", cate_id)
                            .put("start_time", startTime)
                            .put("end_time", endTime)
                            .put("coupon_id", coupon_id)
                            .put("goods_id", getIntent().getExtras().getString("goods_id"))
                            .put("subject_id", subject_id)
                            .put("city_id", city_id)
                            .put("sex", sex)
                            .put("price", totalPrice)
                            .put("number", number)
                            .put("slogan", etDescribe.getText().toString().trim())
                            .put("status", status)
                            .put("picture", jsonArray)
                            .put("popularize_id", popularize_id)
                            .put("member_id", Config.member_id)
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
                                    if (!"4".equals(status)) {
                                        PayUtils.requestPayment(ywLoadingDialog, "0", jsonObject.getJSONObject("data").getString("popularize_id"), 4, 1, totalPrice, getBaseActivity());
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
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

    /**
     * 提示
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
                            .put("article_id", "15")
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
                                    tvTip.setText(jsonObject.getJSONObject("data").getString("contents"));
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

    @Override
    public void onClickImage(SimpleDraweeView simpleDraweeView, int position, List<String> list) {
        startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                .putStringArrayListExtra("data", (ArrayList<String>) list)
                .putExtra("current", position));
    }

    @Override
    public void onClose(int position, List<String> list) {
        // TODO: 不做任何操作，内部已经帮忙处理掉了2018/1/18
    }
}
