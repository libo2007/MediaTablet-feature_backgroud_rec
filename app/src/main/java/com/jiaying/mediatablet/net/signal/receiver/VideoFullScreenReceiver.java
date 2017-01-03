package com.jiaying.mediatablet.net.signal.receiver;

import com.jiaying.mediatablet.activity.MainActivity;

/**
 * Created by hipil on 2016/9/16.
 */
public class VideoFullScreenReceiver extends Receiver{
    private MainActivity mainActivity;

    public VideoFullScreenReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        this.mainActivity.uiComponent(false, false, false, false);
    }
}
