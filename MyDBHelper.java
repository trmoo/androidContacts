package com.example.homework04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{
    public MyDBHelper(Context context) {
        super(context, "myContacts1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE people (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, alias TEXT, phone TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS people;");
        onCreate(sqLiteDatabase);
    }
}
