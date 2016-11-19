package com.sachin.karthik.sachinist;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Sachin extends AppCompatActivity {

    private String[] titles;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 0;

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            selectItem(pos);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sachin);

        titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView)findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLay);
        drawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        if(savedInstanceState == null)
            selectItem(0);
        else{
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        }

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        //drawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener(){

                    @Override
                    public void onBackStackChanged() {
                        Fragment currFrag = getFragmentManager().findFragmentByTag("visible fragment");
                        if(currFrag instanceof HomePage)
                            currentPosition = 0;
                        else if(currFrag instanceof Centuries)
                            currentPosition = 1;
                        else if(currFrag instanceof LastTest)
                            currentPosition = 2;
                        else if(currFrag instanceof Messages)
                            currentPosition = 3;

                        setActionBarTitle(currentPosition);
                        drawerList.setItemChecked(currentPosition,true);
                    }
                }
        );
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("position",currentPosition);
    }

    public void selectItem(int pos) {
        currentPosition = pos;
        Fragment frag;
        switch (pos){
            case 1:
                frag = new Centuries();
                break;
            case 2:
                frag = new LastTest();
                break;
            case 3:
                frag = new Messages();
                break;
            default:
                frag = new HomePage();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,frag,"visible fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        setActionBarTitle(pos);
        drawerLayout.closeDrawer(drawerList);
    }

    public void setActionBarTitle(int pos) {
        String title = "";
        if(pos == 0)
            title = getResources().getString(R.string.app_name);
        else
            title = titles[pos];
        getSupportActionBar().setTitle(title);
    }
}
