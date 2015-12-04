package com.example.williamdking.homework_7;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private ListView contactList;
    private MySimpleAdapater contactAdapter;
    private MyDataBaseHelpter dataBaseHelpter;


    private class MySimpleAdapater extends SimpleAdapter {
        private ArrayList<Map<String, String>> mData;

        public MySimpleAdapater(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.mData = (ArrayList<Map<String, String>>) data;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            final int mPosition = position;
            return super.getView(position, convertView, parent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

        //contactAdapter = new MySimpleAdapater(MainActivity.this, );

        contactList = new ListView(this);

    }

}
