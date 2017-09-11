package com.sasha.tetris;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "recordDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_RECORD = "RECORD";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "DATE LONG, "
                + "RESULT INTEGER);");
        //insertResult("Test",100);
        //insertResult("Test1",200);
        //insert(db,"Test",100);
        //insert(db,"Test1",200);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertResult(String name, int result){
        Cursor cursor = getResults();
        if (cursor.getCount() >= 5) {
            cursor.moveToLast();
            if (result >= cursor.getInt(0)) {
                getWritableDatabase().delete(TABLE_RECORD, "_id = " + cursor.getInt(0), null);
            } else {
                return 0;
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("NAME",name);
        cv.put("DATE", System.currentTimeMillis());
        cv.put("RESULT",result);
        return getWritableDatabase().insert(TABLE_RECORD, null, cv);
    }

    public void insert(SQLiteDatabase db, String name, int result){
        ContentValues cv = new ContentValues();
        cv.put("NAME",name);
        cv.put("DATE", System.currentTimeMillis());
        cv.put("RESULT",result);
        db.insert(TABLE_RECORD, null, cv);
    }

    public Cursor getResults(){
        return getReadableDatabase().query(TABLE_RECORD,null,null,null,null,null,"RESULT desc");
    }

}
