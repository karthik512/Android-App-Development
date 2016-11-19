package com.sachin.karthik.sachinist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Karthik on 25-09-2016.
 */
public class DetailsDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "Details";
    private static final int DB_VERSION = 1;

    DetailsDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String stmt = "CREATE TABLE DETAILS ( _id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CLASS TEXT, IMAGE_ID INTEGER, CLICKS INTEGER);";
        db.execSQL(stmt);
        for(int i = 0;i < Details.cenDetails.length;++i)
            insertData(db,Details.cenDetails[i].getName(),"Centuries",Details.cenDetails[i].getImageResourceId(),0);

        for(int i = 0;i < Details.lastTest.length;++i)
            insertData(db,Details.lastTest[i].getName(),"LastTest",Details.lastTest[i].getImageResourceId(),0);
    }

    private static void insertData(SQLiteDatabase db,String name, String clss, int imageId, int clicks){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("CLASS",clss);
        contentValues.put("IMAGE_ID",imageId);
        contentValues.put("CLICKS",clicks);
        db.insert(DB_NAME,null,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
