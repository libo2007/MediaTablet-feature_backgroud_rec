package com.jiaying.mediatablet.fragment.collection;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.adapter.VideoAdapter;
import com.jiaying.mediatablet.entity.VideoEntity;
import com.jiaying.mediatablet.entity.VideoPathEntity;
import com.jiaying.mediatablet.entity.VideoScanPathEntity;
import com.jiaying.mediatablet.net.signal.RecSignal;

import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.SelfFile;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/*
视频列表
 */
public class VideoListFragment extends Fragment {

    private static final String TAG = "VideoListFragment";

    private GridView collection_video_gridview;

    private List<VideoEntity> collection_video_list;
    private VideoAdapter collection_video_adapter;

    private static final String ARG_PARAM1 = "param1";
    private String scan_video_path = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);

//        if(getArguments()!=null){
//            scan_video_path = (String) getArguments().get(ARG_PARAM1);
//        }
        MyLog.e(TAG, "VideoListFragment onCreateView");
        collection_video_gridview = (GridView) view.findViewById(R.id.gridview);
        collection_video_list = new ArrayList<>();
        final MainActivity mainActivity = (MainActivity) getActivity();
        final SoftReference<MainActivity> softReference = new SoftReference(mainActivity);
        collection_video_adapter = new VideoAdapter(mainActivity, collection_video_list, softReference, mainActivity.getTabletStateContext());
        collection_video_gridview.setAdapter(collection_video_adapter);
        collection_video_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoPathEntity.videoPath = collection_video_list.get(position).getPlay_url();
                mainActivity.getTabletStateContext().handleMessge(softReference.get().getRecordState(),
                        softReference.get().getObservableZXDCSignalListenerThread(), null, null, RecSignal.STARTVIDEO);
            }
        });
        new LoadLocalVideoTask().execute();
        return view;
    }

    public static VideoListFragment newInstance(String param1) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private class LoadLocalVideoTask extends AsyncTask<Void, Void, List<VideoEntity>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyLog.e(TAG, "LoadLocalVideoTask onPreExecute");

        }

        @Override
        protected List<VideoEntity> doInBackground(Void... params) {

            List<VideoEntity> videoList = SelfFile.getLocalVideoList(VideoScanPathEntity.videoScanPath);

            MyLog.e(TAG, "LoadLocalVideoTask  doInBackground videolist size:" );
            return videoList;
        }

        @Override
        protected void onPostExecute(List<VideoEntity> videoList) {
            super.onPostExecute(videoList);
            MyLog.e(TAG, "videolist onPostExecute");

            if (videoList != null) {
                collection_video_list.clear();
                collection_video_list.addAll(videoList);
                collection_video_adapter.notifyDataSetChanged();
            }
        }
    }


}