package com.example.william.android_lastern_project;

import android.app.Application;

import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * Created by william on 16-1-4.
 */
public class BulletScreen extends Application {

    public OutputStream outputStream;
    public BufferedReader bufferedReader;
    public ConnectActivity.ContactThread listeningThread;

    BulletScreen() {
        outputStream = null;
        bufferedReader = null;
        listeningThread = null;
    }

}