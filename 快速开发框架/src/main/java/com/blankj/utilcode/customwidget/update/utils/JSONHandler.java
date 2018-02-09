package com.blankj.utilcode.customwidget.update.utils;

import com.blankj.utilcode.customwidget.update.pojo.UpdateInfo;
import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;


/**
 * Created by Shelwee on 14-5-8.
 */
public class JSONHandler {

    public static UpdateInfo toUpdateInfo(InputStream is) throws Exception {
        if (is == null) {
            return null;
        }
        String byteData = new String(readStream(is));
        is.close();
        JSONObject jsonObject = new JSONObject(byteData);
        UpdateInfo updateInfo = new UpdateInfo();
        LogUtils.e(jsonObject.toString());
        if ("1".equals(jsonObject.getString("status"))){
            updateInfo.setApkUrl(jsonObject.getJSONObject("data").getString("apk_url"));
            updateInfo.setAppName(jsonObject.getJSONObject("data").getString("app_name"));
            updateInfo.setVersionCode(jsonObject.getJSONObject("data").getString("version_code"));
            updateInfo.setVersionName(jsonObject.getJSONObject("data").getString("version_name"));
            String []log=jsonObject.getJSONObject("data").getString("change_log").split("@_@");
            StringBuilder stringBuilder=new StringBuilder();
            for (int i = 0; i < log.length; i++) {
                if (i<log.length-1){
                    stringBuilder.append(log[i]+"\n");
                }else {
                    stringBuilder.append(log[i]);
                }

            }
            updateInfo.setChangeLog(stringBuilder.toString());
            updateInfo.setUpdateTips(jsonObject.getJSONObject("data").getString("update_tips"));
            updateInfo.setForceUpgrade(jsonObject.getJSONObject("data").getBoolean("force_upgrade"));
        }

        return updateInfo;
    }

    private static byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] array = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(array)) != -1) {
            outputStream.write(array, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

}
