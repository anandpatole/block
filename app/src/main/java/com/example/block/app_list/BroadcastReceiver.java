package com.example.block.app_list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BroadcastReceiver extends android.content.BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            context.startForegroundService(new Intent(context, MyService.class));

        }
        else
        {
            context.startService(new Intent(context, MyService.class));

        }

        context.startService(new Intent(context,Block_All_Notification.class));

    }
}
