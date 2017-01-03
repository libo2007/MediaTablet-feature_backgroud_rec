package com.jiaying.mediatablet.net.thread;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.jiaying.mediatablet.utils.MsgFlag;
import com.jiaying.mediatablet.utils.WifiAdmin;

/**
 * Created by hipil on 2016/10/13.
 */
public class ReconnectWifiThread extends Thread {
    public static String TAG = "ReconnectWifiThread";
    private boolean wifiIsOk = false;
    private String SSID = null;
    private String PWD = null;
    private int TYPE = 0;
    private WifiAdmin wifiAdmin = null;
    private ConnectWifiThread.OnConnSuccessListener onConnSuccessListener;

    public ReconnectWifiThread(String SSID, String PWD, int TYPE, Context context) {
        this.SSID = SSID;
        this.PWD = PWD;
        this.TYPE = TYPE;
        wifiAdmin = new WifiAdmin(context);
    }

    @Override
    public void run() {
        super.run();
        wifiAdmin.closeWifi();
        int count = 4;
        while (--count >0) {
            Log.e(TAG, "ReconnectWifiThread 关闭wifi"+this.toString());
            //判断wifi是否已经打开
            if (wifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED) {//wifi已经打开
                  /*连接网络,此处的addNetwork是异步操作，不能确保其可以立即添加网络成功，
                    所以以3秒为间隔来反复轮询网络添加结果*/
                Log.e(TAG, "ReconnectWifiThread 连接wifi"+this.toString());
                wifiIsOk = wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(SSID, PWD, TYPE));
                //判断wifi是否已经连接上
                if (wifiIsOk) {
                    //界面跳转
                    Log.e(TAG, "ReconnectWifiThread 连上了wifi"+this.toString());
                    if (this.onConnSuccessListener == null)
                        throw new RuntimeException("onConnSuccessListener is null");

                    this.onConnSuccessListener.onConnSuccess();
                }
            } else {//wifi没有打开
                wifiAdmin.openWifi();
                Log.e(TAG, "ReconnectWifiThread 打开wifi"+this.toString());
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                /*
                Thrown when a waiting thread is activated
                before the condition it was waiting for has been satisfied
                比如：在sleep期间，调用了Interrupt()函数会抛出该异常。
                */
                e.printStackTrace();
            }
        }
    }

    public void setOnConnSuccessListener(ConnectWifiThread.OnConnSuccessListener onConnSuccessListener) {
        this.onConnSuccessListener = onConnSuccessListener;
    }
}
