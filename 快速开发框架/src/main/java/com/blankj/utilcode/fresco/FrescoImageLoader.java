package com.blankj.utilcode.fresco;

/**
 * Created by leilei on 2017/5/27.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.finalteam.galleryfinal.widget.GFImageView;


/**
 * 配合GalleryFinal实现照片墙选择器
 * Desction:fresco image loader
 * Author:pengjianbo
 * Date:15/12/24 下午9:34
 */
public class FrescoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Context context;


    public FrescoImageLoader(Context context) {
        this.context = context;

    }

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, final Drawable defaultDrawable, int width, int height) {
        LogUtils.e(path);
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                //.centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.glide_tag, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.glide_tag);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {
        LogUtils.e("clearMemoryCache");
    }
}