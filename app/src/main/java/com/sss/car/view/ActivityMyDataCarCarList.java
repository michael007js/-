package com.sss.car.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.GridView.InnerGridView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.CarName;
import com.sss.car.EventBusModel.CarSearch;
import com.sss.car.EventBusModel.CreateCarModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.adapter.CarListAdapter;
import com.sss.car.adapter.ChildCarAdapter;
import com.sss.car.adapter.HotCarAdapter;
import com.sss.car.dao.MyCarOperationCallBack;
import com.sss.car.model.CarListModel;
import com.sss.car.model.ChildCarModel;
import com.sss.car.model.HotCarModel;

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
import okhttp3.Call;


/**
 * Created by leilei on 2017/8/16.
 */

public class ActivityMyDataCarCarList extends BaseActivity implements MyCarOperationCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;

    @BindView(R.id.list_activity_my_data_car_carlist)
    ListView listActivityMyDataCarCarlist;
    @BindView(R.id.activity_my_data_car_carlist)
    DrawerLayout activityMyDataCarCarlist;
    @BindView(R.id.search_activity_my_data_car_carlist)
    LinearLayout searchActivityMyDataCarCarlist;
    @BindView(R.id.child_car_list_activity_my_data_car_carlist)
    ListView childCarListActivityMyDataCarCarlist;
    @BindView(R.id.drawer_content_activity_my_data_car_carlist)
    LinearLayout drawerContentActivityMyDataCarCarlist;
    YWLoadingDialog ywLoadingDialog;
    List<HotCarModel> hotList = new ArrayList<>();
    HotCarAdapter hotCarAdapter;
    HotCarModel hotCarModel;

    List<CarListModel> carList = new ArrayList<>();
    CarListAdapter carListAdapter;


    InnerGridView activityMyDataCarListHead;


    LinearLayout parentHead;
    TextView title;

    ChildCarAdapter childCarAdapter;
    List<ChildCarModel> childList = new ArrayList<>();
    LinearLayout childParentHead;
    TextView childTitle;
    ImageView childLogo;
    String ids = "-1";


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (childCarAdapter != null) {
            childCarAdapter.clear();
        }
        childCarAdapter = null;
        if (childList != null) {
            childList.clear();
        }
        childList = null;
        childParentHead = null;
        childTitle = null;
        ids = null;
        childLogo = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (hotList != null) {
            hotList.clear();
        }
        hotList = null;
        if (hotCarAdapter != null) {
            hotCarAdapter.clear();
        }
        hotCarAdapter = null;
        hotCarModel = null;

        if (carListAdapter != null) {
            carListAdapter.clear();
        }
        carListAdapter = null;
        if (carList != null) {
            carList.clear();
        }
        carList = null;

        if (parentHead != null) {
            parentHead.removeAllViews();
        }
        title = null;
        parentHead = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        activityMyDataCarListHead = null;
        listActivityMyDataCarCarlist = null;
        activityMyDataCarCarlist = null;
        childCarListActivityMyDataCarCarlist = null;
        drawerContentActivityMyDataCarCarlist = null;
        searchActivityMyDataCarCarlist = null;
    }


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @OnClick({R.id.back_top, R.id.search_activity_my_data_car_carlist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.search_activity_my_data_car_carlist:
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarListSearch.class));
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarSearch event) {
        try {
            carChildList(event.brand_id, event.logo, event.name);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CarName event) {
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CreateCarModel event) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_car_carlist);
        ButterKnife.bind(this);
        customInit(activityMyDataCarCarlist, false, true, true);
        try {
            hotCar();
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:hot car-0");
            }
            e.printStackTrace();
        }
        try {
            carList();
        } catch (JSONException e) {
            if (getBaseActivityContext() != null) {
                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car-0");
            }
            e.printStackTrace();
        }
        //////////////////////////////////////////////////
        hotCarAdapter = new HotCarAdapter(hotList, getBaseActivityContext(), this, new LoadImageCallBack() {
            @Override
            public void onLoad(ImageView imageView) {
                addImageViewList(imageView);
            }
        });

        //////////////////////////////////////////////////
        carListAdapter = new CarListAdapter(carList, getBaseActivityContext(), this, new LoadImageCallBack() {
            @Override
            public void onLoad(ImageView imageView) {
                addImageViewList(imageView);
            }
        });
        listActivityMyDataCarCarlist.setAdapter(carListAdapter);
        //////////////////////////////////////////////////
        childCarAdapter = new ChildCarAdapter(childList, getBaseActivityContext(), this);
        childCarListActivityMyDataCarCarlist.setAdapter(childCarAdapter);


        activityMyDataCarCarlist.setEnabled(false);
        activityMyDataCarCarlist.setDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View arg0) {
            }

            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View arg0) {
                activityMyDataCarCarlist.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
        activityMyDataCarCarlist.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        titleTop.setText("请选择品牌");
    }


    /**
     * 车辆列表
     *
     * @throws JSONException
     */
    void carList() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.carList(
                new JSONObject().toString(), new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "服务器访问错误");
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
                            ToastUtils.showShortToast(getBaseActivityContext(), "服务器返回错误");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        CarListModel carListModel = new CarListModel();
                                        if (jsonArray.getJSONObject(i).has("title")) {
                                            carListModel.title = jsonArray.getJSONObject(i).getString("title");
                                        } else if (jsonArray.getJSONObject(i).has("name")) {
                                            carListModel.brand_id = jsonArray.getJSONObject(i).getString("brand_id");
                                            carListModel.name = jsonArray.getJSONObject(i).getString("name");
                                            carListModel.logo = jsonArray.getJSONObject(i).getString("logo");
                                        }
                                        carList.add(carListModel);
                                    }

                                    carListAdapter.refresh(carList);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car-0");
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    /**
     * 热门车辆
     *
     * @throws JSONException
     */
    void hotCar() throws JSONException {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.hotCar(
                new JSONObject().toString(), new StringCallback() {
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < 10; i++) {
                                        hotCarModel = new HotCarModel();
                                        if (i < jsonArray.length()) {
                                            hotCarModel.brand_id = jsonArray.getJSONObject(i).getString("brand_id");
                                            hotCarModel.name = jsonArray.getJSONObject(i).getString("name");
                                            hotCarModel.logo = jsonArray.getJSONObject(i).getString("logo");
                                        }
                                        hotList.add(hotCarModel);
                                    }
                                    createCarListHead();
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:hot car-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }

    /**
     * 创建车辆品牌列表头部
     */
    void createCarListHead() {
        if (getBaseActivityContext() != null) {
            parentHead = new LinearLayout(getBaseActivityContext());
            parentHead.setOrientation(OrientationHelper.VERTICAL);
            title = new TextView(getBaseActivityContext());
            title.setText("热门品牌");
            title.setBackgroundColor(Color.parseColor("#f3f3f3"));
            title.setPadding(10, 5, 5, 10);
            parentHead.addView(title);
            activityMyDataCarListHead = new InnerGridView(getBaseActivityContext());
            activityMyDataCarListHead.setNumColumns(5);
            activityMyDataCarListHead.setAdapter(hotCarAdapter);
            hotCarAdapter.refresh(hotList);
            parentHead.addView(activityMyDataCarListHead);
            listActivityMyDataCarCarlist.addHeaderView(parentHead);
        }
    }

    /**
     * 热门车型与车型列表点击回调
     *
     * @param from     从哪个列表点击
     * @param brand_id
     * @param logo
     * @param name
     */
    @Override
    public void clickCarFromHotListOrCarList(String from, String brand_id, String logo, String name) {
        ids = brand_id;
        if ("hotList".equals(from)) {//热门车型
            try {
                carChildList(brand_id, logo, name);
            } catch (JSONException e) {
                if (getBaseActivityContext() != null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car child list-0");
                }
                e.printStackTrace();
            }
        } else if ("carList".equals(from)) {//车型列表
            try {
                carChildList(brand_id, logo, name);
            } catch (JSONException e) {
                if (getBaseActivityContext() != null) {
                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car child list-0");
                }
                e.printStackTrace();
            }
        }

    }

    /**
     * 侧滑页面子品牌列表点击回调(选择车辆类型)
     *
     * @param series_id
     * @param name
     */
    @Override
    public void clickFromChildCarList(String carType, String series_id, String logo, String name) {
        if (getBaseActivityContext() != null) {
            startActivity(new Intent(getBaseActivityContext(), ActivityMyDataCarDisplacement.class)
                    .putExtra("carType", carType)
                    .putExtra("id", series_id)
                    .putExtra("ids", ids + "," + series_id)
                    .putExtra("carLabel", name)
                    .putExtra("carLogo", logo));
        }
    }

    /**
     * 获取侧滑列表(车辆子品牌)
     *
     * @throws JSONException
     */
    void carChildList(final String brand_id, final String logo, final String carType) throws JSONException {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.carChildList(
                new JSONObject()
                        .put("brand_id", brand_id)
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    childList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ChildCarModel childCarModel = new ChildCarModel();
                                        //0为标题,其余为有数据
                                        childCarModel.parent_id = jsonArray.getJSONObject(i).getString("parent_id");
                                        childCarModel.name = jsonArray.getJSONObject(i).getString("name");
                                        childCarModel.logo = logo;
                                        childCarModel.series_id = jsonArray.getJSONObject(i).getString("series_id");
                                        childList.add(childCarModel);
                                    }
                                    createChildHeadShow(logo, carType);

                                } else {
                                    if (getBaseActivityContext() != null) {
                                        ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                if (getBaseActivityContext() != null) {
                                    ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:car child list-0");
                                }
                                e.printStackTrace();
                            }
                        }
                    }

                })));
    }

    /**
     * 创建侧滑列表头部及展示服务器返回数据
     */
    void createChildHeadShow(String logo, String carType) {
        if (getBaseActivityContext() != null) {
            activityMyDataCarCarlist.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            if (!activityMyDataCarCarlist.isDrawerOpen(drawerContentActivityMyDataCarCarlist)) {
                activityMyDataCarCarlist.openDrawer(drawerContentActivityMyDataCarCarlist);
            }
            if (childParentHead == null) {
                childParentHead = new LinearLayout(getBaseActivityContext());
                childParentHead.setOrientation(OrientationHelper.VERTICAL);
                childParentHead.setGravity(Gravity.CENTER_VERTICAL);

                childTitle = new TextView(getBaseActivityContext());

                childParentHead.addView(childTitle);

                childLogo = new ImageView(getBaseActivityContext());
                childLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                childParentHead.addView(childLogo);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childLogo.getLayoutParams();
                params.width = 100;
                params.height = 100;
                childLogo.setLayoutParams(params);
                childParentHead.setPadding(30, 10, 10, 10);
                childCarListActivityMyDataCarCarlist.addHeaderView(childParentHead);
            }
            childTitle.setText(carType + "");
            childLogo.setTag(R.id.glide_tag, Config.url + logo);
            addImageViewList(GlidUtils.downLoader(false, childLogo, getBaseActivityContext()));
            childCarAdapter.refresh(carType, childList);
        }
    }


}
