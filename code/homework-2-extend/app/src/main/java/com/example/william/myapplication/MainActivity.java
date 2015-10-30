package com.example.william.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.beans.EventHandler;
import java.lang.Override;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        layout = new TableLayout(mContext);
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numbers = (TextView)findViewById(R.id.number);
        RelativeLayout root = (RelativeLayout)findViewById(R.id.root);
        mLayoutParams.addRule(RelativeLayout.BELOW, R.id.number);
        root.addView(layout, mLayoutParams);
        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                number.setText(number.getText() + ((Button)view).getText());
            }
        }

        int i, j;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = numbers.getText().toString();
                if (text.length() > 11) return;
                numbers.setText(text + ((Button)view).getText().toString());
            }
        };
        for (i = 0; i < 3; ++i) {
            TableRow row = new TableRow(mContext);
            for (j = 1; j <=3; ++j) {
                Button button = new Button(mContext);
                button.setText(i * 3 + j + "");
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(listener);
                row.addView(button);
                button.setOnClickListener(listener);
            }
            layout.addView(row);
        }
    }

    private Context mContext;
    private TableLayout layout;
    private TextView numbers;
}
