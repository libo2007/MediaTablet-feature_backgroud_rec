package com.jiaying.mediatablet.net.signal.receiver;

import android.app.ProgressDialog;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;

/**
 * Created by hipil on 2016/9/16.
 */
public class PlasmaWeightReceiver extends Receiver {
    private MainActivity mainActivity;

    public PlasmaWeightReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void work() {

        HorizontalProgressBar collect_pb = this.mainActivity.getHorizontalProgressBar();;
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());

    }
}
