package com.jiaying.mediatablet.net.state.stateswitch;

import android.graphics.Bitmap;
import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import android.util.Log;


import com.jiaying.mediatablet.entity.AuthPassFace;
import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;


import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;


import com.jiaying.mediatablet.utils.BitmapUtils;
import com.jiaying.mediatablet.utils.MyLog;

import com.jiaying.mediatablet.thread.SendVideoThread;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.SelfFile;


import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


import java.io.File;
import java.io.FileOutputStream;

import java.util.Date;

import java.util.HashMap;

/**
 * Created by hipil on 2016/4/13.
 */
public class WaitingForAuthState extends AbstractState {
    private static WaitingForAuthState waitingForAuthState = null;

    private WaitingForAuthState() {
    }

    public static WaitingForAuthState getInstance() {
        if (waitingForAuthState == null) {
            waitingForAuthState = new WaitingForAuthState();
        }
        return waitingForAuthState;
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

            case CANCLEAUTHPASS:
                String iid = textUnit.ObjToString(cmd.getValue("donorId"));

                if(DonorEntity.getInstance().getDocument().getId().equals(iid)) {

                    //记录状态
                    recordState.recCheckOver();

                    //获取数据

                    //切换状态
                    tabletStateContext.setCurrentState(WaitingForDonorState.getInstance());

                    //发送信号
                    listenerThread.notifyObservers(RecSignal.CHECKOVER);
                }
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


            case AUTHPASS:

                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForSerZxdcResState.getInstance());

                //发送信号
                sendAuthPassCmd();

                sendAuthPassCmd1();

                sendAuthPassPic();
                listenerThread.notifyObservers(RecSignal.AUTHPASS);

                break;

            case RECORDDONORVIDEO:
                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(RecordAuthVideoState.getInstance());
                //发送信号
                listenerThread.notifyObservers(RecSignal.RECORDDONORVIDEO);
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

    private void sendAuthPassCmd() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("authentication_donor");
        retcmd.setHasResponse(true);
        retcmd.setLevel(2);
        HashMap<String, Object> values = new HashMap<>();
        values.put("donorId", DonorEntity.getInstance().getIdentityCard().getId());
        values.put("deviceId", DeviceEntity.getInstance().getAp());
        values.put("isManual", "false");
        retcmd.setValues(values);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }

    private void sendAuthPassCmd1() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("auth_pass");
        retcmd.setHasResponse(true);
        retcmd.setLevel(2);
        HashMap<String, Object> values = new HashMap<>();
        values.put("donorId", DonorEntity.getInstance().getIdentityCard().getId());
        values.put("isManual", "false");
        retcmd.setValues(values);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }


    private void sendAuthPassPic() {


        if (AuthPassFace.authFace != null)
        // 这里的copy是整张图像
        {
            Mat copy = AuthPassFace.authFace;

            MatOfByte mob = new MatOfByte();

            Imgcodecs.imencode(".jpg", copy, mob);

            byte[] byteArray = mob.toArray();

            byte2image(byteArray, "/sdcard/authpass.pic");
            new SendVideoThread("/sdcard/authpass.pic", SelfFile.generateRemotePicName()).start();
        } else {

        }

    }

    public void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) return;
        try {
            FileOutputStream imageOutput = new FileOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            Log.e("error", "图片保存成功");
        } catch (Exception ex) {
            Log.e("error", "图片保存失败");
            ex.printStackTrace();
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
