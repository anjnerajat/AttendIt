package com.example.dell.attendit.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TimetableDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "timetableDB.db";
    public static final String EVENTS_TABLE_NAME = "timetable";
    public static final String EVENTS_COLUMN_ID= "id";
    public static final String CONTACTS_COLUMN_NAME= "name";
    public static final String CONTACTS_COLUMN_MON = "monday";
    public static final String CONTACTS_COLUMN_TUE = "tuesday";
    public static final String CONTACTS_COLUMN_WED = "wednesday";
    public static final String CONTACTS_COLUMN_THU = "thursday";
    public static final String CONTACTS_COLUMN_FRI = "friday";
    public static final String CONTACTS_COLUMN_SAT = "saturday";
    public static final String CONTACTS_COLUMN_SUN = "sunday";

    public TimetableDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table timetable " +
                        "(id integer primary key, name text,monday integer,tuesday integer, wednesday integer, thursday integer, friday integer, saturday integer, sunday integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS timetable");
        onCreate(db);
    }

    public boolean insertTimetable (String name,int monday,int tuesday,int wednesday,int thursday,int friday,int saturday,int sunday) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, monday);
        contentValues.put(CONTACTS_COLUMN_TUE, tuesday);
        contentValues.put(CONTACTS_COLUMN_WED, wednesday);
        contentValues.put(CONTACTS_COLUMN_THU, thursday);
        contentValues.put(CONTACTS_COLUMN_FRI, friday);
        contentValues.put(CONTACTS_COLUMN_SAT, saturday);
        contentValues.put(CONTACTS_COLUMN_SUN, sunday);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertMonday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 1);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;

    }

    public boolean insertTuesday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 1);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertWednesday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 1);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertThursday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 1);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertFriday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 1);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertSaturday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 1);
        contentValues.put(CONTACTS_COLUMN_SUN, 0);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean insertSunday(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_MON, 0);
        contentValues.put(CONTACTS_COLUMN_TUE, 0);
        contentValues.put(CONTACTS_COLUMN_WED, 0);
        contentValues.put(CONTACTS_COLUMN_THU, 0);
        contentValues.put(CONTACTS_COLUMN_FRI, 0);
        contentValues.put(CONTACTS_COLUMN_SAT, 0);
        contentValues.put(CONTACTS_COLUMN_SUN, 1);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from timetable where id="+id+"", null );
        db.close();
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_TABLE_NAME);
        db.close();
        return numRows;
    }

    public Integer deleteTimetable (String name, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("timetable", "name = ? AND " + day + " = 1", new String[] { name });
    }

    public Integer deleteTimetableOverall (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("timetable", "name = ?", new String[] { name });
    }

   // public int editTimetable(String name, String updatedName, int updatedMinimum/*,int credit,int hours,int total,int present,int absent,int cancelled,int minimum,int percentage*/){
 /*       SQLiteDatabase db = this.getReadableDatabase();
        //db.rawQuery( "UPDATE subjects SET name = " + updatedName + " WHERE name = " + name + "",null );
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_NAME,updatedName);
        contentValues.put(CONTACTS_COLUMN_MINIMUM,updatedMinimum);
        return db.update("subjects",contentValues,"name = ?",new String[]{ name });
    }
*/
    public List<Timetable> getAllTimeTable(String day)
    {
        List<Timetable> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from timetable " + "where " + day + " = 1 ", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Timetable event = new Timetable(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            array_list.add(event);
            res.moveToNext();
        }
        db.close();
        res.close();
        return array_list;
    }
/*
    public boolean isSubjectPresent(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "select * from " + EVENTS_TABLE_NAME+ " where " + EVENTS_COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{fieldValue});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
*/
}


