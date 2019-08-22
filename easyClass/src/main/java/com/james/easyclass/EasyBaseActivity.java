
package com.james.easyclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.james.easyclass.imageloader.ImageLoader;
import com.james.easyclass.model.Res;
import com.james.easydatabase.EasySharedPreference;
import com.james.utils.LogUtils;
import com.james.utils.PhoneUtils;

import java.util.Date;

public class EasyBaseActivity extends Activity {
    protected final String TAG = "ActivityBase";
    protected final String FROM_ACTIVITY = "from_activity";
    protected final int REQUEST_CODE = 0x5797;
    protected Context mContext;
    protected Activity activity;
    protected Bundle fromBundle;
    protected String mProvider;
    protected Location mLocation;
    protected LocationManager mLocationManager;
    protected EasySharedPreference easySharedPreference;
    protected ImageManager imageManager;
    protected ImageLoader imageLoader;

    protected boolean flag = true;
    protected boolean isOnResume = false;

    private String fromClassName;
    private ProgressDialog progressDialog;

    private OnActivityResultCall mOnActivityResultCall;

    public interface OnCancelListener {
        public void onCancel();
    }

    public interface OnActivityResultCall {
        public void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        mContext = this;

        activity = this;
        //
        if (this.getIntent().getExtras() != null) {
            fromBundle = this.getIntent().getExtras();
            fromClassName = fromBundle.getString(FROM_ACTIVITY);
        }

        MemoryManager.getUsedMemory(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;

        if (isOnResume) {
            onResumeCall();
        }
        isOnResume = true;
    }

    protected void onResumeCall() {
    }

    protected boolean reload() {
        return true;
    }

    /**
     *
     */
    @Override
    public void startActivity(Intent intent) {
        if (flag) {
            flag = false;
            super.startActivity(intent);
        }
    }

    /**
     *
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fromClass
     * @return
     */
    public boolean isFrom(Class<?> fromClass) {
        if (fromClassName == null)
            return false;

        return fromClassName.equalsIgnoreCase(fromClass.getSimpleName());
    }

    /**
     * @return
     */
    public String fromClass() {
        return fromClassName;
    }

    /**
     * @param finish
     * @param c
     * @param toBundle
     */
    public void startActivity(Class<?> fromClass, Class<?> toClass, Bundle toBundle) {
        startActivity(true, fromClass, toClass, toBundle);
    }

    /**
     * @param finish
     * @param c
     * @param toBundle
     */
    public void startActivity(boolean finish, Class<?> fromClass, Class<?> toClass, Bundle toBundle) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(FROM_ACTIVITY, fromClass.getSimpleName());

        if (toBundle != null) {
            intent.putExtras(toBundle);
        }

        startActivity(intent);

        if (finish) {
            super.finish();
        }
    }

    public void startActivityForResult(Intent intent, OnActivityResultCall onActivityResultCall) {
        startActivityForResult(intent, null, onActivityResultCall);
    }

    public void startActivityForResult(Intent intent, Bundle toBundle, OnActivityResultCall onActivityResultCall) {
        startActivityForResult(intent, REQUEST_CODE, toBundle, onActivityResultCall);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle toBundle, OnActivityResultCall onActivityResultCall) {
        mOnActivityResultCall = onActivityResultCall;

        if (toBundle != null) {
            intent.putExtras(toBundle);
        }

        startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Class<?> fromClass, Class<?> toClass, Bundle toBundle, OnActivityResultCall onActivityResultCall) {
        startActivityForResult(fromClass, toClass, REQUEST_CODE, toBundle, onActivityResultCall);
    }

    public void startActivityForResult(Class<?> fromClass, Class<?> toClass, int requestCode, Bundle toBundle, OnActivityResultCall onActivityResultCall) {
        mOnActivityResultCall = onActivityResultCall;

        Intent intent = new Intent(this, toClass);
        intent.putExtra(FROM_ACTIVITY, fromClass.getSimpleName());

        if (toBundle != null) {
            intent.putExtras(toBundle);
        }

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mOnActivityResultCall != null) {
            mOnActivityResultCall.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * @param finish
     * @param uri
     */
    public void startActivity(boolean finish, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        if (finish) {
            super.finish();
        }
    }

    public void showExitDialog() {
        showCheckDialog("Alert", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    finish();
                    MemoryManager.killProcess();
                }
            }

        });
    }

    public void showTripleCheckDialog(int resid, final DialogInterface.OnClickListener onClickListener) {
        showTripleCheckDialog(getResources().getString(resid), onClickListener);
    }

    public void showTripleCheckDialog(String message, final DialogInterface.OnClickListener onClickListener) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);

            AlertDialog dialog = builder.create();

            String confirm = Res.string.dialog_confirm_button;
            String skip = Res.string.dialog_skip_button;
            String cancel = Res.string.dialog_cancel_button;
            if (onClickListener != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel, onClickListener);
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, skip, onClickListener);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, confirm, onClickListener);
            }

            dialog.show();
        }
    }

    public void showCheckDialog(int resid, final DialogInterface.OnClickListener onClickListener) {
        String message = getResources().getString(resid);
        showCheckDialog(message, onClickListener);
    }

    public void showCheckDialog(String message, final DialogInterface.OnClickListener onClickListener) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);

            AlertDialog dialog = builder.create();

            String confirm = Res.string.dialog_confirm_button;
            String cancel = Res.string.dialog_cancel_button;
            if (onClickListener != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel, onClickListener);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, confirm, onClickListener);
            }

            dialog.show();
        }
    }

    public void showSimpleDialog(int resid) {
        String content = getResources().getString(resid);
        showSimpleDialog(content, null);
    }

    public void showSimpleDialog(int resid, final DialogInterface.OnClickListener onClickListener) {
        String content = getResources().getString(resid);
        showSimpleDialog(content, onClickListener);
    }

    public void showSimpleDialog(String message) {
        showSimpleDialog(message, null);
    }

    public void showSimpleDialog(String message, final DialogInterface.OnClickListener onClickListener) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);

            AlertDialog dialog = builder.create();

            String text = Res.string.dialog_confirm_button;
            if (onClickListener != null) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, onClickListener);
            } else {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            dialog.show();
        }
    }

    public Dialog showProgressDialog(int resid) {
        String text = getResources().getString(resid);
        return showProgressDialog(text);
    }

    public Dialog showProgressDialog(String text) {
        if (!isFinishing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(text);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void setProgressDialog(String text) {
        if (progressDialog != null) {
            progressDialog.setMessage(text);
        }
    }

    public void setProgressDialog(int resid, int progress) {
        String text = getResources().getString(resid, progress);
        if (progressDialog != null) {
            progressDialog.setMessage(text + "%");
        }
    }

    public void startSettingGPS(final OnCancelListener onCancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Res.string.dialog_gps_title)
                .setMessage(Res.string.dialog_gps_message)
                .setCancelable(false)
                .setPositiveButton(Res.string.dialog_confirm_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(Res.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (onCancelListener != null) {
                            onCancelListener.onCancel();
                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * @return
     */
    public boolean updateLocation() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        mLocation = null;
        if (mLocation == null) {
            mProvider = LocationManager.NETWORK_PROVIDER;
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (mLocation == null) {
            mProvider = LocationManager.GPS_PROVIDER;
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (mLocation == null) {
            Criteria criteria = new Criteria();
            mProvider = mLocationManager.getBestProvider(criteria, true);
            if (mProvider != null) {
                mLocation = mLocationManager.getLastKnownLocation(mProvider);
            }
        }

        if (mLocation == null) {
            mProvider = null;
            Toast.makeText(this, Res.string.toast_gps_fail, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * @return
     */
    public boolean checkInternetStatus(final OnCancelListener onCancelListener) {
        boolean hasNetwork = PhoneUtils.hasNetwork(mContext);

        if (!hasNetwork) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Res.string.dialog_internet_title)
                    .setMessage(Res.string.dialog_internet_message)
                    .setCancelable(false)
                    .setPositiveButton(Res.string.dialog_confirm_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(Res.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (onCancelListener != null) {
                                onCancelListener.onCancel();
                            }
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        return hasNetwork;
    }

    /**
     * @return
     */
    public boolean checkWiFiStatus(final int resid, final OnCancelListener onCancelListener) {
        boolean hasWiFi = PhoneUtils.hasWiFi(mContext);

        if (!hasWiFi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Res.string.dialog_internet_title)
                    .setMessage(resid)
                    .setCancelable(false)
                    .setPositiveButton(Res.string.dialog_confirm_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(Res.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (onCancelListener != null) {
                                onCancelListener.onCancel();
                            }
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        return hasWiFi;
    }

    public void getDatePick(final Date datePick, final Runnable runnable) {
        Date date = new Date();

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hour, int minute) {
                datePick.setHours(hour);
                datePick.setMinutes(minute);
                if (runnable != null)
                    runOnUiThread(runnable);
            }
        }, 0, 0, true);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int date) {
                timePickerDialog.show();
                datePick.setYear(year - 1900);
                datePick.setMonth(month);
                datePick.setDate(date);
                LogUtils.i(TAG, "datePick: " + datePick.getYear());
                LogUtils.i(TAG, "datePick: " + datePick.getMonth());
            }
        }, date.getYear() + 1900, date.getMonth(), date.getDate());
        datePickerDialog.show();
    }

    public void showSingleSelectDialog(final String title, final CharSequence[] items,
                                       DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setItems(items, onClickListener);
        builder.show();
    }

    public void showInputDialog(String title, String message, final Handler inputHandler) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(title);
        alert.setMessage(message);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(Res.string.dialog_confirm_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (inputHandler != null) {
                    Message msg = new Message();
                    msg.obj = value;
                    inputHandler.sendMessage(msg);
                }
            }
        });

        alert.setNegativeButton(Res.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    //
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            onRestoreInstanceStateException();
        }
    }

    protected void onRestoreInstanceStateException() {

    }
}
