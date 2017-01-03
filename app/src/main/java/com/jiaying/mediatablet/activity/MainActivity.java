package com.jiaying.mediatablet.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.constants.IntentAction;
import com.jiaying.mediatablet.constants.IntentExtra;
import com.jiaying.mediatablet.constants.Status;
import com.jiaying.mediatablet.db.DataPreference;
import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;
import com.jiaying.mediatablet.fragment.check.CheckFragment;
import com.jiaying.mediatablet.net.btstate.BTConFailureState;
import com.jiaying.mediatablet.net.btstate.BTConSuccessState;
import com.jiaying.mediatablet.net.btstate.BTclosedState;
import com.jiaying.mediatablet.net.btstate.BluetoothContextState;
import com.jiaying.mediatablet.net.btstate.ConnectBTState;
import com.jiaying.mediatablet.net.btstate.InitialBTState;
import com.jiaying.mediatablet.net.btstate.ScanBTState;
import com.jiaying.mediatablet.net.handler.ObserverZXDCSignalUIHandler;
import com.jiaying.mediatablet.net.serveraddress.LogServer;
import com.jiaying.mediatablet.net.serveraddress.SignalServer;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.StateIndex;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForTimestampState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.service.BackgroundMonitorService;
import com.jiaying.mediatablet.service.ScanBackupVideoService;
import com.jiaying.mediatablet.service.TimeService;
import com.jiaying.mediatablet.thread.AniThread;
import com.jiaying.mediatablet.thread.CheckSerReachable;
import com.jiaying.mediatablet.thread.CheckTimeout;
import com.jiaying.mediatablet.utils.AppInfoUtils;
import com.jiaying.mediatablet.utils.BrightnessTools;
import com.jiaying.mediatablet.utils.DateTime;
import com.jiaying.mediatablet.utils.LauActFlag;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;
import com.jiaying.mediatablet.widget.VerticalProgressBar;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, CheckTimeout.OnTimeout, CheckSerReachable.OnUnreachableCallback {


    public static String TAG = "MainActivity";

    private RecordState recordState;
    private FragmentManager fragmentManager;

    public AniThread getStartFist() {
        return startFist;
    }

    public void setStartFist(AniThread startFist) {
        this.startFist = startFist;
    }

    private AniThread startFist;
    private View title_bar_view;//标题栏
    private RadioGroup mGroup;
    private ImageView overflow_image;//弹出功能
    private PopupWindow mPopupWindow;
    private View mPopView;
    private View mParentView;

    public ImageView getIvStartFistHint() {
        return ivStartFistHint;
    }

    private ImageView ivStartFistHint;
    private ImageView ivLogoAndBack;
    private ImageView ivBack;//解决长按logo和点击logo冲突而增加的返回按钮
    private TextView fun_txt;//功能设置
    private TextView server_txt;//参数设置
    private TextView restart_txt;//软件重启
    private TextView net_state_txt;//网络链接状态
    private TextView wifi_not_txt;
    private TextView title_txt;//标题
    private VerticalProgressBar battery_pb;//剩余电量
    private View left_hint_view;//采浆过程状态显示
    private ImageView iv_call;//呼叫护士
    private TextView battery_not_connect_txt;//电源未连接提示
    private ProgressDialog mDialog = null;
    private TextView time_txt;//当前时间
    private HorizontalProgressBar collect_pb;//采集进度
    private View dlg_call_service_view;//电话服务view
    private ProgressDialog allocDevDialog = null;

    public CollectionPreviewFragment getCollectionPreviewFragment() {
        return collectionPreviewFragment;
    }

    public void setCollectionPreviewFragment(CollectionPreviewFragment collectionPreviewFragment) {
        this.collectionPreviewFragment = collectionPreviewFragment;
    }

    private CollectionPreviewFragment collectionPreviewFragment;
    private LinearLayout ll_cl;

    //告警电量值

    private static final int WARNING_BATTERY_VALUE = 5;

    //电量检查是否通过
    private boolean batteryIsOk = false;

    private TabletStateContext tabletStateContext;

    public void setAllocDevDialog(ProgressDialog allocDevDialog) {
        this.allocDevDialog = allocDevDialog;
    }

    public void setFailAllocDialog(AlertDialog failAllocDialog) {
        this.failAllocDialog = failAllocDialog;
    }

    public AlertDialog getFailAllocDialog() {
        return failAllocDialog;
    }

    private AlertDialog failAllocDialog = null;

    private ObserverZXDCSignalUIHandler observerZXDCSignalUIHandler;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread = null;

    private CheckSerReachable checkSerReachable = null;

    private LinearLayout ll_appoint;
    private TextView tv_date;
    private TextView tv_week;

    /**
     * --服务评价开始--
     **/
    private ImageView iv_eval;

    private FrameLayout fl_service_evalution;
    private ImageView iv_evalution_close;
    private int eval_puncture = Status.STATUS_EVL_DEFAULT;//标志穿刺评价
    private int eval_attitude = Status.STATUS_EVL_DEFAULT;//标志态度评价

    //服务评价之1.穿刺评价
    private Button btn_submit;
    private ImageView iv_good_puncture;
    private ImageView iv_soso_puncture;
    private ImageView iv_terrible_puncture;
    private LinearLayout ll_not_good_puncture;

    //二珍穿刺
    private CheckBox cb_erzhengchuanci;
    //疼痛
    private CheckBox cb_tengtong;
    //动作缓慢
    private CheckBox cb_huanman;

    //服务评价之2.态度评价
    private ImageView iv_good;
    private ImageView iv_soso;
    private ImageView iv_terrible;
    private LinearLayout ll_not_good;


    //不礼貌
    private CheckBox cb_bulimao;
    //辱骂
    private CheckBox cb_ruma;
    /**
     * --服务评价结束--
     **/

    private ImageView iv_rise;
    private ImageView iv_down;

    private TextView tv_service_zhijin;
    private TextView tv_service_tangguo;
    private TextView tv_service_reshui;
    private TextView tv_service_zazhi;
    private TextView tv_service_zixun;
    private TextView tv_service_cancel;


    //====蓝牙操作begin====

    private BluetoothContextState bluetoothContextState;

    private static final String BT_LOG = "bt_log";

    private FrameLayout fl_main_content;

    public LinearLayout getLl_bt_container() {
        return ll_bt_container;
    }

    //蓝牙操作的父控件
    private LinearLayout ll_bt_container;
    //蓝牙状态和结果的显示
    private TextView tv_bt_status;
    //蓝牙结果按钮的父控件
    private LinearLayout ll_bt_result_control;
    //蓝牙跳过
    private Button btn_bt_jump;
    //蓝牙重连
    private Button btn_bt_reconnect;
    //蓝牙设置
    private Button btn_bt_set;

    //需要连接的蓝牙名字
    private String bt_name = "";
    private BluetoothAdapter bt_adapter = null;
    private BluetoothDevice currentDevice = null;
    private BluetoothHeadset bt_headset = null;
    private BluetoothA2dp bt_a2dp = null;

    private boolean successHeadset = false;

    private boolean successA2DP = false;


    //蓝牙最多重连次数
    private static final int BT_CONN_COUNT_MAX = 10;
    //当前连接的次数
    private int bt_conn_count = 0;


    private boolean isStartCheckBTFlag = true;

    //====蓝牙操作end=====
    public TabletStateContext getTabletStateContext() {
        return this.tabletStateContext;
    }

    public ObservableZXDCSignalListenerThread getObservableZXDCSignalListenerThread() {
        return observableZXDCSignalListenerThread;
    }

    public RecordState getRecordState() {
        return recordState;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //判断电量
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                //获取最大电量，如未获取到具体数值，则默认为100
                int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);

                //显示电量
                battery_pb.setMax(batteryScale);
                battery_pb.setProgress(batteryLevel);

                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);


                //如果没有充电的状态下，判断电量是否充足
                String state = recordState.getState();
                boolean isCheckBattery = false;
                if (TextUtils.isEmpty(state)) {
                    isCheckBattery = true;
                } else if (state.equals(StateIndex.WAITINGFORDONOR) || state.equals(StateIndex.WAITINGFORCHECKOVER)) {
                    //如果没有处于充电状态，那么久需要在等待献浆员状态和检查设备状态下需要查看电源量。
                    isCheckBattery = true;
                }

                MyLog.e(TAG, tabletStateContext.getCurrentState().toString());
                MyLog.e(TAG, "recordState " + state + ",isCheckBattery " + isCheckBattery);
                if (isCheckBattery && batteryLevel <= WARNING_BATTERY_VALUE) {
                    MyLog.e(TAG, "正在检查状态");
                    battery_not_connect_txt.setVisibility(View.VISIBLE);
                    battery_not_connect_txt.setText(getString(R.string.battery_low));
                    batteryIsOk = false;
                    tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.LOWPOWER);
                } else if (isCheckBattery && batteryLevel > WARNING_BATTERY_VALUE) {
                    battery_not_connect_txt.setVisibility(View.GONE);
                    batteryIsOk = true;
                } else if (isCheckBattery && batteryLevel <= WARNING_BATTERY_VALUE) {
                    batteryIsOk = false;
                } else if (isCheckBattery && batteryLevel > WARNING_BATTERY_VALUE) {
                    batteryIsOk = true;
                }

            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//                    net_state_txt.setVisibility(View.VISIBLE);
//                    wifi_not_txt.setVisibility(View.VISIBLE);
//                } else {
                net_state_txt.setVisibility(View.GONE);
                wifi_not_txt.setVisibility(View.GONE);
//                }
            } else if (IntentAction.ACTION_UPDATE_TIME.equals(action)) {


                long sysTime = intent.getLongExtra(IntentExtra.EXTRA_TIME, System.currentTimeMillis());

                //更新顶部栏的时间显示
                time_txt.setText(DateTime.timeStamp2Time(sysTime + ""));
                //更新预约服务上面的日期和星期
                tv_date.setText(DateTime.timeStamp2Date(sysTime + ""));

                tv_week.setText(DateTime.getWeekDay(sysTime + ""));
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {


            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {

                if (bluetoothContextState.getCurrentState() instanceof BTclosedState && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    MyLog.e(BT_LOG, "蓝牙状态值发生改变：" + BluetoothAdapter.getDefaultAdapter().isEnabled());

                    MyLog.e(BT_LOG, "蓝牙打开====设置状态为扫描状态");
                    bluetoothContextState.setCurrentState(new ScanBTState());
                    new startBTDiscoveryThread().start();
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //发现蓝牙设备
                MyLog.e(BT_LOG, "有action_found");
                if (bluetoothContextState.getCurrentState() instanceof ScanBTState) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e(BT_LOG, "搜索到的蓝牙设备：" + device.getName());
                    // 如果查找到的设备符合要连接的设备，处理
                    if (TextUtils.isEmpty(device.getName())) {
                        Log.e(BT_LOG, "搜索到的蓝牙设名字为null");
                        return;
                    }
                    if (device.getName().equalsIgnoreCase(bt_name)) {
                        Log.e(BT_LOG, "要配对的蓝牙名：" + device.getName());

                        currentDevice = device;
                        // 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
                        if (bt_adapter != null) {
                            bt_adapter.cancelDiscovery();
                        }
                        // 获取蓝牙设备的连接状态
                        int connectState = device.getBondState();
                        switch (connectState) {
                            // 未配对

                            case BluetoothDevice.BOND_NONE:

                                bluetoothContextState.setCurrentState(new ConnectBTState());
                                // 配对
                                try {

                                    MyLog.e(BT_LOG, "未配对开始配对：开始" + bt_name);
                                    Method createBondMethod = BluetoothDevice.class
                                            .getMethod("createBond");
                                    createBondMethod.invoke(device);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    MyLog.e(BT_LOG, "未配对开始配对：出错：" + e.toString());
                                }
                                break;
                            // 已配对
                            case BluetoothDevice.BOND_BONDED:
                                MyLog.e(BT_LOG, "已配对开始连接：开始" + bt_name);
                                btProfileConnect();
                                break;
                        }

                    }

                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                // 状态改变的广播
                MyLog.e(BT_LOG, "有ACTION_BOND_STATE_CHANGED");
                MyLog.e(BT_LOG, "状态" + (bluetoothContextState.getCurrentState() instanceof ConnectBTState));

                if (bluetoothContextState.getCurrentState() instanceof ConnectBTState) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getName().equalsIgnoreCase(bt_name)) {
                        currentDevice = device;
                        int connectState = device.getBondState();
                        MyLog.e(BT_LOG, "connectState  " + connectState);
                        switch (connectState) {
                            case BluetoothDevice.BOND_NONE:
                                break;
                            case BluetoothDevice.BOND_BONDING:
                                break;
                            case BluetoothDevice.BOND_BONDED:
                                btProfileConnect();
                                break;
                        }
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(IntentAction.ACTION_UPDATE_TIME);

        //bluetooth begin
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        //bluetooth end
        registerReceiver(receiver, filter);

        //启动北京时间service
// TODO: 2016/11/24 这里的service在MainActivity重启的时候会不会多次启动 
        startTimeService();

        //启动扫描backup文件夹视频文件
        startScanBackupVideoService();

        //启动后台监听服务
        startMonitorService();
        //禁止锁屏
        forbidLockScreen();
    }

    @Override
    protected void initVariables() {
        Log.e("ERROR", "开始执行MainActivity中的onCreate()函数");
        fragmentManager = getFragmentManager();

//        状态模式，所有观察者收到的信号都要，经过状态模式的筛选，才能决定是否能发送执行
        tabletStateContext = new TabletStateContext();
        bluetoothContextState = new BluetoothContextState();

        LauActFlag.is = false;


        //记录现场
        recordState = RecordState.getInstance(this);

        //如果是断电重启后，无论关机前是什么状态，都需设置到等待等待时间信号
        boolean isBoot = getIntent().getBooleanExtra(IntentExtra.EXTRA_BOOT, false);

//        true 来自LaunchActivity
//        false MainAcitvity被推出去后弹回来
        if (isBoot) {

            recordState.recTimeStamp();
            recordState.commit();
        }

        allocDevDialog = new ProgressDialog(this);

//        总Context实例个数 = Service个数 + Activity个数 + 1（Application对应的Context实例）

//        初始化日志服务器信息；
        LogServer.getInstance().setIdataPreference(new DataPreference(getApplicationContext()));

//        信号服务器信息；
        SignalServer.getInstance().setIdataPreference(new DataPreference(getApplicationContext()));

//        视频服务器信息；
        VideoServer.getInstance().setIdataPreference(new DataPreference(getApplicationContext()));

//        初始化设备
        DeviceEntity.getInstance().setDataPreference(new DataPreference(getApplicationContext()));

//        初始化献浆员
        DonorEntity.getInstance().setDataPreference(new DataPreference(getApplicationContext()));

//        周期性ping服务器，查看和服务器是否通畅
        checkSerReachable = new CheckSerReachable(5000, SignalServer.getInstance().getIp());
        checkSerReachable.setOnUnreachableCallback(this);

//        开机后处于等待时间信号状态
        tabletStateContext.setCurrentState(WaitingForTimestampState.getInstance());

        // 观察者模式
        // Observer Pattern: ObservableZXDCSignalListenerThread(Observable),ObserverZXDCSignalUIHandler(Observer),
        observableZXDCSignalListenerThread = new ObservableZXDCSignalListenerThread(recordState, tabletStateContext);
        observerZXDCSignalUIHandler = new ObserverZXDCSignalUIHandler(new SoftReference<>(this));

        // Add the observers into the observable object.
        observableZXDCSignalListenerThread.addObserver(observerZXDCSignalUIHandler);
    }


    @Override
    protected void initView() {
        Log.e("ERROR", "initView");

        setContentView(R.layout.activity_main);

        //左侧提示栏
        initLeftView();

        //顶部标题栏
        initTitleBar();

        //选择按钮
        initTabGroup();

        //主内容区
        initMainUI();

        //蓝牙界面
        initBTUI();
    }


    //蓝牙界面
    private void initBTUI() {

        //外部线性布局
        ll_bt_container = (LinearLayout) findViewById(R.id.ll_bt_container);
        ll_bt_container.setVisibility(View.GONE);

        //显示连接结果区域
        tv_bt_status = (TextView) findViewById(R.id.tv_bt_status);

        //连接失败处理方式线性布局
        ll_bt_result_control = (LinearLayout) findViewById(R.id.ll_bt_result_control);

        //失败处理方式按钮
        btn_bt_jump = (Button) findViewById(R.id.btn_bt_jump);
        btn_bt_reconnect = (Button) findViewById(R.id.btn_bt_reconnect);
        btn_bt_set = (Button) findViewById(R.id.btn_bt_set);
    }

    private void initLeftView() {

        //屏幕左侧的提示栏
        left_hint_view = findViewById(R.id.left_view_container);

        //握拳提示图片
        ivStartFistHint = (ImageView) this.findViewById(R.id.iv_start_fist);

        //预约服务
        ll_appoint = (LinearLayout) findViewById(R.id.ll_appoint);
        ll_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e("ERROR", "预约服务 click");
            }
        });

        //预约服务上面的日期和星期
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_week = (TextView) findViewById(R.id.tv_week);

        //服务评价初始化开始--
        fl_service_evalution = (FrameLayout) findViewById(R.id.fl_service_evalution);

        iv_eval = (ImageView) findViewById(R.id.iv_eval);
        iv_eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fl_service_evalution.setVisibility(View.VISIBLE);
            }
        });
        ll_not_good_puncture = (LinearLayout) findViewById(R.id.ll_not_good_puncture);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fl_service_evalution.setVisibility(View.GONE);
            }
        });
        iv_evalution_close = (ImageView) findViewById(R.id.iv_evalution_close);
        iv_evalution_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_service_evalution.setVisibility(View.GONE);
            }
        });

        iv_good_puncture = (ImageView) findViewById(R.id.iv_good_puncture);
        iv_soso_puncture = (ImageView) findViewById(R.id.iv_soso_puncture);
        iv_terrible_puncture = (ImageView) findViewById(R.id.iv_terrible_puncture);

        iv_good_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_puncture = Status.STATUS_EVL_GOOD;
                switchEvalutionButtonVisibility();
                iv_good_puncture.setImageResource(R.mipmap.good_press);
                iv_soso_puncture.setImageResource(R.mipmap.soso);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible);

                ll_not_good_puncture.setVisibility(View.INVISIBLE);


            }
        });
        iv_soso_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_puncture = Status.STATUS_EVL_SOSO;
                switchEvalutionButtonVisibility();
                iv_good_puncture.setImageResource(R.mipmap.good);
                iv_soso_puncture.setImageResource(R.mipmap.soso_press);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible);

                ll_not_good_puncture.setVisibility(View.VISIBLE);
            }
        });
        iv_terrible_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_puncture = Status.STATUS_EVL_TERRIBLE;
                switchEvalutionButtonVisibility();
                iv_good_puncture.setImageResource(R.mipmap.good);
                iv_soso_puncture.setImageResource(R.mipmap.soso);
                iv_terrible_puncture.setImageResource(R.mipmap.terrible_press);

                ll_not_good_puncture.setVisibility(View.VISIBLE);
            }
        });
        //选择项
        cb_erzhengchuanci = (CheckBox) findViewById(R.id.cb_erzhengchuanci);
        cb_huanman = (CheckBox) findViewById(R.id.cb_huanman);
        cb_tengtong = (CheckBox) findViewById(R.id.cb_tengtong);

        //态度评价
        ll_not_good = (LinearLayout) findViewById(R.id.ll_not_good);
        iv_good = (ImageView) findViewById(R.id.iv_good);
        iv_soso = (ImageView) findViewById(R.id.iv_soso);
        iv_terrible = (ImageView) findViewById(R.id.iv_terrible);

        iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_attitude = Status.STATUS_EVL_GOOD;
                switchEvalutionButtonVisibility();
                iv_good.setImageResource(R.mipmap.good_press);
                iv_soso.setImageResource(R.mipmap.soso);
                iv_terrible.setImageResource(R.mipmap.terrible);

                ll_not_good.setVisibility(View.INVISIBLE);

            }
        });
        iv_soso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_attitude = Status.STATUS_EVL_SOSO;
                switchEvalutionButtonVisibility();
                iv_good.setImageResource(R.mipmap.good);
                iv_soso.setImageResource(R.mipmap.soso_press);
                iv_terrible.setImageResource(R.mipmap.terrible);

                ll_not_good.setVisibility(View.VISIBLE);
            }
        });
        iv_terrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval_attitude = Status.STATUS_EVL_TERRIBLE;
                switchEvalutionButtonVisibility();
                iv_good.setImageResource(R.mipmap.good);
                iv_soso.setImageResource(R.mipmap.soso);
                iv_terrible.setImageResource(R.mipmap.terrible_press);
                ll_not_good.setVisibility(View.VISIBLE);
            }
        });

        //选项
        cb_bulimao = (CheckBox) findViewById(R.id.cb_bulimao);
        cb_ruma = (CheckBox) findViewById(R.id.cb_ruma);

        //---服务评价初始化结束---

        //呼叫护士服务请求
        iv_call = (ImageView) findViewById(R.id.iv_call);
//        iv_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dlg_call_service_view.setVisibility(View.VISIBLE);
//            }
//        });


//        1.纸巾
        tv_service_zhijin = (TextView) findViewById(R.id.tv_service_zhijin);
        tv_service_zhijin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "zhijin click");

            }
        });

        //        2.糖果
        tv_service_tangguo = (TextView) findViewById(R.id.tv_service_tangguo);
        tv_service_tangguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "tangguo click");

            }
        });

        //        3.热水
        tv_service_reshui = (TextView) findViewById(R.id.tv_service_reshui);
        tv_service_reshui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "reshui click");

            }
        });

        //        4.杂志
        tv_service_zazhi = (TextView) findViewById(R.id.tv_service_zazhi);
        tv_service_zazhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "zazhi click");
            }
        });

        //        5咨询
        tv_service_zixun = (TextView) findViewById(R.id.tv_service_zixun);
        tv_service_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "zixun click");
            }
        });

        //        6取消
        tv_service_cancel = (TextView) findViewById(R.id.tv_service_cancel);
        tv_service_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);

                MyLog.e("ERROR", "cancel click");
            }
        });
        /**** 呼叫护士服务内容   *******/
        //调节位置 上
        iv_rise = (ImageView) findViewById(R.id.iv_rise);
        iv_rise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e("ERROR", "rise");
            }
        });
        //调节位置 下
        iv_down = (ImageView) findViewById(R.id.iv_down);
        iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e("ERROR", "down");
            }
        });
    }


    //服务评价的提交按钮显示判断
    private void switchEvalutionButtonVisibility() {
        if (eval_attitude != Status.STATUS_EVL_DEFAULT && eval_puncture != Status.STATUS_EVL_DEFAULT) {
            btn_submit.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.INVISIBLE);
        }
    }


    //标题栏初始化
    private void initTitleBar() {

        //屏幕顶端的标题栏
        title_bar_view = findViewById(R.id.title_bar_view);

        //标题栏内部左侧的标题栏名字
        title_txt = (TextView) findViewById(R.id.title_txt);
        title_txt.setText(R.string.fragment_wait_check_title);

        //标题栏内部左侧的图标
        ivLogoAndBack = (ImageView) findViewById(R.id.logo_or_back);

        ivBack = (ImageView) findViewById(R.id.iv_back);

        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
                return false;
            }
        });
        mParentView = getLayoutInflater().inflate(R.layout.activity_main,
                null);
        mPopView = getLayoutInflater().inflate(R.layout.popupwin_main, null);

        //标题栏内部中间的采集进度
        ll_cl = (LinearLayout) findViewById(R.id.ll_cl);
        collect_pb = (HorizontalProgressBar) findViewById(R.id.collect_pb);
        collect_pb.setProgress(0);

        //标题栏内部右侧的北京时间
        time_txt = (TextView) findViewById(R.id.time_txt);

        //标题栏内部右侧的电量、网络信号。
        battery_pb = (VerticalProgressBar) findViewById(R.id.battery_pb);
        wifi_not_txt = (TextView) findViewById(R.id.wifi_not_txt);
        net_state_txt = (TextView) findViewById(R.id.net_state_txt);
        net_state_txt.setOnClickListener(this);

        //标题栏内部右侧的选择功能设置，服务器地址设置以及重启
        // TODO: 2016/4/29 参数配置及应用重启的功能还未能实现。
        overflow_image = (ImageView) findViewById(R.id.overflow_image);
        overflow_image.setOnClickListener(this);
//        fun_txt = (TextView) mPopView.findViewById(R.id.fun_txt);
//        fun_txt.setOnClickListener(this);

        restart_txt = (TextView) mPopView.findViewById(R.id.restart_txt);
        restart_txt.setOnClickListener(this);
    }

    //初始化tab选择
    private void initTabGroup() {
        mGroup = (RadioGroup) findViewById(R.id.tab_group);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_video:
                        //视频分类列表

                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

                        break;

                    case R.id.btn_surfinternet:
                        //上网
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOSURF);
                        break;

                    case R.id.btn_music:
                        //音乐分类列表
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOMUSICCATEGORY);
                        break;

                }
            }
        });
    }

    //中间部分的ui初始化
    private void initMainUI() {


        dlg_call_service_view = findViewById(R.id.dlg_call_service_view);


        switchFragment(R.id.fragment_container, CheckFragment.newInstance());

        battery_not_connect_txt = (TextView) findViewById(R.id.battery_not_connect_txt);
    }

    @Override
    public void loadData() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        test();
        try {
//      启动联网
            observableZXDCSignalListenerThread.start();
//      周期性PING服务器，查看是否和服务器中断联系
            checkSerReachable.start();
        } catch (IllegalThreadStateException e) {
            MyLog.e(TAG, "Thread Exception:" + e.toString());
        }


//      启动超时检测
        CheckTimeout checkTimeout = new CheckTimeout(1000 * 60);
        checkTimeout.setOnTimeoutCallback(this);
        checkTimeout.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e(TAG, "开始执行MainActivity中的onPause()函数" + this.toString());
        long start = System.currentTimeMillis();

//      停止周期性ping服务器的动作
        if (checkSerReachable != null) {
            checkSerReachable.interrupt();
        }



//      发送关闭MainActivity信号，执行保存现场的动作
        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.POWEROFF);

//      停止和服务器通信线程;并且删除观察者。
        if (observableZXDCSignalListenerThread != null) {
            observableZXDCSignalListenerThread.interrupt();
            observableZXDCSignalListenerThread.deleteObserver(observerZXDCSignalUIHandler);
        }

        Log.e(TAG, "暂停下的状态  " + recordState.getState());

        if (!LauActFlag.is) {
//            startMainActivityAgain();
        }

        long end = System.currentTimeMillis();
        Log.e(TAG, "结束执行MainActivity中的onPause()函数，耗时：" + (end - start) / 1000.0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "开始执行MainActivity中的onStop()函数");
        Log.e(TAG, "结束执行MainActivity中的onStop()函数");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "开始执行MainActivity中的onDestroy()函数");

        //      释放接收器
        if (receiver != null) {
            unregisterReceiver(receiver);
        }


        //断开服务
        if (bt_headset != null) {
            BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.HEADSET, bt_headset);
            bt_headset = null;
        }
        if (bt_a2dp != null) {
            BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.A2DP, bt_a2dp);
            bt_a2dp = null;
        }
        Log.e(TAG, "开始执行MainActivity中的onDestroy()函数");
    }


    public synchronized void dealBTCon() {


//        蓝牙连接成功：发送CHECKSTART
//        蓝牙连接失败：发送BTCONFAILURE

        Log.e(BT_LOG, "开始--处理蓝牙连接" + this.toString());
        title_txt.setText(R.string.bt_connecting);
        bluetoothContextState.setCurrentState(new BTclosedState());
        successA2DP = false;
        successHeadset = false;
        bt_conn_count = 0;
        DataPreference dataPreference = new DataPreference(this);
        bt_name = dataPreference.readStr("bluetooth_name");
        MyLog.e(BT_LOG, "setting中保存的蓝牙名字：" + bt_name);

        new AutoBTConThread().start();

        if (isStartCheckBTFlag) {
            new CheckConnStateThread().start();
            isStartCheckBTFlag = false;
        }

        ll_bt_container.setVisibility(View.VISIBLE);
        ll_bt_result_control.setVisibility(View.GONE);
        tv_bt_status.setText(R.string.bt_connecting);

        Log.e(BT_LOG, "结束--处理蓝牙连接");
    }

    @Override
    public void onUnreachable() {
        this.tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.RECONNECTWIFI);
    }

    private class AutoBTConThread extends Thread {

        @Override
        public void run() {
            super.run();
            bt_adapter = BluetoothAdapter.getDefaultAdapter();
            if (bt_adapter == null) {
                // 设备不支持蓝牙
                MyLog.e(BT_LOG, "不支持蓝牙");
                return;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            bluetoothContextState.setCurrentState(new InitialBTState());

            if (bt_adapter.isEnabled()) {
                MyLog.e(BT_LOG, "蓝牙已经打开");
                if (closeOpendBT(bt_adapter, 5)) {
                    MyLog.e(BT_LOG, "蓝牙关闭成功====再次打开蓝牙");
                } else {
                    MyLog.e(BT_LOG, "蓝牙关闭失败====返回");
                    return;
                }
            }

            MyLog.e(BT_LOG, "设置状态为关闭状态");
            bluetoothContextState.setCurrentState(new BTclosedState());

            if (openClosedBT(bt_adapter, 5)) {
                MyLog.e(BT_LOG, "蓝牙打开成功");
            } else {
                MyLog.e(BT_LOG, "蓝牙打开失败");
                return;
            }
        }
    }


    private class CheckConnStateThread extends Thread {

        private int n = 2;

        @Override
        public void run() {
            super.run();

            do {
                try {
                    sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!(bluetoothContextState.getCurrentState() instanceof BTConSuccessState)) {
                    tabletStateContext.handleMessge(recordState,
                            observableZXDCSignalListenerThread, null, null, RecSignal.BTCONSTART);
                }
            } while (--n > 0);
            tabletStateContext.handleMessge(recordState,
                    observableZXDCSignalListenerThread, null, null, RecSignal.BTCONFAILURE);
        }
    }

    private Boolean closeOpendBT(BluetoothAdapter bluetoothAdapter, int n) {
        MyLog.e(BT_LOG, "closeOpendBT");
        //每次尝试打开蓝牙前停顿2S
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        打开5次都不能打开成功就认为是蓝牙坏了

        if (n < 1) {
            return false;
        }

        //打开关闭的蓝牙
        if (bluetoothAdapter.disable()) {
            return true;
        } else {
//            打开失败使用递归方式继续打开
            return closeOpendBT(bluetoothAdapter, n--);
        }
    }

    private Boolean openClosedBT(BluetoothAdapter bluetoothAdapter, int n) {
        MyLog.e(BT_LOG, "openClosedBT");
        //每次尝试打开蓝牙前停顿2S
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        打开5次都不能打开成功就认为是蓝牙坏了

        if (n < 1) {
            return false;
        }

        //打开关闭的蓝牙
        if (bluetoothAdapter.enable()) {
            return true;
        } else {
//            打开失败使用递归方式继续打开
            return openClosedBT(bluetoothAdapter, n--);
        }
    }

    /*
蓝牙扫描
 */
    private class startBTDiscoveryThread extends Thread {
        @Override
        public void run() {
            super.run();
            bt_adapter = BluetoothAdapter.getDefaultAdapter();

            //停顿4S
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //开始扫描
            if (startDis(bt_adapter, 3)) {
                MyLog.e(BT_LOG, "开始扫描成功");
            } else {
                MyLog.e(BT_LOG, "开始扫描失败");

            }
        }
    }

    /**
     * Start the remote device discovery process.
     * <p>The discovery process usually involves an inquiry scan of about 12
     *
     * @return true on success, false on error
     */

    private boolean startDis(BluetoothAdapter bluetoothAdapter, int n) {
        //每次尝试开始扫描蓝牙前停顿2S
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       扫描5次都不能扫描成功就认为是蓝牙坏了
        if (n < 1) {
            return false;
        }

        //开始扫描蓝牙
        if (bluetoothAdapter.startDiscovery()) {
            return true;
        } else {
//            扫描失败使用递归方式继续扫描
            return startDis(bluetoothAdapter, n--);
        }
    }

    private void btProfileConnect() {
        bt_adapter.getProfileProxy(MainActivity.this, new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile,
                                           BluetoothProfile proxy) {
                if (profile == BluetoothProfile.HEADSET) {
                    bt_headset = (BluetoothHeadset) proxy;
                    try {
                        Method m = bt_headset.getClass()
                                .getDeclaredMethod("connect",
                                        BluetoothDevice.class);
                        m.setAccessible(true);
                        // 连接Headset
                        successHeadset = (Boolean) m.invoke(
                                bt_headset, currentDevice);
                        MyLog.e(BT_LOG, "BluetoothHeadset 连接结果：" + successHeadset
                                + ",device:" + currentDevice.getName());
                        checkBTConnState(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyLog.e(BT_LOG, "BluetoothHeadset连接结果出错" + e.toString());
                    }

                }
            }

            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.HEADSET) {
                    bt_headset = null;
                }
            }
        }, BluetoothProfile.HEADSET);

        bt_adapter.getProfileProxy(MainActivity.this, new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.A2DP) {
                    bt_a2dp = (BluetoothA2dp) proxy;
                    try {
                        MyLog.e(BT_LOG, "a2dp开始连接" + currentDevice);
                        Method m = bt_a2dp.getClass().getDeclaredMethod(
                                "connect", BluetoothDevice.class);
                        m.setAccessible(true);
                        // 连接a2dp
                        successA2DP = (Boolean) m.invoke(
                                bt_a2dp, currentDevice);
                        MyLog.e(BT_LOG, "a2dp连接结果：" + successA2DP
                                + ",device:" + currentDevice.getName());
                        checkBTConnState(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyLog.e(BT_LOG, "a2dp连接结果出错" + e.toString());
                    }

                }
            }

            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.A2DP) {
                    bt_a2dp = null;
                }
            }
        }, BluetoothProfile.A2DP);

    }

    //判断蓝牙是否连接成功，标准1.headset成功2.a2dp成功

    private boolean checkBTConnState(boolean isChangeState) {
        boolean sucess = false;
        if (successHeadset && successA2DP) {
            //成功连接
            MyLog.e(BT_LOG, "连接成功");
            if (isChangeState) {
                ll_bt_container.setVisibility(View.GONE);
                bluetoothContextState.setCurrentState(new BTConSuccessState());
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
            }
            sucess = true;
        } else {
            //连接失败
            MyLog.e(BT_LOG, "连接失败");
            if (isChangeState) {
                bluetoothContextState.setCurrentState(new BTConFailureState());
            }
            sucess = false;
        }
        return sucess;
    }

    public synchronized void dealBTConFailure() {

        //跳过就进入：发送CHECKSTART
        //进入设置界：SETTING
        //重连:BTCONSTART


        ll_bt_container.setVisibility(View.VISIBLE);
        tv_bt_status.setText(R.string.bt_connect_fail);
        ll_bt_result_control.setVisibility(View.VISIBLE);


        btn_bt_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bt_container.setVisibility(View.GONE);
                ll_bt_result_control.setVisibility(View.GONE);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKSTART);
            }
        });


        btn_bt_reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bt_container.setVisibility(View.GONE);
                ll_bt_result_control.setVisibility(View.GONE);

                isStartCheckBTFlag = true;
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.BTCONSTART);

            }
        });

        btn_bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bt_container.setVisibility(View.GONE);
                ll_bt_result_control.setVisibility(View.GONE);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
            }
        });
    }


    public HorizontalProgressBar getHorizontalProgressBar() {
        return collect_pb;
    }


    public boolean isBatteryOK() {
        return batteryIsOk;
    }


    public ImageView getIvBack() {
        return ivBack;
    }

    public ProgressDialog getAllocDevDialog() {
        return allocDevDialog;
    }

    public TextView getTitleTV() {
        return title_txt;
    }

    public ImageView getIvLogoAndBack() {
        return this.ivLogoAndBack;
    }

    public void uiComponent(boolean leftHint, boolean titleBar, boolean tabGroup, boolean collection) {
        if (leftHint) {
            left_hint_view.setVisibility(View.VISIBLE);
        } else {
            left_hint_view.setVisibility(View.GONE);
        }

        if (titleBar) {
            title_bar_view.setVisibility(View.VISIBLE);
        } else {
            title_bar_view.setVisibility(View.GONE);
        }

        if (tabGroup) {
            mGroup.setVisibility(View.VISIBLE);
        } else {
            mGroup.setVisibility(View.GONE);
        }

        if (collection) {

            ll_cl.setVisibility(View.VISIBLE);
        } else {
            ll_cl.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {

            case R.id.restart_txt:
                //重启
//                LauActFlag.is = true;
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.RESTART);
                break;
            case R.id.overflow_image:
                showPopWindow();
                break;
            case R.id.net_state_txt:
                //检测网络和检查服务器配置
//                recordState
                startMainActivityAgain();
                break;

        }
        if (it != null) {
            startActivity(it);
        }
    }


    private void showPopWindow() {
        View view = findViewById(R.id.ll_test);
        view.setVisibility(View.VISIBLE);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    false);
            mPopupWindow.setHeight(AppInfoUtils.dip2px(MainActivity.this, 100));
            mPopupWindow.setWidth(AppInfoUtils.dip2px(MainActivity.this, 210));
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

        }
        mPopupWindow
                .setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                    }
                });
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.showAtLocation(mParentView, Gravity.RIGHT
                        | Gravity.TOP, AppInfoUtils.dip2px(MainActivity.this, 2),
                AppInfoUtils.dip2px(MainActivity.this, 76));
    }

    public void switchFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            fragmentManager.beginTransaction().replace(containerViewId, fragment).commitAllowingStateLoss();
        }
    }

    // 重新开启一个MainActivity
    public void startMainActivityAgain() {
        Log.e("error", "startMainActivityAgain");
        Intent intentToNewMainActivity = new Intent(MainActivity.this, MainActivity.class);
//        intentToNewMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intentToNewMainActivity);
        MainActivity.this.finish();
    }

    // 重新开启LaunchActivity
    public void startLaunchActivityAgain() {
        Log.e("error", "startLaunchActivityAgain");
        Intent intentToNewMainActivity = new Intent(MainActivity.this, LaunchActivity.class);
//        intentToNewMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intentToNewMainActivity);
        MainActivity.this.finish();
    }

    private void startTimeService() {
        Intent it = new Intent(MainActivity.this, TimeService.class);
        it.putExtra("currenttime", ServerTime.curtime);
        startService(it);
    }

    private void startScanBackupVideoService() {
        Intent it = new Intent(MainActivity.this, ScanBackupVideoService.class);
        startService(it);
    }

    private void startMonitorService() {
        Intent it = new Intent(MainActivity.this, BackgroundMonitorService.class);
        startService(it);
    }

    //    如果60秒后还没连上服务器，就执行如下动作；
    @Override
    public void timeout() {
        tabletStateContext.handleMessge(recordState,
                observableZXDCSignalListenerThread,
                null, null, RecSignal.TIMESTAMPTIMEOUT);
    }

    private void test() {
        //收到浆员信息
        Button btn_send_donor_info = (Button) this.findViewById(R.id.btn_send_donor_info);
        btn_send_donor_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CONFIRM);
            }
        });

        //连接蓝牙
        Button btn_connect_bt = (Button) this.findViewById(R.id.btn_connect_bt);
        btn_connect_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dealBTCon();

//                tabletStateContext.setCurrentState(WaitingForBTConState.getInstance());

//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.BTCONSTART);
            }
        });

//        蓝牙连接失败
        Button btn_connect_bt_fail = (Button) this.findViewById(R.id.btn_connect_bt_fail);
        btn_connect_bt_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.BTCONFAILURE);
            }
        });

        //认证通过
        Button btn_auth_pass = (Button) this.findViewById(R.id.btn_auth_pass);
        btn_auth_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHPASS);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.ZXDCAUTHRES);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SERAUTHRES);

            }
        });


        //加压
        Button btn_compression = (Button) this.findViewById(R.id.btn_compression);
        btn_compression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.COMPRESSINON);

                MyLog.e("SCR", "screen:" + BrightnessTools.getScreenBrightness(MainActivity.this));
            }
        });

        //穿刺
        Button btn_puncture = (Button) this.findViewById(R.id.btn_puncture);
        btn_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PUNCTURE);
            }
        });

        //采集开始
        Button btn_collection_start = (Button) this.findViewById(R.id.btn_collection_start);
        btn_collection_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.START);
            }
        });

        //血浆重量
        Button btn_plasma_weight = (Button) this.findViewById(R.id.btn_plasma_weight);
        btn_plasma_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PLASMAWEIGHT);
            }
        });

        //管压过低
        Button btn_pipe_low = (Button) this.findViewById(R.id.btn_pipe_low);
        btn_pipe_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PIPELOW);
            }
        });

        //管压正常
        Button btn_pipe_normal = (Button) this.findViewById(R.id.btn_pipe_normal);
        btn_pipe_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PIPENORMAL);
            }
        });

        //采集结束
        Button btn_collection_end = (Button) this.findViewById(R.id.btn_collection_end);
        btn_collection_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.END);
            }
        });

        //设备可用
        Button btn_check_pass = (Button) this.findViewById(R.id.btn_device_available);
        btn_check_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);
            }
        });

        //设备不可用
        Button btn_device_unavailable = (Button) this.findViewById(R.id.btn_device_unavailable);
        btn_device_unavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.LOWPOWER);
            }
        });

    }

    //调低屏幕亮度
    private void setBrightnessLow() {
        BrightnessTools.stopAutoBrightness(this);
        BrightnessTools.setBrightness(this, 2);
    }

    //调正常屏幕亮度
    public void setBrightnessNormal() {
        BrightnessTools.startAutoBrightness(this);
    }
}

