package com.example.spider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class myAlarm extends BroadcastReceiver {
    private static final String CHANNEL_ID = "AlarmNotificationID";
    final static String TAG = "API";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "AppWakeUp");
    }
}
