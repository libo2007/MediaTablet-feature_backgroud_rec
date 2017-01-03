package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.check.CheckFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/17.
 */
public class CheckStartReceiver extends Receiver {
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public CheckStartReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = this.mainActivity.getTabletStateContext();
        this.recordState = this.mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = this.mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        //设置显示状态

        this.mainActivity.setBrightnessNormal();

        this.mainActivity.uiComponent(false, true, false, false);

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.check2);

        //界面切换：
        this.mainActivity.switchFragment(R.id.fragment_container, new CheckFragment());


        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
//        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
//                return false;
//            }
//        });

        //启动相关动作
        checkBattery();
    }

    private void checkBattery() {
//        batteryIsOk = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    boolean batteryIsOk = mainActivity.isBatteryOK();
                    if (batteryIsOk) {
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);

                        break;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
