package com.jiaying.mediatablet.net.state.RecoverState;

import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.stateswitch.CollectionState;
import com.jiaying.mediatablet.net.state.stateswitch.EndState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForAuthState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForCheckOverState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForCompressionState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForDonorState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForPunctureState;

import com.jiaying.mediatablet.net.state.stateswitch.WaitingForSerResState;

import com.jiaying.mediatablet.net.state.stateswitch.WaitingForStartState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForTimestampState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/4/3.
 */
public class RecoverState {

    public void recover(RecordState recordState, ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread, TabletStateContext tabletStateContext) {

        recordState.retrieve();
        String state = recordState.getState();
        if (state == null) {
            //设置当前的状态为结束状态
            tabletStateContext.setCurrentState(EndState.getInstance());

            //发送开始检查设备命令
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
        } else if (StateIndex.SYNTIME.equals(state)) {
            //检查电量状态的恢复方式
            //设置当前的状态为等待时间同步状态
            tabletStateContext.setCurrentState(WaitingForTimestampState.getInstance());

        } else if (StateIndex.WAITINGFORCHECKOVER.equals(state)) {
            //检查电量状态的恢复方式
            //设置当前的状态为结束状态
            tabletStateContext.setCurrentState(EndState.getInstance());

            //发送开始检查设备命令
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
        } else if (StateIndex.WAITINGFORDONOR.equals(state)) {
            //等待献浆员状态
            //设置当前的状态为检查中
            tabletStateContext.setCurrentState(WaitingForCheckOverState.getInstance());

            //发送检查通过信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);
        } else if (StateIndex.WAITINGFORAUTH.equals(state)) {
            //等待认证通过
            //设置当前的状态为等待献浆状态
            tabletStateContext.setCurrentState(WaitingForDonorState.getInstance());

            //发送浆员信息信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CONFIRM);
        } else if (StateIndex.WAITINGFORCOMPRESSION.equals(state)) {
            //等待加压状态
            //设置当前的状态为等待服务器应答状态
            tabletStateContext.setCurrentState(WaitingForSerResState.getInstance());

            //发送服务器收到认证通过信号应答
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SERAUTHRES);
        } else if (StateIndex.WAITINGFORPUNCTURE.equals(state)) {
            //恢复等待穿刺状态
            //设置当前的状态为等待加压
            tabletStateContext.setCurrentState(WaitingForCompressionState.getInstance());

            //发送加压信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.COMPRESSINON);
        } else if (StateIndex.WAITINGFORSTART.equals(state)) {
            //恢复等待开始采集状态
            //设置当前的状态为等待穿刺状态
            tabletStateContext.setCurrentState(WaitingForPunctureState.getInstance());

            //发送穿刺信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PUNCTURE);
        } else if (StateIndex.COLLECTION.equals(state)) {
            //恢复采集中状态
            //设置当前的状态为等待开始采集状态
            tabletStateContext.setCurrentState(WaitingForStartState.getInstance());

            //发送开始采集信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.START);
        } else if (StateIndex.END.equals(state)) {
            //恢复结束采浆状态
            //设置当前的状态为采集中状态
            tabletStateContext.setCurrentState(CollectionState.getInstance());

            //发送采集结束信号
            tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.END);
        }
    }

}
