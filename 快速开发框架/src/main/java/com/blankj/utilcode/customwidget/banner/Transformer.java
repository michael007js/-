package com.blankj.utilcode.customwidget.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.blankj.utilcode.customwidget.banner.transformer.AccordionTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.BackgroundToForegroundTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.CubeInTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.CubeOutTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.DefaultTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.DepthPageTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.FlipHorizontalTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.FlipVerticalTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.ForegroundToBackgroundTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.RotateDownTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.RotateUpTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.ScaleInOutTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.StackTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.TabletTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.ZoomInTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.ZoomOutSlideTransformer;
import com.blankj.utilcode.customwidget.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
