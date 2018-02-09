package com.blankj.utilcode.util;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.UUID;

/**
 * BluetoothKit蓝牙框架工具类
 * 参考连接:https://github.com/dingjikerbo/BluetoothKit
 * Created by leilei on 2017/5/13.
 */

public class BluetoothKitUtils {
    private static BluetoothClient mClient;

    public BluetoothKitUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static BluetoothClient getBluetoothClient(Context context) {
        if (mClient == null) {
            mClient = new BluetoothClient(context);
        }
        return mClient;
    }

    /**
     * 是否支持低功耗蓝牙
     *
     * @return
     */
    public static BluetoothClient isBleSupported() {
        mClient.isBleSupported();
        return mClient;
    }

    /**
     * 蓝牙是否被打开
     *
     * @return
     */
    public static boolean isBluetoothOpened() {
        return  mClient.isBluetoothOpened();
    }

    /**
     * 打开蓝牙
     *
     * @return
     */
    public static boolean openBluetooth() {
        return mClient.openBluetooth();
    }

    /**
     * 断开蓝牙
     *
     * @param mac
     * @return
     */
    public static BluetoothClient disconnect(String mac) {
        mClient.disconnect(mac);
        return mClient;
    }

    /**
     * 关闭蓝牙
     *
     * @return
     */
    public static boolean closeBluetooth() {
        return mClient.closeBluetooth();
    }


    /**
     * 扫描设备：支持经典蓝牙和低功耗蓝牙设备混合扫描
     *
     * @param searchResponse
     */
    public static BluetoothClient search(SearchResponse searchResponse) {
        mClient.search(new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)// 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000)// 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)// 再扫BLE设备2s
                .build(), searchResponse);
        return mClient;
    }

    /**
     *停止扫描
     * @return
     */
    public static BluetoothClient stopSearch() {
        mClient.stopSearch();
        return mClient;
    }

    /**
     * 注册蓝牙配对监听
     * 监听设备配对状态变化
     * @param listener
     * @return
     */
    public static BluetoothClient registerBluetoothBondListener(BluetoothBondListener listener) {
        mClient.registerBluetoothBondListener(listener);
        return mClient;
    }

    /**
     * 解除蓝牙配对监听
     * 监听设备配对状态变化
     * @param listener
     * @return
     */
    public static BluetoothClient unregisterBluetoothBondListener(BluetoothBondListener listener) {
        mClient.unregisterBluetoothBondListener(listener);
        return mClient;
    }


    /**
     * 注册蓝牙状态监听
     * 蓝牙打开或关闭需要一段时间，可以注册回调监听状态，回调的参数如果是true表示蓝牙已打开，false表示蓝牙关闭
     * @param listener
     * @return
     */
    public static BluetoothClient registerBluetoothStateListener(BluetoothStateListener listener) {
        mClient.registerBluetoothStateListener(listener);
        return mClient;
    }

    /**
     * 解除蓝牙状态监听
     * 蓝牙打开或关闭需要一段时间，可以注册回调监听状态，回调的参数如果是true表示蓝牙已打开，false表示蓝牙关闭
     * @param listener
     * @return
     */
    public static BluetoothClient unregisterBluetoothStateListener(BluetoothStateListener listener) {
        mClient.unregisterBluetoothStateListener(listener);
        return mClient;
    }





    /**
     * 注册蓝牙连接状态监听
     * @param mac
     * @param listener
     * @return
     */
    public static BluetoothClient registerConnectStatusListener(String mac, BleConnectStatusListener listener) {
        mClient.registerConnectStatusListener(mac,listener);
        return mClient;
    }





    /**
     * 解除蓝牙连接状态监听
     * @param mac
     * @param listener
     * @return
     */
    public static BluetoothClient unregisterConnectStatusListener(String mac, BleConnectStatusListener listener) {
        mClient.unregisterConnectStatusListener(mac,listener);
        return mClient;
    }







    /**
     *低功耗蓝牙设备通信
     * 不配置连接参数
     * 连接过程包括了普通的连接(connectGatt)和发现服务(discoverServices)，这里收到回调时表明服务发现已完成。
     * 回调参数BleGattProfile包括了所有的service和characteristic的uuid。
     * 返回的code表示操作状态，包括成功，失败或超时等，所有常量都在Constants类中。
     * @param mac
     * @param bleConnectResponse
     * @return
     */

    /*
        new BleConnectResponse() {
        @Override
        public void onResponse(int code, BleGattProfile profile) {
        //返回的code表示操作状态，包括0成功，-1失败或-7超时等，所有常量都在Constants类中。
            if (code == 0) {
            }
        }
    }
    *
    * */
    public static BluetoothClient connect(String mac,BleConnectResponse bleConnectResponse){
        mClient.connect(mac,bleConnectResponse );
        return mClient;
    }







    /**
     * 配置连接参数
     * 连接过程包括了普通的连接(connectGatt)和发现服务(discoverServices)，这里收到回调时表明服务发现已完成。
     * 回调参数BleGattProfile包括了所有的service和characteristic的uuid。
     * 返回的code表示操作状态，包括成功，失败或超时等，所有常量都在Constants类中。
     * @param mac
     * @param options
     * @param bleConnectResponse
     * @return
     */
    /*
    BleConnectOptions options = new BleConnectOptions.Builder()
     .setConnectRetry(3)   // 连接如果失败重试3次
     .setConnectTimeout(30000)   // 连接超时30s
     .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
     .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
     .build();

     mClient.connect(MAC, options, new BleConnectResponse() {
         @Override
         public void onResponse(int code, BleGattProfile data) {

          }
    });
    * */
    public static BluetoothClient connect(String mac, BleConnectOptions options, BleConnectResponse bleConnectResponse){
        mClient.connect(mac,options,bleConnectResponse );
        return mClient;
    }


    /**
     * 返回设备连接状态
     // Constants.STATUS_UNKNOWN
     // Constants.STATUS_DEVICE_CONNECTED
     // Constants.STATUS_DEVICE_CONNECTING
     // Constants.STATUS_DEVICE_DISCONNECTING
     // Constants.STATUS_DEVICE_DISCONNECTED
     * @param mac
     * @return
     */
    public int get(String mac){
        return mClient.getConnectStatus(mac);
    }






    /**
     * 读取数据
     * @param mac
     * @param serviceUUID
     * @param characterUUID
     * @param bleReadResponse
     */
    /*
    new BleReadResponse() {
        @Override
            public void onResponse(int code, byte[] data) {
                if (code == REQUEST_SUCCESS) {
                 }
             }
    }
    */
    public static BluetoothClient read(String mac, UUID serviceUUID,UUID characterUUID,BleReadResponse bleReadResponse){
        mClient.read(mac, serviceUUID, characterUUID, bleReadResponse);
        return mClient;
    }






    /**
     * 写入数据
     * @param mac
     * @param serviceUUID
     * @param characterUUID
     * @param bytes
     * @param bleReadResponse
     */

    /*
    new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {

            }
        }
    }
    * */
    public static BluetoothClient write(String mac, UUID serviceUUID,UUID characterUUID,byte[]bytes,BleWriteResponse bleReadResponse){
        mClient.write(mac, serviceUUID, characterUUID,bytes, bleReadResponse);
        return mClient;
    }


    /**
     * 打开Notify
     * @param mac
     * @param serviceUUID
     * @param characterUUID
     * @param bleNotifyResponse
     * @return
     */

    /*
    new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {

        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {

            }
        }
    }
    * */
    public static BluetoothClient notify(String mac,UUID serviceUUID,UUID characterUUID,BleNotifyResponse bleNotifyResponse){
        mClient.notify(mac, serviceUUID, characterUUID, bleNotifyResponse);
        return mClient;
    }



    /**
     * 关闭Notify
     * @param mac
     * @param serviceUUID
     * @param characterUUID
     * @param bleUnnotifyResponse
     * @return
     */

    /*
   new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {

            }
        }
    }
    * */
    public static BluetoothClient unnotify(String mac,UUID serviceUUID,UUID characterUUID,BleUnnotifyResponse bleUnnotifyResponse){
        mClient.unnotify(mac, serviceUUID, characterUUID, bleUnnotifyResponse);
        return mClient;
    }

    /**
     * 清理请求
     * @param mac
     * @param clearType
     * @return
     */
    public static BluetoothClient clearRequest(String mac,int clearType){
        mClient.clearRequest(mac, clearType);
        // Constants.REQUEST_READ，所有读请求
        // Constants.REQUEST_WRITE，所有写请求
        // Constants.REQUEST_NOTIFY，所有通知相关的请求
        // Constants.REQUEST_RSSI，所有读信号强度的请求
        return mClient;
    }


    /**
     * 刷新缓存
     * @param mac
     * @return
     */
    public static BluetoothClient refreshCache(String mac){
        mClient.refreshCache(mac);
        return mClient;
    }
}
