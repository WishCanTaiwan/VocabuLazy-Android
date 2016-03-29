package com.wishcan.www.vocabulazy;

import android.app.Application;

//import io.uxtesting.UXTeting;

/**
 * This class is created for applying UXTesting.
 */
public class VLApplication extends Application {

    private static final String UXTESTING_APP_KEY = "JzskKAJgBGPlmP-NIBxBug";

    @Override
    public void onCreate() {
        super.onCreate();
//        UXTesting.Init(this, UXTESTING_APP_KEY);
    }
}
