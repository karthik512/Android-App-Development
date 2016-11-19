package com.sachin.karthik.sachinist;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Centuries extends Fragment {

    SQLiteOpenHelper cenDat;
    SQLiteDatabase db;
    Cursor cursor;
    String[] cenNames;
    int[] cenImages;

    public Centuries() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView centuryRecycler = (RecyclerView)inflater.inflate(R.layout.fragment_centuries, container, false);

        try{
            cenDat = new DetailsDatabase(getActivity());
            db = cenDat.getWritableDatabase();
            cursor = db.query("Details",new String[]{"NAME","IMAGE_ID"},"CLASS = ?",new String[]{"Centuries"},null,null,null);

            cenNames = new String[cursor.getCount()];
            cenImages = new int[cursor.getCount()];

            int i = 0;

            while(cursor.moveToNext())
            {
                cenNames[i] = cursor.getString(0);
                cenImages[i++] = cursor.getInt(1);
            }
        }catch (SQLiteException e){
            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor != null)
                cursor.close();
            if(db != null)
                db.close();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(cenNames,cenImages);
        centuryRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        centuryRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                showDetail(getActivity(),cenImages[position]);
            }
        });

        return centuryRecycler;
    }

    public void showDetail(Activity a,int id)
    {
        Intent intent = new Intent(a,CenturyDetail.class);
        intent.putExtra(CenturyDetail.CENTURY_NO,id);
        try{
            cenDat = new DetailsDatabase(a);
            db = cenDat.getWritableDatabase();
            cursor = db.query("Details",new String[]{"CLICKS"},"CLASS = ? AND IMAGE_ID = ?",new String[]{"Centuries",Integer.toString(id)},null,null,null);
            int click = 0;
            if(cursor.moveToFirst())
                click = cursor.getInt(0);
            ContentValues cv = new ContentValues();
            cv.put("CLICKS",click+1);
            db.update("Details",cv,"CLASS = ? AND IMAGE_ID = ?",new String[]{"Centuries",Integer.toString(id)});
        }catch (SQLiteException e){
            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor != null)
                cursor.close();
            if(db != null)
                db.close();
        }
        a.startActivity(intent);
    }

}
