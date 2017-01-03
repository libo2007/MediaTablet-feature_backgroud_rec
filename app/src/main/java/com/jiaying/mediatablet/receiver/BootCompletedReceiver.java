package com.jiaying.mediatablet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jiaying.mediatablet.activity.LaunchActivity;

import com.jiaying.mediatablet.constants.IntentExtra;


public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent launchIntent = new Intent(context, LaunchActivity.class);

            launchIntent.putExtra(IntentExtra.EXTRA_BOOT, true);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(launchIntent);
        }
    }
}
