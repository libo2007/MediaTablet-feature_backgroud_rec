package com.jiaying.mediatablet.fragment.collection;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.adapter.MusicAdapter;
import com.jiaying.mediatablet.entity.MusicEntity;
import com.jiaying.mediatablet.entity.MusicPathEntity;
import com.jiaying.mediatablet.entity.MusicScanPathEntity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.SelfFile;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/*
音乐列表
 */
public class MusicListFragment extends Fragment {
    private static final String TAG = "MusicListFragment";
    private GridView collection_video_gridview;
    private List<MusicEntity> collection_music_list;
    private MusicAdapter collection_music_adapter;
    private static final String ARG_PARAM1 = "param1";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);

        MyLog.e(TAG, "MusicListFragment onCreateView");
        collection_video_gridview = (GridView) view.findViewById(R.id.gridview);
        collection_music_list = new ArrayList<>();
        final MainActivity mainActivity = (MainActivity) getActivity();
        final SoftReference<MainActivity> softReference = new SoftReference(mainActivity);
        collection_music_adapter = new MusicAdapter(mainActivity, collection_music_list, softReference, mainActivity.getTabletStateContext());
        collection_video_gridview.setAdapter(collection_music_adapter);
        collection_video_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicPathEntity.musicPath = collection_music_list.get(position).getPlay_url();
                mainActivity.getTabletStateContext().handleMessge(softReference.get().getRecordState(),
                        softReference.get().getObservableZXDCSignalListenerThread(), null, null, RecSignal.STARTMUSIC);

                mainActivity.getTabletStateContext().handleMessge(softReference.get().getRecordState(),
                        softReference.get().getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEO_FULLSCREEN);
            }
        });
        new LoadLocalMusciTask().execute();
        return view;
    }

    public static MusicListFragment newInstance(String param1) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    private class LoadLocalMusciTask extends AsyncTask<Void, Void, List<MusicEntity>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyLog.e(TAG, "LoadLocalMusciTask onPreExecute");

        }

        @Override
        protected List<MusicEntity> doInBackground(Void... params) {

            List<MusicEntity> musicEntityList = SelfFile.getLocalMusicList(MusicScanPathEntity.musicScanPath);

            MyLog.e(TAG, "LoadLocalMusciTask  doInBackground videolist size:");
            return musicEntityList;
        }

        @Override
        protected void onPostExecute(List<MusicEntity> videoList) {
            super.onPostExecute(videoList);
            MyLog.e(TAG, "LoadLocalMusciTask onPostExecute");

            if (videoList != null) {
                collection_music_list.clear();
                collection_music_list.addAll(videoList);
                collection_music_adapter.notifyDataSetChanged();
            }
        }
    }


}