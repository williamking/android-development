package com.example.williamdking.mymediaplayer;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.logging.Handler;


public class MainActivity extends Activity {

    private void bindServiceConncection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    private MusicService musicService;
    private TextView timeView;
    private Button pp;
    private Button stop;
    private Button quit;
    private SeekBar bar;
    private TextView state;
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");

    private android.os.Handler handler = new android.os.Handler();

    private Runnable myThread;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = (TextView)findViewById(R.id.time);
        pp = (Button)findViewById(R.id.pp);
        stop = (Button)findViewById(R.id.stop);
        quit = (Button)findViewById(R.id.quit);
        bar = (SeekBar)findViewById(R.id.process);
        state = (TextView)findViewById(R.id.state);
        bindServiceConncection();

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicService.mp.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.mp.isPlaying()) {
                    musicService.mp.pause();
                    state.setText("Pausing");
                } else {
                    musicService.mp.start();
                    state.setText("Playing");
                    bar.setProgress(musicService.mp.getCurrentPosition());
                    bar.setMax(musicService.mp.getDuration());
                }
                handler.post(myThread);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.mp != null) {
                    //musicService.stop();
                    musicService.mp.pause();
                    musicService.mp.seekTo(0);
                    bar.setProgress(0   );
                    state.setText("");
                    timeView.setText("");
                    handler.removeCallbacks(myThread);
                }
            }

            ;
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(myThread);
                unbindService(sc);
                try {
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        myThread = new Runnable() {
            public void run() {
                timeView.setText(time.format(musicService.mp.getCurrentPosition()) + "/" + time.format(musicService.mp.getDuration()));
                bar.setProgress(musicService.mp.getCurrentPosition());

                handler.postDelayed(myThread, 100);
            }
        };
    };


}
