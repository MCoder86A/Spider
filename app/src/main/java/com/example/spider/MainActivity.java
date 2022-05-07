package com.example.spider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.Edits;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.ArrayList;
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

        FIleManager fm = new FIleManager(getApplicationContext());
        JSONObject json_root = fm.readFileString2JSON();

        TextView timeV = (TextView)findViewById(R.id.timeView);
        try {
            String prefix = "Last update: ";
            timeV.setText(prefix+json_root.getString("Time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> urlListToDisplay = new ArrayList<>();
        try {
            JSONObject json_listUrls = json_root.getJSONObject("url");
            for(Iterator<String> it = json_listUrls.keys(); it.hasNext();){
                String url = it.next();
                if(!url.equals("")) {
                    urlListToDisplay.add(url);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView urlList = (RecyclerView) findViewById(R.id.RV);
        UrlListRecyclerView rAdapter = new UrlListRecyclerView(urlListToDisplay);
        urlList.setHasFixedSize(true);
        urlList.setLayoutManager(new LinearLayoutManager(this));
        urlList.setAdapter(rAdapter);

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

    public void StartAddUrlActivity(View view) {
        Intent addUrlIntent = new Intent(this, AddUrlActivity.class);
        startActivity(addUrlIntent);

    }
}