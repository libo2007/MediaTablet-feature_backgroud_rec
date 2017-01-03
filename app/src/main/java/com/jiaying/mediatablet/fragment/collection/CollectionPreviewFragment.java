package com.jiaying.mediatablet.fragment.collection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cylinder.www.facedetect.FdRecordActivity;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.utils.ToastUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionPreviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionPreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionPreviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FdRecordActivity fdActivity;
    private OnFragmentInteractionListener mListener;
    private SendStopRecSig sendStopRecSig;

    public CollectionPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionPreviewFragment newInstance(String param1, String param2) {
        CollectionPreviewFragment fragment = new CollectionPreviewFragment();
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
        sendStopRecSig = new SendStopRecSig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hint, container, false);

        fdActivity = new FdRecordActivity(this, 0);

        fdActivity.onCreate(view);

        // Inflate the layout for this fragment
        return view;
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
            sendStopRecSig.removeCallbacks();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (fdActivity != null) {
            fdActivity.onResume();

            //4分钟后发送停止录制信号；
            sendStopRecSig.postDelayed(1000 * 60 * 4);
        }
    }

    private class SendStopRecSig {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.STOPREC);
                ToastUtils.showToast(mainActivity, "1分钟后，发送结束信号");
            }
        };

        public void postDelayed(long delay) {
            boolean b = handler.postDelayed(runnable, delay);
        }

        public void removeCallbacks() {
            handler.removeCallbacks(runnable);
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
