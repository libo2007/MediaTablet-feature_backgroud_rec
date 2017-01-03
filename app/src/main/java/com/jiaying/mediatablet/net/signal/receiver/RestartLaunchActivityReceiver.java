package com.jiaying.mediatablet.net.signal.receiver;

import com.jiaying.mediatablet.activity.MainActivity;

/**
 * Created by hipil on 2016/10/11.
 */
public class RestartLaunchActivityReceiver extends Receiver{
    private MainActivity mainActivity;

    public RestartLaunchActivityReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        mainActivity.startLaunchActivityAgain();
    }
}
