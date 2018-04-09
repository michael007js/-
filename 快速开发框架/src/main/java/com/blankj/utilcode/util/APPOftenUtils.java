package com.blankj.utilcode.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.R;
import com.blankj.utilcode.Service.MyNotificationService;
import com.blankj.utilcode.adapter.sssAdapter.SSS_Adapter;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.listener.OnOperItemClickL;
import com.blankj.utilcode.customwidget.Dialog.FlycoDialog.dialog.widget.ActionSheetDialog;
import com.blankj.utilcode.dao.CustomExceptionCallBack;
import com.blankj.utilcode.dao.LocationStatusListener;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.dao.QRCodeDataListener;
import com.blankj.utilcode.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;

import static android.R.attr.mode;


/**
 * APP开发常用的方法
 */
@SuppressWarnings("ALL")
public final class APPOftenUtils {


    private APPOftenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将文本内容放到系统剪贴板里。
     *
     * @param context
     * @param content
     */
    public static void copyTxt2Clipboard(Context context, String content) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(content);
    }

    /**
     * 从系统剪贴板里获取复制过的文本。
     *
     * @param context
     */
    public static CharSequence getTxtFromClipboard(Context context) {
        return ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).getText().toString();
    }


    /**
     * 验证各种导航地图是否安装
     * 高德 com.autonavi.minimap
     * 谷歌 com.google.android.apps.map
     * 百度 com.baidu.BaiduMap
     *
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isInstallMaps(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<>();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
                LogUtils.e(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 调用外部地图
     *
     * @param context
     * @param targetName
     * @param targetLat
     * @param targetLng
     */
    public static void navigation(final Context context, final String mySelfLat, final String myselfLng, final String targetName, final String targetLat, final String targetLng) {
        List<String> mapList = new ArrayList<>();
        mapList.add("com.autonavi.minimap");
        mapList.add("com.baidu.BaiduMap");
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            if (APPOftenUtils.isInstallMaps(context, mapList.get(i))) {
                temp.add(mapList.get(i));
                LogUtils.e(temp.get(i));
            }
        }

        if (temp.size() == 0) {
            ToastUtils.showShortToast(context, "您的手机上未安装任何地图");
            return;
        }

        final String[] title = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            if ("com.autonavi.minimap".equals(temp.get(i))) {
                title[i] = "高德地图";
            } else if ("com.baidu.BaiduMap".equals(temp.get(i))) {
                title[i] = "百度地图";
            }
        }


        final ActionSheetDialog dialog = new ActionSheetDialog(context, title, null)
                .isTitleShow(false)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("选择地图");
        dialog.titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if ("高德地图".equals(title[position])) {
                            Intent intent = new Intent();
                            intent.setData(Uri
                                    .parse("androidamap://route?sourceApplication="+AppUtils.getAppPackageName(context)+"&sname=我的位置"
                                            + "&slat=" + mySelfLat + "&slon=" + myselfLng  + "&dlat=" + targetLat
                                            + "&dlon=" + targetLng + "&dname="+targetName+"&dev=0&m=0&t=1&showType=1"));
                            context.startActivity(intent);
                        }
                        dialog.dismiss();
                        break;
                    case 1:
                        if ("百度地图".equals(title[position])) {
                            Intent intent = new Intent();
                            intent.setData(Uri.parse(


                                    "baidumap://map/direction?destination="+targetLat+","+ targetLng + "&mode=driving"


                            ));

                            context.startActivity(intent);
                        }
                        dialog.dismiss();
                        break;
                }
            }
        });
    }


    /**
     * 最小化返回桌面
     *
     * @param context
     */
    public static void smallBackToDesktop(Context context) {
        context.getApplicationContext().startActivity(IntentUtils.getHomeDesktopIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * textview下划线
     *
     * @param textView
     */
    public static TextView underLineOfTextView(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
        return textView;
    }

    /**
     * 设置背景
     * 在API16以前使用setBackgroundDrawable，在API16以后使用setBackground
     * API16<---->4.1
     *
     * @param view
     * @param drawable
     */
    public static void setBackgroundOfVersion(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API16，使用setBackground
            view.setBackground(drawable);
        } else {
            //Android系统小于API16，使用setBackground
            view.setBackgroundDrawable(drawable);
        }
    }


    /**
     * <service android:name="com.blankj.utilcode.Service.MyNotificationService"
     * android:label="@string/app_name"
     * android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
     * <intent-filter>
     * <action android:name="android.service.notification.NotificationListenerService" />
     * </intent-filter>
     * </service>
     * <p>
     * 初始化通知栏监听服务
     */
    public static void initNotificationServiceListener(WeakReference<Context> weakReference, CustomExceptionCallBack customExceptionCallBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (weakReference == null || weakReference.get() == null) {
                if (customExceptionCallBack != null) {
                    customExceptionCallBack.onEmptyException("initNotificationServiceListener,context is null");
                }
                return;
            }
            String string = Settings.Secure.getString(weakReference.get().getContentResolver(), "enabled_notification_listeners");
            LogUtils.e("已经允许使用通知权的应用:" + string);
            // 数据库中保存的格式：包名/服务名:包名/服务名，如：
            // com.example.notification/com.example.notification.NotificationService
            // :com.example.smartface/com.example.smartface.notification.SmartFaceListenerService
            if (StringUtils.isEmpty(string) || !string.contains(MyNotificationService.class.getName())) {
                ToastUtils.showShortToast(weakReference.get(), "请开启必须权限!");
                weakReference.get().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else {
                if (!ServiceUtils.isServiceRunning(weakReference.get(), MyNotificationService.class)) {
                    weakReference.get().startService(new Intent(weakReference.get(), MyNotificationService.class));
                }
            }
        }
    }

    /**
     * 初始化融云推送(搭配  APPOftenUtils.initNotificationServiceListener与UtilCodeApplication.initOnePxAlive()在ROOT模式下可实现程序保活)
     */
    public static void initRongYunPushService(Context context) {
        if (!ServiceUtils.isServiceRunning(context, io.rong.push.PushService.class)) {
            context.startService(new Intent(context, io.rong.push.PushService.class));
            LogUtils.e("initRongYunPushService");
        }
    }

    /**
     * 清空listview
     *
     * @param listView
     * @param sss_adapter
     * @param list
     */
    public static void clearListView(ListView listView, SSS_Adapter sss_adapter, List<?> list) {
        list.clear();
        listView.setAdapter(sss_adapter);
        sss_adapter.setList(list);
    }

    /**
     * 创建对话框
     *
     * @param context
     * @param title
     * @param onAskDialogCallBack
     */
    public static void createAskDialog(Context context, String title, final OnAskDialogCallBack onAskDialogCallBack) {
        final Dialog dialog = new Dialog(context, R.style.RcDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ask, null);
        TextView content_dialog_ask = $.f(view, R.id.content_dialog_ask);
        TextView cancel_dialog_ask = $.f(view, R.id.cancel_dialog_ask);
        TextView yes_dialog_ask = $.f(view, R.id.yes_dialog_ask);
        content_dialog_ask.setText(title);
        content_dialog_ask.setTextColor(context.getResources().getColor(R.color.textColor));
        cancel_dialog_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAskDialogCallBack != null) {
                    onAskDialogCallBack.onCancel(dialog);
                }
            }
        });
        yes_dialog_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAskDialogCallBack != null) {
                    onAskDialogCallBack.onOKey(dialog);
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();

    }

    /**
     * 创建相机与照片选择对话框
     *
     * @param weakReference
     * @param listener
     */
    public static void createPhotoChooseDialog(final int requestCode, final int maxSize, final WeakReference<Context> weakReference, final FunctionConfig functionConfig, final GalleryFinal.OnHanlderResultCallback listener) {
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        createPhotoChooseDialog(requestCode, maxSize, weakReference.get(), functionConfig, listener);
    }

    /**
     * <activity
     * android:name="com.blankj.utilcode.zxing.activity.CaptureActivity"
     * android:screenOrientation="portrait"
     * android:theme="@android:style/Theme.Black.NoTitleBar"/>
     * 开启二维码扫描界面
     *
     * @param context
     * @param qrCodeDataListener
     */
    public static void startQRScanView(final Context context, final QRCodeDataListener qrCodeDataListener) {
        new Thread() {
            @Override
            public void run() {
                context.startActivity(new Intent(context, CaptureActivity.class));
                try {
                    sleep(500);
                    CaptureActivity mCaptureActivity = (CaptureActivity) ActivityManagerUtils.getActivityManager().existActivity("com.blankj.utilcode.zxing.activity.CaptureActivity");
                    if (mCaptureActivity != null) {
                        if (mCaptureActivity.getmSSS_QRCodeDataListener() == null) {
                            mCaptureActivity.setSSS_QRCodeDataListener(qrCodeDataListener);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }


    /**
     * 创建相机与照片选择对话框
     *
     * @param requestCode
     * @param maxSize
     * @param context
     * @param functionConfig
     * @param listener
     */
    public static void createPhotoChooseDialog(final int requestCode, final int maxSize, final Context context, final FunctionConfig functionConfig, final GalleryFinal.OnHanlderResultCallback listener) {
        String[] stringItems = {"拍照", "从相册库选择"};
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null)
                .isTitleShow(false)
                .itemTextColor(Color.parseColor("#e83e41"))
                .setmCancelBgColor(Color.parseColor("#e83e41"))
                .cancelText(Color.WHITE);
        dialog.title("选择您的方式");
        dialog.titleTextSize_SP(14.5f).show();
        stringItems = null;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        GalleryFinal.openCamera(requestCode, functionConfig, listener);
                        dialog.dismiss();
                        break;
                    case 1:
                        GalleryFinal.openGalleryMuti(requestCode, maxSize, listener);
                        dialog.dismiss();
                        break;
                }
            }
        });

    }


    /**
     * 定位失败时创建对话框
     *
     * @param weakReference
     */
    public static void createLocationErrorTip(final WeakReference<Context> weakReference, String str, final LocationStatusListener locationStatusListener) {
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        String[] stringItems = {"检查网络", "检查GPS", "重新定位"};
        final ActionSheetDialog dialog = new ActionSheetDialog(weakReference.get(), stringItems, null);
        String[] temp = str.split(" ");
        if (temp.length > 0) {
            dialog.title(temp[0]);
        } else {
            dialog.title("请检查您的网络和GPS");
        }
        dialog.cancelText(weakReference.get().getResources().getColor(R.color.white))
                .itemTextColor(weakReference.get().getResources().getColor(R.color.mainColor))
                .titleTextSize_SP(14.5f).show();
        stringItems = null;
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        weakReference.get().startActivity(IntentUtils.getNetworkIntent());
                        break;
                    case 1:
                        weakReference.get().startActivity(IntentUtils.getGPSIntent());

                        break;
                    case 2:
                        if (locationStatusListener != null) {
                            locationStatusListener.onReLocation(weakReference.get());
                        }
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    /**
     * 开启沉浸式状态栏
     *
     * @param activity
     */
    public static void startImmersionStatusBar(Activity activity) {
        /**
         * 沉浸式状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // status_bar底色
    public static void setThem(Activity activity, int Color, LinearLayout linear_bar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            activity.getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏的颜色
            tintManager.setStatusBarTintResource(Color);
            activity.getWindow().getDecorView().setFitsSystemWindows(true);
            startImmersionStatusBar(activity, linear_bar);
        }

    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     * @param activity
     * @param linear_bar 这个是隐藏的布局，然后通过动态的设置高度达到效果
     */
    public static void startImmersionStatusBar(Activity activity, LinearLayout linear_bar) {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            activity. getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            linear_bar.setVisibility(View.VISIBLE);
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            //获取到状态栏的高度
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                params.height = activity.getResources().getDimensionPixelSize(x);
                linear_bar.setLayoutParams(params);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 保存Bitmap到相册
     *
     * @param context
     * @param bmp
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            if (bmp == null) {
                fos.close();
                return false;
            }
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}