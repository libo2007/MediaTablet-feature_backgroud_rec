package com.jiaying.mediatablet.net.signal.receiver;

import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.fragment.collection.CollectionFragment;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;

/**
 * Created by hipil on 2016/9/16.
 */
public class StartReceiver extends Receiver{
    private MainActivity mainActivity;

    public StartReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //设置显示状态
        this.mainActivity.uiComponent(true, true, false, true);

        //界面切换：
        //播放采集提示
        this.mainActivity.switchFragment(R.id.fragment_container, new CollectionFragment());

        //如果加压信号跳过了，需要调出采集中预览画面
        CollectionPreviewFragment collectionPreviewFragment= mainActivity.getCollectionPreviewFragment();
        if (collectionPreviewFragment == null) {
            collectionPreviewFragment = new CollectionPreviewFragment();
            mainActivity.setCollectionPreviewFragment(collectionPreviewFragment);
            this.mainActivity.switchFragment(R.id.fragment_record_container, collectionPreviewFragment);
        }
        //设置文字内容
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_collect_title);

        HorizontalProgressBar collect_pb = mainActivity.getHorizontalProgressBar();
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());

        //设置logo按钮事件
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);
    }
}
