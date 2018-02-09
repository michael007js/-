package com.blankj.utilcode.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import com.blankj.utilcode.R;
import com.blankj.utilcode.dao.PermissionListener;
import com.blankj.utilcode.util.ActivityManagerUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by leilei on 2017/5/16.
 */

public class PermissionsActivity extends Activity{
    private PermissionListener mlistener;
    private List<String> permissionList; //用于存放为授权的权限
    private List<String> deniedPermissions;//被用户拒绝的权限集合
    private   List<String> grantedPermissions;//用户通过的权限集合



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ActivityManagerUtils.getActivityManager().addActivity(this);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }

//    /**
//     * 防止点击空白处关闭
//     * @param context
//     * @param event
//     * @return
//     */
//    private boolean isOutOfBounds(Activity context, MotionEvent event) {
//        final int x = (int) event.getX();
//        final int y = (int) event.getY();
//        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
//        final View decorView = context.getWindow().getDecorView();
//        return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ActivityManagerUtils.getActivityManager().finishActivity(this);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlistener=null;
        if (permissionList!=null){
            permissionList.clear();
        }
        permissionList=null;
        if (deniedPermissions!=null){
            deniedPermissions.clear();
        }
        deniedPermissions=null;
        if (grantedPermissions!=null){
            grantedPermissions.clear();
        }
        grantedPermissions=null;
    }

    /**
     * 权限申请
     * @param permissions 待申请的权限集合
     * @param listener  申请结果监听事件
     */
    public void requestRunTimePermission(String[] permissions,PermissionListener listener){
        this.mlistener = listener;
        permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()){  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else{  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    //被用户拒绝的权限集合
                    deniedPermissions = new ArrayList<>();
                    grantedPermissions= new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED){ //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){  //用户拒绝权限为空
                        mlistener.onGranted();
                    }else {  //不为空
                        //回调授权成功的接口
                        mlistener.onDenied(deniedPermissions);
                        //回调授权失败的接口
                        mlistener.onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;

        }
        finish();
    }
}
