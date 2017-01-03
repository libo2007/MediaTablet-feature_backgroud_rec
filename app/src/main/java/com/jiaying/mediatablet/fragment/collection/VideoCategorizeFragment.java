package com.jiaying.mediatablet.fragment.collection;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;

import com.jiaying.mediatablet.constants.Constants;
import com.jiaying.mediatablet.entity.VideoScanPathEntity;
import com.jiaying.mediatablet.net.signal.RecSignal;

import com.jiaying.mediatablet.utils.MyLog;


/*
视频分类(公益，献浆，娱乐)
 */
public class VideoCategorizeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "VideoListFragment";
    private Button btn_gongyi;
    private Button btn_xianjiang;
    private Button btn_yule;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_categorize, null);
        btn_gongyi = (Button) view.findViewById(R.id.btn_gongyi);
        btn_xianjiang = (Button) view.findViewById(R.id.btn_xianjiang);
        btn_yule = (Button) view.findViewById(R.id.btn_yule);
        btn_gongyi.setOnClickListener(this);
        btn_xianjiang.setOnClickListener(this);
        btn_yule.setOnClickListener(this);
        mainActivity = (MainActivity) getActivity();
        return view;
    }


    @Override
    public void onClick(View v) {
        if (mainActivity == null) {
            MyLog.e(TAG, "mainActivity == null");
            return;
        }
        switch (v.getId()) {
            case R.id.btn_gongyi:

                VideoScanPathEntity.videoScanPath= Constants.VIDEO_PATH_GONGYI;
                break;
            case R.id.btn_xianjiang:
                VideoScanPathEntity.videoScanPath= Constants.VIDEO_PATH_XIANJIANG;
                break;
            case R.id.btn_yule:
                VideoScanPathEntity.videoScanPath= Constants.VIDEO_PATH_YULE;
                break;
        }

        mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
    }
}