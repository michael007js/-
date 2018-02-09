package com.blankj.utilcode.customwidget.PhotoSelectView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.R;
import com.blankj.utilcode.adapter.sssAdapter.SSS_HolderHelper;
import com.blankj.utilcode.adapter.sssAdapter.SSS_RVAdapter;
import com.blankj.utilcode.application.UtilCodeApplication;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;


/**
 * 横向图片选择/展示器
 * Created by leilei on 2017/10/26.
 */

/*
 List<String> list = new ArrayList<>();
                                list.add(PhotoSelect.HOLD_STRING);
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=be1a7ceaddca7bcb6976cf6cd6600116/0b46f21fbe096b633a301b7906338744ebf8acea.jpg");
                                psMainPicFragmentCommodity.init(getBaseFragmentActivityContext(), 1, 140, 140, false, true, list,new LoadImageCallBack() {
                                    @Override
                                    public void onLoad(ImageView imageView) {
                                        addImageViewList(imageView);
                                    }
                                });
                                psMainPicFragmentCommodity.setPhotoSelectCallBack(new PhotoSelect.PhotoSelectCallBack() {
                                    @Override
                                    public void onClickFromPic(List<String> list, int position) {

                                    }

                                    @Override
                                    public void onClosePic(int position) {

                                    }

                                    @Override
                                    public void PhotoFail(int requestCode, String errorMsg) {

                                    }
                                });

*/

public class PhotoSelect extends LinearLayout {
    Context context;
    public static final String HOLD_STRING = "default";
    View view;
    RecyclerView scoll_photo_select;
    SSS_RVAdapter sss_rvAdapter;
    int width = 100;
    int height = 100;
    int maxPhoto = 1;
    LoadImageCallBack loadImageCallBack;
    List<String> list = new ArrayList<>();
    PhotoSelectCallBack photoSelectCallBack;

    public void clear() {
        scoll_photo_select = null;
        if (sss_rvAdapter != null) {
            sss_rvAdapter.clear();
        }
        sss_rvAdapter = null;
        if (list != null) {
            list.clear();
        }
        list = null;
        view = null;
        loadImageCallBack = null;
        photoSelectCallBack = null;

    }

    public List<String> getPhotoList() {
        return list;
    }

    public PhotoSelect(Context context) {
        super(context);
    }

    public PhotoSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param list
     */
    public void setList(List<String> list  ) {

        this.list.clear();

        for (int i = 0; i < list.size(); i++) {
            this.list.add(list.get(i));
        }


        if (sss_rvAdapter != null) {
            sss_rvAdapter.setList(this.list);
        }
    }

    public void setData(String image) {
        list.add(image);
        if (sss_rvAdapter != null) {
            sss_rvAdapter.setList(list);
        }
    }

    public void setPhotoSelectCallBack(PhotoSelectCallBack photoSelectCallBack) {
        this.photoSelectCallBack = photoSelectCallBack;
    }

    /**
     * 定制功能
     *
     * @param context
     * @param maxPhoto          选择图片的数量
     * @param width             图片宽度
     * @param height            图片高度
     * @param radius            圆角
     * @param isShowClose       是否允许显示关闭按钮
     * @param list              数据集合
     * @param loadImageCallBack
     */
    public void init(final Context context,
                     final int maxPhoto,
                     final int width,
                     final int height,
                     final boolean radius,
                     final boolean isShowClose,
                     final List<String> list,
                     LoadImageCallBack loadImageCallBack) {
        this.context = context;
        this.loadImageCallBack = loadImageCallBack;
        this.maxPhoto = maxPhoto;
        this.width = width;
        this.height = height;
        for (int i = 0; i < list.size(); i++) {
            this.list.add(list.get(i));
        }
        view = LayoutInflater.from(PhotoSelect.this.context).inflate(R.layout.photo_select, null);
        scoll_photo_select = $.f(view, R.id.scoll_photo_select);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scoll_photo_select.setLayoutManager(linearLayoutManager);


        sss_rvAdapter = new SSS_RVAdapter<String>(scoll_photo_select, R.layout.item_photo_select, this.list) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, String bean) {
                ((RelativeLayout) helper.getView(R.id.parent_item_photo_select)).setLayoutParams(new LinearLayout.LayoutParams(width, height));
                ((ImageView) helper.getView(R.id.pic_item_photo_select)).setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                if (HOLD_STRING.equals(bean)) {
                    ((ImageView) helper.getView(R.id.pic_item_photo_select)).setScaleType(ImageView.ScaleType.FIT_CENTER);
                    helper.setVisibility(R.id.close_item_photo_select, GONE);
                    if (PhotoSelect.this.loadImageCallBack != null) {
                        PhotoSelect.this.loadImageCallBack.onLoad(GlidUtils.glideLoad(radius, ((ImageView) helper.getView(R.id.pic_item_photo_select)), PhotoSelect.this.context, R.mipmap.photo_select_add_image));
                    } else {
                        GlidUtils.glideLoad(radius, ((ImageView) helper.getView(R.id.pic_item_photo_select)), PhotoSelect.this.context, R.mipmap.photo_select_add_image);
                    }
                } else {
                    ((ImageView) helper.getView(R.id.pic_item_photo_select)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (isShowClose) {
                        helper.setVisibility(R.id.close_item_photo_select, VISIBLE);
                    } else {
                        helper.setVisibility(R.id.close_item_photo_select, GONE);
                    }
                    ((ImageView) helper.getView(R.id.pic_item_photo_select)).setTag(R.id.glide_tag, bean);
                    if (PhotoSelect.this.loadImageCallBack != null) {
                        PhotoSelect.this.loadImageCallBack.onLoad(GlidUtils.downLoader(radius, ((ImageView) helper.getView(R.id.pic_item_photo_select)), PhotoSelect.this.context));
                    } else {
                        GlidUtils.downLoader(radius, ((ImageView) helper.getView(R.id.pic_item_photo_select)), PhotoSelect.this.context);
                    }
                }


                ((ImageView) helper.getView(R.id.pic_item_photo_select)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (HOLD_STRING.equals(PhotoSelect.this.list.get(position))) {
                            APPOftenUtils.createPhotoChooseDialog(0, maxPhoto, context, UtilCodeApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                                @Override
                                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                    if (resultList == null || resultList.size() == 0) {
                                        if (photoSelectCallBack != null) {
                                            photoSelectCallBack.PhotoFail(-1, "图片未选择");
                                        }
                                        return;
                                    }
                                    for (int j = 0; j < resultList.size(); j++) {
                                        PhotoSelect.this.list.add(resultList.get(j).getPhotoPath());
                                    }
                                    sss_rvAdapter.setList(PhotoSelect.this.list);
                                }

                                @Override
                                public void onHanlderFailure(int requestCode, String errorMsg) {
                                    if (photoSelectCallBack != null) {
                                        photoSelectCallBack.PhotoFail(requestCode, errorMsg);
                                    }
                                }
                            });
                        } else {
                            List<String> temp = new ArrayList<>();
                            if (photoSelectCallBack != null) {
                                int holdCount = 0;
                                for (int i = 0; i < PhotoSelect.this.list.size(); i++) {
                                    if (!HOLD_STRING.equals(PhotoSelect.this.list.get(i))) {
                                        temp.add(PhotoSelect.this.list.get(i));
                                    } else {
                                        holdCount++;
                                    }
                                }
                                photoSelectCallBack.onClickFromPic(temp, position - holdCount);//这里如果默认的占位符HOLD_STRING的数量超过1的话可能导致数组越界
                            }
                        }
                    }
                });


                if (isShowClose) {
                    ((ImageView) helper.getView(R.id.close_item_photo_select)).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoSelect.this.list.remove(position);
                            sss_rvAdapter.setList(PhotoSelect.this.list);
                            if (photoSelectCallBack != null) {
                                photoSelectCallBack.onClosePic(position);
                            }
                        }
                    });
                }

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }

        };


        scoll_photo_select.setAdapter(sss_rvAdapter);


        this.addView(view);
    }


    public interface PhotoSelectCallBack {

        void onClickFromPic(List<String> list, int position);

        void onClosePic(int position);

        void PhotoFail(int requestCode, String errorMsg);
    }
}
