package com.jiaying.mediatablet.net.signal.receiver;

import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.authentication.WelcomeFragment;

/**
 * Created by hipil on 2016/9/15.
 */
public class KReceiver extends Receiver{
    private MainActivity mainActivity;
    private ProgressDialog allocDevDialog;

    public KReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //设置显示状态
        mainActivity.uiComponent(false, true, false, true);

        //界面切换：
        String name = DonorEntity.getInstance().getIdentityCard().getName();
        String sloganone = mainActivity.getString(R.string.sloganoneabove);
        WelcomeFragment welcomeFragment = WelcomeFragment.newInstance(name, sloganone);
        mainActivity.switchFragment(R.id.fragment_container, welcomeFragment);
        //隐藏认证预览界面
        BlankFragment blankFragment = new BlankFragment();
        mainActivity.switchFragment(R.id.fragment_auth_container, blankFragment);


        disAllocDevDialog();

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_welcome_plasm_title);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
    }

    //关闭分配中对话框
    private void disAllocDevDialog() {
        if (!mainActivity.isFinishing()) {
            allocDevDialog = mainActivity.getAllocDevDialog();
            if (allocDevDialog != null) {
                allocDevDialog.dismiss();
                allocDevDialog = null;
            }
        }
    }
}
