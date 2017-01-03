package com.jiaying.mediatablet.constants;

/**
 * 作者：lenovo on 2016/4/3 09:25
 * 邮箱：353510746@qq.com
 * 功能：当前所在的状态
 */
public class Status {

    //初始化
    public static final int STATUS_INIT = 0;
    //等待献浆员
    public static final int STATUS_WAIT_PLASM = 1;
    //欢迎献浆员
    public static final int STATUS_WELCOME_PLASM = 2;
    //加压提示
    public static final int STATUS_PRESSING = 3;
    //穿刺
    public static final int STATUS_PUNCTURE = 4;
    //采集
    public static final int STATUS_COLLECTION = 5;
    //握拳
    public static final int STATUS_FIST = 6;
    //结束
    public static final int STATUS_OVER = 7;

    //标志评价的态度 0 为选择 好1 中2 差3
    public static final int  STATUS_EVL_DEFAULT = 0;
    public static final int  STATUS_EVL_GOOD = 1;
    public static final int  STATUS_EVL_SOSO = 2;
    public static final int  STATUS_EVL_TERRIBLE = 3;


}
