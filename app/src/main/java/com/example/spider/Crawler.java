package com.example.spider;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

class Crawler {
    final static String TAG = "Crawler";

    String getHash(String url) {
        try {
            URL api_url = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection)api_url.openConnection();
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                Scanner sc = new Scanner(response, String.valueOf(StandardCharsets.UTF_8));
                StringBuilder responseText = new StringBuilder("");
                while(sc.hasNextLine()){
                    responseText.append(sc.nextLine());
                    responseText.append('\n');
                }

                Document html_doc = Jsoup.parse(responseText.toString());
                Elements bodyEls = html_doc.getElementsByTag("body");

                Element body = bodyEls.get(0);

                return sha256(body.text());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
    String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
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