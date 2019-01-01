package com.example.block.app_list;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

class CommonUtils {

    static String CountryID = "";
    static String CountryZipCode = "";

    /*
    Method to get Country Id and Zip code
     */
    static void getCountryZipCode() {

        TelephonyManager manager = (TelephonyManager) MyApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = Objects.requireNonNull(manager).getSimCountryIso().toUpperCase();
        String[] rl = MyApplication.getAppContext().getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                Log.e("CountryID : ZipCode", CountryID + " : " + CountryZipCode);
                break;
            }
        }
    }

    static void disableDrawerIcon(String component) {
        try {
            PackageManager pm = MyApplication.getAppContext().getApplicationContext()
                    .getPackageManager();

            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
            Collections
                    .sort(appList, new ResolveInfo.DisplayNameComparator(pm));
            ComponentName componentName = null;

            //for (ResolveInfo temp : appList) {
            for (int i=0; i<appList.size(); i++) {
                if (appList.get(i).activityInfo.packageName.equals(component)) {

                    componentName = new ComponentName(component,
                            appList.get(i).activityInfo.name);
                }

            }

            if (componentName != null) {
                pm.setComponentEnabledSetting(componentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                Log.i("Application", "Icon disabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void enableDrawerIcon(String component) {
        try {
            PackageManager pm = MyApplication.getAppContext().getApplicationContext()
                    .getPackageManager();

            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
            Collections
                    .sort(appList, new ResolveInfo.DisplayNameComparator(pm));
            ComponentName componentName = null;

            for (ResolveInfo temp : appList) {

                if (temp.activityInfo.packageName.equals(component)) {

                    componentName = new ComponentName(component,
                            temp.activityInfo.name);
                }

            }

            if (componentName != null) {

            }
            pm.setComponentEnabledSetting(componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            Log.i("Application", "Icon enabled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
