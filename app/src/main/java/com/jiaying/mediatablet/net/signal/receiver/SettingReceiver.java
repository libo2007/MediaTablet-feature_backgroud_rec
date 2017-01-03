package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.ServerSettingFragment;
import com.jiaying.mediatablet.utils.LauActFlag;

/**
 * Created by hipil on 2016/9/14.
 */
public class SettingReceiver extends Receiver {
    private MainActivity mainActivity;

    public SettingReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //调整界面组成
        this.mainActivity.uiComponent(false, true, false, false);

        //界面切换
        this.mainActivity.switchFragment(R.id.fragment_container, new ServerSettingFragment());

        //调整界面文字
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_server_setting_title);
        LinearLayout linearLayout = this.mainActivity.getLl_bt_container();
        linearLayout.setVisibility(View.GONE);

        //设置logo的相关事件
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作
        LauActFlag.is = true;
    }
}
