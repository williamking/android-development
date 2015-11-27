package com.example.williamdking.homework_6;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Created by William.D.King on 2015/11/27.
 */
public class FileUtils {
    public void saveContent(Context context, String filename, String fileText) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            fos.write(fileText.getBytes());
            fos.close();

            Toast.makeText(context, "Save Content Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Save Content Failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String getContent(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] contents = new byte[fis.available()];

            fis.read(contents);
            fis.close();

            Toast.makeText(context, "Read Content Success", Toast.LENGTH_SHORT).show();

            return new String(contents);
        } catch (IOException e) {
            Toast.makeText(context, "Read Content Failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return new String("");
        }
    }

    public void deleteFile(Context context, String fileName) {
        context.deleteFile(fileName);
        Toast.makeText(context, "Delete File Success", Toast.LENGTH_SHORT).show();
    }
}