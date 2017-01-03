package com.jiaying.mediatablet.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cylinder.www.facedetect.FdAuthActivity;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.constants.Constants;
import com.jiaying.mediatablet.db.DataPreference;
import com.jiaying.mediatablet.entity.AuthPassFace;
import com.jiaying.mediatablet.entity.CurrentDate;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.utils.TimeRecord;

public class AuthPreviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FdAuthActivity fdAuthActivity;

    private AuthenticationThread authenticationThread;

    private OnAuthFragmentInteractionListener mListener;


    public AuthPreviewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static AuthPreviewFragment newInstance(String param1, String param2) {
        AuthPreviewFragment fragment = new AuthPreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        AuthPassFace.authFace = null;
        TimeRecord.getInstance().setStartPicDate(CurrentDate.curDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        authenticationThread = new AuthenticationThread();

        DataPreference dataPreference = new DataPreference(getActivity());
        float face_rate = dataPreference.readFloat("face_rate");
        if (face_rate == -0.1f) {
            face_rate = Constants.FACE_RATE;
        }


        int face_send_num = dataPreference.readInt("face_send_num");
        if (face_send_num == -1) {
            face_send_num = Constants.FACE_SEND_NUM;
        }


            fdAuthActivity = new FdAuthActivity(this, 1, face_rate, face_send_num);
            fdAuthActivity.onCreate(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (fdAuthActivity != null) {
            fdAuthActivity.onResume();
            authenticationThread.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        authenticationThread.interrupt();
        if (fdAuthActivity != null) {
            fdAuthActivity.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (fdAuthActivity != null) {
            fdAuthActivity.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (fdAuthActivity != null) {
            fdAuthActivity.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fdAuthActivity != null) {
            fdAuthActivity.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class AuthenticationThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {

                if (fdAuthActivity.isFaceAuthentication() && fdAuthActivity != null) {

                    Log.e("auth", "人脸识别 通过");
                    MainActivity mainActivity = (MainActivity) getActivity();

                    AuthPassFace.authFace = fdAuthActivity.getSimilarmRgba();

                    mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.AUTHPASS);

                    break;
                } else {
                    Log.e("auth", "人脸识别 未通过");
                }
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    //restore the interrupt status
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public interface OnAuthFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAuthFragmentInteraction(RecSignal recSignal);
    }


}
