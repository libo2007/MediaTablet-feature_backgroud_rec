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
 * Created by hipil on 2016/9/14.
 */
public class LowPowerReceiver extends Receiver {
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;


    public LowPowerReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        tabletStateContext = mainActivity.getTabletStateContext();
        recordState = mainActivity.getRecordState();
        observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        //设置显示状态
        mainActivity.uiComponent(false, true, false, false);

        //界面切换：
        //调出企业文化界面
        mainActivity.switchFragment(R.id.fragment_container, new CheckFragment());

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.check1);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
//        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
//
//
//            @Override
//            public boolean onLongClick(View v) {
//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
//                return false;
//            }
//        });

//        启动相关操作
        checkdBattery();
    }

    private void checkdBattery() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (mainActivity.isBatteryOK()) {
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
