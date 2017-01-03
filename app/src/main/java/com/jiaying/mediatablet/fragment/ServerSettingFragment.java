package com.jiaying.mediatablet.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.db.DataPreference;

import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.net.serveraddress.LogServer;
import com.jiaying.mediatablet.net.serveraddress.SignalServer;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.utils.ToastUtils;

/*
服务器配置
 */
public class ServerSettingFragment extends Fragment {

    private static String TAG = "ServerSettingFragment";

    //客户端登录相关参数
    EditText et_dev_ap;

    EditText et_dev_org;

    EditText et_dev_password;

    EditText et_dev_serverap;

    EditText et_dev_serverorg;

    //服务器相关配置
    EditText log_server_ip, log_server_port;

    EditText signal_server_ip, signal_server_port;

    EditText video_server_ip, video_server_port;

    EditText et_bluetooth_name;

    EditText et_face_pass_rate;
    EditText et_face_send_num;

    Button btn_save;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server_setting, null);

        //显示当前日志服务器ip地址和端口
        log_server_ip = (EditText) view.findViewById(R.id.log_server_ip);
        log_server_ip.setText(LogServer.getInstance().getIp());
        log_server_port = (EditText) view.findViewById(R.id.log_server_port);
        log_server_port.setText(String.valueOf(LogServer.getInstance().getPort()));

        //显示当前信号服务器ip地址和端口
        signal_server_ip = (EditText) view.findViewById(R.id.signal_server_ip);
        signal_server_ip.setText(SignalServer.getInstance().getIp());
        signal_server_port = (EditText) view.findViewById(R.id.signal_server_port);
        signal_server_port.setText(String.valueOf(SignalServer.getInstance().getPort()));

        //显示当前视频服务器ip地址和端口
        video_server_ip = (EditText) view.findViewById(R.id.video_server_ip);
        video_server_ip.setText(VideoServer.getInstance().getIp());
        video_server_port = (EditText) view.findViewById(R.id.video_server_port);
        video_server_port.setText(String.valueOf(VideoServer.getInstance().getPort()));

        //设备相关信息
        et_dev_ap = (EditText) view.findViewById(R.id.et_dev_ap);
        et_dev_ap.setText(DeviceEntity.getInstance().getAp());

        et_dev_org = (EditText) view.findViewById(R.id.et_dev_org);
        et_dev_org.setText(DeviceEntity.getInstance().getOrg());

        et_dev_password = (EditText) view.findViewById(R.id.et_dev_password);
        et_dev_password.setText(DeviceEntity.getInstance().getPassword());

        et_dev_serverap = (EditText) view.findViewById(R.id.et_dev_serverap);
        et_dev_serverap.setText(DeviceEntity.getInstance().getServerAp());

        et_dev_serverorg = (EditText) view.findViewById(R.id.et_dev_serverorg);
        et_dev_serverorg.setText(DeviceEntity.getInstance().getServerOrg());

        //要连接的蓝牙名称
        et_bluetooth_name = (EditText) view.findViewById(R.id.et_bluetooth_name);
        DataPreference dataPreference = new DataPreference(getActivity());
        String bluetoothName = dataPreference.readStr("bluetooth_name");
        et_bluetooth_name.setText(bluetoothName);

        //人脸识别率
        et_face_pass_rate = (EditText) view.findViewById(R.id.et_face_pass_rate);
        float face_rate = dataPreference.readFloat("face_rate");
        et_face_pass_rate.setText(face_rate + "");


        //人脸照片上传数字
        et_face_send_num = (EditText) view.findViewById(R.id.et_face_send_num);
        int face_send_num = dataPreference.readInt("face_send_num");
        et_face_send_num.setText(face_send_num + "");


        btn_save = (Button) view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //保存相关参数到本地

                    //蓝牙名称
                    DataPreference dataPreference = new DataPreference(getActivity());
                    dataPreference.writeStr("bluetooth_name", et_bluetooth_name.getText().toString());
                    dataPreference.commit();


                    //人脸通过率

                    dataPreference.writeFloat("face_rate", Float.parseFloat(et_face_pass_rate.getText().toString()));
                    dataPreference.commit();

                    //人脸发送张数
                    dataPreference.writeInt("face_send_num", Integer.parseInt(et_face_send_num.getText().toString()));
                    dataPreference.commit();

                    //服务器IP和端口
                    LogServer.getInstance().setIp(log_server_ip.getText().toString().trim());
                    LogServer.getInstance().setPort(Integer.parseInt(log_server_port.getText().toString().trim()));

                    SignalServer.getInstance().setIp(signal_server_ip.getText().toString().trim());
                    SignalServer.getInstance().setPort(Integer.parseInt(signal_server_port.getText().toString().trim()));

                    VideoServer.getInstance().setIp(video_server_ip.getText().toString().trim());
                    VideoServer.getInstance().setPort(Integer.parseInt(video_server_port.getText().toString().trim()));


                    //ap
                    DeviceEntity.getInstance().setAp(String.valueOf(et_dev_ap.getText()).trim());

                    DeviceEntity.getInstance().setOrg(String.valueOf(et_dev_org.getText()).trim());

                    DeviceEntity.getInstance().setPassword(String.valueOf(et_dev_password.getText()).trim());

                    DeviceEntity.getInstance().setServerAp(String.valueOf(et_dev_serverap.getText()).trim());

                    DeviceEntity.getInstance().setServerOrg(String.valueOf(et_dev_serverorg.getText()).trim());
                } catch (Exception e) {

                    ToastUtils.showToast(getActivity(), "参数输入错误");
                }

                MainActivity mainActivity = (MainActivity) getActivity();
                Log.e("ERROR", "服务器配置界面点击重启");
                mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.STARTLAUN);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
