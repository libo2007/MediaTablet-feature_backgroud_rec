package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.VideoListFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/9/16.
 */
public class ToVideoListReceiver extends Receiver{
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;

    public ToVideoListReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.tabletStateContext = mainActivity.getTabletStateContext();
        this.recordState = mainActivity.getRecordState();
        this.observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        this.mainActivity.uiComponent(true, true, false, true);

        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.video_list);

        //设置logo按钮事件
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.drawable.iv_back_selector);
        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

            }
        });

        this.mainActivity.switchFragment(R.id.fragment_container, new VideoListFragment());
    }
}
