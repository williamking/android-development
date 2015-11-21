package com.example.williamdking.mymediaplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


public class MainActivity extends Activity {

    private void bindServiceConncection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    private MusicService musicService;
    private Button pp;
    private Button stop;
    private Button quit;
    private SeekBar bar;
    private MediaPlayer mp;

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
        bindServiceConncection();
        pp = (Button)findViewById(R.id.pp);
        stop = (Button)findViewById(R.id.stop);
        quit = (Button)findViewById(R.id.quit);
        bar = (SeekBar)findViewById(R.id.process);

        mp = musicService.mp;

        bar.setProgress(mp.getCurrentPosition());
        bar.setMax(mp.getDuration());

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.start();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp != null) {
                    mp.stop();
                    try {
                        mp.prepare();
                        mp.seekTo(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        });
    };

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }

}
