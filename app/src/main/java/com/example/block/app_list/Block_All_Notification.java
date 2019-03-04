package com.example.block.app_list;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.block.RootUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Block_All_Notification extends NotificationListenerService {
    ArrayList<String> currentTasks;
    public  ArrayList<HashMap<String,ArrayList<String>>> dayslist;
    public  ArrayList<HashMap<String,ArrayList<Long>>> timelist;
    @Override
    public IBinder onBind(Intent intent) {

        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        // Inform the notification manager about dismissal of all notifications.
        try {
            Log.d("Msg", sbn.getPackageName());
            // Block_All_Notification.this.cancelAllNotifications();
            String packageName = sbn.getPackageName();
            SharedPreferences prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE);
            try {
                currentTasks = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(new ArrayList<String>())));
                dayslist = (ArrayList<HashMap<String, ArrayList<String>>>) ObjectSerializer.deserialize(prefs.getString("days", ObjectSerializer.serialize(new ArrayList<HashMap<String, ArrayList<String>>>())));
                timelist = (ArrayList<HashMap<String, ArrayList<Long>>>) ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(new ArrayList<HashMap<String, ArrayList<Long>>>())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String todaysDay = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
            if (currentTasks != null) {
                if (currentTasks.size() > 0) {
                    for (String a : currentTasks) {
                        if (packageName.equalsIgnoreCase(a)) {
                            for (HashMap<String, ArrayList<String>> ab : dayslist) {
                                if (ab.containsKey(packageName)) {
                                    ArrayList<String> dayslists = ab.get(packageName);
                                    for (String ach : dayslists) {
                                        Log.e("day",todaysDay);
                                        if (ach.contains(todaysDay)) {
                                            for (HashMap<String, ArrayList<Long>> abc : timelist) {

                                                if (abc.containsKey(packageName)) {
                                                    ArrayList<Long> timelists = abc.get(packageName);
                                                    long starttime = timelists.get(0);
                                                    long stoptime = timelists.get(1);

                                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

                                                    String start = sdf.format(new Date(starttime));
                                                    String end = sdf.format(new Date(stoptime));
                                                    String current = sdf.format(new Date());
                                                    Log.e("check condition","wewew");

                                                    try {
                                                        if (isTimeBetweenTwoTime(start, end, current)) {

                                                            Log.e("cancel notification",sbn.getKey().toString());
                                                            cancelNotification(sbn.getKey());
                                                        }


                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e("exception",e.getMessage());
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
        Log.d("Msg", "Notification Removed");
    }

    public  boolean isTimeBetweenTwoTime(String argStartTime,
                                         String argEndTime, String argCurrentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9])$";
        //
        if (argStartTime.matches(reg) && argEndTime.matches(reg)
                && argCurrentTime.matches(reg)) {
            boolean valid = false;
            // Start Time
            java.util.Date startTime = new SimpleDateFormat("HH:mm")
                    .parse(argStartTime);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);

            // Current Time
            java.util.Date currentTime = new SimpleDateFormat("HH:mm")
                    .parse(argCurrentTime);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentTime);

            // End Time
            java.util.Date endTime = new SimpleDateFormat("HH:mm")
                    .parse(argEndTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);

            //
            if (currentTime.compareTo(endTime) < 0) {

                currentCalendar.add(Calendar.DATE, 1);
                currentTime = currentCalendar.getTime();

            }

            if (startTime.compareTo(endTime) < 0) {

                startCalendar.add(Calendar.DATE, 1);
                startTime = startCalendar.getTime();

            }
            //
            if (currentTime.before(startTime)) {

                System.out.println(" Time is Lesser ");

                valid = false;
            } else {

                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1);
                    endTime = endCalendar.getTime();

                }

                System.out.println("Comparing , Start Time /n " + startTime);
                System.out.println("Comparing , End Time /n " + endTime);
                System.out
                        .println("Comparing , Current Time /n " + currentTime);

                if (currentTime.before(endTime)) {
                    System.out.println("RESULT, Time lies b/w");
                    valid = true;
                } else {
                    valid = false;
                    System.out.println("RESULT, Time does not lies b/w");
                }

            }
            return valid;

        } else {
            throw new IllegalArgumentException(
                    "Not a valid time, expecting HH:MM:SS format");
        }

    }




}

