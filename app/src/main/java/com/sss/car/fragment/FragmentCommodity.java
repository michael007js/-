package com.sss.car.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.Fragment.BaseFragment;
import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.application.UtilCodeApplication;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.PhotoSelectView.PhotoSelect;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.model.CommodityAddGoodsTypeModelOne;
import com.sss.car.model.CommodityAddGoodsTypeModelThree;
import com.sss.car.model.CommodityAddGoodsTypeModelTwo;
import com.sss.car.model.CommodityEditModel;
import com.sss.car.view.ActivityImages;
import com.sss.car.view.CommodityAddSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

import static com.blankj.utilcode.util.ConvertUtils.bitmapToBase64;


/**
 * 商品上架Fragment
 * Created by leilei on 2017/10/26.
 */

@SuppressLint("ValidFragment")
public class FragmentCommodity extends BaseFragment {
    public static final int LAUNCH_MODE_PUBLISH = -1;
    public static final int LAUNCH_MODE_EDIT = -2;
    @BindView(R.id.cb_new_fragment_commodity)
    CheckBox cbNewFragmentCommodity;
    @BindView(R.id.cb_old_fragment_commodity)
    CheckBox cbOldFragmentCommodity;
    @BindView(R.id.spinner_one_fragment_commodity)
    Spinner spinnerOneFragmentCommodity;
    @BindView(R.id.spinner_two_fragment_commodity)
    Spinner spinnerTwoFragmentCommodity;
    @BindView(R.id.spinner_three_fragment_commodity)
    Spinner spinnerThreeFragmentCommodity;
    @BindView(R.id.et_title_fragment_commodity)
    EditText etTitleFragmentCommodity;
    @BindView(R.id.et_peddle_fragment_commodity)
    EditText etPeddleFragmentCommodity;
    @BindView(R.id.tv_specifications_fragment_commodity)
    public TextView tvSpecificationsFragmentCommodity;
    @BindView(R.id.et_price_fragment_commodity)
    EditText etPriceFragmentCommodity;
    @BindView(R.id.et_repertory_fragment_commodity)
    EditText etRepertoryFragmentCommodity;
    @BindView(R.id.ps_main_pic_fragment_commodity)
    SimpleDraweeView main_pic;
    @BindView(R.id.ps_pic_fragment_commodity)
    PhotoSelect psPicFragmentCommodity;
    @BindView(R.id.ps_pic_text_fragment_commodity)
    PhotoSelect psPicTextFragmentCommodity;
    @BindView(R.id.tv_view_fragment_commodity)
    TextView tvViewFragmentCommodity;
    @BindView(R.id.tv_tip_fragment_commodity)
    TextView tvTipFragmentCommodity;
    Unbinder unbinder;

    @BindView(R.id.scrollview_fragment_commodity)
    ScrollView scrollviewFragmentCommodity;
    List<String> imageList = new ArrayList<>();
    List<String> imageTextList = new ArrayList<>();
    List<CommodityAddGoodsTypeModelOne> commodityAddGoodsTypeModelOneList = new ArrayList<>();
    @BindView(R.id.parent_specifications_fragment_commodity)
    LinearLayout parentSpecificationsFragmentCommodity;
    @BindView(R.id.parent_price_fragment_commodity)
    LinearLayout parentPriceFragmentCommodity;


    public JSONArray sizeName, sizeData;
    String type = "2";
    String master_map;
    String status = "0";
    String goods_id;
    int mode = LAUNCH_MODE_PUBLISH;//publish:发布edit:编辑
    @BindView(R.id.parent_repertory_fragment_commodity)
    LinearLayout parentRepertoryFragmentCommodity;

    CommodityEditModel commodityEditModel = new CommodityEditModel();
    @BindView(R.id.parent_type)
    LinearLayout parentType;

    public FragmentCommodity() {
    }

    /**
     * 发布调用
     *
     * @param type
     * @param mode
     */
    public FragmentCommodity(String type, int mode) {
        this.type = type;
        this.mode = mode;
    }

    /**
     * 编辑调用
     *
     * @param type
     * @param mode
     * @param goods_id
     */
    public FragmentCommodity(String type, int mode, String goods_id) {
        this.type = type;
        this.mode = mode;
        this.goods_id = goods_id;

    }

    YWLoadingDialog ywLoadingDialog;

    @Override
    public void onDestroy() {
        if (commodityAddGoodsTypeModelOneList != null) {
            commodityAddGoodsTypeModelOneList.clear();
        }
        commodityAddGoodsTypeModelOneList = null;
        if (imageList != null) {
            imageList.clear();
        }
        imageList = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        main_pic = null;
        if (psPicFragmentCommodity != null) {
            psPicFragmentCommodity.clear();
        }
        psPicFragmentCommodity = null;
        if (psPicTextFragmentCommodity != null) {
            psPicTextFragmentCommodity.clear();
        }
        psPicTextFragmentCommodity = null;
        cbNewFragmentCommodity = null;
        cbOldFragmentCommodity = null;
        spinnerOneFragmentCommodity = null;
        spinnerTwoFragmentCommodity = null;
        spinnerThreeFragmentCommodity = null;
        etTitleFragmentCommodity = null;
        etPeddleFragmentCommodity = null;
        tvSpecificationsFragmentCommodity = null;
        etPriceFragmentCommodity = null;
        etRepertoryFragmentCommodity = null;
        tvViewFragmentCommodity = null;
        tvTipFragmentCommodity = null;
        super.onDestroy();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_commodity;
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
                                if (parentType != null) {
                                    if ("1".equals(type)) {
                                        parentType.setVisibility(View.VISIBLE);
                                    } else {
                                        parentType.setVisibility(View.GONE);
                                    }
                                }



                                tip();
                                if (mode == LAUNCH_MODE_PUBLISH) {
                                    //无
                                } else if (mode == LAUNCH_MODE_EDIT) {
                                    update_goods();
                                }
                                get_classify();
                                init();
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

    public void setData(JSONArray sizeName, JSONArray sizeData) {

        this.sizeName = sizeName;
        this.sizeData = sizeData;
        tvSpecificationsFragmentCommodity.setText("已设置");
    }

    void init() {
        if ("1".equals(type)) {
            spinnerThreeFragmentCommodity.setVisibility(View.GONE);
        } else {
            spinnerThreeFragmentCommodity.setVisibility(View.VISIBLE);
        }


/************************************************CheckBox****************************************************************/
        cbNewFragmentCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbNewFragmentCommodity.setChecked(true);
                cbOldFragmentCommodity.setChecked(false);
            }
        });
        cbOldFragmentCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOldFragmentCommodity.setChecked(true);
                cbNewFragmentCommodity.setChecked(false);
            }
        });

/************************************************产品主图****************************************************************/
        addImageViewList(FrescoUtils.showImage(false,
                140, 140,
                Uri.parse("res://" + AppUtils.getAppPackageName(getBaseFragmentActivityContext()) + "/" + com.blankj.utilcode.R.mipmap.photo_select_add_image),
                main_pic,
                0f));
        main_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APPOftenUtils.createPhotoChooseDialog(0, 1, getBaseFragmentActivityContext(), UtilCodeApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList == null || resultList.size() == 0) {
                            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "图片未选择");
                            return;
                        }

                        for (int j = 0; j < resultList.size(); j++) {
                            master_map = resultList.get(j).getPhotoPath();
                            addImageViewList(FrescoUtils.showImage(false,
                                    140, 140,
                                    Uri.fromFile(new File(resultList.get(j).getPhotoPath())),
                                    main_pic,
                                    0f));
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        ToastUtils.showLongToast(getBaseFragmentActivityContext(), errorMsg);
                    }
                });
            }
        });
/************************************************产品图片****************************************************************/
        imageList.add(PhotoSelect.HOLD_STRING);
        psPicFragmentCommodity.init(getBaseFragmentActivityContext(), 5, 140, 140, false, true, imageList, new LoadImageCallBack() {
            @Override
            public void onLoad(ImageView imageView) {
                addImageViewList(imageView);
            }
        });
        psPicFragmentCommodity.setPhotoSelectCallBack(new PhotoSelect.PhotoSelectCallBack() {
            @Override
            public void onClickFromPic(List<String> list, int position) {
                if (getBaseFragmentActivityContext() != null) {
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                            .putStringArrayListExtra("data", (ArrayList<String>) list)
                            .putExtra("current", position));
                }
            }

            @Override
            public void onClosePic(int position) {

            }

            @Override
            public void PhotoFail(int requestCode, String errorMsg) {
                ToastUtils.showLongToast(getBaseFragmentActivityContext(), errorMsg);

            }
        });
/***************************************************图文详情*************************************************************/
        imageTextList.add(PhotoSelect.HOLD_STRING);
        psPicTextFragmentCommodity.init(getBaseFragmentActivityContext(), 5, 140, 140, false, true, imageTextList, new LoadImageCallBack() {
            @Override
            public void onLoad(ImageView imageView) {
                addImageViewList(imageView);
            }
        });
        psPicTextFragmentCommodity.setPhotoSelectCallBack(new PhotoSelect.PhotoSelectCallBack() {
            @Override
            public void onClickFromPic(List<String> urlList, int position) {

                if (getBaseFragmentActivityContext() != null) {
                    List<String> temp=new ArrayList<>();
                    for (int i = 0; i < urlList.size(); i++) {
                        temp.add(Config.url+urlList.get(i));
                    }
                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                            .putStringArrayListExtra("data", (ArrayList<String>) temp)
                            .putExtra("current", position));
                }
            }

            @Override
            public void onClosePic(int position) {

            }

            @Override
            public void PhotoFail(int requestCode, String errorMsg) {
                ToastUtils.showLongToast(getBaseFragmentActivityContext(), errorMsg);

            }
        });


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

    @OnClick({R.id.tv_specifications_fragment_commodity, R.id.tv_tip_fragment_commodity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_specifications_fragment_commodity:
                if (!StringUtils.isEmpty(classify_id)) {
                    if (getBaseFragmentActivityContext() != null) {
                        startActivity(new Intent(getBaseFragmentActivityContext(), CommodityAddSize.class)
                                .putExtra("classify_id", classify_id)
                                .putExtra("type", type));
                    }
                }
                break;
            case R.id.tv_tip_fragment_commodity:
                break;
        }
    }

    /**
     * 保存
     */
    public void save_goods() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        final String mainPic = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(master_map, 480, 960));


        final JSONArray picture = new JSONArray();
        for (int i = 0; i < psPicFragmentCommodity.getPhotoList().size(); i++) {
            LogUtils.e(psPicFragmentCommodity.getPhotoList().get(i));
            if (psPicFragmentCommodity.getPhotoList().get(i).startsWith("/storage/")) {
                picture.put(bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(psPicFragmentCommodity.getPhotoList().get(i), 480, 960)));
            } else {
                GlidUtils.downloadBitmap(psPicFragmentCommodity.getPhotoList().get(i), getBaseFragmentActivityContext(), new GlidUtils.GlidUtilsCallBack() {
                    @Override
                    public void onBitmap(Bitmap bitmap) {
                        picture.put(bitmapToBase64(bitmap));
                    }
                });

            }
        }

        final JSONArray photo = new JSONArray();
        for (int i = 0; i < psPicTextFragmentCommodity.getPhotoList().size(); i++) {
            if (psPicTextFragmentCommodity.getPhotoList().get(i).startsWith("/storage/")) {
                photo.put(bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(psPicTextFragmentCommodity.getPhotoList().get(i), 480, 960)));
            } else {
                GlidUtils.downloadBitmap(psPicTextFragmentCommodity.getPhotoList().get(i), getBaseFragmentActivityContext(), new GlidUtils.GlidUtilsCallBack() {
                    @Override
                    public void onBitmap(Bitmap bitmap) {
                        photo.put(bitmapToBase64(bitmap));
                    }
                });

            }
        }
        String goods_type = null;
        if (cbNewFragmentCommodity.isChecked()) {
            goods_type = "1";
        } else if (cbOldFragmentCommodity.isChecked()) {
            goods_type = "2";
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.save_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("classify_id", classify_id)
                            .put("title", etTitleFragmentCommodity.getText().toString().trim())
                            .put("slogan", etPeddleFragmentCommodity.getText().toString().trim())
                            .put("price", etPriceFragmentCommodity.getText().toString().trim())
                            .put("number", etRepertoryFragmentCommodity.getText().toString().trim())
                            .put("size_name", sizeName)
                            .put("size_data", sizeData)
                            .put("master_map", mainPic)
                            .put("picture", picture)
                            .put("goods_type", goods_type)
                            .put("type", type)
                            .put("photo", photo)
                            .put("status", status)
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
                                    ToastUtils.showLongToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    getActivity().finish();
                                } else {
                                    ToastUtils.showLongToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }


    }

    /**
     * 发布/更新
     */
    public void addAndUpdate() {

        if (StringUtils.isEmpty(classify_id)) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "信息刷新中,请稍后...");
            return;
        }
//        if (!cbOldFragmentCommodity.isChecked() && !cbNewFragmentCommodity.isChecked()) {
//            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请设置产品类型");
//            return;
//        }
        if (StringUtils.isEmpty(etTitleFragmentCommodity.getText().toString().trim())) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请填写标题");
            return;
        }
        if (StringUtils.isEmpty(etPeddleFragmentCommodity.getText().toString().trim())) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请填写卖点");
            return;
        }
        if (StringUtils.isEmpty(master_map)) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请设置产品主图");
            return;
        }
        if (psPicFragmentCommodity.getPhotoList().size() == 0) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请设置产品图片");
            return;
        }
        if (psPicTextFragmentCommodity.getPhotoList().size() == 0) {
            ToastUtils.showLongToast(getBaseFragmentActivityContext(), "请设置产品图文详情");
            return;
        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        final String[] mainPic = {null};
        if (master_map.startsWith("/storage/")) {
            mainPic[0] = ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(master_map, 480, 960));
        } else {
            GlidUtils.downloadBitmap(master_map, getBaseFragmentActivityContext(), new GlidUtils.GlidUtilsCallBack() {
                @Override
                public void onBitmap(Bitmap bitmap) {
                    mainPic[0] = bitmapToBase64(bitmap);
                    LogUtils.e("sss" + mainPic[0] + TimeUtils.millis2String(System.currentTimeMillis()));
                }
            });
        }
        final JSONArray picture = new JSONArray();
        for (int i = 0; i < psPicFragmentCommodity.getPhotoList().size(); i++) {
            if (psPicFragmentCommodity.getPhotoList().get(i).startsWith("/storage/")) {
                picture.put(bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(psPicFragmentCommodity.getPhotoList().get(i), 480, 960)));
            } else {
                GlidUtils.downloadBitmap(psPicFragmentCommodity.getPhotoList().get(i), getBaseFragmentActivityContext(), new GlidUtils.GlidUtilsCallBack() {
                    @Override
                    public void onBitmap(Bitmap bitmap) {
                        picture.put(bitmapToBase64(bitmap));
                    }
                });

            }
        }

        final JSONArray photo = new JSONArray();
        for (int i = 0; i < psPicTextFragmentCommodity.getPhotoList().size(); i++) {
            if (psPicTextFragmentCommodity.getPhotoList().get(i).startsWith("/storage/")) {
                photo.put(bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(psPicTextFragmentCommodity.getPhotoList().get(i), 480, 960)));
            } else {
                GlidUtils.downloadBitmap(psPicTextFragmentCommodity.getPhotoList().get(i), getBaseFragmentActivityContext(), new GlidUtils.GlidUtilsCallBack() {
                    @Override
                    public void onBitmap(Bitmap bitmap) {
                        photo.put(bitmapToBase64(bitmap));
                    }
                });

            }
        }
        String goods_type = null;
        if (cbNewFragmentCommodity.isChecked()) {
            goods_type = "0";
        } else if (cbOldFragmentCommodity.isChecked()) {
            goods_type = "1";
        }
        LogUtils.e("sss" + mainPic[0] + TimeUtils.millis2String(System.currentTimeMillis()));
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_goods(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("classify_id", classify_id)
                            .put("title", etTitleFragmentCommodity.getText().toString().trim())
                            .put("slogan", etPeddleFragmentCommodity.getText().toString().trim())
                            .put("price", etPriceFragmentCommodity.getText().toString().trim())
                            .put("number", etRepertoryFragmentCommodity.getText().toString().trim())
                            .put("size_name", sizeName)
                            .put("size_data", sizeData)
                            .put("goods_id", goods_id)
                            .put("master_map", mainPic[0])
                            .put("picture", picture)
                            .put("goods_type", goods_type)
                            .put("type", type)
                            .put("photo", photo)
                            .put("status", status)
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
                                    ToastUtils.showLongToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                    getActivity().finish();
                                } else {
                                    ToastUtils.showLongToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
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
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
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
                                    commodityEditModel.goods_id = jsonObject.getJSONObject("data").getString("goods_id");
                                    commodityEditModel.title = jsonObject.getJSONObject("data").getString("title");
                                    commodityEditModel.slogan = jsonObject.getJSONObject("data").getString("slogan");
                                    commodityEditModel.master_map = jsonObject.getJSONObject("data").getString("master_map");
                                    commodityEditModel.price = jsonObject.getJSONObject("data").getString("price");
                                    commodityEditModel.number = jsonObject.getJSONObject("data").getString("number");
                                    commodityEditModel.goods_type = jsonObject.getJSONObject("data").getString("goods_type");
                                    commodityEditModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    commodityEditModel.type = jsonObject.getJSONObject("data").getString("type");
                                    sizeName = jsonObject.getJSONObject("data").getJSONArray("size_name");
                                    sizeData = jsonObject.getJSONObject("data").getJSONArray("size_data");
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("photo");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        imageList.add(Config.url + jsonArray.getString(i));
                                    }
                                    JSONArray jsonArray2 = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    for (int i = 0; i < jsonArray2.length(); i++) {
                                        imageTextList.add(Config.url + jsonArray2.getString(i));
                                    }
                                    List<String> classify_id = new ArrayList<>();

                                    JSONArray jsonArray3 = jsonObject.getJSONObject("data").getJSONArray("classify_id");
                                    for (int i = 0; i < jsonArray3.length(); i++) {
                                        classify_id.add(jsonArray3.getString(i));
                                    }
                                    commodityEditModel.classify_id = classify_id;
                                    showData();
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    void showData() {
        if ("0".equals(commodityEditModel.goods_type)) {//0全新1二手
            cbNewFragmentCommodity.setChecked(true);
        } else if ("1".equals(commodityEditModel.goods_type)) {//0全新1二手
            cbOldFragmentCommodity.setChecked(true);
        }
        etTitleFragmentCommodity.setText(commodityEditModel.title);
        etPeddleFragmentCommodity.setText(commodityEditModel.slogan);
        etPriceFragmentCommodity.setText(commodityEditModel.price);
        etRepertoryFragmentCommodity.setText(commodityEditModel.number);
        if (sizeName.length() > 0) {
            tvSpecificationsFragmentCommodity.setText("已设置");
        }
        if (!StringUtils.isEmpty(commodityEditModel.master_map)) {
            addImageViewList(FrescoUtils.showImage(false,
                    140, 140,
                    Uri.parse(Config.url + commodityEditModel.master_map),
                    main_pic,
                    0f));
        }
        master_map = Config.url + commodityEditModel.master_map;
        psPicFragmentCommodity.setList(imageList);
        psPicTextFragmentCommodity.setList(imageTextList);

    }


    /**
     * 获取订单提示
     */
    public void tip() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.orderTip(
                    new JSONObject()
                            .put("article_id", "13")//文章ID (3实物类)
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
                                    tvTipFragmentCommodity.setText(jsonObject.getJSONObject("data").getString("contents"));
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的商品==>添加商品==>获取商品规格
     */
    public void classify_size(final String classify_id) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.classify_size(
                    new JSONObject()
                            .put("classify_id", classify_id)
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
                            FragmentCommodity.this.classify_id = classify_id;
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    parentSpecificationsFragmentCommodity.setVisibility(View.VISIBLE);
                                    parentPriceFragmentCommodity.setVisibility(View.GONE);
                                    parentRepertoryFragmentCommodity.setVisibility(View.GONE);
                                } else {
                                    parentSpecificationsFragmentCommodity.setVisibility(View.GONE);
                                    parentPriceFragmentCommodity.setVisibility(View.VISIBLE);
                                    parentRepertoryFragmentCommodity.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    /**
     * 我的商品==>添加商品==>获取商品类别信息
     */
    void get_classify() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseFragmentActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.get_classify(
                    new JSONObject()
                            .put("type", type)
                            .toString()
                    , new StringCallback() {
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
                                    commodityAddGoodsTypeModelOneList.add(new CommodityAddGoodsTypeModelOne("", "请选择", ""));
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        CommodityAddGoodsTypeModelOne one = new CommodityAddGoodsTypeModelOne();
                                        one.classify_id = jsonArray.getJSONObject(i).getString("classify_id");
                                        one.name = jsonArray.getJSONObject(i).getString("name");
                                        one.parent_id = jsonArray.getJSONObject(i).getString("parent_id");
                                        List<CommodityAddGoodsTypeModelTwo> commodityAddGoodsTypeModelTwoList = new ArrayList<>();
                                        commodityAddGoodsTypeModelTwoList.add(new CommodityAddGoodsTypeModelTwo("", "请选择", ""));
                                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("subclass");
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            CommodityAddGoodsTypeModelTwo two = new CommodityAddGoodsTypeModelTwo();
                                            two.classify_id = jsonArray1.getJSONObject(j).getString("classify_id");
                                            two.name = jsonArray1.getJSONObject(j).getString("name");
                                            two.parent_id = jsonArray1.getJSONObject(j).getString("parent_id");
                                            JSONArray jsonArray2 = jsonArray1.getJSONObject(j).getJSONArray("subclass");
                                            List<CommodityAddGoodsTypeModelThree> commodityAddGoodsTypeModelThreeList = new ArrayList<>();
                                            commodityAddGoodsTypeModelThreeList.add(new CommodityAddGoodsTypeModelThree("", "请选择", ""));
                                            for (int k = 0; k < jsonArray2.length(); k++) {
                                                CommodityAddGoodsTypeModelThree three = new CommodityAddGoodsTypeModelThree();
                                                three.classify_id = jsonArray2.getJSONObject(k).getString("classify_id");
                                                three.name = jsonArray2.getJSONObject(k).getString("name");
                                                three.parent_id = jsonArray2.getJSONObject(k).getString("parent_id");
                                                commodityAddGoodsTypeModelThreeList.add(three);
                                            }
                                            two.three = commodityAddGoodsTypeModelThreeList;
                                            commodityAddGoodsTypeModelTwoList.add(two);
                                        }
                                        one.two = commodityAddGoodsTypeModelTwoList;
                                        commodityAddGoodsTypeModelOneList.add(one);
                                    }
                                    if (commodityAddGoodsTypeModelOneList.size() > 0) {
                                        initAdapter();
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseFragmentActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseFragmentActivityContext(), "数据解析错误Err:-0");
            e.printStackTrace();
        }
    }

    SSS_Adapter sss_adapterOne;
    SSS_Adapter sss_adapterTwo;
    SSS_Adapter sss_adapterThree;

    int a, b, c = 0;
    String classify_id;

    void initAdapter() {
        sss_adapterOne = new SSS_Adapter<CommodityAddGoodsTypeModelOne>(getBaseFragmentActivityContext(), R.layout.item_spinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CommodityAddGoodsTypeModelOne bean, SSS_Adapter instance) {

                helper.setText(R.id.text_item_spinner, bean.name);

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };


        sss_adapterTwo = new SSS_Adapter<CommodityAddGoodsTypeModelTwo>(getBaseFragmentActivityContext(), R.layout.item_spinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CommodityAddGoodsTypeModelTwo bean, SSS_Adapter instance) {
                helper.setText(R.id.text_item_spinner, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };


        sss_adapterThree = new SSS_Adapter<CommodityAddGoodsTypeModelThree>(getBaseFragmentActivityContext(), R.layout.item_spinner) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, CommodityAddGoodsTypeModelThree bean, SSS_Adapter instance) {
                c = position;
                helper.setText(R.id.text_item_spinner, bean.name);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };


        spinnerOneFragmentCommodity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = position;
                b = 0;
                c = 0;
                sss_adapterTwo.setList(commodityAddGoodsTypeModelOneList.get(a).two);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerTwoFragmentCommodity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                b = position;
                c = 0;

                if ("1".equals(type)) {
                    if (!"请选择".equals(commodityAddGoodsTypeModelOneList.get(a).two.get(b).name)) {
                        classify_size(commodityAddGoodsTypeModelOneList.get(a).two.get(b).classify_id);
                    }
                } else {
                    sss_adapterThree.setList(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerThreeFragmentCommodity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c = position;
                if (commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.size() > 0) {
                    if (!"请选择".equals(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.get(c).name)) {
                        classify_size(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.get(c).classify_id);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOneFragmentCommodity.setAdapter(sss_adapterOne);
        spinnerTwoFragmentCommodity.setAdapter(sss_adapterTwo);
        spinnerThreeFragmentCommodity.setAdapter(sss_adapterThree);
        sss_adapterOne.setList(commodityAddGoodsTypeModelOneList);
        if (mode == FragmentCommodity.LAUNCH_MODE_PUBLISH) {
            spinnerOneFragmentCommodity.setSelection(0);
        } else if (mode == FragmentCommodity.LAUNCH_MODE_EDIT) {
            if (commodityEditModel.classify_id.size() > 1) {
                for (int i = 0; i < commodityAddGoodsTypeModelOneList.size(); i++) {
                    if (commodityEditModel.classify_id.get(1).equals(commodityAddGoodsTypeModelOneList.get(i).classify_id)) {
                        a = i;
                        spinnerOneFragmentCommodity.setSelection(a);
                        break;
                    }
                }

                for (int i = 0; i < commodityAddGoodsTypeModelOneList.get(a).two.size(); i++) {
                    LogUtils.e(commodityEditModel.classify_id.get(2) + "---" + commodityAddGoodsTypeModelOneList.get(a).two.get(i).classify_id);
                    if (commodityEditModel.classify_id.get(2).equals(commodityAddGoodsTypeModelOneList.get(a).two.get(i).classify_id)) {
                        b = i;
                        sss_adapterTwo.setList(commodityAddGoodsTypeModelOneList.get(a).two);
                        spinnerTwoFragmentCommodity.setSelection(b);
                        break;
                    }
                }

                if ("2".equals(type)) {
                    for (int i = 0; i < commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.size(); i++) {
                        if (commodityEditModel.classify_id.get(3).equals(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.get(i).classify_id)) {
                            c = i;
                            sss_adapterThree.setList(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three);
                            spinnerThreeFragmentCommodity.setSelection(c);
                            classify_size(commodityAddGoodsTypeModelOneList.get(a).two.get(b).three.get(c).classify_id);
                            break;
                        }
                    }

                }

            }
        }


    }

}
