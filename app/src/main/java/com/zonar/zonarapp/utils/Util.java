package com.zonar.zonarapp.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static long getAvailableStorage(){
        long availableSize = 0;

        if(hasWriteableExternalStorage()){
            File path = Environment.getExternalStorageDirectory();

            // get the info of memory space
            StatFs info = new StatFs(path.getPath());

            // calculate to available size
            availableSize = (long)info.getBlockSize() * (long)info.getAvailableBlocks() - 102400L;
        }
        return availableSize >= 0 ? availableSize : 0;
    }

    public static boolean hasWriteableExternalStorage(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    private static String getNow(String format) {
        String str;
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());

        str = formatter.format(curDate);
        return str;
    }

    public static String getCurrentDateTime(){
        return getNow("yyyy-MM-dd_HHmmss");
    }
}
