package com.jiaying.mediatablet.net.signal.receiver;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.handler.ObserverZXDCSignalUIHandler;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/14.
 */
public class RestartMainActivityReceiver extends Receiver {
    private MainActivity mainActivity;

    public RestartMainActivityReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //调整界面组成


        //界面切换


        //调整界面文字


        //设置logo的相关事件


        //启动相关动作
        //记录此刻的状态
        RecordState recordState = mainActivity.getRecordState();
        recordState.recTimeStamp();
        recordState.commit();

        TabletStateContext tabletStateContext = mainActivity.getTabletStateContext();
        ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();

        //发送关机信号，告知系统需要将此刻的状态持久化的保存到本地上。
        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.POWEROFF);

        //重启MainActivity,重启的MainActivity需要读取本地信息并且恢复出前面的状态。
        mainActivity.startMainActivityAgain();
    }
}
