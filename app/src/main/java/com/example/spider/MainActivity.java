package com.example.spider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "NotificationID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        WorkRequest myWorker = new OneTimeWorkRequest.Builder(Workmgr.class)
//                .setConstraints(new Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build())
//                .build();
        WorkRequest myWorker = new PeriodicWorkRequest.Builder(Workmgr.class,
                1, TimeUnit.HOURS,
                15, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();
        WorkManager.getInstance(this)
                .enqueue(myWorker);

        new Thread(new Runnable() {
            @Override
            public void run() {
                restapi http = new restapi();
                http.getResp();
            }
        }).start();

    }

}