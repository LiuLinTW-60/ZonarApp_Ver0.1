package com.zonar.zonarapp.utils;

import android.annotation.SuppressLint;
import android.util.Log;

public class Debug {
    private static final String TAG = "XroundApp";
    private static long startTime = 0;
    private static long endTime = 0;

    public static void errLog(String msg) {
        Log.e(TAG, msg);
    }

    public static void testLog(String msg) {
        Log.d(TAG, msg);
    }

    public static void infoLog(String msg) {
        Log.d(TAG, msg);
    }

    public static void logStartTime() {
        startTime = endTime = System.currentTimeMillis();
    }

    public static void logEndTime() {
        endTime = System.currentTimeMillis();
    }

    public static long getLogTimeDistance() {
        return getLogTimeDistance(null);
    }

    @SuppressLint("DefaultLocale")
    public static long getLogTimeDistance(String tag) {
        long distance = (endTime - startTime);

        if (tag != null) {
            testLog(String.format("## %s used time:%d", tag, distance));
        } else {
            testLog("## used time:" + distance);
        }
        return distance;
    }
}
