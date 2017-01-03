package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.authentication.RecordDonorFragment;

/**
 * Created by hipil on 2016/9/22.
 */
public class RecordDonorVideoReceiver extends Receiver{
    private MainActivity mainActivity;

    public RecordDonorVideoReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //设置显示状态
        this.mainActivity.uiComponent(false, true, false, false);

        // 调整录制浆员信息
        RecordDonorFragment recordDonorFragment = new RecordDonorFragment();
        this.mainActivity.switchFragment(R.id.fragment_container, recordDonorFragment);

        //隐藏认证预览界面
        BlankFragment blankFragment = new BlankFragment();
        this.mainActivity.switchFragment(R.id.fragment_auth_container, blankFragment);

        //设置文字内容
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.record_donor);

        //设置返回按钮事件
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setVisibility(View.GONE);

        ImageView ivBack = this.mainActivity.getIvBack();
        ivBack.setVisibility(View.VISIBLE);
    }
}
