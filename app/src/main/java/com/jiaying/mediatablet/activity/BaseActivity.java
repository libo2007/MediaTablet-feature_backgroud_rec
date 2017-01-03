package com.jiaying.mediatablet.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * activity基类
 */
public abstract class BaseActivity extends Activity {
    public static String TAG = "BaseActivity";

    private PowerManager.WakeLock mWakelock;
    private KeyguardManager km;
    private PowerManager pm;

    /*
    * we use template method pattern,the function onCreate() is a template method pattern*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        loadData();
    }

    //初始化变量，包括Intent带的数据和Activity内的变量
    protected abstract void initVariables();

    //加载layout布局文件，初始化控件，为控件挂上事件方法
    protected abstract void initView();

    //调用服务器API加载数据
    protected abstract void loadData();

    //禁止返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            // The donor can't use the BACK button to close the APP.
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //禁止屏幕休眠，但长时间无献浆消息推送过来，屏幕处于低亮度。
    protected void forbidLockScreen() {

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");

        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");

        //解锁
        kl.disableKeyguard();

        //点亮
        mWakelock.acquire();
    }
}
