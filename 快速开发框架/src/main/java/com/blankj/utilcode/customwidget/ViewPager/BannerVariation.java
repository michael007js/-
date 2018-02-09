package com.blankj.utilcode.customwidget.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.R;
import com.blankj.utilcode.customwidget.banner.Banner;
import com.blankj.utilcode.customwidget.banner.listener.OnBannerListener;
import com.blankj.utilcode.customwidget.banner.loader.ImageLoaderInterface;
import com.blankj.utilcode.util.$;

import java.util.ArrayList;
import java.util.List;


/*

step1:

         List<String> list1 = new ArrayList<>();
        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
        list1.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2699494154,1003505126&fm=173&s=F9B4099844803FF914A5D28C0300F085&w=640&h=449&img.JPG");
        banner
                .setImages(list1)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setDelayTime(3000)
                .setImageLoader(new ImageLoaderInterface() {
                    @Override
                    public void displayImage(Context context, Object path, View imageView) {
                        imageView.setTag(R.id.glide_tag, ((String) path));
                        addImageViewList(GlidUtils.downLoader(false, (ImageView) imageView, context));
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (getBaseFragmentActivityContext() != null) {
                                    getBaseFragmentActivityContext().startActivity(new Intent(getBaseFragmentActivityContext(), ActivitySaleAndSeckill.class)
                                            .putExtra("type", type));
                                }
                            }
                        });
                    }

                    @Override
                    public View createImageView(Context context) {
                        return null;
                    }
                });
        banner.start();
        banner.startAutoPlay();

step2:
        @Override
        protected void onFragmentVisibleChange(boolean isVisible) {
            super.onFragmentVisibleChange(isVisible);
            if (isLoad && banner != null) {
                if (isVisible) {
                    if (banner != null) {
                        banner.startAutoPlay();
                    }
                    if (banner != null) {
                        banner.startAutoPlay();
                    }
                } else {
                    if (banner != null) {
                        banner.stopAutoPlay();
                    }
                    if (banner != null) {
                        banner.stopAutoPlay();
                    }
                }
            }
        }


* */

/**
 * Created by leilei on 2017/9/21.
 */

public class BannerVariation extends LinearLayout {
    Banner banner;
    View view;
    List<String> list = new ArrayList<>();

    public void clear() {
        if (banner!=null){
            banner.stopAutoPlay();
            banner.releaseBanner();
        }

        if (list != null) {
            list.clear();
        }
        list = null;
        view = null;
        banner = null;
    }

    public BannerVariation(Context context) {
        super(context);
        init(context);
    }

    public BannerVariation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerVariation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_banner_variation, null);
        banner = $.f(view, R.id.banner_layout_banner_variation);
        this.addView(view);
    }


    /**
     * 设置轮播样式（默认为CIRCLE_INDICATOR）
     * BannerConfig.NOT_INDICATOR	不显示指示器和标题
     * BannerConfig.CIRCLE_INDICATOR	显示圆形指示器
     * BannerConfig.NUM_INDICATOR	显示数字指示器
     * BannerConfig.NUM_INDICATOR_TITLE	显示数字指示器和标题
     * BannerConfig.CIRCLE_INDICATOR_TITLE	显示圆形指示器和标题（垂直显示）
     * BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE	显示圆形指示器和标题（水平显示）
     *
     * @param bannerStyle
     */
    public BannerVariation setBannerStyle(int bannerStyle) {
        banner.setBannerStyle(bannerStyle);
        return this;
    }

    /**
     * 设置指示器位置（没有标题默认为右边,有标题时默认左边）
     * BannerConfig.LEFT	指示器居左
     * BannerConfig.CENTER	指示器居中
     * BannerConfig.RIGHT	指示器居右
     */
    public BannerVariation setIndicatorGravity(int type) {
        banner.setIndicatorGravity(type);
        banner.releaseBanner();
        return this;
    }

    /**
     * 设置是否自动轮播（默认自动）
     *
     * @param isAutoPlay
     * @return
     */
    public BannerVariation isAutoPlay(boolean isAutoPlay) {
        banner.isAutoPlay(isAutoPlay);
        return this;
    }

    /**
     * 设置是否允许手动滑动轮播图（默认true）
     *
     * @param isScroll
     * @return
     */
    public BannerVariation setViewPagerIsScroll(boolean isScroll) {
        banner.isAutoPlay(isScroll);
        return this;
    }

    /**
     * 更新图片和标题
     *
     * @param imageUrls
     * @param titles
     * @return
     */
    public BannerVariation update(List<?> imageUrls, List<String> titles) {
        banner.update(imageUrls, titles);
        return this;
    }

    /**
     * 更新图片
     *
     * @param imageUrls
     * @return
     */
    public BannerVariation update(List<?> imageUrls) {
        banner.update(imageUrls);
        return this;
    }

    /**
     * 开始轮播
     * 此方法只作用于banner加载完毕-->需要在start()后执行
     *
     * @return
     */
    public BannerVariation startAutoPlay() {
        banner.startAutoPlay();
        return this;
    }


    /**
     * 开始轮播
     * 此方法只作用于banner加载完毕-->需要在start()后执行
     *
     * @return
     */
    public BannerVariation stopAutoPlay() {
        banner.stopAutoPlay();
        return this;
    }

    /**
     * 开始进行banner渲染
     *
     * @return
     */
    public BannerVariation start() {
        banner.start();
        return this;
    }

    /**
     * 同viewpager的方法作用一样
     *
     * @return
     */
    public BannerVariation setOffscreenPageLimit(int limit) {
        banner.setOffscreenPageLimit(limit);
        return this;
    }


    /**
     * 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
     *
     * @return
     */
    public BannerVariation setBannerTitles(List<String> titles) {
        banner.setBannerTitles(titles);
        return this;
    }


    /**
     * 设置轮播图片间隔时间（单位毫秒，默认为2000）
     *
     * @return
     */
    public BannerVariation setDelayTime(int time) {
        banner.setDelayTime(time);
        return this;
    }

    /**
     * 设置轮播图片(所有设置参数方法都放在此方法之前执行)
     *
     * @return
     */
    public BannerVariation setImages(List<?> imageUrls) {
        banner.setImages(imageUrls);
        banner.setOffscreenPageLimit(imageUrls.size());
        return this;
    }

    /**
     * 设置点击事件，下标是从0开始
     *
     * @return
     */
    public BannerVariation setOnBannerListener(OnBannerListener listener) {
        banner.setOnBannerListener(listener);
        return this;
    }

    /**
     * 设置图片加载器
     *
     * @return
     */
    public BannerVariation setImageLoader(ImageLoaderInterface imageLoader) {
        banner.setImageLoader(imageLoader);
        return this;
    }

    /**
     * 设置viewpager的滑动监听
     *
     * @return
     */
    public BannerVariation setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        banner.setOnPageChangeListener(onPageChangeListener);
        return this;
    }
}
