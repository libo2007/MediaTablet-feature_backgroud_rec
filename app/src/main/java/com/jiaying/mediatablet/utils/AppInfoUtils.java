package com.jiaying.mediatablet.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;

/*
获取app信息
 */
public class AppInfoUtils {
    /**
     *
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * apk文件是否已经安装
     *
     * @param context
     * @param pkgName
     *            对应的包名
     * @return
     */
    public static boolean isAppInstalled(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * sd卡是否可用
     *
     * @param
     * @return
     */
    public static boolean isVaiSDCard() {
        boolean result = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return result;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕 的宽
     */
    public static int getWith(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕 的高
     */
    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static boolean fileIsExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean copyApkFromAssets(Context context, String fileName,
                                            String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static void installApp(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + path),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean startAppliction(Context context, String packageName) {
        boolean isStartSucessed = false;
        Intent intent = new Intent();
        PackageInfo packinfo;
        try {
            packinfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);

            ActivityInfo[] activityinfos = packinfo.activities;
            if (activityinfos != null && activityinfos.length > 0) {
                String className = activityinfos[0].name;
                intent.setClassName(packageName, className);
                context.startActivity(intent);
                isStartSucessed = true;
            } else {
                isStartSucessed = false;
            }
        } catch (NameNotFoundException e) {// 使用C语言实现的应用程序，在DDMS中没有对应的包名
            e.printStackTrace();
            isStartSucessed = false;
        }
        return isStartSucessed;

    }
    /**
     * 判断程序是否在前台运行
     * @param context
     * @return true在前台，false在后台
     */
    public static boolean isAppForeground(Context context) {
        boolean isForground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String pkgName : processInfo.pkgList) {
                        if (pkgName.equals(context.getPackageName())) {
                            isForground = true;
                        }
                    }
                }
            }
        } else {
            //@deprecated As of {@link android.os.Build.VERSION_CODES#LOLLIPOP}, 从Android5.0开始不能使用getRunningTask函数
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isForground = true;
            }
        }

        return isForground;
    }
}
