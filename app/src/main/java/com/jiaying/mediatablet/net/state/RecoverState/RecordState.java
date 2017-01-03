package com.jiaying.mediatablet.net.state.RecoverState;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.jiaying.mediatablet.net.signal.RecSignal;

import java.util.HashMap;

/**
 * Created by hipil on 2016/4/3.
 */

// It's used when the app is closed and then restart.
public class RecordState {
    private SharedPreferences settings;
    private SharedPreferences.Editor localEditor;
    private String state;
    private static RecordState recordState = null;

    private RecordState(Context activity) {

        // Initialize the variables.
        settings = activity.getSharedPreferences("state_record",Context.MODE_PRIVATE);
        localEditor = this.settings.edit();
    }

    //
    public static RecordState getInstance(Context activity) {
        if (recordState == null) {
            recordState = new RecordState(activity);
        }
        return recordState;
    }

    // commit the state info and donor info into preference.
    public synchronized void commit() {
        localEditor.putString("state", state);
        localEditor.commit();
    }

    public synchronized void retrieve() {
        state = settings.getString("state", null);
    }


    public synchronized String getState() {
        return state;
    }

    public synchronized void reset() {

    }
// **********record the state info***********

    public synchronized void recCheckOver() {
        state = StateIndex.WAITINGFORDONOR;
    }

    public synchronized void recConfirm() {
        state = StateIndex.WAITINGFORAUTH;
    }

    public synchronized void recAuth() {
        state = StateIndex.WAITINGFORCOMPRESSION;
    }

    public synchronized void recCompression() {
        state = StateIndex.WAITINGFORPUNCTURE;
    }


    public synchronized void recPuncture() {
        state = StateIndex.WAITINGFORSTART;
    }


    public synchronized void recCollection() {
        state = StateIndex.COLLECTION;
    }

    public synchronized void recTranfusion() {
        state = StateIndex.TRANFUSION;
    }


    public synchronized void recEnd() {
        state = StateIndex.END;
    }

    public synchronized void recCheckStart() {
        state = StateIndex.WAITINGFORCHECKOVER;
    }

    public synchronized void recTimeStamp() {
        state = StateIndex.SYNTIME;
    }


}
