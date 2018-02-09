package com.sss.car.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;

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

import static com.sss.car.Config.member_id;

/**
 * 店铺图片
 * Created by leilei on 2017/11/9.
 */

public class ActivityShopPics extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.gridview_activity_shop_pic)
    GridView gridviewActivityShopPic;
    @BindView(R.id.activity_shop_pic)
    LinearLayout activityShopPic;
    SSS_Adapter sss_adapter;
    List<String> list = new ArrayList<>();
    YWLoadingDialog ywLoadingDialog;
    String shop_id;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    boolean isChanged = false;
    int images = 0;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pics);
        ButterKnife.bind(this);
        customInit(activityShopPic, false, true, false);
        titleTop.setText("商铺图片");
        rightButtonTop.setText("保存");
        rightButtonTop.setTextColor(getResources().getColor(R.color.black));
        initAdapter();
        getshopInfo();
    }


    void initAdapter() {
        sss_adapter = new SSS_Adapter<String>(getBaseActivityContext(), R.layout.item_photo_select, list) {
            @Override
            protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {

                ((ImageView) helper.getView(R.id.pic_item_photo_select)).setLayoutParams(new RelativeLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() / 2 - 10, getWindowManager().getDefaultDisplay().getWidth() / 2 - 10));
                if (bean.startsWith("default")) {
                    helper.setVisibility(R.id.close_item_photo_select, View.GONE);
                    addImageViewList(GlidUtils.glideLoad(false, ((ImageView) helper.getView(R.id.pic_item_photo_select)), getBaseActivityContext(), R.mipmap.add_photo));
                } else {
                    helper.setVisibility(R.id.close_item_photo_select, View.VISIBLE);
                    ((ImageView) helper.getView(R.id.pic_item_photo_select)).setTag(R.id.glide_tag, bean);
                    addImageViewList(GlidUtils.downLoader(false, ((ImageView) helper.getView(R.id.pic_item_photo_select)), getBaseActivityContext()));

                }
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {
                helper.setItemChildClickListener(R.id.pic_item_photo_select);
                helper.setItemChildClickListener(R.id.close_item_photo_select);
            }
        };
        sss_adapter.setOnItemListener(new SSS_OnItemListener() {

            @Override
            public void onItemChildClick(View view, int position, SSS_HolderHelper helper) {
                switch (view.getId()) {
                    case R.id.pic_item_photo_select:
                        if (list.get(position).startsWith("default")) {
                            APPOftenUtils.createPhotoChooseDialog(0, 3, getBaseActivityContext(), MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                                @Override
                                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                    if (resultList == null || resultList.size() == 0) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "未选中图片");
                                        return;
                                    }
                                    for (int i = 0; i < list.size(); i++) {
                                        if ("default".equals(list.get(i))) {
                                            list.remove(i);
                                        }
                                    }
                                    for (int i = 0; i < resultList.size(); i++) {
                                        LogUtils.e(resultList.get(i).getPhotoPath());
                                        list.add(resultList.get(i).getPhotoPath());
                                    }
                                    list.add("default");
                                    sss_adapter.setList(list);
                                    isChanged = true;
                                }

                                @Override
                                public void onHanlderFailure(int requestCode, String errorMsg) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), errorMsg);
                                }
                            });
                        } else {
                            List<String> temp = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (!"default".equals(list.get(i))) {
                                    temp.add(list.get(i));
                                }
                            }
                            if (temp.size()>0){
                                if (getBaseActivityContext() != null) {
                                    startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) temp)
                                            .putExtra("current", position));
                                }
                            }

                        }
                        break;
                    case R.id.close_item_photo_select:
                        if (list.get(position).startsWith("http")) {
                            images--;
                        }
                        list.remove(position);
                        isChanged = true;
                        sss_adapter.setList(list);
                        break;
                }

            }
        });

        gridviewActivityShopPic.setAdapter(sss_adapter);
    }


    /*
* 设置店铺信息
*/
    void setshopInfo() {
        if (isChanged == false) {
            ToastUtils.showShortToast(getBaseActivityContext(), "您未修改任何图片");
            return;
        }

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }

        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("/storage/")) {
                jsonArray.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(list.get(i), getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight())));
            } else if (list.get(i).startsWith("http")) {
                jsonArray.put(list.get(i));
            }
        }
        send(jsonArray);
    }

    void send(JSONArray jsonArray) {
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.setShop(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("shop_id", shop_id)
                            .put("picture", jsonArray)
                            .toString(), "设置店铺图片", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }

                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                        finish();
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: set Shop-0");
            }
            e.printStackTrace();
        }
    }


    /**
     * 获取店铺信息
     */
    void getshopInfo() {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {

            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getShop(
                    new JSONObject()
                            .put("member_id", member_id)
                            .toString(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (getBaseActivityContext() != null) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
                            }
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            if (StringUtils.isEmpty(response)) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                                }
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("picture");
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                images++;
                                                list.add(Config.url + jsonArray.getString(i));
                                            }
                                        }
                                        list.add("default");
                                        sss_adapter.setList(list);
                                    } else {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }

                    })));
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err: get Shop-0");
            }
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                setshopInfo();
                break;
        }
    }
}
