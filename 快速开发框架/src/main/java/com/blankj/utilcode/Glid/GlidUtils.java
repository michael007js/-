package com.blankj.utilcode.Glid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.Layout.coverFlow.ui.containers.contentbands.BasicContentBand;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;

/**
 * Created by leilei on 2017/4/25.
 */


public class GlidUtils {

    /**
     * 清理视图内的图片
     *
     * @param view
     */
    public static void clearImg(View view) {
        if (view != null) {
            Glide.clear(view);
        }
    }


    public static ImageView downloadWithAutoSize(final String url, final ImageView imageview, final Context context) {
        Glide.with(context)
                .load(url)
                .asBitmap()
//                .placeholder(R.drawable.default)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        int imageHeight = resource.getHeight();
                        if (imageHeight > 4096) {
                            imageHeight = 4096;
                            ViewGroup.LayoutParams para = imageview.getLayoutParams();
                            para.width = BasicContentBand.LayoutParams.MATCH_PARENT;
                            para.height = imageHeight;
                            imageview.setLayoutParams(para);

                            Glide.with(context)
                                    .load(url)
//                                    .placeholder(R.drawable.default)
                                    .dontAnimate()
                                    .centerCrop()
                                    .into(imageview);
                        } else {
                            Glide.with(context)
                                    .load(url)
//                                    .placeholder(R.Rdrawable.default)
                                    .dontAnimate()
                                    .into(imageview);
                        }
                    }

                });
        return imageview;
    }


    public interface GlidUtilsCallBack {
        void onBitmap(Bitmap bitmap);
    }

    /**
     * 下载Bitmap(须在线程中执行)
     * @param url
     * @param context
     * @param glidUtilsCallBack
     */
    public static void downloadBitmap(final String url, final Context context, final GlidUtilsCallBack glidUtilsCallBack) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
               if (glidUtilsCallBack!=null){
                   glidUtilsCallBack.onBitmap(resource);
               }
            }
        });

    }


    //GLIDE框架下载图片
    public static ImageView downLoader(boolean isCircle, final ImageView imageView, final Context context) {
        if (context == null) {
            return null;
        }


        if (isCircle) {
//        BitmapUtils.releaseImageViewResouce(imageView);
            Glide.with(context)
                    .load((String) imageView.getTag(R.id.glide_tag))
                    .asBitmap()

                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
                    .fitCenter()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存
                        /*
                         *DiskCacheStrategy.NONE 什么都不缓存
				         *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				         *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				         *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				         * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.NORMAL)//优先级，设置图片加载的顺序：
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            //        BitmapUtils.releaseImageViewResouce(imageView);
            Glide.with(context.getApplicationContext())
                    .load((String) imageView.getTag(R.id.glide_tag))
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存


                /*
                 *DiskCacheStrategy.NONE 什么都不缓存
				 *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				 *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				 *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				 * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.NORMAL)//优先级，设置图片加载的顺序：
                    .into(imageView);
//                  .into(target);//我们通过网络得到的图片有的时候不一定就直接设置到ImageView上，有的时候就只是要一个Bitmap


        }

        return imageView;
    }

    //GLIDE框架下载图片(带圆角)
    public static ImageView downLoader(boolean isCircle, int round, final ImageView imageView, final Context context) {
        if (context == null) {
            return null;
        }


        if (isCircle) {
//        BitmapUtils.releaseImageViewResouce(imageView);
            Glide.with(context)
                    .load((String) imageView.getTag(R.id.glide_tag))
                    .asBitmap()
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存
                        /*
                         *DiskCacheStrategy.NONE 什么都不缓存
				         *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				         *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				         *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				         * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            //        BitmapUtils.releaseImageViewResouce(imageView);
            Glide.with(context.getApplicationContext())
                    .load((String) imageView.getTag(R.id.glide_tag))
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存

                /*
                 *DiskCacheStrategy.NONE 什么都不缓存
				 *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				 *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				 *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				 * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .transform(new GlideRoundTransform(context, round))
                    .into(imageView);


        }

        return imageView;
    }

    /**
     * 加载本地资源图片
     *
     * @param isCircle
     * @param imageView
     * @param context
     * @param resourceId
     */
    public static ImageView glideLoad(boolean isCircle, final ImageView imageView, final Context context, int resourceId) {
        if (context == null) {
            return null;
        }
//        BitmapUtils.releaseImageViewResouce(imageViewResource);
        if (isCircle) {
            Glide.with(context)
                    .load((String) imageView.getTag(R.id.glide_tag))
                    .asBitmap()
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
//                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存
                        /*
                         *DiskCacheStrategy.NONE 什么都不缓存
				         *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				         *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				         *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				         * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Glide
                    .with(context)
                    .load(resourceId)
                    .asBitmap()
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
//                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
//                .dontAnimate()//不带动画
                    .skipMemoryCache(true)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .into(imageView);
        }

        return imageView;
    }

    /**
     * 加载bitmap
     *
     * @param isCircle
     * @param imageView
     * @param context
     * @param bitmap
     * @return
     */
    public static ImageView glidLoad(boolean isCircle, final ImageView imageView, final Context context, Bitmap bitmap) {
        if (context == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (isCircle) {
            Glide.with(context)
                    .load(bytes)
                    .asBitmap()
                    .thumbnail(0.7f)//缩略图(先加载图片的百分之多少来显示，然后在加载原图)
                    .centerCrop()//使用fitCenter即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围,该图像将会完全显示，但可能不会填满整个*/
                    .skipMemoryCache(true)//跳过内存缓存
                        /*
                         *DiskCacheStrategy.NONE 什么都不缓存
				         *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				         *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				         *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				         * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Glide.with(context)
                    .load(bytes)
                    .skipMemoryCache(true)//跳过内存缓存
                        /*
                         *DiskCacheStrategy.NONE 什么都不缓存
				         *DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
				         *DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
				         *DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
				         * */
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)//优先级，设置图片加载的顺序：
                    .into(imageView);
        }


        return imageView;
    }

    /**
     * 当列表在滑动的时候，调用pauseRequests()取消图片加载请求
     *
     * @param context
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }


    /**
     * 恢复请求
     *
     * @param context
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }


    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSizeByString(Context context) {
        try {
            return getFormatSize(getFolderSize(getDir(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static long getCacheSizeByLong(Context context) {
        try {
            return getFolderSize(getDir(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static File getDir(Context context) {
        return new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(long size) {


        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return 0 + "KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
