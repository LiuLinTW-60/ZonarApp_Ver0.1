package com.zonar.zonarapp;

import android.app.Application;
import android.content.Context;

public class ZaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    public static Context getGlobalContext() {
        return context;
    }
}
