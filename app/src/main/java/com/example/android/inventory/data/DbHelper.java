
package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventory.data.Contract.Entry;


public class DbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "inventory.db";


    private static final int DATABASE_VERSION = 1;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE =  "CREATE TABLE " + Entry.TABLE_NAME + " ("
                + Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Entry.COLUMN_Name + " TEXT, "
                + Entry.COLUMN_price + " INTEGER, "
                + Entry.COLUMN_Quantity + " INTEGER, "
                + Entry.COLUMN_Supplier_Name + " TEXT, "
                + Entry.COLUMN_Supplier_Phone_Number + " TEXT);";

        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}