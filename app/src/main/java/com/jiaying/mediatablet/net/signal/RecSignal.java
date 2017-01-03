package com.jiaying.mediatablet.net.signal;

/**
 * Created by hipil on 2016/4/2.
 */
public enum RecSignal {

    // The signals received from the plasma.

    //服务器来的时间戳信号
    TIMESTAMP,

    //30S超时为收到服务器发送的时间信号
    TIMESTAMPTIMEOUT,

    //得到时间信号后，接着连接蓝牙
    BTCONSTART,

    //蓝牙连接失败
    BTCONFAILURE,

    //蓝牙连接成功，开始进入检查设备电量
    CHECKSTART,

    //电量检查通过
    CHECKOVER,

    //服务器推送了浆员信息过来
    CONFIRM,

    //取消认证
    CANCEL,

    //录制献浆员视频
    RECORDDONORVIDEO,

    //人脸识别长时间不通过，录制护士视频
    RECORDNURSEVIDEO,

    //人脸识别长时间不通过，录制视频完成
    RECORDOVER,

    //人脸识别通过
    AUTHPASS,

    //手动人脸识别通过
    MANUALAUTHPASS,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了服务器应答
    SERAUTHRES,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了ZXDC应答
    ZXDCAUTHRES,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了全部应答
    AUTHRESOK,

    //人脸识别通过后，向服务器发送认证通过信号后，得到应答超时
    AUTHRESTIMEOUT,

    //得到应答超时后，重发人脸识别通过信号
    REAUTHPASS,

    //得到应答超时后，取消发送人脸识别通过信号
    CANCLEAUTHPASS,

    //得到服务器传送来的，机器开始加压信号
    COMPRESSINON,

    //得到服务器传送来的，机器开始穿刺信号
    PUNCTURE,

    //得到服务器传来的，机器开始采集信号
    START,

    //视频已经录制两分钟了，停止录制
    STOPREC,

    //呼叫请求糖果
    TISSUE,

    //呼叫请求热水
    BOILEDWATER,


    //呼叫请求糖果
    CANDY,

    //呼叫请求杂质
    MAGAZINE,

    //呼叫请求咨询
    CONSULTATION,

    //座椅向上调节
    RISE,

    //座椅向下调节
    DOWN,


    //如果心跳信号丢失了，要发送重连wifi信号，排除是wifi掉线导致的
    RECONNECTWIFI,

    //在参数设置界面点击重启，重新进入LaunchActivity
    STARTLAUN,


    AUTOTRANFUSIONSTART,
    AUTOTRANFUSIONEND,
    PLASMAWEIGHT,
    PIPELOW,
    PIPENORMAL,
    PAUSED,
    END,

    //
    LOWPOWER,


    AVAILABLERES,
    WAITING,
    STARTPUNTUREVIDEO,
    STARTCOLLECTIONVIDEO,


    //
    SETTINGS,
    RESTART,


    // 这个是视频列表
    TOVIDEOLIST,
    //视频分类列表
    TOVIDEOCATEGORY,

    //视频全屏播放
    TOVIDEO_FULLSCREEN,

    //视频没有全屏播放
    TOVIDEO_NOT_FULLSCREEN,

    // 这个是音乐列表
    TOMUSICLIST,
    //音乐分类列表
    TOMUSICCATEGORY,

    //开始播放音乐
    STARTMUSIC,

    // Switch between the tabs

    TOSURF,
    TOSUGGEST,
    TOAPPOINT,

    //between activity and fragment
    VIDEOTOMAIN,
    CLICKSUGGESTION,
    CLICKEVALUATION,
    CLICKAPPOINTMENT,
    SAVEAPPOINTMENT,
    SAVESUGGESTION,
    SAVEEVALUATION,
    AUTH,
    STARTVIDEO,

    //back button
    BACKTOVIDEOLIST,
    BACKTOADVICE,
    BACKTOAPPOINTMENT,

    //
    NOTHING,

    //The three physical keys
    POWEROFF,
    RECENT,
    HOME
}
