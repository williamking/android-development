package com.example.william.android_lastern_project;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TalkActivity extends AppCompatActivity {

    private Button submit;
    private EditText bulletText;
    private ServiceConnection sc;
    private BulletScreen bulletScreen;
    private ProgressDialog dialog;
    private TextView titleView;
    private TextView remainTime;
    private ListView bulletLists;
    private MybroadcastReceiver receiver;
    protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";

    private int remainSeconds = -1;
    private ArrayList<String> bullets = new ArrayList<String>();


    public static final int CONNECTED = 0;
    public static final int UPDATE = 1;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTED:
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        String title = obj.getString("Name");
                        titleView.setText(title);
                        String time = obj.getString("Time");
                        remainSeconds = Integer.parseInt(time);
                        if (remainSeconds > 0) {
                            remainTime.setText("距离弹幕吐槽开始还有 " + remainSeconds + " 秒");
                        } else {
                            remainTime.setText("吐槽已经开始，尽情弹幕轰炸吧！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                    Toast.makeText(TalkActivity.this, "已到达弹幕区战场", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("BULLET START");
                    intent.putExtra("message", "距离弹幕吐槽开始还有 " + remainSeconds + " 秒");
                    intent.setAction(ACTION);
                    sendBroadcast(intent);
                    break;
                case UPDATE:
                    JSONObject obj2 = (JSONObject)msg.obj;
                    try {
                        String content = obj2.getString("dm");
                        bullets.add(content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void connectToSever(String url, int port) {
        Intent intent = new Intent(TalkActivity.this, BulletScreen.class);
        intent.putExtra("url", url);
        intent.putExtra("port", port);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    private void getToProgressing() {
        if (dialog == null) dialog = new ProgressDialog(TalkActivity.this);
        dialog.setTitle("Connecting");
        dialog.setMessage("connecting....");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    private void fillBullets() {
        bulletLists.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, bullets));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        submit = (Button)findViewById(R.id.submit);
        bulletText = (EditText)findViewById(R.id.bullet_text);
        titleView = (TextView)findViewById(R.id.title);
        bulletLists = (ListView)findViewById(R.id.lists);
        remainTime = (TextView)findViewById(R.id.time_to_start);

        String url = getIntent().getExtras().getString("url");
        int port = getIntent().getExtras().getInt("port");

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bulletScreen = ((BulletScreen.MyBinder)service).getBulletScreen();
                bulletScreen.handler = handler;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bulletScreen = null;
            }
        };

        connectToSever(url, port);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = bulletText.getText().toString();
                bulletScreen.sendBullet(text);
            }
        });

    }
}
