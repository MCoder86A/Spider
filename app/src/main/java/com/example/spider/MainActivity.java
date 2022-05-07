package com.example.spider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarm();
        setPeriodicWork();

    }

    public void onUrlSubmit(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText urlText = (EditText) findViewById(R.id.urlText);
                String urlTextStr = urlText.getText().toString();

                FIleManager fm = new FIleManager(getApplicationContext());
                JSONObject jObj = fm.readFileString2JSON();
                try {
                    JSONObject urlList = jObj.getJSONObject("url");
                    if(!urlList.has(urlTextStr)){
                        urlList.put(urlTextStr, "");
                        fm.writeFileJSON2String(jObj);
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void setPeriodicWork() {
        PeriodicWorkRequest myWorker = new PeriodicWorkRequest.Builder(Workmgr.class,
                20, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .setInitialDelay(15, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("work01",
                        ExistingPeriodicWorkPolicy.KEEP,
                        myWorker);
    }

    void setAlarm(){
        Intent intent = new Intent(this, myAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + (60 * 1000), //1M
                43200000, //12Hr
                pendingIntent);
    }

}