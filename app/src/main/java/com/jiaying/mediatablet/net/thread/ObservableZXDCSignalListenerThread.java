package com.jiaying.mediatablet.net.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.IDataCenterProcess;
import android.softfan.dataCenter.config.DataCenterClientConfig;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.dataCenter.task.IDataCenterNotify;
import android.util.Log;

import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.net.serveraddress.SignalServer;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecoverState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.utils.Conversion;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;

/**
 * Created by hipilee on 2014/11/19.
 */
// Consider using AsyncTask or HandlerThread
public class ObservableZXDCSignalListenerThread extends Thread implements IDataCenterNotify, IDataCenterProcess {

    public static String TAG = "ObservableZXDCSignalListenerThread";
    private ObservableHint observableHint;

    private RecordState recordState;
    private RecoverState recoverState;
    private TabletStateContext tabletStateContext;


    private static DataCenterClientService clientService;

    public ObservableZXDCSignalListenerThread(RecordState recordState, TabletStateContext tabletStateContext) {

        this.observableHint = new ObservableHint();
        this.recordState = recordState;
        this.recoverState = new RecoverState();
        this.tabletStateContext = tabletStateContext;
    }

    public void addObserver(Observer observer) {
        observableHint.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        observableHint.deleteObserver(observer);
    }

    public synchronized void notifyObservers(RecSignal signal) {

//        POWEROFF应用被异常关闭，需要立即保存现场供恢复现场使用
        if (signal == RecSignal.POWEROFF) {
            this.recordState.commit();
        } else {
            this.observableHint.notifyObservers(signal);
        }
    }


    @Override
    public void run() {
        super.run();
//        开始可以处理消息了，但是此时还未登陆服务器，所以不会接收到服务器信号
        this.tabletStateContext.open();

//        恢复状态
        this.recoverState.recover(recordState, this, this.tabletStateContext);

/*      统一关闭和服务器的连接，这样避免的问题是如果还是使用上一次使用的clientService
        就会造成config.setProcess(this)中的this没有更新。
        private static List<DataCenterClientService> clientServices = new ArrayList();
        因为这是一个静态变量，会跟随类一直存在*/
        DataCenterClientService.shutdown();

//        是否已经存在该客户端
        clientService = DataCenterClientService.get(DeviceEntity.getInstance().getAp(), "*");
        if (clientService == null) {

//            填写配置
            DataCenterClientConfig config = new DataCenterClientConfig();

//            服务器IP地址
            config.setAddr(SignalServer.getInstance().getIp());

//            服务器端口地址
            config.setPort(SignalServer.getInstance().getPort());

//            客户端AP号
            config.setAp(DeviceEntity.getInstance().getAp());

//            客户端组织号
            config.setOrg(DeviceEntity.getInstance().getOrg());

//            密码
            config.setPassword(DeviceEntity.getInstance().getPassword());

//            服务器AP号
            config.setServerAp(DeviceEntity.getInstance().getServerAp());

//            服务器组织号
            config.setServerOrg(DeviceEntity.getInstance().getServerOrg());

//            设置回调函数的处理对象
            config.setProcess(this);

//            连接服务器
            DataCenterClientService.startup(config);

            clientService = DataCenterClientService.get(DeviceEntity.getInstance().getAp(), DeviceEntity.getInstance().getOrg());
        }


        while (!isInterrupted()) {
            synchronized (this) {
                try {
                    Log.e(TAG, "循环执行 " + this.toString().hashCode());
                    this.wait(5000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class ObservableHint extends Observable {
        private ArrayList<Observer> arrayListObserver;

        private ObservableHint() {
            arrayListObserver = new ArrayList<>();
        }

        @Override
        public void addObserver(Observer observer) {
            super.addObserver(observer);
            arrayListObserver.add(observer);
        }

        @Override
        public synchronized void deleteObserver(Observer observer) {
            super.deleteObserver(observer);
            arrayListObserver.remove(observer);
        }

        @Override
        public void notifyObservers(Object data) {
            super.notifyObservers(data);
            for (Observer observer : arrayListObserver) {
                observer.update(observableHint, data);
            }
        }
    }


    public void onSend(DataCenterTaskCmd selfCmd) throws DataCenterException {
    }

    public void onResponse(DataCenterTaskCmd selfCmd, DataCenterTaskCmd responseCmd) throws DataCenterException {
    }

    public void onFree(DataCenterTaskCmd selfCmd) {
    }

    public void processMsg(DataCenterRun dataCenterRun, DataCenterTaskCmd cmd) throws DataCenterException {
//在这个地方没有直接将命令传出去，而是转换一次不太合适；
        RecSignal recSignal = Conversion.conver(cmd.getCmd());
        Log.e(TAG, "得到的消息命令是" + cmd.getCmd() + " 转换后为 " + recSignal.toString());

        if (cmd.isHasResponse()) {
            DataCenterTaskCmd resCmd = new DataCenterTaskCmd();
            sendResCmd(resCmd, cmd, dataCenterRun);
        }

//        所有的消息先需要通过状态机制过滤，符合当下状态的信号才可以被发出去；
        this.tabletStateContext.handleMessge(recordState, this,
                dataCenterRun, cmd, Conversion.conver(cmd.getCmd()));
    }

    private void sendResCmd(DataCenterTaskCmd retcmd, DataCenterTaskCmd cmd, DataCenterRun dataCenterRun) {
        retcmd.setSeq(cmd.getSeq());
        retcmd.setCmd("response");
        HashMap<String, Object> values = new HashMap<>();
        retcmd.setValues(values);
        try {
            dataCenterRun.sendResponseCmd(retcmd);
        } catch (DataCenterException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void processResponseMsg(DataCenterRun dataCenterRun, DataCenterTaskCmd dataCenterTaskCmd, DataCenterTaskCmd dataCenterTaskCmd1) throws DataCenterException {

        Log.e(TAG, "得到的应消息是 " + "dataCenterTaskCmd: " + dataCenterTaskCmd.getCmd() + " dataCenterTaskCmd1: " + dataCenterTaskCmd1.getCmd());
        if ("authentication_donor".equals(dataCenterTaskCmd1.getCmd())) {
            this.tabletStateContext.handleMessge(recordState, this, dataCenterRun, dataCenterTaskCmd, RecSignal.SERAUTHRES);
        }
        if ("auth_pass".equals(dataCenterTaskCmd1.getCmd())) {
            this.tabletStateContext.handleMessge(recordState, this, dataCenterRun, dataCenterTaskCmd, RecSignal.SERAUTHRES);
        }

    }

    @Override
    public void onSended(DataCenterTaskCmd selfCmd) throws DataCenterException {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendTimeout(DataCenterTaskCmd selfCmd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResponseTimeout(DataCenterTaskCmd selfCmd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAdd(DataCenterTaskCmd dataCenterTaskCmd, List<DataCenterTaskCmd> list) {

    }

    public void startMsgProcess() {

        Log.e(TAG, "开始处理消息");

    }

    public void stopMsgProcess() {
//// TODO: 2016/9/20 发送一个重连wifi的命令,这个wifi断了重连还是要依靠，ping命令去检测更可靠；
        Log.e(TAG, "停止处理消息");

    }

    public static DataCenterClientService getClientService() {

        return clientService;
    }
}
