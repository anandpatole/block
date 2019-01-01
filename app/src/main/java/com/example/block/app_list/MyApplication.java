package com.example.block.app_list;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate () {
        super.onCreate ( );
        MyApplication.context = getApplicationContext ( );
    }

    @Override
    protected void attachBaseContext ( Context base ) {
        super.attachBaseContext ( base );
    }

    public static Context getAppContext () {
        return MyApplication.context;
    }
}