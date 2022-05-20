package com.example.spider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

class SpawnNotification {

   private static final String CHANNEL_ID = "NotificationID";

   void buildNotification(Context context, String title, String contentText, PendingIntent PI){
      createNotificationChannel(context);
      NotificationCompat.Builder builder = new NotificationCompat
              .Builder(context, CHANNEL_ID)
              .setSmallIcon(R.drawable.ic_launcher_foreground)
              .setContentTitle(title)
              .setContentText(contentText)
              .setContentIntent(PI)
              .setPriority(NotificationCompat.PRIORITY_DEFAULT);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.
              from(context);
      int uid = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
      notificationManager.notify(uid, builder.build());

   }


   private void createNotificationChannel(Context context) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         CharSequence name = "Notification name";
         String description = "Description";
         int importance = NotificationManager.IMPORTANCE_DEFAULT;
         NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
         channel.setDescription(description);
         NotificationManager notificationManager = context
                 .getSystemService(NotificationManager.class);
         notificationManager.createNotificationChannel(channel);
      }
   }
}
