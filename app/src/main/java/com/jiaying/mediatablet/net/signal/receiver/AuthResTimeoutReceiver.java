package com.jiaying.mediatablet.net.signal.receiver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/14.
 */
public class AuthResTimeoutReceiver extends Receiver{
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;
    private AlertDialog failAllocDialog;

    public AuthResTimeoutReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        mainActivity.uiComponent(false, true, false, false);

        //界面切换
        showTimeoutDialog("应答失败！", "超时");

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.authres);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
    }

    //超时对话框
    private void showTimeoutDialog(String title, String msg) {
        if (mainActivity.isFinishing()) {
            return;
        }
        AlertDialog.Builder failAllocDialogBuilder = new AlertDialog.Builder(mainActivity);
        failAllocDialogBuilder.setIcon(R.mipmap.ic_launcher);
        failAllocDialogBuilder.setTitle(title);
        failAllocDialogBuilder.setMessage(msg);


        failAllocDialogBuilder.setPositiveButton("重发", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.REAUTHPASS);
            }
        });
        failAllocDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CANCLEAUTHPASS);
            }
        });
        failAllocDialog = mainActivity.getFailAllocDialog();
        if (failAllocDialog == null) {
            failAllocDialog = failAllocDialogBuilder.create();
            mainActivity.setFailAllocDialog(failAllocDialog);
        }

        failAllocDialog.setCanceledOnTouchOutside(false);
        failAllocDialog.setCancelable(false);
        failAllocDialog.show();
    }
}
