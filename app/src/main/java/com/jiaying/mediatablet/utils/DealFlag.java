package com.jiaying.mediatablet.utils;

/**
 * Created by hipil on 2016/4/6.
 */
public class DealFlag {
    private boolean isDeal = true;

    public synchronized boolean isFirst() {
        boolean reFlag = isDeal;
        isDeal = false;
        return reFlag;
    }

    public synchronized void reset() {
        this.isDeal = true;
    }
}
