package com.jiaying.mediatablet.fragment.collection;



/*
视频播放
 */

import android.app.Activity;
import android.app.Fragment;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;


import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;


import java.io.IOException;


public class PlayVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private View view;
    private SurfaceHolder.Callback sfCallback;
    private Button btn_submit;
    private LinearLayout ll_evaluation;



    private boolean isCollectionVideo = false;//是否是采集视频的时候播放
    private ProgressDialog mEvalutionDialog = null;//评价对话框

    private static final int WHAT_DLG_TIMEOUT = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_DLG_TIMEOUT) {
                if (getActivity() != null && mEvalutionDialog != null) {
                    mEvalutionDialog.dismiss();
                }
            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayVideoFragment newInstance(String param1, String param2) {
        PlayVideoFragment fragment = new PlayVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof PlayVideoFragmentInteractionListener) {
//            mListener = (PlayVideoFragmentInteractionListener) activity;
//        } else {
//            throw new RuntimeException(activity.toString()
//                    + " must implement PlayVideoFragmentInteractionListener");
//        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PlayVideoFragment", "onCreate ");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            if (!TextUtils.isEmpty(mParam2)) {
                if (mParam2.equals("StartCollcetionVideo")) {
                    isCollectionVideo = true;
                }
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("PlayVideoFragment", "onCreateView 1");
        mediaPlayer = new MediaPlayer();


        view = inflater.inflate(R.layout.fragment_play_video, container, false);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                ll_evaluation.setVisibility(View.GONE);
            }
        });

        ll_evaluation = (LinearLayout) view.findViewById(R.id.ll_evaluation);


        surfaceView = (SurfaceView) view.findViewById(R.id.video_player);


        if (isCollectionVideo) {

            ll_evaluation.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("PlayVideoFragment", "onActivityCreated 1");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("PlayVideoFragment", "onStart ");

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("PlayVideoFragment", "onResume ");
        final MainActivity mainActivity = (MainActivity) getActivity();
        sfCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // mediaPlayer state:idle
                Log.e("PlayVideoFragment", "surfaceCreated 1");
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Log.e("PlayVideoFragment", "surfaceCreated 2");
                mediaPlayer.setDisplay(holder);
                Log.e("PlayVideoFragment", "surfaceCreated 3");
                try {
                    mediaPlayer.setDataSource(mParam1);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (!TextUtils.isEmpty(mParam2)) {
                            if (mParam2.equals("PunctureVideo")) {

                            } else {

                                mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                                mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                            }
                        } else {
                            mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                            mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                        }
                    }
                });

                Log.e("PlayVideoFragment", "surfaceCreated 4");
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // mediaPlayer state:prepared

                        adjustTheScreenSize(mediaPlayer, surfaceView);
                        mp.start();

                        // mediaPlayer state:started
                    }
                });

                // mediaPlayer state:initialized
                mediaPlayer.prepareAsync();
                Log.e("PlayVideoFragment", "surfaceCreated 5");

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e("PlayVideoFragment", "surfaceChanged");


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e("PlayVideoFragment", "surfaceDestroyed");
            }
        };

        surfaceView.getHolder().addCallback(sfCallback);


    }

    @Override
    public void onPause() {
        super.onPause();
//        surfaceView.getHolder().removeCallback(sfCallback);
        mediaPlayer.pause();

        Log.e("PlayVideoFragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("PlayVideoFragment", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("PlayVideoFragment", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("PlayVideoFragment", "onDestroy");
        mediaPlayer.release();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("PlayVideoFragment", "onDetach");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private boolean adjustTheScreenSize(MediaPlayer mp, SurfaceView surfaceView) {

        // The video size.
        int vH = mp.getVideoHeight();
        int vW = mp.getVideoWidth();

        // The layout size.
        int lH = view.findViewById(R.id.video_frame_layout).getHeight();
        int lW = view.findViewById(R.id.video_frame_layout).getWidth();

        // The ratio.
        double vRatio = ((double) (vH) / vW);
        double fRatio = ((double) (lH) / lW);

        //check the ratio.
        if (vRatio > fRatio) {

            ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
            layoutParams.height = lH;
            layoutParams.width = (int) ((1.0 * vW / vH) * lH - 0.5);

            surfaceView.setLayoutParams(layoutParams);
            surfaceView.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
            layoutParams.width = lW;
            layoutParams.height = (int) ((1.0 * vH / vW) * lW - 0.5);
            surfaceView.setLayoutParams(layoutParams);
            surfaceView.setVisibility(View.VISIBLE);
        }
        return true;
    }

}
