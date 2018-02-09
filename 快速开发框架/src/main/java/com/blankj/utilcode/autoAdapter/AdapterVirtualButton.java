package com.blankj.utilcode.autoAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 适配虚拟按键
 * Created by win7 on 2016/9/9.
 */
public class AdapterVirtualButton {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    public static void assistActivity(View content,Context context) {
        new AdapterVirtualButton(content,context);
    }
    private boolean isKeyBordVisiable;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AdapterVirtualButton(View content, final Context context) {
        mChildOfContent = content;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(context);
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(Context context) {
        int usableHeightNow = computeUsableHeight();


        if (usableHeightNow != usableHeightPrevious&&checkDeviceHasNavigationBar(context)) {
            //如果两次高度不一致


//            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                // keyboard probably just became visible
//                isKeyBordVisiable=true;
//            } else {
//                // keyboard probably just became hidden
//                isKeyBordVisiable=false;
//            }
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

  //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        if(context==null){
            return false;
        }
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 获取导航按键高度
     * @param context
     * @return
     */
    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize =getAppUsableScreenSize(context);
        Point realScreenSize =getRealScreenSize(context);
// navigation bar on the right
        if(appUsableSize.x< realScreenSize.x) {
            return new Point(realScreenSize.x- appUsableSize.x,appUsableSize.y);
        }
// navigation bar at the bottom
        if(appUsableSize.y< realScreenSize.y) {
            return new Point(appUsableSize.x,realScreenSize.y- appUsableSize.y);
        }
// navigation bar is not present
        return new Point();
    }

    /**
     * 获取App使用的尺寸
     * @param context
     * @return
     */
    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size =new Point();
        display.getSize(size);
        return size;
    }

    /**
     * 获取真实的屏幕尺寸
     * @param context
     * @return
     */
    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size =new Point();
        if(Build.VERSION.SDK_INT>=17) {
            display.getRealSize(size);
        }else if(Build.VERSION.SDK_INT>=14) {
            try{
                size.x= (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y= (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            }catch(IllegalAccessException e) {}catch(InvocationTargetException e) {}catch(NoSuchMethodException e) {}
        }
        return size;
    }



}