package com.jiaying.mediatablet.net.signal.receiver;

import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.collection.SurfInternetFragment;

/**
 * Created by hipil on 2016/9/16.
 */
public class ToSurfReceiver extends Receiver {
    private MainActivity mainActivity;

    public ToSurfReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        this.mainActivity.switchFragment(R.id.fragment_container, new SurfInternetFragment());

        TextView tv_title = mainActivity.getTitleTV();
        tv_title.setText(R.string.surf_internet);

    }
}
