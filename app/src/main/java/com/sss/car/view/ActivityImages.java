package com.sss.car.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.adapter.ViewPagerAdpter;
import com.blankj.utilcode.customwidget.ImageView.ImageViewPhotoview.PhotoView;
import com.blankj.utilcode.customwidget.PhotoDraweeView.PhotoDraweeView;
import com.blankj.utilcode.customwidget.ViewPager.AnimationViewPager;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sss.car.Config;
import com.sss.car.R;
import com.sss.car.custom.ViewPagerObjAdpter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                                            .putExtra("current", position));
                                }
* */

/**
 * Created by leilei on 2017/8/29.
 */

public class ActivityImages extends BaseActivity {
    @BindView(R.id.activity_image)
    LinearLayout activityImage;
    @BindView(R.id.viewpager_activity_image)
    AnimationViewPager viewpagerActivityImage;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.click)
    LinearLayout click;
    List<String> list = new ArrayList<>();
    List<View> photoViewList = new ArrayList<>();

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (list != null) {
            list.clear();
        }
        list = null;
        if (photoViewList != null) {
            photoViewList.clear();
        }
        photoViewList = null;
        activityImage = null;
        viewpagerActivityImage = null;
        back = null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);
        customInit(activityImage, false, true, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }
        }
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误error-1");
            finish();
        }
        list = getIntent().getExtras().getStringArrayList("data");
        if (list == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误error-2");
            finish();
        }
        addImageViewList(GlidUtils.glideLoad(false, back, getBaseActivityContext(), R.mipmap.back));


        for (int i = 0; i < list.size(); i++) {
            final View view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.item_activity_images, null);
            PhotoDraweeView photoView = $.f(view, R.id.pic_item_activity_images);
            TextView save = $.f(view, R.id.save);
            photoView.setZoomTransitionDuration(500);//设置 动画持续时间
            photoView.setScale(3f);// 获取/设置 最大缩放倍数
            LogUtils.e(list.get(i));
            photoViewList.add(view);
//            addImageViewList(GlidUtils.downLoader(false, photoView, getBaseActivityContext()));
            if (list.get(i).startsWith("/storage/")) {
                photoView.setPhotoUri(Uri.fromFile(new File(list.get(i))));
            } else {
                photoView.setPhotoUri(Uri.parse(list.get(i)));

            }
            final int finalI = i;

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (APPOftenUtils.saveImageToGallery(getBaseActivityContext(), FrescoUtils.returnBitmap(Uri.parse(list.get(finalI))))) {
                        ToastUtils.showShortToast(getBaseActivityContext(), "保存成功");
                    } else {
                        ToastUtils.showShortToast(getBaseActivityContext(), "保存失败");
                    }
                }
            });
        }
        viewpagerActivityImage.setAdapter(new ViewPagerObjAdpter(photoViewList));
        viewpagerActivityImage.setCurrentItem(getIntent().getExtras().getInt("current"));
    }

    @OnClick(R.id.click)
    public void onViewClicked() {
        finish();
    }
}
