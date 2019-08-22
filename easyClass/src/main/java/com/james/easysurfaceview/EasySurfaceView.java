/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.easysurfaceview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class EasySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "EasySurfaceView";
    private DrawThread mDrawThread;
    protected int boundaryWidth, boundaryHeight;
    private int sleepSpan = 10;
    private long mTimeTick = 0;

    public EasySurfaceView(Context context) {
        super(context);
        mDrawThread = new DrawThread(getHolder());
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public int resize(int size) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return size * getResources().getDisplayMetrics().widthPixels / 640;
        } else {
            return size * getResources().getDisplayMetrics().heightPixels / 960;
        }
    }

    public float resize(float size) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return size * getResources().getDisplayMetrics().widthPixels / 640f;
        } else {
            return size * getResources().getDisplayMetrics().heightPixels / 960f;
        }
    }

    public Rect resize(Rect rect) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            rect.left = rect.left * getResources().getDisplayMetrics().widthPixels / 640;
            rect.top = rect.top * getResources().getDisplayMetrics().widthPixels / 640;
            rect.right = rect.right * getResources().getDisplayMetrics().widthPixels / 640;
            rect.bottom = rect.bottom * getResources().getDisplayMetrics().widthPixels / 640;

            return rect;
        } else {
            rect.left = rect.left * getResources().getDisplayMetrics().heightPixels / 960;
            rect.top = rect.top * getResources().getDisplayMetrics().heightPixels / 960;
            rect.right = rect.right * getResources().getDisplayMetrics().heightPixels / 960;
            rect.bottom = rect.bottom * getResources().getDisplayMetrics().heightPixels / 960;

            return rect;
        }
    }

    public RectF resize(RectF rect) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            rect.left = rect.left * getResources().getDisplayMetrics().widthPixels / 640f;
            rect.top = rect.top * getResources().getDisplayMetrics().widthPixels / 640f;
            rect.right = rect.right * getResources().getDisplayMetrics().widthPixels / 640f;
            rect.bottom = rect.bottom * getResources().getDisplayMetrics().widthPixels / 640f;

            return rect;
        } else {
            rect.left = rect.left * getResources().getDisplayMetrics().heightPixels / 960f;
            rect.top = rect.top * getResources().getDisplayMetrics().heightPixels / 960f;
            rect.right = rect.right * getResources().getDisplayMetrics().heightPixels / 960f;
            rect.bottom = rect.bottom * getResources().getDisplayMetrics().heightPixels / 960f;

            return rect;
        }
    }

    public int getMonitorCenterX() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return getResources().getDisplayMetrics().widthPixels / 2;
        } else {
            return getResources().getDisplayMetrics().heightPixels / 2;
        }
    }

    public int getMonitorCenterY() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return getResources().getDisplayMetrics().heightPixels / 2;
        } else {
            return getResources().getDisplayMetrics().widthPixels / 2;
        }
    }

    public int getCenterX() {
        return getWidth() / 2;
    }

    public int getCenterY() {
        return getHeight() / 2;
    }

    /**
     * 在這邊實作繪圖的內容
     *
     * @param canvas
     */
    public abstract void easyDraw(Canvas canvas);

    /**
     * @deprecated
     */
    public void setBoundary() {

    }

    public abstract void onViewCreated();

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            easyDraw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        boundaryWidth = w;
        boundaryHeight = h;

        setBoundary();

        onViewCreated();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated");
        if (mDrawThread == null) {
            mDrawThread = new DrawThread(getHolder());
        }
        mDrawThread.setFlag(true);
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.v(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestroyed");
        boolean retry = true;
        mDrawThread.setFlag(false);
        while (retry) {
            try {
                mDrawThread.join();
                retry = false;
            } catch (Exception e) {
            }
        }
        mDrawThread = null;
    }

    /**
     * set sleep span
     *
     * @param sleepSpan (int) 毫秒
     */
    public void setSleepSpan(int sleepSpan) {
        this.sleepSpan = sleepSpan;
    }

    /**
     * get cycle time
     *
     * @return
     */
    public long getTime() {
        return mTimeTick;
    }

    private class DrawThread extends Thread {
        private SurfaceHolder surfaceHolder = null;
        private boolean flag = true;

        private DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            Canvas canvas;
            while (flag) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        onDraw(canvas);
                        mTimeTick++;
                    }
                } finally {
                    try {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(sleepSpan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
