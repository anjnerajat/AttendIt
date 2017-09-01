package com.example.dell.attendit.Classes;

/**
 * Created by dell on 03/07/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ExtraDB extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "extraDB.db";
    public static final String EVENTS_TABLE_NAME = "extras";
    public static final String EVENTS_COLUMN_ID= "id";
    public static final String CONTACTS_COLUMN_NAME= "name";

    public ExtraDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table extras " +
                        "(id integer primary key, name text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS extras");
        onCreate(db);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_TABLE_NAME);
        return numRows;
    }

    public boolean insertExtra (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        return true;
    }
    
    public int deleteAllExtra(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EVENTS_TABLE_NAME, "1", null);
    }

    public List<Timetable> getAllExtras()
    {
        List<Timetable> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from extras ", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Timetable event = new Timetable(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            array_list.add(event);
            res.moveToNext();
        }
        return array_list;
    }
}
