package com.example.block.app_list;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Toast.makeText(context.getApplicationContext(),"aasa",Toast.LENGTH_SHORT).show();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Toast.makeText(context.getApplicationContext(),"service is running",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

            //updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.new_app_widget);
            Intent configIntent = new Intent(context.getApplicationContext(), MyService.class);
            PendingIntent configPendingIntent;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                Toast.makeText(context.getApplicationContext(),"foregorundservice",Toast.LENGTH_SHORT).show();
                 configPendingIntent = PendingIntent.getForegroundService(context, 0, configIntent, 0);

            }
            else
            {
                Toast.makeText(context.getApplicationContext(),"normalservice",Toast.LENGTH_SHORT).show();
                 configPendingIntent = PendingIntent.getService(context, 0, configIntent, 0);

            }


            remoteViews.setOnClickPendingIntent(R.id.buttonwidget, configPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

