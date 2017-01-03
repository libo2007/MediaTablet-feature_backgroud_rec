package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.fragment.authentication.AuthFragment;
import com.jiaying.mediatablet.fragment.authentication.AuthPreviewFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;

/**
 * Created by hipil on 2016/9/14.
 */
public class ConfirmReceiver extends Receiver {
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public ConfirmReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {

        mainActivity.setBrightnessNormal();

        //调整界面组成
        mainActivity.uiComponent(false, true, false, true);

        //界面切换
        //调整出身份证信息和档案信息
        AuthFragment authFragment = AuthFragment.newInstance();
        mainActivity.switchFragment(R.id.fragment_container, authFragment);

        //调整出认证预览界面
        AuthPreviewFragment authPreviewFragment = new AuthPreviewFragment();
        mainActivity.switchFragment(R.id.fragment_auth_container, authPreviewFragment);

        //调整界面文字
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.auth);

        //设置logo的相关事件
        ImageView ivBack = mainActivity.getIvBack();
        ivBack.setVisibility(View.GONE);

        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setVisibility(View.VISIBLE);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setOnClickListener(null);
        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {

//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.RECORDDONORVIDEO);
//                // TODO: 2016/5/20 录制献浆员视频和护士视频的模块做好后，调整为发送RECORDDONORVIDEO命令
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHPASS);
                return false;
            }
        });

        //启动相关动作
        HorizontalProgressBar collect_pb = mainActivity.getHorizontalProgressBar();
        collect_pb.setProgress(0);
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());
    }
}
