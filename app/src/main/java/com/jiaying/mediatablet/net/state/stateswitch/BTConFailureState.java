package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/7/10.
 */
public class BTConFailureState extends AbstractState {

    private static BTConFailureState btConFailureState = null;

    private BTConFailureState() {
    }

    public static BTConFailureState getInstance() {
        if (btConFailureState == null) {
            btConFailureState = new BTConFailureState();
        }
        return btConFailureState;
    }

    @Override
    protected void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread, DataCenterRun dataCenterRun, DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
        switch (recSignal) {

            //记录状态

            //获取数据

            //切换状态

            //发送信号

            case TIMESTAMP:

                //记录状态

                //获取数据
                if ("timestamp".equals(cmd.getCmd())) {
                    ServerTime.curtime = Long.parseLong(textUnit.ObjToString(cmd.getValue("t")));
                }

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TIMESTAMP);
                break;

            case RECONNECTWIFI:
                listenerThread.notifyObservers(RecSignal.RECONNECTWIFI);
                break;

            case CHECKSTART:
                //记录状态
                recordState.recCheckStart();
                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForCheckOverState.getInstance());
                //发送信号
                listenerThread.notifyObservers(RecSignal.CHECKSTART);
                break;

            case SETTINGS:
                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(SettingState.getInstance());
                //发送信号
                listenerThread.notifyObservers(RecSignal.SETTINGS);
                break;

            case BTCONSTART:
                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForBTConState.getInstance());
                //发送信号
                listenerThread.notifyObservers(RecSignal.BTCONSTART);
                break;
            case RESTART:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.RESTART);
                break;
        }
    }
}
