package com.jiaying.mediatablet.net.signal.receiver;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;

/**
 * Created by hipil on 2016/9/16.
 */
public class StopRecReceiver extends Receiver{
    private MainActivity mainActivity;

    public StopRecReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void work() {
        CollectionPreviewFragment collectionPreviewFragment = mainActivity.getCollectionPreviewFragment();

        if (collectionPreviewFragment != null) {
            BlankFragment blankFragment = new BlankFragment();
            mainActivity.switchFragment(R.id.fragment_record_container, blankFragment);
        }

    }
}
