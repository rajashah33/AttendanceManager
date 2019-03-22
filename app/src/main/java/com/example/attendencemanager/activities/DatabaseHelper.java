package com.example.attendencemanager.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
     private static final String DATABASE_NAME = "Student.db";
     private static final String TABLE_NAME = "student_table";
     private static final String COL_1 = "id";
     private static final String COL_2 = "subject_name";
     private static final String COL_3 = "present";
     private static final String COL_4 = "absent";
     private static final String COL_5 = "total";

     DatabaseHelper(Context context) {
          super(context, DATABASE_NAME, null, 1);
     }

     @Override
     public void onCreate(SQLiteDatabase db) {
          db.execSQL("create table " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, subject_name TEXT , present TEXT, absent TEXT, total TEXT)");
     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
          onCreate(db);
     }

     public boolean insertData(String subject_name, String present, String absent, String total) {
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues contentValues = new ContentValues();
          contentValues.put(COL_2, subject_name);
          contentValues.put(COL_3, present);
          contentValues.put(COL_4, absent);
          contentValues.put(COL_5, total);
          long result = db.insert(TABLE_NAME, null, contentValues);
          if (result == -1)
               return false;
          else
               return true;
     }

     Cursor getAllData() {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
          return res;
     }

     Cursor getAData(int pos) {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + pos + ";", null);
          return res;
     }

     Cursor getAData(String subject) {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where subject_name ='" + subject + "';", null);
          return res;
     }

     boolean updateData(String id, String subject_name, String present, String absent, String total) {
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues contentValues = new ContentValues();
          contentValues.put(COL_1, id);
          contentValues.put(COL_2, subject_name);
          contentValues.put(COL_3, present);
          contentValues.put(COL_4, absent);
          contentValues.put(COL_5, total);
          db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
          return true;
     }

     public Integer deleteData(String id) {
          SQLiteDatabase db = this.getWritableDatabase();
          int status = db.delete(TABLE_NAME, "ID = ?", new String[]{id});
          db.execSQL("create table tmp (id INTEGER PRIMARY KEY AUTOINCREMENT, subject_name TEXT , present TEXT, absent TEXT, total TEXT)");
          db.execSQL("INSERT INTO tmp (subject_name, present, absent, total) SELECT subject_name, present, absent, total FROM " + TABLE_NAME + ";");
          db.execSQL("DROP TABLE " + TABLE_NAME + ";");
          db.execSQL("ALTER TABLE tmp RENAME TO " + TABLE_NAME + ";");
          return status;


     }

}