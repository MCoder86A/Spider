package com.example.spider;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class FIleManager {
   private static final String TAG = "FIleManager";
   Context context;
   FIleManager(Context context){
      this.context = context;
   }


   JSONObject readFileString2JSON(){
      File file = new File(context
              .getExternalFilesDir(null), "urlFile.txt");

      try {
         FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis);
         Scanner sc = new Scanner(isr);
         StringBuilder sb = new StringBuilder();
         while(sc.hasNextLine()) {
            sb.append(sc.nextLine());
            sb.append('\n');
         }
         fis.close();
         sc.close();
         isr.close();
         return new JSONObject(sb.toString());

      } catch (IOException | JSONException e) {
         e.printStackTrace();
         buildFileJSON2String();
      }
      return null;
   }
   void writeFileJSON2String(JSONObject jObj){
      File file = new File(context
              .getExternalFilesDir(null), "urlFile.txt");
      try {
         String writeable = jObj.toString();
         FileOutputStream fos = new FileOutputStream(file);
         fos.write(writeable.getBytes());
         fos.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   void buildFileJSON2String(){
      File file = new File(context
              .getExternalFilesDir(null), "urlFile.txt");
      JSONObject jObj =  new JSONObject();
      JSONObject jObj_url = new JSONObject();

      try {
         jObj.put("url", jObj_url);
         String writeable = jObj.toString();
         FileOutputStream fos = new FileOutputStream(file);
         fos.write(writeable.getBytes());
         fos.close();
      } catch (JSONException | IOException e) {
         e.printStackTrace();
      }

   }
   void buildErrorFile(){
      File file = new File(context
              .getExternalFilesDir(null), "err");

      try {
         FileOutputStream fos = new FileOutputStream(file);
         fos.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }
}
