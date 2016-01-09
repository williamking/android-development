package com.example.william.android_lastern_project;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        connect = (Button)findViewById(R.id.connect);
        url = (EditText)findViewById(R.id.url);
        port = (EditText)findViewById(R.id.port);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, TalkActivity.class);
                String addr = url.getText().toString();
                intent.putExtra("url", addr);
                intent.putExtra("port", Integer.parseInt(port.getText().toString()));
                startActivity(intent);
            }

        });

    }
}
