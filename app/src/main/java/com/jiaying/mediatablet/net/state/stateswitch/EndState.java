package com.jiaying.mediatablet.net.state.stateswitch;

import android.graphics.Bitmap;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.utils.BitmapUtils;

import java.util.HashMap;

/**
 * Created by hipil on 2016/4/20.
 */
public class EndState extends AbstractState {
    private static EndState ourInstance = new EndState();

    public static EndState getInstance() {
        return ourInstance;
    }

    private EndState() {
    }

    @Override
    protected void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread, DataCenterRun dataCenterRun,
                                 DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
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

            case CHECKSTART:
                //记录状态
                recordState.recCheckStart();
                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForCheckOverState.getInstance());
                //发送信号
                listenerThread.notifyObservers(RecSignal.CHECKSTART);

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
