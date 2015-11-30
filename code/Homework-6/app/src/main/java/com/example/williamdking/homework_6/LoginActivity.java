package com.example.williamdking.homework_6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

    private Button regBtn;
    private Button logBtn;
    private TextView username;
    private TextView password;
    private CheckBox repass;

    private Intent intent;

    private SharedPreferences regInfo;

    private void registInfo() {
        SharedPreferences.Editor editor = regInfo.edit();
        editor.putString("user", username.getText().toString());
        editor.putString("password", password.getText().toString());

        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(this, EditFileActivity.class);

        regInfo = getSharedPreferences("data", MODE_PRIVATE);
        regBtn = (Button)findViewById(R.id.register);
        logBtn = (Button)findViewById(R.id.login);
        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        repass = (CheckBox)findViewById(R.id.repass);

        username.setText("");
        password.setText("");
        repass.setChecked(false);

        if (regInfo.getBoolean("remember", false)) {
            username.setText(regInfo.getString("user", ""));
            password.setText(regInfo.getString("password", ""));
            repass.setChecked(true);
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registInfo();
                Toast.makeText(getBaseContext(), "Register Success", Toast.LENGTH_SHORT).show();
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((username.getText().toString().compareTo(regInfo.getString("user", "")) == 0) && (password.getText().toString().compareTo(regInfo.getString("password", ""))) == 0) {
                    startActivity(intent);
                }
            }
        });

        repass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = regInfo.edit();
                editor.putBoolean("remember", isChecked);
                editor.commit();
            }
        });
    }

}
