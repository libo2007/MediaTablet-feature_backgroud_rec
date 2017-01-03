package com.jiaying.mediatablet.utils;

import android.text.TextUtils;

import com.jiaying.mediatablet.entity.CurrentDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 作者：lenovo on 2016/7/4 19:50
 * 邮箱：353510746@qq.com
 * 功能：时间日期工具
 */
public class DateTime {
    private static final String TAG = "DateTime";
    public static String timeStamp2Time(String seconds) {
        if (TextUtils.isEmpty(seconds)) {
            return "";
        }
        String format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        CurrentDate.curDate = new Date(Long.valueOf(seconds + ""));
        return sdf.format(CurrentDate.curDate);
    }


    public static String timeStamp2Date(String seconds) {
        if (TextUtils.isEmpty(seconds)) {
            return "";
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        CurrentDate.curDate = new Date(Long.valueOf(seconds + ""));
        return sdf.format(CurrentDate.curDate);
    }

    public static String getWeekDay(String seconds) {
        Date date = new Date(Long.valueOf(seconds + ""));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (day) {
            case Calendar.SUNDAY:
                week = "星期天";
                break;
            case Calendar.MONDAY:
                week = "星期一";
                break;
            case Calendar.TUESDAY:
                week = "星期二";
                break;
            case Calendar.WEDNESDAY:
                week = "星期三";
                break;
            case Calendar.THURSDAY:
                week = "星期四";
                break;
            case Calendar.FRIDAY:
                week = "星期五";
                break;
            case Calendar.SATURDAY:
                week = "星期六";
                break;


        }

        return week;
    }
}
