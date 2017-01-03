package com.jiaying.mediatablet.fragment.collection;



/*
jc视频播放
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.player.lib.JCVideoPlayer;
import com.jiaying.mediatablet.player.lib.JCVideoPlayerStandard;

import com.jiaying.mediatablet.utils.MyLog;



public class JCPlayVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View view;
    private Button btn_submit;

    private LinearLayout ll_evaluation_puncture;
    private ImageView iv_good_puncture;
    private ImageView iv_soso_puncture;
    private ImageView iv_terrible_puncture;
    private LinearLayout ll_not_good_puncture;
    private JCVideoPlayerStandard video_player;

    private boolean isCollectionVideo = false;//是否是采集视频的时候播放

    private boolean isFullScreen;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JCPlayVideoFragment newInstance(String param1, String param2,boolean param3) {

        JCPlayVideoFragment fragment = new JCPlayVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);

        fragment.setArguments(args);
        return fragment;
    }

    public JCPlayVideoFragment() {
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
            isFullScreen = getArguments().getBoolean(ARG_PARAM3);

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


        view = inflater.inflate(R.layout.fragment_jc_play_video, container, false);

        video_player = (JCVideoPlayerStandard) view.findViewById(R.id.video_player);
        //第一个参数为播放地址，第二参数为视频名字

        video_player.setUp(mParam1, "");

        video_player.playVideo(isFullScreen);






        ll_evaluation_puncture = (LinearLayout) view.findViewById(R.id.ll_evaluation_puncture);
        if (isCollectionVideo) {
            ll_evaluation_puncture.setVisibility(View.VISIBLE);
        }
        else{
            ll_evaluation_puncture.setVisibility(View.INVISIBLE);
        }
        ll_not_good_puncture = (LinearLayout) view.findViewById(R.id.ll_not_good_puncture);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ll_evaluation_puncture.setVisibility(View.GONE);
            }
        });
        iv_good_puncture = (ImageView) view.findViewById(R.id.iv_good_puncture);
        iv_soso_puncture = (ImageView) view.findViewById(R.id.iv_soso_puncture);
        iv_terrible_puncture = (ImageView) view.findViewById(R.id.iv_terrible_puncture);

        iv_good_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_good_puncture.setImageResource(R.mipmap.good_press);
                iv_soso_puncture.setImageResource(R.mipmap.soso);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible);

                ll_not_good_puncture.setVisibility(View.INVISIBLE);
                btn_submit.setVisibility(View.VISIBLE);

            }
        });
        iv_soso_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_good_puncture.setImageResource(R.mipmap.good);
                iv_soso_puncture.setImageResource(R.mipmap.soso_press);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible);

                ll_not_good_puncture.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        });
        iv_terrible_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_good_puncture.setImageResource(R.mipmap.good);
                iv_soso_puncture.setImageResource(R.mipmap.soso);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible_press);

                ll_not_good_puncture.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        });


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

        video_player.setJCPlayCompleteCallback(new JCVideoPlayer.JCPlayCompleteCallback() {
            @Override
            public void onComplete() {
                if (!TextUtils.isEmpty(mParam2)) {
                    if (mParam2.equals("PunctureVideo")) {

                    } else {

                        mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                    }
                } else {
                    mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEOLIST);
                }
            }
        });

        video_player.setJCPlayFullScreenCallback(new JCVideoPlayer.JCPlayFullScreenCallback() {
            @Override
            public void isFullScreen(boolean isFullScreen) {
                MyLog.e("PlayVideoFragment", "isFullScreen:" + isFullScreen);
                if (isFullScreen) {
                    mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEO_FULLSCREEN);
                } else {
                    mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.TOVIDEO_NOT_FULLSCREEN);
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
//        surfaceView.getHolder().removeCallback(sfCallback);
        JCVideoPlayer.releaseAllVideos();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("PlayVideoFragment", "onDetach");
    }


}
