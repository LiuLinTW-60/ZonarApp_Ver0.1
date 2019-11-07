package com.zonar.zonarapp.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.zonar.zonarapp.R;

public class DialogHelper {
    public static void showErrorMessage(Context context, int msgResId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msgResId);
        dialog.setPositiveButton(R.string.btn_ok, null);
        dialog.show();
    }
}
