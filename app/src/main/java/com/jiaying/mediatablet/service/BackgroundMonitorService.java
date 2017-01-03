package com.jiaying.mediatablet.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;

import com.jiaying.mediatablet.activity.LaunchActivity;
import com.jiaying.mediatablet.constants.IntentAction;
import com.jiaying.mediatablet.constants.IntentExtra;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.RecoverState.StateIndex;
import com.jiaying.mediatablet.utils.AppInfoUtils;
import com.jiaying.mediatablet.utils.MyLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 后台监听服务，包括监听应用退到后台后，根据服务端返回的不同信号判断，是否启动到前台等操作
 */
public class BackgroundMonitorService extends Service {
    private static final String TAG = "BackgroundMonitorService";

    //定时刷新时间任务
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    //是否需要在应用退到后台的时候启动到前台
    private boolean isNeedAppForeground = false;
    //状态记录
    private RecordState recordState;

    @Override
    public void onCreate() {
        super.onCreate();
        recordState = RecordState.getInstance(this);
        startTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // START_NOT_STICKY如果系统在 onStartCommand() 返回后终止服务，则除非有挂起 Intent 要传递，
        // 否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。

        // START_STICKY如果系统在 onStartCommand() 返回后终止服务，则会重建服务并调用 onStartCommand()，
        // 但绝对不会重新传递最后一个 Intent。相反，除非有挂起 Intent 要启动服务（在这种情况下，将传递这些 Intent ），
        // 否则系统会通过空 Intent 调用 onStartCommand()。这适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。

        // START_REDELIVER_INTENT如果系统在 onStartCommand() 返回后终止服务，则会重建服务，并通过传递给服务的最后一个 Intent
        // 调用 onStartCommand()。任何挂起 Intent 均依次传递。这适用于主动执行应该立即恢复的作业（例如下载文件）的服务。
        return START_STICKY;
    }


    @Override
    //clean up any resources such as threads, registered listeners, receivers, etc.
    public void onDestroy() {
        super.onDestroy();
        //关闭定时器
        stopTimerTask();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimerTask() {
        //关闭可能已经打开的时间任务
        stopTimerTask();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                boolean isAppForeground = AppInfoUtils.isAppForeground(BackgroundMonitorService.this);
                MyLog.e(TAG, "监听到应用是否在前台运行：" + isAppForeground);
                if (recordState != null) {
                    String state = recordState.getState();
                    if (TextUtils.equals(state, StateIndex.END)) {
                        isNeedAppForeground = true;
                    }
                }
                MyLog.e(TAG, "应用是否需要从后台启动到前台：" + isNeedAppForeground);
                if (!isAppForeground && isNeedAppForeground) {
                    //启动到前台去
                    Intent intent = getPackageManager().getLaunchIntentForPackage(BackgroundMonitorService.this.getPackageName());
                    BackgroundMonitorService.this.startActivity(intent);
                }
            }
        };
        try {
            if (mTimer != null) {
                mTimer.schedule(mTimerTask, 0, 1000);
            }
        } catch (IllegalArgumentException IllegalArgumentException) {
            //if delay < 0 or period <= 0.
            // TODO: 2016/8/2 错误写入数据库
        } catch (IllegalStateException illegalStateException) {
            //if the Timer has been canceled, or if the task has been scheduled or canceled.
            // TODO: 2016/8/2 错误写入数据库
        }
    }

    private void stopTimerTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
}