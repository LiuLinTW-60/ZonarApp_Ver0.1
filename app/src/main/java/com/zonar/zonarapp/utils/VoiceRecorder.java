package com.zonar.zonarapp.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

//import com.xround_app_sdk.Analysis;
import com.xround_lib.Analysis;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VoiceRecorder {
    private VoiceRecorder instance;

    public static final int MEDIA_RECORDER_INFO_TICKED = 999;
    public static final int MEDIA_RECORDER_INFO_LACK_OF_STORAGE = 1;
    public static final int RECORD_PROCESSING = 2;
    public static final int RECORD_FINISHED = 3;

    private final int SAMPLE_RATE = 48000;
    private final int CHANNELS = 2;
    private final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    private final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSize = 20 * 2 * SAMPLE_RATE;

    short[] leftData = new short[bufferSize/2];
    short[] rightData = new short[bufferSize/2];
    boolean analysis_done = false;
    double[] EQ = {0,0,0,0,0,0,0,0,0,0};
    String showLOG = "1";

    private OnInfoListener infoListener = null;
    private CountDownTimer timer = null;

    private float gain = 1;
    private String filePathL = null;
    private String filePathR = null;
    private String filePath = null;
    private boolean isRecording = false;
    private int duration = -1;

    private Object mutex = new Object();

    private Analysis Analysis = new Analysis();

    public VoiceRecorder() {
        super();
        instance = this;
    }

    public void setMaxDuration(int sec) {
        this.duration = sec;

        if (duration <= 0) {
            timer = null;

            return;
        }

        timer = new CountDownTimer(duration * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (infoListener != null) {
                    infoListener.onInfo(MEDIA_RECORDER_INFO_TICKED);
                }
            }

            public void onFinish() {
                infoListener.onInfo(MEDIA_RECORDER_INFO_TICKED);
                instance.stop();

            }
        };
    }

    public void start() {
        synchronized (mutex) {
            if (this.isRecording) {
                mutex.notify();
                return;
            }

            if (this.filePathL == null || this.filePathR == null || this.filePath == null) {
                Debug.errLog("Audio Recorder: output file name is null!!");
                return;
            }

            this.isRecording = true;
            RecordTask recordTask = new RecordTask();
            recordTask.execute();
        }
    }

    public void stop() {
        if (infoListener != null) {
            infoListener.onInfo(RECORD_PROCESSING);
        }

        try {
            isRecording = false;
        } catch (IllegalStateException e) {
            // do nothing.
        }

        releaseTimer();
    }

    public void setOutputFileL(String path) {
        filePathL = path;
    }

    public void setOutputFileR(String path) {
        filePathR = path;
    }

    public void setOutputFile(String path) {
        filePath = path;
    }

    public void setOnInfoListener(OnInfoListener listener) {
        this.infoListener = listener;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    private void releaseTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    private class RecordTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                recordProcess();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            if (infoListener != null) {
                infoListener.onInfo(MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED);
            }
        }
    }

    private void recordProcess() throws IOException {
        showLOG = "4";
        synchronized (mutex) {
            while (!this.isRecording) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException("Wait() interrupted!", e);
                }
            }
        }

        // raise the priority
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        //int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_ENCODING);
        final AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE,
                        CHANNEL_CONFIG,
                        AUDIO_ENCODING,
                        bufferSize);

        short[] tempBuffer = new short[bufferSize];
        long limitBuf = Util.getAvailableStorage() / 2L;
        int bufferRead = -1;
        long bufferCount = 0;
        int idxBuffer = 0;
        ArrayList<Short> allBuf = new ArrayList<Short>();
        ArrayList<Short> allBufL = new ArrayList<Short>();
        ArrayList<Short> allBufR = new ArrayList<Short>();

        showLOG = "3";
        Log.e(showLOG, "before record");
        recorder.startRecording();
        Log.e(showLOG, "after record");

        if (timer != null) {
            timer.start();
        }

        while (this.isRecording) {
            //bufferRead = recorder.read(tempBuffer, 0, bufferSize, AudioRecord.READ_BLOCKING);
            bufferRead = recorder.read(tempBuffer,0, bufferSize);
            if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
                throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
            } else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
                throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
            } else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
                throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
            }
            for (idxBuffer = 0; idxBuffer < bufferRead; idxBuffer++) {
                allBuf.add(tempBuffer[idxBuffer]);
                if (idxBuffer % 2 == 0) {
                    allBufL.add(tempBuffer[idxBuffer]);
                } else {
                    allBufR.add(tempBuffer[idxBuffer]);
                }
            }

            bufferCount = bufferCount + 1;
            if(bufferCount + 1 > limitBuf) {
                stop();
                if (infoListener != null) {
                    infoListener.onInfo(MEDIA_RECORDER_INFO_LACK_OF_STORAGE);
                }
                break;
            }

        }
        Log.e(showLOG, "finish record");

        recorder.stop();
        recorder.release();
        showLOG = "2";
        Log.e(showLOG, "release record");


        int[] analy_eq = Analysis.analysis(allBufL,allBufR,SAMPLE_RATE);
        //System.arraycopy(analy_eq,0,EQ,0,analy_eq.length);
        //EQ = Analysis.analysis(leftData,rightData,SAMPLE_RATE);
        //EQ = Analysis.analysis(allBufL,allBufR,SAMPLE_RATE);
        showLOG = Arrays.toString(analy_eq);
        analysis_done = true;
        showLOG = showLOG+"overtrue";
        Log.e(showLOG, "analysis done");

        if (infoListener != null) {
            infoListener.onInfo(RECORD_FINISHED);
        }
        Log.e(showLOG, "all done");
        if(analysis_done)
            Log.e(showLOG, "analyis done");
        else
            Log.e(showLOG, "analyis not done");
        //this.saveToWav(allBufL, "L");
        //this.saveToWav(allBufR,"R");
        //this.AllsaveToWav(allBuf);
        //analysis.analysis(leftData,rightData,SAMPLE_RATE);
    }

    public short[] getLeftData() {
        return leftData;
    }

    public short[] getRightData() {
        return rightData;
    }

    public double[] getEQ() {
        double[] eq = {3,3,3,3,3,3,3,3,3,3};
        if (analysis_done) {
            return EQ;
        } else {
            return eq;
        }
    }

    public String getShowLOG() {
        return showLOG;
    }

    private void saveToWav(ArrayList<Short> allBuf, String LorR) throws IOException {
        File waveFileName;
        if (LorR == "L") {
            waveFileName = new File(filePathL);
        } else {
            waveFileName = new File(filePathR);
        }

        DataOutputStream waveOut = null;
        try {
            waveOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(waveFileName)));

        } catch (FileNotFoundException e) {
            Debug.errLog(e.toString());
            return;
        }

        int bufLen = allBuf.size();
        int mNumBytes = bufLen * CHANNELS/2 * 2;

        // write RIFF header
        waveOut.writeBytes("RIFF");
        this.writeInt(waveOut, 36 + mNumBytes);
        waveOut.writeBytes("WAVE");

        // write fmt chunk
        waveOut.writeBytes("fmt ");
        this.writeInt(waveOut, 16);
        this.writeShort(waveOut, (short) 1);
        this.writeShort(waveOut, (short) (CHANNELS/2));
        this.writeInt(waveOut, SAMPLE_RATE);
        this.writeInt(waveOut, CHANNELS/2 * SAMPLE_RATE * 2);
        this.writeShort(waveOut, (short) (CHANNELS/2 * 2));
        this.writeShort(waveOut, (short) 16);

        // write data chunk
        waveOut.writeBytes("data");
        this.writeInt(waveOut, mNumBytes);

        //write sample points
        for (int i = 0; i < bufLen; i++) {
            short sample = allBuf.get(i);
            this.writeShort(waveOut, sample);
        }

        waveOut.flush();
        waveOut.close();

        if (infoListener != null) {
            infoListener.onInfo(RECORD_FINISHED);
        }
    }

    private void AllsaveToWav(ArrayList<Short> allBuf) throws IOException {
        File waveFileName;
        waveFileName = new File(filePath);

        DataOutputStream waveOut = null;
        try {
            waveOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(waveFileName)));

        } catch (FileNotFoundException e) {
            Debug.errLog(e.toString());
            return;
        }

        int bufLen = allBuf.size();
        int mNumBytes = bufLen * CHANNELS * 2;

        // write RIFF header
        waveOut.writeBytes("RIFF");
        this.writeInt(waveOut, 36 + mNumBytes);
        waveOut.writeBytes("WAVE");

        // write fmt chunk
        waveOut.writeBytes("fmt ");
        this.writeInt(waveOut, 16);
        this.writeShort(waveOut, (short) 1);
        this.writeShort(waveOut, (short) (CHANNELS));
        this.writeInt(waveOut, SAMPLE_RATE);
        this.writeInt(waveOut, CHANNELS * SAMPLE_RATE * 2);
        this.writeShort(waveOut, (short) (CHANNELS * 2));
        this.writeShort(waveOut, (short) 16);

        // write data chunk
        waveOut.writeBytes("data");
        this.writeInt(waveOut, mNumBytes/2);

        //write sample points
        for (int i = 0; i < bufLen; i++) {
            short sample = allBuf.get(i);
            this.writeShort(waveOut, sample);
        }

        waveOut.flush();
        waveOut.close();

        if (infoListener != null) {
            infoListener.onInfo(RECORD_FINISHED);
        }
    }

    private void writeInt(DataOutputStream out, int val) throws IOException {
        out.write(val >> 0);
        out.write(val >> 8);
        out.write(val >> 16);
        out.write(val >> 24);
    }

    private void writeShort(DataOutputStream out, short val) throws IOException {
        out.write(val >> 0);
        out.write(val >> 8);
    }

    private void writeFloat(DataOutputStream out, float val) throws IOException {
        out.writeFloat(val);
    }
    public interface OnInfoListener {
        public void onInfo(int what);
    }
}
