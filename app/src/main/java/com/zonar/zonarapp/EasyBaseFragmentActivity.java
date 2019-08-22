
package com.zonar.zonarapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.james.easyclass.ImageManager;
import com.james.easyclass.MemoryManager;
import com.james.easyclass.imageloader.ImageLoader;
import com.james.easyclass.model.Res;
import com.james.easydatabase.EasySharedPreference;
import com.james.utils.PhoneUtils;
import com.james.views.dialog.EasyAlertDialog;
import com.zonar.zonarapp.model.Consumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EasyBaseFragmentActivity extends FragmentActivity {
    public static final String PERMISSION_READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String PERMISSION_READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";

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
    protected boolean isPaused = false;

    private String fromClassName;
    private ProgressDialog progressDialog;

    private OnActivityResultCall mOnActivityResultCall;

    private HashMap<Integer, Runnable> permissionRequestCallbacks = new HashMap<>();

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

        imageLoader = ImageLoader.getInstance(this, R.mipmap.ic_launcher);

        MemoryManager.getUsedMemory(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
        isPaused = false;

        if (isOnResume) {
            onResumeCall();
        }
        isOnResume = true;
    }

    protected void onResumeCall() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    public boolean isPaused() {
        return isPaused;
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
     * @param fromClass
     * @param toClass
     * @param toBundle
     */
    public void startActivity(Class<?> fromClass, Class<?> toClass, Bundle toBundle) {
        startActivity(true, fromClass, toClass, toBundle);
    }

    /**
     * @param finish
     * @param fromClass
     * @param toClass
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
    @SuppressLint("MissingPermission")
    public boolean updateLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mProvider = LocationManager.NETWORK_PROVIDER;
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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
            for (String provider : mLocationManager.getAllProviders()) {
                if (mLocationManager.isProviderEnabled(provider)) {
                    mLocation = mLocationManager.getLastKnownLocation(provider);
                    if (mLocation != null) {
                        return true;
                    }
                }
            }
        }

        if (mLocation == null) {
            mProvider = null;
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

    public void showDatePickDialog(final Date defaultDate, final Consumer<Date> consumer) {
        int y = defaultDate.getYear() + 1900;
        int m = defaultDate.getMonth() + 1;
        int d = defaultDate.getDate();

//        SimpleDatePickerDialog simpleDatePickerDialog = new SimpleDatePickerDialog(this, y, m, d);
//        simpleDatePickerDialog.setOnDatePickConfirmListener(new SimpleDatePickerDialog.OnDatePickConfirmListener() {
//
//            @Override
//            public void onDatePick(int year, int month, int date) {
//                Date datePick = new Date();
//                datePick.setYear(year - 1900);
//                datePick.setMonth(month - 1);
//                datePick.setDate(date);
//                if (consumer != null)
//                    consumer.accept(datePick);
//            }
//        });
//        simpleDatePickerDialog.show();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int date) {
                Date datePick = new Date();
                datePick.setYear(year - 1900);
                datePick.setMonth(month);
                datePick.setDate(date);
                if (consumer != null)
                    consumer.accept(datePick);
            }
        }, y, m, d);
        datePickerDialog.show();

//        NaDatePickDialog.showSlef(this, datePick, new NaDatePickDialog.OnDatePickListener() {
//            @Override
//            public void onTimePick(Date date) {
//                datePick.setTime(date.getTime());
//                if (runnable != null) {
//                    runOnUiThread(runnable);
//                }
//            }
//        });
    }

    public void getTimePick(final Date datePick, final Runnable runnable) {
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

    public boolean checkPermission(final String... permissions) {
        ArrayList<String> tmps = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                tmps.add(permission);
            }
        }

        return tmps.size() == 0;
    }

    public void checkAndRequestPermission(final String permission, final int requestCode, final Runnable onRequestCallback) {
        checkAndRequestPermission(new String[]{permission}, requestCode, onRequestCallback);
    }

    public void checkAndRequestPermission(final String[] permissions, final int requestCode, final Runnable onRequestCallback) {
        ArrayList<String> tmps = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                tmps.add(permission);
            }
        }

        String warning = "";
        if (tmps.contains(PERMISSION_ACCESS_FINE_LOCATION) && tmps.contains(PERMISSION_READ_PHONE_STATE)) {
            warning = "請同意開啟定位服務以獲得精準的地理位置資訊";
        } else if (tmps.contains(PERMISSION_ACCESS_FINE_LOCATION)) {
            warning = "請同意開啟定位服務以獲得精準的地理位置資訊";
        } else if (tmps.contains(PERMISSION_READ_PHONE_STATE)) {
            warning = "請同意取得手機資訊，此功能為推播所需要";
        }

        final String[] notGrantedPermissions = tmps.toArray(new String[0]);
        if (TextUtils.isEmpty(warning)) {
            requestPermission(notGrantedPermissions, requestCode, onRequestCallback);
        } else {
            EasyAlertDialog.showSelf(this, warning, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermission(notGrantedPermissions, requestCode, onRequestCallback);
                }
            });
        }

    }

    public void requestPermission(String[] notGrantedPermissions, int requestCode, Runnable onRequestCallback) {
        if (notGrantedPermissions.length == 0) { // all permission are granted
            if (onRequestCallback != null) {
                onRequestCallback.run();
            }
        } else { // request not granted permissions
            permissionRequestCallbacks.put(requestCode, onRequestCallback);
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    notGrantedPermissions,
                    requestCode);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

//        // Should we show an explanation?
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                permission)) {
//
//            // Show an expanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//
//        } else {
//            //
//            permissionRequestCallbacks.put(requestCode, onRequestCallback);
//            // No explanation needed, we can request the permission.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permission, ACCESS_FINE_LOCATION},
//                    requestCode);
//
//            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//            // app-defined int constant. The callback method gets the
//            // result of the request.
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Runnable onRequestCallback = permissionRequestCallbacks.get(requestCode);
        if (onRequestCallback != null) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRequestCallback.run();
                permissionRequestCallbacks.remove(requestCode);
            }
        }
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

    @Override
    protected void onStart() {
        super.onStart();

//        BasicRageShake.getInstance(this, BasicDevelopMode.getInstance()).start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

//        BasicRageShake.getInstance(this, BasicDevelopMode.getInstance()).stop(this);
    }

    public void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void showKeyboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        }, 500);
    }
}
