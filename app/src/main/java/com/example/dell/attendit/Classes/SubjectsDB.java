package com.example.dell.attendit.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectsDB extends SQLiteOpenHelper {
    public Context context;
    public SubjectsDB subjectsDB;
    public TimetableDB timetableDB;
    public static final String DATABASE_NAME = "subjectsDB.db";
    public static final String EVENTS_TABLE_NAME = "subjects";
    public static final String EVENTS_COLUMN_ID= "id";
    public static final String EVENTS_COLUMN_NAME= "name";
    public static final String EVENTS_COLUMN_CREDIT = "credit";
    public static final String CONTACTS_COLUMN_HOURS = "hours";
    public static final String CONTACTS_COLUMN_TOTAL = "total";
    public static final String CONTACTS_COLUMN_PRESENT = "present";
    public static final String CONTACTS_COLUMN_ABSENT = "absent";
    public static final String CONTACTS_COLUMN_CANCELLED = "cancelled";
    public static final String CONTACTS_COLUMN_MINIMUM = "minimum";
    public static final String CONTACTS_COLUMN_PERCENTAGE = "percentage";
    public static final String CONTACTS_COLUMN_STATUS = "status";

    public SubjectsDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table subjects " +
                        "(id integer primary key, name text,credit integer, hours integer, total integer, present integer, absent integer, cancelled integer, minimum integer, percentage real, status integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS subjects");
        onCreate(db);
    }

    public boolean insertSubject (String name,int credit,int hours,int total,int present,int absent,int cancelled,int minimum,double percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_NAME, name);
        contentValues.put(EVENTS_COLUMN_CREDIT, credit);
        contentValues.put(CONTACTS_COLUMN_HOURS, hours);
        contentValues.put(CONTACTS_COLUMN_TOTAL, total);
        contentValues.put(CONTACTS_COLUMN_PRESENT,present);
        contentValues.put(CONTACTS_COLUMN_ABSENT, absent);
        contentValues.put(CONTACTS_COLUMN_CANCELLED, cancelled);
        contentValues.put(CONTACTS_COLUMN_MINIMUM, minimum);
        contentValues.put(CONTACTS_COLUMN_PERCENTAGE, percentage);
        contentValues.put(CONTACTS_COLUMN_STATUS, 0);
        db.insert("subjects", null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from subjects where id="+id+"", null );
        return res;
    }

    public Subject getSubjectDetails(String name){
        Subject subject;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from subjects WHERE name = '" + name + "'", null);
        if(res!=null && res.moveToFirst()){
            subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            res.close();
            db.close();
            return subject;
        }
        return  null;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_TABLE_NAME);
        db.close();
        return numRows;
    }

    public Integer deleteSubject (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        timetableDB = new TimetableDB(context);
        timetableDB.deleteTimetableOverall(name);
        return db.delete("subjects", "name = ? ", new String[] { name });
    }

    public int editSubject(String name, String updatedName, int updatedMinimum/*,int credit,int hours,int total,int present,int absent,int cancelled,int minimum,int percentage*/){
        SQLiteDatabase db = this.getReadableDatabase();
        //db.rawQuery( "UPDATE subjects SET name = " + updatedName + " WHERE name = " + name + "",null );
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_NAME,updatedName);
        contentValues.put(CONTACTS_COLUMN_MINIMUM, updatedMinimum);
        return db.update("subjects", contentValues, "name = ?", new String[]{name});
    }

    public int isPresent(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Subject subject ;
        Cursor res =  db.rawQuery("select * from subjects WHERE name = '" + name + "'", null);
        if(res!=null && res.moveToFirst()){
            subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            res.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_PRESENT,subject.getPresent()+1);
            contentValues.put(CONTACTS_COLUMN_TOTAL,subject.getTotal()+1);
            return db.update("subjects", contentValues, "name = ?", new String[]{name});
        }
        return 0;
    }

    public int isAbsent(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Subject subject ;
        Cursor res =  db.rawQuery("select * from subjects WHERE name = '" + name + "'", null);
        if(res!=null && res.moveToFirst()){
            subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            res.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_ABSENT,subject.getAbsent()+1);
            contentValues.put(CONTACTS_COLUMN_TOTAL,subject.getTotal()+1);
            return db.update("subjects", contentValues, "name = ?", new String[]{name});
        }
        return 0;
    }

    public int isCancel(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Subject subject ;
        Cursor res =  db.rawQuery("select * from subjects WHERE name = '" + name + "'", null);
        if(res!=null && res.moveToFirst()){
            subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            res.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_CANCELLED,subject.getCancelled()+1);
            return db.update("subjects", contentValues, "name = ?", new String[]{name});
        }
        db.close();
        return 0;
    }

    public int setStatus(String name, int newStatus){
        SQLiteDatabase db = this.getReadableDatabase();
        Subject subject ;
        Cursor res = db.rawQuery("select * from subjects WHERE name = '" + name + "'", null);
        if(res!=null && res.moveToFirst()){
            subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            res.close();
            ContentValues contentValues = new ContentValues();
            int oldStatus =  subject.getStatus();
            switch (newStatus){
                case 0:
                    switch (oldStatus){
                        case 0: break;
                        case 1: contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() - 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() - 1);
                                break;
                        case 2: contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() - 1);
                                contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() - 1);
                                break;
                        case 3: contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() - 1);
                                break;
                    }
                    break;
                case 1:
                    switch (oldStatus){
                        case 0: contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() + 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() + 1);
                                break;
                        case 1: break;
                        case 2: contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() + 1);
                                contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() - 1);
                                break;
                        case 3: contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() + 1);
                                contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() - 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() + 1);
                                break;
                    }
                    break;
                case 2:
                    switch (oldStatus){
                        case 0: contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() + 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() + 1);
                                break;
                        case 1: contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() + 1);
                                contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() - 1);
                                break;
                        case 2: break;
                        case 3: contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() + 1);
                                contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() - 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() + 1);
                                break;
                    }
                    break;
                case 3:
                    switch (oldStatus){
                        case 0: contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() + 1);
                                break;
                        case 1: contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() + 1);
                                contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() - 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() - 1);
                                break;
                        case 2:
                                contentValues.put(CONTACTS_COLUMN_CANCELLED, subject.getCancelled() + 1);
                                contentValues.put(CONTACTS_COLUMN_ABSENT, subject.getAbsent() - 1);
                                contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() - 1);
                                break;
                        case 3: break;
                    }
                    break;
            }
            contentValues.put(CONTACTS_COLUMN_STATUS,newStatus);
            return db.update("subjects", contentValues, "name = ?", new String[]{name});
        }
        db.close();
        return 0;
    }

    public int resetStatus(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from subjects", null);
        if(res!=null && res.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_STATUS, 0);
            res.close();
            return db.update("subjects", contentValues, "", null);
        }
        return 0;
    }

    public void allPresent(){
        List<Subject> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from subjects", null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            Subject subject = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_TOTAL)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PRESENT)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ABSENT)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_CANCELLED)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)), res.getInt(res.getColumnIndex(CONTACTS_COLUMN_STATUS)));
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_PRESENT, subject.getPresent() + 1);
            contentValues.put(CONTACTS_COLUMN_TOTAL, subject.getTotal() + 1);
            db.update(EVENTS_TABLE_NAME, contentValues, "name = ?", new String[]{ subject.getName() });
            res.moveToNext();
        }
        res.close();
        db.close();
    }

    public List<Subject> getAllSubjects()
    {
        List<Subject> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from subjects", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Subject event = new Subject(res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),res.getInt(res.getColumnIndex(CONTACTS_COLUMN_MINIMUM)));
            array_list.add(event);
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    public boolean isSubjectPresent(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "select * from " + EVENTS_TABLE_NAME+ " where " + EVENTS_COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(Query, new String[]{fieldValue});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }
}

