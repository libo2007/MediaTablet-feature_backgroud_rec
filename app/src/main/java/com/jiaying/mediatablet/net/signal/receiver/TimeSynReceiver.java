package com.jiaying.mediatablet.net.signal.receiver;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.fragment.ServerSettingFragment;
import com.jiaying.mediatablet.service.TimeService;

/**
 * Created by hipil on 2016/9/14.
 */
public class TimeSynReceiver extends Receiver {
    private MainActivity mainActivity;

    public TimeSynReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //调整界面组成

        //界面切换

        //调整界面文字

        //设置logo的相关事件

        //启动相关动作
        startTimeService();
    }

    private ComponentName startTimeService() {

        Intent it = new Intent(mainActivity, TimeService.class);
        it.putExtra("currenttime", ServerTime.curtime);

        return mainActivity.startService(it);
    }
}
