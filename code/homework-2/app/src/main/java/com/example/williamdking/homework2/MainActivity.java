package com.example.williamdking.homework2;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        imgbtn = (ImageButton)findViewById(R.id.state);
        btn = (Button)findViewById(R.id.reset);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = username.getText().toString();
                String pswd = password.getText().toString();
                if (uname.equals("LeiBusi") && pswd.equals("Halo3Q")) {
                    username.setVisibility(View.INVISIBLE);
                    password.setVisibility(View.INVISIBLE);
                    imgbtn.setImageDrawable(getResources().getDrawable(R.mipmap.state2));
                } else {
                    password.setText("");
                    password.setHint("密码错误");
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);
                    password.requestFocus();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setText("");
                password.setText("");
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                password.setHint("请输入密码");
                username.setFocusable(true);
                username.setFocusableInTouchMode(true);
                username.requestFocus();
                imgbtn.setImageDrawable(getResources().getDrawable(R.mipmap.state1));
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
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

    private EditText username;
    private EditText password;
    private ImageButton imgbtn;
    private Button btn;
}
