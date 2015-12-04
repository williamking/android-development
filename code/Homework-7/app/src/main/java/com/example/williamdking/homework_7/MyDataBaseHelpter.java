package com.example.williamdking.homework_7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by William.D.King on 2015/12/4.
 */
public class MyDataBaseHelpter extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public MyDataBaseHelpter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE table" + TABLE_NAME + "(_id integer primary key autoincrement, " + "_mp text not null, _name text not null, _pnumber text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME);
        onCreate(db);
    }

    public long insert(Contact entity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_no", entity.get_no());
        values.put("_name", entity.get_name());
        values.put("_pnumber", entity.get_pnumber());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int update(Contact entity) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_no = ?";
        String[] whereArgs = {entity.get_no()};

        ContentValues values = new ContentValues();
        values.put("_no", entity.get_no());
        values.put("_name", entity.get_name());
        values.put("_pnumber", entity.get_pnumber());

        int rows = db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int delete(Contact entity) {
        SQLiteDatabase db = getWritableDatabase();
        String whereCaluse = "_no = ?";
        String[] whereArgs = {entity.get_no()};

        int rows = db.delete(TABLE_NAME, whereCaluse, whereArgs);
        db.close();
        return rows;
    }

    public Cursor query() {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

}
