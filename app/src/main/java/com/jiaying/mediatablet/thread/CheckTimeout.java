package com.jiaying.mediatablet.thread;

/**
 * Created by hipil on 2016/11/18.
 */
public class CheckTimeout extends Thread {
    private int seconds;
    private OnTimeout onTimeout;

    public CheckTimeout(int seconds) {
        this.seconds = seconds;
    }

    public void setOnTimeoutCallback(OnTimeout onTimeout) {
        this.onTimeout = onTimeout;
    }

    @Override
    public void run() {
        super.run();
        try {
            this.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }

        if (this.onTimeout != null) {
            this.onTimeout.timeout();
        } else {
            throw new RuntimeException("onTimeout is null");
        }
    }

    public interface OnTimeout {
        void timeout();
    }
}
