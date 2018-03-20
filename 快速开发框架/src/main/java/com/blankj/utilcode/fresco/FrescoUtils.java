package com.blankj.utilcode.fresco;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultBitmapMemoryCacheParamsSupplier;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.math.BigDecimal;


/**
 * Created by leilei on 2017/4/8.
 */

public class FrescoUtils {

    //分配的可用内存
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    //使用的缓存数量
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    //小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 20 * ByteConstants.MB;

    //小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 60 * ByteConstants.MB;

    //默认图极低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 20 * ByteConstants.MB;

    //默认图低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_LOW_SIZE = 60 * ByteConstants.MB;

    //默认图磁盘缓存的最大值
    private static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;

    //小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "ImagePipelineCacheSmall";

    //默认图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "ImagePipelineCacheDefault";

    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {

        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE,// 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中图片的最大数量。
                MAX_MEMORY_CACHE_SIZE,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,// 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);// 内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(context.getApplicationContext().getCacheDir())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance()) // 报内存警告时的监听
                .setBitmapMemoryCacheParamsSupplier(new DefaultBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                .setResizeAndRotateEnabledForNetwork(true);


        // 当内存紧张时采取的措施
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                LogUtils.e("sssss", String.format("onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });
        return configBuilder.build();
    }

    /**
     * 显示图片
     *
     * @param roundAsCircle    是否生成圆形图
     * @param width            宽度
     * @param height           高德角度
     * @param uri              图片URI
     * @param simpleDraweeView 图片框
     * @param radius           四个角的弧度
     */
    public static SimpleDraweeView showImage(boolean roundAsCircle, int width, int height, Uri uri, SimpleDraweeView simpleDraweeView, float radius) {
        if (uri == null || simpleDraweeView == null) {
            return simpleDraweeView;
        }
        if (roundAsCircle == false) {
            if (simpleDraweeView.getHierarchy() == null) {
                return simpleDraweeView;
            }
            simpleDraweeView.getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(radius));

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            simpleDraweeView.setController(controller);
            request = null;
            controller = null;
        } else {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(0f);
            if (roundingParams == null) {
                return simpleDraweeView;
            }
            roundingParams.setRoundAsCircle(true);
            if (simpleDraweeView.getHierarchy() == null) {
                return simpleDraweeView;
            }
            simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .setLocalThumbnailPreviewsEnabled(true)//允许显示缩略
                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.DISK_CACHE)
                    /**
                     * 允许设置一个最低请求级别，请求级别和上面对应地有以下几个取值:
                     BITMAP_MEMORY_CACHE
                     ENCODED_MEMORY_CACHE
                     DISK_CACHE
                     FULL_FETCH*/
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            simpleDraweeView.setController(controller);
            request = null;
            controller = null;
            roundingParams = null;
        }
        return simpleDraweeView;
    }


    /**
     * 高斯模糊后显示
     *
     * @param uri
     * @param draweeView
     * @param width      draweeView的宽
     * @param height     draweeView的高
     * @param context
     * @param radius     高斯模糊的半径, 每一个像素都取周边(多少个)像素的平均值
     * @param sampling   采样率 原本是设置到BlurPostprocessor上的,因为高斯模糊本身对图片清晰度要求就不高,
     *                   所以此处直接设置到ResizeOptions上,直接让解码生成的bitmap就缩小,而BlurPostprocessor
     *                   内部sampling设置为1,无需再缩
     */
    public static void gaussianBlur(Uri uri, SimpleDraweeView draweeView, int width, int height, Context context, int radius, int sampling) {

        if (sampling < 2) {
            sampling = 2;
        }
        loadUrl(uri, draweeView, new BlurPostprocessor(context, radius, 1), width / sampling, height / sampling, null);

    }


    public static Uri getFileUri(File file) {
        return Uri.fromFile(file);
    }

    public static Uri getFileUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public static Uri getResUri(int resId, String packageName) {
        return Uri.parse("res://" + packageName + "/" + resId);
    }

    /**
     * 如果图像有一些调整选项，我们将调整后的图像放到缓存中，并使用不同的键。
     * 目前不支持对gif进行向下采样/调整。
     *
     * @param uri
     * @param draweeView
     * @param processor
     * @param width
     * @param height
     * @param listener
     */
    public static void loadUrl(Uri uri, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height, BaseControllerListener listener) {
        load(uri, draweeView, processor, width, height, listener);
    }

    public static void loadFile(String file, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height, BaseControllerListener listener) {
        load(getFileUri(file), draweeView, processor, width, height, listener);
    }

    public static void loadFile(File file, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height, BaseControllerListener listener) {
        load(getFileUri(file), draweeView, processor, width, height, listener);
    }

    public static void loadRes(Context context, int resId, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height, BaseControllerListener listener) {
        load(getResUri(resId, AppUtils.getAppPackageName(context)), draweeView, processor, width, height, listener);
    }

    public static void load(Uri uri, SimpleDraweeView draweeView, BasePostprocessor processor, int width, int height, BaseControllerListener listener) {
        ResizeOptions resizeOptions = null;
        if (width > 0 && height > 0) {
            resizeOptions = new ResizeOptions(width, height);
        }
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setPostprocessor(processor)
                        .setResizeOptions(resizeOptions)
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setControllerListener(listener)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();


        draweeView.setController(controller);
    }

    /**
     * 当设置roundAsCircle为true无效时,采用这个方法,常用在gif的圆形效果上
     * <p>
     * 或者在xml中设置:fresco:roundWithOverlayColor="@color/you_color_id"
     * "you_color_id"是指你的背景色，这样也可以实现圆角、圆圈效果
     * <p>
     * roundAsCircle的局限性:
     * 当使用BITMAP_ONLY（默认）模式时的限制：
     * 并非所有的图片分支部分都可以实现圆角，目前只有占位图片和实际图片可以实现圆角，我们正在努力为背景图片实现圆角功能。
     * 只有BitmapDrawable 和 ColorDrawable类的图片可以实现圆角。我们目前不支持包括NinePatchDrawable和 ShapeDrawable在内的其他类型图片。（无论他们是在XML或是程序中声明的）
     * 动画不能被圆角。
     * 由于Android的BitmapShader的限制，当一个图片不能覆盖全部的View的时候，边缘部分会被重复显示，而非留白。对这种情况可以使用不同的缩放类型
     * （比如centerCrop）来保证图片覆盖了全部的View。 OVERLAY_COLOR模式没有上述限制，但由于这个模式使用在图片上覆盖一个纯色图层的方式来模拟圆角效果，
     * 因此只有在图标背景是静止的并且与图层同色的情况下才能获得较好的效果。
     *
     * @param draweeView
     * @param bgColor    圆形遮罩的颜色,应该与背景色一致
     */
    public static void setCircle(SimpleDraweeView draweeView, int bgColor) {
        RoundingParams roundingParams = RoundingParams.asCircle();//这个方法在某些情况下无法成圆,比如gif
        roundingParams.setOverlayColor(bgColor);//加一层遮罩
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }
    public static  Bitmap returnBitmap(Uri uri) {
        if (uri==null){
            return null;
        }else {
            return BitmapFactory.decodeFile(((FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()))).getFile().getPath());
        }
    }

    /**
     * 暂停网络请求
     * 在listview快速滑动时使用
     */
    public static void pause() {
        Fresco.getImagePipeline().pause();
    }


    /**
     * 获取缓存大小
     */
    public static String getCacheSize(){
        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        return getFormatSize(Fresco.getImagePipelineFactory().getMainFileCache().getSize());
    }


    /**
     * 恢复网络请求
     * 当滑动停止时使用
     */
    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

    /**
     * 清除磁盘缓存
     */
    public static void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }


    /**
     * 清理内存缓存
     */
    public static void clearCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清除单张图片的磁盘缓存
     *
     * @param url
     */
    public static void clearCacheByUrl(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        // imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        //imagePipeline.evictFromCache(uri);//这个包含了从内存移除和从硬盘移除
    }

    /**
     * 清除SimpleDraweeView中的缓存
     *
     * @param imagePath
     */
    public static void clearCacheOfSimpleDraweeView(String imagePath) {
        if (imagePath == null) {
            return;
        }
        Uri imgurl = Uri.parse(imagePath);
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);

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