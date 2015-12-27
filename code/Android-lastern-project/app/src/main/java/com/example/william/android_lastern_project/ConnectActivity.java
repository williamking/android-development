package com.example.william.android_lastern_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.InetAddress;

public class ConnectActivity extends AppCompatActivity {

    private Button connect;
    private EditText url;

    private void connectToSever(String url) {
        Socket socket = null;
        try {
            socket = new Socket(url, 8080);
            String msg = "";

            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            Thread.sleep(50, 0);

            dout.writeBytes(msg);
            dout.flush();

            byte[] buffer = new byte[din.available()];
            din.read(buffer);
            String response = new String(buffer);
            if (response.equals("Success")) {
                Toast.makeText(ConnectActivity.this, "Connect Success", Toast.LENGTH_SHORT).show();
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

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
