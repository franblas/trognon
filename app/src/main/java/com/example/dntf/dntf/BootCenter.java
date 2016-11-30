package com.example.dntf.dntf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by franblas on 30/11/16.
 */
public class BootCenter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationsCenter.setupAlarm(new MainActivity());
    }
}
