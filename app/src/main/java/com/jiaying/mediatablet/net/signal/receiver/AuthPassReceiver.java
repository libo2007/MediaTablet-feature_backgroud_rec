package com.jiaying.mediatablet.net.signal.receiver;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.view.View;
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
public class AuthPassReceiver extends Receiver{

    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;
    private ProgressDialog allocDevDialog;

    public AuthPassReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        //设置显示状态
        mainActivity.uiComponent(false, true, false, false);

        //界面切换：

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.authres);

        //设置logo按钮事件
        ImageView ivBack = mainActivity.getIvBack();
        ivBack.setVisibility(View.GONE);

        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setVisibility(View.VISIBLE);
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作
        showProgress("认证通过，等待应答！", "服务器（**）\n单采机（**）");

        CountDownTimer countDownTimer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                disAllocDevDialog();
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHRESTIMEOUT);
            }
        };
        countDownTimer.start();
    }

    //显示认证应答进度框
    private void showProgress(String title, String msg) {
        if (mainActivity.isFinishing()) {
            return;
        }
        allocDevDialog = mainActivity.getAllocDevDialog();
        if (allocDevDialog == null) {
            allocDevDialog = new ProgressDialog(mainActivity);
            mainActivity.setAllocDevDialog(allocDevDialog);
        }
        allocDevDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        allocDevDialog.setTitle(title);
        allocDevDialog.setMessage(msg);
        allocDevDialog.setIcon(R.mipmap.ic_launcher);


//        设置点击进度对话框外的区域对话框不消失
        allocDevDialog.setCanceledOnTouchOutside(false);
        allocDevDialog.setIndeterminate(false);
        allocDevDialog.setCancelable(false);
        allocDevDialog.show();
    }

    //关闭分配中对话框
    private void disAllocDevDialog() {
        if (!mainActivity.isFinishing()) {
            if (allocDevDialog != null) {
                allocDevDialog.dismiss();
                allocDevDialog = null;
            }
        }
    }
}
