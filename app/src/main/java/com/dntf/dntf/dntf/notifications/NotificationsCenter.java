package com.dntf.dntf.dntf.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import com.dntf.dntf.dntf.logic.ExpiredFoodLogic;
import com.dntf.dntf.dntf.data.FoodApi;
import com.dntf.dntf.dntf.FoodListActivity;
import com.dntf.dntf.dntf.MainActivity;
import com.dntf.dntf.dntf.R;
import com.dntf.dntf.dntf.data.SharedData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by franblas on 30/11/16.
 */
public class NotificationsCenter extends BroadcastReceiver {

    private final Set<Integer> badFoodStatus = new HashSet<>(Arrays.asList(
            ExpiredFoodLogic.CRITICAL_STATUS,
            ExpiredFoodLogic.SEVERE_STATUS,
            ExpiredFoodLogic.INTERMEDIATE_STATUS));

    public static void setupAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);
        Intent intent = new Intent(context, NotificationsCenter.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(MainActivity.ALARM_SERVICE);
        am.cancel(pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void sendNotification(Context context, int nbProducts) {
        final String notificationTitle = context.getString(R.string.notification_title);
        Intent intent = new Intent(context, FoodListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setAutoCancel(true)
         .setDefaults(Notification.DEFAULT_ALL)
         .setWhen(System.currentTimeMillis())
         .setColor(Color.rgb(39, 174, 96)) // nephritis color
         .setLights(Color.GREEN, 3000, 3000)
         .setSmallIcon(R.drawable.dntf_logo_dark)
         .setTicker(notificationTitle)
         .setContentTitle(notificationTitle)
         .setContentText(context.getString(R.string.notification_content_1) + " " + nbProducts + " " + context.getString(R.string.notification_content_2))
         .setDefaults(Notification.DEFAULT_SOUND)
         .setContentIntent(contentIntent)
         .setContentInfo(notificationTitle);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long now = System.currentTimeMillis();
        int numberOfBadStatus = 0;

        SharedData sharedData = new SharedData(context);
        ArrayList<JSONObject> products = sharedData.getProductsList();

        for (JSONObject product : products) {
            long addedTime = FoodApi.getAddedTime(product);
            int expiredFoodStatus = ExpiredFoodLogic.getStatus(now, addedTime);
            if (badFoodStatus.contains(expiredFoodStatus)) {
                numberOfBadStatus ++;
            }
        }
        // Only send a notification if necessary
        if (numberOfBadStatus > 0) {
            sendNotification(context, numberOfBadStatus);
        }
    }
}
