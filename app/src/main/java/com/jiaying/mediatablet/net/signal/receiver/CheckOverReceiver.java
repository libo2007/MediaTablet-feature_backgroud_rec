package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.authentication.WaitingForDonorFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/17.
 */
public class CheckOverReceiver extends Receiver {
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public CheckOverReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = this.mainActivity.getTabletStateContext();
        this.recordState = this.mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = this.mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        this.mainActivity.setBrightnessNormal();

        //设置显示状态
        this.mainActivity.uiComponent(false, true, false, false);

        //界面切换：

        //切换
        WaitingForDonorFragment waitingForDonorFragment = WaitingForDonorFragment.newInstance(this.mainActivity.getString(R.string.general_welcome));
        this.mainActivity.switchFragment(R.id.fragment_container, waitingForDonorFragment);

        BlankFragment blankFragment = new BlankFragment();
        this.mainActivity.switchFragment(R.id.fragment_auth_container, blankFragment);

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_wait_plasm_title);

        //设置logo按钮事件
        ImageView ivBack = this.mainActivity.getIvBack();
        ivBack.setVisibility(View.GONE);

        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setVisibility(View.VISIBLE);
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setVisibility(View.VISIBLE);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
    }
}
