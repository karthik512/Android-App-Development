package com.sachin.karthik.sachinist;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastTest extends Fragment {

    SQLiteOpenHelper lasDat;
    SQLiteDatabase db;
    Cursor cursor;
    String[] names;
    int[] images;

    public LastTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView lastRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_centuries,container,false);

        try{
            lasDat = new DetailsDatabase(getActivity());
            db = lasDat.getReadableDatabase();
            cursor = db.query("Details",new String[]{"NAME","IMAGE_ID"},"CLASS = ?",new String[]{"LastTest"},null,null,null);

            names = new String[cursor.getCount()];
            images = new int[cursor.getCount()];

            int i = 0;

            while(cursor.moveToNext())
            {
                names[i] = cursor.getString(0);
                images[i++] = cursor.getInt(1);
            }
        }catch (SQLiteException e){
            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor != null)
                cursor.close();
            if(db != null)
                db.close();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(names, images);
        lastRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lastRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                showDetail(getActivity(),images[position]);
            }
        });

        return lastRecycler;
    }

    public void showDetail(Activity a, int id)
    {
        try{
            lasDat = new DetailsDatabase(a);
            db = lasDat.getWritableDatabase();
            cursor = db.query("Details",new String[]{"CLICKS"},"CLASS = ? AND IMAGE_ID = ?",new String[]{"LastTest",Integer.toString(id)},null,null,null);
            int click = 0;
            if(cursor.moveToFirst())
                click = cursor.getInt(0);

            ContentValues cv = new ContentValues();
            cv.put("CLICKS",click+1);

            db.update("Details",cv,"CLASS = ? AND IMAGE_ID = ?",new String[]{"LastTest",Integer.toString(id)});
        }catch (SQLiteException e){
            Toast.makeText(a,"Failed",Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
    }

}
