package com.example.williamdking.homework_7;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Button submit = (Button)findViewById(R.id.submit_contact);

        final MyDataBaseHelpter dataBaseHelpter = new MyDataBaseHelpter(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = ((EditText)findViewById(R.id.no_info)).getText().toString();
                String name = ((EditText)findViewById(R.id.name_info)).getText().toString();
                String pnumber = ((EditText)findViewById(R.id.pnumber_info)).getText().toString();
                if (no.length() == 0 || name.length() == 0 || pnumber.length() != 11) {
                    Toast.makeText(DetailActivity.this, "Information Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("no", no);
                mBundle.putString("name", name);
                mBundle.putString("pnumber", pnumber);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

    }
}
