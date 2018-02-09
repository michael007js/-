package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.PaymentOverForOrder;
import com.sss.car.EventBusModel.changedShopCart;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ShoppingCartView;
import com.sss.car.dao.ShoppingCartCallBack;
import com.sss.car.model.GoodsChooseModel;
import com.sss.car.model.GoodsChooseSizeData;
import com.sss.car.model.GoodsChooseSizeName;
import com.sss.car.model.GoodsChooseSizeName_Model;
import com.sss.car.model.ShoppingCart;
import com.sss.car.model.ShoppingCart_Data;
import com.sss.car.model.ShoppingCart_Data_Options;
import com.sss.car.order.OrderGoodsReadyBuy;
import com.sss.car.order.OrderServiceReadyBuy;
import com.sss.car.utils.MenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
 * 购物车列表
 * Created by leilei on 2017/9/25.
 */

public class ActivityShoppingCart extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.activity_shopping_cart)
    LinearLayout activityShoppingCart;
    YWLoadingDialog ywLoadingDialog;

    List<ShoppingCart> list = new ArrayList<>();
    List<ShoppingCart> temp = new ArrayList<>();

    @BindView(R.id.ShoppingCartView_activity_shopping_cart)
    ShoppingCartView ShoppingCartViewActivityShoppingCart;

    @BindView(R.id.all_choose_activity_shopping_cart)
    CheckBox all_choose_activity_shopping_cart;
    @BindView(R.id.total_price_choose_activity_shopping_cart_bottom_edit_mode)
    TextView totalPriceChooseActivityShoppingCartBottomEditMode;
    @BindView(R.id.ready_buy_choose_activity_shopping_cart_bottom_edit_mode)
    TextView readyBuyChooseActivityShoppingCartBottomEditMode;
    @BindView(R.id.parent_choose_activity_shopping_cart_bottom_edit_mode)
    LinearLayout parentChooseActivityShoppingCartBottomEditMode;
    @BindView(R.id.collect_choose_activity_shopping_cart_bottom_no_edit_mode)
    TextView collectChooseActivityShoppingCartBottomNoEditMode;
    @BindView(R.id.delete_choose_activity_shopping_cart_bottom_no_edit_mode)
    TextView deleteChooseActivityShoppingCartBottomNoEditMode;
    @BindView(R.id.parent_choose_activity_shopping_cart_bottom_no_edit_mode)
    LinearLayout parentChooseActivityShoppingCartBottomNoEditMode;

    GoodsChooseModel goodsChooseModel = new GoodsChooseModel();

    MenuDialog menuDialog;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Subscribe
    public void onMessageEvent(PaymentOverForOrder event){
        finish();
    }
    @Override
    protected void onDestroy() {
        if (menuDialog != null) {
            menuDialog.clear();
        }
        menuDialog = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        if (temp != null) {
            temp.clear();
        }
        temp = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        activityShoppingCart = null;
        if (ShoppingCartViewActivityShoppingCart != null) {
            ShoppingCartViewActivityShoppingCart.clear();
        }
        ShoppingCartViewActivityShoppingCart = null;

        goodsChooseModel = null;

        totalPriceChooseActivityShoppingCartBottomEditMode = null;
        readyBuyChooseActivityShoppingCartBottomEditMode = null;
        parentChooseActivityShoppingCartBottomEditMode = null;
        all_choose_activity_shopping_cart = null;
        collectChooseActivityShoppingCartBottomNoEditMode = null;
        deleteChooseActivityShoppingCartBottomNoEditMode = null;
        parentChooseActivityShoppingCartBottomNoEditMode = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShoppingCart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        customInit(activityShoppingCart, false, true, true);
        rightButtonTop.setText("编辑");
        rightButtonTop.setVisibility(View.INVISIBLE);
        titleTop.setText("购物车");

        ShoppingCartViewActivityShoppingCart.setLoadImageCallBack(
                rightButtonTop,
                new LoadImageCallBack() {
                    @Override
                    public void onLoad(ImageView imageView) {
                        addImageViewList(imageView);
                    }
                },
                new ShoppingCartView.ShoppingCartViewCallBack() {


                    @Override
                    public void onShopName(String shop_id) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                                    .putExtra("shop_id", shop_id)
                                    );
                        }
                    }

                    @Override
                    public void onClick(String goods_id, String type) {
                        if (getBaseActivityContext() != null) {
                            startActivity(new Intent(getBaseActivityContext(), ActivityGoodsServiceDetails.class)
                                    .putExtra("goods_id", goods_id)
                                    .putExtra("type",type));
                        }
                    }

                    @Override
                    public void onCollect(int i, int position, List<ShoppingCart> list, SSS_HolderHelper holderHelper, SSS_Adapter sss_adapter) {
                        postsCollectCancelCollect(i, position, list, holderHelper);
                    }

                    @Override
                    public void onDelete(int i, int position, JSONArray jsonArray, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter) {
                        deleteShoppingCart(jsonArray);
                    }

                    @Override
                    public void onSpecification(int i, int position, List<ShoppingCart> list, SSS_HolderHelper holder, SSS_Adapter sss_adapter) {
                        details(i, position, list, list.get(i).data.get(position).sid, list.get(i).data.get(position).id, holder, sss_adapter);

                    }

                    @Override
                    public void onChanged(int i, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter) {
                        parentChooseActivityShoppingCartBottomEditMode.setVisibility(View.VISIBLE);
                        parentChooseActivityShoppingCartBottomNoEditMode.setVisibility(View.GONE);
                        try {
                            updateShoppingCart(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCheckedChange(int i, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter) {
                        total();
                    }

                });


        all_choose_activity_shopping_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temp = ShoppingCartViewActivityShoppingCart.getList();
                for (int i = 0; i < temp.size(); i++) {
                    temp.get(i).isChoose = isChecked;
                    for (int j = 0; j < temp.get(i).data.size(); j++) {
                        temp.get(i).data.get(j).isChoose = isChecked;
                    }
                }
                ShoppingCartViewActivityShoppingCart.setList(temp, getBaseActivityContext());
                total();
            }
        });
    }


    /**
     * 计算总价
     */
    int a = 0;

    void total() {
        a = 0;
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (ShoppingCartViewActivityShoppingCart.getList().size() != 0) {
                    for (int i = 0; i < ShoppingCartViewActivityShoppingCart.getList().size(); i++) {
                        for (int j = 0; j < ShoppingCartViewActivityShoppingCart.getList().get(i).data.size(); j++) {
                            if (ShoppingCartViewActivityShoppingCart.getList().get(i).data.size() > 0) {
                                if (ShoppingCartViewActivityShoppingCart.getList().get(i).data.get(j).isChoose) {
                                    double c=Double.valueOf(ShoppingCartViewActivityShoppingCart.getList().get(i).data.get(j).price.trim());
                                    a = a + Integer.parseInt(ShoppingCartViewActivityShoppingCart.getList().get(i).data.get(j).num.trim()) * (int)c;
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalPriceChooseActivityShoppingCartBottomEditMode.setText(a + ".00");
                    }
                });
            }
        }.start();
    }

    @OnClick({R.id.back_top, R.id.right_button_top, R.id.ready_buy_choose_activity_shopping_cart_bottom_edit_mode, R.id.collect_choose_activity_shopping_cart_bottom_no_edit_mode, R.id.delete_choose_activity_shopping_cart_bottom_no_edit_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                if ("编辑".equals(rightButtonTop.getText().toString().trim())) {
                    rightButtonTop.setText("完成");
                    parentChooseActivityShoppingCartBottomEditMode.setVisibility(View.GONE);
                    parentChooseActivityShoppingCartBottomNoEditMode.setVisibility(View.VISIBLE);
                    ShoppingCartViewActivityShoppingCart.editt(true, getBaseActivityContext());
                } else if ("完成".equals(rightButtonTop.getText().toString().trim())) {
                    ShoppingCartViewActivityShoppingCart.editt(false, getBaseActivityContext());
                    rightButtonTop.setText("编辑");
                    parentChooseActivityShoppingCartBottomEditMode.setVisibility(View.VISIBLE);
                    parentChooseActivityShoppingCartBottomNoEditMode.setVisibility(View.GONE);
                    try {
                        updateShoppingCart(ShoppingCartViewActivityShoppingCart.getList());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ready_buy_choose_activity_shopping_cart_bottom_edit_mode:
                try {
                    insert_order();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.collect_choose_activity_shopping_cart_bottom_no_edit_mode:
                try {
                    collect_all(ShoppingCartViewActivityShoppingCart.getList());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.delete_choose_activity_shopping_cart_bottom_no_edit_mode:
                deleteShoppingCart(getSid());
                break;
        }
    }

    JSONArray getSid() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ShoppingCartViewActivityShoppingCart.getList().size(); i++) {
            for (int j = 0; j < ShoppingCartViewActivityShoppingCart.getList().get(i).data.size(); j++) {
                if (ShoppingCartViewActivityShoppingCart.getList().get(i).data.get(j).isChoose) {
                    jsonArray.put(ShoppingCartViewActivityShoppingCart.getList().get(i).data.get(j).sid);
                }
            }
        }
        return jsonArray;
    }


    /**
     * 获取购物车列表
     */
    public void getShoppingCart() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getShoppingCartOrder(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "cart")//购物车type=cart，订单type=order
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
                            rightButtonTop.setText("编辑");
                            parentChooseActivityShoppingCartBottomEditMode.setVisibility(View.VISIBLE);
                            parentChooseActivityShoppingCartBottomNoEditMode.setVisibility(View.GONE);
                            all_choose_activity_shopping_cart.setChecked(false);
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        list.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            ShoppingCart shoppingCart = new ShoppingCart();
                                            shoppingCart.shop_id = jsonArray.getJSONObject(i).getString("shop_id");
                                            shoppingCart.name = jsonArray.getJSONObject(i).getString("name");
                                            shoppingCart.logo = jsonArray.getJSONObject(i).getString("logo");
                                            shoppingCart.total_rows = jsonArray.getJSONObject(i).getString("total_rows");
                                            shoppingCart.total = jsonArray.getJSONObject(i).getString("total");
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("data");
                                            List<ShoppingCart_Data> ShoppingCart_Data = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {
                                                ShoppingCart_Data shoppingCart_data = new ShoppingCart_Data();
                                                shoppingCart_data.id = jsonArray1.getJSONObject(j).getString("id");
                                                shoppingCart_data.name = jsonArray1.getJSONObject(j).getString("name");
                                                shoppingCart_data.num = jsonArray1.getJSONObject(j).getString("num");
                                                shoppingCart_data.price = jsonArray1.getJSONObject(j).getString("price");
                                                shoppingCart_data.shop_id = jsonArray1.getJSONObject(j).getString("shop_id");
                                                shoppingCart_data.sid = jsonArray1.getJSONObject(j).getString("sid");
                                                shoppingCart_data.master_map = jsonArray1.getJSONObject(j).getString("master_map");
                                                shoppingCart_data.total = jsonArray1.getJSONObject(j).getString("total");
                                                shoppingCart_data.is_collect = jsonArray1.getJSONObject(j).getString("is_collect");
                                                shoppingCart_data.size_name = jsonArray1.getJSONObject(j).getString("size_name");
                                                shoppingCart_data.type = jsonArray1.getJSONObject(j).getString("type");

                                                List<ShoppingCart_Data_Options> options = new ArrayList<>();
//                                                if (jsonArray1.getJSONObject(j).has("options")){
                                                JSONArray jsonArray2 = jsonArray1.getJSONObject(j).getJSONArray("options");
                                                for (int k = 0; k < jsonArray2.length(); k++) {
                                                    ShoppingCart_Data_Options shoppingCart_data_options = new ShoppingCart_Data_Options();
                                                    shoppingCart_data_options.name = jsonArray2.getJSONObject(k).getString("name");
                                                    shoppingCart_data_options.title = jsonArray2.getJSONObject(k).getString("title");
                                                    options.add(shoppingCart_data_options);
                                                }
//                                                }

                                                shoppingCart_data.options = options;
                                                ShoppingCart_Data.add(shoppingCart_data);
                                            }

                                            shoppingCart.data = ShoppingCart_Data;
                                            list.add(shoppingCart);


                                        }

                                        ShoppingCartViewActivityShoppingCart.setList(list, getBaseActivityContext());
                                        total();
                                    }

                                } else {
                                    list.clear();
                                    ShoppingCartViewActivityShoppingCart.setList(list, getBaseActivityContext());
                                    total();
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }


    /**
     * 商品收藏（取消收藏）
     */
    public void postsCollectCancelCollect(final int i, final int position, final List<ShoppingCart> list, final SSS_HolderHelper holderHelper) {

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.postsCollectCancelCollect(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("type", "goods")
                            .put("collect_id", list.get(i).data.get(position).id)//此处传要收藏的文章ID
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
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        list.get(i).data.get(position).is_collect = "1";
                                        holderHelper.setText(R.id.add_fav_item_shopping_cart_adapter, "取消收藏");
                                    } else if ("0".equals(jsonObject.getJSONObject("data").getString("code"))) {
                                        list.get(i).data.get(position).is_collect = "0";
                                        holderHelper.setText(R.id.add_fav_item_shopping_cart_adapter, "加入收藏");
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Collect-0");
            e.printStackTrace();
        }
    }

    /**
     * 批量收藏
     */
    public void collect_all(final List<ShoppingCart> list) throws JSONException {


        JSONArray collect_id = new JSONArray();
        for (int j = 0; j < list.size(); j++) {
            for (int k = 0; k < list.get(j).data.size(); k++) {
                collect_id.put(list.get(j).data.get(k).id);
            }
        }


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.collect_all(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("collect_id", collect_id)
                            .put("status", "1")//1收藏，0取消
                            .put("type", "goods")//类型（goods,trends,community）等
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

                                    for (int j = 0; j < list.size(); j++) {
                                        for (int k = 0; k < list.get(j).data.size(); k++) {
                                            list.get(j).data.get(k).is_collect = "1";
                                        }
                                    }

                                    ShoppingCartViewActivityShoppingCart.setList(list, getBaseActivityContext());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
            e.printStackTrace();
        }
    }

    /**
     * 更新购物车,预购,订单内的商品
     */
    public void updateShoppingCart(final List<ShoppingCart> list) throws JSONException {

        JSONArray price = new JSONArray();
        JSONArray sid = new JSONArray();
        JSONArray num = new JSONArray();
        JSONArray options = new JSONArray();
        for (int j = 0; j < list.size(); j++) {

            for (int k = 0; k < list.get(j).data.size(); k++) {
                sid.put(list.get(j).data.get(k).sid);
                num.put(list.get(j).data.get(k).num);
                price.put(list.get(j).data.get(k).price);

                JSONArray jsonArray = new JSONArray();
                for (int l = 0; l < list.get(j).data.get(k).options.size(); l++) {
                    jsonArray.put(new JSONObject().put("name", list.get(j).data.get(k).options.get(l).name)
                            .put("title", list.get(j).data.get(k).options.get(l).title));
                }
                options.put(jsonArray);
            }
        }


        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.UpdateShoppingCart(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("sid", sid)
                            .put("num", num)
                            .put("price",price)
                            .put("options", options)
                            .put("type", "cart")//购物车（cart）预购（pre_order）订单（order）
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
                                    ShoppingCartViewActivityShoppingCart.setList(list, getBaseActivityContext());
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
            e.printStackTrace();
        }
    }

    /**
     * 删除购物车的商品
     */
    public void deleteShoppingCart(final JSONArray jsonArray) {


//        JSONArray sid=new JSONArray();
//        for (int j = 0; j < list.size(); j++) {
//            for (int k = 0; k < list.get(j).data.size(); k++) {
//                sid.put(list.get(j).data.get(k).sid);
//            }
//        }
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        if (getBaseActivityContext() != null) {
            ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
            ywLoadingDialog.show();
        }
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.deleteShoppingCart(
                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("sid", jsonArray)
                            .put("type", "cart")//购物车（cart）预购（pre_order）订单（order）
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
                                    getShoppingCart();
                                    EventBus.getDefault().post(new changedShopCart());

//                                    for (int j = 0; j < list.size(); j++) {
//                                            if (list.get(j).data.size()>0){
//                                                for (int k = 0; k < list.get(j).data.size(); k++) {
//                                                    for (int l = 0; l < jsonArray.length(); l++) {
//                                                        if ( list.get(j).data.get(k).sid.equals(jsonArray.getString(l))){
//                                                            list.get(j).data.remove(k);
//                                                            if (list.get(j).data.size()<1){
//                                                                list.remove(j);
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//
//
//
////                                        LogUtils.e(list.size());
////                                        for (int k = 0; k < jsonArray.length(); k++) {
////
////                                            LogUtils.e(list.get(j).data.size());
////
////                                            if (list.get(j).data.get(k).sid.equals(jsonArray.getString(k))){
////
////                                                LogUtils.e(list.get(j).data.get(k).sid+"111"+jsonArray.getString(k));
////                                                list.get(j).data.remove(k);
////                                                if (list.get(j).data.size() < 1) {
////                                                    list.remove(j);
////                                                }
////                                            }
////                                        }
//                                    }

//                                    sss_adapter.removeItem(position);
//                                    list.get(i).data.remove(position);
//                                    LogUtils.e(list.get(i).data.size());
//                                    if (list.get(i).data.size() < 1) {
//                                        list.remove(i);
//                                    }
//                                    ShoppingCartViewActivityShoppingCart.setList(list, getBaseActivityContext());

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:Shopping Cart-0");
            e.printStackTrace();
        }
    }

    /**
     * 点击加入购物车时弹出的商品信息选择框
     */
    public void details(final int i, final int position, final List<ShoppingCart> data, String sid, String goods_id, final SSS_HolderHelper holder, final SSS_Adapter sss_adapter) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.details(
                    new JSONObject()
                            .put("goods_id", goods_id)
                            .put("member_id", Config.member_id)
                            .put("sid", sid)
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
                                    goodsChooseModel.goods_id = jsonObject.getJSONObject("data").getString("goods_id");
                                    goodsChooseModel.title = jsonObject.getJSONObject("data").getString("title");
                                    goodsChooseModel.slogan = jsonObject.getJSONObject("data").getString("slogan");
                                    goodsChooseModel.master_map = jsonObject.getJSONObject("data").getString("master_map");
                                    goodsChooseModel.cost_price = jsonObject.getJSONObject("data").getString("cost_price");
                                    goodsChooseModel.price = jsonObject.getJSONObject("data").getString("price");
                                    goodsChooseModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
                                    goodsChooseModel.end_time = jsonObject.getJSONObject("data").getString("end_time");
                                    goodsChooseModel.likes = jsonObject.getJSONObject("data").getString("likes");
                                    goodsChooseModel.share = jsonObject.getJSONObject("data").getString("share");
                                    goodsChooseModel.sell = jsonObject.getJSONObject("data").getString("sell");
                                    goodsChooseModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    goodsChooseModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    goodsChooseModel.distance = jsonObject.getJSONObject("data").getString("distance");
                                    goodsChooseModel.is_like = jsonObject.getJSONObject("data").getString("is_like");
////////////////////////////////////////////////////////////商品图片↓////////////////////////////////////////////////////////////////////////////////////
                                    List<String> picture = new ArrayList<>();
                                    JSONArray jsonArray1 = jsonObject.getJSONObject("data").getJSONArray("picture");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        picture.add(jsonArray1.getString(j));
                                    }
                                    goodsChooseModel.picture = picture;
//////////////////////////////////////////////////////////////详情图片↓//////////////////////////////////////////////////////////////////////////////////
                                    List<String> photo = new ArrayList<>();
                                    JSONArray jsonArray2 = jsonObject.getJSONObject("data").getJSONArray("photo");
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        picture.add(jsonArray2.getString(k));
                                    }
                                    goodsChooseModel.photo = photo;
////////////////////////////////////////////////////////////////最后的尺寸价格数据///////////////////////////////////////////////////////////////////////////////

                                    List<GoodsChooseSizeData> size_dat = new ArrayList<>();
                                    JSONArray jsonArray3 = jsonObject.getJSONObject("data").getJSONArray("size_data");
                                    for (int l = 0; l < jsonArray3.length(); l++) {
                                        GoodsChooseSizeData goodsChooseSizeData = new GoodsChooseSizeData();
                                        goodsChooseSizeData.name = jsonArray3.getJSONObject(l).getString("name");
                                        goodsChooseSizeData.price = jsonArray3.getJSONObject(l).getString("price");
                                        goodsChooseSizeData.number = jsonArray3.getJSONObject(l).getString("number");


                                        size_dat.add(goodsChooseSizeData);
                                    }
                                    goodsChooseModel.size_dat = size_dat;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                                    JSONArray jsonArray4 = jsonObject.getJSONObject("data").getJSONArray("size_name");

                                    List<GoodsChooseSizeName> size_name = new ArrayList<>();
                                    for (int m = 0; m < jsonArray4.length(); m++) {
                                        GoodsChooseSizeName goodsChooseSizeName = new GoodsChooseSizeName();
                                        goodsChooseSizeName.title = jsonArray4.getJSONObject(m).getString("title");
                                        List<GoodsChooseSizeName_Model> datas = new ArrayList<>();
                                        JSONArray jsonArray = jsonArray4.getJSONObject(m).getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            if ("1".equals(jsonArray.getJSONObject(i).getString("is_check"))) {
                                                datas.add(new GoodsChooseSizeName_Model(jsonArray.getJSONObject(i).getString("name"), true));
                                            } else {
                                                datas.add(new GoodsChooseSizeName_Model(jsonArray.getJSONObject(i).getString("name"), false));
                                            }
                                        }
                                        goodsChooseSizeName.data = datas;
                                        size_name.add(goodsChooseSizeName);
                                    }

                                    goodsChooseModel.size_name = size_name;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    if (menuDialog == null) {
                                        menuDialog = new MenuDialog(ActivityShoppingCart.this);
                                    }
                                    menuDialog.createGoodsBottomDialog("cart", Integer.parseInt(data.get(i).data.get(position).num), getBaseActivityContext(), 3, goodsChooseModel,
                                            new ShoppingCartCallBack() {
                                                @Override
                                                public void onShoppingCartCallBack(int price, int number, List<GoodsChooseSizeName> size_name, JSONArray jsonArray, String type) {
                                                    LogUtils.e("onShoppingCartCallBack");
                                                    if (jsonArray == null || jsonArray.length() == 0) {
                                                        ToastUtils.showShortToast(getBaseActivityContext(), "请选择商品规格");
                                                        return;
                                                    }
                                                    LogUtils.e(price+"---"+number);
                                                    LogUtils.e("onShoppingCartCallBack11111");
                                                    if ("save".equals(type)) {
                                                        data.get(i).data.get(position).num = String.valueOf(number);
                                                        if (jsonArray.length() == data.get(i).data.get(position).options.size()) {
                                                            for (int j = data.get(i).data.get(position).options.size() - 1; j > -1; j--) {
                                                                try {
                                                                    data.get(i).data.get(position).price= String.valueOf(price);
                                                                    data.get(i).data.get(position).options.get(j).name = jsonArray.getJSONObject(j).getString("name");
                                                                    data.get(i).data.get(position).options.get(j).title = jsonArray.getJSONObject(j).getString("title");
                                                                    data.get(i).data.get(position).size_name = data.get(i).data.get(position).options.get(j).title + ":" + data.get(i).data.get(position).options.get(j).name;

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            sss_adapter.setList(data.get(i).data);
                                                        } else {
                                                            ToastUtils.showShortToast(getBaseActivityContext(), "规格数据与原数据不匹配");
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }


    /**
     * 加入预购
     */
    public void insert_order() throws JSONException {

        JSONArray goods_id = new JSONArray();
        JSONArray price = new JSONArray();
        JSONArray num = new JSONArray();
        JSONArray options = new JSONArray();
        for (int j = 0; j < ShoppingCartViewActivityShoppingCart.getList().size(); j++) {
            for (int k = 0; k < ShoppingCartViewActivityShoppingCart.getList().get(j).data.size(); k++) {
                if ((ShoppingCartViewActivityShoppingCart.getList().get(j).data.get(k).isChoose)) {
                    goods_id.put(ShoppingCartViewActivityShoppingCart.getList().get(j).data.get(k).id);
                    num.put(ShoppingCartViewActivityShoppingCart.getList().get(j).data.get(k).num);
                    price.put(list.get(j).data.get(k).price);
                    JSONArray jsonArray = new JSONArray();
                    for (int l = 0; l < ShoppingCartViewActivityShoppingCart.getList().get(j).data.get(k).options.size(); l++) {
                        jsonArray.put(new JSONObject() .put("title", ShoppingCartViewActivityShoppingCart.getList().get(j).data.get(k).options.get(l).title)
                                .put("name", list.get(j).data.get(k).options.get(l).name)
                               );
                    }
                    options.put(jsonArray);
                }

            }
        }

        if (goods_id.length() == 0 || price.length() == 0 || num.length() == 0 || options.length() == 0) {
            ToastUtils.showShortToast(getBaseActivityContext(), "您未选中任何商品");
            return;
        }

        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();


        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.insert_order(

                    new JSONObject()
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("price", price)
                            .put("num", num)
                            .put("options", options)
                            .put("type", "order")//订单类型（pre_order）预购，（order）订单
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
                            rightButtonTop.setText("编辑");
                            parentChooseActivityShoppingCartBottomEditMode.setVisibility(View.VISIBLE);
                            parentChooseActivityShoppingCartBottomNoEditMode.setVisibility(View.GONE);
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    if (getBaseActivityContext() != null) {
                                        if ("1".equals(jsonObject.getJSONObject("data").getString("jump"))){
                                            if ("1".equals(jsonObject.getJSONObject("data").getString("type"))){
                                                startActivity(new Intent(getBaseActivityContext(), OrderGoodsReadyBuy.class));
                                            }else {
                                                startActivity(new Intent(getBaseActivityContext(), OrderServiceReadyBuy.class)
                                                .putExtra("type","2"));
                                            }
                                        }else {
                                            startActivity(new Intent(getBaseActivityContext(), ActivityOrderSynthesize.class));
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:goods-0");
            e.printStackTrace();
        }
    }

}
