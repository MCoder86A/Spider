package com.example.spider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_url2);
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            getMainExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Url added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}