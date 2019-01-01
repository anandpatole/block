package com.example.block.app_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

public class Block_All_Notification extends NotificationListenerService {
    ArrayList<String> currentTasks;
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        // Inform the notification manager about dismissal of all notifications.
        Log.d("Msg", sbn.getPackageName());
       // Block_All_Notification.this.cancelAllNotifications();
        String packageName = sbn.getPackageName();
        SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
        try {
            currentTasks = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(currentTasks!=null) {
            if(currentTasks.size()>0) {
                for (String a : currentTasks) {
                    if (packageName.equalsIgnoreCase(a)) {
//            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            nMgr.cancel(sbn.getId());
                        // cancelNotification(packageName);
                        cancelNotification(sbn.getKey());

                        //cancelAllNotifications();
                    }
                }
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
        Log.d("Msg", "Notification Removed");
    }
}
