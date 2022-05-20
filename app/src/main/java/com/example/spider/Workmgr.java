package com.example.spider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
                try{
                    String s = it.next();
                    String hash = crawler.getHash(s);
                    String localHash = jObj_urls.getString(s);
                    if (!hash.equals(localHash)) {
                        jObj_urls.put(s, hash);
                        notifyUSer(s);
                    }
                }
                catch (RuntimeException e){
                    e.printStackTrace();
                }
            }

            long milliSec = System.currentTimeMillis();
            String time = DateUtils.formatDateTime(getApplicationContext(),
                    milliSec,
                    DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE);
            jObj_root.put("Time", time);

            fm.writeFileJSON2String(jObj_root);

        } catch (JSONException | RuntimeException e) {
            e.printStackTrace();
        }

        return Result.success();
    }

    private void notifyUSer(String url_forUri){
        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
        urlIntent.setData(Uri.parse(url_forUri));
        PendingIntent urlListPI = PendingIntent.getActivity(getApplicationContext(),
                0 , urlIntent, 0);

        int maxContentLen = Math.min(150, url_forUri.length());
        new SpawnNotification().buildNotification(getApplicationContext(),
                "Spider",
                "Found new version of "+ url_forUri.substring(0, maxContentLen),
                urlListPI);

    }

}