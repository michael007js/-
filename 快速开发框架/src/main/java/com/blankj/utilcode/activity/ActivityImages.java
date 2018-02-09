package com.blankj.utilcode.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.R;
import com.blankj.utilcode.adapter.ViewPagerObjAdpter;
import com.blankj.utilcode.customwidget.PhotoDraweeView.PhotoDraweeView;
import com.blankj.utilcode.customwidget.ViewPager.AnimationViewPager;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/*
 if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                                            .putExtra("current", position));
                                }
* */

/**
 *
 * Created by leilei on 2017/8/29.
 */

public class ActivityImages extends BaseActivity {
    LinearLayout activityImage;
    AnimationViewPager viewpagerActivityImage;
    ImageView back;
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
        activityImage= (LinearLayout) findViewById(R.id.activity_image);
        viewpagerActivityImage= (AnimationViewPager) findViewById(R.id.viewpager_activity_image);
        back= (ImageView) findViewById(R.id.back);
        click= (LinearLayout) findViewById(R.id.click);
        customInit(activityImage, false, true, false,true);
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
        addImageViewList(GlidUtils.glideLoad(false, back, getBaseActivityContext(), R.drawable.back));



        for (int i = 0; i < list.size(); i++) {
            final View view = LayoutInflater.from(getBaseActivityContext()).inflate(R.layout.item_activity_images, null);

            PhotoDraweeView photoView = $.f(view, R.id.pic_item_activity_images);
            photoView.setZoomTransitionDuration(500);//设置 动画持续时间
            photoView.setScale(3f);// 获取/设置 最大缩放倍数
            LogUtils.e(list.get(i));
//            Glide.with(getBaseActivityContext())
//                    .load(Config.url + list.get(i))
//                    .asBitmap()
//                    .dontAnimate()
//                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                            int imageWidth = resource.getWidth();
//                            int imageHeight = resource.getHeight();
//                            if (imageHeight > 4096) {
//                                imageHeight = 4096;
//                            }
//                            double precent = 1.0 * imageWidth / imageHeight;
//                            LogUtils.e(precent);
//
//                            ViewPager.LayoutParams para = (ViewPager.LayoutParams) view.getLayoutParams();
//                            if (para!=null){
//                                if ( getWindowManager()!=null){
//                                    if (getWindowManager().getDefaultDisplay()!=null){
//                                        para.width = getWindowManager().getDefaultDisplay().getWidth();
//                                        para.height = (int) (getWindowManager().getDefaultDisplay().getWidth() / precent);
//                                        view.setLayoutParams(para);
//                                        LogUtils.e(imageHeight);
//                                    }
//                                }
//                            }
//                        }
//
//                    });
            photoViewList.add(view);
//            addImageViewList(GlidUtils.downLoader(false, photoView, getBaseActivityContext()));
            if (list.get(i).startsWith("/storage/")){
                photoView.setPhotoUri( Uri.fromFile(new File(list.get(i))));
            }else {
                photoView.setPhotoUri(Uri.parse(list.get(i)));

            }
        }
        viewpagerActivityImage.setAdapter(new ViewPagerObjAdpter(photoViewList));
        viewpagerActivityImage.setCurrentItem(getIntent().getExtras().getInt("current"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
