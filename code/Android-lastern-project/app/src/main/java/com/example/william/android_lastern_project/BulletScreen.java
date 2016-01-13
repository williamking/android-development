package com.example.william.android_lastern_project;

import android.app.Application;
import android.app.ExpandableListActivity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by william on 16-1-4.
 */
public class BulletScreen extends Service {

    public BufferedReader bufferedReader;
    public PrintWriter printer;
    public Thread listeningThread;

    private Socket socket;
    private String url;
    private int port;

    public ArrayAdapter<String> listAdapter = null;
    public android.os.Handler handler = null;

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        BulletScreen getBulletScreen() {
            return BulletScreen.this;
        }
    }

    public BulletScreen() {
        printer = null;
        bufferedReader = null;
        listeningThread = null;
    }

    public void sendBullet(String text) {
        if (printer != null) {
            try {
                String content = "{\"dm\":" + '\"' + text + "\"}\n";
                printer.print(content);
                printer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                socket = new Socket(url, port);
                printer = new PrintWriter(socket.getOutputStream(), true);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String content;
            try {
                while (true) {
                    //Log.e("thread", "runnning");
                    if ((content = bufferedReader.readLine()) != null) {
                        try {
                            JSONTokener jsonParser = new JSONTokener(content);
                            JSONObject obj = (JSONObject) jsonParser.nextValue();
                            if (obj.has("Name") && obj.getString("Name") != null) {
                                if (handler != null) {
                                    Message msg = new Message();
                                    msg.what = TalkActivity.CONNECTED;
                                    msg.obj = obj;
                                    handler.sendMessage(msg);
                                }
                            } else {
                                if (obj.has("dm") && obj.getString("dm") != null) {
                                    Message msg  = new Message();
                                    msg.what = TalkActivity.UPDATE;
                                    msg.obj = obj;
                                    handler.sendMessage(msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        url = intent.getExtras().getString("url");
        port = intent.getExtras().getInt("port");

        listeningThread = new Thread(mRunnable);
        listeningThread.start();

        return binder;
    }

}