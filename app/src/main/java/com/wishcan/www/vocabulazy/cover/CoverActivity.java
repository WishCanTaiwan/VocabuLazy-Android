package com.wishcan.www.vocabulazy.cover;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.MainActivity;

public class CoverActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_cover, null);
        setContentView(view);
        final Intent intent = new Intent(this, MainActivity.class);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 2000);
    }

}
