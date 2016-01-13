package com.example.william.android_lastern_project;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by william on 2016/1/9.
 */
public class MybroadcastReceiver extends BroadcastReceiver{
    private String url;
    private int port;

    public MybroadcastReceiver(String addr, int portNum) {
        super();
        url = addr;
        port = portNum;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        remoteViews.setTextViewText(R.id.widget, intent.getStringExtra("message"));
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context.getApplicationContext(), MyWidgetProvider.class), remoteViews);
    }
}
