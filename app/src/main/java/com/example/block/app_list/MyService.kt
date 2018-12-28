package com.example.block.app_list

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import java.util.concurrent.TimeUnit

import android.app.ActivityManager.RunningAppProcessInfo

import android.app.ActivityManager

import java.util.*

import kotlin.collections.ArrayList
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

import com.rvalerio.fgchecker.AppChecker
import com.example.block.app_list.MainActivity
import android.content.ComponentName



class MyService : Service() {


    private var v: Vibrator? = null

    internal lateinit var notification: NotificationCompat.Builder
    override fun onCreate() {
        super.onCreate()

        notification = NotificationCompat.Builder(this)
        notification.setAutoCancel(true)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        var sharedPreferences = getSharedPreferences("Timer", Context.MODE_PRIVATE)
        var dur = sharedPreferences.getInt("duration", 0)
        startService(Intent(this@MyService, Block_All_Notification::class.java))
        var list =MainActivity.list


        var  listchecker = ArrayList<AppChecker>()

        for (i in list.indices) {

            var appChecker = AppChecker()


            appChecker.whenAny { process ->
                if (process.equals(list[i], ignoreCase = true)) {

                    var  currentTasks = java.util.ArrayList<String>()

                    val prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE)
                    try {
                        currentTasks = ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(java.util.ArrayList<String>()))) as java.util.ArrayList<String>
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if(!currentTasks.contains(process))
                    {

                        appChecker.stop();
                    }
                    else {

                        val startMain = Intent(Intent.ACTION_MAIN)
                        startMain.addCategory(Intent.CATEGORY_HOME)
                        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(startMain)
                        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                        activityManager.killBackgroundProcesses(list[i])
                    }
                    //  activityManager.restartPackage("com.whatsapp")



                }

            }

                    .timeout(1000)
                    .start(this)

            listchecker.add(appChecker)
        }

        var newAdded =AppsAdapter.newChecked
        if(newAdded.size>0)
        {
            for (i in newAdded.indices) {
                startTimer(newAdded[i], dur.toLong())
            }
        }
        else {
            for (i in MainActivity.list.indices) {
                startTimer(list[i], dur.toLong())
            }
        }
//            var countDownTimer = object : CountDownTimer(dur.toLong(), 1000) {
//                override fun onTick(dur: Long) {
//                    var millis = dur
//                    var hms = String.format("%02d:%02d:%02d",
//
//                            TimeUnit.MILLISECONDS.toHours(millis),
//                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//
//                    )//seconds
//
//                     startForeground(uniqueID, buildForegroundNotification(hms))
//                }
//
//                override fun onFinish() {
//                    stopService(Intent(this@MyService, Block_All_Notification::class.java))
//                    for (i in listchecker.indices) {
//                        listchecker[i].stop()
//                    }
//                    val preferences = getSharedPreferences("packagePref", Context.MODE_PRIVATE)
//                    val editor = preferences.edit()
//                    editor.clear()
//                    editor.commit()
//                    endnotif()
//
//                }
//            }
//            countDownTimer.start()

        ////////////////////



        return Service.START_STICKY
    }
    fun startTimer(name :String ,dur : Long)
    {

            var countDownTimer = object : CountDownTimer(dur.toLong(), 1000) {
                override fun onTick(dur: Long) {
                    var millis = dur
                    var hms = String.format("%02d:%02d:%02d",

                            TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))


                    )//seconds
//                    val p = getPackageManager()
//                    val launchIntent = p.getLaunchIntentForPackage(name)
//                    val className = launchIntent.getComponent()!!.getClassName()
//
//                    val componentName = ComponentName(this@MyService, className!!)
//                    p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  PackageManager.DONT_KILL_APP);

                    //startForeground(uniqueID, buildForegroundNotification(hms))
                }

                override fun onFinish() {
                    //stopService(Intent(this@MyServiceNew, Block_All_Notification::class.java))


                    var  currentTasks = java.util.ArrayList<String>()
                     var timelist: java.util.ArrayList<HashMap<String, String>>
                    val prefs = getSharedPreferences("packagePref", Context.MODE_PRIVATE)
                    try {
                        currentTasks = ObjectSerializer.deserialize(prefs.getString("package", ObjectSerializer.serialize(java.util.ArrayList<String>()))) as java.util.ArrayList<String>
                        timelist = ObjectSerializer.deserialize(prefs.getString("time", ObjectSerializer.serialize(java.util.ArrayList<HashMap<String, String>>()))) as java.util.ArrayList<HashMap<String, String>>
                        if(currentTasks.contains(name))
                        {
//                            val p = getPackageManager()
//                            val launchIntent = p.getLaunchIntentForPackage(name)
//                            val className = launchIntent.getComponent()!!.getClassName()
//                            val componentName = ComponentName(this@MyService, className!!)
//                            p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                            for (a in timelist) {
                                a.containsKey(name)
                                timelist.remove(a)

                            }
                            currentTasks.remove(name)
                            val editor = prefs.edit()
                            editor.putString("package", ObjectSerializer.serialize(currentTasks))
                            editor.putString("time",ObjectSerializer.serialize(timelist))
                            editor.commit();
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            }
            countDownTimer.start()

    }

//    fun getActivePackagesCompat(): Array<String> {
//        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val taskInfo = activityManager.getRunningTasks(1)
//        val componentName = taskInfo.get(0).topActivity
//        val activePackages = arrayOfNulls<String>(1)
//        activePackages[0] = componentName.getPackageName()
//        return activePackages
//    }

    fun getActivePackages(): Array<String> {
        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var activePackages = HashSet<String>()
        var processInfos = activityManager.getRunningAppProcesses()
        for (processInfo in processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(processInfo.pkgList)
            }
        }
        return activePackages.toTypedArray()
    }

    override fun onDestroy() {
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    fun endnotif() {
        notification.setSmallIcon(R.drawable.openlock)
        notification.setContentText("00:00")
        notification.setTicker("apps are now unblocked!")
        notification.setWhen(System.currentTimeMillis())
        notification.setContentTitle("You survived!")
        notification.setContentText("Apps are now unblocked!")
        ClickNotif()

    }

    //other parts of  notif
    fun ClickNotif() {
        var intent1 = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        notification.setContentIntent(pendingIntent)

        var nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(uniqueID, notification.build())
        onDestroy()
    }

    private fun buildForegroundNotification(hms: String): Notification {


        var channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    var b = NotificationCompat.Builder(this)
                    b.setOngoing(true)
                    b.setContentTitle("Be Productive!")
                            .setContentText(hms)
                            .setSmallIcon(R.drawable.closedlock)
                            .setTicker("Apps are now Blocked")
                    return b.build()
                }

        var notificationBuilder = NotificationCompat.Builder(this, channelId )
        var notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.closedlock)
                .setTicker("Apps are now Blocked")
                .setContentText(hms)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        return notification
        // startForeground(101, notification)
    }


    companion object {
        private var uniqueID = 71399
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        var chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        var service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun getForegroundApp(): RunningAppProcessInfo? {
        var result: RunningAppProcessInfo? = null
        var info: RunningAppProcessInfo? = null

        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var l = activityManager.runningAppProcesses
        var i = l.iterator()
        while (i.hasNext()) {
            info = i.next()
            if (info!!.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && !isRunningService(info.processName)) {
                result = info
                break
            }
        }
        return result
    }

    private fun isRunningService(processName: String?): Boolean {
        if (processName == null)
            return false

        var service: ActivityManager.RunningServiceInfo

        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var l = activityManager.getRunningServices(9999)
        var i = l.iterator()
        while (i.hasNext()) {
            service = i.next()
            if (service.process.equals(processName))
                return true
        }
        return false
    }

    private fun isAppOnForeground(processName: String): Boolean {
        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == processName) {
                return true
            }
        }
        return false
    }

    private fun isRunningApp(processName: String?): Boolean {
        if (processName == null)
            return false

        var app: RunningAppProcessInfo

        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var l = activityManager.runningAppProcesses
        var i = l.iterator()
        while (i.hasNext()) {
            app = i.next()
            if (app.processName == processName && app.importance != RunningAppProcessInfo.IMPORTANCE_SERVICE)
                return true
        }
        return false
    }


    private fun checkifThisIsActive(target: RunningAppProcessInfo?): Boolean {
        var result = false
        var info: ActivityManager.RunningTaskInfo

        if (target == null)
            return false

        var activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var l = activityManager.getRunningTasks(9999)
        var i = l.iterator()

        while (i.hasNext()) {
            info = i.next()
            if (info.baseActivity.packageName == target.processName) {
                result = true
                break
            }
        }

        return result
    }


    // what is in b that is not in a ?
    fun subtractSets(a: Collection<*>, b: Collection<*>): Collection<*> {
        var result = ArrayList(b)
        result.removeAll(a)
        return result
    }

    private fun killAppBypackage(packageTokill: String) {

        var packages: List<ApplicationInfo>
        var pm: PackageManager
        pm = packageManager
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0)


        var mActivityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var myPackage = applicationContext.packageName

        for (packageInfo in packages) {

            if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                continue
            }
            if (packageInfo.packageName == myPackage) {
                continue
            }
            if (packageInfo.packageName == packageTokill) {

                mActivityManager.killBackgroundProcesses(packageInfo.packageName)
                mActivityManager.restartPackage(packageInfo.packageName)


            }

        }

    }
}


