package com.jiaying.mediatablet.utils;

/**
 * 作者：lenovo on 2016/6/1 10:14
 * 邮箱：353510746@qq.com
 * 功能：亮度调解功能
 */
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.WindowManager;

public class BrightnessTools {
    private static final String TAG ="BrightnessTools";
    /**
     * 判断是否开启了自动亮度调节
     */

    public static boolean isAutoBrightness(Context context) {

        boolean automicBrightness = false;

        try {
            automicBrightness = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e)

        {

            e.printStackTrace();

        }

        return automicBrightness;
    }

    /**
     * 获取屏幕的亮度
     */

    public static int getScreenBrightness(Context context) {

        int nowBrightnessValue = 0;

        ContentResolver resolver = context.getContentResolver();

        try {

            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);

        } catch (Exception e) {

            e.printStackTrace();

        }

        MyLog.e(TAG,"nowBrightnessValue:" + nowBrightnessValue);
        return nowBrightnessValue;
    }

    /**
     * 设置亮度
     */

    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 停止自动亮度调节
     */

    public static void stopAutoBrightness(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * * 开启亮度自动调节 *
     *
     * @param context
     */

    public static void startAutoBrightness(Activity context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        int brightNess = getScreenBrightness(context);
        if(brightNess >=200||brightNess<=70){
            brightNess =80;
        }
        setBrightness(context,brightNess);
    }

    /**
     * 保存亮度设置状态
     */

    public static void saveBrightness(Context context, int brightness) {
        //记得权限 <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
        Uri uri = android.provider.Settings.System
                .getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(context.getContentResolver(),
                "screen_brightness", brightness);
        context.getContentResolver().notifyChange(uri, null);
    }
}
