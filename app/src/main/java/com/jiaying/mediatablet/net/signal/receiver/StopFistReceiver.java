package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.thread.AniThread;

/**
 * Created by hipil on 2016/9/16.
 */
public class StopFistReceiver extends Receiver {
    private MainActivity mainActivity;

    public StopFistReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        AniThread startFist = this.mainActivity.getStartFist();
        if (startFist != null) {
            startFist.finishAni();
            this.mainActivity.setStartFist(null);
        }
    }
}
