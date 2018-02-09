package com.blankj.utilcode.dao;

import java.util.List;

/**
 * 权限回调接口
 * Created by leilei on 2017/5/16.
 */

public interface PermissionListener {
    //授权成功
    void onGranted();

    /**
     * 授权部分
     * @param grantedPermission 用户拒绝授权的权限
     */
    void onGranted(List<String> grantedPermission);

    /**拒绝授权
     * @param deniedPermission 用户拒绝授权的权限
     */
    void onDenied(List<String> deniedPermission);
}
