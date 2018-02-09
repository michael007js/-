package com.blankj.utilcode.fresco;

/**
 * Created by leilei on 2017/5/25.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;


/**
 * Created by 李可乐 on 2017/2/17 0017.
 * 图片加载的封装builder类
 * 使用示例
 * <p>
 * ImageLoadBuilder.Start(getContext(),mImageUser,url_head)
 * .setIsCircle(true)
 * .build();
 * </p>
 */

public class FrescoImageLoadBuilder {
    //必要参数
    Context mContext;
    SimpleDraweeView mSimpleDraweeView;
    String mUrl;

    //非必要参数
    String mUrlLow;//低分率图地址

    Drawable mPlaceHolderImage;//占位图
    Drawable mProgressBarImage;//loading图
    Drawable mRetryImage;//重试图
    Drawable mFailureImage;//失败图
    Drawable mBackgroundImage;//背景图

    ScalingUtils.ScaleType mActualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP;
    boolean mIsCircle = false;//是否圆形图片
    boolean mIsRadius = false;//是否圆角
    boolean mIsBorder = false;//是否有包边
    float mRadius = 10;//圆角度数 默认10
    ResizeOptions mResizeOptions = new ResizeOptions(3000, 3000);//图片的大小限制

    ControllerListener mControllerListener;//图片加载的回调

    BaseBitmapDataSubscriber mBitmapDataSubscriber;

    /**
     * 构造器的构造方法 传入必要参数
     *
     * @param mContext
     * @param mSimpleDraweeView
     * @param mUrl
     */
    public FrescoImageLoadBuilder(Context mContext, SimpleDraweeView mSimpleDraweeView, String mUrl) {
        this.mContext = mContext;
        this.mSimpleDraweeView = mSimpleDraweeView;
        this.mUrl = mUrl;
    }

    //http://www.cnblogs.com/ldq2016/p/6645185.html



    //http://cache.baiducontent.com/c?m=9d78d513d9d431df4f9997697c1cc0151a4381137d8c8f5721838449e3321b120231b4ac2652545f84806b6671ff121ab5a3746670437eb8c18ece08cabae13532d87969250b863643924ff3dc46529d77d64798fc59b1bbf43195acd1d2da504e99134321d1aedc4756438c3cab4a70fea19b43115810bcee&p=ce769a478e9c5be808e2977e094990&newp=882a9645d2d213e50be2962f7f0789231610db2151d7d401298ffe0cc4241a1a1a3aecbf21221000d1c17b6c04ae4c5de8f130743c0634f1f689df08d2ecce7e3eda6d&user=baidu&fm=sc&query=%BB%F1%C8%A1simpledraweeviewbitmap&qid=8e87448e0001db04&p1=2
    public static FrescoImageLoadBuilder Start(Context mContext, SimpleDraweeView mSimpleDraweeView, String mUrl) {
        return new FrescoImageLoadBuilder(mContext, mSimpleDraweeView, mUrl);
    }

    /**
     * 构造器的build方法 构造真正的对象 并返回
     * 构造之前需要检查
     *
     * @return
     */
    public FrescoImageLoad build() {
        LogUtils.e("图片开始加载 viewId=" + this.mSimpleDraweeView.getId() + " url" + this.mUrl);
//            if (TextUtils.isEmpty(mUrl)) {
//                throw new IllegalArgumentException("URL不能为空");
//            }

        //不能同时设定 圆形圆角
        if (mIsCircle && mIsRadius) {
            throw new IllegalArgumentException("图片不能同时设置圆角和圆形");
        }

        return new FrescoImageLoad(this);
    }

    public FrescoImageLoadBuilder setBitmapDataSubscriber(BaseBitmapDataSubscriber mBitmapDataSubscriber) {
        this.mBitmapDataSubscriber = mBitmapDataSubscriber;
        return this;
    }

    public FrescoImageLoadBuilder setUrlLow(String urlLow) {
        this.mUrlLow = urlLow;
        return this;
    }

    public FrescoImageLoadBuilder setActualImageScaleType(ScalingUtils.ScaleType mActualImageScaleType) {
        this.mActualImageScaleType = mActualImageScaleType;
        return this;
    }

    public FrescoImageLoadBuilder setPlaceHolderImage(Drawable mPlaceHolderImage) {
        this.mPlaceHolderImage = mPlaceHolderImage;
        return this;
    }

    public FrescoImageLoadBuilder setProgressBarImage(Drawable mProgressBarImage) {
        this.mProgressBarImage = mProgressBarImage;
        return this;
    }

    public FrescoImageLoadBuilder setRetryImage(Drawable mRetryImage) {
        this.mRetryImage = mRetryImage;
        return this;
    }

    public FrescoImageLoadBuilder setFailureImage(Drawable mFailureImage) {
        this.mFailureImage = mFailureImage;
        return this;
    }

    public FrescoImageLoadBuilder setBackgroundImage(Drawable mBackgroundImage) {
        this.mBackgroundImage = mBackgroundImage;
        return this;
    }

    public FrescoImageLoadBuilder setBackgroupImageColor(int colorId) {
        Drawable color = ContextCompat.getDrawable(mContext, colorId);
        this.mBackgroundImage = color;
        return this;
    }

    public FrescoImageLoadBuilder setIsCircle(boolean mIsCircle) {
        this.mIsCircle = mIsCircle;
        return this;
    }

    public FrescoImageLoadBuilder setIsCircle(boolean mIsCircle, boolean mIsBorder) {
        this.mIsBorder = mIsBorder;
        this.mIsCircle = mIsCircle;
        return this;
    }

    public FrescoImageLoadBuilder setIsRadius(boolean mIsRadius) {
        this.mIsRadius = mIsRadius;
        return this;
    }

    public FrescoImageLoadBuilder setIsRadius(boolean mIsRadius, float mRadius) {
        this.mRadius = mRadius;
        return setIsRadius(mIsRadius);
    }

    public FrescoImageLoadBuilder setRadius(float mRadius) {
        this.mRadius = mRadius;
        return this;
    }

    public FrescoImageLoadBuilder setResizeOptions(ResizeOptions mResizeOptions) {
        this.mResizeOptions = mResizeOptions;
        return this;
    }

    public FrescoImageLoadBuilder setControllerListener(ControllerListener mControllerListener) {
        this.mControllerListener = mControllerListener;
        return this;
    }
}