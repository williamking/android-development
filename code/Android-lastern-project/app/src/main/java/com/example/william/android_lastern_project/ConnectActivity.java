package com.example.william.android_lastern_project;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private Button connect;
    private AutoCompleteTextView url;
    private AutoCompleteTextView port;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> urlList;
    private ArrayList<String> portList;

    private void loadURLList(String listString) throws StreamCorruptedException, IOException, ClassNotFoundException{
        if (listString.length() == 0) {
            urlList = new ArrayList<String>();
            return;
        }
        byte[] bytes = Base64.decode(listString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        urlList = (ArrayList<String>)objectInputStream.readObject();
        objectInputStream.close();
    }

    private void loadPortList(String portsString) throws StreamCorruptedException, IOException, ClassNotFoundException{
        if (portsString.length() == 0) {
            portList = new ArrayList<String>();
            return;
        }
        byte[] bytes = Base64.decode(portsString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        portList = (ArrayList<String>)objectInputStream.readObject();
        objectInputStream.close();
    }

    private void saveList() throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(urlList);
        String urlString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        objectOutputStream.close();
        byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(portList);
        String portString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        objectOutputStream.close();
        editor.putString("url", urlString);
        editor.putString("port", portString);
        editor.commit();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        connect = (Button)findViewById(R.id.connect);
        url = (AutoCompleteTextView)findViewById(R.id.url);
        port = (AutoCompleteTextView)findViewById(R.id.port);

        try {
            loadURLList(sharedPreferences.getString("url", ""));
            loadPortList(sharedPreferences.getString("port", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> av1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, urlList);
        url.setAdapter(av1);
        ArrayAdapter<String> av2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, portList);
        port.setAdapter(av2);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, TalkActivity.class);
                String addr = url.getText().toString();
                if (!urlList.contains(addr))
                    urlList.add(addr);
                if (!portList.contains(port.getText().toString()))
                    portList.add(port.getText().toString());
                intent.putExtra("url", addr);
                intent.putExtra("port", Integer.parseInt(port.getText().toString()));
                try {
                    saveList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }

        });

    }
}
