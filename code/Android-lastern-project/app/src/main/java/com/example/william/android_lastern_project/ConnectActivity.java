package com.example.william.android_lastern_project;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class ConnectActivity extends AppCompatActivity {

    private Button connect;
    private EditText url;
    private BulletScreen bulletScreen;
    private Socket socket;
    private ContactThread contactThread;

    private void connectToSever(String url, int port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(url, port), 5000);
            ((BulletScreen)getApplication()).outputStream = socket.getOutputStream();
            ((BulletScreen)getApplication()).bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ContactThread extends Thread {

        private String ip;
        private int port;

        public ContactThread(String ipaddr, int portNum) {
            ip = ipaddr;
            port = portNum;
        }

        @Override
        public void run() {
            BufferedReader br = ((BulletScreen)getApplication()).bufferedReader;

            try {
                String content = null;
                while ((content = br.readLine()) != null) {
                    content += '\n';
                }

            } catch (SocketTimeoutException e) {

            }   catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        bulletScreen = (BulletScreen)getApplication();

        connect = (Button)findViewById(R.id.connect);
        url = (EditText)findViewById(R.id.url);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = url.getText().toString();
                connectToSever(addr);
            }

        });

    }
}
