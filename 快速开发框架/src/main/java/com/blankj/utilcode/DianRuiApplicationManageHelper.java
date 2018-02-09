package com.blankj.utilcode;

import android.content.Context;

import com.blankj.utilcode.customwidget.update.UpdateHelper;
import com.blankj.utilcode.util.AppUtils;

/**
 * APP验证授权管理类(仅限于点芮科技)
 * Created by leilei on 2016/10/9.
 */

public class DianRuiApplicationManageHelper {

    /**
     * 自动更新
     * DianRuiApplicationManageHelper.checkUpdate(getBaseActivityContext(), Config.app_id, false, true);
     *
     * @param context
     * @param app_id           APPID,在APP管理后台中得到:http://apply.nx.021dr.cn/admin/index/index.html
     * @param isHintNewVersion 没有新版本时是否提示
     * @param isAutoInstall    是否自动安装
     */
    public static void checkUpdate(Context context, String app_id, boolean isHintNewVersion, boolean isAutoInstall) {
        UpdateHelper updateHelper = new UpdateHelper.Builder(context)
                .checkUrl("http://apply.nx.021dr.cn/index.php/Api/Update/app/app_version/" + AppUtils.getAppVersionCode(context) + "/app_id/" + app_id)
                .isHintNewVersion(isHintNewVersion)//当没有新版本时是否提示
                .isAutoInstall(isAutoInstall) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
                .build();
        updateHelper.check();
    }

}
