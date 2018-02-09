package com.blankj.utilcode.activity;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.blankj.utilcode.Glid.GlidUtils;
import com.blankj.utilcode.autoAdapter.AdapterVirtualButton;
import com.blankj.utilcode.autoAdapter.AutoUtils;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.ADLibrary.AdConstant;
import com.blankj.utilcode.customwidget.ADLibrary.anim.AnimSpring;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.Layout.LayoutSwipeback.lib.app.SwipeBackActivity;
import com.blankj.utilcode.fresco.FrescoUtils;
import com.blankj.utilcode.okhttp.OkHttpUtils;
import com.blankj.utilcode.util.ActivityManagerUtils;
import com.blankj.utilcode.util.BadgerUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


/**
 * Created by leilei on 2017/7/25.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    View view;
    boolean isWithAnimation = false;
    public WeakReference<Context> weakReference;
    public WeakReference<BaseActivity> weakReferenceActivity;
    List<RequestModel> callList = new ArrayList<>();
    List<ImageView> imageViewList = new ArrayList<>();

    boolean isRegisterEventBus = false;
    YWLoadingDialog ywLoadingDialog;

    public Context getBaseActivityContext() {
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public BaseActivity getBaseActivity() {
        if (weakReferenceActivity != null) {
            return weakReferenceActivity.get();
        }
        return null;
    }

    public void showLoading(String title) {
        if (ywLoadingDialog == null) {
            ywLoadingDialog = new YWLoadingDialog(weakReference.get());
        }
        ywLoadingDialog.disMiss();
        ywLoadingDialog.show();
        if (!StringUtils.isEmpty(title)) {
            ywLoadingDialog.setTitle(title);
        }
    }


    public void dissmissLoading() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
    }



    @Override
    protected void onUserLeaveHint() {
        //TODO 当用户的操作使一个activity准备进入后台时，此方法会像activity的生命周期的一部分被调用。
        //TODO 例如，当用户按下Home键，但是当来电导致来电activity自动占据前台，将不会被回调。
        super.onUserLeaveHint();
    }

    @Override
    public void onUserInteraction() {
        //TODO  activity在分发各种事件的时候会调用该方法，
        //TODO 注意：启动另一个activity,Activity#onUserInteraction()会被调用两次，一次是activity捕获到事件，另一次是调用Activity#onUserLeaveHint()之前会调用Activity#onUserInteraction()。
        super.onUserInteraction();
    }


    /**
     * 添加网络请求
     *
     * @param requestModel
     */
    public void addRequestCall(RequestModel requestModel) {
        if (callList != null) {
            callList.add(requestModel);
        }
    }

    /**
     * 清除网络请求
     */
    public void clearRequestCall(String string) {
        for (int i = 0; i < callList.size(); i++) {
            if (EmptyUtils.isNotEmpty(callList.get(i))) {
                if (string.equals(callList.get(i).string)) {
                    if (!callList.get(i).call.isCanceled()) {
                        callList.get(i).call.cancel();
                        callList.get(i).call = null;
                        callList.remove(i);
                    }
                }
            }
        }
    }


    /**
     * 清除网络请求
     */
    public void clearRequestCall() {
        for (int i = 0; i < callList.size(); i++) {
            if (EmptyUtils.isNotEmpty(callList.get(i))) {
                callList.get(i).call.cancel();
                callList.get(i).call = null;
                callList.remove(i);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        weakReference = new WeakReference<Context>(this);
        weakReferenceActivity = new WeakReference<>(this);
        ActivityManagerUtils.getActivityManager().addActivity(this);
        /*调整尺寸*/
        AutoUtils.setSize(this, true);
        /*请求权限*/
//        requestPermissions();
    }





    /**
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于100：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于100：键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 保存图片引用
     *
     * @param imageView
     */
    public void addImageViewList(ImageView imageView) {
        imageViewList.add(imageView);
    }

    /**
     * 清除图片引用
     */
    public void clearImageViewList() {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (imageViewList.get(i) != null) {
                GlidUtils.clearImg(imageViewList.get(i));
                imageViewList.set(i, null);
            }
        }
        if (imageViewList != null) {
            imageViewList.clear();
        }
        imageViewList = null;

    }

    /**
     * 自定义
     *
     * @param view
     * @param isWithAnimation 是否带动画运行
     * @param isSwipeBack     是否侧滑返回
     */
    public void customInit(View view, boolean isWithAnimation, boolean isSwipeBack, boolean isRegisterEventBus) {
        this.view = view;
        this.isWithAnimation = isWithAnimation;
        this.isRegisterEventBus = isRegisterEventBus;
        /*点击edittext之外的地方隐藏键盘*/
        KeyboardUtils.wrap(this);
        /*侧滑返回*/
        setSwipeBackEnable(isSwipeBack);
        if (view != null) {
           /*适配虚拟按键*/
            AdapterVirtualButton.assistActivity(view, this);
            /*带动画运行*/
            startWithAnimation(view);
        }
        if (this.isRegisterEventBus) {
            EventBus.getDefault().register(this);
        }
//        Eyes.setStatusBarColor(this,getResources().getColor(R.color.transparent));//沉浸式
    }

    /**
     * 自定义
     *
     * @param view
     * @param isWithAnimation 是否带动画运行
     */
    public void customInit(View view, boolean isWithAnimation, boolean isSwipeBack, boolean isRegisterEventBus, boolean isHideKeyBroad) {
        this.view = view;
        this.isWithAnimation = isWithAnimation;
        this.isRegisterEventBus = isRegisterEventBus;
        setSwipeBackEnable(isSwipeBack);
        /*点击edittext之外的地方隐藏键盘*/
        if (isHideKeyBroad) {
            KeyboardUtils.wrap(this);
        }
        if (view != null) {
           /*适配虚拟按键*/
            AdapterVirtualButton.assistActivity(view, this);
            /*带动画运行*/
            startWithAnimation(view);
        }
        if (this.isRegisterEventBus) {
            EventBus.getDefault().register(this);
        }
    }


    /**
     * 请求权限
     */
    public void requestPermissions(List<PermissionItem> permissionItemList,PermissionCallback permissionCallback) {
        PermissionUtils.requestRunTimePermission(getBaseActivityContext(),permissionItemList,permissionCallback);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /**
                 * 回调只有当我们程序中的所有UI组件全部不可见的时候才会触发，
                 * 这和onStop()方法还是有很大区别的，
                 * 因为onStop()方法只是当一个Activity完全不可见的时候就会调用，
                 * 比如说用户打开了我们程序中的另一个Activity。因此，
                 * 我们可以在onStop()方法中去释放一些Activity相关的资源，
                 * 比如说取消网络连接或者注销广播接收器等，
                 * 但是像UI相关的资源应该一直要等到onTrimMemory(TRIM_MEMORY_UI_HIDDEN)这个回调之后才去释放，
                 * 这样可以保证如果用户只是从我们程序的一个Activity回到了另外一个Activity，
                 * 界面相关的资源都不需要重新加载，
                 * 从而提升响应速度。
                 * */

                TRIM_MEMORY_UI_HIDDEN();
                clearRequestCall();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经有点低了，
                 * 系统可能会开始根据LRU缓存规则来去杀死进程了。
                 * */

                TRIM_MEMORY_RUNNING_MODERATE();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经非常低了，
                 * 我们应该去释放掉一些不必要的资源以提升系统的性能，
                 * 同时这也会直接影响到我们应用程序的性能。
                 * */

                TRIM_MEMORY_RUNNING_LOW();
                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /**
                 * 表示应用程序仍然正常运行，
                 * 但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
                 * 这个时候我们应当尽可能地去释放任何不必要的资源，
                 * 不然的话系统可能会继续杀掉所有缓存中的进程，
                 * 并且开始杀掉一些本来应当保持运行的进程，
                 * 比如说后台运行的服务。
                 * */
                TRIM_MEMORY_RUNNING_CRITICAL();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                /**
                 *  表示手机目前内存已经很低了，
                 *  系统准备开始根据LRU缓存来清理进程。
                 *  这个时候我们的程序在LRU缓存列表的最近位置，
                 *  是不太可能被清理掉的，
                 *  但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
                 *  从而让我们的程序更长时间地保留在缓存当中，
                 *  这样当用户返回我们的程序时会感觉非常顺畅，
                 *  而不是经历了一次重新启动的过程。
                 * */
                TRIM_MEMORY_BACKGROUND();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的中间位置，
                 * 如果手机内存还得不到进一步释放的话，
                 * 那么我们的程序就有被系统杀掉的风险了。
                 * */
                TRIM_MEMORY_MODERATE();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的最边缘位置，
                 * 系统会最优先考虑杀掉我们的应用程序，
                 * 在这个时候应当尽可能地把一切可以释放的东西都进行释放。
                 * */
                TRIM_MEMORY_COMPLETE();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BadgerUtils.removeCount(weakReference);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.dismiss();
        }
        ywLoadingDialog = null;
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this);
        }
        if (view != null) {
            stopWithAnimation(view);
        }
        view = null;
        if (weakReference != null) {
            weakReference.clear();
        }
        weakReference = null;
        if (weakReferenceActivity != null) {
            weakReferenceActivity.clear();
        }
        weakReferenceActivity.clear();

        if (callList != null) {
            clearRequestCall();
            callList.clear();
        }
        callList = null;
        FrescoUtils.clearCache();
        clearImageViewList();
        OkHttpUtils.getInstance().cancelTag(getLocalClassName());
        ActivityManagerUtils.getActivityManager().finishActivity(this);
        Runtime.getRuntime().gc();

    }


    /**
     * 启动动画
     *
     * @param view
     */
    protected void startWithAnimation(View view) {
        if (view != null && isWithAnimation) {
            AnimSpring.getInstance().startAnim(AdConstant.ANIM_DOWN_TO_UP, view, 1, 5);
        }
    }

    /**
     * 关闭动画
     *
     * @param view
     */
    protected void stopWithAnimation(View view) {
        if (view != null && isWithAnimation) {
            AnimSpring.getInstance().stopAnim(AdConstant.ANIM_STOP_TRANSPARENT, view);
        }
    }

    /**
     * 沉浸式状态栏
     */
    protected void immersiveStatusBar() {
        ImmersionBar.with(this)
//                .transparentStatusBar()  //透明状态栏，不写默认透明色
//                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
//                .statusBarColor(R.color.mainColor)     //状态栏颜色，不写默认透明色
//                .navigationBarColor(R.color.mainColor) //导航栏颜色，不写默认黑色
//                .barColor(R.color.mainColor)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .flymeOSStatusBarFontColor(R.color.mainColor)  //修改flyme OS状态栏字体颜色
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//                .hideBar(BarHide.FLAG_SHOW_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//                .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
//                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
//                .supportActionBar(true) //支持ActionBar使用
//                .statusBarColorTransform(R.color.mainColor)  //状态栏变色后的颜色
//                .navigationBarColorTransform(R.color.mainColor) //导航栏变色后的颜色
//                .barColorTransform(R.color.mainColor)  //状态栏和导航栏变色后的颜色
//                .removeSupportView(toolbar)  //移除指定view支持
//                .removeSupportAllView() //移除全部view支持
                .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
//                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4手机导航栏颜色，默认为true
                .fixMarginAtBottom(true)   //当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
//                .reset()  //重置所以沉浸式参数
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .init();  //必须调用方可沉浸式
    }

    protected abstract void TRIM_MEMORY_UI_HIDDEN();

    protected void TRIM_MEMORY_RUNNING_MODERATE() {
    }

    protected void TRIM_MEMORY_RUNNING_LOW() {
    }

    protected void TRIM_MEMORY_RUNNING_CRITICAL() {
    }

    protected void TRIM_MEMORY_BACKGROUND() {
    }

    protected void TRIM_MEMORY_MODERATE() {
    }

    protected void TRIM_MEMORY_COMPLETE() {
    }

}
