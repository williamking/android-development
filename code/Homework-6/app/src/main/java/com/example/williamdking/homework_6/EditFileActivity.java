package com.example.williamdking.homework_6;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.sql.BatchUpdateException;

public class EditFileActivity extends Activity{

    private FileUtils fileReader = new FileUtils();

    private AutoCompleteTextView fileName;

    private EditText content;

    private Button save;

    private Button read;

    private Button del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        save = (Button)findViewById(R.id.save);
        read = (Button)findViewById(R.id.read);
        del = (Button)findViewById(R.id.delete);
        content = (EditText)findViewById(R.id.fileContent);

        fileName = (AutoCompleteTextView)findViewById(R.id.fileName);
        ArrayAdapter<String> av = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, this.fileList());
        fileName.setAdapter(av);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileReader.saveContent(getBaseContext(), fileName.getText().toString(), content.getText().toString());
            }
        });
    }
}
