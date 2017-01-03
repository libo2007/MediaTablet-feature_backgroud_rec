package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.MusicPathEntity;
import com.jiaying.mediatablet.entity.VideoPathEntity;
import com.jiaying.mediatablet.fragment.collection.JCPlayMusiciFragment;
import com.jiaying.mediatablet.fragment.collection.JCPlayVideoFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/16.
 */
public class PlayMusicReceiver extends Receiver {
    private MainActivity mainActivity;
    TabletStateContext tabletStateContext;
    RecordState recordState;
    ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public PlayMusicReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {

        this.mainActivity.uiComponent(true, true, false, true);
        //界面切换：

        JCPlayMusiciFragment playMusiciFragment = JCPlayMusiciFragment.newInstance(MusicPathEntity.musicPath, "");

        this.mainActivity.switchFragment(R.id.fragment_container, playMusiciFragment);

        //设置文字内容
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.play_music);

        //设置logo按钮事件

        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);

        ivLogoAndBack.setImageResource(R.drawable.iv_back_selector);

        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOMUSICLIST);
            }
        });

    }
}
