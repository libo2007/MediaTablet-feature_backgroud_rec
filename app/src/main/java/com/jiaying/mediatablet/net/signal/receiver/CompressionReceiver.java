package com.jiaying.mediatablet.net.signal.receiver;

import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;
import com.jiaying.mediatablet.fragment.pression.PressingFragment;

/**
 * Created by hipil on 2016/9/16.
 */
public class CompressionReceiver extends Receiver{
    private MainActivity mainActivity;

    public CompressionReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        //设置显示状态
        this.mainActivity.uiComponent(true, true, false, true);

        //界面切换：
        //调出企业文化界面
        //播报加压提示,播放语播放完毕后会自动发送穿刺信号。
        PressingFragment pressingFragment = new PressingFragment();
        this.mainActivity.switchFragment(R.id.fragment_container, pressingFragment);


        //调出采集过程中预览画面
        CollectionPreviewFragment collectionPreviewFragment = new CollectionPreviewFragment();
        mainActivity.setCollectionPreviewFragment(collectionPreviewFragment);
        this.mainActivity.switchFragment(R.id.fragment_record_container, collectionPreviewFragment);

        //设置文字内容
        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.fragment_pressing_title);

        //设置logo按钮事件
        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

    }
}
