package com.jiaying.mediatablet.utils;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/6 0006.
 */
public class TimeRecord {
    private static TimeRecord ourInstance = new TimeRecord();



    private Date startVideoDate = null;
    private Date endVideoDate = null;
    private Date startPicDate = null;
    private long duration;

    public static TimeRecord getInstance() {
        return ourInstance;
    }

    private TimeRecord() {

        startVideoDate = new Date();
        endVideoDate = new Date();
    }

    public Date getStartPicDate() {
        return startPicDate;
    }

    public void setStartPicDate(Date startPicDate) {
        this.startPicDate = startPicDate;
    }

    public Date getStartVideoDate() {
        return startVideoDate;
    }

    public Date getEndVideoDate() {
        return endVideoDate;

    }

    public long getDuration() {
        return duration;
    }


    public void setStartVideoDate(Date startDate) {
        this.startVideoDate = startDate;
    }

    public void setEndVideoDate(Date endDate) {
        this.endVideoDate = endDate;

    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
