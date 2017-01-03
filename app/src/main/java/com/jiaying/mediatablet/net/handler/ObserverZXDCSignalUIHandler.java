package com.jiaying.mediatablet.net.handler;

import android.os.Message;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.Invoker;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.signal.command.AuthPassCommand;
import com.jiaying.mediatablet.net.signal.command.AuthResOverCommand;
import com.jiaying.mediatablet.net.signal.command.CheckOverCommand;
import com.jiaying.mediatablet.net.signal.command.CheckStartCommand;
import com.jiaying.mediatablet.net.signal.command.Command;
import com.jiaying.mediatablet.net.signal.command.CompressionCommand;
import com.jiaying.mediatablet.net.signal.command.ConfirmCommand;
import com.jiaying.mediatablet.net.signal.command.EndCommand;
import com.jiaying.mediatablet.net.signal.command.LowPowerCommand;
import com.jiaying.mediatablet.net.signal.command.PlasmaWeightCommand;
import com.jiaying.mediatablet.net.signal.command.PlayColVideoCommand;
import com.jiaying.mediatablet.net.signal.command.PlayMusicCommand;
import com.jiaying.mediatablet.net.signal.command.PlayVideoCommand;
import com.jiaying.mediatablet.net.signal.command.PlayVideoFinishCommand;
import com.jiaying.mediatablet.net.signal.command.PunctureCommand;
import com.jiaying.mediatablet.net.signal.command.ReconnectWIFICommand;
import com.jiaying.mediatablet.net.signal.command.RestartLaunchActivityCommand;
import com.jiaying.mediatablet.net.signal.command.RestartMainAcitivityCommand;
import com.jiaying.mediatablet.net.signal.command.SerAuthResCommand;
import com.jiaying.mediatablet.net.signal.command.SettingCommand;
import com.jiaying.mediatablet.net.signal.command.StartCommand;
import com.jiaying.mediatablet.net.signal.command.StartFistCommand;
import com.jiaying.mediatablet.net.signal.command.StopFistCommand;
import com.jiaying.mediatablet.net.signal.command.TimeSynCommand;
import com.jiaying.mediatablet.net.signal.command.ToMusicCategoryCommand;
import com.jiaying.mediatablet.net.signal.command.ToMusicListCommand;
import com.jiaying.mediatablet.net.signal.command.ToSurfCommand;
import com.jiaying.mediatablet.net.signal.command.ToVideoCategoryCommand;
import com.jiaying.mediatablet.net.signal.command.ToVideoListCommand;
import com.jiaying.mediatablet.net.signal.command.VideoFullScreenCommand;
import com.jiaying.mediatablet.net.signal.command.VideoNotFullScreenCommand;
import com.jiaying.mediatablet.net.signal.command.ZXDCAuthResCommand;
import com.jiaying.mediatablet.net.signal.receiver.AuthPassReceiver;
import com.jiaying.mediatablet.net.signal.receiver.AuthResOverReceiver;
import com.jiaying.mediatablet.net.signal.receiver.AuthResTimeoutReceiver;
import com.jiaying.mediatablet.net.signal.receiver.CheckOverReceiver;
import com.jiaying.mediatablet.net.signal.receiver.CheckStartReceiver;
import com.jiaying.mediatablet.net.signal.receiver.CompressionReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ConfirmReceiver;
import com.jiaying.mediatablet.net.signal.receiver.EndReceiver;
import com.jiaying.mediatablet.net.signal.receiver.LowPowerReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PlasmaWeightReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PlayColVideoReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PlayMusicReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PlayVideoFinishReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PlayVideoReceiver;
import com.jiaying.mediatablet.net.signal.receiver.PunctureReceiver;
import com.jiaying.mediatablet.net.signal.receiver.Receiver;
import com.jiaying.mediatablet.net.signal.receiver.ReconnectWIFIReceiver;
import com.jiaying.mediatablet.net.signal.receiver.RestartLaunchActivityReceiver;
import com.jiaying.mediatablet.net.signal.receiver.RestartMainActivityReceiver;
import com.jiaying.mediatablet.net.signal.receiver.SerAuthResReceiver;
import com.jiaying.mediatablet.net.signal.receiver.SettingReceiver;
import com.jiaying.mediatablet.net.signal.receiver.StartFistReceiver;
import com.jiaying.mediatablet.net.signal.receiver.StartReceiver;
import com.jiaying.mediatablet.net.signal.receiver.StopFistReceiver;
import com.jiaying.mediatablet.net.signal.receiver.StopRecReceiver;
import com.jiaying.mediatablet.net.signal.receiver.TimeSynReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ToMusicCategoryReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ToMusicListReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ToSurfReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ToVideoCategoryReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ToVideoListReceiver;
import com.jiaying.mediatablet.net.signal.receiver.VideoFullScreenReceiver;
import com.jiaying.mediatablet.net.signal.receiver.VideoNotFullScreenReceiver;
import com.jiaying.mediatablet.net.signal.receiver.ZXDCAuthResReceiver;
import com.jiaying.mediatablet.utils.ToastUtils;

import java.lang.ref.SoftReference;
import java.util.Observable;

/**
 * Created by Administrator on 2015/9/13 0013.
 */
public class ObserverZXDCSignalUIHandler extends android.os.Handler implements java.util.Observer {

    private SoftReference<MainActivity> srMActivity;

    private Invoker invoker;
    private Receiver receiver;
    private Command command;

    public ObserverZXDCSignalUIHandler(SoftReference<MainActivity> mActivity) {
        this.srMActivity = mActivity;
        invoker = new Invoker();

    }

    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch ((RecSignal) msg.obj) {

            case RECONNECTWIFI:
                receiver = new ReconnectWIFIReceiver(srMActivity.get());
                command = new ReconnectWIFICommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case STARTLAUN:
                receiver = new RestartLaunchActivityReceiver(srMActivity.get());
                command = new RestartLaunchActivityCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;


            case TIMESTAMP:
                receiver = new TimeSynReceiver(srMActivity.get());
                command = new TimeSynCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case BTCONSTART:
                dealSignalBTConstart(this);
                break;

            case BTCONFAILURE:
                dealSignalBTConfailure(this);
                break;

            case LOWPOWER:
                receiver = new LowPowerReceiver(srMActivity.get());
                command = new LowPowerCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // The nurse make sure the info of the donor is right.
            case CONFIRM:
                ToastUtils.showToast( this.srMActivity.get(), "收到了auth_info命令");
                receiver = new ConfirmReceiver(srMActivity.get());
                command = new ConfirmCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case RECORDDONORVIDEO:

                break;

            case RECORDNURSEVIDEO:

                break;

            case AUTHPASS:
                receiver = new AuthPassReceiver(srMActivity.get());
                command = new AuthPassCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case AUTHRESTIMEOUT:
                receiver = new AuthResTimeoutReceiver(srMActivity.get());
                command = new AuthPassCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case SERAUTHRES:
                ToastUtils.showToast(this.srMActivity.get(), "收到服务器的auth_pass应答");
                receiver = new SerAuthResReceiver(srMActivity.get());
                command = new SerAuthResCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case ZXDCAUTHRES:
                ToastUtils.showToast(this.srMActivity.get(), "收到单采机的auth_pass应答");
                receiver = new ZXDCAuthResReceiver(srMActivity.get());
                command = new ZXDCAuthResCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case AUTHRESOK:
                receiver = new AuthResOverReceiver(srMActivity.get());
                command = new AuthResOverCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case COMPRESSINON:
                receiver = new CompressionReceiver(srMActivity.get());
                command = new CompressionCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case STOPREC:
                receiver = new StopRecReceiver(srMActivity.get());
                command = new SerAuthResCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // The nurse punctuate the donor.
            case PUNCTURE:
                receiver = new PunctureReceiver(srMActivity.get());
                command = new PunctureCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // Start the collection of plasma.
            case START:
                receiver = new StartReceiver(srMActivity.get());
                command = new StartCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // Start the startSpeech the video collection of plasma.
            case STARTCOLLECTIONVIDEO:
                receiver = new PlayColVideoReceiver(srMActivity.get());
                command = new PlayColVideoCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOVIDEOLIST:
                receiver = new ToVideoListReceiver(srMActivity.get());
                command = new ToVideoListCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOVIDEOCATEGORY:
                receiver = new ToVideoCategoryReceiver(srMActivity.get());
                command = new ToVideoCategoryCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOVIDEO_FULLSCREEN:
                receiver = new VideoFullScreenReceiver(srMActivity.get());
                command = new VideoFullScreenCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOVIDEO_NOT_FULLSCREEN:
                receiver = new VideoNotFullScreenReceiver(srMActivity.get());
                command = new VideoNotFullScreenCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOSURF:
                receiver = new ToSurfReceiver(srMActivity.get());
                command = new ToSurfCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case TOMUSICCATEGORY:
//                dealSignalToMusicCategory(this);
                receiver = new ToMusicCategoryReceiver(srMActivity.get());
                command = new ToMusicCategoryCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;
            case TOMUSICLIST:
//                dealSignalToMusic(this);
                receiver = new ToMusicListReceiver(srMActivity.get());
                command = new ToMusicListCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case PLASMAWEIGHT:
                receiver = new PlasmaWeightReceiver(srMActivity.get());
                command = new PlasmaWeightCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // The pressure is not enough, recommend the donor to make a fist.
            case PIPELOW:
                receiver = new StartFistReceiver(srMActivity.get());
                command = new StartFistCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case PIPENORMAL:
                receiver = new StopFistReceiver(srMActivity.get());
                command = new StopFistCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;


            case VIDEOTOMAIN:
                receiver = new PlayVideoFinishReceiver(srMActivity.get());
                command = new PlayVideoFinishCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case BACKTOVIDEOLIST:
                receiver = new PlayVideoFinishReceiver(srMActivity.get());
                command = new PlayVideoFinishCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case STARTVIDEO:
                receiver = new PlayVideoReceiver(srMActivity.get());
                command = new PlayVideoCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case STARTMUSIC:
//                dealSignalStartMusic(this);
                receiver = new PlayMusicReceiver(srMActivity.get());
                command = new PlayMusicCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            // The collection is over.
            case END:
                receiver = new EndReceiver(srMActivity.get());
                command = new EndCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case CHECKSTART:
                receiver = new CheckStartReceiver(srMActivity.get());
                command = new CheckStartCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case CHECKOVER:
                receiver = new CheckOverReceiver(srMActivity.get());
                command = new CheckOverCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case RESTART:
                receiver = new RestartMainActivityReceiver(srMActivity.get());
                command = new RestartMainAcitivityCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            case SETTINGS:
                receiver = new SettingReceiver(srMActivity.get());
                command = new SettingCommand(receiver);
                invoker.setCommand(command);
                invoker.action();
                break;

            default:
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = Message.obtain();
        switch ((RecSignal) data) {

            case RECONNECTWIFI:
                msg.obj = RecSignal.RECONNECTWIFI;
                sendMessage(msg);
                break;

            case STARTLAUN:
                msg.obj = RecSignal.STARTLAUN;
                sendMessage(msg);
                break;

            case WAITING:
                msg.obj = RecSignal.WAITING;
                sendMessage(msg);
                break;

            case TIMESTAMP:
                msg.obj = RecSignal.TIMESTAMP;
                sendMessage(msg);
                break;

            case BTCONSTART:
                msg.obj = RecSignal.BTCONSTART;
                sendMessage(msg);
                break;

            case BTCONFAILURE:
                msg.obj = RecSignal.BTCONFAILURE;
                sendMessage(msg);
                break;

            case LOWPOWER:
                msg.obj = RecSignal.LOWPOWER;
                sendMessage(msg);
                break;

            case CONFIRM:
                msg.obj = RecSignal.CONFIRM;
                sendMessage(msg);
                break;

            case RECORDDONORVIDEO:
                msg.obj = RecSignal.RECORDDONORVIDEO;
                sendMessage(msg);
                break;

            case RECORDNURSEVIDEO:
                msg.obj = RecSignal.RECORDNURSEVIDEO;
                sendMessage(msg);
                break;

            case AUTHPASS:
                msg.obj = RecSignal.AUTHPASS;
                sendMessage(msg);
                break;

            case AUTHRESTIMEOUT:
                msg.obj = RecSignal.AUTHRESTIMEOUT;
                sendMessage(msg);
                break;

            case SERAUTHRES:
                msg.obj = RecSignal.SERAUTHRES;
                sendMessage(msg);
                break;

            case ZXDCAUTHRES:
                msg.obj = RecSignal.ZXDCAUTHRES;
                sendMessage(msg);
                break;

            case AUTHRESOK:
                msg.obj = RecSignal.AUTHRESOK;
                sendMessage(msg);
                break;

            case COMPRESSINON:
                msg.obj = RecSignal.COMPRESSINON;
                sendMessage(msg);
                break;

            case STOPREC:
                msg.obj = RecSignal.STOPREC;
                sendMessage(msg);
                break;

            case PUNCTURE:
                msg.obj = RecSignal.PUNCTURE;
                sendMessage(msg);
                break;

            case STARTPUNTUREVIDEO:
                msg.obj = RecSignal.STARTPUNTUREVIDEO;
                sendMessage(msg);
                break;

            case START:
                msg.obj = RecSignal.START;
                sendMessage(msg);
                break;

            case STARTCOLLECTIONVIDEO:
                msg.obj = RecSignal.STARTCOLLECTIONVIDEO;
                sendMessage(msg);
                break;

            case TOVIDEOLIST:
                msg.obj = RecSignal.TOVIDEOLIST;
                sendMessage(msg);
                break;

            case TOVIDEOCATEGORY:
                msg.obj = RecSignal.TOVIDEOCATEGORY;
                sendMessage(msg);
                break;

            case TOVIDEO_FULLSCREEN:
                msg.obj = RecSignal.TOVIDEO_FULLSCREEN;
                sendMessage(msg);
                break;

            case TOVIDEO_NOT_FULLSCREEN:
                msg.obj = RecSignal.TOVIDEO_NOT_FULLSCREEN;
                sendMessage(msg);
                break;

            case TOSURF:
                msg.obj = RecSignal.TOSURF;
                sendMessage(msg);
                break;

            case TOMUSICCATEGORY:
                msg.obj = RecSignal.TOMUSICCATEGORY;
                sendMessage(msg);
                break;

            case TOMUSICLIST:
                msg.obj = RecSignal.TOMUSICLIST;
                sendMessage(msg);
                break;

            case TOSUGGEST:
                msg.obj = RecSignal.TOSUGGEST;
                sendMessage(msg);
                break;

            case CLICKSUGGESTION:
                msg.obj = RecSignal.CLICKSUGGESTION;
                sendMessage(msg);
                break;

            case CLICKEVALUATION:
                msg.obj = RecSignal.CLICKEVALUATION;
                sendMessage(msg);
                break;

            case TOAPPOINT:
                msg.obj = RecSignal.TOAPPOINT;
                sendMessage(msg);
                break;

            case CLICKAPPOINTMENT:
                msg.obj = RecSignal.CLICKAPPOINTMENT;
                sendMessage(msg);
                break;

            case PIPELOW:
                msg.obj = RecSignal.PIPELOW;
                sendMessage(msg);
                break;

            case PIPENORMAL:
                msg.obj = RecSignal.PIPENORMAL;
                sendMessage(msg);
                break;

            case SAVEAPPOINTMENT:
                msg.obj = RecSignal.SAVEAPPOINTMENT;
                sendMessage(msg);
                break;

            case SAVESUGGESTION:
                msg.obj = RecSignal.SAVESUGGESTION;
                sendMessage(msg);
                break;

            case SAVEEVALUATION:
                msg.obj = RecSignal.SAVEEVALUATION;
                sendMessage(msg);
                break;
            case BACKTOVIDEOLIST:
                msg.obj = RecSignal.BACKTOVIDEOLIST;
                sendMessage(msg);
                break;

            case STARTVIDEO:
                msg.obj = RecSignal.STARTVIDEO;
                sendMessage(msg);
                break;

            case STARTMUSIC:
                msg.obj = RecSignal.STARTMUSIC;
                sendMessage(msg);
                break;

            case PLASMAWEIGHT:
                msg.obj = RecSignal.PLASMAWEIGHT;
                sendMessage(msg);
                break;

            case END:
                msg.obj = RecSignal.END;
                sendMessage(msg);
                break;

            case CHECKSTART:
                msg.obj = RecSignal.CHECKSTART;
                sendMessage(msg);
                break;

            case CHECKOVER:
                msg.obj = RecSignal.CHECKOVER;
                sendMessage(msg);
                break;

            case RESTART:
                msg.obj = RecSignal.RESTART;
                sendMessage(msg);
                break;

            case SETTINGS:
                msg.obj = RecSignal.SETTINGS;
                sendMessage(msg);
                break;


            default:
                break;
        }
    }


    private void dealSignalBTConstart(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealBTCon();
    }

    private void dealSignalBTConfailure(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealBTConFailure();
    }
}