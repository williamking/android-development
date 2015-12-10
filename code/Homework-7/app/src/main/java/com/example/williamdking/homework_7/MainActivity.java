package com.example.williamdking.homework_7;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private ListView contactList;
    private MySimpleAdapater contactAdapter;
    private MyDataBaseHelpter dataBaseHelpter;
    private  Cursor cursor;
    private ArrayList<Map<String, String>> datalist;

    private class DeleteDialog extends AlertDialog {
        private int id;

        public DeleteDialog(Context context, int id) {
            super(context);
            this.id = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_delete);
            setTitle("Delete");
            Button delBtn = (Button)findViewById(R.id.delete);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater)(getApplicationContext()).getSystemService(LAYOUT_INFLATER_SERVICE);
                    Object item = contactList.getItemAtPosition(id);
                    String no = ((HashMap)item).get("no").toString();
                    String name = ((HashMap)item).get("name").toString();
                    String pnumber = ((HashMap)item).get("pNumber").toString();
                    Contact contact = new Contact(no, name, pnumber);
                    dataBaseHelpter.delete(contact);
                    datalist.remove(id);
                    contactAdapter.notifyDataSetChanged();
                    DeleteDialog.this.dismiss();
                    Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class EditDialog extends AlertDialog {
        private int id;
        private String no;
        private String name;
        private String pnumber;

        public EditDialog(Context context, int id, String no, String name, String pnumber) {
            super(context);
            this.id = id;
            this.no = no;
            this.name = name;
            this.pnumber = pnumber;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_edit);
            setTitle("Delete");
            Button delBtn = (Button)findViewById(R.id.edit_contact);
            ((EditText) findViewById(R.id.edit_name)).setText(name);
            ((EditText) findViewById(R.id.edit_pnumber)).setText(pnumber);
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) (getApplicationContext()).getSystemService(LAYOUT_INFLATER_SERVICE);
                    String name_info = ((EditText) findViewById(R.id.edit_name)).getText().toString();
                    String pnumber_info = ((EditText) findViewById(R.id.edit_pnumber)).getText().toString();
                    if (name_info.length() < 4) {
                        Toast.makeText(MainActivity.this, "The length of name should be at least 4", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pnumber_info.length() != 11) {
                        Toast.makeText(MainActivity.this, "The length of name should be 11", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Contact contact = new Contact(no, name_info, pnumber_info);
                    dataBaseHelpter.update(contact);
                    datalist.clear();
                    setData(datalist);
                    contactAdapter.notifyDataSetChanged();
                    EditDialog.this.dismiss();
                    Toast.makeText(MainActivity.this, "Edit Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private DeleteDialog deleteDialog;
    private EditDialog editDialog;

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

        dataBaseHelpter = new MyDataBaseHelpter(this);
        cursor = dataBaseHelpter.query();

        datalist = new ArrayList<Map<String, String>>();
        setData(datalist);

        contactAdapter = new MySimpleAdapater(MainActivity.this, datalist, R.layout.contact, new String[] {"no", "name", "pNumber"}, new int[] {R.id.no, R.id.name, R.id.pnumber});

        contactList = new ListView(this);
        contactList.setAdapter(contactAdapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteDialog = new DeleteDialog(MainActivity.this, i);
                deleteDialog.show();
            }
        });
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editDialog = new EditDialog(MainActivity.this, position, ((TextView)view.findViewById(R.id.no)).getText().toString(),
                        ((TextView)view.findViewById(R.id.name)).getText().toString(), ((TextView)view.findViewById(R.id.pnumber)).getText().toString());
                editDialog.show();
                return true;
            }
        });


        Button addList = (Button)findViewById(R.id.add_contact);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout main = (LinearLayout)findViewById(R.id.main);
        main.addView(contactList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String no = bundle.getString("no");
            String name = bundle.getString("name");
            String pnumber = bundle.getString("pnumber");
            Contact contact = new Contact(no, name, pnumber);
            dataBaseHelpter.insert(contact);

//            ArrayList<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
            setData(datalist);
//            contactAdapter = new MySimpleAdapater(MainActivity.this, datalist, R.layout.contact, new String[] {"no", "name", "pNumber"}, new int[] {R.id.no, R.id.name, R.id.pnumber});
//            contactList.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();

            Toast.makeText(MainActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(List<Map<String, String>> mDataList) {
        Map<String, String> data;
        Cursor c = dataBaseHelpter.query();
        while (c.moveToNext()) {
            data = new HashMap<String, String>();
            data.put("no", c.getString(c.getColumnIndex("_no")));
            data.put("name", c.getString(c.getColumnIndex("_name")));
            data.put("pNumber", c.getString(c.getColumnIndex("_pnumber")));
            mDataList.add(data);
        }
    }

}
