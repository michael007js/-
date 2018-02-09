package com.sss.car.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.customwidget.ImageView.ImageViewPhotoview.PhotoView;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.QRCodeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.WriterException;
import com.sss.car.Config;
import com.sss.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by leilei on 2017/8/29.
 */

public class ActivityQRImage extends BaseActivity {
    @BindView(R.id.photo)
    PhotoView photoView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.activity_qr_image)
    LinearLayout activityQRImage;

    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoView = null;
        back = null;
        activityQRImage = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_image);
        ButterKnife.bind(this);
        customInit(activityQRImage, false, true, false);
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
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }

        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoView.enable();// 启用图片缩放功能
        photoView.enableRotate();//启用图片旋转
        photoView.setMaxScale(3f);// 获取/设置 最大缩放倍数
        photoView.setAnimaDuring(500);//设置 动画持续时间
        photoView.setTag(R.id.glide_tag,Config.url+getIntent().getExtras().getString("data"));
        GlidUtils.downLoader(false, photoView, getBaseActivityContext());

    }

    @OnClick(R.id.click)
    public void onViewClicked() {
        finish();
    }
}
