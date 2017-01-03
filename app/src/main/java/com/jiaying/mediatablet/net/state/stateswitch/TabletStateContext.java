package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.util.Log;

import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/4/13.
 */
public class TabletStateContext {
    private AbstractState state;


    private Boolean isContinue = false;

    public void open() {
        this.isContinue = true;
    }

    public void close() {
        isContinue = false;
    }

    public boolean isOpen() {
        return isContinue;
    }

    public TabletStateContext() {

    }

    public synchronized void setCurrentState(AbstractState istate) {
        this.state = istate;
    }

    public synchronized AbstractState getCurrentState() {
        return state;
    }

    public synchronized void handleMessge(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread, DataCenterRun dataCenterRun,
                                          DataCenterTaskCmd cmd, RecSignal recSignal) {
        if (cmd != null) {
            Log.e("error", cmd.getCmd());
        }

        if (isOpen()) {
            if (recSignal == RecSignal.POWEROFF) {
                close();
                listenerThread.notifyObservers(recSignal);
            } else {
                // TODO: 2016/11/18 这个地方其实可以不要 recSignal这个参数
                state.handleMessage(recordState, listenerThread, dataCenterRun, cmd, recSignal, this);
            }
        }
    }
}
