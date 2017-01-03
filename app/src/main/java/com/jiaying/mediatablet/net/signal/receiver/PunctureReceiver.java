package com.jiaying.mediatablet.net.signal.receiver;

import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;
import com.jiaying.mediatablet.fragment.collection.JCPlayVideoFragment;

/**
 * Created by hipil on 2016/9/17.
 */

//处理穿刺信号，这个穿刺来自pressingFragment页面播报结束后，对服务器的穿刺信号做了忽略。

public class PunctureReceiver extends Receiver {
    private MainActivity mainActivity;

    public PunctureReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void work() {
        this.mainActivity.uiComponent(true, true, false, true);

        //界面切换：
        //调出企业文化界面
        //开始播放采集视频

        JCPlayVideoFragment playVideoFragment = JCPlayVideoFragment.newInstance("/sdcard/donation.mp4", "PunctureVideo",false);

//        PlayVideoFragment playVideoFragment = PlayVideoFragment.newInstance("/sdcard/donation.mp4", "PunctureVideo");

        this.mainActivity.switchFragment(R.id.fragment_container, playVideoFragment);
        //如果加压信号跳过了，需要调出采集中预览画面
        CollectionPreviewFragment collectionPreviewFragment = this.mainActivity.getCollectionPreviewFragment();
        if (collectionPreviewFragment == null) {
            collectionPreviewFragment = new CollectionPreviewFragment();
            this.mainActivity.setCollectionPreviewFragment(collectionPreviewFragment);
            this.mainActivity.switchFragment(R.id.fragment_record_container, collectionPreviewFragment);
        }

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_puncture_video);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);
    }
}
