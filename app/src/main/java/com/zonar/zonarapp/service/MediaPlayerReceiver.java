package com.zonar.zonarapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.james.utils.LogUtils;

public class MediaPlayerReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaPlayerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String cmd = intent.getStringExtra("command");
        LogUtils.v(TAG, "[MediaPlayerReceiver] " + action + " / " + cmd);

        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        String xiaMiName = intent.getStringExtra("widget_song_name");
        String artistName = intent.getStringExtra("notify_artistname");
        String audioName = intent.getStringExtra("notify_audioname");
        boolean playing = intent.getBooleanExtra("playing", false);
        long duration = intent.getLongExtra("duration", 3000);
        long position = intent.getLongExtra("position", 1000);
        LogUtils.v(TAG, "[MediaPlayerReceiver] artist -> " + artist + ", album -> " + album + ", playing -> " + playing);
    }
}
