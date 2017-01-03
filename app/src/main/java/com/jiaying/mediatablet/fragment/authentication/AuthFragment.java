package com.jiaying.mediatablet.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;
import com.jiaying.mediatablet.fragment.BaseFragment;

public class AuthFragment extends BaseFragment {
    public static String TAG = "AuthFragment";

    //  献浆员的个人身份证资料
    PersonInfo idcardPersonInfo;
    //  献浆员的档案资料
    PersonInfo documentPersonInfo;

    public AuthFragment() {
        // Required empty public constructor
    }

    public static AuthFragment newInstance() {

        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // 系统会在创建片段时调用此方法。您应该在实现内初始化您想
    // 在片段暂停或停止后恢复时保留的必需片段组件。
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        献浆员的个人身份证资料
        idcardPersonInfo = DonorEntity.getInstance().getIdentityCard();
        //        献浆员的档案资料
        documentPersonInfo = DonorEntity.getInstance().getDocument();
    }

    // 系统会在片段首次绘制其用户界面时调用此方法。
    // 要想为您的片段绘制 UI，您从此方法中返回的 View 必须是片段布局的根视图。
    // 如果片段未提供 UI，您可以返回 null。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_auth_pass, container, false);

//        显示个人信息
        showPersonInfo(viewRoot);

//        显示档案信息
        showDocumentInfo(viewRoot);

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code = startSpeech(getString(R.string.auth), synthesizerListener);
                if (code == -1) {
                    Log.e(TAG, "播报失败");
                }
            }
        }).start();
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

    private void showPersonInfo(View viewRoot) {
        //身份证信息显示
        TextView tv_name = (TextView) viewRoot.findViewById(R.id.tv_name);
        tv_name.setText(idcardPersonInfo.getName());

        TextView tv_sex = (TextView) viewRoot.findViewById(R.id.tv_sex);
        tv_sex.setText(idcardPersonInfo.getGender());

        TextView tv_nation = (TextView) viewRoot.findViewById(R.id.tv_nation);
        tv_nation.setText(idcardPersonInfo.getNation());

        TextView tv_birthday = (TextView) viewRoot.findViewById(R.id.tv_birthday);
        tv_birthday.setText(idcardPersonInfo.getBirth_year() + "年" +
                idcardPersonInfo.getBirth_month() + "月" + idcardPersonInfo.getBirth_day() + "日");

        TextView tv_address = (TextView) viewRoot.findViewById(R.id.tv_address);
        tv_address.setText(idcardPersonInfo.getAddress());

        TextView tv_idcard = (TextView) viewRoot.findViewById(R.id.tv_idcard);
        tv_idcard.setText(idcardPersonInfo.getId());
        tv_idcard.setText("******************");

        ImageView imageView = (ImageView) viewRoot.findViewById(R.id.iv_head);
        imageView.setImageBitmap(idcardPersonInfo.getFaceBitmap());
    }

    private void showDocumentInfo(View viewRoot) {
        //档案信息

        ImageView iv_document_pic = (ImageView) viewRoot.findViewById(R.id.iv_document_pic);
        iv_document_pic.setImageBitmap(documentPersonInfo.getFaceBitmap());

        TextView tv_document_address = (TextView) viewRoot.findViewById(R.id.tv_document_address);
        tv_document_address.setText(documentPersonInfo.getAddress());

        TextView tv_document_id = (TextView) viewRoot.findViewById(R.id.tv_document_id);
        tv_document_id.setText(documentPersonInfo.getId());

        TextView tv_document_gender = (TextView) viewRoot.findViewById(R.id.tv_document_gender);
        tv_document_gender.setText(documentPersonInfo.getGender());
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener synthesizerListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.e(TAG, "播放完成，无错误");
            } else if (error != null) {
                Log.e(TAG, "播放完成，有错误");
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
}
