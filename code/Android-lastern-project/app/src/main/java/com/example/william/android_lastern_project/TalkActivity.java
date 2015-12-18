package com.example.william.android_lastern_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class TalkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        Toast.makeText(TalkActivity.this, "聊天室连接成功", Toast.LENGTH_SHORT).show();

    }
}
