package com.wishcan.www.vocabulazy.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.storage.Database;

public class MainActivity extends FragmentActivity {

    public static final int VIEW_MAIN_RES_ID = R.id.activity_main_container;
    public static final int ANIM_ENTER_RES_ID = R.anim.fragment_translate_slide_from_right_to_center;
    public static final int ANIM_EXIT_RES_ID = R.anim.fragment_translate_slide_from_center_to_right;

    private static final int VIEW_ACTIVITY_RES_ID = R.layout.view_main_activity;

    private MainFragment mMainFragment;
    private FragmentManager mFragmentManager;
    private static Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            mFragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(VIEW_MAIN_RES_ID, mMainFragment, "MainFragment");
            fragmentTransaction.commit();
        }

        if (mDatabase == null) {
            mDatabase = new Database(this);
        } else {
            Log.d("MainActivity", "database already exist.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchActivity.class);
            // the number "1" is to identify the action
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Database getDatabase() {
        return mDatabase;
    }


}
