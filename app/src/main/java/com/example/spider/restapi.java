package com.example.spider;

import android.text.Html;
import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Documented;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

class restapi {
   final static String TAG = "API";

   void getResp() {
      URL api_url = null;

      {
         try {
            api_url = new URL("https://notification.gauhati.ac.in/college/examination");
         } catch (MalformedURLException e) {
            e.printStackTrace();
         }
      }

      HttpsURLConnection connection;

      {
         try {
            connection = (HttpsURLConnection) api_url.openConnection();
            if (connection.getResponseCode() == 200) {
               InputStream response = connection.getInputStream();
               Scanner sc = new Scanner(response, String.valueOf(StandardCharsets.UTF_8));
               StringBuilder responseText = new StringBuilder("");
               while(sc.hasNextLine()){
                  responseText.append(sc.nextLine());
                  responseText.append('\n');
               }

               Document html_doc = Jsoup.parse(responseText.toString());
               Elements sectionEl = html_doc.getElementsByTag("section");

               Element top_notice_el = sectionEl.get(1);

               Log.i(TAG, top_notice_el.text());


            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   String sha256(final String base) {
      try{
         final MessageDigest digest = MessageDigest.getInstance("SHA-256");
         final byte[] hash = digest.digest(base.getBytes("UTF-8"));
         final StringBuilder hexString = new StringBuilder();
         for (int i = 0; i < hash.length; i++) {
            final String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
               hexString.append('0');
            hexString.append(hex);
         }
         return hexString.toString();
      } catch(Exception ex){
         throw new RuntimeException(ex);
      }
   }
}