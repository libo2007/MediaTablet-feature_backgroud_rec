package com.jiaying.mediatablet.fragment.authentication;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.graphics.font.AbstractTypeface;
import com.jiaying.mediatablet.graphics.font.AbstractTypefaceCreator;
import com.jiaying.mediatablet.graphics.font.XKTypefaceCreator;

/*
等待献浆员信息的界面
 */
public class WaitingForDonorFragment extends Fragment {
    private static final String ARG_SLOGAN = "slogan";


    private String slogan;

    private AbstractTypeface XKface;
    private AbstractTypefaceCreator typefaceCreator;

    public WaitingForDonorFragment() {
    }

    public static WaitingForDonorFragment newInstance(String slogan) {
        WaitingForDonorFragment fragment = new WaitingForDonorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SLOGAN, slogan);
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
            slogan = getArguments().getString(ARG_SLOGAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Given the typeface, we should construct a factory pattern for these type face.
        typefaceCreator = new XKTypefaceCreator();
        XKface = typefaceCreator.createTypeface(getActivity());

//        Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_plasm, container, false);
        TextView textViewSlogan = (TextView) view.findViewById(R.id.tv_slogan);

//        设置标语字体
        textViewSlogan.setTypeface(XKface.getTypeface());

//        设置标语
        textViewSlogan.setText(slogan);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
