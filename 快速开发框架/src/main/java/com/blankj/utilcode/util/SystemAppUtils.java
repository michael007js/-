package com.blankj.utilcode.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/5/2.
 */

public class SystemAppUtils {
    /**
     * 直接呼叫指定的号码(需要<uses-permission android:name="android.permission.CALL_PHONE"/>权限)
     * @param context 上下文Context
     * @param phoneNumber 需要呼叫的手机号码
     */
    public static void callPhone(Context context, String phoneNumber){
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent call = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(call);
    }
    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 跳转至拨号界面
     * @param context 上下文Context
     * @param phoneNumber 需要呼叫的手机号码
     */
    public static void toCallPhoneActivity(Context context,String phoneNumber){
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent call = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(call);
    }

    /**
     * 直接调用短信API发送信息(设置监听发送和接收状态)
     * @param strPhone 手机号码
     * @param strMsgContext 短信内容
     */
    public static void sendMessage(final Context context,final String strPhone,final String strMsgContext){

        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sendIntent= PendingIntent.getBroadcast(context, 0, sentIntent,0);
        // register the Broadcast Receivers
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ToastUtils.showShortToast(context,"短信发送成功");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent backIntent= PendingIntent.getBroadcast(context, 0,deliverIntent, 0);
        context.registerReceiver(new BroadcastReceiver() {
                                      @Override
                                      public void onReceive(Context _context, Intent _intent) {
                                          ToastUtils.showShortToast(context,"短信接收成功");
                                      }
                                  },
                new IntentFilter(DELIVERED_SMS_ACTION)
        );

        //拆分短信内容（手机短信长度限制）
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgList = smsManager.divideMessage(strMsgContext);
        for (String text : msgList) {
            smsManager.sendTextMessage(strPhone, null, text, sendIntent, backIntent);
        }
    }

    /**
     * 跳转至发送短信界面(自动设置接收方的号码)
     * @param strPhone 手机号码
     * @param strMsgContext 短信内容
     */
    public static void toSendMessageActivity(Context context,String strPhone,String strMsgContext){
        if(PhoneNumberUtils.isGlobalPhoneNumber(strPhone)){
            Uri uri = Uri.parse("smsto:" + strPhone);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", strMsgContext);
            context.startActivity(sendIntent);
        }
    }

    /**
     * 跳转至联系人选择界面
     * @param requestCode 请求返回区分代码
     */
    public static void toChooseContactsList(Activity context,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取选择的联系人的手机号码
     * @param context 上下文
     * @param resultCode 请求返回Result状态区分代码
     * @param data onActivityResult返回的Intent
     * @return
     */
    public static String getChoosedPhoneNumber(Activity context,int resultCode,Intent data) {
        //返回结果
        String phoneResult = "";
        if (Activity.RESULT_OK == resultCode)
        {
            Uri uri = data.getData();
            Cursor mCursor = context.managedQuery(uri, null, null, null, null);
            mCursor.moveToFirst();

            int phoneColumn = mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int phoneNum = mCursor.getInt(phoneColumn);
            if (phoneNum > 0) {
                // 获得联系人的ID号
                int idColumn = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactId = mCursor.getString(idColumn);
                // 获得联系人的电话号码的cursor;
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                if (phones.moveToFirst()) {
                    // 遍历所有的电话号码
                    for (; !phones.isAfterLast(); phones.moveToNext()) {
                        int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                        int phone_type = phones.getInt(typeindex);
                        String phoneNumber = phones.getString(index);
                        switch (phone_type) {
                            case 2:
                                phoneResult = phoneNumber;
                                break;
                        }
                    }
                    if (!phones.isClosed()) {
                        phones.close();
                    }
                }
            }
            //关闭游标
            mCursor.close();
        }

        return phoneResult;
    }

    /**
     * 跳转至拍照程序界面
     * @param context 上下文
     * @param requestCode 请求返回Result区分代码
     */
    public static void toCameraActivity(Activity context,int requestCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至相册选择界面
     * @param context 上下文
     * @param requestCode
     */
    public static void toImagePickerActivity(Activity context,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获得选中相册的图片
     * @param context 上下文
     * @param data onActivityResult返回的Intent
     * @return
     */
    public static Bitmap getChoosedImage(Activity context, Intent data){
        if (data == null) {
            return null;
        }

        Bitmap bm = null;

        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = context.getContentResolver();

        // 此处的用于判断接收的Activity是不是你想要的那个
        try {
            Uri originalUri = data.getData(); // 获得图片的uri
            bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
            // 这里开始的第二部分，获取图片的路径：
            String[] proj = {
                    MediaStore.Images.Media.DATA
            };
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = context.managedQuery(originalUri, proj, null, null, null);
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            //不用了关闭游标
            cursor.close();
        } catch (Exception e) {
            Log.e("ToolPhone", e.getMessage());
        }

        return bm;
    }

    /**
     * 调用本地浏览器打开一个网页
     * @param context 上下文
     * @param strSiteUrl 网页地址
     */
    public static void openWebSite(Context context,String strSiteUrl){
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strSiteUrl));
        context.startActivity(webIntent);
    }

    /**
     * 跳转至系统设置界面
     * @param context 上下文
     */
    public static void toSettingActivity(Context context){
        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(settingsIntent);
    }

    /**
     * 跳转至WIFI设置界面
     * @param context 上下文
     */
    public static void toWIFISettingActivity(Context context){
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(wifiSettingsIntent);
    }

    /**
     * 启动本地应用打开PDF
     * @param context 上下文
     * @param filePath 文件路径
     */
    public static void openPDFFile(Context context, String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(context, "未检测到可打开PDF相关软件", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 启动本地应用打开PDF
     * @param context 上下文
     * @param filePath 文件路径
     */
    public static void openWordFile(Context context, String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(path, "application/msword");
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(context, "未检测到可打开Word文档相关软件", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 调用WPS打开office文档
     * http://bbs.wps.cn/thread-22349340-1-1.html
     * @param context 上下文
     * @param filePath 文件路径
     */
    public static void openOfficeByWPS(Context context, String filePath){

        try {

            //文件存在性检查
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(context, filePath+"文件路径不存在", Toast.LENGTH_SHORT).show();
                return;
            }

            //检查是否安装WPS
            String wpsPackageEng = "cn.wps.moffice_eng";//普通版与英文版一样
//          String wpsActivity = "cn.wps.moffice.documentmanager.PreStartActivity";
            String wpsActivity2 = "cn.wps.moffice.documentmanager.PreStartActivity2";//默认第三方程序启动

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setClassName(wpsPackageEng,wpsActivity2);

            Uri uri = Uri.fromFile(new File(filePath));
            intent.setData(uri);
            context.startActivity(intent);

        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "本地未安装WPS", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "打开文档失败", Toast.LENGTH_SHORT).show();
        }
    }


}
