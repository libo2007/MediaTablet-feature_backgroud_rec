package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

import java.util.HashMap;

/**
 * Created by hipil on 2016/5/12.
 */
public class RecordAuthVideoState extends AbstractState {
    private static RecordAuthVideoState ourInstance = new RecordAuthVideoState();

    public static RecordAuthVideoState getInstance() {
        return ourInstance;
    }

    private RecordAuthVideoState() {
    }

    @Override
    protected void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread,
                                 DataCenterRun dataCenterRun, DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
        switch (recSignal) {

            case TIMESTAMP:
                //记录状态

                //获取数据
                if ("timestamp".equals(cmd.getCmd())) {
                    ServerTime.curtime = Long.parseLong(textUnit.ObjToString(cmd.getValue("t")));
                }
                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TIMESTAMP);

                break;


            case RECONNECTWIFI:
                listenerThread.notifyObservers(RecSignal.RECONNECTWIFI);
                break;

            case RECORDNURSEVIDEO:
                listenerThread.notifyObservers(recSignal);
                break;

            case AUTHPASS:

                //切换到等待服务器和浆机应答状态
                tabletStateContext.setCurrentState(WaitingForSerZxdcResState.getInstance());

                //向服务器发送认证通过信号
                sendManualAuthPassCmd();

                //发送信号
                listenerThread.notifyObservers(RecSignal.AUTHPASS);
                break;

            case RESTART:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.RESTART);
                break;

        }
    }

    private void sendManualAuthPassCmd() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("authentication_donor");
        retcmd.setHasResponse(true);
        retcmd.setLevel(2);
        HashMap<String, Object> values = new HashMap<>();
        values.put("donorId", DonorEntity.getInstance().getIdentityCard().getId());
        values.put("deviceId", DeviceEntity.getInstance().getAp());
        values.put("isManual", "true");
        retcmd.setValues(values);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }

}
