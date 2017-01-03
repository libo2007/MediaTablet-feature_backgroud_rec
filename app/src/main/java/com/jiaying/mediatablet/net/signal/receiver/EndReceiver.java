package com.jiaying.mediatablet.net.signal.receiver;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.constants.Status;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.end.EndFragment;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.thread.AniThread;

/**
 * Created by hipil on 2016/11/22.
 */
public class EndReceiver extends Receiver {
    private MainActivity mainActivity;
    private TabletStateContext tabletStateContext;
    private RecordState recordState;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread;


    public EndReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        tabletStateContext = mainActivity.getTabletStateContext();
        recordState = mainActivity.getRecordState();
        observableZXDCSignalListenerThread = mainActivity.getObservableZXDCSignalListenerThread();
    }

    @Override
    public void work() {
        //设置显示状态
        this.mainActivity.uiComponent(false, true, false, false);

        //界面切换：
        this.mainActivity.switchFragment(R.id.fragment_record_container, new BlankFragment());
        this.mainActivity.switchFragment(R.id.fragment_container, new EndFragment());
        PlasmaWeightEntity.getInstance().setCurWeight(0);
        //设置文字内容
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

        TextView title_txt = this.mainActivity.getTitleTV();
        title_txt.setText(R.string.end);
        //设置logo按钮事件

        //启动相关动作

        //有可能在结束的时候还未关闭握拳提示，如果不关闭则会在后台一直运行该线程。
        AniThread aniThread = this.mainActivity.getStartFist();
        if (aniThread != null) {
            aniThread.finishAni();
        }
        //服务评价要初始化

//        eval_puncture = Status.STATUS_EVL_DEFAULT;
//        eval_attitude = Status.STATUS_EVL_DEFAULT;
//        iv_good_puncture.setImageResource(R.mipmap.good);
//        iv_soso_puncture.setImageResource(R.mipmap.soso);
//        iv_terrible_puncture.setImageResource(R.mipmap.terrible);
//
//        iv_good.setImageResource(R.mipmap.good);
//        iv_soso.setImageResource(R.mipmap.soso);
//        iv_terrible.setImageResource(R.mipmap.terrible);
//
//        ll_not_good_puncture.setVisibility(View.INVISIBLE);
//        ll_not_good.setVisibility(View.INVISIBLE);
//        btn_submit.setVisibility(View.INVISIBLE);
//        fl_service_evalution.setVisibility(View.GONE);
//
//
//        cb_erzhengchuanci.setChecked(false);
//        cb_ruma.setChecked(false);
//        cb_tengtong.setChecked(false);
//        cb_bulimao.setChecked(false);
//        cb_huanman.setChecked(false);
//
//        dlg_call_service_view.setVisibility(View.GONE);
    }
}
