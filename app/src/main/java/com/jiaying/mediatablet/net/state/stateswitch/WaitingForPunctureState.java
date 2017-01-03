package com.jiaying.mediatablet.net.state.stateswitch;

import android.graphics.Bitmap;
import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.utils.BitmapUtils;

import java.util.HashMap;

/**
 * Created by hipil on 2016/4/13.
 */
public class WaitingForPunctureState extends AbstractState {
    private static WaitingForPunctureState waitingForPunctureState = null;

    private WaitingForPunctureState() {
    }

    public static WaitingForPunctureState getInstance() {
        if (waitingForPunctureState == null) {
            waitingForPunctureState = new WaitingForPunctureState();
        }
        return waitingForPunctureState;
    }

    @Override
    public synchronized void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread,
                                           DataCenterRun dataCenterRun, DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
        switch (recSignal) {

            //记录状态

            //获取数据

            //切换状态

            //发送信号

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

            case CONFIRM:

                //获取到浆员信息状态
                recordState.recConfirm();

                //切换到认证状态
                tabletStateContext.setCurrentState(WaitingForAuthState.getInstance());

                //记录浆员信息
                if (cmd != null) {
                    setDonor(DonorEntity.getInstance(), cmd);

                }

                //发送信号
                listenerThread.notifyObservers(RecSignal.CONFIRM);
                break;


            case RECONNECTWIFI:
                listenerThread.notifyObservers(RecSignal.RECONNECTWIFI);
                break;

            case PUNCTURE:
                //记录状态
                recordState.recPuncture();

                //获取数据

                //状态切换
                tabletStateContext.setCurrentState(WaitingForStartState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.PUNCTURE);
                break;

            case STOPREC:
                //记录状态


                //获取数据

                //状态切换

                //发送信号
                listenerThread.notifyObservers(RecSignal.STOPREC);
                break;

            case START:
                //记录状态
                recordState.recCollection();

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(CollectionState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.START);
                if (cmd != null) {
                    sendTabletRevStartCmdRes();
                }
                break;

            case RESTART:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.RESTART);
                break;

            case TOVIDEO_FULLSCREEN:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOVIDEO_FULLSCREEN);
                break;
            case TOVIDEO_NOT_FULLSCREEN:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOVIDEO_NOT_FULLSCREEN);
                break;

            case TISSUE:
                sendCallForTissueCmd("tissue");
                break;

            case BOILEDWATER:
                sendCallForTissueCmd("boiledWater");
                break;

            case CANDY:
                sendCallForTissueCmd("CANDY");
                break;

            case MAGAZINE:
                sendCallForTissueCmd("MAGAZINE");
                break;

            case CONSULTATION:
                sendCallForTissueCmd("CONSULTATION");
                break;

            case RISE:
                sendChairRiseCmd();
                break;

            case DOWN:
                sendChairDownCmd();
                break;
        }
    }

    private void sendTabletRevStartCmdRes() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("tablet_rev_start");
        retcmd.setHasResponse(false);
        retcmd.setLevel(2);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }

    private void sendChairRiseCmd() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("chair_rise");
        retcmd.setHasResponse(false);
        retcmd.setLevel(2);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }

    private void sendChairDownCmd() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("chair_down");
        retcmd.setHasResponse(false);
        retcmd.setLevel(2);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }

    private void sendCallForTissueCmd(String content) {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("callService");
        retcmd.setHasResponse(false);
        retcmd.setLevel(2);
        HashMap<String, Object> values = new HashMap<>();
        values.put("content", content);
        retcmd.setValues(values);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }
    //设置浆员信息
    private void setDonor(DonorEntity donorEntity, DataCenterTaskCmd cmd) {
        String iaddress = textUnit.ObjToString(cmd.getValue("address"));
        String ibirth_year = textUnit.ObjToString(cmd.getValue("year"));
        String ibirth_month = textUnit.ObjToString(cmd.getValue("month"));
        String ibirth_day = textUnit.ObjToString(cmd.getValue("day"));
        Bitmap ifaceBitmap = BitmapUtils.base64ToBitmap(textUnit.ObjToString(cmd.getValue("face")));
        String igender = textUnit.ObjToString(cmd.getValue("gender"));
        String iid = textUnit.ObjToString(cmd.getValue("donor_id"));
        String iname = textUnit.ObjToString(cmd.getValue("donor_name"));
        String ination = textUnit.ObjToString(cmd.getValue("nationality"));

        PersonInfo identityCard = new PersonInfo(iaddress, ibirth_year, ibirth_month, ibirth_day, ifaceBitmap, igender, iid, iname, ination);

        donorEntity.setIdentityCard(identityCard);


        String daddress = textUnit.ObjToString(cmd.getValue("dz"));

        Bitmap dfaceBitmap = BitmapUtils.base64ToBitmap(textUnit.ObjToString(cmd.getValue("photo")));
        String dgender = textUnit.ObjToString(cmd.getValue("sex"));
        String did = textUnit.ObjToString(cmd.getValue("donor_id"));


        PersonInfo document = new PersonInfo(daddress, "****", "**", "**", dfaceBitmap, dgender, did, "***", "*");

        donorEntity.setDocument(document);

    }



}
