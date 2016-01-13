package com.example.william.android_lastern_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TalkActivity extends AppCompatActivity {

    private Button submit;
    private EditText bulletText;
    private ServiceConnection sc;
    private BulletScreen bulletScreen;
    private ProgressDialog dialog;
    private TextView titleView;
    private TextView remainTime;
    private ListViewForScrollView bulletLists;
    private MybroadcastReceiver receiver;
    private ScrollView scroll;
    private Button shake;
    private int shakeMode = 0;
    private CountDownTimer timer;
    private boolean started = false;

    protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    private String[] danmakuList = new String[]{"楼上没菊花", "这里的弹幕被我占领了!", "怎么可以让你们看到我老婆的脸!", "楼下的都是我儿子"};

    private static final int SENSOR_SHAKE = 10;
    private SensorManager sensorManager;
    private Vibrator vibrator;

    public class ListViewForScrollView extends ListView {
        public ListViewForScrollView(Context context) {
            super(context);
        }
        public ListViewForScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expendSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            if (expendSpec > 310) expendSpec = 310;
            super.onMeasure(widthMeasureSpec, expendSpec);
        }
    }

    private int remainSeconds = -1;
    private ArrayList<String> bullets = new ArrayList<String>();


    public static final int CONNECTED = 0;
    public static final int UPDATE = 1;
    public static final int TIME = 2;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTED:
                    remainSeconds = 0;
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        String title = obj.getString("Name");
                        titleView.setText(title);
                        String time = obj.getString("Time");
                        remainSeconds = Integer.parseInt(time);
                        if (remainSeconds > 0) {
                            timer = new CountDownTimer(remainSeconds * 1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    Message message = new Message();
                                    message.what = TIME;
                                    message.obj = millisUntilFinished;
                                    handler.sendMessage(message);
                                }

                                @Override
                                public void onFinish() {
                                    Message message = new Message();
                                    message.what = TIME;
                                    message.obj = 0;
                                    handler.sendMessage(message);
                                }
                            };
                            timer.start();
                        } else {
                            remainTime.setText("吐槽已经开始，尽情弹幕轰炸吧！");
                            started = true;
                            Intent intent = new Intent("BULLET START");
                            intent.putExtra("message", "吐槽\"" + title + "\"已经开始，尽情弹幕轰炸吧！");
                            intent.setAction(ACTION);
                            sendBroadcast(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                    Toast.makeText(TalkActivity.this, "已到达弹幕区战场", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE:
                    JSONObject obj2 = (JSONObject)msg.obj;
                    try {
                        String content = obj2.getString("dm");
                        Log.e("hah", "add a damaku");
                        bullets.add(content);
                        ((ArrayAdapter<String>) bulletLists.getAdapter()).notifyDataSetChanged();
                        scrollToBottom(scroll, bulletLists);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case TIME:
                    long remainSeconds = (long)msg.obj;
                    String title = titleView.getText().toString();
                    if (remainSeconds > 0) {
                        remainTime.setText("距离弹幕吐槽开始还有 " + (remainSeconds / 1000) + " 秒");
                        updateBroadcast(title, remainSeconds / 1000);
                    } else {
                        remainTime.setText("吐槽已经开始，尽情弹幕轰炸吧！");
                        Intent intent = new Intent("BULLET START");
                        intent.putExtra("message", "吐槽\"" + title + "\"已经开始，尽情弹幕轰炸吧！");
                        intent.setAction(ACTION);
                        sendBroadcast(intent);
                        started = true;
                    }
                default:
                    break;
            }
        }
    };

    public void scrollToBottom(final View scroll, final View inner) {
        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    private void updateBroadcast(String title, long remainSeconds) {
        Intent intent = new Intent("BULLET START");
        intent.putExtra("message", "距离弹幕吐槽\"" + title + "\"开始还有 " + remainSeconds + " 秒");
        intent.setAction(ACTION);
        sendBroadcast(intent);
    }

    private void registBroadcast() {
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION);
            registerReceiver(receiver, filter);
        }
    }

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

    private void startSensor() {
        if (sensorManager != null && shakeMode == 0) {
            shakeMode = 1;
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void stopSensor() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
            shakeMode = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_talk);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        submit = (Button)findViewById(R.id.submit);
        bulletText = (EditText)findViewById(R.id.bullet_text);
        titleView = (TextView)findViewById(R.id.title);
        //bulletLists = (ListView)findViewById(R.id.lists);
        bulletLists = new ListViewForScrollView(this);
        scroll = (ScrollView)findViewById(R.id.scroll);
        shake = (Button)findViewById(R.id.shake);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        remainTime = (TextView)findViewById(R.id.time_to_start);

        //初始化感应器

        String url = getIntent().getExtras().getString("url");
        int port = getIntent().getExtras().getInt("port");

        receiver = new MybroadcastReceiver(url, port);

        registBroadcast();

        fillBullets();
        scroll.addView(bulletLists);

        getToProgressing();

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bulletScreen = ((BulletScreen.MyBinder)service).getBulletScreen();
                bulletScreen.handler = handler;
                bulletScreen.listAdapter = (ArrayAdapter<String>) bulletLists.getAdapter();
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
                if (!started) {
                    Toast.makeText(TalkActivity.this, "弹幕事件还未开始，耐心等等吧!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String text = bulletText.getText().toString();
                bulletText.setText("");
                bulletScreen.sendBullet(text);
            }
        });

        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shakeMode == 0) {
                    new AlertDialog.Builder(TalkActivity.this).setTitle("通知").setMessage("进入摇一摇状态后，你可以通过摇动手机随机发送弹幕^_^").
                            setPositiveButton("知道了", null).show();
                    startSensor();
                    shake.setText("退出摇一摇");
                } else {
                    new AlertDialog.Builder(TalkActivity.this).setTitle("通知").setMessage("已退出摇一摇状态").
                            setPositiveButton("知道了", null).show();
                    stopSensor();
                    shake.setText("进入摇一摇状态");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shakeMode == 1 && sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            shake.setText("退出摇一摇");
        }
        if (shakeMode == 0) {
            shake.setText("进入摇一摇状态");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
        unregisterReceiver(receiver);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            Log.i("Vibrate", "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            int mediumValue = 19;
            if (Math.abs(x) > mediumValue || Math.abs(y) > mediumValue || Math.abs(z) > mediumValue) {
                vibrator.vibrate(200);
                sendRandomDanmaku();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void sendRandomDanmaku() {
        if (bulletScreen == null) return;
        Random random = new Random();
        int index = random.nextInt(danmakuList.length - 1);
        bulletScreen.sendBullet(danmakuList[index]);
        new AlertDialog.Builder(TalkActivity.this).setTitle("摇一摇发送弹幕").setMessage("经过摇一摇，您刚才发送了弹幕\"" + danmakuList[index] + "\"!")
                .setPositiveButton("知道了", null).show();
    }

}
