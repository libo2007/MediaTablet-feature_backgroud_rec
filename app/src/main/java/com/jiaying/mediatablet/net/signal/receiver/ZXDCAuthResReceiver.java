package com.jiaying.mediatablet.net.signal.receiver;

import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;

/**
 * Created by hipil on 2016/9/15.
 */
public class ZXDCAuthResReceiver extends Receiver{
    private MainActivity mainActivity;
    private ProgressDialog allocDevDialog;

    public ZXDCAuthResReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //设置显示状态
        mainActivity.uiComponent(false, true, false, false);

        //界面切换：
        //调整应答
        showProgress("认证通过，等待应答！", "服务器（**）\n单采机（应答）");

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.authres);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作
    }
    //    ***********************对话框***************************************
    //显示认证应答进度框
    private void showProgress(String title, String msg) {
        if (mainActivity.isFinishing()) {
            return;
        }
        allocDevDialog = mainActivity.getAllocDevDialog();
        if (allocDevDialog == null) {
            mainActivity.setAllocDevDialog(allocDevDialog);
            allocDevDialog = new ProgressDialog(mainActivity);
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
}
