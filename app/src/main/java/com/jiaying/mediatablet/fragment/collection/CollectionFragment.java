package com.jiaying.mediatablet.fragment.collection;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.BaseFragment;
import com.jiaying.mediatablet.net.signal.RecSignal;

/*
采集提示页面
 */
public class CollectionFragment extends BaseFragment {
    public String TAG = "CollectionFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment_collection_view = inflater.inflate(R.layout.fragment_collection, null);
        TextView content_txt = (TextView) fragment_collection_view.findViewById(R.id.content_txt);
        SpannableString ss = new SpannableString(getString(R.string.fragment_collect_content));
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content_txt.setText(ss);
        return fragment_collection_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = startSpeech(getString(R.string.fragment_collect_content), mTtsListener);
                Log.e(TAG, "CODE IS " + code + " " + mTtsListener.toString().hashCode());
//                延时发送播放视频信号~
                try {
                    Thread.currentThread().sleep(13000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MainActivity mainActivity = (MainActivity) CollectionFragment.this.getActivity();
                if (mainActivity != null) {
                    mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.STARTCOLLECTIONVIDEO);
                }

            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.e(TAG, "onSpeakBegin" + this.toString().hashCode());
        }

        @Override
        public void onSpeakPaused() {
            Log.e(TAG, "onSpeakPaused" + this.toString());
        }

        @Override
        public void onSpeakResumed() {
            Log.e(TAG, "onSpeakResumed" + this.toString().hashCode());
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            Log.e(TAG, percent + " " + beginPos + " " + endPos + "onBufferProgress" + this.toString().hashCode());
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            Log.e(TAG, percent + " " + beginPos + " " + endPos + "onSpeakProgress" + this.toString().hashCode());
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {

                Log.e(TAG, "采集播放完毕error is null" + this.toString().hashCode());

            } else if (error != null) {
                Log.e(TAG, "采集播放完毕error is not null" + this.toString().hashCode());
            }


        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            Log.e(TAG, "onEvent type is " + eventType + " " + this.toString().hashCode());
        }
    };


}
