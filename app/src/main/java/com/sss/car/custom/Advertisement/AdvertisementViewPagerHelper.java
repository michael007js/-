package com.sss.car.custom.Advertisement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blankj.utilcode.JieCaoPlayer.JCBuriedPointStandard;
import com.blankj.utilcode.JieCaoPlayer.JCMediaManager;
import com.blankj.utilcode.JieCaoPlayer.JCVideoPlayer;
import com.blankj.utilcode.JieCaoPlayer.JCVideoPlayerStandard;
import com.blankj.utilcode.ViewpagerHelper.bean.PageBean;
import com.blankj.utilcode.ViewpagerHelper.callback.PageHelperListener;
import com.blankj.utilcode.ViewpagerHelper.indicator.ZoomIndicator;
import com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.util.$;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadPoolUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.R;
import com.sss.car.custom.Advertisement.Model.AdvertisementModel;

import java.util.HashMap;
import java.util.List;

import static android.R.attr.data;
import static com.sss.car.Config.url;




/*
 step1:
  <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager
            xmlns:zsr="http://schemas.android.com/apk/res-auto"
            android:id="@+id/banner"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:clipChildren="false"
            zsr:isloop="false"
            zsr:switchtime="5000">

        </com.blankj.utilcode.ViewpagerHelper.view.BannerViewPager>

        <com.blankj.utilcode.ViewpagerHelper.indicator.ZoomIndicator
            xmlns:zsr="http://schemas.android.com/apk/res-auto"
            android:id="@+id/bottom_zoom_arc"
            android:layout_width="match_parent"
            android:layout_height="30dp"
            android:gravity="center"
            android:layout_marginBottom="10dp"
            android:layout_alignParentBottom="true"
            zsr:zoom_alpha_min="0.5"
            zsr:zoom_leftmargin="10dp"
            zsr:zoom_max="1.5"
            zsr:zoom_selector="@drawable/bottom_circle"/>
    </RelativeLayout>

step2:
    AdvertisementViewPager advertisementViewPagerHelper=new AdvertisementViewPager();
    advertisementViewPagerHelper .setView(banner,bottomZoomArc) .setData(getBaseActivity(),getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight(),list);
* */

/**
 * 自定义带视频播放的广告显示Viewpager
 * Created by leilei on 2017/11/27.
 */

@SuppressWarnings("ALL")
public class AdvertisementViewPagerHelper implements JCBuriedPointStandard {
    private BannerViewPager bannerViewPager;
    private ZoomIndicator zoomIndicator;
    private ThreadPoolUtils threadPoolUtils;
    private OnAdvertisementClickCallBack onAdvertisementClickCallBack;

    public void onDestroy() {
        zoomIndicator = null;
        if (threadPoolUtils != null) {
            threadPoolUtils.shutDownNow();
        }
        if (bannerViewPager != null) {
            bannerViewPager.onReusme();
        }
        bannerViewPager = null;
        threadPoolUtils = null;
    }

    public void onPause() {
        JCVideoPlayer.releaseAllVideos();
        if (bannerViewPager != null) {
            bannerViewPager.onPause();
        }

    }


    public void pausePlay(){
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                JCMediaManager.instance().mediaPlayer.pause();
            }
        }catch (IllegalStateException e){

        }
    }


    public void onReusme() {
        if (bannerViewPager != null) {
            bannerViewPager.onReusme();
        }
    }

    public void setOnAdvertisementClickCallBack(OnAdvertisementClickCallBack onAdvertisementClickCallBack) {
        this.onAdvertisementClickCallBack = onAdvertisementClickCallBack;
    }

    /**
     * 设置视图
     *
     * @param bannerViewPager
     * @param zoomIndicator
     * @return
     */
    public AdvertisementViewPagerHelper setView(BannerViewPager bannerViewPager, ZoomIndicator zoomIndicator) {
        this.bannerViewPager = bannerViewPager;
        this.zoomIndicator = zoomIndicator;
        return this;
    }

    /**
     * 绑定数据
     *
     * @param activity
     * @param width    图片宽度
     * @param height   图片高度
     * @param list     数据集合
     */
    public AdvertisementViewPagerHelper setData(final Activity activity, final int width, final int height, final List<AdvertisementModel> list, final boolean isAutoPlay) {
        if (bannerViewPager != null) {
            threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.CachedThread, 2);
            bannerViewPager.setOffscreenPageLimit(10);
            //bannerViewPager.setPageTransformer(false, new MzTransformer());
            PageBean bean = new PageBean.Builder<AdvertisementModel>()
                    .setDataObjects(list)
                    .setIndicator(zoomIndicator)
                    .builder();
            //https://github.com/LillteZheng/ViewPagerHelper
            bannerViewPager.setPageListener(bean, R.layout.item_advertisement, new PageHelperListener() {
                @Override
                public void getItemView(View view, final Object data) {
//                    LogUtils.e(((AdvertisementModel) data).picture);
                    SimpleDraweeView simpleDraweeView = $.f(view, R.id.pic_item_advertisement);
//                    final JCVideoPlayerStandard jcVideoPlayerStandard = $.f(view, R.id.video_item_advertisement);
//                    if ("1".equals(((AdvertisementModel) data).is_video)) {
                        simpleDraweeView.setVisibility(View.VISIBLE);
//                        jcVideoPlayerStandard.setVisibility(View.GONE);
                        FrescoUtils.showImage(false, width, height, Uri.parse(((AdvertisementModel) data).picture), simpleDraweeView, 0f);
                        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onAdvertisementClickCallBack != null) {
                                    onAdvertisementClickCallBack.onClick(((AdvertisementModel) data).link_url);
                                }
                            }
                        });
//                    } else {
//                        simpleDraweeView.setVisibility(View.GONE);
//                        jcVideoPlayerStandard.setVisibility(View.VISIBLE);
//                        //设置视频地址、缩略图地址、标题
//                        jcVideoPlayerStandard.setUp(((AdvertisementModel) data).picture, "");
//                        threadPoolUtils.submit(new Runnable() {
//                            @Override
//                            public void run() {
//                                createVideoThumbnail(((AdvertisementModel) data).picture,
//                                        activity.getWindowManager().getDefaultDisplay().getWidth(),
//                                        activity.getWindowManager().getDefaultDisplay().getHeight(), new OnCreateVideoThumbnailCallBack() {
//                                            @Override
//                                            public void onBitmap(final Bitmap bitmap) {
//                                                activity.runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        jcVideoPlayerStandard.thumbImageView.setImageBitmap(bitmap);
//                                                    }
//                                                });
//                                            }
//                                        });
//                            }
//                        });
//                        if (isAutoPlay) {
//                            jcVideoPlayerStandard.performClick();
//                        }
//                    }
                }
            });
            bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pausePlay();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        return this;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Bitmap createVideoThumbnail(String url, int width, int height, OnCreateVideoThumbnailCallBack oncreateVideoThumbnailCallBack) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        if (oncreateVideoThumbnailCallBack != null) {
            oncreateVideoThumbnailCallBack.onBitmap(bitmap);
        }
        return bitmap;
    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {

    }

    @Override
    public void onClickBlank(String url, Object... objects) {

    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {

    }

    @Override
    public void onClickStartError(String url, Object... objects) {

    }

    @Override
    public void onClickStop(String url, Object... objects) {

    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickResume(String url, Object... objects) {

    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {

    }

    @Override
    public void onAutoComplete(String url, Object... objects) {

    }

    @Override
    public void onAutoCompleteFullscreen(String url, Object... objects) {

    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {

    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {

    }


    private interface OnCreateVideoThumbnailCallBack {
        void onBitmap(Bitmap bitmap);
    }


    public interface OnAdvertisementClickCallBack {
        void onClick(String url);
    }

}
