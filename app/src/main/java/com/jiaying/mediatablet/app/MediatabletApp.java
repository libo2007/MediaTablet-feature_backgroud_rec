package com.jiaying.mediatablet.app;

        import android.app.Application;
        import android.graphics.Bitmap;

        import com.iflytek.cloud.SpeechUtility;
        import com.jiaying.mediatablet.constants.Constants;
        import com.jiaying.mediatablet.utils.CrashHandler;
        import com.jiaying.mediatablet.utils.MyLog;
        import com.tencent.bugly.crashreport.CrashReport;

/**
 * 作者：lenovo on 2016/4/3 13:59
 * 邮箱：353510746@qq.com
 * 功能：application
 */
public class MediatabletApp extends Application {
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MediatabletApp.this, Constants.FLYTEK_APP_ID);
//        CrashReport.initCrashReport(getApplicationContext(), "900028556", MyLog.DEBUG);
        CrashReport.initCrashReport(getApplicationContext(), "900028556", true);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        super.onCreate();
    }

}
