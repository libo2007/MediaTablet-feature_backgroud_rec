package com.jiaying.mediatablet.net.signal.receiver;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.MusicCategorizeFragment;
import com.jiaying.mediatablet.fragment.collection.VideoCategorizeFragment;

/**
 * Created by hipil on 2016/9/16.
 */
public class ToMusicCategoryReceiver extends Receiver{
    private MainActivity mainActivity;

    public ToMusicCategoryReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        Log.e("ERROR", "开始--音乐分类");
        this.mainActivity.uiComponent(true, true, true, true);
        TextView tv_title = this.mainActivity.getTitleTV();
        tv_title.setText(R.string.play_music);
        ImageView ivLogoAndBack = this.mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

        this.mainActivity.switchFragment( R.id.fragment_container, new MusicCategorizeFragment());
        Log.e("ERROR", "结束--音乐分类");
    }
}
