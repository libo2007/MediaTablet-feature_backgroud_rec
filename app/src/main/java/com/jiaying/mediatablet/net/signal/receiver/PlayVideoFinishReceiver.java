package com.jiaying.mediatablet.net.signal.receiver;

import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.VideoListFragment;

/**
 * Created by hipil on 2016/9/17.
 */
public class PlayVideoFinishReceiver extends Receiver{
    private MainActivity mainActivity;

    public PlayVideoFinishReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {

        this.mainActivity.uiComponent(true, true, true, true);

        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.watch_film);

        ImageView ivLogoAndBack = mainActivity.getIvLogoAndBack();
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        ivLogoAndBack.setEnabled(false);

        //switch
        this.mainActivity.switchFragment(R.id.fragment_container, new VideoListFragment());
    }
}
