package com.jiaying.mediatablet.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.jiaying.mediatablet.constants.IntentAction;
import com.jiaying.mediatablet.constants.IntentExtra;
import com.jiaying.mediatablet.utils.MyLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：lenovo on 2016/5/13 10:13
 * 邮箱：353510746@qq.com
 * 功能：时间计时
 */
public class TimeService extends Service {

    //服务器获取到的正确时间
    private long currentTime = System.currentTimeMillis();

    //由于重启服务保存的时间
    private long currentLocalTime = System.currentTimeMillis();

    //定时刷新时间任务
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
        currentLocalTime = sharedPreferences.getLong("time", System.currentTimeMillis());
        startTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            currentTime = intent.getLongExtra("currenttime", currentLocalTime);
        }
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
        sharedPreferences.edit().putLong("time", currentLocalTime).apply();

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
                Intent timeUpdateIntent = new Intent();
                timeUpdateIntent.setAction(IntentAction.ACTION_UPDATE_TIME).putExtra(IntentExtra.EXTRA_TIME, currentTime);
                sendBroadcast(timeUpdateIntent);
                currentTime += 1000;
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