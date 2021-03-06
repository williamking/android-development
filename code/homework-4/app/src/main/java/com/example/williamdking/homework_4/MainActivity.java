package com.example.williamdking.homework_4;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private MyBroadcastReceiver receiver;
    protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new MyBroadcastReceiver();
        final Button regist = (Button)findViewById(R.id.register);
        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regist.getText().toString().compareTo("Unregister Broadcast") != 0) return;
                Intent intent = new Intent("SYSU_ANDROID_2015");
                EditText message = (EditText)findViewById(R.id.message);
                intent.putExtra("message", message.getText().toString());
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if (b.getText().toString().compareTo("Register Broadcast") == 0) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(ACTION);
                    registerReceiver(receiver, filter);
                    b.setText("Unregister Broadcast");
                } else {
                    unregisterReceiver(receiver);
                    b.setText("Register Broadcast");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
