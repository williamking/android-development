package com.example.williamdking.homework_4;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by William.D.King on 2015/11/13.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        remoteViews.setTextViewText(R.id.widget, intent.getStringExtra("message"));
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context.getApplicationContext(), MyWidgetProvider.class), remoteViews);
    }
}
