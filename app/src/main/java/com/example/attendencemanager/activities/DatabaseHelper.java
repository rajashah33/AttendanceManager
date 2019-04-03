package com.example.attendencemanager.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
     private static final String DATABASE_NAME = "Student.db";
     private static final String TABLE_NAME = "student_table";
     private static final String TABLE_NAME2 = "subject_table";
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
          db.execSQL("create table " + TABLE_NAME2 + " (date INTEGER PRIMARY KEY)");

     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
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

     Cursor getMonthDataFromSubjectTable(String subject_name, String lastDayOfMonthStr) {
          SQLiteDatabase db = this.getReadableDatabase();
          int firstDayOfMonth = Integer.parseInt(lastDayOfMonthStr.substring(0, 6) + "01");
          int lastDayOfMonth = Integer.parseInt(lastDayOfMonthStr);

          Cursor res = db.rawQuery("select date, "+subject_name+" from "+TABLE_NAME2+" where date >= "+firstDayOfMonth+" and date <= "+lastDayOfMonth+";", null);
          return res;
     }

     String getADataFromSubjectTable(String subject_name, String date) {
          SQLiteDatabase db = this.getReadableDatabase();
          int selectedDay = Integer.parseInt(date);
          Cursor res = db.rawQuery("select "+subject_name+" from "+TABLE_NAME2+" where date = "+selectedDay+" ;", null);
          if(res.moveToNext())
               return res.getString(0);
          else
               return "notEntered";
     }

     Cursor getAData(String subject) {
          SQLiteDatabase db = this.getWritableDatabase();
          Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where subject_name ='" + subject + "';", null);
          return res;
     }

     boolean addColumnToSubjectTable(String subject_name) {
          SQLiteDatabase db = this.getWritableDatabase();
          try {
               db.execSQL("ALTER TABLE " + TABLE_NAME2 + " ADD COLUMN " + subject_name + " TEXT;");
          } catch (Exception e) {
               return false;
          }
          return true;
     }

     boolean updateData(String status, String id, String subject_name, String present, String absent, String total) {
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues contentValues = new ContentValues();
          contentValues.put(COL_1, id);
          contentValues.put(COL_2, subject_name);
          contentValues.put(COL_3, present);
          contentValues.put(COL_4, absent);
          contentValues.put(COL_5, total);
          db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
          if (updateDataToSubjectTable(status, subject_name))
               return true;
          else
               return false;
     }

     boolean updateDataToSubjectTable(String status, String subject_name) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          int date = Integer.parseInt(sdf.format(new Date()));

          SQLiteDatabase db = this.getWritableDatabase();
          try {
               Cursor res = db.rawQuery("select * from " + TABLE_NAME2 + " where date = " + date + ";", null);
               if (res.moveToFirst()) {
                    db.execSQL("UPDATE " + TABLE_NAME2 + " SET " + subject_name + " ='" + status + "' WHERE date =" + date + ";");
                    // return false;

               } else {
                    db.execSQL("INSERT INTO " + TABLE_NAME2 + " (date, " + subject_name + ") values(" + date + ",'" + status + "');");
               }
          } catch (Exception e) {
               return false;
          }
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