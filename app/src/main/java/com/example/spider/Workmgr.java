package com.example.spider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Workmgr extends Worker {

    private static final String CHANNEL_ID = "NotificationID";
    private static final String TAG = "WorkMgr";

    public Workmgr(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Crawler crawler = new Crawler();
        FIleManager fm = new FIleManager(getApplicationContext());
        JSONObject jObj_root = fm.readFileString2JSON();
        try {
            JSONObject jObj_urls = jObj_root.getJSONObject("url");
            for (Iterator<String> it = jObj_urls.keys(); it.hasNext(); ) {
                String s = it.next();
                String hash = crawler.getHash(s);
                String localHash = jObj_urls.getString(s);
                if(!hash.equals(localHash)){
                    jObj_urls.put(s, hash);
                    buildNotification();
                }
            }

            long milliSec = System.currentTimeMillis();
            String time = DateUtils.formatDateTime(getApplicationContext(),
                    milliSec,
                    DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE);
            jObj_root.put("Time", time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            fm.writeFileJSON2String(jObj_root);
        }

        return Result.success();
    }

    private void buildNotification(){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Spider")
                .setContentText("Found new version")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.
                from(getApplicationContext());
        int uid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(uid, builder.build());

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext()
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}