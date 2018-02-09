package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_OnItemListener;
import com.blankj.utilcode.customwidget.Layout.SwipeMenuLayout;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.ShoppingCart;
import com.sss.car.model.ShoppingCart_Data;
import com.sss.car.view.ActivityShoppingCart;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by leilei on 2017/9/25.
 */

public class ShoppingCartView extends LinearLayout {
    List<ShoppingCart> list = new ArrayList<>();
    LoadImageCallBack loadImageCallBack;
    TextView textView;
    ShoppingCartViewCallBack shoppingCartViewCallBack;

    public void clear() {
        if (list != null) {
            list.clear();
        }
        list = null;
        loadImageCallBack = null;
        textView = null;
        shoppingCartViewCallBack = null;
    }

    public ShoppingCartView(Context context) {
        super(context);
    }

    public ShoppingCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShoppingCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLoadImageCallBack(TextView textView, LoadImageCallBack loadImageCallBack, ShoppingCartViewCallBack shoppingCartViewCallBack) {
        this.loadImageCallBack = loadImageCallBack;
        this.shoppingCartViewCallBack = shoppingCartViewCallBack;
        this.textView = textView;
    }

    public void setList(List<ShoppingCart> data, Context context) {
        this.list = data;
        this.removeAllViews();
        showData(context);
    }

    void showData(Context context) {
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            final View view = LayoutInflater.from(context).inflate(R.layout.listview_shopping_cart, null);
            final CheckBox select_shop_item_share_post_details_comment_adapter = $.f(view, R.id.select_shop_item_share_post_details_comment_adapter);
            SimpleDraweeView pic_item_share_post_details_comment_adapter = $.f(view, R.id.pic_item_share_post_details_comment_adapter);
            TextView shop_name_shopping_cart = $.f(view, R.id.shop_name_shopping_cart);
            final TextView edit_shopping_cart = $.f(view, R.id.edit_shopping_cart);
            InnerListview listview_listview_shopping_cart = $.f(view, R.id.listview_listview_shopping_cart);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            final SSS_Adapter sss_adapter = new SSS_Adapter<ShoppingCart_Data>(context, R.layout.item_shopping_cart_adapter) {

                @Override
                protected void setView(final SSS_HolderHelper helper, final int position, final ShoppingCart_Data bean, final SSS_Adapter instance) {
                    /**************************************公共部分******************************************************/
                    if (bean.editMode) {
                        helper.setVisibility(R.id.edittext_parent_item_shopping_choose_adapter, VISIBLE);
                        helper.setVisibility(R.id.edittext_parent_item_shopping_cart_adapter, GONE);
                    } else {
                        helper.setVisibility(R.id.edittext_parent_item_shopping_choose_adapter, GONE);
                        helper.setVisibility(R.id.edittext_parent_item_shopping_cart_adapter, VISIBLE);
                    }
                    if ("1".equals(bean.is_collect)) {
                        helper.setText(R.id.add_fav_item_shopping_cart_adapter, "取消收藏");
                    } else {
                        helper.setText(R.id.add_fav_item_shopping_cart_adapter, "加入收藏");
                    }
                    if (bean.isAddShow) {
                        helper.setVisibility(R.id.add_fav_item_shopping_cart_adapter, VISIBLE);
                    } else {
                        helper.setVisibility(R.id.add_fav_item_shopping_cart_adapter, GONE);
                    }

                    helper.getView(R.id.click).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shoppingCartViewCallBack != null) {
                                shoppingCartViewCallBack.onClick(bean.id, bean.type);
                            }
                        }
                    });

                    /***************************************未编辑界面*****************************************************/
                    if (loadImageCallBack != null) {
                        loadImageCallBack.onLoad(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_shopping_cart_edit_adapter)), 0f));
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_shopping_cart_edit_adapter)), 0f);
                    }
                    helper.setText(R.id.content_item_shopping_cart_edit_adapter, bean.name);
                    helper.setText(R.id.price_item_shopping_cart_edit_adapter, "¥" + bean.price);
                    helper.setText(R.id.number_item_shopping_cart_edit_adapter, "×" + bean.num);
                    helper.setChecked(R.id.select_item_shopping_cart_edit_adapter, bean.isChoose);
                    /*****************************************编辑界面***************************************************/

                    if (loadImageCallBack != null) {
                        loadImageCallBack.onLoad(FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_shopping_cart_choose_adapter)), 0f));
                    } else {
                        FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + bean.master_map), ((SimpleDraweeView) helper.getView(R.id.pic_item_shopping_cart_choose_adapter)), 0f);
                    }
                    helper.setChecked(R.id.select_item_shopping_cart_choose_adapter, bean.isChoose);
                    helper.setText(R.id.specification_item_shopping_cart_choose_adapter,bean.size_name);
                    ((EditText) helper.getView(R.id.input_item_shopping_cart_choose_adapter)).setText(bean.num);
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                    /***************************************未编辑界面*****************************************************/
                    helper.setItemChildClickListener(R.id.add_fav_item_shopping_cart_adapter);
                    helper.setItemChildClickListener(R.id.delete_item_shopping_cart_adapter);
                    helper.setItemChildCheckedChangeListener(R.id.select_item_shopping_cart_edit_adapter);
                    /*****************************************编辑界面***************************************************/
                    helper.setItemChildCheckedChangeListener(R.id.select_item_shopping_cart_choose_adapter);
                    helper.setItemChildClickListener(R.id.sub_item_shopping_cart_choose_adapter);
                    helper.setItemChildClickListener(R.id.add_item_shopping_cart_choose_adapter);
                    helper.setItemChildClickListener(R.id.click_specification_item_shopping_cart_choose_adapter);
                }
            };
            sss_adapter.setOnItemListener(new SSS_OnItemListener() {

                @Override
                public void onItemChildClick(View view, int position, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.add_fav_item_shopping_cart_adapter:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item)).smoothClose();
                            if (shoppingCartViewCallBack != null) {
                                shoppingCartViewCallBack.onCollect(finalI, position, list, holder,sss_adapter);
                            }
                            break;
                        case R.id.delete_item_shopping_cart_adapter:
                            ((SwipeMenuLayout) holder.getView(R.id.scoll_item)).smoothClose();
                            if (shoppingCartViewCallBack != null) {
                                shoppingCartViewCallBack.onDelete(finalI, position, new JSONArray().put(list.get(finalI).data.get(position).sid),list,view,sss_adapter);
                            }
                            break;
                        case R.id.sub_item_shopping_cart_choose_adapter:
                            int a= Integer.parseInt(((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).getText().toString());
                            ((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).setText(a - 1+"");
                            String aa = ((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).getText().toString();
                            if (Integer.valueOf(aa) < 1) {
                                aa="1";
                            }
                            holder.setText(R.id.input_item_shopping_cart_choose_adapter,aa);
                            holder.setText(R.id.number_item_shopping_cart_edit_adapter, "×" + aa);
                            holder.setVisibility(R.id.number_item_shopping_cart_edit_adapter, VISIBLE);
                            list.get(finalI).data.get(position).num = aa;
                            LogUtils.e(list.get(finalI).data.get(position).num);
                            break;
                        case R.id.add_item_shopping_cart_choose_adapter:
                            int b= Integer.parseInt(((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).getText().toString());
                            ((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).setText(b + 1+"");
                            String bb = ((EditText) holder.getView(R.id.input_item_shopping_cart_choose_adapter)).getText().toString();
                            if (Integer.valueOf(bb) < 1) {
                                bb="1";
                            }

                            holder.setText(R.id.number_item_shopping_cart_edit_adapter, "×" + bb);
                            holder.setVisibility(R.id.number_item_shopping_cart_edit_adapter, VISIBLE);
                            list.get(finalI).data.get(position).num = bb;
                            LogUtils.e(list.get(finalI).data.get(position).num);
                            break;
                        case R.id.click_specification_item_shopping_cart_choose_adapter:
                            if (shoppingCartViewCallBack != null) {
                                shoppingCartViewCallBack.onSpecification(finalI, position, list, holder,sss_adapter);
                            }
                            break;
                    }
                }

                @Override
                public void onItemChildCheckedChanged(CompoundButton view, int position, boolean isChecked, SSS_HolderHelper holder) {
                    switch (view.getId()) {
                        case R.id.select_item_shopping_cart_edit_adapter:
                            list.get(finalI).data.get(position).isChoose = isChecked;
                            if (!isSelect(finalI)) {
                                select_shop_item_share_post_details_comment_adapter.setChecked(false);
                            }
                            if (shoppingCartViewCallBack!=null){
                                shoppingCartViewCallBack.onCheckedChange(finalI,list,view,sss_adapter);
                            }
                            break;
                        case R.id.select_item_shopping_cart_choose_adapter:
                            list.get(finalI).data.get(position).isChoose = isChecked;
                            if (!isSelect(finalI)) {
                                select_shop_item_share_post_details_comment_adapter.setChecked(false);
                            }
                            if (shoppingCartViewCallBack!=null){
                                shoppingCartViewCallBack.onCheckedChange(finalI,list,view,sss_adapter);
                            }
                            break;
                    }
                }
            });


            listview_listview_shopping_cart.setAdapter(sss_adapter);
            sss_adapter.setList(list.get(finalI).data);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (loadImageCallBack != null) {
                loadImageCallBack.onLoad(FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_shop_no), pic_item_share_post_details_comment_adapter, 0f));
            } else {
                FrescoUtils.showImage(false, 20, 20, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_shop_no), pic_item_share_post_details_comment_adapter, 0f);
            }
            shop_name_shopping_cart.setText(list.get(i).name);


            if (list.get(finalI).editMode) {
                edit_shopping_cart.setText("完成");
            } else {
                edit_shopping_cart.setText("编辑");
            }
            pic_item_share_post_details_comment_adapter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shoppingCartViewCallBack!=null){
                        shoppingCartViewCallBack.onShopName(list.get(finalI).shop_id);
                    }
                }
            });
            shop_name_shopping_cart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shoppingCartViewCallBack!=null){
                        shoppingCartViewCallBack.onShopName(list.get(finalI).shop_id);
                    }
                }
            });
            edit_shopping_cart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("编辑".equals(edit_shopping_cart.getText().toString().trim())) {
                        list.get(finalI).editMode = true;
                        edit_shopping_cart.setText("完成");
                        if (textView != null) {
                            textView.setText("完成");
                        }

                        isEditModeSingel(true);
                        sss_adapter.setList(list.get(finalI).data);
                    } else if ("完成".equals(edit_shopping_cart.getText().toString().trim())) {
                        list.get(finalI).editMode = false;
                        edit_shopping_cart.setText("编辑");
                        int a = 0;
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).editMode) {
                                a++;
                            }
                        }
                        if (a == 0) {
                            if (textView != null) {
                                textView.setText("编辑");
                            }
                        }
                        isEditModeSingel(false);
                        if (shoppingCartViewCallBack!=null){
                            shoppingCartViewCallBack.onChanged(finalI,list,view,sss_adapter);
                        }
                    }

                }
            });


            select_shop_item_share_post_details_comment_adapter.setChecked(list.get(i).isChoose);
            select_shop_item_share_post_details_comment_adapter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    list.get(finalI).isChoose = isChecked;
                    for (int j = 0; j < list.get(finalI).data.size(); j++) {
                        list.get(finalI).data.get(j).isChoose = isChecked;
                    }
                    sss_adapter.setList(list.get(finalI).data);

                }
            });


            this.addView(view);
        }
    }

    public List<ShoppingCart> getList() {
        return list;
    }

    boolean isSelect(int x) {
        int a = 0;
        for (int i = 0; i < list.get(x).data.size(); i++) {
            if (list.get(x).data.get(i).isChoose) {
                a++;
            }
        }
        if (a == 0) {
            return false;
        } else {
            return true;
        }
    }


    void isEditModeSingel(boolean b) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).data.size(); j++) {
                list.get(i).data.get(j).editMode = b;
                list.get(i).data.get(j).isAddShow = !b;
            }
        }
    }

    public void editt(boolean isShow, Context context) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).editMode = isShow;
            for (int j = 0; j < list.get(i).data.size(); j++) {
                list.get(i).data.get(j).editMode = isShow;
                list.get(i).data.get(j).isAddShow = !isShow;
            }
        }
        this.removeAllViews();
        showData(context);
    }


    public interface ShoppingCartViewCallBack {

        void onShopName(String shop_id);

        void onClick(String goods_id,String type);

        void onCollect(int i, int position, List<ShoppingCart> list, SSS_HolderHelper holder, SSS_Adapter sss_adapter);

        void onDelete(int i, int position, JSONArray sid, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter);

        void onSpecification(int i, int position, List<ShoppingCart> list, SSS_HolderHelper holder, SSS_Adapter sss_adapter);

        void onChanged(int i, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter);

        void onCheckedChange(int i, List<ShoppingCart> list, View view, SSS_Adapter sss_adapter);
    }


}
