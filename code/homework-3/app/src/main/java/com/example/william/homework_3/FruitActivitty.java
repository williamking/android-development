package com.example.william.homework_3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.william.homework_3.R;

/**
 * Created by william on 15-10-31.
 */
public class FruitActivitty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_main);
        Button back = (Button)findViewById(R.id.back);
        TextView text = (TextView)findViewById(R.id.text);
        Bundle bundle = getIntent().getExtras();
        text.setText(bundle.getString("Data"));
        text.setTextColor(Color.RED);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FruitActivitty.this.finish();
            }
        });
    }
}
