package com.example.williamdking.mymediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    public static MediaPlayer mp = new MediaPlayer();

    public MusicService() {
        try {
            mp.setDataSource("/data/Wonderful Rush.mp3");
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public final IBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public void stop() {
        try {
            mp.stop();
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.seekTo(0);
        mp.getDuration();
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }
}
