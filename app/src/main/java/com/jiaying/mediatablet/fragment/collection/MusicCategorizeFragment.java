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
import com.jiaying.mediatablet.entity.MusicScanPathEntity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.utils.MyLog;


/*
音乐分类(现代，流行，经典)
 */
public class MusicCategorizeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "VideoListFragment";
    private Button btn_modern;
    private Button btn_popular;
    private Button btn_classic;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_categorize, null);
        btn_modern = (Button) view.findViewById(R.id.btn_modern);
        btn_popular = (Button) view.findViewById(R.id.btn_popular);
        btn_classic = (Button) view.findViewById(R.id.btn_classic);
        btn_modern.setOnClickListener(this);
        btn_popular.setOnClickListener(this);
        btn_classic.setOnClickListener(this);
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
            case R.id.btn_modern:

                MusicScanPathEntity.musicScanPath = Constants.MUSIC_PATH_MODERN;
                break;
            case R.id.btn_popular:
                MusicScanPathEntity.musicScanPath= Constants.MUSIC_PATH_POPULAR;
                break;
            case R.id.btn_classic:
                MusicScanPathEntity.musicScanPath= Constants.MUSIC_PATH_CLASSIC;
                break;
        }

        mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOMUSICLIST);
    }
}