package com.sachin.karthik.sachinist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CenturyDetail extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    public static final String CENTURY_NO = "centuryNo";
    public static final int NOTIFICATION_ID = 0512;
    private TextView tv;
    private ImageView iv;
    private int cenImgNo;
    SQLiteOpenHelper detDat;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_century_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cenImgNo = (Integer) getIntent().getExtras().get(CENTURY_NO);

        try{
            detDat = new DetailsDatabase(this);
            db = detDat.getReadableDatabase();
            cursor = db.query("Details",new String[]{"NAME","IMAGE_ID"},"IMAGE_ID = ?",new String[]{Integer.toString(cenImgNo)},null,null,null);

            if(cursor.moveToFirst())
            {
                String name = cursor.getString(0);
                int image = cursor.getInt(1);

                tv = (TextView)findViewById(R.id.century_head);
                iv = (ImageView)findViewById(R.id.century_image);

                tv.setText(name);
                iv.setImageResource(image);
                iv.setContentDescription(name);
            }
        }catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "Failed to display from database", Toast.LENGTH_SHORT).show();
        }finally{
            cursor.close();
            db.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        String text = tv.getText().toString();

        MenuItem item = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Bitmap b = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        try{
            File cache = getApplicationContext().getExternalCacheDir();
            File shareFile = new File(cache,"example.png");
            FileOutputStream out = new FileOutputStream(shareFile);
            b.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+shareFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shareActionProvider.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    public void showNotif()
    {
        Intent intent = new Intent(this,Sachin.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(Sachin.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(cenImgNo)
                .setContentTitle("Sachinist")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText("Image Sharing")
                .build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share){
            showNotif();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
