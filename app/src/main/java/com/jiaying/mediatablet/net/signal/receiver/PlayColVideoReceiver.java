package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.fragment.collection.JCPlayVideoFragment;
import com.jiaying.mediatablet.fragment.collection.VideoListFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;

/**
 * Created by hipil on 2016/9/16.
 */
public class PlayColVideoReceiver extends Receiver{
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public PlayColVideoReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        //设置显示状态
        this.mainActivity.uiComponent(true, true, false, true);

        //界面切换：
        //播放默认的采集视频
        this.mainActivity.switchFragment(R.id.fragment_container, new VideoListFragment());


        JCPlayVideoFragment playVideoFragment = JCPlayVideoFragment.newInstance("/sdcard/kindness.mp4", "StartCollcetionVideo",false);


//        PlayVideoFragment playVideoFragment = PlayVideoFragment.newInstance(path, "StartCollcetionVideo");

        this.mainActivity.switchFragment(R.id.fragment_container, playVideoFragment);

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.play_video);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);

        ivLogoAndBack.setImageResource(R.drawable.iv_back_selector);

        //给返回按钮设置回掉函数
        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

            }
        });

        //启动相关动作
        HorizontalProgressBar collect_pb = mainActivity.getHorizontalProgressBar();
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());
    }
}
