package com.jiaying.mediatablet.fragment.authentication;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;

import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.fragment.BaseFragment;
import com.jiaying.mediatablet.graphics.font.AbstractTypeface;
import com.jiaying.mediatablet.graphics.font.AbstractTypefaceCreator;
import com.jiaying.mediatablet.graphics.font.XKTypefaceCreator;
import com.jiaying.mediatablet.utils.MyLog;

/*
欢迎献浆员
 */
public class WelcomeFragment extends BaseFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_SLOGAN= "slogan";

    private String name;
    private String slogan;

    private AbstractTypeface XKface;
    private AbstractTypefaceCreator xKtypefaceCreator;

    String title;

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
//                showTip("播放完成");
            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
                MyLog.e("ERROR", "播放完成：" + error.getPlainDescription(true));

            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    public static WelcomeFragment newInstance(String name, String slogan) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_SLOGAN, slogan);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            slogan = getArguments().getString(ARG_SLOGAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_welcome_plasm, container, false);

        // Generate the typeface.
        xKtypefaceCreator = new XKTypefaceCreator();
        XKface = xKtypefaceCreator.createTypeface(getActivity());


        TextView tv_name = (TextView) viewRoot.findViewById(R.id.tv_name);

        if ("男".equals(DonorEntity.getInstance().getIdentityCard().getGender())) {
            title = "先生";
        } else {
            title = "女士";
        }

        tv_name.setText(name + "(" + title + ")" + ":");
        tv_name.setTypeface(XKface.getTypeface());

        // Set the slogan text view.
        TextView SloganTextView = (TextView) viewRoot.findViewById(R.id.slogan_text_view);
        SloganTextView.setTypeface(XKface.getTypeface());
        SloganTextView.setText(slogan);

        // Set the welcome text view.
        TextView welcomeTextView = (TextView) viewRoot.findViewById(R.id.welcome_text_view);
        welcomeTextView.setTypeface(XKface.getTypeface());
        welcomeTextView.setText("\n欢迎您来献浆。\n");

        return viewRoot;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {

                startSpeech(name + title + "欢迎您来献浆。" + slogan, mTtsListener);
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
