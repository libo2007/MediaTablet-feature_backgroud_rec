package com.jiaying.mediatablet.net.btstate;

import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.stateswitch.AbstractState;

/**
 * Created by hipil on 2016/7/14.
 */
public class BluetoothContextState {
    private AbstractBTState abstractBTState;

    public synchronized void setCurrentState(AbstractBTState istate) {
        this.abstractBTState = istate;
    }

    public synchronized AbstractBTState getCurrentState(){
        return abstractBTState;
    }

    public synchronized void handleMessge(RecSignal recSignal) {
        abstractBTState.handleMessage(recSignal);
    }
}
