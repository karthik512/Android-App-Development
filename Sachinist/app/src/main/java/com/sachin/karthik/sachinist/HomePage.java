package com.sachin.karthik.sachinist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {

    SQLiteOpenHelper mosDat;
    SQLiteDatabase db;
    Cursor cursor;
    String[] headings;
    int[] images;
    LastTest lastTest;
    Centuries cent;
    String ts;

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView recentRecycler = (RecyclerView)relativeLayout.findViewById(R.id.home_recycler);
        cent = new Centuries();
        lastTest = new LastTest();
        try{
            mosDat = new DetailsDatabase(getActivity());
            db = mosDat.getReadableDatabase();
            cursor = db.query("Details",new String[]{"NAME","IMAGE_ID","CLICKS"},null,null,null,null,"CLICKS DESC");

            headings = new String[4];
            images = new int[4];

            int i = 0;
            while(cursor.moveToNext() && i < 4)
            {
                //if(cursor.getInt(2) != 0)
                {
                    headings[i] = cursor.getString(0);
                    images[i++] = cursor.getInt(1);
                }
            }
        }catch(SQLiteException e){
            Toast.makeText(getActivity(),"FAILED",Toast.LENGTH_SHORT).show();
        }finally{
            if(cursor != null)
                cursor.close();
            if(db != null)
                db.close();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recentRecycler.setLayoutManager(layoutManager);

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(headings,images);
        recentRecycler.setAdapter(adapter);

        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            String cls;
            @Override
            public void onClick(int position) {
                try{
                    mosDat = new DetailsDatabase(getActivity());
                    db = mosDat.getWritableDatabase();
                    cursor = db.query("Details",new String[]{"CLASS"},"NAME = ? AND IMAGE_ID = ?",new String[]{headings[position],Integer.toString(images[position])},null,null,null);
                    if(cursor.moveToFirst())
                        cls = cursor.getString(0);
                }catch (SQLiteException e){
                    Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                }finally {
                    if(cursor != null)
                        cursor.close();
                    if(db != null)
                        db.close();
                }
                if(cls.equals("Centuries"))
                    cent.showDetail(getActivity(), images[position]);
                else if(cls.equals("LastTest"))
                    lastTest.showDetail(getActivity(),images[position]);
            }
        });

        return relativeLayout;
    }

}
