package com.zonar.zonarapp;

//import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zonar.zonarapp.utils.DialogHelper;
import com.zonar.zonarapp.utils.FileManager;
import com.zonar.zonarapp.utils.MediaManager;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

//import com.xround_app_sdk.Controller;

public class MeasureActivity extends AppCompatActivity {
    @BindView(R.id.spSample) Spinner spSample;
    @BindView(R.id.btnTestStart) Button btnTestStart;
    @BindView(R.id.txtPlayGain) TextView txtPlayGain;
    @BindView(R.id.txtRecordGain) TextView txtRecordGain;
    @BindView(R.id.sbarPlayGain) VerticalSeekBar sbarPlayGain;
    @BindView(R.id.sbarRecordGain) VerticalSeekBar sbarRecordGain;
    @BindView(R.id.btn_next) Button btnNext;

    private Unbinder unbinder;
    //Controller Controller;

    private MediaManager mediaManager;
    private ArrayAdapter<String> sampleSpinnerAdapter;
    private ProgressDialog pgDialog;
    double [] EQ = {5,5,5,5,5,5,5,5,5,0};
    private boolean isSampleInited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        //Controller = new Controller(this);
        unbinder = ButterKnife.bind(this);
        btnNext.setEnabled(false);
        init();
        RxPermissions rxPermissions = new RxPermissions(MeasureActivity.this);
        rxPermissions
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            initSampleSpinner();
                        }
                    }
                });
        btnNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Controller.write_EQ(EQ, 1);
                Intent myIntent = new Intent(MeasureActivity.this, ZaMajorActivity.class);
                myIntent.putExtra("EQ_array", EQ); //Optional parameters
                //Toast.makeText(MeasureActivity.this, Arrays.toString(EQ), Toast.LENGTH_LONG).show();
                Toast.makeText(MeasureActivity.this, mediaManager.getShowlog(),Toast.LENGTH_LONG).show();
                MeasureActivity.this.startActivity(myIntent);
                //finish();
            }
        });
        //handler = new Handler();
    }
    @Override
    protected void onPause() {
        mediaManager.stop();
        super.onPause();
    }

    private void init() {
        mediaManager = new MediaManager(this);
        mediaManager.setOnMediaListener(new MediaListener());

        MediaManager.VolumeInfo volumeInfo = mediaManager.getSystemVolume();
        sbarPlayGain.setMax(volumeInfo.maxVolume);
        sbarPlayGain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String valueString = String.format("%d", progress);
                txtPlayGain.setText(valueString);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaManager.setPlayGain(getApplicationContext(), seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
        });
        sbarPlayGain.setProgress(volumeInfo.volume);

        sbarRecordGain.setMax(volumeInfo.maxVolume);
        sbarRecordGain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String valueString = String.format("%d", progress);
                txtRecordGain.setText(valueString);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaManager.setRecordGain(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
        });
        sbarRecordGain.setProgress(volumeInfo.maxVolume);
    }

    private void initSampleSpinner() {
        ArrayList<String> spinnerSampleList = prepareSpinnerSampleList();
        sampleSpinnerAdapter = new ArrayAdapter<>(MeasureActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerSampleList);
        spSample.setAdapter(sampleSpinnerAdapter);
        spSample.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String fileName = (String) spSample.getAdapter().getItem(position);
                mediaManager.initTest(fileName);
                showProgress("Initialing...");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing.
            }
        });
        isSampleInited = true;
    }

    private ArrayList<String> prepareSpinnerSampleList() {
        File[] sampleFileList = FileManager.getInstance().getSampleList();
        ArrayList<String> spinnerSampleList = new ArrayList<>();

        if (sampleFileList != null) {
            for (File sample : sampleFileList) {
                spinnerSampleList.add(sample.getName());
            }
        }

        return spinnerSampleList;
    }

    @OnClick(R.id.btnTestStart) void onTestStartClicked() {
        /**boolean hasPermision = mUsbManager.hasPermission(device);
         if (hasPermision) {
         mUsbDeviceConnection = mUsbManager.openDevice(device);
         } else {
         mUsbManager.requestPermission(device, mPermissionIntent);
         return;
         }*/
        //if (!Controller.deviceConnected()) {
          //  Controller.Alert(this);
            //return;
        //}
        RxPermissions rxPermissions = new RxPermissions(MeasureActivity.this);
        rxPermissions
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            if (!isSampleInited) {
                                initSampleSpinner();
                            }
                            /**SwitchtoErrMic();*/
                            //Controller.Switch_MIC(2);
                            Thread.sleep(1);
                            mediaManager.triggerTestPlayButton();
                        }
                    }
                });
    }

    private class MediaListener implements MediaManager.OnMediaListener {

        @Override
        public void onStateChanged(MediaManager.State state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (state) {
                        case INITED:
                            spSample.setEnabled(true);
                            sbarPlayGain.setEnabled(true);
                            sbarRecordGain.setEnabled(true);
                            btnTestStart.setText(R.string.txt_start);
                            dismissProgress();
                            break;

                        case TESTING:
                            spSample.setEnabled(false);
                            sbarPlayGain.setEnabled(false);
                            sbarRecordGain.setEnabled(false);
                            btnTestStart.setText(R.string.txt_stop);
                            break;

                        case STOP_TEST:
                            spSample.setEnabled(true);
                            sbarPlayGain.setEnabled(true);
                            sbarRecordGain.setEnabled(true);
                            btnTestStart.setText(R.string.txt_start);
                            //SwitchtoRefMic();
                            break;

                        case RECORD_PROCESSING:
                            showProgress("Analyzing...");
                            break;

                        case RECORD_FINISHED:
                            String fileName = (String) spSample.getAdapter().getItem(spSample.getSelectedItemPosition());
                            mediaManager.initTest(fileName);
                            //txtSavedFileName.setText(mediaManager.getLastRecordFileName());
                            showProgress("Initialing...");
                            EQ = mediaManager.getEQ();
                            btnNext.setEnabled(true);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                    }
                }
            });
        }

        @Override
        public void onError(MediaManager.Error error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int errorMsgResId = 0;

                    switch (error) {
                        case RECORDER_NO_STORAGE:
                            errorMsgResId = R.string.dl_recorder_storage_err_msg;
                            break;
                        case RECORDER_STORAGE_FULL:
                            errorMsgResId = R.string.dl_recorder_storage_full_err_msg;
                            break;
                        case RECORDER_INIT_FAILED:
                            errorMsgResId = R.string.dl_recorder_initial_err_msg;
                            break;
                    }
                    spSample.setEnabled(true);
                    sbarPlayGain.setEnabled(true);
                    sbarRecordGain.setEnabled(true);
                    btnTestStart.setText(R.string.txt_stop);
                    dismissProgress();
                    DialogHelper.showErrorMessage(MeasureActivity.this, errorMsgResId);
                }
            });
        }
    }
    private void showProgress(String msg) {
        String message = msg != null ? msg : "";
        if (pgDialog == null) {
            pgDialog = new ProgressDialog(MeasureActivity.this);
            pgDialog.setCancelable(false);
            pgDialog.show();
        }
        pgDialog.setMessage(message);
    }

    private void dismissProgress() {
        if (pgDialog != null) {
            pgDialog.dismiss();
            pgDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroy();
    }
}
