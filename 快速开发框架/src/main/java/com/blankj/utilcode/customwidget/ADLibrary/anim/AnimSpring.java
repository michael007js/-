package com.blankj.utilcode.customwidget.ADLibrary.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.blankj.utilcode.customwidget.ADLibrary.AdConstant;
import com.blankj.utilcode.customwidget.ADLibrary.AnimDialogUtils;
import com.blankj.utilcode.customwidget.ADLibrary.utils.DisplayUtil;
import com.blankj.utilcode.customwidget.Layout.LayoutKugou.facebook.rebound.SimpleSpringListener;
import com.blankj.utilcode.customwidget.Layout.LayoutKugou.facebook.rebound.Spring;
import com.blankj.utilcode.customwidget.Layout.LayoutKugou.facebook.rebound.SpringConfig;
import com.blankj.utilcode.customwidget.Layout.LayoutKugou.facebook.rebound.SpringSystem;

/**
 * Created by aaron on 16/8/3.
 * 弹性动画操作类
 */
public class AnimSpring {

    public static AnimSpring animSpring = null;
    public static SpringSystem springSystem = null;
    public SpringConfig springConfig = SpringConfig.fromOrigamiTensionAndFriction(8, 2);

    public static AnimSpring getInstance() {
        if (springSystem == null) {
            springSystem = SpringSystem.create();
        }
        if (animSpring == null) {
            animSpring = new AnimSpring();
        }

        return animSpring;
    }


    // #################### 弹窗打开时的动画效果 #############################
    /**
     * 打开时的动画效果
     * @param animType
     * @param animContainer
     */
    public void startAnim(final int animType, final View animContainer, double bounciness, double speed) {
        springConfig = SpringConfig.fromOrigamiTensionAndFriction(bounciness, speed);
        // 常量类型动画效果
        if (AdConstant.isConstantAnim(animType)) {
            startConstantAnim(animType, animContainer);
        } else if (AdConstant.isCircleAnim(animType)) {
            startCircleAnim(animType, animContainer);
        } else {
            animContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开始动画-定义动画开始角度
     * @param animType
     * @param animContainer
     */
    public void startCircleAnim(final int animType, final View animContainer) {
        double radius = Math.sqrt(DisplayUtil.screenhightPx * DisplayUtil.screenhightPx + DisplayUtil.screenWidthPx * DisplayUtil.screenWidthPx);
        double heightY = - Math.sin(Math.toRadians(animType)) * radius;
        double widthX = Math.cos(Math.toRadians(animType)) * radius;

        Spring tranSpring = springSystem.createSpring();
        Spring tranSpring1 = springSystem.createSpring();
        tranSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringActivate(Spring spring) {
                animContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSpringUpdate(Spring spring) {
                animContainer.setTranslationX((float) spring.getCurrentValue());
            }
        });

        tranSpring1.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringActivate(Spring spring) {
                animContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSpringUpdate(Spring spring) {
                animContainer.setTranslationY((float) spring.getCurrentValue());
            }
        });

        tranSpring.setSpringConfig(springConfig);
        tranSpring1.setSpringConfig(springConfig);
        tranSpring.setCurrentValue(widthX);
        tranSpring.setEndValue(0);
        tranSpring1.setCurrentValue(heightY);
        tranSpring1.setEndValue(0);
    }


    /**
     * 开始动画-固定类型动画
     */
    public void startConstantAnim(final int animType, final View animContainer) {

        if (animType == AdConstant.ANIM_DOWN_TO_UP) {
            startCircleAnim(270, animContainer);
        } else if (animType == AdConstant.ANIM_UP_TO_DOWN){
            startCircleAnim(90, animContainer);
        } else if (animType == AdConstant.ANIM_LEFT_TO_RIGHT) {
            startCircleAnim(180, animContainer);
        } else if (animType == AdConstant.ANIM_RIGHT_TO_LEFT) {
            startCircleAnim(0, animContainer);
        } else if (animType == AdConstant.ANIM_UPLEFT_TO_CENTER) {
            startCircleAnim(135, animContainer);
        } else if (animType == AdConstant.ANIM_UPRIGHT_TO_CENTER) {
            startCircleAnim(45, animContainer);
        } else if (animType == AdConstant.ANIM_DOWNLEFT_TO_CENTER) {
            startCircleAnim(225, animContainer);
        } else if (animType == AdConstant.ANIM_DOWNRIGHT_TO_CENTER) {
            startCircleAnim(315, animContainer);
        }
    }

    // ############################## 关闭时的动画效果 ##################################

    /**
     * 退出时的动画
     * @param animType
     * @param animDialogUtils
     */
    public void stopAnim(int animType, final AnimDialogUtils animDialogUtils) {
        if (animDialogUtils == null) {
            return;
        }

        if (animType == AdConstant.ANIM_STOP_TRANSPARENT) {
            animDialogUtils.getAnimContainer().animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animDialogUtils.getAndroidContentView().removeView(animDialogUtils.getRootView());
                    animDialogUtils.setShowing(false);
                }
            }).setDuration(500).setInterpolator(new AccelerateInterpolator()).start();
        } else {
            animDialogUtils.getAndroidContentView().removeView(animDialogUtils.getRootView());
            animDialogUtils.setShowing(false);
        }
    }

    /**
     * 退出时的动画
     * @param animType
     */
    public void stopAnim(int animType,View view) {
        if (view!=null){
            if (animType == AdConstant.ANIM_STOP_TRANSPARENT) {
                view.animate().alpha(0).setDuration(1000).setInterpolator(new AccelerateInterpolator()).start();
            }
        }
    }
}
