package com.jiaying.mediatablet.net.btstate;

import com.jiaying.mediatablet.net.signal.RecSignal;


/**
 * Created by hipil on 2016/7/14.
 */
public abstract class AbstractBTState {
    protected BluetoothContextState bluetoothContextState;

    protected abstract void handleMessage(RecSignal recSignal);

}
