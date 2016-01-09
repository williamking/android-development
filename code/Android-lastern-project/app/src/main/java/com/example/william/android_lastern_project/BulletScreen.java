package com.example.william.android_lastern_project;

import android.app.Application;
import android.app.ExpandableListActivity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

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
    public ContactThread listeningThread;

    private Socket socket;
    private String url;
    private int port;

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
                String content = "{dm:" + '\"' + text + "\"}";
                printer.print(content);
                printer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ContactThread extends Thread {

        private String ip;
        private int port;

        @Override
        public void run() {
            String content = "";
            try {
                while ((content = bufferedReader.readLine()) != null) {
                    content += '\n';
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }   catch (IOException e) {
                e.printStackTrace();
            }

            if (content != "") {
                try {
                    JSONTokener jsonParser = new JSONTokener(content);
                    JSONObject obj = (JSONObject)jsonParser.nextValue();
                    if (obj.getString("Name") != null) {
                        if (handler != null) {
                            Message msg = new Message();
                            msg.what = TalkActivity.CONNECTED;
                            msg.obj = obj;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        url = intent.getExtras().getString("url");
        port = intent.getExtras().getInt("port");

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(url, port), 5000);
            printer = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            listeningThread = new ContactThread();
            listeningThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return binder;
    }

}