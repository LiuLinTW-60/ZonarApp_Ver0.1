package com.zonar.zonarapp.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class MediaManager {
    public enum State {
        IDLE,
        PLAYER_INITED,
        RECORDER_INITED,
        INITED,
        TESTING,
        STOP_TEST,
        RECORD_PROCESSING,
        RECORD_FINISHED
    }

    public enum Error {
        PLAYER_INIT_FAILED,
        RECORDER_NO_STORAGE,
        RECORDER_STORAGE_FULL,
        RECORDER_INIT_FAILED
    }

    private State state = State.IDLE;
    private OnMediaListener onMediaListener;

    private RecorderListener recorderListener;
    private PlayerListener playerListener;
    private VoiceRecorder voiceRecorder = null;
    private MediaPlayer voicePlayer = null;
    private VolumeInfo volumeInfo = null;

    String lastRecordFileName = "";
    float recordGain;
    short[] left = null;
    short[] right = null;
    double [] eq = null;
    String showlog = "0";

    public MediaManager(Context context) {
        recorderListener = new RecorderListener();
        playerListener = new PlayerListener();
        volumeInfo = new VolumeInfo();
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int minVolume = am.getStreamMinVolume(AudioManager.STREAM_MUSIC);
            int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeInfo.volume = volume;
            volumeInfo.maxVolume = maxVolume;
            volumeInfo.minVolume = minVolume;
        }
    }

    public void initTest(String fileName) {
        Debug.infoLog("## prepare sample file:" + fileName);
        initPlayer(fileName);
    }

    private void initPlayer(String fileName) {
        try {
            if (voicePlayer == null || !voicePlayer.isPlaying()) {
                voicePlayer = new MediaPlayer();
                voicePlayer.setDataSource(FileManager.getInstance().getSampleFile(fileName));
                voicePlayer.setOnCompletionListener(playerListener);
                voicePlayer.setOnPreparedListener(playerListener);
                voicePlayer.setOnErrorListener(playerListener);
                voicePlayer.prepare();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecorder() {
        if (voiceRecorder == null || !voiceRecorder.isRecording()) {
            if (!Util.hasWriteableExternalStorage()) {
                handleError(Error.RECORDER_NO_STORAGE);
                return;
            }

            if (Util.getAvailableStorage() == 0) {
                handleError(Error.RECORDER_STORAGE_FULL);
                return;
            }

            try {
                // set Recorder & start recording
                voiceRecorder = new VoiceRecorder();
                voiceRecorder.setOnInfoListener(recorderListener);
                voiceRecorder.setGain(recordGain);
                setState(State.RECORDER_INITED);

            } catch (IllegalStateException e) {
                e.printStackTrace();
                releaseVoiceRecorder();
                handleError(Error.RECORDER_INIT_FAILED);
            }
        }
    }

    public void triggerTestPlayButton() {
        switch (state) {
            case TESTING:
                setState(State.STOP_TEST);
                break;

            case INITED:
                setState(State.TESTING);
                break;
        }
    }

    public void stop() {
        switch (state) {
            case TESTING:
                setState(State.STOP_TEST);
                break;
        }
    }

    private synchronized void releaseVoicePlayer() {
        if (voicePlayer != null) {
            voicePlayer.stop();
            voicePlayer.reset();
            voicePlayer.release();
            voicePlayer = null;
        }
    }

    private void releaseVoiceRecorder() {
        if (voiceRecorder != null){
            voiceRecorder.stop();
            voiceRecorder = null;
        }
    }

    private void handleError(Error error) {
        if (onMediaListener != null) {
            onMediaListener.onError(error);
        }
    }

    private synchronized void setState(State newState) {
        state = newState;

        Debug.infoLog(">> setState:" + state.name());

        if (onMediaListener != null) {
            onMediaListener.onStateChanged(state);
        }

        switch (state) {
            case PLAYER_INITED:
                initRecorder();
                break;

            case RECORDER_INITED:
                setState(State.INITED);
                break;

            case INITED:
                break;

            case STOP_TEST:
                //left = voiceRecorder.getLeftData();
                //right = voiceRecorder.getRightData();
                eq = voiceRecorder.getEQ();
                showlog = voiceRecorder.getShowLOG();
                releaseVoiceRecorder();
                releaseVoicePlayer();
                break;

            case TESTING:
                String L = "L", R = "R";
                String newFilePathL = FileManager.getInstance().createRecordFile(L);
                File newFileL = new File(newFilePathL);
                lastRecordFileName = newFileL.getName();
                voiceRecorder.setOutputFileL(newFilePathL);
                String newFilePathR = FileManager.getInstance().createRecordFile(R);
                File newFileR = new File(newFilePathR);
                lastRecordFileName = newFileR.getName();
                voiceRecorder.setOutputFileR(newFilePathR);
                String newFilePath = FileManager.getInstance().createRecordFile("All");
                File newFile = new File(newFilePath);
                lastRecordFileName = newFile.getName();
                voiceRecorder.setOutputFile(newFilePath);
                voicePlayer.start();
                voiceRecorder.start();
                break;
        }
    }

    public short[] getLeftData() {
        return left;
    }

    public short[] getRightData() {
        return right;
    }

    public double[] getEQ() {
        return eq;
    }

    public String getShowlog(){
        return showlog;}
    public State getState() {
        return state;
    }

    public String getLastRecordFileName() {
        return this.lastRecordFileName;
    }

    public void setOnMediaListener(OnMediaListener onMediaListener) {
        this.onMediaListener = onMediaListener;
    }

    public VolumeInfo getSystemVolume() {
        return volumeInfo;
    }

    public void setPlayGain(Context context, int playGain) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        if (am != null) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, playGain, AudioManager.FLAG_PLAY_SOUND);
        }

        if (voicePlayer != null) {
            voicePlayer.setVolume(playGain, playGain);
        }
    }

    public void setRecordGain(int gain) {
        recordGain = (float)gain / (float)volumeInfo.maxVolume;
    }

    private class RecorderListener implements VoiceRecorder.OnInfoListener {
        @Override
        public void onInfo(int what) {
            switch(what) {
                case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
                    // Do nothing.
                    break;

                case VoiceRecorder.MEDIA_RECORDER_INFO_LACK_OF_STORAGE:
                    setState(State.STOP_TEST);
                    break;

                case VoiceRecorder.MEDIA_RECORDER_INFO_TICKED:
                    // Do nothing.
                    break;

                case VoiceRecorder.RECORD_PROCESSING:
                    setState(State.RECORD_PROCESSING);
                    break;

                case VoiceRecorder.RECORD_FINISHED:
                    setState(State.RECORD_FINISHED);
                    break;
            }
        }
    }

    private class PlayerListener implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            setState(State.STOP_TEST);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            setState(State.PLAYER_INITED);
        }

        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            releaseVoicePlayer();
            handleError(Error.PLAYER_INIT_FAILED);
            return false;
        }
    }

    public interface OnMediaListener {
        void onStateChanged(State state);
        void onError(Error error);
    }

    public class VolumeInfo {
        public int volume;
        public int maxVolume;
        int minVolume;
    }
}
