package com.sss.car.order_new;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azhong.ratingbar.OnChangeListener;
import com.azhong.ratingbar.RatingBar;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.customwidget.ListView.HorizontalListView;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.model.OrderModel_goods_data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/10/9.
 */

public class ListViewComment extends LinearLayout {
    OrderModel data;
    String targetPic;
    ListViewOrderCommentCallBack listViewOrderCommentCallBack;
    LoadImageCallBack loadImageCallBack;

    public ListViewComment(Context context) {
        super(context);
    }

    public ListViewComment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewComment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListViewOrderCommentCallBack(ListViewOrderCommentCallBack listViewOrderCommentCallBack) {
        this.listViewOrderCommentCallBack = listViewOrderCommentCallBack;
    }

    public void setLoadImageCallBack(LoadImageCallBack loadImageCallBack) {
        this.loadImageCallBack = loadImageCallBack;
    }


    public void setList(Context context, OrderModel data, String targetPic) {
        this.removeAllViews();
        this.data = data;
        this.targetPic = targetPic;
        showData(context);
    }

    void showData(final Context context) {
        for (int i = 0; i < data.goods_data.size(); i++) {
            final int finalI = i;
            View view = LayoutInflater.from(context).inflate(R.layout.item_listview_order_comment_adapter, null);
            final SimpleDraweeView pic_item_listview_order_comment_adapter = $.f(view, R.id.pic_item_listview_order_comment_adapter);
            RatingBar ratingBar = $.f(view, R.id.startbar_item_listview_order_comment_adapter);
            final TextView state_item_listview_order_comment_adapter = $.f(view, R.id.state_item_listview_order_comment_adapter);
            final EditText input_item_listview_order_comment_adapter = $.f(view, R.id.input_item_listview_order_comment_adapter);
            HorizontalListView HorizontalListView_item_listview_order_comment_adapter = $.f(view, R.id.HorizontalListView_item_listview_order_comment_adapter);
            input_item_listview_order_comment_adapter.setText(data.goods_data.get(finalI).customContent);
            LogUtils.e(targetPic);
            if (StringUtils.isEmpty(targetPic)) {
                FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_shop_no), pic_item_listview_order_comment_adapter, 0f);
            } else {
                FrescoUtils.showImage(false, 80, 80, Uri.parse(Config.url + targetPic), pic_item_listview_order_comment_adapter, 30f);
            }
            ratingBar.setStarCount(5);
            ratingBar.setStar( data.goods_data.get(finalI).customGrade);
            ratingBar.setOnStarChangeListener(new OnChangeListener() {
                @Override
                public void onChange(int star) {

                    switch (star) {
                        case 1:
                            data.goods_data.get(finalI).customGrade = star;
                            state_item_listview_order_comment_adapter.setText("非常差");
                            break;
                        case 2:
                            data.goods_data.get(finalI).customGrade = star;
                            state_item_listview_order_comment_adapter.setText("差");
                            break;
                        case 3:
                            data.goods_data.get(finalI).customGrade = star;
                            state_item_listview_order_comment_adapter.setText("一般");
                            break;
                        case 4:
                            data.goods_data.get(finalI).customGrade = star;
                            state_item_listview_order_comment_adapter.setText("好");
                            break;
                        case 5:
                            data.goods_data.get(finalI).customGrade = star;
                            state_item_listview_order_comment_adapter.setText("非常好");
                            break;
                        default:
                            data.goods_data.get(finalI).customGrade = 0;
                            state_item_listview_order_comment_adapter.setText("");
                    }
                    if (listViewOrderCommentCallBack != null) {
                        listViewOrderCommentCallBack.onStar(finalI, data.goods_data.get(finalI).customGrade);
                    }
                }
            });
            input_item_listview_order_comment_adapter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    data.goods_data.get(finalI).customContent = input_item_listview_order_comment_adapter.getText().toString().trim();
                    if (listViewOrderCommentCallBack != null) {
                        listViewOrderCommentCallBack.onComment(finalI, s.toString());
                    }
                }
            });

            final SSS_Adapter sss_adapter = new SSS_Adapter<String>(context, R.layout.item_image) {
                @Override
                protected void setView(SSS_HolderHelper helper, int position, String bean, SSS_Adapter instance) {
                    LogUtils.e("setView");
                    if (loadImageCallBack != null) {
                        if ("default".equals(bean)) {
                            FrescoUtils.showImage(false, 80, 80, Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.logo_add_image), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                        } else {
                            FrescoUtils.showImage(false, 80, 80, Uri.fromFile(new File(bean)), ((SimpleDraweeView) helper.getView(R.id.pic_item_image)), 0f);
                        }
                    }
                }

                @Override
                protected void setItemListener(SSS_HolderHelper helper) {
                }
            };
            if (data.goods_data.get(finalI).photos.size() == 0) {
                data.goods_data.get(finalI).photos.add("default");
            }
            HorizontalListView_item_listview_order_comment_adapter.setAdapter(sss_adapter);
            HorizontalListView_item_listview_order_comment_adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("default".equals(data.goods_data.get(finalI).photos.get(position))) {
                        LogUtils.e("onAddPhoto");
                        if (listViewOrderCommentCallBack != null) {
                            listViewOrderCommentCallBack.onAddPhoto(finalI, position, data.goods_data);
                        }
                    } else {
                        LogUtils.e("onClickImage");
                        if (listViewOrderCommentCallBack != null) {
                            listViewOrderCommentCallBack.onClickImage(finalI, position, data.goods_data);
                        }
                    }
                }
            });
            sss_adapter.setList(data.goods_data.get(finalI).photos);

            this.addView(view);
        }
    }

    public interface ListViewOrderCommentCallBack {
        void onClickImage(int finaI, int position, List<OrderModel_GoodsData> data);

        void onAddPhoto(int finaI, int position, List<OrderModel_GoodsData> data);

        void onStar(int finaI, int star);

        void onComment(int finalI ,String content);
    }

}
