package com.jiaying.mediatablet.fragment.authentication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cylinder.www.facedetect.FdNotAuthRecordActivity;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordNurseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordNurseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordNurseFragment extends Fragment {

    private static final String TAG = "RecordNurseFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FdNotAuthRecordActivity fdActivity;
    private OnFragmentInteractionListener mListener;

    //最多录制10s
    private static final int MAX_TIME = 3;
    private Button btn_record;
    private ProgressBar mProgressBar;
    private boolean isRecording = false;
    private boolean isRecordOver = false;
    private int currentProgress = 0;
    private static final int MSG_UPDATE_PROGRESS = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_PROGRESS) {
                MyLog.e(TAG, "currentProgress:" + currentProgress);
                mProgressBar.setProgress(currentProgress);
                if (currentProgress > MAX_TIME) {
                    isRecordOver = true;
                    stopRecord(true);
                }
            }
        }
    };

    public RecordNurseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordNurseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordNurseFragment newInstance(String param1, String param2) {
        RecordNurseFragment fragment = new RecordNurseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record_nurse_video, container, false);

        fdActivity = new FdNotAuthRecordActivity(this, 1);
        fdActivity.onCreate(view);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb);
        mProgressBar.setMax(MAX_TIME);
        btn_record = (Button) view.findViewById(R.id.btn_record);
        btn_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //开始录制视频
                        btn_record.setBackgroundResource(R.drawable.video_record_press_selector);
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_record.setBackgroundResource(R.drawable.video_record_normal_selector);
                        if (!isRecordOver) {
                            stopRecord(false);
                        }
                        break;
                }
                return true;
            }
        });
        return view;
    }

    private void stopRecord(boolean isSaveAndSendVideoFile ) {
        MyLog.e(TAG, "停止录制护士视频");
        isRecording = false;
        currentProgress = 0;
        mProgressBar.setProgress(0);
        if (isSaveAndSendVideoFile) {
            ToastUtils.showToast(getActivity(), R.string.record_norse_over);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(),  mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.AUTHPASS);
        }else {
            ToastUtils.showToast(getActivity(), R.string.record_time_not_enough);
        }
        fdActivity.stopRecord(isSaveAndSendVideoFile);
    }

    private void startRecord() {
        isRecording = true;
        isRecordOver = false;
        currentProgress = 0;
        mProgressBar.setProgress(0);
        new ProgressThread().start();
        fdActivity.startRecord();
        MyLog.e(TAG, "开始录制护士视频");

    }

    private class ProgressThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isRecording) {
                    break;
                }
                currentProgress++;
                mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


//        if (context instanceof OnEvaluationFragmentListener) {
//            mListener = (OnEvaluationFragmentListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement PlayVideoFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (fdActivity != null) {
            fdActivity.onPause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (fdActivity != null) {
            fdActivity.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (fdActivity != null) {
            fdActivity.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (fdActivity != null) {
            fdActivity.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fdActivity != null) {
            fdActivity.onDestroy();
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
