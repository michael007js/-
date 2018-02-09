package com.sss.car.custom;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.InnerListview;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.AppUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;
import com.sss.car.model.CouponGetModel;
import com.sss.car.model.CouponGetModel_List;

import java.util.List;

/**
 * Created by leilei on 2017/11/2.
 */

public class ListviewCouponSaleSeckillDiscount extends LinearLayout {

    ListviewCouponSaleSeckillDiscountOperationCallBack ListviewCouponOperationCallBack;


    public ListviewCouponSaleSeckillDiscount(Context context) {
        super(context);
    }

    public ListviewCouponSaleSeckillDiscount(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewCouponSaleSeckillDiscount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setData(final Context context, final List<CouponGetModel> data, final LoadImageCallBack loadImageCallBack) {

        for (int i = 0; i < data.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.listview_coupon, null);
            InnerListview listview_listview_coupon = $.f(view, R.id.listview_listview_coupon);
            LinearLayout click_listview_coupon = $.f(view, R.id.click_listview_coupon);
            TextView title_listview_coupon = $.f(view, R.id.title_listview_coupon);
            TextView more_listview_coupon = $.f(view, R.id.more_listview_coupon);
            title_listview_coupon.setText(data.get(finalI).scope_name);

            final SSS_Adapter sss_adapter = new SSS_Adapter<CouponGetModel_List>(context, R.layout.item_fragment_coupon_sale_seckill_discount, data.get(finalI).list) {
                @Override
                protected void setView(SSS_HolderHelper helper, final int position, CouponGetModel_List bean, final SSS_Adapter instance) {
                    helper.setText(R.id.discounts_item_fragment_coupon, bean.money);
                    helper.setText(R.id.date_item_fragment_coupon, bean.duration);
                    helper.setText(R.id.title_item_fragment_coupon, bean.name);
                    helper.setText(R.id.slogan_item_fragment_coupon, bean.describe);
                    helper.setText(R.id.price_item_fragment_coupon, "¥" + bean.sell_price);
                    if ("1".equals(bean.is_join)) {
                        helper.setText(R.id.get_item_fragment_coupon, "已抢");
                        ((TextView) helper.getView(R.id.get_item_fragment_coupon)).setOnClickListener(null);
                    } else if ("0".equals(bean.is_join)) {
                        helper.setText(R.id.get_item_fragment_coupon, "马上抢");
                        helper.setItemChildClickListener(R.id.get_item_fragment_coupon);
                        ((TextView) helper.getView(R.id.get_item_fragment_coupon)).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (ListviewCouponOperationCallBack != null) {
//                                    ListviewCouponOperationCallBack.onGetCoupon(data.get(finalI).list, position, instance);
//                                }
                                if (ListviewCouponOperationCallBack != null) {
                                    ListviewCouponOperationCallBack.onClickCoupon(data.get(finalI).list.get(position).coupon_id,data.get(finalI).list.get(position).money);
                                }
                            }
                        });
                    }
                    loadImageCallBack.onLoad(FrescoUtils.showImage(false, 150, 110, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.logo_coupon), ((SimpleDraweeView) helper.getView(R.id.pic_item_fragment_coupon)), 0f));
                    /*1满减券，2现金券，3折扣券*/
                    switch (bean.type) {
                        case "1":
                            helper.setText(R.id.type_item_fragment_coupon, "满减券");
                            break;
                        case "2":
                            helper.setText(R.id.type_item_fragment_coupon, "现金券");
                            break;
                        case "3":
                            helper.setText(R.id.type_item_fragment_coupon, "折扣券");
                            break;
                    }

                    helper.getView(R.id.click).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ListviewCouponOperationCallBack != null) {
                                ListviewCouponOperationCallBack.onClickCoupon(data.get(finalI).list.get(position).coupon_id, data.get(finalI).list.get(position).money);
                            }
                        }
                    });
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {


                }
            };

            listview_listview_coupon.setAdapter(sss_adapter);
            click_listview_coupon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ListviewCouponOperationCallBack != null) {
//                        ListviewCouponOperationCallBack.onClickTitle_ListviewCouponOperationCallBack(data.get(finalI).classify_id, data.get(finalI).name);
                    }
                }
            });
            more_listview_coupon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ListviewCouponOperationCallBack != null) {
                        ListviewCouponOperationCallBack.onClickMore_ListviewCouponOperationCallBack( data.get(finalI).scope, data.get(finalI).list, sss_adapter);
                    }
                }
            });
            this.addView(view);
        }

    }


    public void setListviewCouponSaleSeckillDiscountOperationCallBack(ListviewCouponSaleSeckillDiscountOperationCallBack ListviewCouponOperationCallBack) {
        this.ListviewCouponOperationCallBack = ListviewCouponOperationCallBack;
    }


    public interface ListviewCouponSaleSeckillDiscountOperationCallBack {

        void onClickCoupon(String coupon_id, String money);

        void onClickMore_ListviewCouponOperationCallBack(String scope, List<CouponGetModel_List> list, SSS_Adapter sss_adapter);

        void onClickTitle_ListviewCouponOperationCallBack(String classify_id, String title);

        void onGetCoupon(List<CouponGetModel_List> data, int position, SSS_Adapter sss_adapter);
    }
}
